package cn.honry.oa.activiti.processDefinitions.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.oa.activiti.processDefinitions.dao.ProcessDefinitionsDao;
import cn.honry.oa.activiti.processDefinitions.vo.ProcessDefinitionsVO;

@Repository("processDefinitionsDao")
public class ProcessDefinitionsDaoImpl implements ProcessDefinitionsDao {
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Override
	public List<ProcessDefinitionsVO> getProcessDefinitionsList(String name,
			Integer page, Integer rows) {
		Map<String ,String> map = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		sb.append(" Select * From (Select t.process_definition_id As id ,substr(p.resource_name_,0,length(p.resource_name_)-11) as name,t.process_definition_version as version,t.createtime as createTime ,Rownum rn ");
		sb.append(" from t_oa_bpm_conf_base t left join act_re_procdef p on p.id_=t.process_definition_id ");
		sb.append(" Where Rownum <="+rows);
		if(StringUtils.isNotBlank(name)){
			sb.append(" and  p.resource_name_ like '%"+name+"%'");
		}
		sb.append(" order by t.createtime desc) tab Where tab.rn>"+page+" order by tab.createtime desc ");
		List<ProcessDefinitionsVO> list = namedParameterJdbcTemplate.query(sb.toString(), map, new BeanPropertyRowMapper(ProcessDefinitionsVO.class));
		if(list!=null &&list.size()>.0){
			return list;
		}
		return new ArrayList<ProcessDefinitionsVO>();
	}

	@Override
	public int getProcessDefinitionsTotal(String name) {
		Map<String ,String> map = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		sb.append(" Select Count(1) from t_oa_bpm_conf_base t left join act_re_procdef p on p.id_=t.process_definition_id ");
		if(StringUtils.isNotBlank(name)){
			sb.append(" Where p.resource_name_ like '%"+name+"%'");
		}
		return namedParameterJdbcTemplate.queryForInt(sb.toString(), map);
	}

}
