package xyz.svc.wechat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

@Service
public interface WeChatSvc {
	
		/**
		 * 处理get请求(初次调用接口验证接入)
		 * @param request
		 * @param response
		 * @throws IOException 
		 */
		public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException;

		/**
		 * 处理post请求(处理用户发送的请求)
		 * @param request
		 * @param response
		 * @throws IOException 
		 * @throws DocumentException 
		 */
		public void doPost(HttpServletRequest request,HttpServletResponse response) throws DocumentException, IOException;
}
