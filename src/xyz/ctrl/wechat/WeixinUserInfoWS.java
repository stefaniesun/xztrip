package xyz.ctrl.wechat;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.svc.wechat.WeixinUserInfoSvc;


/**
 * 微信用户信息
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/WeixinUserInfoWS")
public class WeixinUserInfoWS {

	@Autowired
	private WeixinUserInfoSvc weixinUserInfoSvc;
	/**
	 * 微信平台快速用户登录
	 */
	@RequestMapping(value="openidLogin")
	@ResponseBody
	public Map<String, Object> openidLogin(String openid){
		return weixinUserInfoSvc.openidLoginOper(openid);
	}
	
	/**
	 * 绑定用户openid
	 */
	@RequestMapping(value="bindOpenid")
	@ResponseBody
	public Map<String, Object> bindOpenid(String openid, String username, String password){
		return weixinUserInfoSvc.bindOpenidOper(openid, username, password);
	}
}
