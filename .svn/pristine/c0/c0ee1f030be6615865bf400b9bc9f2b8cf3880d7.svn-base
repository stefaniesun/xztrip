package xyz.svc.wechat.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.model.member.WeixinUserInfo;
import xyz.svc.wechat.WeixinUserInfoSvc;

@Service
public class WeixinUserInfoSvcImp implements WeixinUserInfoSvc {

	@Autowired
	CommonDao commonDao;
	
	@Override
	public Map<String, Object> queryWeixinUserInfoList(int offset,int pagesize,String nickname) {
		String hql = "from WeixinUserInfo where 1=1";
		if(nickname != null && !"".equals(nickname)){
			hql += " and nickname like '%"+nickname+"%'";
		}
		
		String countHql = "select count(iidd)"+hql;
		Query countQuery = commonDao.getQuery(countHql);
		Number countNum = (Number) countQuery.uniqueResult();
		int count = countNum==null?0:countNum.intValue();
		
		Query query = commonDao.getQuery(hql);
		query.setMaxResults(pagesize);
		query.setFirstResult(offset);
		@SuppressWarnings("unchecked")
		List<WeixinUserInfo> weixinUserInfo = query.list();
		
		Map<String,Object> mapContent = new HashMap<String,Object>();
		mapContent.put("total", count);
		mapContent.put("rows", weixinUserInfo);
		return ReturnUtil.returnMap(1, mapContent);
	}

	@Override
	public Map<String, Object> getWeixinUserInfo(String openid,String account) {
		if(openid == null && account == null){
			return ReturnUtil.returnMap(0, "缺少参数！");
		}
		
		WeixinUserInfo  weixinUserInfo;
		if(openid != null && !"".equals(openid)){
			weixinUserInfo = (WeixinUserInfo)commonDao.getObjectByUniqueCode("WeixinUserInfo", "openid", openid);
		}else{
			weixinUserInfo = (WeixinUserInfo)commonDao.getObjectByUniqueCode("WeixinUserInfo", "account", account);
		}
		
		if(weixinUserInfo == null){
			return ReturnUtil.returnMap(0, "该用户不存在！");
		}
		return ReturnUtil.returnMap(1, weixinUserInfo);
	}

	@Override
	public Map<String, Object> addWeixinUser(WeixinUserInfo weixinUserInfo) {
		if(weixinUserInfo == null){
			return ReturnUtil.returnMap(0, "缺少参数！");
		}
		
		WeixinUserInfo weixinUser = (WeixinUserInfo)commonDao.getObjectByUniqueCode("WeixinUserInfo", "openid", weixinUserInfo.getOpenid());
		if(weixinUser != null){
			weixinUser.setSubscribe(weixinUserInfo.getSubscribe());
			weixinUser.setSubscribe_time(weixinUserInfo.getSubscribe_time());
			weixinUser.setNickname(weixinUserInfo.getNickname());
			weixinUser.setSex(weixinUserInfo.getSex());
			weixinUser.setLanguage(weixinUserInfo.getLanguage());
			weixinUser.setCity(weixinUserInfo.getCity());
			weixinUser.setProvince(weixinUserInfo.getProvince());
			weixinUser.setCountry(weixinUserInfo.getCountry());
			weixinUser.setHeadimgurl(weixinUserInfo.getHeadimgurl());
			commonDao.save(weixinUser);
		}else{
			commonDao.save(weixinUserInfo);
		}
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String,Object> editWeixinUser(String openid,String account,String longitude,String latitude,String positionTime){
		if(openid == null && "".equals(openid)){
			return ReturnUtil.returnMap(0, "缺少参数！");
		}
		
		WeixinUserInfo weixinUserInfo = (WeixinUserInfo)commonDao.getObjectByUniqueCode("WeixinUserInfo", "openid", openid);
		if(weixinUserInfo != null){
			if(account != null && !"".equals(account)){		//保存erp账号
				WeixinUserInfo wui = (WeixinUserInfo)commonDao.getObjectByUniqueCode("WeixinUserInfo", "account", account);
				if(wui != null){
					wui.setAccount("");
					commonDao.update(wui);
				}
				weixinUserInfo.setAccount(account);
			}else{	//取消关注
				weixinUserInfo.setSubscribe("0");
			}
			
			if(longitude != null && latitude != null && positionTime != null){
				weixinUserInfo.setLongitude(longitude);
				weixinUserInfo.setLatitude(latitude);
				weixinUserInfo.setPositionTime(positionTime);
			}
			commonDao.update(weixinUserInfo);
			return ReturnUtil.returnMap(1, null);
		}else{
			return ReturnUtil.returnMap(0, "用户不存在！");
		}
	}
}
