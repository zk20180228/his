package cn.honry.oa.patInformation.dao.impl;

import java.math.BigDecimal;
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
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.Information;
import cn.honry.base.bean.model.InformationSubscripe;
import cn.honry.base.bean.model.OaPortalWidget;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.SysRole;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.utli.DataRightUtils;
import cn.honry.oa.information.dao.InformationDao;
import cn.honry.oa.patInformation.dao.PatInformationDao;
import cn.honry.oa.patInformation.vo.MenuCkeckedVO;
import cn.honry.oa.patInformation.vo.MenuVO;
import cn.honry.oa.patInformation.vo.SubscripeVO;
import cn.honry.utils.ShiroSessionUtils;

@Repository("patInformationDao")
@SuppressWarnings({"all"})
public class PatInformationDaoImpl extends HibernateEntityDao<Information> implements PatInformationDao {
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Override
	public MenuVO findMenuByid(final String id) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" Select t.ID id,t.MENU_NAME name,t.MENU_CODE code,t.MENU_PUBLISHDIRT publishDirt,t.MENU_DEPT dept,t.MENU_COMMENT isComment From T_OA_MENU t ");
		sb.append(" Where t.Menu_Code = :menuid And t.stop_flag = 0 And t.del_flag = 0 ");
		List<MenuVO> list = this.getHibernateTemplate().execute(new HibernateCallback<List<MenuVO>>() {

			@Override
			public List<MenuVO> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.setParameter("menuid", id);
				query.addScalar("name").addScalar("code").addScalar("publishDirt",Hibernate.INTEGER)
					.addScalar("dept").addScalar("isComment").addScalar("id");
				return query.setResultTransformer(Transformers.aliasToBean(MenuVO.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	@Override
	public List<MenuCkeckedVO> queryMenuCheckByid(final String menuid,final String type) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" select distinct t.RIGHT_RIGHTTYPE rightType, t.RIGHT_TYPE type,t.RIGHT_CODE code,t.MENU_ID menuid from t_oa_menuright t ");
		sb.append(" Where t.menu_id = :menuid And t.RIGHT_RIGHTTYPE = :type");
		List<MenuCkeckedVO> list = this.getHibernateTemplate().execute(new HibernateCallback<List<MenuCkeckedVO>>() {

			@Override
			public List<MenuCkeckedVO> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.setParameter("menuid", menuid).setParameter("type", type);
				query.addScalar("rightType").addScalar("type").addScalar("code").addScalar("menuid");
				return query.setResultTransformer(Transformers.aliasToBean(MenuCkeckedVO.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<MenuCkeckedVO>();
	}
	@Override
	public List<SysEmployee> queryAuthEmp(final String menuId, final String post,
			final String title,String page,String rows) {
		final Integer p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
		final Integer r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):20;
		final String sb = this.getEmpSQL(menuId, post, title);
		List<SysEmployee> list = this.getHibernateTemplate().execute(new HibernateCallback<List<SysEmployee>>() {

			@Override
			public List<SysEmployee> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				if (StringUtils.isNotBlank(title)) {
					query.setParameter("title", title);
				}
				if (StringUtils.isNotBlank(post)) {
					query.setParameter("post", post);
				}
				query.setParameter("menuId", menuId);
				query.addScalar("jobNo").addScalar("name").addScalar("post").addScalar("title").addScalar("deptName").addScalar("deptCode");
				query.setFirstResult((p-1)*r).setMaxResults(p*r);
				return query.setResultTransformer(Transformers.aliasToBean(SysEmployee.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<SysEmployee>();
	}
	@Override
	public int queryAuthEmpTotal(final String menuId, final String post, final String title) {
		final String sql = "select count(1) from ( "+this.getEmpSQL(menuId, post, title)+" )";
		Integer total = this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				if (StringUtils.isNotBlank(title)) {
					query.setParameter("title", title);
				}
				if (StringUtils.isNotBlank(post)) {
					query.setParameter("post", post);
				}
				query.setParameter("menuId", menuId);
				BigDecimal big = (BigDecimal) query.uniqueResult();
				return big.intValue();
			}
		});
		return total;
	}
	private String getEmpSQL(String menuId, String post, String title){
		StringBuffer sb = new StringBuffer();
		sb.append(" Select e.EMPLOYEE_JOBNO jobNo,e.EMPLOYEE_NAME name,e.EMPLOYEE_POST post,e.EMPLOYEE_TITLE title,d.dept_code deptCode,d.dept_name deptName From T_OA_MENURIGHT t,T_EMPLOYEE e,t_department d ");
		sb.append(" Where t.right_code = e.EMPLOYEE_JOBNO And e.dept_id = d.dept_id And t.RIGHT_RIGHTTYPE = '3' And t.RIGHT_TYPE = '1' And e.STOP_FLG = 0 And e.DEL_FLG = 0 ");
		sb.append(" and t.MENU_ID = :menuId ");
		if(StringUtils.isNotBlank(title)){
			sb.append(" and e.EMPLOYEE_TITLE = :title ");
		}
		if(StringUtils.isNotBlank(post)){
			sb.append(" and e.EMPLOYEE_POST = :post ");
			
		}
		return sb.toString();
	}
	@Override
	public List<MenuCkeckedVO> queryRoleAuth(final String menuId) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" select t.RIGHT_CODE code,r.role_name name from t_oa_menuright t ,t_sys_role r ");
		sb.append(" Where t.right_code = r.role_id And t.right_type = '2' And t.right_righttype = '3' And t.menu_id = :menuId ");
		List<MenuCkeckedVO> list = this.getHibernateTemplate().execute(new HibernateCallback<List<MenuCkeckedVO>>() {

			@Override
			public List<MenuCkeckedVO> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.setParameter("menuId", menuId);
				query.addScalar("code").addScalar("name");
				return query.setResultTransformer(Transformers.aliasToBean(MenuCkeckedVO.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<MenuCkeckedVO>();
	}
	@Override
	public List<MenuCkeckedVO> queryDeptAuth(final String menuId) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" Select m.RIGHT_CODE code, d.dept_name name  From t_oa_menuright m,T_DEPARTMENT d Where m.right_code = d.dept_code And m.RIGHT_RIGHTTYPE = '3' And m.right_code = '3' And m.menu_id = :menuId ");
		List<MenuCkeckedVO> list = this.getHibernateTemplate().execute(new HibernateCallback<List<MenuCkeckedVO>>() {

			@Override
			public List<MenuCkeckedVO> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.setParameter("menuId", menuId);
				query.addScalar("code").addScalar("name");
				return query.setResultTransformer(Transformers.aliasToBean(MenuCkeckedVO.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<MenuCkeckedVO>();
	}
	@Override
	public List<MenuCkeckedVO> queryDutyAuth(final String menuId) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" Select m.right_code code,d.code_name name From T_OA_MENURIGHT m,T_BUSINESS_DICTIONARY d ");
		sb.append(" Where m.right_code = d.CODE_TYPE And m.right_righttype = '3' And m.right_type = '4'"
				+ " And d.code_type = 'duties' And d.stop_flg = 0 And d.del_flg = 0 And m.menu_id = :menuId ");
		List<MenuCkeckedVO> list = this.getHibernateTemplate().execute(new HibernateCallback<List<MenuCkeckedVO>>() {

			@Override
			public List<MenuCkeckedVO> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.setParameter("menuId", menuId);
				query.addScalar("code").addScalar("name");
				return query.setResultTransformer(Transformers.aliasToBean(MenuCkeckedVO.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<MenuCkeckedVO>();
	}
	@Override
	public List<MenuVO> findMenuVo(final String deptCode, final String dutyCode,
			final String roleCode, final String acount) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" Select m.menu_code code,m.menu_name name,m.menu_parentcode parentCode,m.menu_path path,m.menu_parentpath parentPath ");
		sb.append(" From t_Oa_Menuright t, t_Oa_Menu m ");
		sb.append(" Where t.Menu_Id = m.Id And t.Right_Righttype = '1' ");
		sb.append(" And ((t.Right_Type = '0') ");
		if(StringUtils.isNotBlank(acount)){
			sb.append("Or (t.Right_Type = '1' And t.Right_Code = :acount) ");
		}
		if(StringUtils.isNotBlank(roleCode)){
			sb.append("Or (t.Right_Type = '2' And t.Right_Code = :roleCode) ");
		}
		if(StringUtils.isNotBlank(deptCode)){
			sb.append( "Or (t.Right_Type = '3' And t.Right_Code = :deptCode) ");
		}
		if(StringUtils.isNotBlank(dutyCode)){
			sb.append("Or (t.Right_Type = '4' And t.Right_Code = :dutyCode)");
		}
		sb.append(") ");
		List<MenuVO> list = this.getHibernateTemplate().execute(new HibernateCallback<List<MenuVO>>() {

			@Override
			public List<MenuVO> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				if(StringUtils.isNotBlank(acount)){
					query.setParameter("acount", acount);
				}
				if(StringUtils.isNotBlank(roleCode)){
					query.setParameter("roleCode", roleCode);
				}
				if(StringUtils.isNotBlank(deptCode)){
					query.setParameter("deptCode", deptCode);
				}
				if(StringUtils.isNotBlank(dutyCode)){
					query.setParameter("dutyCode", dutyCode);
				}
				query.addScalar("code").addScalar("name").addScalar("parentCode")
					.addScalar("path").addScalar("parentPath");
				return query.setResultTransformer(Transformers.aliasToBean(MenuVO.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<MenuVO>();
	}
	@Override
	public List<MenuCkeckedVO> checkAuth(final String menuid, final String deptCode,
			final String dutyCode, final String roleCode, final String acount) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" Select t.right_righttype rightType,t.right_type type,t.right_code code,t.menu_id menuid "
				+ "from t_oa_menuright t "
				+ "Where t.menu_id = :menuid and t.Right_Righttype = '1' ");
		sb.append(" And ((t.Right_Type = '0') ");
		if(StringUtils.isNotBlank(acount)){
			sb.append("Or (t.Right_Type = '1' And t.Right_Code = :acount) ");
		}
		if(StringUtils.isNotBlank(roleCode)){
			sb.append("Or (t.Right_Type = '2' And t.Right_Code = :roleCode) ");
		}
		if(StringUtils.isNotBlank(deptCode)){
			sb.append( "Or (t.Right_Type = '3' And t.Right_Code = :deptCode) ");
		}
		if(StringUtils.isNotBlank(dutyCode)){
			sb.append("Or (t.Right_Type = '4' And t.Right_Code = :dutyCode)");
		}
		sb.append(") ");
		List<MenuCkeckedVO> list = this.getHibernateTemplate().execute(new HibernateCallback<List<MenuCkeckedVO>>() {

			@Override
			public List<MenuCkeckedVO> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.setParameter("menuid", menuid);
				if(StringUtils.isNotBlank(acount)){
					query.setParameter("acount", acount);
				}
				if(StringUtils.isNotBlank(roleCode)){
					query.setParameter("roleCode", roleCode);
				}
				if(StringUtils.isNotBlank(deptCode)){
					query.setParameter("deptCode", deptCode);
				}
				if(StringUtils.isNotBlank(dutyCode)){
					query.setParameter("dutyCode", dutyCode);
				}
				query.addScalar("rightType").addScalar("type").addScalar("code").addScalar("menuid");
				return query.setResultTransformer(Transformers.aliasToBean(MenuCkeckedVO.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	@Override
	public List<Information> findinformationList(final String name, final String menuid,
			String page, String rows,final String type) {
		final Integer p = StringUtils.isNoneBlank(page)?Integer.valueOf(page):1;
		final Integer r = StringUtils.isNoneBlank(rows)?Integer.valueOf(rows):20;
		final StringBuffer sb = new StringBuffer();
		sb.append(" from Information where stop_flg=0 and del_flg=0 ");
		if(StringUtils.isNoneBlank(menuid)){
			sb.append(" and infoMenuid =:menuid ");
		}
		if(StringUtils.isNoneBlank(name)){
			sb.append(" and infoTitle like :name ");
		}
		if(StringUtils.isNoneBlank(type)){
			sb.append(" and infoPubflag = :type and infoWirteuser =:acount ");
		}
		sb.append(" order by createTime Desc ");
		List<Information> list = this.getHibernateTemplate().execute(new HibernateCallback<List<Information>>() {

			@Override
			public List<Information> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(sb.toString());
				if(StringUtils.isNoneBlank(menuid)){
					sb.append(" and infoMenuid =:menuid ");
					query.setParameter("menuid", menuid);
				}
				if(StringUtils.isNoneBlank(name)){
					query.setParameter("name", "%"+name+"%");
				}
				if(StringUtils.isNoneBlank(type)){
					query.setParameter("type", Integer.valueOf(type));
					query.setParameter("acount", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				}
				return query.setMaxResults(r).setFirstResult((p-1)*r).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<Information>();
	}
	@Override
	public List<Information> queryInformationList(String name, final String menuid,
			String page, String rows,String checkflag,String pubflag){
		final Integer p = StringUtils.isNoneBlank(page)?Integer.valueOf(page):1;
		final Integer r = StringUtils.isNoneBlank(rows)?Integer.valueOf(rows):20;
		final StringBuffer sb = new StringBuffer();
		sb.append(" Select Distinct I.INFO_ID id,i.INFO_MENUID infoMenuid,I.INFO_TITLE infoTitle,I.INFO_KEYWORD infoKeyword,I.INFO_BREV infoBrev,I.INFO_EDITOR infoEditor,"
				+ " I.INFO_PUBFLAG infoPubflag,I.INFO_PUBTIME infoPubtime,I.INFO_PUBUSER infoPubuser,I.INFO_WIRTEUSER infoWirteuser,I.INFO_CHECKER infoChecker,I.INFO_CHECK_FLAG infoCheckFlag,"
				+ " i.INFO_PUBLISHUSERNAME pubuserName,i.INFO_WRITERUSERNAME writerName,i.INFO_EDITORNAME editorName, i.Createtime ");
		sb.append(" From t_Oa_Information i Left Join t_Oa_Menuright m On i.Info_Menuid = m.Menu_Id ");
		sb.append(" where i.Stop_Flg = 0 And i.Del_Flg = 0 and i.INFO_MENUID = :menuid ");
		sb.append(" And (i.Info_Editor = :acount Or (i.Info_Check_Flag = 0 And (m.Right_Righttype = 2 And (m.Right_Code In (:param)))) or (m.Right_Righttype = 3 And (m.Right_Code In (:param))) ) ");
		if(StringUtils.isNotBlank(name)){
			sb.append(" and( i.INFO_TITLE like '%"+name+"%' or i.INFO_KEYWORD like '%"+name+"%' or i.INFO_EDITORNAME like '%"+name+"%' "
					+ " or i.INFO_PUBLISHUSERNAME like '%"+name+"%' or i.INFO_WRITERUSERNAME like '%"+name+"%') ");
		}
		if(StringUtils.isNotBlank(pubflag)){
			sb.append(" and i.INFO_PUBFLAG = "+pubflag);
		}
		if (StringUtils.isNotBlank(checkflag)) {
			sb.append(" and i.INFO_CHECK_FLAG = "+checkflag);
		}
		sb.append(" order by i.CREATETIME desc  ");
		List<Information> list = this.getHibernateTemplate().execute(new HibernateCallback<List<Information>>() {

			@Override
			public List<Information> doInHibernate(Session session)
					throws HibernateException, SQLException {
				List<String> param = new ArrayList<String>();
				param.add("all");
				String deptCode = "";
				String dutyCode = "";
				String titleCode = "";
				String acount = "";
				SysEmployee employee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
				User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
				SysRole role = ShiroSessionUtils.getCurrentUserLoginRoleFromShiroSession();
				SysDepartment department = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();
				if(department!=null){
					deptCode = department.getDeptCode();
					param.add(deptCode);
				}
				if(employee!=null){
					dutyCode = employee.getPost();
					titleCode = employee.getTitle();
					param.add(dutyCode);
					param.add(titleCode);
				}
				if(user!=null){
					acount = user.getAccount();
					param.add(acount);
				}
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.setParameter("menuid", menuid);
				query.setParameter("acount", acount);
				query.setParameterList("param", param);
				query.addScalar("id").addScalar("infoMenuid").addScalar("infoTitle").addScalar("infoKeyword").addScalar("infoBrev")
					.addScalar("infoEditor").addScalar("infoPubflag",Hibernate.INTEGER).addScalar("infoPubtime",Hibernate.TIMESTAMP)
					.addScalar("infoPubuser").addScalar("infoWirteuser").addScalar("infoChecker").addScalar("infoCheckFlag",Hibernate.INTEGER)
					.addScalar("pubuserName").addScalar("writerName").addScalar("editorName");
				return query.setResultTransformer(Transformers.aliasToBean(Information.class)).setFirstResult((p-1)*r).setMaxResults(p*r).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<Information>();
	}
	
	@Override
	public int queryInformationTotal(String name, final String menuid,String checkflag,String pubflag) {
		final StringBuffer sb = new StringBuffer();
		sb.append("select count(1) from(Select Distinct I.INFO_ID id,i.INFO_MENUID infoMenuid,I.INFO_TITLE infoTitle,I.INFO_KEYWORD infoKeyword,I.INFO_BREV infoBrev,I.INFO_EDITOR infoEditor,"
				+ "I.INFO_PUBFLAG infoPubflag,I.INFO_PUBTIME infoPubtime,I.INFO_PUBUSER infoPubuser,I.INFO_WIRTEUSER infoWirteuser,I.INFO_CHECKER infoChecker,I.INFO_CHECK_FLAG infoCheckFlag ");
		sb.append(" From t_Oa_Information i Left Join t_Oa_Menuright m On i.Info_Menuid = m.Menu_Id ");
		sb.append(" where i.Stop_Flg = 0 And i.Del_Flg = 0 and i.INFO_MENUID = :menuid ");
		sb.append(" And (i.Info_Editor = :acount Or (i.Info_Check_Flag = 0 And (m.Right_Righttype = 2 And (m.Right_Code In (:param)))) or (m.Right_Righttype = 3 And (m.Right_Code In (:param)))) ");
		if(StringUtils.isNotBlank(name)){
			sb.append(" and( i.INFO_TITLE like '%"+name+"%' or i.INFO_KEYWORD like '%"+name+"%' or i.INFO_EDITORNAME like '%"+name+"%' "
					+ " or i.INFO_PUBLISHUSERNAME like '%"+name+"%' or i.INFO_WRITERUSERNAME like '%"+name+"%') ");
		}
		if(StringUtils.isNotBlank(pubflag)){
			sb.append(" and i.INFO_PUBFLAG = "+pubflag);
		}
		if (StringUtils.isNotBlank(checkflag)) {
			sb.append(" and i.INFO_CHECK_FLAG = "+checkflag);
		}
		sb.append(" ) ");
		int total = this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				List<String> param = new ArrayList<String>();
				param.add("all");
				String deptCode = "";
				String dutyCode = "";
				String titleCode = "";
				String acount = "";
				SysEmployee employee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
				User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
				SysRole role = ShiroSessionUtils.getCurrentUserLoginRoleFromShiroSession();
				SysDepartment department = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();
				if(department!=null){
					deptCode = department.getDeptCode();
					param.add(deptCode);
				}
				if(employee!=null){
					dutyCode = employee.getPost();
					titleCode = employee.getTitle();
					param.add(titleCode);
					param.add(dutyCode);
				}
				if(user!=null){
					acount = user.getAccount();
					param.add(acount);
				}
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.setParameter("menuid", menuid);
				query.setParameter("acount", acount);
				query.setParameterList("param", param);
				BigDecimal uniqueResult = (BigDecimal)query.uniqueResult();
				return uniqueResult.intValue();
			}
		});
		return total;
	}
	@Override
	public int findinformationTotal(final String name, final String menuid, final String type) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" from Information where stop_flg=0 and del_flg=0 ");
		if(StringUtils.isNoneBlank(menuid)){
			sb.append(" and infoMenuid ='"+menuid+"'");
		}
		if(StringUtils.isNoneBlank(name)){
			sb.append(" and infoTitle like '%"+name+"' ");
		}
		if(StringUtils.isNoneBlank(type)){
			sb.append(" and infoPubflag = '"+type+"' and infoWirteuser ='"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"'");
		}
		int total = super.getTotal(sb.toString());
		return total;
	}
	@Override
	public List<SubscripeVO> findSub(String type, final String code) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" Select distinct t.Employee_Jobno code, t.Employee_Name name from t_Employee t,t_department d ");
		sb.append(" where t.DEPT_ID = d.DEPT_ID and t.del_flg = 0 And t.stop_flg = 0 and d.del_flg = 0 And d.stop_flg = 0 ");
		if(StringUtils.isBlank(type)){
			return new ArrayList<SubscripeVO>();
		}else if("2".equals(type)){
			sb.append(" and t.employee_title = :code ");
		}else if("3".equals(type)){
			sb.append(" and d.DEPT_CODE = :code ");
		}else if("4".equals(type)){
			sb.append(" and t.EMPLOYEE_POST = :code ");
		}else{
			return new ArrayList<SubscripeVO>();
		}
		List<SubscripeVO> list = this.getHibernateTemplate().execute(new HibernateCallback<List<SubscripeVO>>() {

			@Override
			public List<SubscripeVO> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.setParameter("code", code);
				query.addScalar("code").addScalar("name");
				
				return query.setResultTransformer(Transformers.aliasToBean(SubscripeVO.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<SubscripeVO>();
	}
	@Override
	public void updateSubscripe(final String menuId,final String acount) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" Update t_oa_information_subscripe t Set t.ISREAD = 1 Where t.SUBSCRIPEPERSION=:acount And t.INFORMATIONID=:menuId ");
		int num = this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.setParameter("menuId", menuId).setParameter("acount", acount);
				return query.executeUpdate();
			}
		});
	}
	@Override
	public List<Information> getInfomationView(final String menuId, String page,
			String rows) {
		final Integer p = StringUtils.isNoneBlank(page)?Integer.valueOf(page):1;
		final Integer r = StringUtils.isNoneBlank(rows)?Integer.valueOf(rows):20;
		final StringBuffer sb = new StringBuffer();
		sb.append(" select  * from( select t.*,rownum as n from ( ");
		sb.append(" Select Distinct I.INFO_ID id,i.INFO_MENUID infoMenuid,'['||m.menu_Name||']'||I.INFO_TITLE infoTitle,I.INFO_KEYWORD infoKeyword,I.INFO_BREV infoBrev,I.INFO_EDITOR infoEditor,"
				+ "I.INFO_PUBFLAG infoPubflag,I.INFO_PUBTIME infoPubtime,I.INFO_PUBUSER infoPubuser,I.INFO_WIRTEUSER infoWirteuser,I.INFO_CHECKER infoChecker,I.INFO_CHECK_FLAG infoCheckFlag,s.ISREAD isRead,i.Createtime ");
		sb.append(" From t_Oa_Information i Left Join t_oa_information_subscripe s On i.Info_Id = s.informationid "
				+ " Left Join t_oa_menu m On m.menu_code = i.info_menuid ");
		sb.append(" Where i.Stop_Flg = 0 And i.Del_Flg = 0 ");
		if(StringUtils.isNotBlank(menuId)){
			sb.append(" And i.Info_Menuid = :menuId ");
		}
		sb.append(" And (s.SUBSCRIPEPERSION = :acount Or s.Type = '0') Order By i.Createtime Desc ");
		sb.append("  ) t where rownum <= :page * :row) t1 where t1.n > (:page -1) * :row  ");
		List<Information> list = this.getHibernateTemplate().execute(new HibernateCallback<List<Information>>() {

			@Override
			public List<Information> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String acount = "";
				User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
				if(user!=null){
					acount = user.getAccount();
				}
				SQLQuery query = session.createSQLQuery(sb.toString());
				if(StringUtils.isNotBlank(menuId)){
					query.setParameter("menuId", menuId);
				}
				query.setParameter("acount", acount);
				query.setParameter("page", p);
				query.setParameter("row", r);
				query.addScalar("id").addScalar("infoMenuid").addScalar("infoTitle").addScalar("infoKeyword").addScalar("infoBrev")
				.addScalar("infoEditor").addScalar("infoPubflag",Hibernate.INTEGER).addScalar("infoPubtime",Hibernate.TIMESTAMP)
				.addScalar("infoPubuser").addScalar("infoWirteuser").addScalar("infoChecker").addScalar("infoCheckFlag",Hibernate.INTEGER)
				.addScalar("isRead", Hibernate.INTEGER);
				return query.setResultTransformer(Transformers.aliasToBean(Information.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<Information>();
	}
	@Override
	public int getInfomationViewTotal(final String menuId) {
		final StringBuffer sb = new StringBuffer();
		sb.append("select count(1) from(Select Distinct I.INFO_ID id,i.INFO_MENUID infoMenuid,I.INFO_TITLE infoTitle,I.INFO_KEYWORD infoKeyword,I.INFO_BREV infoBrev,I.INFO_EDITOR infoEditor,"
				+ "I.INFO_PUBFLAG infoPubflag,I.INFO_PUBTIME infoPubtime,I.INFO_PUBUSER infoPubuser,I.INFO_WIRTEUSER infoWirteuser,I.INFO_CHECKER infoChecker,I.INFO_CHECK_FLAG infoCheckFlag,s.ISREAD isRead,i.Createtime ");
		sb.append(" From t_Oa_Information i Left Join t_oa_information_subscripe s On i.Info_Id = s.informationid ");
		sb.append(" Where i.Stop_Flg = 0 And i.Del_Flg = 0 And (s.SUBSCRIPEPERSION = :acount Or s.Type = '0') ) ");
		if(StringUtils.isNotBlank(menuId)){
			sb.append(" And i.Info_Menuid = :menuId ");
		}
		int total = this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				String acount = "";
				User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
				if(user!=null){
					acount = user.getAccount();
				}
				SQLQuery query = session.createSQLQuery(sb.toString());
				if(StringUtils.isNotBlank(menuId)){
					query.setParameter("menuId", menuId);
				}
				query.setParameter("acount", acount);
				BigDecimal uniqueResult = (BigDecimal)query.uniqueResult();
				return uniqueResult.intValue();
			}
		});
		return total;
	}
	@Override
	public MenuVO getparentCodeAndName(final String menuCode) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" select T.MENU_CODE code,T.MENU_NAME name,"
				+" (select m.menu_name from t_oa_menu m where m.menu_code = "
				+ "substr(t.menu_path,instr(t.menu_path, ',', 1, 1)+1,"
				+ "decode(instr(t.menu_path, ',', 1, 2),0, length(t.menu_path) - 2,(instr(t.menu_path, ',', 1, 2))-(instr(t.menu_path, ',', 1, 1)+1)))) parentName, ");
		sb.append(" substr(t.menu_path,instr(t.menu_path, ',', 1, 1)+1,decode(instr(t.menu_path, ',', 1, 2),0, length(t.menu_path)-2,(instr(t.menu_path, ',', 1, 2))-(instr(t.menu_path, ',', 1, 1)+1))) parentCode ");
		sb.append(" from t_oa_menu t where t.menu_code = :menuCode and t.stop_flag = 0 and t.del_flag = 0 ");
		List<MenuVO> list = this.getHibernateTemplate().execute(new HibernateCallback<List<MenuVO>>() {

			@Override
			public List<MenuVO> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.setParameter("menuCode", menuCode);
				query.addScalar("code").addScalar("name").addScalar("parentCode").addScalar("parentName");
				return query.setResultTransformer(Transformers.aliasToBean(MenuVO.class)).list();
			}
		});
		if (list!=null&&list.size()>0) {
			return list.get(0);
		}
		return new MenuVO();
	}
	@Override
	public List<MenuCkeckedVO> judgeAuthBymenuCode(final String menuid, final String type) {
		final StringBuffer sb = new StringBuffer();
		if(StringUtils.isNoneBlank(type)&&!"3".equals(type)){//不是查看权限验证
			sb.append(" select distinct t.RIGHT_RIGHTTYPE rightType, t.RIGHT_TYPE type,t.RIGHT_CODE code,t.MENU_ID menuid from t_oa_menuright t ");
			sb.append(" Where t.menu_id = :menuid And t.RIGHT_RIGHTTYPE = :type ");
			sb.append(" and t.Right_Code In (:param) ");
		}else{//查看权限验证
			sb.append(" select distinct t.RIGHT_RIGHTTYPE rightType, t.RIGHT_TYPE type,t.RIGHT_CODE code,t.MENU_ID menuid from t_oa_information i, t_oa_menuright t ");
			sb.append(" where t.menu_id = i.info_menuid  ");
			sb.append(" and ( "
					+ " (t.menu_id = :menuid And t.RIGHT_RIGHTTYPE = :type and  t.Right_Code In (:param)) "
					+ " or (i.INFO_PUBUSER = :acount and i.info_menuid = :menuid and i.stop_flg = 0 and i.del_flg = 0)"
					+ " ) ");
		}
		List<MenuCkeckedVO> list = this.getHibernateTemplate().execute(new HibernateCallback<List<MenuCkeckedVO>>() {

			@Override
			public List<MenuCkeckedVO> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				List<String> param = new ArrayList<String>();
				param.add("all");
				String deptCode = "";
				String dutyCode = "";
				String titleCode = "";
				String acount = "";
				SysEmployee employee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
				User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
				SysRole role = ShiroSessionUtils.getCurrentUserLoginRoleFromShiroSession();
				SysDepartment department = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();
				if(department!=null){
					deptCode = department.getDeptCode();
					param.add(deptCode);
				}
				if(employee!=null){
					dutyCode = employee.getPost();
					titleCode = employee.getTitle();
					param.add(titleCode);
					param.add(dutyCode);
				}
				if(user!=null){
					acount = user.getAccount();
					param.add(acount);
				}
				query.setParameter("menuid", menuid).setParameter("type", type).setParameterList("param", param);
				if(StringUtils.isNoneBlank(type)&&"3".equals(type)){
					query.setParameter("acount", acount);
				}
				query.addScalar("rightType").addScalar("type").addScalar("code").addScalar("menuid");
				return query.setResultTransformer(Transformers.aliasToBean(MenuCkeckedVO.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<MenuCkeckedVO>();
	}
	@Override
	public void deleteCkeck(final String infoid) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" delete from t_oa_information_check t where t.INFORMATIONID = :infoid ");
		int num = this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.setParameter("infoid", infoid);
				return query.executeUpdate();
			}
		});
	}
	@Override
	public List<Information> getInformationCheck(final String menuId, String page,
			String rows) {
		Integer p = StringUtils.isNoneBlank(page)?Integer.valueOf(page):1;
		Integer r = StringUtils.isNoneBlank(rows)?Integer.valueOf(rows):20;
		StringBuffer sb = new StringBuffer();
		
		SysDepartment department = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();
		SysEmployee employee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();//当前登录人
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();//当前登录人
		//查询当前登录人的且未读的,或者是所有人可读的信息//未审核且已发布的才可以审核
		sb.append(" Select Distinct t.INFO_ID id,t.INFO_MENUID infoMenuid,'['||m.menu_Name||']'||t.INFO_TITLE infoTitle,t.INFO_KEYWORD infoKeyword,t.INFO_BREV infoBrev,t.INFO_EDITOR infoEditor,"
			+ "t.INFO_PUBFLAG infoPubflag,t.INFO_PUBTIME infoPubtime,t.INFO_PUBUSER infoPubuser,t.INFO_WIRTEUSER infoWirteuser,t.INFO_CHECKER infoChecker,t.INFO_CHECK_FLAG infoCheckFlag,t.Createtime "
			+ "from T_OA_INFORMATION t, T_OA_MENURIGHT menu, T_OA_MENU m  "
			+ "where t.INFO_CHECK_FLAG=0 and t.INFO_PUBFLAG=1 "
			+ "and m.menu_code = t.info_menuid "
			+ "and t.INFO_MENUID=menu.MENU_ID and menu.RIGHT_RIGHTTYPE='2' "
			+ "and t.del_flg=0 and t.stop_flg=0 "
			+ "and (menu.RIGHT_TYPE='0' or (menu.RIGHT_TYPE='1' and menu.RIGHT_CODE='"+user.getAccount()+"') ");
			if(employee!=null && StringUtils.isNotBlank(employee.getTitle())){
				sb.append("or (menu.RIGHT_TYPE='2' and menu.RIGHT_CODE='"+employee.getTitle()+"') ");
			}
			if(department!=null && StringUtils.isNotBlank(department.getDeptCode())){
				sb.append("or (menu.RIGHT_TYPE='3' and menu.RIGHT_CODE='"+department.getDeptCode()+"') ");
			}
			if(employee!=null && StringUtils.isNotBlank(employee.getPost())){
				sb.append("or (menu.RIGHT_TYPE='4' and menu.RIGHT_CODE='"+employee.getPost()+"')");
			}
			sb.append(") order by t.INFO_PUBTIME desc");
		SQLQuery query = this.getSession().createSQLQuery(sb.toString()).addScalar("id").addScalar("infoMenuid").addScalar("infoTitle").addScalar("infoKeyword").addScalar("infoBrev")
				.addScalar("infoEditor").addScalar("infoPubflag",Hibernate.INTEGER).addScalar("infoPubtime",Hibernate.TIMESTAMP)
				.addScalar("infoPubuser").addScalar("infoWirteuser").addScalar("infoChecker").addScalar("infoCheckFlag",Hibernate.INTEGER);
		List<Information> information = query.setResultTransformer(Transformers.aliasToBean(Information.class)).setFirstResult((p-1)*r).setMaxResults(p*r).list();
		if(information!=null && information.size()>0){
			return information;
		}
		return new ArrayList<Information>();
	}
	@Override
	public int getInformationCheckTotal(String menuId) {
		final StringBuffer sb = new StringBuffer();
		SysDepartment department = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();
		SysEmployee employee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();//当前登录人
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();//当前登录人
		//查询当前登录人的且未读的,或者是所有人可读的信息//未审核且已发布的才可以审核
		sb.append(" select count(1) from(select t.INFO_ID as id, t.INFO_TITLE as infoTitle, t.INFO_PUBTIME as infoPubtime,t.INFO_MENUID as infoMenuid "
			+ "from T_OA_INFORMATION t, T_OA_MENURIGHT menu "
			+ "where t.INFO_CHECK_FLAG=0 and t.INFO_PUBFLAG=1 "
			+ "and t.INFO_MENUID=menu.MENU_ID and menu.RIGHT_RIGHTTYPE='2' "
			+ "and t.del_flg=0 and t.stop_flg=0 "
			+ "and (menu.RIGHT_TYPE='0' or (menu.RIGHT_TYPE='1' and menu.RIGHT_CODE='"+user.getAccount()+"') ");
			if(employee!=null && StringUtils.isNotBlank(employee.getTitle())){
				sb.append("or (menu.RIGHT_TYPE='2' and menu.RIGHT_CODE='"+employee.getTitle()+"') ");
			}
			if(department!=null && StringUtils.isNotBlank(department.getDeptCode())){
				sb.append("or (menu.RIGHT_TYPE='3' and menu.RIGHT_CODE='"+department.getDeptCode()+"') ");
			}
			if(employee!=null && StringUtils.isNotBlank(employee.getPost())){
				sb.append("or (menu.RIGHT_TYPE='4' and menu.RIGHT_CODE='"+employee.getPost()+"')");
			}
			sb.append("))");
		int total = this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				BigDecimal uniqueResult = (BigDecimal)query.uniqueResult();
				return uniqueResult.intValue();
			}
		});
		return total;
	}
	@Override
	public InformationSubscripe querySubscripe(String infoMenuid, String account) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from InformationSubscripe t where t.informationId='"+infoMenuid+"' and t.subscripePerson='"+account+"'");
		List<InformationSubscripe> list=find(hql.toString(), null);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	@Override
	public void updViews(String infoid) {
		StringBuffer sb = new StringBuffer();
		sb.append("Update T_OA_INFORMATION Set views = (Case When views = Null Then 1 Else views+1 End ) Where INFO_ID = '"+infoid+"'");
		org.hibernate.classic.Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sb.toString());
		query.executeUpdate();
	}
	@Override
	public List<String> getPatientAppUser() {
		StringBuffer sb = new StringBuffer();
		sb.append(" select t.mobile mobile from m_patient_user t Where t.status = 1 ");
		List<String> list = this.getSessionFactory().getCurrentSession().createSQLQuery(sb.toString()).addScalar("mobile", Hibernate.STRING).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	
}
