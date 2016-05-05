package xyz.ctrl.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.svc.core.OrderContentSvc;

@Component
public class OrderST {

	@Autowired
	OrderContentSvc orderContentSvc;
	
	/**
	 * 自动订单推送到ERP
	 */
	public int pushOrdertoErp(){
		return orderContentSvc.pushOrdertoErpOper();
	}
	
	/**
	 * 自动查找支付超时的订单，同时回退库存
	 */
	public int rollbackOverOrder(){
		return orderContentSvc.rollbackOverOrderOper();
	}

	
}
