package xyz.ctrl.main;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.svc.main.ProductStockSvc;

@Controller
@RequestMapping(value="/ProductStockWS")
public class ProductStockWS {
	@Autowired
	private ProductStockSvc productStockSvc;
	
	@RequestMapping(value="queryProductStockList")
	@ResponseBody
	public Map<String, Object> queryProductStockList(
			String product,
			Date dateStart,
			Date dateEnd){
		return productStockSvc.queryProductStockList(product, dateStart,dateEnd);
	}
	
	@RequestMapping(value="queryProductStockAllList")
	@ResponseBody
	public Map<String, Object> queryProductStockAllList(String product){
		return productStockSvc.queryProductStockAllList(product);
	}
	
	@RequestMapping(value="addProductStock")
	@ResponseBody
	public Map<String,Object> addProductStock(
			String product, 
			int count, 
			String dateInfo, 
			BigDecimal price,
			int isAlterPrice){
		return productStockSvc.addProductStock(product, count, dateInfo,price,isAlterPrice);
		
	}
	
	@RequestMapping(value="deleteProductStock")
	@ResponseBody
	public Map<String,Object> deleteProductStock(String numberCodes){
		return productStockSvc.deleteProductStock(numberCodes);
	}
	
	@RequestMapping(value="deleteProductStock2")
	@ResponseBody
	public Map<String,Object> deleteProductStock2(
			String product,
			String dateInfo
			){
		return productStockSvc.deleteProductStock2(product,dateInfo);
	}
	
	@RequestMapping(value="queryLogProductStock")
	@ResponseBody
	public Map<String,Object> queryLogProductStock(String product,Date dateInfo){
		return productStockSvc.queryLogProductStock(product, dateInfo);
	}
	

	@RequestMapping(value="queryProductForStockList")
	@ResponseBody
	public Map<String, Object> queryProductForStockList(
			int page,
			int rows,
			String product,
			Date dateInfo){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return productStockSvc.queryProductForStockList(offset, pagesize, product,dateInfo);
	}
	
	
}
