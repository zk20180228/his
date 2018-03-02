package cn.honry.statistics.operation.operaction.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.OperationApply;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.statistics.operation.operaction.dao.OperactionActionDao;
import cn.honry.statistics.operation.operaction.service.OperactionActionService;
import cn.honry.statistics.operation.operationDetails.vo.OperationDetailsVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;

@Service("operactionActionService")
@Transactional
@SuppressWarnings({ "all" })
public class OperactionActionServiceImpl implements OperactionActionService{
	
	@Autowired
	@Qualifier(value="operactionActionDao")
	private OperactionActionDao operactionActionDao;
	
	@Autowired
	@Qualifier(value="innerCodeService")
	private CodeInInterService innerCodeService;
	
	@Override
	public OperationApply get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(OperationApply arg0) {
		
	}
	

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
	@Override
	public List<InpatientItemListNow> getOperactionlist(String login,String end,Double price,String repno,String deptId,String page,String rows) {
		return operactionActionDao.getOperactionlist(login,end,price,repno,deptId,page,rows);
	}

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
	@Override
	public List<InpatientItemListNow> getoperationDetailsList(String login,
			String end, Double price, String repno, String deptId,String page,String rows, String identityCard) {
		return operactionActionDao.getoperationDetailsList(login,end,price,repno,page,rows, identityCard);
	}

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
	@Override
	public List<InpatientItemListNow> queryInvLogExp(String login, String end,
			Double price, String repno) {
		return operactionActionDao.queryInvLogExp(login, end, price,repno,"");
	}
	
	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: zhangjin
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	**/
	@Override
	public FileUtil export(List<InpatientItemListNow> list, FileUtil fUtil) {
		for (InpatientItemListNow model : list) {
			String record="";
				record = CommonStringUtils.trimToEmpty(model.getUndrugGbcode()) + ",";
				record += CommonStringUtils.trimToEmpty(model.getItemName()) + ",";
				record += model.getUnitPrice() + ",";
				record += model.getQty() + ",";
				record += model.getTotCost() ;
				try {
					fUtil.write(record);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return fUtil;
	}


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
	@Override
	public List<InpatientItemListNow> queryInvLogExpoper(String login, String end,
			Double price, String repno, String identityCard) {
		return operactionActionDao.queryInvLogExpoper(login,end,price,repno, identityCard);
	}
	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: zhangjin
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	**/
	@Override
	public byte[] exportoper(List<InpatientItemListNow> list) {
		//字段名信息
		String[] headMessage = { "开立科室","开立医生","病历号","患者姓名","记帐编号", "项目名称","规格型号", "单价","单位" ,"数量","合计金额","记帐时间","操作员" };
		String[][] values = new String[list.size() + 1][13];
		values[0] = headMessage;
		int index = 1;
		//数据信息
		for (InpatientItemListNow model : list) {
			values[index][0] = CommonStringUtils.trimToEmpty(model.getRecipeDeptcode());
			values[index][1] = CommonStringUtils.trimToEmpty(model.getRecipeDoccode()) ;
			values[index][2] = CommonStringUtils.trimToEmpty(model.getInpatientNo());
			values[index][3] = CommonStringUtils.trimToEmpty(model.getName());
			values[index][4] = CommonStringUtils.trimToEmpty(model.getUndrugGbcode());
			values[index][5] = CommonStringUtils.trimToEmpty(model.getItemName());
			values[index][6] = "";
			values[index][7] = model.getUnitPrice() + "";
			values[index][8] = CommonStringUtils.trimToEmpty(model.getCurrentUnit());
			values[index][9] = model.getQty() + "";
			values[index][10] = model.getTotCost() + "";
			values[index][11] = DateUtils.formatDateY_M_D_H_M_S(model.getFeeDate());
			values[index][12] = CommonStringUtils.trimToEmpty(model.getFeeOpercode());
			index++;
		}
		//调用接口生成xls文件字节流
		return innerCodeService.exportExcel(values);
	}

	
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
	@Override
	public int getoperationDetailsTotal(String login, String end, Double price,
			String repno, String deptId, String identityCard) {
		return operactionActionDao.getoperationDetailsTotal(login,end,price,repno, identityCard);
	}

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
	@Override
	public int getOperationListTotal(String login, String end, Double price,
			String repno, String deptId) {
		return operactionActionDao.getgetOperationListTotal(login,end,price,repno,deptId);
	}

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
	@Override
	public List<OperationDetailsVo> queryInvLogDetails(String login, String end, Double price, String repno, String identityCard) {
		return operactionActionDao.queryInvLogDetails(login,end,price,repno,identityCard);
	}

	
	
}
