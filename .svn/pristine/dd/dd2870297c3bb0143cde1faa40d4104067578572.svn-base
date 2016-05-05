package xyz.ctrl.buyer;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.model.member.XyzSessionLogin;
import xyz.svc.buyer.CustomerSvc;


@Controller
@RequestMapping(value="/CustomerWS")
public class CustomerWS {

	@Autowired
	CustomerSvc customerSvc;
	
	/**
	 * 获取验证码
	 */
	@RequestMapping(value="getRandCode")
	@ResponseBody
	public Map<String,Object> getRandCode(String phone,String content){
		return customerSvc.getRandCodeOper(phone);
	}
	
	/**
	 * 验证验证码
	 */
	@RequestMapping(value="verifyRandCode")
	@ResponseBody
	public Map<String,Object> verifyRandCode(String phone,String randCode){
		return customerSvc.verifyRandCodeOper(phone,randCode);
	}
	
	/**
	 * 用户注册
	 */
	@RequestMapping(value="register")
	@ResponseBody
	public Map<String,Object> register(String phone,String password){
		return customerSvc.registerOper(phone,password);
	}
	
	/**
	 * 用户登录
	 */
	@RequestMapping(value="login")
	@ResponseBody
	public Map<String, Object> login(
			String username,
			String password){
		return customerSvc.loginOper(username, password);
	}
	
	/**
	 * 微信平台快速用户登录
	 */
	@RequestMapping(value="wxLogin")
	@ResponseBody
	public Map<String, Object> wxLogin(String username){
		return customerSvc.wxLoginOper(username);
	}
	
	
	/**
	 * 用户修改密码
	 */
	@RequestMapping(value="alterPassword")
	@ResponseBody
	public Map<String, Object> alterPassword(
			String oldPassword,
			String newPassword){
		return customerSvc.alterPasswordOper(oldPassword,newPassword);
	}
	
	
	/**
	 * 用户找回密码
	 */
	@RequestMapping(value="recoverPassword")
	@ResponseBody
	public Map<String, Object> recoverPassword(String username,String newPassword){
		return customerSvc.recoverPasswordOper(username,newPassword);
	}
	
	
	/**
	 * 验证登录
	 * @return
	 */
	@RequestMapping(value="decideLogin")
	@ResponseBody
	public Map<String, Object> decideLogin(){
		XyzSessionLogin xyzSessionLogin = MyRequestUtil.getXyzSessionLogin();
		if(xyzSessionLogin==null){
			return ReturnUtil.returnMap(0, "无有效登录信息");
		}else{
			return ReturnUtil.returnMap(1,xyzSessionLogin);
		}
	}
	
	/**
	 * 用户退出
	 */
	@RequestMapping(value="exit")
	@ResponseBody
	public Map<String, Object> customerExit(){
		return customerSvc.customerExit();
	}
	
	
	/**
	 * 用户修改常用联系人信息
	 */
	@RequestMapping(value="editCustomerLinkInfo")
	@ResponseBody
	public Map<String, Object> editCustomerLinkInfo(String linkman,String linkPhone){
		return customerSvc.editCustomerLinkInfo(linkman,linkPhone);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 查询客户
	 * @param username
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value="queryCustomerList")
	@ResponseBody
	public Map<String,Object> queryCustomerList(String username,
			int page,
			int rows){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return customerSvc.queryCustomerList(username,offset,pagesize);
	}
	
	/**
	 * 获取客户详细信息
	 */	
	@RequestMapping(value="getCustomer")
	@ResponseBody
	public Map<String,Object> getCustomer(String username){
		return customerSvc.getCustomer(username);
	}
	
	
	
	/**
	 * 编辑客户详细信息
	 */	
	@RequestMapping(value="editCustomer")
	@ResponseBody
	public Map<String,Object> editCustomer(String iidd,
			String nickName,String phone,String email,String linkman,String linkPhone){
		return customerSvc.editCustomer(iidd, nickName,phone,email,linkman,linkPhone);
	}
	
	/**
	 * 编辑客户可用状态
	 */
	@RequestMapping(value="editCustomerEnabled")
	@ResponseBody
	public Map<String,Object> editCustomerEnabled(String iidd,int enabled){
		return customerSvc.editCustomerEnabled(iidd, enabled);
	}
	
	
	/**
	 * 重置客户密码
	 */
	@RequestMapping(value="editCustomerPassword")
	@ResponseBody
	public Map<String,Object> editCustomerPassword(String username,String password){
		return customerSvc.editCustomerPassword(username, password);
	}
	
	@RequestMapping(value="queryCustomerUserTagTrueList")
	@ResponseBody
	public Map<String, Object> queryCustomerUserTagTrueList(
			int page,
			int rows,
			String customer,
			String userTag,
			String nameCn){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return customerSvc.queryCustomerUserTagList(true, offset, pagesize, customer, userTag, nameCn);
	}
	
	@RequestMapping(value="queryCustomerUserTagFalseList")
	@ResponseBody
	public Map<String, Object> queryCustomerUserTagFalseList(
			int page,
			int rows,
			String customer,
			String userTag,
			String nameCn){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return customerSvc.queryCustomerUserTagList(false, offset, pagesize, customer, userTag, nameCn);
	}
	
	@RequestMapping(value="addCustomerUserTag")
	@ResponseBody
	public Map<String,Object> addCustomerUserTag(String customer,String userTags){
		return customerSvc.addCustomerUserTag(customer, userTags);
	}
	
	@RequestMapping(value="deleteCustomerUserTag")
	@ResponseBody
	public Map<String,Object> deleteCustomerUserTag(String customer,String userTags){
		return customerSvc.deleteCustomerUserTag(customer, userTags);
	}
}
