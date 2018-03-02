package cn.honry.inner.drug.drugInfo.service;

import java.util.List;

import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.service.BaseService;

public interface DrugInfoInInerService extends BaseService<DrugInfo>{
	/**  
	 *  
	 * @Description：  分页查询－获得总条数
	 * @Author：wujiao
	 * @CreateDate：2015-12-2 上午11:59:48  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-12-2上午11:59:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(DrugInfo drug);
	/**  
	 *  
	 * @Description：  分页查询－获得列表数据
	 * @Author：wujiao
	 * @CreateDate：2015-12-2 上午11:59:48  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-12-2上午11:59:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<DrugInfo> getPage(String page, String rows, DrugInfo drug);
	/***
	 * 根据药品编码和开方单位，判断单位类型
	 * @Title: companyCode 
	 * @author  wfj
	 * @date 创建时间：2016年4月16日
	 * @param drugid ： 药品id
	 * @param companyid : 开方单位id
	 * @version 1.0
	 * @since
	 */
	String companyCode(String drugid);
	/**
	 * @author conglin
	 * @return map
	 * @descipt 药品渲染
	 */
	List<DrugInfo> getDrugCodeAndName();
}
