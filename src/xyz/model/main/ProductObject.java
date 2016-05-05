package xyz.model.main;

import java.math.BigDecimal;

public class ProductObject {

	private String provider;
	
	private String providerNameCn;
	
	private String numberCode;
	
	private String nameCn;
	
	private int isPms;
	
	private BigDecimal price;
	
	private int isTag;
	
	private String pmsRoomType;

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
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

	public String getProviderNameCn() {
		return providerNameCn;
	}

	public void setProviderNameCn(String providerNameCn) {
		this.providerNameCn = providerNameCn;
	}

	public int getIsPms() {
		return isPms;
	}

	public void setIsPms(int isPms) {
		this.isPms = isPms;
	}

	public String getPmsRoomType() {
		return pmsRoomType;
	}

	public void setPmsRoomType(String pmsRoomType) {
		this.pmsRoomType = pmsRoomType;
	}

	public int getIsTag() {
		return isTag;
	}

	public void setIsTag(int isTag) {
		this.isTag = isTag;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
