package cn.honry.inpatient.pathwaydept.service;

import java.util.List;

import cn.honry.base.bean.model.CpDept;
import cn.honry.base.bean.model.CpWay;

public interface PathWayService {
	List<CpDept> getcpDept(String page,String rows,String deptCode,String name);
	int getcpDeptTotal(String deptCode,String name);
	void saveOrUpdateCpDept(CpDept info);
	CpDept getByid(String id );
	List<CpWay> getCpWay(); 
}
