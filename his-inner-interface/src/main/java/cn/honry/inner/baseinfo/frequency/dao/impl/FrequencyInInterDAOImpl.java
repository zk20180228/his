package cn.honry.inner.baseinfo.frequency.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.frequency.dao.FrequencyInInterDAO;
import cn.honry.utils.SessionUtils;

@Repository("frequencyInInterDAO")
@SuppressWarnings({ "all" })
public class FrequencyInInterDAOImpl extends HibernateEntityDao<BusinessFrequency> implements FrequencyInInterDAO {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public BusinessFrequency getCode(String drugFrequency) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from BusinessFrequency b where b.del_flg=0 and b.stop_flg=0 and b.encode='"+drugFrequency+"'");
		List<BusinessFrequency> list=super.findByObjectProperty(hql.toString(), null);
		if(list.size()>0){
			return list.get(0);
		}
		return new BusinessFrequency();
	}

	@Override
	public List<BusinessFrequency> queryFrequencyList() {
		String hql = "from BusinessFrequency t where t.stop_flg = 0 and t.del_flg = 0";
		List<BusinessFrequency> frequencyList=super.findByObjectProperty(hql.toString(), null);
		if(frequencyList!=null && frequencyList.size()>0){
			return frequencyList;
		}		
		return new ArrayList<BusinessFrequency>();
	}

	/**  
	 * 
	 * <p> 根据编码及开立科室获取频次信息 </p>
	 * <p> 如果该科室下无对应编码的信息则返回全院频次对应编码的频次信息 </p>
	 * <p> 如果以上都不存在则返回null </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年10月27日 上午10:31:32 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年10月27日 上午10:31:32 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public BusinessFrequency getCodeAndDept(String frequencyCode,String deptCode) {
		if(StringUtils.isBlank(frequencyCode)||StringUtils.isBlank(deptCode)){
			return null;
		}
		String hql = "from BusinessFrequency b where b.del_flg=0 and b.stop_flg=0 and b.encode='"+frequencyCode+"' and b.createDept='"+deptCode+"'";
		BusinessFrequency frequency = (BusinessFrequency) super.excHqlGetUniqueness(hql, null);
		if(frequency!=null&&StringUtils.isNotBlank(frequency.getId())){
			return frequency;
		}
		String thql = "from BusinessFrequency b where b.del_flg=0 and b.stop_flg=0 and b.encode='"+frequencyCode+"' and  b.createDept is null";
		BusinessFrequency freq = (BusinessFrequency) super.excHqlGetUniqueness(thql, null);
		if(freq!=null&&StringUtils.isNotBlank(freq.getId())){
			return freq;
		}
		String thq2 = "from BusinessFrequency b where b.del_flg=0 and b.stop_flg=0 and b.encode='"+frequencyCode+"' and  b.createDept = 'ROOT'";
		BusinessFrequency freq2 = (BusinessFrequency) super.excHqlGetUniqueness(thq2, null);
		if(freq2!=null&&StringUtils.isNotBlank(freq2.getId())){
			return freq2;
		}
		return null;
	}
}
