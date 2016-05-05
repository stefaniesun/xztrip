/**
 * 
 */
var request=function(url,method,data,cb){
    loading();
    var xhr=new XMLHttpRequest();
    xhr.open(method,url,true);
    if(method=="POST"||method=="post"){
        xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded; charset=utf-8");
        xhr.send(data);
    }else{
        xhr.send();
    }
    xhr.onreadystatechange=function(){
        if(xhr.readyState==4){
            hideloading();
            if(xhr.status==200){
                var res=xhr.responseText;
                if(typeof cb == "function"){
                    cb(true, res);
                }
            }else{
                if(typeof cb == "function"){
                    cb(false, null);
                }
            }
        }
    };
    xhr.onerror = function(e) {
        hideloading();
        console.log('onerror');
    };
};
function loading(){
    // console.log('loading');
}
function hideloading(){
    // console.log('hideloading');
}
//取地址get参数
function getUrlRequest() {
   var url = location.search; //获取url中"?"符后的字
   var theRequest = new Object();
   if (url.indexOf("?") != -1) {
      var str = url.substr(1);
      strs = str.split("&");
      for(var i = 0; i < strs.length; i ++) {
         theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
      }
   }
   return theRequest;
}
//判断是否有指定的类样式
function hasClass(obj, cls) {return obj.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));}
//增加类样式
function addClass(obj, cls) {
    if (!hasClass(obj, cls)) {
        var addVal = (obj.className)?' '+cls:cls;
        obj.className += addVal;
    }
}
//删除类样式
function removeClass(obj, cls) {
    if (hasClass(obj, cls)) {
        var reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
        obj.className = obj.className.replace(reg, '');
    }
}
function getCookie(name){
    var ret=document.cookie.match(new RegExp('(?:^|;\\s)'+name+'=(.*?)(?:;\\s|$)'));
    return ret?ret[1]:"";
}
function randomString(len) {
   len = len || 32;
   var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';    /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
   var maxPos = $chars.length;
   var str = '';
   for (var i = 0; i < len; i++) {
       str += $chars.charAt(Math.floor(Math.random() * maxPos));
   }
   return str;
}
function stime(){
    return Date.now()/1000-C_TIME+S_TIME;
}
function alipay(bill_no){
	request('pay.json?bill_no='+bill_no,'get','',function(flag,res){
		res = JSON.parse(res);
		if(res.ResultCode == 1){
			//TODO 考虑是否需要先关闭其他的支付窗口
			//OpenWindow = window.open("", Math.random());
			//OpenWindow.document.write(res.Content);
			//OpenWindow.close()
			document.write(res.Content);
		}
	}); 
}