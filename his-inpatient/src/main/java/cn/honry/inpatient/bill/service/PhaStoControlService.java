package cn.honry.inpatient.bill.service;

import java.util.List;

import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.service.BaseService;

public interface PhaStoControlService extends BaseService<OutpatientDrugcontrol>{

	/**
	 * @Author：  lt
	 * @CreateDate： 2015-9-7
	 * @param @param page
	 * @param @param rows
	 * @param @param phaStoControlSerc
	 * @param @return   
	 * @return List<PhaStoControl>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<OutpatientDrugcontrol> getPage(String page, String rows, OutpatientDrugcontrol outpatientDrugcontrol) throws Exception;

	/**
	 * @Author：  lt
	 * @CreateDate： 2015-9-7
	 * @param @param phaStoControlSerc
	 * @param @return   
	 * @return int  
	 * @version 1.0
	 * @throws Exception 
	**/
	int getTotal(OutpatientDrugcontrol outpatientDrugcontrol) throws Exception;
	/**
	 *  
	 * @Description：   分页查询
	 * @Author：dh
	 * @CreateDate：2015-12-25 上午10:24:49  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<OutpatientDrugcontrol> getPageList(String page, String rows, OutpatientDrugcontrol outpatientDrugcontrol) throws Exception;
	/**  
	 *  
	 * @Description：  保存
	 * @Author：dh
	 * @ModifyDate：2015-12-21 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	void saveOutpatientDrugcontrol(OutpatientDrugcontrol outpatientDrugcontrol) throws Exception;
	/**
	 * @Description:查询摆药台列表
	 * @Author：  dh
	 * @CreateDate： 2015-12-28
	 * @param @param page
	 * @param @param rows
	 * @param @param OutpatientDrugcontrol
	 * @param @return   
	 * @return List<DrugOutstore>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<OutpatientDrugcontrol> QueryOutpatientDrugcontrol(String page, String rows, OutpatientDrugcontrol outpatientDrugcontrol) throws Exception;

	
	/**
	 * @Description:查询摆药台总条目
	 * @Author：  dh
	 * @CreateDate： 2015-12-28
	 * @param @param OutpatientDrugcontrol
	 * @param @return   
	 * @return int  
	 * @version 1.0
	 * @throws Exception 
	**/
	int getTotalOutpatientDrugcontrol(OutpatientDrugcontrol outpatientDrugcontrol) throws Exception;
	/**
	 * @Description:删除
	 * @Author：  dh
	 * @CreateDate： 2015-12-3 下午02:36:53 
	 * @Modifier：dh
	 * @ModifyDate：2015-12-3 下午02:36:53 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	**/
	void delUpdate(String ids) throws Exception;
	/**
	 * 修改摆药台
	 * @throws Exception 
	 */
	public void UpdateOutpatientDrugcontrol(String deptCode,
			String controlName, String showLevel, String controlAttr,
			String sendType, String mark) throws Exception;
	/**
	 * @Description:根据json串保存
	 * @Author：  lt
	 * @CreateDate： 2015-8-26
	 * @param @param infoJson   
	 * @return void  
	 * @version 1.0
	 * @param detailJson 
	 * @throws Exception 
	**/
	void saveOrUpdate(String billClassIds,String deptCode,String billJson) throws Exception;
	/**
	 * @Description:根据json串修改
	 * @Author：  lt
	 * @CreateDate： 2015-8-26
	 * @param @param infoJson   
	 * @return void  
	 * @version 1.0
	 * @param detailJson 
	 * @throws Exception 
	 **/
	void Update(String classId,String deptCode,String con,String billJson) throws Exception;
	/**
	 * 查询修改的一条
	 * @throws Exception 
	 */
	List<OutpatientDrugcontrol> QueryOutpatientDrugcontrolupdate(String id) throws Exception;
	/**
	 * 根据摆药台id 查询摆药单
	 * @throws Exception 
	 */
	List<DrugBillclass> queryDrugBillclass(String controId) throws Exception;
}
