package cn.honry.inpatient.bill.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.bill.vo.OutpatientDrugcontrolDrugbillclass;

@SuppressWarnings({"all"})
public interface OutpatientDrugcontrolDrugbillclassDAO extends EntityDao<OutpatientDrugcontrolDrugbillclass> {
	/**
	 * @Description:查询列表
	 * @Author：  dh
	 * @CreateDate： 2015-12-28
	 * @param @param page
	 * @param @param rows
	 * @param @param billclassSerc
	 * @param @return   
	 * @return List<OutpatientDrugcontrolDrugbillclass>  
	 * @version 1.0
	**/
	List<OutpatientDrugcontrolDrugbillclass> getPage(String page, String rows, OutpatientDrugcontrolDrugbillclass outpatientDrugcontrolDrugbillclass);
	
	/**
	 * @Description:查询列表总条数
	 * @Author：  dh
	 * @CreateDate： 2015-12-28
	 * @param @param page
	 * @param @param rows
	 * @param @param billclassSerc
	 * @param @return   
	 * @return List<OutpatientDrugcontrolDrugbillclass>  
	 * @version 1.0
	**/
	int getTotal(OutpatientDrugcontrolDrugbillclass outpatientDrugcontrolDrugbillclass);
}	
