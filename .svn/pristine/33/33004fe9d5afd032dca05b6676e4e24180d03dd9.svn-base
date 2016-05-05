package com.alipay.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xyz.filter.JSON;

import com.alipay.web.model.Bill;
import com.alipay.web.service.PayService;

/**
 * Servlet implementation class AlipayServlet
 */
public class AlipayServlet extends ResourceServlet {
	private static final long serialVersionUID = 1L;

	private PayService payService = PayService.getInstance();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AlipayServlet() {
		super("support/http/resources");
	}

	@Override
	protected String process(String url, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		Map<String, String> params = getParameters(url);
		// 进入交易trade页面后，加载数据
		if (url.startsWith("/trade.json")) {
			String bill_no = params.get("bill_no");
			Bill bill = payService.viewBill(bill_no);
			Map<String, Object> resultMap = new HashMap<String, Object>(3);
			if (null != bill) {
				resultMap.put("order_subject", bill.getOrder_subject());
				resultMap.put("order_amount", bill.getOrder_amount());
				resultMap.put("back_url", bill.getBack_url());
			}
			return returnJSONResult(1, resultMap);
		}

		// 点击支付后，获取支付宝form参数
		if (url.startsWith("/pay.json")) {
			String bill_no = params.get("bill_no");
			String alipay = payService.getAlipayForm(bill_no);

			System.out.println(JSON.toJosn(alipay));
			return returnJSONResult(1, alipay);
		}

		// 异步通知
		if (url.startsWith("/notify.json")) {
			return payService.doNotify(request);
		}

		// 同步通知
		if (url.startsWith("/return.json")) {
			try {
				response.sendRedirect(payService.doReturn(request));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 查询订单
		if (url.startsWith("/query.json")) {
			String bill_no = request.getParameter("bill_no");
			if (null == bill_no || bill_no.trim().equals("")) {
				return returnJSONResult(0, "参数错误");
			}

			Bill bill = payService.viewBill(bill_no);
			if (null == bill) {
				return returnJSONResult(0, "未查询到该订单");
			}
			Map<String, Object> resultMap = new HashMap<String, Object>(5);
			resultMap.put("bill_no", bill.getBill_no());
			resultMap.put("bill_status", bill.getBill_status());
			resultMap.put("pay_time", bill.getBill_pay_time());
			resultMap.put("notify_status", bill.getNotify_status());
			resultMap.put("notify_msg", bill.getNotify_msg());

			return returnJSONResult(1, resultMap);
		}
		
		if (url.startsWith("/alipayMobile.json")) {
	        String contextPath = request.getContextPath();
	        String servletPath = request.getServletPath();
	        String requestURI = request.getRequestURI();
	        String requestURL = request.getRequestURL().toString();
	        
//	        String path = requestURI.substring(contextPath.length() + servletPath.length());
	        String urlPath = requestURL.substring(0,requestURL.length()-requestURI.length())+requestURI;
        	/**
        	 * 读取请求参数，生成账单
        	 */
        	//账单号
        	String bill_no = String.valueOf(System.currentTimeMillis());
    		// 服务器异步通知页面路径
    		String notify_url = urlPath+"/notify.json";
    		// 页面跳转同步通知页面路径
    		String return_url = urlPath+"/return.json";
    		
    		//返回商城地址
    		String back_url = request.getParameter("back_url");
    		//返回成功地址
    		String success_url = request.getParameter("success_url");
    		//返回失败地址
    		String fail_url = request.getParameter("fail_url");

    		// 商户订单号
    		String order_no = request
    				.getParameter("order_no");

    		// 订单名称
    		String order_subject = request.getParameter("order_subject");
    		// 必填

    		// 付款金额
    		String order_amount = request.getParameter("order_amount");
    		// 必填

    		// 支付确认页面地址
    		String show_url = urlPath+"/trade.html?bill_no="+bill_no;
    		// 必填，需以http://开头的完整路径，例如：http://www.商户网址.com/myorder.html

    		// 订单描述
    		String order_body = request.getParameter("order_body");
    		// 选填

    		if(order_amount==null||"".equals(order_amount)){
    			return returnJSONResult(0, "缺少参数");
    		}
    		if(order_subject==null||"".equals(order_subject)){
    			return returnJSONResult(0, "缺少参数");
    		}
    		if(order_no==null||"".equals(order_no)){
    			return returnJSONResult(0, "缺少参数");
    		}
    		if(order_body==null||"".equals(order_body)){
    			return returnJSONResult(0, "缺少参数");
    		}
    		
    		Bill bill = new Bill();
        	//账单号
        	bill.setBill_no(bill_no);
        	//支付类型
        	bill.setPay_type("01");
        	bill.setOrder_no(order_no);
        	bill.setOrder_amount(order_amount);
        	bill.setOrder_subject(order_subject);
        	bill.setOrder_body(order_body);
        	bill.setBill_status("0");
        	bill.setBill_create_time(new Date());
        	bill.setBack_url(back_url);
        	bill.setReturn_url(return_url);
        	bill.setNotify_url(notify_url);
        	bill.setShow_url(show_url);
        	bill.setSuccess_url(success_url);
        	bill.setFail_url(fail_url);
        	
        	payService.createBill(bill);
        	
        	String alipayMobileStr = payService.getAlipayMobile(bill_no);
        	System.out.println("================alipayMobileStr================");
        	System.out.println(alipayMobileStr);
        	System.out.println("================alipayMobileStr================");
        	
        	return returnJSONResult(1, alipayMobileStr);
		}

		return null;
	}

	public static String returnJSONResult(int resultCode, Object content) {
		Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
		dataMap.put("ResultCode", resultCode);
		dataMap.put("Content", content);
		return com.alipay.util.json.JSONUtils.toJSONString(dataMap);
	}

	public static Map<String, String> getParameters(String url) {
		if (url == null || (url = url.trim()).length() == 0) {
			return Collections.<String, String> emptyMap();
		}

		String parametersStr = com.alipay.util.StringUtils.subString(url, "?",
				null);
		if (parametersStr == null || parametersStr.length() == 0) {
			return Collections.<String, String> emptyMap();
		}

		String[] parametersArray = parametersStr.split("&");
		Map<String, String> parameters = new LinkedHashMap<String, String>();

		for (String parameterStr : parametersArray) {
			int index = parameterStr.indexOf("=");
			if (index <= 0) {
				continue;
			}

			String name = parameterStr.substring(0, index);
			String value = parameterStr.substring(index + 1);
			parameters.put(name, value);
		}
		return parameters;
	}

}
