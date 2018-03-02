package cn.honry.outpatient.grade.dao;

import java.util.List;

import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * @className：GradeDAO 
 * @Description：  挂号级别DAO 
 * @Author：wujiao
 * @CreateDate：2015-6-18 下午01:49:41  
 * @Modifier：wujiao
 * @ModifyDate：2015-6-18 下午01:49:41  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface GradeDAO extends EntityDao<RegisterGrade>{
	
	/**  
	 *  
	 * @Description：  挂号级别分页查询 - 获得列表
	 * @Author：wujiao
	 * @CreateDate：2015-6-18 下午01:45:11  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-18 下午01:45:11  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterGrade> getPage(RegisterGrade entity, String page, String rows);

	/**  
	 *  
	 * @Description：  挂号级别分页查询 - 获得统计总条数
	 * @Author：wujiao
	 * @CreateDate：2015-6-18 下午01:45:11  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-18 下午01:45:11  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(RegisterGrade entity);

	/**  
	 *  
	 * @Description：  挂号级别查询全部信息
	 * @Author：wujiao
	 * @CreateDate：2015-6-18 下午01:45:11  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-18 下午01:45:11  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public List<RegisterGrade> queryAll();
	
	/**  
	 *  
	 * @Description：  挂号级别 - 添加&修改
	 * @Author：wujiao
	 * @CreateDate：2015-6-18 下午01:45:11  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-18 下午01:45:11  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public boolean saveOrUpdate( String stackInfosJson);

	/**  
	 *  
	 * @Description：  挂号级别 - 获得下拉列表
	 * @Author：wujiao
	 * @CreateDate：2015-6-18 下午01:45:11  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-18 下午01:45:11  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterGrade> getCombobox(String time);
	/**   
	*  
	* @description：查询修改的那一条级别信息
	* @author：ldl
	* @createDate：2015-10-15 上午09:13:36  
	* @modifyRmk：  
	* @version 1.0
	*/
	List<RegisterGrade> findGradeEdit(String id);
	/**   
	*  
	* @description：唯一验证
	* @author：ldl
	* @createDate：2015-11-4 上午09:13:36  
	* @modifyRmk：  
	* @version 1.0
	*/
	List<RegisterGrade> findGradeSize(String id);
	
	public Long getMaxOrder();
	
}
