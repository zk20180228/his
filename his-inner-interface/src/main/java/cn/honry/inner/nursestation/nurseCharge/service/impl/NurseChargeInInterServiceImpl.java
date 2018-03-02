package cn.honry.inner.nursestation.nurseCharge.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugStockinfo;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientMedicineList;
import cn.honry.base.bean.model.MatUndrugCompare;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.inpatient.info.dao.InpatientInfoInInterDAO;
import cn.honry.inner.nursestation.nurseCharge.dao.NurseChargeInInterDAO;
import cn.honry.inner.nursestation.nurseCharge.service.NurseChargeInInterService;
import cn.honry.inner.nursestation.nurseCharge.vo.NurseChargeVo;
import cn.honry.utils.TreeJson;

/**
 * 护士站收费ServiceImpl
 *
 */
@Service("nurseChargeInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class NurseChargeInInterServiceImpl implements NurseChargeInInterService {
	
	
	/**
	 * 住院主表DAO
	 */
	@Autowired
	@Qualifier(value = "inpatientInfoInInterDAO")
	private InpatientInfoInInterDAO inpatientInfoDAO;
	
	/**
	 * 科室编码表
	 */
	@Autowired
	@Qualifier(value = "deptInInterDAO")
	private DeptInInterDAO departmentDAO;
	
	/**
	 * 住院收费DAO
	 */
	@Autowired
	@Qualifier(value = "nurseChargeInInterDAO")
	private NurseChargeInInterDAO nurseChargeInInterDAO;
	

	@Override
	public void removeUnused(String id) {
	}

	@Override
	public DrugUndrug get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(DrugUndrug drugUndrug) {

	}

	/**
	 * 
	 * @Description：渲染开方医生（根据床号）
	 * @Author：zhangjin
	 * @CreateDate：2016-1-6
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Override
	public NurseChargeVo querNurseDoctor(String id) {
		return nurseChargeInInterDAO.querNurseDoctor(id);
	}

	/**  
	 * 
	 * 获取最小费用代码树
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:id 最小费用cost
	 * @param:soushu 判断是否为手术
	 * @throws:
	 * @return: List<TreeJson>
	 *
	 */
	@Override
	public List<TreeJson> treeNurseAdvice(String id,String soushu) {
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		if (StringUtils.isBlank(id)) {
			//获取最小费用代码
			List<NurseChargeVo> listDept = nurseChargeInInterDAO.treeNurseAdvice(soushu);
			if (listDept != null) {
				TreeJson treeJson = null;
				TreeJson treeJsonp = null;
				Map<String, String> attributesChp = null;
				for (NurseChargeVo vo : listDept) {
					treeJson = new TreeJson();
					treeJson.setId(vo.getFeeStatCode());
					treeJson.setText(vo.getFeeStatName());
					treeJson.setState("closed");
					treeJsonList.add(treeJson);
					//获取最小费用与统计大类子节点
					List<NurseChargeVo> listcDept = nurseChargeInInterDAO
							.treeNurseAdvicep(vo.getFeeStatCode(),soushu);
					List<TreeJson> treeJsonListp = new ArrayList<TreeJson>();
					for (NurseChargeVo vop : listcDept) {
						treeJsonp = new TreeJson();
						treeJsonp.setId(vop.getMinfeeCode());
						if (vop.getFeename() == "" || vop.getFeename() == null) {
							treeJsonp.setText("未知");
						}
						treeJsonp.setText(vop.getFeename());
						treeJsonp.setState("closed");
						treeJsonListp.add(treeJsonp);
					}
					treeJson.setChildren(treeJsonListp);
				}
			}
		} else {
			    //获取最小费用与统计大类信息
				List<NurseChargeVo> listDept = nurseChargeInInterDAO.chargeMinfeetostat(id);
				TreeJson treeJson = null;
				Map<String, String> attributes = null;
				for (NurseChargeVo vo : listDept) {
					treeJson = new TreeJson();
					attributes = new HashMap<String, String>();
					treeJson.setId(vo.getUndrugId());
					treeJson.setText(vo.getUndrugName());
					  attributes.put("pid", id);
					  attributes.put("undrugName",
							  vo.getUndrugName());//项目名称 
					  attributes.put("money",
					          vo.getMoney().toString());//单价
					  attributes.put("unit", 
							  vo.getUnit());//单位
					  attributes.put("dept", 
							  vo.getDept());//执行科室
					  attributes.put("category",
							  vo.getCategory());//类型（非药品1，和中草药）
					  if(vo.getUndrugChildrenprice() != null){
						      attributes.put("undrugChildrenprice",
							             vo.getUndrugChildrenprice().toString());//儿童价
					  }
					  if(vo.getUndrugSpecialprice() != null){
					          attributes.put("undrugSpecialprice",
							             vo.getUndrugSpecialprice().toString());//特诊价
					  }
					  attributes.put("undrugMinimumcost",
							  vo.getUndrugMinimumcost());//最小代码
					  attributes.put("spec", 
							  vo.getSpec());//规格
					  attributes.put("itemCode", 
							  vo.getItemCode());//项目编码
					  attributes.put("nid", 
							  vo.getNid());//项目编码
					  attributes.put("ty", 
							  vo.getTy());//是否非药品
					treeJson.setAttributes(attributes);
					treeJsonList.add(treeJson);
				}
		}
		return treeJsonList;
	}

	/**
	 * 
	 * @Description：组套信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-11
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Override
	public List<TreeJson> treeSonNurseAdvice(String deptId, String id) {
		List<NurseChargeVo> listDept = new ArrayList<NurseChargeVo>();
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		TreeJson treeJson = null;
		TreeJson treeJsonp = null;
		TreeJson pTreeJson = null;
		if (StringUtils.isBlank(id)) {// 根节点
			// 加入科室诊室间关系树的根节点
			pTreeJson = new TreeJson();
			pTreeJson.setId("1");
			pTreeJson.setText("科室组套");
			treeJsonList.add(pTreeJson);
		}
		listDept = nurseChargeInInterDAO.treeStackNurseAdvicep(deptId);
		if (listDept != null && listDept.size() > 0) {
			for (NurseChargeVo sysDept : listDept) {
				treeJson = new TreeJson();
				treeJson.setId(sysDept.getStackId());
				treeJson.setText(sysDept.getStackName());
				Map<String, String> attributes = new HashMap<String, String>();
				attributes.put("pid", "1");
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
			List<NurseChargeVo> listcDept = nurseChargeInInterDAO
					.treeStackSonNurseAdvice(deptId);
			if (listcDept != null && listcDept.size() > 0) {
				for (NurseChargeVo sysDeptp : listcDept) {
					treeJsonp = new TreeJson();
					treeJsonp.setId(sysDeptp.getNid());
					treeJsonp.setText(sysDeptp.getName());
					Map<String, String> attributes = new HashMap<String, String>();
					attributes.put("pid", sysDeptp.getStackId());
					 treeJsonp.setAttributes(attributes);
					treeJsonList.add(treeJsonp);
				}
			}
		}
		return TreeJson.formatTree(treeJsonList);

	}


	/**
	 * 
	 * @Description：回显组套信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-9
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> businessStack(String naId, String deptCode) {

		return nurseChargeInInterDAO.businessStack(naId, deptCode);
	}

	/**
	 * 
	 * @Description：最小费用与统计大类信息下的非药品信息v
	 * @Author：zhangjin
	 * @CreateDate：2016-1-12
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> treeNurseAdviceUndrug(String naId) {
		return nurseChargeInInterDAO.treeNurseAdviceUndrug(naId);
	}

	/**
	 * 
	 * @Description：项目名称（下拉框（datagrid））总数
	 * @Author：zhangjin
	 * @CreateDate：2016-1-13
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Override
	public int getTotal(String q) {
		return nurseChargeInInterDAO.getTotal(q);
	}
	
	/**
	 * 
	 * @Description：项目名称（下拉框（datagrid））分页
	 * @Author：zhangjin
	 * @CreateDate：2016-1-13
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> getPage(String parameter, String parameter2,String q) {
		return nurseChargeInInterDAO.getPageSql(parameter, parameter2,q);
	}

	/**
	 * 
	 * @Description：渲染单价
	 * @Author：zhangjin
	 * @CreateDate：2016-1-13
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> nurseChargeMoney() {
		return nurseChargeInInterDAO.nurseChargeMoney();
	}

	/**
	 * 
	 * @Description：渲染合同
	 * @Author：zhangjin
	 * @CreateDate：2016-1-13
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> nurseChargePactCode() {

		return nurseChargeInInterDAO.nurseChargePactCode();
	}

	

	/**
	 * 
	 * @Description：加载患者药品和非药品明细细信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-18
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> InpatientMessage(String no) {
		return nurseChargeInInterDAO.InpatientMessage(no);
	}


	/**
	 * 
	 * @Description：获取价格形式
	 * @Author：zhangjin
	 * @CreateDate：2016-1-19
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> querNursePubRati(String id) {
		return nurseChargeInInterDAO.querNursePubRati(id);
	}

	/**
	 * 
	 * @Description：加载婴儿是否绑定母亲
	 * @Author：zhangjin
	 * @CreateDate：2016-1-20
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> hospitalParameter() {
		return nurseChargeInInterDAO.hospitalParameter();
	}

	/**
	 * 
	 * @Description：加载住院金额
	 * @Author：zhangjin
	 * @CreateDate：2016-1-20
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> nurseInpatBalance(String id) {
		return nurseChargeInInterDAO.nurseInpatBalance(id);
	}

	
	/**  
	 *  
	 * @Description：查询本病区下的医生
	 * @Author：zhangjin
	 * @CreateDate：2016-3-25
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysEmployee> queryEmplCode() {
		return nurseChargeInInterDAO.queryEmplCode();
	}
	/**  
	 *  
	 * @Description：查询当日的收费信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-22
	 * @Modifier：zhangjin
	 * @ModifyDate：2016-3-22 
	 *  @Modifier：zhangjin
	 * @ModifyDate：2016-3-25  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<NurseChargeVo> dayCharge(String inpatientNo) {
		return nurseChargeInInterDAO.dayCharge(inpatientNo);
	}
	@Override
	public List<InpatientInfo> queryInpatientInfoList(String inpatientNo) {
		return nurseChargeInInterDAO.queryInpatientInfoList(inpatientNo);
	}
	/**  
	 *  
	 * @Description：渲染科室
	 * @Author：zhangjin
	 * @CreateDate：2016-3-30
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> querydeptComboboxs() {
		return nurseChargeInInterDAO.querydeptComboboxs();
	}

	/**
	 * 查询非药品项目名称
	 * @author   zhangjin
	 * @createDate： 
	 * @modifyDate：2016-4-13
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<DrugUndrug> gainUndrug(String id) {
		return nurseChargeInInterDAO.gainUndrug(id);
	}

	/**
	 * 查询计费人
	 * @author   zhangjin
	 * @createDate： 
	 * @modifyDate：2016-4-13
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<User> nurseEmply() {
		return nurseChargeInInterDAO.nurseEmply();
	}

	/**
	 * 查询是否绑定物资
	 * @author   zhangjin
	 * @createDate： 
	 * @modifyDate：2016-4-13
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public MatUndrugCompare queryWz(String nid) {
		return nurseChargeInInterDAO.queryWz(nid);
	}

	/**
	 * 查询是否物资
	 * @author   zhangjin
	 * @createDate： 
	 * @modifyDate：2016-4-13
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public InpatientItemList getSequenceNo(String nid, String recipeNo) {
		return nurseChargeInInterDAO.getSequenceNo(nid,recipeNo);
	}

	/**
	 * 获取住院科室对应药房的id和库存数量
	 * @author   zhangjin
	 * @createDate： 
	 * @modifyDate：2016-5-18
	 * @param：   nid(项目ID)
	 * @param2：   deptCode(住院科室)
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public DrugStockinfo gekydept(String deptCode,String nid) {
		return nurseChargeInInterDAO.getkydept(deptCode,nid);
	}

	/**
	 * 对成功收费的药品进行查询（扣库存）
	 * @author   zhangjin
	 * @createDate： 
	 * @modifyDate：2016-6-3
	 * @param：   nid(项目ID)
	 * @param2：   deptCode(住院科室)
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public InpatientMedicineList getDrugSequenceNo(String nid, String string) {
		return nurseChargeInInterDAO.getDrugSequenceNo( nid, string);
	}

	@Override
	public void del(String id, String zsd) {
		
	}

	@Override
	public List<DrugInfo> queryDrugInfo(String itemCode) {
		return inpatientInfoDAO.queryDrugInfo(itemCode);
	}
}
