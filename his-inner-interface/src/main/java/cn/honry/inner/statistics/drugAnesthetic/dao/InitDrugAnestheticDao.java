package cn.honry.inner.statistics.drugAnesthetic.dao;

public interface InitDrugAnestheticDao {
	/**
	 * 麻醉精神药品统计(日)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_MZJSYPTJ_DAY(String menuAlias,String type,String date);
}
