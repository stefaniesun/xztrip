package xyz.svc.core;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface ExceptionLogSvc {

	Map<String, Object> queryExceptionLogList(int offset, int pagesize,
			String numberCode, String type);

	Map<String, Object> addExceptionLog(String type, String content);

}
