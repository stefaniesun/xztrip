package xyz.ctrl.base;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.svc.core.ScenicSvc;

@Controller
@RequestMapping(value="/ScenicWS")
public class ScenicWS {

	@Autowired
	ScenicSvc scenicSvc;
	
	@RequestMapping(value="updateScenicList")
	@ResponseBody
	public Map<String, Object> updateScenicList(){
		return scenicSvc.updateScenicList();
	}
	
	@RequestMapping(value="queryScenicList")
	@ResponseBody
	public Map<String, Object> queryScenicList(int page,
			int rows,String nameCn,String region){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return scenicSvc.queryScenicList(offset, pagesize,nameCn,region);
	}
}
