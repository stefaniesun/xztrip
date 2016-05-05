var o = xyz.getUrlparam();
var dateStart = new Date();
var dateEnd = new Date().xyzNextMonth().xyzNextMonth().xyzNextMonth();
var cal ;

$(function(){
	$('.back-left').click(function(){
		xyz.back();
	});
	$('.go-right').click(function(){
		window.location.href=xyz.setUrlparam(xyz.getFullurl('index.html'), {"openid":window.localStorage.openid?window.localStorage.openid:''});
	});
	xyz.id('dateInfoModify').addEventListener('click', function(e){
		var hideInfo = xyz.id('hideInfo').innerHTML;
		if(!xyz.isNull(hideInfo)){
			hideInfo = JSON.parse(hideInfo);
			if(hideInfo.length>=2){
				cal.show(new Date(hideInfo[0].date), new Date(hideInfo[hideInfo.length-1].date));
			}else{
				cal.show();
			}
		}else{
			cal.show();
		}
	},false);
	xyz.id('chooseNum').addEventListener('click', function(e){
		chooseNum(e);
	},false);
	xyz.id('submitBtn').addEventListener('click', function() {
		submitOrder();
	},false);
	//初始化日历
	cal = new xyzCalendar(
			dateStart,
			dateEnd,
			function(data){
				if(data.length >=2){
					xyz.id('hideInfo').innerHTML = JSON.stringify(data);
					xyz.id('dateInfoStart').innerText = data[0].date.xyzFormat('MM月dd日');
					xyz.id('dateInfoStart').setAttribute('data-date',data[0].date.xyzFormat('yyyy-MM-dd')); 
					xyz.id('dateInfoEnd').innerText = data[data.length-1].date.xyzFormat('MM月dd日');
					xyz.id('dateInfoEnd').setAttribute('data-date',data[data.length-1].date.xyzFormat('yyyy-MM-dd')); 
					xyz.id('dayCount').innerHTML=data.length-1;
				}
				cal.hide();
				calcTotal();
			},
			true
		);
	//加载数据
	loadData();
	//填写默认联系人
	var info = window.localStorage.localLoginInfo;
	if(!xyz.isNull(info)){
		info = JSON.parse(info);
		if(!xyz.isNull(info.linkman)){
			xyz.id('linkmanForm').setAttribute('placeholder',info.linkman);
		}
		if(!xyz.isNull(info.linkPhone)){
			xyz.id('linkPhoneForm').setAttribute('placeholder',info.linkPhone);
		}
	}
	//拉取库存
	getProductStock();
});

function loadData(){
	xyz.ajax({
		url:'BuyerHotelWS/getHotelProduct.app',
		data:{
			numberCode:o.numberCode
		},
		success:function(data){
			if(data.status==1){
				xyz.id('productNameCnShow').innerHTML=data.content.nameCn;
				xyz.id('spotName').innerHTML=data.content.nameCn;
				xyz.id('priceShow').innerHTML=new Number(data.content.price).toFixed(2);
			}else{
				weui.alert(data.msg);
			}
		}
	});
}

function getProductStock(){
	xyz.ajax({
		url:'ProductStockWS/queryProductStockList.app',
		data:{
			apikey:window.localStorage.apikey,
			product:o.numberCode,
			dateStart:dateStart.xyzFormat('yyyy-MM-dd'),
			dateEnd:dateEnd.xyzFormat('yyyy-MM-dd'),
			page:1,
			rows:1000
		},
		success:function(data){
 			if(data.status==1){
				var array = data.content.rows;
				var len = array.length;
				if(len==0){
					weui.alert("本产品近期没有库存。");
					//dateInfo.value="";
				}else{
					for(var i=0;i<len;i++){
						var o = array[i];
						var cc = o.count-o.useCount;
						if(cc>0){
							cal.bindData(o.dateInfo.split(' ')[0], {price:o.price, count:cc}, '<span>¥'+new Number(o.price).toFixed(2)+'</span><span>余'+cc+'</span>');
						}
					}
				}
			}else{
				weui.alert(data.msg);
			}
		}
	});
}

function chooseNum(e){
	var ele = 'IMG'==e.target.nodeName.toUpperCase()?e.target.parentNode:e.target;
	var count = xyz.id('count');
	var val = new Number(count.value);
	if('add'==ele.getAttribute('data-ext')){
		count.value = val+1;
	}else{
		count.value = val<=1?1:val-1;
	}
	calcTotal();
}

function calcTotal(){
	var hideInfo = xyz.id('hideInfo').innerHTML;
	var count = xyz.id('count').value;//购买数量
	if(xyz.isNull(hideInfo)){
		weui.toast('请先选择入/离店日期');
		xyz.id('priceTotal').innerHTML = '0.00';
		xyz.id('count').value=count>1?count-1:count;
		return ;
	}
	hideInfo = JSON.parse(hideInfo);
	var totalSinglePrice = new Number(0);
	var stockArr = new Array();
	for(var i=0; i<hideInfo.length-1; i++){
		var d = hideInfo[i].data;
		totalSinglePrice += new Number(d.price);
		stockArr.push(d.count);
	}
	var min = stockArr.sort(function sortNum(a, b){
		return a - b;
	})[0];
	if(count>min){
		weui.toast('余房不足！<br/>仅剩【'+min+'】间可选');
		xyz.id('count').value=count>1?count-1:count;
		return ;
	}
	
	var total=(totalSinglePrice*count);
	xyz.id('priceTotal').innerHTML=total.toFixed(2);
}

function submitOrder(){
	var apikey = window.localStorage.apikey;
	if(xyz.isNull(apikey)){
		window.location.href=xyz.getFullurl('page/login.html');
		return ;
	}
	var start = xyz.id('dateInfoStart').getAttribute('data-date');
	var end= xyz.id('dateInfoEnd').getAttribute('data-date');
	if(xyz.isNull(start)){
		weui.alert(2000,'请选择入住日期');
		return ;
	}
	if(xyz.isNull(end)){
		weui.alert(2000,'请选择离店日期');
		return ;
	}
	
	var dayCount = 0;
	var dateStart = new Date(start.replace(/-/g,'/'));
	var dateEnd = new Date(end.replace(/-/g,'/'));
	while(dateStart.getTime()<dateEnd.getTime()){
		dayCount++;
		dateStart.setDate(dateStart.getDate()+1);
	}
	
	var count=xyz.id('count').value;
	var linkman = xyz.id('linkmanForm').value;
	var linkPhone = xyz.id('linkPhoneForm').value;
	if(xyz.isNull(linkman)){
		linkman = xyz.id('linkmanForm').getAttribute('placeholder');
	}
	if(xyz.isNull(linkPhone)){
		linkPhone = xyz.id('linkPhoneForm').getAttribute('placeholder');
	}
	if(xyz.isNull(linkman)){
		weui.toast('请填写联系人以便下单成功后可以联系到您');
		return ;
	}
	if(xyz.isNull(linkPhone)){
		weui.toast('请填写联系电话以便下单成功后可以联系到您');
		return ;
	}
	if(!xyz.isPhone(linkPhone)){
		weui.toast('请填写11位正确的手机号码（仅支持中国大陆手机号）');
		return ;
	}
	weui.confirm('提交并支付您的订单？',function(){
		xyz.ajax({
			url:'BuyerOrderWS/createOrder.cus',
			data:{
				apikey:apikey,
				product:o.numberCode,
				count:count,
				dateInfo:start,
				day:dayCount,
				linkman:linkman,
				linkPhone:linkPhone,
				remarkBuy:xyz.id('remarkBuyForm').value
			},
			success:function(data){
				if(data.status==1){
					//submitPay(data.content, money, productNameCn);
					weui.toast('下单成功！请支付',function(){
						window.location.href = xyz.setUrlparam(xyz.getFullurl('page/order.html'),{tab:0});
					});
				}else{
					if(data.msg.indexOf('重新登录')>-1){
						window.location.href=xyz.getFullurl('page/login.html');
					}else{
						weui.alert(data.msg);
					}
				}
			}
		});
	});
	
}
