package xyz.svc.buyer.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.filter.RmiUtil;
import xyz.model.base.Sms;
import xyz.model.form.R_Customer_UserTag;
import xyz.model.member.Customer;
import xyz.model.member.CustomerUserTag;
import xyz.model.member.XyzSessionLogin;
import xyz.model.member.XyzSessionUtil;
import xyz.model.security.LogOper;
import xyz.svc.buyer.CustomerSvc;
import xyz.util.Constant;
import xyz.util.EncryptionUtil;
import xyz.util.StringTool;
import xyz.util.StringUtil;
import xyz.util.UUIDUtil;

@Service
public class CustomerSvcImp implements CustomerSvc {

	@Autowired
	CommonDao commonDao;
	
	@Override
	public Map<String, Object> getRandCodeOper(String phone) {
		if(phone.length()!=11 || !"1".equals(phone.substring(0, 1))){
			return ReturnUtil.returnMap(0,"发送短信失败:手机号码不符合规范");
		}
		
		String randCode=StringUtil.getRandomStr(6);
//		String randCode="123456";
		String content="您的验证码为:"+randCode+"，有效期30分钟。若非本人操作，请忽略。";
		String dataKey=UUIDUtil.getUUIDStringFor32();
		Map<String,String> accessoryParam = new HashMap<String, String>();
		accessoryParam.put("content", content);
		accessoryParam.put("phone", phone);
		accessoryParam.put("dataKey",dataKey);
		accessoryParam.put("business","XZ");
		
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>)new RmiUtil().loadData(Constant.smsUrl_smsSend, accessoryParam);
		if(result==null){
			return ReturnUtil.returnMap(0,"短信发送失败！");
		}
		Integer status = (Integer)result.get(Constant.result_status);
		if(status==null){
			return ReturnUtil.returnMap(0,"短信发送失败！");
		}
		if(status==0){
			return ReturnUtil.returnMap(0,"短信发送失败！失败原因【"+result.get(Constant.result_msg)+"】 ");
		}
	
		Date date=new Date();
		Sms sms = new Sms();
		sms.setContent(content);
		sms.setNumberCode(UUIDUtil.getUUIDStringFor32());
		sms.setPhone(phone);
        sms.setBusiness("ABC");
        sms.setUsername(phone);
        sms.setAlterDate(date);
        sms.setAddDate(date);
        sms.setRandCode(randCode);
        sms.setDataKey(dataKey);
        sms.setStatus("已发送");
		commonDao.save(sms);
		
		return ReturnUtil.returnMap(1,null);
	}

	@Override
	public Map<String, Object> registerOper(String phone, String password) {
		Customer customer=(Customer) commonDao.getObjectByUniqueCode("Customer", "username", phone);
		if(customer!=null){
			return ReturnUtil.returnMap(0,"请勿重复注册");
		}
		password = EncryptionUtil.md5(password+"{"+phone+"}");
		
		Date date=new Date();
		customer=new Customer();
		customer.setUsername(phone);
		customer.setAddDate(date);
		customer.setAlterDate(date);
		customer.setEnabled(1);
		customer.setPassword(password);
		commonDao.save(customer);
		
		String apikey = UUIDUtil.getUUIDStringFor32();
		XyzSessionLogin xyzSessionLogin = new XyzSessionLogin();
		xyzSessionLogin.setApikey(apikey);
		xyzSessionLogin.setUsername(customer.getUsername());
		xyzSessionLogin.setExpireDate(new Date(new Date().getTime()+Constant.sessionTimes));
		XyzSessionUtil.logins.put(apikey, xyzSessionLogin);
		
		return ReturnUtil.returnMap(1,null);
	}

	@Override
	public Map<String, Object> loginOper(String username, String password) {
	
		String passwordSe = EncryptionUtil.md5(password+"{"+username+"}");
		
		String hql1 = "from Customer t where t.username = '"+username+"'  and t.password = '"+passwordSe+"'"; 
		Customer customer = (Customer) commonDao.queryUniqueByHql(hql1);
		
		/**
		 * 验证用户登录的账号是否合格
		 */
		if(customer == null){
			return ReturnUtil.returnMap(0,"用户名或密码错误！");
		}
		if(customer.getEnabled()==0){
			return ReturnUtil.returnMap(0,"用户受限,暂不允许登录!");
		}
		
		hql1="from CustomerUserTag where customer ='"+customer.getIidd()+"'";
		@SuppressWarnings("unchecked")
		List<CustomerUserTag> userTags=commonDao.queryByHql(hql1);
		String tags="";
		for(CustomerUserTag tag:userTags){
			tags+="'"+tag.getUserTag()+"',";
		}
		if(tags.length()>1){
			tags=tags.substring(0, tags.length()-1);
		}
		
		String apikey = UUIDUtil.getUUIDStringFor32();
		XyzSessionLogin xyzSessionLogin = new XyzSessionLogin();
		xyzSessionLogin.setApikey(apikey);
		xyzSessionLogin.setUserTags(tags);
		xyzSessionLogin.setUsername(customer.getUsername());
		xyzSessionLogin.setLinkman(customer.getLinkman());
		xyzSessionLogin.setLinkPhone(customer.getLinkPhone());
		xyzSessionLogin.setExpireDate(new Date(new Date().getTime()+Constant.sessionTimes));
		XyzSessionUtil.logins.put(apikey, xyzSessionLogin);
		
		return ReturnUtil.returnMap(1, xyzSessionLogin);
	}

	@Override
	public Map<String, Object> alterPasswordOper(String oldPassword, String newPassword) {
		XyzSessionLogin xyzSessionLogin = MyRequestUtil.getXyzSessionLogin();
		
		if(xyzSessionLogin==null){
			return ReturnUtil.returnMap(0,"无有效登录信息");
		}
		String username=xyzSessionLogin.getUsername();
		String hql = "from Customer  where username = '"+username+"'";
		Customer customer = (Customer)commonDao.queryUniqueByHql(hql);
		
		if(customer==null){
			return ReturnUtil.returnMap(0,"用户不存在");
		}else{
			String password = EncryptionUtil.md5(oldPassword+"{"+customer.getUsername()+"}");
			
			if(!password.equals(customer.getPassword())){
				return ReturnUtil.returnMap(0,"密码输入错误");
			}else{
				password = EncryptionUtil.md5(newPassword+"{"+customer.getUsername()+"}");
				customer.setPassword(password);
				commonDao.update(customer);
				
				LogOper logOper = new LogOper();
				logOper.setAddDate(new Date());
				logOper.setDataContent(null);
				logOper.setFlagResult(1);
				logOper.setInterfacePath("/CustomerWS/alterPassword.app");
				logOper.setIpInfo(MyRequestUtil.getIp());
				logOper.setIsWork(1);
				logOper.setRemark("用户主动修改密码");
				logOper.setUsername(username);
				
				return ReturnUtil.returnMap(1,null);
			}
		}
	}

	@Override
	public Map<String, Object> customerExit() {
		XyzSessionLogin xyzSessionLogin = MyRequestUtil.getXyzSessionLogin();
		if(xyzSessionLogin==null){
			return ReturnUtil.returnMap(1,null);
		}
		String apikey = xyzSessionLogin.getApikey();
		XyzSessionUtil.logins.remove(apikey);
		return ReturnUtil.returnMap(1,null);
	}



	@Override
	public Map<String, Object> recoverPasswordOper(String username,String newPassword) {

		String hql = "from Customer  where username = '"+username+"'";
		Customer customer = (Customer)commonDao.queryUniqueByHql(hql);
		
		if(customer==null){
			return ReturnUtil.returnMap(0,"用户不存在");
		}else{
			String password = EncryptionUtil.md5(newPassword+"{"+customer.getUsername()+"}");
			customer.setPassword(password);
			commonDao.update(customer);
			
			LogOper logOper = new LogOper();
			logOper.setAddDate(new Date());
			logOper.setDataContent(null);
			logOper.setFlagResult(1);
			logOper.setInterfacePath("/CustomerWS/recoverPassword.app");
			logOper.setIpInfo(MyRequestUtil.getIp());
			logOper.setIsWork(1);
			logOper.setRemark("用户找回重置密码");
			logOper.setUsername(username);
		}	
		return ReturnUtil.returnMap(1,null);
	}

	@Override
	public Map<String, Object> verifyRandCodeOper(String phone, String randCode) {
		String hql="From Sms where phone='"+phone+"' order by addDate desc";
		Query query = commonDao.getQuery(hql);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked")
		List<Sms> smsList=commonDao.queryByHql(hql);
		if(smsList.size()<1){
			return ReturnUtil.returnMap(0,"短信验证码不正确");
		}else{
			Sms sms=smsList.get(0);
			if(!randCode.equals(sms.getRandCode())){
				return ReturnUtil.returnMap(0,"短信验证码不正确");
			}
		}
		return ReturnUtil.returnMap(1,null);
	}

	@Override
	public Map<String, Object> queryCustomerList(String userName,String nickName,String userTag,int enabled, int offset,
			int pagesize) {
		String hql="from Customer where 1=1 ";
		if(!"".equals(userName)&&userName!=null){
			hql+=" and username like '%"+userName+"%' ";
		}
		
		if(!"".equals(nickName)&&nickName!=null){
			hql+=" and nickName like '%"+nickName+"%' ";
		}
		
		if(!"".equals(userTag)&&userTag!=null){
			String hql0="select customer from CustomerUserTag  where userTag = '"+userTag+"'";
			@SuppressWarnings("unchecked")
			List<String> userTagStr=commonDao.queryByHql(hql0);
			hql+=" and iidd IN ("+StringTool.listToSqlString(userTagStr)+") ";
		}
		
		if(enabled==1){
			hql+=" and enabled = 1 ";
		}else if(enabled==2){
			hql+=" and enabled = 0 ";
		}
		
		String countHql = "select count(iidd) "+hql;
		Query countQuery = commonDao.getQuery(countHql);
		Number countTemp = (Number)countQuery.uniqueResult();
		int count = countTemp==null?0:countTemp.intValue();
		
		Query query = commonDao.getQuery(hql);
		query.setMaxResults(pagesize);
		query.setFirstResult(offset);
		@SuppressWarnings("unchecked")
		List<Customer> customerList=query.list();
		List<String> customerStr=new ArrayList<String>();
		for(Customer customer:customerList){
			customerStr.add(customer.getIidd());
		}
		String hql1="select customer,userTag from CustomerUserTag where customer IN ("+StringTool.listToSqlString(customerStr)+")";
		
		@SuppressWarnings("unchecked")
		List<Object[]> customerTagList=commonDao.queryByHql(hql1);
		for(Customer customer:customerList){
			List<String> customerTagStr=new ArrayList<String>();
			for(int i=0;i<customerTagList.size();i++){
				if(customer.getIidd().equals(customerTagList.get(i)[0].toString())){
					customerTagStr.add(customerTagList.get(i)[1].toString());
				}
			}
			if(customerTagStr.size()>0){
				String hql2="select nameCn from UserTag where numberCode IN("+StringTool.listToSqlString(customerTagStr)+")";
				@SuppressWarnings("unchecked")
				List<String> userTagList=commonDao.queryByHql(hql2);
				customer.setUserTag(StringTool.listToSqlString(userTagList).replace("\'", ""));
			}
		}
		
		Map<String,Object> mapContent=new HashMap<String, Object>();
		mapContent.put("total", count);
		mapContent.put("rows",customerList);
		return ReturnUtil.returnMap(1, mapContent);
	}

	@Override
	public Map<String, Object> editCustomer(String iidd,String nickName,String phone,String email,String linkman,String linkPhone) {
		Customer customer=(Customer)commonDao.getObjectByUniqueCode("Customer", "iidd", iidd);
		if(customer==null){
			return ReturnUtil.returnMap(0,"客户不存在");
		}
		customer.setNickName(nickName);
		customer.setPhone(phone);
		customer.setEmail(email);
		customer.setLinkman(linkman);
		customer.setLinkPhone(linkPhone);
		customer.setAlterDate(new Date());
		commonDao.update(customer);
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> editCustomerEnabled(String iidd, int enabled) {
		Customer customer=(Customer)commonDao.getObjectByUniqueCode("Customer", "iidd", iidd);
		customer.setEnabled(enabled);
		commonDao.update(customer);
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> editCustomerLinkInfo(String linkman,
			String linkPhone) {
		XyzSessionLogin xyzSessionLogin = MyRequestUtil.getXyzSessionLogin();
		if(xyzSessionLogin==null){
			return ReturnUtil.returnMap(0,"无有效登录信息");
		}
		if(linkman==null||linkman.equals("")){
			return  ReturnUtil.returnMap(0, "联系人不能为空！");
		}
		if(linkPhone==null||linkPhone.equals("")){
			return  ReturnUtil.returnMap(0, "联系电话不能为空！");
		}
		
		Customer customer=(Customer) commonDao.getObjectByUniqueCode("Customer", "username", xyzSessionLogin.getUsername());
		customer.setLinkman(linkman);
		customer.setLinkPhone(linkPhone);
		commonDao.save(customer);
		
		return ReturnUtil.returnMap(1,null);
	}

	@Override
	public Map<String, Object> editCustomerPassword(String username,
			String password) {

		String hql = "from Customer  where username = '"+username+"'";
		Customer customer = (Customer)commonDao.queryUniqueByHql(hql);
		
		if(customer==null){
			return ReturnUtil.returnMap(0,"用户不存在");
		}else{
			String newPassword = EncryptionUtil.md5(password+"{"+customer.getUsername()+"}");
			customer.setPassword(newPassword);
			commonDao.update(customer);
			
			LogOper logOper = new LogOper();
			logOper.setAddDate(new Date());
			logOper.setDataContent(null);
			logOper.setFlagResult(1);
			logOper.setInterfacePath("/CustomerWS/editCustomerPassword.do");
			logOper.setIpInfo(MyRequestUtil.getIp());
			logOper.setIsWork(1);
			logOper.setRemark("管理员重置密码");
			logOper.setUsername(username);
		}	
		return ReturnUtil.returnMap(1,null);
	
	}

	@Override
	public Map<String, Object> getCustomer(String username) {
		Customer customer=(Customer) commonDao.getObjectByUniqueCode("Customer", "username", username);
		if(customer==null){
			return ReturnUtil.returnMap(0,"客户不存在");
		}
		return ReturnUtil.returnMap(1,customer);
	}

	@Override
	public Map<String, Object> queryCustomerUserTagList(boolean isContain,
			int offset, int pagesize, String customer, String userTag,
			String nameCn) {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT aa.number_code AS userTag,");
		sql.append("aa.name_cn AS nameCn, ");
		sql.append("aabb.customer AS customer ");
		sql.append(" FROM (");
		sql.append(" SELECT a.number_code,");
		sql.append(" a.name_cn");
		sql.append(" FROM user_tag a WHERE 1 = 1");
		if(nameCn != null && !"".equals(nameCn)){
			sql.append(" and a.name_cn like '%"+nameCn+"%'");
		}
		sql.append(" AND a.number_code");
		if(isContain){
			sql.append(" IN ");
		}else{
			sql.append(" NOT IN ");
		}
		sql.append(" (SELECT t.usertag FROM");
		sql.append(" (SELECT ab.usertag FROM customer_usertag ab WHERE ab.customer = '"+customer+"' ) t )) aa");
		sql.append(" LEFT JOIN customer_usertag aabb ON aa.number_code = aabb.usertag and aabb.customer='"+customer+"' ");
		sql.append(" GROUP BY aa.number_code");

		String countHql = "select count(ttt.usertag) from ("+sql.toString()+") ttt";
		SQLQuery countQuery = commonDao.getSqlQuery(countHql);
		Number countTemp = (Number)countQuery.uniqueResult();
		int count = countTemp==null?0:countTemp.intValue();
		
		SQLQuery query = commonDao.getSqlQuery(sql.toString());
		query.setMaxResults(pagesize);
		query.setFirstResult(offset);
		query.addScalar("userTag").
		addScalar("nameCn").
		addScalar("customer").
		setResultTransformer(Transformers.aliasToBean(R_Customer_UserTag.class));
		
		@SuppressWarnings("unchecked")
		List<R_Customer_UserTag> customerUserTagList = query.list();
		
		Map<String,Object> mapContent = new HashMap<String, Object>();
		mapContent.put("total",count);
		mapContent.put("rows",customerUserTagList);
		
		return ReturnUtil.returnMap(1, mapContent);
	}

	@Override
	public Map<String, Object> deleteCustomerUserTag(String customer,
			String userTags) {
		if(userTags!=null&&!"".equals(userTags)){
			String hql="delete from CustomerUserTag where customer = '"+customer+"' and userTag in ("+StringTool.StrToSqlString(userTags)+") ";
			commonDao.getQuery(hql).executeUpdate();
			return ReturnUtil.returnMap(1, null);
		}else{
			return ReturnUtil.returnMap(0, "请选择要删除的标签!");
		}
	}

	@Override
	public Map<String, Object> addCustomerUserTag(String customer,
			String userTags) {
		if(userTags!=null&&!"".equals(userTags)){
			String[] arrayStr=userTags.split(",");
			for(String userTag : arrayStr){
				CustomerUserTag cu=new CustomerUserTag();
				cu.setCustomer(customer);
				cu.setUserTag(userTag);
				commonDao.save(cu);
			}
			return ReturnUtil.returnMap(1, null);
		}else{
			return ReturnUtil.returnMap(0, "请选择要添加的标签！");
		}
		
	}
}
