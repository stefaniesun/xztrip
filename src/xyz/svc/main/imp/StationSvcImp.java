package xyz.svc.main.imp;

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
import xyz.model.main.ProductHotel;
import xyz.model.main.ProductScenic;
import xyz.model.main.Station;
import xyz.svc.core.config.ConstantPOI;
import xyz.svc.main.StationSvc;
import xyz.util.StringTool;
import xyz.util.StringUtil;
import xyz.util.UUIDUtil;

@Service
public class StationSvcImp implements StationSvc {

	@Autowired
	CommonDao commonDao;
	
	@Autowired
	RmiUtil rmiUtil;
	
	
	@Override
	public Map<String, Object> queryStationList(int offset, int pagesize) {

		String hql=" from Station";
		
		String countHql = "select count(numberCode) "+hql;
		Query countQuery = commonDao.getQuery(countHql);
		Number countTemp = (Number)countQuery.uniqueResult();
		int count = countTemp==null?0:countTemp.intValue();
		
		Query query = commonDao.getQuery(hql);
		query.setMaxResults(pagesize);
		query.setFirstResult(offset);
		@SuppressWarnings("unchecked")
		List<Station> stations=query.list();
		
		
		Map<String,Object> mapContent=new HashMap<String, Object>();
		mapContent.put("total", count);
		mapContent.put("rows",stations);
		return ReturnUtil.returnMap(1, mapContent);
	}


	@Override
	public Map<String, Object> addStation(String nameCn, String phone,
			String linkman, String address, String remark, String lat,String lng) {

		Station station=new Station();
		station.setNumberCode(UUIDUtil.getUUIDStringFor32());
		station.setNameCn(nameCn);
		station.setPhone(phone);
		station.setLinkman(linkman);
		station.setAddress(address);
		station.setRemark(remark);
		station.setLat(new BigDecimal(lat));
		station.setLng(new BigDecimal(lng));
		
		commonDao.save(station);
		
		return ReturnUtil.returnMap(1, null);
	}


	@Override
	public Map<String, Object> editStation(String numberCode, String nameCn,
			String phone, String linkman, String address, String remark,
			String lat, String lng) {
		
		Station station=(Station) commonDao.getObjectByUniqueCode("Station", "numberCode", numberCode);
		if(station==null){
			return ReturnUtil.returnMap(0, "对象不存在");
		}
		station.setNameCn(nameCn);
		station.setPhone(phone);
		station.setLinkman(linkman);
		station.setAddress(address);
		station.setRemark(remark);
		station.setLat(new BigDecimal(lat));
		station.setLng(new BigDecimal(lng));
		
		commonDao.update(station);
		
		return ReturnUtil.returnMap(1, null);
	}


	@Override
	public Map<String, Object> deleteStation(String numberCodes) {
		
		/*if(!"".equals(numberCodes)&&numberCodes!=null){
			String sql = "delete from station where number_code in ("+StringTool.StrToSqlString(numberCodes)+")";
			commonDao.getSqlQuery(sql).executeUpdate();
			return ReturnUtil.returnMap(1, null);
		}else{
			return ReturnUtil.returnMap(0, "请先选中需要删除的对象！");
		}*/
		

		
		String hql="from Station";
		@SuppressWarnings("unchecked")
		List<Station> stations=commonDao.queryByHql(hql);
		
		for(Station station:stations){
			Map<String,String> accessoryParam = new HashMap<String, String>();
			
			String url=ConstantPOI.DIRECTION_URL+"?";
			url+="&origin=29.697597,92.197266";
			url+="&destination="+station.getLng()+","+station.getLat();
			url+="&mode=driving";
			url+="&region=西藏";
			url+="&origin_region=西藏";
			url+="&destination_region=西藏";
			url+="&output=json";
			url+="&ak="+ConstantPOI.AK;
			
			System.out.println("url==="+url);
			
			@SuppressWarnings("unchecked")
			Map<String, Object> result= (Map<String, Object>) rmiUtil.loadData(url,accessoryParam);
			
			if((Integer)result.get("status")!=0){
				return ReturnUtil.returnMap(0, "计算距离数据失败:");
			}else{
				/*@SuppressWarnings("unchecked")
				Object[] objects=JSON.toObject(JSON.toJosn(result.get("results")), Object[].class);*/
				System.out.println(JSON.toJosn(result.get("results")));
				
				@SuppressWarnings("unchecked")
				Map<String, Object> map=JSON.toObject(result.get("result").toString(),Map.class);
			}
			
		}
		
		
		return ReturnUtil.returnMap(1, null);
	
		
	}


	@Override
	public Map<String, Object> queryStationListByLocation(BigDecimal lat,
			BigDecimal lng) {
	
		String hql="from Station";
		@SuppressWarnings("unchecked")
		List<Station> stations=commonDao.queryByHql(hql);
		
		for(Station station:stations){
			Map<String,String> accessoryParam = new HashMap<String, String>();
			
			String url=ConstantPOI.DIRECTION_URL+"?";
			url+="&origin="+lat+","+lng;
			url+="&destination="+station.getLat()+","+station.getLng();
			url+="&mode=driving";
			url+="&region=西藏";
			url+="&origin_region=西藏";
			url+="&destination_region=西藏";
			url+="&output=json";
			url+="&ak="+ConstantPOI.AK;
			
			@SuppressWarnings("unchecked")
			Map<String, Object> result= (Map<String, Object>) rmiUtil.loadData(url,accessoryParam);
			
			if((Integer)result.get("status")!=0){
				return ReturnUtil.returnMap(0, "计算距离数据失败:");
			}else{
				/*@SuppressWarnings("unchecked")
				Object[] objects=JSON.toObject(JSON.toJosn(result.get("results")), Object[].class);*/
				System.out.println(JSON.toJosn(result.get("results")));
			}
			
		}
		
		
		return ReturnUtil.returnMap(1, null);
	}

}
