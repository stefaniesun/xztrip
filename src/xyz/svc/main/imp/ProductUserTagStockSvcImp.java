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
import xyz.model.main.ProductUserTagStock;
import xyz.svc.main.ProductUserTagStockSvc;
import xyz.util.DateUtil;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
@Service
public class ProductUserTagStockSvcImp implements ProductUserTagStockSvc {
	@Autowired
	CommonDao commonDao;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryProductUserTagStockList(
			int offset,
			int pagesize,
			String productUserTag,
			Date dateStart,
			Date dateEnd) {
		if(productUserTag==null || "".equals(productUserTag)){
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
		
		StringBuffer hql = new StringBuffer("from ProductUserTagStock t where t.productUserTag = '"+productUserTag+"' ");
		
		hql.append(" and t.dateInfo >= '"+DateUtil.dateToShortString(dateStart)+"'");
		
		hql.append(" and t.dateInfo <= '"+DateUtil.getDateEndForQuery(dateEnd)+"'");
		
		hql.append(" order by t.dateInfo asc ");
		
		Query queryNum = commonDao.getQuery("select count(t.iidd) "+hql.toString());
		Number countNum = (Number)queryNum.uniqueResult();
		int count = countNum==null?0:countNum.intValue();
		
		Query query = commonDao.getQuery(hql.toString());
		query.setMaxResults(pagesize);
		query.setFirstResult(offset);
		
		List<ProductUserTagStock> productUserTagStockList = query.list();
		
		Set<String> dateInfoList = new HashSet<String>();
		for(ProductUserTagStock productUserTagStock : productUserTagStockList){
			dateInfoList.add(DateUtil.dateToShortString(productUserTagStock.getDateInfo()));
		}
		
		Map<String,Object> mapContent = new HashMap<String, Object>();
		mapContent.put("total", count);
		mapContent.put("rows", productUserTagStockList);
		return ReturnUtil.returnMap(1, mapContent);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryProductUserTagStockAllList(String productUserTag) {
		if(productUserTag==null || "".equals(productUserTag)){
			return ReturnUtil.returnMap(0, "参数丢失");
		}
		String hql ="from ProductUserTagStock t where t.productUserTag='"+productUserTag+"'";
		List<ProductUserTagStock> productUserTagStockList = (List<ProductUserTagStock>) commonDao.queryByHql(hql);
		
		Set<String> dateInfoList = new HashSet<String>();
		for(ProductUserTagStock productUserTagStock : productUserTagStockList){
			dateInfoList.add(DateUtil.dateToShortString(productUserTagStock.getDateInfo()));
		}
		
		
		return ReturnUtil.returnMap(1, productUserTagStockList);
	}

	@Override
	public Map<String, Object> addProductUserTagStock(
			String productUserTag,
			String dateInfo, 
			BigDecimal disccount,
			BigDecimal reduction) {
		String username = MyRequestUtil.getSecurityLogin().getUsername();
		if(productUserTag==null || "".equals(productUserTag)){
			return ReturnUtil.returnMap(0, "参数丢失");
		}
		String tableName = "ProductUserTag";
		
		Object productObj = commonDao.getObjectByUniqueCode(tableName, "numberCode", productUserTag);
		if(productObj==null){
			return ReturnUtil.returnMap(0, "参数不正确");
		}
		if(dateInfo==null || "".equals(dateInfo)){
			return ReturnUtil.returnMap(0,"特价库存日期不能为空");
		}
		String[] sDateInfo = dateInfo.split(",");
		String invalidDateInfo = null;
		for(String s : sDateInfo){
			Date date = DateUtil.shortStringToDate(s);
			invalidDateInfo = addProductUserTagStock2(username,productUserTag,date,disccount,reduction);
			if(invalidDateInfo!=null && !"".equals(invalidDateInfo)){
				return ReturnUtil.returnMap(0, invalidDateInfo);
			}
		}
		return ReturnUtil.returnMap(1, null);
	}
	
	private String addProductUserTagStock2(String username,String productUserTag,Date dateInfo,BigDecimal discount,BigDecimal reduction){
		Date date = new Date();
		String dateInfo2 = DateUtil.dateToShortString(dateInfo);
		discount=discount.divide(new BigDecimal(10));
		String hql ="from ProductUserTagStock t where t.productUserTag='"+productUserTag+"' and dateInfo='"+dateInfo2+"'";
		ProductUserTagStock productUserTagStock = (ProductUserTagStock) commonDao.queryUniqueByHql(hql);
		if(productUserTagStock==null){
			productUserTagStock = new ProductUserTagStock();
			productUserTagStock.setAddDate(date);
			productUserTagStock.setAlterDate(date);
			productUserTagStock.setDateInfo(dateInfo);
			productUserTagStock.setNumberCode(UUIDUtil.getUUIDStringFor32());
			productUserTagStock.setOperator(username);
			productUserTagStock.setDiscount(discount);
			productUserTagStock.setReduction(reduction);
			productUserTagStock.setProductUserTag(productUserTag);
			commonDao.save(productUserTagStock);
			String logContent = "【新增特价策略】新增【"+DateUtil.dateToShortString(dateInfo)+"】折扣【"+discount+"】减免【"+reduction+"】";
			
			LogWork logWork = new LogWork();
			logWork.setAddDate(new Date());
			logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
			logWork.setValue(productUserTag);
			logWork.setRemark(logContent);
			logWork.setTableName("productUserTag");
			logWork.setUsername(username);
			commonDao.save(logWork);
		}else{
			productUserTagStock.setAlterDate(date);
			productUserTagStock.setOperator(username);
			productUserTagStock.setDiscount(discount);
			productUserTagStock.setReduction(reduction);
			String logContent = "";
			logContent = "【修改特价策略】修改【"+DateUtil.dateToShortString(dateInfo)+"】折扣【"+discount+"】减免【"+reduction+"】";
			
			LogWork logWork = new LogWork();
			logWork.setAddDate(new Date());
			logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
			logWork.setValue(productUserTag);
			logWork.setRemark(logContent);
			logWork.setTableName("productUserTag");
			logWork.setUsername(username);
			commonDao.save(logWork);
		}
		return "";
	}
	@Override
	public Map<String, Object> deleteProductUserTagStock(String numberCodes) {
		String username = MyRequestUtil.getSecurityLogin().getUsername();
		String hql ="from ProductUserTagStock t where t.numberCode in ("+StringTool.StrToSqlString(numberCodes)+")";
		@SuppressWarnings("unchecked")
		List<ProductUserTagStock> productUserTagStockList = commonDao.queryByHql(hql);
		for(ProductUserTagStock productUserTagStock : productUserTagStockList){
           commonDao.delete(productUserTagStock);
   			String logContent = "【删除特价库存价格】删除【"+DateUtil.dateToShortString(productUserTagStock.getDateInfo())+"】";
   			
   			LogWork logWork = new LogWork();
			logWork.setAddDate(new Date());
			logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
			logWork.setValue(productUserTagStock.getProductUserTag());
			logWork.setRemark(logContent);
			logWork.setTableName("productUserTag");
			logWork.setUsername(username);
			commonDao.save(logWork);
		}
		return ReturnUtil.returnMap(1, null);
	}
	
	
	@Override
	public Map<String, Object> deleteProductUserTagStock2(
			String productUserTag,
			String dateInfo){
		String username = MyRequestUtil.getSecurityLogin().getUsername();
		String tableName = "ProductUserTag";
		
		Object productObject = commonDao.getObjectByUniqueCode(tableName, "numberCode", productUserTag);
		if(productObject == null){
			return ReturnUtil.returnMap(0, "特价库存异常！可能已被删除！");
		}
		
		if(dateInfo==null || "".equals(dateInfo)){
			return ReturnUtil.returnMap(0,"库存日期不能为空");
		}
		String[] sDateInfo = dateInfo.split(",");
		for(String s : sDateInfo){
			Date deleteDate = DateUtil.shortStringToDate(s);
			deleteProductUserTagStock3(username,productUserTag,deleteDate);
		}
		return ReturnUtil.returnMap(1, null);
	}

	private void deleteProductUserTagStock3(String username,String productUserTag,Date dateInfo){
		String hql ="from ProductUserTagStock t where t.productUserTag='"+productUserTag+"' and dateInfo='"+DateUtil.dateToShortString(dateInfo)+"'";
		ProductUserTagStock productUserTagStock = (ProductUserTagStock) commonDao.queryUniqueByHql(hql);
		if(productUserTagStock!=null){
			commonDao.delete(productUserTagStock);
   			String logContent = "【删除特价库存价格】删除【"+DateUtil.dateToShortString(productUserTagStock.getDateInfo())+"】";
   			
   			LogWork logWork = new LogWork();
			logWork.setAddDate(new Date());
			logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
			logWork.setValue(productUserTagStock.getProductUserTag());
			logWork.setRemark(logContent);
			logWork.setTableName("productUserTag");
			logWork.setUsername(username);
			commonDao.save(logWork);
		}
	}
	
	@Override
	public Map<String, Object> queryLogProductUserTagStock(String productUserTag,Date dateInfo) {
	    String hql ="from LogWork t where t.tableName = 'productUserTag' and t.value='"+productUserTag+"'";
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
			String productUserTag, Date dateInfo) {
		if(productUserTag==null || "".equals(productUserTag)){
			return ReturnUtil.returnMap(0, "参数丢失");
		}
		StringBuffer hql = new StringBuffer("from ProductUserTagStock t where t.productUserTag = '"+productUserTag+"' ");
		
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
		List<ProductUserTagStock> productUserTagStockList = query.list();
		
		Set<String> dateInfoList = new HashSet<String>();
		for(ProductUserTagStock productUserTagStock : productUserTagStockList){
			dateInfoList.add(DateUtil.dateToShortString(productUserTagStock.getDateInfo()));
		}
		
		Map<String,Object> mapContent = new HashMap<String, Object>();
		mapContent.put("total", count);
		mapContent.put("rows", productUserTagStockList);
		return ReturnUtil.returnMap(1, mapContent);
	}
}
