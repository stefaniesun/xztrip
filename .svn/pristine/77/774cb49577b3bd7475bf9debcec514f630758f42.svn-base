package xyz.svc.main.imp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.model.base.LogWork;
import xyz.model.main.ProductHotel;
import xyz.model.main.ProductObject;
import xyz.model.main.ProductStock;
import xyz.svc.main.ProductStockSvc;
import xyz.svc.main.config.ConstantProduct;
import xyz.util.DateUtil;
import xyz.util.PmsStockPriceUtil;
import xyz.util.ProductUtil;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.util.UserTagPriceUtil;
@Service
public class ProductStockSvcImp implements ProductStockSvc {
	@Autowired
	CommonDao commonDao;
	
	@Autowired
	PmsStockPriceUtil pmsStockPriceUtil;
	
	@Autowired
	ProductUtil productUtil;
	
	@Autowired
	UserTagPriceUtil userTagPriceUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryProductStockList(
			String product,
			Date dateStart,
			Date dateEnd) {
		if(product==null || "".equals(product)){
			return ReturnUtil.returnMap(0, "参数丢失");
		}
		if(dateStart==null){
			return ReturnUtil.returnMap(0, "起始日期参数丢失");
		}
		if(dateEnd==null){
			return ReturnUtil.returnMap(0, "结束日期参数丢失");
		}
		if(dateStart.compareTo(dateEnd)>0){
			return ReturnUtil.returnMap(0, "起始日期大于结束日期");
		}
		
		if(productUtil.isPmsProduct(product)){
			ProductHotel hotel=(ProductHotel) commonDao.getObjectByUniqueCode("ProductHotel", "numberCode", product);
			try{
				return pmsStockPriceUtil.queryProductStockList(product,hotel.getPmsRoomType(),dateStart,dateEnd);				
			}catch(Exception e){
				e.printStackTrace();
				return ReturnUtil.returnMap(0, "供应商库存拉取失败，请稍后再试");
			}
		}
		
		StringBuffer hql = new StringBuffer("from ProductStock t where t.product = '"+product+"' ");
		
		hql.append(" and t.dateInfo >= '"+DateUtil.dateToShortString(dateStart)+"'");
		
		hql.append(" and t.dateInfo <= '"+DateUtil.getDateEndForQuery(dateEnd)+"'");
		
		hql.append(" order by t.dateInfo asc ");
		
		Query query = commonDao.getQuery(hql.toString());
		
	 	List<ProductStock> productStockList = query.list();
		
		Set<String> dateInfoList = new HashSet<String>();
	
		ProductObject productObject=productUtil.getProductByNumberCode(product);
		if(productObject.getIsTag()==1){
			//产品关联标签价格取标签最低价
			for(ProductStock productStock : productStockList){
				BigDecimal minPrice=userTagPriceUtil.getMinPrice(product,productStock.getDateInfo(),productStock.getPrice());
				if(minPrice!=null){
					productStock.setPrice(minPrice);
				}
				dateInfoList.add(DateUtil.dateToShortString(productStock.getDateInfo()));
			}
		}else{
			for(ProductStock productStock : productStockList){
				dateInfoList.add(DateUtil.dateToShortString(productStock.getDateInfo()));
			}
		}
		
		
		Map<String, Object> mapContent=new HashMap<String, Object>();
		mapContent.put("rows", productStockList);

		return ReturnUtil.returnMap(1, mapContent);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryProductStockAllList(String product) {
		if(product==null || "".equals(product)){
			return ReturnUtil.returnMap(0, "参数丢失");
		}
		String hql ="from ProductStock t where t.product='"+product+"'";
		List<ProductStock> productStockList = (List<ProductStock>) commonDao.queryByHql(hql);
		
		Set<String> dateInfoList = new HashSet<String>();
		for(ProductStock productStock : productStockList){
			dateInfoList.add(DateUtil.dateToShortString(productStock.getDateInfo()));
		}
		
	/*	String sql = "select t.date_info,sum(t.count) from order_content t where t.product = '"+product+"' and t.date_info in ("+StringTool.listToSqlString(dateInfoList)+") group by t.date_info";
		List<Object[]> ttList = commonDao.getSqlQuery(sql).list();
		for(ProductStock productStock : productStockList){
			for(Object[] t : ttList){
				if(productStock.getDateInfo().compareTo((Date)t[0])==0){
					Number tp = (Number)t[1];
					int tpp = tp==null?0:tp.intValue();
					productStock.setUseCount(tpp);
				}
			}
		}*/
		
		return ReturnUtil.returnMap(1, productStockList);
	}

	@Override
	public Map<String, Object> addProductStock(
			String product,
			int count,
			String dateInfo, 
			BigDecimal price,
			int isAlterPrice) {
		String username = MyRequestUtil.getSecurityLogin().getUsername();
		if(product==null || "".equals(product)){
			return ReturnUtil.returnMap(0, "参数丢失");
		}
		String tableName = "";
		if(product.startsWith(ConstantProduct.HOTEL)){
			tableName="ProductHotel";
		}else if(product.startsWith(ConstantProduct.SCENIC)){
			tableName="ProductScenic";
		}else{
			return ReturnUtil.returnMap(0, "产品编号有误！");
		}
		
		Object productObj = commonDao.getObjectByUniqueCode(tableName, "numberCode", product);
		if(productObj==null){
			return ReturnUtil.returnMap(0, "参数不正确");
		}
		if(dateInfo==null || "".equals(dateInfo)){
			return ReturnUtil.returnMap(0,"库存日期不能为空");
		}
		String[] sDateInfo = dateInfo.split(",");
		String invalidDateInfo = null;
		for(String s : sDateInfo){
			Date date = DateUtil.shortStringToDate(s);
			invalidDateInfo = addProductStock2(username,product,count,date,price,isAlterPrice);
			if(invalidDateInfo!=null && !"".equals(invalidDateInfo)){
				return ReturnUtil.returnMap(0, invalidDateInfo);
			}
		}
		return ReturnUtil.returnMap(1, null);
	}
	
	private String addProductStock2(String username,String product,int count,Date dateInfo,BigDecimal price,int isAlterPrice){
		Date date = new Date();
		String dateInfo2 = DateUtil.dateToShortString(dateInfo);
		String hql ="from ProductStock t where t.product='"+product+"' and dateInfo='"+dateInfo2+"'";
		ProductStock productStock = (ProductStock) commonDao.queryUniqueByHql(hql);
		if(productStock==null){
			if(isAlterPrice==1){
				 return "【"+dateInfo2+"】未设置价格";
			}
			if(count<0){
				 return "【"+dateInfo2+"】库存不能为负数";
			}
			productStock = new ProductStock();
			productStock.setAddDate(date);
			productStock.setAlterDate(date);
			productStock.setCount(count);
			productStock.setDateInfo(dateInfo);
			productStock.setNumberCode(UUIDUtil.getUUIDStringFor32());
			productStock.setOperator(username);
			productStock.setPrice(price);
			productStock.setProduct(product);
			commonDao.save(productStock);
			String logContent = "【新增单品库存】新增【"+DateUtil.dateToShortString(dateInfo)+"】数量【"+count+"】价格【"+price+"】";
			
			LogWork logWork = new LogWork();
			logWork.setAddDate(new Date());
			logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
			logWork.setValue(product);
			logWork.setRemark(logContent);
			logWork.setTableName("product");
			logWork.setUsername(username);
			commonDao.save(logWork);
		}else{
			if((productStock.getCount()+count)<0){
				return "【"+dateInfo2+"】已超出减少库存范围";
			}
			productStock.setAlterDate(date);
			productStock.setOperator(username);
			if(isAlterPrice==1){
				productStock.setPrice(productStock.getPrice());
			}else{
				productStock.setPrice(price);
			}
			productStock.setCount(productStock.getCount()+count);
			String logContent = "";
			if(count>=0){
				logContent = "【新增单品库存】新增【"+DateUtil.dateToShortString(dateInfo)+"】数量【"+count+"】价格【"+price+"】";
			}else{
				logContent = "【减少单品库存】减少【"+DateUtil.dateToShortString(dateInfo)+"】数量【"+count+"】价格【"+price+"】";
			}
			
			LogWork logWork = new LogWork();
			logWork.setAddDate(new Date());
			logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
			logWork.setValue(product);
			logWork.setRemark(logContent);
			logWork.setTableName("product");
			logWork.setUsername(username);
			commonDao.save(logWork);
		}
		return "";
	}
	@Override
	public Map<String, Object> deleteProductStock(String numberCodes) {
		String username = MyRequestUtil.getSecurityLogin().getUsername();
		String hql ="from ProductStock t where t.numberCode in ("+StringTool.StrToSqlString(numberCodes)+")";
		@SuppressWarnings("unchecked")
		List<ProductStock> productStockList = commonDao.queryByHql(hql);
		for(ProductStock productStock : productStockList){
           commonDao.delete(productStock);
   			String logContent = "【删除单品库存】删除【"+DateUtil.dateToShortString(productStock.getDateInfo())+"】";
   			
   			LogWork logWork = new LogWork();
			logWork.setAddDate(new Date());
			logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
			logWork.setValue(productStock.getProduct());
			logWork.setRemark(logContent);
			logWork.setTableName("product");
			logWork.setUsername(username);
			commonDao.save(logWork);
		}
		return ReturnUtil.returnMap(1, null);
	}
	
	
	@Override
	public Map<String, Object> deleteProductStock2(
			String product,
			String dateInfo){
		String username = MyRequestUtil.getSecurityLogin().getUsername();
		String tableName = "";
		if(ConstantProduct.HOTEL.equals(product.substring(0, 2))){
			tableName="ProductHotel";
		}else if(ConstantProduct.SCENIC.equals(product.substring(0, 2))){
			tableName="ProductScenic";
		}else{
			return ReturnUtil.returnMap(0, "产品编号有误！");
		}
		
		Object productObject = commonDao.getObjectByUniqueCode(tableName, "numberCode", product);
		if(productObject == null){
			return ReturnUtil.returnMap(0, "单品异常！可能已被删除！");
		}
		
		if(dateInfo==null || "".equals(dateInfo)){
			return ReturnUtil.returnMap(0,"库存日期不能为空");
		}
		String[] sDateInfo = dateInfo.split(",");
		for(String s : sDateInfo){
			Date deleteDate = DateUtil.shortStringToDate(s);
			deleteProductStock3(username,product,deleteDate);
		}
		return ReturnUtil.returnMap(1, null);
	}

	private void deleteProductStock3(String username,String product,Date dateInfo){
		String hql ="from ProductStock t where t.product='"+product+"' and dateInfo='"+DateUtil.dateToShortString(dateInfo)+"'";
		ProductStock productStock = (ProductStock) commonDao.queryUniqueByHql(hql);
		if(productStock!=null){
			commonDao.delete(productStock);
   			String logContent = "【删除单品库存】删除【"+DateUtil.dateToShortString(productStock.getDateInfo())+"】";
   			
   			LogWork logWork = new LogWork();
			logWork.setAddDate(new Date());
			logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
			logWork.setValue(productStock.getProduct());
			logWork.setRemark(logContent);
			logWork.setTableName("product");
			logWork.setUsername(username);
			commonDao.save(logWork);
		}
	}
	
	@Override
	public Map<String, Object> queryLogProductStock(String product,Date dateInfo) {
	    String hql ="from LogWork t where t.tableName = 'product' and t.value='"+product+"'";
	    if(dateInfo!=null){
	    	hql += " and t.remark like '%"+DateUtil.dateToShortString(dateInfo)+"%'";
	    }
	    hql += "order by t.addDate desc";
	    @SuppressWarnings("unchecked")
		List<LogWork> logWorkList = commonDao.queryByHql(hql);
		return ReturnUtil.returnMap(1, logWorkList);
	}

	@Override
	public Map<String, Object> queryProductForStockList(int offset, int pagesize,
			String product, Date dateInfo) {
		if(product==null || "".equals(product)){
			return ReturnUtil.returnMap(0, "参数丢失");
		}
		StringBuffer hql = new StringBuffer("from ProductStock t where t.product = '"+product+"' ");
		
		if(dateInfo!=null){
			hql.append(" and t.dateInfo = '"+DateUtil.dateToShortString(dateInfo)+"'");
		}
		hql.append(" order by t.dateInfo asc ");
		
		Query queryNum = commonDao.getQuery("select count(t.iidd) "+hql.toString());
		Number countNum = (Number)queryNum.uniqueResult();
		int count = countNum==null?0:countNum.intValue();
		
		Query query = commonDao.getQuery(hql.toString());
		query.setMaxResults(pagesize);
		query.setFirstResult(offset);
		
		@SuppressWarnings("unchecked")
		List<ProductStock> productStockList = query.list();
		
		Set<String> dateInfoList = new HashSet<String>();
		for(ProductStock productStock : productStockList){
			dateInfoList.add(DateUtil.dateToShortString(productStock.getDateInfo()));
		}
		
		String sql = "select t.date_info,sum(t.count) from order_content t where t.product = '"+product+"' and t.date_info in ("+StringTool.listToSqlString(dateInfoList)+") group by t.date_info";
		@SuppressWarnings("unchecked")
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
		
		Map<String,Object> mapContent = new HashMap<String, Object>();
		mapContent.put("total", count);
		mapContent.put("rows", productStockList);
		return ReturnUtil.returnMap(1, mapContent);
	}
}
