package xyz.ctrl.main;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.svc.main.ProductUserTagSvc;

@Controller
@RequestMapping(value="/ProductUserTagWS")
public class ProductUserTagWS {

	@Autowired
	private ProductUserTagSvc productUserTagSvc;
	
	@RequestMapping(value="queryProductUserTagList")
	@ResponseBody
	public Map<String, Object> queryProductUserTagList(
			int page,
			int rows,
			String product,String nameCn){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return productUserTagSvc.queryProductUserTagList(offset, pagesize, product,nameCn);
	}
	
	@RequestMapping(value="addProductUserTag")
	@ResponseBody
	public Map<String, Object> addProductUserTag(
			String product,
			String nameCn,
			String userTag){
		return productUserTagSvc.addProductUserTag(product, nameCn, userTag);
	}
	
	@RequestMapping(value="editProductUserTag")
	@ResponseBody
	public Map<String, Object> editProductUserTag(
			String numberCode,
			String nameCn,
			String userTag){
		return productUserTagSvc.editProductUserTag(numberCode, nameCn, userTag);
	}
	
	@RequestMapping(value="deleteProductUserTag")
	@ResponseBody
	public Map<String, Object> deleteProductUserTag(
			String numberCodes){
		return productUserTagSvc.deleteProductUserTag(numberCodes);
	}

}
