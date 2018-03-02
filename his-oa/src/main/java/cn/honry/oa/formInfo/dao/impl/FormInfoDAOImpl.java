package cn.honry.oa.formInfo.dao.impl;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.ExterInter;
import cn.honry.base.bean.model.OaFormInfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.formInfo.dao.FormInfoDAO;
import cn.honry.oa.formInfo.vo.KeyValVo;

/**  
 *  
 * @className：FormInfoDAOImpl
 * @Description：  自定义表单维护
 * @Author：aizhonghua
 * @CreateDate：2017-7-17 下午18:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-7-17 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("formInfoDAO")
@SuppressWarnings({ "all" })
public class FormInfoDAOImpl extends HibernateEntityDao<OaFormInfo> implements FormInfoDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**  
	 *  
	 * @Description：  获取列表-获取总条数
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public int getFormInfoTotal(String search) {
		String hql = getFormInfoHql(search);
		if(StringUtils.isBlank(hql)){
			return 0;
		}
		return super.getTotal(hql);
	}

	/**  
	 *  
	 * @Description：  获取列表-获取展示信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<OaFormInfo> getFormInfoRows(String page, String rows, String search) {
		String hql = getFormInfoHql(search);
		if(StringUtils.isBlank(hql)){
			return new ArrayList<OaFormInfo>();
		}
		return super.getPage(hql, page, rows);
	}
	
	/**  
	 *  
	 * @Description：  获取HQL
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	private String getFormInfoHql(String search){
		StringBuffer buffer = new StringBuffer();
		buffer.append("from OaFormInfo f where f.del_flg = 0 ");
		if(StringUtils.isNotBlank(search)){
			buffer.append("and (upper(f.formCode) like '%").append(search).append("%' or f.formName like '%").append(search).append("%') ");
		}
		buffer.append(" order by f.formState,f.stop_flg,f.updateTime desc");
		return buffer.toString();
	}

	/**  
	 *  
	 * 停用/启用
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public int stopflgFormInfo(final List<String> stopflgList, final int flg) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE T_OA_FORMINFO f SET f.STOP_FLG = ? WHERE f.FORM_STATE = ? AND f.ID IN (:stopflgList)");
		Integer total = (Integer) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(buffer.toString());
				queryObject.setInteger(0, flg).setInteger(1, 0).setParameterList("stopflgList", stopflgList);
				return queryObject.executeUpdate();
			}
		});
		return total;
	}

	/**  
	 *  
	 * 删除
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public int delFormInfo(final List<String> delList) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE T_OA_FORMINFO f SET f.DEL_FLG = ? WHERE f.FORM_STATE = ? AND f.ID IN (:delList)");
		Integer total = (Integer) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(buffer.toString());
				queryObject.setInteger(0, 1).setInteger(1, 0).setParameterList("delList", delList);
				return queryObject.executeUpdate();
			}
		});
		return total;
	}

	/**  
	 *  
	 * 查询code是否重复
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-18 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-18 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public int findFormCode(final String id, final String formCode) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT COUNT(ID) FROM T_OA_FORMINFO f WHERE f.DEL_FLG = ? AND f.FORM_CODE = ?");
		if(StringUtils.isNotBlank(id)){
			buffer.append(" AND f.ID != ?");
		}
		Integer total = (Integer) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(buffer.toString());
				queryObject.setInteger(0, 0).setString(1, formCode);
				if(StringUtils.isNotBlank(id)){
					queryObject.setString(2, id);
				}
				return ((BigDecimal)queryObject.uniqueResult()).intValue();
			}
		});
		return total;
	}
	
	/**  
	 *  
	 * 创建表
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-18 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-18 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Integer createTable(String formName, ArrayList<KeyValVo> keyValueList) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("CREATE TABLE ").append(formName).append(" ( ");
		buffer.append("\"ID\" VARCHAR2(50 BYTE) NOT NULL , ");
		for(KeyValVo vo : keyValueList){
			if("textarea".equals(vo.getCode())){
				buffer.append("\"").append(vo.getName().toUpperCase()).append("\" VARCHAR2(2000 BYTE) NULL , ");
			}
//			else if("datepicker".equals(vo.getCode())){
//				buffer.append("\"").append(vo.getName().toUpperCase()).append("\" DATE NOT NULL , ");
//			}
			else{
				buffer.append("\"").append(vo.getName().toUpperCase()).append("\" VARCHAR2(50 BYTE) NULL , ");
			}
		}
		buffer.append("\"CREATEUSER\" VARCHAR2(50 BYTE) NOT NULL , ");
		buffer.append("\"CREATEDEPT\" VARCHAR2(50 BYTE) NULL , ");
		buffer.append("\"CREATETIME\" DATE NOT NULL , ");
		buffer.append("PRIMARY KEY (\"ID\") ");
		buffer.append(") ");
		buffer.append("PCTFREE 10 INITRANS 1 ");
		buffer.append("STORAGE ( ");
		buffer.append("INITIAL 65536 ");
		buffer.append("NEXT 1048576 ");
		buffer.append("MINEXTENTS 1 ");
		buffer.append("MAXEXTENTS 2147483645 ");
		buffer.append("BUFFER_POOL DEFAULT ");
		buffer.append(") ");
		buffer.append("TABLESPACE \"ZD_OA\" ");
		buffer.append("LOGGING ");
		buffer.append("NOCOMPRESS ");
		buffer.append("NOCACHE ");
		Integer total = (Integer) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(buffer.toString());
				return queryObject.executeUpdate();
			}
		});
		return total;
	}

	/**  
	 *  
	 * 接口-获取可用表单
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<KeyValVo> getValidFormInfo() {
		final String sql = "SELECT f.FORM_CODE code,f.FORM_NAME name FROM T_OA_FORMINFO f WHERE f.DEL_FLG = ? AND f.STOP_FLG = ? ORDER BY f.FORM_STATE,f.UPDATETIME DESC";
		List<KeyValVo> list = (List<KeyValVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql)
						.addScalar("code")
						.addScalar("name");
				queryObject.setInteger(0,0).setInteger(1,0); 
				return queryObject.setResultTransformer(Transformers.aliasToBean(KeyValVo.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	/**  
	 *  
	 * 接口-获取表单主件<br>
	 * code表单编码
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Clob getPartOfFormInfo(final String code) {
		final String sql = "SELECT f.FORM_INFO FROM T_OA_FORMINFO f WHERE f.FORM_CODE = ? AND f.DEL_FLG = ?";
		Clob sections = (Clob)this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql);
				queryObject.setString(0,code).setInteger(1,0); 
				return queryObject.uniqueResult();
			}
		});
		if(sections!=null){
			return sections;
		}
		return null;
	}

	@Override
	public OaFormInfo getFromByCode(String tableCode) {
		String hql="from OaFormInfo t where t.formCode= '"+tableCode+"'";
		List<OaFormInfo> list=this.getSession().createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new OaFormInfo();
	}

}
