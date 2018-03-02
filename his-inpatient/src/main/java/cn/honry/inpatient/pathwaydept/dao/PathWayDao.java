package cn.honry.inpatient.pathwaydept.dao;

import java.util.List;

import cn.honry.base.bean.model.CpDept;
import cn.honry.base.bean.model.CpWay;
import cn.honry.base.dao.EntityDao;

public interface PathWayDao extends EntityDao<CpDept>{
	List<CpDept> getcpDept(String page,String rows,String deptCode,String name);
	int getcpDeptTotal(String deptCode,String name);
	List<CpWay> getCpWay(); 
}
