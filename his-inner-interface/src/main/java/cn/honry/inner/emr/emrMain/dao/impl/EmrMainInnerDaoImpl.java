package cn.honry.inner.emr.emrMain.dao.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.EmrMain;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.emr.emrMain.dao.EmrMainInnerDao;
@Repository("emrMainInnerDao")
@SuppressWarnings({ "all" })
public class EmrMainInnerDaoImpl extends HibernateEntityDao<EmrMain> implements EmrMainInnerDao {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Override
	public int emrCount(String medicalrecordId, String emrName) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select count(1)");
		sqlBuffer.append(" from t_emr_main t");
		sqlBuffer.append(" left join t_emr_template e on t.temp_id = e.temp_id");
		sqlBuffer.append(" left join t_business_dictionary d on t.emr_type = d.code_encode and d.code_type = 'emrtype'");
		sqlBuffer.append(" where t.del_flg = 0  and t.emr_source = 2 and t.emr_patid = '");
		sqlBuffer.append(medicalrecordId);
		sqlBuffer.append("'");
		if (StringUtils.isNotBlank(emrName)) {
			sqlBuffer.append(" and e.temp_name like '%");
			sqlBuffer.append(emrName);
			sqlBuffer.append("%'");
		}
		Query query = this.getSession().createSQLQuery(sqlBuffer.toString());
		Object count=getSession().createSQLQuery(sqlBuffer.toString()).uniqueResult();
		return Integer.valueOf(count.toString());
	}

	@Override
	public List<EmrMain> emrList(String medicalrecordId, String emrName,
			String name, String rows, String page) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select t.emr_id as id,t.emr_sn as emrSN,t.emr_patid as emrPatId,");
		sqlBuffer.append("e.temp_name as tempName,");
		sqlBuffer.append("d.code_name as typeName");
		sqlBuffer.append(" from t_emr_main t");
		sqlBuffer.append(" left join t_emr_template e on t.temp_id = e.temp_id");
		sqlBuffer.append(" left join t_business_dictionary d on t.emr_type = d.code_encode and d.code_type = 'emrtype'");
		sqlBuffer.append(" where t.del_flg = 0  and t.emr_source = 2 and t.emr_patid = '");
		sqlBuffer.append(medicalrecordId);
		sqlBuffer.append("'");
		if (StringUtils.isNotBlank(emrName)) {
			sqlBuffer.append(" and e.temp_name like '%");
			sqlBuffer.append(emrName);
			sqlBuffer.append("%'");
		}
		Query query = super.getSession().createSQLQuery(sqlBuffer.toString()).addScalar("id").addScalar("emrSN")
				.addScalar("emrPatId").addScalar("tempName").addScalar("typeName");
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		List<EmrMain> list = query.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(EmrMain.class)).list();
		if (list == null || list.size() == 0) {
			return new ArrayList<EmrMain>();
		}
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setPatientName(name);
		}
		return list;
	}
	/**将Clob大数据类型装换成String
	 * @Description: 将Clob大数据类型装换成String
	 * @param clob大数据
	 * @Author: dutianliang
	 * @CreateDate: 2016年5月24日
	 * @Version: V 1.0
	 */
	@Override
	public String ClobToString(Clob clob) {
		String clobStr = "";
		Reader is = null;
		try {
			if(clob != null){
			is = clob.getCharacterStream();
			// 得到流
			BufferedReader br = new BufferedReader(is);
			String s = null;
			s = br.readLine();
			StringBuffer sb = new StringBuffer();
			// 执行循环将字符串全部取出赋值给StringBuffer，由StringBuffer转成String
			while (s != null) {
				sb.append(s);
				s = br.readLine();
			}
			clobStr = sb.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return clobStr;
	}
}
