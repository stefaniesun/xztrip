package com.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088611398543295";
	
	// 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
	public static String seller_id = partner;
	
	//商户的MD5私钥
	public static String private_key = "octm13suu8jfvyfjbp9788lbmg00cxpf";
	
	// 支付宝的RSA公钥，无需修改该值
	public static String ali_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	//rsa_remark_url : http://www.oschina.net/question/163899_24007;
	public static String rsa_private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIiW3k0Pfm+j99P50H64zc5am1p/Posx1djqn7nqq0HFMhuaYuDlalr6NqEZmXn82Orp04hwrOFqJ0EmilJgIY1GsA2Zh5sLsPCjclHoMT9CwP5vV/dzSyhIreJXHc83Xvll/Bl5jCXLwEvf8KZDhdV+2d4ob/JZyWuLbytfSjTxAgMBAAECgYA1eSL0dkFggYfPc/ic88qWo8A4MKKSqeL3dfssNOPw7ruMNOfK9eWqUlS6wyPjrwpZBUusGbiE0yMlDWqPj1Tyv1dQk/ZG9ekCw4BtHdNeZSct3IYjC4Xk3tcMdGo/3SwPqalEu3KN/3RK/o3zX1E3odrmiw/BtrIK0b4SHdGscQJBANZTaaa2bZzs78TnSVT1wldGgxPOU+WIRYtE7YHmuS4k/6AQ/ao32r4OZkeEl89TBYS+in5GyvyTISmcGJMCqH0CQQCjJe7b47ym5BXRSmvSVkavdNMGfK6aQQp2zti4iDFUv8JrTQRGbmP/dCZc2vNFkQDRSEXTbQZff9oDtIXaVxyFAkA3DjhOlgA0Vn0FJLCmvXbNupRDSzYr+UR8ERm9y8n3+5MayCKRTkXHmqVVa88fd+EsB1JGO+M63IShTrLVWNJ1AkEAgYa5N2x8N0e4JpxeM/T5U419iJXujVr78s1P9Fl0SOOheE0CCuTTkVlmp6vRHjb8Hiux/CR/vcwki3KEHo+wfQJASdlMwK8z7k0BKsYl+IdSEtLzWPoQMrUgHEdor5sRH1AbMWFXGrM11mEW9AZCiF1DaezdCTaoUZ5DmzrGkcdzEQ==";
	public static String rsa_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCIlt5ND35vo/fT+dB+uM3OWptafz6LMdXY6p+56qtBxTIbmmLg5Wpa+jahGZl5/Njq6dOIcKzhaidBJopSYCGNRrANmYebC7Dwo3JR6DE/QsD+b1f3c0soSK3iVx3PN175ZfwZeYwly8BL3/CmQ4XVftneKG/yWclri28rX0o08QIDAQAB";

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "MD5";

}
