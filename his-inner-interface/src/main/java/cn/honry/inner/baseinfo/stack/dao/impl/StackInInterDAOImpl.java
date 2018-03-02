package cn.honry.inner.baseinfo.stack.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessStack;
import cn.honry.base.bean.model.BusinessStackinfo;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.stack.dao.StackInInterDAO;
import cn.honry.inner.baseinfo.stack.vo.StackAndStockInfoInInterVo;
import cn.honry.inner.baseinfo.stackInfo.dao.StackinfoInInterDAO;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;

import com.google.gson.reflect.TypeToken;

@Repository("stackInInterDAO")
@SuppressWarnings({ "all" })
public class StackInInterDAOImpl  extends HibernateEntityDao<BusinessStack> implements StackInInterDAO{
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	private StackinfoInInterDAO stackinfoInInterDAO;
	
	public void batchDel(String tablName,String ids,String idName){
		//User user = WebUtils.getSessionUser();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr=format.format(new Date());
		StringBuilder sb = new StringBuilder();
		String idArg=this.stringUtils(ids);
		sb.append("update "+tablName+" t set DEL_FLG = 1 , DELETETIME=to_date('"+dateStr+"','yyyy-MM-dd HH24:mi:ss')  where "+idName+" in ("+idArg+")");
		SQLQuery query = getSession().createSQLQuery(sb.toString());
		query.executeUpdate();
		String[] idArgs=ids.split(",");
		for (String idStr:idArgs) {
			List<BusinessStackinfo> businessInfoList=stackinfoInInterDAO.findStackInfoByFk(idStr);
			StringBuilder sb1 = new StringBuilder();
			sb1.append("update "+tablName+" t set DEL_FLG = 1 , DELETETIME=to_date('"+dateStr+"','yyyy-MM-dd HH24:mi:ss')  where "+idName+" in ("+idArg+")");
			SQLQuery query1 = getSession().createSQLQuery(sb1.toString());
			query1.executeUpdate();
		}
	}
	private String stringUtils(String ids){
		String idArg="";
		String[] idArgs=ids.split(",");
		if(idArgs.length>1){
			for(int i=0;i<idArgs.length;i++){
				idArg +="'"+idArgs[i]+"'"+",";
			}
			idArg=idArg.substring(0, idArg.length()-1);
		}else{
			idArg="'"+ids+"'";
		}
		return idArg;
	}
	
	/**
	 * 组套修改的保存方法
	 */
	public boolean saveOrUpdate(BusinessStack businessStack, String stackInfosJson) {
		//将json转化为map的list
		List<BusinessStackinfo> stackInfoMapList =new ArrayList<BusinessStackinfo>();
		//登录信息
		User user = (User) SessionUtils.getCurrentUserFromShiroSession();
		SysDepartment department = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(StringUtils.isNotBlank(stackInfosJson) && !"[]".equals(stackInfosJson)){
			try {
				stackInfosJson=stackInfosJson.replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
						.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
						.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
				stackInfoMapList = JSONUtils.fromJson(stackInfosJson,new TypeToken<List<BusinessStackinfo>>(){});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(businessStack.getId()==null){//保存操作
			businessStack.setCreateDept(department.getDeptCode());
			businessStack.setCreateTime(new Date());
			businessStack.setCreateUser(user.getAccount());
			if(StringUtils.isNotEmpty(businessStack.getName())){
				String str = super.getSpellCode(businessStack.getName());
				int index=str.indexOf("$");
				String pinyin=str.substring(0,index);
				String wb=str.substring(index+1);
				businessStack.setPinYin(pinyin);
				businessStack.setWb(wb);
			}
			businessStack.setIsValid(1);
			//保存主表信息
			super.save(businessStack);
			this.getSession().flush();
			//将id 循环插入stackInfo的外键字段
			if(stackInfoMapList.size()>0){
				for (BusinessStackinfo businessStackinfo : stackInfoMapList) {
					businessStackinfo.setStackId(businessStack);
				}
				//批量保存子表
				for (int i=0;i<stackInfoMapList.size();i++) {
					super.save(stackInfoMapList.get(i));
				}
			}
		}else{//修改操作
			businessStack.setUpdateTime(new Date());
			businessStack.setUpdateUser(user.getAccount());
			if(StringUtils.isNotEmpty(businessStack.getName())){
				String str = super.getSpellCode(businessStack.getName());
				int index=str.indexOf("$");
				String pinyin=str.substring(0,index);
				String wb=str.substring(index+1);
				businessStack.setPinYin(pinyin);
				businessStack.setWb(wb);
			}
			//保存主表信息
			super.save(businessStack);
			this.getSession().flush();
			//将id 循环插入stackInfo的外键字段
			if(stackInfoMapList.size()>0){
					try {
						for (BusinessStackinfo businessStackinfo : stackInfoMapList) {
							businessStackinfo.setStackId(businessStack);
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				
				//批量保存子表
				for (int i=0;i<stackInfoMapList.size();i++) {
					if("".equals(stackInfoMapList.get(i).getId())){
						businessStack.setId(null);
					}
					super.save(stackInfoMapList.get(i));
				}
			}
		}
		return true;
	}
	/**
	 * 分页查询组套主表信息 
	 */
	public List<BusinessStack> getPage(String page, String rows,
			BusinessStack entity) {
		String hql = joint(entity);
		return super.getPage(hql, page, rows);
	}
	/**
	 * 获得分页总数
	 */
	public int getTotal(BusinessStack entity) {
		String hql = joint(entity);
		return super.getTotal(hql);
	}
	//查询组套信息hql
	public String joint(BusinessStack entity){
		String hql="from BusinessStack bs where bs.del_flg=0 and bs.stop_flg=0 ";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getDeptId()) && !("1").equals(entity.getDeptId())){
				hql = hql+" AND  bs.deptId= '"+entity.getDeptId()+"'";
			}
			if(StringUtils.isNotEmpty(entity.getName())){
				hql = hql +" AND (bs.name LIKE '%"+entity.getName()+"%'"
				  + " OR bs.pinYin LIKE '%"+entity.getName().toUpperCase()+"%'" 
				  + " OR bs.wb LIKE '%"+entity.getName().toUpperCase()+"%'"
				  + " OR bs.inputCode LIKE '%"+entity.getName().toUpperCase()+"%')";
			}
			if(StringUtils.isNotBlank(entity.getParent())){
				hql = hql+" AND bs.parent='"+entity.getParent()+"'";
			}
		}
		return hql;
	}
	
	public List stackInfoName(String page, String rows) {
		StringBuilder sql = new StringBuilder();
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		sql.append("select t.drug_id as id,t.drug_name as name,t.drug_spec as spec,t.drug_packagingunit as drugPackagingunit,t.drug_minimumunit as unit,"
				+ "t.drug_packagingnum as packagingnum,t.drug_retailprice as defaultprice,null as childrenprice,t.drug_doseunit as drugDoseunit,null as "
				+ "specialprice from t_drug_info t where t.del_flg=0 and t.stop_flg=0");
		sql.append(" union ");
		sql.append("select t1.undrug_id as id,t1.undrug_name as name,t1.undrug_spec as spec,null as drugPackagingunit,t1.undrug_unit as unit,"
				+ "null as packagingnum,t1.undrug_defaultprice as defaultprice,t1.undrug_childrenprice as childrenprice,null as drugDoseunit,"
				+ "t1.undrug_specialprice as specialprice from t_drug_undrug t1 where t1.del_flg=0 and t1.stop_flg=0");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString()).addScalar("id").addScalar("name")
								.addScalar("spec").addScalar("drugPackagingunit").addScalar("unit").addScalar("packagingnum",Hibernate.INTEGER)
								.addScalar("defaultprice",Hibernate.DOUBLE).addScalar("childrenprice",Hibernate.DOUBLE)
								.addScalar("specialprice",Hibernate.DOUBLE).addScalar("drugDoseunit");
		List<DrugInfo> list = queryObject.setResultTransformer(Transformers.aliasToBean(DrugInfo.class)).setFirstResult((start - 1) * count).setMaxResults(count).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return null;
	}
	
	public void saveOrupdataBusinessStack(BusinessStack businessStack) {
		if(StringUtils.isNotEmpty(businessStack.getName())){
			String str = super.getSpellCode(businessStack.getName());
			int index=str.indexOf("$");
			String pinyin=str.substring(0,index);
			String wb=str.substring(index+1);
			businessStack.setPinYin(pinyin);
			businessStack.setWb(wb);
		}
		if(StringUtils.isNotBlank(businessStack.getId())){
			businessStack.setId(null);
		}
		businessStack.setParent("1");
		businessStack.setDel_flg(0);
		businessStack.setStop_flg(0);
		super.save(businessStack);
	}
	
	public void deleteBusinessStack(BusinessStack businessStack) {
		StringBuilder sql = new StringBuilder();
		sql.append("update BusinessStack set deleteUser="+businessStack.getDeleteUser()+",deleteTime="+businessStack.getDeleteTime()+",del_Flg =1 ");
		sql.append("where id in ('"+businessStack.getId()+"')");
		this.getSession().createQuery(sql.toString()).executeUpdate();
	}
	
	public BusinessStack getBusinessStackById(BusinessStack businessStack) {
		StringBuilder sql = new StringBuilder();
		if(businessStack.getId() != null ){
			sql.append("from BusinessStack where Id = "+businessStack.getId()+"");
			return (BusinessStack) this.getSession().get(BusinessStack.class, businessStack.getId());
		}
		return businessStack;
	}
	
	public List<BusinessStack> getSysDepartmentById(String id) {
		String hql="FROM BusinessStack s WHERE s.del_flg = 0 AND s.stop_flg = 0 AND s.deptId = '"+id+"'";
		List<BusinessStack> stackList=super.findByObjectProperty(hql, null);
		if(stackList!=null && stackList.size()>0){
			return stackList;
		}
		return null;
	}
	
	public List<BusinessStack> getDepartmentById(String deptId) {
		String hql="FROM BusinessStack s WHERE s.del_flg = 0 AND s.stop_flg = 0 and s.deptId='"+deptId+"'";
		List<BusinessStack> stackList=super.findByObjectProperty(hql, null);
		if(stackList!=null && stackList.size()>0){
			return stackList;
		}
		return null;
	}
	/**  
	 *  根据组套类型获得组套信息
	 * @Description：  
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-7 上午11:12:09  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-7 上午11:12:09  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	
	public List<BusinessStack> getStackByType(int type, String id) {
		String hql="FROM BusinessStack s WHERE s.del_flg = 0 AND s.stop_flg = 0 AND s.type = '"+type+"' ";
		if(StringUtils.isNotBlank(id)){
			hql += " AND s.source = "+Integer.parseInt(id)+" ";
		}
		List<BusinessStack> stackList=super.findByObjectProperty(hql, null);
		if(stackList!=null && stackList.size()>0){
			return stackList;
		}
		return null;
	}
	
	public int countDrugAndUndrug(String page, String rows) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(sum) from (");
		sql.append("select count(1) as sum  from t_drug_info t where t.del_flg = 0 and t.stop_flg = 0 ");
		sql.append("union ");
		sql.append("select count(1) as sum from t_drug_undrug t1 where t1.del_flg = 0 and t1.stop_flg = 0)");
		Object count=getSession().createSQLQuery(sql.toString()).uniqueResult();
		return Integer.valueOf(count.toString());
	}
	/**  
	 *  
	 * @Description：   根据组套来源获得组套信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-8 下午06:54:22  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-8 下午06:54:22  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	
	public List<BusinessStack> getStackBySource(String id) {
		User user2 = (User) SessionUtils.getCurrentUserFromShiroSession();
		SysDepartment department = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String hql="";
		if("1".equals(id)){//全院组套
			hql = "FROM BusinessStack s WHERE s.del_flg = 0 AND s.stop_flg = 0 AND s.source = "+Integer.parseInt(id)+" ";
		}else if("2".equals(id)){//科室组套
			hql = "FROM BusinessStack s WHERE s.del_flg = 0 AND s.stop_flg = 0 AND s.source = "+Integer.parseInt(id)+" AND s.createDept = '"+department.getDeptCode()+"' ";
		}else if("3".equals(id)){//个人
			hql = "FROM BusinessStack s WHERE s.del_flg = 0 AND s.stop_flg = 0 AND s.source = "+Integer.parseInt(id)+" AND s.createUser = '"+user2.getAccount()+"' ";
		}else{
			return null;
		}
		List<BusinessStack> stackList=super.findByObjectProperty(hql, null);
		if(stackList!=null && stackList.size()>0){
			return stackList;
		}
		return new ArrayList<BusinessStack>();
	}
	
	@Override
	public List<SysDepartment> queryDept(String name) {
		String hql="from SysDepartment s where s.del_flg=0 and s.stop_flg=0 ";
		if(StringUtils.isNotBlank(name)){
			hql+=" and s.deptName like '%"+name+"%'";
		}
		List<SysDepartment> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<SysDepartment>();
	}
	@Override
	public List<BusinessDictionary> packagingUnitcomboBox(String pid, String uid,String name,String stackId) {
		String hql="from CodeDrugpackagingunit s where s.del_flg=0 and s.stop_flg=0 ";
		if(StringUtils.isNotBlank(pid)&&StringUtils.isBlank(uid)){
			hql+=" and s.id= '"+pid+"'";
		}
		if(StringUtils.isNotBlank(uid)&&StringUtils.isBlank(pid)){
			hql+=" and s.id = '"+uid+"'";
		}
		if(StringUtils.isNotBlank(stackId)){
			if(StringUtils.isNotBlank(pid)&&StringUtils.isNotBlank(pid)){
				hql+= " and (s.id= '"+pid+"' or s.id = '"+uid+"' ) ";
			}
		}
		if(StringUtils.isNotBlank(name)){
			hql+=" and s.name like '%"+name+"%'";
		}
		List<BusinessDictionary> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<BusinessDictionary>();
	}
	@Override
	public List<SysDepartment> getDeptName(String queryName) {
		String hql="From SysDepartment d where d.del_flg=0 and d.stop_flg=0";
		if(queryName!=null){    //根据部门名称、部门code、五笔码、拼音码、自定义码
			hql+=" and (d.deptName like '%"+queryName+"%' or d.deptCode like '%"+queryName+"%' "
				+ "or d.deptWb like '%"+queryName.toUpperCase()+"%'  or d.deptPinyin like '%"+queryName.toUpperCase()+"%' "
				+ "or d.deptInputcode like '%"+queryName.toUpperCase()+"%')";
		}
		List<SysDepartment> listDeptContact=super.find(hql, null);
		if(listDeptContact!=null&&listDeptContact.size()>0){
			return listDeptContact;
		}
		return new ArrayList<SysDepartment>();
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
	public List<BusinessStack> getStackInfo( String id,String deptId,String userId,String stackObject,String remark,String root) {
		String hql = " from BusinessStack k where k.stop_flg=0 and k.del_flg=0";
		/**
		 * 组套来源 1=id医生、id=2科室、id=3全院
		 * T_BUSINESS_STACK表中t.stack_deptid = 'ALL' or t.stack_deptid is null标识的是全院的信息
		 */
		if ("1".equals(id)) {
			hql += " and k.source=3  and k.doc ='"+userId+"'";
		}
		if ("2".equals(id)) {
			hql += " and k.source=2 and k.deptId='"+deptId+"'";
		}
		if ("3".equals(id)) {
			hql += " and k.source=1 and (k.deptId = 'ALL' or k.deptId is null)";
		}
		//stackObject(组套对象: 1是财务用，2是医嘱用)  remark(门诊还是住院，收费不区分，医嘱区分)
		if("1".equals(stackObject)){
			hql += " and k.stackObject=1";
		}
		if("2".equals(stackObject)){
			if("1".equals(remark)){
				hql += " and k.stackObject=2 and k.stackInpmertype=1 ";
			}else if("2".equals(remark)){
				hql += " and k.stackObject=2 and k.stackInpmertype=2 ";
			}
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
	public List<StackAndStockInfoInInterVo> queryStackInfoById(String id,String drugstoreId,String feelType) {
		if(StringUtils.isBlank(id)){
			return new ArrayList<StackAndStockInfoInInterVo>();
		}
		String hql = "";
		if("1".equals(feelType) || StringUtils.isNotBlank(drugstoreId)){
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
					"BS.ONCE_DOSE AS drugOncedosage, "+//一次用量
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
					"NULL AS labsample "+//样本类型
			"FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_INFO D "+
			"LEFT JOIN "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_STOCKINFO S ON D.DRUG_ID = S.DRUG_ID AND S.STORAGE_DEPTID='"+drugstoreId+"' "+
			"LEFT JOIN "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_STACKINFO BS ON BS.STACKINFO_ITEMID = D.DRUG_ID "+
			"WHERE D.DRUG_ID IN ( "+
					"SELECT B.STACKINFO_ITEMID  "+
					"FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_STACKINFO B "+
					"WHERE B.STACK_ID = '"+id+"' "+
			") "+
			"AND BS.STACK_ID = '"+id+"' and D.del_flg=0 and D.stop_flg=0"+
	"UNION "+
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
					"U.UNDRUG_LABSAMPLE AS labsample "+//样本类型
			"FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_UNDRUG U "+
			"LEFT JOIN "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_STACKINFO BS ON BS.STACKINFO_ITEMID = U.UNDRUG_ID "+
			"WHERE U.UNDRUG_ID IN ( "+
					"SELECT B.STACKINFO_ITEMID FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_STACKINFO B "+
					"WHERE B.STACK_ID = '"+id+"'"+
			") "+
			"AND BS.STACK_ID = '"+id+"'  and U.stop_flg=0 and U.del_flg=0";
		
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
					"U.UNDRUG_LABSAMPLE AS labsample "+//样本类型
			"FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_DRUG_UNDRUG U "+
			"LEFT JOIN "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_STACKINFO BS ON BS.STACKINFO_ITEMID = U.UNDRUG_ID "+
			"WHERE U.UNDRUG_ID IN ( "+
					"SELECT B.STACKINFO_ITEMID FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_STACKINFO B "+
					"WHERE B.STACK_ID = '"+id+"' "+
			") "+
			"AND BS.STACK_ID = '"+id+"' and U.stop_flg=0 and U.del_flg=0";
		
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
				.addScalar("labsample");//样本类型
		List<StackAndStockInfoInInterVo> list = query.setResultTransformer(Transformers.aliasToBean(StackAndStockInfoInInterVo.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<StackAndStockInfoInInterVo>();
	}
	@Override
	public List<BusinessStack> queryStackByIdparent(String parent) {
		String hql=" from BusinessStack y where y.stop_flg=0 and y.del_flg=0 and y.parent = :parent";
		List<BusinessStack> list = this.getSession().createQuery(hql).setParameter("parent", parent).list();
		if (list.size() > 0 && list != null) {
			return list;
		}
		return new ArrayList<BusinessStack>();
	}
	@Override
	public List<BusinessStack> getstackNameParam(String stackName,String stackObject,String remark) {
		SysDepartment deptId=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String deptCode =null;
		if(StringUtils.isNotBlank(deptCode)){
			deptCode = deptId.getDeptCode();
		}
		String userId=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		//1.医生
		String hql = " from BusinessStack k where k.stop_flg=0 and k.del_flg=0";
		hql += " and k.source=3  and k.doc ='"+userId+"'";
		//stackObject(组套对象: 1是财务用，2是医嘱用)  remark(门诊还是住院，收费不区分，医嘱区分)
		if("1".equals(stackObject)){
			hql += " and k.stackObject=1";
		}
		if("2".equals(stackObject)){
			if("1".equals(remark)){
				hql += " and k.stackObject=2 and k.stackInpmertype=1 ";
			}else if("2".equals(remark)){
				hql += " and k.stackObject=2 and k.stackInpmertype=2 ";
			}
		}
		hql += " and k.parent = 'ROOT' ";
		hql += " and (k.name like :stackName or k.pinYin like :stackName or k.wb like :stackName or k.inputCode like :stackName) ";
		List<BusinessStack> list = this.getSession().createQuery(hql).setParameter("stackName", "%"+stackName+"%").list();
		
		//2.科室
		String hql1 = " from BusinessStack k where k.stop_flg=0 and k.del_flg=0";
		hql1 += " and k.source=2 and k.deptId='"+deptCode+"'";
		//stackObject(组套对象: 1是财务用，2是医嘱用)  remark(门诊还是住院，收费不区分，医嘱区分)
		if("1".equals(stackObject)){
			hql1 += " and k.stackObject=1";
		}
		if("2".equals(stackObject)){
			if("1".equals(remark)){
				hql1 += " and k.stackObject=2 and k.stackInpmertype=1 ";
			}else if("2".equals(remark)){
				hql1 += " and k.stackObject=2 and k.stackInpmertype=2 ";
			}
		}
		hql1 += " and k.parent = 'ROOT' ";
		hql1 += " and (k.name like :stackName or k.pinYin like :stackName or k.wb like :stackName or k.inputCode like :stackName) ";
		List<BusinessStack> list1 = this.getSession().createQuery(hql1).setParameter("stackName", "%"+stackName+"%").list();
		
		//3.全院
		String hql2 = " from BusinessStack k where k.stop_flg=0 and k.del_flg=0";
		hql2 += " and k.source=1 and (k.deptId = 'ALL' or k.deptId is null)";
		//stackObject(组套对象: 1是财务用，2是医嘱用)  remark(门诊还是住院，收费不区分，医嘱区分)
		if("1".equals(stackObject)){
			hql2 += " and k.stackObject=1";
		}
		if("2".equals(stackObject)){
			if("1".equals(remark)){
				hql2 += " and k.stackObject=2 and k.stackInpmertype=1 ";
			}else if("2".equals(remark)){
				hql2 += " and k.stackObject=2 and k.stackInpmertype=2 ";
			}
		}
		hql2 += " and k.parent = 'ROOT' ";
		hql2 += " and (k.name like :stackName or k.pinYin like :stackName or k.wb like :stackName or k.inputCode like :stackName) ";
		List<BusinessStack> list2 = this.getSession().createQuery(hql2).setParameter("stackName", "%"+stackName+"%").list();
		
		List<BusinessStack> businessStacks = new ArrayList<BusinessStack>();
		businessStacks.addAll(list);
		businessStacks.addAll(list1);
		businessStacks.addAll(list2);
		if (businessStacks.size() > 0 && businessStacks != null) {
			return businessStacks;
		}
		return new ArrayList<BusinessStack>();
	}
}
