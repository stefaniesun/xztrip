package xyz.ctrl.wechat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.model.member.WeixinUserInfo;
import xyz.model.wechat.response.Article;
import xyz.model.wechat.response.NewsMessage;
import xyz.model.wechat.response.TextMessage;
import xyz.svc.core.SeekHelpSvc;
import xyz.svc.member.MemberSvc;
import xyz.util.MessageUtil;
import xyz.util.WeixinUtil;


@Controller
@RequestMapping(value="/WeChatWS")
public class WeChatWS {
	
	Map<String,String> location_select = new HashMap<String,String>();	//存储location_select事件创建时间 key:openid  value:createtime
	
	@Autowired
	private MemberSvc memberSvc;
	@Autowired
	private SeekHelpSvc seekHelpSvc;
	
	/**
	 * 微信商城
	 * @throws Exception 
	 */
	@RequestMapping(value="mall")
	@ResponseBody
	public void weChatMall(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String type = request.getMethod();	//请求类型
		if("GET".equals(type))
			doGet(request, response);
		else
			doPost(request,response);
	}
	
	
	/**
	 * 处理get请求
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		String token="xztrip";	//标识符
	    String signature =  request.getParameter("signature");		//微信加密签名
	    String timestamp = request.getParameter("timestamp");	//时间戳
	    String nonce = request.getParameter("nonce");		//随机数
	    String echostr = request.getParameter("echostr");	//随机字符串
		String action = request.getParameter("action");
		
		if(action != null && "mallHomepage".equals(action))
			mallHomepage(request,response);
		else
			weixinValidate(response,token,signature,timestamp,nonce,echostr);
	}
	
	
	/**
	 * 处理post请求(处理用户发送的请求)
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws DocumentException, IOException{
		request.setCharacterEncoding("UTF-8");  
		response.setCharacterEncoding("UTF-8");	//防止中文乱码
		Map<String,Object> requestMap = MessageUtil.parseXml(request);		//请求相关信息
		String msgType = requestMap.get("MsgType").toString();	//消息类型
		  
		if(MessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(msgType)){		//接收的text类型
			handleTextMessage(request,response,requestMap);
		}else if(MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgType)){	//接收的event类型
			
			String event = requestMap.get("Event").toString();	//事件类型
			String eventKey = requestMap.get("EventKey") == null ? "":requestMap.get("EventKey").toString();
			
			if("subscribe".equals(event)){		//订阅公众号
				handleSubscribeMessage(request,response,requestMap);
			}else if("location_select".equals(event) && "2".equals(eventKey)){		//我要求助菜单
				
				@SuppressWarnings("unchecked")
				Map<String, Object> infoMap = (Map<String, Object>)requestMap.get("SendLocationInfo");
				
				String label = infoMap.get("Label").toString();
				String scale = infoMap.get("Scale").toString();
				String location_X = infoMap.get("Location_X").toString();
				String location_Y = infoMap.get("Location_Y").toString();
				
				Map<String,Object> returnInfo = seekHelpSvc.addSeekHelp(requestMap.get("FromUserName").toString(), location_X, location_Y, label, scale);
				if((Integer)returnInfo.get("status") == 1)
					location_select.put(requestMap.get("FromUserName").toString(),requestMap.get("CreateTime").toString()+"-scuess-"+returnInfo.get("content"));
				else
					location_select.put(requestMap.get("FromUserName").toString(),requestMap.get("CreateTime").toString()+"-fail-"+returnInfo.get("content"));
			}else if("VIEW".equals(event)){	//商城菜单
			
			}else if("CLICK".equals(event) && "31".equals(eventKey)){		//消息模板
				/**
		    	 * jsonText参数说明：
		    	 * 		OPENID：用户微信id
		    	 * 		TEMPLATE_ID：模板消息id
		    	 * 		URL：模板消息详情跳转页面
		    	 */
				String jsonText = "{\"touser\":\"OPENID\",\"template_id\":\"TEMPLATE_ID\",\"url\":\"URL\","+
											"\"topcolor\":\"#FF0000\",\"data\":{\"first\":{\"value\":\"firstVALUE\",\"color\":\"blue\"},"+
											"\"ticketNum\": {\"value\":\"ticketNumVALUE\",\"color\":\"#173177\"},"+
											"\"name\":{\"value\":\"nameVALUE\",\"color\":\"#173177\"},"+
											"\"product\":{\"value\":\"productVALUE\",\"color\":\"#173177\"},"+
											"\"number\":{\"value\":\"numberVALUE\",\"color\":\"#173177\"},"+
											"\"remindReason\":{\"value\":\"remindReasonVALUE\",\"color\":\"#173177\"},"+
											"\"time\": {\"value\":\"timeVALUE\",\"color\":\"#173177\"}}}";                                                                                             

				Map<String,String> dataMap = new HashMap<String,String>();
				dataMap.put("firstVALUE", "订单消息");
				dataMap.put("ticketNumVALUE", "ABCP4456716666666");
				dataMap.put("nameVALUE", "南京上好假期国旅专营店");
				dataMap.put("productVALUE", "雪狼湖门票");
				dataMap.put("numberVALUE", "2");
				dataMap.put("remindReasonVALUE", "订单出票");
				dataMap.put("timeVALUE", "2016-02-29");
				
		    	String template_id = "cwycKTanbyIII2AWYYwFYTefVJw6M27iaiitxfsyem4	";
		    	String redirectUrl = "http://testdis.oicp.net/xztrip/WeChatWS/mall.xyz?action=mallHomepage";
		    	String url= WeixinUtil.user_accredit.replace("APPID", WeixinUtil.appid).replace("REDIRECT_URI", redirectUrl).replace("SCOPE", "snsapi_userinfo");
		    	System.out.println("跳转URL="+url);
		    	String jsonData = jsonText.replace("OPENID", requestMap.get("FromUserName").toString()).replace("TEMPLATE_ID", template_id.trim())
											.replace("URL", url).replace("firstVALUE", dataMap.get("firstVALUE")).replace("ticketNumVALUE", dataMap.get("ticketNumVALUE"))
											.replace("nameVALUE", dataMap.get("nameVALUE")).replace("productVALUE", dataMap.get("productVALUE"))
											.replace("numberVALUE", dataMap.get("numberVALUE")).replace("remindReasonVALUE", dataMap.get("remindReasonVALUE"))
											.replace("timeVALUE", dataMap.get("timeVALUE"));
				System.out.println("jsonData============"+jsonData);
				int result = WeixinUtil.sendTemplateMessage(jsonData);
				if(result == 1)
					System.out.println("发送成功！");
				else 
					System.out.println("发送失败！");
			}else if("CLICK".equals(event) && "32".equals(eventKey)){		//修改绑定
				
			}else if("CLICK".equals(event) && "33".equals(eventKey)){		//图文消息
				NewsMessage newsMessage = new NewsMessage(); 
				List<Article> articleList = new ArrayList<Article>();
				
				Article article1 = new Article();
				article1.setTitle("西藏微信商城");
				article1.setDescription("这是西藏微信商城哦");
				article1.setPicUrl("http://testdis.oicp.net/xztrip/wx/images/xzmall.jpg");
				article1.setUrl("http://testdis.oicp.net/xztrip/wx/index.html");
				
				Article article2 = new Article();
				article2.setTitle("上海潘博网络科技有限公司");
				article2.setDescription("潘博");
				article2.setPicUrl("http://testdis.oicp.net/xztrip/wx/images/kobe.jpg");
				article2.setUrl("http://www.itacc.com.cn/");
				
				articleList.add(article1);
				articleList.add(article2);
				newsMessage.setToUserName(requestMap.get("FromUserName").toString());
				newsMessage.setFromUserName(requestMap.get("ToUserName").toString());
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);
				newsMessage.setArticleCount(articleList.size());  
                newsMessage.setArticles(articleList);  
                String respMessage = MessageUtil.newsMessageToXml(newsMessage);
                
                PrintWriter out = response.getWriter();
                out.write(respMessage);
                out.close();
			}
		}else if(MessageUtil.REQ_MESSAGE_TYPE_LOCATION.equals(msgType)){	//接收的location类型
			if(location_select.get(requestMap.get("FromUserName")) != null){		//处理location_select类型
				int location_select_time = Integer.parseInt(location_select.get(requestMap.get("FromUserName")).split("-")[0]);
				int location_time = Integer.parseInt(requestMap.get("CreateTime").toString());
				if(location_time - location_select_time < 10){
					PrintWriter out = response.getWriter();
					String fromUserName = requestMap.get("FromUserName").toString();
					String toUserName = requestMap.get("ToUserName").toString();
					TextMessage textMessage = null;
					String contents[] = location_select.get(requestMap.get("FromUserName")).split("-");
					textMessage = MessageUtil.returnTextMessage(fromUserName, toUserName, contents[contents.length-1]);
					String responseMessage = MessageUtil.textMessageToXml(textMessage);
					out.write(responseMessage);
					out.close();
					location_select.remove(requestMap.get("FromUserName"));
				}
			}
		}	
	}
	
	/**
	 * 处理用户关注公众号
	 * @param request
	 * @param response
	 * @param requestMap
	 * @throws IOException 
	 */
	private void handleSubscribeMessage(HttpServletRequest request,HttpServletResponse response,Map<String,Object> requestMap) throws IOException{
		String accessToken = WeixinUtil.getAccessToken(WeixinUtil.appid, WeixinUtil.appsecret).getToken();	//获取token
		String requestUrl = WeixinUtil.get_user_info.replaceAll("ACCESS_TOKEN", accessToken).replaceAll("OPENID", requestMap.get("FromUserName").toString());
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
		weixinUserInfo.setUnionid(jsonObject.get("unionid")==null?"":jsonObject.getString("unionid"));
		weixinUserInfo.setGroupid(jsonObject.getString("groupid"));
		
		memberSvc.addWeixinUserInfo(weixinUserInfo);	//保存关注者信息
		
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎关注西藏旅游！！！为了让您能够更好的享受到我们的服务，我们强烈建议您");
		sb.append("绑定手机。<a href=\"http://testdis.oicp.net/xztrip/wx/index.html?openid=OPENID\">点击绑定</a>，");
		sb.append("我们将竭诚为您服务。");
		String  content = sb.toString().replaceAll("OPENID",  requestMap.get("FromUserName").toString());
		
		PrintWriter out = response.getWriter();
		TextMessage textMessage = MessageUtil.returnTextMessage(requestMap.get("FromUserName").toString(), requestMap.get("ToUserName").toString(), content);
		String responseMessgae  = MessageUtil.textMessageToXml(textMessage);
		out.write(responseMessgae);
		out.close();
	}
	
	/**
	 * 响应用户发送的文本信息,
	 * 当发送文本格式为"【绑定或修改手机号】15700001111"时，
	 * 将其视为绑定或修改手机号码
	 * @throws IOException 
	 */
	private void handleTextMessage(HttpServletRequest request,HttpServletResponse response,Map<String,Object> requestMap) throws IOException{
		String fromUserName = requestMap.get("FromUserName").toString();	//发送方账号
		String toUserName = requestMap.get("ToUserName").toString();	//公众号账号
		
		String content = requestMap.get("Content").toString();
		String example = "【绑定或修改手机号】15700001111";	//示例
		String regex = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";	//验证手机
		
		PrintWriter out = response.getWriter();
		TextMessage textMessage = null;
		String responseMessgae = "";
		
		if(content.length() == example.length()){
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(content.substring(10, content.length()));
			if("【绑定或修改手机号】".equals(content.substring(0, 10)) && matcher.matches()){	//格式匹配
				String phone = content.substring(10, content.length());
				Map<String,Object> result = memberSvc.addUserRelation(fromUserName, phone);
				if((Integer)result.get("status") == 1){	//绑定成功
					textMessage = MessageUtil.returnTextMessage(fromUserName, toUserName, "恭喜你绑定成功！");
				}else{	//绑定失败
					textMessage = MessageUtil.returnTextMessage(fromUserName, toUserName, "绑定失败，请重试！");
				}
			}else{	//格式不匹配
				textMessage = MessageUtil.returnTextMessage(fromUserName, toUserName, "你输入的格式不对，示例：“【绑定或修改手机号】15700001111”");
			}
			responseMessgae = MessageUtil.textMessageToXml(textMessage);
			out.write(responseMessgae);
			out.close();
			return;
		}
		
		textMessage = MessageUtil.returnTextMessage(fromUserName, toUserName, "绑定或修改手机号请按以下格式：“【绑定或修改手机号】15700001111”");
		responseMessgae = MessageUtil.textMessageToXml(textMessage);
		out.write(responseMessgae);
		out.close();
	}
	
	
	/**
	 * 微信初次调用验证
	 * @param response
	 * @param token	标识符
	 * @param signature	微信加密签名
	 * @param timestamp		时间戳
	 * @param nonce	随机数
	 * @param echostr	随机字符串
	 * @throws IOException
	 */
	private void weixinValidate(HttpServletResponse response,String token,String signature,String timestamp,String nonce,String echostr) throws IOException{
		boolean result =  WeixinUtil.checkSignature(token, signature,timestamp,nonce);
        PrintWriter out = response.getWriter();
	    if(result)
			out.write(echostr);
	    else
			out.write("");
	    out.close();
	}
	
	/**
	 * 处理微信商城首页请求
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void mallHomepage(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		/**
		 * 微信网页授权步骤：
		 * 		1.用户同意授权，获取code
		 * 		2.通过code换取网页授权access_token
		 * 		3.刷新access_token（如果需要）
		 * 		4.拉取用户信息(需scope为 snsapi_userinfo)
		 */
		
		String code = request.getParameter("code");
		String requestUrl = "";
		
		if(code != null){
			requestUrl = WeixinUtil.webPageAccessToken.replace("APPID", WeixinUtil.appid).replace("SECRET", WeixinUtil.appsecret).replace("CODE", code);;
			JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "POST", null);
			response.sendRedirect("../wx/erp-order-details.html?openid="+jsonObject.getString("openid"));
		}else{
			response.sendRedirect("../wx/erp-order-details.html");
		}
	}
}

