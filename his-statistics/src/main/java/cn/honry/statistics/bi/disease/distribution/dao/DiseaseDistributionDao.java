package cn.honry.statistics.bi.disease.distribution.dao;

import java.util.List;

/**
 * @author 朱振坤
 */
public interface DiseaseDistributionDao {

    /**
     * 查询中国所有的市
     *
     * @return 集合
     */
    List<String> queryAllChineseCitys();

    /**
     * 查询某省下的市或某直辖市的区县
     *
     * @param province 省或直辖市
     * @return 集合
     */
    List<String> queryCitysByProvince(String province);

    /**
     * 查询某省下的区县
     *
     * @param province 省
     * @return 集合
     */
    List<String> queryDistrictsByProvince(String province);

    /**
     * 查询某非直辖市的区县
     *
     * @param city 非直辖市
     * @return 集合
     */
    List<String> queryDistrictsByCity(String city);


}
