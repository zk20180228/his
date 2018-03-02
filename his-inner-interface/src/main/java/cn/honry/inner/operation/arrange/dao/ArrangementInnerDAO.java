package cn.honry.inner.operation.arrange.dao;

import java.util.List;

import cn.honry.base.bean.model.OperationArrange;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.operation.record.vo.OperationUserVo;
@SuppressWarnings({"all"})
public interface ArrangementInnerDAO extends EntityDao<OperationArrange>{

	
	/**
	 * @Description:通过手术序号得到人员信息
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月15日
	 * @param:id-手术序号
	 * @param:type-要查询的类型，巡回，洗手，助手医生，临时助手等
	 * @return:List<OperationUserVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<OperationUserVo> getOperationUserList(String id,String type,String fore);
}
