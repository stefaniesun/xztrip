package xyz.ctrl.main;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.svc.main.ProviderSvc;
import xyz.svc.scheduled.PriceSTSvc;

@Controller
@RequestMapping(value="/ProviderWS")
public class ProviderWS {

	@Autowired
	private ProviderSvc providerSvc;
	@Autowired
	private PriceSTSvc priceSTSvc;
	
	/**
	 * 查询供应商列表
	 * @param nameCn
	 * @param page
	 * @param rows
	 * @param type
	 * @return
	 */
	@RequestMapping(value="queryProviderList")
	@ResponseBody
	public Map<String,Object> queryProviderList(String nameCn,
			String type,
			int page,
			int rows){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return providerSvc.queryProviderList(nameCn, type, offset, pagesize);
	}

	/**
	 * 添加供应商
	 * @param nameCn
	 * @param type
	 * @param level
	 * @param levelSystem
	 * @param phone
	 * @param qq
	 * @param email
	 * @param linkman
	 * @param address
	 * @return
	 */
	@RequestMapping(value="addProvider")
	@ResponseBody
	public Map<String,Object> addProvider(String nameCn,
			int  isScenic,
			int  isHotel,
			int  isCar,
			int  isSpecialty,
			String level,
			String levelSystem,
			String phone,
			String qq,
			String email,
			String linkman,
			String address,
			String remark,
			String longitudeLatitude
			){
		
		return providerSvc.addProvider(nameCn, isScenic,isHotel,isCar,isSpecialty,level, levelSystem, phone, qq, email, linkman, address,remark,longitudeLatitude);
	}
	
	/**
	 * 修改供应商
	 * @param numberCode
	 * @param nameCn
	 * @param type
	 * @param level
	 * @param levelSystem
	 * @param phone
	 * @param qq
	 * @param email
	 * @param linkman
	 * @param address
	 * @return
	 */
	@RequestMapping(value="editProvider")
	@ResponseBody
	public Map<String,Object> editProvider(String numberCode,
			String nameCn,
			int  isScenic,
			int  isHotel,
			int isCar,
			int  isSpecialty,
			String level,
			String levelSystem,
			String phone,
			String qq,
			String email,
			String linkman,
			String address,
			String remark,
			String longitudeLatitude){
		return providerSvc.editProvider(numberCode, nameCn, isScenic, isHotel,isCar,isSpecialty,level, levelSystem, phone, qq, email, linkman, address,remark,longitudeLatitude);
	}
	
	@RequestMapping(value="editProviderImage")
	@ResponseBody
	public Map<String,Object> editProviderImage(String numberCode,String imageUrl){
		return providerSvc.editProviderImage(numberCode,imageUrl);
	}
	
	/**
	 * 删除供应商
	 * @param numberCodes
	 * @return
	 */
	@RequestMapping(value="deleteProvider")
	@ResponseBody
	public Map<String,Object> deleteProvider(String numberCodes){
		return providerSvc.deleteProvider(numberCodes);
	}
	
	/**
	 * 更新供应商和产品的基础价格展示
	 * @param provider
	 * @return
	 */
	@RequestMapping(value="updateBasePrice")
	@ResponseBody
	public Map<String, Object> updateBasePrice(String provider){
		return priceSTSvc.autoUpdatePriceByProvierOper(provider);
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
		return providerSvc.editOnlineFlag(numberCode, value);
	}
}
