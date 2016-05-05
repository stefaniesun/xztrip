$(function(){
	var o = xyz.getUrlparam();
	if(o && !xyz.isNull(o.openid)){
		window.localStorage.openid = o.openid;
	}
	$('.back-left').click(function(){
		xyz.back();
	});
	$('.go-right').click(function(){
		window.location.href=xyz.getFullurl('index.html');
	});
	xyz.id('loginBtn').addEventListener('click', function() {
		userLogin();
	});
	
});

function userLogin(){
	var username = xyz.id("username").value;
	var password = xyz.id("password").value;
	if(xyz.isNull(username) || xyz.isNull(password)){
		weui.alert('手机号不能为空');
		return;
	}
	if(username.length<3 || username.length>16){
		weui.alert('用户名需要3到16为字符');
		return;
	}
	if(password.length<3 || password.length>16){
		weui.alert('密码需要3到16为字符');
		return;
	}
	var md5password = $.md5(password).substr(8,16);
	xyz.ajax({
		url:'WeixinUserInfoWS/bindOpenid.app',
		async:true,
		data:{
			openid : window.localStorage.openid,
			username:username,
			password:md5password
		},
		success:function(data){
			if(data.status==1){
				xyz.id("password").value='';
				window.location.href=xyz.setUrlparam(xyz.getFullurl('index.html'), {"openid":(window.localStorage.openid?window.localStorage.openid:'')});
			}else{
				weui.alert(data.msg);
			}
		}
	});
}