package cn.honry.oa.menumanager.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaMenu;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.menumanager.dao.MenuManagerDao;
import cn.honry.oa.menumanager.vo.MenuVo;

@Repository("menuManagerDao")
public class MenuManagerDaoImpl extends HibernateEntityDao<OaMenu> implements MenuManagerDao {
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<MenuVo> showTree(String pid) throws Exception {
		return null;
	}

	@Override
	public List<MenuVo> findAllChild(String code) throws Exception{
		
		StringBuilder sb = new StringBuilder();
		sb.append("select t.menu_name name, t.menu_code code, t.menu_path path from t_oa_menu t where t.del_flag = '0' and t.type='0' ");
		sb.append(" and t.menu_parentpath = ").append("'"+code+"'");
		sb.append(" order by t.menu_order");
		
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sb.toString())
				.addScalar("name").addScalar("code").addScalar("path");
		List<MenuVo> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(MenuVo.class)).list();
		if(list.size() > 0 && list != null){
			return list;
		}
		return new ArrayList<MenuVo>();
	}

	@Override
	public List<MenuVo> queryMenuByParentcode(String code) {
		StringBuilder sb = new StringBuilder();
		sb.append("select t.menu_name name, t.menu_code code, t.menu_path path, t.id id, ");
		sb.append(" t.menu_dept dept, t.menu_comment mcomment, t.stop_flag stop_flag, ");
		sb.append(" t.menu_explain explain, t.menu_publishdirt publishdirt, t.menu_parentcode parentcode, ");
		sb.append(" t.menu_parentpath parentpath, t.menu_order morder, t.del_flag del_flag ");
		sb.append(" from t_oa_menu t where t.del_flag = '0' and t.type='0' ");
		sb.append(" and t.menu_parentcode = ").append("'"+code+"' order by t.menu_order ");
		
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sb.toString())
				.addScalar("name").addScalar("code").addScalar("id").addScalar("path").addScalar("dept")
				.addScalar("mcomment", Hibernate.INTEGER).addScalar("stop_flag", Hibernate.INTEGER)
				.addScalar("explain").addScalar("publishdirt", Hibernate.INTEGER).addScalar("parentcode").addScalar("parentpath")
				.addScalar("morder", Hibernate.INTEGER).addScalar("del_flag", Hibernate.INTEGER);
		List<MenuVo> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(MenuVo.class)).list();
		if(list.size() > 0 && list != null){
			return list;
		}
		return new ArrayList<MenuVo>();
	}

	@Override
	public Integer getMaxOrder() {
		String sql = "select max(t.MENU_ORDER) as morder from t_oa_menu t";
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sql).addScalar("morder", Hibernate.INTEGER);
		List<MenuVo> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(MenuVo.class)).list();
		if(list != null && list.size() >0){
			return list.get(0).getMorder();
		}else {
			return 0;
		}
	}

	@Override
	public List<OaMenu> queryAllChildren(String path) {

		StringBuilder sb = new StringBuilder();
		sb.append("select t.menu_name name, t.menu_code code, t.menu_path path, t.id id, ");
		sb.append(" t.menu_dept dept, t.menu_comment mcomment, t.stop_flag stop_flag, ");
		sb.append(" t.menu_explain explain, t.menu_publishdirt publishdirt, t.menu_parentcode parentcode, ");
		sb.append(" t.menu_parentpath parentpath, t.menu_order morder, t.del_flag del_flg, ");
		sb.append(" t.CREATEUSER createUser, t.CREATEDEPT createDept, t.CREATETIME createTime,t.UPDATEUSER updateUser,UPDATETIME updateTime ");
		sb.append(" from t_oa_menu t where t.del_flag = '0' and t.stop_flag = '0' ");
		sb.append(" and t.menu_parentpath like ").append("'%"+path+"%'");
		
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sb.toString())
				.addScalar("name").addScalar("code").addScalar("id").addScalar("path").addScalar("dept")
				.addScalar("mcomment", Hibernate.INTEGER).addScalar("stop_flag", Hibernate.INTEGER)
				.addScalar("explain").addScalar("publishdirt", Hibernate.INTEGER).addScalar("parentcode").addScalar("parentpath")
				.addScalar("morder", Hibernate.INTEGER).addScalar("del_flg", Hibernate.INTEGER)
				.addScalar("createUser").addScalar("createDept").addScalar("createTime",Hibernate.DATE).addScalar("updateUser").addScalar("updateTime",Hibernate.DATE);
		List<OaMenu> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(OaMenu.class)).list();
		if(list.size() > 0 && list != null){
			return list;
		}
		return new ArrayList<OaMenu>();
	
	}


	@Override
	public List<MenuVo> findAllChilds(String id) {
		StringBuilder sb = new StringBuilder();
		sb.append("select t.menu_name name, t.menu_code code, t.menu_path path, t.id id from t_oa_menu t where t.del_flag = '0' and t.stop_flag = '0' ");
		sb.append(" and t.menu_parentpath like ").append("'%"+id+"%'");
		sb.append(" order by t.menu_order");
		
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sb.toString())
				.addScalar("name").addScalar("code").addScalar("id").addScalar("path");
		List<MenuVo> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(MenuVo.class)).list();
		if(list.size() > 0 && list != null){
			return list;
		}
		return new ArrayList<MenuVo>();
	}

	@Override
	public List<MenuVo> findAllChildWithCode(String path) {
		StringBuilder sb = new StringBuilder();
		sb.append("select t.menu_name name, t.menu_code code, t.menu_path path from t_oa_menu t where t.del_flag = '0' and t.stop_flag = '0' ");
		sb.append(" and t.menu_parentpath like ").append("'%"+path+"%'");
		sb.append(" order by t.menu_order");
		
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sb.toString())
				.addScalar("name").addScalar("code").addScalar("path");
		List<MenuVo> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(MenuVo.class)).list();
		if(list.size() > 0 && list != null){
			return list;
		}
		return new ArrayList<MenuVo>();
	}

	@Override
	public List<MenuVo> queryMenuByCode(String code) {
		StringBuilder sb = new StringBuilder();
		sb.append("select t.menu_name name, t.menu_code code, t.menu_path path, t.id id, ");
		sb.append(" t.menu_dept dept, t.menu_comment mcomment, t.stop_flag stop_flag, ");
		sb.append(" t.menu_explain explain, t.menu_publishdirt publishdirt, t.menu_parentcode parentcode, ");
		sb.append(" t.menu_parentpath parentpath, t.menu_order morder, t.del_flag del_flag ");
		sb.append(" from t_oa_menu t where t.del_flag = '0' and t.type='0' ");
		sb.append(" and t.menu_code = ").append("'"+code+"'");
		
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sb.toString())
				.addScalar("name").addScalar("code").addScalar("id").addScalar("path").addScalar("dept")
				.addScalar("mcomment", Hibernate.INTEGER).addScalar("stop_flag", Hibernate.INTEGER)
				.addScalar("explain").addScalar("publishdirt", Hibernate.INTEGER).addScalar("parentcode").addScalar("parentpath")
				.addScalar("morder", Hibernate.INTEGER).addScalar("del_flag", Hibernate.INTEGER);
		List<MenuVo> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(MenuVo.class)).list();
		if(list.size() > 0 && list != null){
			return list;
		}
		return new ArrayList<MenuVo>();
	}

	@Override
	public List<MenuVo> queryMenuById(String id) {
		StringBuilder sb = new StringBuilder();
		sb.append("select t.menu_name name, t.menu_code code, t.menu_path path, t.id id, ");
		sb.append(" t.menu_dept dept, t.menu_comment mcomment, t.stop_flag stop_flag, ");
		sb.append(" t.menu_explain explain, t.menu_publishdirt publishdirt, t.menu_parentcode parentcode, ");
		sb.append(" t.menu_parentpath parentpath, t.menu_order morder, t.del_flag del_flag ");
		sb.append(" from t_oa_menu t where t.del_flag = '0' ");
		sb.append(" and t.id = ").append("'"+id+"'");
		
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sb.toString())
				.addScalar("name").addScalar("code").addScalar("id").addScalar("path").addScalar("dept")
				.addScalar("mcomment", Hibernate.INTEGER).addScalar("stop_flag", Hibernate.INTEGER)
				.addScalar("explain").addScalar("publishdirt", Hibernate.INTEGER).addScalar("parentcode").addScalar("parentpath")
				.addScalar("morder", Hibernate.INTEGER).addScalar("del_flag", Hibernate.INTEGER);
		List<MenuVo> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(MenuVo.class)).list();
		if(list.size() > 0 && list != null){
			return list;
		}
		return new ArrayList<MenuVo>();
	}

	@Override
	public List<OaMenu> findFirst(String path) {
		StringBuilder sb = new StringBuilder();
		sb.append("select t.menu_name name, t.menu_code code, t.menu_path path, t.id id, ");
		sb.append(" t.menu_dept dept, t.menu_comment mcomment, t.stop_flag stop_flag, ");
		sb.append(" t.menu_explain explain, t.menu_publishdirt publishdirt, t.menu_parentcode parentcode, ");
		sb.append(" t.menu_parentpath parentpath, t.menu_order morder, t.del_flag del_flg, ");
		sb.append(" t.CREATEUSER createUser, t.CREATEDEPT createDept, t.CREATETIME createTime,t.UPDATEUSER updateUser,UPDATETIME updateTime ");
		sb.append(" from t_oa_menu t where t.del_flag = '0' and t.stop_flag = '0' ");
		sb.append(" and t.menu_parentpath = ").append("'"+path+"'");
		
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sb.toString())
				.addScalar("name").addScalar("code").addScalar("id").addScalar("path").addScalar("dept")
				.addScalar("mcomment", Hibernate.INTEGER).addScalar("stop_flag", Hibernate.INTEGER)
				.addScalar("explain").addScalar("publishdirt", Hibernate.INTEGER).addScalar("parentcode").addScalar("parentpath")
				.addScalar("morder", Hibernate.INTEGER).addScalar("del_flg", Hibernate.INTEGER)
				.addScalar("createUser").addScalar("createDept").addScalar("createTime",Hibernate.DATE).addScalar("updateUser").addScalar("updateTime",Hibernate.DATE);
		List<OaMenu> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(OaMenu.class)).list();
		if(list.size() > 0 && list != null){
			return list;
		}
		return new ArrayList<OaMenu>();
	}

	@Override
	public List<OaMenu> findNoFirst(String path) {
		StringBuilder sb = new StringBuilder();
		sb.append("select t.menu_name name, t.menu_code code, t.menu_path path, t.id id, ");
		sb.append(" t.menu_dept dept, t.menu_comment mcomment, t.stop_flag stop_flag, ");
		sb.append(" t.menu_explain explain, t.menu_publishdirt publishdirt, t.menu_parentcode parentcode, ");
		sb.append(" t.menu_parentpath parentpath, t.menu_order morder, t.del_flag del_flg, ");
		sb.append(" t.CREATEUSER createUser, t.CREATEDEPT createDept, t.CREATETIME createTime,t.UPDATEUSER updateUser,UPDATETIME updateTime ");
		sb.append(" from t_oa_menu t where t.del_flag = '0' and t.stop_flag = '0' ");
		sb.append(" and t.menu_parentpath like ").append("'%"+path+",%'");
		
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sb.toString())
				.addScalar("name").addScalar("code").addScalar("id").addScalar("path").addScalar("dept")
				.addScalar("mcomment", Hibernate.INTEGER).addScalar("stop_flag", Hibernate.INTEGER)
				.addScalar("explain").addScalar("publishdirt", Hibernate.INTEGER).addScalar("parentcode").addScalar("parentpath")
				.addScalar("morder", Hibernate.INTEGER).addScalar("del_flg", Hibernate.INTEGER)
				.addScalar("createUser").addScalar("createDept").addScalar("createTime",Hibernate.DATE).addScalar("updateUser").addScalar("updateTime",Hibernate.DATE);
		List<OaMenu> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(OaMenu.class)).list();
		if(list.size() > 0 && list != null){
			return list;
		}
		return new ArrayList<OaMenu>();
	}

	@Override
	public List<MenuVo> findAllChildWithOutStop(String code) {

		StringBuilder sb = new StringBuilder();
		sb.append("select t.menu_name name, t.menu_code code, t.menu_path path from t_oa_menu t where t.del_flag = '0' and t.stop_flag = '0'  ");
		sb.append(" and t.menu_parentpath = ").append("'"+code+"'");
		sb.append(" order by t.menu_order");
		
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sb.toString())
				.addScalar("name").addScalar("code").addScalar("path");
		List<MenuVo> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(MenuVo.class)).list();
		if(list.size() > 0 && list != null){
			return list;
		}
		return new ArrayList<MenuVo>();
	}

	@Override
	public MenuVo getLast(Integer morder,String parentCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("select t.menu_name name, t.menu_code code, t.menu_path path, t.id id, ");
		sb.append(" t.menu_dept dept, t.menu_comment mcomment, t.stop_flag stop_flag, ");
		sb.append(" t.menu_explain explain, t.menu_publishdirt publishdirt, t.menu_parentcode parentcode, ");
		sb.append(" t.menu_parentpath parentpath, t.menu_order morder ");
		sb.append(" from t_oa_menu t where t.del_flag = '0' ");
		sb.append(" and t.menu_parentcode = ").append("'"+parentCode+"'");
		sb.append(" and t.menu_order < ").append("'"+morder+"'");
		sb.append(" order by t.menu_order desc");
		
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sb.toString())
				.addScalar("name").addScalar("code").addScalar("id").addScalar("path").addScalar("dept")
				.addScalar("mcomment", Hibernate.INTEGER).addScalar("stop_flag", Hibernate.INTEGER)
				.addScalar("explain").addScalar("publishdirt", Hibernate.INTEGER).addScalar("parentcode").addScalar("parentpath")
				.addScalar("morder", Hibernate.INTEGER);
		List<MenuVo> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(MenuVo.class)).list();
		if(list.size() > 0 && list != null){
			return list.get(0);
		}
		return new MenuVo();
	}

	@Override
	public MenuVo getNext(Integer morder, String parentCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("select t.menu_name name, t.menu_code code, t.menu_path path, t.id id, ");
		sb.append(" t.menu_dept dept, t.menu_comment mcomment, t.stop_flag stop_flag, ");
		sb.append(" t.menu_explain explain, t.menu_publishdirt publishdirt, t.menu_parentcode parentcode, ");
		sb.append(" t.menu_parentpath parentpath, t.menu_order morder ");
		sb.append(" from t_oa_menu t where t.del_flag = '0' ");
		sb.append(" and t.menu_parentcode = ").append("'"+parentCode+"'");
		sb.append(" and t.menu_order > ").append("'"+morder+"'");
		sb.append(" order by t.menu_order");
		
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sb.toString())
				.addScalar("name").addScalar("code").addScalar("id").addScalar("path").addScalar("dept")
				.addScalar("mcomment", Hibernate.INTEGER).addScalar("stop_flag", Hibernate.INTEGER)
				.addScalar("explain").addScalar("publishdirt", Hibernate.INTEGER).addScalar("parentcode").addScalar("parentpath")
				.addScalar("morder", Hibernate.INTEGER);
		List<MenuVo> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(MenuVo.class)).list();
		if(list.size() > 0 && list != null){
			return list.get(0);
		}
		return new MenuVo();
	}

}
