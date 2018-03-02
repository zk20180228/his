  package cn.honry.inner.inpatient.docAdvManage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.inpatient.docAdvManage.dao.DocAdvManageInInterDAO;
import cn.honry.inner.inpatient.docAdvManage.vo.ProInfoInInterVo;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.utils.HisParameters;
import cn.honry.utils.SessionUtils;
@Repository("docAdvManageInInterDAO")
@SuppressWarnings({ "all" })
/**
 * 
* @ClassName: DocAdvManageDAOImpl
* @Description: 
* @author yeguanqun
* @date 2016年5月10日 下午2:02:16
*
 */
public class DocAdvManageInInterDAOImpl extends HibernateEntityDao<InpatientOrder> implements DocAdvManageInInterDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterDAO;//医院参数dao
	@Override
	public List<ProInfoInInterVo> querySysInfo(String name,String type,String sysTypeName,String id) {
		SysDepartment  dept = (SysDepartment) SessionUtils.getCurrentUserLoginPharmacyFromShiroSession();//获取当前用户选择的药房
		String sql="";
		String parameterValue = parameterDAO.getParameterByCode("yzklypdjdm");
		if("".equals(parameterValue)){
			parameterValue = "0";
		}
		List<BusinessDictionary> codeDrugtype=new ArrayList<BusinessDictionary>();
		if("0".equals(parameterValue)){			
			if(StringUtils.isNotBlank(type)){
				String hql = "from BusinessDictionary t where t.stop_flg = 0 and t.del_flg = 0 and t.type='drugType' and t.name='"+sysTypeName+"'";
				codeDrugtype=this.getSession().createQuery(hql).list();
			}			
			if("全部".equals(sysTypeName)){
				sql ="select c.* from (select a.DRUG_ID as itemId,a.DRUG_COMMONNAME as name,a.DRUG_SYSTYPE as sysType,a.DRUG_SPEC as specs,a.DRUG_RETAILPRICE as defaultprice,a.DRUG_PACKAGINGUNIT as drugPackagingunit,a.DRUG_MINIMUMUNIT as unit,a.DRUG_GRADE as drugGrade,a.DRUG_ISCOOPERATIVEMEDICAL as undrugIsprovincelimit,a.DRUG_ISCITYLIMIT as undrugIscitylimit,a.SELF_FLG as undrugIsownexpense,a.DRUG_RESTRICTIONOFANTIBIOTIC as undrugIsspecificitems,";
				sql =sql+" a.DRUG_CNAMEPINYIN as namepinyin,a.DRUG_CNAMEWB as namewb,a.DRUG_CNAMEINPUTCODE as nameinputcode,a.DRUG_COMMONNAME as drugCommonname,a.DRUG_ID as drugId,d.STORAGE_DEPTID as dept,'' as inspectionSite,'' as diseaseClassification,'' as specialtyName,'' as medicalHistory,'' as requirements,a.DRUG_NOTES as notes,a.DRUG_GBCODE as gbcode,a.DRUG_INSTRUCTION as drugInstruction,a.DRUG_ONCEDOSAGE as drugOncedosage,a.DRUG_DOSEUNIT as drugDoseunit,a.DRUG_FREQUENCY as drugFrequency,d.low_sum as lowSum,a.DRUG_DOSAGEFORM as drugDosageform,a.DRUG_TYPE as drugType,a.DRUG_NATURE as drugNature,a.DRUG_RETAILPRICE as drugRetailprice,a.DRUG_REMARK as remark,a.DRUG_USEMODE as drugUsemode,a.DRUG_BASICDOSE as drugBasicdose,to_number('') as undrugIsinformedconsent,'1' as ty,a.DRUG_RESTRICTIONOFANTIBIOTIC as drugRestrictionofantibiotic,d.STORE_SUM as storeSum,a.STOP_FLG as stop_flg,d.STOP_FLG as stockStop_flg,a.DRUG_ISTESTSENSITIVITY as drugIstestsensitivity,a.DRUG_PACKAGINGNUM as packagingnum from "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_INFO a left join "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_STOCKINFO d on a.DRUG_ID = d.DRUG_ID where d.STORAGE_DEPTID='"+dept.getId()+"'";		
				sql =sql+" union select b.UNDRUG_ID as itemId,b.UNDRUG_NAME as name,b.UNDRUG_SYSTYPE as sysType,b.UNDRUG_SPEC as specs,b.UNDRUG_DEFAULTPRICE as defaultprice,b.UNDRUG_UNIT as drugPackagingunit,'' as unit,'' as drugGrade,b.UNDRUG_ISPROVINCELIMIT as undrugIsprovincelimit,b.UNDRUG_ISCITYLIMIT as undrugIscitylimit,b.UNDRUG_ISOWNEXPENSE as undrugIsownexpense,b.UNDRUG_ISSPECIFICITEMS as undrugIsspecificitems,";
				sql =sql+" b.UNDRUG_PINYIN as namepinyin,b.UNDRUG_WB as namewb,b.UNDRUG_INPUTCODE as nameinputcode,'' as drugCommonname,b.UNDRUG_GBCODE as drugId,b.UNDRUG_DEPT as dept,b.UNDRUG_INSPECTIONSITE as inspectionSite,b.UNDRUG_DISEASECLASSIFICATION as diseaseClassification,b.UNDRUG_SPECIALTYNAME as specialtyName,b.UNDRUG_MEDICALHISTORY as medicalHistory,b.UNDRUG_REQUIREMENTS as requirements,b.UNDRUG_NOTES as notes,'' as gbcode,'' as drugInstruction,to_number('') as drugOncedosage,'' as drugDoseunit,'' as drugFrequency,to_number('') as lowSum,'' as drugDosageform,'' as drugType,'' as drugNature,to_number('') as drugRetailprice,b.UNDRUG_REMARK as remark,'' as drugUsemode,to_number('') as drugBasicdose,b.UNDRUG_ISINFORMEDCONSENT as undrugIsinformedconsent,'0' as ty,to_number('') as drugRestrictionofantibiotic,to_number('') as storeSum,b.STOP_FLG as stop_flg,to_number('') as stockStop_flg,to_number('') as drugIstestsensitivity,to_number('') as packagingnum from "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_UNDRUG b) c where 1=1";
				if(StringUtils.isNotBlank(type)){
					sql =sql+" and c.sysType in("+type+")";
				}
				if(StringUtils.isNotBlank(name)){
					sql =sql+" and (c.name like '%"+name+"%' or c.namepinyin like '%"+name+"%' or c.namewb like '%"+name+"%' or c.nameinputcode like '%"+name+"%')";
				}	
				if(StringUtils.isNotBlank(id)){
					sql =sql+" and c.itemId = '"+id+"'";
				}	
			}
			if(StringUtils.isNotBlank(type) && codeDrugtype.size()>0){
				sql ="select c.* from (select a.DRUG_ID as itemId,a.DRUG_COMMONNAME as name,a.DRUG_SYSTYPE as sysType,a.DRUG_SPEC as specs,a.DRUG_RETAILPRICE as defaultprice,a.DRUG_PACKAGINGUNIT as drugPackagingunit,a.DRUG_MINIMUMUNIT as unit,a.DRUG_GRADE as drugGrade,a.DRUG_ISCOOPERATIVEMEDICAL as undrugIsprovincelimit,a.DRUG_ISCITYLIMIT as undrugIscitylimit,a.SELF_FLG as undrugIsownexpense,a.DRUG_RESTRICTIONOFANTIBIOTIC as undrugIsspecificitems,";
				sql =sql+" a.DRUG_CNAMEPINYIN as namepinyin,a.DRUG_CNAMEWB as namewb,a.DRUG_CNAMEINPUTCODE as nameinputcode,a.DRUG_COMMONNAME as drugCommonname,a.DRUG_ID as drugId,d.STORAGE_DEPTID as dept,'' as inspectionSite,'' as diseaseClassification,'' as specialtyName,'' as medicalHistory,'' as requirements,a.DRUG_NOTES as notes,a.DRUG_GBCODE as gbcode,a.DRUG_INSTRUCTION as drugInstruction,a.DRUG_ONCEDOSAGE as drugOncedosage,a.DRUG_DOSEUNIT as drugDoseunit,a.DRUG_FREQUENCY as drugFrequency,d.low_sum as lowSum,a.DRUG_DOSAGEFORM as drugDosageform,a.DRUG_TYPE as drugType,a.DRUG_NATURE as drugNature,a.DRUG_RETAILPRICE as drugRetailprice,a.DRUG_REMARK as remark,a.DRUG_USEMODE as drugUsemode,a.DRUG_BASICDOSE as drugBasicdose,to_number('') as undrugIsinformedconsent,'1' as ty,a.DRUG_RESTRICTIONOFANTIBIOTIC as drugRestrictionofantibiotic,d.STORE_SUM as storeSum,a.STOP_FLG as stop_flg,d.STOP_FLG as stockStop_flg,a.DRUG_ISTESTSENSITIVITY as drugIstestsensitivity,a.DRUG_PACKAGINGNUM as packagingnum from "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_INFO a left join "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_STOCKINFO d on a.DRUG_ID = d.DRUG_ID where d.STORAGE_DEPTID='"+dept.getId()+"') c where 1=1";
				if(StringUtils.isNotBlank(type)){
					sql =sql+" and c.sysType='"+type+"'";
				}
				if(StringUtils.isNotBlank(name)){
					sql =sql+" and (c.name like '%"+name+"%' or c.namepinyin like '%"+name+"%' or c.namewb like '%"+name+"%' or c.nameinputcode like '%"+name+"%')";
				}	
				if(StringUtils.isNotBlank(id)){
					sql =sql+" and c.itemId = '"+id+"'";
				}
			}
			if(StringUtils.isNotBlank(type) && codeDrugtype.size()==0 && !"全部".equals(sysTypeName)){
				sql ="select c.* from (select b.UNDRUG_ID as itemId,b.UNDRUG_NAME as name,b.UNDRUG_SYSTYPE as sysType,b.UNDRUG_SPEC as specs,b.UNDRUG_DEFAULTPRICE as defaultprice,b.UNDRUG_UNIT as drugPackagingunit,'' as unit,'' as drugGrade,b.UNDRUG_ISPROVINCELIMIT as undrugIsprovincelimit,b.UNDRUG_ISCITYLIMIT as undrugIscitylimit,b.UNDRUG_ISOWNEXPENSE as undrugIsownexpense,b.UNDRUG_ISSPECIFICITEMS as undrugIsspecificitems,";
				sql =sql+" b.UNDRUG_PINYIN as namepinyin,b.UNDRUG_WB as namewb,b.UNDRUG_INPUTCODE as nameinputcode,'' as drugCommonname,b.UNDRUG_GBCODE as drugId,b.UNDRUG_DEPT as dept,b.UNDRUG_INSPECTIONSITE as inspectionSite,b.UNDRUG_DISEASECLASSIFICATION as diseaseClassification,b.UNDRUG_SPECIALTYNAME as specialtyName,b.UNDRUG_MEDICALHISTORY as medicalHistory,b.UNDRUG_REQUIREMENTS as requirements,b.UNDRUG_NOTES as notes,'' as gbcode,'' as drugInstruction,to_number('') as drugOncedosage,'' as drugDoseunit,'' as drugFrequency,to_number('') as lowSum,'' as drugDosageform,'' as drugType,'' as drugNature,to_number('') as drugRetailprice,b.UNDRUG_REMARK as remark,'' as drugUsemode,to_number('') as drugBasicdose,b.UNDRUG_ISINFORMEDCONSENT as undrugIsinformedconsent,'0' as ty,to_number('') as drugRestrictionofantibiotic,to_number('') as storeSum,b.STOP_FLG as stop_flg,to_number('') as stockStop_flg,to_number('') as drugIstestsensitivity,to_number('') as packagingnum from "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_UNDRUG b) c where 1=1";
				if(StringUtils.isNotBlank(type)){
					sql =sql+" and c.sysType='"+type+"'";
				}
				if(StringUtils.isNotBlank(name)){
					sql =sql+" and (c.name like '%"+name+"%' or c.namepinyin like '%"+name+"%' or c.namewb like '%"+name+"%' or c.nameinputcode like '%"+name+"%')";
				}	
				if(StringUtils.isNotBlank(id)){
					sql =sql+" and c.itemId = '"+id+"'";
				}
			}
		}
		
		SQLQuery queryObject = this.getSession().createSQLQuery(sql)
						.addScalar("itemId")
						.addScalar("name")
						.addScalar("sysType")
						.addScalar("specs")
						.addScalar("defaultprice",Hibernate.DOUBLE)
						.addScalar("drugPackagingunit")
						.addScalar("unit")
						.addScalar("drugGrade")
						.addScalar("undrugIsprovincelimit",Hibernate.INTEGER)
						.addScalar("undrugIscitylimit",Hibernate.INTEGER)
						.addScalar("undrugIsownexpense",Hibernate.INTEGER)
						.addScalar("undrugIsspecificitems",Hibernate.INTEGER)
						.addScalar("namepinyin")
						.addScalar("namewb")
						.addScalar("nameinputcode")
						.addScalar("drugCommonname")
						.addScalar("drugId")
						.addScalar("dept")
						.addScalar("inspectionSite")
						.addScalar("diseaseClassification")
						.addScalar("specialtyName")
						.addScalar("medicalHistory")
						.addScalar("requirements")
						.addScalar("notes")
						.addScalar("gbcode")
						.addScalar("drugInstruction")
						.addScalar("drugOncedosage",Hibernate.DOUBLE)
						.addScalar("drugDoseunit")
						.addScalar("drugFrequency")
						.addScalar("lowSum",Hibernate.DOUBLE)
						.addScalar("drugDosageform")
						.addScalar("drugType")
						.addScalar("drugNature")
						.addScalar("drugRetailprice",Hibernate.DOUBLE)
						.addScalar("remark")
						.addScalar("drugUsemode")
						.addScalar("drugBasicdose",Hibernate.DOUBLE)
						.addScalar("undrugIsinformedconsent",Hibernate.INTEGER)
						.addScalar("ty",Hibernate.INTEGER)
						.addScalar("drugRestrictionofantibiotic",Hibernate.INTEGER)
						.addScalar("storeSum",Hibernate.DOUBLE)
						.addScalar("stop_flg",Hibernate.INTEGER)
						.addScalar("stockStop_flg",Hibernate.INTEGER)
						.addScalar("drugIstestsensitivity",Hibernate.INTEGER)
						.addScalar("packagingnum",Hibernate.INTEGER);
		int start = 1;
		int count = 20;
		List<ProInfoInInterVo> sysInfoList =null;
		try{
			sysInfoList = queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(ProInfoInInterVo.class)).list();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		if(sysInfoList==null||sysInfoList.size()<=0){
			return new ArrayList<ProInfoInInterVo>();
		}
		return sysInfoList;
	}

}
