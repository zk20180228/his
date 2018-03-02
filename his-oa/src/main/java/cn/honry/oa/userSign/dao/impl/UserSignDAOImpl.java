package cn.honry.oa.userSign.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.OaUserSign;
import cn.honry.base.bean.model.OaUserSignChange;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.SysRole;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.userSign.dao.UserSignDAO;
import cn.honry.utils.ShiroSessionUtils;

/**  
 *  
 * @className：UserSignDAOImpl
 * @Description：  用户电子签章维护
 * @Author：aizhonghua
 * @CreateDate：2017-7-14 下午16:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-7-14 下午16:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("userSignDAO")
@SuppressWarnings({ "all" })
public class UserSignDAOImpl extends HibernateEntityDao<OaUserSign> implements UserSignDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**  
	 *  
	 * @Description：  获取列表-获得总条数
	 * @Author：aizhonghua
	 * @CreateDate：2017-7-14 下午17:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-7-14 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public int getUserSignTotal(String search) {
		String hql = getUserSignHql(search);
		if(hql==null){
			return 0;
		}
		return super.getTotal(hql);
	}

	/**  
	 *  
	 * @Description：  获取列表-获得显示信息
	 * @Author：aizhonghua
	 * @CreateDate：2017-7-14 下午17:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-7-14 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<OaUserSign> getUserSignRows(String page, String rows, String search) {
		String hql = getUserSignHql(search);
		if(hql==null){
			return new ArrayList<OaUserSign>();
		}
		return super.getPage(hql, page, rows);
	}
	
	/**  
	 *  
	 * @Description：  获取列表-获得HQL
	 * @Author：aizhonghua
	 * @CreateDate：2017-7-14 下午17:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-7-14 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	private String getUserSignHql(String search) {
		SysRole role = ShiroSessionUtils.getCurrentUserLoginRoleFromShiroSession();
		if(role==null){
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("from OaUserSign s where s.del_flg = 0");
		if(!"superManager".equals(role.getAlias())){
			User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
			buffer.append("and s.userAcc = '").append(user.getAccount()).append("'");
		}
		if(StringUtils.isNotBlank(search)){
			buffer.append("and (s.signName like '%").append(search)
			.append("%' or upper(s.signPinYin) like '%").append(search)
			.append("%' or upper(s.signWb) like '%").append(search)
			.append("%' or upper(s.userAcc) like '%").append(search)
			.append("%' or upper(s.userAccName) like '%").append(search)
			.append("%' or upper(s.signInputcode) like '%").append(search).append("%') ");
		}
		buffer.append("order by s.userAcc,s.signType");
		return buffer.toString();
	}

	@Override
	public List<SysRole> getSysRole(String page, String rows,String q) {
		String hql = "from SysRole s where s.del_flg = 0 ";
		if(StringUtils.isNotBlank(q)){
			q=q.toUpperCase();
			hql+="and (upper(s.alias) like '%"+q+"%' or upper(s.name) like '%"+q+"%' or upper(s.pinyin) like '%"+q+"%' or upper(s.wb) like '%"+q+"%' or upper(s.inputcode) like '%"+q+"%' )";
		}
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		Query query = this.getSession().createQuery(hql);
		List<SysRole> list = query.setFirstResult((start - 1) * count).setMaxResults(count).list();
		if(list.size()>0){
			return list;
		}
		return new ArrayList<SysRole>();
	}

	@Override
	public int getSysRoleTotal(String q) {
		String hql = "from SysRole s where s.del_flg = 0";
		if(StringUtils.isNotBlank(q)){
			q=q.toUpperCase();
			hql+="and (upper(s.alias) like '%"+q+"%' or upper(s.name) like '%"+q+"%' or upper(s.pinyin) like '%"+q+"%' or upper(s.wb) like '%"+q+"%' or upper(s.inputcode) like '%"+q+"%' )";
		}
		List<SysRole> list=super.find(hql, null);
		if(list.size()>0){
			return list.size();
		}
		return 0;
	}

	@Override
	public List<BusinessDictionary> getBusinessDictionary(String page,
			String rows,String q) {
		String hql = "from BusinessDictionary s where s.del_flg = 0 and s.stop_flg=0 and s.type='duties' ";
		if(StringUtils.isNotBlank(q)){
			q=q.toUpperCase();
			hql+="and (upper(s.encode) like '%"+q+"%' or upper(s.name) like '%"+q+"%' or upper(s.pinyin) like '%"+q+"%' or upper(s.wb) like '"+q+"' or upper(s.inputCode) like '"+q+"' )";
		}
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		Query query = this.getSession().createQuery(hql);
		List<BusinessDictionary> list = query.setFirstResult((start - 1) * count).setMaxResults(count).list();
		if(list.size()>0){
			return list;
		}
		return new ArrayList<BusinessDictionary>();
	}

	@Override
	public int getBusinessDictionaryTotal(String q) {
		String hql = "from BusinessDictionary s where s.del_flg = 0 and s.stop_flg=0 and s.type='duties'";
		if(StringUtils.isNotBlank(q)){
			q=q.toUpperCase();
			hql+="and (upper(s.encode) like '%"+q+"%' or upper(s.name) like '%"+q+"%' or upper(s.pinyin) like '%"+q+"%' or upper(s.wb) like '"+q+"' or upper(s.inputCode) like '"+q+"' )";
		}
		List<BusinessDictionary> list=super.find(hql, null);
		if(list.size()>0){
			return list.size();
		}
		return 0;
	}

	@Override
	public OaUserSign getSignRow(String signid, String password,
			String account) {
		String hql = "from OaUserSign s where s.del_flg = 0 and s.stop_flg = 0 ";
		if(StringUtils.isNotBlank(signid)){
			hql += " and s.id = '"+signid+"'";
		}
		if(StringUtils.isNotBlank(account)){
			hql += " and s.userAcc = '"+account+"' and signType = 1";
		}
		List<OaUserSign> list=super.find(hql, null);
		if(list.size()>0){
			return list.get(0);
		}
		return new OaUserSign();
	}

	@Override
	public List<OaUserSign> getUserOneSignRows(String search, String account) {
		String hql = "from OaUserSign s where s.del_flg = 0 and s.stop_flg = 0 ";
		if(StringUtils.isNotBlank(account)){
			hql += " and s.userAcc = '"+account+"' and signType = 1";
		}
		if(StringUtils.isNotBlank(search)){
			hql += "and (s.signName like '%"+search+"%'"
					+ " or upper(s.signPinYin) like '%"+search+"%' or upper(s.signWb) like '%"+search+"%'"
					+ " or upper(s.signInputcode) like '%"+search+"%' ) ";
		}
		List<OaUserSign> list=super.find(hql, null);
		if(list.size()>0){
			return list;
		}
		return new ArrayList<OaUserSign>();
	}

	@Override
	public List<OaUserSign> queryOaUserSigns(String account) {
		String hql = "from OaUserSign s where s.del_flg = 0 and s.stop_flg = 0 ";
		hql +=" and s.userAcc like '%"+account+"%' ";
		List<OaUserSign> list=super.find(hql, null);
		if(list.size()>0){
			return list;
		}
		return new ArrayList<OaUserSign>();
	}

	@Override
	public OaUserSign queryOaUserSignByid(String id, String version) {
		String hql = "from OaUserSign s where s.del_flg = 0 and s.stop_flg = 0 ";
		if(StringUtils.isNotBlank(id)){//个人签名
			hql +=" and s.id='"+id+"' ";
		}
		if(StringUtils.isNotBlank(version)){
			hql +=" and s.version='"+version+"' ";
		}
		List<OaUserSign> list=super.find(hql, null);
		if(list.size()>0){
			return list.get(0);
		}
		return new OaUserSign();
	}

	@Override
	public OaUserSignChange queryOaUserSignChangeByid(String id, String version) {
		String hql = "from OaUserSignChange s where s.del_flg = 0 and s.stop_flg = 0 ";
		if(StringUtils.isNotBlank(id)){//个人签名
			hql +=" and s.signId='"+id+"' ";
		}
		if(StringUtils.isNotBlank(version)){
			hql +=" and s.version='"+version+"' ";
		}
		List<OaUserSignChange> list=super.find(hql, null);
		if(list.size()>0){
			return list.get(0);
		}
		return new OaUserSignChange();
	}

	@Override
	public OaUserSign getElecSign(String account) {
		String hql = "from OaUserSign s where s.del_flg = 0 and s.stop_flg = 0 and s.userAcc ='"+account+"'";
		List<OaUserSign> list=super.find(hql, null);
		if(list.size()>0){
			return list.get(0);
		}
		return new OaUserSign();
	}

}
