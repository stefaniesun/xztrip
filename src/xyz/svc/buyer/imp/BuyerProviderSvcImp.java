package xyz.svc.buyer.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.model.main.Provider;
import xyz.svc.buyer.BuyerProviderSvc;
import xyz.svc.main.config.ConstantProduct;

@Service
public class BuyerProviderSvcImp implements BuyerProviderSvc {

	@Autowired
	CommonDao commonDao;
	
	@Override
	public Map<String, Object> queryProviderList(String nameCn, String type,
			String minPrice, String maxPrice,String orderBy, int offset, int pagesize) {

		String hql=" from Provider where onlineFlag=1  ";
		String parameter1="";
		String parameter2="";
		if(!"".equals(type)&&type!=null){
			if(ConstantProduct.SCENIC.equals(type)){
				parameter1="isScenic";
				parameter2="scenicPrice";
			}else if (ConstantProduct.HOTEL.equals(type)) {
				parameter1="isHotel";
				parameter2="hotelPrice";
			}else if (ConstantProduct.CAR.equals(type)) {
				parameter1="isCar";
				parameter2="carPrice";
			}
			
			hql+=" and "+parameter1+" =1 and "+parameter2+" is not null";
			if(minPrice!=null&&!"".equals(minPrice)){
				hql+=" and "+parameter2+">="+minPrice;
			}
			if(maxPrice!=null&&!"".equals(maxPrice)){
				hql+=" and "+parameter2+"<="+maxPrice;
			}
		}
		
		if(!"".equals(nameCn)&&nameCn!=null){
			hql+=" and nameCn like '%"+nameCn+"%' ";
		}
		
		String countHql = "select count(numberCode) "+hql;
		Query countQuery = commonDao.getQuery(countHql);
		Number countTemp = (Number)countQuery.uniqueResult();
		int count = countTemp==null?0:countTemp.intValue();
		
		if(!"".equals(orderBy)&&orderBy!=null){
			hql+=" ORDER BY "+orderBy+" ";
		}
		
		Query query = commonDao.getQuery(hql);
		query.setMaxResults(pagesize);
		query.setFirstResult(offset);
		@SuppressWarnings("unchecked")
		List<Provider> providerList=query.list();
		Map<String,Object> mapContent=new HashMap<String, Object>();
		mapContent.put("total", count);
		mapContent.put("rows",providerList);
		return ReturnUtil.returnMap(1, mapContent);
	
	}

	@Override
	public Map<String, Object> getProvider(String numberCode) {
		Provider provider=(Provider) commonDao.getObjectByUniqueCode("Provider", "numberCode", numberCode);
		if(provider==null){
			return ReturnUtil.returnMap(0, null);
		}
		return ReturnUtil.returnMap(1, provider);
	}

}
