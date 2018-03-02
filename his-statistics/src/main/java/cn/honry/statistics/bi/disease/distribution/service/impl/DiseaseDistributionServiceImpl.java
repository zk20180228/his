package cn.honry.statistics.bi.disease.distribution.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.filters.FiltersAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.missing.Missing;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.honry.statistics.bi.disease.distribution.dao.DiseaseDistributionDao;
import cn.honry.statistics.bi.disease.distribution.service.DiseaseDistributionService;
import cn.honry.statistics.icd.service.IcdAssortService;
import cn.honry.statistics.util.customVo.CustomVo;
import cn.honry.utils.DateUtils;

/**
 * @author 朱振坤
 */
@Service("diseaseDistributionService")
public class DiseaseDistributionServiceImpl implements DiseaseDistributionService {
    private static final String UNKNOWN = "未记录";
    private static final String MALE_GENDER = "男";
    private static final String FEMALE_GENDER = "女";
    @Resource(name = "diseaseDistributionDao")
    private DiseaseDistributionDao diseaseDistributionDao;

    @Resource(name = "icdAssortService")
    private IcdAssortService icdAssortService;

    @Resource(name = "client")
    private Client client;

    @Value("${es.business_diagnose.index}")
    private String diagnoseIndex;

    @Value("${es.business_diagnose.type}")
    private String diagnoseType;

    @Override
    public Map<String, List<CustomVo>> queryMapData(String icdClassificationId, String icdCode, String years, String mapLevel, String address) {
        String[] yearArray = years.split(",");
        Map<String, List<CustomVo>> resultMap = new HashMap<>(yearArray.length);
        // 初始化结构，即使没有数据也返回相同的结构
        for (String year : yearArray) {
            resultMap.put(year, new ArrayList<CustomVo>());
        }
        List<String> icdCodeList = new ArrayList<>();
        // 如果前台传来的是icd分类id，则根据id查出该分类下的所有icd code
        if (StringUtils.isNotBlank(icdClassificationId)) {
            icdCodeList.addAll(icdAssortService.queryIcdCodesByIcdAssortId(icdClassificationId));
        }
        if (StringUtils.isNotBlank(icdCode)) {
            icdCodeList.add(icdCode);
        }
        if (icdCodeList.size() == 0) {
            return resultMap;
        }
        List<String> addressList = new ArrayList<>();
        if ("0".equals(mapLevel)) {
            addressList.addAll(queryAllChineseCitys());
        } else if ("1".equals(mapLevel) && StringUtils.isNotBlank(address)) {
            addressList.addAll(queryDistrictsByProvince(address));
        }
        if (addressList.size() == 0) {
            return resultMap;
        }
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termsQuery("icd_code", icdCodeList))
                .filter(QueryBuilders.rangeQuery("in_date")
                        .gte(DateUtils.parseDateY(yearArray[0]))
                        .lt(DateUtils.parseDateY(String.valueOf(Integer.valueOf(yearArray[yearArray.length - 1]) + 1))));
        // 必须加上时区，否则开始时间是上一年
        DateHistogramBuilder dateHistogramBuilder = AggregationBuilders.dateHistogram("by_year").field("in_date")
                .interval(DateHistogramInterval.YEAR).timeZone(Calendar.getInstance().getTimeZone().getID());
        FiltersAggregationBuilder filtersAggregationBuilder = AggregationBuilders.filters("by_district");
        for (String subDistrict : addressList) {
            filtersAggregationBuilder.filter(subDistrict, QueryBuilders.matchQuery("current_address", subDistrict));
        }
        SearchResponse searchResponse = client.prepareSearch(diagnoseIndex).setTypes(diagnoseType).setQuery(boolQuery).setSize(0)
                .addAggregation(dateHistogramBuilder.subAggregation(filtersAggregationBuilder)).get();
        Histogram yearHistogram = searchResponse.getAggregations().get("by_year");
        for (Histogram.Bucket yearEntry : yearHistogram.getBuckets()) {
            List<CustomVo> voList = new ArrayList<>();
            DateTime dataTime = (DateTime) yearEntry.getKey();
            Filters districtFilters = yearEntry.getAggregations().get("by_district");
            for (Filters.Bucket districtEntry : districtFilters.getBuckets()) {
                String districtKey = districtEntry.getKeyAsString();
                long districtCount = districtEntry.getDocCount();
                if (districtCount != 0L) {
                    CustomVo vo = new CustomVo();
                    vo.setName(districtKey);
                    vo.setValue((double) districtCount);
                    voList.add(vo);
                }
            }
            resultMap.put(DateUtils.formatDateY(dataTime.toDate()), voList);
        }
        return resultMap;
    }

    @Override
    public Map<String, Map<String, List<Long>>> queryBarAndPieData(String icdClassificationId, String icdCode, String years, String address) {
        String[] yearArray = years.split(",");
        Map<String, Map<String, List<Long>>> resultMap = new HashMap<>(3);
        // 初始化结构，即使没有数据也返回相同的结构
        Map<String, List<Long>> valueMap = new HashMap<>(yearArray.length);
        List<Long> yearValue = new ArrayList<>();
        // 10个年龄段，前台写死
        for (int i = 0; i < 10; i++) {
            yearValue.add(0L);
        }
        for (String year : yearArray) {
            valueMap.put(year, yearValue);
        }
        resultMap.put(MALE_GENDER, valueMap);
        resultMap.put(FEMALE_GENDER, valueMap);
        resultMap.put(UNKNOWN, valueMap);
        List<String> icdCodeList = new ArrayList<>();
        // 如果前台传来的是icd分类id，则根据id查出该分类下的所有icd code
        if (StringUtils.isNotBlank(icdClassificationId)) {
            icdCodeList.addAll(icdAssortService.queryIcdCodesByIcdAssortId(icdClassificationId));
        }
        if (StringUtils.isNotBlank(icdCode)) {
            icdCodeList.add(icdCode);
        }
        if (icdCodeList.size() == 0) {
            return resultMap;
        }
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termsQuery("icd_code", icdCodeList))
                .filter(QueryBuilders.rangeQuery("in_date")
                        .gte(DateUtils.parseDateY(yearArray[0]))
                        .lt(DateUtils.parseDateY(String.valueOf(Integer.valueOf(yearArray[yearArray.length - 1]) + 1))));
        if (!"china".equals(address)) {
            boolQuery.filter(QueryBuilders.matchQuery("current_address", address));
        }
        SearchResponse searchResponse = client.prepareSearch(diagnoseIndex).setTypes(diagnoseType).setQuery(boolQuery).setSize(0)
                .addAggregation(AggregationBuilders.terms("by_gender").field("sex_code").missing(UNKNOWN).size(0)
                        .subAggregation(AggregationBuilders.dateHistogram("by_year").field("in_date").interval(DateHistogramInterval.YEAR)
                                .timeZone(Calendar.getInstance().getTimeZone().getID())
                                .subAggregation(AggregationBuilders.range("by_age").field("age").addRange(0, 8).addRange(8, 14).addRange(14, 21)
                                        .addRange(21, 36).addRange(36, 46).addRange(46, 56).addRange(56, 66).addRange(66, 76).addUnboundedFrom(76))
                                .subAggregation(AggregationBuilders.missing("miss_age").field("age")))).get();
        Terms genderTerms = searchResponse.getAggregations().get("by_gender");
        for (Terms.Bucket genderEntry : genderTerms.getBuckets()) {
            Map<String, List<Long>> genderMap = new HashMap<>(genderTerms.getBuckets().size());
            // 补全结构
            for (String year : yearArray) {
                genderMap.put(year, yearValue);
            }
            String genderKey = genderEntry.getKeyAsString();
            Histogram yearHistogram = genderEntry.getAggregations().get("by_year");
            for (Histogram.Bucket yearEntry : yearHistogram.getBuckets()) {
                List<Long> voList = new ArrayList<>();
                DateTime dataTime = (DateTime) yearEntry.getKey();
                Range ageRange = yearEntry.getAggregations().get("by_age");
                for (Range.Bucket ageEntry : ageRange.getBuckets()) {
                    long ageCount = ageEntry.getDocCount();
                    voList.add(ageCount);
                }
                Missing ageMissing = yearEntry.getAggregations().get("miss_age");
                voList.add(ageMissing.getDocCount());
                genderMap.put(DateUtils.formatDateY(dataTime.toDate()), voList);
            }
            resultMap.put(genderKey, genderMap);
        }
        return resultMap;
    }



    @Override
    public List<String> queryAllChineseCitys() {
        return diseaseDistributionDao.queryAllChineseCitys();
    }

    @Override
    public List<String> queryCitysByProvince(String province) {
        return diseaseDistributionDao.queryCitysByProvince(province);
    }

    @Override
    public List<String> queryDistrictsByProvince(String province) {
        return diseaseDistributionDao.queryDistrictsByProvince(province);
    }

    @Override
    public List<String> queryDistrictsByCity(String city) {
        return diseaseDistributionDao.queryDistrictsByCity(city);
    }

}
