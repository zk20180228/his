package cn.honry.outpatient.feedetail.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.OutpatientRecipedetail;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.feedetail.dao.FeedetailDAO;
import cn.honry.utils.DateUtils;

@Repository("feedetailDAO")
@SuppressWarnings({ "all" })
public class FeedetailDAOImpl extends HibernateEntityDao<OutpatientFeedetailNow> implements FeedetailDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<OutpatientRecipedetail> queryRecipedetailByCaseNo(String no) {
		String hql= " from OutpatientRecipedetail where clinicCode = '"+no+"' and del_flg=0 and stop_flg=0 and to_char(operDate,'yyyy-MM-dd') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and status=0 and chargeFlag = 0 ";
		List<OutpatientRecipedetail> recipedetailList = super.find(hql, null);
		if(recipedetailList==null||recipedetailList.size()<=0){
			return new ArrayList<OutpatientRecipedetail>();
		}
		return recipedetailList;
	}

	@Override
	public OutpatientFeedetail queryOutpatientFeedetail(String moOrder,String itemId) {
		String hql= " from  OutpatientFeedetail  where itemCode= '"+itemId+"' and moOrder='"+moOrder+"' and extFlag1=0";
		List<OutpatientFeedetail> list = super.find(hql, null);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	/**  
	 *  
	 * @Description： 获得处方收费明细
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-7 下午08:24:25  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-7 下午08:24:25  
	 * @ModifyRmk：  
	 * @param:recipeNo处方号; sequencenNo处方流水号; recipeSeq处方内流水号 ; seeNo看诊序号; clinicCode门诊号
	 * @version 1.0
	 *
	 */
	@Override
	public OutpatientFeedetailNow getFeeByRecipeAndSequence(String recipeNo,String sequencenNo,Integer recipeSeq,String clinicCode) {
		String hql= " FROM OutpatientFeedetailNow f WHERE f.recipeNo = '"+recipeNo+"' AND f.moOrder = '"+sequencenNo+"' AND f.sequenceNo = "+recipeSeq+" AND f.clinicCode = '"+clinicCode+"'";
		List<OutpatientFeedetailNow> list = super.find(hql, null);
		if(list!=null&&list.size()==1){
			return list.get(0);
		}
		return null;
	}
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
	@Override
	public List<OutpatientFeedetailNow> getFeeListByRecipeNoAndFeeId(String recipeNo, String id) {
		String hql= " FROM OutpatientFeedetailNow f WHERE f.recipeNo = '"+recipeNo+"' AND f.extendTwo = '"+id+"' AND f.subjobFlag = 1";
		List<OutpatientFeedetailNow> list = super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
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
	@Override
	public OutpatientFeedetail getFeeByRecipeNoAndFeeId(String recipeNo,String id) {
		String hql= " FROM OutpatientFeedetail f WHERE f.recipeNo = '"+recipeNo+"' AND f.extendOne = '"+id+"' AND f.subjobFlag = 2";
		List<OutpatientFeedetail> list = super.find(hql, null);
		if(list!=null&&list.size()==1){
			return list.get(0);
		}
		return null;
	}
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
	@Override
	public void delByAdviceIds(String id, String userId) {
		super.del(id, userId);
	}
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
	@Override
	public List<OutpatientFeedetailNow> getFeedListByIds(String id) {
		id = id.replaceAll(",", "','");
		String hql= " FROM OutpatientFeedetailNow f WHERE f.extendTwo IN ('"+id+"')";
		List<OutpatientFeedetailNow> list = super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

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
	@Override
	public OutpatientFeedetailNow getFeeChimedByRecipeNo(String recipeNo,String combNo) {
		String hql= "FROM OutpatientFeedetailNow f WHERE f.recipeNo = ? AND f.combNo = ? AND f.sequenceNo = 0 AND f.extendTwo IS NULL";
		List<OutpatientFeedetailNow> list = super.find(hql, recipeNo,combNo);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

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
	@Override
	public List<OutpatientFeedetailNow> getgFeeListByRecipeAndSequence(String itemCode, String recipeNo, String sequencenNo,Integer recipeSeq, String clinicCode) {
		String hql= " FROM OutpatientFeedetailNow f WHERE f.packageCode = '"+itemCode+"' AND f.recipeNo = '"+recipeNo+"' AND f.moOrder = '"+sequencenNo+"' AND f.sequenceNo = '"+recipeSeq+"' AND f.clinicCode = '"+clinicCode+"'";
		List<OutpatientFeedetailNow> list = super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public List<OutpatientFeedetailNow> queryFeeByIdAndSequencenNo(String id,String sequencenNo) {
		String hql= " FROM OutpatientFeedetailNow f WHERE f.extendTwo = '"+id+"' OR f.extendOne = '"+sequencenNo+"'";
		List<OutpatientFeedetailNow> list = super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	

}
