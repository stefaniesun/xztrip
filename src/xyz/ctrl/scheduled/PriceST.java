package xyz.ctrl.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.svc.scheduled.PriceSTSvc;

@Component
public class PriceST {

	@Autowired
	private PriceSTSvc priceSTSvc;
	
	/**
	 * 自动同步更新当天的价格信息到产品上
	 */
	public int autoUpdatePrice(){
		return priceSTSvc.autoUpdatePriceOper();
	}
}
