package cn.honry.inner.baseinfo.district.service;

import java.io.InputStream;
import java.util.List;

import cn.honry.base.bean.model.District;
import cn.honry.base.service.BaseService;
import cn.honry.utils.TreeJson;


/**  
 *  
 * @className：DistrictAction
 * @Description：  DistrictAction
 * @Author：wujiao
 * @CreateDate：2015-10-29 上午11:56:31  
 * @Modifier：wujiao
 * @ModifyDate：2015-10-29 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface DistrictInnerService extends BaseService<District>{

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
	List<District> getPage(String page, String rows,
			District district);

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
	 * @Description：  获得栏目树
	 * @Author：wujiao
	 * @CreateDate：2015-11-2 上午12:16:53  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-11-2 上午12:16:53  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<TreeJson> queryDistrictTree(String parId);
	
	/**  
	 *  
	 * @Description：  获得栏目树三级联动
	 * @Author：zpty
	 * @CreateDate：2015-12-16 上午12:16:53  
	 * @version 1.0
	 *
	 */
	List<District> queryDistricttreeSJLD(Integer ld, String parId);
	
	/**  
	 *  
	 * @Description：  获得栏目树三级联动查询
	 * @Author：zpty
	 * @CreateDate：2015-12-16 上午12:16:53  
	 * @version 1.0
	 *
	 */
	List<District> queryDistricttreeSJLDCX( String parId);
	
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
	 *  
	 * @Description：导入行政代码
	 * @Author：zpty
	 * @CreateDate：2015-11-23上午11:57:39  
	 * @version 1.0
	 *
	 */
	String anaylzeExcelXls(InputStream inputStream);
	
	/**
	 * 获取某一城市的所有下级
	 * @Author：lsy
	 * @param code 城市代码
	 * @return
	 */
	List<District> queryByCityCode(String code);
	
	/** 根据县级代码获得地址全称（“，”分割）
	* @Title: getDistriByCountyCode 根据县级代码获得地址全称（“，”分割）
	* @Description: 根据县级代码获得地址全称（“，”分割）
	* @param code 县代码
	* @author dtl 
	* @date 2017年5月15日
	*/
	String getDistriByCountyCode(String code);

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
