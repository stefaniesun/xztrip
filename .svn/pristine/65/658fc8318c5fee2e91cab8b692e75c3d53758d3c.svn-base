package xyz.svc.pay.imp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import xyz.filter.ReturnUtil;
import xyz.model.core.WxBill;
import xyz.util.httpClient.HttpUtil;

public class WxPayUtil {
    public static final String KEY ="086E7CA6906293007F9D92FF23432A2A";//api密匙
    //服务器需要使用的
    public static final String CORPID="wx8ea7fc6017e0d884";//微信分配的公众账号ID（企业号corpid即为此appId）
    public static final String MCH_ID="1318613101";// 微信支付分配的商户号
    
	public static final String WXPAY_URL_UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";// 统一下单API
	public static final String WXPAY_URL_ClOSEORDER = "https://api.mch.weixin.qq.com/pay/closeorder";// 关闭订单
	public static final String WXPAY_URL_ORDERQUERY = "https://api.mch.weixin.qq.com/pay/orderquery";// 查询订单
	
	public static final String WXPAY_NOTFIY_URL = "http://www.xingzang.net/PayWS/wxPayNotify.xyz";
	
	public static Map<String,Object> doWxPay(WxBill bill){
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid",bill.getAppid());
		packageParams.put("mch_id",bill.getMch_id());
		packageParams.put("nonce_str",bill.getNonce_str());
		
		packageParams.put("body", bill.getBody());
		packageParams.put("out_trade_no", bill.getOut_trade_no());
		packageParams.put("total_fee", String.valueOf(bill.getTotal_fee()));
		packageParams.put("spbill_create_ip", bill.getSpbill_create_ip());
		packageParams.put("notify_url", bill.getNotify_url());
		packageParams.put("trade_type", bill.getTrade_type());
		packageParams.put("device_info", bill.getDevice_info());
		packageParams.put("detail", bill.getDetail());
		packageParams.put("attach", bill.getAttach());
		packageParams.put("product_id", bill.getProduct_id());
		packageParams.put("time_start", bill.getTime_start());
		packageParams.put("time_expire", bill.getTime_expire());
		packageParams.put("fee_type", bill.getFee_type());
		packageParams.put("goods_tag", bill.getGoods_tag());
		packageParams.put("limit_pay", bill.getLimit_pay());
		packageParams.put("openid", bill.getOpenid());
		
		//签名
		packageParams.put("sign", wxPaySign(packageParams).toUpperCase());
		
		//map2xml
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xml", packageParams);
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			result = xml2map(HttpUtil.httpsRequest(WXPAY_URL_UNIFIEDORDER, "POST", map2xml(map)));
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String return_code = result.get("return_code")==null?"":result.get("return_code").toString();
		if("SUCCESS".equals(return_code)){
			String result_code = result.get("result_code")==null?"":result.get("result_code").toString();
			if("SUCCESS".equals(result_code)){
				return ReturnUtil.returnMap(1, result);
			}else{
				String err_code = result.get("err_code")==null?"":result.get("err_code").toString();
				String err_code_des = result.get("err_code_des")==null?"":result.get("err_code_des").toString();
				String tipStr = err_code_des+err_code;
				if("".equals(tipStr)){
					tipStr = "微信服务异常";
				}
				return ReturnUtil.returnMap(0, tipStr);
			}
		}else if("FAIL".equals(return_code)){
			String return_msg = result.get("return_msg")==null?"微信未返回错误原因":result.get("return_msg").toString();
			return ReturnUtil.returnMap(0, return_msg);
		}
		
		return ReturnUtil.returnMap(0, "服务器出错了");
	}
	/**
	 * APP支付
	 * @param appid
	 * @param partnerid
	 * @param prepayid
	 * @return
	 */
	public static SortedMap<String, String> wxPayRequest2App(String appid, String partnerid, String prepayid){
		SortedMap<String, String> params = new TreeMap<String, String>();
		params.put("appid",appid);
		params.put("partnerid",partnerid);
		params.put("noncestr",getNonceStr(15));
		params.put("prepayid", prepayid);// 微信返回的支付交易会话ID
		params.put("package", "Sign=WXPay");// 固定值
		params.put("timestamp", String.valueOf((int)(System.currentTimeMillis()/1000)));// 10位时间戳
		// 根据之前添加的参数进行签名运算并加入map
		params.put("sign", wxPaySign(params));
		return  params;
	}
	public static SortedMap<String, String> wxPayRequest2App(String prepayid){
		SortedMap<String, String> params = new TreeMap<String, String>();
		params.put("appid",CORPID);
		params.put("partnerid",MCH_ID);
		params.put("noncestr",getNonceStr(15));
		params.put("prepayid", prepayid);// 微信返回的支付交易会话ID
		params.put("package", "Sign=WXPay");// 固定值
		params.put("timestamp", String.valueOf((int)(System.currentTimeMillis()/1000)));// 10位时间戳
		// 根据之前添加的参数进行签名运算并加入map
		params.put("sign", wxPaySign(params));
		return  params;
	}
	
	/**
	 * 微信内网页SDK支付调用
	 * @param appid
	 * @param partnerid
	 * @param prepayid
	 * @return
	 */
	public static SortedMap<String, String> wxPayRequest2WxWebApp(String appid, String prepayid){
		SortedMap<String, String> params = new TreeMap<String, String>();
		params.put("appid",appid);
//		params.put("partnerid",partnerid);
		params.put("noncestr",getNonceStr(15));
//		params.put("prepayid", prepayid);// 微信返回的支付交易会话ID
		params.put("package", "prepay_id="+prepayid);// 固定值
		params.put("timestamp", String.valueOf((int)(System.currentTimeMillis()/1000)));// 10位时间戳
		// 根据之前添加的参数进行签名运算并加入map
		params.put("paySign", wxPaySign(params));
		params.put("signType", "MD5");
		return  params;
	}
	
	public static SortedMap<String, String> wxPayRequest2WxWebApp(String prepayid){
		SortedMap<String, String> params = new TreeMap<String, String>();
		params.put("appid",CORPID);
//		params.put("partnerid",MCH_ID);
		params.put("noncestr",getNonceStr(15));
//		params.put("prepayid", prepayid);// 微信返回的支付交易会话ID
		params.put("package", "prepay_id="+prepayid);// 固定值
		params.put("timestamp", String.valueOf((int)(System.currentTimeMillis()/1000)));// 10位时间戳
		// 根据之前添加的参数进行签名运算并加入map
		params.put("paySign", wxPaySign(params));
		params.put("signType", "MD5");
		return  params;
	}
	
	public static String wxPaySign(SortedMap<String, String> param){
    	StringBuffer sb = new StringBuffer("");
    	for (Map.Entry<String, String> entry : param.entrySet()) {
    	    String key = entry.getKey();
    	    Object val = entry.getValue();
    	    if(val instanceof String){
    	    	String v = (String) val;
        	    if(!v.isEmpty()){
        	    	sb.append(key);
        	    	sb.append("=");
        	    	sb.append(v);
        	    	sb.append("&");
        	    }
    	    }
    	}
    	sb.append("key="+KEY);
    	return MD5Encode(sb.toString());
	}
	
	
    /**
     * MD5编码
     * @param origin 原始字符串
     * @return 经过MD5加密之后的结果
     */
    public static String MD5Encode(String origin) {
    	String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7","8", "9", "a", "b", "c", "d", "e", "f"};
        StringBuffer result = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(origin.getBytes("UTF-8"));
            for(int b : array){
            	b = b<0?(256+b):b;
            	result.append(hex[b/16]);
            	result.append(hex[b%16]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    
    /**
     * 获取一定长度的随机字符串
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getNonceStr(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    
    /**
     * Map转XML(响应微信服务器)
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
	public static String map2xml(Map<String, Object> map){
		StringBuffer xmlString = new StringBuffer();
		for(Map.Entry<String, Object> item : map.entrySet()){
			String key = item.getKey();
			Object value = item.getValue();
			xmlString.append("<"+key+">");
			if(value instanceof String || value instanceof Long ||value instanceof Integer || value instanceof Double || value instanceof Boolean){
				xmlString.append("<![CDATA["+value+"]]>");
			}else if(value instanceof Map){
				xmlString.append(map2xml((Map<String, Object>)value));
			}else{
				xmlString.append("不支持的解析类型："+value.getClass().getName());
			}
			xmlString.append("</"+key+">");
		}
		return xmlString.toString();
	}
    
    /**
     * 解析微信发来的请求(XML转为Map)
     * @param request
     * @return
     */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> xml2map(String xmlStr){
		Map<String, Object> map  = new HashMap<String, Object>();
		// 从request中取得输入流  
        //InputStream inputStream = null;
		try {
			//inputStream = request.getInputStream();
        // 读取输入流  
        SAXReader reader = new SAXReader();  
        Document document = reader.read(new ByteArrayInputStream(xmlStr.getBytes("UTF-8")));  
        // 得到xml根元素  
        Element root = document.getRootElement();  
        // 得到根元素的所有子节点
		map = iteraElement(root.elements());
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return map;  
    }
	/**
	 * 解析微信发来的请求(XML转为Map)
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> xml2map(InputStream inputStream){
		Map<String, Object> map  = new HashMap<String, Object>();
		// 从request中取得输入流  
		//InputStream inputStream = null;
		try {
			//inputStream = request.getInputStream();
			// 读取输入流  
			SAXReader reader = new SAXReader();  
			Document document = reader.read(inputStream);  
			// 得到xml根元素  
			Element root = document.getRootElement();  
			// 得到根元素的所有子节点
			map = iteraElement(root.elements());
		} catch (DocumentException e) {
			e.printStackTrace();
		} 
		return map;  
	}
	
    /**
     * 遍历节点
     * @return
     */
    @SuppressWarnings("unchecked")
	public static Map<String, Object> iteraElement(List<Element> elements){
    	Map<String, Object> map = new HashMap<String, Object>();
    	for (Element e : elements){
        	if(e.isTextOnly()){
        		map.put(e.getName(), e.getText());
        	}else{
        		map.put(e.getName(), iteraElement(e.elements()));
        	}
        }
    	return map;
    }
	
    
    
	
}
