package cn.honry.statistics.drug.queryInstore.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.DrugInStore;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugSupplycompany;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface QueryInstoreDao extends EntityDao<DrugInStore>{
	/***
	 * 药品入库查询(统计)记录
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	 */
	List<DrugInStore> queryInstore(String Stime,String Etime,String drug,String page,String rows,String company,String user) throws Exception;
	/***
	 * 药品入库查询(统计)条数
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	 */
	int queryInstoreTotle(String Stime,String Etime,String drug,String company,String user) throws Exception;
	/**  
	 *  
	 * @Description：  药品下拉框
	 * @Author：zpty
	 * @CreateDate：2016-6-22
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<DrugInfo> getComboboxdrug() throws Exception;
	
	/**  
	 *  
	 * @Description：  生产厂家下拉框
	 * @Author：zpty
	 * @CreateDate：2016-6-22
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<DrugSupplycompany> getComboboxCompany() throws Exception;
	/**
	 * 根据id获取公司名称
	 * @return
	 * @throws Exception 
	 */
	Map<String, String> getCompanyName() throws Exception;
	/**  
	 *  
	 * @Description：  人员下拉框
	 * @Author：zpty
	 * @CreateDate：2016-6-22
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<User> getComboboxUser() throws Exception;
	
	/***
	 * 药品入库查询(统计)记录--导出用
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	 */
	List<DrugInStore> queryInstoreExp(String stime, String etime, String drug,
			String company, String user) throws Exception;
}
