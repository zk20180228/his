package cn.honry.inner.statistics.kidneyDiseaseWithDept.dao;

public interface InitKidneyDiseaseWithDeptDao {
	/**
	 * 科室对比表 (日)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_SBKSDBB(String menuAlias,String type,String date);
	/**
	 * 科室对比表 (月)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_SBKSDBB_MONTH(String menuAlias,String type,String date);
}
