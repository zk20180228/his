package cn.honry.statistics.bi.disease.health.vo;

import java.io.Serializable;

/**
 * 健康数据统计表数据
 *
 * @author 朱振坤
 */
public class HealthDataGridVo implements Serializable{

    /**
     * 疾病名称或分类名称
     */
    private String name;

    /**
     * icd10 code
     */
    private String icd10Code;

    /**
     * 缓此病的总人数
     */
    private Integer totalCount = 0;
    /**
     * 男性人数
     */
    private Integer male = 0;
    /**
     * 女性人数
     */
    private Integer female = 0;
    /**
     * 未记录性别人数
     */
    private Integer unknownGender = 0;
    /**
     * 0-7岁人数
     */
    private Integer gte0lte7 = 0;
    /**
     * 8-13岁人数
     */
    private Integer gte8lte13 = 0;
    /**
     * 14-20岁人数
     */
    private Integer gte14lte20 = 0;
    /**
     * 21-35岁人数
     */
    private Integer gte21lte35 = 0;
    /**
     * 36-45岁人数
     */
    private Integer gte36lte45 = 0;
    /**
     * 46-55岁人数
     */
    private Integer gte46lte55 = 0;
    /**
     * 56-65岁人数
     */
    private Integer gte56lte65 = 0;
    /**
     * 66-75岁人数
     */
    private Integer gte66lte75 = 0;
    /**
     * 76岁以上人数
     */
    private Integer gte76 = 0;
    /**
     * 未记录年龄人数
     */
    private Integer unknownAge = 0;
    /**
     * 城市人数
     */
    private Integer city = 0;
    /**
     * 农村人数
     */
    private Integer village = 0;
    /**
     * 未记录地区人数
     */
    private Integer unknownDistrict = 0;

    public HealthDataGridVo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcd10Code() {
        return icd10Code;
    }

    public void setIcd10Code(String icd10Code) {
        this.icd10Code = icd10Code;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getMale() {
        return male;
    }

    public void setMale(Integer male) {
        this.male = male;
    }

    public Integer getFemale() {
        return female;
    }

    public void setFemale(Integer female) {
        this.female = female;
    }

    public Integer getUnknownGender() {
        return unknownGender;
    }

    public void setUnknownGender(Integer unknownGender) {
        this.unknownGender = unknownGender;
    }

    public Integer getGte0lte7() {
        return gte0lte7;
    }

    public void setGte0lte7(Integer gte0lte7) {
        this.gte0lte7 = gte0lte7;
    }

    public Integer getGte8lte13() {
        return gte8lte13;
    }

    public void setGte8lte13(Integer gte8lte13) {
        this.gte8lte13 = gte8lte13;
    }

    public Integer getGte14lte20() {
        return gte14lte20;
    }

    public void setGte14lte20(Integer gte14lte20) {
        this.gte14lte20 = gte14lte20;
    }

    public Integer getGte21lte35() {
        return gte21lte35;
    }

    public void setGte21lte35(Integer gte21lte35) {
        this.gte21lte35 = gte21lte35;
    }

    public Integer getGte36lte45() {
        return gte36lte45;
    }

    public void setGte36lte45(Integer gte36lte45) {
        this.gte36lte45 = gte36lte45;
    }

    public Integer getGte46lte55() {
        return gte46lte55;
    }

    public void setGte46lte55(Integer gte46lte55) {
        this.gte46lte55 = gte46lte55;
    }

    public Integer getGte56lte65() {
        return gte56lte65;
    }

    public void setGte56lte65(Integer gte56lte65) {
        this.gte56lte65 = gte56lte65;
    }

    public Integer getGte66lte75() {
        return gte66lte75;
    }

    public void setGte66lte75(Integer gte66lte75) {
        this.gte66lte75 = gte66lte75;
    }

    public Integer getGte76() {
        return gte76;
    }

    public void setGte76(Integer gte76) {
        this.gte76 = gte76;
    }

    public Integer getUnknownAge() {
        return unknownAge;
    }

    public void setUnknownAge(Integer unknownAge) {
        this.unknownAge = unknownAge;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getVillage() {
        return village;
    }

    public void setVillage(Integer village) {
        this.village = village;
    }

    public Integer getUnknownDistrict() {
        return unknownDistrict;
    }

    public void setUnknownDistrict(Integer unknownDistrict) {
        this.unknownDistrict = unknownDistrict;
    }
}
