package cn.honry.inner.baseinfo.hospital.dao;

import java.util.List;

import cn.honry.base.bean.model.Hospital;
import cn.honry.base.dao.EntityDao;
/**
 * @Description  医院信息DAO层 
 * @author    tangfeishuai
 * @version   1.0 
 * @CreateDate 2016-3-28
 * @Modifier：tangfeishuai
 * @ModifyDate：2016-4-13上午12:02:16  
 * @ModifyRmk：
 */
@SuppressWarnings({"all"})
public interface HospitalInInterDAO extends EntityDao<Hospital>{
	
	/**
	 * @Description:根据id获取Hospital集合
	 * @Author： tangfeishuai
	 * @CreateDate： 2016-3-30
	 * @ModifyDate：2016-3-30
	 * @ModifyRmk：  
	 * @param  String param
	 * @return List<Hospital>  
	 * @version 1.0
	 */
	List<Hospital> getHospLikeName(String param);
	/**
	 * 根据id获取医院
	 * @Description 
	 * @author  marongbin
	 * @createDate： 2016年9月23日 下午1:49:08 
	 * @modifier marongbin
	 * @modifyDate：2016年9月23日 下午1:49:08
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	Hospital getHospByHospId(Integer id);
	/** 得到所有医院信息
	* @Title: getAllHosp 得到所有医院信息
	* @Description: 得到所有医院信息
	* @author dtl 
	* @date 2016年11月9日
	*/
	List<Hospital> getAllHosp();
	
	/**  
	 * 
	 * <p> 根据code获取医院 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2017年2月20日 下午2:37:15 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2017年2月20日 下午2:37:15 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: Hospital
	 *
	 */
	Hospital getHospitalByCode(String code);

}
