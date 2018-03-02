package cn.honry.statistics.drug.anesthetic.service;

import java.util.List;

import cn.honry.statistics.drug.anesthetic.vo.Anestheticvo;
import cn.honry.utils.FileUtil;
@SuppressWarnings({"all"})
public interface AnestheticService{
	/** 分页方法
	* @param login 开始时间
	* @param end 结束时间
	* @param drug 药品类型
	* @param deptId 科室code
	* @param rows 行数
	* @param page 页数
	* @param flag 是否查询所有
	* @author dtl 
	* @date 2017年3月25日
	*/
	List<Anestheticvo> getAnestheList(String login,String end,String drug,String deptId, String rows, String page, String flag);
	/**
	 *
	 * @Description：获取当前登陆科室
	 * @Author：zhangjin
	 * @CreateDate：2016年6月22日
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	String getDeptName(String deptId);
	/**
	 * @Description:导出 
	 * @Author： zhangjin @CreateDate： 2016-6-24
	 * @param @throws Exception
	 * @return void
	 * @version 1.0
	 **/
	List<Anestheticvo> queryInvLogExp(String login, String end, String drug, String deptId);

	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: zhangjin
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	**/
	FileUtil export(List<Anestheticvo> list, FileUtil fUtil);
	/** <p>获得条数</p>
	* @param login 开始时间
	* @param end 结束时间
	* @param drug 药品类型
	* @param deptId 科室code
	* @author dtl 
	* @date 2017年3月25日
	*/
	Integer getAnestheTotal(String login, String end, String drug, String deptId);
	/**  
	 * 
	 * 初始化数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年9月26日 下午7:21:45 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年9月26日 下午7:21:45 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public void init_MZJSYPTJ(String begin,String end,Integer type) throws Exception;
	/**  
	 * 
	 * 从mongondb中获取数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年9月28日 上午9:29:52 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年9月28日 上午9:29:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public List<Anestheticvo> queryAnestheticvo(String begin,String end, String drug, String deptId, String rows, String page);

}
