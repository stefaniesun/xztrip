var o = xyz.getUrlparam();
$(function(){
	$('.back-left').click(function(){
		xyz.back();
	});
	if(o && !xyz.isNull(o.tab)){
		//处理默认选中
		var li = xyz.id('chooseTab').querySelectorAll('li');
		for(var i=0;i<li.length;i++){
			if(o.tab == li[i].getAttribute('data-ext')){
				li[i].className = 'item-active';
			}else{
				li[i].className = '';
			}
		}
	}
	if(xyz.id('chooseTab').querySelectorAll('.item-active').length<=0){
		xyz.id('chooseTab').querySelector('li').className = 'item-active';
	}
	xyz.id('chooseTab').addEventListener('click',function(e){
		xyz.id('chooseTab').querySelector('.item-active').className="";
		e.target.className="item-active";
		xyz.id('orderList').innerHTML="";
		queryList();
	});
	queryList();
});

function queryList(){
	var rows = 0; //记录条数
	var page = 0; //每页数
	$('#orderList').dropload({
		scrollArea : window,
		domDown : {
			domClass : 'dropload-down',
			domRefresh : '<div class="dropload-refresh">↑上拉加载更多</div>',
			domLoad : '<div class="dropload-load">加载中...</div>',
			domNoData : '<div class="dropload-noData">暂无数据</div>'
		},
		autoLoad : true, //自动加载
		loadDownFn : function(me){
			var flagPay = xyz.id('chooseTab').querySelector('.item-active').getAttribute('data-ext');
			rows += 10; //记录以及加载的条数
			page++ ; //没调用一次  页数加1
			xyz.ajax({
				url:'BuyerOrderWS/queryOrderList.cus',
				data : {
					apikey: window.localStorage.apikey,
					page : page,
					rows : 10,
					flagPay : flagPay
				},
				success : function(data) {
					if(data.status==1){
						console.log(data);
						var total = data.content.total; 
						var result = data.content;
						var table = xyz.id('orderList');
    					if(page==1){
    						table.innerHTML = '';
    					}
    					var len = result.rows.length;
    					for(var i=0;i<len;i++){
    						var o = result.rows[i];
    						var type = o.product;
    						if(type.indexOf('SC')==0){
    							table.appendChild(showItemScenic(o));
    						}else if(type.indexOf('HO')==0){
    							table.appendChild(showItemHotel(o));
    						}else{
    							table.appendChild(showItemScenic(o));
    						}
    					}
    					//createHtml(table , result.rows);
						if(rows >= total){ //每页数据
							me.lock();
							me.noData();
						}
						me.resetload();
					}else{
						if(data.msg.indexOf('重新登录')>-1){
							window.location.href=xyz.getFullurl('page/login.html');
						}else{
							weui.alert(data.msg);
						}
					}
				}
			});
		}
	});
}

function showItemHotel(o){
	var dateInfoEnd = new Date(new Date(o.dateInfoEnd.split(' ')[0].replace(/-/g,"/")).getTime()+86400000).format('yyyy-MM-dd');
	var html = new Array();
	var pay = o.flagPay==1?'<span style="color:green">已支付</span>':'<span style="color:red;">未付款</span>';
	var url = xyz.setUrlparam(xyz.getFullurl('page/orderDetail.html'),{orderNum:o.orderNum});
	html.push('<li>');
	html.push('<p class="order-num">订单号：'+o.orderNum+'</p>');
	html.push('<span class="order-struts color-danger">'+pay+'</span>');
	html.push('</li>');
	html.push('<li>');
	html.push('<a href="'+url+'" class="list-item-link">');
//		html.push('<div class="order-img">');
//		html.push('<img src="../images/hotel.jpg">');
//		html.push('</div>');
	html.push('<div class="order-inner">');
	html.push('<div class="order-title-row">');
	html.push('<div class="order-title">');
	html.push(o.productNameCn);
	html.push('</div>');
	html.push('<div class="order-title-after">¥'+new Number(o.money).toFixed(2)+'</div>');
	html.push('</div>');
	html.push('<div class="subtitle">');
	html.push('数量：'+o.count+'间');
	html.push('</div>');
	html.push('<div class="subtitle">');
	html.push('入住：<time>'+o.dateInfoStart.split(' ')[0]+'</time>&nbsp;&nbsp;离店：<time>'+dateInfoEnd+'</time>');
	html.push('</div>');
	html.push('</div>');
	html.push('<img class="icon-more" src="../images/i-list-chevron-right.png" />');
	html.push('</a>');
	html.push('</li>');
	html.push('<li>');
	html.push('<p class="order-bz"><span style="color: #b31e23;">备&nbsp;注</span>：'+(xyz.isNull(o.remark)?'暂无备注':o.remark)+'</p>');
	html.push('</li>');
	html.push('<li>');
	html.push('<div class="order-btn">');
	html.push('<button class="btn-reset" onclick="deleteOrder(this)" data-ext="'+o.orderNum+'">删除订单</button>');
	if(o.flagPay==0){
		html.push('<button class="orderpay-btn" onclick="window.location.href=\''+url+'\'">立即付款</button>');
	}
	html.push('</div>');
	html.push('</li>');
	var ul = document.createElement('ul');
	ul.className='myorder-list-item';
	ul.innerHTML = html.join('');
	return ul;
}

function showItemScenic(o){
	var html = new Array();
	var pay = o.flagPay==1?'<span style="color:green">已支付</span>':'<span style="color:red;">未付款</span>';
	var url = xyz.setUrlparam(xyz.getFullurl('page/orderDetail.html'),{orderNum:o.orderNum});
	html.push('<li>');
	html.push('<p class="order-num">订单号：'+o.orderNum+'</p>');
	html.push('<span class="order-struts color-danger">'+pay+'</span>');
	html.push('</li>');
	html.push('<li>');
	html.push('<a href="'+url+'" class="list-item-link">');
//		html.push('<div class="order-img">');
//		html.push('<img src="../images/hotel.jpg">');
//		html.push('</div>');
	html.push('<div class="order-inner">');
	html.push('<div class="order-title-row">');
	html.push('<div class="order-title">');
	html.push(o.productNameCn);
	html.push('</div>');
	html.push('<div class="order-title-after">¥'+new Number(o.money).toFixed(2)+'</div>');
	html.push('</div>');
	html.push('<div class="subtitle">');
	html.push('数量：'+o.count+'张');
	html.push('</div>');
	html.push('<div class="subtitle">');
	html.push('出行日期：<time>'+o.dateInfoStart.split(' ')[0]+'</time>');
	html.push('</div>');
	html.push('</div>');
	html.push('<img class="icon-more" src="../images/i-list-chevron-right.png" />');
	html.push('</a>');
	html.push('</li>');
	html.push('<li>');
	html.push('<p class="order-bz"><span style="color: #b31e23;">备&nbsp;注</span>：'+(xyz.isNull(o.remark)?'暂无备注':o.remark)+'</p>');
	html.push('</li>');
	html.push('<li>');
	html.push('<div class="order-btn">');
	html.push('<button class="btn-reset" onclick="deleteOrder(this)" data-ext="'+o.orderNum+'">删除订单</button>');
	if(o.flagPay==0){
		html.push('<button class="orderpay-btn" onclick="window.location.href=\''+url+'\'">立即付款</button>');
	}
	html.push('</div>');
	html.push('</li>');
	var ul = document.createElement('ul');
	ul.className='myorder-list-item';
	ul.innerHTML = html.join('');
	return ul;
}

function deleteOrder(ele){
	var orderNum = ele.getAttribute('data-ext');
	if(xyz.isNull(orderNum)){
		weui.alert('订单编号不存在');
		return ;
	}
	weui.confirm('您确定要删除该订单吗？删除后不可恢复',function(){
		xyz.ajax({
			url:'BuyerOrderWS/deleteOrder.cus',
			data:{
				apikey:window.localStorage.apikey,
				orderNum:orderNum
			},
			success:function(data){
				if(data.status==1){
					var o = xyz.id(orderNum);
					o.parentNode.removeChild(o);
					weui.toast('删除成功');
				}else{
					weui.alert(data.msg);
				}
			}
		});
	});
}