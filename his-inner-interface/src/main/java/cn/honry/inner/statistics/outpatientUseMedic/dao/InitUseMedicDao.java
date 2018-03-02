package cn.honry.inner.statistics.outpatientUseMedic.dao;

public interface InitUseMedicDao {
	/**
	 * 门诊用药监控 药占比(日)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_MZYYJK_YZB(String menuAlias,String type,String date);
	/**
	 * 门诊用药监控 药占比(月)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_MZYYJK_YZB_MONTH(String menuAlias,String type,String date);
	/**
	 * 门诊用药监控 用药天数(日)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_MZYYJK_YYTS(String menuAlias,String type,String date);
	/**
	 * 门诊用药监控 用药天数(月)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_MZYYJK_YYTS_MONTH(String menuAlias,String type,String date);
	/**
	 * 门诊用药监控 科室用药金额(日)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_MZYYJK_KSYYJE(String menuAlias,String type,String date);
	/**
	 * 门诊用药监控 科室用药金额(月)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_MZYYJK_KSYYJE_MONTH(String menuAlias,String type,String date);
	/**
	 * 门诊用药监控 门诊月药品金额，用药数量，人次(日)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_MZYYJK_YPJE(String menuAlias,String type,String date);
	/**
	 * 门诊用药监控 门诊月药品金额，用药数量，人次(月)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_MZYYJK_YPJE_MONTH(String menuAlias,String type,String date);
	/**
	 * 门诊用药监控 医生用药金额(日)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_MZYYJK_YSYYJE(String menuAlias,String type,String date);
	/**
	 * 门诊用药监控 医生用药金额(月)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_MZYYJK_YSYYJE_MONTH(String menuAlias,String type,String date);
	
}
