package xyz.util;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import xyz.dao.CommonDao;
import xyz.model.main.ProductHotel;
import xyz.model.main.ProductObject;
import xyz.model.main.ProductScenic;
import xyz.model.main.Provider;
import xyz.svc.main.config.ConstantProduct;

@Component
public class ProductUtil {

	@Resource
	CommonDao commonDao;
	
	public ProductObject getProductByNumberCode(String numberCode) {
		ProductObject productObject=null;

		if(numberCode.startsWith(ConstantProduct.SCENIC)){
			ProductScenic scenic= (ProductScenic) commonDao.getObjectByUniqueCode("ProductScenic", "numberCode", numberCode);
			productObject=new ProductObject();
			productObject.setNumberCode(scenic.getNumberCode());
			productObject.setProvider(scenic.getProvider());
			productObject.setNameCn(scenic.getNameCn());
			productObject.setIsTag(scenic.getIsTag());
		}else if(numberCode.startsWith(ConstantProduct.HOTEL)){
			ProductHotel hotel= (ProductHotel) commonDao.getObjectByUniqueCode("ProductHotel", "numberCode", numberCode);
			productObject=new ProductObject();
			productObject.setNumberCode(hotel.getNumberCode());
			productObject.setProvider(hotel.getProvider());
			productObject.setNameCn(hotel.getNameCn());
			productObject.setIsPms(hotel.getIsPms());
			productObject.setPmsRoomType(hotel.getPmsRoomType());
			productObject.setIsTag(hotel.getIsTag());
		}
		
		
		Provider provider=(Provider) commonDao.getObjectByUniqueCode("Provider", "numberCode", productObject.getProvider());
		productObject.setProviderNameCn(provider.getNameCn());
		
		return productObject;
	}
	
	
	public boolean isPmsProduct(String product){ 
		if(product.startsWith(ConstantProduct.HOTEL)){
			ProductHotel hotel= (ProductHotel) commonDao.getObjectByUniqueCode("ProductHotel", "numberCode", product);
			if(hotel.getIsPms()==1){
				return true;
			}
		}
		return false;
	}
}
