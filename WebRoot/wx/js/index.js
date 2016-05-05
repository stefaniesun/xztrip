wx.hideOptionMenu();
//alert(JSON.stringify(xyz.getUrlparam()));
var page = 0;
$(function(){
	$("#showBtn").click(function(event){
		$('.menu-show').slideToggle(100);
		$(document).one("click",function(){
			$('.menu-show').hide();
		});
		event.stopPropagation();//阻止事件冒泡
	});
	xyz.id('chooseTab').addEventListener('click',function(e){
		var oldtarget = xyz.id('chooseTab').querySelector('.category-choice-active');
		oldtarget.className=oldtarget.className.replace(' category-choice-active', '');
		e.target.className=e.target.className+" category-choice-active";
		xyz.id('nameCnForm').setAttribute('placeholder', e.target.getAttribute('data-ext')=='SC'?'请输入景点名称':'请输入酒店名称');
		xyz.id('orderList').innerHTML="";
		queryList(page=1);
	});
/*	$(".dropload-down").remove();
	$('#listContianer').dropload({
		scrollArea : window,
		loadDownFn : function(me){
			queryList(++page,me);
		}
	});*/
	xyz.id('searchBtn').addEventListener('click',function(e){
		queryList(page=1);
	});
	queryList(page=1);
	autoLogin();
});

function queryList(page,dropload){
	var target = xyz.id('chooseTab').querySelector('.category-choice-active');
	
	xyz.ajax({
		url:'BuyerProviderWS/queryProviderList.app',
		progress:false,
		data : {
			nameCn : xyz.id('nameCnForm').value,
			page : page,
			rows : 10,
			providerType : target.getAttribute('data-ext'),
			orderBy : 'alterDate DESC'
		},
		success : function(data) {
			if(data.status==1){
				var result = data.content;//当前数据条数
				var table = document.body.querySelector('#orderList');
				if(page==1){
					table.innerHTML = '';
				}
				createHtml(table , result.rows);
				//重置
				if(dropload){
					dropload.resetload();
					if(page==1){
						dropload.unlock();
						dropload.noData(false);
					}
					if(result.rows.length<5){
						dropload.lock('down');
						dropload.noData();
					}
				}
			}else{
				if(dropload){
					dropload.lock();
					dropload.noData();
					dropload.resetload();
				}
				weui.toast(data.msg);
			}
		},
		error: function(xhr, type){
            weui.toast('无法访问服务器啦');
            if(dropload){
            	dropload.lock();
				dropload.noData();
				dropload.resetload();
			}
        }
	});
}

function createHtml(table , list){
	for(var i=0; i<list.length; i++){
		var o = list[i];
		var li = document.createElement('li');
		li.className = 'index-hotel-list';
		var url = xyz.setUrlparam(xyz.getFullurl(o.type=='SC'?'page/productScenicList.html':'page/productHotelList.html'),{numberCode:o.numberCode});
		var html = '<div class="index-hotel-img">';
        html +='<a href="javascript:void(0);" title="" onclick="window.location.href=\''+url+'\'"><img src="'+xyz.getMiddleImageUrl(o.imageUrl)+'" alt=""/></a>';
        html +='</div>';
        html +='<div class="index-hotel-info">';
        html +='<p class="index-hotel-name">';
        html +='<a href="javascript:void(0);">'+o.nameCn+'</a>';
        html +='</p>';
        html +='<p class="index-hotel-price">';
        if(o.price && !isNaN(o.price)){
        	html +='<i>¥'+o.price+'</i>起';
        }else{
        	html +='<i>暂无报价</i>';
        }
        html +='</p>';
        html +='</div>';
		
		//html += '<span id="json_'+o.numberCode+'" style="display:none;">'+JSON.stringify(o)+'</span>';
		li.innerHTML = html;
		table.appendChild(li);
	}
}

function autoLogin(){
	var o = xyz.getUrlparam();
	if(o && !xyz.isNull(o.openid)){
		window.localStorage.openid = o.openid;
	}
	xyz.ajax({
		url:'WeixinUserInfoWS/openidLogin.app',
		async:true,
		data:{
			openid : window.localStorage.openid
		},
		success:function(data){
			if(data.status==1){
				var obj = data.content;
				window.localStorage.apikey = obj.apikey;
				window.localStorage.loginInfo = JSON.stringify(obj);
				window.localStorage.username = obj.username;
				var localLoginInfo = {};
				localLoginInfo.username = obj.username;
//				localLoginInfo.password = md5password;
				localLoginInfo.linkman = obj.linkman;
				localLoginInfo.linkPhone = obj.linkPhone;
				//过期时间7天
//				localLoginInfo.expTime = new Date().getTime()+(7*24*60*60*1000);
				window.localStorage.localLoginInfo = JSON.stringify(localLoginInfo);
			}else{
				if(data.msg.indexOf('没有绑定')>-1){
					window.location.href=xyz.getFullurl('page/bind.html');
				}else{
					weui.alert(data.msg);
				}
			}
		}
	});
}