package xyz.model.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 微信流水账单
 * @author Administrator
 * API字段说明
 * https://pay.weixin.qq.com/wiki/doc/api/app.php?chapter=9_1
 */
@Entity
@Table(name="wx_bill")
public class WxBill {
	//账单号，主键
	@Id
	@Column(name="bill_no",unique=true,nullable=false)
	private String billNo;//自定义字段 订单号
	
	@Column(name="bill_status")
	private int billStatus;//自定义字段 订单状态
	
	@Column(name="bill_create_time")
	private Date billCreateTime;//自定义字段 订单创建时间
	
	@Column(name="order_no")
	private String orderNo;//自定义字段 B2B商城订单号
	
	@Column(name="appid")
	private String appid;// 必填	String	wxd678efh567hg6787	微信分配的公众账号ID（企业号corpid即为此appId）
	
	@Column(name="mch_id")
	private String mch_id;//必填 String(32)	1230000109	微信支付分配的商户号
	
	@Column(name="device_info")
	private String device_info;//非必填 String(32)	013467007045764	终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
	
	@Column(name="nonce_str")
	private String  nonce_str;//必填 String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位。推荐随机数生成算法
	
	@Column(name="sign")
	private String sign;//必填 String(32)	C380BEC2BFD727A4B6845133519F3AD6	签名，详见签名生成算法
	
	@Column(name="body")
	private String body;//必填 String(128)	Ipad mini  16G  白色	商品或支付单简要描述
	
	@Column(name="detail")
	private String detail;//非必填 String(8192)	Ipad mini  16G  白色	商品名称明细列表
	
	@Column(name="attach")
	private String attach;//非必填 String(127)	深圳分店	附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
	
	@Column(name="out_trade_no")
	private String out_trade_no;//必填 String(32)	20150806125346	商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
	
	@Column(name="fee_type")
	private String fee_type;//非必填 String(16)	CNY	符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	
	@Column(name="total_fee")
	private int total_fee;//必填  Int	888	订单总金额，单位为分，详见支付金额
	
	@Column(name="spbill_create_ip")
	private String spbill_create_ip;//必填 String(16)	123.12.12.123	APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
	
	@Column(name="time_start")
	private String time_start;//非必填 String(14)	20091225091010	订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
	
	@Column(name="time_expire")
	private String time_expire;//非必填 String(14)	20091227091010 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则 注意：最短失效时间间隔必须大于5分钟
	
	@Column(name="goods_tag")
	private String goods_tag;//非必填 String(32)	WXG	商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
	
	@Column(name="notify_url")
	private String notify_url;//必填 String(256)	http://www.weixin.qq.com/wxpay/pay.php	接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
	
	@Column(name="trade_type")
	private String trade_type;//必填 String(16)	JSAPI	取值如下：JSAPI，NATIVE，APP，详细说明见参数规定
	
	@Column(name="product_id")
	private String product_id;//非必填 String(32)	12235413214070356458058	trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
	
	@Column(name="limit_pay")
	private String limit_pay;//非必填 String(32)	no_credit	no_credit--指定不能使用信用卡支付
	
	@Column(name="openid")
	private String openid;//非必填 String(128)	oUpF8uMuAJO_M2pxb1Q9zNjWeS6o	trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。企业号请使用【企业号OAuth2.0接口】获取企业号内成员userid，再调用【企业号userid转openid接口】进行转换

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public int getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(int billStatus) {
		this.billStatus = billStatus;
	}

	public Date getBillCreateTime() {
		return billCreateTime;
	}

	public void setBillCreateTime(Date billCreateTime) {
		this.billCreateTime = billCreateTime;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTime_start() {
		return time_start;
	}

	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_expire() {
		return time_expire;
	}

	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}

	public String getGoods_tag() {
		return goods_tag;
	}

	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getLimit_pay() {
		return limit_pay;
	}

	public void setLimit_pay(String limit_pay) {
		this.limit_pay = limit_pay;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

}
