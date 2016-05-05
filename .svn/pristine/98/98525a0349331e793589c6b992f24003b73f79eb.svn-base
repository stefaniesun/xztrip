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
import xyz.model.main.ProductHotel;
import xyz.svc.buyer.BuyerHotelSvc;
import xyz.util.UserTagPriceUtil;

@Service
public class BuyerHotelSvcImp implements BuyerHotelSvc {

	
	@Autowired
	CommonDao commonDao;
	
	@Autowired
	UserTagPriceUtil userTagPriceUtil;
	
	@Override
	public Map<String, Object> queryHotelList(String nameCn, String provider,
			int offset, int pagesize) {
		String hql=" from ProductHotel where onlineFlag=1 ";
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
		List<ProductHotel> productHotelList=query.list();
		
		for(ProductHotel hotel:productHotelList){
			if(hotel.getIsTag()==1){
				BigDecimal minPrice=userTagPriceUtil.getMinPrice(hotel.getNumberCode(),new Date(),hotel.getPrice());
				if(minPrice!=null){
					hotel.setPrice(minPrice);
				}
			}
		}
		
		Map<String,Object> mapContent=new HashMap<String, Object>();
		mapContent.put("total", count);
		mapContent.put("rows",productHotelList);
		return ReturnUtil.returnMap(1, mapContent);
	}

	@Override
	public Map<String, Object> getHotelProduct(String numberCode) {
		ProductHotel hotel=(ProductHotel) commonDao.getObjectByUniqueCode("ProductHotel", "numberCode", numberCode);
		if(hotel==null){
			return ReturnUtil.returnMap(0, "对象不存在");
		}
		if(hotel.getIsTag()==1){
			BigDecimal minPrice=userTagPriceUtil.getMinPrice(hotel.getNumberCode(),new Date(),hotel.getPrice());
			if(minPrice!=null&&minPrice.compareTo(hotel.getPrice())<0){
				hotel.setPrice(minPrice);
			}
		}
		return ReturnUtil.returnMap(1, hotel);
	}

}
