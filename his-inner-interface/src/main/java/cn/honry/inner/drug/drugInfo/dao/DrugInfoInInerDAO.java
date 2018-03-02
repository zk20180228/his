package cn.honry.inner.drug.drugInfo.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.dao.EntityDao;
/**
 * 
 * @Description 药品接口DAO层
 * @author  lyy
 * @createDate： 2016年7月7日 下午4:29:48 
 * @modifier lyy
 * @modifyDate：2016年7月7日 下午4:29:48
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
public interface DrugInfoInInerDAO extends EntityDao<DrugInfo>{
	/**  
	 *  
	 * @Description：  分页查询－获得总条数
	 * @Author：wujiao
	 * @CreateDate：2015-10-29 上午11:59:48  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-10-29上午11:59:48  
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
	 * @return String ：1 包装 单位 ，0 最小单位
	 * @version 1.0
	 * @since
	 */
	String companyCode(String drugid);
	
	/**
	 * 根据药品编码获取药品信息
	 * @param code 药品编码
	 * @return
	 */
	public DrugInfo getByCode(String code);
	public DrugInfo getDrugName(String drugCode);
	
	List<DrugInfo> getDrugCodeAndName();
}
