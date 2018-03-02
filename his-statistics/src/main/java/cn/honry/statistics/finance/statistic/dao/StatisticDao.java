package cn.honry.statistics.finance.statistic.dao;

import java.util.List;

import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.statistic.vo.QueryVo;
import cn.honry.statistics.finance.statistic.vo.StatisticVo;

/**  
 *  收入统计汇总数据访问接口
 * @Author:luyanshou
 * @version 1.0
 */
public interface StatisticDao {

	/**
	 * 查询住院下的科室信息
	 * @throws Exception 
	 */
	public List<SysDepartment> getDept() throws Exception;
	
	/**
	 * 查询非药品明细统计信息
	 * @param tnLItem 
	 * @param tnLMed 
	 * @param vo 封装的查询条件
	 * @throws Exception 
	 */
	public List<StatisticVo> getCostData(List<String> tnLMed, List<String> tnLItem, QueryVo vo) throws Exception;
	
	/**
	 * 根据科室id查询科室名称
	 * @throws Exception 
	 */
	public String getDeptName(String id) throws Exception;
	
	/**
	 * 根据最小费用代码查询最小费用与统计大类对照信息
	 * @throws Exception 
	 */
	public List<MinfeeStatCode> getMinfee(String minfeeCode) throws Exception;
	
	/**
	 * 根据报表代码查询最小费用与统计大类对照信息
	 * @throws Exception 
	 */
	public List<StatisticVo> getfeetStat(String reportCode) throws Exception;
	/**
	 * 查询报表记录信息
	 * @throws Exception 
	 */
	public List<StatisticVo> getreport() throws Exception;
	
	/**
	 * 根据报表代码查询 统计费用名称
	 * @throws Exception 
	 */
	public List<StatisticVo> getfeeStatName(String reportCode) throws Exception;

	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws Exception 
	 *
	 */
	public StatVo findMaxMinItem() throws Exception;
	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws Exception 
	 *
	 */
	public StatVo findMaxMinMed() throws Exception;
	/**
	 * @Description 查询统计大类
	 * @author  donghe
	 * @createDate： 2017-4-14 下午7:15:24 
	 * @modifier 
	 * @modifyDate：
	 * @param 
	 * @return: List<DeptListVO>
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<MenuListVO> getDeptList() throws Exception;
}
