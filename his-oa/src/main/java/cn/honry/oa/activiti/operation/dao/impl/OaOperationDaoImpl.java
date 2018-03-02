package cn.honry.oa.activiti.operation.dao.impl;

import cn.honry.oa.activiti.operation.dao.OaOperationDao;
import cn.honry.oa.activiti.operation.vo.EducationVo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: zhangkui
 * @CreateDate: 2018/1/22 20:39
 * @Modifier: zhangkui
 * @version: V1.0
 */
@Repository("oaOperationDao")
public class OaOperationDaoImpl implements OaOperationDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Map<String, String> findEdcuations() {

        Map<String, String> map = new HashMap<>();
        String sql="SELECT DISTINCT CODE_ENCODE as eCode,CODE_NAME as eName FROM T_BUSINESS_DICTIONARY WHERE CODE_TYPE = 'degree' and DEL_FLG=0 and STOP_FLG=0";
        List<EducationVo> list = namedParameterJdbcTemplate.query(sql,new HashMap(), new BeanPropertyRowMapper(EducationVo.class));

        for (EducationVo v : list) {
            map.put(v.geteCode(),v.geteName());
        }

        return map;

    }
}
