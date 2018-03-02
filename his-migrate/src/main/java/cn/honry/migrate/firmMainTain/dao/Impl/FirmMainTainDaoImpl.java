package cn.honry.migrate.firmMainTain.dao.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.FirmMainTain;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.migrate.firmMainTain.dao.FirmMainTainDao;
@Repository("firmMainTainDao")
@SuppressWarnings({"all"})
public class FirmMainTainDaoImpl extends HibernateEntityDao<FirmMainTain> implements FirmMainTainDao{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**  
	 * 厂商维护列表查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月27日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月27日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public List<FirmMainTain> queryFirmMainTain(String code, String page,String rows, String menuAlias) {
		String hql="from FirmMainTain t where 1=1 ";
		if(StringUtils.isNotBlank(code)){
			hql+=" and (t.firmCode like '%"+code+"%' ";
			hql+=" or t.firmName like '%"+code+"%' )";
		}
		hql+="order by to_number(t.firmCode)";
		return super.getPage(hql, page, rows);
	}
	/**  
	 * 厂商维护(总条数)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月27日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月27日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public int queryTotal(String code) {
		String hql="from FirmMainTain t where 1=1 ";
		if(StringUtils.isNotBlank(code)){
			hql+=" and (t.firmCode like '%"+code+"%' ";
			hql+=" or t.firmName like '%"+code+"%' )";
		}
		return super.getTotal(hql);
	}
	/**  
	 * 厂商维护 删除
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月27日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月27日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public void delFirmMainTain(String id) {
		String sql="delete from I_FIRM_MAINTAIN where id='"+id+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();		
	}
	/**  
	 * 厂商维护 查询要修改的记录
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月27日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月27日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public FirmMainTain getOnedata(String id) {
		String hql="from FirmMainTain t where t.id= '"+id+"'";
		List<FirmMainTain> list=this.getSession().createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new FirmMainTain();
		
	}
	@Override
	public void updatePasswor(String id, String pass) {
		String sql="update I_FIRM_MAINTAIN t set t.PASSWORD ='"+pass+"' where t.id='"+id+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();
		
	}
	@Override
	public FirmMainTain getMaxCode(String id) {
		String hql="from FirmMainTain t order by t.firmCode desc";
		List<FirmMainTain> list=this.getSession().createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new FirmMainTain();
	}
	@Override
	public List<FirmMainTain> queryFirm() {
		String hql="from FirmMainTain  ";
		List<FirmMainTain> list=this.getSession().createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<FirmMainTain>();
	}
	
}
