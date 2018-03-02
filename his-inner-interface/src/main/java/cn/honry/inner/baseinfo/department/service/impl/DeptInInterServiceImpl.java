package cn.honry.inner.baseinfo.department.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.FictitiousContact;
import cn.honry.base.bean.model.FictitiousDept;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.department.vo.FicDeptVO;
import cn.honry.inner.baseinfo.employee.dao.EmployeeInInterDAO;
import cn.honry.inner.system.user.dao.UserInInterDAO;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.TreeJson;

/**  
 *  
 * 内部接口：科室 
 * @Author：aizhonghua
 * @CreateDate：2016-7-05 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-7-05 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("deptInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class DeptInInterServiceImpl implements DeptInInterService{

	/**
	 * 缓存类
	 */
	@Resource
	private RedisUtil redis;
	
	@Autowired
	@Qualifier(value = "deptInInterDAO")
	private DeptInInterDAO deptInInterDAO;
	@Autowired
	@Qualifier(value = "employeeInInterDAO")
	private EmployeeInInterDAO employeeInInterDAO;
	@Autowired
	@Qualifier(value = "userInInterDAO")
	private UserInInterDAO userInInterDAO;
	@Autowired
	@Qualifier(value = "dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public SysDepartment get(String id) {
		return deptInInterDAO.get(id);
	}

	@Override
	public void saveOrUpdate(SysDepartment entity) {
		
	}
	
	/**  
	 *  
	 * 根据id获得部门
	 * @Author：aizhonghua
	 * @CreateDate：2015-10-22 上午11:57:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-10-22 上午11:57:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public SysDepartment getDeptById(String deptId) {
		return deptInInterDAO.get(deptId);
	}
	
	/**
	 *
	 * <li>根据科室获得关联的病区（护士站）
	 * <li>参数科室本身不能为病区
	 * <li>如果该科室关联多个病区默认取第一个做为登录病区
	 * <li>如果该科室没有关联病区则返回null
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月21日 上午9:57:55 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param id 科室id 该科室不能为病区
	 * @return：病区
	 *
	 */
	@Override
	public SysDepartment getNursingStationByLoginDeptId(String id) {
		return deptInInterDAO.getNursingStationByLoginDeptId(id);
	}

	/**  
	 * 获得登录用户关联的科室
	 * @Description:
	 * @Author：aizhonghua
     * @CreateDate：2016-4-20 下午09:44:35  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-4-20 下午09:44:35  
	 * @ModifyRmk：  新增默认关联员工的所属科室
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> queryDepts(String userId) {
		return deptInInterDAO.findDeptsByUserId(userId);
	}
	
	/**  
	 * 获得部门树
	 * @Description:
	 * @Author：aizhonghua
     * @CreateDate：2016-4-20 下午09:44:35  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-4-20 下午09:44:35  
	 * @version 1.0
	 *
	 */
	@Override
	public List<TreeJson> QueryTreeDepartmen(boolean treeAll, String deptType,Integer deptDistrict,String hospitalId) {
		List<TreeJson> tree=null;
		try {
			if(StringUtils.isBlank(deptType)){
				tree=(List<TreeJson>) redis.get("deptTree");
				if(tree!=null){
					return tree;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<SysDepartment> sysDepartmentList = new ArrayList<SysDepartment>();
		if("false".equals(treeAll)){
			sysDepartmentList = deptInInterDAO.findTree(false,deptType);
		}else{
			sysDepartmentList =deptInInterDAO.findTree(true,deptType);
		}
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		String sType="";
		TreeJson topTreeJson = new TreeJson();
		topTreeJson.setId("1");
		topTreeJson.setText("科室信息");
		topTreeJson.setIconCls("icon-branch");
		Map<String,String> tAttMap = new HashMap<String, String>();
		topTreeJson.setAttributes(tAttMap);
		treeJsonList.add(topTreeJson);
		if(sysDepartmentList!=null){
			String curType="";
			for(SysDepartment sysDepartment : sysDepartmentList){
				TreeJson treeJson = new TreeJson();
				if(sysDepartment.getDeptType()!=null && !sysDepartment.getDeptType().equals(curType)){
					TreeJson typeTreeJson = new TreeJson();
					curType=sysDepartment.getDeptType();
					typeTreeJson.setId("type_"+curType);
					if(curType.equals("C")){
						typeTreeJson.setText("门诊");
						typeTreeJson.setIconCls("icon-vcard");
					}else if(curType.equals("I")){
						typeTreeJson.setText("住院");
						typeTreeJson.setIconCls("icon-application_side_list");
					}else if(curType.equals("F")){
						typeTreeJson.setText("财务");
						typeTreeJson.setIconCls("icon-coins");
					}else if(curType.equals("L")){
						typeTreeJson.setText("后勤");
						typeTreeJson.setIconCls("icon-report");
					}else if(curType.equals("PI")){
						typeTreeJson.setText("药库");
						typeTreeJson.setIconCls("icon-bullet_home");
					}else if(curType.equals("T")){
						typeTreeJson.setText("医技");
						typeTreeJson.setIconCls("icon-application_side_list");
					}else if(curType.equals("D")){
						typeTreeJson.setText("行政");
						typeTreeJson.setIconCls("icon-client");
					}else if(curType.equals("P")){
						typeTreeJson.setText("药房");
						typeTreeJson.setIconCls("icon-brick");
					}else if(curType.equals("N")){
						typeTreeJson.setText("护士站");
						typeTreeJson.setIconCls("icon-bullet_home");
					}else if(curType.equals("S")){
						typeTreeJson.setText("科研");
						typeTreeJson.setIconCls("icon-bullet_home");
					}else if(curType.equals("OP")){
						typeTreeJson.setText("手术");
						typeTreeJson.setIconCls("icon-bullet_home");
					}else if(curType.equals("U")){
						typeTreeJson.setText("科室");
						typeTreeJson.setIconCls("icon-bullet_home");
					}else if(curType.equals("O")){
						typeTreeJson.setText("其他");
						typeTreeJson.setIconCls("icon-bullet_home");
					}
					if("".equals(sType)){
						sType=curType;
					}else{
						sType+=","+curType;
					}
					Map<String,String> attributes = new HashMap<String, String>();
					attributes.put("pid","1");
					attributes.put("hasson","1");
					attributes.put("deptCode","code_"+curType);
					attributes.put("code", sysDepartment.getDeptCode());
					typeTreeJson.setState("closed");
					typeTreeJson.setAttributes(attributes);
					treeJsonList.add(typeTreeJson);							
				}
				
				treeJson.setId(sysDepartment.getId());
				treeJson.setText(sysDepartment.getDeptName());
				treeJson.setState("open");
				if(sysDepartment.getDeptHasson()==null){
					sysDepartment.setDeptHasson(0);
				}
				if(sysDepartment.getDeptHasson()==0){
					treeJson.setIconCls("icon-user_brown");
				}
				
				Map<String,String> attributes = new HashMap<String, String>();
				if(sysDepartment.getDeptLevel()==1){
					attributes.put("pid","type_"+curType);
				}else{
					attributes.put("pid",sysDepartment.getDeptParent());
				}
				attributes.put("hasson","2");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}

		}

		if(StringUtils.isBlank(deptType)){//当查询所有的科室时才显示所有的分类
			if((","+sType+",").indexOf(",C,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_C");
				treeJson.setText("门诊");
				treeJson.setIconCls("icon-vcard");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_C");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",I,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_I");
				treeJson.setText("住院");
				treeJson.setIconCls("icon-application_side_list");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_I");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",F,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_F");
				treeJson.setText("财务");
				treeJson.setIconCls("icon-coins");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_F");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",L,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_L");
				treeJson.setText("后勤");
				treeJson.setIconCls("icon-report");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_L");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",PI,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_PI");
				treeJson.setText("药库");
				treeJson.setIconCls("icon-bullet_home");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_PI");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",T,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_T");
				treeJson.setText("医技");
				treeJson.setIconCls("icon-application_side_list");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_T");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",D,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_D");
				treeJson.setText("行政");
				treeJson.setIconCls("icon-client");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_D");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",P,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_P");
				treeJson.setText("药房");
				treeJson.setIconCls("icon-application_side_list");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_P");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",N,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_N");
				treeJson.setText("护士站");
				treeJson.setIconCls("icon-application_cascade");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_N");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",S,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_S");
				treeJson.setText("科研");
				treeJson.setIconCls("icon-application_side_list");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_S");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",OP,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_OP");
				treeJson.setText("手术");
				treeJson.setIconCls("icon-application_side_list");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_OP");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",U,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_U");
				treeJson.setText("科室");
				treeJson.setIconCls("icon-application_side_list");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_U");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",O,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_O");
				treeJson.setText("其他");
				treeJson.setIconCls("icon-application_side_list");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_O");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
		}
		tree = TreeJson.formatTree(treeJsonList);
		try {
			if(StringUtils.isBlank(deptType)){
				redis.set("deptTree", tree);
				redis.persist("deptTree");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tree;
	}
	public List<TreeJson> QueryTreeFicDepartmen(boolean treeAll, String deptType,Integer deptDistrict,String hospitalId){
		List<TreeJson> tree=null;
		//建此map的原因是排除重复添加数据，添加过的节点不再进行添加
		Map<String,String> map = new HashMap<String,String>();
		try {
			if(StringUtils.isBlank(deptType)){
				tree=(List<TreeJson>) redis.get("deptTree");
				if(tree!=null){
					return tree;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<FictitiousDept> FictitiousDeptList = new ArrayList<FictitiousDept>();
		if("false".equals(treeAll)){
			FictitiousDeptList = deptInInterDAO.findFicDept(false,deptType, null, null);
		}else{
			FictitiousDeptList =deptInInterDAO.findFicDept(true,deptType, null, null);
		}
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		String sType="";
		TreeJson topTreeJson = new TreeJson();
		topTreeJson.setId("1");
		topTreeJson.setText("科室信息");
		topTreeJson.setIconCls("icon-branch");
		Map<String,String> tAttMap = new HashMap<String, String>();
		topTreeJson.setAttributes(tAttMap);
		treeJsonList.add(topTreeJson);
		if(FictitiousDeptList!=null){
			String curType="";
			for(FictitiousDept dic : FictitiousDeptList){
				TreeJson treeJson = new TreeJson();
				if(dic.getDeptType()!=null && !dic.getDeptType().equals(curType)){
					curType=dic.getDeptType();
					if(!map.containsKey(curType)){
						map.put(curType, curType);
						TreeJson typeTreeJson = new TreeJson();
						typeTreeJson.setId("type_"+curType);
						if(curType.equals("C")){
							typeTreeJson.setText("门诊");
							typeTreeJson.setIconCls("icon-vcard");
						}else if(curType.equals("I")){
							typeTreeJson.setText("住院");
							typeTreeJson.setIconCls("icon-application_side_list");
						}else if(curType.equals("F")){
							typeTreeJson.setText("财务");
							typeTreeJson.setIconCls("icon-coins");
						}else if(curType.equals("L")){
							typeTreeJson.setText("后勤");
							typeTreeJson.setIconCls("icon-report");
						}else if(curType.equals("PI")){
							typeTreeJson.setText("药库");
							typeTreeJson.setIconCls("icon-bullet_home");
						}else if(curType.equals("T")){
							typeTreeJson.setText("医技");
							typeTreeJson.setIconCls("icon-application_side_list");
						}else if(curType.equals("D")){
							typeTreeJson.setText("行政");
							typeTreeJson.setIconCls("icon-client");
						}else if(curType.equals("P")){
							typeTreeJson.setText("药房");
							typeTreeJson.setIconCls("icon-brick");
						}else if(curType.equals("N")){
							typeTreeJson.setText("护士站");
							typeTreeJson.setIconCls("icon-bullet_home");
						}else if(curType.equals("S")){
							typeTreeJson.setText("科研");
							typeTreeJson.setIconCls("icon-bullet_home");
						}else if(curType.equals("OP")){
							typeTreeJson.setText("手术");
							typeTreeJson.setIconCls("icon-bullet_home");
						}else if(curType.equals("U")){
							typeTreeJson.setText("科室");
							typeTreeJson.setIconCls("icon-bullet_home");
						}else if(curType.equals("O")){
							typeTreeJson.setText("其他");
							typeTreeJson.setIconCls("icon-bullet_home");
						}
						if("".equals(sType)){
							sType=curType;
						}else{
							sType+=","+curType;
						}
						Map<String,String> attributes = new HashMap<String, String>();
						attributes.put("pid","1");
						attributes.put("hasson","1");
						attributes.put("deptCode","code_"+curType);
						attributes.put("code", dic.getDeptCode());
						typeTreeJson.setAttributes(attributes);
						treeJsonList.add(typeTreeJson);							
					}
				}
				
				treeJson.setId(dic.getDeptCode());
				treeJson.setText(dic.getDeptName());

//				if(dic.getDeptHasson()==null){
//					dic.setDeptHasson(0);
//				}
//				if(dic.getDeptHasson()==0){
					treeJson.setIconCls("icon-bullet_home");
//				}
				
				Map<String,String> attributes = new HashMap<String, String>();
				if(dic.getDeptLevel()==1){
					attributes.put("pid","type_"+curType);
				}else{
					attributes.put("pid",dic.getDeptParent());
				}
				attributes.put("hasson","1");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
				List<FictitiousContact> conDept = deptInInterDAO.findFicConDept(treeAll, null,dic.getDeptCode());
				for (FictitiousContact con : conDept) {
					TreeJson ctreeJson = new TreeJson();
					ctreeJson.setId(con.getDeptCode());
					ctreeJson.setText(con.getDeptName());
					ctreeJson.setIconCls("icon-user_brown");
					Map<String,String> attr = new HashMap<String, String>();
					attr.put("pid", con.getFictCode());
					attr.put("hasson","2");
					ctreeJson.setAttributes(attr);
					treeJsonList.add(ctreeJson);
				}
			}

		}

		if(StringUtils.isBlank(deptType)){//当查询所有的科室时才显示所有的分类
			if((","+sType+",").indexOf(",C,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_C");
				treeJson.setText("门诊");
				treeJson.setIconCls("icon-vcard");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_C");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",I,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_I");
				treeJson.setText("住院");
				treeJson.setIconCls("icon-application_side_list");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_I");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",F,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_F");
				treeJson.setText("财务");
				treeJson.setIconCls("icon-coins");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_F");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",L,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_L");
				treeJson.setText("后勤");
				treeJson.setIconCls("icon-report");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_L");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",PI,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_PI");
				treeJson.setText("药库");
				treeJson.setIconCls("icon-bullet_home");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_PI");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",T,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_T");
				treeJson.setText("医技");
				treeJson.setIconCls("icon-application_side_list");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_T");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",D,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_D");
				treeJson.setText("行政");
				treeJson.setIconCls("icon-client");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_D");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",P,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_P");
				treeJson.setText("药房");
				treeJson.setIconCls("icon-application_side_list");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_P");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",N,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_N");
				treeJson.setText("护士站");
				treeJson.setIconCls("icon-application_cascade");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_N");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",S,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_S");
				treeJson.setText("科研");
				treeJson.setIconCls("icon-application_side_list");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_S");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",OP,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_OP");
				treeJson.setText("手术");
				treeJson.setIconCls("icon-application_side_list");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_OP");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",U,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_U");
				treeJson.setText("科室");
				treeJson.setIconCls("icon-application_side_list");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_U");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			
			if((","+sType+",").indexOf(",O,")==-1){
				TreeJson treeJson = new TreeJson();
				treeJson.setId("type_O");
				treeJson.setText("其他");
				treeJson.setIconCls("icon-application_side_list");
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				attributes.put("hasson","0");
				attributes.put("deptCode","code_O");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
		}
		tree = TreeJson.formatTree(treeJsonList);
		try {
			if(StringUtils.isBlank(deptType)){
				redis.set("deptTree", tree);
				redis.persist("deptTree");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tree;
	}
	/**
	 * @Description:展示科室树的全部，也可展示一部分
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月18日
	 * @param:id:树展开节点时候的id
	 * @param：map：用来选择性展示树的信息，例如，如果map.get("C")不为空，则展示门诊，为空则不展示
	 * @return:List<TreeJson>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public List<TreeJson> findDeptTree(String id,Map<String,String> map) {
		List<TreeJson> retList = new ArrayList<TreeJson>();
		if(StringUtils.isBlank(id)){
			if(StringUtils.isNotBlank(map.get("C"))){
				TreeJson ctreeJson = new TreeJson();
				ctreeJson.setId("C_C");
				ctreeJson.setText("门诊");
				ctreeJson.setState("closed");
				Map<String,String> attributes1 = new HashMap<String,String>();
				attributes1.put("type","0");
				ctreeJson.setAttributes(attributes1);
				retList.add(ctreeJson);
			}
			
			if(StringUtils.isNotBlank(map.get("I"))){
				TreeJson itreeJson = new TreeJson();
				itreeJson.setId("I_I");
				itreeJson.setText("住院");
				itreeJson.setState("closed");
				Map<String,String> attributes2 = new HashMap<String,String>();
				attributes2.put("type","0");
				itreeJson.setAttributes(attributes2);
				retList.add(itreeJson);
			}
			
			if(StringUtils.isNotBlank(map.get("F"))){
				TreeJson ftreeJson = new TreeJson();
				ftreeJson.setId("F_F");
				ftreeJson.setText("财务");
				ftreeJson.setState("closed");
				Map<String,String> attributes3 = new HashMap<String,String>();
				attributes3.put("type","0");
				ftreeJson.setAttributes(attributes3);
				retList.add(ftreeJson);
			}
			
			if(StringUtils.isNotBlank(map.get("PI"))){
				TreeJson pitreeJson = new TreeJson();
				pitreeJson.setId("PI_PI");
				pitreeJson.setText("药库");
				pitreeJson.setState("closed");
				Map<String,String> attributes4 = new HashMap<String,String>();
				attributes4.put("type","0");
				pitreeJson.setAttributes(attributes4);
				retList.add(pitreeJson);
			}
			
			if(StringUtils.isNotBlank(map.get("P"))){
				TreeJson ptreeJson = new TreeJson();
				ptreeJson.setId("P_P");
				ptreeJson.setText("药房");
				ptreeJson.setState("closed");
				Map<String,String> attributes5 = new HashMap<String,String>();
				attributes5.put("type","0");
				ptreeJson.setAttributes(attributes5);
				retList.add(ptreeJson);
			}
			
			if(StringUtils.isNotBlank(map.get("D"))){
				TreeJson dtreeJson = new TreeJson();
				dtreeJson.setId("D_D");
				dtreeJson.setText("行政");
				dtreeJson.setState("closed");
				Map<String,String> attributes6 = new HashMap<String,String>();
				attributes6.put("type","0");
				dtreeJson.setAttributes(attributes6);
				retList.add(dtreeJson);
			}
			
			if(StringUtils.isNotBlank(map.get("T"))){
				TreeJson ttreeJson = new TreeJson();
				ttreeJson.setId("T_T");
				ttreeJson.setText("医技");
				ttreeJson.setState("closed");
				Map<String,String> attributes7 = new HashMap<String,String>();
				attributes7.put("type","0");
				ttreeJson.setAttributes(attributes7);
				retList.add(ttreeJson);
			}
			
			if(StringUtils.isNotBlank(map.get("L"))){
				TreeJson ltreeJson = new TreeJson();
				ltreeJson.setId("L_L");
				ltreeJson.setText("后勤");
				ltreeJson.setState("closed");
				Map<String,String> attributes8 = new HashMap<String,String>();
				attributes8.put("type","0");
				ltreeJson.setAttributes(attributes8);
				retList.add(ltreeJson);
			}
			
			if(StringUtils.isNotBlank(map.get("N"))){
				TreeJson ntreeJson = new TreeJson();
				ntreeJson.setId("N_N");
				ntreeJson.setText("护士站");
				ntreeJson.setState("closed");
				Map<String,String> attributes9 = new HashMap<String,String>();
				attributes9.put("type","0");
				ntreeJson.setAttributes(attributes9);
				retList.add(ntreeJson);
			}
			
			if(StringUtils.isNotBlank(map.get("S"))){
				TreeJson streeJson = new TreeJson();
				streeJson.setId("S_S");
				streeJson.setText("科研");
				streeJson.setState("closed");
				Map<String,String> attributes10 = new HashMap<String,String>();
				attributes10.put("type","0");
				streeJson.setAttributes(attributes10);
				retList.add(streeJson);
			}
			
			if(StringUtils.isNotBlank(map.get("U"))){
				TreeJson utreeJson = new TreeJson();
				utreeJson.setId("U_U");
				utreeJson.setText("科室");
				utreeJson.setState("closed");
				Map<String,String> attributes11 = new HashMap<String,String>();
				attributes11.put("type","0");
				utreeJson.setAttributes(attributes11);
				retList.add(utreeJson);
			}
			if(StringUtils.isNotBlank(map.get("OP"))){
				TreeJson optreeJson = new TreeJson();
				optreeJson.setId("OP_OP");
				optreeJson.setText("手术");
				optreeJson.setState("closed");
				Map<String,String> attributes12 = new HashMap<String,String>();
				attributes12.put("type","0");
				optreeJson.setAttributes(attributes12);
				retList.add(optreeJson);
			}
			
			if(StringUtils.isNotBlank(map.get("O"))){
				TreeJson otreeJson = new TreeJson();
				otreeJson.setId("O_O");
				otreeJson.setText("其他");
				otreeJson.setState("closed");
				Map<String,String> attributes13 = new HashMap<String,String>();
				attributes13.put("type","0");
				otreeJson.setAttributes(attributes13);
				retList.add(otreeJson);
			}
		}else{
			if(id.contains("_")){
				String[] arr = id.split("_");
				List<SysDepartment> deptList = deptInInterDAO.findDeptTree(arr[0]);
				if(deptList.size()>0){
					TreeJson treeJson = null;
					for(SysDepartment dept : deptList){
						treeJson = new TreeJson();
						treeJson.setId(dept.getId());
						treeJson.setText(dept.getDeptName());
						treeJson.setState("closed");
						Map<String,String> attributes = new HashMap<String,String>();
						attributes.put("pid",arr[0]);
						attributes.put("type","1");
						treeJson.setAttributes(attributes);
						retList.add(treeJson);
					}
				}
			}else{
				List<User> userList = null;
				userList = userInInterDAO.findUserByDept(id);
				if(userList!=null&&userList.size()>0){
					TreeJson treeJson = null;
					for(User user : userList){
						treeJson = new TreeJson();
						treeJson.setId(user.getId());
						treeJson.setText(user.getName());
						treeJson.setState("open");
						Map<String,String> attributes = new HashMap<String,String>();
						attributes.put("pid",id);
						attributes.put("type","2");
						List<SysEmployee> employeelist =employeeInInterDAO.findEmpByUserId(user.getId());
						String employeeId = "";//员工Id
						String jobNo = ""; //工作号
						String code = "";  //code
						if(employeelist!=null&&employeelist.size()>0){
							employeeId = employeelist.get(0).getId();
							jobNo = employeelist.get(0).getJobNo();
							code = employeelist.get(0).getCode();
							attributes.put("employeeId", employeeId);
							attributes.put("jobNo", jobNo);
							attributes.put("code", code);
							treeJson.setAttributes(attributes);
							retList.add(treeJson);
						}
					}
				}
			}
		}
		return retList;
	}
	
	
	@Override
	public List<SysDepartment> findTree(boolean b,String deptTypes) {
		return deptInInterDAO.findTree(b,deptTypes);
	}

	/**  
	 *  
	 * @Description：  查询科室
	 * @Author：zhangjin
	 * @CreateDate：2016-7-18 
	 * @Modifier：
	 * @ModifyDate： 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> getDept() {
		try {
			List<SysDepartment> list = (List<SysDepartment>) redis.get("dept_queryAll");
			if(list!=null){
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deptInInterDAO.getDept();
	}

	@Override
	public List<SysDepartment> queryAllDept() {
		try {
			List<SysDepartment> list = (List<SysDepartment>) redis.get("dept_queryAll");
			if(list!=null){
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deptInInterDAO.queryAllDept();
	}

	/**  
	 *  
	 * @Description：  获得科室code和name
	 * @Author：aizhonghua
	 * @CreateDate：2015-5-23 下午03:45:17  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-7 下午14:30:49  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, String> querydeptCodeAndNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<SysDepartment> list=null;
		try {
			list = (List<SysDepartment>) redis.get("dept_queryAll");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list==null){
			list = deptInInterDAO.getAll();
		}
		if(list!=null&&list.size()>0){
			for(SysDepartment dept : list){
				map.put(dept.getDeptCode(), dept.getDeptName());
			}
		}
		return map;
	}

	@Override
	public SysDepartment getDeptCode(String deptCode) {
		try {
			SysDepartment dept=(SysDepartment) redis.hget("dept", deptCode);
			if(dept!=null){
				return dept;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deptInInterDAO.getDeptCode(deptCode);
	}


	@Override
	public SysDepartment getByCode(String code) {
		try {
			SysDepartment dept=(SysDepartment) redis.hget("dept", code);
			if(dept!=null){
				return dept;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deptInInterDAO.getByCode(code);
	}
	
	/**
	 * 根据科室编码、名称、五笔、拼音等及时查询科室信息
	 * @param q
	 * @return
	 */
	public List<SysDepartment> getDeptByQ(String q){
		return deptInInterDAO.getDeptByQ(q);
	}

	/**
	 * 判断该账户是否有关联科室
	 * @param q
	 * @return
	 */
	@Override
	public boolean isHaveDept(String account) {
		return deptInInterDAO.isHaveDept(account);
	}

	@Override
	public List<SysDepartment> getPageByQ(String page, String rows, String q) {
		return deptInInterDAO.getPageByQ(page, rows, q);
	}

	@Override
	public int getTotalByQ(String q) {
		return deptInInterDAO.getTotalByQ(q);
	}

	@Override
	public List<SysDepartment> getDeptByMenutypeAndUserCode(String menutype,String usercode) {
//		return deptInInterDAO.getDeptByMenutypeAndUserCode(menutype, usercode);
		return dataJurisInInterDAO.getJurisDeptList(menutype,usercode);
	}
	
}
