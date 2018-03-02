package cn.honry.inpatient.delivery.dao;

import java.util.List;

import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientStoMsg;
import cn.honry.base.bean.model.InpatientStoMsgNow;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.delivery.vo.DeliveryVo;
/**
 * 集中发送数据库层
 * @author  lyy
 * @createDate： 2015年12月28日 上午10:50:39 
 * @modifier lyy
 * @modifyDate：2015年12月28日 上午10:50:39  
 * @modifyRmk：  
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface DeliveryDAO extends EntityDao<DrugApplyout> {
	/**
	 * 根据出库表中的摆药单分类查询摆药单分类表
	 * @author  lyy
	 * @createDate： 2015年12月28日 上午10:56:15 
	 * @modifier lyy
	 * @modifyDate：2016年4月15日 上午10:56:15  
	 * @modifyRmk：  billclasss 摆药分类
	 * @version 1.0
	 */
	List<DrugBillclass> queryBillclass(String billclasss);
	/**
	 * 集中发送带分页查询
	 * @author  lyy
	 * @createDate： 2015年12月29日 上午10:23:10 
	 * @modifier lyy
	 * @modifyDate：2015年12月29日 上午10:23:10  
	 * @modifyRmk：  deliveryVo 集中发送虚拟实体   page 分页查询总页数    rows  总条数  deptCode 登录病区所对应的科室或者登录科室
	 * @version 1.0
	 */
	List<DeliveryVo> getPage(DeliveryVo deliveryVo,String page, String rows,String deptCode) ;
	/**
	 * 集中发送总条数
	 * @author  lyy
	 * @createDate： 2015年12月29日 上午10:23:53 
	 * @modifier lyy
	 * @modifyDate：2015年12月29日 上午10:23:53  
	 * @modifyRmk：  deliveryVo 集中发送虚拟实体  deptCode 登录病区所对应的科室或者登录科室
	 * @version 1.0
	 */
	int getTotal(DeliveryVo deliveryVo,String deptCode);
	/**
	 * 发送多个药品
	 * @author  lyy
	 * @createDate： 2015年12月30日 下午2:36:13 
	 * @modifier lyy
	 * @modifyDate：2015年12月30日 下午2:36:13  
	 * @modifyRmk：  ids 出库申请表的主键id集合    drugEdBill 摆药单号  sendType 发送类型  userId 登录人
	 * @version 1.0
	 */
	String applyOutUpdate(String[] ids,String drugEdBill,Integer sendType,String userId);
	/**
	 * 根据住院主表的患者编码去查询出库申请表中的摆药分类
	 * 已摆药的摆药单  状态为住院摆药的摆药单
	 * @author  lyy
	 * @createDate： 2015年12月30日 下午4:14:21 
	 * @modifier lyy
	 * @modifyDate：2016年4月15日 下午4:14:21  
	 * @modifyRmk：  patientidlist 患者编号集合
	 * @version 1.0
	 */
	List<DrugApplyoutNow> queryApplyOut(List patientidlist);
	/**
	 * 根据住院主表的患者编码去查询出库申请表中的摆药分类
	 * 已摆药的摆药单  状态为住院退药的摆药单
	 * @author  lyy
	 * @createDate： 2015年12月30日 下午4:14:21 
	 * @modifier lyy
	 * @modifyDate：2016年4月15日 下午4:14:21  
	 * @modifyRmk：  patientidlist 患者编号集合
	 * @version 1.0
	 */
	List<DrugApplyoutNow> queryReturnDrug(List patientidlist);
	/**
	 * 获得药品出库的总条数
	 * @author  lyy
	 * @createDate： 2015年12月30日 下午4:15:44 
	 * @modifier lyy
	 * @modifyDate：2015年12月30日 下午4:15:44  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int total();
	/**
	 * 根据登录科室获所有病区
	 * @author  lyy
	 * @createDate： 2016年4月14日 下午8:40:57 
	 * @modifier lyy
	 * @modifyDate：2016年4月14日 下午8:40:57
	 * @param：    deptId 登录科室id
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<DepartmentContact> queryDeptContact(String deptId);
	/**
	 * 根据病区下id去查询患者的住院流水号
	 * @author  lyy
	 * @createDate： 2016年4月15日 上午10:36:45 
	 * @modifier lyy
	 * @modifyDate：2016年4月15日 上午10:36:45
	 * @param：    deptCodeId 病区id
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<InpatientInfoNow> getQueryInpatientNo(List deptContactList);
	/**
	 * 根据住院主表的患者编码去查询出库申请表中的摆药分类
	 * 未摆药的摆药单
	 * @author  lyy
	 * @createDate： 2016年4月15日 下午2:36:39 
	 * @modifier lyy
	 * @modifyDate：2016年4月15日 下午2:36:39
	 * @param：    patientid 患者主键
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<DrugApplyoutNow> getQueryDrugApply(String patientid);
	/**
	 * 打印后的修改方法
	 * @author  lyy
	 * @createDate： 2016年4月19日 上午9:54:59 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 上午9:54:59
	 * @param：    id 出库表id sendType 发送类型 userId 登录人
	 * @modifyRmk：  
	 * @version 1.0
	 */
	String applyOutStamp(String id,Integer sendType,String userId);
	/**
	 * 根据摆药单号查询摆药单详细信息总条数
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午2:15:12 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 下午2:15:12
	 * @param：    deliverySerch 集中发送虚拟实体
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getTotalBill(DeliveryVo deliverySerch);
	/**
	 * 根据摆药单号分页查询摆药单详细信息
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午2:15:21 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 下午2:15:21
	 * @param：    deliveryVo 集中发送虚拟实体   page 分页查询总页数    rows  总条数
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<DeliveryVo> getPageBill(DeliveryVo deliverySerch, String page, String rows);
	/**
	 * 查询员工名称
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午3:54:04 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 下午3:54:04
	 * @param：    
	 * @modifyRmk：   原来是查的是员工表现在查的是用户表
	 * @version 1.0
	 */
	List<User> queryEmpName();
	/**
	 * 根据出库申请主键去查询摆药分类
	 * @author  lyy
	 * @createDate： 2016年4月21日 上午9:32:00 
	 * @modifier lyy
	 * @modifyDate：2016年4月21日 上午9:32:00
	 * @param：    id 出库申请表中的主键id
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<DrugApplyoutNow> queryDrugBill(String[] ids);
	/**
	 * 根据摆药分类查询摆药通知单
	 * @author  lyy
	 * @createDate： 2016年4月21日 上午10:42:13 
	 * @modifier lyy
	 * @modifyDate：2016年4月21日 上午10:42:13
	 * @param：    billclassCode 摆药分类id drugCode 取药科室  sendType 发药类型
	 * @modifyRmk：  
	 * @version 1.0
	 */
	InpatientStoMsgNow queryStoMsg(String billclassCode,String drugCode,Integer sendType);
	/**
	 * 查询所有的摆药分类名称
	 * @author  lyy
	 * @createDate： 2016年5月3日 下午7:10:31 
	 * @modifier lyy
	 * @modifyDate：2016年5月3日 下午7:10:31
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<DrugBillclass> queryBillClassName();
}
