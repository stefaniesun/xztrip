package xyz.svc.scheduled.imp;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.model.main.Provider;
import xyz.svc.scheduled.SaleSTSvc;
import xyz.util.StringTool;

@Service
public class SaleSTSvcImp implements SaleSTSvc {

	@Autowired
	CommonDao commonDao;
	
	@Override
	public int autoUpdateMonthSaleOper() {
		
		long before=System.currentTimeMillis();
		System.out.println("自动开始更新销量");
		
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Date today=new Date();
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.MONTH, -1);
		Date date=calendar.getTime();
		
		String sql="select sum(count),provider,product from order_content where flag_pay=1 and add_date>='"+format.format(date)+"'  GROUP BY provider,product";
		
		@SuppressWarnings("unchecked")
		List<Object> objects=commonDao.getSqlQuery(sql).list();
		
		List<String> providerString=new ArrayList<String>();
		
		for(int i=0;i<objects.size();i++){
			Object[] arr=(Object[]) objects.get(i);
			if(arr[1]!=null&&!"".equals(arr[1])){
				providerString.add(arr[1].toString());
			}
		}
		
		String hql="from Provider where numberCode in ("+StringTool.listToSqlString(providerString)+")";
		@SuppressWarnings("unchecked")
		List<Provider> providers=commonDao.getQuery(hql).list();
		
		for(Provider provider:providers){
			for(Object object:objects){
				Object[] arr=(Object[]) object;
				if(arr[1]!=null&&!"".equals(arr[1])&&provider.getNumberCode().equals(arr[1].toString())){
					if(arr[2]!=null&&!"".equals(arr[2])&&arr[2].toString().length()>1){
						if(arr[2].toString().startsWith("SC")){
							if(arr[0]!=null&&!"".equals(arr[0])){
								provider.setScenicMonthSale(new BigDecimal(arr[0].toString()).intValue());
							}
						}
						if(arr[2].toString().startsWith("HO")){
							if(arr[0]!=null&&!"".equals(arr[0])){
								provider.setHotelMonthSale(new BigDecimal(arr[0].toString()).intValue());
							}
						}
						if(arr[2].toString().startsWith("CA")){
							if(arr[0]!=null&&!"".equals(arr[0])){
								provider.setCarMonthSale(new BigDecimal(arr[0].toString()).intValue());
							}
						}
					}
					commonDao.update(provider);
				}
			}
		}
		long after=System.currentTimeMillis();
		System.out.println("更新销量完成，用时"+(after-before)+"毫秒");
		
		return 0;
	}

	@Override
	public int autoUpdateTotalSaleOper() {
		
		long before=System.currentTimeMillis();
		System.out.println("自动开始更新销量");
	
		String sql="select sum(count),provider,product from order_content where flag_pay=1  GROUP BY provider,product";
		
		@SuppressWarnings("unchecked")
		List<Object> objects=commonDao.getSqlQuery(sql).list();
		
		List<String> providerString=new ArrayList<String>();
		
		for(int i=0;i<objects.size();i++){
			Object[] arr=(Object[]) objects.get(i);
			if(arr[1]!=null&&!"".equals(arr[1])){
				providerString.add(arr[1].toString());
			}
		}
		
		String hql="from Provider where numberCode in ("+StringTool.listToSqlString(providerString)+")";
		@SuppressWarnings("unchecked")
		List<Provider> providers=commonDao.getQuery(hql).list();
		
		for(Provider provider:providers){
			for(Object object:objects){
				Object[] arr=(Object[]) object;
				if(arr[1]!=null&&!"".equals(arr[1])&&provider.getNumberCode().equals(arr[1].toString())){
					if(arr[2]!=null&&!"".equals(arr[2])&&arr[2].toString().length()>1){
						if(arr[2].toString().startsWith("SC")){
							if(arr[0]!=null&&!"".equals(arr[0])){
								provider.setScenicTotalSale(new BigDecimal(arr[0].toString()).intValue());
							}
						}
						if(arr[2].toString().startsWith("HO")){
							if(arr[0]!=null&&!"".equals(arr[0])){
								provider.setHotelTotalSale(new BigDecimal(arr[0].toString()).intValue());
							}
						}
						if(arr[2].toString().startsWith("CA")){
							if(arr[0]!=null&&!"".equals(arr[0])){
								provider.setCarTotalSale(new BigDecimal(arr[0].toString()).intValue());
							}
						}
					}
					commonDao.update(provider);
				}
			}
		}
		long after=System.currentTimeMillis();
		System.out.println("更新销量完成，用时"+(after-before)+"毫秒");
		
		return 0;
	}

}
