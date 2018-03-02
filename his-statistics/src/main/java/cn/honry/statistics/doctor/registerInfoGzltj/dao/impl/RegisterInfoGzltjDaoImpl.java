package cn.honry.statistics.doctor.registerInfoGzltj.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.employee.dao.EmployeeInInterDAO;
import cn.honry.inner.outpatient.register.dao.RegisterInfoInInterDAO;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.doctor.registerInfoGzltj.dao.RegisterInfoGzltjDao;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Repository("registerInfoGzltjDAO")
@SuppressWarnings({ "all" })
public class RegisterInfoGzltjDaoImpl extends HibernateEntityDao<RegisterInfoGzltjVo> implements RegisterInfoGzltjDao{
	
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	private EmployeeInInterDAO employeeDAO;
	@Autowired
	private RegisterInfoInInterDAO RegisterInfoInInterDAO;
	
	/**  
	 *  
	 * @Description： 查询工作量列表
	 * @param Stime开始时间
	 * @param Etime结束时间
	 * @param dept科室id
	 * @param expxrt专家id
	 * @Author：wujiao
	 * @CreateDate：2016-4-26 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	//先查科室
	public List<RegisterInfoGzltjVo> findInfo(String Stime,String Etime,String dept,String expxrt) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select  distinct( e.employee_id) as expxrtId, d.dept_name as dept,  d.dept_id as deptId, ");
		sb.append(" e.employee_name as expxrt, g.grade_name as title,  g.grade_id  as titleId ");
		sb.append(" from T_REGISTER_SCHEDULE r  left join T_DEPARTMENT d on r.schedule_deptid=d.dept_id ");
		sb.append(" left join T_EMPLOYEE e on e.employee_id=r.schedule_doctor ");
		sb.append(" left join T_REGISTER_GRADE g on g.grade_id=r.schedule_reggrade ");
		sb.append(" where r.del_flg=0 and r.stop_flg=0 ");
		sb.append("  and TO_CHAR(r.schedule_date, 'yyyy-MM-dd') BETWEEN '"+Stime+"' and '"+Etime+"' ");
		
			//科室id不为空；
			if(StringUtils.isNotBlank(dept)){
				sb.append(" and d.dept_id='"+dept+"' ");
			}
			
			//专家id不为空；
			if(StringUtils.isNotBlank(expxrt)){
				sb.append(" and e.EMPLOYEE_ID='"+expxrt+"' ");
			}
			
			sb.append(" order by d.dept_name,e.employee_id ");
			Query query = this.getSession().createSQLQuery(sb.toString())
					.addScalar("dept").addScalar("deptId")
					.addScalar("expxrt").addScalar("expxrtId")
					.addScalar("title").addScalar("titleId");
			List<RegisterInfoGzltjVo> list = query.setResultTransformer(Transformers.aliasToBean(RegisterInfoGzltjVo.class)).list();
			
		return list;
	}

	/**  
	 * @Description：  跳转修改页面(查询同科室同级别医生)
	 * @param id
	 * @Author：liudelin
	 * @CreateDate：2015-6-24 下午05:45:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	
	public void registerTriageSave(String id, String expxrt) {
		String hql = "update RegisterInfo set expxrt=? where id=?";
		this.getSession().createQuery(hql).setString(0, expxrt)
		.setString(1, id).executeUpdate();
	}
	
	/**  
	 *  
	 * @Description： 查询
	 * @Author：liudelin
	 * @CreateDate：2015-6-25 下午01:44:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	
	public RegisterInfo queryRegisterTiragegl(String sEncode) {
		String hql = "from RegisterInfo where TO_CHAR(date,'YYYY-MM-DD') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' AND idcardId = '"+sEncode+"'";
		List<RegisterInfo>  registerInfoList = super.find(hql, null);
		if(registerInfoList!=null&&registerInfoList.size()>0){
			SysEmployee employee = employeeDAO.get(registerInfoList.get(0).getExpxrt());
			registerInfoList.get(0).setExpxrtName(employee.getName());
			return registerInfoList.get(0);
		}
		
		return new RegisterInfo();
	}
	
	/**  
	 *  
	 * @Description： 查询工作量列表 查询挂号科室表
	 * @param time时间
	 * @param deptId科室id
	 * @param expxrtId专家id
	 * @Author：wujiao
	 * @CreateDate：2016-4-26 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	
	public List<RegisterInfo> findPrereInfo(String time, String deptId, String expxrtId) {
			String hql = "from RegisterInfo i where TO_CHAR(i.date,'YYYY-MM-DD')='"+time+"' and i.del_flg=0 and i.stop_flg=0 and expxrt='"+expxrtId+"' and dept='"+deptId+"' ";
			List<RegisterInfo> list = RegisterInfoInInterDAO.findByObjectProperty(hql, null);
			
			return list;
	}
	/**  
	 * @Description： 查询工作量列表 查询挂号科室表
	 * @param time时间
	 * @param deptId科室id
	 * @param expxrtId专家id
	 * @Author：wujiao
	 * @CreateDate：2016-4-26 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	
	public List<RegisterInfoGzltjVo> findPrereSum(Map<Integer, String> map, String deptId, String expxrtId) {
		String monNum = map.get(1)==null?"":map.get(1).replaceAll(",", "','");
		String tueNum = map.get(2)==null?"":map.get(2).replaceAll(",", "','");
		String wedNum = map.get(3)==null?"":map.get(3).replaceAll(",", "','");
		String thuNum = map.get(4)==null?"":map.get(4).replaceAll(",", "','");
		String friNum = map.get(5)==null?"":map.get(5).replaceAll(",", "','");
		String satNum = map.get(6)==null?"":map.get(6).replaceAll(",", "','");
		String sunNum = map.get(7)==null?"":map.get(7).replaceAll(",", "','");
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append(" (SELECT COUNT(i.REGISTER_Id) FROM T_REGISTER_INFO i WHERE i.REGISTER_DEPT = '"+deptId+"' AND i.REGISTER_EXPERT = '"+expxrtId+"' "); 
		sb.append(" AND TO_CHAR(i.REGISTER_DATE,'yyyy-MM-dd') IN ('"+monNum+"')) AS monNum, "                                                        ); 
		sb.append(" (SELECT COUNT(i.REGISTER_Id) FROM T_REGISTER_INFO i WHERE i.REGISTER_DEPT = '"+deptId+"' AND i.REGISTER_EXPERT = '"+expxrtId+"' "); 
		sb.append(" AND TO_CHAR(i.REGISTER_DATE,'yyyy-MM-dd') IN ('"+tueNum+"')) AS tueNum, "                                                        ); 
		sb.append(" (SELECT COUNT(i.REGISTER_Id) FROM T_REGISTER_INFO i WHERE i.REGISTER_DEPT = '"+deptId+"' AND i.REGISTER_EXPERT = '"+expxrtId+"' "); 
		sb.append(" AND TO_CHAR(i.REGISTER_DATE,'yyyy-MM-dd') IN ('"+wedNum+"')) AS wedNum, "                                                        ); 
		sb.append(" (SELECT COUNT(i.REGISTER_Id) FROM T_REGISTER_INFO i WHERE i.REGISTER_DEPT = '"+deptId+"' AND i.REGISTER_EXPERT = '"+expxrtId+"' "); 
		sb.append(" AND TO_CHAR(i.REGISTER_DATE,'yyyy-MM-dd') IN ('"+thuNum+"')) AS thuNum, "                                                        ); 
		sb.append(" (SELECT COUNT(i.REGISTER_Id) FROM T_REGISTER_INFO i WHERE i.REGISTER_DEPT = '"+deptId+"' AND i.REGISTER_EXPERT = '"+expxrtId+"' "); 
		sb.append(" AND TO_CHAR(i.REGISTER_DATE,'yyyy-MM-dd') IN ('"+friNum+"')) AS friNum, "                                                        ); 
		sb.append(" (SELECT COUNT(i.REGISTER_Id) FROM T_REGISTER_INFO i WHERE i.REGISTER_DEPT = '"+deptId+"' AND i.REGISTER_EXPERT = '"+expxrtId+"' "); 
		sb.append(" AND TO_CHAR(i.REGISTER_DATE,'yyyy-MM-dd') IN ('"+satNum+"')) AS satNum, "                                                        ); 
		sb.append(" (SELECT COUNT(i.REGISTER_Id) FROM T_REGISTER_INFO i WHERE i.REGISTER_DEPT = '"+deptId+"' AND i.REGISTER_EXPERT = '"+expxrtId+"' "); 
		sb.append(" AND TO_CHAR(i.REGISTER_DATE,'yyyy-MM-dd') IN ('"+sunNum+"')) AS sunNum FROM dual ");
		
		Query query = this.getSession().createSQLQuery(sb.toString())
		.addScalar("monNum", Hibernate.INTEGER)
		.addScalar("tueNum", Hibernate.INTEGER)
		.addScalar("wedNum", Hibernate.INTEGER)
		.addScalar("thuNum", Hibernate.INTEGER)
		.addScalar("friNum", Hibernate.INTEGER)
		.addScalar("satNum", Hibernate.INTEGER)
		.addScalar("sunNum", Hibernate.INTEGER);
		
		List<RegisterInfoGzltjVo> list = query.setResultTransformer(Transformers.aliasToBean(RegisterInfoGzltjVo.class)).list();
		
		return list;
	}

	/**  
	 * 
	 * <p> 获取需要统计的表或分区表中的科室及医生信息  </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param：isZone是否连接分区tnL查询的表或分期集合stime开始时间etime结束时间dept科室编码expxrt医生编码
	 *
	 */
	
	public List<RegisterInfoGzltjVo> statRegDorWorkloadFindDept(List<String> tnL,final String stime,final String etime,final String dept,final String expxrt) {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<RegisterInfoGzltjVo>();
		}
		final StringBuffer sb = new StringBuffer();
		if(tnL.size()>1){
			sb.append("SELECT deptId,dept,expxrtId,expxrt,title FROM( ");
		}
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("UNION ");
			}
			
			sb.append("SELECT DISTINCT rm").append(i).append(".DEPT_CODE AS deptId,rm").append(i).append(".DEPT_NAME AS dept,rm")
			.append(i).append(".DOCT_CODE AS expxrtId,rm").append(i).append(".DOCT_NAME AS expxrt,rm").append(i).append(".REGLEVL_NAME AS title ");
			sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i).append(" WHERE rm")
			.append(i).append(".DEL_FLG = :flg AND rm").append(i).append(".STOP_FLG = :flg AND rm").append(i).append(".IN_STATE = :flg ");
			
			if(StringUtils.isNotBlank(stime)){
				sb.append("AND trunc(rm").append(i).append(".REG_DATE,'dd') >= to_date(:stime,'yyyy-MM-dd') ");
			}
			if(StringUtils.isNotBlank(etime)){
				sb.append("AND trunc(rm").append(i).append(".REG_DATE,'dd') <= to_date(:etime,'yyyy-MM-dd') ");
			}
			if(StringUtils.isNotBlank(dept)){
				sb.append("AND rm").append(i).append(".DEPT_CODE = :dept ");
			}
			if(StringUtils.isNotBlank(expxrt)){
				sb.append("AND rm").append(i).append(".DOCT_CODE = :expxrt ");
			}
		}
		if(tnL.size()>1){
			sb.append(") ");
		}
		sb.append("ORDER BY deptId ");
		List<RegisterInfoGzltjVo> voList = (List<RegisterInfoGzltjVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			
			public List<RegisterInfoGzltjVo> doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString())
				.addScalar("deptId")
				.addScalar("dept")
				.addScalar("expxrtId")
				.addScalar("expxrt")
				.addScalar("title");
				queryObject.setParameter("flg",0);
				if(StringUtils.isNotBlank(stime)){
					queryObject.setParameter("stime",stime);
				}
				if(StringUtils.isNotBlank(etime)){
					queryObject.setParameter("etime",etime );
				}
				if(StringUtils.isNotBlank(dept)){
					queryObject.setParameter("dept", dept);
				}
				if(StringUtils.isNotBlank(expxrt)){
					queryObject.setParameter("expxrt", expxrt);
				}
				return queryObject.setResultTransformer(Transformers.aliasToBean(RegisterInfoGzltjVo.class)).list();
			}
		});
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		
		return new ArrayList<RegisterInfoGzltjVo>();
	}

	/**  
	 * 
	 * <p> 根据科室及医生获取此医生一周的工作量  </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param：isZone是否连接分区tnL查询的表或分期集合stime开始时间etime结束时间vo统计对象
	 *
	 */
	
	public RegisterInfoGzltjVo statRegDorWorkloadFindInfo(List<String> tnL,final String stime,final String etime,final RegisterInfoGzltjVo vo) {
		if(tnL==null||tnL.size()<0){
			return new RegisterInfoGzltjVo();
		}
		final StringBuffer sb = new StringBuffer();
		if(tnL.size()>1){
			sb.append("SELECT SUM(monNum) AS monNum,SUM(tueNum) AS tueNum,SUM(wedNum) AS wedNum,SUM(thuNum) AS thuNum,SUM(friNum) AS friNum,SUM(satNum) AS satNum,SUM(sunNum) AS sunNum, ");
			sb.append("SUM(monCost) AS monCost,SUM(tueCost) AS tueCost,SUM(wedCost) AS wedCost,SUM(thuCost) AS thuCost,SUM(friCost) AS friCost,SUM(satCost) AS satCost,SUM(sunCost) AS sunCost, ");
			sb.append("SUM(num) AS num,SUM(cost) AS cost FROM(");
		}
		String[] numArr = {"monNum","tueNum","wedNum","thuNum","friNum","satNum","sunNum"};
		String[] costArr = {"monCost","tueCost","wedCost","thuCost","friCost","satCost","sunCost"};
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("UNION ALL ");
			}
			sb.append("SELECT ");
			
			for(int j=0;j<numArr.length;j++){
				sb.append("(SELECT COUNT(1) FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" mn").append(j+""+i)
				.append(" WHERE mn").append(j+""+i).append(".DEPT_CODE = :deptCode AND mn").append(j+""+i).append(".DOCT_CODE = :doctCode AND to_char(mn")
				.append(j+""+i).append(".REG_DATE,'D') = '").append(j+2>7?j-5:j+2).append("' AND mn").append(j+""+i)
				.append(".DEL_FLG = :flg AND mn").append(j+""+i).append(".STOP_FLG = :flg AND mn").append(j+""+i).append(".IN_STATE = :flg ");
				if(StringUtils.isNotBlank(stime)){
					sb.append("AND trunc(mn").append(j+""+i).append(".REG_DATE,'dd') >= to_date(:stime,'yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(etime)){
					sb.append("AND trunc(mn").append(j+""+i).append(".REG_DATE,'dd') <= to_date(:etime,'yyyy-MM-dd') ");
				}
				sb.append(") AS ").append(numArr[j]).append(", ");
			}
			for(int j=7;j<numArr.length+7;j++){
				sb.append("(SELECT SUM(mn").append(j+""+i).append(".REG_FEE+mn").append(j+""+i).append(".CHCK_FEE+mn").append(j+""+i)
				.append(".DIAG_FEE+mn").append(j+""+i).append(".OTH_FEE+mn").append(j+""+i).append(".BOOK_FEE) FROM ").append(HisParameters.HISPARSCHEMAHISUSER)
				.append(tnL.get(i)).append(" mn").append(j+""+i).append(" WHERE mn").append(j+""+i).append(".DEPT_CODE = :deptCode AND mn").append(j+""+i)
				.append(".DOCT_CODE = :doctCode AND to_char(mn").append(j+""+i).append(".REG_DATE,'D') = '").append(j-5>7?j-12:j-5).append("' AND mn")
				.append(j+""+i).append(".DEL_FLG = :flg AND mn").append(j+""+i).append(".STOP_FLG = :flg AND mn").append(j+""+i).append(".IN_STATE = :flg ");
				if(StringUtils.isNotBlank(stime)){
					sb.append("AND trunc(mn").append(j+""+i).append(".REG_DATE,'dd') >= to_date(:stime,'yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(etime)){
					sb.append("AND trunc(mn").append(j+""+i).append(".REG_DATE,'dd') <= to_date(:etime,'yyyy-MM-dd') ");
				}
				sb.append(") AS ").append(costArr[j-7]).append(", ");
			}
			sb.append("(SELECT COUNT(1) FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" mnx").append(i).append(" WHERE mnx").append(i)
			.append(".DEPT_CODE = :deptCode AND mnx").append(i).append(".DOCT_CODE = :doctCode AND mnx").append(i).append(".DEL_FLG = :flg AND mnx")
			.append(i).append(".STOP_FLG = :flg AND mnx").append(i).append(".IN_STATE = :flg ");
			if(StringUtils.isNotBlank(stime)){
				sb.append("AND trunc(mnx").append(i).append(".REG_DATE,'dd') >= to_date(:stime,'yyyy-MM-dd') ");
			}
			if(StringUtils.isNotBlank(etime)){
				sb.append("AND trunc(mnx").append(i).append(".REG_DATE,'dd') <= to_date(:etime,'yyyy-MM-dd') ");
			}
			sb.append(") AS num, ");
			sb.append("(SELECT SUM(mny").append(i).append(".REG_FEE+mny").append(i).append(".CHCK_FEE+mny").append(i).append(".DIAG_FEE+mny").append(i)
			.append(".OTH_FEE+mny").append(i).append(".BOOK_FEE) FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" mny").append(i)
			.append(" WHERE mny").append(i).append(".DEPT_CODE = :deptCode AND mny").append(i).append(".DOCT_CODE = :doctCode AND mny").append(i)
			.append(".DEL_FLG = :flg AND mny").append(i).append(".STOP_FLG = :flg AND mny").append(i).append(".IN_STATE = :flg ");
			if(StringUtils.isNotBlank(stime)){
				sb.append("AND trunc(mny").append(i).append(".REG_DATE,'dd') >= to_date(:stime,'yyyy-MM-dd') ");
			}
			if(StringUtils.isNotBlank(etime)){
				sb.append("AND trunc(mny").append(i).append(".REG_DATE,'dd') <= to_date(:etime,'yyyy-MM-dd') ");
			}
			sb.append(") AS cost ");
			sb.append(" FROM dual ");
		}
		if(tnL.size()>1){
			sb.append(") ");
		}
		RegisterInfoGzltjVo voa = (RegisterInfoGzltjVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			
			public RegisterInfoGzltjVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString())
				.addScalar("monNum",Hibernate.INTEGER)
				.addScalar("tueNum",Hibernate.INTEGER)
				.addScalar("wedNum",Hibernate.INTEGER)
				.addScalar("thuNum",Hibernate.INTEGER)
				.addScalar("friNum",Hibernate.INTEGER)
				.addScalar("satNum",Hibernate.INTEGER)
				.addScalar("sunNum",Hibernate.INTEGER)
				.addScalar("monCost",Hibernate.DOUBLE)
				.addScalar("tueCost",Hibernate.DOUBLE)
				.addScalar("wedCost",Hibernate.DOUBLE)
				.addScalar("thuCost",Hibernate.DOUBLE)
				.addScalar("friCost",Hibernate.DOUBLE)
				.addScalar("satCost",Hibernate.DOUBLE)
				.addScalar("sunCost",Hibernate.DOUBLE)
				.addScalar("num",Hibernate.INTEGER)
				.addScalar("cost",Hibernate.DOUBLE);
				
				queryObject.setParameter("deptCode",vo.getDeptId()).setParameter("doctCode",vo.getExpxrtId()).setParameter("flg",0);
				if(StringUtils.isNotBlank(stime)){
					queryObject.setParameter("stime",stime);
				}
				if(StringUtils.isNotBlank(etime)){
					queryObject.setParameter("etime",etime );
				}
				return (RegisterInfoGzltjVo) queryObject.setResultTransformer(Transformers.aliasToBean(RegisterInfoGzltjVo.class)).uniqueResult();
			}
		});
		vo.setMonNum(voa.getMonNum()==null?0:voa.getMonNum());
		vo.setTueNum(voa.getTueNum()==null?0:voa.getTueNum());
		vo.setWedNum(voa.getWedNum()==null?0:voa.getWedNum());
		vo.setThuNum(voa.getThuNum()==null?0:voa.getThuNum());
		vo.setFriNum(voa.getFriNum()==null?0:voa.getFriNum());
		vo.setSatNum(voa.getSatNum()==null?0:voa.getSatNum());
		vo.setSunNum(voa.getSunNum()==null?0:voa.getSunNum());
		vo.setMonCost(voa.getMonCost()==null?0:voa.getMonCost());
		vo.setTueCost(voa.getTueCost()==null?0:voa.getTueCost());
		vo.setWedCost(voa.getWedCost()==null?0:voa.getWedCost());
		vo.setThuCost(voa.getThuCost()==null?0:voa.getThuCost());
		vo.setFriCost(voa.getFriCost()==null?0:voa.getFriCost());
		vo.setSatCost(voa.getSatCost()==null?0:voa.getSatCost());
		vo.setSunCost(voa.getSunCost()==null?0:voa.getSunCost());
		vo.setNum(voa.getNum()==null?0:voa.getNum());
		vo.setCost(voa.getCost()==null?0:voa.getCost());
		
		return vo;
	}

	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	
	public StatVo findMaxMin() {
		final String sql = "SELECT MAX(mn.REG_DATE) AS eTime ,MIN(mn.REG_DATE) AS sTime FROM T_REGISTER_MAIN_NOW mn";
		StatVo vo = (StatVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			
			public StatVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("eTime",Hibernate.DATE).addScalar("sTime",Hibernate.DATE);
				return (StatVo) queryObject.setResultTransformer(Transformers.aliasToBean(StatVo.class)).uniqueResult();
			}
		});
		
		return vo;
	}

}
