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
	xyz.id('dateInfoChoose').addEventListener('click', function(e){
		var dateInfo = xyz.id('dateInfoChoose').getAttribute('data-date');
		if(!xyz.isNull(dateInfo)){
			cal.show(dateInfo);
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
			function(date,data){
				xyz.id('hideInfo').innerHTML = JSON.stringify(data);
				xyz.id('dateInfoShow').innerText = date.xyzFormat('yyyy年MM月dd日');
				xyz.id('dateInfoChoose').setAttribute('data-date',date.xyzFormat('yyyy-MM-dd'));
				cal.hide();
				calcTotal();
			}
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
		url:'BuyerScenicWS/getScenicProduct.app',
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
							cal.bindData(o.dateInfo.split(' ')[0], {price:o.price, count:o.count}, '<span>¥'+new Number(o.price).toFixed(2)+'</span><span>余'+o.count+'</span>');
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
	var price = 0;
//	var stock = 0;
	var count = xyz.id('count').value;//购买数量
	if(xyz.isNull(hideInfo)){
		weui.toast('请先选择出行日期');
		xyz.id('priceTotal').innerHTML = '0.00';
		xyz.id('count').value=count>1?count-1:count;
		return ;
	}
	hideInfo = JSON.parse(hideInfo);
	if(hideInfo.count<count){
		weui.alert('库存不足！<br/>仅剩【'+hideInfo.count+'】张');
		xyz.id('count').value=count>1?count-1:count;
		return;
	}
	price = hideInfo.price;
	xyz.id('priceShow').innerHTML = new Number(price).toFixed(2);
	xyz.id('priceTotal').innerHTML = new Number(price*count).toFixed(2);
}

function submitOrder(){
	var apikey = window.localStorage.apikey;
	if(xyz.isNull(apikey)){
		window.location.href=xyz.getFullurl('page/login.html');
		return ;
	}
	var dateInfo = xyz.id('dateInfoChoose').getAttribute('data-date');
	if(xyz.isNull(dateInfo)){
		weui.alert(2000,'请选择出行日期');
		return ;
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
				dateInfo:dateInfo,
				day:1,
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

/*function registerWxPay(){
	xyz.ajax({
		url:'PayWS/registerWxJs.xyz',
		progress:false,
		data : {},
		success : function(data) {
			if(data.status==1){
				wx.config({
				    debug: false,
				    appId: data.content.appId,
				    timestamp: data.content.timestamp,
				    nonceStr: data.content.nonceStr,
				    signature: data.content.signature,
				    jsApiList: ['chooseWXPay']
				});
			}else{
				alert(data.msg);
			}
		}
	});
}*/

function submitPay(orderNum, money, productNameCn){
	xyz.ajax({
		url:'PayWS/getWxJsPayOrderInfo.xyz',
		data:{
			apikey:window.localStorage.apikey,
			orderNo:orderNum,
			totalFee:money,
			body:productNameCn
		},
		success:function(data){
			if(data.status==1){
				alert(JSON.stringify(data));
				wx.chooseWXPay({    
					timestamp: data.content.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
					nonceStr: data.content.nonceStr, // 支付签名随机串，不长于 32 位
					package: data.content.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
					signType: data.content.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
					paySign: data.content.paySign, // 支付签名
					success: function (res) {
						alert(JSON.stringify(res));
					}
				});
			}else{
				weui.alert(data.msg);
			}
		}
	});
}

