package cn.honry.statistics.operation.operaction.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.OperationApply;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.operation.operationDetails.vo.OperationDetailsVo;
import cn.honry.utils.FileUtil;
@SuppressWarnings({"all"})
public interface OperactionActionService extends BaseService<OperationApply>{
	/**  
	 * 
	 * 导出手术耗材统计
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
			Double price, String repno, String deptId,String page,String rows, String identityCard);

	/**
	 * @Description:导出 
	 * @Author： zhangjin @CreateDate： 2016-6-24
	 * @param @throws Exception
	 * @return void
	 * @version 1.0
	 **/
	List<InpatientItemListNow> queryInvLogExp(String login, String end, Double price,
			String repno);
	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: zhangjin
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	**/
	FileUtil export(List<InpatientItemListNow> list, FileUtil fUtil);


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
	List<InpatientItemListNow> queryInvLogExpoper(String login, String end, Double price,
			String repno, String identityCard);
	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: zhangjin
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	**/
	byte[] exportoper(List<InpatientItemListNow> list);
	
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
			String repno, String deptId, String identityCard);

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
	int getOperationListTotal(String login, String end, Double price,
			String repno, String deptId);

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
