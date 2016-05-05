$(document).ready(function() {
	$("#productButton").click(function(){
		loadTable();
	});
	
	initTable();
	
});

function initTable(){
	var toolbar = [];
	if(xyzControlButton('buttonCode_h20160504113101')){
		toolbar[toolbar.length]={
				text: '新增服务点',
				border:'1px solid #bbb',
				iconCls: 'icon-add',
				handler: function(){var title=$(this).text();addStationButton(title);}
		};
		toolbar[toolbar.length]='-';
	}
	if(xyzControlButton('buttonCode_h20160504113102')){
		toolbar[toolbar.length]={
				text: '编辑服务点',
				border:'1px solid #bbb',
				iconCls: 'icon-edit',
				handler: function(){var title=$(this).text();editStationButton(title);}
		};
		toolbar[toolbar.length]='-';
	}
	if(xyzControlButton('buttonCode_h20160504113103')){
		toolbar[toolbar.length]={
				text: '删除服务点',
				border:'1px solid #bbb',
				iconCls: 'icon-remove',
				handler: function(){deleteStationButton();}
		};
		toolbar[toolbar.length]='-';
	}
	
	xyzgrid({
		table : 'stationManagerTable',
		title : '服务站列表',
		url:'../StationWS/queryStationList.do',
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
			{field:'phone',title:'电话',width:100},
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
	$("#stationManagerTable").datagrid("load",{
		nameCn:nameCn,
		type:type
	});
}


function addStationButton(title){
	
	xyzdialog({
		dialog : 'dialogFormDiv_addStation',
		title : title,
	    href : '../jsp_main/addStation.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				addStationSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addStation").dialog("destroy");
			}
		}]
	});
	
}

function editStationButton(title){
	
	var station = $("#stationManagerTable").datagrid("getChecked");
	if(station.length != 1){
		top.$.messager.alert("提示","请先选中单个对象！","info");
		return;
	}
	var row = station[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editStation',
		title : title,
	    href : '../jsp_main/editStation.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				editStationSubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editStation").dialog("destroy");
			}
		}],
		onLoad:function(){
			$("#nameCnForm").val(row.nameCn);
			$("#phoneForm").val(row.phone);
			$("#linkmanForm").val(row.linkman);
			$("#addressForm").val(row.address);
			$("#remarkForm").val(row.remark);
			$("#mapLaLoForm").val(row.lat+","+row.lng);
			
		}
	});
}

function addStationSubmit(target){
	
	var nameCn=$("#nameCnForm").val();
	var phone=$("#phoneForm").val();
	var linkman=$("#linkmanForm").val();
	var address=$("#addressForm").val();
	var remark=$("#remarkForm").val();
	var mapLaLo=$("#mapLaLoForm").val();
	var lat=mapLaLo.split(",")[0];
	var lng=mapLaLo.split(",")[1];

	
	if(!$("form").form('validate')){
		return;
	}
	
	xyzAjax({
		url:"../StationWS/addStation.do",
		data:{
			nameCn:nameCn,
			phone:phone,
			linkman:linkman,
			address:address,
			remark:remark,
			lat:lat,
			lng:lng
		},
		success:function(data){
			if(data.status==1){
				top.$.messager.alert("提示","操作成功","info");
				$("#dialogFormDiv_addStation").dialog("destroy");
				$("#StationManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});
}

function editStationSubmit(numberCode){
	var nameCn=$("#nameCnForm").val();
	var phone=$("#phoneForm").val();
	var linkman=$("#linkmanForm").val();
	var address=$("#addressForm").val();
	var remark=$("#remarkForm").val();
	var mapLaLo=$("#mapLaLoForm").val();
	var lat=mapLaLo.split(",")[0];
	var lng=mapLaLo.split(",")[1];
	
	if(!$("form").form('validate')){
		return;
	}
	
	xyzAjax({
		url:"../StationWS/editStation.do",
		data:{
			numberCode:numberCode,
			nameCn:nameCn,
			phone:phone,
			linkman:linkman,
			address:address,
			remark:remark,
			lat:lat,
			lng:lng
		},
		success:function(data){
			if(data.status==1){
				top.$.messager.alert("提示","操作成功","info");
				$("#dialogFormDiv_editStation").dialog("destroy");
				$("#stationManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});
	
}

function deleteStationButton(){
	var stations = $.map($("#stationManagerTable").datagrid("getChecked"),function(p){return p.numberCode;}).join(",");
	if(stations == null || stations == ''){
		top.$.messager.alert("提示","请先选中需要删除的对象！","info");
		return;
	}
	if(!confirm("彻底删除对象，确定？")){
		return;
	}
	

	
	/*xyzAjax({
		url : '../StationWS/queryStationListByLocation.do',
		data : {
			lat : "29.697597",
			lng:"92.197266"
		},
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","操作成功","info");
				$("#stationManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});*/
	
	xyzAjax({
		url : '../StationWS/deleteStation.do',
		data : {
			numberCodes : stations
		},
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","操作成功","info");
				$("#StationManagerTable").datagrid("reload");
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
	    content: 'editStationImageUrl.html?numberCode='+numberCode+'&imageUrl='+imageUrl
	});
	
}

function editOnlineFlag(numberCode,value){
	xyzAjax({
		url : '../StationWS/editOnlineFlag.do',
		data : {
			numberCode : numberCode,
			value : value
		},
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","操作成功","info");
				$("#stationManagerTable").datagrid("reload");
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