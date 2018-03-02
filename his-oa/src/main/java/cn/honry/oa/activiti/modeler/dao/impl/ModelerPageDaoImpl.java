package cn.honry.oa.activiti.modeler.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.oa.activiti.modeler.dao.ModelerPageDao;
import cn.honry.oa.activiti.modeler.vo.ModelerVO;
@Repository("modelerPageDao")
public class ModelerPageDaoImpl implements ModelerPageDao {
	@Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Override
	public List<ModelerVO> getModeler(String name, Integer page, Integer rows) {
		 Map<String, String> map = new HashMap<>();
		Integer p = (page-1) * rows;
		Integer r = page * rows;
		StringBuffer sb = new StringBuffer();
		sb.append(" Select tab.* From (Select t.Id_ As id, t.Create_Time_ As createTime, t.Last_Update_Time_ As lastUpdateTime, p.Id_ As deploymentId, t.Name_ As Name,Rownum rn ");
		sb.append(" From Act_Re_Model t Left Join Act_Re_Procdef p On p.Deployment_Id_ = t.Deployment_Id_ ");
		sb.append(" Where Rownum <= "+r);
		if(StringUtils.isNotBlank(name)){
			sb.append(" and t.Name_ like '%"+name+"%'");
		}
		sb.append(" Order By t.Last_Update_Time_ Desc) tab Where tab.rn>="+p+" Order By tab.lastUpdateTime Desc ");
		List<ModelerVO> list = namedParameterJdbcTemplate.query(sb.toString(), map, new BeanPropertyRowMapper(ModelerVO.class));
		if(list != null && list.size()>0){
			return list;
		}
		return new ArrayList<ModelerVO>();
	}

	@Override
	public int getModelerTotal(String name) {
		Map<String, String> map = new HashMap<>();
		StringBuffer sb = new StringBuffer();
		sb.append(" Select Count(1) From Act_Re_Model t Left Join Act_Re_Procdef p On p.Deployment_Id_ = t.Deployment_Id_ ");
		if(StringUtils.isNotBlank(name)){
			sb.append(" Where t.Name_ like '%"+name+"%'");
		}
		int total = namedParameterJdbcTemplate.queryForInt(sb.toString(), map);
		return total;
	}

}
