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
	
	@RequestMapping(value="/queryWeixinUserInfolList")
	@ResponseBody
	private Map<String,Object> queryWeixinUserInfoList(int page,int rows,String nickname){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return weixinUserInfoSvc.queryWeixinUserInfoList(offset, pagesize,nickname);
	}
	
}
