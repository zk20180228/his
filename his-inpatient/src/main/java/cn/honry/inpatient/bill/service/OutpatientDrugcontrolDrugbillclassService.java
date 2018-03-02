package cn.honry.inpatient.bill.service;

import java.util.List;

import cn.honry.inpatient.bill.vo.OutpatientDrugcontrolDrugbillclass;


public interface OutpatientDrugcontrolDrugbillclassService {
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
	List<OutpatientDrugcontrolDrugbillclass> getWarnLine(OutpatientDrugcontrolDrugbillclass vo, String page, String rows);
	/**
	 * @Description:查询列表总条数
	 * @Author：  dh
	 * @CreateDate： 2015-12-28
	 * @param @param page
	 * @param @param rows
	 * @param @param billclassSerc
	 * @version 1.0
	**/
	 int getTotalCount(OutpatientDrugcontrolDrugbillclass vo);
}
