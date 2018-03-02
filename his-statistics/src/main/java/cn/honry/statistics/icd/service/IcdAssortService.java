package cn.honry.statistics.icd.service;

import java.util.List;

import cn.honry.statistics.icd.vo.IcdAssortTree;
import cn.honry.statistics.icd.vo.IcdAssortVo;

public interface IcdAssortService {
	
	public List<IcdAssortTree> icdTree(String parent_Id);

	public void addIcdAssort(String assort_Name, String parent_Id);

	public List<IcdAssortVo> findIcdList(String page, String rows,String icdCode);
	
	public Integer findIcdCount(String page, String rows, String icdCode);

	public void updateIcdSorrt(String icdId, String assortId);

	/**
	 * 根据icd分类id查询icd code集合
	 *
	 * @param icdAssortId icd分类id
	 * @return icd code集合
	 */
	List<String> queryIcdCodesByIcdAssortId(String icdAssortId);

	/**
	 * <p>根据父id加载tree列表 </p>
	 *
	 * @param id
	 * @param assortName
	 * @return
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月15日 下午1:40:34
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月15日 下午1:40:34
	 * @ModifyRmk:
	 * @version: V1.0
	 * @throws:
	 */
	public List<IcdAssortTree> icdTree(String id, String assortName);


	/**
	 * 根据icd分类id查询分类名称
	 *
	 * @param icdAssortId icd分类id
	 * @return 分类名称
	 */
	String queryIcdAssortNameByIcdAssortId(String icdAssortId);

	/**
	 * 根据icd id查询icd名称
	 *
	 * @param icdId icd id
	 * @return icd名称
	 */
	String queryIcdNameByIcdId(String icdId);

	/**
	 * 根据icd id查询icd code
	 *
	 * @param icdId icd id
	 * @return icd code
	 */
	String queryIcdCodeByIcdId(String icdId);

	/**
	 * 根据icd code查询icd 名称
	 *
	 * @param icdCode icd code
	 * @return icd 名称
	 */
	String queryIcdNameByIcdCode(String icdCode);

	/**
	 * 查询所有的icd code
	 *
	 * @return icd code
	 */
	List<String> queryAllIcdCodes();

	/**
	 * 模糊查询icd code
	 *
	 * @return icd code
	 */
	List<String> queryIcdCodeByLike(String where);
	
}
