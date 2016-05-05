$(function(){
	$('.back-left').click(function(){
		xyz.back();
	});
	$('.go-right').click(function(){
		window.location.href=xyz.setUrlparam(xyz.getFullurl('index.html'), {"openid":window.localStorage.openid?window.localStorage.openid:''});
	});
	xyz.id('loginBtn').addEventListener('click', function() {
		userLogin();
	});
	
});

function userLogin(){
	var username = xyz.id("username").value;
	var password = xyz.id("password").value;
	if(xyz.isNull(username) || xyz.isNull(password)){
		weui.alert('用户名和密码不能为空');
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
		url:'CustomerWS/login.app',
		async:true,
		data:{
			username:username,
			password:md5password
		},
		success:function(data){
			if(data.status==1){
				xyz.id("password").value='';
				var obj = data.content;
				window.localStorage.apikey = obj.apikey;
				window.localStorage.loginInfo = JSON.stringify(obj);
				window.localStorage.username = username;
				var localLoginInfo = {};
				localLoginInfo.username = username;
//				localLoginInfo.password = md5password;
				localLoginInfo.linkman = obj.linkman;
				localLoginInfo.linkPhone = obj.linkPhone;
				//过期时间7天
//				localLoginInfo.expTime = new Date().getTime()+(7*24*60*60*1000);
				window.localStorage.localLoginInfo = JSON.stringify(localLoginInfo);
				xyz.back();
			}else{
				weui.alert(data.msg);
			}
		}
	});
}