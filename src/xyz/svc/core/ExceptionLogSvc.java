package xyz.svc.core;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface ExceptionLogSvc {

	public Map<String, Object> queryExceptionLogList(int offset, int pagesize,
			String orderNum, String type,Date dateStart,Date dateEnd,String handleType);

	public Map<String, Object>  addOrderException(String orderNum, String type,
			String content);

	public Map<String, Object> handleExceptionLogOper(String numberCode);

}
