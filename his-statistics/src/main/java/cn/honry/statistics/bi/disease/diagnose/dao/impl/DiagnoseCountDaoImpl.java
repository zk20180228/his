package cn.honry.statistics.bi.disease.diagnose.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.statistics.bi.disease.diagnose.dao.DiagnoseCountDao;
import cn.honry.statistics.bi.disease.diagnose.vo.DiagnoseVo;

@Repository
public class DiagnoseCountDaoImpl implements DiagnoseCountDao {

	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	
	@Override
	public List<DiagnoseVo> diagnoseList(String page, String rows, String feature) {
		
		Integer p= (page==null?1:Integer.valueOf(page));
		Integer r= (rows==null?20:Integer.valueOf(rows));
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("	SELECT                                                                        ");
		sb.append("		b.feature as feature,                                                     ");
		sb.append("		b.result as result,                                                       ");
		sb.append("		b.COUNT as COUNT                                                          ");
		sb.append("	FROM                                                                          ");
		sb.append("		(                                                                         ");
		sb.append("			SELECT                                                                ");
		sb.append("				T .*, ROWNUM AS rs                                                ");
		sb.append("			FROM                                                                  ");
		sb.append("				(                                                                 ");
		sb.append("					SELECT                                                        ");
		sb.append(" feature, ");
		sb.append(" result, ");
		sb.append(" COUNT(1) AS COUNT ");
		sb.append("					FROM                                                          ");
		sb.append("						(                                                         ");
		sb.append("							SELECT                                                ");
		sb.append("								SUBSTR (CLINIC_DIAGICDNAME, 0, 2) AS feature,     ");
		sb.append("								REPLACE (MAIN_DIAGICDNAME, '？', '') AS result   ");
		sb.append("							FROM                                                  ");
		sb.append("								T_EMR_BASE                                        ");
		sb.append("							WHERE                                                 ");
		sb.append("								CLINIC_DIAGICDNAME LIKE '__待查%'                 ");
		sb.append("							GROUP BY                                              ");
		sb.append("								CLINIC_DIAGICDNAME,                               ");
		sb.append("								MAIN_DIAGICDNAME                                  ");
		sb.append("						)                                                         ");
		if(StringUtils.isNotBlank(feature)){
			sb.append("					WHERE                                                     ");
			String[] features=feature.split(",");
			for(int i=0;i<features.length;i++){
				if(i==0){
					sb.append("			FEATURE LIKE '%"+features[i]+"%'                           ");
				}else{
					sb.append("			OR FEATURE LIKE '%"+features[i]+"%'                        ");
				}
			}
		}
		sb.append(" GROUP BY feature, result ");
		sb.append("				) T                                                               ");
		sb.append("			WHERE                                                                 ");
		sb.append("				ROWNUM <= "+p*r+"                                                 ");
		sb.append("		) b                                                                       ");
		sb.append("	WHERE                                                                         ");
		sb.append("		b.rs > "+(p-1)*r+"                                                        ");
				
		
		return namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(DiagnoseVo.class));
	}

	
	@Override
	public Long diagnoseTotal(String feature) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("		SELECT                                                             ");
		sb.append("		COUNT (1)                                                          ");
		sb.append("	FROM                                                                   ");
		sb.append("		(                                                                  ");
		sb.append("			SELECT                                                         ");
		sb.append("				feature,                                                   ");
		sb.append("				result,                                                    ");
		sb.append("				COUNT (1) AS COUNT                                         ");
		sb.append("			FROM	                                                       ");
		sb.append("					(                                                      ");
		sb.append("						SELECT                                             ");
		sb.append("							SUBSTR (CLINIC_DIAGICDNAME, 0, 2) AS feature,  ");
		sb.append("							REPLACE (MAIN_DIAGICDNAME, '？', '') AS result ");
		sb.append("						FROM                                               ");
		sb.append("							T_EMR_BASE                                     ");
		sb.append("						WHERE                                              ");
		sb.append("							CLINIC_DIAGICDNAME LIKE '__待查%'              ");
		sb.append("						GROUP BY                                           ");
		sb.append("							CLINIC_DIAGICDNAME,                            ");
		sb.append("							MAIN_DIAGICDNAME                               ");
		sb.append("					                                                       ");
		sb.append("					)                                                      ");
		if(StringUtils.isNotBlank(feature)){
			sb.append("					WHERE                                       ");
			String[] features=feature.split(",");
			for(int i=0;i<features.length;i++){
				if(i==0){
					sb.append("			FEATURE LIKE '%"+features[i]+"%'            ");
				}else{
					sb.append("			OR FEATURE LIKE '%"+features[i]+"%'          ");
				}
			}
		}
		sb.append("						GROUP BY                                           ");
		sb.append("							feature,                                       ");
		sb.append("							result                                         ");
		sb.append("	)                                                                      ");
		

		return namedParameterJdbcTemplate.queryForObject(sb.toString(), new HashMap(), Long.class);
	}


	@Override
	public List<String> featureMap() {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT												 ");
		sb.append("	 feature AS feature                                   ");
		sb.append(" FROM                                                  ");
		sb.append(" 	(                                                 ");
		sb.append("		SELECT                                           ");
		sb.append("			SUBSTR (CLINIC_DIAGICDNAME, 0, 2) AS feature ");
		sb.append("		FROM                                             ");
		sb.append("			T_EMR_BASE                                   ");
		sb.append("		WHERE                                            ");
		sb.append("			CLINIC_DIAGICDNAME LIKE '__待查%'             ");
		sb.append(" 	)                                                ");
		sb.append("  GROUP BY                                            ");
		sb.append("	 feature                                             ");
		List<DiagnoseVo> list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(DiagnoseVo.class));
		List<String> arrayList = new ArrayList<String>();
		for(DiagnoseVo v :list){
			arrayList.add(v.getFeature());
		}
			
		return arrayList;
	}

}
