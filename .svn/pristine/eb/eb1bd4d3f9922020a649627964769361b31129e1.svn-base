package xyz.ctrl.wechat;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.svc.wechat.SeekHelpSvc;

@Controller
@RequestMapping(value="/SeekHelpWS")
public class SeekHelpWS {

	@Autowired
	private SeekHelpSvc seekHelpSvc;
	
	@RequestMapping(value="querySeekHelpList")
	@ResponseBody
	public Map<String, Object> querySeekHelpList(int page,
			int rows,Date dateStart,Date dateEnd){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return seekHelpSvc.querySeekHelpList(offset, pagesize,dateStart,dateEnd);
	}
	
	@RequestMapping(value="updateSeekHelp")
	@ResponseBody
	public Map<String, Object> updateSeekHelp(String numberCode){
		return seekHelpSvc.updateSeekHelp(numberCode);
	}
	
	@RequestMapping(value="deleteSeekHelp")
	@ResponseBody
	public Map<String, Object> deleteSeekHelp(String numberCode){
		return seekHelpSvc.deleteSeekHelp(numberCode);
	}
	
	@RequestMapping(value="getlatXandLatY")
	@ResponseBody
	public Map<String,Object>getlatXandLatY(Date dateStart,Date dateEnd){
		return seekHelpSvc.getlatXandLatY(dateStart, dateEnd);
	}
}
