package cn.honry.inpatient.prepayin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.BusinessIcd;
import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.District;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientPrepayin;
import cn.honry.base.bean.model.InpatientProof;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.VIdcardPatient;
import cn.honry.inpatient.prepayin.dao.InpatientPrepayinDAO;
import cn.honry.inpatient.prepayin.service.InpatientPrepayinService;
import cn.honry.inpatient.prepayin.vo.PrepayinVo;

@Service("inpatientPrepayinService")
@Transactional
@SuppressWarnings({"all"})
public class InpatientPrepayinServiceImpl implements InpatientPrepayinService{

	@Autowired
	@Qualifier(value = "inpatientPrepayinqDAO")
	private InpatientPrepayinDAO inpatientPrepayinqDAO;
	
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public InpatientPrepayin get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(InpatientPrepayin entity) {
		
	}
	/**
	 * 获取记录条数
	 * @param entity 查询条件封装实体类
	 * @author  liudelin
	 * @date 2015-08-11
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getTotal(InpatientPrepayin entity) {
		int total = inpatientPrepayinqDAO.getTotal(entity);
		return total;
	}
	/**
	 * 列表查询
	 * @param page 页码
	 * @param rows 显示列表数据
	 * @param entity 查询条件封装实体类
	 * @author  liudelin
	 * @date 2015-07-28
	 * @version 1.0
	 * @param string2 
	 * @param string 
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<InpatientPrepayin> prepayinList(String page, String rows,InpatientPrepayin entity) throws Exception {
		List lst =  inpatientPrepayinqDAO.query(page,rows,entity);
		return lst;
	}
	/**
	 * 取消预约
	 * @author  ldl
	 * @date 2015-8-11 14:00
	 * @version 1.0
	 */
	@Override
	public void del(String ids) throws Exception{
		String idStr[] = ids.split(",");
		for (String id : idStr) {
			InpatientPrepayin i = inpatientPrepayinqDAO.get(id);
			inpatientPrepayinqDAO.updatePrepayin(i);
		}
	}
	/**  
	 *  
	 * @Description：查询并赋值
	 * @Author：ldl
	 * @CreateDate：2015-8-11 下午16:29:35 
	 * @param：medicalrecordId（病例号） 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public InpatientInfoNow queryPrepayinInpatient(String medicalrecordId) throws Exception {
		InpatientInfoNow lst = inpatientPrepayinqDAO.queryPrepayinInpatient(medicalrecordId);
		return lst;
	}
	/**  
	 *  
	 * @Description：查询患者信息
	 * @Author：ldl
	 * @CreateDate：2015-8-11 下午16:29:35 
	 * @param：medicalrecordId（病例号） 
	 * @ModifyRmk：  当查询住院表中没有数据时根据病历号查询 就诊卡表----患者信息表视图
	 * @version 1.0
	 *
	 */
	@Override
	public VIdcardPatient queryVIdcardPatient(String medicalrecordId) throws Exception  {
		VIdcardPatient lst = inpatientPrepayinqDAO.queryVIdcardPatient(medicalrecordId);
		return lst;
	}
	/**  
	 *  
	 * @Description：  保存
	 * @Author：liudelin
	 * @ModifyDate：2015-8-12 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public void savePrepayin(InpatientPrepayin entity) throws Exception  {
		inpatientPrepayinqDAO.savePrepayin(entity);
	}
	/**  
	 *  
	 * @Description：  下拉诊断
	 * @Author：liudelin
	 * @ModifyDate：2015-8-12 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessIcd> queryPrepayinIcd() throws Exception  {
		List<BusinessIcd> lst = inpatientPrepayinqDAO.queryPrepayinIcd();
		return lst;
	}
	/**  
	 *  
	 * @Description：  下拉病床
	 * @Author：liudelin
	 * @ModifyDate：2015-8-12 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessHospitalbed> queryPrepayinBed() throws Exception  {
		List<BusinessHospitalbed> lst = inpatientPrepayinqDAO.queryPrepayinBed();
		return lst;
	}
	/**  
	 *  
	 * @Description：  下拉医生
	 * @Author：liudelin
	 * @ModifyDate：2015-8-12 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysEmployee> queryPrepayinPredoct() throws Exception  {
		List<SysEmployee> lst = inpatientPrepayinqDAO.queryPrepayinPredoct();
		return lst;
	}

	/**  
	 *  
	 * @Description：  查询病人及登记信息
	 * @Author：tangfeishuai
	 * @ModifyDate：2016-6-20 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public PrepayinVo queryPrepayVo(String mId,String no) throws Exception  {
		return inpatientPrepayinqDAO.queryPrepayinVo(mId,no).get(0);
	}
	@Override
	public List<PrepayinVo> queryPrepayVoList(String mId,String no) throws Exception  {
		return inpatientPrepayinqDAO.queryPrepayinVo(mId,no);
	}

	
	/**  
	 *  
	 * @Description：  根据病区id查询床号
	 * @Author：tangfeishuai
	 * @ModifyDate：2016-6-20 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessHospitalbed> queryBedByNurse(String nurId) throws Exception  {
		return inpatientPrepayinqDAO.queryBedByNurse(nurId);
	}

	/**  
	 *  
	 * @Description：  合同单位渲染
	 * @Author：tangfeishuai
	 * @ModifyDate：2016-6-20 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, String> queryContractunit() throws Exception  {
		List<BusinessContractunit> list = inpatientPrepayinqDAO.queryContractunit();
		HashMap<String , String> map =new HashMap<String,String>();
		for (BusinessContractunit b : list) {
			map.put(b.getEncode(), b.getName());
		}
		return map;
 	}

	/**  
	 *  
	 * @Description：  就诊卡号渲染
	 * @Author：tangfeishuai
	 * @ModifyDate：2016-6-20 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, String> queryIdCard() throws Exception  {
		List<PatientIdcard> list = inpatientPrepayinqDAO.queryIdCard();
		HashMap<String , String> map =new HashMap<String,String>();
		for (PatientIdcard b : list) {
			map.put(b.getId(), b.getIdcardNo());
		}
		return map;
	}
	/**  
	 *  
	 * @Description：查询住院主表是否有该患者
	 * @Author：tangfeishuai
	 * @CreateDate：2016-6-21 下午16:29:35 
	 * @param：mId（病例号） 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public InpatientInfoNow queryInpatientInfo(String mId) throws Exception  {
		return inpatientPrepayinqDAO.queryInpatientInfo(mId);
	}

	/**  
	 *  
	 * @Description： 城市渲染
	 * @Author：tangfeishuai
	 * @ModifyDate：2016-6-20 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, String> queryDistinct() throws Exception  {
		List<District> list = inpatientPrepayinqDAO.queryDistinct();
		HashMap<String , String> map =new HashMap<String,String>();
		for (District b : list) {
			map.put(b.getCityCode(), b.getCityName());
		}
		return map;
	}

	/**  
	 *  
	 * @Description：查询开立住院证表是否有该患者
	 * @Author：tangfeishuai
	 * @CreateDate：2016-6-21 下午16:29:35 
	 * @param：mId（病例号） 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public InpatientProof queryInpatientProof(String mId,String no) throws Exception  {
		return  inpatientPrepayinqDAO.queryInpatientProof(mId,no);
	}
	
	/**  
	 *  
	 * @Description：根据病床id查询维护该病床的医生
	 * @Author：tangfeishuai
	 * @CreateDate：2016-6-24下午16:29:35 
	 * @param：
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysEmployee> queryInpatientBedinfo(String bedId) throws Exception  {
		return inpatientPrepayinqDAO.queryInpatientBedinfo(bedId);
	}
	/**  
	 *  
	 * @Description：查询患者是否已经住院预约
	 * @Author：tangfeishuai
	 * @CreateDate：2016-7-11下午16:29:35 
	 * @param：
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public InpatientPrepayin inpPrepayin(String mId) throws Exception  {
		return inpatientPrepayinqDAO.inpPrepayin(mId);
	}

	@Override
	public List<DepartmentContact> queryNurInfo(String dept) throws Exception  {
		return inpatientPrepayinqDAO.queryNurInfo(dept);
	}

	@Override
	public List<InpatientProof> queryProofList(String medId) throws Exception  {
		return inpatientPrepayinqDAO.queryProofList(medId);
	}

	@Override
	public List<BusinessBedward> queryBedWardInfo(String bedId) throws Exception  {
		return inpatientPrepayinqDAO.queryBedWardInfo(bedId);
	}

	@Override
	public InpatientProof getProof(String medicalrecordId, String medId) throws Exception  {
		return inpatientPrepayinqDAO.getProof(medicalrecordId,medId);
	}

	
}
