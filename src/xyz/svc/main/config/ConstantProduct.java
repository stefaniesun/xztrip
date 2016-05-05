package xyz.svc.main.config;



/**
 * 常量
 * @author 钟华
 */
public final class ConstantProduct {
	public static final String HOTEL = "HO";//	
	public static final String SCENIC = "SC";//
	public static final String CAR = "CA";//
	public static final String SPECIALTY = "SP";//
	
	public static String getFlagCn(String flagField){
		String flagCn = "";
		try {
			flagCn = ConstantProduct.class.getField(flagField).get(null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			flagCn = "ERROR";
		}
		return flagCn;
	}

}
