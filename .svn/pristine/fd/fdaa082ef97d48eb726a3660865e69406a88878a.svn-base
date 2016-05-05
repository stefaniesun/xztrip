package xyz.ctrl.main;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.svc.main.ProductHotelSvc;
import xyz.svc.main.ProductSpecialtySvc;

@Controller
@RequestMapping(value="/ProductSpecialtyWS")
public class ProductSpecialtyWS {

	@Autowired
	ProductSpecialtySvc productSpecialtySvc;
	
	/**
	 * 查询土特产产品列表
	 * @param nameCn
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value="queryProductSpecialtyList")
	@ResponseBody
	public Map<String,Object> queryProductSpecialtyList(String nameCn,String provider,
			int page,
			int rows){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return productSpecialtySvc.queryProductSpecialtyList(nameCn,provider, offset, pagesize);
	}
	
	
	/**
	 * 添加产品
	 * @param nameCn
	 * @param provider
	 * @return
	 */
	@RequestMapping(value="addProductSpecialty")
	@ResponseBody
	public Map<String,Object> addProductSpecialty(String nameCn,String provider,String remark,int refundTime,int maxDate){
		
		return productSpecialtySvc.addProductSpecialty(nameCn, provider,remark,refundTime,maxDate);
	}
	
	
	/**
	 * 修改指定产品
	 * @param numberCode
	 * @param nameCn
	 * @param provider
	 * @return
	 */
	@RequestMapping(value="editProductSpecialty")
	@ResponseBody
	public Map<String,Object> editProductSpecialty(String numberCode,String nameCn,String provider,String remark,int refundTime,int maxDate){
		return productSpecialtySvc.editProductSpecialty(numberCode, nameCn, provider,remark,refundTime,maxDate);
	}
	
	@RequestMapping(value="editUserTag")
	@ResponseBody
	public Map<String,Object> editUserTag(String numberCode,int value){
		return productSpecialtySvc.editUserTag(numberCode, value);
	}
	
	@RequestMapping(value="editProductSpecialtyImage")
	@ResponseBody
	public Map<String,Object> editProductSpecialtyImage(String numberCode,String imageUrl){
		return productSpecialtySvc.editProductSpecialtyImage(numberCode, imageUrl);
	}
	
	/**
	 * 设置上下线
	 * @param numberCode
	 * @param value
	 * @return
	 */
	@RequestMapping(value="editOnlineFlag")
	@ResponseBody
	public Map<String, Object> editOnlineFlag(String numberCode, int value){
		return productSpecialtySvc.editOnlineFlag(numberCode, value);
	}
	
	/**
	 * 删除产品
	 * @param numberCodes
	 * @return
	 */
	@RequestMapping(value="deleteProductSpecialty")
	@ResponseBody
	public Map<String,Object> deleteProductSpecialty(String numberCodes){
		return productSpecialtySvc.deleteProductSpecialty(numberCodes);
	}
	
}
