package xyz.util;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.model.wechat.request.AccessToken;
import xyz.model.wechat.request.Button;
import xyz.model.wechat.request.CommonButton;
import xyz.model.wechat.request.ComplexButton;
import xyz.model.wechat.request.LocationSelect;
import xyz.model.wechat.request.Menu;
import xyz.model.wechat.request.ViewButton;

/**
 * 菜单管理器
 * @author Administrator
 *
 */
public class MenuManager {
	private static Logger log = LoggerFactory.getLogger(MenuManager.class);  
	  
    public static void main(String[] args) throws UnsupportedEncodingException {  
        // 调用接口获取access_token  
        AccessToken at = WeixinUtil.getAccessToken(WeixinUtil.appid, WeixinUtil.appsecret);  
  
        if (null != at) {  
            // 调用接口创建菜单  
            int result = WeixinUtil.createMenu(getMenu(), at.getToken());  
            System.out.println("result="+result);
            // 判断菜单创建结果  
            if (0 == result) { 
                log.info("菜单创建成功！");  
                System.out.println("创建成功");
            }else{  
            	System.out.println("创建失败");
                log.info("菜单创建失败，错误码：" + result);  
            }    
          }  
    }  
  
    /** 
     * 组装菜单数据 
     *  
     * @return 
     * @throws UnsupportedEncodingException 
     */  
    private static Menu getMenu() throws UnsupportedEncodingException {  
        
//        ComplexButton mainBtn1 = new ComplexButton();  
//        mainBtn1.setName("上海潘博");  
//        mainBtn1.setSub_button(new CommonButton[] { btn11, btn12, btn13, btn14 });  
  
//        ComplexButton mainBtn2 = new ComplexButton();  
//        mainBtn2.setName("HELP");  
//        mainBtn2.setSub_button(new CommonButton[] { btn21, btn22, btn23, btn24, btn25 });  
  
//        ComplexButton mainBtn3 = new ComplexButton();  
//        mainBtn3.setName("不要点击");  
//        mainBtn3.setSub_button(new CommonButton[] { btn31, btn32, btn33 });  
  
        /** 
         *  
         * 某个一级菜单下没有二级菜单的情况
         * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 }); 
         */  
    	 ViewButton btn1 = new ViewButton();  
         btn1.setName("商城");  
         btn1.setType("view"); 
         String redirectUrl = "http://testdis.oicp.net/xztrip/WeChatWS/mall.xyz?action=mallHomepage";
         String url = WeixinUtil.user_accredit.replace("APPID", WeixinUtil.appid).replace("REDIRECT_URI", redirectUrl).replace("SCOPE", "snsapi_userinfo");
         btn1.setUrl(url);
		
         LocationSelect btn2 = new LocationSelect();  
         btn2.setName("遇险求助");  
         btn2.setType("location_select");  
         btn2.setKey("2");
         
         CommonButton btn31 = new CommonButton();  
         btn31.setName("模板消息");  
         btn31.setType("click");  
         btn31.setKey("31");
         
         CommonButton btn32 = new CommonButton();  
         btn32.setName("修改绑定");  
         btn32.setType("click");  
         btn32.setKey("32");
         
         CommonButton btn33 = new CommonButton();  
         btn33.setName("图文消息");  
         btn33.setType("click");  
         btn33.setKey("33");
         
         ComplexButton btn3 = new ComplexButton();
         btn3.setName("更多功能");
         btn3.setSub_button(new CommonButton[]{btn31,btn32,btn33});
         
        Menu menu = new Menu();  
//        menu.setButton(new Button[] { mainBtn1, mainBtn2,mainBtn3 });  
        menu.setButton(new Button[]{btn1,btn2,btn3});
  
        return menu;  
    }  
}
