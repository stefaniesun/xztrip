package xyz.ctrl.main;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.svc.main.ProductSvc;


@Controller
@RequestMapping(value="/ProductWS")
public class ProductWS{
	
	@Autowired
	 ProductSvc productSvc;
	
	@RequestMapping(value="queryProductList")
	@ResponseBody
	public Map<String,Object> queryProductList(int page,int rows,String userName){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return productSvc.queryProductList(offset, pagesize, userName);
	}
}
