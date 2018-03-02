package cn.honry.statistics.sys.workloadStatistics.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.sys.workloadStatistics.vo.CountVo;

@SuppressWarnings({"all"})
public interface CountDao extends EntityDao<RegisterInfo>{
	
	

	List<Registration> findinfo(String seldept,String Stime,String Etime);
	
	/**
	 * @Description 获取挂号科室
	 * @author  marongbin
	 * @createDate： 2016年12月1日 下午4:27:34 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<Registration> registerDept(List<String> tnL,String seldept,String Stime,String Etime);
	
	/**
	 * @Description 查询科室下的统计信息
	 * @author  marongbin
	 * @createDate： 2016年12月1日 下午8:39:45 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<CountVo> getCount(Map<String, String> grade,Map<String, String> triage,String deptCode,List<String> tnl,String Stime,String Etime,String page,String rows,String menuAlias);
	
	/**
	 * @Description 获取级别 返回map
	 * @author  marongbin
	 * @createDate： 2016年12月20日 下午6:07:09 
	 * @modifier 
	 * @modifyDate：
	 * @return: Map<String,String>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	Map<String,String> getGrade();
	
	/**
	 * @Description 获取分诊级别
	 * @author  marongbin
	 * @createDate： 2016年12月20日 下午6:22:13 
	 * @modifier 
	 * @modifyDate：
	 * @return: Map<String,String>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	Map<String,String> getTriage();
	
	List<SysDepartment> finddept();
	
	/**
	 * 医生级别查询
	 * 
	 * @date 2016-04-28
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	List<RegisterGrade> findgrade();
	
	/**
	 * @Description 获取总页数
	 * @author  marongbin
	 * @createDate： 2017年2月13日 下午2:23:58 
	 * @modifier 
	 * @modifyDate：
	 * @param dept
	 * @param Stime
	 * @param Etime
	 * @return: int
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getTotal(Map<String, String> grade,Map<String, String> triage, String deptCode,List<String> tnl,String Stime, String Etime,String menuAlias);
	
	/** 科室公作量统计
	 * @param dept
	 * @param Stime
	 * @param Etime
	 * @param page
	 * @param rows
	 * @param menuAlias
	 * @return
	 */
	List<CountVo> findInfo(String dept, String Stime,String Etime, String page, String rows, String menuAlias);
	
	/** 获取总页数
	 * @param dept
	 * @param Stime
	 * @param Etime
	 * @param page
	 * @param menuAlias
	 * @return
	 */
	int findInfoTotal(String dept, String Stime,String Etime, String page, String menuAlias);
	
	/**
	 * @Description:根据开始时间，结束时间，科室编号，查询科室的工作量
	 * @param dept 科室编号
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @param page 查询的页码
	 * @param rows 每页显示的记录数
	 * @param menuAlias 栏目的别名
	 * @exception:Exception
	 * @author: zhangkui
	 * @time:2017年6月28日 上午9:35:12
	 */
	public  Map<String,Object> listDeptWorkCountByMongo(String deptCode,String sTime, String eTime, String page, String rows)throws Exception;
}
