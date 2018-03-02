package cn.honry.oa.homePage.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.oa.homePage.dao.OAHomeDao;
import cn.honry.oa.homePage.service.OAHomeService;
import cn.honry.oa.homePage.vo.ArticleVo;
import cn.honry.oa.homePage.vo.MenuVo;

@Service("oAHomeService")
@Transactional
public class OAHomeServiceImpl implements OAHomeService{
	
	@Resource
	private OAHomeDao oAHomeDao;
	
	@Override
	public List<MenuVo> MenuList(String menuCode) throws Exception {
		
		return oAHomeDao.MenuList(menuCode);
	}

	/**
	 * 
	 * <p>根据栏目的code,查询文章列表，当isMore='true'是，代表查询更多，这时会分页，当isMore='false'时，默认显示前8条</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年7月26日 上午11:14:46 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月26日 上午11:14:46 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuCode 栏目的code
	 * @param isMore 是否是更多项
	 * @param page 当前页
	 * @param rows 每页显示的记录数
	 * @return
	 * @throws:
	 *
	 */
	public List<ArticleVo> articleList(String menuCode,String isMore,String page,String rows) throws Exception {
		
		return oAHomeDao.articleList(menuCode,isMore,page, rows);
	}

	/**
	 * 
	 * <p>根据栏目的code,查询文章列表，当articleList中的isMore='true'时，代表查询更多，这时会分页，查询总记录数</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年7月26日 上午11:14:46 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月26日 上午11:14:46 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuCode 栏目的code
	 * @return
	 * @throws:
	 *
	 */
	public Integer pageCount(String menuCode,String infoTitle) throws Exception{
		
		return oAHomeDao.pageCount(menuCode,infoTitle);
	}
	
	/**
	 * 
	 * <p>查询文章列表 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年7月26日 下午5:37:14 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月26日 下午5:37:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuCode 栏目的code
	 * @param infoTitle 文章的标题
	 * @param page 当前页
	 * @param rows 每页显示的记录熟数
	 * @return
	 * @throws Exception
	 * @throws:
	 *
	 */
	public List<ArticleVo> infoList(String menuCode,String infoTitle,String page,String rows)throws Exception{
		
		return oAHomeDao.infoList(menuCode,infoTitle,page, rows);
	}
	
	
}
