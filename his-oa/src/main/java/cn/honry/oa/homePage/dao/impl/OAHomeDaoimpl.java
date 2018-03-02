package cn.honry.oa.homePage.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.homePage.dao.OAHomeDao;
import cn.honry.oa.homePage.vo.ArticleVo;
import cn.honry.oa.homePage.vo.MenuVo;
import cn.honry.oa.workProcessCount.vo.WorkProcessCountVo;
import cn.honry.utils.HisParameters;

@Repository("oAHomeDao")
public class OAHomeDaoimpl implements OAHomeDao{

	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**
	 * 
	 * <p>查询栏目列表，当menuCode为空时，查询的是一级栏目列表 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年7月25日 下午7:43:48 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月25日 下午7:43:48 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 *
	 */
	public List<MenuVo> MenuList(String menuCode) throws Exception{
		if(StringUtils.isBlank(menuCode)){
			menuCode="1";
		}
		String sql =" SELECT  T.MENU_NAME AS MENUNAME,T.MENU_CODE AS MENUCODE FROM T_OA_MENU T WHERE T.MENU_PARENTCODE = '"+menuCode+"' AND T.DEL_FLAG=0 AND T.STOP_FLAG=0 ORDER BY T.MENU_ORDER ASC ";
		
		List<MenuVo> list = namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper(MenuVo.class));
		
		return list;
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
		
		
		StringBuffer sb = new StringBuffer();
		Integer p=null;
		Integer r= null;
		if("true".equals(isMore)){//当时更多时，要分页
			p=Integer.parseInt(page);
			r= Integer.parseInt(rows);
			sb.append(" SELECT M.INFOID AS INFOID,M.INFOTITLE AS INFOTITLE,M.INFOPUBTIME AS INFOPUBTIME,M.IMGPATH AS IMGPATH FROM ( ");
		}
		
		sb.append(" SELECT ");
		sb.append(" T .INFO_ID AS infoid, ");
		sb.append(" T .INFO_TITLE AS infotitle, ");
		sb.append(" T .INFO_PUBTIME AS infopubtime, ");
		sb.append(" T.INFO_TITLEIMG AS IMGPATH ");
		if("true".equals(isMore)){//当时更多时，要分页
			sb.append(" , ROWNUM AS IDS ");
		}
		sb.append(" FROM ");
		sb.append(" T_OA_INFORMATION T ");
		sb.append(" WHERE ");
		sb.append(" T.INFO_MENUID =TRIM('"+menuCode+"') ");
		sb.append(" AND T.INFO_PUBFLAG=1 ");
		sb.append(" AND T.DEL_FLG=0 ");
		sb.append(" AND T.STOP_FLG=0 ");
		if("true".equals(isMore)){
			sb.append(" AND ROWNUM <= "+p*r);
		}else if("home".equals(isMore)){//如果是true,那么默认显示前6条
			sb.append(" AND ROWNUM <= 6 ");
		}else{
			sb.append(" AND ROWNUM <= 8 ");
		}
		sb.append(" ORDER BY ");
		sb.append(" T .INFO_PUBTIME DESC ");
		if("true".equals(isMore)){//当时更多时，要分页
			sb.append(" ) M WHERE M.IDS>"+(p-1)*r);
		}
		
		List<ArticleVo> list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(ArticleVo.class));
		
		return list;
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
		
		Integer p = Integer.parseInt(page==null?"1":page);
		Integer r = Integer.parseInt(rows==null?"20":rows);
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT ");
		sb.append(" M.INFOID AS INFOID, ");
		sb.append(" M.INFOTITLE AS INFOTITLE, ");
		sb.append(" M.INFOPUBTIME AS INFOPUBTIME, ");
		sb.append(" M.INFOKEYWORD AS  INFOKEYWORD, ");
		sb.append(" M.INFOEDITOR AS INFOEDITOR, ");
		sb.append(" M.UPDATEUSER AS UPDATEUSER, ");
		sb.append(" M.UPDATETIME AS UPDATETIME, ");
		sb.append(" M.OUTTIME AS OUTTIME ");
		sb.append(" FROM ( ");
		
		sb.append(" SELECT ");
		sb.append(" T.INFO_ID AS INFOID, ");
		sb.append(" T.INFO_TITLE AS INFOTITLE, ");
		sb.append(" T.INFO_PUBTIME AS INFOPUBTIME, ");
		sb.append(" T.INFO_KEYWORD AS  INFOKEYWORD, ");
		sb.append(" T.INFO_EDITOR AS INFOEDITOR, ");
		sb.append(" T.UPDATEUSER AS UPDATEUSER, ");
		sb.append(" T.UPDATETIME AS UPDATETIME, ");
		sb.append(" T.OUTTIME AS OUTTIME, ");
		sb.append(" ROWNUM AS IDS ");
		sb.append(" FROM ");
		sb.append(" T_OA_INFORMATION T ");
		sb.append(" WHERE  ");
		sb.append(" T.INFO_PUBFLAG=1 ");
		sb.append(" AND T.DEL_FLG=0 ");
		sb.append(" AND T.STOP_FLG=0 ");
		if(StringUtils.isNotBlank(menuCode)){
			sb.append(" AND INFO_MENUID =TRIM('"+menuCode+"')");
		}
		if(StringUtils.isNotBlank(infoTitle)){
			sb.append("AND T.INFO_TITLE LIKE trim('%"+infoTitle+"%')");
		}
		sb.append(" AND ROWNUM <="+p*r);
		sb.append(" ORDER BY ");
		sb.append(" T.INFO_PUBTIME DESC ");
	
		sb.append(" ) M ");
		sb.append(" WHERE ");
		sb.append(" M.IDS >"+(p-1)*r);
		
		List<ArticleVo> list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(ArticleVo.class));
		
		return list;
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
	public Integer pageCount(String menuCode,String infoTitle) throws Exception {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT ");
		sb.append(" COUNT(1) AS TOTALPAGE");
		sb.append(" FROM ");
		sb.append(" T_OA_INFORMATION T ");
		sb.append(" WHERE ");
		sb.append(" T.INFO_PUBFLAG=1 ");
		sb.append(" AND T.DEL_FLG=0 ");
		sb.append(" AND T.STOP_FLG=0 ");
		if(StringUtils.isNotBlank(menuCode)){
			sb.append(" AND T.INFO_MENUID =TRIM('"+menuCode+"') ");
		}
		if(StringUtils.isNotBlank(infoTitle)){
			sb.append(" AND T.INFO_TITLE like TRIM('%"+infoTitle+"%') ");
		}
		
		
		List<ArticleVo> list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(ArticleVo.class));
		
		
		return list.get(0).getTotalPage();
	}

	
	
	
	
	
}
