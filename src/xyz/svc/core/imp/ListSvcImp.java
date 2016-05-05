package xyz.svc.core.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.filter.RmiUtil;
import xyz.svc.config.ConstantPms;
import xyz.svc.core.ListSvc;
import xyz.svc.main.config.ConstantProduct;
import xyz.util.ListNumberCode;



@Service
public class ListSvcImp  implements ListSvc{
	
	@Autowired
	CommonDao commonDao;
	
	@Autowired
	RmiUtil rmiUtil;
	
	@Override
	public Map<String, Object> getSecurityUserList(){
		String sql = "SELECT p.username AS value,p.nick_name AS text FROM security_user p ";
		SQLQuery query = commonDao.getSqlQuery(sql);
		query.addScalar("value").
		addScalar("text").
		setResultTransformer(Transformers.aliasToBean(ListNumberCode.class));
		
		@SuppressWarnings("unchecked")
		List<ListNumberCode> results = query.list();
		return ReturnUtil.returnMap(1, results);
	}

	@Override
	public Map<String, Object> getProviderList(String type) {
		String sql="select name_cn AS text, number_code AS value from provider where 1=1 ";
		if(type!=null&&!"".equals(type)){
			if(ConstantProduct.SCENIC.equals(type)){
				sql+=" and is_scenic=1 ";
			}else if(ConstantProduct.HOTEL.equals(type)){
				sql+=" and is_hotel=1 ";
			}else if(ConstantProduct.CAR.equals(type)){
				sql+=" and is_car=1 ";
			}else if(ConstantProduct.SPECIALTY.equals(type)){
				sql+=" and is_specialty=1 ";
			}
		}
		SQLQuery query=commonDao.getSqlQuery(sql);
		query.addScalar("text").addScalar("value").
		setResultTransformer(Transformers.aliasToBean(ListNumberCode.class));
		@SuppressWarnings("unchecked")
		List<ListNumberCode> providerList=query.list();
		return ReturnUtil.returnMap(1, providerList);
	}

	@Override
	public Map<String, Object> getPmsHotelList() {
		Map<String,String> accessoryParam = new HashMap<String, String>();
		@SuppressWarnings("unchecked")
		Map<String, Object> tempMap = (Map<String, Object>)rmiUtil.loadData(ConstantPms.pms_getHotelList,accessoryParam);
		return tempMap;
	}

	@Override
	public Map<String, Object> getPmsRoomTypeList(String hotelid) {
		Map<String,String> accessoryParam = new HashMap<String, String>();
		accessoryParam.put("hotelid", hotelid);
		@SuppressWarnings("unchecked")
		Map<String, Object> tempMap = (Map<String, Object>)rmiUtil.loadData(ConstantPms.pms_getHotelRoomTypeByhotelid,accessoryParam);
		return tempMap;
	}

	@Override
	public Map<String, Object> getUserTagList() {
		String sql="select name_cn AS text, number_code AS value from user_tag where 1=1 ";
		SQLQuery query=commonDao.getSqlQuery(sql);
		query.addScalar("text").addScalar("value").
		setResultTransformer(Transformers.aliasToBean(ListNumberCode.class));
		@SuppressWarnings("unchecked")
		List<ListNumberCode> providerList=query.list();
		return ReturnUtil.returnMap(1, providerList);
	}

	@Override
	public Map<String, Object> getRegionList() {
		String sql="select name_cn AS text, number_code AS value from region where 1=1 ";
		SQLQuery query=commonDao.getSqlQuery(sql);
		query.addScalar("text").addScalar("value").
		setResultTransformer(Transformers.aliasToBean(ListNumberCode.class));
		@SuppressWarnings("unchecked")
		List<ListNumberCode> providerList=query.list();
		return ReturnUtil.returnMap(1, providerList);
	}
}

