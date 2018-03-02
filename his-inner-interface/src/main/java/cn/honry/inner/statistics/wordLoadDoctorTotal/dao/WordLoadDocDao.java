package cn.honry.inner.statistics.wordLoadDoctorTotal.dao;

import java.util.Date;
import java.util.List;


public interface WordLoadDocDao {
	/**
	 * 在线更新返回分区表 住院表
	 * @param beginTime  开始时间
	 * @param endTime 结束时间
	 * @param tables String[]表名
	 * @param type MZ ZY 门诊表或住院表
	 * @return
	 */
	public List<String> returnInTables(String beginTime,String endTime,String[] tables,String type);
	/**
	 * 住院医生工作量统计明细   医嘱开立
	 * @param menuAlias栏目别名
	 * @param type 统计类型
	 * @param date 时间
	 */
	public void init_ZYYSGZLTJ_Detail(String menuAlias,String type,String date);
	/**
	 * 住院医生工作量统计汇总
	 * @param menuAlias栏目别名
	 * @param type 统计类型
	 * @param date 时间
	 */
	public void init_ZYYSGZLTJ_Total(String menuAlias,String type,String date);
	/**
	 * 住院医生工作量统计 住院主表
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_ZYYSGZLTJ_Num(String menuAlias,String type,String date);
	/**
	 * 保存日志
	 * @param date
	 * @param menuAlias
	 * @param list
	 * @param queryDate
	 */
	public void saveMongoLog(Date date,String menuAlias,List list,String queryDate);
	
	/**
	 * 住院收入科室汇总
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_ZYSRQK_Dept(String menuAlias, String type, String date);
	/**
	 * 住院收入科室医生汇总
	 * @param menuAlias
	 * @param type
	 * @param date
	 * @param queryType DOC 医生汇总 DEPT科室汇总
	 */
	public void init_ZYSRQK_DeptOrDoc(String menuAlias, String type, String date,String queryType);
	/**
	 * 住院收入医生汇总
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_ZYSRQK_Doc(String menuAlias, String type, String date);
	
	/**
	 * 住院收入医生科室汇总
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_ZYSRQK_Deatail(String menuAlias, String type, String date);
	/**
	 * 住院收入门诊住院汇总
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_ZYSRQK_MzZy(String menuAlias, String type, String date);
	/**
	 * 住院收入门诊住院汇总 月 年
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_ZYSRQK_MzZy_MoreDay(String menuAlias, String type, String date);
	/**
	 * 
	 * 
	 * <p>住院医生工作量出院治疗效果预处理 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月14日 下午1:43:46 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月14日 下午1:43:46 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias
	 * @param type
	 * @param date:
	 *
	 */
	public void init_ZYYSGZLTJ_effect(String menuAlias, String type, String date);
	/**
	 * 
	 * 
	 * <p>替换特殊字符 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年8月2日 下午7:45:47 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年8月2日 下午7:45:47 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param column
	 * @return:
	 *
	 */
	public String returnColumn(String column);
	
	/**
	 * 
	 * 
	 * <p>pc端科室工作量预处理</p>
	 * @Author: XCL
	 * @CreateDate: 2018年1月31日 下午2:49:06 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年1月31日 下午2:49:06 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias
	 * @param type
	 * @param date:
	 *
	 */
	public void pCDeptWorktotal(String menuAlias, String type, String date);
	
	/**
	 * 
	 * 
	 * <p>pc端科室工作量年月预处理 </p>
	 * @Author: XCL
	 * @CreateDate: 2018年2月2日 下午5:19:11 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年2月2日 下午5:19:11 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias
	 * @param type
	 * @param date:
	 *
	 */
	public void pcDeptWorkTotalMonthAndYear(String menuAlias, String type, String date);
	
	/**
	 * 
	 * 
	 * <p>pc端住院医生工作量年月预处理 </p>
	 * @Author: XCL
	 * @CreateDate: 2018年2月2日 下午5:19:11 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年2月2日 下午5:19:11 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias
	 * @param type
	 * @param date:
	 *
	 */
	public void pcDoctorWorkTotalMonthAndYear(String menuAlias, String type, String date);
	
	
}
