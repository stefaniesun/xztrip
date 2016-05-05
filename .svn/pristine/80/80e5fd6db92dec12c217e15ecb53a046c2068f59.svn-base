$(document).ready(function() {
	$("#productButton").click(function(){
		loadTable();
	});
	
	initTable();
	
});

function initTable(){

	xyzgrid({
		table : 'providerManagerTable',
		title : '异常日志列表',
		url:'../ExceptionLogWS/queryExceptionLogList.do',
		pageList : [5,10,15,30,50],
		pageSize : 15,
		toolbar:toolbar,
		singleSelect : false,
		idField : 'numberCode',
		height:'auto',
		columns : [[
		    {field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'编号',hidden:true},
			{field:'type',title:'异常类型',width:100},
			{field:'content',title:'异常信息',width:200},
			{field:'addDate',title:'添加时间',width:100}
		]]
	});
	
}

function loadTable(){
	var type = $("#type").combobox("getValue");
	$("#providerManagerTable").datagrid("load",{
		type:type
	});
}
