package cn.honry.statistics.doctor.registerInfoGzltj.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.UserLogin;
import cn.honry.inner.statistics.registerInfoGzltj.vo.RegisterInfoVo;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.DoctorVo;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;

@SuppressWarnings({"all"})
public interface RegisterInfoStatDao {
	
		//统计
		public int count();
		
		//查询
		List<UserLogin> getAll();
		 
		int save(final UserLogin login);
		
		//修改
		public int update(final UserLogin login);
		
		//删除
		int delete(String id);
		
		//医生工作量统计
		List<RegisterInfoGzltjVo> statRegDorWork(List<String> tnL,String stime, String etime, String dept, String expxrt,String page,String rows);
		
		//医生工作量统计----------------mongodb
		Map<String, Object> statRegDorWorkByMongo(String stime, String etime, String dept, String expxrt,String page,String rows,String menuAlias)throws Exception;
		
		/**
		 * @Description 获取总页数
		 * @author  marongbin
		 * @createDate： 2017年2月13日 下午5:27:39 
		 * @modifier 
		 * @modifyDate：
		 * @param tnL
		 * @param stime
		 * @param etime
		 * @param dept
		 * @param expxrt
		 * @return: int
		 * @modifyRmk：  
		 * @version 1.0
		 */
		int getTotal(List<String> tnL,String stime, String etime, String dept, String expxrt);
		
		//全表查询测试
		void textZone();
		
		/**
		 * @Description 根据科室code集合获取医生jobno和name
		 * @author  marongbin
		 * @createDate： 2017年2月14日 上午10:21:06 
		 * @modifier 
		 * @modifyDate：
		 * @param deptCodes
		 * @return: List<DoctorVO>
		 * @modifyRmk：  
		 * @version 1.0
		 */
		List<DoctorVo> getDoctorBydeptCodes(String deptCodes);
		
		/**
		 * @Description 查询所有医生,根据需求只查某几个分类下的科室医生
		 * @author  gaotiantian
		 * @createDate： 2017-4-20 上午9:29:24 
		 * @modifier 
		 * @modifyDate：
		 * @param 
		 * @return: List<DoctorVO>
		 * @modifyRmk：  
		 * @version 1.0
		 */
		List<MenuListVO> getDoctorList(String deptTypes,String menuAlias);
		
		/** 挂号科室工作量查询
		* @Title: findRegisterDeptInfo 
		* @Description: 
		* @param collectionName
		* @param date
		* @param deptCode
		* @return
		* @return List<RegisterInfoVo>    返回类型 
		* @throws Exception
		* @author mrb
		* @date 2017年6月24日
		*/
		List<RegisterInfoVo> findRegisterDeptInfo(String collectionName,List<String> date,List<String> deptCode)throws Exception;
}
