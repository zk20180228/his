package cn.honry.inner.baseinfo.hospital.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import oracle.net.ano.SupervisorService;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.Hospital;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.hospital.dao.HospitalInInterDAO;


/**
 * @Description  医院信息DAO实现层 
 * @author    tangfeishuai
 * @version   1.0 
 * @CreateDate 2016-3-28
 * @Modifier：tangfeishuai
 * @ModifyDate：2016-4-13上午12:02:16  
 * @ModifyRmk：
 */
@Repository("hospitalInInterDAO")
@SuppressWarnings({ "all" })
public class HospitalInInterDAOImpl extends HibernateEntityDao<Hospital> implements HospitalInInterDAO {
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**
	 * @Description:根据id获取Hospital集合
	 * @Author： tangfeishuai
	 * @CreateDate： 2016-3-30
	 * @ModifyDate：2016-3-30
	 * @ModifyRmk：  
	 * @param  String param
	 * @return List<Hospital>  
	 * @version 1.0
	 */
	@Override
	public List<Hospital> getHospLikeName(String param) {
		String hql = "from Hospital h where h.stop_flg=0 and h.del_flg=0 and (h.name like '%"+param+"%' or h.brev like '%"+param+"%' "
				+ "or h.district like '%"+param+"%' or h.description like '%"+param+"%')";
		List<Hospital> list = this.createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<Hospital>();
	}
	/**
	 * 根据id获取医院
	 * @Description 
	 * @author  marongbin
	 * @createDate： 2016年9月23日 下午1:49:08 
	 * @modifier marongbin
	 * @modifyDate：2016年9月23日 下午1:49:08
	 * @param：  数组id
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public Hospital getHospByHospId(Integer id) {
		return super.get(id);
	}
	/** 得到所有医院信息
	* @Title: getAllHosp 得到所有医院信息
	* @Description: 得到所有医院信息
	* @author dtl 
	* @date 2016年11月9日
	*/
	@Override
	public List<Hospital> getAllHosp() {
		String hql = "from Hospital h where h.del_flg = 0 and h.stop_flg = 0";
		return super.find(hql, null);
	}

	/**  
	 * 
	 * <p> 根据code获取医院 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2017年2月20日 下午2:37:15 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2017年2月20日 下午2:37:15 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: Hospital
	 *
	 */
	@Override
	public Hospital getHospitalByCode(String code) {
		return findUniqueBy("code", code);
	}
	
}