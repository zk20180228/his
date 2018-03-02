package cn.honry.inpatient.bill.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.utli.DataRightUtils;
import cn.honry.inpatient.bill.dao.PhaStoControlDAO;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

import com.google.gson.reflect.TypeToken;
@Repository("phaStoControlDAO")
@SuppressWarnings("all")
public class PhaStoControlDAOImpl extends HibernateEntityDao<OutpatientDrugcontrol> implements PhaStoControlDAO{
	@Resource(name="sessionFactory")
	private void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<OutpatientDrugcontrol> getPage(String page, String rows,OutpatientDrugcontrol outpatientDrugcontrol) {
		String hql = "from OutpatientDrugcontrol p where p.del_flg = 0 and p.stop_flg = 0 AND "+DataRightUtils.connectHQLSentence("p")+"";
		if(outpatientDrugcontrol!=null){
			if(StringUtils.isNotBlank(outpatientDrugcontrol.getDeptCode())){
				hql +=  " and p.deptCode = '" + outpatientDrugcontrol.getDeptCode() + "'";
			}
		}
		return super.getPage(hql, page, rows);
	}
	//按条件查询
	@Override
	public List<OutpatientDrugcontrol> QueryOutpatientDrugcontrol(String page, String rows, OutpatientDrugcontrol outpatientDrugcontrol) {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct t.control_name as controlName,t.control_attr as controlAttr,t.mark as mark,t.show_level as showLevel,t.send_type as sendType,t.dept_code as deptCode from t_outpatient_drugcontrol t where t.del_flg=0 ");
		if(outpatientDrugcontrol!=null){
			if(StringUtils.isNotBlank(outpatientDrugcontrol.getDeptCode())){
				sql.append(" and t.dept_code = '" + outpatientDrugcontrol.getDeptCode() + "'");
			}
		}
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString()).addScalar("controlName").addScalar("controlAttr")
		.addScalar("mark").addScalar("showLevel",Hibernate.INTEGER).addScalar("sendType",Hibernate.INTEGER).addScalar("deptCode");
		List<OutpatientDrugcontrol> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientDrugcontrol.class)).setFirstResult((start - 1) * count).setMaxResults(count).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OutpatientDrugcontrol>();
	}
	//查询
	@Override
	public List<OutpatientDrugcontrol> getPageList(String page, String rows, OutpatientDrugcontrol outpatientDrugcontrol) {
		String hql="FROM OutpatientDrugcontrol d where d.del_flg = 0 and d.stop_flg = 0";
		if(StringUtils.isNotBlank(outpatientDrugcontrol.getDeptCode())){
			hql=hql+" and d.deptCode='"+outpatientDrugcontrol.getDeptCode()+"'";
		}
		return super.getPage(hql, page, rows);
	}
	
	@Override
	public int getTotal(OutpatientDrugcontrol outpatientDrugcontrol) {
		String hql = "from OutpatientDrugcontrol p where p.del_flg = 0 and p.stop_flg = 0";
		if(outpatientDrugcontrol!=null){
			if(StringUtils.isNotBlank(outpatientDrugcontrol.getDeptCode())){
				hql +=  "and p.deptCode = '" + outpatientDrugcontrol.getDeptCode() + "'";
			}
		}
		return super.getTotal(hql);
	}
	/**  
	 *  
	 * @Description： 保存
	 * @Author：dh
	 * @CreateDate：2015-12-21上午09:56:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public void saveOutpatientDrugcontrol(
			OutpatientDrugcontrol outpatientDrugcontrol) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		outpatientDrugcontrol.setCreateUser(user.getId());
		outpatientDrugcontrol.setCreateDept(dept.getId());
		outpatientDrugcontrol.setCreateTime(new Date());
		outpatientDrugcontrol.setStop_flg(0);
		outpatientDrugcontrol.setDel_flg(0);
		super.save(outpatientDrugcontrol);
	}
	@Override
	/**
	 * @Modifier：liujl
	 * @ModifyDate：2016-6-6 上午09:56:35  
	 * @ModifyRmk：  修改获取记录总条数方法
	 */	
	public int getTotalOutpatientDrugcontrol(
			OutpatientDrugcontrol outpatientDrugcontrol) {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct t.control_name as controlName,t.control_attr as controlAttr,t.mark as mark,t.show_level as showLevel,t.send_type as sendType,t.dept_code as deptCode from t_outpatient_drugcontrol t where t.del_flg=0 ");
		if(outpatientDrugcontrol!=null){
			if(StringUtils.isNotBlank(outpatientDrugcontrol.getDeptCode())){
				sql.append(" and t.dept_code = '" + outpatientDrugcontrol.getDeptCode() + "'");
			}
		}
		return super.getSqlTotal(sql.toString());
	}
	//删除
	@Override
	public void delUpdate(String id) {
		String sql="update t_outpatient_drugcontrol set del_flg= 1 where control_id= ? ";
		this.getSession().createSQLQuery(sql).setString(0, id).executeUpdate();
	}

	@Override
	public void UpdateOutpatientDrugcontrol(String deptCode,String con,String billJson) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		List<OutpatientDrugcontrol> modelList = null;
		try {
			billJson=billJson.replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
			modelList =JSONUtils.fromJson(billJson,  new TypeToken<List<OutpatientDrugcontrol>>(){}, "yyyy-MM-dd hh:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String controlAttr = modelList.get(0).getControlAttr();//摆药台属性
		String controlName = modelList.get(0).getControlName();//摆药台名称
		int showLevel = modelList.get(0).getShowLevel();//显示等级
		int sendType = modelList.get(0).getSendType();//发送类型
		String mark = modelList.get(0).getMark();//备注
		if("".equals(deptCode)){
			String sql="update t_outpatient_drugcontrol set control_attr= ?,mark = ?,send_type=? ,show_level=?,control_name = ?,updatetime=?,updateuser=? where control_id = ?";
			this.getSession().createSQLQuery(sql).setString(0, controlAttr).setString(1, mark).setInteger(2, sendType).setInteger(3, showLevel).setString(4, controlName).setDate(5, new Date()).setString(6, user.getAccount()).setString(7, con).executeUpdate();
		}else{
			String sql="update t_outpatient_drugcontrol set control_attr= ?,mark = ?,send_type=? ,show_level=?,control_name = ?,updatetime=?,updateuser=?,dept_code=? where control_id = ?";
			this.getSession().createSQLQuery(sql).setString(0, controlAttr).setString(1, mark).setInteger(2, sendType).setInteger(3, showLevel).setString(4, controlName).setDate(5, new Date()).setString(6, user.getAccount()).setString(7, deptCode).setString(8, con).executeUpdate();
		}
	}

	@Override
	public List<OutpatientDrugcontrol> QueryOutpatientDrugcontrolupdate(String id) {
		String hql="from OutpatientDrugcontrol where id='"+id+"' ";
		List<OutpatientDrugcontrol> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return null;
	}
	
	//删除
	@Override
	public void updateControlId(String ControlId,String id) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		String sql="update t_drug_billclass set CONTROL_ID=? , UPDATEUSER =?,UPDATETIME =? where billclass_id = ?";
		this.getSession().createSQLQuery(sql).setString(0, ControlId).setString(1, user.getAccount()).setDate(2, new Date()).setString(3, id).executeUpdate();
	}

	@Override
	public List<DrugBillclass> queryDrugBillclass(String controId) {
		String hql="from DrugBillclass where controlId='"+controId+"' ";
		List<DrugBillclass> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return null;
	}

	@Override
	public List<String> findDrugBillclass(String con) {
		StringBuilder sql = new StringBuilder();
		sql.append("select t.billclass_id as billclassId from t_drug_billclass t where t.del_flg=0 and t.stop_flg=0 and t.control_id =:controId");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString()).addScalar("billclassId");
		queryObject.setParameter("controId", con);
		List<String> list = queryObject.list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<String>();
	}

	@Override
	public void updateClassBillcon(String controId) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		String sql="update t_drug_billclass set CONTROL_ID='', UPDATEUSER =?,UPDATETIME =? where billclass_id = ? ";
		this.getSession().createSQLQuery(sql).setString(0, user.getAccount()).setDate(1, new Date()).setString(2, controId).executeUpdate();
		
	}
}
