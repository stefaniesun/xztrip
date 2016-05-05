package xyz.svc.wechat;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface SeekHelpSvc {
	
	public Map<String, Object> querySeekHelpList(int offset, int pagesize,Date dateStart,Date dateEnd);
	
	public Map<String, Object> addSeekHelp(String openid, String location_x, String location_y, String label, String scale,String helpType,String helpTime,String phone);
	
	public Map<String, Object> updateSeekHelp(String numberCode);
	
	public Map<String, Object> deleteSeekHelp(String numberCode);
	public Map<String,Object>getlatXandLatY(Date dateStart,Date dateEnd);

}