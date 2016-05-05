package xyz.ctrl.main;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.svc.main.UserTagSvc;

@Controller
@RequestMapping(value="UserTagWS")
public class UserTagWS {
	
	@Autowired
	UserTagSvc userTagSvc;
	
	@RequestMapping(value="queryUserTagList")
	@ResponseBody
	public Map<String, Object> queryUserTagList(int page,
			int rows,
			String nameCn){
		int pagesize = rows;
		int offset = (page - 1) * pagesize;
		return userTagSvc.queryUserTagList(offset, pagesize, nameCn);
	}
	
	@RequestMapping(value="addUserTag")
	@ResponseBody
	public Map<String, Object> addUserTag(String nameCn,
			String remark){
				return userTagSvc.addUserTag(nameCn, remark);
			}
			
	@RequestMapping(value="editUserTag")
	@ResponseBody
	public Map<String, Object> editUserTag(String iidd,
			String nameCn,
			String remark){
				return userTagSvc.editUserTag(iidd, nameCn, remark);
			}
			
	@RequestMapping(value="deleteUserTag")
	@ResponseBody
	public Map<String, Object> deleteUserTag(String iidd){
		return userTagSvc.deleteUserTag(iidd);
	}
}
