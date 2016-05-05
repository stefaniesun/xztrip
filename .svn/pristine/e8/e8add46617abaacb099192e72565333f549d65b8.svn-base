package xyz.svc.core.imp;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.JSON;
import xyz.filter.ReturnUtil;
import xyz.filter.RmiUtil;
import xyz.model.base.Region;
import xyz.model.base.Scenic;
import xyz.svc.core.ScenicSvc;
import xyz.svc.core.config.ConstantPOI;

@Service
public class ScenicSvcImp implements ScenicSvc {

	@Autowired
	RmiUtil rmiUtil;
	
	@Autowired
	CommonDao commonDao;
	
	@Override
	public Map<String, Object> updateScenicList() {
		
		String hql="from Region";
	
		@SuppressWarnings("unchecked")
		List<Region> regions=commonDao.queryByHql(hql);
		
		for(Region region:regions){
			
			Map<String,String> accessoryParam = new HashMap<String, String>();
			
			String url=ConstantPOI.POI_URL+"?";
			url+="&query=景点";
			url+="&page_size=20";
			url+="&page_num=0";
			url+="&scope=1";
			url+="&region="+region.getNameCn();
			url+="&output=json";
			url+="&ak="+ConstantPOI.AK;
			
			@SuppressWarnings("unchecked")
			Map<String, Object> result= (Map<String, Object>) rmiUtil.loadData(url,accessoryParam);
		
			if((Integer)result.get("status")!=0){
				return ReturnUtil.returnMap(0, "更新行政区数据失败:");
			}else{
				
				int total=(Integer)result.get("total");
				int pageSize=(total-1)/20+1;
				
				for(int i=0;i<pageSize;i++){
					url=ConstantPOI.POI_URL+"?";
					url+="&query=景点";
					url+="&page_size=20";
					url+="&page_num="+i;
					url+="&scope=1";
					url+="&region="+region.getNameCn();
					url+="&output=json";
					url+="&ak="+ConstantPOI.AK;
					
					result= (Map<String, Object>) rmiUtil.loadData(url,accessoryParam);
					
					@SuppressWarnings("unchecked")
					Object[] objects=JSON.toObject(JSON.toJosn(result.get("results")), Object[].class);
					for(Object object:objects){
						@SuppressWarnings("unchecked")
						Map<String, Object> map=(Map<String, Object>) object;
						Scenic scenic=(Scenic) commonDao.getObjectByUniqueCode("Scenic", "numberCode", map.get("uid").toString());
						if(scenic==null){
							@SuppressWarnings("unchecked")
							Map<String, Object> location=JSON.toObject(JSON.toJosn(map.get("location")),Map.class);
							scenic=new Scenic();
							scenic.setNumberCode(map.get("uid").toString());
							scenic.setNameCn(map.get("name").toString());
							scenic.setAddress(map.get("address").toString());
							scenic.setLat(new BigDecimal(location.get("lat").toString()).setScale(6,BigDecimal.ROUND_HALF_UP));
							scenic.setLng(new BigDecimal(location.get("lng").toString()).setScale(6,BigDecimal.ROUND_HALF_UP));
							scenic.setRegion(region.getNumberCode());
							scenic.setRegionName(region.getNameCn());
							commonDao.save(scenic);
						}		
					}
					
				}
				
		
			}
			
		}
		
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> queryScenicList(int offset, int pagesize,String nameCn,String region) {

		String hql=" from Scenic where 1=1 ";
		
		if(nameCn!=null&&!"".equals(nameCn)){
			hql+=" and nameCn like '%"+nameCn+"%'";
		}
		if(region!=null&&!"".equals(region)){
			hql+=" and region='"+region+"'";
		}

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
