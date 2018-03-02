package cn.honry.statistics.finance.inoutstore.dao;

import java.util.List;

import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.inoutstore.vo.StoreResultVO;
import cn.honry.statistics.finance.inoutstore.vo.StoreSearchVo;

/**  
 *  住院药房入出库台账查询DAO接口
 * @Author:luyanshou
 * @version 1.0
 */
public interface InOutStoreDao {

	/**
	 * 查询药品名称和编码
	 */
	public List<StoreResultVO> getdrugInfo(String q) throws Exception;
	
	/**
	 * 查询入出库记录数据
	 */
	public List<StoreResultVO> getStoreData(List<String> tnL,StoreSearchVo vo,int firstResult,int page,int rows) throws Exception;
	
	/**
	 * 根据科室id查询科室名称
	 */
	public String getDeptName(String id) throws Exception;
	
	/**
	 * 根据id查询员工姓名
	 */
	public String getUserName(String id) throws Exception;
	/**
	 * 查询总量
	 */
	public Integer getCount(List<String> tnL,StoreSearchVo vo,int page,int rows) throws Exception;
	/**
	 * 查询最大最小日期
	 */
	public StatVo findMaxMin() throws Exception;
}
