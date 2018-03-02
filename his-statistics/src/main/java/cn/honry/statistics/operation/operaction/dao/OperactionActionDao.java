package cn.honry.statistics.operation.operaction.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.OperationApply;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.operation.operationDetails.vo.OperationDetailsVo;

@SuppressWarnings({"all"})
public interface OperactionActionDao extends EntityDao<OperationApply>{


	/**  
	 * 
	 * 手术耗材统计查询
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:deptId 科室code
	 * @param:repno 记账编号
	 * @throws:
	 * @return: void
	 *
	 */
	List<InpatientItemListNow> getOperactionlist(String login,String end,Double price,String repno,String deptId,String page,String rows);

	/**  
	 * 
	 * 手术耗材统计明细List
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:repno 记账编码
	 * @param:deptId 科室
	 * @param:identityCard 身份证号
	 * @param:page页数
	 * @param:rows行数
	 * @throws:
	 * @return: void
	 *
	 */
	List<InpatientItemListNow> getoperationDetailsList(String login, String end,
			Double price, String repno,String page,String rows, String identityCard);

	
	/**  
	 * 
	 * 手术耗材统计明细List(总条数)
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:repno 记账编码
	 * @param:deptId 科室
	 * @param:identityCard 身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	int getoperationDetailsTotal(String login, String end, Double price,
			String repno, String identityCard);


	/**  
	 * 
	 * 导出手术耗材统计明细List
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:repno 记账编码
	 * @param:deptId 科室
	 * @param:identityCard 身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	List<InpatientItemListNow> queryInvLogExpoper(String login, String end,
			Double price, String repno, String identityCard);

	/**  
	 * 
	 * 手术耗材统计查询（总条数）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:deptId 科室code
	 * @param:repno 记账编号
	 * @throws:
	 * @return: void
	 *
	 */
	int getgetOperationListTotal(String login, String end, Double price,
			String repno, String deptId);

	/**  
	 * 
	 * 导出手术耗材统计List
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:repno 记账编码
	 * @param:deptId 科室
	 * @param:identityCard 身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	List<InpatientItemListNow> queryInvLogExp(String login, String end,
			Double price, String repno, String string);
	/**  
	 * 
	 * 手术耗材统计明细(打印)-20170316 hedong 报表打印 window.open采用post方式提交参数示例
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:repno 记账编码
	 * @param:deptId 科室
	 * @param:identityCard 身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	List<OperationDetailsVo> queryInvLogDetails(String login, String end, Double price, String repno, String identityCard);

	

}
