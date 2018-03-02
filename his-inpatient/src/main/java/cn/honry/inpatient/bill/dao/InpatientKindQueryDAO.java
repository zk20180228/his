package cn.honry.inpatient.bill.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.dao.EntityDao;

/**
 * @Description： 医嘱类别维护DAO层 
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface InpatientKindQueryDAO extends EntityDao<InpatientKind>{
	 /**
	  * 查看信息
	  * @param codeOrdercategory  参数       传递的实体
	  * @return
	  */
	 List searchInpatientKindList(InpatientKind inpatientKind);
	 
	 /**
	  * 获取记录条数
	  * @param codeOrdercategory  参数       传递的实体
	  * @return
	  */
	 int getTotal(InpatientKind inpatientKind);
	 /**
	  * 根据pinyin、wb、inputcode、name
	  * 模糊查询医嘱类别名称
	  * @param code  保存的是文本框的值
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
	 void UpdateBillclass(String infoJson);
	 /**
	  * 修改摆药明细
	  */
	 void UpdateBilllist(Map<String, String> parameterMap,String infolistId);
	 /**
	  * 删除摆药明细
	  */
	 void deleteBilllist(String BillclassId);
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
