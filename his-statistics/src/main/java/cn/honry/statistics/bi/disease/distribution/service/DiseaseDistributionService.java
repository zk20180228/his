package cn.honry.statistics.bi.disease.distribution.service;

import java.util.List;
import java.util.Map;

import cn.honry.statistics.util.customVo.CustomVo;

/**
 * @author 朱振坤
 */
public interface DiseaseDistributionService {
    /**
     * 某些病种某年某地区患者数量
     *
     * @param icdClassificationId 病种分类id
     * @param icdCode             病种icd code
     * @param years               患病发生的年份，多个年份以英文逗号隔开
     * @param mapLevel            地址级别 0：全国、1：省、2：市、3：区
     * @param address             患病的地区
     * @return 患者数量
     */
    Map<String, List<CustomVo>> queryMapData(String icdClassificationId, String icdCode, String years, String mapLevel, String address);

    /**
     * 某些病种某年某地区患者性别和年龄组成
     *
     * @param icdClassificationId 病种分类id
     * @param icdCode             病种icd code
     * @param years               患病发生的年份
     * @param address             患病的地区，多个年份以英文逗号隔开
     * @return 患者性别和年龄组成
     */
    Map<String, Map<String, List<Long>>> queryBarAndPieData(String icdClassificationId, String icdCode, String years, String address);


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
