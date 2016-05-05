package xyz.svc.main;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface StationSvc {

	public Map<String, Object> queryStationList(int offset, int pagesize);

	public Map<String, Object> addStation(String nameCn, String phone,
			String linkman, String address, String remark, String lat,String lng);

	public Map<String, Object> editStation(String numberCode, String nameCn,
			String phone, String linkman, String address, String remark,
			String lat, String lng);

	public Map<String, Object> deleteStation(String numberCodes);

	public Map<String, Object> queryStationListByLocation(BigDecimal lat,
			BigDecimal lng);

}
