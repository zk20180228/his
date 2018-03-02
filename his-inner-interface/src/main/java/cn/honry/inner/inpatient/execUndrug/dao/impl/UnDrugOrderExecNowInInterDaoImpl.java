package cn.honry.inner.inpatient.execUndrug.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientExecundrugNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.inpatient.execUndrug.dao.UnDrugOrderExecNowInInterDao;
import cn.honry.utils.ShiroSessionUtils;


@Repository("unDrugOrderExecNowInInterDao")
@SuppressWarnings({"all"})
public class UnDrugOrderExecNowInInterDaoImpl extends HibernateEntityDao<InpatientExecundrugNow> implements UnDrugOrderExecNowInInterDao {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**  
	 *  
	 * @Description：  分页查询 - 获得信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-3-9 下午16:56:31   
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-3-9 下午16:56:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<InpatientExecundrugNow> getPageInfo(String hql, String page,String rows) {
		return super.getPage(hql, page, rows);
	}

	/**  
	 *  
	 * @Description：  分页查询 - 获得总条数
	 * @Author：aizhonghua
	 * @CreateDate：2016-3-9 下午16:56:31   
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-3-9 下午16:56:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public int getTotalInfo(String hql) {
		return super.getTotal(hql);
	}
	
/**
	 * 获得患者住院号和需要发送的执行记录id的集合
	 * @Author：aizhonghua
	 * @CreateDate：2016年5月25日 上午9:02:02 
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016年5月25日 上午9:02:02 
	 * @ModifyRmk：
	 * @version： 1.0：
	 * @param：orderIds执行记录id
	 * @param：patNoData患者住院流水号
	 *
	 */
	@Override
	public List<InpatientExecundrugNow> getUnDrugListByIdsAndPatNo(String orderIds,String patNoData) {
		if(StringUtils.isBlank(orderIds)&&StringUtils.isBlank(patNoData)){
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("from InpatientExecundrugNow e ");
		buffer.append("WHERE ");
		if(StringUtils.isNotBlank(orderIds)){
			orderIds = orderIds.replaceAll(",", "','");
			buffer.append("e.id IN ('").append(orderIds).append("') ");
		}
		if(StringUtils.isNotBlank(patNoData)){
			if(StringUtils.isNotBlank(orderIds)){
				buffer.append("OR ");
			}
			patNoData = patNoData.replaceAll(",", "','");
			SysDepartment nurDept = ShiroSessionUtils.getCurrentUserLoginNursingStationShiroSession();
			buffer.append("e.inpatientNo IN ('").append(patNoData).append("') AND e.nurseCellCode = '").append(nurDept.getDeptCode()).append("' AND e.validFlag = 1 AND e.execFlag = 0 AND e.del_flg = 0 AND e.stop_flg = 0");
		}
		List<InpatientExecundrugNow> list = this.find(buffer.toString(), null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
}
