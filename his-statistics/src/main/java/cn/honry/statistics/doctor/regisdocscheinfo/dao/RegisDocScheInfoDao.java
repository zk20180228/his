package cn.honry.statistics.doctor.regisdocscheinfo.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.doctor.regisdocscheinfo.vo.RegisDocScheInfoVo;

/***
 * 挂号医生排班信息查询DAO层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年6月22日 上午9:47:41 
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface RegisDocScheInfoDao extends EntityDao <RegisDocScheInfoVo>{
	
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
	List<RegisDocScheInfoVo> getReRegisDocVoList(String menutype,String deptName,String doctorName,String page,String rows,String begin,String end,List<String> tnL)throws Exception;

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
	int getTotal(String deptName,String doctorName,String begin,String end,List<String> tnL,String menuAlias);
	
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
	List<RegisDocScheInfoVo> getAllReRegisDocVoList(String doctorName,String begin,String end,List<String> tnL)throws Exception;
	

	/**
	 * @Description:渲染星期的方法
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:weekName 星期id
	 * @return String
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	String getWeekName(int  weekName);
	
	/**
	 * @Description:渲染午别的方法
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:noonName 午别id
	 * @return String
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	String getNoonName(int  noonName);
	
	/**
	 * @Description:获取表中最小时间
	 * @Author: zhangjin
	 * @CreateDate: 2016年11月30日
	 * @param:
	 * @return 
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	StatVo findMaxMin();
	
	/**
	 * @Description:打印报表
	 */
	List<RegisDocScheInfoVo> regisDocVoList(String deptName,String doctorName,String begin,String end,List<String> tnL,String menutype)throws Exception;

}
