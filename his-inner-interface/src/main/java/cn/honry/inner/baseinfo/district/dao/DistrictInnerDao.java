package cn.honry.inner.baseinfo.district.dao;

import java.util.List;

import cn.honry.base.bean.model.District;
import cn.honry.base.bean.model.EmployeeBlacklist;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface DistrictInnerDao extends EntityDao<District>{
	
	
	
	/**  
	 *  
	 * @Description：  分页查询－获得列表数据
	 * @Author：wujiao
	 * @CreateDate：2015-10-29 上午11:59:48  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-10-29上午11:59:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */

	List<District> getPage(String page, String rows, District district);
	/**  
	 *  
	 * @Description：  分页查询－获得总条数
	 * @Author：wujiao
	 * @CreateDate：2015-10-29 上午11:59:48  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-10-29上午11:59:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(District district);
	
	/**  
	 *  
	 * @Description：  通过父id和所有父级获得所有子级
	 * @Author：wujiao
	 * @CreateDate：2015-10-31 下午04:47:03  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-10-31 下午04:47:03  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<District> getChildById(String id, String upperPath);
	/**  
	 *  
	 * @Description：  获得栏目树
	 * @Author：wujiao
	 * @CreateDate：2015-11-2 上午12:16:53  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-11-2 上午12:16:53  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<District> queryDistrictTree(String parId);
	
	/**  
	 *  
	 * @Description：  获得栏目树三级联动
	 * @Author：zpty
	 * @CreateDate：2015-12-16 上午12:16:53  
	 * @version 1.0
	 *
	 */
	List<District> queryDistrictTreeSJLD(Integer ld,String parId);
	
	/**  
	 *  
	 * @Description：  获得栏目树三级联动查询
	 * @Author：zpty
	 * @CreateDate：2015-12-16 上午12:16:53  
	 * @version 1.0
	 *
	 */
	List<District> queryDistrictTreeSJLDCX(String parId);
	
	/**  
	 *  
	 * @Description：  删除
	 * @Author：wujiao
	 * @CreateDate：2015-11-2 上午12:16:53   
	 * @Modifier：wujiao
	 * @ModifyDate：2015-11-2 上午12:16:53   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void delDis(String id);
	
	/**
	 * 根据某一城市的cityCode查询它的下级城市
	 * @param pid
	 * @return
	 */
	public List<District> queryByPid(String pid);
	/** 验证编码是否存在
	* @Title: vailCode 验证编码是否存在
	* @Description: 验证编码是否存在
	* @param disCode 编码
	* @param disId id
	* @return
	* @return Integer    返回类型 
	* @throws 
	* @author dtl 
	* @date 2017年6月28日
	*/
	Integer vailCode(String disCode, String disId);
}
