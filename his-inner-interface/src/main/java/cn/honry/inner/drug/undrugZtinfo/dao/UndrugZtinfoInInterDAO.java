package cn.honry.inner.drug.undrugZtinfo.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.UndrugZtinfo;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.MedicalVo;

/**  
 *  
 * @className：UndrugZtinfoInInterDAO
 * @Description：  复合项目
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface UndrugZtinfoInInterDAO extends EntityDao<UndrugZtinfo>{

	/**  
	 * 
	 * <p> 查询复合项目明细信息  </p>
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
	List<DrugUndrug> getUndrugZtinfoByPackageCode(String code);

	/**  
	 * 
	 * <p> 查询复合项目明细信息用于进行费用分类  </p>
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
	List<MedicalVo> queryMedicalVoByCode(String code);

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
	Double queryZtinfoTotalByCode(String packCode);

}
