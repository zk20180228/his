package cn.honry.oa.juris.dao.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
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

import cn.honry.base.bean.model.OaActivitiDept;
import cn.honry.base.bean.model.OaBpmCategory;
import cn.honry.base.bean.model.OaJuris;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.activiti.bpm.process.vo.OaProcessVo;
import cn.honry.oa.juris.dao.JurisDAO;

/** 
 * 
 * @Author：aizhonghua
 * @CreateDate：2018-2-1 下午20:32:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2018-2-1 下午20:32:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("jurisDAO")
@SuppressWarnings({ "all" })
public class JurisDAOImpl extends HibernateEntityDao<OaJuris> implements JurisDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**  
	 * 根据权限id删除流程权限
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void delJuris(String id) {
		String hql = "delete OaJuris s where s.flowCode = ?";
		super.excUpdateHql(hql, id);
	}

	/**  
	 * 根据流程id获取流程权限
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<OaJuris> getJurisByFlowCode(String id) {
		String hql = "from OaJuris s where s.flowCode = ?";
		List<OaJuris> list = super.find(hql, id);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	/**  
	 * 获取有权限的流程id,返回id集合或null
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<String> getJurisListByJobNo(String jobNo) {
		if(StringUtils.isNotBlank(jobNo)){
			try {
				CallableStatement statement = getSession().connection().prepareCall("{Call PRC_GETJURISBYJOBNO(:jobNo,:retval)}");
				statement.setString("jobNo", jobNo);
				statement.registerOutParameter("retval", Types.LONGVARCHAR);
				statement.executeUpdate();  
				String keycode = statement.getString("retval");
				if(keycode!=null){
					String[] ids = keycode.split(",");
					List<String> idList = new ArrayList<String>(Arrays.asList(ids));
					return idList;
				}
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
	
	/**  
	 * 获取有权限的流程分类
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<OaBpmCategory> getCategoryById(final List<String> idList) {
		if(idList!=null&&idList.size()>0){
			final StringBuffer sb = new StringBuffer();
			sb.append("SELECT ");
			sb.append("c.ID id, ");
			sb.append("c.name ");
			sb.append("FROM T_OA_BPM_CATEGORY c ");
			sb.append("WHERE ");
			sb.append("c.ID IN ( ");
			sb.append("SELECT p.CATEGORY_CODE FROM T_OA_BPM_PROCESS p ");
			sb.append("WHERE p.ID IN (:idList) ");
			sb.append("AND p.STOP_FLG = :STOP_FLG AND p.DEL_FLG = :DEL_FLG) ");
			sb.append("ORDER BY c.PRIORITY ");
			List<OaBpmCategory> voList = (List<OaBpmCategory>) this.getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException,SQLException {
					SQLQuery queryObject = session.createSQLQuery(sb.toString())
							.addScalar("id")
							.addScalar("name");
					queryObject.setParameterList("idList", idList).setInteger("STOP_FLG", 0).setInteger("DEL_FLG", 0);
					return queryObject.setResultTransformer(Transformers.aliasToBean(OaBpmCategory.class)).list();
				}
			});
			if(voList!=null&&voList.size()>0){
				return voList;
			}
		}
		return null;
	}

	/**  
	 * 获取有权限的流程科室
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<OaActivitiDept> getActivitiDeptById(final List<String> idList) {
		if(idList!=null&&idList.size()>0){
			final StringBuffer sb = new StringBuffer();
			sb.append("SELECT ");
			sb.append("d.DEPT_CODE deptCode, ");
			sb.append("d.DEPT_NAME deptName ");
			sb.append("FROM T_OA_ACTIVITI_DEPT d ");
			sb.append("WHERE ");
			sb.append("d.DEPT_CODE IN ( ");
			sb.append("SELECT p.DEPT_CODE FROM T_OA_BPM_PROCESS p ");
			sb.append("WHERE p.ID IN (:idList) ");
			sb.append("AND p.STOP_FLG = :STOP_FLG AND p.DEL_FLG = :DEL_FLG) ");
			sb.append("ORDER BY d.DEPT_ORDER ");
			List<OaActivitiDept> voList = (List<OaActivitiDept>) this.getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException,SQLException {
					SQLQuery queryObject = session.createSQLQuery(sb.toString())
							.addScalar("deptCode")
							.addScalar("deptName");
					queryObject.setParameterList("idList", idList).setInteger("STOP_FLG", 0).setInteger("DEL_FLG", 0);
					return queryObject.setResultTransformer(Transformers.aliasToBean(OaActivitiDept.class)).list();
				}
			});
			if(voList!=null&&voList.size()>0){
				return voList;
			}
		}
		return null;
	}

	/**  
	 * 获取有权限的流程列表-分页数据
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<OaProcessVo> getProcessListByJuris(final int page, final int rows, final String name, final String category, final String deptcode,final List<String> idList) {
		if(idList!=null&&idList.size()>0){
			final StringBuffer sb = new StringBuffer();
			sb.append("SELECT p.ID id, ");
			sb.append("p.NAME name, ");
			sb.append("p.DESCN descn, ");
			sb.append("p.CATEGORY_CODE categoryCode, ");
			sb.append("c.NAME categoryName, ");
			sb.append("p.TOP_FLOW topFlow ");
			sb.append(getProcessJurisSql(name,category,deptcode,idList));
			List<OaProcessVo> voList = (List<OaProcessVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException,SQLException {
					SQLQuery queryObject = session.createSQLQuery(sb.toString())
							.addScalar("id")
							.addScalar("name")
							.addScalar("descn")
							.addScalar("categoryCode")
							.addScalar("categoryName")
							.addScalar("topFlow");
					if(StringUtils.isNotBlank(name)){
						queryObject.setString("name", "%"+name+"%");
					}
					if(StringUtils.isNotBlank(category)){
						queryObject.setString("category", category);
					}
					if(StringUtils.isNotBlank(deptcode)){
						queryObject.setString("deptcode", deptcode);
					}
					queryObject.setParameterList("idList", idList);
					return queryObject
							.setFirstResult((page - 1) * rows)
							.setMaxResults(rows)
							.setResultTransformer(Transformers.aliasToBean(OaProcessVo.class)).list();
				}
			});
			if(voList!=null&&voList.size()>0){
				return voList;
			}
		}
		return new ArrayList<OaProcessVo>();
	}

	/**  
	 * 获取有权限的流程列表-总条数
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public int getProcessTotalByJuris(final String name, final String category, final String deptcode,final List<String> idList) {
		if(idList!=null&&idList.size()>0){
			final StringBuffer sb = new StringBuffer();
			sb.append("SELECT COUNT(p.ID) ");
			sb.append(getProcessJurisSql(name,category,deptcode,idList));
			Integer total = (Integer) this.getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException,SQLException {
					SQLQuery queryObject = session.createSQLQuery(sb.toString());
					if(StringUtils.isNotBlank(name)){
						queryObject.setString("name", "%"+name+"%");
					}
					if(StringUtils.isNotBlank(category)){
						queryObject.setString("category", category);
					}
					if(StringUtils.isNotBlank(deptcode)){
						queryObject.setString("deptcode", deptcode);
					}
					queryObject.setParameterList("idList", idList);
					BigDecimal re = (BigDecimal) queryObject.uniqueResult();
					return re.intValue();
				}
			});
			return total;
		}
		return 0;
	}

	/**  
	 * 获取有权限的流程列表-SQL
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	private String getProcessJurisSql(String name, String category, String deptcode, List<String> idList) {
		StringBuffer sb = new StringBuffer();
		sb.append("FROM T_OA_BPM_PROCESS p ");
		sb.append("LEFT JOIN T_OA_BPM_CATEGORY c ");
		sb.append("ON p.CATEGORY_CODE = c.ID ");
		sb.append("WHERE p.STOP_FLG = 0 AND p.DEL_FLG = 0 ");
		if(StringUtils.isNotBlank(name)){
			sb.append(" AND p.NAME LIKE :name ");
		}
		if(StringUtils.isNotBlank(category)){
			sb.append(" AND p.CATEGORY_CODE = :category ");
		}
		if(StringUtils.isNotBlank(deptcode)){
			sb.append(" AND p.DEPT_CODE = :deptcode ");
		}
		sb.append(" AND p.ID in (:idList) ");
		return sb.toString();
	}

}
