package xyz.svc.main.imp;

import java.util.ArrayList;
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
import xyz.filter.ReturnUtil;
import xyz.model.main.ProductScenic;
import xyz.model.main.Provider;
import xyz.svc.main.ProductScenicSvc;
import xyz.util.StringTool;
import xyz.util.StringUtil;

@Service
public class ProductScenicSvcImp implements ProductScenicSvc {

	@Autowired
	CommonDao commonDao;
	
	@Override
	public Map<String, Object> queryProductScenicList(String nameCn, String provider,
			int offset,
			int pagesize) {
		String hql=" from ProductScenic where 1=1 ";
		if(!"".equals(nameCn)&&nameCn!=null){
			hql+=" and nameCn like '%"+nameCn+"%'";
		}
		if(!"".equals(provider)&&provider!=null){
			hql+=" and provider = '"+provider+"'";
		}
		
		hql+="order by alertDate desc";
	
		String countHql = "select count(numberCode) "+hql;
		Query countQuery = commonDao.getQuery(countHql);
		Number countTemp = (Number)countQuery.uniqueResult();
		int count = countTemp==null?0:countTemp.intValue();
		
		Query query = commonDao.getQuery(hql);
		query.setMaxResults(pagesize);
		query.setFirstResult(offset);
		@SuppressWarnings("unchecked")
		List<ProductScenic> productScenicList=query.list();
		
		List<String> providerStringList=new ArrayList<String>();
		for(ProductScenic productScenic:productScenicList){
			providerStringList.add(productScenic.getProvider());
		}
		
		String hql1="select numberCode,nameCn from Provider where numberCode in ("+StringTool.listToSqlString(providerStringList)+")";
		@SuppressWarnings("unchecked")
		List<Object[]> providerList=commonDao.getQuery(hql1).list();
		for(int i=0;i<productScenicList.size();i++){
			for(int ii=0;ii<providerList.size();ii++){
				if(productScenicList.get(i).getProvider().toString().equals(providerList.get(ii)[0].toString())){
					productScenicList.get(i).setProviderNameCn(providerList.get(ii)[1].toString());
					break;
				}
			}
		}
		
		Map<String,Object> mapContent=new HashMap<String, Object>();
		mapContent.put("total", count);
		mapContent.put("rows",productScenicList);
		return ReturnUtil.returnMap(1, mapContent);
	}

	@Override
	public Map<String, Object> addProductScenic(String nameCn, String provider,String remark,int refundTime,int maxDate) {
		if(provider==null||"".equals(provider)){
			return ReturnUtil.returnMap(0, "请选择关联的供应商");
		}
		ProductScenic productscenic =new ProductScenic();
		productscenic.setNameCn(nameCn);
		productscenic.setNumberCode(StringUtil.get_new_product("SC"));
		productscenic.setProvider(provider);
		productscenic.setRemark(remark);
		productscenic.setOnlineFlag(0);
		productscenic.setMaxDate(maxDate);
		productscenic.setRefundTime(refundTime);
		productscenic.setAddDate(new Date());
		productscenic.setAlertDate(new Date());
		commonDao.save(productscenic);
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> editProductScenic(String numberCode,
			String nameCn, String provider,String remark,int refundTime,int maxDate) {
		if(provider==null||"".equals(provider)){
			return ReturnUtil.returnMap(0, "请选择关联的供应商");
		}
		ProductScenic productscenic=(ProductScenic)commonDao.getObjectByUniqueCode("ProductScenic", "numberCode", numberCode);
		productscenic.setNameCn(nameCn);
		productscenic.setProvider(provider);
		productscenic.setRemark(remark);
		productscenic.setMaxDate(maxDate);
		productscenic.setRefundTime(refundTime);
		productscenic.setAlertDate(new Date());
		commonDao.update(productscenic);
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> deleteProductScenic(String numberCodes) {
		if(!"".equals(numberCodes)&&numberCodes!=null){
			String hql="from ProductScenic where onlineFlag = 1 and numberCode IN("+StringTool.StrToSqlString(numberCodes)+")";
			@SuppressWarnings("unchecked")
			List<ProductScenic> productScenicList=commonDao.queryByHql(hql);
			if(productScenicList.size()>0){
				return ReturnUtil.returnMap(0, "当前选择的产品中存在已经上线的产品请下线后删除！");
			}else{
				
				String sql0="select number_code from product_user_tag where product in ("+StringTool.StrToSqlString(numberCodes)+")";
				@SuppressWarnings("unchecked")
				List<String> productUserTagList=commonDao.getSqlQuery(sql0).list();
				String sql1 = "delete from product_user_tag_stock where product_user_tag in ("+StringTool.listToSqlString(productUserTagList)+")";
				commonDao.getSqlQuery(sql1).executeUpdate();
				String sql2 = "delete from product_user_tag where product in ("+StringTool.StrToSqlString(numberCodes)+")";
				commonDao.getSqlQuery(sql2).executeUpdate();
				String sql3 = "delete from product_stock where product in ("+StringTool.StrToSqlString(numberCodes)+")";
				commonDao.getSqlQuery(sql3).executeUpdate();
				String sql = "delete from product_scenic where number_code in ("+StringTool.StrToSqlString(numberCodes)+")";
				commonDao.getSqlQuery(sql).executeUpdate();
				return ReturnUtil.returnMap(1, null);
			}
		}else{
			return ReturnUtil.returnMap(0, "请先选中需要删除的对象！");
		}
	}

	@Override
	public Map<String, Object> queryProductScenicListForStock(String nameCn,
			String provider, int offset, int pagesize) {
		String hql=" from ProductScenic where 1=1 ";
		if(!"".equals(nameCn)&&nameCn!=null){
			hql+=" and nameCn like '%"+nameCn+"%'";
		}
		if(!"".equals(provider)&&provider!=null){
			hql+=" and provider = '"+provider+"'";
		}
		
		String countHql = "select count(numberCode) "+hql;
		Query countQuery = commonDao.getQuery(countHql);
		Number countTemp = (Number)countQuery.uniqueResult();
		int count = countTemp==null?0:countTemp.intValue();
		
		Query query = commonDao.getQuery(hql);
		query.setMaxResults(pagesize);
		query.setFirstResult(offset);
		@SuppressWarnings("unchecked")
		List<ProductScenic> productScenicList=query.list();
		
		Set<String> productNumberCodeList = new HashSet<String>();
		for(ProductScenic product: productScenicList){
			productNumberCodeList.add(product.getNumberCode());
		}
		String hqlDetail = "from Provider p where p.numberCode in ("+StringTool.listToSqlString(productNumberCodeList)+")";
		@SuppressWarnings("unchecked")
		List<Provider> providerList = commonDao.queryByHql(hqlDetail);
		
		List<Object> resultList = new ArrayList<Object>();
		
		//拼装数据
		for(ProductScenic product: productScenicList){
			Map<String,Object> productDetailMap = new HashMap<String,Object>();
			productDetailMap.put("nameCn", product.getNameCn());
			productDetailMap.put("numberCode", product.getNumberCode());
			for(Provider p : providerList){
				if(product.getProvider().equals(p.getNumberCode())){
					productDetailMap.put("provider",p.getNameCn());
					break;
				}
			}
			resultList.add(productDetailMap);
		}
		
		Map<String,Object> mapContent=new HashMap<String, Object>();
		mapContent.put("total", count);
		mapContent.put("rows",resultList);
		
		return ReturnUtil.returnMap(1, mapContent);
	
	}

	@Override
	public Map<String, Object> editUserTag(String numberCode, int value) {
		ProductScenic scenic=(ProductScenic) commonDao.getObjectByUniqueCode("ProductScenic", "numberCode", numberCode);
		scenic.setIsTag(value);
		commonDao.update(scenic);
		return ReturnUtil.returnMap(1, null);
	}
	@Override
	public Map<String, Object> editOnlineFlag(String numberCode, int value) {
		ProductScenic scenic=(ProductScenic) commonDao.getObjectByUniqueCode("ProductScenic", "numberCode", numberCode);
		scenic.setOnlineFlag(value);
		commonDao.update(scenic);
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> editProductScenicImage(String numberCode,
			String imageUrl) {
		ProductScenic productScenic=(ProductScenic) commonDao.getObjectByUniqueCode("ProductScenic", "numberCode", numberCode);
		if(productScenic==null){
			return ReturnUtil.returnMap(0, "产品不存在");
		}
		productScenic.setImageUrl(imageUrl);
		commonDao.update(productScenic);
		return ReturnUtil.returnMap(1, null);
	}
	
	
}
