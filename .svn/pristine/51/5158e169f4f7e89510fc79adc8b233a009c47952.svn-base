package xyz.ctrl.base;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.svc.core.ExceptionLogSvc;

@Controller
@RequestMapping(value="/ExceptionLogWS")
public class ExceptionLogWS {

	@Autowired
	private ExceptionLogSvc exceptionLogSvc;
	
	@RequestMapping(value="queryExceptionLogList")
	@ResponseBody
	public Map<String, Object> queryExceptionLogList(
			int page,
			int rows,
			String numberCode,
			String type){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return exceptionLogSvc.queryExceptionLogList(offset, pagesize, numberCode, type);
	}
	
	
	@RequestMapping(value="addExceptionLog")
	@ResponseBody
	public Map<String, Object> addExceptionLog(
			String type,
			String content){
		return exceptionLogSvc.addExceptionLog(type, content);
	}
}
