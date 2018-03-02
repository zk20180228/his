package cn.honry.statistics.sys.stop.service;

import java.util.List;

import cn.honry.base.service.BaseService;
import cn.honry.statistics.sys.stop.vo.OutPatientVo;
import cn.honry.utils.FileUtil;
/**
 * 门诊停诊原因统计表service层
 * @author  lyy
 * @createDate： 2016年6月23日 上午10:42:41 
 * @modifier lyy
 * @modifyDate：2016年6月23日 上午10:42:41
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface OutpatientStopService extends BaseService<OutPatientVo>{
	
	/**
	 * 查询门诊停诊原因所有人数
	 * @author  lyy
	 * @createDate： 2016年6月23日 上午10:42:38 
	 * @modifier lyy，zhangkui
	 * @modifyDate：2016年6月23日 上午10:42:38,2017-07-03
	 * @param：  firstData 开始时间
	 * @param：  endData  结束时间
	 * @modifyRmk：  添加注释的参数
	 * @version 1.0
	 */
	List<OutPatientVo> getPageOutpatientStop(String firstData, String endData)throws Exception;
	
	/**
	 * 导出
	 * @author  lyy
	 * @createDate： 2016年6月24日 下午6:14:34 
	 * @modifier lyy
	 * @modifyDate：2016年6月24日 下午6:14:34
	 * @param：    outPatientList List<OutPatientVo>
	 * @throws Exception
	 * @modifyRmk：  
	 * @version 1.0
	 */
	FileUtil export(List<OutPatientVo> outPatientList, FileUtil fileUtil)throws Exception;

}
