package cn.honry.inpatient.delivery.service;

import java.util.List;

import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.User;
import cn.honry.base.service.BaseService;
import cn.honry.inpatient.delivery.vo.DeliveryVo;
import cn.honry.utils.TreeJson;
	/**
	 * 摆药单业务层
	 * @author  lyy
	 * @createDate： 2015年12月28日 上午10:51:13 
	 * @modifier lyy
	 * @modifyDate：2015年12月28日 上午10:51:13  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public interface DeliveryService extends BaseService<DrugApplyout>{
	/**
	 * 查询所有的摆药单分类
	 * @author  lyy
	 * @createDate： 2015年12月28日 上午10:56:15 
	 * @modifier lyy
	 * @modifyDate：2015年12月28日 上午10:56:15  
	 * @modifyRmk： id 摆药分类id 
	 * @version 1.0
	 * @throws Exception 
	 */
	List<TreeJson> queryBillclass(String id) throws Exception;
	/**
	 * 集中发送带分页查询list集合
	 * @author  lyy
	 * @createDate： 2015年12月29日 上午10:56:15 
	 * @modifier lyy
	 * @modifyDate：2016年5月4日 上午10:56:15    
	 * @modifyRmk：  deliveryVo 集中发送虚拟实体   page 分页查询总页数    rows  总条数  deptCode 登录病区所关联的科室或者登录科室
	 * @version 1.0
	 * @throws Exception 
	 */
	List<DeliveryVo> getPage(DeliveryVo deliveryVo,String page,String rows,String deptCode) throws Exception;
	/**
	 * 集中发送总条数
	 * @author  lyy
	 * @createDate： 2015年12月29日 上午10:56:15 
	 * @modifier lyy
	 * @modifyDate：2016年5月4日 上午10:56:15   
	 * @modifyRmk：  deliveryVo 集中发送虚拟实体     deptCode 登录病区所关联的科室或者登录科室
	 * @version 1.0
	 * @throws Exception 
	 */
	int getTotal(DeliveryVo deliveryVo,String deptCode) throws Exception;
	/**
	 * 获得药品集中发送，不打印的修改方法 保存过程
	 * @author  lyy
	 * @createDate： 2015年12月30日 下午4:15:44 
	 * @modifier lyy
	 * @modifyDate：2015年12月30日 下午4:15:44  
	 * @modifyRmk：  ids  药品出库表中的主键id集合ids sendType 发送状态
	 * @version 1.0
	 * @throws Exception 
	 */
	String applyOutUpdate(String ids,Integer sendType) throws Exception;
	/**
	 * 获得药品集中发送，打印后的修改方法   保存过程
	 * @author  lyy
	 * @createDate： 2016年4月19日 上午9:53:14 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 上午9:53:14
	 * @param：    ids  药品出库表中的主键id集合ids sendType 发送状态
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	String applyOutUpdateStamp(String ids,Integer sendType) throws Exception;
	/**
	 * 摆药单号树型列表
	 * @author  lyy
	 * @createDate： 2016年4月15日 下午9:03:56 
	 * @modifier lyy
	 * @modifyDate：2016年4月15日 下午9:03:56
	 * @param：      id 摆药单号主键id 
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	String queryApply(String id) throws Exception;
	/**
	 * 总条数已发送状态
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午2:12:56 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 下午2:12:56
	 * @param：    deliverySerch 集中发送虚拟实体
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	int getTotalBill(DeliveryVo deliverySerch) throws Exception;
	/**
	 * 带分页查询已发送状态
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午2:13:00 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 下午2:13:00
	 * @param：   deliverySerch 集中发送虚拟实体   page 分页查询总页数    rows  总条数 
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<DeliveryVo> getPageBill(DeliveryVo deliverySerch, String page, String rows) throws Exception;
	/**
	 * 查询用户
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午3:53:16 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 下午3:53:16
	 * @param：    
	 * @modifyRmk：   原来是查的是员工表现在查的是用户表
	 * @version 1.0
	 */
	List<User> queryEmpName();
	/**
	 * 摆药分类
	 * @author  lyy
	 * @createDate： 2016年5月3日 下午7:05:05 
	 * @modifier lyy
	 * @modifyDate：2016年5月3日 下午7:05:05
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<TreeJson> queryBillClassName(String id) throws Exception;
	/**
	 * 根据登录科室获所有病区
	 * @author  lyy
	 * @createDate： 2016年4月14日 下午8:40:57 
	 * @modifier lyy
	 * @modifyDate：2016年4月14日 下午8:40:57
	 * @param：    deptId 登录科室id
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<DepartmentContact> queryDeptContact(String id) throws Exception;
	/**
	 * 
	 * 
	 * <p>摆药单打印 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月5日 上午10:42:32 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月5日 上午10:42:32 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param tid 患者编号
	 * @param drugedbill 摆药单号
	 * @return:
	 * @throws Exception 
	 *
	 */
	public List<DeliveryVo> iReportInvoiceBill(String tid,String drugedbill) throws Exception;
}
