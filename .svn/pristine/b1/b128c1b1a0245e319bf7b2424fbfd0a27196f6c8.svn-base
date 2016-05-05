package xyz.model.member;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="money_flow")
public class MoneyFlow {
	//账单类型
	public static final int TYPE_BUSSINESS_INPOUR=0;//(用户充值)
	public static final int TYPE_BUSSINESS_WITHDRAW=1;//(用户提现)
	public static final int TYPE_ADMIN_INPOUR=2;//(管理员充值)
	public static final int TYPE_ORDER_PAY=3;//(订单付款)
	public static final int TYPE_ORDER_REFUND=4;//(订单退款)

	@Id
	@Column(name="iidd",unique=true,nullable=false)
	@GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "identity")
	private int iidd;//
	
	@Column(name="number_code",unique=true,nullable=false)
	private String numberCode;//主键
	
	@Column(name="account")
	private String account;//账户名
	
	@Column(name="balance")
	private BigDecimal balance;//用户交易发生之前账户余额
	
	@Column(name="in_amount")
	private BigDecimal inAmount;//收入金额
	
	@Column(name="out_amount")
	private BigDecimal outAmount;//支出金额
	
	@Column(name="type")
	private int type;//交易类型
	
	@Column(name="add_date")
	private Date addDate;
	
	@Column(name="alter_date")
	private Date alterDate;
	
	@Column(name="remark",length=5000)
	private String remark;//

	public int getIidd() {
		return iidd;
	}

	public void setIidd(int iidd) {
		this.iidd = iidd;
	}

	public String getNumberCode() {
		return numberCode;
	}

	public void setNumberCode(String numberCode) {
		this.numberCode = numberCode;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getInAmount() {
		return inAmount;
	}

	public void setInAmount(BigDecimal inAmount) {
		this.inAmount = inAmount;
	}

	public BigDecimal getOutAmount() {
		return outAmount;
	}

	public void setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public static int getTypeBussinessInpour() {
		return TYPE_BUSSINESS_INPOUR;
	}

	public static int getTypeBussinessWithdraw() {
		return TYPE_BUSSINESS_WITHDRAW;
	}

	public static int getTypeAdminInpour() {
		return TYPE_ADMIN_INPOUR;
	}

	public static int getTypeOrderPay() {
		return TYPE_ORDER_PAY;
	}

	public static int getTypeOrderRefund() {
		return TYPE_ORDER_REFUND;
	}


}
