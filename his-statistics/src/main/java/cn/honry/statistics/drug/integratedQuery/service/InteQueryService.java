package cn.honry.statistics.drug.integratedQuery.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.drug.integratedQuery.vo.InteQueryVO;
import cn.honry.utils.TreeJson;
/**  
 *  药房药库综合查询Service接口
 * @Author:luyanshou
 * @version 1.0
 */
public interface InteQueryService {

	/**
	 * 获取药房药库树形列表
	 */
	public List<TreeJson> getDrugStore() throws Exception;
	
	/**
	 * 获取分页数据
	 * @param vo 查询条件 
	 * @param kind 汇总类型(0-按药品;1-按单据;2-按部门)
	 * @param type 查询类别(0-入库;1-出库;2-盘点;3-调价)
	 * @param firstResult 起始位置
	 * @param maxResults 每页显示的记录数
	 * @throws Exception 
	 */
	public Map getListByPage(InteQueryVO vo,int kind, int type,int firstResult,int maxResults) throws Exception;
	
	/**
	 * 获取字段名称
	 * @param kind 汇总类型(0-按药品;1-按单据;2-按部门)
	 * @param type 查询类别(0-入库;1-出库;2-盘点;3-调价)
	 */
	public Map<String,String> getName(int kind, int type) throws Exception;
	
	/**
	 * 获取列字段
	 * @param kind 汇总类型(0-按药品;1-按单据;2-按部门)
	 * @param type 查询类别(0-入库;1-出库;2-盘点;3-调价)
	 */
	public String[] getfields(int kind,int type) throws Exception;
	
	/**
	 * 获取公司id和名称
	 */
	public List<Map<String,String>> getCompanyName() throws Exception;
	
	/**
	 * 查询科室信息
	 */
	public List<SysDepartment> getDeptIdName() throws Exception;
}
