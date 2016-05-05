package xyz.ctrl.wechat;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.filter.JSON;
import xyz.filter.ReturnUtil;
import xyz.model.wechat.WeixinUserInfo;
import xyz.svc.wechat.SeekHelpSvc;
import xyz.svc.wechat.WeixinUserInfoSvc;
import xyz.util.HttpUtil;
import xyz.util.StringTool;

@Controller
@RequestMapping(value="/WeixinWS")
public class WeixinWS {
	
	@Autowired
	private WeixinUserInfoSvc weixinUserInfoSvc;
	
	@Autowired
	private SeekHelpSvc seekHelpSvc;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="xztrip")
	@ResponseBody
	public Map<String,Object> xztrip(HttpServletRequest request,HttpServletResponse response){
		String requestData = HttpUtil.parseHttpStream(request);
		System.out.println("dataStr===="+requestData);
		if("".equals(requestData)){
			return ReturnUtil.returnMap(0, "参数丢失！");
		}
		Map<String,Object> jsonMap = JSON.toObject(requestData, Map.class);
		String msgType = StringTool.getStringByMap(jsonMap, "MsgType");  //消息类型
		String event = StringTool.getStringByMap(jsonMap, "Event");  
		String eventKey = StringTool.getStringByMap(jsonMap, "EventKey");
		String fromUserName = StringTool.getStringByMap(jsonMap, "FromUserName");
		String toUserName =  StringTool.getStringByMap(jsonMap, "ToUserName");
		
		{
			if(jsonMap.get("WeixinUser") != null){
				Map<String,Object> userMap = (Map<String, Object>) jsonMap.get("WeixinUser");
				weixinUserInfoSvc.addOrUpdateWeixinUser(userMap);
			}
		}
		
		if("event".equals(msgType)){//推送事件
			Map<String,Object> map = null;
			if("VIEW".equals(event)){
				
			}else if("CLICK".equals(event)){
				if("modify".equals(eventKey)){
					Map<String,Object> weixinUserInfoMap = weixinUserInfoSvc.getWeixinUserInfo(fromUserName);
					map = new HashMap<String,Object>();
					map.put("toUserName", fromUserName);
					map.put("fromUserName",toUserName);
					if("0".equals(weixinUserInfoMap.get("status").toString())){
						return ReturnUtil.returnMap(0, null);
					}
					
					WeixinUserInfo weixinUserInfo = (WeixinUserInfo) weixinUserInfoMap.get("content");
					if(weixinUserInfo.getLinkPhone() == null || "".equals(weixinUserInfo.getLinkPhone())){
						map.put("content", "您还没绑定电话不能修改，绑定电话请回复文字：“手机号+您的联系电话”。\n示例：手机号15700000000");
					}else{
						map.put("content", "修改电话请回复文字：“手机号+您的联系电话”。\n示例：手机号15700000000");
					}
					return ReturnUtil.returnMap(1, map);
				}
			}else if("subscribe".equals(event)){//关注公众号
				map = new HashMap<String,Object>();
				map.put("toUserName", fromUserName);
				map.put("fromUserName",toUserName);
				map.put("content", "欢迎关注西藏智慧旅游！为了您能够享受到更好地服务，我们建议您<a href=\"http://www.xingzang.net/wx/page/bind.html?openid="+fromUserName+"\">绑定账户</a>");
				return ReturnUtil.returnMap(1, map);
			}else if("unsubscribe".equals(event)){	//取消关注
				return ReturnUtil.returnMap(0, null);
			}else if("location_select".equals(event)){	
				seekHelpSvc.addSeekHelp(fromUserName,
						StringTool.getMapByMap(jsonMap, "SendLocationInfo").get("Location_X").toString(), 
						StringTool.getMapByMap(jsonMap, "SendLocationInfo").get("Location_Y").toString(), 
						StringTool.getMapByMap(jsonMap, "SendLocationInfo").get("Label").toString(), 
						StringTool.getMapByMap(jsonMap, "SendLocationInfo").get("Scale").toString(),
						eventKey,
						StringTool.getStringByMap(jsonMap, "CreateTime"),null);
			}
		}else if("location".equals(msgType)){	//location事件
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("toUserName", fromUserName);
			map.put("fromUserName", toUserName);
			Map<String,Object> weixinUserInfoMap = weixinUserInfoSvc.getWeixinUserInfo(fromUserName);
			if("1".equals(weixinUserInfoMap.get("status").toString())){
				WeixinUserInfo weixinUserInfo = (WeixinUserInfo) weixinUserInfoMap.get("content");
				if(weixinUserInfo.getLinkPhone() == null || "".equals(weixinUserInfo.getLinkPhone())){
					map.put("content", "请先提供您的联系电话，再点击求助菜单，否则我们无法与您取得联系！请回复文字：“手机号+您的联系电话”。\n示例：手机号15700000000\n\n若您未点击【遇险求助】菜单，请忽略该信息。");
				}else{
					map.put("content", "救助已发送成功，我们将尽快联系您，您的电话是【"+weixinUserInfo.getLinkPhone()+"】，请保持手机畅通；若联系电话有变更，请回复文字：“手机号+您的联系电话”。若您未点击【遇险求助】菜单，请忽略该信息。");
				}
			}
			return ReturnUtil.returnMap(1, map);
		}else if("text".equals(msgType)){
			Map<String, Object> map = new HashMap<String,Object>();
			String content = StringTool.getStringByMap(jsonMap, "Content").replaceAll(" ", "");	//发送内容
			String example = "手机号15700001111";	//示例
			String regex = "^1{1}\\d{10}$";	//验证手机
			
			map.put("toUserName", fromUserName);
			map.put("fromUserName", toUserName);
			if(content.length() == example.length()){
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(content.substring(3, content.length()));
				if("手机号".equals(content.substring(0, 3)) && matcher.matches()){	//格式匹配
					String phone = content.substring(3, content.length());
					Map<String,Object> result = weixinUserInfoSvc.editWeixinUser(fromUserName,phone);
					if("1".equals(result.get("status").toString())){	//绑定成功
						map.put("content", "恭喜你绑定成功！");
					}else{	//绑定失败
						map.put("content", "绑定失败，请重试！");
					}
				}else{	//格式不匹配
					map.put("content", "你输入的格式不对，示例：手机号15700000000");
				}
			}else if("测试".equals(content)){
				map.put("content", "点击【<a href=\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx30eaaeefedd9d0a9&redirect_uri=http://wx.duanyi.com.cn/weixinServer/WeixinMsgWS/receiveMsg.xyz?action=webAuth&response_type=code&scope=snsapi_userinfo&state=http://www.xingzang.net/wx/index.html?xztrip#wechat_redirect\">测试</a>】进入");
			}
			return ReturnUtil.returnMap(1, map);
		}
		return ReturnUtil.returnMap(0, null);
	}
	
}
