package cn.honry.statistics.finance.statistic.service;

import java.util.List;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.finance.statistic.vo.ResultVo;
import cn.honry.statistics.finance.statistic.vo.StatisticVo;
import cn.honry.utils.FileUtil;

/**  
 *  收入统计汇总业务逻辑接口
 * @Author:luyanshou
 * @version 1.0
 */
public interface StatisticService {
	
	/**
	 * 查询住院下的科室信息
	 * @throws Exception 
	 */
	public List<SysDepartment> getDept() throws Exception;
	
	/**
	 * 查询统计结果信息列表 elasticsearch实现
	 * @Author: 朱振坤
	 * @param sTime 查询开始时间 以“createtime”为查询字段
	 * @param eTime 查询结束时间 包括当日
	 * @param ids 科室id字符串，多个id以“,”隔开
	 * @return 封装easyUi表格的json数据的集合
	 */
	public List<ResultVo> statisticDataByES(String sTime,String eTime,String ids);
	
	/**
	 * 根据报表代码查询 最小费用与统计大类对照信息
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
	 * @Description 查询统计大类
	 * @author  donghe
	 * @createDate： 2017-4-14 下午7:15:24 
	 * @modifier 
	 * @modifyDate：
	 * @param 
	 * @return: List<MenuListVO>
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<MenuListVO> getDeptList() throws Exception;
	
	/**  
	 * 
	 * 住院收入统计汇总表导出
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月19日 下午6:00:49 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月19日 下午6:00:49 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public FileUtil export(List<ResultVo> list, FileUtil fUtil);
}
