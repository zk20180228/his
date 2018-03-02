package cn.honry.outpatient.feedetail.dao;

import java.util.List;

import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.OutpatientRecipedetail;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface FeedetailDAO extends EntityDao<OutpatientFeedetailNow>{

	/**  
	 *  
	 * @Description： 根据病历号查询医嘱
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-7 下午08:24:25  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-7 下午08:24:25  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<OutpatientRecipedetail> queryRecipedetailByCaseNo(String no);
	/**  
	 *  
	 * @Description： 根据医嘱流水号，项目编号，正常状态查询处方收费明细
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-7 下午08:24:25  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-7 下午08:24:25  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	OutpatientFeedetail queryOutpatientFeedetail(String moOrder,String itemId);
	/**  
	 *  
	 * @Description： 获得处方收费明细
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-7 下午08:24:25  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-7 下午08:24:25  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	OutpatientFeedetailNow getFeeByRecipeAndSequence(String recipeNo,String sequencenNo,Integer recipeSeq,String clinicCode);
	/**  
	 *  
	 * @Description： 获得处方收费明细附材信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-7 下午08:24:25  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-7 下午08:24:25  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<OutpatientFeedetailNow> getFeeListByRecipeNoAndFeeId(String recipeNo,String id);
	/**  
	 *  
	 * @Description： 获得处方收费明细煎药信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-7 下午08:24:25  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-7 下午08:24:25  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	OutpatientFeedetail getFeeByRecipeNoAndFeeId(String recipeNo, String id);
	/**  
	 *  
	 * @Description： 根据处方id删除关联收费信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-7 下午08:24:25  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-7 下午08:24:25  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void delByAdviceIds(String id, String userId);
	/**  
	 *  
	 * @Description： 获得全部收费信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-7 下午08:24:25  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-7 下午08:24:25  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<OutpatientFeedetailNow> getFeedListByIds(String id);
	
	/**
	 * 
	 * 原有煎药信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月26日 下午4:02:11 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param recipeNo
	 * @return：
	 *
	 */
	OutpatientFeedetailNow getFeeChimedByRecipeNo(String recipeNo,String combNo);
	
	/**  
	 * 
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月9日 上午11:11:08 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月9日 上午11:11:08 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<OutpatientFeedetail>
	 *
	 */
	List<OutpatientFeedetailNow> getgFeeListByRecipeAndSequence(String itemCode,String recipeNo, String sequencenNo, Integer recipeSeq,String clinicCode);
	
	/**  
	 * 
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月9日 上午11:11:08 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月9日 上午11:11:08 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<OutpatientFeedetail>
	 *
	 */
	List<OutpatientFeedetailNow> queryFeeByIdAndSequencenNo(String id,String sequencenNo);
}
