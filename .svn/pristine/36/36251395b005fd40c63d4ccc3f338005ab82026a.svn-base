package xyz.ctrl.base;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.svc.core.RegionSvc;

@Controller
@RequestMapping(value="/RegionWS")
public class RegionWS {
	
	@Autowired
	RegionSvc regionSvc;
	
	@RequestMapping(value="updateRegionList")
	@ResponseBody
	public Map<String, Object> updateRegionList(){
		return regionSvc.updateRegionList();
	}
	
	@RequestMapping(value="queryRegionList")
	@ResponseBody
	public Map<String, Object> queryRegionList(int page,
			int rows){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return regionSvc.queryRegionList(offset, pagesize);
	}

	
}
