package cn.honry.inner.system.parameter.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.HospitalParameterRef;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface ParameterInnerDAO extends EntityDao<HospitalParameter> {
	/**
	 * @Description:获取分页列表
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	List<HospitalParameter> getPage(HospitalParameter entity, String page, String rows);

	/**
	 * @Description:获取总条数
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	int getTotal(HospitalParameter entity);

	/**
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	String getParameterByCode(String code);

	/**
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	List<HospitalParameter> getStartAndEnd(String string);

	/**  
	 *  
	 * @Description：  根据参数名称和参数值获得参数对象
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-18 上午09:25:54  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-18 上午09:25:54  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	HospitalParameter getParamByCodeAndValue(String worktime, String string);
	
	/**
	 * @Description:根据id获取参数对象
	 * @Author： tangfeishuai
	 * @CreateDate： 2016-3-30
	 * @ModifyDate：2016-3-30
	 * @ModifyRmk：  
	 * @param @param String id
	 * @return HospitalParameter 对象
	 * @version 1.0
	**/
	HospitalParameter getEntyById(String id);
	
	
	/**
	 * @Description:根据参数获取参数对象HospitalParameterRef
	 * @Author： tangfeishuai
	 * @CreateDate： 2016-3-30
	 * @ModifyDate：2016-3-30
	 * @ModifyRmk：  
	 * @param String  pid
	 * @return List<HospitalParameterRef> 对象
	 * @version 1.0
	 **/
	List<HospitalParameterRef> getHosParRefs(String  pid);
	
	/**
	 * @Description:根据医院id和参数id获取集合List<HospitalParameterRef>
	 * @Author： tangfeishuai
	 * @CreateDate： 2016-3-30
	 * @ModifyDate：2016-3-30
	 * @ModifyRmk：  
	 * @param String  hid
	 * @param String pid
	 * @return List<HospitalParameterRef> 对象
	 * @version 1.0
	 **/
	List<HospitalParameterRef> getHosParRefsByHpid(Integer  hid,String pid);
	
	/**
	 * @Description:根据参数获取集合List<HospitalParameterRef>
	 * @Author： tangfeishuai
	 * @CreateDate： 2016-3-30
	 * @ModifyDate：2016-3-30
	 * @ModifyRmk：  
	 * @param String  pid
	 * @return List<HospitalParameterRef> 对象
	 * @version 1.0
	 **/
	 List<HospitalParameterRef> getHosParDelRefs(String pid);
    /**
	 * @Description:根据参数代码,医院代码获取参数值
	 * @Author：  hedong
	 * @CreateDate： 2017-03-14
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @param paramCode 参数code
	 * @param hospitalCode 医院code
	 * @return
	 */
	String getParameterByCode(String paramCode, String hospitalCode);
	/**
	 * @Description:根据参数代码数组,医院代码数组获取多个参数值的Map。
	 * 需注意的是paramCodeArr（参数code数组）和hospitalCodeArr（医院code数组）长度需保持一致,否则会返回一个空的map。
	 * @Author：  hedong
	 * @CreateDate： 2017-03-14
	 * @param paramCodeArr 参数code数组
	 * @param hospitalCodeArr 医院code数组
	 * @return Map<String, String> 该map 由 paramCode_hospitalCode组成的key,paramValue组成的值组成
	 */
	Map<String, String> getParameterByCodeArr(String[] paramCodeArr,
			String[] hospitalCodeArr);
	 /**
     * @Description:根据参数代码数组,一个医院代码获取多个参数值的Map。
     * @Author：  hedong
	 * @CreateDate： 2017-03-14
     * @param paramCodeArr  参数code数组
     * @param hospitalCode  医院code
     * @return Map<String, String> 该map 由 paramCode_hospitalCode组成的key,paramValue组成的值组成
     */
	Map<String, String> getParmetersByCodes(String[] paramCodeArr,
			String hospitalCode);
	

}
