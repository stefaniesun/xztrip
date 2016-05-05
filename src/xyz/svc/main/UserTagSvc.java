package xyz.svc.main;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface UserTagSvc {

	public Map<String, Object> queryUserTagList(int offset,
			int pagesize,
			String nameCn);
	
	public Map<String, Object> addUserTag(String nameCn,
			String remark);
	
	public Map<String, Object> editUserTag(String iidd,
			String nameCn,
			String remark);
	
	public Map<String, Object> deleteUserTag(String iidd);
	
}
