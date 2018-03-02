package cn.honry.inpatient.prepayin.dao;

import java.util.List;

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
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.prepayin.vo.PrepayinVo;



@SuppressWarnings({"all"})
public interface InpatientPrepayinDAO extends EntityDao<InpatientPrepayin>{
	
	/**
	 * 获取记录条数
	 * @param entity 查询条件封装实体类
	 * @author  liudelin
	 * @date 2015-08-11
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	int getTotal(InpatientPrepayin entity);
	
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
	List query(String page, String rows, InpatientPrepayin entity);
	
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
	InpatientInfoNow queryPrepayinInpatient(String medicalrecordId);
	
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
	VIdcardPatient queryVIdcardPatient(String medicalrecordId);
	
	/**  
	 *  
	 * @Description：  保存
	 * @Author：liudelin
	 * @ModifyDate：2015-8-12 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	void savePrepayin(InpatientPrepayin entity);
	
	/**  
	 *  
	 * @Description：  下拉诊断
	 * @Author：liudelin
	 * @ModifyDate：2015-8-12 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	List<BusinessIcd> queryPrepayinIcd();
	
	/**  
	 *  
	 * @Description：  下拉病床
	 * @Author：liudelin
	 * @ModifyDate：2015-8-12 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	List<BusinessHospitalbed> queryPrepayinBed();
	
	/**  
	 *  
	 * @Description：  下拉医生
	 * @Author：liudelin
	 * @ModifyDate：2015-8-12 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	List<SysEmployee> queryPrepayinPredoct();
	
	/**  
	 *  
	 * @Description：  查询病人及登记信息
	 * @Author：tangfeishuai
	 * @ModifyDate：2016-6-20 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	List<PrepayinVo> queryPrepayinVo(String mId,String no);
	
	/**  
	 *  
	 * @Description：  根据病区id查询床号
	 * @Author：tangfeishuai
	 * @ModifyDate：2016-6-20 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	List<BusinessHospitalbed> queryBedByNurse(String nurId);
	
	/**  
	 *  
	 * @Description：  合同单位渲染
	 * @Author：tangfeishuai
	 * @ModifyDate：2016-6-20 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	List<BusinessContractunit> queryContractunit();
	
	/**  
	 *  
	 * @Description：  就诊卡号渲染
	 * @Author：tangfeishuai
	 * @ModifyDate：2016-6-20 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	List<PatientIdcard> queryIdCard();
	
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
	InpatientInfoNow queryInpatientInfo(String mId);
	
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
	InpatientProof queryInpatientProof(String mId,String no);
	
	/**  
	 *  
	 * @Description：渲染城市
	 * @Author：tangfeishuai
	 * @CreateDate：2016-6-21 下午16:29:35 
	 * @param：
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<District> queryDistinct();
	
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
	List<SysEmployee> queryInpatientBedinfo(String bedId);
	
	/**  
	 *  
	 * @Description：查询患者是否已经住院预约
	 * @Author：tangfeishuai
	 * @CreateDate：2016-6-24下午16:29:35 
	 * @param：
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	InpatientPrepayin inpPrepayin(String mId);
	
	/**
	 * 
	 * 
	 * <p>根据部门ID查询部门CODE和NAME </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 上午11:00:27 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 上午11:00:27 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param dept 部门ID
	 * @return:
	 *
	 */
	List<DepartmentContact> queryNurInfo(String dept);
	
	/**
	 * 
	 * 
	 * <p>根据病历号查询住院证明表 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 上午11:02:51 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 上午11:02:51 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param medId
	 * @return:
	 *
	 */
	List<InpatientProof> queryProofList(String medId);
	
	/**
	 * 
	 * 
	 * <p>根据医院病床ID查询病房号  根据病房编号查询病床号 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 上午11:04:32 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 上午11:04:32 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param bedId 医院病床ID
	 * @return:
	 *
	 */
	List<BusinessBedward> queryBedWardInfo(String bedId);
	
	/**
	 * 
	 * 
	 * <p>根据病历号和就诊卡号查询住院证明</p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 上午11:08:30 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 上午11:08:30 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param medicalrecordId 病历号
	 * @param medId 就诊卡号
	 * @return:
	 *
	 */
	InpatientProof getProof(String medicalrecordId, String medId);
	
	/**
	 * 
	 * 
	 * <p>根据ID更新住院预约表</p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 上午11:17:10 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 上午11:17:10 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param i 住院预约表ID
	 * @return:
	 *
	 */
	int updatePrepayin(InpatientPrepayin i);
	
}
