package cn.honry.inner.system.keyvalue.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysKeyvalue;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.outpatient.medicineList.dao.MedicineListInInterDAO;
import cn.honry.inner.system.keyvalue.dao.KeyvalueInInterDAO;

@Repository("keyvalueInInterDAO")
@SuppressWarnings({ "all" })
public class KeyvalueInInterDAOImpl extends HibernateEntityDao<SysKeyvalue> implements KeyvalueInInterDAO {
	/**
	 * 为父类HibernateDaoSupport注入sessionFactory的值
	 * @author  zpty
	 * @param 参数
	 * @date 2015-05-20 16:00
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	@Qualifier(value = "medicineListInInterDAO")
	private MedicineListInInterDAO medicineListInInterDAO;
	@Override
	public int getVal(String string) {
//		String hql="FROM SysKeyvalue k WHERE k.keyFlag = '"+string+"' ";
//		List<SysKeyvalue> sysKeyvalueList=super.findByObjectProperty(hql, null);
//		if(sysKeyvalueList!=null && sysKeyvalueList.size()>0){
//			String id = sysKeyvalueList.get(0).getId();
//			SysKeyvalue keyvalue = super.get(id);
//			keyvalue.setKeyValue(keyvalue.getKeyValue()+1);
//			super.save(keyvalue);
//			return sysKeyvalueList.get(0).getKeyValue();
//		}else{
//			SysKeyvalue keyvalue = new SysKeyvalue();
//			keyvalue.setId(null);
//			keyvalue.setKeyValue(1);
//			super.save(keyvalue);
//			return 1;
//		}
		//通过序列获取，并未使用查询参数
		String seq = medicineListInInterDAO.getSeqByName("SEQ_REGISTER_CLICNICCODE");
		return Integer.valueOf(seq);
	}

	@Override
	public int getVal(String deptId, String flag,String currentYearMonth) {
		String hql="FROM SysKeyvalue k WHERE k.keyFlag = '"+ deptId +"' and k.keyName='" + flag + "' and k.keyBak1 = '" + currentYearMonth + "'";
		List<SysKeyvalue> sysKeyvalueList = null;
		sysKeyvalueList = this.getSession().createQuery(hql).list();
		if(sysKeyvalueList!=null && sysKeyvalueList.size()>0){
			String id = sysKeyvalueList.get(0).getId();
			SysKeyvalue keyvalue = super.get(id);
			keyvalue.setKeyValue(keyvalue.getKeyValue()+1);
			super.save(keyvalue);
//			super.flush();
			return sysKeyvalueList.get(0).getKeyValue();
		}else{
			SysKeyvalue keyvalue = new SysKeyvalue();
			keyvalue.setId(null);
			keyvalue.setKeyValue(1);
			keyvalue.setKeyFlag(deptId);
			keyvalue.setKeyName(flag);
			keyvalue.setKeyBak1(currentYearMonth);
			super.save(keyvalue);
			super.flush();
			return 1;
		}
	}

	/**
	 * @Description: 根据登录人科室编号 当前时间 和标志“入库单据号”得到完整的值
	 * @param deptId 登录人科室编号
	 * @param flag 标志
	 * @param currentYearMonth 当前时间 
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月13日
	 * @Version: V 1.0
	 */
	@Override
	public String getTotalVal(String deptId, String flag,String currentYearMonth){
		int val = this.getVal(deptId, flag, currentYearMonth);
		String vals = ""+val;
		for(int i = 4 - vals.length(); i > 0; i--){
			vals = "0" + vals;
		}
		return deptId+currentYearMonth + vals;
	}
	
} 
