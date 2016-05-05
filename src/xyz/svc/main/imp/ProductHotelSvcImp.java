package xyz.svc.main.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.model.main.ProductHotel;
import xyz.model.main.ProductScenic;
import xyz.model.main.Provider;
import xyz.svc.main.ProductHotelSvc;
import xyz.util.StringTool;
import xyz.util.StringUtil;

@Service
public class ProductHotelSvcImp implements ProductHotelSvc {

	@Autowired
	CommonDao commonDao;
	
	@Override
	public Map<String, Object> queryProductHotelList(String nameCn, String provider,
			int offset,
			int pagesize) {
		String hql=" from ProductHotel where 1=1 ";
		if(!"".equals(nameCn)&&nameCn!=null){
			hql+=" and nameCn like '%"+nameCn+"%'";
		}
		
		if(!"".equals(provider)&&provider!=null){
			hql+=" and provider = '"+provider+"'";
		}
		
		hql+="order by alertDate desc";
		
		String countHql = "select count(numberCode) "+hql;
		Query countQuery = commonDao.getQuery(countHql);
		Number countTemp = (Number)countQuery.uniqueResult();
		int count = countTemp==null?0:countTemp.intValue();
		
		Query query = commonDao.getQuery(hql);
		query.setMaxResults(pagesize);
		query.setFirstResult(offset);
		@SuppressWarnings("unchecked")
		List<ProductHotel> productHotelList=query.list();
		
		List<String> providerStringList=new ArrayList<String>();
		for(ProductHotel productHotel:productHotelList){
			providerStringList.add(productHotel.getProvider());
		}
		
		String hql1="select numberCode,nameCn from Provider where numberCode in ("+StringTool.listToSqlString(providerStringList)+")";
		@SuppressWarnings("unchecked")
		List<Object[]> providerList=commonDao.getQuery(hql1).list();
		for(int i=0;i<productHotelList.size();i++){
			for(int ii=0;ii<providerList.size();ii++){
				if(productHotelList.get(i).getProvider().toString().equals(providerList.get(ii)[0].toString())){
					productHotelList.get(i).setProviderNameCn(providerList.get(ii)[1].toString());
					break;
				}
			}
		}
		
		Map<String,Object> mapContent=new HashMap<String, Object>();
		mapContent.put("total", count);
		mapContent.put("rows",productHotelList);
		return ReturnUtil.returnMap(1, mapContent);
	}

	@Override
	public Map<String, Object> addProductHotel(String nameCn, String provider,String remark,int refundTime,int maxDate) {
		if(provider==null||"".equals(provider)){
			return ReturnUtil.returnMap(0, "请选择关联的供应商");
		}
		ProductHotel producthotel =new ProductHotel();
		producthotel.setNameCn(nameCn);
		producthotel.setNumberCode(StringUtil.get_new_product("HO"));
		producthotel.setProvider(provider);
		producthotel.setRemark(remark);
		producthotel.setRefundTime(refundTime);
		producthotel.setOnlineFlag(0);
		producthotel.setMaxDate(maxDate);
		producthotel.setAddDate(new Date());
		producthotel.setAlertDate(new Date());
		commonDao.save(producthotel);
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> editProductHotel(String numberCode,
			String nameCn, String provider,String remark,int refundTime,int maxDate) {
		if(provider==null||"".equals(provider)){
			return ReturnUtil.returnMap(0, "请选择关联的供应商");
		}
		ProductHotel producthotel=(ProductHotel)commonDao.getObjectByUniqueCode("ProductHotel", "numberCode", numberCode);
		producthotel.setNameCn(nameCn);
		producthotel.setProvider(provider);
		producthotel.setRemark(remark);
		producthotel.setRefundTime(refundTime);
		producthotel.setMaxDate(maxDate);
		producthotel.setAlertDate(new Date());
		commonDao.update(producthotel);
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> deleteProductHotel(String numberCodes) {
		if(!"".equals(numberCodes)&&numberCodes!=null){
			String hql="from ProductHotel where onlineFlag = 1 and numberCode IN("+StringTool.StrToSqlString(numberCodes)+")";
			@SuppressWarnings("unchecked")
			List<ProductHotel> productHotelList=commonDao.queryByHql(hql);
			if(productHotelList.size()>0){
				return ReturnUtil.returnMap(0, "当前选择的产品中存在已经上线的产品请下线后删除！");
			}else{
				
				String sql0="select number_code from product_user_tag where product in ("+StringTool.StrToSqlString(numberCodes)+")";
				@SuppressWarnings("unchecked")
				List<String> productUserTagList=commonDao.getSqlQuery(sql0).list();
				String sql1 = "delete from product_user_tag_stock where product_user_tag in ("+StringTool.listToSqlString(productUserTagList)+")";
				commonDao.getSqlQuery(sql1).executeUpdate();
				String sql2 = "delete from product_user_tag where product in ("+StringTool.StrToSqlString(numberCodes)+")";
				commonDao.getSqlQuery(sql2).executeUpdate();
				String sql3 = "delete from product_stock where product in ("+StringTool.StrToSqlString(numberCodes)+")";
				commonDao.getSqlQuery(sql3).executeUpdate();
				String sql = "delete from product_hotel where number_code in ("+StringTool.StrToSqlString(numberCodes)+")";
				commonDao.getSqlQuery(sql).executeUpdate();
				return ReturnUtil.returnMap(1, null);
			}
		}else{
			return ReturnUtil.returnMap(0, "请先选中需要删除的对象！");
		}
	}

	@Override
	public Map<String, Object> queryProductHotelListForStock(String nameCn,
			String provider, int offset, int pagesize) {
		String hql=" from ProductHotel where 1=1 ";
		if(!"".equals(nameCn)&&nameCn!=null){
			hql+=" and nameCn like '%"+nameCn+"%'";
		}
		if(!"".equals(provider)&&provider!=null){
			hql+=" and provider = '"+provider+"'";
		}
		
		String countHql = "select count(numberCode) "+hql;
		Query countQuery = commonDao.getQuery(countHql);
		Number countTemp = (Number)countQuery.uniqueResult();
		int count = countTemp==null?0:countTemp.intValue();
		
		Query query = commonDao.getQuery(hql);
		query.setMaxResults(pagesize);
		query.setFirstResult(offset);
		@SuppressWarnings("unchecked")
		List<ProductScenic> productScenicList=query.list();
		
		Set<String> productNumberCodeList = new HashSet<String>();
		for(ProductScenic product: productScenicList){
			productNumberCodeList.add(product.getNumberCode());
		}
		String hqlDetail = "from Provider p where p.numberCode in ("+StringTool.listToSqlString(productNumberCodeList)+")";
		@SuppressWarnings("unchecked")
		List<Provider> providerList = commonDao.queryByHql(hqlDetail);
		
		List<Object> resultList = new ArrayList<Object>();
		
		//拼装数据
		for(ProductScenic product: productScenicList){
			Map<String,Object> productDetailMap = new HashMap<String,Object>();
			productDetailMap.put("nameCn", product.getNameCn());
			productDetailMap.put("numberCode", product.getNumberCode());
			for(Provider p : providerList){
				if(product.getProvider().equals(p.getNumberCode())){
					productDetailMap.put("provider",p.getNameCn());
					break;
				}
			}
			resultList.add(productDetailMap);
		}
		
		Map<String,Object> mapContent=new HashMap<String, Object>();
		mapContent.put("total", count);
		mapContent.put("rows",resultList);
		
		return ReturnUtil.returnMap(1, mapContent);
	
	}

	@Override
	public Map<String, Object> deletePms(String numberCode) {
		
		ProductHotel hotel=(ProductHotel) commonDao.getObjectByUniqueCode("ProductHotel", "numberCode", numberCode);
		if(hotel==null){
			return ReturnUtil.returnMap(0, "产品不存在");
		}
		hotel.setIsPms(0);
		hotel.setPmsHotelid("");
		hotel.setPmsRoomType("");
		commonDao.save(hotel);
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> editPms(String numberCode, String hotel,
			String roomType) {
		ProductHotel h=(ProductHotel) commonDao.getObjectByUniqueCode("ProductHotel", "numberCode", numberCode);
		if(h==null){
			return ReturnUtil.returnMap(0, "产品不存在");
		}
		h.setIsPms(1);
        h.setPmsHotelid(hotel);
        h.setPmsRoomType(roomType);
		commonDao.update(h);
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> editProductHotelImage(String numberCode,
			String imageUrl) {
		ProductHotel productHotel=(ProductHotel) commonDao.getObjectByUniqueCode("ProductHotel", "numberCode", numberCode);
		if(productHotel==null){
			return ReturnUtil.returnMap(0, "产品不存在");
		}
		productHotel.setImageUrl(imageUrl);
		commonDao.update(productHotel);
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> editUserTag(String numberCode, int value) {
		ProductHotel hotel=(ProductHotel) commonDao.getObjectByUniqueCode("ProductHotel", "numberCode", numberCode);
		hotel.setIsTag(value);
		commonDao.update(hotel);
		return ReturnUtil.returnMap(1, null);
	}
	@Override
	public Map<String, Object> editOnlineFlag(String numberCode, int value) {
		ProductHotel hotel=(ProductHotel) commonDao.getObjectByUniqueCode("ProductHotel", "numberCode", numberCode);
		hotel.setOnlineFlag(value);
		commonDao.update(hotel);
		return ReturnUtil.returnMap(1, null);
	}
}