package cn.honry.inner.operation.record.dao;

import java.util.List;

import cn.honry.base.bean.model.OperAtionRecord;
import cn.honry.base.bean.model.OperationArrange;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.operation.record.vo.OpNameVo;
import cn.honry.inner.operation.record.vo.OperationUserVo;

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
public interface RecordInInterDAO extends EntityDao<OperAtionRecord>{
	
	/**
	 * @Description:通过手术序号得到人员信息
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月15日
	 * @param:id-手术序号
	 * @param:type-要查询的类型，巡回，洗手，助手医生，临时助手等
	 * @return:List<OperationUserVo>
	 * @Modifier:zhangjin
	 * @ModifyDate:2016-05-20
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<OperationArrange> getOperationUserList(String id,String type);
	
	/**
	 * @Description:通过手术序号得到人员信息
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月15日
	 * @param:id-手术序号
	 * @param:fore-手术状态
	 * @param:type-要查询的类型，巡回，洗手，助手医生，临时助手等
	 * @return:List<OperationUserVo>
	 * @Modifier:zhangjin
	 * @ModifyDate:2016-05-20
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<OperationUserVo> getOperationUserCancelList(String id,String type,String fore);
	
	/**
	 * @Description:通过手术序号查询手术名称
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年4月15日
	 * @param:id-手术序号
	 * @return:List<OpNameVo>手术名称集合list
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<OpNameVo> getOpNameVoCancleList(String id);
	
}
