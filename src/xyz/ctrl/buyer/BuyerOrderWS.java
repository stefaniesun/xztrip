package xyz.ctrl.buyer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.svc.buyer.BuyerOrderSvc;


@Controller
@RequestMapping(value="/BuyerOrderWS")
public class BuyerOrderWS {

	@Autowired
	BuyerOrderSvc buyerOrderSvc;
	
	/**
	 * 生成订单接口
	 */
	@RequestMapping(value="createOrder")
	@ResponseBody
	public Map<String,Object> createOrder(
			String product,
			int count,
			Date dateInfo,
			int day,
			String remarkBuy,
			String linkman,
			String linkPhone){
		return buyerOrderSvc.createOrder(product, count,dateInfo,day,remarkBuy,linkman,linkPhone);
	}
	
	/**
	 * 查询订单接口
	 */
	@RequestMapping(value="queryOrderList")
	@ResponseBody
	public Map<String,Object> queryOrderList(
			int flagPay,
			int page,
			int rows){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return buyerOrderSvc.queryOrderList(flagPay,offset,pagesize);
	}
	
	/**
	 * 查询订单详情
	 */
	@RequestMapping(value="getOrder")
	@ResponseBody
	public Map<String,Object> getOrder(String orderNum){
		return buyerOrderSvc.getOrder(orderNum);
	}
	
	/**
	 * 删除订单
	 */
	@RequestMapping(value="deleteOrder")
	@ResponseBody
	public Map<String,Object> deleteOrder(String orderNum){
		return buyerOrderSvc.deleteOrder(orderNum);
	}
	
	/**
	 * 修改订单
	 */
	@RequestMapping(value="editOrder")
	@ResponseBody
	public Map<String,Object> editOrder(String orderNum,String linkman,String linkPhone){
		return buyerOrderSvc.editOrder(orderNum,linkman,linkPhone);
	}
	
	
	/**
	 * 修改订单出行人信息
	 */
	@RequestMapping(value="editOrderPersonInfo")
	@ResponseBody
	public Map<String,Object> editOrderPersonInfo(String orderNum,String personInfo){
		return buyerOrderSvc.editOrderPersonInfo(orderNum,personInfo);
	}
	
	/**
	 * 支付前验证金额并锁定库存
	 * @param orderNum
	 * @return
	 */
	@RequestMapping(value="holdStockOper")
	@ResponseBody
	public Map<String, Object> holdStockOper(String orderNum, BigDecimal money){
		Map<String, Object> map = buyerOrderSvc.decideOrder(orderNum, money);
		if(Integer.parseInt(map.get("status").toString())==1){
			return buyerOrderSvc.holdStockOper(orderNum);
		}
		return map;
	}
	
	@RequestMapping(value="decideOrder")
	@ResponseBody
	public Map<String, Object> decideOrder(String orderNum, BigDecimal money){
		return buyerOrderSvc.decideOrder(orderNum, money);
	}
	
	/**
	 * 支付成功后的回调
	 * @return
	 */
	@RequestMapping(value="paySuccessOper")
	@ResponseBody
	public Map<String, Object> paySuccessOper(){
		return buyerOrderSvc.paySuccessOper();
	}
	
	
	/**
	 * 同步订单到PMS
	 * @return
	 */
	@RequestMapping(value="createPmsOrder")
	@ResponseBody
	public Map<String, Object> createPmsOrder(String orderNum){
		return buyerOrderSvc.createPmsOrder(orderNum);
	}
	
}
