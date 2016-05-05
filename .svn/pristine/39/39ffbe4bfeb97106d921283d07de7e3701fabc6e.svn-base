package xyz.svc.main.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.model.security.SecurityUser;
import xyz.svc.main.ProductSvc;

@Service
public class ProductSvcImp implements ProductSvc{
	@Autowired
	CommonDao commonDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> queryProductList(int offset,int pagesize,String userName){
		if("".equals(userName) || userName == null){
			return ReturnUtil.returnMap(0, "参数丢失");
		}
		
		Query queryProvider = commonDao.getQuery("from SecurityUser su where su.username='"+userName+"'");
		SecurityUser securityUser = (SecurityUser)queryProvider.uniqueResult();
		
		StringBuffer sql = new StringBuffer("select * from (select * from product_hotel union all select * from product_scenic  ) p where 1=1");
		if(!"admin".equals(userName)){
			sql.append(" and p.provider='"+securityUser.getPossessor()+"'");
		}
		
		Query query = commonDao.getSqlQuery(sql.toString());
		query.setMaxResults(pagesize);
		query.setFirstResult(offset);
		List<Object[]> productData = query.list();
		int count = productData.size();
		@SuppressWarnings("rawtypes")
		List productList = new ArrayList();
		
		//拼装数据
		for(Object[] product : productData){
			Map<String,Object> productMap = new HashMap<String,Object>();
			productMap.put("iidd", product[0]);
			productMap.put("nameCn", product[1]);
			productMap.put("numberCode", product[2]);
			productMap.put("provider", product[3]);
			productMap.put("remark", product[4]);
			productList.add(productMap);
		}
		
		Map<String,Object> mapContent = new HashMap<String,Object>();
		mapContent.put("total", count);
		mapContent.put("rows", productList);
		return ReturnUtil.returnMap(1, mapContent);
	}
}
