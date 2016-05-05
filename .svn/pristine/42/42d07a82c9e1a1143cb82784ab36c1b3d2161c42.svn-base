package xyz.svc.core.imp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.JSON;
import xyz.filter.ReturnUtil;
import xyz.filter.RmiUtil;
import xyz.model.base.ExceptionLog;
import xyz.model.base.Region;
import xyz.model.core.OrderContent;
import xyz.model.main.ProductStock;
import xyz.svc.config.ConstantPms;
import xyz.svc.core.RegionSvc;
import xyz.svc.core.config.ConstantPOI;
import xyz.util.Constant;
import xyz.util.DateUtil;
import xyz.util.StringTool;

@Service
public class RegionSvcImp implements RegionSvc {

	@Autowired
	RmiUtil rmiUtil;
	
	@Autowired
	CommonDao commonDao;
	
	@Override
	public Map<String, Object> updateRegionList() {
	
		Map<String,String> accessoryParam = new HashMap<String, String>();
		
		String url=ConstantPOI.POI_URL+"?";
		url+="&query=行政区";
		url+="&page_size=100";
		url+="&page_num=0";
		url+="&scope=1";
		url+="&region=西藏";
		url+="&output=json";
		url+="&ak="+ConstantPOI.AK;
		
		@SuppressWarnings("unchecked")
		Map<String, Object> result= (Map<String, Object>) rmiUtil.loadData(url,accessoryParam);
				
		if((Integer)result.get("status")!=0){
			return ReturnUtil.returnMap(0, "更新行政区数据失败:");
		}else{
			@SuppressWarnings("unchecked")
			Object[] objects=JSON.toObject(JSON.toJosn(result.get("results")), Object[].class);
			for(Object object:objects){
				Map<String, Object> map=(Map<String, Object>) object;
				Region region=(Region) commonDao.getObjectByUniqueCode("Region", "numberCode", map.get("num").toString());
				if(region==null){
					region=new Region();
					region.setNumberCode(map.get("num").toString());
					region.setNameCn(map.get("name").toString());
					commonDao.save(region);
				}		
			}
		}
		
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> queryRegionList(int offset, int pagesize) {

		String hql=" from Region";

		
		String countHql = "select count(numberCode) "+hql;
		Query countQuery = commonDao.getQuery(countHql);
		Number countTemp = (Number)countQuery.uniqueResult();
		int count = countTemp==null?0:countTemp.intValue();
		
		Query query = commonDao.getQuery(hql);
		query.setMaxResults(pagesize);
		query.setFirstResult(offset);
		@SuppressWarnings("unchecked")
		List<Region> regionList=query.list();
		
		
		Map<String,Object> mapContent=new HashMap<String, Object>();
		mapContent.put("total", count);
		mapContent.put("rows",regionList);
		return ReturnUtil.returnMap(1, mapContent);
	}

}
