package cn.honry.mobile.updateVersion.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.MApkVersion;
import cn.honry.base.service.BaseService;

public interface UpdateVersionService extends BaseService<MApkVersion>{

	/**  
	 * 
	 * <p>分页查询版本升级信息</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月17日 下午1:45:36 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月17日 下午1:45:36 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param apkVersion
	 * @param rows
	 * @param page
	 * @return: List<MApkVersion> 当前页版本升级信息
	 *
	 */
	List<MApkVersion> getPagedVersionList(MApkVersion mApkVersion, String rows, String page) throws Exception;


	/**  
	 * 
	 * <p>获得分页总条数</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月17日 下午1:47:05 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月17日 下午1:47:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param apkVersion
	 * @return: int 分页总条数
	 *
	 */
	int getTotal(MApkVersion mApkVersion) throws Exception;
	
	/** 通过id删除版本信息
	* @param ids 版本ID
	* @author zxl 
	 * @throws Exception 
	* @date 2017年6月20日
	*/
	void delVersions(String ids) throws Exception;

	/** 保存或修改版本信息
	* @param mApkVersion 版本信息实体
	* @author zxl
	 * @return 
	 * @throws Exception 
	* @date 2017年6月20日
	*/
	Map<String, String> save(MApkVersion mApkVersion) throws Exception;

}
