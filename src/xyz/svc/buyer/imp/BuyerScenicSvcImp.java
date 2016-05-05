package xyz.svc.buyer.imp;

import java.math.BigDecimal;
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
import xyz.svc.buyer.BuyerScenicSvc;
import xyz.util.UserTagPriceUtil;

@Service
public class BuyerScenicSvcImp implements BuyerScenicSvc {

	
	@Autowired
	CommonDao commonDao;
	
	@Autowired
	UserTagPriceUtil userTagPriceUtil;
	
	@Override
	public Map<String, Object> queryScenicList(String nameCn, String provider,
			int offset, int pagesize) {
		String hql=" from ProductScenic where onlineFlag=1 ";
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
		
		for(ProductScenic scenic:productScenicList){
			if(scenic.getIsTag()==1){
				BigDecimal minPrice=userTagPriceUtil.getMinPrice(scenic.getNumberCode(),new Date(),scenic.getPrice());
				if(minPrice!=null){
					scenic.setPrice(minPrice);
				}
			}
		}
				
		Map<String,Object> mapContent=new HashMap<String, Object>();
		mapContent.put("total", count);
		mapContent.put("rows",productScenicList);
		return ReturnUtil.returnMap(1, mapContent);
	}

	@Override
	public Map<String, Object> getScenicProduct(String numberCode) {
		ProductScenic scenic=(ProductScenic) commonDao.getObjectByUniqueCode("ProductScenic", "numberCode", numberCode);
		if(scenic==null){
			return ReturnUtil.returnMap(0, "对象不存在");
		}
		if(scenic.getIsTag()==1){
			BigDecimal minPrice=userTagPriceUtil.getMinPrice(scenic.getNumberCode(),new Date(),scenic.getPrice());
			if(minPrice!=null&&minPrice.compareTo(scenic.getPrice())<0){
				scenic.setPrice(minPrice);
			}
		}
		return ReturnUtil.returnMap(1, scenic);
	}

}
