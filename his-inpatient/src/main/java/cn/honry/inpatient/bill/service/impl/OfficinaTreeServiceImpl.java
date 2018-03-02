package cn.honry.inpatient.bill.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inpatient.bill.dao.OfficinaTreeDAO;
import cn.honry.inpatient.bill.service.OfficinaTreeService;
import cn.honry.utils.TreeJson;

@Service("officinaTreeService")
@Transactional
@SuppressWarnings({ "all" })
public class OfficinaTreeServiceImpl implements OfficinaTreeService{
	
	@Autowired
	@Qualifier(value="officinaTreeDAO")
	private OfficinaTreeDAO officinaTreeDAO;
	
	@Override
	public void removeUnused(String id) {
	}

	@Override
	public SysDepartment get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(SysDepartment entity) {
	}

	@Override
	public List<TreeJson> QueryTreeDepartmen(int flag) {
		List<SysDepartment> sysDepartmentList =officinaTreeDAO.findTreeType(flag);
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		String sType="";
		TreeJson topTreeJson = new TreeJson();
		topTreeJson.setId("1");
		topTreeJson.setText("科室信息");
		topTreeJson.setIconCls("icon-branch");
		Map<String,String> tAttMap = new HashMap<String, String>();
		topTreeJson.setAttributes(tAttMap);
		treeJsonList.add(topTreeJson);
		if(sysDepartmentList!=null&&sysDepartmentList.size()>0){
			String curType="";
			for(SysDepartment sysDepartment : sysDepartmentList){
				TreeJson treeJson = new TreeJson();
				if(sysDepartment.getDeptType()!=null && !sysDepartment.getDeptType().equals(curType)){
					TreeJson typeTreeJson = new TreeJson();
					curType=sysDepartment.getDeptType();
					typeTreeJson.setId("type_"+curType);
					if("P".equals(curType)){
						typeTreeJson.setText("药房");
						typeTreeJson.setIconCls("icon-brick");
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
					typeTreeJson.setAttributes(attributes);
					treeJsonList.add(typeTreeJson);							
				}
				treeJson.setId(sysDepartment.getId());
				treeJson.setText(sysDepartment.getDeptName());
				if(sysDepartment.getDeptHasson()==0){
					treeJson.setIconCls("icon-user_brown");
				}
				Map<String,String> attributes = new HashMap<String, String>();
				if(sysDepartment.getDeptLevel()==1){
					attributes.put("pid","type_"+curType);
				}else{
					attributes.put("pid",sysDepartment.getDeptParent());
				}
				attributes.put("hasson",sysDepartment.getDeptHasson().toString());
				attributes.put("deptCode",sysDepartment.getDeptCode().toString());
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}

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
		return TreeJson.formatTree(treeJsonList);
	}

}
