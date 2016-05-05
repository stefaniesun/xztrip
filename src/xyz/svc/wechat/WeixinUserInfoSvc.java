package xyz.svc.wechat;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface WeixinUserInfoSvc {
	/**
	 * 查询用户信息
	 * @param openid	用户微信id
	 * @param openid ERP账号
	 * @return
	 */
	public Map<String,Object> getWeixinUserInfo(String openid);
	
	public Map<String,Object> addOrUpdateWeixinUser(Map<String,Object> userInfo);
	
	/**
	 * 修改用户信息
	 * @param openid
	 * @param account erp账号
	 * @param linkPhone 联系电话
	 * @return
	 */
	public Map<String,Object> editWeixinUser(String openid,String linkPhone);
	
	
	
	
	/**
	 * 微信平台快速登录，只需要用户名即可登录
	 * @param username
	 * @return
	 */
	public Map<String, Object> openidLoginOper(String openid);
	
	public Map<String, Object> bindOpenidOper(String openid, String username, String password);
}


