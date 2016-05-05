package xyz.svc.core.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.model.base.ExceptionLog;
import xyz.svc.core.ExceptionLogSvc;
import xyz.util.UUIDUtil;

@Service
public class ExceptionLogSvcImp implements ExceptionLogSvc {

	@Autowired
	CommonDao commonDao;
	
	@Override
	public Map<String, Object> queryExceptionLogList(int offset, int pagesize,
			String numberCode, String type) {

		String hql=" from ExceptionLog where 1=1  ";
		if(!"".equals(numberCode)&&numberCode!=null){
			hql+=" and numberCode ='"+numberCode+"'";
		}
		if(!"".equals(type)&&type!=null){
			hql+=" and type = '"+type+"' ";
		}
		
		String countHql = "select count(numberCode) "+hql;
		Query countQuery = commonDao.getQuery(countHql);
		Number countTemp = (Number)countQuery.uniqueResult();
		int count = countTemp==null?0:countTemp.intValue();
		
		Query query = commonDao.getQuery(hql);
		query.setMaxResults(pagesize);
		query.setFirstResult(offset);
		@SuppressWarnings("unchecked")
		List<ExceptionLog> exceptionLogList=query.list();
		Map<String,Object> mapContent=new HashMap<String, Object>();
		mapContent.put("total", count);
		mapContent.put("rows",exceptionLogList);
		return ReturnUtil.returnMap(1, mapContent);
	
	}

	@Override
	public Map<String, Object> addExceptionLog(String type, String content) {
		ExceptionLog exceptionLog=new ExceptionLog();
		exceptionLog.setNumberCode(UUIDUtil.getUUIDStringFor32());
		exceptionLog.setAddDate(new Date());
		exceptionLog.setContent(content);
		exceptionLog.setType(type);
		commonDao.save(exceptionLog);
		return ReturnUtil.returnMap(1, null);
	}

}
