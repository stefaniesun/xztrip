package xyz.svc.main;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface ProductSvc {
	
	public Map<String,Object> queryProductList(int offset,int pagesize,String userName);
	
}
