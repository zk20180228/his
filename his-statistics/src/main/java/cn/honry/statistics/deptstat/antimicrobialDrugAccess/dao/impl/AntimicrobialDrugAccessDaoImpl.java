package cn.honry.statistics.deptstat.antimicrobialDrugAccess.dao.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.base.bean.model.DrugChecklogs;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.deptstat.antimicrobialDrugAccess.dao.AntimicrobialDrugAccessDao;
import cn.honry.statistics.deptstat.antimicrobialDrugAccess.vo.AntimicrobialDrugAccessVo;
import cn.honry.statistics.deptstat.operationProportion.dao.OperationProportionDao;
import cn.honry.statistics.deptstat.operationProportion.vo.OperationProportionVo;
import cn.honry.statistics.doctor.wordLoadDoctorTotal.vo.WordLoadVO;
import cn.honry.utils.ShiroSessionUtils;

@Repository("antimicrobialDrugAccessDao")
@SuppressWarnings({"all"})
public class AntimicrobialDrugAccessDaoImpl extends HibernateEntityDao<AntimicrobialDrugAccessVo> implements AntimicrobialDrugAccessDao{
	// 为父类HibernateDaoSupport注入sessionFactory的值
		@Resource(name = "sessionFactory")
		public void setSuperSessionFactory(SessionFactory sessionFactory) {
			super.setSessionFactory(sessionFactory);
		}
		@Resource
		private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
		@Autowired
		@Qualifier(value="deptInInterService")
		private DeptInInterService deptInInterService;
		
		public void setDeptInInterService(DeptInInterService deptInInterService) {
			this.deptInInterService = deptInInterService;
		}
		@Autowired
		@Qualifier(value="dataJurisInInterDAO")
		private DataJurisInInterDAO dataJurisInInterDAO;
		
		public void setDataJurisInInterDAO(DataJurisInInterDAO dataJurisInInterDAO) {
			this.dataJurisInInterDAO = dataJurisInInterDAO;
		}

		@Override
		public List<AntimicrobialDrugAccessVo> queryType() {
			String sql="select t.code_encode tcode,t.code_name tname  from t_business_dictionary t where ext_c1='人员类型' ";
			List<AntimicrobialDrugAccessVo> list = this.getSession().createSQLQuery(sql).addScalar("tcode").addScalar("tname")
			.setResultTransformer(Transformers.aliasToBean(AntimicrobialDrugAccessVo.class)).list();
			return list;
		}

		@Override
		public List<AntimicrobialDrugAccessVo> queryAntimicrobialDrugAccess(
				String dept,String page,String rows,String menuAlias) {
			Integer p=Integer.parseInt(page);
			Integer r=Integer.parseInt(rows);
			Integer s=(p-1)*r+1;
			Integer e=s+r-1;
			String sql="select * from (Select  ename,ecode,elevel,eaccess,rownum rn  From (select a.EMPLOYEE_name ename,a.EMPLOYEE_JOBNO ecode,(select d.code_name  from t_business_dictionary d where "
					+ "d.code_encode = a.EMPLOYEE_TITLE  and d.code_type = 'title'  and d.CODE_VALID_STATE = '0')  elevel,case when a.EMPLOYEE_TITLE in ('1', '2', '3') "
					+ "then '特殊级抗菌药物、限制级抗菌药物、非限制级抗菌药物'  when a.EMPLOYEE_TITLE = '4' then  '限制级抗菌药物、非限制级抗菌药物' "
					+ " when a.EMPLOYEE_TITLE in ('5', '33') then  '非限制级抗菌药物'  end  eaccess from t_employee a where  A.DEL_FLG = '0' ";
			if(StringUtils.isNotBlank(dept)){
				sql+="  and a.DEPT_ID in ("+dept+")";
			}else{
				sql+="  and a.DEPT_ID in ("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+")";
			}
			sql+="  order by a.EMPLOYEE_TITLE )) r where rn between "+s+" and "+e;
			SQLQuery sqlquery= this.getSession().createSQLQuery(sql).addScalar("ename").addScalar("ecode").addScalar("elevel").addScalar("eaccess");
			if(StringUtils.isBlank(dept)){
				sqlquery.setParameter("menuAlias", menuAlias).setParameter("user", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			}
			List<AntimicrobialDrugAccessVo> list =sqlquery.setResultTransformer(Transformers.aliasToBean(AntimicrobialDrugAccessVo.class)).list();
			return list;
		}

		@Override
		public int queryAntimicrobialDrugAccessTotal(String dept,String menuAlias) {
			String sql="select count(1) total from (Select  ename,ecode,elevel,eaccess  From (select a.EMPLOYEE_name ename,a.EMPLOYEE_JOBNO ecode,(select d.code_name  from t_business_dictionary d where "
					+ "d.code_encode = a.EMPLOYEE_TITLE  and d.code_type = 'title'  and d.CODE_VALID_STATE = '0')  elevel,case when a.EMPLOYEE_TITLE in ('1', '2', '3') "
					+ "then '特殊级抗菌药物、限制级抗菌药物、非限制级抗菌药物'  when a.EMPLOYEE_TITLE = '4' then  '限制级抗菌药物、非限制级抗菌药物' "
					+ " when a.EMPLOYEE_TITLE in ('5', '33') then  '非限制级抗菌药物'  end  eaccess from t_employee a where  A.DEL_FLG = '0' ";
			if(StringUtils.isNotBlank(dept)){
				sql+="  and a.DEPT_ID in ("+dept+")";
			}else{
				sql+="  and a.DEPT_ID in ("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+")";
			}
			sql+="))" ;
			SQLQuery sqlQuery= this.getSession().createSQLQuery(sql).addScalar("total",Hibernate.INTEGER);
			if(StringUtils.isBlank(dept)){
				sqlQuery.setParameter("menuAlias", menuAlias).setParameter("user", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			}
			AntimicrobialDrugAccessVo vo=(AntimicrobialDrugAccessVo) sqlQuery.setResultTransformer(Transformers.aliasToBean(AntimicrobialDrugAccessVo.class)).uniqueResult();
			if(vo!=null&&vo.getTotal()!=null){
				return vo.getTotal();
			}
			return 0;
		}

		@Override
		public List<AntimicrobialDrugAccessVo> queryOperationProportionFromDB(
				List<String> dept, String page, String rows,String menuAlias) {
			Map map=new HashMap();
			map.put("total", 0);
			map.put("rows", new ArrayList<WordLoadVO>());
			BasicDBObject bdObject = new BasicDBObject();
			BasicDBList mongoDeptList = new BasicDBList();
			List<AntimicrobialDrugAccessVo> list=new ArrayList<AntimicrobialDrugAccessVo>();
			if(dept.size()>0){
				for(String d:dept){
					mongoDeptList.add(new BasicDBObject("deptCode",d));
				}
				bdObject.put("$or", mongoDeptList);
			}else{
				String jobNo=ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
				List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,jobNo);
				for(int i = 0,len=deptList.size();i<len;i++){
					mongoDeptList.add(new BasicDBObject("deptCode",deptList.get(i).getDeptName()));
				}
				bdObject.put("$or", mongoDeptList);
			}
			DBCursor cursor = new MongoBasicDao().findAllDataSortBy("KJYWSYQX", "_id", bdObject,Integer.parseInt(rows),Integer.parseInt(page));
			DBObject dbCursor;
			while(cursor.hasNext()){
				AntimicrobialDrugAccessVo voOne=new  AntimicrobialDrugAccessVo();
				 dbCursor = cursor.next();
					 String ename =(String)dbCursor.get("ename") ;//出院人数
					 String ecode=(String)dbCursor.get("ecode") ;//科室名称
					 String elevel=(String)dbCursor.get("elevel") ;//科室编码
					 String eaccess=(String)dbCursor.get("eaccess") ;//手术占比
					 voOne.setEname(ename);
					 voOne.setEcode(ecode);
					 voOne.setElevel(elevel);
					 voOne.setEaccess(eaccess);
					 list.add(voOne);
			  }
			return list;
		}

		
}
