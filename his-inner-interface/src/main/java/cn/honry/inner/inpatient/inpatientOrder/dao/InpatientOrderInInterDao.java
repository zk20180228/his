package cn.honry.inner.inpatient.inpatientOrder.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.bean.model.DrugPreoutstore;
import cn.honry.base.bean.model.DrugSplit;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.bean.model.VidOrderBedname;
import cn.honry.base.bean.model.VidOrderBednameKs;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.inpatient.inpatientOrder.vo.InpatientOrderInInterVO;
import cn.honry.inner.inpatient.inpatientOrder.vo.OrderInInterVO;

public interface InpatientOrderInInterDao extends EntityDao<InpatientOrder> {

	/**
	 * 分页带条件查询order表
	 * @param inpatientOrder
	 * @param rows
	 * @param page
	 * @return
	 */
	List<InpatientOrder> listOrder(InpatientOrder inpatientOrder,String rows,String page,String typeName);
	
	
	List<InpatientOrder> lllistOrder(InpatientOrder inpatientOrder);
	/***
	 * 获取分页总数
	 * @param inpatientOrder
	 * @return
	 */
	int getTotal(InpatientOrder inpatientOrder);
	
	
	/**
	 * 已审核医嘱下拉框
	 */
	List<InpatientOrder> getItemName();
	
	/**
	 * 根据id来查询资料信息
	 * @param exeId
	 * @return
	 */
	InpatientOrder getOrderddInfoById(String exeId);
	
	/**
	 * @param pharmacyCode 取药药房代码
	 * @param moType 医嘱类型
	 * @param useAge 用法代码
	 * @param drugType 药品类型
	 * @param drugQuality 药品性质
	 * @param doseModelCode 剂型代码
	 * @param 新加属性 药房
	 * @return
	 */
	DrugBilllist getListByProperty(String... exist);
	
	/**
	 * 根据billId查询摆药分类
	 */
	DrugBillclass getClassById(DrugBillclass drugBillclass);

	/**
	 * 通过住院流水号得到
	 * @param inpatientNo
	 * @return
	 */
	InpatientInfo getInfoByInpatientNo(String inpatientNo);
	
	/**
	 * 通过多个住院流水号得到所有的患者
	 * @param inpatientNo
	 * @return
	 */
	List<InpatientInfo> getInfosByInpatientNos(String inpatientNo);
	

	/**
	 * 根据频次名称查找频次数据
	 */
	BusinessFrequency getListByName(String name);
	/**
	 * 查询科室
	 */
   SysDepartment department(String id);
   /**
    * 根据医嘱类别分页查询医嘱详细信息
    * @author  lyy
    * @createDate： 2016年3月17日 下午5:57:13 
    * @modifier lyy
    * @modifyDate：2016年3月17日 下午5:57:13  
    * @modifyRmk：  
    * @version 1.0
    */
	List<VidOrderBedname> getOrdersInquiryPage(String rows, String page, VidOrderBedname order,String deptId );
	/**
	 * 根据医嘱类别分页查询医嘱总条数
	 * @author  lyy
	 * @createDate： 2016年3月17日 下午5:56:47 
	 * @modifier lyy
	 * @modifyDate：2016年3月17日 下午5:56:47  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getOrdersInquiryTotal(VidOrderBedname order,String deptId);
	/**
    * 根据医嘱类别分页查询医嘱详细信息
    * @author  lyy
    * @createDate： 2016年3月17日 下午5:57:13 
    * @modifier lyy
    * @modifyDate：2016年3月17日 下午5:57:13  
    * @modifyRmk：  
    * @version 1.0
    */
	List<VidOrderBednameKs> getOrdersInquiryPageks(String rows, String page, VidOrderBednameKs order,String deptId );
	/**
	 * 根据医嘱类别分页查询医嘱总条数
	 * @author  lyy
	 * @createDate： 2016年3月17日 下午5:56:47 
	 * @modifier lyy
	 * @modifyDate：2016年3月17日 下午5:56:47  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getOrdersInquiryTotalks(VidOrderBednameKs order,String deptId);
	/**
	 * 医嘱类型（下拉框）
	 * @author  lyy
	 * @createDate： 2016年3月18日 上午11:49:51 
	 * @modifier lyy
	 * @modifyDate：2016年3月18日 上午11:49:51  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<InpatientKind> getOrdersType();
	/**
	 * 临时医嘱分页查询
	 * @author  lyy
	 * @createDate： 2016年3月18日 上午11:48:31 
	 * @modifier lyy
	 * @modifyDate：2016年3月18日 上午11:48:31  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<VidOrderBedname> getOrdersInterimInquiryPage(String rows, String page, VidOrderBedname order,String deptId);
	/**
	 * 临时医嘱总条数
	 * @author  lyy
	 * @createDate： 2016年3月18日 上午11:48:31 
	 * @modifier lyy
	 * @modifyDate：2016年3月18日 上午11:48:31  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getOrdersInterimInquiryTotal(VidOrderBedname order,String deptId);
	/**
	 * 临时医嘱分页查询
	 * @author  lyy
	 * @createDate： 2016年3月18日 上午11:48:31 
	 * @modifier lyy
	 * @modifyDate：2016年3月18日 上午11:48:31  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<VidOrderBednameKs> getOrdersInterimInquiryPageks(String rows, String page, VidOrderBednameKs order,String deptId);
	/**
	 * 临时医嘱总条数
	 * @author  lyy
	 * @createDate： 2016年3月18日 上午11:48:31 
	 * @modifier lyy
	 * @modifyDate：2016年3月18日 上午11:48:31  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getOrdersInterimInquiryTotalks(VidOrderBednameKs order,String deptId);

	/**
	 * 科室渲染
	 * @author  lyy
	 * @createDate： 2016年3月18日 下午5:35:29 
	 * @modifier lyy
	 * @modifyDate：2016年3月18日 下午5:35:29  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> querydeptCombobox();
   /**
    * 渲染医嘱类别
    */
   List<InpatientKind> kindMap();
   /**
    * 渲染医生
    */
   List<User> userMap();
   /**
    * 渲染执行科室
    */
   List<SysDepartment> deptMap();
   /**
    * 根据住院流水号查询该患者医嘱
    */
   List<InpatientOrder> getOrderByInpatientNo(String inpatientNo,String type);

   /**
    * 根据医嘱中的住院流水号查询住院主表
    * @author  zl
    * @createDate： 2016年4月14日 上午9:42:43 
    * @modifier zl
    * @modifyDate：2016年4月14日 上午9:42:43
    * @param：  
    * @modifyRmk：  
    * @version 1.0
    */
  List<InpatientInfo> getInpatientViewByNo(String inpatientNo);
  /**
   * 根据医嘱类别id查询医嘱类别信息
   * @author  zl
   * @createDate： 2016年4月18日 下午3:08:34 
   * @modifier zl
   * @modifyDate：2016年4月18日 下午3:08:34
   * @param：  
   * @modifyRmk：  
   * @version 1.0
   */
  InpatientKind getkindById(String id);
  
  /**
   * 根据科室类型获取取药药房
   * @author  zl
   * @createDate： 2016年4月19日 上午9:10:22 
   * @modifier zl
   * @modifyDate：2016年4月19日 上午9:10:22
   * @param：  
   * @modifyRmk：  
   * @version 1.0
   */
  List<SysDepartment> getPmDept();
  /**
   * 根据药品id和科室id查询药品 拆分表
   * @author  zl
   * @createDate： 2016年4月19日 上午10:50:56 
   * @modifier zl
   * @modifyDate：2016年4月19日 上午10:50:56
   * @param：  
   * @modifyRmk：  
   * @version 1.0
   */
  DrugSplit getSplitByDeptIdAndDrugId(String...ob);
  /**
   * 更新库存维护表
   * @author  zl
   * @createDate： 2016年4月21日 上午9:33:38 
   * @modifier zl
   * @modifyDate：2016年4月21日 上午9:33:38
   * @param：  
   * @modifyRmk：  
   * @version 1.0
   */
  void saveDrugPreoutstore(DrugPreoutstore preoutstore);
  /**
   * 根据主医嘱查询其附材医嘱
   * @author liujl
   */
   List<InpatientOrder> getSubOrderByOrder(InpatientOrder order);

   /**
    * 根据多个id查询医嘱数组信息
    * @author  liujl
    * @createDate： 2016年4月21日 上午9:33:38 
    * @modifier liujl
    * @modifyDate：2016年4月21日 上午9:33:38
    * @param： ids 格式："id1,id2"
    * @modifyRmk：  
    * @version 1.0
    */
   List<InpatientOrder> getOrdersByIds(String orderIds);
   /**
    * 审核医嘱
    * @author  liujl
    * @createDate： 2016年4月21日 上午9:33:38 
    * @modifier liujl
    * @modifyDate：2016年4月27日 下午1:36:38
    * @param： order：医嘱对象
    * @modifyRmk：  
    * @version 1.0
    */
   void confirmOrder(InpatientOrder order);

   /**
    * 审核作废医嘱：记录医嘱停止的审核人、医嘱停止的审核时间，设置医嘱停止标志
    * @author  liujl
    * @createDate： 2016年4月21日 上午9:33:38 
    * @modifier liujl
    * @modifyDate：2016年4月21日 上午9:33:38
    * @param：  moOrder：医嘱流水号，moState：医嘱状态，opteTime：操作时间
    * @modifyRmk：  
    * @version 1.0
    */
   void stopInvalidOrder(InpatientOrder order);

   /**
    * 设置医嘱的本次分解时间和下次分解时间
    * @author  liujl
    * @createDate： 2016年4月29日 下午9:33:38 
    * @param：  orderVO：自定义医嘱对象
    * @modifyRmk：  
    * @version 1.0
    */
   void updateExecTime(InpatientOrderInInterVO orderVO);

   /**
	 * 根据患者的住院流水号获取所有的审核、执行状态的医嘱
	 * @author liujl 2016-4-28
	 * @param patientIds 患者住院流水号
	 * @return
	 */
   List<InpatientOrder> getOrdersByPatientIds(String patientIds);

   /**
	 * 根据医嘱流水号获取医嘱信息
	 * @author liujl 2016-5-5
	 * @param moOrder 医嘱流水号
	 * @return
	 */
   InpatientOrder getOrderByMoOrder(String moOrder);

	/**
	 *
	 * 根据患者住院号及医嘱类型获得医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年5月9日 下午6:14:36 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param patIds
	 * @param advType
	 * @return：
	 *
	 */
   List<InpatientOrder> getOrderListByPatIds(String patIds, String advType);

	/**
	 *
	 * 根据id获得医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年5月9日 下午6:14:36 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param patIds
	 * @param advType
	 * @return：
	 *
	 */
	List<InpatientOrder> getOrderById(String id);

	/**
	 *
	 * 根据id统计附材数量
	 * @Author：aizhonghua
	 * @CreateDate：2016年5月9日 下午6:14:36 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param patIds
	 * @param advType
	 * @return：
	 *
	 */
	Integer findPanelById(String combNo);

	/**
	 *
	 * 查询分解记录
	 * @Author：aizhonghua
	 * @CreateDate：2016年5月16日 下午8:02:02 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	int getOrderTotal(String inpatientNos);

	/**
	 *
	 * 查询分解记录
	 * @Author：aizhonghua
	 * @CreateDate：2016年5月16日 下午8:02:02 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	List<InpatientOrder> getOrderPage(String inpatientNos,String page,String rows);

	/**
	 * 通过住院流水号得患者信息
	 * @param patientIds
	 * @return
	 */
	InpatientInfo getInfosByInpatientNo(String patientNo);


	List<InpatientOrder> getOrdersByIdsAndInPatientNo(String orderIds,
			String patNos);

	/**
	 * 根据床位记录表中的id查询床位名称
	 * @Author：liujl
	 * @CreateDate：2016年5月23日 下午5:38:02 
	 * @version： 1.0：
	 */
	String getBedName(String bedId);

	/**
	 * 根据执行记录id获得执行记录（药品非药品）
	 * @Author：aizhonghua
	 * @CreateDate：2016年5月25日 上午9:02:02 
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016年5月25日 上午9:02:02 
	 * @ModifyRmk：
	 * @param1:exeIds 执行单id  “111,222”
	 * @version： 1.0：
	 *
	 */
	List<OrderInInterVO> queryOrderByIds(String exeIds);
}
