package xyz.util;


import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.dao.CommonDao;
import xyz.exception.MyExceptionForRole;
import xyz.model.core.OrderContent;
import xyz.model.main.LogTsTemp;
import xyz.model.main.ProductObject;
import xyz.model.main.ProductStock;


@Component
public class StockPriceUtil {

	@Resource
	CommonDao commonDao;
	
	@Autowired
	ProductUtil productUtil;
	
	@Autowired
	PmsStockPriceUtil pmsStockPriceUtil;
	
	@Autowired
	UserTagPriceUtil userTagPriceUtil;
	
	public ProductStock getStockByDate(String product, Date dateInfo) {
		
		ProductStock productStock=new ProductStock();
		
		if(productUtil.isPmsProduct(product)){
			productStock= pmsStockPriceUtil.getStockByDate(product,dateInfo);
		}else{
			String hql = "from ProductStock t where t.product = '"+product+"' and  t.dateInfo='"+DateUtil.dateToShortString(dateInfo)+"'";
			productStock=(ProductStock) commonDao.queryUniqueByHql(hql);
		}
		ProductObject productObject=productUtil.getProductByNumberCode(product);
		if(productObject.getIsTag()==1){
			BigDecimal minPrice=userTagPriceUtil.getMinPrice(product,productStock.getDateInfo(),productStock.getPrice());
			if(minPrice!=null){
				productStock.setPrice(minPrice);
			}
		}
		return productStock;
	}
	
	//根据选择日期区间查询对应的库存价格明细
	/*public List<ProductStock> getStockByDate(String product, Date dateStart,Date dateEnd) {
		
		if(productUtil.isPmsProduct(product)){
			return pmsStockPriceUtil.getStockByDate(product,dateStart,dateEnd);
		}
		
		StringBuffer hql = new StringBuffer("from ProductStock t where t.product = '"+product+"' ");
		
		hql.append(" and t.dateInfo >= '"+DateUtil.dateToShortString(dateStart)+"'");
		
		hql.append(" and t.dateInfo <= '"+DateUtil.getDateEndForQuery(dateEnd)+"'");
		
		hql.append(" order by t.dateInfo asc ");
		
		List<ProductStock> productStockList = commonDao.queryByHql(hql.toString());
		
		Set<String> dateInfoList = new HashSet<String>();
		for(ProductStock productStock : productStockList){
			dateInfoList.add(DateUtil.dateToShortString(productStock.getDateInfo()));
		}
		
		String sql = "select t.date_info,sum(t.count) from order_content t where t.product = '"+product+"' and t.date_info in ("+StringTool.listToSqlString(dateInfoList)+") group by t.date_info";
		List<Object[]> ttList = commonDao.getSqlQuery(sql).list();
		for(ProductStock productStock : productStockList){
			for(Object[] t : ttList){
				if(productStock.getDateInfo().compareTo((Date)t[0])==0){
					Number tp = (Number)t[1];
					int tpp = tp==null?0:tp.intValue();
					productStock.setUseCount(tpp);
				}
			}
		}
		return productStockList;
	}*/


	
	//订单支付后库存占位
	public void occupyingStock(String clientCode) {

		OrderContent orderContent=(OrderContent) commonDao.getObjectByUniqueCode("OrderContent", "clientCode", clientCode);
		
		if(orderContent==null){
			throw new MyExceptionForRole("订单不存在！");
		}
		
		ProductObject productObject=productUtil.getProductByNumberCode(orderContent.getProduct());
		if(productObject==null){
			throw new MyExceptionForRole("产品不存在！");
		}
		
		Date date = new Date();
		
		ProductStock productStock=getStockByDate(productObject.getNumberCode(), orderContent.getDateInfo());
		int countHave =productStock.getCount()-productStock.getUseCount();
		if(countHave<orderContent.getCount()){
			throw new MyExceptionForRole("库存不足！");
		}
		productStock.setUseCount(productStock.getUseCount()+orderContent.getCount());
		commonDao.update(productStock);
		
		LogTsTemp logTsTemp = new LogTsTemp();
		logTsTemp.setAddDate(date);
		logTsTemp.setCount(orderContent.getCount());
		logTsTemp.setDateInfo(orderContent.getDateInfo());
		logTsTemp.setNumberCode(UUIDUtil.getUUIDStringFor32());
		logTsTemp.setRemark("["+clientCode+"]库存占位");
		logTsTemp.setSystemType("b2b");
		logTsTemp.setProduct(productObject.getNumberCode());
		logTsTemp.setProductNameCn(productObject.getNameCn());
		commonDao.save(logTsTemp);
	}
	
	//未支付超时订单库存回滚
	public void rollbackStock(String clientCode) {

		OrderContent orderContent=(OrderContent) commonDao.getObjectByUniqueCode("OrderContent", "clientCode", clientCode);
		
		if(orderContent==null){
			throw new MyExceptionForRole("订单不存在！");
		}
		
		ProductObject productObject=productUtil.getProductByNumberCode(orderContent.getProduct());
		if(productObject==null){
			throw new MyExceptionForRole("产品不存在！");
		}
		
		Date date = new Date();
		
		ProductStock productStock=getStockByDate(productObject.getNumberCode(), orderContent.getDateInfo());
		if(productStock.getUseCount()<orderContent.getCount()){
			throw new MyExceptionForRole("库存回退异常！");
		}
		productStock.setUseCount(productStock.getUseCount()-orderContent.getCount());
		commonDao.update(productStock);
		
		LogTsTemp logTsTemp = new LogTsTemp();
		logTsTemp.setAddDate(date);
		logTsTemp.setCount(orderContent.getCount());
		logTsTemp.setDateInfo(orderContent.getDateInfo());
		logTsTemp.setNumberCode(UUIDUtil.getUUIDStringFor32());
		logTsTemp.setRemark("["+clientCode+"]库存回滚");
		logTsTemp.setSystemType("b2b");
		logTsTemp.setProduct(productObject.getNumberCode());
		logTsTemp.setProductNameCn(productObject.getNameCn());
		commonDao.save(logTsTemp);
	}

}
