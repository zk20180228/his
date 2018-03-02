package cn.honry.statistics.bi.disease.health.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import cn.honry.statistics.bi.disease.health.service.HealthDataService;
import cn.honry.statistics.bi.disease.health.vo.HealthDataGridVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

/**
 * 健康数据统计表
 *
 * @author 朱振坤
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = {@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/disease/health")
public class HealthDataAction {
    private Logger logger = Logger.getLogger(HealthDataAction.class);
    @Resource(name = "hiasExceptionService")
    private HIASExceptionService hiasExceptionService;
    @Resource(name = "healthDataService")
    private HealthDataService healthDataService;

    /**
     * icd 分类id
     */
    private String icdAssortId;

    public void setIcdAssortId(String icdAssortId) {
        this.icdAssortId = icdAssortId;
    }

    private String icdCode;

    public void setIcdCode(String icdCode) {
        this.icdCode = icdCode;
    }

    private String where;

    public void setWhere(String where) {
        this.where = where;
    }

    private Integer page;

    public void setPage(Integer page) {
        this.page = page;
    }

    private Integer rows;

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    @Action(value = "queryHealthData", results = {@Result(name = "json", type = "json")})
    public void queryHealthData() {
        Map<String, Object> map = new HashMap<>(2);
        try {
            List<HealthDataGridVo> list = this.healthDataService.queryHealthData(icdAssortId, icdCode, where, page, rows);
            List<HealthDataGridVo> headerVo = this.healthDataService.queryFooterHealthData(icdAssortId, icdCode, where);
            int count = this.healthDataService.queryHealthCount(icdAssortId, icdCode, where);
            map.put("resCode", "success");
            map.put("total", count);
            map.put("rows", list);
            map.put("footer", headerVo);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("resCode", "error");
            map.put("resMsg", "服务异常，请联系管理员！");
            this.hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("JKSJTJB", "运营分析_健康数据统计表", "2", "0"), e);
            this.logger.error("JKSJTJB", e);
        } finally {
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        }
    }


}
