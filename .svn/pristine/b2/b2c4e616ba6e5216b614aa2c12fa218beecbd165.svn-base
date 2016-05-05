/**
 * 
 */
package xyz.model.core;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Administrator
 *
 */
@Entity
@Table(name="bill")
public class Bill implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//账单号，主键
	@Id
	@Column(name="bill_no",unique=true,nullable=false)
	private String billNo;
	
	//支付方式
	@Column(name="pay_type")
	private String payType;
	
	//商城订单号
	@Column(name="order_no")
	private String orderNo;
	
	//订单名称
	@Column(name="order_subject")
	private String orderSubject;
	
	//订单金额，单位分
	@Column(name="order_amount")
	private String orderAmount;
	
	//订单描述
	@Column(name="order_body")
	private String orderBody;
	
	
	//账单状态
	@Column(name="bill_status")
	private String billStatus;
	
	//账单创建时间
	@Column(name="bill_create_time")
	private Date billCreateTime;
	
	//账单支付时间
	@Column(name="bill_pay_time")
	private Date billPayTime;
	
	//支付宝通知时间
	@Column(name="notify_time")
	private Date notifyTime;
	
	//支付宝通知状态
	@Column(name="notify_status")
	private String notifyStatus;
	
	//支付宝通知内容
	@Column(name="notify_msg")
	private String notifyMsg;
	
	//支付宝交易号
	@Column(name="third_trade_no")
	private String thirdTradeNo;
	
	//商城地址
	@Column(name="back_url")
	private String backUrl;
	
	@Column(name="success_url")
	private String successUrl;
	
	@Column(name="fail_url")
	private String failUrl;
	
	//支付系统地址
	@Column(name="return_url")
	private String returnUrl;
	
	@Column(name="notify_url")
	private String notifyUrl;
	
	@Column(name="show_url")
	private String showUrl;

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderSubject() {
		return orderSubject;
	}

	public void setOrderSubject(String orderSubject) {
		this.orderSubject = orderSubject;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getOrderBody() {
		return orderBody;
	}

	public void setOrderBody(String orderBody) {
		this.orderBody = orderBody;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public Date getBillCreateTime() {
		return billCreateTime;
	}

	public void setBillCreateTime(Date billCreateTime) {
		this.billCreateTime = billCreateTime;
	}

	public Date getBillPayTime() {
		return billPayTime;
	}

	public void setBillPayTime(Date billPayTime) {
		this.billPayTime = billPayTime;
	}

	public Date getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(Date notifyTime) {
		this.notifyTime = notifyTime;
	}

	public String getNotifyStatus() {
		return notifyStatus;
	}

	public void setNotifyStatus(String notifyStatus) {
		this.notifyStatus = notifyStatus;
	}

	public String getNotifyMsg() {
		return notifyMsg;
	}

	public void setNotifyMsg(String notifyMsg) {
		this.notifyMsg = notifyMsg;
	}

	public String getThirdTradeNo() {
		return thirdTradeNo;
	}

	public void setThirdTradeNo(String thirdTradeNo) {
		this.thirdTradeNo = thirdTradeNo;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getFailUrl() {
		return failUrl;
	}

	public void setFailUrl(String failUrl) {
		this.failUrl = failUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getShowUrl() {
		return showUrl;
	}

	public void setShowUrl(String showUrl) {
		this.showUrl = showUrl;
	}
	
}
