package xyz.ctrl.pay;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.filter.JSON;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.model.base.ExceptionLog;
import xyz.model.core.Bill;
import xyz.model.core.WxBill;
import xyz.svc.buyer.BuyerOrderSvc;
import xyz.svc.core.ExceptionLogSvc;
import xyz.svc.pay.PaySvc;
import xyz.svc.pay.imp.AliPayUtil;
import xyz.svc.pay.imp.WxJsPayUtil;
import xyz.svc.pay.imp.WxPayUtil;

@Controller
@RequestMapping(value="/PayWS")
public class PayWS {
	@Autowired
	private PaySvc paySvc;
	@Autowired
	private BuyerOrderSvc buyerOrderSvc;
	@Autowired
	private ExceptionLogSvc exceptionLogSvc;
	
	/**
	 * 获得微信支付订单信息
	 * @return
	 */
	@RequestMapping(value="getWxPayOrderInfo")
	@ResponseBody
	public Map<String, Object> getWxPayOrderInfo(String orderNo, BigDecimal totalFee, String body){
		if(totalFee==null || totalFee.compareTo(new BigDecimal("0"))<=0){
			return ReturnUtil.returnMap(0, "金额不能少于一分");
		}
		if(orderNo==null || "".equals(orderNo)){
			return ReturnUtil.returnMap(0, "缺少参数");
		}
		WxBill bill = new WxBill();
		bill.setOrderNo(orderNo);
		bill.setTotal_fee(totalFee.multiply(new BigDecimal("100")).intValue());//乘以100转成整型
		bill.setBody(body);
		bill.setSpbill_create_ip(MyRequestUtil.getIp());
		
		return paySvc.addWxBill(bill);
	}
	
	@RequestMapping(value="registerWxJs")
	@ResponseBody
	public Map<String, Object> registerWxJs(){
		return ReturnUtil.returnMap(1, WxJsPayUtil.registerJs());
	}
	
	@RequestMapping(value="getWxJsPayOrderInfo")
	@ResponseBody
	public Map<String, Object> getWxJsPayOrderInfo(String orderNo, BigDecimal totalFee, String body){
		if(totalFee==null || totalFee.compareTo(new BigDecimal("0"))<=0){
			return ReturnUtil.returnMap(0, "金额不能少于一分");
		}
		if(orderNo==null || "".equals(orderNo)){
			return ReturnUtil.returnMap(0, "缺少参数");
		}
		WxBill bill = new WxBill();
		bill.setOrderNo(orderNo);
		bill.setTotal_fee(totalFee.multiply(new BigDecimal("100")).intValue());//乘以100转成整型
		bill.setBody(body);
		bill.setSpbill_create_ip(MyRequestUtil.getIp());
		
		return paySvc.addWxBillByJs(bill);
	}
	
	@RequestMapping(value="getAliPayOrderInfo")
	@ResponseBody
	public Map<String, Object> getAliPayOrderInfo(String orderNo, BigDecimal totalFee, String body){
		if(totalFee==null || totalFee.compareTo(new BigDecimal("0"))<=0){
			return ReturnUtil.returnMap(0, "金额不能少于一分");
		}
		if(orderNo==null || "".equals(orderNo)){
			return ReturnUtil.returnMap(0, "缺少参数");
		}
		Bill bill = new Bill();
		bill.setOrderNo(orderNo);
		bill.setOrderAmount(totalFee.toString());
		bill.setOrderBody(body);
		bill.setOrderSubject(body);
		return paySvc.addAliBill(bill);
	}
	
	@RequestMapping(value="wxPayNotify")
	@ResponseBody
	public void wxPayNotify(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");  
		response.setCharacterEncoding("UTF-8");	//防止中文乱码
		Map<String, Object> map = WxPayUtil.xml2map(request.getInputStream());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("===============微信支付回调成功【"+format.format(new Date())+"】==============");
		System.out.println(JSON.toJosn(map));
		System.out.println("===============微信支付回调成功【END】==============");
		
		Map<String, Object> returnContent = new HashMap<String, Object>();
		returnContent.put("return_msg", "OK");
		
		//验证签名，确保消息来自微信服务器
		String sign = map.get("sign")==null?"":map.get("sign").toString();
		map.remove("sign");
		//按字母顺序排列各参数
		SortedMap< String, String> sortedMap = new TreeMap<String, String>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
    	    String key = entry.getKey();
    	    String val = entry.getValue()==null?"":entry.getValue().toString();
    	    sortedMap.put(key, val);
    	}
		String mySign = WxPayUtil.wxPaySign(sortedMap).toUpperCase();
		System.out.println("result sign="+sign);
		System.out.println("my sign="+mySign);
		if(!sign.equals(mySign)){
			System.out.println("验证不通过，消息不是来自微信服务器。");
			return ;
		}
		
		Map<String, Object> resultMap = paySvc.wxPayCallbackOper(map);
		if(resultMap.get("status")!=null && 1==Integer.parseInt(resultMap.get("status").toString())){
			WxBill bill = (WxBill)resultMap.get("content");
			String orderNum = bill.getOrderNo();
			
			//BigDecimal money = new BigDecimal(wxBill.getTotal_fee()).divide(new BigDecimal("100.00"));
			buyerOrderSvc.paySuccessOper(orderNum);//通知处理支付成功的订单

			returnContent.put("return_code", "SUCCESS");//失败返回  FAIL
		}else{
			returnContent.put("return_code", "FAIL");//失败返回  FAIL
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("xml", returnContent);
		
		String outxml = WxPayUtil.map2xml(returnMap);
		System.out.println(outxml);
		
		PrintWriter out = response.getWriter();
		out.write(outxml);
		out.flush();
		out.close();
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="alipayNotify")
	@ResponseBody
	public void alipayNotify(HttpServletRequest request,HttpServletResponse response) throws IOException{
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		
		System.out.println("====================alipay回调start================");
		System.out.println(JSON.toJosn(params));
		System.out.println("====================alipay回调end================");
		
//		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//支付宝交易号
//		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//交易状态
//		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
		String notify_id = params.get("notify_id");
		String out_trade_no = params.get("out_trade_no");
		String trade_status = params.get("trade_status");
		String total_fee = params.get("total_fee");
		if("WAIT_BUYER_PAY".equals(trade_status)){
			response.getWriter().print(AliPayUtil.alipayNotifyValidate(notify_id)?"success":"fail");
			return;
		}
		if("TRADE_SUCCESS".equals(trade_status)){
			Map<String, Object> resultMap = paySvc.aliPayCallbackOper(total_fee, out_trade_no);
			if(resultMap.get("status")!=null && 1==Integer.parseInt(resultMap.get("status").toString())){
				Bill bill = (Bill)resultMap.get("content");
				String orderNum = bill.getOrderNo();
				buyerOrderSvc.paySuccessOper(orderNum);//通知处理支付成功的订单
				
				//BigDecimal money = new BigDecimal(wxBill.getTotal_fee()).divide(new BigDecimal("100.00"));
				response.getWriter().print("success");
			}
		}
		response.getWriter().print("fail");

	}
}
