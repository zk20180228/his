package cn.honry.inner.baseinfo.department.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.inner.baseinfo.department.vo.FicDeptVO;
import cn.honry.utils.TreeJson;

/**  
 *  
 * 内部接口：科室 
 * @Author：aizhonghua
 * @CreateDate：2016-7-05 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-7-05 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface DeptInInterService  extends BaseService<SysDepartment>{

	/**  
	 *  
	 * 根据id获得部门
	 * @Author：aizhonghua
	 * @CreateDate：2015-10-22 上午11:57:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-10-22 上午11:57:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	SysDepartment getDeptById(String deptId);
	/**
	 *
	 * <li>根据科室获得关联的病区（护士站）
	 * <li>参数科室本身不能为病区
	 * <li>如果该科室关联多个病区默认取第一个做为登录病区
	 * <li>如果该科室没有关联病区则返回null
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月21日 上午9:57:55 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param id 科室id 该科室不能为病区
	 * @return：病区
	 *
	 */
	SysDepartment getNursingStationByLoginDeptId(String id);
	
	/**  
	 * 获得登录用户关联的科室
	 * @Description:
	 * @Author：aizhonghua
     * @CreateDate：2016-4-20 下午09:44:35  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-4-20 下午09:44:35  
	 * @ModifyRmk：  新增默认关联员工的所属科室
	 * @version 1.0
	 *
	 */
	List<SysDepartment> queryDepts(String id);
	
	/**  
	 * 获得部门树
	 * @Description:
	 * @Author：aizhonghua
     * @CreateDate：2016-4-20 下午09:44:35  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-4-20 下午09:44:35  
	 * @version 1.0
	 ** @param treeAll 是否显示所有类别
	* @param deptType 要显示的类别代码
	* @param deptDistrict 院区
	* @param hospitalId 医院id
	 */
	List<TreeJson> QueryTreeDepartmen(boolean treeAll, String deptType,Integer deptDistrict,String hospitalId);
	/** 
	* @Title: QueryTreeFicDepartmen 
	* @Description: 获取科室树---带虚拟科室
	* @param treeAll
	* @param deptType
	* @param deptDistrict
	* @param hospitalId
	* @return
	* @return List<TreeJson>    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年5月17日
	*/
	List<TreeJson> QueryTreeFicDepartmen(boolean treeAll, String deptType,Integer deptDistrict,String hospitalId);
	
	/**
	 * @Description:展示科室树的全部，也可展示一部分
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月18日
	 * @param:id:树展开节点时候的id
	 * @param：map：用来选择性展示树的信息，例如，如果map.get("C")不为空，则展示门诊，为空则不展示
	 * @return:List<TreeJson>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<TreeJson> findDeptTree(String id,Map<String,String> map);
	
	/**  
	 *  
	 * @Description：  查询科室树
	 * @Author：aizhonghua
	 * @CreateDate：2015-5-23 下午03:45:17  
	 * @Modifier：lt
	 * @ModifyDate：2015-7-7 下午14:30:49  
	 * @ModifyRmk：  加了个参数为了方便查询部分科室树
	 * @version 1.0
	 *
	 */
	List<SysDepartment> findTree(boolean b,String deptTypes);
	/**
	 *  查询全部科室
	 *  @author 涂川江
	 *  @CreateDate 2016/7/14
	 *  @version 1.0
	 */
	List<SysDepartment> queryAllDept();
	/**  
	 *  
	 * @Description：  查询科室
	 * @Author：zhangjin
	 * @CreateDate：2016-7-18 
	 * @Modifier：
	 * @ModifyDate： 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<SysDepartment> getDept();
	
	/**  
	 *  
	 * @Description：  获得科室code和name
	 * @Author：aizhonghua
	 * @CreateDate：2015-5-23 下午03:45:17  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-7 下午14:30:49  
	 * @version 1.0
	 *
	 */
	Map<String, String> querydeptCodeAndNameMap();
	/**
	 * 
	 * @Description 根据部门code查询整条信息
	 * @author  lyy
	 * @createDate： 2016年7月20日 上午11:54:02 
	 * @modifier lyy
	 * @modifyDate：2016年7月20日 上午11:54:02
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	SysDepartment getDeptCode(String deptCode);

	
	/**
	 * 根据科室编码查询 科室信息
	 * @param code 科室编码
	 * @return
	 */
	public SysDepartment getByCode(String code);
	
	/**
	 * 根据科室编码、名称、五笔、拼音等及时查询科室信息
	 * @param q
	 * @return
	 */
	public List<SysDepartment> getDeptByQ(String q);
	
	/**
	 * 判断该账户是否有关联科室
	 * @param q
	 * @return
	 */
	boolean isHaveDept(String account);
	/** 根据参数q查询分页
	* @Title: getPageByQ
	* @Description: 根据参数q查询分页 
	* @param page 页数
	* @param rows 行数
	* @param q 查询条件
	* @author dtl 
	* @date 2017年2月8日
	*/
	List<SysDepartment> getPageByQ(String page, String rows, String q);
	/** 根据参数q查询分页
	* @Title: getTotalByQ 
	* @Description: 根据参数q查询分页
	* @param q 查询条件
	* @author dtl 
	* @date 2017年2月8日
	*/
	int getTotalByQ(String q);
	/**
	 * 根据权限，和用户，获取他所有关联科室
	 * @Author zxh
	 * @time 2017年5月24日
	 * @param menutype
	 * @param usercode
	 * @return
	 */
	List<SysDepartment> getDeptByMenutypeAndUserCode(String menutype, String usercode);
	
}
