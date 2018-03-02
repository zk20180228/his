package cn.honry.outpatient.grade.service;

import java.util.List;

import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.service.BaseService;


/**  
 *  
 * @className：GradeService 
 * @Description：  挂号级别Service
 * @Author：wujiao
 * @CreateDate：2015-6-18 下午01:44:48  
 * @Modifier：wujiao
 * @ModifyDate：2015-6-18 下午01:44:48  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface GradeService extends BaseService<RegisterGrade>{

	/**  
	 *  
	 * @Description：  挂号级别添加&修改
	 * @Author：wujiao
	 * @CreateDate：2015-6-18 下午01:45:11  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-18 下午01:45:11  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void saveOrUpdagrade(RegisterGrade registerGrade);
	
	/**  
	 *  
	 * @Description：  挂号级别查询全部
	 * @Author：wujiao
	 * @CreateDate：2015-6-18 下午01:45:11  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-18 下午01:45:11  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterGrade> queryAll();
	
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
	List<RegisterGrade> getPage(String page, String rows,RegisterGrade registerGrade);

	/**  
	 *  
	 * @Description：  挂号级别分页查询 - 获得总条数
	 * @Author：wujiao
	 * @CreateDate：2015-6-18 下午01:45:11  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-18 下午01:45:11  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(RegisterGrade registerGrade);
	
	/**  
	 *  
	 * @Description：  挂号级别保存
	 * @Author：wujiao
	 * @CreateDate：2015-6-18 下午01:45:11  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-18 下午01:45:11  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public boolean save(String stackInfosJson);
	
	/**  
	 *  
	 * @Description：  挂号级别删除
	 * @Author：wujiao
	 * @CreateDate：2015-6-18 下午01:45:11  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-18 下午01:45:11  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void del(String id);
	
	/**  
	 *  
	 * @Description：  挂号级别获得下拉列表
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

}
