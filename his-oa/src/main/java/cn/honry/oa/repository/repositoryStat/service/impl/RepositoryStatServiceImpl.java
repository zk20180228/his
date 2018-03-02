package cn.honry.oa.repository.repositoryStat.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.oa.repository.repositoryStat.dao.RepositoryStatDao;
import cn.honry.oa.repository.repositoryStat.service.RepositoryStatService;
import cn.honry.oa.repository.repositoryStat.vo.RepositoryStatVo;

@Service("repositoryStatService")
@Transactional
@SuppressWarnings({"all"})
public class RepositoryStatServiceImpl implements RepositoryStatService{
	@Autowired
	@Qualifier(value = "repositoryStatDao")
	private RepositoryStatDao repositoryStatDao;
	
	
	/**  
	 * 
	 * 知识库统计  科室
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月18日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月18日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	@Override
	public List<RepositoryStatVo> queryRepositoryStatKS(String deptCode,String menuAlias,String page,String rows) {
		List<RepositoryStatVo> list=new ArrayList<RepositoryStatVo>();
			list=repositoryStatDao.queryRepositoryStatKS(deptCode,menuAlias,page,rows);
		return list;
	}


	@Override
	public int getTotalKS(String deptCode, String menuAlias) {
		return repositoryStatDao.getTotalKS(deptCode,menuAlias);
	}
	
	/**  
	 * 
	 * 知识库统计  作者
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月18日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月18日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	@Override
	public List<RepositoryStatVo> queryRepositoryStatZZ(String deptCode,String menuAlias,String page,String rows) {
		List<RepositoryStatVo> list=new ArrayList<RepositoryStatVo>();
		list=repositoryStatDao.queryRepositoryStatZZ(deptCode,menuAlias,page,rows);
		return list;
	}
	
	
	@Override
	public int getTotalZZ(String deptCode, String menuAlias) {
		return repositoryStatDao.getTotalZZ(deptCode,menuAlias);
	}

	/**  
	 * 
	 * 知识库统计  阅读量
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月20日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月20日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	@Override
	public List<RepositoryStatVo> queryRepositoryStatYDL(String deptCode,String menuAlias, String page, String rows) {
		List<RepositoryStatVo> list=new ArrayList<RepositoryStatVo>();
		list=repositoryStatDao.queryRepositoryStatYDL(deptCode,menuAlias,page,rows);
		return list;
	}


	@Override
	public int getTotalYDL(String deptCode, String menuAlias) {
		return repositoryStatDao.getTotalYDL(deptCode,menuAlias);
	}


}
