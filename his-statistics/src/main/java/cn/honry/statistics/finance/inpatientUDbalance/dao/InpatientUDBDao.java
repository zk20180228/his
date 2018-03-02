package cn.honry.statistics.finance.inpatientUDbalance.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.finance.inpatientUDbalance.vo.InpatientUDBVo;

@SuppressWarnings({"all"})
public interface InpatientUDBDao  extends EntityDao<InpatientUDBVo>{
	

	/**
	 * 
	 *<p> 收款员列表</p>
	 * @Author: 
	 * @CreateDate: 2017年7月4日 下午5:42:43 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月4日 下午5:42:43 
	 * @ModifyRmk:  添加注释
	 * @version: V1.0
	 * @param date1 开始时间：yyyy-MM-dd
	 * @param date2 结束时间：yyyy-MM-dd
	 * @return List<InpatientUDBVo> 收款员列表集合
	 * @throws:
	 *
	 */
	List<InpatientUDBVo> queryDateInfo(String date1, String date2);

}
