package cn.honry.statistics.bi.disease.distribution.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.statistics.bi.disease.distribution.dao.DiseaseDistributionDao;

/**
 * @author 朱振坤
 */
@Repository("diseaseDistributionDao")
public class DiseaseDistributionDaoImpl implements DiseaseDistributionDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<String> queryAllChineseCitys() {
        String sql = "SELECT t.city_name FROM T_DISTRICT t WHERE t.city_level = 1 AND t.municipality_flag = 2" +
                " UNION ALL SELECT t.city_name FROM T_DISTRICT t WHERE t.city_level = 2 AND t.city_name NOT IN ('北京', '市辖区','县')";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public List<String> queryCitysByProvince(String province) {
        String sql = "SELECT t1.city_name FROM T_DISTRICT t1, T_DISTRICT t2" +
                " WHERE t1.city_parentid = t2.city_code AND t1.city_name NOT IN ('北京', '市辖区', '县', '省直辖行政单位', '省直辖县级行政单位') AND t2.city_level = 1 AND t2.city_name LIKE ?" +
                " UNION ALL SELECT t1.city_name FROM T_DISTRICT t1,T_DISTRICT t2, T_DISTRICT t3" +
                " WHERE t1.city_parentid = t2.city_code AND t2.city_parentid = t3.city_code AND t2.city_name IN('市辖区', '县', '省直辖行政单位', '省直辖县级行政单位') AND t3.city_level = 1 AND t3.city_name LIKE ?";
        return jdbcTemplate.queryForList(sql, String.class, province + "%", province + "%");
    }

    @Override
    public List<String> queryDistrictsByProvince(String province) {
        String sql = "SELECT t3.city_name" +
                "  FROM T_DISTRICT t1, T_DISTRICT t2, T_DISTRICT t3" +
                " WHERE t3.city_parentid = t2.city_code" +
                "   AND t2.city_parentid = t1.city_code" +
                "   AND t3.city_name NOT IN ('市辖区')" +
                "   AND t2.city_name NOT IN" +
                "       ('北京', '市辖区', '县', '省直辖行政单位', '省直辖县级行政单位')" +
                "   AND t1.city_level = 1" +
                "   AND t1.city_name LIKE ?" +
                " UNION ALL" +
                " SELECT t4.city_name" +
                "  FROM T_DISTRICT t1, T_DISTRICT t2, T_DISTRICT t3, T_DISTRICT t4" +
                " WHERE t4.city_parentid = t3.city_code" +
                "   AND t3.city_parentid = t2.city_code" +
                "   AND t2.city_parentid = t1.city_code" +
                "   AND t3.city_name IN ('市辖区')" +
                "   AND t2.city_name NOT IN" +
                "       ('北京', '市辖区', '县', '省直辖行政单位', '省直辖县级行政单位')" +
                "   AND t1.city_level = 1" +
                "   AND t1.city_name LIKE ?";
        return jdbcTemplate.queryForList(sql, String.class, province + "%", province + "%");
    }

    @Override
    public List<String> queryDistrictsByCity(String city) {
        String sql = "SELECT t1.city_name FROM T_DISTRICT t1, T_DISTRICT t2" +
                " WHERE t1.city_parentid = t2.city_code AND t1.city_name NOT IN ('市辖区') AND t2.city_level = 2 AND t2.city_name LIKE ?" +
                " UNION ALL SELECT t1.city_name FROM T_DISTRICT t1, T_DISTRICT t2, T_DISTRICT t3" +
                " WHERE t1.city_parentid = t2.city_code AND t2.city_parentid = t3.city_code AND t2.city_name IN('市辖区') AND t3.city_level = 2 AND t3.city_name LIKE ?";
        return jdbcTemplate.queryForList(sql, String.class, city + "%", city + "%");
    }


}
