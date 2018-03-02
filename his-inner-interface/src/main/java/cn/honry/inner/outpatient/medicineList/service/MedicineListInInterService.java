package cn.honry.inner.outpatient.medicineList.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.service.BaseService;
import cn.honry.inner.outpatient.medicineList.vo.ChargeVo;

public interface MedicineListInInterService extends BaseService<OutpatientFeedetail>{
	/**  
	 * @Description：门诊收费接口
	 * @Author：ldl
	 * @CreateDate：2016-04-26
	 * @ModifyRmk：  
	 * @param：chargeVo 收费参数  
	 * @version 1.0
	 */
	Map<String,String> chargeImplement(ChargeVo chargeVo,String feeWhenUnenougth);
	/**
	 * @Description 根据收费员code和类型获取单张发票号
	 * @author  marongbin
	 * @createDate： 2017年1月21日 下午4:33:24 
	 * @modifier 
	 * @modifyDate：
	 * @param id
	 * @param invoiceType
	 * @return: Map<String,Object>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	Map<String,String> getInvoiceNoByCode(String id,String invoiceType,int num);
	/**
	 * @Description 根据处方明细集合或者所需要换算的处方集合ID进行换算
	 * @author  marongbin
	 * @createDate： 2017年2月24日 下午1:33:52 
	 * @modifier 
	 * @modifyDate：
	 * @param feelist 已查询的处方集合
	 * @param feeIds 处方集合id，通过查询获得处方集合
	 * @param contsCode 合同单位code
	 * @return: List<OutpatientFeedetailNow>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<OutpatientFeedetailNow> getchangeByconts(List<OutpatientFeedetailNow> feelist,String feeIds,String contsCode);
	/**
	 * @Description 根据id调整发票号
	 * @author  marongbin
	 * @createDate： 2017年3月11日 下午5:57:12 
	 * @modifier 
	 * @modifyDate：
	 * @param id 该发票号所在发票组的id
	 * @param invoiceNo 调整后的发票号
	 * @return: Map<String,String>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	Map<String,String> changeInvoiceNO(String id,String invoiceNo);
	
	/**  
	 * 
	 * 保存药品明细信息
	 * @Author: zxl
	 * @CreateDate: 2017年7月4日 下午6:07:53 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月4日 下午6:07:53 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	void save(InpatientMedicineListNow med);
}
