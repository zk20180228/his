package cn.honry.statistics.bi.wdWin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BIBaseDistrict;
import cn.honry.base.bean.model.BiBaseDictionary;
import cn.honry.base.bean.model.BiBaseEmployee;
import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.statistics.bi.wdWin.dao.WdWinDao;
import cn.honry.statistics.bi.wdWin.service.WdWinService;
import cn.honry.utils.TreeJson;
@Service("wdWinService")
@Transactional
@SuppressWarnings({ "all" })
public class WdWinServiceImpl implements WdWinService{
	/** 科室 **/
	@Autowired
	@Qualifier(value = "wdWinDao")
	private WdWinDao wdWinDao;
	@Override
	public BIBaseDistrict get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
	}

	@Override
	public void saveOrUpdate(BIBaseDistrict arg0) {
	}

	@Override
	public List<TreeJson> QueryTreeDistrict(String id, String provinceOnly) {
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		if(StringUtils.isNotBlank(provinceOnly)){
			if("0".equals(provinceOnly)){
				TreeJson topTreeJson = new TreeJson();
				topTreeJson.setId("1");
				topTreeJson.setText("全部");
				topTreeJson.setState("open");
				List<BIBaseDistrict> baseDistrictList = wdWinDao.findTreeLevelFirst();
				if(baseDistrictList!=null && baseDistrictList.size()>0){
					List<TreeJson> cTreeJsonList = new ArrayList<TreeJson>();
					Map<String,String> map = null;
					TreeJson treeJson = null;
					for(BIBaseDistrict bIBaseDistrict : baseDistrictList){
						treeJson = new TreeJson();
						map = new HashMap<String,String>();
						treeJson.setId(bIBaseDistrict.getCityCode());
						treeJson.setText(bIBaseDistrict.getCityName());
						treeJson.setState("open");
						treeJson.setAttributes(map);
						cTreeJsonList.add(treeJson);
					}
					topTreeJson.setChildren(cTreeJsonList);
				}
				treeJsonList.add(topTreeJson);
			}else{
				if(StringUtils.isBlank(id)){
					TreeJson topTreeJson = new TreeJson();
					topTreeJson.setId("1");
					topTreeJson.setText("全部");
					topTreeJson.setState("open");
					List<BIBaseDistrict> baseDistrictList = wdWinDao.findTreeLevelFirst();
					if(baseDistrictList!=null && baseDistrictList.size()>0){
						List<TreeJson> cTreeJsonList = new ArrayList<TreeJson>();
						Map<String,String> map = null;
						TreeJson treeJson = null;
						for(BIBaseDistrict bIBaseDistrict : baseDistrictList){
							treeJson = new TreeJson();
							map = new HashMap<String,String>();
							treeJson.setId(bIBaseDistrict.getCityCode());
							treeJson.setText(bIBaseDistrict.getCityName());
							treeJson.setState("closed");
							treeJson.setAttributes(map);
							cTreeJsonList.add(treeJson);
						}
						topTreeJson.setChildren(cTreeJsonList);
					}
					treeJsonList.add(topTreeJson);
				}else{
					List<BIBaseDistrict> baseDistrictList = wdWinDao.findTreeByParentId(id);
					if(baseDistrictList!=null && baseDistrictList.size()>0){
						TreeJson treeJson = null;
						for(BIBaseDistrict bIBaseDistrict : baseDistrictList){
							boolean isOpen = wdWinDao.isOpen(bIBaseDistrict.getCityCode());
							treeJson = new TreeJson();
							treeJson.setId(bIBaseDistrict.getCityCode());
							treeJson.setText(bIBaseDistrict.getCityName());
							treeJson.setState(isOpen?"closed":"open");
							treeJsonList.add(treeJson);
						}
					}
				}
			}
		}
		
		return treeJsonList;
	}

	@Override
	public List<TreeJson> QueryTreeOrg(String id,String deptTypes) {//List<BiBaseOrganization> sysDepartmentList = wdWinDao.findTreeOrg(deptTypes);
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		if(StringUtils.isBlank(id)){
			TreeJson topTreeJson = new TreeJson();
			topTreeJson.setId("1");
			topTreeJson.setText("全部");
			topTreeJson.setState("open");
			List<BiBaseOrganization> baseDistrictList = wdWinDao.findTreeOrg(deptTypes);
			if(baseDistrictList!=null && baseDistrictList.size()>0){
				List<TreeJson> cTreeJsonList = new ArrayList<TreeJson>();
				Map<String,String> map = null;
				TreeJson treeJson = null;
				for(BiBaseOrganization bIBaseDistrict : baseDistrictList){
					treeJson = new TreeJson();
					map = new HashMap<String,String>();
					/*treeJson.setId(bIBaseDistrict.getOrgKindCode());
					treeJson.setText(bIBaseDistrict.getOrgParentName());*/
					treeJson.setId(bIBaseDistrict.getOrgCode());
					treeJson.setText(bIBaseDistrict.getOrgName());
					treeJson.setState("closed");
					treeJson.setAttributes(map);
					cTreeJsonList.add(treeJson);
				}
				topTreeJson.setChildren(cTreeJsonList);
			}
			treeJsonList.add(topTreeJson);
		}else{
			List<BiBaseOrganization> baseDistrictList = wdWinDao.findTreeOrgByParentId(id);
			if(baseDistrictList!=null && baseDistrictList.size()>0){
				TreeJson treeJson = null;
				for(BiBaseOrganization bIBaseDistrict : baseDistrictList){
					treeJson = new TreeJson();
					treeJson.setId(bIBaseDistrict.getOrgCode());
					treeJson.setText(bIBaseDistrict.getOrgName());
					treeJson.setState("open");
					treeJsonList.add(treeJson);
				}
			}
		}
		return treeJsonList;
		
		
	}

	@Override
	public List<TreeJson> queryDeptEmpTree(String id, String deptTypes) {
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		if(StringUtils.isBlank(id)){
			TreeJson topTreeJson = new TreeJson();
			topTreeJson.setId("1");
			topTreeJson.setText("全部");
			topTreeJson.setState("open");
			List<BiBaseOrganization> baseDistrictList = wdWinDao.findTreeOrg(deptTypes);
			if(baseDistrictList!=null && baseDistrictList.size()>0){
				List<TreeJson> cTreeJsonList = new ArrayList<TreeJson>();
				Map<String,String> map = null;
				TreeJson treeJson = null;
				for(BiBaseOrganization bIBaseDistrict : baseDistrictList){
					treeJson = new TreeJson();
					map = new HashMap<String,String>();
					/*treeJson.setId(bIBaseDistrict.getOrgKindCode());
					treeJson.setText(bIBaseDistrict.getOrgParentName());*/
					treeJson.setId(bIBaseDistrict.getOrgCode());
					treeJson.setText(bIBaseDistrict.getOrgName());
					treeJson.setState("closed");
					treeJson.setAttributes(map);
					cTreeJsonList.add(treeJson);
				}
				topTreeJson.setChildren(cTreeJsonList);
			}
			treeJsonList.add(topTreeJson);
		}else{
			if(this.verify(id)){
				List<BiBaseOrganization> baseDistrictList = wdWinDao.findTreeOrgByParentId(id);
				if(baseDistrictList!=null && baseDistrictList.size()>0){
					TreeJson treeJson = null;
					for(BiBaseOrganization bIBaseDistrict : baseDistrictList){
						treeJson = new TreeJson();
						treeJson.setId(bIBaseDistrict.getOrgCode());
						treeJson.setText(bIBaseDistrict.getOrgName());
						treeJson.setState("closed");
						treeJsonList.add(treeJson);
					}
				}
			}else{
				List<BiBaseEmployee> baseEmplist = wdWinDao.queryEmpByDeptCode(id);
				if(baseEmplist!=null && baseEmplist.size()>0){
					TreeJson treeJson = null;
					for(BiBaseEmployee emp : baseEmplist){
						treeJson = new TreeJson();
						treeJson.setId(emp.getEmployeeNo());
						treeJson.setText(emp.getEmployeeName());
						treeJson.setState("open");
						treeJsonList.add(treeJson);
					}
				}
			}
		}
		return treeJsonList;
	}
	/**
	 * 判断传过来的科室code的类型   返回true为 科室分类   返回false 为具体的科室
	 * @param id
	 * @return
	 */
	private boolean verify(String id) {
		if(Integer.valueOf(id)<=13&&1<=Integer.valueOf(id)){
			return true;
		}
		return false;
	}

	
	@Override
	public List<RegisterGrade> queryDocLevelForBiPublic() {
		return wdWinDao.queryDocLevelForBiPublic();
	}
	
	@Override
	public List<BiBaseOrganization> findTreeOrg(String deptTypes) {
		return wdWinDao.findTreeOrg(deptTypes);
	}

	@Override
	public List<BiBaseOrganization> findTreeOrgByParentId(String orgCode) {
		 return wdWinDao.findTreeOrgByParentId(orgCode);
	}

	@Override
	public List<BiBaseEmployee> queryDocForBiPublic() {
		return wdWinDao.queryDocForBiPublic();
	}
	/**  
	 * @Description：  门诊科室
	 * @Author：zhangjin
	 * @CreateDate：2016-8-22 
	 * @remark：  
	 * @version 1.0
	 */
	@Override
	public List<BiBaseOrganization> treeBaseOrgOut() {
		return wdWinDao.treeBaseOrgOut();
	}
	/**  
	 * @Description：  挂号级别
	 * @Author：zhangjin
	 * @CreateDate：2016-8-22 
	 * @remark：  
	 * @version 1.0
	 */
	@Override
	public List<RegisterGrade> queryregcode() {
		return wdWinDao.queryregcode();
	}
}
