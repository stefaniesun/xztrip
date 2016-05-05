package xyz.svc.core;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface ScenicSvc {

	public Map<String, Object> updateScenicList();

	public Map<String, Object> queryScenicList(int offset, int pagesize,String nameCn,String region);

}
