package xyz.svc.main;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface ProductSpecialtySvc {

	public Map<String, Object> queryProductSpecialtyList(String nameCn,
			String provider, int offset, int pagesize);

	public Map<String, Object> addProductSpecialty(String nameCn,
			String provider, String remark, int refundTime, int maxDate);

	public Map<String, Object> editProductSpecialty(String numberCode,
			String nameCn, String provider, String remark, int refundTime,
			int maxDate);

	public Map<String, Object> editUserTag(String numberCode, int value);

	public Map<String, Object> editProductSpecialtyImage(String numberCode,
			String imageUrl);

	public Map<String, Object> editOnlineFlag(String numberCode, int value);

	public Map<String, Object> deleteProductSpecialty(String numberCodes);

}
