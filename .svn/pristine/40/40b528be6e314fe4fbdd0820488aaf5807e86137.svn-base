
<%
	/* *
	 *功能：支付宝手机网站支付接口调试入口页面
	 *版本：3.3
	 *日期：2012-08-17
	 *说明：
	 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
	 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>支付宝手机网站支付接口</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport"
		content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no">
<style>
* {
	margin: 0;
	padding: 0;
}

ul, ol {
	list-style: none;
}

.title {
	color: #ADADAD;
	font-size: 14px;
	font-weight: bold;
	padding: 8px 16px 5px 10px;
}

.hidden {
	display: none;
}

.new-btn-login-sp {
	border: 1px solid #02aaf1;
	padding: 1px;
	display: inline-block;
}

.new-btn-login {
	background-color: #02aaf1;
	color: #FFFFFF;
	font-weight: bold;
	border: medium none;
	width: 100px;
	height: 28px;
}

.new-btn-login:hover {
	background-color: #02aaf1;
	width: 100px;
	color: #FFFFFF;
	font-weight: bold;
	height: 28px;
}

#main {
	width: 100%;
	margin: 0 auto;
	font-size: 14px;
	font-family: '宋体';
}

.red-star {
	color: #f00;
	width: 10px;
	display: inline-block;
}

.null-star {
	color: #fff;
}

.content {
	margin-top: 5px;
}

.content dt {
	width: 100px;
	display: inline-block;
	text-align: right;
	float: left;
}

.content dd {
	margin-left: 120px;
	margin-bottom: 5px;
}

#foot {
	margin-top: 10px;
}

.foot-ul li {
	text-align: center;
}

.note-help {
	color: #999999;
	font-size: 12px;
	line-height: 130%;
	padding-left: 3px;
}

.cashier-nav {
	font-size: 14px;
	margin: 15px 0 10px;
	text-align: left;
	height: 30px;
	border-bottom: solid 2px #CFD2D7;
}

.cashier-nav ol li {
	float: left;
}

.cashier-nav li.current {
	color: #AB4400;
	font-weight: bold;
}

.cashier-nav li.last {
	clear: right;
}

.alipay_link {
	text-align: right;
}

.alipay_link a:link {
	text-decoration: none;
	color: #8D8D8D;
}

.alipay_link a:visited {
	text-decoration: none;
	color: #8D8D8D;
}
</style>
</head>
<body text=#000000 bgColor="#ffffff" leftMargin=0 topMargin=4>
	<div id="main">
		<div id="head">
			<span class="title">支付宝手机网站支付接口快速通道</span>
		</div>
		<div class="cashier-nav">
			<ol>
				<li class="current">1、确认信息 →</li>
				<li>2、点击确认 →</li>
				<li class="last">3、确认完成</li>
			</ol>
		</div>
		<form name=alipayment action=alipaywap? method=post>
			<div id="body" style="clear: left">
				<dl class="content">
					<dt>商户订单号：</dt>
					<dd>
						<input size="30" name="order_no" />
					</dd>
					<dt>订单名称：</dt>
					<dd>
						<input size="30" name="order_subject" />
					</dd>
					<dt>付款金额：</dt>
					<dd>
						<input size="30" name="order_amount" />
					</dd>
					<dt>订单描述：</dt>
					<dd>
						<input size="30" name="order_body" />
					</dd>
					<dt>返回地址：</dt>
					<dd>
						<input size="30" name="back_url" />
					</dd>
					<dt>成功地址：</dt>
					<dd>
						<input size="30" name="success_url" />
					</dd>
					<dt>失败地址：</dt>
					<dd>
						<input size="30" name="fail_url" />
					</dd>
					<dt></dt>
					<dd>
						<span class="new-btn-login-sp">
							<button class="new-btn-login" type="button" id="pay" onclick="toPay()"
								style="text-align: center;">确 认</button>
						</span>
					</dd>
				</dl>
			</div>
		</form>
		<div id="foot">
			<ul class="foot-ul">
				<li><font class="note-help">如果您点击“确认”按钮，即表示您同意该次的执行操作。 </font></li>
				<li>支付宝版权所有 2011-2015 ALIPAY.COM</li>
			</ul>
		</div>
	</div>
</body>
</html>

<script src="js/library/jquery-1.7.2.js"></script>
<script src="pay/js/common.js"></script>
<script type="text/javascript">
	function toPay() {
		//TODO 创建订单，形成如下订单数据后调用request函数
		/*
		1.订单号																			order_no
		2.订单名称																			order_subject
		3.订单金额，单位分															order_amount
		4.订单描述																			order_body
		5.返回地址（取消支付后回退地址）										back_url
		6.成功页面																			success_url
		7.失败页面																			fail_url
		*/
		
		//get提交
		/* var order_info = $("form").serialize();
		request('pay?'+order_info,'get','',function(flag,res){
			location.href = res;
		});  */
		//post提交
		var order_info = $("form").serialize();
		alert(order_info);
		request('http://192.168.1.10:8080/xztrip/pay','post',order_info,function(flag,res){
			location.href = res;
		}); 
	}

	/* 
	 * .serializeObject (c) Dan Heberden
	 * danheberden.com
	 * 
	 * Gives you a pretty object for your form elements
	 */
	(function($) {
		$.fn.serializeObject = function() {
			if (!this.length) {
				return false;
			}

			var $el = this, data = {}, lookup = data; //current reference of data

			$el
					.find(
							':input[type!="checkbox"][type!="radio"], input:checked')
					.each(
							function() {
								// data[a][b] becomes [ data, a, b ]
								var named = this.name.replace(/\[([^\]]+)?\]/g,
										',$1').split(','), cap = named.length - 1, i = 0;

								for (; i < cap; i++) {
									// move down the tree - create objects or array if necessary
									lookup = lookup[named[i]] = lookup[named[i]]
											|| (named[i + 1] == "" ? [] : {});
								}

								// at the end, psuh or assign the value
								if (lookup.length != undefined) {
									lookup.push($(this).val());
								} else {
									lookup[named[cap]] = $(this).val();
								}

								// assign the reference back to root
								lookup = data;
							});

			return data;
		};
	})(jQuery);
</script>