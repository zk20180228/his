package cn.honry.statistics.doctor.regisdocscheinfo.service;

import java.util.List;

import cn.honry.statistics.doctor.regisdocscheinfo.vo.RegisDocScheInfoVo;
import cn.honry.utils.FileUtil;

/**挂号排班医生信息查询SERVICE
 * @author  tangfeishuai
 * @date 创建时间：2016年6月22日 下午5:53:22
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
@SuppressWarnings({"all"})
public interface RegisDocScheInfoService {
	
	/**
	 * @Description:根据条件查询医生排班信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:doctorName 医生姓名； page 当前页数 ；  rows 分页条数
	 * @return List<RegisDocScheInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<RegisDocScheInfoVo> getReRegisDocVoList(String deptName,String doctorName,String page,String rows,String begin,String end,String menuAlias)throws Exception;
	
	/**
	 * @Description:根据条件查询医生排班信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:doctorName 医生姓名； page 当前页数 ；  rows 分页条数
	 * @return List<RegisDocScheInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	int getTotal(String deptName,String doctorName,String begin,String end,String menuAlias);
	
	/**
	 * @Description:根据条件查询所有医生排班信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:doctorName 医生姓名
	 * @return List<RegisDocScheInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<RegisDocScheInfoVo> getAllReRegisDocVoList(String doctorName,String begin,String end)throws Exception;
	
	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	**/
	FileUtil export(List<RegisDocScheInfoVo> list, FileUtil fUtil)throws Exception;
	
	/**
	 * @Description:打印报表
	**/
	List<RegisDocScheInfoVo> regisDocVoList(String deptName,String doctorName,String begin,String end,String menuAlias) throws Exception;
}
