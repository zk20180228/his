package cn.honry.oa.meeting.emGroup.dao;

import java.util.List;

import cn.honry.oa.meeting.emGroup.vo.EmGroupVo;
import cn.honry.oa.meeting.emGroup.vo.GroupVo;

public interface EmGroupDao {
	
	/**
	 * 
	 * <p>加载组树</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月5日 上午10:31:13 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月5日 上午10:31:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param text节点的文本值，用于根据节点名字搜索
	 * @return
	 * @throws:
	 *
	 */
	public List<GroupVo> loadGroup(String text);
     
    
	/**
	 * 
	 * <p>根据组id查询该组下的所有成员，包含搜索条件,分页 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月5日 上午10:36:33 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月5日 上午10:36:33 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id 组id
	 * @param employee_name 员工姓名
	 * @param employee_jobon 员工号
	 * @param dept_name 科室名字
	 * @param page 当前页
	 * @param rows 每页显示多少行
	 * @return
	 * @throws:
	 *
	 */
	public List<EmGroupVo> groupList(String id, String employee_name,String employee_jobon, String dept_name, String page, String rows);
     
	/**
	 * 
	 * <p>计算该组下的所有成员 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月5日 上午10:39:55 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月5日 上午10:39:55 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id 组id
	 * @param employee_name 员工姓名
	 * @param employee_jobon 员工号
	 * @param dept_name 科室名字
	 * @return
	 * @throws:
	 *
	 */
	public int groupCount(String id, String employee_name, String employee_jobon,String dept_name);
    
	/**
	 * 
	 * <p>根据员工维护表的id，删除该员工 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月5日 上午10:41:32 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月5日 上午10:41:32 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id 员工维护表的id
	 * @throws:
	 *
	 */
	public void delEmployeeById(String id);
	
	/**
	 * 
	 * <p>添加员工 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月6日 下午3:41:51 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月6日 下午3:41:51 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id 组id
	 * @param text 组名
	 * @param employee_jobon 一组员工号，用逗号分开
	 * @throws:
	 *
	 */
	public void addEmp(String id, String text, String employee_jobon);
	
	/**
	 * 
	 * <p>查询指定组下的所有科室名，当组id为null时,加载所有科室 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月7日 上午11:48:22 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月7日 上午11:48:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id 组id
	 * @return
	 * @throws:
	 *
	 */
	public List<GroupVo> loadDept(String id);

}
