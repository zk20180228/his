package cn.honry.inpatient.bill.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.service.BaseService;

/**
 * @Description： 医嘱类别编码服务层
 * @version 1.0
 *
 */
public interface InpatientKindQueryService extends BaseService<InpatientKind>{
	/**
	 * 查询所有的list页面
	 * @param codeOrdercategory  实体
	 * @return List<CodeOrdercategory> 
	 */
	
	public List<InpatientKind> searchOrdercategoryList(InpatientKind inpatientKind);
	/**
	 * 获取记录条数
	 * @param codeOrdercategory  实体
	 * @return int
	 */
	int getTotal(InpatientKind inpatientKind);
	/**
	 * 下拉框的值
	 * @param code  传递的是查询条件
	 * @return
	 */
	List<InpatientKind> likeSearch(String code);
	 /**
	  * 医嘱类别查询列表
	  */
	 List<InpatientKind> getKind(String code);
	 /**
	  * 根据摆药分类id查询摆药分类明细信息
	  */
	 List<DrugBilllist> queryBilllist(String BillclassId);
	 /**
	  * 根据摆药id查询摆药信息
	  */
	 List<DrugBillclass> queryBillclass(String BillclassId);
	 /**
	  * 修改摆药单
	  */
	void saveOrUpdate(Map<String, String> parameterMap,String infoJson) ;
	/**
	  * 根据摆药id查询医嘱类型  并去重
	  */
	 List<DrugBilllist> queryTypeCode(String billssId);
	 /**
	  * 根据摆药id查询药品类型  并去重
	  */
	 List<DrugBilllist> queryDrugType(String billssId);
	 /**
	  * 根据摆药id查询药品药剂并去重
	  */
	 List<DrugBilllist> queryDoseModelCode(String billssId);
	 /**
	  * 根据摆药id查询药品用法去重
	  */
	 List<DrugBilllist> queryUsageCode(String billssId);
	 /**
	  * 根据摆药id查询药品性质  并去重
	  */
	 List<DrugBilllist> queryDrugQuality(String billssId);
		/**
		 * 下拉框查询
		 * @param code 参数  查询条件的参数
		 * @return List<CodeCenterfeecode>
		 */
		List<BusinessDictionary> likeSearch(String type,String code);
}
