package cn.honry.inner.inpatient.nurseApply.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientShiftApply;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;


@SuppressWarnings({"all"})
public interface NurseApplyInInterDao extends EntityDao<InpatientShiftApply> {
	
	/**
     * 根据登录科室去查询科室的id
     * @author  lyy
     * @createDate： 2016年3月29日 下午3:04:39 
     * @modifier lyy
     * @modifyDate：2016年3月29日 下午3:04:39  
     * @modifyRmk：  
     * @version 1.0
     */
    SysDepartment queryState(String deptId);
    
    /**
     * 根据科室的deptId,科室类型type,患者树的Id
     * @author  lyy
     * @createDate： 2016年3月29日 下午3:05:06 
     * @modifier lyy
     * @modifyDate：2016年3月29日 下午3:05:06  
     * @modifyRmk：  id 患者树父节点id  deptId  登录科室的Id type  科室的类型
     * @version 1.0
     */
	List<InpatientInfo> infoo(String id, String deptId, String type);
    
}

