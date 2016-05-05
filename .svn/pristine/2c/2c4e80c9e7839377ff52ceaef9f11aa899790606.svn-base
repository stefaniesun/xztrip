package xyz.ctrl.base;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.svc.core.ListSvc;


@Controller
@RequestMapping(value="/ListWS")
public class ListWS{
	
	@Autowired
	private ListSvc listSvc;
	
	/**
	 * 下拉框-获取指定类型供应商
	 * @param providerType
	 * @return
	 */
	@RequestMapping(value="getProviderList")
	@ResponseBody
	public Map<String, Object> getProviderList(String type){
		Map<String, Object> results =listSvc.getProviderList(type) ;
		return results;
	}
	
	
	/**
	 * 下拉框-获取PMS所有酒店列表
	 */
	@RequestMapping(value="getPmsHotelList")
	@ResponseBody
	public Map<String, Object> getPmsHotelList(){
		Map<String, Object> results =listSvc.getPmsHotelList() ;
		return results;
	}
	
	/**
	 * 下拉框-根据PMS酒店numberCode获取酒店房型列表
	 */
	@RequestMapping(value="getPmsRoomTypeList")
	@ResponseBody
	public Map<String, Object> getPmsRoomTypeList(String hotelid){
		Map<String, Object> results =listSvc.getPmsRoomTypeList(hotelid) ;
		return results;
	}
	
	/**
	 * 下拉框-用户标签列表
	 */
	@RequestMapping(value="getUserTagList")
	@ResponseBody
	public Map<String, Object> getProductUserTagList(){
		Map<String, Object> results =listSvc.getUserTagList() ;
		return results;
	}
	
	
}
