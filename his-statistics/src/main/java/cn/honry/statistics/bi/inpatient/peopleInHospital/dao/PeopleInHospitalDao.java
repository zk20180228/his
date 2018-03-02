package cn.honry.statistics.bi.inpatient.peopleInHospital.dao;


import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BiInpatientInfo;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.inpatient.peopleInHospital.vo.PeopleInHospitalVo;
import cn.honry.statistics.util.dateVo.DateVo;
/***
 * 在院人次统计DAO层
 * @Description:
 * @author: hanzurong
 * @CreateDate: 2016年8月10日 
 * @version 1.0
 */
public interface PeopleInHospitalDao extends EntityDao<BiInpatientInfo>{


	/**
	 * 动态查询统计数据(在院人次统计)
	 * @author hanzurong
	 * @param diArrayKey :选择的维度种类拼接的数组
	 * @param list :key为维度种类，value为维度对应的值的字符串（用","分隔）  的 map 作为元素的list
	 * @param dateKey :查询出来的时间代理外键拼接的字符串（中间用","隔开）
	 * @param dateType :选择的时间维度的种类标识 1：年 ，2：季度，3：月，4：日（部分模块需要）
	 * @createDate：2016/8/17
	 * @version 1.0
	 */
	List<PeopleInHospitalVo> queryregisterid(String[] diArrayKey, List<Map<String, List<String>>> list,
			int dateType, DateVo datevo);
	

}
