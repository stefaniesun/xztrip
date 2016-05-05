package xyz.svc.scheduled.imp;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.filter.RmiUtil;
import xyz.model.main.ProductHotel;
import xyz.model.main.ProductStock;
import xyz.model.main.Provider;
import xyz.svc.scheduled.PriceSTSvc;
import xyz.util.PmsStockPriceUtil;
import xyz.util.StringTool;

@Service
public class PriceSTSvcImp implements PriceSTSvc{

	@Autowired
	CommonDao commonDao;
	
	@Autowired
	RmiUtil rmiUtil;
	
	@Autowired
	PmsStockPriceUtil pmsStockPriceUtil;
	
	@Override
	public int autoUpdatePriceOper() {
	
		Date today=new Date();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		long before=System.currentTimeMillis();
		
		//更新旅游类目产品价格
		
		String sql="update product_scenic s,product_stock p set s.price=p.price where p.date_info='"+format.format(today)+"' and s.number_code=p.product";
		
		int scenicCount=commonDao.getSqlQuery(sql).executeUpdate();
		
		//更新酒店类目产品价格
		
		sql="update product_hotel s,product_stock p set s.price=p.price where p.date_info='"+format.format(today)+"' and s.is_pms=0 and s.number_code=p.product";
		int hotelCount=commonDao.getSqlQuery(sql).executeUpdate();
		
		
		/*sql="update product_car s,product_stock p set s.price=p.price where p.date_info='"+format.format(today)+"' and s.number_code=p.product";
		int carCount=commonDao.getSqlQuery(sql).executeUpdate();*/
		
		//预先加载PMS酒店价格列表
		List<ProductStock> productStocks=pmsStockPriceUtil.getAllProductStock(new Date());
		
		String hql="from ProductHotel where isPms=1";
		@SuppressWarnings("unchecked")
		List<ProductHotel> productHotels=commonDao.queryByHql(hql);
		
		Collections.sort(productStocks);
		Collections.sort(productHotels);
		
		int i=0;
		int pmsCount=0;
		for(int j=0;j<productHotels.size();j++){
			if(productStocks.get(i).getProduct().equals(productHotels.get(j).getPmsRoomType())){
				productHotels.get(j).setPrice(productStocks.get(i).getPrice());
				commonDao.update(productHotels.get(j));
				
				i++;
				pmsCount++;
			}
		}
		
		//更新土特产类目产品价格
		
		sql="update product_specialty s,product_stock p set s.price=p.price where p.date_info='"+format.format(today)+"' and s.number_code=p.product";
		
		int specialtyCount=commonDao.getSqlQuery(sql).executeUpdate();
		
		commonDao.flush();
		//更新provider价格
		sql="update  provider p,(select provider,min(price) as price from product_scenic GROUP BY provider) s set p.scenic_price=s.price where  p.number_code=s.provider"; 
		int providerCount=commonDao.getSqlQuery(sql).executeUpdate();
		
		sql="update  provider p,(select provider,min(price) as price from product_hotel GROUP BY provider) s set p.hotel_price=s.price where  p.number_code=s.provider"; 
		providerCount+=commonDao.getSqlQuery(sql).executeUpdate();
		
		
		/*sql="update  provider p,(select provider,min(price) as price from product_car GROUP BY provider) s set p.car_price=s.price where  p.number_code=s.provider"; 
		providerCount+=commonDao.getSqlQuery(sql).executeUpdate();*/
		
		sql="update  provider p,(select provider,min(price) as price from product_specialty GROUP BY provider) s set p.specialty_price=s.price where  p.number_code=s.provider"; 
		providerCount+=commonDao.getSqlQuery(sql).executeUpdate();
		
		long after=System.currentTimeMillis();
		
		System.out.println("自动更新当天价格数据成功,用时"+(after-before)+"毫秒");
		System.out.println("更新了"+scenicCount+"条旅游价格数据");
		System.out.println("更新了"+hotelCount+"条酒店价格数据");
/*		System.out.println("更新了"+carCount+"条租车价格数据");*/
		System.out.println("更新了"+pmsCount+"条PMS价格数据");
		System.out.println("更新了"+providerCount+"条provider价格数据");
		
		return 0;
	}
	
	@Override
	public Map<String, Object> autoUpdatePriceByProvierOper(String provider) {
		
		Provider proverObj = (Provider)commonDao.getObjectByUniqueCode("Provider", "numberCode", provider);
		if(proverObj==null){
			return ReturnUtil.returnMap(0, "数据不存在");
		}
		
		Date today=new Date();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		
		if(proverObj.getIsScenic()==1){
			
			@SuppressWarnings("unchecked")
			List<String> productNum = commonDao.getSqlQuery("select number_code from product_scenic where provider = '"+provider+"'").list();
			
			//更新旅游类目产品价格
			String sql="update product_scenic s,product_stock p set s.price=p.price where s.number_code=p.product and p.date_info='"+format.format(today)+"' and s.number_code IN ("+StringTool.listToSqlString(productNum)+")";
			commonDao.getSqlQuery(sql).executeUpdate();
			
			commonDao.flush();
			//更新provider价格
			sql="update  provider p,(select provider,min(price) as price from product_scenic where provider = '"+provider+"') s set p.scenic_price=s.price where  p.number_code=s.provider"; 
			commonDao.getSqlQuery(sql).executeUpdate();
		}
		/*if(proverObj.getIsCar()==1){
			
			@SuppressWarnings("unchecked")
			List<String> productNum = commonDao.getSqlQuery("select number_code from product_car where provider = '"+provider+"'").list();
			
			//更新旅游类目产品价格
			String sql="update product_car s,product_stock p set s.car_price=p.price where s.number_code=p.product and p.date_info='"+format.format(today)+"' and s.number_code IN ("+StringTool.listToSqlString(productNum)+")";
			commonDao.getSqlQuery(sql).executeUpdate();
			
			commonDao.flush();
			//更新provider价格
			sql="update  provider p,(select provider,min(price) as price from product_car where provider = '"+provider+"') s set p.car_price=s.price where  p.number_code=s.provider"; 
			commonDao.getSqlQuery(sql).executeUpdate();
		}*/
		if(proverObj.getIsHotel()==1){
			@SuppressWarnings("unchecked")
			List<String> productNum = commonDao.getSqlQuery("select number_code from product_hotel where is_pms=0 and provider = '"+provider+"'").list();
			//更新酒店类目产品价格
			String sql="update product_hotel s,product_stock p set s.price=p.price where s.number_code=p.product and p.date_info='"+format.format(today)+"' and s.is_pms=0 and s.number_code IN ("+StringTool.listToSqlString(productNum)+")";
			commonDao.getSqlQuery(sql).executeUpdate();
			
			@SuppressWarnings("unchecked")
			List<String> roomTypes = commonDao.getSqlQuery("select pms_roomType from product_hotel where is_pms=1 and provider = '"+provider+"'").list();
			
			if(roomTypes.size()>0){
				//预先加载PMS酒店价格列表
				List<ProductStock> productStocks=pmsStockPriceUtil.getRoomTypeProductStock(new Date(),roomTypes);
				
				String hql="from ProductHotel where isPms=1 and provider ='"+provider+"'";
				@SuppressWarnings("unchecked")
				List<ProductHotel> productHotels=commonDao.queryByHql(hql);
				
				for(ProductStock productStock:productStocks){
					for(ProductHotel productHotel:productHotels){
						if(productStock.getProduct().equals(productHotel.getPmsRoomType())){
							productHotel.setPrice(productStock.getPrice());
							commonDao.update(productHotel);
						}
					}
				}
			}
			
			//更新provider价格
			commonDao.flush();
			sql="update  provider p,(select provider,min(price) as price from product_hotel where provider = '"+provider+"') s set p.hotel_price=s.price where  p.number_code=s.provider"; 
			commonDao.getSqlQuery(sql).executeUpdate();
		}
		
	if(proverObj.getIsSpecialty()==1){
			
			@SuppressWarnings("unchecked")
			List<String> productNum = commonDao.getSqlQuery("select number_code from product_specialty where provider = '"+provider+"'").list();
			
			//更新旅游类目产品价格
			String sql="update product_specialty s,product_stock p set s.price=p.price where s.number_code=p.product and p.date_info='"+format.format(today)+"' and s.number_code IN ("+StringTool.listToSqlString(productNum)+")";
			commonDao.getSqlQuery(sql).executeUpdate();
			
			commonDao.flush();
			//更新provider价格
			sql="update  provider p,(select provider,min(price) as price from product_specialty where provider = '"+provider+"') s set p.scenic_price=s.price where  p.number_code=s.provider"; 
			commonDao.getSqlQuery(sql).executeUpdate();
		}
		
		return ReturnUtil.returnMap(1, "数据更新成功");
	}

}
