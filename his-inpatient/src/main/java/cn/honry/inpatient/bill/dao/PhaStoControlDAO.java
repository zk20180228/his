package cn.honry.inpatient.bill.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.dao.EntityDao;

public interface PhaStoControlDAO extends EntityDao<OutpatientDrugcontrol> {
	/**
	 * @Author：  lt
	 * @CreateDate： 2015-9-7
	 * @param @param page
	 * @param @param rows
	 * @param @param OutpatientDrugcontrol
	 * @param @return   
	 * @return List<PhaStoControl>  
	 * @version 1.0
	**/
	List<OutpatientDrugcontrol> getPage(String page, String rows, OutpatientDrugcontrol outpatientDrugcontrol);

	/**
	 * @Author：  lt
	 * @CreateDate： 2015-9-7
	 * @param @param OutpatientDrugcontrol
	 * @param @return   
	 * @return int  
	 * @version 1.0
	**/
	int getTotal(OutpatientDrugcontrol outpatientDrugcontrol);

	/**
	 * @Author： dh
	 * @CreateDate： 2015-12-25
	 * @param @param page
	 * @param @param rows
	 * @param @param OutpatientDrugcontrol
	 * @param @return   
	 * @return List<PhaStoControl>  
	 * @version 1.0
	**/
	List<OutpatientDrugcontrol> getPageList(String page, String rows, OutpatientDrugcontrol outpatientDrugcontrol);
	/**
	 * @Author： dh
	 * @CreateDate： 2015-12-25
	 * @param @param page
	 * @param @param rows
	 * @param @param OutpatientDrugcontrol
	 * @param @return   
	 * @return List<PhaStoControl>  
	 * @version 1.0
	**/
	List<OutpatientDrugcontrol> QueryOutpatientDrugcontrol(String page, String rows, OutpatientDrugcontrol outpatientDrugcontrol);
	/**
	 * @Author：  lt
	 * @CreateDate： 2015-12-28
	 * @param @param OutpatientDrugcontrol
	 * @param @return   
	 * @return int  
	 * @version 1.0
	**/
	int getTotalOutpatientDrugcontrol(OutpatientDrugcontrol outpatientDrugcontrol);
	/**
	 * @Description：保存
	 * @Author：dh
	 * @CreateDate：2015-12-21 上午09:30:30 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	void saveOutpatientDrugcontrol(OutpatientDrugcontrol outpatientDrugcontrol);
	/**
	 * @Description：删除
	 * @Author：dh
	 * @CreateDate：2015-12-21 上午09:30:30 
	 * @param id，name
	 * @return
	 * @throws Exception
	 */
	void delUpdate(String id);
	/**
	 * @Description：修改
	 * @Author：dh
	 * @CreateDate：2016-3-31 14:00
	 * @param con，billJson
	 * @return
	 */
	void UpdateOutpatientDrugcontrol(String deptCode,String con,String billJson);
	/**
	 * @Description：修改某一条
	 * @Author：dh
	 * @CreateDate：2016-3-31 14:00
	 * @param name，deptCode
	 * @return
	 */
	List<OutpatientDrugcontrol> QueryOutpatientDrugcontrolupdate(String id);
	/**
	 * 修改摆药单ControlId字段
	 */
	void updateControlId(String ControlId,String id);
	/**
	 * 根据摆药台id 查询摆药单
	 */
	List<DrugBillclass> queryDrugBillclass(String controId);

	/**
	 * 根据摆药台id 查询摆药单id
	 * @Author：zxl
	 * @param con 摆药台id
	 */
	List<String> findDrugBillclass(String con);

	/**
	 * 根据查询摆药单id，更新摆药单分类表
	 * @Author：zxl
	 * @param con 摆药单id
	 */
	void updateClassBillcon(String controId);
}
