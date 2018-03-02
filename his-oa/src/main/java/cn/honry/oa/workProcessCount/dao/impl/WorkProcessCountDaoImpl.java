package cn.honry.oa.workProcessCount.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.workProcessCount.dao.WorkProcessCountDao;
import cn.honry.oa.workProcessCount.vo.WorkProcessCountVo;

@Repository("workProcessCountDao")
public class WorkProcessCountDaoImpl extends HibernateEntityDao<WorkProcessCountVo> implements WorkProcessCountDao {

	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	
	@Override
	public List<WorkProcessCountVo> workList(String workFlag,String serialNumber, String workName, String page, String rows) {
		
		
		
		return null;
	}

	@Override
	public void addWork(String workFlag, WorkProcessCountVo workProcessCountVo) {
		
	}


	@Override
	public void hangUpRecoveryWork(String workFlag, String[] id) {
		
		
	}

	@Override
	public void takeBackEntrustWork(String workFlag, String[] id) {
		
		
	}

	@Override
	public void urgeDoWork(String workFlag, String[] id) {
		
		
	}

	@Override
	public void delWork(String workFlag, String[] id) {
		
		
	}

	@Override
	public void exportWork(String workFlag, String[] id) {
		
		
	}

	@Override
	public void hostWork(String workFlag, String[] id) {
		
		
	}

	@Override
	public void postilWork(String workFlag, String[] id) {
		
		
	}

	@Override
	public void hangUpwork(String workFlag, String[] id) {
		
		
	}

	@Override
	public void entrustWork(String workFlag, String[] id) {
		
		
	}


	@Override
	public List<String> getTaskExecutionId(String userId) {
		
		List<String> rsList=new ArrayList<String>();
		String sql =" SELECT T.EXECUTIONID AS EXECUTIONID FROM T_OA_EXPANDACTIVITI_ATTENTION T WHERE T.USERID='"+userId+"'";
		List<WorkProcessCountVo> list = namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper(WorkProcessCountVo.class));
		
		if(list!=null&&list.size()>0){
			for(WorkProcessCountVo vo:list){
				rsList.add(vo.getExecutionId());
			}
		}
		return rsList;
	}


	
	public void deleteAttention(String[] exectionId)throws Exception {
		
		String sql =" DELETE FROM T_OA_EXPANDACTIVITI_ATTENTION T WHERE T.EXECUTIONID IN (:exectionId) ";
		HashMap<String,Object> map = new HashMap<String, Object>();
		List<String> list = Arrays.asList(exectionId);
		map.put("exectionId", list);
		namedParameterJdbcTemplate.update(sql, map);
	}


	@Override
	public void attention(String userId, String[] workId) throws Exception {
		
		
		String sql =" INSERT INTO T_OA_EXPANDACTIVITI_ATTENTION (ID,EXECUTIONID,USERID) VALUES (:id,:workId,:userId) ";
		String checkSql=" DELETE FROM T_OA_EXPANDACTIVITI_ATTENTION T WHERE T.EXECUTIONID = :exectionId ";
		for(String i:workId){
			String id = UUID.randomUUID().toString().replace("-", "");
			HashMap<String,Object> map = new HashMap<String, Object>();
			map.put("id", id);
			map.put("workId", i);
			map.put("userId", userId);
			HashMap<String,Object> checkMap = new HashMap<String, Object>();
			checkMap.put("exectionId", i);//关注之前先删除，以免有重复数据
			namedParameterJdbcTemplate.update(checkSql, checkMap);
			namedParameterJdbcTemplate.update(sql, map);
		}
		
	}

}
