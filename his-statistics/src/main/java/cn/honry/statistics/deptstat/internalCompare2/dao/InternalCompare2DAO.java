package cn.honry.statistics.deptstat.internalCompare2.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.deptstat.internalCompare2.vo.FicDeptVO;
import cn.honry.statistics.deptstat.internalCompare2.vo.InternalCompare2Vo;
import cn.honry.statistics.doctor.regisdocscheinfo.vo.RegisDocScheInfoVo;
import cn.honry.statistics.doctor.wordLoadDoctorTotal.vo.WordLoadVO;

@SuppressWarnings({ "all" })
public interface InternalCompare2DAO extends EntityDao<InternalCompare2Vo> {
	/**
	 * 
	 * <p>查询医院内科医学部和内二医学部对比数据</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午4:50:36 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午4:50:36 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<InternalCompare2Vo>
	 *
	 */
	List<InternalCompare2Vo> queryinternalCompare2list(String timed, String stime, String deptCode1List,
			List<String> tnLMZ1, List<String> tnLMZ2, List<String> tnLZY1, List<String> tnLZY2)  throws Exception;

	/**
	 * 医院内科医学部和内二医学部对比表2 预处理
	 * @param begin
	 * @param end
	 * @param type
	 * @throws Exception 
	 */
	void initMZZYTotalByDayOrHours(List<String> maintnl, List<String> tnL, String begin, String end, Integer hours) throws Exception;
	/**
	 * 
	 * <p>从mongo查询医院内科医学部和内二医学部对比数据</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午4:50:36 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午4:50:36 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<InternalCompare2Vo>
	 *
	 */
	List<InternalCompare2Vo> queryForDBSque(String timed, String stime,String deptCode1List)  throws Exception;
	
}
