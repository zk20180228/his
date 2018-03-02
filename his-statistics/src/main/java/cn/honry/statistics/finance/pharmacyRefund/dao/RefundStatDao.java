package cn.honry.statistics.finance.pharmacyRefund.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientCancelitem;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.finance.pharmacyRefund.vo.RefundVo;

@SuppressWarnings({"all"})
public interface RefundStatDao extends EntityDao<InpatientCancelitem>{
	
	/***
	 * 退费检索
	 * @Title: query 
	 * @author  WFJ
	 * @createDate ：2016年6月27日
	 * @param feeStatCode 检索费用类别
	 * @param beginDate 检索起始日期
	 * @param endDate 检索终止日期
	 * @return List<RefundVO> vo
	 * @version 1.0
	 */
	List<RefundVo> query(List<String> invoiceInfoPartName,List<String> cancelPartName,String feeStatCode,String beginDate,String endDate,String page,String rows);
	
	/***
	 * 
	 * @Title: queryTotal 
	 * @author  WFJ
	 * @createDate ：2016年6月28日
	 * @param feeStatCode
	 * @param beginDate
	 * @param endDate
	 * @return int
	 * @version 1.0
	 */
	int queryTotal(List<String> invoiceInfoPartName,List<String> cancelPartName,String feeStatCode,String beginDate,String endDate);
	
	/***
	 * 门诊药房退费查询表打印
	 */
	List<RefundVo> queryReport(List<String> invoiceInfoPartName,List<String> cancelPartName,String feeStatCode,String beginDate,String endDate);
	
	/**
	 * 
	 * @Description:将门诊药房退费统计数据从mongodb中读出
	 * @param feeStatCode 药品类别(发票科目类型)
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 * List<RefundVO>
	 * @exception:
	 * @author:zhangkui
	 * @time:2017年5月13日 上午10:38:04
	 */
	public List<RefundVo> queryByMongo(String feeStatCode, String beginDate, String endDate,String page,String rows) throws Exception;

	/**
	 * 
	 * @Description:根据条件,在mongodb中查询总数量
	 * @param feeStatCode 药品种类
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 * int 总记录数
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年5月13日 下午3:01:42
	 */
	public int queryByMongoCount(String feeStatCode, String beginDate, String endDate);
	
}
