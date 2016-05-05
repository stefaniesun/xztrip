package xyz.ctrl.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.svc.scheduled.SaleSTSvc;

@Component
public class SaleST {

	@Autowired
	private SaleSTSvc saleSTSvc;
	
	/**
	 *更新月销量(近30天)
	 */
	public int autoUpdateMonthSaleOper(){
		return saleSTSvc.autoUpdateMonthSaleOper();
	}
}
