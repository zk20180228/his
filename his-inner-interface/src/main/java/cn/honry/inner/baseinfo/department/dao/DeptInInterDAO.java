package cn.honry.inner.baseinfo.department.dao;

import java.util.List;

import cn.honry.base.bean.model.FictitiousContact;
import cn.honry.base.bean.model.FictitiousDept;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.UserLoginDept;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.baseinfo.department.vo.FicDeptVO;

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
@SuppressWarnings({"all"})
public interface DeptInInterDAO extends EntityDao<SysDepartment>{

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
	List<SysDepartment> findDeptsByUserId(String userId);

	/**  
	 * 获得部门树
	 * @Description:
	 * @Author：aizhonghua
     * @CreateDate：2016-4-20 下午09:44:35  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-4-20 下午09:44:35  
	 * @version 1.0
	 *
	 */
	List<SysDepartment> findTree(boolean b, String deptType);
	/** 
	* @Title: findFicDept 
	* @Description: 获取所有的虚拟科室
	* @param flag
	* @param deptTpye
	* @param deptDistrict 所属院区
	* @return
	* @return List<FictitiousDept>    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年5月16日
	*/
	List<FictitiousDept> findFicDept(boolean flag,String deptTpye,Integer deptDistrict,String hospitalId);
	/** 
	* @Title: findFicConDept 
	* @Description: 获取所有的科室
	* @param flag
	* @param deptTpye
	* @return
	* @return List<FictitiousContact>    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年5月16日
	*/
	List<FictitiousContact> findFicConDept(boolean flag,String deptTpye,String fictCode);
	
	/**  
	 *  
	 * @Description：角色用户--科室
	 * @Author：wujiao
	 * @CreateDate：2015-8-11 下午01:32:08 
	 * @Modifier：wujiao
	 * @ModifyDate：2015-8-11 下午01:32:08  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<SysDepartment> findDeptTree(String parentId);
	
	/**  
	 *  
	 * @Description：  查询挂号科室树
	 * @Author：aizhonghua
	 * @CreateDate：2015年12月23日 上午9:18:38  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015年12月23日 上午9:18:38  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<SysDepartment> treeDeptSchedule();
	/**
	 * 
	 * @Description 根据科室名称,五笔码，拼音码，自定义码，系统编码模糊查询
	 * @author  lyy
	 * @createDate： 2016年7月7日 下午2:28:39 
	 * @modifier lyy
	 * @modifyDate：2016年7月7日 下午2:28:39
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> getDeptList(String deptName);
	/**
	 *  查询全部科室
	 *  @author 涂川江
	 *  @CreateDate 2016/7/14
	 *  @version 1.0
	 */
	List<SysDepartment> queryAllDept();
	
	/**
	 * 根据科室编码查询 科室信息
	 * @param code 科室编码
	 * @return
	 */
	public SysDepartment getByCode(String code);

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
	 * @Description 
	 * @author  lyy
	 * @createDate： 2016年7月20日 上午11:58:46 
	 * @modifier lyy
	 * @modifyDate：2016年7月20日 上午11:58:46
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	SysDepartment getDeptCode(String deptCode);
	
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

}
