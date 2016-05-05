package xyz.svc.main;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface ProductUserTagStockSvc {
	public Map<String,Object> queryProductUserTagStockList(
			int offset,
			int pagesize,
			String productUserTag,
			Date dateStart,
			Date dateEnd);
	
	public Map<String, Object> queryProductUserTagStockAllList(String productUserTag);
	
	public Map<String,Object> addProductUserTagStock(
			String productUserTag, 
			String dateInfo, 
			BigDecimal price,
			int isAlterPrice);
	
	public Map<String,Object> deleteProductUserTagStock(String numberCodes);
	
	public Map<String,Object> deleteProductUserTagStock2(
			String productUserTag,
			String dateInfo
			);
	
	public Map<String,Object> queryLogProductUserTagStock(String numberCode,Date dateInfo);
	
	public Map<String,Object> queryProductForStockList(
			int offset,
			int pagesize,
			String productUserTag,
			Date dateInfo);
}
