package cn.honry.inner.baseinfo.clinic.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.Clinic;
import cn.honry.inner.baseinfo.clinic.dao.ClinicInInterDAO;
import cn.honry.inner.baseinfo.clinic.service.ClinicInInterService;

/**  
 *  
 * 接口：诊室
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("clinicInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class ClinicInInterServiceImpl implements ClinicInInterService{

	@Autowired
	@Qualifier(value = "clinicInInterDAO")
	private ClinicInInterDAO clinicInInterDAO;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public Clinic get(String id) {
		return clinicInInterDAO.get(id);
	}

	@Override
	public void saveOrUpdate(Clinic entity) {
		
	}
	
	/**  
	 *  
	 * 接口：诊室
	 * @Author：aizhonghua
	 * @Author：aizhonghua
	 * @CreateDate：2016-2-24 上午11:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-2-24 上午11:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, String> queryClinicIdAndNameMap() {
		Map<String,String> map = new HashMap<String, String>();
		List<Clinic> list = clinicInInterDAO.getAll();
		if(list!=null&&list.size()>0){
			for(Clinic cli : list){
				map.put(cli.getId(), cli.getClinicName());
			}
		}
		return map;
	}

}
