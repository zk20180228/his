package cn.honry.statistics.bi.statisticalSetting.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.BiDimensionSet;
import cn.honry.base.bean.model.BiStatSet;
import cn.honry.statistics.bi.statisticalSetting.service.StatisticalSettingService;
import cn.honry.statistics.bi.statisticalSetting.vo.VoshowList;
import cn.honry.statistics.bi.statisticalSetting.vo.VtableName;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**  
 *  统计设置Action
 * @Author:donghe
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/bi/statisticalSetting")
@SuppressWarnings({"all"})
public class StatisticalSettingAction extends ActionSupport{
	private StatisticalSettingService statisticalSettingService;
	@Autowired
	@Qualifier(value = "statisticalSettingService")
	public void setStatisticalSettingService(
			StatisticalSettingService statisticalSettingService) {
		this.statisticalSettingService = statisticalSettingService;
	}
	/************************************************参数***************************************************************************/
	//分页
	private String page;
	private String rows;
	private String menuAlias;
	private BiStatSet statSet=new BiStatSet();
	private String biSubsectionSetJson;//分段
	private String biIndexSetJson;//指标
	private String indexId;
	private String dimensionNumbers;
	private String weiyi;
	private List<BiDimensionSet> biDimensionSetlist=new ArrayList<BiDimensionSet>();
	private String setGroupid;
	/**字段名称**/
	private String columnName;
	/**多个id**/
	private String idss;
	/**添加或修改标志**/
	private String flag;
	/**维度编号**/
	private String dimensionNumber;
	
	public String getSetGroupid() {
		return setGroupid;
	}
	public void setSetGroupid(String setGroupid) {
		this.setGroupid = setGroupid;
	}
	public String getWeiyi() {
		return weiyi;
	}
	public void setWeiyi(String weiyi) {
		this.weiyi = weiyi;
	}
	public String getDimensionNumbers() {
		return dimensionNumbers;
	}
	public void setDimensionNumbers(String dimensionNumbers) {
		this.dimensionNumbers = dimensionNumbers;
	}
	public String getIndexId() {
		return indexId;
	}
	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}
	public String getBiSubsectionSetJson() {
		return biSubsectionSetJson;
	}
	public void setBiSubsectionSetJson(String biSubsectionSetJson) {
		this.biSubsectionSetJson = biSubsectionSetJson;
	}
	public String getBiIndexSetJson() {
		return biIndexSetJson;
	}
	public void setBiIndexSetJson(String biIndexSetJson) {
		this.biIndexSetJson = biIndexSetJson;
	}
	public List<BiDimensionSet> getBiDimensionSetlist() {
		return biDimensionSetlist;
	}
	public void setBiDimensionSetlist(List<BiDimensionSet> biDimensionSetlist) {
		this.biDimensionSetlist = biDimensionSetlist;
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
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public BiStatSet getStatSet() {
		return statSet;
	}
	public void setStatSet(BiStatSet statSet) {
		this.statSet = statSet;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getIdss() {
		return idss;
	}
	public void setIdss(String idss) {
		this.idss = idss;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getDimensionNumber() {
		return dimensionNumber;
	}
	public void setDimensionNumber(String dimensionNumber) {
		this.dimensionNumber = dimensionNumber;
	}
	/************************************************方法**************************************************************/
	/**
	 * 跳转到统计图表设置页面	
	 * @return
	 * @throws ClassNotFoundException 
	 */
	@RequiresPermissions(value={"TJTBSZ:function:view"})
	@Action(value = "statisticalSettinglist", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/statisticalSetting/statisticalSetting.jsp") },
	interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String statisticalSettinglist() throws ClassNotFoundException{//跳转
		return "list";
	}
	/**  
	 * 
	 * @Description：  查询统计图形列表
	 * @Author：dh
	 * @ModifyDate：2016-7-12
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "queryStatSetList")
	public void queryStatSetList(){
		List<BiStatSet> list = statisticalSettingService.queryBiStatSetList(statSet, page, rows);
		int total = statisticalSettingService.queryBiStatSetTotal(statSet);
		Map<String,Object> listMap = new HashMap<String,Object>();
		listMap.put("total", total);
		listMap.put("rows", list);
		String mapJosn = JSONUtils.toJson(listMap);
		WebUtils.webSendJSON(mapJosn);
	}
	
	/**
	 * 跳转到统计图表设置添加或修改页面	
	 * @return
	 */
	@RequiresPermissions(value={"TJTBSZ:function:add"})
	@Action(value = "statisticalSettingadd", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/statisticalSetting/statisticalSettingAdd.jsp") },
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String statisticalSettingadd(){//跳转到收入统计汇总页面
		List list = new ArrayList();
		if(StringUtils.isNotBlank(idss)){
			statSet=statisticalSettingService.get(idss);
		}else{
			statSet.setSetGroupname("第一组");
			statSet.setSetGroupid("1");
			statSet.setSetType(1);
			statSet.setSetSqlType(1);
		}
		return "list";
	}
	
	/**
	 * 跳转到统计图表设置添加或修改页面	
	 * @return
	 */
	@Action(value = "biDimensionSetadd", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/statisticalSetting/dimension.jsp") },
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String biDimensionSetadd(){//跳转到收入统计汇总页面
		biDimensionSetlist = statisticalSettingService.queryBiDimensionSet(dimensionNumber);
		return "list";
	}
	/**  
	 *  
	 * @Description：  查询table下拉
	 * @Author：dh
	 * @ModifyDate：2016-7-12
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "querytableList")
	public void querytableList(){
		List<VtableName> list = statisticalSettingService.querytablename();
		String mapJosn = JSONUtils.toJson(list);
		WebUtils.webSendJSON(mapJosn);
	}
	/**  
	 *  
	 * @Description：  查询表字段的下拉
	 * @Author：dh
	 * @ModifyDate：2016-7-12
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "querycolumnNameList")
	public void querycolumnNameList(){
		List<VtableName> list = statisticalSettingService.queryColumnname(columnName);
		String mapJosn = JSONUtils.toJson(list);
		WebUtils.webSendJSON(mapJosn);
	}
	/**  
	 *  
	 * @Description：  保存统计表单
	 * @Author：dh
	 * @ModifyDate：2016-7-12
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "saveStatSet")
	public void saveStatSet(){
		String result="";
		try{
			statisticalSettingService.saveOrUpdate(statSet);
			result="success";
		}catch(Exception e){
			result="error";
		}
		WebUtils.webSendString(result);
	}
	/**
	 * @Description:删除统计
	 * @Author：dh
	 * @ModifyDate：2016-7-12
	 * @ModifyRmk： 
	 * @version 1.0
	**/
	@Action(value="delStatSet")
	public void delStatSet (){
		String result="";
		try{
			String[] sourceStrArray = idss.split(",");
	        for (int i = 0; i < sourceStrArray.length; i++) {
	            statisticalSettingService.removeUnused(sourceStrArray[i]);
	        }
			result="success";
		}catch(Exception e){
			result="error";
		}
		WebUtils.webSendString(result);
	}
	/**  
	 *  
	 * @Description：  查询维度下拉
	 * @Author：dh
	 * @ModifyDate：2016-7-12
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "queryBiDimensionSetList")
	public void queryBiDimensionSetList(){
		List<BiDimensionSet> list = new ArrayList<BiDimensionSet>();
		List<BiDimensionSet> list1 = new ArrayList<BiDimensionSet>();
		if(dimensionNumber==null){
			list = statisticalSettingService.queryBiDimensionSet(dimensionNumber);
			String mapJosn = JSONUtils.toJson(list);
			WebUtils.webSendJSON(mapJosn);
		}else{
			int sort = 0;
			String[] sourceStrArray = dimensionNumber.split(",");
			for (int i = 0; i < sourceStrArray.length; i++) {
				list = statisticalSettingService.queryBiDimensionSet(sourceStrArray[i]);
				sort = sort+1;
				list.get(0).setSort(String.valueOf(sort));
				list1.addAll(list);
			}
			String mapJosn = JSONUtils.toJson(list1);
			WebUtils.webSendJSON(mapJosn);
		}
	}
	/**  
	 *  
	 * @Description：  排序设置
	 * @Author：dh
	 * @ModifyDate：2016-7-12
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "updateSort")
	public void updateSort(){
		List<BiDimensionSet> list = new ArrayList<BiDimensionSet>();
		List<BiDimensionSet> list1 = new ArrayList<BiDimensionSet>();
		int sort = 0;
		String[] sourceStrArray = dimensionNumbers.split(",");
		for (int i = 0; i < sourceStrArray.length; i++) {
			list = statisticalSettingService.queryBiDimensionSet(sourceStrArray[i]);
			sort = sort+1;
			list.get(0).setSort(String.valueOf(sort));
			list1.addAll(list);
		}
		if("1".equals(weiyi)){
			list1.get(Integer.valueOf(indexId)).setSort(String.valueOf(Integer.valueOf(list1.get(Integer.valueOf(indexId)).getSort())-1));
			list1.get(Integer.valueOf(indexId)-1).setSort(String.valueOf((Integer.valueOf(list1.get(Integer.valueOf(indexId)-1).getSort())+1)));
			Collections.swap(list1, Integer.valueOf(indexId), Integer.valueOf(indexId)-1);
		}
		if("2".equals(weiyi)){
			list1.get(Integer.valueOf(indexId)).setSort(String.valueOf(Integer.valueOf(list1.get(Integer.valueOf(indexId)).getSort())+1));
			list1.get(Integer.valueOf(indexId)+1).setSort(String.valueOf((Integer.valueOf(list1.get(Integer.valueOf(indexId)+1).getSort())-1)));
			Collections.swap(list1, Integer.valueOf(indexId), Integer.valueOf(indexId)+1);
		}
		String mapJosn = JSONUtils.toJson(list1);
		WebUtils.webSendJSON(mapJosn);
	}
	/**  
	 *  
	 * @Description：  保存指标和维度
	 * @Author：dh
	 * @ModifyDate：2016-7-12
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "saveBiSubsectionSetOrBiIndexSet")
	public void saveBiSubsectionSetOrBiIndexSet(){
		String result="";
		try{
			statisticalSettingService.saveIndexOrSubsection(biSubsectionSetJson, biIndexSetJson, dimensionNumber);
			result="保存成功";
		}catch(Exception e){
			result="error";
		}
		WebUtils.webSendString(result);
	}
	/**
	 * 跳转到列表显示的页面	
	 * @return
	 * @throws ClassNotFoundException 
	 */
	@Action(value = "showlist", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/statisticalSetting/showlist.jsp") },
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String showlist() throws ClassNotFoundException{
		return "list";
	}
	/**  
	 *  
	 * @Description：  合并表查询
	 * @Author：dh
	 * @ModifyDate：2016-7-12
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ClassNotFoundException 
	 *
	 */
	@Action(value = "listshow")
	public void listshow() throws ClassNotFoundException{
		List<Object> list = new ArrayList<Object>();
		list = statisticalSettingService.queryObject();
		//设置类成员属性
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		List<VtableName> vtableNames = statisticalSettingService.queryViewColumnName("V_DYNAMICS_LIST");
		for (int i = 0; i < vtableNames.size(); i++) {
			if("VARCHAR2".equals(vtableNames.get(i).getColumnType())){
				hashMap.put(vtableNames.get(i).getColumnName(), Class.forName("java.lang.String"));
			}else if("NUMBER".equals(vtableNames.get(i).getColumnType())){
				hashMap.put(vtableNames.get(i).getColumnName(), Class.forName("java.lang.String"));
			}
		}
		String columns = "";
		//动态生成Bean
		/*CglibBeanUtil bean = new CglibBeanUtil(hashMap);
		for (Object key : bean.beanMap.keySet()) {
			if(columns.equals("")){
				columns = key.toString();
			}else{
				columns += "," + key;
			}
		}
		String str[] = columns.split(",");
		HashMap<String, Object> hashMap1 = new HashMap<String, Object>();
		List list2 = new ArrayList();
		String aa = null;
		String bb = null;
		String cc = null;
		String dd = null;
		String ee = null;
		String gg = null;
		String hh = null;
		for (int i = 0; i < list.size(); i++) {
			Object ss = list.get(i);
			Object[] d =(Object[]) ss; 
			for (int j = 0; j < d.length; j++) {
				if(aa==null){
					aa = String.valueOf(d[0]);
				}else{
					aa += "," + String.valueOf(d[0]);
				}
				String aaa[] = aa.split(",");
				if(aaa.length==9){
					list2.add(aa);
				}
				
				if(bb==null){
					bb = String.valueOf(d[1]);
				}else{
					bb += "," + String.valueOf(d[1]);
				}
				String bbb[] = bb.split(",");
				if(bbb.length==9){
					list2.add(bb);
				}
				
				if(cc==null){
					cc = String.valueOf(d[2]);
				}else{
					cc += "," + String.valueOf(d[2]);
				}
				String ccc[] = cc.split(",");
				if(ccc.length==9){
					list2.add(cc);
				}
				
				if(dd==null){
					dd = String.valueOf(d[3]);
				}else{
					dd += "," + String.valueOf(d[3]);
				}
				String ddd[] = dd.split(",");
				if(ddd.length==9){
					list2.add(dd);
				}
				
				if(ee==null){
					ee = String.valueOf(d[4]);
				}else{
					ee += "," + String.valueOf(d[4]);
				}
				String eee[] = ee.split(",");
				if(eee.length==9){
					list2.add(ee);
				}
				
				if(gg==null){
					gg = String.valueOf(d[5]);
				}else{
					gg += "," + String.valueOf(d[5]);
				}
				String ggg[] = gg.split(",");
				if(ggg.length==9){
					list2.add(gg);
				}
				
				if(hh==null){
					hh = String.valueOf(d[6]);
				}else{
					hh += "," + String.valueOf(d[6]);
				}
				String hhh[] = hh.split(",");
				if(hhh.length==9){
					list2.add(hh);
				}
				break;
			}
		}
		
		for (int i = 0; i < list2.size(); i++) {
			bean.setValue("NAME", list2.get(0));
			bean.setValue("2014", list2.get(1));
			bean.setValue("2015", list2.get(2));
			bean.setValue("2016", list2.get(3));
			bean.setValue("COUNT1", list2.get(4));
			bean.setValue("DEPTCODE", list2.get(5));
			bean.setValue("COUNT2", list2.get(6));
		}
		String json=JSONUtils.toJson(bean.beanMap);
		WebUtils.webSendJSON(json);*/
	}
	/**  
	 *  
	 * @Description：  排序设置
	 * @Author：dh
	 * @ModifyDate：2016-7-12
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "querylistshow")
	public void querylistshow(){
		List<VoshowList> voshowLists =statisticalSettingService.queryListShowList();
		String mapJosn = JSONUtils.toJson(voshowLists);
		WebUtils.webSendJSON(mapJosn);
	}
}
