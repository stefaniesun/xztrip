package xyz.model.wechat;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 遇险求助
 * @author Administrator
 *
 */
@Entity
@Table(name="seek_help")
public class SeekHelp {
	@Id
	@Column(name="iidd",unique=true,nullable=false)
	@GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	private String iidd;//主键
	
	@Column(name="number_code",unique=true,nullable=false)
	private String numberCode;//主键
	
	@Column(name="openid")
	private String openid;//
	
	@Column(name="location_x")
	private String locationX;
	
	@Column(name="location_y")
	private String locationY;
	
	@Column(name="scale")
	private String scale;
	
	@Column(name="label")
	private String label;
	
	@Column(name="add_date")
	private Date addDate;
	
	@Column(name="alter_date")
	private Date alterDate;
	
	@Column(name="status")
	private int status;//处理状态 0未处理 1已处理
	
	@Column(name="help_type")
	private String helpType;	//求救类型
	
	@Column(name="help_time")
	private String helpTime;//发起求救时间
	
	@Transient
	private String openidAppend;//点好号码实时查询的时候赋进来，因为用户可能发起求助后会修改联系电话
	
	@Transient
	private String phone;//点好号码实时查询的时候赋进来，因为用户可能发起求助后会修改联系电话

	public String getHelpType() {
		return helpType;
	}

	public void setHelpType(String helpType) {
		this.helpType = helpType;
	}

	public String getHelpTime() {
		return helpTime;
	}

	public void setHelpTime(String helpTime) {
		this.helpTime = helpTime;
	}

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

	public String getLocationX() {
		return locationX;
	}

	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}

	public String getLocationY() {
		return locationY;
	}

	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public Date getAlterDate() {
		return alterDate;
	}

	public void setAlterDate(Date alterDate) {
		this.alterDate = alterDate;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getNumberCode() {
		return numberCode;
	}

	public void setNumberCode(String numberCode) {
		this.numberCode = numberCode;
	}
	
}
