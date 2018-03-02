package cn.honry.statistics.operation.operaction.dao.impl;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.OperationApply;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.operation.operaction.dao.OperactionActionDao;
import cn.honry.statistics.operation.operationDept.vo.OpDoctorDetailVo;
import cn.honry.statistics.operation.operationDetails.vo.OperationDetailsVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import freemarker.template.SimpleDate;


@Repository("operactionActionDao")
@SuppressWarnings({ "all" })
public class OperactionActionDaoImpl extends HibernateEntityDao<OperationApply> implements OperactionActionDao{

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	

	/**  
	 * 
	 * 手术耗材统计查询
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:deptId 科室code
	 * @param:repno 记账编号
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public List<InpatientItemListNow> getOperactionlist(String login,String end,Double price,String repno,String deptId,String page,String rows) {
		if(StringUtils.isBlank(end)){
			end=DateUtils.getDate();
		}
		List<String> tnL = null;
		try {
			Date sTime = DateUtils.parseDateY_M_D(login);
			Date eTime = DateUtils.parseDateY_M_D(end);
			tnL = new ArrayList<String>();
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",login,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),login);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		
		if(tnL==null||tnL.size()<0){
			return  new ArrayList<InpatientItemListNow>();
		}
		
		String hql=sql2(login,end,price,repno, deptId,tnL);
		
		SQLQuery query=this.getSession().createSQLQuery(hql).addScalar("undrugGbcode").addScalar("itemName")
				.addScalar("unitPrice",Hibernate.DOUBLE).addScalar("qty",Hibernate.DOUBLE).addScalar("totCost",Hibernate.DOUBLE).addScalar("itemCode");
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		List<InpatientItemListNow> list=query.setFirstResult((start-1)*count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(InpatientItemListNow.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientItemListNow>();
		
	}
	/**  
	 * 
	 * 拼接sql(手术耗材)
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:deptId 科室code
	 * @param:repno记账编号
	 * @throws:
	 * @return: void
	 *
	 */
	private String sql2(String login,String end,Double price,String repno,String deptId,List<String> tnL){
		StringBuffer sb=new StringBuffer();
		if(tnL.size()>1){
			sb.append(" select tt.undrugGbcode,tt.itemName,tt.unitPrice,tt.qty,"
					+ "tt.totCost,tt.itemCode from (");
		}
		for(int i=0;i<tnL.size();i++){
			if(i>0){
				sb.append(" union all ");
			}
		
		sb.append("select u.undrug_gbcode as undrugGbcode,");
		sb.append( "t").append(i).append(".item_name as itemName,");
		sb.append( "round(t").append(i).append(".unit_price,2) as unitPrice,");
		sb.append( "round(sum(t").append(i).append(".qty),2) qty,");
		sb.append( "round(sum(t").append(i).append(".tot_cost),2) as totCost,");
		sb.append("t").append(i).append(".item_code as itemCode from ").append(tnL.get(i)).append(" t").append(i).append(" left join t_drug_undrug u on "
				+ "u.UNDRUG_CODE=t").append(i).append(".item_code where t").append(i).append(".del_flg=0 and t").append(i).append(".stop_flg=0  ");
		if(StringUtils.isNotBlank(login)){
			sb.append(" and t").append(i).append(".fee_date>=to_date('"+login+"','yyyy-MM-dd HH24:mi:ss') ");
		}
		if(StringUtils.isNotBlank(end)){
			sb.append(" and t").append(i).append(".fee_date<=to_date('"+end+"','yyyy-MM-dd HH24:mi:ss') ");
		}
		if(StringUtils.isNotBlank(repno)){
			sb.append(" and u.undrug_gbcode like '%"+repno+"' ");
		}
		if(price!=null){
			sb.append(" and t").append(i).append(".unit_price>='"+price+"' ");
		}
		sb.append("group by item_code,item_name,unit_price,undrug_gbcode ");
		}
		if(tnL.size()>1){
			sb.append(" )tt order by itemName ");
		}
		return sb.toString();
	}
	/**  
	 * 
	 * 手术耗材统计明细List
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:repno 记账编码
	 * @param:deptId 科室
	 * @param:identityCard 身份证号
	 * @param:page页数
	 * @param:rows行数
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public List<InpatientItemListNow> getoperationDetailsList(String login,
			String end, Double price, String repno,String page,String rows, String identityCard) {
		if(StringUtils.isBlank(end)){
			end=DateUtils.getDate();
		}
		List<String> tnL = null;
		try {
			Date sTime = DateUtils.parseDateY_M_D(login);
			Date eTime = DateUtils.parseDateY_M_D(end);
			tnL = new ArrayList<String>();
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",login,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),login);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(tnL==null||tnL.size()<0){
			return  new ArrayList<InpatientItemListNow>();
		}
		String hql =sql(login,end,price,repno,tnL,identityCard);
		SQLQuery query=this.getSession().createSQLQuery(hql).addScalar("undrugGbcode").addScalar("itemName")
				.addScalar("unitPrice",Hibernate.DOUBLE).addScalar("qty",Hibernate.DOUBLE)
				.addScalar("totCost",Hibernate.DOUBLE).addScalar("name").addScalar("recipeDeptcode")
				.addScalar("recipeDoccode").addScalar("feeOpercode").addScalar("feeDate",Hibernate.TIMESTAMP)
				.addScalar("itemCode").addScalar("currentUnit").addScalar("inpatientNo").addScalar("spec");
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		List<InpatientItemListNow> list=query.setFirstResult((start-1)*count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(InpatientItemListNow.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientItemListNow>();
	}

	
	/**  
	 * 
	 * 手术耗材统计明细sql拼接
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:repno 记账编码
	 * @param:deptId 科室
	 * @param:identityCard 身份证号
	 * @throws:
	 * @return: void
	 *
	 */
   private String sql(String login,
			String end, Double price, String repno,List<String> tnL,String identityCard){
	   StringBuffer sb=new StringBuffer();
		if(tnL.size()>1){
			sb.append(" select tt.undrugGbcode,tt.itemName,tt.unitPrice,tt.qty,"
					+ "tt.totCost,tt.name,tt.recipeDeptcode,tt.recipeDoccode,tt.feeOpercode,tt.feeDate,"
					+ "tt.itemCode,tt.currentUnit,tt.inpatientNo,tt.spec from (");
		}
		for(int i=0;i<tnL.size();i++){
			if(i>0){
				sb.append(" union all ");
			}
			sb.append(" select u.undrug_gbcode as undrugGbcode,t").append(i).append(".item_name as itemName,t").append(i).append(".unit_price as unitPrice,t").append(i).append(".qty as qty,");
			sb.append("t").append(i).append(".tot_cost as totCost,t").append(i).append(".name as name,t").append(i).append(".recipe_deptname as recipeDeptcode,");
			sb.append("t").append(i).append(".recipe_docname as recipeDoccode,");
			sb.append("(select EMPLOYEE_NAME from T_EMPLOYEE where EMPLOYEE_JOBNO= t").append(i).append(".fee_opercode) as feeOpercode,"
					+ "t").append(i).append(".fee_date as feeDate,t").append(i).append(".item_code as itemCode,t").append(i).append(".current_unit as currentUnit,");
			sb.append("p.MEDICALRECORD_ID as inpatientNo,u.undrug_spec as spec ");
			sb.append( " from ").append(tnL.get(i)).append(" t").append(i);
			if("t_inpatient_itemlist".equals(tnL.get(i))){
				sb.append( " inner join  T_INPATIENT_INFO p on p.INPATIENT_NO= t").append(i).append(".inpatient_no  ");
			}else{
				sb.append( " inner join  T_INPATIENT_INFO_now p on p.INPATIENT_NO= t").append(i).append(".inpatient_no  ");
			}
			if(StringUtils.isNotBlank(identityCard)){
				sb.append(" and p.CERTIFICATES_NO='"+identityCard+"' ");
			}
			sb.append( " left join t_drug_undrug u on u.UNDRUG_CODE = t").append(i).append(".item_code  where"
					+ " t").append(i).append(".del_flg=0 and t").append(i).append(".stop_flg=0  ");
			
			
	       if(StringUtils.isNotBlank(login)){
	    	  sb.append( " and  t").append(i).append(".fee_date>=to_date('"+login+"','yyyy-MM-dd') ");
	       } 
	       if(StringUtils.isNotBlank(end)){
	    	  sb.append(" and  t").append(i).append(".fee_date<to_date('"+end+"','yyyy-MM-dd') ");
	       } 
			if(StringUtils.isNotBlank(repno)){
				sb.append(" and u.undrug_gbcode like'%"+repno+"' ");
			}
			if(price!=null){
				sb.append(" and t").append(i).append(".unit_price>='"+price+"' ");
			}
		}
		if(tnL.size()>1){
			sb.append(" )tt");
		}
		return sb.toString();
   }
   
	
	/**  
	 * 
	 * 手术耗材统计明细List(总条数)
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:repno 记账编码
	 * @param:deptId 科室
	 * @param:identityCard 身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public int getoperationDetailsTotal(String login, String end, Double price,
			String repno,String identityCard) {
		if(StringUtils.isBlank(end)){
			end=DateUtils.getDate();
		}
		List<String> tnL = null;
		try {
			Date sTime = DateUtils.parseDateY_M_D(login);
			Date eTime = DateUtils.parseDateY_M_D(end);
			tnL = new ArrayList<String>();
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",login,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),login);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(tnL==null||tnL.size()<0){
			return  0;
		}
		String hql =sql(login,end,price,repno,tnL,identityCard);
		SQLQuery query=this.getSession().createSQLQuery(hql).addScalar("undrugGbcode").addScalar("itemName")
				.addScalar("unitPrice",Hibernate.DOUBLE).addScalar("qty",Hibernate.DOUBLE)
				.addScalar("totCost",Hibernate.DOUBLE).addScalar("name").addScalar("recipeDeptcode")
				.addScalar("recipeDoccode").addScalar("feeOpercode").addScalar("feeDate",Hibernate.TIMESTAMP)
				.addScalar("itemCode").addScalar("currentUnit").addScalar("inpatientNo").addScalar("recipeDeptcode").addScalar("spec");
		List<InpatientItemListNow> list=query.setResultTransformer(Transformers.aliasToBean(InpatientItemListNow.class)).list();
		if(list!=null&&list.size()>0){
			return list.size();
		}
		return 0;
	}

	/**  
	 * 
	 * 导出手术耗材统计明细List
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:repno 记账编码
	 * @param:deptId 科室
	 * @param:identityCard 身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public List<InpatientItemListNow> queryInvLogExpoper(String login, String end,
			Double price, String repno,String identityCard) {
		if(StringUtils.isBlank(end)){
			end=DateUtils.getDate();
		}
		List<String> tnL = null;
		try {
			Date sTime = DateUtils.parseDateY_M_D(login);
			Date eTime = DateUtils.parseDateY_M_D(end);
			tnL = new ArrayList<String>();
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",login,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),login);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(tnL==null||tnL.size()<0){
			return  new ArrayList<InpatientItemListNow>();
		}
		String hql=sql(login,end,price,repno,tnL,identityCard);
		SQLQuery query=this.getSession().createSQLQuery(hql).addScalar("undrugGbcode").addScalar("itemName")
				.addScalar("unitPrice",Hibernate.DOUBLE).addScalar("qty",Hibernate.DOUBLE)
				.addScalar("totCost",Hibernate.DOUBLE).addScalar("name").addScalar("recipeDeptcode")
				.addScalar("recipeDoccode").addScalar("feeOpercode").addScalar("feeDate",Hibernate.TIMESTAMP)
				.addScalar("itemCode").addScalar("currentUnit").addScalar("inpatientNo").addScalar("recipeDeptcode").addScalar("spec");
		List<InpatientItemListNow> list=query.setResultTransformer(Transformers.aliasToBean(InpatientItemListNow.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	
	/**  
	 * 
	 * 手术耗材统计查询（总条数）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:deptId 科室code
	 * @param:repno 记账编号
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public int getgetOperationListTotal(String login, String end, Double price,
			String repno, String deptId) {
		if(StringUtils.isBlank(end)){
			end=DateUtils.getDate();
		}
		List<String> tnL = null;
		try {
			Date sTime = DateUtils.parseDateY_M_D(login);
			Date eTime = DateUtils.parseDateY_M_D(end);
			tnL = new ArrayList<String>();
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",login,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),login);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(tnL==null||tnL.size()<0){
			return  0;
		}
		String hql=sql2(login,end,price,repno, deptId,tnL);
		SQLQuery query=this.getSession().createSQLQuery(hql).addScalar("undrugGbcode").addScalar("itemName")
				.addScalar("unitPrice",Hibernate.DOUBLE).addScalar("qty",Hibernate.DOUBLE).addScalar("totCost",Hibernate.DOUBLE).addScalar("itemCode");
		List<InpatientItemListNow> list=query.setResultTransformer(Transformers.aliasToBean(InpatientItemListNow.class)).list();
		if(list!=null&&list.size()>0){
			return list.size();
		}
		return 0;
	}
	/**  
	 * 
	 * 导出手术耗材统计List
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:repno 记账编码
	 * @param:deptId 科室
	 * @param:identityCard 身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public List<InpatientItemListNow> queryInvLogExp(String login, String end,
			Double price, String repno, String string) {
		if(StringUtils.isBlank(end)){
			end=DateUtils.getDate();
		}
		List<String> tnL = null;
		try {
			Date sTime = DateUtils.parseDateY_M_D(login);
			Date eTime = DateUtils.parseDateY_M_D(end);
			tnL = new ArrayList<String>();
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",login,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),login);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(tnL==null||tnL.size()<0){
			return  new ArrayList<InpatientItemListNow>();
		}
		String hql=sql2(login,end,price,repno, string,tnL);
		SQLQuery query=this.getSession().createSQLQuery(hql).addScalar("undrugGbcode").addScalar("itemName")
				.addScalar("unitPrice",Hibernate.DOUBLE).addScalar("qty",Hibernate.DOUBLE).addScalar("totCost",Hibernate.DOUBLE).addScalar("itemCode");
		List<InpatientItemListNow> list=query.setResultTransformer(Transformers.aliasToBean(InpatientItemListNow.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	
	private StatVo findMaxMin() {
		StringBuffer sb=new StringBuffer();
		sb.append(" SELECT MAX(mn.FEE_DATE) AS eTime ,MIN(mn.FEE_DATE) AS sTime FROM t_inpatient_itemlist_now mn ");
		final String  sql=sb.toString();
		StatVo sta=(StatVo)this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public StatVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql);
				queryObject.addScalar("eTime",Hibernate.DATE).addScalar("sTime",Hibernate.DATE);
				return (StatVo) queryObject.setResultTransformer(Transformers.aliasToBean(StatVo.class)).uniqueResult();
			}
		});
		return sta;
	}
	/**  
	 * 
	 * 手术耗材统计明细(打印)-20170316 hedong 报表打印 window.open采用post方式提交参数示例
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:repno 记账编码
	 * @param:deptId 科室
	 * @param:identityCard 身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public List<OperationDetailsVo> queryInvLogDetails(String login, String end, Double price, String repno,String identityCard) {
		if(StringUtils.isBlank(end)){
			end=DateUtils.getDate();
		}
		List<String> tnL = null;
		try {
			Date sTime = DateUtils.parseDateY_M_D(login);
			Date eTime = DateUtils.parseDateY_M_D(end);
			tnL = new ArrayList<String>();
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",login,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),login);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(tnL==null||tnL.size()<0){
			return  new ArrayList<OperationDetailsVo>();
		}
		String hql=sql(login,end,price,repno,tnL, identityCard);
		SQLQuery query=this.getSession().createSQLQuery(hql).addScalar("undrugGbcode").addScalar("itemName")
				.addScalar("unitPrice",Hibernate.DOUBLE).addScalar("qty",Hibernate.DOUBLE)
				.addScalar("totCost",Hibernate.DOUBLE).addScalar("name").addScalar("recipeDeptcode")
				.addScalar("recipeDoccode").addScalar("feeOpercode").addScalar("feeDate",Hibernate.TIMESTAMP)
				.addScalar("itemCode").addScalar("currentUnit").addScalar("inpatientNo").addScalar("recipeDeptcode").addScalar("spec");
		List<OperationDetailsVo> list=query.setResultTransformer(Transformers.aliasToBean(OperationDetailsVo.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

}