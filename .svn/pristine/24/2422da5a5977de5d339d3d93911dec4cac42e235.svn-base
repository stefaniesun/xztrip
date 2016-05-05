package xyz.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import xyz.dao.CommonDao;
import xyz.filter.MyRequestUtil;
import xyz.model.member.XyzSessionLogin;

@Component
public class UserTagPriceUtil {

	@Resource
	CommonDao commonDao;

	public BigDecimal getMinPrice(String product, Date date) {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		XyzSessionLogin xyzSessionLogin = MyRequestUtil.getXyzSessionLogin();
		if(xyzSessionLogin==null){
			return null;
		}
		String userTags=xyzSessionLogin.getUserTags();
		if(userTags==null||"".equals(userTags)){
			return null;
		}
		String sql="select min(price) from product_user_tag_stock where date_info='"+format.format(date)+"' and product_user_tag in(select number_code from product_user_tag where product ='"+product+"' and user_tag in("+userTags+"))";
		@SuppressWarnings("unchecked")
		List<Object> list=commonDao.getSqlQuery(sql).list();
		return list.get(0)==null?null:new BigDecimal(list.get(0).toString());
	}

	
}
