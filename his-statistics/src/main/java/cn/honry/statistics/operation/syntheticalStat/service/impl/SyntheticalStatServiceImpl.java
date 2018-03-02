package cn.honry.statistics.operation.syntheticalStat.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.TreeJson;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.doctor.regisdocscheinfo.vo.RegisDocScheInfoVo;
import cn.honry.statistics.operation.syntheticalStat.dao.SyntheticalStatDao;
import cn.honry.statistics.operation.syntheticalStat.service.SyntheticalStatService;
import cn.honry.statistics.operation.syntheticalStat.vo.InvoiceInfoVo;
import cn.honry.statistics.operation.syntheticalStat.vo.MedicalInfoVo;
import cn.honry.statistics.operation.syntheticalStat.vo.PatientInfoVo;
import cn.honry.statistics.operation.syntheticalStat.vo.RegisterInfoVo;
import cn.honry.statistics.operation.syntheticalStat.vo.TreeInfoVo;

/**  
 *  
 * @className：SyntheticalStatServiceImpl
 * @Description： 门诊综合查询
 * @Author：aizhonghua
 * @CreateDate：2016-6-23 下午04:41:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-6-23 下午04:41:31 
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("syntheticalStatService")
@Transactional
@SuppressWarnings({ "all" })
public class SyntheticalStatServiceImpl implements SyntheticalStatService{

	@Autowired
	@Qualifier(value = "syntheticalStatDAO")
	private SyntheticalStatDao syntheticalStatDAO;
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;

	/**  
	 *  
	 * 查询患者信息 - 获得信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String,Object> getRegisterInfo(String page, String rows,String startTime, String endTime, String type, String para, String vague) {

		Date sTime = DateUtils.parseDateY_M_D(startTime);
		Date eTime = DateUtils.parseDateY_M_D(endTime);
		
		//2.获取门诊数据保留时间
		String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
		
		//3.获得当前时间
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dTime;
		try {
			dTime = df.parse(df.format(new Date()));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
		//4.获得在线库数据应保留最小时间
		Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
		
		List<String> tnL = new ArrayList<String>();
		
		//判断查询类型
		if(DateUtils.compareDate(sTime, cTime)==-1){
			if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
				
				//获取需要查询的全部分区
				tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",startTime,endTime);
			}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
				
				//获得时间差(年)
				int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
				
				//获取相差年分的分区集合，默认加1
				tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",yNum+1);
				tnL.add(0,"T_REGISTER_MAIN_NOW");
			}
		}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
			tnL.add("T_REGISTER_MAIN_NOW");
		}
		
		Map<String,Object> map = syntheticalStatDAO.getRegisterInfo(page,rows,startTime,endTime,type,para,vague, tnL);
		
		return map;
	}

	/**  
	 *  
	 * 查询患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public PatientInfoVo queryPatientInfo(String patientId) {
		
		return syntheticalStatDAO.queryPatientInfo(patientId);
	}
	
	/**  
	 *  
	 * 查询患者发票信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<InvoiceInfoVo> queryInvoiceInfo(String registerNo,String tab) {
		
		return syntheticalStatDAO.queryInvoiceInfo(registerNo,tab);
	}

	/**  
	 *  
	 * 查询患者历史医嘱树
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<TreeJson> queryMedicalTree(String recordNo) {
		List<TreeJson> treeList = new ArrayList<TreeJson>();
		TreeJson pTree = new TreeJson();
		pTree.setId("1");
		pTree.setText("历史医嘱信息");
		pTree.setState("open");
		pTree.setIconCls("icon-table");
		Map<String, String> pMap = new HashMap<String, String>();
		pMap.put("type","0");
		pTree.setAttributes(pMap);
		if(StringUtils.isNotBlank(recordNo)){
			List<TreeInfoVo> viList = syntheticalStatDAO.queryMedicalTree(recordNo);
			if(viList!=null && viList.size()>0){
				List<TreeJson> cTreeList = new ArrayList<TreeJson>();
				TreeJson cTree = null;
				for(TreeInfoVo vo : viList){
					cTree = new TreeJson();
					cTree.setId(vo.getClinicNo());
					if(StringUtils.isBlank(vo.getDoc())){
						vo.setDoc("");
					}
					if(StringUtils.isBlank(vo.getDept())){
						vo.setDept("");
					}
					if(StringUtils.isBlank(vo.getClinicNo())){
						vo.setClinicNo("");
					}
					cTree.setText("["+vo.getClinicNo()+"]["+vo.getDept()+"]["+vo.getDoc()+"]");
					cTree.setState("open");
					Map<String, String> cMap = new HashMap<String, String>();
					cMap.put("type","1");
					cMap.put("tab",vo.getTab());
					cTree.setAttributes(cMap);
					cTreeList.add(cTree);
				}
				pTree.setChildren(cTreeList);
				
			}
		}
		treeList.add(pTree);
		
		return treeList;
	}

	
	/**  
	 *  
	 * 查询患者历史医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<MedicalInfoVo> queryMedicalInfo(String registerNo,String tab) {
		
		return syntheticalStatDAO.queryMedicalInfo(registerNo,tab);
	}

}
