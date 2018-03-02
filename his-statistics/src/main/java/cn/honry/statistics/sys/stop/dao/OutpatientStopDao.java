package cn.honry.statistics.sys.stop.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.sys.stop.vo.OutPatientVo;

/**
 * 门诊停诊原因统计表DAO层
 * @author  lyy
 * @createDate： 2016年6月23日 上午10:53:10 
 * @modifier lyy
 * @modifyDate：2016年6月23日 上午10:53:10
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
public interface OutpatientStopDao extends EntityDao<OutPatientVo> {
	
	/**
	 * 查询门诊停诊原因所有人数
	 * @author  lyy
	 * @createDate： 2016年6月23日 上午10:56:17 
	 * @modifier lyy
	 * @modifyDate：2016年6月23日 上午10:56:17
	 * @param：   firstData 开始时间  endData 结束时间 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<OutPatientVo> getPageOutpatientStop(String firstData, String endData,List<String> tnL);
	
	/**
	 * @Description:获取表中最小时间
	 * @Author: zhangjin
	 * @CreateDate: 2016年12月1日
	 * @param:
	 * @return 
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */	
	StatVo findMaxMin();
	
	/**
	 * @Description:获取停诊原因
	 * @Author: zhangjin
	 * @CreateDate: 2016年12月1日
	 * @param:
	 * @return 
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */	
	OutPatientVo getPageOutpatientStopMX(OutPatientVo voList,String firstData,String endData ,List<String> tnL);
	
	/**
	 * @Description 获取停诊原因
	 * @author  marongbin
	 * @createDate： 2016年12月21日 上午11:27:22 
	 * @modifier 
	 * @modifyDate：
	 * @return: Map<String,String>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	Map<String,String> getStopReason();
	
	/**
	 * @Description 门诊停诊统计
	 * @author  marongbin
	 * @createDate： 2016年12月21日 上午11:43:06 
	 * @modifier 
	 * @modifyDate：
	 * @param map
	 * @param tnL
	 * @param firstData
	 * @param endData
	 * @return: List<OutPatientVo>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<OutPatientVo> getOutpatientStop(Map<String,String> map,List<String> tnL,String firstData,String endData);
}
