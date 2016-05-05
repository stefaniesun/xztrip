//全局类型变量
$(document).ready(function() {
	product = $("#dialogFormDiv_managerProductStockIframe",window.parent.document).attr("name");
	initTable();
	$("#dateInfoQueryInput").datebox();
	$('#queryProductStockButton').click(function(){
		loadTable();
	});
	dateArr = new Array();
	sEventCurrent = false;
	document.onkeydown=function(event){
		var e = event || window.event || arguments.callee.caller.arguments[0];
		if(e.keyCode==83){
			sEventCurrent = true;
		}
	};
	document.onkeyup=function(event){
		var e = event || window.event || arguments.callee.caller.arguments[0];
		if(e.keyCode==83){
			sEventCurrent = false;
		}
	};
	var currentFirstDateInfo = "";
	$("#productStockCalendar").live('mousemove', function() {
		var pageFirstDateInfo = $("#productStockCalendar table tbody tr td").attr("abbr");
		if(currentFirstDateInfo==pageFirstDateInfo){
			return;
		}else{
			currentFirstDateInfo=pageFirstDateInfo;
			//此处调用方法，清洗日历的选中状态
			var tdObj = $("#productStockCalendar .calendar-dtable tr td");
			for(var i=0;i<tdObj.length;i++){
				$(tdObj[i]).removeClass("calendar-other-month calendar-selected");
			}
			setSelectCalendar("productStockCalendar",dateArr);
		}
	});
	$("#productStockCalendar .calendar-dtable > thead > tr > th").live('hover', function() {
		$(this).addClass("calendar-th-hover");
	}).live("click",function(){
		setWeekCalendar("productStockCalendar",this);
	});
	$("#keepPrice").live("click",function(){
		var priceForm = false;
		var priceFormDis = false;
		if($(this).is(':checked')){
			priceForm = false;
			priceFormDis = true;
			$("#priceForm").numberbox("setValue","");
		}else{
			priceForm = true;
			priceFormDis = false;
			$("#priceForm").numberbox("setValue","");
		}
		$('#priceForm').numberbox({
			disabled:priceFormDis,
			required:priceForm
		});
	});
});
function loadTable(){
	var dateInfo = $("#dateInfoQueryInput").datebox("getValue");
	$("#productStockManagerTable").datagrid('load',{
		dateInfo : dateInfo,
		product : product
	});
}
function initTable(){
	var toolbar = [];
	if(xyzControlButton('buttonCode_y20151229104901')){
		toolbar[toolbar.length]={
				text: '新增库存',
				border:'1px solid #bbb',
				iconCls: 'icon-add',
				handler: function(){addProductStock();}
		};
		toolbar[toolbar.length]='-';
	}
	if(xyzControlButton('buttonCode_y20151229104901')){
		toolbar[toolbar.length]={
				text: '删除库存',
				border:'1px solid #bbb',
				iconCls: 'icon-remove',
				handler: function(){deleteProductStock1();}
		};
		toolbar[toolbar.length]='-';
	}
	xyzgrid({
		table : 'productStockManagerTable',
		url:'../ProductStockWS/queryProductForStockList.do',
		toolbar : toolbar,
		title:'管理库存',
		height:'auto',
		singleSelect :false,
		columns:[[
		    {field:'checkboxTemp',checkbox:true},
			{field:'showLogProductStock',width:30,
				formatter: function(value,row,index){
					var img1 = "<img src='../image/other/search.png' alt='详' title='详情' style='cursor:pointer;' onclick='showLogProductStock(\""+row.numberCode+"\");'/>";
					return img1;
				}
			},
		    {field:'numberCode',title:'编号',hidden:true},
		    {field:'product',title:'单品编号',hidden:true},
		    {field:'dateInfo',title:'日期',
				formatter: function(value,row,index){
					return value.split(" ")[0];
				}
			},
		    {field:'price',title:'价格'},
		    {field:'count',title:'剩余数量'},
		    {field:'useCount',title:'已预约数量'},
			{field:'remark',title:'备注'},
			{field:'operator',title:'操作人'},
			{field:'alterDate',title:'修改时间'},
			{field:'addDate',title:'添加时间',hidden:true}
		]],
		queryParams : {
			product : product
		}
	});
}
//新增库存
function addProductStock(){
	var title = $(this).text();
	xyzdialog({
		dialog : 'dialogFormDiv_addProductStock',
		title : title,
	    href : '../jsp_main/addProductStock.html',
	    buttons:[{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addProductStock").dialog("destroy");
				$("#productStockManagerTable").datagrid("reload");
				dateArr = [];
			}
		}],
		onLoad : function(){
			$("#chanageTypeForm").combobox({
				onChange : function(newValue, oldValue){
					$("#priceForm").numberbox("setValue","");
					$("#countForm").numberbox("setValue","");
					if(newValue==0){
						$("#priceStockForm tr td").show();
					}else{
						$("#priceStockForm tr td").hide();
					}
				}
			});
			$("#stockCommboxForm").combobox({});
			initCalendar();
			$("#productStockCalendar .calendar-dtable tr td").removeClass("calendar-other-month calendar-selected calendar-today");
			$("#queryProductStockForCalendarButton").click(function(){
				queryProductStockForCalendarButton();
			});
			$("#queryCurrerentProductStockForCalendarButton").click(function(){
				queryCurrerentProductStockForCalendarButton();
			});
		}
	});
}
function productStockButtonSubmit(){
	var chanageType = $("#chanageTypeForm").combobox("getValue");
	if(chanageType==0){
		//添加/减少库存
		addProductStockSubmit();
	}else{
		//删除库存/价格
		deleteProductStock2();
	};
}
function addProductStockSubmit(){
	if(!$("#deleteForm").form('validate')){
		return false;
	}
	if(product.length<=0){
		top.$.messager.alert("警告","参数异常无法提交","warning");
		return false;
	}
	var stockCommboxForm = $("#stockCommboxForm").combobox("getValue");
	var count = $("#countForm").numberbox("getValue").trim();
	var price = $("#priceForm").numberbox("getValue").trim();
	if(stockCommboxForm=="+"){
		;
	}else if(stockCommboxForm=="-"){
		count = "-"+count;
	}else{
		top.$.messager.alert("提示","请在下拉框选择正确的加减库存选项","info");
		return false;
	}
	var dateInfo = dateArr.join(",");
	var isAlterPrice = 0;
	if($("#keepPrice").is(":checked")){
		isAlterPrice=1;
	}
	xyzAjax({
		url : "../ProductStockWS/addProductStock.do",
		data : {
			product : product,
			count : count,
			dateInfo : dateInfo,
			price : price,
			isAlterPrice:isAlterPrice
		},
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","操作成功","info");
				initCalendar();
				productStockReset();
				$("#productStockCalendar .calendar-dtable tr td").removeClass("calendar-other-month");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});
}
function showLogProductStock(numberCode){
	var row = xyzGetCurrentRow("productStockManagerTable","numberCode",numberCode);
	xyzAjax({
		url : "../ProductStockWS/queryLogProductStock.do",
		data : {
			product : row.product,
			dateInfo : row.dateInfo
		},
		success : function(data) {
			if(data.status==1){
				content = data.content;
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});
	xyzdialog({
		dialog : 'dialogFormDiv_queryLogProductStock',
		title : "单品日志记录",
	    content : '<div class="xieyaozhong" id="logProductStoc"></div>',
	    buttons:[{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_queryLogProductStock").dialog("destroy");
			}
		}],
		onOpen : function(){
			logProductStockHTML ="";
			for(var i =0;i<content.length;i++){
				var logWork = content[i];
				logProductStockHTML +='<span style="color : green">'+logWork.remark+"【"+logWork.username+"】"+"【"+logWork.addDate+"】"+'<span><br/>';
			}
			$("#logProductStoc").empty();
			$("#logProductStoc").append(logProductStockHTML);
		}
	});
}
function getStart2EndDateArr(sDate,eDate){
	var sDateArr = [];
	var ab = sDate.replace(/-/g,"/");
	var ae = eDate.replace(/-/g,"/");
	var db = new Date(ab);
	var de = new Date(ae);
	var unixDb=db.getTime();
	var unixDe=de.getTime();
	sDateArr.push(sDate);
	for(var k=unixDb+24*60*60*1000;k<unixDe;){
		sDateArr.push(new Date(k).Format("yyyy-MM-dd"));
		k=k+24*60*60*1000;
	}
	sDateArr.push(eDate);
	return sDateArr;
}
function addOrSubDate(date,days){ 
    var d=new Date(date); 
    d.setDate(d.getDate()+days); 
    var m=d.getMonth()+1; 
    return new Date(d.getFullYear()+'/'+m+'/'+d.getDate()).Format("yyyy-MM-dd");
}
function initCalendar(){
	//获取sku数据
	var productStockList = getProductStockListAjax();
	//初始化日历控件
	xyzGetCalendar({
		id : "productStockCalendar",
		data : productStockList,
		//外网设置成false
		isShowOldDate : true,
		isShwoStock : true,
		isShwoPrice : true,
		functionClick : function(date){
			var calendarDate = date.getFullYear()+"/"+(date.getMonth()+1)+"/"+date.getDate();
			var currentDate = new Date();
			var year=currentDate.getFullYear();
			var month = currentDate.getMonth()+1;
			var day = currentDate.getDate();
			var currentCal = year +"/" + month +"/" + day;
			if(new Date(calendarDate).getTime()<new Date(currentCal).getTime()){
				setSelectCalendar("productStockCalendar",dateArr);
				return;
			}
			$("#productStockCalendar").click(function(e){
				calendarDate = new Date(calendarDate).Format("yyyy-MM-dd");
				if(sEventCurrent){
					if(dateArr.length==0){
						dateArr.push(calendarDate);
					}else{
						var isExist = false;
						for(var j in dateArr){
							if(calendarDate == dateArr[j]){
								isExist = true;
								break;
							}
						}
						if(!isExist){
							var sortDateArr = [];
							sortDateArr = dateArr.sort();
							var sortDateLen = sortDateArr.length;
							var min2MaxDateArr = [];
							var beforeMinDate = addOrSubDate(sortDateArr[0],-1);
							var afterMaxDate = addOrSubDate(sortDateArr[sortDateLen-1],1);
							if(calendarDate==beforeMinDate || calendarDate==afterMaxDate){
								dateArr.push(calendarDate);
							}else{
								if(calendarDate<=beforeMinDate){//头
									min2MaxDateArr = getStart2EndDateArr(calendarDate,beforeMinDate);
								}else if(calendarDate>beforeMinDate && calendarDate<afterMaxDate){//中间
									var cal = queryLaterDate(calendarDate,dateArr,calendarDate);
									if(cal!=calendarDate){
										min2MaxDateArr = getStart2EndDateArr(cal,calendarDate);
									}
								}else if(calendarDate>=afterMaxDate){//尾
									min2MaxDateArr = getStart2EndDateArr(afterMaxDate,calendarDate);
								}
								for(var m2md in min2MaxDateArr){
									dateArr.push(min2MaxDateArr[m2md]);
								}
							}
						}
					}
					var sDate2="";
					var dateArr2 = [];
					dateArr2 = getSortDateArr(dateArr);
					for(var d2 in dateArr2){
						sDate2 += dateArr2[d2]+"\n";
					}
					$("#dateInfoForm").val(sDate2);
					setSelectCalendar("productStockCalendar",dateArr);
				}else {
					setClickCalender("productStockCalendar",calendarDate,false);
				}
				$("#productStockCalendar").unbind();
			});
		},
		//外网使用以下验证
		functionValidator : function(date){
			return true;
		}
	});
};
function setClickCalender(calendarId,calendarDate,queryDateFlag){
	if(dateArr.length==0){
		clickDate = true;
		dateArr.push(calendarDate);
	}else{
		var isExist = false;
		for(var j=0;j<dateArr.length;j++){
			if(calendarDate==dateArr[j]){
				isExist = true;
				break;
			}
		}
		if(isExist && !queryDateFlag){
			dateArr.splice(j,1);
		}else if(!isExist){
			dateArr.push(calendarDate);
		}
	}
	var clickDate2="";
	var dateArr2 = [];
	dateArr2 = getSortDateArr(dateArr);
	for(var d2 in dateArr2){
		clickDate2 += dateArr2[d2]+"\n";
	}
	$("#dateInfoForm").val(clickDate2);
	setSelectCalendar(calendarId,dateArr);
}
function setWeekCalendar(id,ttt){//周期
	$("#productStockCalendar .calendar-dtable tr td").removeClass("calendar-other-month");
	var currentDate = new Date();
	var year=currentDate.getFullYear();
	var month = currentDate.getMonth()+1;
	var day = currentDate.getDate();
	var currentCal = year +"/" + month +"/" + day;
	var t = $('#'+id).find(".calendar-dtable > thead > tr > th");
	var tt = $(ttt).text();
	$(t).parent().find('th').each(function(i,n){
		if($(n).text()==tt){
			var weekDateArr = [];
			$('#'+id).find('.calendar-dtable tbody tr td:nth-child('+(i+1)+')').each(function(k,v){
				var abbrArr = $(v).attr("abbr").split(",");
				var date = abbrArr[0]+"/"+abbrArr[1]+"/"+abbrArr[2];
				if(new Date(date).getTime() >= new Date(currentCal).getTime()){
					weekDateArr.push(new Date(date).Format("yyyy-MM-dd"));
				}
			});
			if(dateArr.length==0){
				for(var kk=0;kk<weekDateArr.length;kk++){
					var wd = weekDateArr[kk];
					dateArr.push(wd);
				}
			}else{
				var k = 0;
				for(var i=0;i<weekDateArr.length;i++){
					var isExist = false;
					var wd = weekDateArr[i];
					for(var j=0;j<dateArr.length;j++){
						if(wd == dateArr[j]){
							isExist = true;
							k++;
							continue;
						}
					}
					if(!isExist){
						dateArr.push(wd);
					}
				}
				if(k==weekDateArr.length){
					for(var i=0;i<weekDateArr.length;i++){
						var wd = weekDateArr[i];
						for(var j=0;j<dateArr.length;j++){
							if(wd == dateArr[j]){
								dateArr.splice(j,1);
								continue;
							}
						}
					}
				}
			}
			var weekSelectDate = "";
			var dateArr2 = [];
			dateArr2 = getSortDateArr(dateArr);
			for(var k in dateArr2){
				weekSelectDate += dateArr2[k]+"\n";
			}
			$("#dateInfoForm").val(weekSelectDate);
			setSelectCalendar(id,dateArr,false);
		}
	});
}
function productStockReset(){
	dateArr = [];
	$("#dateInfoForm").val("");
	$('#priceForm').numberbox("setValue","");
	$("#countForm").numberbox("setValue","");
	$("#totalDay").text(0);
	var tdObj = $("#productStockCalendar .calendar-dtable tr td");
	for(var i=0;i<tdObj.length;i++){
		$(tdObj[i]).removeClass("calendar-selected");
	}
}
function addOrSubDate(date,days){ 
    var d=new Date(date); 
    d.setDate(d.getDate()+days); 
    var m=d.getMonth()+1; 
    return new Date(d.getFullYear()+'/'+m+'/'+d.getDate()).Format("yyyy-MM-dd");
}
function queryLaterDate(calendarDate,destDate){
	var isExist = false;
	var laterDate = "";
	for(var j=0;j<destDate.length;j++){
		if(calendarDate == destDate[j]){
			isExist = true;
			laterDate = addOrSubDate(calendarDate,1);
			return laterDate;
		}
	}
	if(!isExist){
		var b = addOrSubDate(calendarDate,-1);
		return queryLaterDate(b,destDate);
	}
}
function GetDateRegion(BeginDate,EndDate){
	var aDate, oDate1, oDate2, iDays;
	var sDate1=BeginDate;
    var sDate2=EndDate;              
	aDate = sDate1.split("-");
	oDate1 = new Date(aDate[1] + '/' + aDate[2] + '/' + aDate[0]);
	aDate = sDate2.split("-");
	oDate2 = new Date(aDate[1] + '/' + aDate[2] + '/' + aDate[0]);
	var i=(oDate2 - oDate1) / 1000 / 60 / 60 /24;
	if(i<0){
		i-=1;
	}
	else{
		i+=1;
	}
	iDays = i-1;   //把相差的毫秒数转换为天数
	return iDays;
}
function getSortDateArr(destDate){
	var endSortDateArr = [];
	var sortDateArr = [];
	sortDateArr = destDate.sort();
	$("#totalDay").text(sortDateArr.length);
	var continues = false;
	var firstContinueDate = "";
	var lastContinueDate = "";
	var sortDateArrLen = sortDateArr.length;
	if(sortDateArrLen==1){
		endSortDateArr.push(sortDateArr[0]);
	}else{
		for(var i=0;i<sortDateArrLen;i++){
			if(i < sortDateArrLen-1){
				var s1 = sortDateArr[i];
				var s2 = sortDateArr[i+1];
				if(GetDateRegion(s1,s2)==1){
					if(!continues){
						firstContinueDate = s1;
					}
					lastContinueDate = s2;
					continues = true;
					if((i+1)==sortDateArrLen-1){
						endSortDateArr.push(firstContinueDate+"~"+lastContinueDate);
						for(var j=0;j<endSortDateArr.length;j++){
							if(firstContinueDate == endSortDateArr[j]){
								endSortDateArr.splice(j,1);
							}
						}
					}
				}else{
					if(continues){
						endSortDateArr.push(firstContinueDate+"~"+lastContinueDate);
					}
					if(i==0){
						endSortDateArr.push(s1);
					}
					endSortDateArr.push(s2);
					for(var j=0;j<endSortDateArr.length;j++){
						if(firstContinueDate == endSortDateArr[j]){
							endSortDateArr.splice(j,1);
						}
					}
					continues = false;
				}
			}
		}
	}
	return endSortDateArr;
}
function setSelectCalendar(calendarId,array){
	if(!(array instanceof Array)){
		top.$.messager.alert("警告","传递参数有误","warning");
		return;
	}
	var tdObj = $("#"+calendarId).find(".calendar-dtable tr td");
	for(var i=0;i<tdObj.length;i++){
		$(tdObj[i]).removeClass("calendar-selected");
	}
	var abbr = [];
	for(var i=0;i<tdObj.length;i++){
		abbr = $(tdObj[i]).attr("abbr").split(",");
		var abbrDate = new Date(abbr[0]+"/"+abbr[1]+"/"+abbr[2]).Format("yyyy-MM-dd");
		for(var j=0;j<array.length;j++){
			if(abbrDate==array[j]){
				$(tdObj[i]).addClass("calendar-selected");
			}
		}
	}
}
function getProductStockListAjax(){
	var result = "";
	xyzAjax({
		url : "../ProductStockWS/queryProductStockAllList.do",
		data : {
			product : product
		},
		success : function(data) {
			if(data.status==1){
				 result = data.content;
		    }else{
		    	top.$.messager.alert("警告",data.msg,"warning");
		    }
		}
	});
	return result;
};

//删除库存 用于datagrid 外页面直接删除
function deleteProductStock1(){
	var numberCodes = $.map($("#productStockManagerTable").datagrid("getChecked"),function(p){return p.numberCode;}).join(",");
	if(numberCodes == null || numberCodes == ''){
		top.$.messager.alert("提示","请先选中需要删除的对象！","info");
		return;
	}
	$.messager.confirm("提示","确定要删除该库存？删除后将不能恢复，如该日期有预约，将不再回退该库存！",function(r){
		if(r){
			xyzAjax({
				url : "../ProductStockWS/deleteProductStock.do",
				data : {
					numberCodes : numberCodes
				},
				success : function(data) {
					if(data.status==1){
						$("#productStockManagerTable").datagrid("reload");
						top.$.messager.alert("提示","操作成功！","info");
					}else{
						top.$.messager.alert("警告",data.msg,"warning");
					}
				}
			});
		}
	});
}
//删除库存 用于日期页面 可视化删除
function deleteProductStock2(){
	var dateInfoForm = $("#dateInfoForm").val();
	if(xyzIsNull(dateInfoForm)){
		return false;
	}
	if(dateArr.length==0){
		return false;
	}
	var dateInfo = "";
	for(var d in dateArr){
		dateInfo += dateArr[d]+",";
	}
	$.messager.confirm('警告','确定要删除该库存？删除后将不能恢复，如该日期有预约，将不再回退该库存！',function(r){    
	    if(r){
	    	xyzAjax({
				url : "../ProductStockWS/deleteProductStock2.do",
				data : {
					product : product,
					dateInfo : dateInfo
				},
				success : function(data) {
					if(data.status==1){
						top.$.messager.alert("提示","操作成功！","info");
						initCalendar();
						productStockReset();
						$("#productStockCalendar .calendar-dtable tr td").removeClass("calendar-other-month");
				    }else{
				    	top.$.messager.alert("警告",data.msg,"warning");
				    }
				}
			});
	    }    
	}); 
}
//查询库存价格
function queryProductStockForCalendarButton(){
	var queryDate = $("#queryDate").datebox("getValue");
	var currerentDate = new Date(new Date().Format("yyyy-MM-dd")+" 00:00:00");
	if(new Date(queryDate)<currerentDate.getTime()){
		top.$.messager.alert("提示","不能小于当前日期！","info");
		return;
	}
	if(xyzIsNull(queryDate)){
		top.$.messager.alert("提示","请选择查询日期","info");
		return;
	}else{
		var str = queryDate.split("-");
		var year = str[0];
		var month = str[1]-1;
		var day = str[2];
		$("#productStockCalendar").calendar('moveTo', new Date(year,month,day));
		var queryDate = new Date(year+"/"+(month+1)+"/"+day).Format("yyyy-MM-dd");
		setClickCalender("productStockCalendar",queryDate,true);
		$("#productStockCalendar .calendar-dtable tr td").removeClass("calendar-other-month");
	}
};
//查询当天价格库存
function queryCurrerentProductStockForCalendarButton(){
	var currerentDate = new Date().Format("yyyy-MM-dd");
	var str = currerentDate.split("-");
	var year = str[0];
	var month = str[1]-1;
	var day = str[2];
	$("#productStockCalendar").calendar('moveTo', new Date(year,month,day));
	var queryDate = new Date(year+"/"+(month+1)+"/"+day).Format("yyyy-MM-dd");
	setClickCalender("productStockCalendar",queryDate,true);
	$("#productStockCalendar .calendar-dtable tr td").removeClass("calendar-other-month");
};