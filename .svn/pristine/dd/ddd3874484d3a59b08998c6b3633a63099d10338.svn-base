package xyz.svc.wechat;

import java.util.Map;

import xyz.model.member.WeixinUserInfo;


public interface WeixinUserInfoSvc {

	/**
	 * 查询用户信息列表
	 * @param offset
	 * @param pagesize
	 * @param nickname 用户昵称
	 * @return
	 */
	public Map<String,Object> queryWeixinUserInfoList(int offset, int pagesize,String nickname);
	
	/**
	 * 查询用户信息
	 * @param openid	用户微信id
	 * @param openid ERP账号
	 * @return
	 */
	public Map<String,Object> getWeixinUserInfo(String openid,String account);
	
	
	/**
	 * 添加用户信息
	 * @param weixinUserInfo
	 * @return
	 */
	public Map<String,Object> addWeixinUser(WeixinUserInfo weixinUserInfo);
	
	/**
	 * 修改用户信息
	 * @param openid
	 * @param account erp账号
	 * @param longitude 经度
	 * @param latitude  纬度
	 * @param positionTime 地理位置上传时间
	 * @return
	 */
	public Map<String,Object> editWeixinUser(String openid,String account,String longitude,String latitude,String positionTime);
}


