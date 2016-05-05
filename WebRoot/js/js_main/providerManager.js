$(document).ready(function() {
	$("#productButton").click(function(){
		loadTable();
	});
	
	initTable();
	
});

function initTable(){
	var toolbar = [];
	if(xyzControlButton('buttonCode_w20151215174401')){
		toolbar[toolbar.length]={
				text: '新增供应商',
				border:'1px solid #bbb',
				iconCls: 'icon-add',
				handler: function(){var title=$(this).text();addProviderButton(title);}
		};
		toolbar[toolbar.length]='-';
	}
	if(xyzControlButton('buttonCode_w20151215174402')){
		toolbar[toolbar.length]={
				text: '编辑供应商',
				border:'1px solid #bbb',
				iconCls: 'icon-edit',
				handler: function(){var title=$(this).text();editProviderButton(title);}
		};
		toolbar[toolbar.length]='-';
	}
	if(xyzControlButton('buttonCode_w20151215174403')){
		toolbar[toolbar.length]={
				text: '删除供应商',
				border:'1px solid #bbb',
				iconCls: 'icon-remove',
				handler: function(){deleteProviderButton();}
		};
		toolbar[toolbar.length]='-';
	}
	
	xyzgrid({
		table : 'providerManagerTable',
		title : '产品列表',
		url:'../ProviderWS/queryProviderList.do',
		pageList : [5,10,15,30,50],
		pageSize : 15,
		toolbar:toolbar,
		singleSelect : false,
		idField : 'numberCode',
		height:'auto',
		columns : [[
		    {field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'供应商编号',hidden:true},
			{field:'nameCn',title:'供应商名称',width:100},
			{field:'operTemp1',title:'供应商类型',width:100,
					formatter: function(value,row){
						var result="";
						if(row.isScenic==1){
							result+="景点-";
						}
						if(row.isHotel==1){
							result+="酒店-";
						}
						if(row.isCar==1){
							result+="租车-";
						}
						if(row.isSpecialty==1){
							result+="土特产-";
						}
						if(result.length>0){
							result=result.substring(0,result.length-1);
						}
						return result;
					}
			},
			{field:'phone',title:'电话',width:100},
			{field:'qq',title:'QQ帐号',width:100},
			{field:'email',title:'邮箱',width:100},
			{field:'linkman',title:'联系人',width:100},
			{field:'address',title:'地址',width:100},
			{field:'imageUrl',title:'设置资源图片',
				formatter: function(value,row){
					if(value==""){
						return "<a href='javascript:void(0);' onclick='editImageUrl(\""+row.numberCode+"\",\""+row.imageUrl+"\")'>设置</a>";
					}else{
						return "<a href='javascript:void(0);' onclick='editImageUrl(\""+row.numberCode+"\",\""+row.imageUrl+"\")'>重置</a>";
					}
				}	
			},
			{field:'updatePrice',title:'更新售价',
				formatter: function(value,row){
					return "<a href='javascript:void(0);' onclick='updateBasePrice(\""+row.numberCode+"\")'>更新默认售价</a>";
				}	
			},
			{field:'onlineFlag',title:'上下线状态',
				formatter:function(value,row,index){
			    	   if(value == 1){
						  return "√";
					   }else {
						  return "×";
					   }
			        },
			        styler:function(value,row,index){
			        	if(value == 1){
							return "background-color:green";
						}else {
							return "background-color:red";
						}
			        }
			},
			{field:'operTemp4',title:'操作',
				formatter: function(value,row){
					if(xyzControlButton("buttonCode_w20160405182601")){
						if(row.onlineFlag=="0"){
							return "<a href='javascript:void(0);' onclick='editOnlineFlag(\""+row.numberCode+"\",\"1\")'>上线</a>";
						}else{
							return "<a href='javascript:void(0);' onclick='editOnlineFlag(\""+row.numberCode+"\",\"0\")'>下线</a>";
						}
					}
				}	
			}
		]]
	});
}

function loadTable(){
	var nameCn = $("#nameCn").val();
	var type = $("#type").combobox("getValue");
	$("#providerManagerTable").datagrid("load",{
		nameCn:nameCn,
		type:type
	});
}

function updateBasePrice(provider){
	if(!confirm("您的操作将更新当前产品价格为默认销售价，继续执行？")){
		return;
	}
	xyzAjax({
		url : '../ProviderWS/updateBasePrice.do',
		data : {
			provider : provider
		},
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","操作成功","info");
				$("#providerManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});
}

function addProviderButton(title){
	
	xyzdialog({
		dialog : 'dialogFormDiv_addProvider',
		title : title,
	    href : '../jsp_main/addProvider.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				addProviderSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addProvider").dialog("destroy");
			}
		}]
	});
	
}

function editProviderButton(title){
	
	var provider = $("#providerManagerTable").datagrid("getChecked");
	if(provider.length != 1){
		top.$.messager.alert("提示","请先选中单个对象！","info");
		return;
	}
	var row = provider[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editProvider',
		title : title,
	    href : '../jsp_main/editProvider.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				editProviderSubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editProvider").dialog("destroy");
			}
		}],
		onLoad:function(){
			$("#isScenicForm").attr('checked',row.isScenic==1);
			$("#isHotelForm").attr('checked',row.isHotel==1);
			$("#isCarForm").attr('checked',row.isCar==1);
			$("#isSpecialtyForm").attr('checked',row.isSpecialty==1);
			$("#nameCnForm").val(row.nameCn);
			$("#levelForm").val(row.level);
			$("#levelSystemForm").val(row.levelSystem);
			$("#phoneForm").val(row.phone);
			$("#qqForm").val(row.qq);
			$("#emailForm").val(row.email);
			$("#linkmanForm").val(row.linkman);
			$("#addressForm").val(row.address);
			$("#remarkForm").val(row.remark);
			$("#mapLaLoForm").val(row.longitudeLatitude);
			
		}
	});
}

function addProviderSubmit(target){
	
	var nameCn=$("#nameCnForm").val();
	var isScenic=$("#isScenicForm").is(':checked')?"1":"0";
	var isHotel=$("#isHotelForm").is(':checked')?"1":"0";
	var isCar=$("#isCarForm").is(':checked')?"1":"0";
	var isSpecialty=$("#isSpecialtyForm").is(':checked')?"1":"0";
	var level=$("#levelForm").val();
	var levelSystem=$("#levelSystemForm").val();
	var phone=$("#phoneForm").val();
	var qq=$("#qqForm").val();
	var email=$("#emailForm").val();
	var linkman=$("#linkmanForm").val();
	var address=$("#addressForm").val();
	var remark=$("#remarkForm").val();
	var mapLaLo=$("#mapLaLoForm").val();
	
	if(!$("form").form('validate')){
		return;
	}
	
	xyzAjax({
		url:"../ProviderWS/addProvider.do",
		data:{
			nameCn:nameCn,
			isScenic:isScenic,
			isHotel:isHotel,
			isCar:isCar,
			isSpecialty:isSpecialty,
			level:level,
			levelSystem:levelSystem,
			phone:phone,
			qq:qq,
			email:email,
			linkman:linkman,
			address:address,
			remark:remark,
			longitudeLatitude:mapLaLo
		},
		success:function(data){
			if(data.status==1){
				top.$.messager.alert("提示","操作成功","info");
				$("#dialogFormDiv_addProvider").dialog("destroy");
				$("#providerManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});
}

function editProviderSubmit(numberCode){
	var nameCn=$("#nameCnForm").val();
	var isScenic=$("#isScenicForm").is(':checked')?"1":"0";
	var isHotel=$("#isHotelForm").is(':checked')?"1":"0";
	var isCar=$("#isCarForm").is(':checked')?"1":"0";
	var isSpecialty=$("#isSpecialtyForm").is(':checked')?"1":"0";
	var level=$("#levelForm").val();
	var levelSystem=$("#levelSystemForm").val();
	var phone=$("#phoneForm").val();
	var qq=$("#qqForm").val();
	var email=$("#emailForm").val();
	var linkman=$("#linkmanForm").val();
	var address=$("#addressForm").val();
	var remark=$("#remarkForm").val();
	var mapLaLo=$("#mapLaLoForm").val();
	if(!$("form").form('validate')){
		return;
	}
	
	xyzAjax({
		url:"../ProviderWS/editProvider.do",
		data:{
			numberCode:numberCode,
			nameCn:nameCn,
			isScenic:isScenic,
			isHotel:isHotel,
			isCar:isCar,
			isSpecialty:isSpecialty,
			level:level,
			levelSystem:levelSystem,
			phone:phone,
			qq:qq,
			email:email,
			linkman:linkman,
			address:address,
			remark:remark,
			longitudeLatitude:mapLaLo
			
		},
		success:function(data){
			if(data.status==1){
				top.$.messager.alert("提示","操作成功","info");
				$("#dialogFormDiv_editProvider").dialog("destroy");
				$("#providerManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});
	
}

function deleteProviderButton(){
	var products = $.map($("#providerManagerTable").datagrid("getChecked"),function(p){return p.numberCode;}).join(",");
	if(products == null || products == ''){
		top.$.messager.alert("提示","请先选中需要删除的对象！","info");
		return;
	}
	if(!confirm("彻底删除对象，确定？")){
		return;
	}
	
	xyzAjax({
		url : '../ProviderWS/deleteProvider.do',
		data : {
			numberCodes : products
		},
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","操作成功","info");
				$("#providerManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});
}


function editImageUrl(numberCode,imageUrl){
	
	/*xyzdialog({
		dialog : 'dialogFormDiv_editProviderImageUrl',
		title : '编辑图片',
	    href : 'editProviderImageUrl.html?numberCode='+numberCode+'&url='+imageUrl,
	    fit:false,
	    height:750,
	    width:1150,
	    buttons:[{
			text:'关闭',
			handler:function(){
				$("#dialogFormDiv_editProviderImageUrl").dialog("destroy");
			}
		}]
	});*/
	
	
	layer.open({
		title:'上传图片',
	    type: 2,
	    area: ['1100px', '700px'],
	    fix: true, //不固定
	    maxmin: false,
	    content: 'editProviderImageUrl.html?numberCode='+numberCode+'&imageUrl='+imageUrl
	});
	
}

function editOnlineFlag(numberCode,value){
	xyzAjax({
		url : '../ProviderWS/editOnlineFlag.do',
		data : {
			numberCode : numberCode,
			value : value
		},
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","操作成功","info");
				$("#providerManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});
}

function setMapLaLo(){
	layer.open({
		title:'设置经纬度',
	    type: 2,
	    area: ['1100px', '700px'],
	    fix: true, //不固定
	    maxmin: false,
	    content: 'setMapLaLo.html'
	});
}