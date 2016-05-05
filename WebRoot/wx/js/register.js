$(function(){
	$('.back-left').click(function(){
		xyz.back();
	});
	//发送验证码
	xyz.id('sendValidateCodeBtn').addEventListener('click', function() {
		sendValidateCode(this);
	});
	//下一步按钮
	xyz.id('nextSetupBtn').addEventListener('click', function() {
		nextSetup();
	});
	//注册
	xyz.id('registerBtn').addEventListener('click', function() {
		register();
	});
});

function sendValidateCode(thiz){
	var isDisabled = thiz.getAttribute("data-isDisabled");
	if(isDisabled!='true'){
		return;
	}
	var phoneNum = xyz.id("phoneNum").value;
	if(xyz.isNull(phoneNum)){
		weui.alert('请填写手机号');
		return;
	}
	if(!xyz.isPhone(phoneNum)){
		weui.alert('请填写11位正确的手机号码（仅支持中国大陆手机号）');
		return;
	}
	xyz.ajax({
		url:'CustomerWS/getRandCode.app',//获取验证码的连接
		data:{
			phone:phoneNum
		},
		success:function(data){
			if(data.status==1){
				//发送成功开始计时30秒后才允许重发
				window.intervalTimeIndex = 0;
				window.intervalIndex = setInterval("intervalFuc()", 1000);
				mui.toast("验证码已发送，注意查收");
				//验证码获取成功
			}else{
				weui.alert(data.msg);
			}
		}
	});
}

function nextSetup(){
	var phoneNum = xyz.id("phoneNum").value;
	var validateCode = xyz.id("validateCode").value;
	if(xyz.isNull(phoneNum)){
		weui.alert('请填写手机号');
		return;
	}
	if(!xyz.isPhone(phoneNum)){
		weui.alert('请填写11位正确的手机号码（仅支持中国大陆手机号）');
		return;
	}
	if(xyz.isNull(validateCode)){
		weui.alert('请填写验证码');
		return;
	}
	xyz.ajax({
		url:'CustomerWS/verifyRandCode.app',
		data:{
			phone:phoneNum,//17091924118
			randCode:validateCode//126169
		},
		success:function(data){
			if(data.status==1){
				xyz.id('account').innerText = phoneNum;
				xyz.id('setup1').style.display='none';
				xyz.id('setup2').style.display='block';
			}else{
				weui.alert(data.msg);
			}
		}
	});
}

function register(){
	var username = xyz.id('account').innerText;
	var password = xyz.id('password').value;
	var repassword = xyz.id('repassword').value;
	if(xyz.isNull(username)){
		weui.alert('账号不能为空');
		return;
	}
	if(xyz.isNull(password)){
		weui.alert('密码不能为空');
		return;
	}
	if(xyz.isNull(repassword)){
		weui.alert('重复密码不能为空');
		return;
	}
	if(password.length<3 || password.length>16){
		weui.alert('密码需要3到16为字符');
		return;
	}
	if(password!=repassword){
		weui.alert('两次输入的密码不匹配');
		return;
	}
	var md5password = $.md5(password).substr(8,16);
	xyz.ajax({
		url:'CustomerWS/register.app',
		data:{
			phone:username,
			password:md5password
		},
		success:function(data){
			if(data.status==1){
				weui.toast("注册成功");
				login(username, md5password);
				//mui.back();
			}else{
				weui.alert(data.msg);
			}
		}
	});
}


function login(username, md5password){
	xyz.ajax({
		url:'CustomerWS/login.app',
		async:true,
		data:{
			username:username,
			password:md5password
		},
		success:function(data){
			if(data.status==1){
				weui.toast("已登录");
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
				if(window.localStorage.openid){
					bind(username, md5password);
				}else{
					window.location.href = xyz.getFullurl('index.html');
				}
			}else{
				weui.alert(data.msg);
			}
		}
	});
}

function bind(username, md5password){
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
				window.location.href=xyz.setUrlparam(xyz.getFullurl('wx.html'), {"openid":(window.localStorage.openid?window.localStorage.openid:''), page:1});
			}else{
				weui.alert(data.msg);
			}
		}
	});
}


//控制按钮倒计时
function intervalFuc(){
	intervalTimeIndex++;
	if(intervalTimeIndex>=60){
		xyz.id('sendValidateCodeBtn').setAttribute("data-isDisabled","true");
		xyz.id('sendValidateCodeBtn').innerText="发送验证码";
		window.clearInterval(window.intervalIndex);
	}else{
		xyz.id('sendValidateCodeBtn').innerText="发送验证码（"+(60-window.intervalTimeIndex)+"）";
		xyz.id('sendValidateCodeBtn').setAttribute("data-isDisabled","false");
	}
}

