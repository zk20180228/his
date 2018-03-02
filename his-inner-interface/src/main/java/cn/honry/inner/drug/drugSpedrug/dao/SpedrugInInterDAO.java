package cn.honry.inner.drug.drugSpedrug.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugSpedrug;
import cn.honry.base.dao.EntityDao;

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
public interface SpedrugInInterDAO extends EntityDao<DrugSpedrug>{

	/**  
	 *  
	 * @Description：   根据患者住院号查询出患者申请的特殊药品
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-6 上午10:45:42  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-6 上午10:45:42  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<DrugSpedrug> findDrugSpedrug(String no,String ifsp);

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
	DrugSpedrug querySpeDrugApply(String clinicNo, String code);

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
	List<DrugSpedrug> querySpeDrugApplyStack(String clinicNo, String para);
}
