package cn.honry.inner.system.userMenuDataJuris.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysUserMenuDatajuris;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.AreaVo;
import cn.honry.inner.vo.DeptVO;

/**  
 *  
 * @className：DataJurisInInterDAO
 * @Description：  用户栏目数据权限维护接口
 * @Author：aizhonghua
 * @CreateDate：2017-6-23 下午18:59:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-6-23 下午18:59:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface DataJurisInInterDAO extends EntityDao<SysUserMenuDatajuris>{

	/**  
	 *  
	 * 根据用户账户和栏目别名获得用户院区权限
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-28 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-28 下午18:59:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<String> getHlAreaList(String account, String menuAlias);
	
	/**  
	 *  
	 * 根据栏目别名得用户栏目权限（院区）
	 * @Author：aizhonghua
	 * @CreateDate：2017-7-12 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-7-12 下午18:59:31   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<AreaVo> getDataJurisAreaList(String account, String menuAlias);

	/**  
	 *  
	 * 根据用户账户、栏目别名及院区获得用户科室权限<br>
	 * 用户账户、栏目别名、院区都不为空时以院区为准<br>
	 * 如果院区不为空，获得院区下的全部科室<br>
	 * 如果院区为空，通过用户账户及栏目别名查询用户授权的科室<br>
	 * 院区为空时用户账户或栏目别名不能为空否则返回null<br>
	 * 用户账户和栏目别名为空时，院区不能为空否则返回null<br>
	 * 用户账户、栏目别名、院区都为空时返回null
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-28 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-28 下午18:59:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<DeptVO> getJurisDept(String userAcc, String menuAlias,List<String> hlAreaList);

	/**  
	 *  
	 * 根据栏目别名及科室类别获得用户栏目权限<br>
	 * 无权限返回null<br>
	 * (Integer)map.get("type")1院区级2科室级<br>
	 * (List<String>)map.get("code")院区或科室List编码
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-28 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-28 下午18:59:31  
	 * @ModifyRmk：  
	 * @param:menuAlias栏目别名
	 * @param:deptTypes科室类别
	 * @version 1.0
	 *
	 */
	Map<String, Object> queryDataJurisByMenu(String menuAlias, String deptTypes);

	/**  
	 *  
	 * 根据栏目别名及用户账户获得全部科室权限<br>
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-28 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-28 下午18:59:31  
	 * @ModifyRmk：  
	 * @param:menuAlias栏目别名
	 * @param:deptTypes科室类别
	 * @version 1.0
	 *
	 */
	List<SysDepartment> getJurisDeptList(String menuAlias, String userAcc);
	
	/**  
	 *  
	 * 获得用户栏目科室SQL用于子查询<br>
	 * menuAlias:栏目占位符名称，默认menuAlias<br>
	 * userAcc：用户占位符名称，默认userAcc
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-28 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-28 下午18:59:31  
	 * @ModifyRmk：  
	 * @param:menuAlias栏目别名
	 * @param:deptTypes科室类别
	 * @version 1.0
	 *
	 */
	public String getJurisDeptSql(String menuAlias,String userAcc);
	
	/**  
	 *  
	 * 获得用户栏目科室HQL用于子查询<br>
	 * menuAlias:栏目占位符名称，默认menuAlias<br>
	 * userAcc：用户占位符名称，默认userAcc
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-28 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-28 下午18:59:31  
	 * @ModifyRmk：  
	 * @param:menuAlias栏目别名
	 * @param:deptTypes科室类别
	 * @version 1.0
	 *
	 */
	public String getJurisDeptHql(String menuAlias,String userAcc);


}
