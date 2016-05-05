package xyz.model.wechat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
/**
 * 微信用户基本信息
 * @author Administrator
 *
 *字段详细解释参见：
 *http://mp.weixin.qq.com/wiki/1/8a5ce6257f1d3b2afb20f83e72b72ce9.html
 *
 */
@Entity
@Table(name="weixin_user_info")
public class WeixinUserInfo {
	@Id
	@Column(name="iidd",unique=true,nullable=false)
	@GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	public String iidd;
	
	@Column(name="openid",unique=true,nullable=false)
	private String openid;
	
	@Column(name="openid_append",length=10000)
	private String openidAppend;
	
	@Column(name="customer")
	private String customer;
	
	@Column(name="link_phone")	//联系电话
	private String linkPhone;

	public String getIidd() {
		return iidd;
	}

	public void setIidd(String iidd) {
		this.iidd = iidd;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOpenidAppend() {
		return openidAppend;
	}

	public void setOpenidAppend(String openidAppend) {
		this.openidAppend = openidAppend;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
}
