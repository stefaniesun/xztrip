package xyz.svc.main.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.model.main.ProductScenic;
import xyz.model.main.ProductSpecialty;
import xyz.svc.main.ProductSpecialtySvc;
import xyz.util.StringTool;
import xyz.util.StringUtil;

@Service
public class ProcustSpecialtySvcImp implements ProductSpecialtySvc {

	@Autowired
	CommonDao commonDao;
	
	@Override
	public Map<String, Object> queryProductSpecialtyList(String nameCn,
			String provider, int offset, int pagesize) {
		String hql=" from ProductSpecialty where 1=1 ";
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
		List<ProductSpecialty> productSpecialtyList=query.list();
		
		List<String> providerStringList=new ArrayList<String>();
		for(ProductSpecialty productSpecialty:productSpecialtyList){
			providerStringList.add(productSpecialty.getProvider());
		}
		
		String hql1="select numberCode,nameCn from Provider where numberCode in ("+StringTool.listToSqlString(providerStringList)+")";
		@SuppressWarnings("unchecked")
		List<Object[]> providerList=commonDao.getQuery(hql1).list();
		for(int i=0;i<productSpecialtyList.size();i++){
			for(int ii=0;ii<providerList.size();ii++){
				if(productSpecialtyList.get(i).getProvider().toString().equals(providerList.get(ii)[0].toString())){
					productSpecialtyList.get(i).setProviderNameCn(providerList.get(ii)[1].toString());
					break;
				}
			}
		}
		
		Map<String,Object> mapContent=new HashMap<String, Object>();
		mapContent.put("total", count);
		mapContent.put("rows",productSpecialtyList);
		return ReturnUtil.returnMap(1, mapContent);
	}

	@Override
	public Map<String, Object> addProductSpecialty(String nameCn,
			String provider, String remark, int refundTime, int maxDate) {
		if(provider==null||"".equals(provider)){
			return ReturnUtil.returnMap(0, "请选择关联的供应商");
		}
		ProductSpecialty productSpecialty =new ProductSpecialty();
		productSpecialty.setNameCn(nameCn);
		productSpecialty.setNumberCode(StringUtil.get_new_product("SP"));
		productSpecialty.setProvider(provider);
		productSpecialty.setRemark(remark);
		productSpecialty.setOnlineFlag(0);
		productSpecialty.setMaxDate(maxDate);
		productSpecialty.setRefundTime(refundTime);
		productSpecialty.setAddDate(new Date());
		productSpecialty.setAlertDate(new Date());
		commonDao.save(productSpecialty);
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> editProductSpecialty(String numberCode,
			String nameCn, String provider, String remark, int refundTime,
			int maxDate) {
		if(provider==null||"".equals(provider)){
			return ReturnUtil.returnMap(0, "请选择关联的供应商");
		}
		ProductSpecialty productSpecialty=(ProductSpecialty)commonDao.getObjectByUniqueCode("ProductSpecialty", "numberCode", numberCode);
		productSpecialty.setNameCn(nameCn);
		productSpecialty.setProvider(provider);
		productSpecialty.setRemark(remark);
		productSpecialty.setMaxDate(maxDate);
		productSpecialty.setRefundTime(refundTime);
		productSpecialty.setAlertDate(new Date());
		commonDao.update(productSpecialty);
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> editUserTag(String numberCode, int value) {
		ProductSpecialty specialty=(ProductSpecialty) commonDao.getObjectByUniqueCode("ProductSpecialty", "numberCode", numberCode);
		specialty.setIsTag(value);
		commonDao.update(specialty);
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> editProductSpecialtyImage(String numberCode,
			String imageUrl) {
		ProductSpecialty specialty=(ProductSpecialty) commonDao.getObjectByUniqueCode("ProductSpecialty", "numberCode", numberCode);
		if(specialty==null){
			return ReturnUtil.returnMap(0, "产品不存在");
		}
		specialty.setImageUrl(imageUrl);
		commonDao.update(specialty);
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> editOnlineFlag(String numberCode, int value) {
		ProductSpecialty specialty=(ProductSpecialty) commonDao.getObjectByUniqueCode("ProductSpecialty", "numberCode", numberCode);
		specialty.setOnlineFlag(value);
		commonDao.update(specialty);
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> deleteProductSpecialty(String numberCodes) {
		if(!"".equals(numberCodes)&&numberCodes!=null){
			String hql="from ProductSpecialty where onlineFlag = 1 and numberCode IN("+StringTool.StrToSqlString(numberCodes)+")";
			@SuppressWarnings("unchecked")
			List<ProductSpecialty> productSpecialtyList=commonDao.queryByHql(hql);
			if(productSpecialtyList.size()>0){
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
				String sql = "delete from product_specialty where number_code in ("+StringTool.StrToSqlString(numberCodes)+")";
				commonDao.getSqlQuery(sql).executeUpdate();
				return ReturnUtil.returnMap(1, null);
			}
		}else{
			return ReturnUtil.returnMap(0, "请先选中需要删除的对象！");
		}
	}

}
