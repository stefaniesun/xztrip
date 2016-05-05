/**
 * 
 */
package com.alipay.web.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 *
 */
public class Bill implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//账单号，主键
	private String bill_no;
	//支付方式
	private String pay_type;
	
	//商城订单号
	private String order_no;
	//订单名称
	private String order_subject;
	//订单金额，单位分
	private String order_amount;
	//订单描述
	private String order_body;
	
	
	//账单状态
	private String bill_status;
	//账单创建时间
	private Date bill_create_time;
	//账单支付时间
	private Date bill_pay_time;
	
	//支付宝通知时间
	private Date notify_time;
	//支付宝通知状态
	private String notify_status;
	//支付宝通知内容
	private String notify_msg;
	//支付宝交易号
	private String third_trade_no;
	
	//商城地址
	private String back_url;
	private String success_url;
	private String fail_url;
	
	//支付系统地址
	private String return_url;
	private String notify_url;
	private String show_url;
	
	public String getBill_no() {
		return bill_no;
	}
	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}
	
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getOrder_subject() {
		return order_subject;
	}
	public void setOrder_subject(String order_subject) {
		this.order_subject = order_subject;
	}
	public String getOrder_amount() {
		return order_amount;
	}
	public void setOrder_amount(String order_amount) {
		this.order_amount = order_amount;
	}
	public String getOrder_body() {
		return order_body;
	}
	public void setOrder_body(String order_body) {
		this.order_body = order_body;
	}
	public String getBill_status() {
		return bill_status;
	}
	public void setBill_status(String bill_status) {
		this.bill_status = bill_status;
	}
	public Date getBill_create_time() {
		return bill_create_time;
	}
	public void setBill_create_time(Date bill_create_time) {
		this.bill_create_time = bill_create_time;
	}
	public Date getBill_pay_time() {
		return bill_pay_time;
	}
	public void setBill_pay_time(Date bill_pay_time) {
		this.bill_pay_time = bill_pay_time;
	}
	
	public Date getNotify_time() {
		return notify_time;
	}
	public void setNotify_time(Date notify_time) {
		this.notify_time = notify_time;
	}
	public String getNotify_status() {
		return notify_status;
	}
	public void setNotify_status(String notify_status) {
		this.notify_status = notify_status;
	}
	public String getNotify_msg() {
		return notify_msg;
	}
	public void setNotify_msg(String notify_msg) {
		this.notify_msg = notify_msg;
	}
	public String getBack_url() {
		return back_url;
	}
	public void setBack_url(String back_url) {
		this.back_url = back_url;
	}
	public String getReturn_url() {
		return return_url;
	}
	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getShow_url() {
		return show_url;
	}
	public void setShow_url(String show_url) {
		this.show_url = show_url;
	}
	public String getThird_trade_no() {
		return third_trade_no;
	}
	public void setThird_trade_no(String third_trade_no) {
		this.third_trade_no = third_trade_no;
	}
	public String getSuccess_url() {
		return success_url;
	}
	public void setSuccess_url(String success_url) {
		this.success_url = success_url;
	}
	public String getFail_url() {
		return fail_url;
	}
	public void setFail_url(String fail_url) {
		this.fail_url = fail_url;
	}
	
	
}
