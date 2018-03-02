package cn.honry.statistics.icd.dao;

import java.util.List;

import cn.honry.statistics.icd.vo.IcdAssortTree;
import cn.honry.statistics.icd.vo.IcdAssortVo;

public interface IcdAssortDao {

	public List<IcdAssortTree> icdTree(String parent_Id);

	public void addIcdAssort(IcdAssortVo icdAssortVo);

	public List<IcdAssortVo> findIcdList(String page, String rows,String icdCode);
	
	public Integer findIcdCount(String page, String rows, String icdCode);

	public void updateIcdSorrt(String icdId, String assortId);

	public void updateParentNode(String parent_Id);

	/**
	 * <p>根据父分类id查询子分类列表</p>
	 *
	 * @param id
	 * @return
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月15日 下午2:26:53
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月15日 下午2:26:53
	 * @ModifyRmk:
	 * @version: V1.0
	 * @throws:
	 */
	List<String> findIcdAssortIds(String id);

	/**
	 * <p>根据icd分类id查询icd诊断码列表 </p>
	 *
	 * @param list icd的分类id的list集合
	 * @return
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月15日 下午1:48:28
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月15日 下午1:48:28
	 * @ModifyRmk:
	 * @version: V1.0
	 * @throws:
	 */
	List<IcdAssortTree> findIcdAssortTreeByAssortId(List<String> list);

	/**
	 * <p>根据父id加载tree列表包含树下的icd </p>
	 *
	 * @param id
	 * @param assortName
	 * @return
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月15日 下午3:43:09
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月15日 下午3:43:09
	 * @ModifyRmk:
	 * @version: V1.0
	 * @throws:
	 */
	List<IcdAssortTree> findSons(String id, String assortName);

	List<String> findIcdCodesByAssortId(List<String> list);

	/**
	 * 根据icdAssortId查询直属子分类id
	 *
	 * @param icdAssortId 病种分类id
	 * @return 病种分类的子分类
	 */
	List<String> querySubIcdAssortIdsByIcdAssortId(String icdAssortId);

	/**
	 * 根据icdAssortId查询直属icd id
	 *
	 * @param icdAssortId 病种分类id
	 * @return 病种分类的子分类
	 */
	List<String> queryIcdIdsByIcdAssortId(String icdAssortId);


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
	 * @return icd code
	 */
	List<String> queryAllIcdCodes();

	/**
	 * 模糊查询icd code
	 * @return icd code
	 */
	List<String> queryIcdCodeByLike(String where);
	
}
