package cn.honry.oa.personalAddressList.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.PersonalAddressList;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.personalAddressList.dao.PersonalAddressListDao;
import cn.honry.utils.ShiroSessionUtils;

@Repository("personalAddressListDao")
@SuppressWarnings({ "all" })
public class PersonalAddressListDaoImpl  extends HibernateEntityDao<PersonalAddressList> implements PersonalAddressListDao{

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**  
	 * 
	 * 获取所有分组
	 * @Author: zxl
	 * @CreateDate: 2017年7月19日 上午10:26:01 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月19日 上午10:26:01 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:userAccount 所属用户
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PersonalAddressList> queryAllGroup(String groupCode,String userAccount) {
		StringBuffer sb=new StringBuffer();
		if(StringUtils.isBlank(groupCode)){
			sb.append("select t.id as id ,t.groupname as groupName ,t.parentcode as parentCode   from t_oa_personaladdresslist t ");
			sb.append(" where t.ifgroup=1  and t.BELONGUSER=:belongUser and del_flg=0 and stop_flg=0");
		}else{
			sb.append("select t.id as id ,t.groupname as groupName ,t.parentcode as parentCode   from t_oa_personaladdresslist t ");
			sb.append(" where t.ifgroup=1 and t.parentcode=:groupCode  and t.BELONGUSER=:belongUser and del_flg=0 and stop_flg=0 ");
		}
		SQLQuery queryObject=this.getSession().createSQLQuery(sb.toString());
		queryObject.setParameter("belongUser", userAccount);
		if(StringUtils.isNotBlank(groupCode)){
			queryObject.setParameter("groupCode", groupCode);
		}
		queryObject.addScalar("id").addScalar("groupName").addScalar("parentCode");
		List<PersonalAddressList> operaVoList =queryObject.setResultTransformer(Transformers.aliasToBean(PersonalAddressList.class)).list();
		if(operaVoList!=null&&operaVoList.size()>0){
			return operaVoList;
		}
		return new ArrayList<PersonalAddressList>();
	}

	/**  
	 * 
	 * 根据当前登录人获取该分组下信息
	 * @Author: zxl
	 * @CreateDate: 2017年7月19日 上午10:26:01 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月19日 上午10:26:01 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param userAccount 所属用户
	 * @param:groupCode 所属分组code
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PersonalAddressList> queryAllPersonal(String groupCode,
			String userAccount) {
		StringBuffer sb=new StringBuffer();
		sb.append("select t.id as id ,t.subgroupname as subgroupName   from t_oa_personaladdresslist t ");
		sb.append(" where t.ifgroup=1 and t.parentcode='root' and t.parentuppath='root' and t.BELONGUSER=:belongUser ");
		SQLQuery queryObject=this.getSession().createSQLQuery(sb.toString());
		queryObject.setParameter("belongUser", userAccount);
		queryObject.addScalar("id").addScalar("subgroupName");
		List<PersonalAddressList> operaVoList =queryObject.setResultTransformer(Transformers.aliasToBean(PersonalAddressList.class)).list();
		if(operaVoList!=null&&operaVoList.size()>0){
			return operaVoList;
		}
		return new ArrayList<PersonalAddressList>();
	}

	@Override
	public List<PersonalAddressList> findAllByParentCode(String id) {
		String sql=" from PersonalAddressList where parentCode=? and del_flg=0 and stop_flg=0";
		List<PersonalAddressList> list = super.find(sql, id);
		return list;
	}

	/**  
	 *  根据id删除该分组
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param： id 分组id
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void delGroupById(String id) throws Exception{
		String sql=" update t_oa_personaladdresslist set del_flg=1, stop_flg=1 where (id=:id or parentUppath like :parentcode) and del_flg=0 and stop_flg=0 and  belongUser=:belongUser";
		SQLQuery queryObject=this.getSession().createSQLQuery(sql);	
		queryObject.setParameter("id", id);
		queryObject.setParameter("parentcode", "%"+id+"%");
		queryObject.setParameter("belongUser", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		queryObject.executeUpdate();
	}

	/**  
	 * 获取总条数
	 * @Author: zxl
	 * @CreateDate: 2017年7月19日 下午8:01:07 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月19日 下午8:01:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:parentCode 上级id
	 * @param:queryName 封装查询参数
	 * @throws:
	 * @return: 
	 *
	 */	
	@Override
	public int getPersonalTotal(String parentCode, String queryName) {
		String userAccount =ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		StringBuffer sb=new StringBuffer();
		sb.append("from PersonalAddressList where del_flg=0 and stop_flg=0 and ifGroup=0 and belongUser='")
		  .append(userAccount).append("'");
		
		if(StringUtils.isNotBlank(parentCode)){
			sb.append(" and parentUppath like '%").append(parentCode).append("%'");
		}
		if(StringUtils.isNotBlank(queryName)){
			sb.append(" and (perName like '%").append(queryName).append("%' or perPinyin like '%").append(queryName.toUpperCase())
			  .append("%' or perWb like '%").append(queryName.toUpperCase()).append("%' or perInputCode like '%").append(queryName)
			  .append("%')");
		}
		return super.getTotal(sb.toString());
	}

	/**  
	 * 获取当前页数据
	 * @Author: zxl
	 * @CreateDate: 2017年7月19日 下午8:01:07 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月19日 下午8:01:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:parentCode 上级id
	 * @param:queryName 封装查询参数
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PersonalAddressList> getPersonalLists(String page, String rows,
			String parentCode, String queryName) {
		String userAccount =ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		StringBuffer sb=new StringBuffer();
		sb.append("from PersonalAddressList where del_flg=0 and stop_flg=0 and ifGroup=0and belongUser='")
		  .append(userAccount).append("'");
		
		if(StringUtils.isNotBlank(parentCode)){
			sb.append(" and parentUppath like '%").append(parentCode).append("%'");
		}
		if(StringUtils.isNotBlank(queryName)){
			sb.append(" and (perName like '%").append(queryName).append("%' or perPinyin like '%").append(queryName.toUpperCase())
			  .append("%' or perWb like '%").append(queryName.toUpperCase()).append("%' or perInputCode like '%").append(queryName)
			  .append("%')");
		}
		sb.append(" order by perName ");
		List<PersonalAddressList> list= super.getPage(sb.toString(), page, rows);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<PersonalAddressList>();
	}

	/**  
	 * 根据id删除个人信息
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param： id
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void delPersonalMes(String id) throws Exception {
		String ids[]=id.split(",");
		String sql=" update t_oa_personaladdresslist set del_flg=1, stop_flg=1 where id in(:id) and del_flg=0 and stop_flg=0 and belonguser=:belongUser";
		SQLQuery queryObject=this.getSession().createSQLQuery(sql);	
		queryObject.setParameterList("id", ids);
		queryObject.setParameter("belongUser", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		queryObject.executeUpdate();
	}
	
	/**  
	 * 根据当前登录用户获取所有科室
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param： id
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<PersonalAddressList> findAllGroup(String userAccount) {
		StringBuffer sb=new StringBuffer();
		sb.append("select t.id as id ,t.groupname as groupName    from t_oa_personaladdresslist t ");
		sb.append(" where t.ifgroup=1  and t.BELONGUSER=:belongUser and del_flg=0 and stop_flg=0");
		SQLQuery queryObject=this.getSession().createSQLQuery(sb.toString());
		queryObject.setParameter("belongUser", userAccount);
		queryObject.addScalar("id").addScalar("groupName");
		List<PersonalAddressList> operaVoList =queryObject.setResultTransformer(Transformers.aliasToBean(PersonalAddressList.class)).list();
		if(operaVoList!=null&&operaVoList.size()>0){
			return operaVoList;
		}
		return new ArrayList<PersonalAddressList>();
	}

	/**  
	 * 
	 * 个人信息移至其他分组
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:id 需要移动的id
	 * @param:personal 上级分组信息
	 * @throws:
	 * @return: String
	 *
	 */
	@Override
	public void movePersonalToGroup(String id, PersonalAddressList personal) throws Exception {
		String ids[]=id.split(",");
		String sql=" update t_oa_personaladdresslist set PARENTCODE=:parentCode, PARENTUPPATH=:parentUppath ,BELONGGROUPNAME=:belongGroupName where id in(:id) and del_flg=0 and stop_flg=0";
		SQLQuery queryObject=this.getSession().createSQLQuery(sql);	
		queryObject.setParameter("parentCode", personal.getId());
		if(StringUtils.isNotBlank(personal.getId())){
			queryObject.setParameter("parentUppath", personal.getParentUppath()+","+personal.getId());
		}else{
			queryObject.setParameter("parentUppath", personal.getParentUppath());
		}
		queryObject.setParameter("belongGroupName", personal.getGroupName());
		queryObject.setParameterList("id", ids);
		
		queryObject.executeUpdate();
		
	}

	@Override
	public int getMaxOrder() {
		String userAccount =ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		StringBuffer sb=new StringBuffer();
		sb.append("select nvl(max(t.PERORDER),0)    from t_oa_personaladdresslist t ");
		sb.append(" where t.BELONGUSER=:belongUser and del_flg=0 and stop_flg=0");
		SQLQuery query=this.getSession().createSQLQuery(sb.toString());  
		query.setParameter("belongUser", userAccount);
		BigDecimal a=(BigDecimal) query.uniqueResult();
		return a.intValue();
	}
}
