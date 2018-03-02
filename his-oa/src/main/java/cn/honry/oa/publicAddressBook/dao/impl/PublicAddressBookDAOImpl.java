package cn.honry.oa.publicAddressBook.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessChangeRecord;
import cn.honry.base.bean.model.PublicAddressBook;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysMenu;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.publicAddressBook.dao.PublicAddressBookDAO;

@Repository(value="publicAddressBookDAO")
@SuppressWarnings({"all"})
public class PublicAddressBookDAOImpl extends HibernateEntityDao<PublicAddressBook> implements PublicAddressBookDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**  
	 * 
	 * 公共通讯录树
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月17日 上午10:37:25 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月17日 上午10:37:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PublicAddressBook> findTree() {
		String hql="from PublicAddressBook where del_flg=0 and stop_flg=0 order by nodeType,path";
		List<PublicAddressBook> list=super.find(hql, null);
		if(list!=null && list.size()>0){
			return list;
		}
			return new ArrayList<PublicAddressBook>();
	}
	/**  
	 * 
	 * 获得栏目的全部子栏目
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月17日 下午5:53:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月17日 下午5:53:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PublicAddressBook> getChildById(String superPath) {
		String hql="FROM PublicAddressBook r WHERE r.superPath LIKE '"+(StringUtils.isBlank(superPath)?"":superPath)+"%' ";
		hql+="and r.stop_flg=0 and r.del_flg=0 ";
		List<PublicAddressBook> list = super.findByObjectProperty(hql, null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<PublicAddressBook>();
	}
	/**  
	 * 
	 * 获得栏目的全部子栏目
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月17日 下午5:53:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月17日 下午5:53:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PublicAddressBook> getChildByIds(String ids) {
		String hql="FROM PublicAddressBook r WHERE r.del_flg=0 ";
		if(ids.contains(",")){
			String[] id = ids.split(",");
			for (int i = 0; i < id.length; i++) {
				if(i==0){
					hql+=" AND r.superPath LIKE '%"+id[i]+",%' ";
				}else{
					hql+=" OR r.superPath LIKE '%"+id[i]+",%' ";
				}
			}
		}else{
			hql+=" AND r.superPath LIKE '%"+ids+",%' ";
		}
		List<PublicAddressBook> list = super.findByObjectProperty(hql, null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<PublicAddressBook>();
	}
	/**  
	 * 
	 * 获得栏目的楼号
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月18日 下午2:27:23 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月18日 下午2:27:23  
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PublicAddressBook> getVoNoStringList() {
		String hql="select distinct t.node_name as name from t_oa_public_book t where t.node_type='11' ";
		hql+="and t.stop_flg=0 and t.del_flg=0 order by t.node_name";
		SQLQuery queryObject = getSession().createSQLQuery(hql);
		queryObject.addScalar("name",Hibernate.STRING);
		List<PublicAddressBook> list = queryObject.setResultTransformer(Transformers.aliasToBean(PublicAddressBook.class)).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<PublicAddressBook>();
	}
	/**  
	 * 
	 * 获得栏目的楼层
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月18日 下午2:27:23 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月18日 下午2:27:23  
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PublicAddressBook> getVoFloorList() {
		String hql="select distinct t.node_name as name from t_oa_public_book t where t.node_type='22' ";
		hql+="and t.stop_flg=0 and t.del_flg=0 order by t.node_name";
		SQLQuery queryObject = getSession().createSQLQuery(hql);
		queryObject.addScalar("name",Hibernate.STRING);
		List<PublicAddressBook> list = queryObject.setResultTransformer(Transformers.aliasToBean(PublicAddressBook.class)).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<PublicAddressBook>();
	}
	/**  
	 * 
	 * 获得栏目的全部工作站
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月18日 上午11:55:51 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月18日 上午11:55:51 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PublicAddressBook> getPublicBookList(String page, String rows,String id,String nodeType,String areaCode,
			String noString, String floor, String typeName, String deptCode) {
		String hql = querySql(page, rows, id, nodeType, areaCode, noString, floor, typeName, deptCode);
		SQLQuery queryObject = this.getSession().createSQLQuery(hql)
				.addScalar("id",Hibernate.STRING)
				.addScalar("areaName",Hibernate.STRING)
				.addScalar("buildingName",Hibernate.STRING)
				.addScalar("floorName",Hibernate.STRING)
				.addScalar("floorType",Hibernate.STRING)
				.addScalar("floorDept",Hibernate.STRING)
				.addScalar("name",Hibernate.STRING)
				.addScalar("phone",Hibernate.STRING)
				.addScalar("minPhone",Hibernate.STRING)
				.addScalar("officePhone",Hibernate.STRING)
				.addScalar("status",Hibernate.INTEGER);
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		List<PublicAddressBook> list = queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(PublicAddressBook.class)).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<PublicAddressBook>();
	}
	/**  
	 * 
	 * 科室
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 上午9:09:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 上午9:09:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PublicAddressBook> getVoDeptList() {
		String hql="select distinct t.node_name as name from t_oa_public_book t where t.node_type='44' ";
		hql+="and t.stop_flg=0 and t.del_flg=0 ";
		SQLQuery queryObject = getSession().createSQLQuery(hql);
		queryObject.addScalar("name",Hibernate.STRING);
		List<PublicAddressBook> list = queryObject.setResultTransformer(Transformers.aliasToBean(PublicAddressBook.class)).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<PublicAddressBook>();
	}
	/**  
	 * 
	 * 类别名称
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 上午9:09:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 上午9:09:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PublicAddressBook> getVoTypeList() {
		String hql="select distinct t.node_name as name from t_oa_public_book t where t.node_type='33' ";
		hql+="and t.stop_flg=0 and t.del_flg=0 ";
		SQLQuery queryObject = getSession().createSQLQuery(hql);
		queryObject.addScalar("name",Hibernate.STRING);
		List<PublicAddressBook> list = queryObject.setResultTransformer(Transformers.aliasToBean(PublicAddressBook.class)).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<PublicAddressBook>();
	}
	/**  
	 * 
	 * 根据科室分类查所有科室
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 下午5:15:48 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 下午5:15:48 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<SysDepartment> getDept(String deptType) {
		String hql=" select t.dept_code as deptCode,t.dept_name as deptName from t_department t where t.stop_flg=0 and t.del_flg=0 ";
		if (StringUtils.isNotBlank(deptType)) {
			hql+=" and t.Dept_Type='"+deptType+"'";
		}
		SQLQuery queryObject = getSession().createSQLQuery(hql);
		queryObject.addScalar("deptCode",Hibernate.STRING)
				.addScalar("deptName", Hibernate.STRING);
		List<SysDepartment> list = queryObject.setResultTransformer(Transformers.aliasToBean(SysDepartment.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<SysDepartment>();
	}
	/**  
	 * 
	 * 院区
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 上午9:09:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 上午9:09:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PublicAddressBook> getVoAreaList() {
		String hql="select distinct t.node_name as name ,t.CREATETIME from t_oa_public_book t where t.node_type='00' ";
		hql+="and t.stop_flg=0 and t.del_flg=0 order by t.CREATETIME desc";
		SQLQuery queryObject = getSession().createSQLQuery(hql);
		queryObject.addScalar("name",Hibernate.STRING);
		List<PublicAddressBook> list = queryObject.setResultTransformer(Transformers.aliasToBean(PublicAddressBook.class)).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<PublicAddressBook>();
	}
	/**  
	 * 
	 * 获得栏目的全部工作站数量
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月25日 下午6:24:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月25日 下午6:24:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public int getPublicBookTotal(String page, String rows, String id,
			String nodeType, String areaCode, String noString, String floor,
			String typeName, String deptCode) {
		String hql = querySql(page, rows, id, nodeType, areaCode, noString, floor, typeName, deptCode);
		return super.getSqlTotal(hql);
	}
	
	private String querySql(String page, String rows,String id,String nodeType,String areaCode,
			String noString, String floor, String typeName, String deptCode){
		String hql="select t.AREA_NAME as areaName,t.BUILDING_NAME as buildingName,t.FLOOR_NAME as floorName, ";
		hql+="t.FLOOR_TYPE as floorType ,t.FLOOR_DEPT as floorDept,t.NODE_NAME as name,t.id as id,t.PHONE as phone,";
		hql+="t.MIN_PHONE as minPhone,t.OFFICE_PHONE as officePhone,t.STATUS as status from t_oa_public_book t where t.node_type='55' ";
		if (StringUtils.isNotBlank(id)) {
			if ("55".equals(nodeType)) {
					hql+="and t.id = '"+id+"' ";
			}else{
					hql+="and t.SUPERPATH like '%,"+id+",%' ";
			}
		}
		if (StringUtils.isNotBlank(areaCode)) {
			hql+="and t.AREA_NAME like '%"+areaCode+"%' ";
		}
		if (StringUtils.isNotBlank(noString)) {
			hql+="and t.BUILDING_NAME = '"+noString+"' ";
		}
		if (StringUtils.isNotBlank(floor)) {
			hql+="and t.FLOOR_NAME = '"+floor+"' ";
		}
		if (StringUtils.isNotBlank(typeName)) {
			hql+="and t.FLOOR_TYPE = '"+typeName+"' ";
		}
		if (StringUtils.isNotBlank(deptCode)) {
			hql+="and t.FLOOR_DEPT like '%"+deptCode+"%' ";
		}
		hql+="and t.stop_flg=0 and t.del_flg=0 order by t.AREA_NAME,BUILDING_NAME,FLOOR_NAME,FLOOR_TYPE,FLOOR_DEPT,NODE_NAME";
		return hql;
		
	}
}
