package cn.honry.statistics.bi.disease.distribution.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.bi.disease.distribution.service.DiseaseDistributionService;
import cn.honry.statistics.util.customVo.CustomVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

/**
 * @author 朱振坤
 * 病种分布图
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = {@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/disease/distribution")
public class DiseaseDistributionAction {
    private Logger logger = Logger.getLogger(DiseaseDistributionAction.class);

    @Resource(name = "hiasExceptionService")
    private HIASExceptionService hiasExceptionService;

    @Resource(name = "diseaseDistributionService")
    private DiseaseDistributionService diseaseDistributionService;

    /**
     * 病种ICD10分类表id
     */
    private String icdClassificationId;

    public void setIcdClassificationId(String icdClassificationId) {
        this.icdClassificationId = icdClassificationId;
    }

    /**
     * 病种ICD10code
     */
    private String icdCode;

    public void setIcdCode(String icdCode) {
        this.icdCode = icdCode;
    }

    /**
     * 患病的年份，多各年份以英文逗号隔开
     */
    private String years;

    public void setYears(String years) {
        this.years = years;
    }

    /**
     * 地址级别 0：全国、1：省、2：市、3：区
     */
    private String mapLevel;

    public void setMapLevel(String mapLevel) {
        this.mapLevel = mapLevel;
    }

    /**
     * 患病的地区
     */
    private String address;

    public void setAddress(String address) {
        this.address = address;
    }

    private String id;//icdTree的id(根据父id异步加载icdTree树)

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    /**
     * 病种分布主页面
     *
     * @return 视图
     */
    @Action(value = "mainPage", results = {@Result(name = "list", location = "/WEB-INF/pages/stat/bi/disease/distribution/diseaseDistribution.jsp")},
            interceptorRefs = {@InterceptorRef(value = "manageInterceptor")})
    public String mainPage() {
        return "list";
    }

    /**
     * 某些病种某年某地区患者数量
     */
    @Action(value = "queryMapData", results = {@Result(name = "json", type = "json")})
    public void queryMapData() {
        Map<String, Object> map = new HashMap<>();
        try {
            if (StringUtils.isBlank(icdClassificationId) && StringUtils.isBlank(icdCode)) {
                map.put("resCode", "error");
                map.put("resMsg", "参数icdClassificationId和icdCode不能都为空！");
                return;
            }
            if (StringUtils.isBlank(years)) {
                map.put("resCode", "error");
                map.put("resMsg", "参数years不能为空！");
                return;
            }
            if (!"0".equals(mapLevel) && !"1".equals(mapLevel) && !"2".equals(mapLevel) && !"3".equals(mapLevel)) {
                map.put("resCode", "error");
                map.put("resMsg", "参数mapLevel无效！");
                return;
            }
            if (StringUtils.isBlank(address)) {
                map.put("resCode", "error");
                map.put("resMsg", "参数address不能为空！");
                return;
            }
            Map<String, List<CustomVo>> resultMap = this.diseaseDistributionService.queryMapData(icdClassificationId, icdCode, years, mapLevel, address);
            map.put("resCode", "success");
            map.put("data", resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("resCode", "error");
            map.put("resMsg", "服务异常，请联系管理员！");
            this.hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BZFB", "运营分析_病种分布", "2", "0"), e);
            this.logger.error("BZFB", e);
        } finally {
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        }
    }

    @Action(value = "queryBarAndPieData", results = {@Result(name = "json", type = "json")})
    public void queryBarAndPieData() {
        Map<String, Object> map = new HashMap<>(2);
        try {
            if (StringUtils.isBlank(icdClassificationId) && StringUtils.isBlank(icdCode)) {
                map.put("resCode", "error");
                map.put("resMsg", "参数icdClassificationId和icdCode不能都为空！");
                return;
            }
            if (StringUtils.isBlank(years)) {
                map.put("resCode", "error");
                map.put("resMsg", "参数years不能为空！");
                return;
            }
            if (StringUtils.isBlank(address)) {
                map.put("resCode", "error");
                map.put("resMsg", "参数address不能为空！");
                return;
            }
            Map<String, Map<String, List<Long>>> resultMap = this.diseaseDistributionService.queryBarAndPieData(icdClassificationId, icdCode, years, address);
            map.put("resCode", "success");
            map.put("data", resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("resCode", "error");
            map.put("resMsg", "服务异常，请联系管理员！");
            this.hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BZFB", "运营分析_病种分布", "2", "0"), e);
            this.logger.error("BZFB", e);
        } finally {
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        }
    }




}
