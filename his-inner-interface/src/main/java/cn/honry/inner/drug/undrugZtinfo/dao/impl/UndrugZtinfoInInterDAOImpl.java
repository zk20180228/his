package cn.honry.inner.drug.undrugZtinfo.dao.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.UndrugZtinfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.drug.undrug.dao.UndrugInInterDAO;
import cn.honry.inner.drug.undrugZtinfo.dao.UndrugZtinfoInInterDAO;
import cn.honry.inner.vo.MedicalVo;

/**  
 *  
 * @className：UndrugZtinfoInInterDAOImpl
 * @Description：  复合项目
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("undrugZtinfoInInterDAO")
@SuppressWarnings({ "all" })
public class UndrugZtinfoInInterDAOImpl extends HibernateEntityDao<UndrugZtinfo> implements UndrugZtinfoInInterDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Autowired
	@Qualifier(value = "undrugInInterDAO")
	private UndrugInInterDAO undrugInInterDAO;
	
	@Override
	public List<DrugUndrug> getUndrugZtinfoByPackageCode(String code) {
		String hql = "FROM DrugUndrug u WHERE u.code IN (SELECT z.itemCode FROM UndrugZtinfo z WHERE z.stop_flg = 0 AND z.del_flg = 0 AND packageCode = '"+code+"')";
		return undrugInInterDAO.findByObjectProperty(hql, null);
	}

	@Override
	public List<MedicalVo> queryMedicalVoByCode(final String code) {
		final StringBuffer sb = new StringBuffer();
		sb.append("SELECT m.FEE_STAT_CODE AS feeStatCode ,m.FEE_STAT_NAME AS feeStatName,u.UNDRUG_DEFAULTPRICE AS adPriceSum FROM T_DRUG_UNDRUG u ");
		sb.append("LEFT JOIN T_CHARGE_MINFEETOSTAT m ON m.MINFEE_CODE = u.UNDRUG_MINIMUMCOST ");
		sb.append("WHERE u.STOP_FLG = 0 AND u.DEL_FLG = 0 AND u.UNDRUG_CODE IN ( ");
		sb.append("SELECT z.ITEM_CODE FROM T_UNDRUG_ZTINFO z WHERE z.STOP_FLG = 0 AND z.DEL_FLG = 0 ");
		sb.append("AND m.STOP_FLG = 0 AND m.DEL_FLG = 0 AND m.REPORT_CODE = 'MZ01'AND z.PACKAGE_CODE = ?)");
		List<MedicalVo> voList = (List<MedicalVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString())
						.addScalar("feeStatCode")
						.addScalar("feeStatName")
						.addScalar("adPriceSum",Hibernate.DOUBLE);
				return queryObject.setString(0, code).setResultTransformer(Transformers.aliasToBean(MedicalVo.class)).list();
			}
		});
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		return null;
	}

	/**  
	 * 
	 * <p> 计算复合项目明细总额  </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月9日 下午3:22:53 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月9日 下午3:22:53 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:packCode复合项目code
	 * @return: void
	 *
	 */
	@Override
	public Double queryZtinfoTotalByCode(final String packCode) {
		final StringBuffer sb = new StringBuffer();
		sb.append("SELECT SUM(NVL(u.UNDRUG_DEFAULTPRICE,0)*NVL(z.QTY,1)) FROM T_UNDRUG_ZTINFO z ");
		sb.append("INNER JOIN T_DRUG_UNDRUG u ON u.UNDRUG_CODE = z.ITEM_CODE ");
		sb.append("WHERE z.PACKAGE_CODE = ? AND z.DEL_FLG = 0 AND z.STOP_FLG = 0 AND u.DEL_FLG = 0 AND u.STOP_FLG = 0");
		Object total = this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString());
				return queryObject.setString(0, packCode).uniqueResult();
			}
		});
		if(total!=null){
			return Double.valueOf(total.toString());
		}
		return null;
	}
	
}
