package xyz.util;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import xyz.filter.JSON;
import xyz.filter.ReturnUtil;
import xyz.filter.RmiUtil;
import xyz.model.main.ProductObject;
import xyz.model.main.ProductStock;
import xyz.svc.config.ConstantPms;

@Component
public class PmsStockPriceUtil {

	@Autowired
	RmiUtil rmiUtil;
	
	@Autowired
	ProductUtil productUtil;
	
	@Autowired
	UserTagPriceUtil userTagPriceUtil;
	
	public ProductStock getStockByDate(String product, Date dateInfo) {
		
		ProductObject productObject=productUtil.getProductByNumberCode(product);
		
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Map<String,String> accessoryParam = new HashMap<String, String>();
		accessoryParam.put("product", productObject.getPmsRoomType());
		accessoryParam.put("dateInfo", format.format(dateInfo));
		@SuppressWarnings("unchecked")
		Map<String, Object> result= (Map<String, Object>)rmiUtil.loadData(ConstantPms.pms_getStockByDate,accessoryParam);
		if((Integer)result.get(Constant.result_status)!=1){
			System.out.print("获取价格库存失败"+result.get(Constant.result_msg));
			return null;
		}else{
			@SuppressWarnings("unchecked")
			Map<String, Object> map=JSON.toObject(JSON.toJosn(result.get("content")), Map.class);
			ProductStock productStock=JSON.toObject(JSON.toJosn(map.get("content")), ProductStock.class);
			return productStock;
		}
		
	}

	@SuppressWarnings("unchecked")
	public  Map<String, Object> queryProductStockList(
		String product,String pmsProduct, Date dateStart, Date dateEnd) {
	
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Map<String,String> accessoryParam = new HashMap<String, String>();
		accessoryParam.put("product", pmsProduct);
		accessoryParam.put("dateStart", format.format(dateStart));
		accessoryParam.put("dateEnd", format.format(dateEnd));
		Map<String, Object> result=(Map<String, Object>)  rmiUtil.loadData(ConstantPms.pms_queryProductStockList,accessoryParam);

		Map<String, Object> map=JSON.toObject(JSON.toJosn(result.get("content")), Map.class);
		List<ProductStock> list=new ArrayList<ProductStock>();

		ProductStock[]  arr=JSON.toObject(JSON.toJosn(map.get("rows")), ProductStock[].class);
		Collections.addAll(list, arr);
		
		ProductObject productObject=productUtil.getProductByNumberCode(product);
		if(productObject.getIsTag()==1){
			//产品关联标签价格取标签最低价
			for(ProductStock productStock : list){
				BigDecimal minPrice=userTagPriceUtil.getMinPrice(product,productStock.getDateInfo(),productStock.getPrice());
				if(minPrice!=null){
					productStock.setPrice(minPrice);
				}
			}
		}
	
		
		Map<String, Object> mapContent=new HashMap<String, Object>();
		mapContent.put("rows", list);
		
		return ReturnUtil.returnMap(1, mapContent);
	}
	

	public List<ProductStock> getAllProductStock(Date dateInfo) {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Map<String,String> accessoryParam = new HashMap<String, String>();
		accessoryParam.put("dateInfo", format.format(dateInfo));
		@SuppressWarnings("unchecked")
		Map<String, Object> result=(Map<String, Object>) rmiUtil.loadData(ConstantPms.pms_getAllProductStock,accessoryParam);
		List<ProductStock> list=new ArrayList<ProductStock>();
		if((Integer)result.get(Constant.result_status)!=1){
			System.out.print("获取价格库存失败"+result.get(Constant.result_msg));
			return null;
		}else{
			@SuppressWarnings("unchecked")
			Map<String, Object> map=JSON.toObject(JSON.toJosn(result.get("content")), Map.class);
			ProductStock[]  arr=JSON.toObject(JSON.toJosn(map.get("rows")), ProductStock[].class);
			Collections.addAll(list, arr);
		}
		return list;
	}

	public List<ProductStock> getRoomTypeProductStock(Date dateInfo,
			List<String> roomTypes) {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		String rooms="";
		for(String roomType:roomTypes){
			rooms+=roomType+",";
		}
		if(!"".equals(rooms)&&rooms.length()>1){
			rooms=rooms.substring(0,rooms.length()-1);
		}
		Map<String,String> accessoryParam = new HashMap<String, String>();
		accessoryParam.put("dateInfo", format.format(dateInfo));
		accessoryParam.put("roomTypes", rooms);
		List<ProductStock> list=new ArrayList<ProductStock>();
		@SuppressWarnings("unchecked")
		Map<String, Object> result=(Map<String, Object>) rmiUtil.loadData(ConstantPms.pms_getRoomTypeProductStock,accessoryParam);
		if((Integer)result.get(Constant.result_status)!=1){
			System.out.print("获取PMS价格信息失败"+result.get(Constant.result_msg));
			return null;
		}else{
			ProductStock[]  arr=JSON.toObject(JSON.toJosn(result.get("content")), ProductStock[].class);
			Collections.addAll(list, arr);
		}
		return list;
	}
}
