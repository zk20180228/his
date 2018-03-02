package cn.honry.statistics.drug.integratedQuery.dao;

import java.util.List;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.drug.integratedQuery.vo.InteQueryVO;
/**  
 *  药房药库综合查询DAO接口
 * @Author:luyanshou
 * @version 1.0
 */
public interface InteQueryDao {

	/**
	 * 获取药房药库信息
	 */
	public List<Object[]> getDrugStore() throws Exception;
	
	/**
	 * 分页 查询
	 * @param vo 查询条件 
	 * @param kind 汇总类型(0-按药品;1-按单据;2-按部门)
	 * @param type 查询类别(0-入库;1-出库;2-盘点;3-调价)
	 * @param firstResult 起始位置
	 * @param maxResults 每页显示的记录数
	 * @return
	 */
	public List getListByPage(InteQueryVO vo,int kind, int type,int firstResult,int maxResults) throws Exception;
	
	/**
	 * 统计记录数
	 * @param vo 查询条件
	 * @param kind 汇总类型(0-按药品;1-按单据;2-按部门)
	 * @param type 查询类别(0-入库;1-出库;2-盘点;3-调价)
	 * @return
	 */
	public Integer getCount(InteQueryVO vo,int kind,int type) throws Exception;
	
	/**
	 * 获取公司id和名称
	 */
	public List<Object[]> getCompanyName() throws Exception;
	
	/**
	 * 查询科室信息
	 */
	public List<SysDepartment> getDept() throws Exception;
}
