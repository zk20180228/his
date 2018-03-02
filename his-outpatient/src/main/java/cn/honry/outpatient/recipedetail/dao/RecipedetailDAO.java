package cn.honry.outpatient.recipedetail.dao;

import java.util.List;

import cn.honry.base.bean.model.OutpatientRecipedetail;
import cn.honry.base.dao.EntityDao;
import cn.honry.outpatient.recipedetail.vo.KeyValueVo;

@SuppressWarnings({"all"})
public interface RecipedetailDAO extends EntityDao<OutpatientRecipedetail>{
		
	
	/**
	 * 获取历史医嘱信息
	 * @author  lhl
	 * @version 1.0
	 * @return 列表
	 */
    List query(String entity);
    /**
	 * 获取记录条数
	 * @param entity 查询条件封装实体类
	 * @author  
	 * @version 1.0
	 * @return
	 */
	int getTotal(OutpatientRecipedetail entity);
	/**
	 * 获取历史医嘱时间信息
	 * @author  lhl
	 * @version 1.0
	 * @return 列表
	 */
    List queryDate();
    /**  
	 *  
	 * @Description： 获得医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<OutpatientRecipedetail> getAdviceListByIds(String id);
	
	
	/**  
	 *  
	 * @Description： 根据门诊号和病历号查询所有的处方号
	 * @Author：wanxing
	 * @CreateDate：2016-03-09 下午18:20:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<String> findRecipeNo(String clinicCode, String patientNo);
	
	/**  
	 *  
	 * @Description： 根据就诊卡号获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	KeyValueVo queryPatient(String string);
	
	/**  
	 *  
	 * @Description：  根据看诊号获得就诊卡号
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	String getIdcardNoByRegisterNo(String string);
}
