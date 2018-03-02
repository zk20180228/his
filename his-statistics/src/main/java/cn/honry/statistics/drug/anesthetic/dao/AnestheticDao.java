package cn.honry.statistics.drug.anesthetic.dao;

import java.util.List;
import java.util.Map;

import cn.honry.statistics.drug.anesthetic.vo.Anestheticvo;
@SuppressWarnings({"all"})
public interface AnestheticDao{

	/** <p>分页方法</p>
	* @param login 开始时间
	* @param end 结束时间
	* @param drug 药品类型
	* @param deptId 科室code
	* @param rows 行数
	* @param page 页数
	* @param flag 是否查询所有
	* @author dtl 
	* @date 2017年3月25日
	*/
	List<Anestheticvo> getAnestheList(String login,String end,String drug,String deptId, String rows, String page, String flag,Map<String, List<String>> map);
	/**
	 *
	 * @Description：获取当前登陆科室
	 * @Author：zhangjin
	 * @CreateDate：2016年6月22日
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	String getDeptName(String deptId);
	/** <p>获得条数</p>
	* @param login 开始时间
	* @param end 结束时间
	* @param drug 药品类型
	* @param deptId 科室code
	* @author dtl 
	* @date 2017年3月25日
	*/
	Integer getAnestheTotal(String login, String end, String drug, String deptId,Map<String, List<String>> map);
}
