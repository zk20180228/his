package cn.honry.inpatient.inpatientNumber.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.InpatientNumber;
import cn.honry.inpatient.inpatientNumber.dao.InpatientNumberDao;
import cn.honry.inpatient.inpatientNumber.service.InpatientNumberService;

@Service("inpatientNumberService")
@Transactional
@SuppressWarnings({"all"})
public class InpatientNumberServiceImpl implements InpatientNumberService {
	
	@Autowired
	private InpatientNumberDao inpatientNumberDao;
	

	@Override
	public InpatientNumber get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(InpatientNumber arg0) {
		
	}
	/**  
	 * @Description：  初始化
	 * @Author：zhangjin
	 * @CreateDate：2016-11-14
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	@Override
	public String getInpatientNumber() {
		return inpatientNumberDao.getInpatientNumber();
	}
	/**  
	 * @Description：  加载数据
	 * @Author：zhangjin
	 * @CreateDate：2016-11-14
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0getNumberList
	 */
	@Override
	public List<InpatientNumber> getNumberList(String medicalrecordId,
			String beganTime, String endTime,String page,String rows) {
		return inpatientNumberDao.getNumberList(medicalrecordId,
				beganTime,endTime,page,rows);
	}

	/**  
	 * @Description：  加载数据总条数
	 * @Author：zhangjin
	 * @CreateDate：2016-11-14
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0getNumberList
	 */
	@Override
	public int getNumberTotal(String medicalrecordId, String beganTime,
			String endTime, String page, String rows) {
		return inpatientNumberDao.getNumberTotal(medicalrecordId,
				beganTime,endTime,page,rows);
	}

	/**  
	 * @Description：获取床号
	 * @Author：zhangjin
	 * @CreateDate：2016-11-18
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	@Override
	public List<BusinessHospitalbed> getBedinfoId() {
		return inpatientNumberDao.getBedinfoId();
	}

}
