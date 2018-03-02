package cn.honry.outpatient.updateStack.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessStack;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.updateStack.dao.UpdateStackDao;
import cn.honry.outpatient.updateStack.vo.StackAndStockInfoVo;
import cn.honry.utils.HisParameters;
import cn.honry.utils.SessionUtils;

@Repository("updateStackDao")
@SuppressWarnings({ "all" })
public class UpdateStackDaoImpl extends HibernateEntityDao<BusinessStack> implements UpdateStackDao {

	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * 按条件查询组套信息
	 * @author  zhenglin
	 * @createDate：
	 * @modifier lyy
	 * @modifyDate：2016年4月11日 下午4:42:51 
	 * @param：   stack 组套实体 id 组套来源  deptId 登录科室 userId 登录人     stackObject(组套对象: 1是财务用，2是医嘱用)  remark(门诊还是住院，收费不区分，医嘱区分) 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<BusinessStack> getStackInfo(BusinessStack stack, String id,String deptId,String userId,String stackObject,String remark,String root) {
		String hql = " from BusinessStack k where k.stop_flg=0 and k.del_flg=0";
		/**
		 * 组套来源 3=id医生、id=2科室、id=1全院
		 */
		if ("1".equals(id)) {
			hql += " and k.source=1";
		}
		if ("2".equals(id)) {
			hql += " and k.source=2 and k.deptId='"+deptId+"'";
		}
		if ("3".equals(id)) {
			hql += " and k.source=3  and k.doc ='"+userId+"'";
		}
		//stackObject(组套对象: 1是财务用，2是医嘱用)  remark(门诊还是住院，收费不区分，医嘱区分)
		if("1".equals(stackObject)){
			hql += " and k.stackObject=1";
		}
		if("2".equals(stackObject)){
			hql += " and k.stackObject=2 and k.remark='"+remark+"'";
		}
		hql += " and k.parent = '"+root+"'";
 		List<BusinessStack> list = super.find(hql, null);
		if (list.size() > 0 && list != null) {
			return list;
		}
		return new ArrayList<BusinessStack>();
	}
	/**
	 * 渲染频次
	 * @author  zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月12日 下午12:02:39 
	 * @param：   
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<BusinessFrequency> getFreq() {
		String hql=" from BusinessFrequency y where y.stop_flg=0 and y.del_flg=0";
		List<BusinessFrequency> list = super.find(hql, null);
		if (list.size() > 0 && list != null) {
			return list;
		}
		return new ArrayList<BusinessFrequency>();
	}
	/**
	 * 查看组套详情
	 * @author  zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月11日 下午2:59:13
	 * @param： id 组套的编号   drugstoreId 诊断的选择的药房  feelType 是否是收费类型
	 * @modifyRmk：  
	 */
	@Override
	public List<StackAndStockInfoVo> queryStackInfoById(String id,String feelType) {
		if(StringUtils.isBlank(id)){
			return new ArrayList<StackAndStockInfoVo>();
		}
		SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginPharmacyFromShiroSession();
		String hql = "";
		if("1".equals(feelType)||dept!=null){
			hql = "SELECT '1' AS ty, "+//类别
					"BS.STACK_ID AS stackId, "+//组套id
					"BS.STACKINFO_ID AS stackInfoId, "+//组套详情id
					"BS.STACKINFO_ISDRUG AS stackInfoIsDrug, "+//是否是药品
					"BS.STACKINFO_NUM AS stackInfoNum, "+//开立数量
					"BS.STACKINFO_UNIT AS stackInfoUnit, "+//单位
					"BS.STACKINFO_DEPTID AS stackInfoDeptId, "+//科室
					"BS.STACKINFO_REMARK AS stackInfoRemark, "+//组套备注			
					"BS.DATE_BGN AS dateBgn, "+//医嘱开始时间
					"BS.DATE_END AS dateEnd, "+//医嘱结束时间
					"BS.DAYS AS days, "+//草药服数
					"BS.INTERVALDAYS AS intervaldays, "+//间隔天数
					"BS.COMB_NO AS combNo ,"+   //组合流水号
					"D.DRUG_ID AS id, "+//药品id
					"D.DRUG_NAME AS name, "+//名称
					"D.DRUG_CODE AS code, "+//编码
					"D.DRUG_GBCODE AS gbCode, "+//国家编码
					"D.DRUG_SPEC AS spec, "+//规格
					"D.DRUG_TYPE AS drugType, "+//药品类别
					"D.DRUG_SYSTYPE AS drugSystype, "+//系统类别
					"D.DRUG_MINIMUMCOST AS drugMinimumcost, "+//最小费用
					"D.DRUG_NATURE AS drugNature, "+//药品性质
					"D.DRUG_DOSAGEFORM  AS drugDosageform, "+//剂型
					"D.DRUG_GRADE AS drugGrade, "+//药品等级
					"D.DRUG_SPLITATTR AS drugSplitattr, "+//拆分属性
					"D.DRUG_MANUFACTURER AS drugManufacturer, "+//生产厂家
					"D.DRUG_PACKAGINGUNIT AS drugPackagingunit, "+//包装单位
					"D.DRUG_PACKAGINGNUM AS packagingnum, "+//包装数量
					"D.DRUG_MINIMUMUNIT AS unit, "+//最小单位
					"D.DRUG_BASICDOSE AS drugBasicdose, "+//基本剂量
					"BS.DOSE_UNIT AS drugDoseunit, "+//剂量单位
					"D.DRUG_RETAILPRICE AS drugRetailprice, "+//零售价
					"D.DRUG_MAXRETAILPRICE AS drugMaxretailprice, "+//最高零售价
					"D.DRUG_WHOLESALEPRICE AS drugWholesaleprice, "+//批发价
					"D.DRUG_PURCHASEPRICE AS drugPurchaseprice, "+//购入价
					"D.DRUG_PRICETYPE AS drugPricetype, "+//价格形式
					"D.DRUG_ISNEW AS drugIsnew, "+//是否新药
					"D.DRUG_ISMANUFACTURE AS drugIsmanufacture, "+//是否自制
					"D.DRUG_ISTESTSENSITIVITY AS drugIstestsensitivity, "+//是否试敏
					"D.DRUG_ISGMP AS drugIsgmp, "+//是否GMP
					"D.DRUG_ISOTC AS drugIsotc, "+//是否OTC
					"D.DRUG_ISLACK AS drugIslack, "+//是否缺药
					"D.DRUG_ISAGREEMENTPRESCRIPTION AS drugIsagreementprescription, "+//是否协定处方
					"D.DRUG_ISCOOPERATIVEMEDICAL AS drugIscooperativemedical, "+//是否合作医疗
					"D.DRUG_RESTRICTIONOFANTIBIOTIC AS drugRestrictionofantibiotic, "+//抗菌药限制特性
					"D.DRUG_REMARK AS drugRemark, "+//备注
					"BS.USAGE_CODE AS drugUsemode, "+//使用方法
					"D.DRUG_ONCEDOSAGE AS drugOncedosage, "+//一次用量
					"BS.FREQUENCY_CODE AS drugFrequency, "+//频次
					"D.DRUG_NOTES AS drugNotes, "+//注意事项
					"D.DRUG_OPERATIVENORM AS drugOperativenorm, "+//执行标准
					"D.DRUG_INSTRUCTION AS drugInstruction, "+////说明书
					"D.STOP_FLG AS stop_flg, "+//停用标志
					"D.DEL_FLG AS del_flg, "+//删除标志
					"NULL AS unDrugState, "+//状态1在用2停用3废弃
					"NULL AS unDrugDept, "+//执行科室:从部门表获取
					"NULL AS unDrugItemlimit, "+//项目约束
					"NULL AS unDrugScope, "+//项目范围
					"NULL AS unDrugRequirements, "+//检查要求
					"NULL AS unDrugInspectionsite, "+//检查部位或标本
					"NULL AS unDrugMedicalhistory, "+//病史检查
					"BS.CHILDREN_PRICE AS unDrugChildrenPrice, "+//儿童价
					"BS.SPECIAL_PRICE AS unDrugSpecialPrice, "+//特诊价
					"NULL AS unDrugEmergencyaserate, "+//急诊比例
					"NULL AS unDrugDiseaseclassificattion, "+//疾病分类:从编码表获取
					"NULL AS unDrugSpecialtyName, "+//专科名称
					"D.DRUG_ISPROVINCELIMIT AS unDrugIsprovincelimit, "+//是否省限制
					"D.DRUG_ISCITYLIMIT AS unDrugIscitylimit, "+//是否市限制
					"NULL AS unDrugIsownexpense, "+//是否自费
					"NULL AS unDrugIssubmit, "+//是否确认
					"NULL AS unDrugIspreorder, "+//是否需要预约
					"NULL AS unDrugIsbirthcontrol, "+//是否计划生育
					"NULL AS unDrugIsspecificitems, "+//是否特定项目
					"NULL AS unDrugIsinformedconsent, "+//是否知情同意书
					"NULL AS unDrugCrontrast, "+//是否对照
					"NULL AS unDrugIsA, "+//是否甲类
					"NULL AS unDrugIsB, "+//是否乙类
					"NULL AS unDrugIsC, "+//是否丙类
					"S.ID AS infoId, "+//库存id
					"S.STORAGE_DEPTID AS storageDeptid, "+//科室
					"NVL(S.STORE_SUM,0) AS storeSum, "+//总数量
					"NVL(S.PREOUT_SUM,0) AS preoutSum, "+//预扣库存数量
					"S.UNIT_FLAG AS unitFlag, "+//默认发药单位标记 '0'－最小单位，'1'－包装单位
					"S.CHANGE_FLAG AS changeFlag, "+//是否可以拆零1是0否，指默认发药单位为最小单位时，是否可以
					"S.VALID_FLAG AS validFlag, "+//有效性标志1  在用 0 停用 2 废弃 
					"NULL AS labsample, ";//样本类型
					if(dept!=null){
						hql+= "BSI.PROPERTY_CODE AS property ";//拆分属性维护
					}else{
						hql+= "NULL AS property ";
					}
					hql+="FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_INFO D "+
					"LEFT JOIN "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_STOCKINFO S ON D.DRUG_CODE = S.DRUG_ID ";
					if(dept!=null){
						hql+= " AND S.STORAGE_DEPTID='"+dept.getDeptCode()+"' ";
					}
					hql+= "  LEFT JOIN "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_STACKINFO BS ON BS.STACKINFO_ITEMID = D.DRUG_CODE ";
					if(dept!=null){
						hql+="LEFT JOIN T_BUSINESS_DRUGPROPERTY BSI ON D.DRUG_CODE = BSI.DRUG_CODE AND BSI.DEPT_CODE = '"+dept.getDeptCode()+"' AND BSI.STOP_FLG = 0 AND BSI.DEL_FLG = 0 ";
					}
					hql+="WHERE D.DRUG_CODE IN ( "+
					"SELECT B.STACKINFO_ITEMID  "+
					"FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_STACKINFO B "+
					"WHERE B.STACK_ID = '"+id+"' "+
			") "+
			"AND BS.STACK_ID = '"+id+"' and D.del_flg=0 "+
	" UNION "+
	"SELECT '2' AS ty, "+//类别
					"BS.STACK_ID AS stackId, "+//组套id
					"BS.STACKINFO_ID AS stackInfoId, "+//组套详情id
					"BS.STACKINFO_ISDRUG AS stackInfoIsDrug, "+//是否是药品
					"BS.STACKINFO_NUM AS stackInfoNum, "+//开立数量
					"BS.STACKINFO_UNIT AS stackInfoUnit, "+//单位
					"BS.STACKINFO_DEPTID AS stackInfoDeptId, "+//科室	
					"BS.STACKINFO_REMARK AS stackInfoRemark, "+//组套备注
					"BS.DATE_BGN AS dateBgn, "+//医嘱开始时间
					"BS.DATE_END AS dateEnd, "+//医嘱结束时间
					"BS.DAYS AS days, "+//草药服数
					"BS.INTERVALDAYS AS intervaldays, "+//间隔天数
					"BS.COMB_NO AS combNo ,"+   //组合流水号
					"U.UNDRUG_ID AS id, "+//非药品id
					"U.UNDRUG_NAME AS name, "+//名称
					"U.UNDRUG_CODE AS code, "+//编码
					"U.UNDRUG_GBCODE AS gbCode, "+//国家编码
					"U.UNDRUG_SPEC AS spec, "+////规格
					"NULL AS drugType, "+//类别
					"U.UNDRUG_SYSTYPE AS drugSystype, "+//系统类别
					"U.UNDRUG_MINIMUMCOST AS drugMinimumcost, "+//最小费用
					"NULL AS drugNature, "+//药品性质
					"NULL AS drugDosageform, "+//剂型
					"NULL AS drugGrade, "+//药品等级
					"NULL AS drugSplitattr, "+//拆分属性
					"NULL AS drugManufacturer, "+//生产厂家
					"NULL AS drugPackagingunit, "+//包装单位
					"NULL AS packagingnum, "+//包装数量
					"U.UNDRUG_UNIT AS unit, "+//单位
					"NULL AS drugBasicdose, "+//基本剂量
					"BS.DOSE_UNIT AS drugDoseunit, "+//剂量单位
					"U.UNDRUG_DEFAULTPRICE AS drugRetailprice, "+//默认价
					"NULL AS drugMaxretailprice, "+//最高零售价
					"NULL AS drugWholesaleprice, "+//批发价
					"NULL AS drugPurchaseprice, "+//购入价
					"NULL AS drugPricetype, "+//价格形式
					"NULL AS drugIsnew, "+//是否新药
					"NULL AS drugIsmanufacture, "+//是否自制
					"NULL AS drugIstestsensitivity, "+//是否试敏
					"NULL AS drugIsgmp, "+//是否GMP
					"NULL AS drugIsotc, "+//是否OTC
					"NULL AS drugIslack, "+//是否缺药
					"NULL AS drugIsagreementprescription, "+//是否协定处方
					"NULL AS drugIscooperativemedical, "+//是否合作医疗
					"NULL AS drugRestrictionofantibiotic, "+//抗菌药限制特性
					"U.UNDRUG_REMARK AS drugRemark, "+//备注
					"BS.USAGE_CODE AS drugUsemode, "+//使用方法
					"BS.ONCE_DOSE AS drugOncedosage, "+//一次用量
					"BS.FREQUENCY_CODE AS drugFrequency, "+//频次
					"U.UNDRUG_NOTES AS drugNotes, "+//注意事项
					"NULL AS drugOperativenorm, "+//执行标准
					"NULL AS drugInstruction, "+////说明书
					"U.STOP_FLG AS stop_flg, "+//停用标志
					"U.DEL_FLG AS del_flg, "+//删除标志
					"U.UNDRUG_STATE AS unDrugState, "+//状态1在用2停用3废弃
					"U.UNDRUG_DEPT AS unDrugDept, "+//执行科室:从部门表获取
					"U.UNDRUG_ITEMLIMIT AS unDrugItemlimit, "+//项目约束
					"U.UNDRUG_SCOPE AS unDrugScope, "+//项目范围
					"U.UNDRUG_REQUIREMENTS AS unDrugRequirements, "+//检查要求
					"U.UNDRUG_INSPECTIONSITE AS unDrugInspectionsite, "+//检查部位或标本
					"U.UNDRUG_MEDICALHISTORY AS unDrugMedicalhistory, "+//病史检查
					"BS.CHILDREN_PRICE AS unDrugChildrenPrice, "+//儿童价
					"BS.SPECIAL_PRICE AS unDrugSpecialPrice, "+//特诊价
					"U.UNDRUG_EMERGENCYCASERATE AS unDrugEmergencyaserate, "+//急诊比例
					"U.UNDRUG_DISEASECLASSIFICATION AS unDrugDiseaseclassificattion, "+//疾病分类:从编码表获取
					"U.UNDRUG_SPECIALTYNAME AS unDrugSpecialtyName, "+//专科名称
					"U.UNDRUG_ISPROVINCELIMIT AS unDrugIsprovincelimit, "+//是否省限制
					"U.UNDRUG_ISCITYLIMIT AS unDrugIscitylimit, "+//是否市限制
					"U.UNDRUG_ISOWNEXPENSE AS unDrugIsownexpense, "+//是否自费
					"U.UNDRUG_ISSUBMIT AS unDrugIssubmit, "+//是否确认
					"U.UNDRUG_ISPREORDER AS unDrugIspreorder, "+//是否需要预约
					"U.UNDRUG_ISBIRTHCONTROL AS unDrugIsbirthcontrol, "+//是否计划生育
					"U.UNDRUG_ISSPECIFICITEMS AS unDrugIsspecificitems, "+//是否特定项目
					"U.UNDRUG_ISINFORMEDCONSENT AS unDrugIsinformedconsent, "+//是否知情同意书
					"U.UNDRUG_CRONTRAST AS unDrugCrontrast, "+//是否对照
					"U.UNDRUG_ISA AS unDrugIsA, "+//是否甲类
					"U.UNDRUG_ISB AS unDrugIsB, "+//是否乙类
					"U.UNDRUG_ISC AS unDrugIsC, "+//是否丙类
					"NULL AS infoId, "+//库存id
					"NULL AS storageDeptid, "+//科室
					"NULL AS storeSum, "+//总数量
					"NULL AS preoutSum, "+//预扣库存数量
					"NULL AS unitFlag, "+//默认发药单位标记 '0'－最小单位，'1'－包装单位
					"NULL AS changeFlag, "+//是否可以拆零1是0否，指默认发药单位为最小单位时，是否可以
					"NULL AS validFlag, "+//有效性标志1  在用 0 停用 2 废弃 
					"U.UNDRUG_LABSAMPLE AS labsample, "+//样本类型
					"NULL AS property "+//拆分属性维护
			"FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_UNDRUG U "+
			"LEFT JOIN "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_STACKINFO BS ON BS.STACKINFO_ITEMID = U.UNDRUG_CODE "+
			"WHERE U.UNDRUG_CODE IN ( "+
					"SELECT B.STACKINFO_ITEMID FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_STACKINFO B "+
					"WHERE B.STACK_ID = '"+id+"'"+
			") "+
			"AND BS.STACK_ID = '"+id+"'  and U.del_flg=0";
		
		}else{
			hql = "SELECT '2' AS ty, "+//类别
					"BS.STACK_ID AS stackId, "+//组套id
					"BS.STACKINFO_ID AS stackInfoId, "+//组套详情id
					"BS.STACKINFO_ISDRUG AS stackInfoIsDrug, "+//是否是药品
					"BS.STACKINFO_NUM AS stackInfoNum, "+//开立数量
					"BS.STACKINFO_UNIT AS stackInfoUnit, "+//单位
					"BS.STACKINFO_DEPTID AS stackInfoDeptId, "+//科室	
					"BS.STACKINFO_REMARK AS stackInfoRemark, "+//组套备注
					"BS.DATE_BGN AS dateBgn, "+//医嘱开始时间
					"BS.DATE_END AS dateEnd, "+//医嘱结束时间
					"BS.DAYS AS days, "+//草药服数
					"BS.INTERVALDAYS AS intervaldays,"+//间隔天数
					"BS.COMB_NO AS combNo ,"+   //组合流水号
					"U.UNDRUG_ID AS id, "+//非药品id
					"U.UNDRUG_NAME AS name, "+//名称
					"U.UNDRUG_CODE AS code, "+//编码
					"U.UNDRUG_GBCODE AS gbCode, "+//国家编码
					"U.UNDRUG_SPEC AS spec, "+////规格
					"NULL AS drugType, "+//类别
					"U.UNDRUG_SYSTYPE AS drugSystype, "+//系统类别
					"U.UNDRUG_MINIMUMCOST AS drugMinimumcost, "+//最小费用
					"NULL AS drugNature, "+//药品性质
					"NULL AS drugDosageform, "+//剂型
					"NULL AS drugGrade, "+//药品等级
					"NULL AS drugSplitattr, "+//拆分属性
					"NULL AS drugManufacturer, "+//生产厂家
					"NULL AS drugPackagingunit, "+//包装单位
					"NULL AS packagingnum, "+//包装数量
					"U.UNDRUG_UNIT AS unit, "+//单位
					"NULL AS drugBasicdose, "+//基本剂量
					"BS.DOSE_UNIT AS drugDoseunit, "+//剂量单位
					"U.UNDRUG_DEFAULTPRICE AS drugRetailprice, "+//默认价
					"NULL AS drugMaxretailprice, "+//最高零售价
					"NULL AS drugWholesaleprice, "+//批发价
					"NULL AS drugPurchaseprice, "+//购入价
					"NULL AS drugPricetype, "+//价格形式
					"NULL AS drugIsnew, "+//是否新药
					"NULL AS drugIsmanufacture, "+//是否自制
					"NULL AS drugIstestsensitivity, "+//是否试敏
					"NULL AS drugIsgmp, "+//是否GMP
					"NULL AS drugIsotc, "+//是否OTC
					"NULL AS drugIslack, "+//是否缺药
					"NULL AS drugIsagreementprescription, "+//是否协定处方
					"NULL AS drugIscooperativemedical, "+//是否合作医疗
					"NULL AS drugRestrictionofantibiotic, "+//抗菌药限制特性
					"U.UNDRUG_REMARK AS drugRemark, "+//备注
					"BS.USAGE_CODE AS drugUsemode, "+//使用方法
					"BS.ONCE_DOSE AS drugOncedosage, "+//一次用量
					"BS.FREQUENCY_CODE AS drugFrequency, "+//频次
					"U.UNDRUG_NOTES AS drugNotes, "+//注意事项
					"NULL AS drugOperativenorm, "+//执行标准
					"NULL AS drugInstruction, "+////说明书
					"U.STOP_FLG AS stop_flg, "+//停用标志
					"U.DEL_FLG AS del_flg, "+//删除标志
					"U.UNDRUG_STATE AS unDrugState, "+//状态1在用2停用3废弃
					"U.UNDRUG_DEPT AS unDrugDept, "+//执行科室:从部门表获取
					"U.UNDRUG_ITEMLIMIT AS unDrugItemlimit, "+//项目约束
					"U.UNDRUG_SCOPE AS unDrugScope, "+//项目范围
					"U.UNDRUG_REQUIREMENTS AS unDrugRequirements, "+//检查要求
					"U.UNDRUG_INSPECTIONSITE AS unDrugInspectionsite, "+//检查部位或标本
					"U.UNDRUG_MEDICALHISTORY AS unDrugMedicalhistory, "+//病史检查
					"BS.CHILDREN_PRICE AS unDrugChildrenPrice, "+//儿童价
					"BS.SPECIAL_PRICE AS unDrugSpecialPrice, "+//特诊价
					"U.UNDRUG_EMERGENCYCASERATE AS unDrugEmergencyaserate, "+//急诊比例
					"U.UNDRUG_DISEASECLASSIFICATION AS unDrugDiseaseclassificattion, "+//疾病分类:从编码表获取
					"U.UNDRUG_SPECIALTYNAME AS unDrugSpecialtyName, "+//专科名称
					"U.UNDRUG_ISPROVINCELIMIT AS unDrugIsprovincelimit, "+//是否省限制
					"U.UNDRUG_ISCITYLIMIT AS unDrugIscitylimit, "+//是否市限制
					"U.UNDRUG_ISOWNEXPENSE AS unDrugIsownexpense, "+//是否自费
					"U.UNDRUG_ISSUBMIT AS unDrugIssubmit, "+//是否确认
					"U.UNDRUG_ISPREORDER AS unDrugIspreorder, "+//是否需要预约
					"U.UNDRUG_ISBIRTHCONTROL AS unDrugIsbirthcontrol, "+//是否计划生育
					"U.UNDRUG_ISSPECIFICITEMS AS unDrugIsspecificitems, "+//是否特定项目
					"U.UNDRUG_ISINFORMEDCONSENT AS unDrugIsinformedconsent, "+//是否知情同意书
					"U.UNDRUG_CRONTRAST AS unDrugCrontrast, "+//是否对照
					"U.UNDRUG_ISA AS unDrugIsA, "+//是否甲类
					"U.UNDRUG_ISB AS unDrugIsB, "+//是否乙类
					"U.UNDRUG_ISC AS unDrugIsC, "+//是否丙类
					"NULL AS infoId, "+//库存id
					"NULL AS storageDeptid, "+//科室
					"NULL AS storeSum, "+//总数量
					"NULL AS preoutSum, "+//预扣库存数量
					"NULL AS unitFlag, "+//默认发药单位标记 '0'－最小单位，'1'－包装单位
					"NULL AS changeFlag, "+//是否可以拆零1是0否，指默认发药单位为最小单位时，是否可以
					"NULL AS validFlag, "+//有效性标志1  在用 0 停用 2 废弃 
					"U.UNDRUG_LABSAMPLE AS labsample, "+//样本类型
					"NULL AS property "+//拆分属性维护
			"FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_UNDRUG U "+
			"LEFT JOIN "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_STACKINFO BS ON BS.STACKINFO_ITEMID = U.UNDRUG_ID "+
			"WHERE U.UNDRUG_ID IN ( "+
					"SELECT B.STACKINFO_ITEMID FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_STACKINFO B "+
					"WHERE B.STACK_ID = '"+id+"' "+
			") "+
			"AND BS.STACK_ID = '"+id+"' and U.del_flg=0";
		
		}
		Query query = this.getSession().createSQLQuery(hql)
				.addScalar("ty",Hibernate.INTEGER)//类别
				.addScalar("stackId")//组套id
				.addScalar("stackInfoId")//组套详情id
				.addScalar("stackInfoIsDrug",Hibernate.INTEGER)//是否是药品
				.addScalar("stackInfoNum",Hibernate.INTEGER)//开立数量
				.addScalar("stackInfoUnit")//单位
				.addScalar("stackInfoDeptId")//科室
				.addScalar("stackInfoRemark")//组套备注		
				.addScalar("dateBgn",Hibernate.TIMESTAMP)//医嘱开始时间
				.addScalar("dateEnd",Hibernate.TIMESTAMP)//医嘱结束时间
				.addScalar("days",Hibernate.INTEGER)//草药服数
				.addScalar("intervaldays")//间隔天数
				.addScalar("combNo")  //组合流水号
				.addScalar("id")//药品id
				.addScalar("name")//名称
				.addScalar("code")//名称
				.addScalar("gbCode")//国家编码
				.addScalar("spec")//规格
				.addScalar("drugType")//药品类别
				.addScalar("drugSystype")//系统类别
				.addScalar("drugMinimumcost")//最小费用
				.addScalar("drugNature")//药品性质
				.addScalar("drugDosageform")//剂型
				.addScalar("drugGrade")//药品等级
				.addScalar("drugSplitattr",Hibernate.INTEGER)//药品等级
				.addScalar("drugManufacturer")//生产厂家
				.addScalar("drugPackagingunit")//包装单位
				.addScalar("packagingnum",Hibernate.INTEGER)//包装数量
				.addScalar("unit")//最小单位
				.addScalar("drugBasicdose",Hibernate.DOUBLE)//基本剂量
				.addScalar("drugDoseunit")//剂量单位
				.addScalar("drugRetailprice",Hibernate.DOUBLE)//零售价
				.addScalar("drugMaxretailprice",Hibernate.DOUBLE)//最高零售价
				.addScalar("drugWholesaleprice",Hibernate.DOUBLE)//批发价
				.addScalar("drugPurchaseprice",Hibernate.DOUBLE)//购入价
				.addScalar("drugPricetype")//价格形式
				.addScalar("drugIsnew",Hibernate.INTEGER)//是否新药
				.addScalar("drugIsmanufacture",Hibernate.INTEGER)//是否自制
				.addScalar("drugIstestsensitivity",Hibernate.INTEGER)//是否试敏
				.addScalar("drugIsgmp",Hibernate.INTEGER)//是否GMP
				.addScalar("drugIsotc",Hibernate.INTEGER)//是否OTC
				.addScalar("drugIslack",Hibernate.INTEGER)//是否缺药
				.addScalar("drugIsagreementprescription",Hibernate.INTEGER)//是否协定处方
				.addScalar("drugIscooperativemedical",Hibernate.INTEGER)//是否合作医疗
				.addScalar("drugRestrictionofantibiotic",Hibernate.INTEGER)//抗菌药限制特性
				.addScalar("drugRemark")//备注
				.addScalar("drugUsemode")//使用方法
				.addScalar("drugOncedosage",Hibernate.DOUBLE)//一次用量
				.addScalar("drugFrequency")//频次
				.addScalar("drugNotes")//注意事项
				.addScalar("drugOperativenorm")//执行标准
				.addScalar("drugInstruction")//说明书
				.addScalar("stop_flg",Hibernate.INTEGER)//停用标志
				.addScalar("del_flg",Hibernate.INTEGER)//删除标志
				.addScalar("unDrugState",Hibernate.INTEGER)//状态1在用2停用3废弃
				.addScalar("unDrugDept")//执行科室:从部门表获取
				.addScalar("unDrugItemlimit")//项目约束
				.addScalar("unDrugScope")//项目范围
				.addScalar("unDrugRequirements")//检查要求
				.addScalar("unDrugInspectionsite")//检查部位或标本
				.addScalar("unDrugMedicalhistory")//病史检查
				.addScalar("unDrugChildrenPrice",Hibernate.DOUBLE)//儿童价
				.addScalar("unDrugSpecialPrice",Hibernate.DOUBLE)//特诊价
				.addScalar("unDrugEmergencyaserate",Hibernate.DOUBLE)//急诊比例
				.addScalar("unDrugDiseaseclassificattion")//疾病分类:从编码表获取
				.addScalar("unDrugSpecialtyName")//疾病分类:从编码表获取
				.addScalar("unDrugIsprovincelimit",Hibernate.INTEGER)//是否省限制
				.addScalar("unDrugIscitylimit",Hibernate.INTEGER)//是否市限制
				.addScalar("unDrugIsownexpense",Hibernate.INTEGER)//是否自费
				.addScalar("unDrugIssubmit",Hibernate.INTEGER)//是否确认
				.addScalar("unDrugIspreorder",Hibernate.INTEGER)//是否需要预约
				.addScalar("unDrugIsbirthcontrol",Hibernate.INTEGER)//是否计划生育
				.addScalar("unDrugIsspecificitems",Hibernate.INTEGER)//是否特定项目
				.addScalar("unDrugIsinformedconsent",Hibernate.INTEGER)//是否知情同意书
				.addScalar("unDrugCrontrast",Hibernate.INTEGER)//是否对照
				.addScalar("unDrugIsA",Hibernate.INTEGER)//是否甲类
				.addScalar("unDrugIsB",Hibernate.INTEGER)//是否乙类
				.addScalar("unDrugIsC",Hibernate.INTEGER)//是否丙类
				.addScalar("infoId")//库存id
				.addScalar("storageDeptid")//科室
				.addScalar("storeSum",Hibernate.DOUBLE)//总数量
				.addScalar("preoutSum",Hibernate.DOUBLE)//预扣库存数量
				.addScalar("unitFlag",Hibernate.INTEGER)//默认发药单位标记 '0'－最小单位，'1'－包装单位
				.addScalar("changeFlag",Hibernate.INTEGER)//是否可以拆零1是0否，指默认发药单位为最小单位时，是否可以
				.addScalar("validFlag",Hibernate.INTEGER)//有效性标志1  在用 0 停用 2 废弃 
				.addScalar("labsample")//样本类型
				.addScalar("property",Hibernate.INTEGER);//拆分属性维护
		List<StackAndStockInfoVo> list = query.setResultTransformer(Transformers.aliasToBean(StackAndStockInfoVo.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<StackAndStockInfoVo>();
	}

}
