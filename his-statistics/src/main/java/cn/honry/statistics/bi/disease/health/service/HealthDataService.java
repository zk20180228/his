package cn.honry.statistics.bi.disease.health.service;

import java.util.List;

import cn.honry.statistics.bi.disease.health.vo.HealthDataGridVo;

/**
 * @author 朱振坤
 */
public interface HealthDataService {
    /**
     * 查询符合一定条件的健康数据
     * @param icdAssortId icd分类id
     * @param icdCode icd code
     * @param where 查询条件，可以是icd code、icd名称
     * @param page 当前第几页
     * @param rows 一页几条
     * @return 表格数据集合
     */
    List<HealthDataGridVo> queryHealthData(String icdAssortId, String icdCode, String where, Integer page, Integer rows);

    /**
     * 查询符合一定条件的健康数据的数量
     * @param icdAssortId icd分类id
     * @param icdCode icd code
     * @param where 查询条件，可以是icd code、icd名称
     * @return
     */
    int queryHealthCount(String icdAssortId, String icdCode, String where);

    /**
     * 查询符合一定条件的健康数据的合计
     * @param icdAssortId icd分类id
     * @param icdCode icd code
     * @param where 查询条件，可以是icd code、icd名称
     * @return
     */
    List<HealthDataGridVo> queryFooterHealthData(String icdAssortId, String icdCode, String where);


}
