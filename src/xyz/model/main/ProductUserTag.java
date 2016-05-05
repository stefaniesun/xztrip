package xyz.model.main;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="product_user_tag")
public class ProductUserTag {
	
	@Id
	@Column(name="iidd",unique=true,nullable=false)
	@GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	private String iidd;//
	
	@Column(name="number_code",unique=true,nullable=false)
	private String numberCode;//
	
	@Column(name="product")
	private String product;
	
	@Column(name="name_cn")
	private String nameCn;
	
	@Column(name="user_tag")
	private String userTag;
	
	@Transient
	private String userTagNameCn;

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

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

	public String getUserTag() {
		return userTag;
	}

	public void setUserTag(String userTag) {
		this.userTag = userTag;
	}

	public String getUserTagNameCn() {
		return userTagNameCn;
	}

	public void setUserTagNameCn(String userTagNameCn) {
		this.userTagNameCn = userTagNameCn;
	}
}
