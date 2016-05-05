$(document).ready(function() {
	
	customer = $("#dialogFormDiv_setCustomerUserTagIframe",window.parent.document).attr("name");
	
	initTable();
});


function initTable(){
	
	var toolbarTrue = [];
	
	toolbarTrue[toolbarTrue.length] = {
		iconCls: 'icon-search',
		text : '查询已选中标签',
		handler: function(){userTagQueryTrue();}
	};
	
	toolbarTrue[toolbarTrue.length] = '-';
	
	toolbarTrue[toolbarTrue.length] = {
			iconCls: 'icon-remove',
			text : '删除组内标签',
			handler: function(){deleteCustomerUserTag();}
		};
	
	var toolbarFalse = [];
		
	toolbarFalse[toolbarFalse.length] = {
		iconCls: 'icon-search',
		text : '查询未选中标签',
		handler: function(){userTagQueryFalse();}
	};
	
	toolbarFalse[toolbarFalse.length] = '-';
	
	toolbarFalse[toolbarFalse.length] = {
		iconCls: 'icon-add',
		text : '添加标签进组',
		handler: function(){addCustomerUserTag();}
	};
	
	xyzgrid({
		table : 'customerUserTagTrueTable',
		title : '已选中的标签',
		url:'../CustomerWS/queryCustomerUserTagTrueList.do',
		height:'auto',
		pageList : [5,10,15,30,50],
		pageSize : 5,
		toolbar : toolbarTrue,
		singleSelect : false,
		idField : 'userTag',
		height:'auto',
		columns:[[
		    {field:'checkboxTemp',checkbox:true},
		    {field:'userTag',title:'编号',hidden:true},
		    {field:'nameCn',title:'名称'}
		]],
		queryParams : {
			customer : customer
		}
	});
	
	xyzgrid({
		table : 'customerUserTagFalseTable',
		title : '未选中的标签',
		url:'../CustomerWS/queryCustomerUserTagFalseList.do',
		height:'auto',
		pageList : [5,15,30,50],
		pageSize : 5,
		toolbar : toolbarFalse,
		singleSelect : false,
		idField : 'userTag',
		height:'auto',
		columns:[[
		    {field:'checkboxTemp',checkbox:true},
		    {field:'userTag',title:'编号',hidden:true},
		    {field:'nameCn',title:'名称'}
		]],
		queryParams : {
			customer : customer
		}
	});
}

function userTagQueryTrue(){
	var nameCn = $("#nameCn").val();
	$("#customerUserTagTrueTable").datagrid('load',{
		customer : customer,
		nameCn : nameCn
	});
}

function userTagQueryFalse(){
	var nameCn = $("#nameCn").val();
	$("#customerUserTagFalseTable").datagrid('load',{
		customer : customer,
		nameCn : nameCn
	});
}

function deleteCustomerUserTag(){
	var userTags = $.map($("#customerUserTagTrueTable").datagrid("getChecked"),function(p){return p.userTag;}).join(",");
	if(xyzIsNull(userTags)){
		top.$.messager.alert("提示","请先选中需要操作的对象！","info");
		return;
	}
	if(!confirm("彻底删除对象，确定？")){
		return;
	}
	
	$.ajax({
		url : "../CustomerWS/deleteCustomerUserTag.do",
		type : "POST",
		data : {
			customer : customer,
			userTags : userTags
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#customerUserTagTrueTable").datagrid("reload");
				$("#customerUserTagFalseTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功！","info");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function addCustomerUserTag(){
	var userTags = $.map($("#customerUserTagFalseTable").datagrid("getChecked"),function(p){return p.userTag;}).join(",");
	if(xyzIsNull(userTags)){
		top.$.messager.alert("提示","请先选中需要操作的对象！","info");
		return;
	}
	
	$.ajax({
		url : "../CustomerWS/addCustomerUserTag.do",
		type : "POST",
		data : {
			customer : customer,
			userTags : userTags
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#customerUserTagTrueTable").datagrid("reload");
				$("#customerUserTagFalseTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功！","info");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}
