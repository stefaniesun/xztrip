package xyz.svc.buyer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface BuyerOrderSvc {

	public Map<String, Object> createOrder(String product, int count,
			Date dateInfo, int day,String remarkBuy,String linkman,String linkPhone);

	public Map<String, Object> queryOrderList(int flagPay,int offset, int pagesize);

	public Map<String, Object> getOrder(String orderNum);

	public Map<String, Object> deleteOrder(String orderNum);

	public Map<String, Object> editOrder(String orderNum, String linkman,
			String linkPhone);

	public Map<String, Object> editOrderPersonInfo(String orderNum, String personInfo);

	public Map<String, Object> holdStockOper(String orderNum);
	
	public Map<String, Object> rollbackStockOper(String orderNum);
	
	public Map<String, Object> paySuccessOper();
	
	public Map<String, Object> paySuccessOper(String orderNum);

	public Map<String, Object> createPmsOrder(String orderNum);
	
	public Map<String, Object> cancelPmsOrder(String orderNum);
	
	public Map<String, Object> modifyOrderPayStatus(String orderNum);
	
	/**
	 * 验证订单（支付前的最后验证）
	 * @param orderNum
	 * @return
	 */
	public Map<String, Object> decideOrder(String orderNum, BigDecimal money);
}
