package cn.honry.statistics.icd.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.statistics.icd.service.IcdAssortService;
import cn.honry.statistics.icd.vo.IcdAssortTree;
import cn.honry.statistics.icd.vo.IcdAssortVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * <p>icd分类维护</p>
 *
 * @Author: zhangkui
 * @CreateDate: 2017年12月11日 上午10:56:22
 * @Modifier: zhangkui
 * @ModifyDate: 2017年12月11日 上午10:56:22
 * @ModifyRmk:
 * @version: V1.0
 * @throws:
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@Namespace("/icdAssort")
public class IcdAssortAction extends ActionSupport {

    private String page;//当前页
    private String rows;//每页显示的记录数

    private String icdId;//诊断码id
    private String assortId;//icd分类

    private String icdCode;//icd的诊断码

    private String assort_Name; //icd分类名称
    private String id;//父节点id

    private String assortName;//icdTree树的搜索名字


    @Resource
    private IcdAssortService icdAssortService;

    public void setIcdAssortService(IcdAssortService icdAssortService) {
        this.icdAssortService = icdAssortService;
    }


    private static final long serialVersionUID = -636395697994068370L;

    //icd分类tree树查询http://localhost:8080/his-portal/icdAssort/icdTree.action
    @Action("/icdAssortTree")
    public void icdAssortTree() {

        List<IcdAssortTree> list = null;
        if (StringUtils.isNotBlank(id)) {
            try {
                list = icdAssortService.icdTree(id);//根据父id，查询子分类
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            List<Object> arrayList = new ArrayList<Object>();
            Map<String, Object> map = new HashMap<String, Object>();
            //模拟根节点
            map.put("id", "root");
            map.put("text", "疾病分类");
            map.put("state", "closed");
            map.put("children", new ArrayList());
            arrayList.add(map);

            String json = JSONUtils.toJson(arrayList);
            WebUtils.webSendJSON(json);
            return;
        }

        if (list == null) {
            list = new ArrayList<IcdAssortTree>();
        }

        String json = JSONUtils.toJson(list);

        WebUtils.webSendJSON(json);
    }


    //icd 分类添加http://localhost:8080/his-portal/icdAssort/addIcdAssort.action
    @Action("/addIcdAssort")
    public void addIcdAssort() {
        String flag = "error";
        try {
            if (StringUtils.isNotBlank(assort_Name) && StringUtils.isNotBlank(id)) {
                icdAssortService.addIcdAssort(assort_Name.trim(), id);
                flag = "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", flag);

        String json = JSONUtils.toJson(map);

        WebUtils.webSendJSON(json);
    }


    //icd-10疾病编码表列表查询

    /**
     * <p>icd疾病编码表列表查询 </p>
     *
     * @Author: zhangkui
     * @CreateDate: 2017年12月11日 上午11:34:49
     * @Modifier: zhangkui
     * @ModifyDate: 2017年12月11日 上午11:34:49
     * @ModifyRmk:
     * @version: V1.0
     * @throws: http://localhost:8080/his-portal/icdAssort/icdList.action
     */
    @Action("/icdList")
    public void icd10List() {
        List<IcdAssortVo> list = null;

        try {
            list = icdAssortService.findIcdList(page, rows, icdCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (list == null) {
            list = new ArrayList<IcdAssortVo>();
        }
        Integer total = 0;

        try {
            total = icdAssortService.findIcdCount(page, rows, icdCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("total", total);
        map.put("rows", list);

        String json = JSONUtils.toJson(map);

        WebUtils.webSendJSON(json);
    }

    //icd-10---疾病编码表--》修改icd-10分类

    /**
     * <p>修改icd-10分类</p>
     *
     * @Author: zhangkui
     * @CreateDate: 2017年12月11日 下午1:48:08
     * @Modifier: zhangkui
     * @ModifyDate: 2017年12月11日 下午1:48:08
     * @ModifyRmk:
     * @version: V1.0
     * @throws: http://localhost:8080/his-portal/icdAssort/updateIcdSorrt.action
     */
    @Action("/updateIcdSorrt")
    public void updateIcdSorrt() {
        String flag = "error";
        try {
            if (StringUtils.isNotBlank(icdId) && StringUtils.isNotBlank(assortId)) {
                icdAssortService.updateIcdSorrt(icdId, assortId);
                flag = "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", flag);

        String json = JSONUtils.toJson(map);

        WebUtils.webSendJSON(json);
    }


    @Action(value = "/toOptionUI", results = {@Result(name = "list", location = "/WEB-INF/pages/stat/icd/toOptionUI.jsp")})
    public String toOptionUI() {

        return "list";
    }


    /**************************************************************************get and set*********************************************************/

    /**根据分类的id查询该分类下的(包括子分类)的icd编码列表
     * 根据分类id
    /**
     * <p>根据分类的id查询该分类下的(包括子分类)的icd编码列表 </p>
     *
     * @Author: zhangkui
     * @CreateDate: 2017年12月15日 上午9:27:58
     * @Modifier: zhangkui
     * @ModifyDate: 2017年12月15日 上午9:27:58
     * @ModifyRmk:
     * @version: V1.0
     * @throws:
     */
    @Action(value = "icdCodeList")
    public void icdCodeList() {

        List<String> list = null;
        try {
            if (StringUtils.isNotBlank(id)) {
                //1.前台会过滤数据，保证传递过来的只是分类的id
                list = icdAssortService.queryIcdCodesByIcdAssortId(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (list == null) {
            list = new ArrayList<String>();
        }

        String json = JSONUtils.toJson(list);
        WebUtils.webSendJSON(json);

    }


    /**
     * <p>根据父id加载tree列表包含树下的icd </p>
     *
     * @Author: zhangkui
     * @CreateDate: 2017年12月15日 上午9:27:54
     * @Modifier: zhangkui
     * @ModifyDate: 2017年12月15日 上午9:27:54
     * @ModifyRmk:
     * @version: V1.0
     */
    @Action(value = "icdTree")
    public void icdTree() {

        List<IcdAssortTree> list = null;
        try {
            //1.当是第一次加载的时候，前台默认传root
            //2.当点击父节点的时候默认传root,节点的id
            //3.当时搜索的时候，id为null,assortName有值
            if (StringUtils.isNotBlank(id)) {
                if (id.contains(",")) {//说明是情况2,真正的id是','后边的值
                    id = id.substring(id.indexOf(",") + 1, id.length()).trim();
                }
            }
            list = icdAssortService.icdTree(id, assortName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (list == null) {
            list = new ArrayList<IcdAssortTree>();
        }

        String json = JSONUtils.toJson(list);
        WebUtils.webSendJSON(json);
    }


    @Action(value = "/toIcdTreeUI", results = {@Result(name = "list", location = "/WEB-INF/pages/stat/bi/disease/distribution/toIcdTreeUI.jsp")})
    public String toIcdTreeUI() {

        return "list";
    }

    @Action(value = "/toSelectIcdUI", results = {@Result(name = "list", location = "/WEB-INF/pages/stat/bi/disease/distribution/toSelectIcdUI.jsp")})
    public String toSelectIcdUI() {

        return "list";
    }


    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getIcdId() {
        return icdId;
    }

    public void setIcdId(String icdId) {
        this.icdId = icdId;
    }

    public String getAssortId() {
        return assortId;
    }

    public void setAssortId(String assortId) {
        this.assortId = assortId;
    }

    public String getAssort_Name() {
        return assort_Name;
    }

    public void setAssort_Name(String assort_Name) {
        this.assort_Name = assort_Name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcdCode() {
        return icdCode;
    }

    public void setIcdCode(String icdCode) {
        this.icdCode = icdCode;
    }


    public String getAssortName() {
        return assortName;
    }

    public void setAssortName(String assortName) {
        this.assortName = assortName;
    }


}
