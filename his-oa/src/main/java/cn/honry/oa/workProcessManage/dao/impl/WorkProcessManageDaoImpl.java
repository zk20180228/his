package cn.honry.oa.workProcessManage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.oa.workProcessManage.dao.WorkProcessManageDao;
import cn.honry.oa.workProcessManage.vo.ProcessInfoVo;
import cn.honry.oa.workProcessManage.vo.WorkProcessManageVo;

@Repository("workProcessManageDao")
public class WorkProcessManageDaoImpl implements WorkProcessManageDao {
		
		//扩展工具类,支持参数名传参
		@Resource
		private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
		
		@Override
		public List<WorkProcessManageVo> fatherMenuList() throws Exception {
			
			//拼接sql
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT ");
			sb.append(" TO_CHAR(T.MENU_CODE) AS MENUCODE, ");
			sb.append(" T.MENU_NAME AS MENUNAME");
			sb.append(" FROM ");
			sb.append(" T_OA_WORKPROMANGE_MENU T ");
			sb.append(" WHERE ");
			sb.append(" T.DEL_FLAG = 0 ");
			sb.append(" AND T.STOP_FLAG = 0 ");
			sb.append(" AND T.MENU_HAVESON = 1 ");
			sb.append(" ORDER BY ");
			sb.append(" T.MENU_CODE ASC");
			
			List<WorkProcessManageVo> list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<WorkProcessManageVo>(WorkProcessManageVo.class));
			
			if(list==null){
				list= new ArrayList<WorkProcessManageVo>();
			}
			
			return list;
		}
	
		@Override
		public List<WorkProcessManageVo> spreadSonMenu(String menuCode)throws Exception {
			
			//拼接sql
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT ");
			sb.append(" T.MENU_CODE AS MENUCODE, ");
			sb.append(" TO_CHAR(T.MENU_NAME) AS MENUNAME ");
			sb.append(" FROM ");
			sb.append(" T_OA_WORKPROMANGE_MENU T ");
			sb.append(" WHERE ");
			sb.append(" T.DEL_FLAG = 0 ");
			sb.append(" AND T.STOP_FLAG = 0 ");
			sb.append(" AND T.MENU_HAVESON = 0 ");
			sb.append(" AND T .MENU_PARENTCODE = '"+menuCode+"' ");
			sb.append(" ORDER BY ");
			sb.append(" T.MENU_CODE ASC");
			
			List<WorkProcessManageVo> list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<WorkProcessManageVo>(WorkProcessManageVo.class));
			
			if(list==null){
				list= new ArrayList<WorkProcessManageVo>();
			}
			
			return list;
		}
	
		@Override
		public List<ProcessInfoVo> queryProcessList(String menuCode)throws Exception {
			
			//拼接sql
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT ");
			sb.append("	T.ID AS ID, ");
			sb.append(" T.TITLE AS TITLE, ");
			sb.append(" TO_CHAR (T.CREATETIME,'yyyy-mm-dd hh24:mi:ss') AS CREATETIME ");
			sb.append(" FROM ");
			sb.append(" T_OA_PROCESSINFO T ");
			sb.append(" WHERE ");
			sb.append(" T.DEL_FLAG = 0 ");
			sb.append(" AND T.STOP_FLAG = 0 ");
			sb.append(" AND T.MENUCODE = '"+menuCode+"'");
			sb.append(" ORDER BY ");
			sb.append(" T.CREATETIME DESC ");
			
			List<ProcessInfoVo> list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<ProcessInfoVo>(ProcessInfoVo.class));
			
			if(list==null){
				list= new ArrayList<ProcessInfoVo>();
			}
			
			return list;
		}

		@Override
		public String queryProcessInfo(String processId) throws Exception {
			
			//拼接sql
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT ");
			sb.append("	T.PROCESSEXPLAIN AS PROCESSEXPLAIN ");
			sb.append(" FROM ");
			sb.append(" T_OA_PROCESSINFO T ");
			sb.append(" WHERE ");
			sb.append(" T.DEL_FLAG = 0 ");
			sb.append(" AND T.STOP_FLAG = 0 ");
			sb.append(" AND T.ID = '"+processId+"'");
			List<ProcessInfoVo> list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<ProcessInfoVo>(ProcessInfoVo.class));
			
			if(list!=null){
				return list.get(0).getProcessExplain();
			}else{
				return null;
			}
		
		}



}
