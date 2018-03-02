package cn.honry.inner.system.parameter.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.HospitalParameterRef;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.parameter.vo.HospitalParamVo;
import cn.honry.utils.HisParameters;
@Repository("parameterInnerDAO")
@SuppressWarnings({ "all" })
public class ParameterInnerDAOImpl extends HibernateEntityDao<HospitalParameter>implements ParameterInnerDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<HospitalParameter> getPage(HospitalParameter entity,
			String page, String rows) {
		String hql = joint(entity);
		return super.getPage(hql, page, rows);
	}

	@Override
	public int getTotal(HospitalParameter entity) {
		String hql = joint(entity);
		return super.getTotal(hql);
	}
	public String joint(HospitalParameter entity){
		String hql="FROM HospitalParameter p ";
		if(entity!=null){
			if(org.apache.commons.lang3.StringUtils.isNotEmpty(entity.getParameterName())){
				String ParameterName=entity.getParameterName();
					hql = hql+" where p.parameterName LIKE '%"+ParameterName+"%'"
					 + " OR p.parameterType LIKE '%"+ParameterName+"%'" 
					;
				}
			/*
			if(entity.getParameterName()!=null&&!"".equals(entity.getParameterName())){
				hql = hql+" where p.parameterName LIKE '%"+entity.getParameterName()+"%'";
			}
			if(entity.getParameterType()!=null&&!"".equals(entity.getParameterType())){
				hql = hql+" AND p.parameterType LIKE '%"+entity.getParameterType()+"%'";
			}*/
		}
		//hql = hql+" ORDER BY p.idcardCreatetime";
		return hql;
	}

	
	/**  
	 *  
	 * @Description：  通过 ParameterCode 获得参数值  返回值为一个 且为 ParameterValue
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-11 上午10:21:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-11 上午10:21:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */ 
	@Override
	public String getParameterByCode(String code) {
		String hospitalCode = HisParameters.CURRENTHOSPITALCODE;//获得当前医院系统编号 
		//hedong 20170315 根据当前医院系统code 参数code 获取指定参数值
		List<HospitalParamVo> parameterList = this.getParamListByCode(code, hospitalCode);
		if(parameterList==null||parameterList.size()<=0){
			return "";
		}
		return parameterList.get(0).getParamValue();
	}

	@Override
	public List<HospitalParameter> getStartAndEnd(String code) {
		String hql="FROM HospitalParameter p WHERE p.parameterCode = '"+code+"' ORDER BY p.parameterValue" ;
		List<HospitalParameter> parameterList=super.findByObjectProperty(hql, null);
		if(parameterList==null||parameterList.size()<=0){
			return new ArrayList<HospitalParameter>();
		}
		return parameterList;
	}

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
	@Override
	public HospitalParameter getParamByCodeAndValue(String code,String value) {
		String hql="FROM HospitalParameter p WHERE p.parameterCode = '"+code+"' AND p.parameterValue = '"+value+"'" ;
		List<HospitalParameter> parameterList=super.findByObjectProperty(hql, null);
		if(parameterList!=null&&parameterList.size()>0){
			return parameterList.get(0);
		}
		return null;
	}
	
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
	@Override
	public HospitalParameter getEntyById(String id) {
		String hql="from HospitalParameter where id='"+id+"'";
		List<HospitalParameter> list1=super.find(hql, null);
		return list1.get(0);
	}
	
	/**
	 * @Description:根据参数获取List<HospitalParameterRef> 
	 * @Author： tangfeishuai
	 * @CreateDate： 2016-3-30
	 * @ModifyDate：2016-3-30
	 * @ModifyRmk：  
	 * @param String  pid
	 * @return List<HospitalParameterRef> 对象
	 * @version 1.0
	 **/
	@Override
	public List<HospitalParameterRef> getHosParRefs(String pid) {
		String hql="from HospitalParameterRef where stop_flg = 0 AND del_flg = 0 AND parameterId='"+pid+"'";
		return super.find(hql, null);
	}
	
	/**
	 * @Description:根据医院id和参数id获取List<HospitalParameterRef>
	 * @Author： tangfeishuai
	 * @CreateDate： 2016-3-30
	 * @ModifyDate：2016-3-30
	 * @ModifyRmk：  
	 * @param String  hid
	 * @param String pid
	 * @return List<HospitalParameterRef> 对象
	 * @version 1.0
	 **/
	@Override
	public List<HospitalParameterRef> getHosParRefsByHpid(Integer hid, String pid) {
		String hql="from HospitalParameterRef where stop_flg = 0 AND del_flg = 0 AND hospitalId = '"+hid+"' AND parameterId='"+pid+"'";
		return super.find(hql, null);
	}

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
	@Override
	public List<HospitalParameterRef> getHosParDelRefs(String pid) {
		String id = "";
		id=pid.replaceAll(",", "','");
		String hql="from HospitalParameterRef where  parameterId in ('"+id+"') ";
		return super.find(hql, null);
	}

	@Override
	public String getParameterByCode(String paramCode, String hospitalCode) {
		if(StringUtils.isBlank(paramCode)||StringUtils.isBlank(hospitalCode)){
			 return "";
		}	
		List<HospitalParamVo> parameterList = this.getParamListByCode(paramCode,hospitalCode);
		if(parameterList==null||parameterList.size()<=0){
			return "";
		}
		return parameterList.get(0).getParamValue();
	}
	@Override
	public Map<String, String> getParameterByCodeArr(String[] paramCodeArr,
			String[] hospitalCodeArr) {
		Map<String,String>  paramValueMap = new HashMap<String,String>();
		if(paramCodeArr.length>0 && hospitalCodeArr.length>0 && paramCodeArr.length==hospitalCodeArr.length){
			for(int i=0;i<paramCodeArr.length;i++){
				List<HospitalParamVo> paramList = this.getParamListByCode(paramCodeArr[i],hospitalCodeArr[i]);
				String paramValue = "";
				if(paramList!=null&&paramList.size()>0){
					paramValue = paramList.get(0).getParamValue();
				}
				paramValueMap.put(paramCodeArr[i]+"_"+hospitalCodeArr[i], paramValue); //key=编码code_医院code
			}
		}
		return paramValueMap;
	}

	@Override
	public Map<String, String> getParmetersByCodes(String[] paramCodeArr,
			String hospitalCode) {
		Map<String,String>  paramValueMap = new HashMap<String,String>();
		if(paramCodeArr.length>0 && StringUtils.isNotBlank(hospitalCode)){
			for(int i=0;i<paramCodeArr.length;i++){
				List<HospitalParamVo> paramList = this.getParamListByCode(paramCodeArr[i],hospitalCode);
				String paramValue = "";
				if(paramList!=null&&paramList.size()>0){
					paramValue = paramList.get(0).getParamValue();
				}
				paramValueMap.put(paramCodeArr[i]+"_"+hospitalCode, paramValue); //key=编码code_医院code
			}
		}
		return paramValueMap;
	}
	
    /**
     * @Description:根据参数代码,医院代码获取含有参数值的一个集合
	 * @Author：  hedong
	 * @CreateDate： 2017-03-14
     * @param paramCode 参数code
     * @param hospitalCode 医院code
     * @return  List<HospitalParamVo>
     */
	private List<HospitalParamVo> getParamListByCode(String paramCode,
			String hospitalCode) {
		StringBuffer sqlBuffer=new StringBuffer();
		sqlBuffer.append(" select a.Parameter_Id as paramId,a.parameter_code as paramCode,a.parameter_name as paramName,a.parameter_value as paramValue from t_hospital_parameter a ");
		sqlBuffer.append(" left join t_hospital_parameter_ref b on a.parameter_id = b.parameter_id ");
		sqlBuffer.append(" left join t_hospital c on b.hospital_id = c.hospital_id");
		sqlBuffer.append(" where a.parameter_code = '"+paramCode+"' ");
		sqlBuffer.append(" and c.hospital_code = '"+hospitalCode+"' ");
		SQLQuery queryObject=this.getSession().createSQLQuery(sqlBuffer.toString())//
							.addScalar("paramId").addScalar("paramCode")//
				            .addScalar("paramName").addScalar("paramValue");
		return queryObject.setResultTransformer(Transformers.aliasToBean(HospitalParamVo.class)).list();
	}
	
	
}
