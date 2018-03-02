package cn.honry.inner.system.userMenuDataJuris.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysUserMenuDatajuris;
import cn.honry.base.service.BaseService;
import cn.honry.inner.vo.AreaVo;
import cn.honry.inner.vo.DeptListVO;

/**  
 *  
 * @className：DataJurisInInterService
 * @Description：  用户栏目数据权限维护接口
 * @Author：aizhonghua
 * @CreateDate：2017-6-23 下午18:59:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-6-23 下午18:59:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface DataJurisInInterService  extends BaseService<SysUserMenuDatajuris>{
	
	/**  
	 *  
	 * 根据栏目别名及科室类别获得用户栏目权限（科室）
	 * @Author：marongbin
	 * @CreateDate：
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-28 下午18:59:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<DeptListVO> queryDataJurisByMenuAndType(String menuAlias,String deptTypes);
	
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
	List<AreaVo> queryDataJurisAreaByMenu(String menuAlias);
	
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
	Map<String,Object> queryDataJurisByMenu(String menuAlias,String deptTypes);
	
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
}
