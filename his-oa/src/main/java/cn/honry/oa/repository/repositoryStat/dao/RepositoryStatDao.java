package cn.honry.oa.repository.repositoryStat.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.oa.repository.repositoryStat.vo.RepositoryStatVo;

public interface RepositoryStatDao extends EntityDao<RepositoryStatVo>{
	/**  
	 * 
	 * 知识库统计
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月18日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月18日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	public List<RepositoryStatVo> queryRepositoryStatKS( String deptCode, String menuAlias,String page,String rows);
	public List<RepositoryStatVo> queryRepositoryStatZZ( String deptCode, String menuAlias,String page,String rows);
	public List<RepositoryStatVo> queryRepositoryStatYDL(String deptCode,String menuAlias, String page, String rows);

	/**  
	 * 
	 * 知识库统计   总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月18日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月18日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	public int getTotalKS(String deptCode, String menuAlias);
	public int getTotalZZ(String deptCode, String menuAlias);
	public int getTotalYDL(String deptCode, String menuAlias);

}
