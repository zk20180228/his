package cn.honry.outpatient.transfuse.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.reflect.TypeToken;

import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.OutpatientMixLiquid;
import cn.honry.base.bean.model.OutpatientRecipedetail;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.outpatient.transfuse.dao.TransfuseDao;
import cn.honry.outpatient.transfuse.service.TransfuseService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
/**   
* @className：TransfuseServiceImpl
* @description：  门诊配液serviceImpl
* @author：tuchuanjiang
* @createDate：2016-06-21
* @version 1.0
 */
@Service("transfuseService")
@Transactional
@SuppressWarnings({ "all" })
public class TransfuseServiceImpl implements TransfuseService {
	@Autowired
	@Qualifier(value = "transfuseDao")
	private TransfuseDao transfuseDao;
	@Override
	public OutpatientMixLiquid get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(OutpatientMixLiquid arg0) {
		
	}
	/**
	 * 通过病历号查询患者的处方信息
	 * @author tuchuanjiang
	 * @CreateDate： 2016-06-21
	 * @version 1.0
	 */
	@Override
	public List<OutpatientRecipedetailNow> queryPatientYZInfo(String clinicCode) {
		return transfuseDao.queryPatientYZInfo(clinicCode);
	}
	/**
	 * 通过就诊卡号查询患者信息
	 * @author tuchuanjiang
	 * @CreateDate： 2016-06-21
	 * @version 1.0
	 */
	@Override
	public List<Patient> queryPatientInfo(String clinicCode) {
		return transfuseDao.queryPatientInfo(clinicCode);
	}

	@Override
	public List<SysEmployee> queryDoctrans() {
		return transfuseDao.queryDoctrans();
	}

	@Override
	public List<SysDepartment> queryDeptTrans() {
		return transfuseDao.queryDeptTrans();
	}

	@Override
	public List<BusinessFrequency> queryFrequencyTrans() {
		return transfuseDao.queryFrequencyTrans();
	}
	@Override
	public String saveform(String rowdata,String clinicCode) {
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		List<OutpatientMixLiquid> omlp =new ArrayList<OutpatientMixLiquid>();
		if(StringUtils.isNotEmpty(rowdata)&&!"[]".equals(rowdata)){
			try {
				rowdata=rowdata.replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
						.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
						.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
				omlp = JSONUtils.fromJson(rowdata,new TypeToken<List<OutpatientMixLiquid>>(){});
				for(int i=0;i<omlp.size();i++){
					Date date = new Date();
					omlp.get(i).setConfirmDate(date);
					omlp.get(i).setCreateUser(user.getAccount());//创建人
					omlp.get(i).setCreateDept(dept.getDeptCode());//创建科室
					omlp.get(i).setCreateTime(date);//创建时间
				}
				transfuseDao.saveOrUpdateList(omlp);
				return "true";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "error";
		
	}

	@Override
	public List<OutpatientMixLiquid> queryRecipedetail(String clinicCode) {
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		List<OutpatientMixLiquid> opml=new ArrayList<OutpatientMixLiquid>();
		List<OutpatientRecipedetailNow> oprl=transfuseDao.queryPatientYZInfo(clinicCode);
		if(oprl!=null&&oprl.size()>0){
			for(int i=0;i<oprl.size();i++){
				OutpatientMixLiquid opm=new OutpatientMixLiquid();
				opm.setItemName(oprl.get(i).getItemName());//项目名称
				opm.setDrugQuanlity(oprl.get(i).getDrugQuanlity());//药品性质
				opm.setFrequencyCode(oprl.get(i).getFrequencyCode());//频次
				opm.setSpecs(oprl.get(i).getSpecs());//规格
				opm.setInjectNumber(oprl.get(i).getInjectNumber());//院注次数
				opm.setExecDpcd(oprl.get(i).getExecDpcd());//执行科室
				opm.setHypotest(oprl.get(i).getHypotest());//皮试
				opm.setMoOrder(oprl.get(i).getDoctCode());//医嘱号
				opm.setSequenceNo(oprl.get(i).getSequencenNo());//项目流水号
				opm.setClinicCode(oprl.get(i).getClinicCode());//门诊号
				opm.setPatientNo(oprl.get(i).getPatientNo());//病历号
				opm.setRegDate(oprl.get(i).getRegDate());//挂号日期
				opm.setRegDept(oprl.get(i).getRegDept());//挂号科室
				opm.setItemCode(oprl.get(i).getItemCode());//项目代码
				opm.setClassCode(oprl.get(i).getClassCode());//系统类别
				opm.setFeeCode(oprl.get(i).getFeeCode());//最小费用代码
				opm.setUnitPrice(oprl.get(i).getUnitPrice());//单价
				opm.setQty(oprl.get(i).getQty());//开立数量
				opm.setDays(oprl.get(i).getDays());//付数
				opm.setPackQty(oprl.get(i).getPackQty());//包装数量
				opm.setItemUnit(oprl.get(i).getItemUnit());//计价单位
				opm.setOwnCost(oprl.get(i).getOwnCost());//自费金额
				opm.setPayCost(oprl.get(i).getPayCost());//自付金额
				opm.setPubCost(oprl.get(i).getPubCost());//报销金额
				opm.setBaseDose(oprl.get(i).getBaseDose());//基本剂量
				opm.setSelfMade(oprl.get(i).getSelfMade());//自制药
				opm.setOnceDose(oprl.get(i).getOnceDose());//每次用量（剂量）
				opm.setOnceUnit(oprl.get(i).getOnceUnit());//每次用量单位
				opm.setDoseModelCode(oprl.get(i).getDoseModelCode());//剂型代码
				opm.setUsageCode(oprl.get(i).getUsageCode());//用法
				opm.setMainDrug(oprl.get(i).getMainDrug());//主药标志
				opm.setCombNo(oprl.get(i).getCombNo());//组合号
				opm.setRemark(oprl.get(i).getRemark());//备注
				opm.setDoctCode(oprl.get(i).getDoctCode());//开立医生
				opm.setDoctDpcd(oprl.get(i).getDoctDpcd());//医生科室
				opm.setOperDate(oprl.get(i).getOperDate());//开立时间
				opm.setStatus(oprl.get(i).getStatus());//处方状态
				opm.setEmcFlag(oprl.get(i).getEmcFlag());//加急标记
				opm.setApplyNo(oprl.get(i).getApplyNo());//申请单号
				opm.setSubtblFlag(oprl.get(i).getSubtblFlag());//附材
				opm.setNeedConfirm(oprl.get(i).getNeedConfirm());//是否需要确认
				opm.setChargeCode(oprl.get(i).getChargeCode());//收费员
				opm.setChargeDate(oprl.get(i).getChargeDate());//收费时间
				opm.setRecipeNo(oprl.get(i).getRecipeNo());//处方号
				opm.setRecipeSeq(oprl.get(i).getRecipeSeq());//处方内流水号
				opm.setPhamarcyCode(oprl.get(i).getPhamarcyCode());//发药药房
				opm.setMinunitFlag(oprl.get(i).getMinunitFlag());//开立单位(1-包装单位 0-最小单位)
				opm.setDataorder(oprl.get(i).getDataorder());//排列序号，按排列序号由大到小顺序显示医嘱
				opm.setConfirmDept(dept.getId());//配液科室
				opml.add(opm);
			}
			return opml;
		}
		return new ArrayList<OutpatientMixLiquid>();
	}

	@Override
	public List<OutpatientMixLiquid> queryMixliquid(String clinicCode) {
		
		return transfuseDao.queryMixliquid(clinicCode);
	}

	@Override
	public List<User> queryUsertrans() {
		return transfuseDao.queryUsertrans();
	}

	@Override
	public List<SysEmployee> queryEmptrans() {
		return transfuseDao.queryEmptrans();
	}



}
