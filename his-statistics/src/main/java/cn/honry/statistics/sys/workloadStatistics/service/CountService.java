package cn.honry.statistics.sys.workloadStatistics.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.sys.workloadStatistics.vo.CountVo;




@SuppressWarnings({"all"})
public interface CountService extends BaseService<RegisterInfo>{

	
	
	
	
	/**
	 * 查询全部
	 * @param entity 查询条件封装实体类
	 * @author  wujiao
	 * @date 2015-08-25
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	List<CountVo> getInfo(String dept,String Stime,String Etime);

	
	/**
	 * @Description  根据科室查询：当科室为空时查询全部，不为空是直接根据科室code查询
	 * @author  marongbin
	 * @createDate： 2016年12月1日 下午4:07:17 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	Map<String,Object> getInfoNew(String dept,String Stime,String Etime,String page,String rows,String menuAlias)throws Exception;
	
	/**根据科室查询：当科室为空时查询全部，不为空是直接根据科室code查询(20170607     根据spark实现)
	 * @param dept
	 * @param Stime
	 * @param Etime
	 * @param page
	 * @param rows
	 * @param menuAlias
	 * @return
	 */
	Map<String,Object> getInfoNow(String dept,String Stime,String Etime,String page,String rows,String menuAlias);

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
	public  Map<String,Object> listDeptWorkCountByMongo(String dept, String sTime, String eTime,String page, String rows, String menuAlias)throws Exception;
	
	
	
	
	

}
