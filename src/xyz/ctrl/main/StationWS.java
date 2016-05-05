package xyz.ctrl.main;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.svc.main.StationSvc;

@Controller
@RequestMapping(value="/StationWS")
public class StationWS {

	@Autowired
	StationSvc stationSvc;
	
	@RequestMapping(value="queryStationList")
	@ResponseBody
	public Map<String, Object> queryStationList(int page,
			int rows){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return stationSvc.queryStationList(offset, pagesize);
	}
	
	@RequestMapping(value="addStation")
	@ResponseBody
	public Map<String, Object> addStation(String nameCn,
			String phone,
			String linkman,
			String address,
			String remark,
			String lat,
			String lng){
		return stationSvc.addStation(nameCn, phone,linkman,address,remark,lat,lng);
	}
	
	@RequestMapping(value="editStation")
	@ResponseBody
	public Map<String, Object> editStation(String numberCode,
			String nameCn,
			String phone,
			String linkman,
			String address,
			String remark,
			String lat,
			String lng){
		return stationSvc.editStation(numberCode,nameCn, phone,linkman,address,remark,lat,lng);
	}
	
	
	@RequestMapping(value="deleteStation")
	@ResponseBody
	public Map<String, Object> deleteStation(String numberCodes) {
		return stationSvc.deleteStation(numberCodes);
	}
	

	@RequestMapping(value="queryStationListByLocation")
	@ResponseBody
	public Map<String, Object> queryStationListByLocation(BigDecimal lat,BigDecimal lng) {
		return stationSvc.queryStationListByLocation(lat,lng);
	}
	
}
