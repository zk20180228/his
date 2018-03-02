package cn.honry.statistics.bi.disease.health.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.filters.FiltersAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.missing.Missing;
import org.elasticsearch.search.aggregations.bucket.missing.MissingBuilder;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.honry.statistics.bi.disease.health.service.HealthDataService;
import cn.honry.statistics.bi.disease.health.vo.HealthDataGridVo;
import cn.honry.statistics.icd.service.IcdAssortService;

/**
 * @author 朱振坤
 */
@Service("healthDataService")
public class HealthDataServiceImpl implements HealthDataService {
    @Resource(name = "client")
    private Client client;

    @Value("${es.business_diagnose.index}")
    private String diagnoseIndex;

    @Value("${es.business_diagnose.type}")
    private String diagnoseType;
    @Resource(name = "icdAssortService")
    private IcdAssortService icdAssortService;

    private static final String UNKNOWN = "未记录";
    private static final String CITY_DISTRICT = "城市";
    private static final String VILLAGE_DISTRICT = "农村";
    private static final String MALE_GENDER = "男";
    private static final String FEMALE_GENDER = "女";

    @Override
    public List<HealthDataGridVo> queryHealthData(String icdAssortId, String icdCode, String where, Integer page, Integer rows) {
        List<HealthDataGridVo> voList = new ArrayList<>();
        HealthDataGridVo vo;
        List<String> icdCodeList = new ArrayList<>();
        List<String> filterIcdCodeList = new ArrayList<>();
        if (StringUtils.isNotBlank(icdAssortId)) {
            if ("root".equals(icdAssortId)) {
                icdCodeList.addAll(icdAssortService.queryAllIcdCodes());
            } else {
                icdCodeList.addAll(icdAssortService.queryIcdCodesByIcdAssortId(icdAssortId));
            }
        } else if (StringUtils.isNotBlank(icdCode)) {
            icdCodeList.add(icdCode);
        } else if (StringUtils.isNotBlank(where)) {
            icdCodeList.addAll(icdAssortService.queryIcdCodeByLike(where));
        } else {
            icdCodeList.addAll(icdAssortService.queryAllIcdCodes());
        }
        Collections.sort(icdCodeList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        // 过滤出当前分页需要的
        for (int i = 0; i < icdCodeList.size(); i++) {
            if (i >= (page - 1) * rows && i < page * rows) {
                filterIcdCodeList.add(icdCodeList.get(i));
            }
        }
        if (filterIcdCodeList.size() > 0) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().filter(QueryBuilders.termsQuery("icd_code", filterIcdCodeList));
            SearchRequestBuilder searchRequestBuilder = client.prepareSearch(diagnoseIndex).setTypes(diagnoseType).setQuery(boolQueryBuilder).setSize(0);
            FiltersAggregationBuilder icdCodeFiltersAggregationBuilder = AggregationBuilders.filters("by_icd_code");
            for (String code : filterIcdCodeList) {
                icdCodeFiltersAggregationBuilder.filter(code, QueryBuilders.termQuery("icd_code",code));
            }
            TermsBuilder icdNameTermsBuilder = AggregationBuilders.terms("by_icd_name").field("icd_diagnosticname").size(0);
            TermsBuilder genderTermsBuilder = AggregationBuilders.terms("by_gender").field("sex_code").missing(UNKNOWN).size(0);
            RangeBuilder ageRangeBuilder = AggregationBuilders.range("by_age").field("age")
                    .addRange(0.0, 8.0).addRange(8.0, 14.0).addRange(14.0, 21.0).addRange(21.0, 36.0)
                    .addRange(36.0, 46.0).addRange(46.0, 56.0).addRange(56.0, 66.0).addRange(66.0, 76.0).addUnboundedFrom(76.0);
            MissingBuilder ageMissingBuilder = AggregationBuilders.missing("miss_age").field("age");
            BoolQueryBuilder cityBoolQueryBuilder = QueryBuilders.boolQuery()
                    .filter(QueryBuilders.existsQuery("current_address"))
                    .mustNot(QueryBuilders.matchQuery("current_address", "村"))
                    .mustNot(QueryBuilders.matchQuery("current_address", "镇"))
                    .mustNot(QueryBuilders.matchQuery("current_address", "乡"))
                    .mustNot(QueryBuilders.matchQuery("current_address", "县"));
            BoolQueryBuilder villageBoolQueryBuilder = QueryBuilders.boolQuery()
                    .filter(QueryBuilders.existsQuery("current_address"))
                    .should(QueryBuilders.matchQuery("current_address", "村"))
                    .should(QueryBuilders.matchQuery("current_address", "镇"))
                    .should(QueryBuilders.matchQuery("current_address", "乡"))
                    .should(QueryBuilders.matchQuery("current_address", "县"));
            BoolQueryBuilder unknownBoolQueryBuilder = QueryBuilders.boolQuery()
                    .mustNot(QueryBuilders.existsQuery("current_address"));
            FiltersAggregationBuilder districtFiltersAggregationBuilder = AggregationBuilders.filters("by_district")
                    .filter(CITY_DISTRICT, cityBoolQueryBuilder)
                    .filter(VILLAGE_DISTRICT, villageBoolQueryBuilder)
                    .filter("未记录", unknownBoolQueryBuilder);
            SearchResponse searchResponse = searchRequestBuilder
                    .addAggregation(icdCodeFiltersAggregationBuilder
                            .subAggregation(icdNameTermsBuilder)
                            .subAggregation(genderTermsBuilder)
                            .subAggregation(ageRangeBuilder)
                            .subAggregation(ageMissingBuilder)
                            .subAggregation(districtFiltersAggregationBuilder)).get();
            Filters icdCodeTerms = searchResponse.getAggregations().get("by_icd_code");
            for (Filters.Bucket icdCodeEntry : icdCodeTerms.getBuckets()) {
                vo = new HealthDataGridVo();
                String icdCodeKey = icdCodeEntry.getKeyAsString();
                long icdCodeDocCount = icdCodeEntry.getDocCount();
                vo.setIcd10Code(icdCodeKey);
                vo.setTotalCount((int) icdCodeDocCount);
                Terms icdNameTerms = icdCodeEntry.getAggregations().get("by_icd_name");
                if (icdNameTerms.getBuckets().size() > 0) {
                    vo.setName(icdNameTerms.getBuckets().get(0).getKeyAsString());
                } else {
                    System.out.println(icdCodeKey);
                    vo.setName(icdAssortService.queryIcdNameByIcdCode(icdCodeKey));
                }
                Terms genderTerms = icdCodeEntry.getAggregations().get("by_gender");
                for (Terms.Bucket genderEntry : genderTerms.getBuckets()) {
                    String genderKey = genderEntry.getKeyAsString();
                    long genderDocCount = genderEntry.getDocCount();
                    if (MALE_GENDER.equals(genderKey)) {
                        vo.setMale((int) genderDocCount);
                    } else if (FEMALE_GENDER.equals(genderKey)) {
                        vo.setFemale((int) genderDocCount);
                    } else {
                        vo.setUnknownGender((int) genderDocCount);
                    }
                }
                Range ageRange = icdCodeEntry.getAggregations().get("by_age");
                for (Range.Bucket ageEntry : ageRange.getBuckets()) {
                    String ageKey = ageEntry.getKeyAsString();
                    long ageDocCount = ageEntry.getDocCount();
                    setHealthTreeGridVoAge(vo, ageKey, (int) ageDocCount);
                }
                Missing ageMissing = icdCodeEntry.getAggregations().get("miss_age");
                long ageMissDocCount = ageMissing.getDocCount();
                vo.setUnknownAge((int) ageMissDocCount);
                Filters districtFilters = icdCodeEntry.getAggregations().get("by_district");
                for (Filters.Bucket districtEntry : districtFilters.getBuckets()) {
                    String districtKey = districtEntry.getKeyAsString();
                    long districtCount = districtEntry.getDocCount();
                    if (CITY_DISTRICT.equals(districtKey)) {
                        vo.setCity((int) districtCount);
                    } else if (VILLAGE_DISTRICT.equals(districtKey)) {
                        vo.setVillage((int) districtCount);
                    } else {
                        vo.setUnknownDistrict((int) districtCount);
                    }
                }
                voList.add(vo);
            }
        }
        return voList;
    }

    @Override
    public int queryHealthCount(String icdAssortId, String icdCode, String where) {
        List<String> icdCodeList = new ArrayList<>();
        if (StringUtils.isNotBlank(icdAssortId)) {
            if ("root".equals(icdAssortId)) {
                icdCodeList.addAll(icdAssortService.queryAllIcdCodes());
            } else {
                icdCodeList.addAll(icdAssortService.queryIcdCodesByIcdAssortId(icdAssortId));
            }
        } else if (StringUtils.isNotBlank(icdCode)) {
            icdCodeList.add(icdCode);
        } else if (StringUtils.isNotBlank(where)) {
            icdCodeList.addAll(icdAssortService.queryIcdCodeByLike(where));
        } else {
            icdCodeList.addAll(icdAssortService.queryAllIcdCodes());
        }
        return icdCodeList.size();
    }

    @Override
    public  List<HealthDataGridVo> queryFooterHealthData(String icdAssortId, String icdCode, String where) {
        List<HealthDataGridVo> voList = new ArrayList<>();
        HealthDataGridVo vo = new HealthDataGridVo();
        vo.setIcd10Code("合计：");
        List<String> icdCodeList = new ArrayList<>();
        if (StringUtils.isNotBlank(icdAssortId)) {
            if ("root".equals(icdAssortId)) {
                icdCodeList.addAll(icdAssortService.queryAllIcdCodes());
                vo.setName("全部");
            } else {
                icdCodeList.addAll(icdAssortService.queryIcdCodesByIcdAssortId(icdAssortId));
                vo.setName(icdAssortService.queryIcdAssortNameByIcdAssortId(icdAssortId));
            }
        } else if (StringUtils.isNotBlank(icdCode)) {
            icdCodeList.add(icdCode);
            vo.setName(icdAssortService.queryIcdNameByIcdCode(icdCode));
        } else if (StringUtils.isNotBlank(where)) {
            icdCodeList.addAll(icdAssortService.queryIcdCodeByLike(where));
            vo.setName("全部");
        } else {
            icdCodeList.addAll(icdAssortService.queryAllIcdCodes());
            vo.setName("全部");
        }
        if (icdCodeList.size() > 0) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().filter(QueryBuilders.termsQuery("icd_code", icdCodeList));
            TermsBuilder genderTermsBuilder = AggregationBuilders.terms("by_gender").field("sex_code").missing(UNKNOWN).size(0);
            RangeBuilder ageRangeBuilder = AggregationBuilders.range("by_age").field("age")
                    .addRange(0.0, 8.0).addRange(8.0, 14.0).addRange(14.0, 21.0).addRange(21.0, 36.0)
                    .addRange(36.0, 46.0).addRange(46.0, 56.0).addRange(56.0, 66.0).addRange(66.0, 76.0).addUnboundedFrom(76.0);
            MissingBuilder ageMissingBuilder = AggregationBuilders.missing("miss_age").field("age");
            BoolQueryBuilder cityBoolQueryBuilder = QueryBuilders.boolQuery()
                    .filter(QueryBuilders.existsQuery("current_address"))
                    .mustNot(QueryBuilders.matchQuery("current_address", "村"))
                    .mustNot(QueryBuilders.matchQuery("current_address", "镇"))
                    .mustNot(QueryBuilders.matchQuery("current_address", "乡"))
                    .mustNot(QueryBuilders.matchQuery("current_address", "县"));
            BoolQueryBuilder villageBoolQueryBuilder = QueryBuilders.boolQuery()
                    .filter(QueryBuilders.existsQuery("current_address"))
                    .should(QueryBuilders.matchQuery("current_address", "村"))
                    .should(QueryBuilders.matchQuery("current_address", "镇"))
                    .should(QueryBuilders.matchQuery("current_address", "乡"))
                    .should(QueryBuilders.matchQuery("current_address", "县"));
            BoolQueryBuilder unknownBoolQueryBuilder = QueryBuilders.boolQuery()
                    .mustNot(QueryBuilders.existsQuery("current_address"));
            FiltersAggregationBuilder districtFiltersAggregationBuilder = AggregationBuilders.filters("by_district")
                    .filter(CITY_DISTRICT, cityBoolQueryBuilder)
                    .filter(VILLAGE_DISTRICT, villageBoolQueryBuilder)
                    .filter("未记录", unknownBoolQueryBuilder);
            SearchResponse searchResponse = client.prepareSearch(diagnoseIndex).setTypes(diagnoseType).setQuery(boolQueryBuilder)
                    .addAggregation(genderTermsBuilder)
                    .addAggregation(ageRangeBuilder)
                    .addAggregation(ageMissingBuilder)
                    .addAggregation(districtFiltersAggregationBuilder)
                    .setSize(0).get();
            long totalCount = searchResponse.getHits().getTotalHits();
            vo.setTotalCount((int) totalCount);
            Terms genderTerms = searchResponse.getAggregations().get("by_gender");
            for (Terms.Bucket genderEntry : genderTerms.getBuckets()) {
                String genderKey = genderEntry.getKeyAsString();
                long genderDocCount = genderEntry.getDocCount();
                if (MALE_GENDER.equals(genderKey)) {
                    vo.setMale((int) genderDocCount);
                } else if (FEMALE_GENDER.equals(genderKey)) {
                    vo.setFemale((int) genderDocCount);
                } else {
                    vo.setUnknownGender((int) genderDocCount);
                }
            }
            Range ageRange = searchResponse.getAggregations().get("by_age");
            for (Range.Bucket ageEntry : ageRange.getBuckets()) {
                String ageKey = ageEntry.getKeyAsString();
                long ageDocCount = ageEntry.getDocCount();
                setHealthTreeGridVoAge(vo, ageKey, (int) ageDocCount);
            }
            Missing ageMissing = searchResponse.getAggregations().get("miss_age");
            long ageMissDocCount = ageMissing.getDocCount();
            vo.setUnknownAge((int) ageMissDocCount);
            Filters districtFilters = searchResponse.getAggregations().get("by_district");
            for (Filters.Bucket districtEntry : districtFilters.getBuckets()) {
                String districtKey = districtEntry.getKeyAsString();
                long districtCount = districtEntry.getDocCount();
                if (CITY_DISTRICT.equals(districtKey)) {
                    vo.setCity((int) districtCount);
                } else if (VILLAGE_DISTRICT.equals(districtKey)) {
                    vo.setVillage((int) districtCount);
                } else {
                    vo.setUnknownDistrict((int) districtCount);
                }
            }
        }
        voList.add(vo);
        return voList;
    }

    private void setHealthTreeGridVoAge(HealthDataGridVo vo, String ageKey, int count) {
        switch (ageKey) {
            case "0.0-8.0":
                vo.setGte0lte7(count);
                break;
            case "8.0-14.0":
                vo.setGte8lte13(count);
                break;
            case "14.0-21.0":
                vo.setGte14lte20(count);
                break;
            case "21.0-36.0":
                vo.setGte21lte35(count);
                break;
            case "36.0-46.0":
                vo.setGte36lte45(count);
                break;
            case "46.0-56.0":
                vo.setGte46lte55(count);
                break;
            case "56.0-66.0":
                vo.setGte56lte65(count);
                break;
            case "66.0-76.0":
                vo.setGte66lte75(count);
                break;
            case "76.0-*":
                vo.setGte76(count);
                break;
            default:
                break;
        }
    }
}
