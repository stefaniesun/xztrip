﻿
package xyz.model.main;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="provider")
public class Provider {
	@Id
	@Column(name="iidd",unique=true,nullable=false)
	@GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	private String iidd;//主键
	
	@Column(name="number_code",unique=true,nullable=false)
	private String numberCode;//编号
	
	@Column(name="name_cn")
	private String nameCn;//名称
	
	@Column(name="is_scenic")
	private int isScenic;//景点标识
	
	@Column(name="is_hotel")
	private int isHotel;//酒店标识
	
	@Column(name="is_car")
	private int isCar;//租车标识
	
	@Column(name="is_specialty")
	private int isSpecialty;//土特产标识
	
	@Column(name="level")
	private String level;//级别
	
	@Column(name="scenic_price")
	private BigDecimal scenicPrice;//当天景点参考价格
	
	@Column(name="hotel_price")
	private BigDecimal hotelPrice;//当天酒店参考价格
	
	@Column(name="car_price")
	private BigDecimal carPrice;//当天租车参考价格
	
	@Column(name="specialty_price")
	private BigDecimal specialtyPrice;//当天土特产参考价格
	
	@Column(name="level_system")
	private String levelSystem;//级别
	
	@Column(name="phone")
	private String phone;//电话
	
	@Column(name="qq")
	private String qq;//腾讯账号
	
	@Column(name="email")
	private String email;//邮箱
	
	@Column(name="linkman")
	private String linkman;//联系人
	
	@Column(name="scenic_total_sale")
	private int scenicTotalSale;
	
	@Column(name="scenic_month_sale")
	private int scenicMonthSale;
	
	
	@Column(name="hotel_total_sale")
	private int hotelTotalSale;
	
	@Column(name="hotel_month_sale")
	private int hotelMonthSale;
	
	@Column(name="car_total_sale")
	private int carTotalSale;
	
	@Column(name="car_month_sale")
	private int carMonthSale;
	
	@Column(name="specialty_total_sale")
	private int specialtyTotalSale;
	
	@Column(name="specialty_month_sale")
	private int specialtyMonthSale;
	
	@Column(name="address")
	private String address;//地址

	@Column(name="remark",length=5000)
	private String remark;
	
	@Column(name="image_url")
	private String imageUrl;
	
	@Column(name="online_flag")
	private int onlineFlag;
	
	@Column(name="add_date")
	private Date addDate;
	
	@Column(name="alter_date")
	private Date alterDate;
	
	@Column(name="longitude_latitude")
	private String  longitudeLatitude;//经纬度
	
	public String getIidd() {
		return iidd;
	}

	public void setIidd(String iidd) {
		this.iidd = iidd;
	}

	public String getNumberCode() {
		return numberCode;
	}

	public void setNumberCode(String numberCode) {
		this.numberCode = numberCode;
	}

	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}



	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLevelSystem() {
		return levelSystem;
	}

	public void setLevelSystem(String levelSystem) {
		this.levelSystem = levelSystem;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public int getIsCar() {
		return isCar;
	}

	public void setIsCar(int isCar) {
		this.isCar = isCar;
	}

	public int getIsScenic() {
		return isScenic;
	}

	public void setIsScenic(int isScenic) {
		this.isScenic = isScenic;
	}

	public int getIsHotel() {
		return isHotel;
	}

	public void setIsHotel(int isHotel) {
		this.isHotel = isHotel;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getOnlineFlag() {
		return onlineFlag;
	}

	public Date getAlterDate() {
		return alterDate;
	}

	public void setAlterDate(Date alterDate) {
		this.alterDate = alterDate;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public void setOnlineFlag(int onlineFlag) {
		this.onlineFlag = onlineFlag;
	}

	public String getLongitudeLatitude() {
		return longitudeLatitude;
	}

	public void setLongitudeLatitude(String longitudeLatitude) {
		this.longitudeLatitude = longitudeLatitude;
	}

	public BigDecimal getScenicPrice() {
		return scenicPrice;
	}

	public void setScenicPrice(BigDecimal scenicPrice) {
		this.scenicPrice = scenicPrice;
	}

	public BigDecimal getHotelPrice() {
		return hotelPrice;
	}

	public void setHotelPrice(BigDecimal hotelPrice) {
		this.hotelPrice = hotelPrice;
	}

	public BigDecimal getCarPrice() {
		return carPrice;
	}

	public void setCarPrice(BigDecimal carPrice) {
		this.carPrice = carPrice;
	}

	public int getScenicTotalSale() {
		return scenicTotalSale;
	}

	public void setScenicTotalSale(int scenicTotalSale) {
		this.scenicTotalSale = scenicTotalSale;
	}

	public int getScenicMonthSale() {
		return scenicMonthSale;
	}

	public void setScenicMonthSale(int scenicMonthSale) {
		this.scenicMonthSale = scenicMonthSale;
	}

	public int getHotelTotalSale() {
		return hotelTotalSale;
	}

	public void setHotelTotalSale(int hotelTotalSale) {
		this.hotelTotalSale = hotelTotalSale;
	}

	public int getHotelMonthSale() {
		return hotelMonthSale;
	}

	public void setHotelMonthSale(int hotelMonthSale) {
		this.hotelMonthSale = hotelMonthSale;
	}

	public int getCarTotalSale() {
		return carTotalSale;
	}

	public void setCarTotalSale(int carTotalSale) {
		this.carTotalSale = carTotalSale;
	}

	public int getCarMonthSale() {
		return carMonthSale;
	}

	public void setCarMonthSale(int carMonthSale) {
		this.carMonthSale = carMonthSale;
	}

	public BigDecimal getSpecialtyPrice() {
		return specialtyPrice;
	}

	public void setSpecialtyPrice(BigDecimal specialtyPrice) {
		this.specialtyPrice = specialtyPrice;
	}

	public int getSpecialtyTotalSale() {
		return specialtyTotalSale;
	}

	public void setSpecialtyTotalSale(int specialtyTotalSale) {
		this.specialtyTotalSale = specialtyTotalSale;
	}

	public int getSpecialtyMonthSale() {
		return specialtyMonthSale;
	}

	public void setSpecialtyMonthSale(int specialtyMonthSale) {
		this.specialtyMonthSale = specialtyMonthSale;
	}

	public int getIsSpecialty() {
		return isSpecialty;
	}

	public void setIsSpecialty(int isSpecialty) {
		this.isSpecialty = isSpecialty;
	}

	
}
