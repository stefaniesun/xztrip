package xyz.svc.wechat.imp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.filter.JSON;
import xyz.model.member.WeixinUserInfo;
import xyz.model.wechat.request.AccessToken;
import xyz.model.wechat.response.TextMessage;
import xyz.svc.member.MemberSvc;
import xyz.svc.wechat.WeChatSvc;
import xyz.util.MessageUtil;
import xyz.util.WeixinUtil;

@Service
public class WeChatSvcImp implements WeChatSvc {
	
	@Autowired
	MemberSvc memberSvc;
	
	@Override
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String token="xztrip";	//标识符
	    String signature =  request.getParameter("signature");		//微信加密签名
	    String timestamp = request.getParameter("timestamp");	//时间戳
	    String nonce = request.getParameter("nonce");		//随机数
	    String echostr = request.getParameter("echostr");	//随机字符串
	    
        boolean result =  WeixinUtil.checkSignature(token, signature,timestamp,nonce);
        PrintWriter out = response.getWriter();
	    if(result)
			out.write(echostr);
	    else
			out.write("");
	    out.close();
	}
	
	
	@Override
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws DocumentException, IOException{
		request.setCharacterEncoding("UTF-8");  
		response.setCharacterEncoding("UTF-8");	//防止中文乱码
		Map<String,Object> requestMap = MessageUtil.parseXml(request);		//请求相关信息
		String msgType = requestMap.get("MsgType").toString();	//消息类型
		
		if(msgType != null && !"".equals(msgType)){
			if(MessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(msgType)){		//接收的text类型
				handleTextMessage(request,response,requestMap);
			}else if(MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgType)){	//接收的event类型
				
				String event = requestMap.get("Event").toString();	//事件类型
				String eventKey = requestMap.get("EventKey").toString();
				
				if("subscribe".equals(event)){		//订阅公众号
					String accessToken = WeixinUtil.getAccessToken(WeixinUtil.appid, WeixinUtil.appsecret).getToken();	//获取token
					String requestUrl = WeixinUtil.get_user_info.replace("ACCESS_TOKEN", accessToken).replace("OPENID", requestMap.get("FromUserName").toString());
					JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl,"GET",null);	//获取关注者的用户信息
					
					
					WeixinUserInfo weixinUserInfo = new WeixinUserInfo();
					weixinUserInfo.setSubscribe(jsonObject.getString("subscribe"));
					weixinUserInfo.setOpenid(jsonObject.getString("openid"));
					weixinUserInfo.setNickname(jsonObject.getString("nickname"));
					weixinUserInfo.setSex(jsonObject.getString("sex"));
					weixinUserInfo.setCity(jsonObject.getString("city"));
					weixinUserInfo.setCountry(jsonObject.getString("country"));
					weixinUserInfo.setProvince(jsonObject.getString("province"));
					weixinUserInfo.setLanguage(jsonObject.getString("language"));
					weixinUserInfo.setRemark(jsonObject.getString("remark"));
					weixinUserInfo.setHeadimgurl(jsonObject.getString("headimgurl"));
					weixinUserInfo.setSubscribe_time(jsonObject.getString("subscribe_time"));
					weixinUserInfo.setUnionid("");
					weixinUserInfo.setGroupid(jsonObject.getString("groupid"));
					System.out.println("unionid="+jsonObject.get("unionid"));
					
					Map<String, Object> map = memberSvc.addWeixinUserInfo(weixinUserInfo);
					System.out.println(JSON.toJosn(map));
					
					System.out.println("unionId="+jsonObject.get("unionid"));
					
					@SuppressWarnings("rawtypes")
					Iterator it = jsonObject.keys();
					while(it.hasNext()){
						String key = (String)it.next();
						String value = jsonObject.getString(key);
						System.out.println("key="+key+";value="+value);
					}
					
					StringBuffer sb = new StringBuffer();
					sb.append("欢迎关注西藏旅游！！！为了让你能够更好的享受到我们的服务，我们强烈建议你");
					sb.append("<a href=\"http://testdis.oicp.net/xztrip/mall/bindUserInfo.html\">完善信息</a>");
					sb.append("，我们将竭诚为你服务。");
					
					PrintWriter out = response.getWriter();
					TextMessage textMessage = MessageUtil.returnTextMessage(requestMap.get("FromUserName").toString(), requestMap.get("ToUserName").toString(), sb.toString());
					String responseMessgae  = MessageUtil.textMessageToXml(textMessage);
					out.write(responseMessgae);
					out.close();
					
				}else if("location_select".equals(event) && "2".equals(eventKey)){		//我要求助菜单
					PrintWriter out = response.getWriter();
					
					TextMessage textMessage = new TextMessage();
					textMessage.setFromUserName(requestMap.get("ToUserName").toString());
					textMessage.setCreateTime(new Date().getTime());
					textMessage.setToUserName(requestMap.get("FromUserName").toString());
					textMessage.setMsgType("text");
					textMessage.setFuncFlag(0);
					textMessage.setContent("已收到你的求助信息，请呆在你现在所处位置(经度: 纬度:)保持镇静,救援队伍将马上前往");
					String responseMessgae  = MessageUtil.textMessageToXml(textMessage);
					out.write(responseMessgae);
					out.close();
					System.out.println("......1111");
				}
			}
		}
	}
	
	/**
	 * 响应用户发送的文本信息
	 * @throws IOException 
	 */
	private void handleTextMessage(HttpServletRequest request,HttpServletResponse response,Map<String,Object> requestMap) throws IOException{
		String fromUserName = requestMap.get("FromUserName").toString();	//发送方账号
		String toUserName = requestMap.get("ToUserName").toString();	//公众号账号
		String msgType = requestMap.get("MsgType").toString();	//消息类型
		PrintWriter out = response.getWriter();
		
		TextMessage textMessage = new TextMessage();
		textMessage.setFromUserName(toUserName);
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setToUserName(fromUserName);
		textMessage.setMsgType(msgType);
		textMessage.setContent("hello world");
		textMessage.setFuncFlag(0);
		
		String responseMessgae = MessageUtil.textMessageToXml(textMessage);
		out.write(responseMessgae);
		out.close();
	}
	
	private void handleClickMessage(HttpServletRequest request,HttpServletResponse response,Map<String,String> requestMap){
		
	}
}





