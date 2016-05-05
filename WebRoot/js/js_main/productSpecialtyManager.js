$(document).ready(function() {
	
	$("#productButton").click(function(){
		loadTable();
	});
	
	xyzCombobox({
		combobox : 'providerLoadForm',
		url : '../ListWS/getProviderList.do?type=SP',
	});
	
	initTable();
	
});

function initTable(){
	
	var toolbar = [];
	if(xyzControlButton('buttonCode_h20151214181001')){
		toolbar[toolbar.length]={
				text: '新增产品',
				border:'1px solid #bbb',
				iconCls: 'icon-add',
				handler: function(){var title=$(this).text();addProductSpecialtyButton(title);}
		};
		toolbar[toolbar.length]='-';
	}
	if(xyzControlButton('buttonCode_h20151214181002')){
		toolbar[toolbar.length]={
				text: '编辑产品',
				border:'1px solid #bbb',
				iconCls: 'icon-edit',
				handler: function(){var title=$(this).text();editProductSpecialtyButton(title);}
		};
		toolbar[toolbar.length]='-';
	}
	if(xyzControlButton('buttonCode_h20151214181003')){
		toolbar[toolbar.length]={
				text: '删除产品',
				border:'1px solid #bbb',
				iconCls: 'icon-remove',
				handler: function(){deleteProductSpecialtyButton();}
		};
		toolbar[toolbar.length]='-';
	}	
	
	xyzgrid({
		table : 'productSpecialtyManagerTable',
		title : '产品列表',
		url:'../ProductSpecialtyWS/queryProductSpecialtyList.do',
		pageList : [5,10,15,30,50],
		pageSize : 15,
		toolbar:toolbar,
		singleSelect : false,
		idField : 'numberCode',
		height:'auto',
		columns : [[
		    {field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'产品编号',hidden:true},
			{field:'nameCn',title:'产品名称',width:300},
			{field:'providerNameCn',title:'供应商名称',width:100},
			{field:'price',title:'当天价格',width:100},
			{field:'imageUrl',title:'设置资源图片',
				formatter: function(value,row){
					if(value==""){
						return "<a href='javascript:void(0);' onclick='editImageUrl(\""+row.numberCode+"\",\""+row.imageUrl+"\")'>设置</a>";
					}else{
						return "<a href='javascript:void(0);' onclick='editImageUrl(\""+row.numberCode+"\",\""+row.imageUrl+"\")'>重置</a>";
					}
				}	
			},
			{field:'isTag',title:'特价启用',
				formatter: function(value,row){
					if(value=="1"){
						return "<a href='javascript:void(0);' onclick='editTagEnable(\""+row.numberCode+"\",\"0\")'>关闭</a>";
					}else{
						return "<a href='javascript:void(0);' onclick='editTagEnable(\""+row.numberCode+"\",\"1\")'>开启</a>";
					}
				}	
			},
			{field:'operTemp2',title:'特价设置',
				formatter: function(value,row){
					return "<a href='javascript:void(0);' onclick='editTag(\""+row.numberCode+"\")'>设置</a>";
				}	
			},
			{field:'operTemp3',title:'库存',
				formatter: function(value,row,index){
					var btn1 = "<a href='javascript:void(0);' onclick='managerProductStock(\""+row.numberCode+"\",\""+row.nameCn+"\")'>库存管理</a>";
					return btn1;
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
					if(xyzControlButton("buttonCode_w20160405182701")){
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
	var provider= $("#providerLoadForm").combobox("getValue");
	$("#productSpecialtyManagerTable").datagrid("load",{
		nameCn : nameCn,
		provider:provider
	});
}

function addProductSpecialtyButton(title){

	xyzdialog({
		dialog : 'dialogFormDiv_addProductSpecialty',
		title : title,
	    href : '../jsp_main/addProductSpecialty.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				addProductSpecialtySubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addProductSpecialty").dialog("destroy");
			}
		}],onLoad:function(){
			xyzCombobox({
				combobox : 'providerForm',
				url : '../ListWS/getProviderList.do?type=SP',
				required:true,
			});
		}
	});
	
}

function editProductSpecialtyButton(title){
	
	var product = $("#productSpecialtyManagerTable").datagrid("getChecked");
	if(product.length != 1){
		top.$.messager.alert("提示","请先选中单个对象！","info");
		return;
	}
	var row = product[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editProductSpecialty',
		title : title,
	    href : '../jsp_main/editProductSpecialty.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				editProductSpecialtySubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editProductSpecialty").dialog("destroy");
			}
		}],
		onLoad:function(){
			xyzCombobox({
				combobox : 'providerForm',
				url : '../ListWS/getProviderList.do?type=SP',
				required:true,
				lazy:false,
			});
			$("#nameCnForm").val(row.nameCn);
			$("#providerForm").combobox("setValue",row.provider);
			$("#remarkForm").val(row.remark);
			$("#refundTimeForm").numberbox("setValue",row.refundTime);
			$("#maxDateForm").numberbox("setValue",row.maxDate);
		}
	});
}

function addProductSpecialtySubmit(){
	var nameCn=$("#nameCnForm").val();
	var provider=$("#providerForm").combobox("getValue");
	var remark=$("#remarkForm").val();
	var refundTime=$("#refundTimeForm").numberbox("getValue");
	var maxDate=$("#maxDateForm").numberbox("getValue");
	if(xyzIsNull(provider)){
		alert("请选择关联的供应商");
		return;
	}
	if(xyzIsNull(refundTime)){
		refundTime=0;
	}
	if(xyzIsNull(maxDate)){
		maxDate=20;
	}
	if(!$("form").form('validate')){
		return;
	}
	xyzAjax({
		url:"../ProductSpecialtyWS/addProductSpecialty.do",
		data:{
			nameCn:nameCn,
			provider:provider,
			remark:remark,
			refundTime:refundTime,
			maxDate:maxDate
		},
		success:function(data){
			if(data.status==1){
				top.$.messager.alert("提示","操作成功","info");
				$("#dialogFormDiv_addProductSpecialty").dialog("destroy");
				$("#productSpecialtyManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});
}

function editProductSpecialtySubmit(numberCode){
	
	if(!$("form").form('validate')){
		return;
	}
	var nameCn=$("#nameCnForm").val();
	var provider=$("#providerForm").combobox("getValue");
	var remark=$("#remarkForm").val();
	var refundTime=$("#refundTimeForm").numberbox("getValue");
	var maxDate=$("#maxDateForm").numberbox("getValue");
	if(xyzIsNull(provider)){
		alert("请选择关联的供应商");
		return;
	}
	if(xyzIsNull(refundTime)){
		refundTime=0;
	}
	if(xyzIsNull(maxDate)){
		maxDate=20;
	}
	xyzAjax({
		url:"../ProductSpecialtyWS/editProductSpecialty.do",
		data:{
			numberCode:numberCode,
			nameCn:nameCn,
			provider:provider,
			remark:remark,
			refundTime:refundTime,
			maxDate:maxDate
		},
		success:function(data){
			if(data.status==1){
				top.$.messager.alert("提示","操作成功","info");
				$("#dialogFormDiv_editProductSpecialty").dialog("destroy");
				$("#productSpecialtyManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});
	
}


function editImageUrl(numberCode,imageUrl){
		
		layer.open({
			title:'上传图片',
		    type: 2,
		    area: ['1100px', '700px'],
		    fix: true, //不固定
		    maxmin: false,
		    content: 'editProductSpecialtyImageUrl.html?numberCode='+numberCode+'&imageUrl='+imageUrl
		});
		
	}

function deleteProductSpecialtyButton(){
	var products = $.map($("#productSpecialtyManagerTable").datagrid("getChecked"),function(p){return p.numberCode;}).join(",");
	if(products == null || products == ''){
		top.$.messager.alert("提示","请先选中需要删除的对象！","info");
		return;
	}
	if(!confirm("彻底删除对象，确定？")){
		return;
	}
	
	xyzAjax({
		url : '../ProductSpecialtyWS/deleteProductSpecialty.do',
		data : {
			numberCodes : products
		},
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","操作成功","info");
				$("#productSpecialtyManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});
}

function managerProductStock(numberCode,nameCn){
	xyzdialog({
		dialog : 'dialogFormDiv_managerProductStock',
		title : "配置单品["+nameCn+"]",
	    content : "<iframe id='dialogFormDiv_managerProductStockIframe' frameborder='0' name='"+numberCode+"'></iframe>",
	    buttons:[{
			text:'返回',
			handler:function(){
				$("#dialogFormDiv_managerProductStock").dialog("destroy");
			}
		}]
	});
	var tempWidth = $("#dialogFormDiv_managerProductStock").css("width");
	var tempHeight = $("#dialogFormDiv_managerProductStock").css("height");
	var tempWidth2 = parseInt(tempWidth.split("px")[0]);
	var tempHeight2 = parseInt(tempHeight.split("px")[0]);
	$("#dialogFormDiv_managerProductStockIframe").css("width",(tempWidth2-20)+"px");
	$("#dialogFormDiv_managerProductStockIframe").css("height",(tempHeight2-50)+"px");
	$("#dialogFormDiv_managerProductStockIframe").attr("src","../jsp_main/productStock.html");
}

function editTag(numberCode){
	xyzdialog({
		dialog : 'dialogFormDiv_managerProductUserTag',
		title : "配置特殊价格",
	    content : "<iframe id='dialogFormDiv_managerProductUserTagIframe' frameborder='0' name='"+numberCode+"'></iframe>",
	    buttons:[{
			text:'返回',
			handler:function(){
				$("#dialogFormDiv_managerProductUserTag").dialog("destroy");
			}
		}]
	});
	var tempWidth = $("#dialogFormDiv_managerProductUserTag").css("width");
	var tempHeight = $("#dialogFormDiv_managerProductUserTag").css("height");
	var tempWidth2 = parseInt(tempWidth.split("px")[0]);
	var tempHeight2 = parseInt(tempHeight.split("px")[0]);
	$("#dialogFormDiv_managerProductUserTagIframe").css("width",(tempWidth2-20)+"px");
	$("#dialogFormDiv_managerProductUserTagIframe").css("height",(tempHeight2-50)+"px");
	$("#dialogFormDiv_managerProductUserTagIframe").attr("src","../jsp_main/productUserTagManager.html");
}

function editTagEnable(numberCode,value){
	xyzAjax({
		url : '../ProductSpecialtyWS/editUserTag.do',
		data : {
			numberCode : numberCode,
			value : value
		},
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","操作成功","info");
				$("#productSpecialtyManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});
}

function editOnlineFlag(numberCode,value){
	xyzAjax({
		url : '../ProductSpecialtyWS/editOnlineFlag.do',
		data : {
			numberCode : numberCode,
			value : value
		},
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","操作成功","info");
				$("#productSpecialtyManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});
}