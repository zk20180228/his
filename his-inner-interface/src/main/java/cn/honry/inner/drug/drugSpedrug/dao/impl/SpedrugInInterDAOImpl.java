package cn.honry.inner.drug.drugSpedrug.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugSpedrug;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.drug.drugSpedrug.dao.SpedrugInInterDAO;
import freemarker.template.utility.StringUtil;

/**  
 *  
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
@Repository("spedrugInInterDAO")
public class SpedrugInInterDAOImpl extends HibernateEntityDao<DrugSpedrug> implements SpedrugInInterDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<DrugSpedrug> findDrugSpedrug(String no,String ifsp) {
		String hql=" from DrugSpedrug where clinicCode = '"+no+"'  ";
		if(StringUtils.isNotBlank(ifsp)){
			if("2".equals(ifsp)){
				hql = hql+" and applicState = 2 ";
			}else{
				hql = hql+" and (applicState <> 2 or applicState is null) ";
			}
			
		}
		List<DrugSpedrug> spedrugList = super.find(hql, null);
		if(spedrugList!=null&&spedrugList.size()>0){
			return spedrugList;
		}
		return new ArrayList<DrugSpedrug>();
	}

	/**  
	 *  
	 * @Description： 查询特限药申请
	 * @Author：aizhonghua
	 * @CreateDate：2017-03-01 上午11:50:53  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-03-01 上午11:50:53  
	 * @ModifyRmk：  clinicNo门诊号code药品编码
	 * @version 1.0
	 *
	 */
	@Override
	public DrugSpedrug querySpeDrugApply(String clinicNo, String code) {
		String hql = "from DrugSpedrug where clinicCode = ? and drugCode = ? and stop_flg = 0 and del_flg = 0";
		List<DrugSpedrug> spedrugList = super.find(hql, clinicNo,code);
		if(spedrugList!=null&&spedrugList.size()>0){
			return spedrugList.get(0);
		}
		return null;
	}

	/**  
	 *  
	 * @Description： 查询特限药申请组套
	 * @Author：aizhonghua
	 * @CreateDate：2017-03-01 上午11:50:53  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-03-01 上午11:50:53  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<DrugSpedrug> querySpeDrugApplyStack(String clinicNo, String para) {
		para = para.replaceAll(",", "','");
		String hql = "from DrugSpedrug where clinicCode = ? and drugCode in ('"+para+"') and stop_flg = 0 and del_flg = 0";
		List<DrugSpedrug> spedrugList = super.find(hql, clinicNo);
		if(spedrugList!=null&&spedrugList.size()>0){
			return spedrugList;
		}
		return null;
	}

	
}
