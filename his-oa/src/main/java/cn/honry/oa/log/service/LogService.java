package cn.honry.oa.log.service;


import cn.honry.oa.log.vo.DepartMentVo;
import cn.honry.oa.log.vo.EmployeeExtendVo;
import cn.honry.oa.log.vo.ProcessTopicVo;
import cn.honry.oa.log.vo.TaskDelVo;

import java.util.List;

/**
 * @Description:
 * @Author: zhangkui
 * @CreateDate: 2018/1/4 16:51
 * @Modifier: zhangkui
 * @version: V1.0
 */
public interface LogService {



    /**
     * @desc:查询列表
     * @author:  zhangkui
     * @create:  2018/1/4 17:26
     * @version: V1.0
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param page 当前页
     * @param rows 每页显示的记录数
     * @throws:
     *
     */
    List<TaskDelVo> findTaskDelVoList(String startTime, String endTime, String page, String rows);

    /**
     * @desc:查询总记录数
     * @author:  zhangkui
     * @create:  2018/1/4 17:26
     * @version: V1.0
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @throws:
     *
     */
    Integer findTaskDelVoCount(String startTime,String endTime);

    /**
     * @desc:根据名字，员工号查询扩展员工列表
     * @author:  zhangkui
     * @create:  2018/1/4 20:45
     * @version: V1.0
     * @param eName 员工名
     * @param jobNO 员工号
     * @param page 当前页
     * @param rows 每页显示的记录数
     * @throws:
     */
    List<EmployeeExtendVo> findEmpList(String eName, String jobNO, String page, String rows);

    /**
     * @desc:根据名字，员工号查询扩展员工总数
     * @author:  zhangkui
     * @create:  2018/1/4 20:45
     * @version: V1.0
     * @param eName 员工名
     * @param jobNO 员工号
     * @throws:
     */
    Integer findEmpCount(String eName,String jobNO);

    /**
     * @desc:根据名字，科室编号查询科室列表
     * @author:  zhangkui
     * @create:  2018/1/4 20:45
     * @version: V1.0
     * @param dName 科室名
     * @param deptCode 科室编号
     * @param page 当前页
     * @param rows 每页显示的记录数
     * @throws:
     */
    List<DepartMentVo> findDeptList(String dName, String deptCode, String page, String rows);

    /**
     * @desc:根据名字，科室编号查询科室总数
     * @author:  zhangkui
     * @create:  2018/1/4 20:45
     * @version: V1.0
     * @param dName 科室名
     * @param deptCode 科室编号
     * @throws:
     */
    Integer findDeptCount(String dName,String deptCode);

    /**
     * @desc:根据名字查询流程分类列表
     * @author:  zhangkui
     * @create:  2018/1/4 20:45
     * @version: V1.0
     * @param pName 科室名
     * @param page 当前页
     * @param rows 每页显示的记录数
     * @throws:
     */
    List<ProcessTopicVo> findProcessTopicList(String pName, String page, String rows);

    /**
     * @desc:根据名字查询流程分类总数
     * @author:  zhangkui
     * @create:  2018/1/4 20:45
     * @version: V1.0
     * @param pName 科室名
     * @throws:
     */
    Integer findProcessTopicCount(String pName);

    /**
     * @desc:
     * @author:  zhangkui
     * @create:  2018/1/4 20:55
     * @version: V1.0
     * @param flag 操作标志：1流程2人员3科室
     * @param condition 1流程分类id2人员员工号3科室编号
     * @param t_value 1流程分类id2人员员工号3科室编号 的名字
     * @throws:
     */
    void updateTaskInfo(String flag,String condition,String t_value);



}
