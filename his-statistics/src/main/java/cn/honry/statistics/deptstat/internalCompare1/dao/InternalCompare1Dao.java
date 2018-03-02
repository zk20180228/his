package cn.honry.statistics.deptstat.internalCompare1.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.deptstat.internalCompare1.vo.FicDeptVO;
import cn.honry.statistics.deptstat.internalCompare1.vo.InternalCompare1Vo;

/**  
 *  
 * @className：InternalCompare1Dao
 * @Description： 郑州大学第一附属医院儿外一门诊和内二医学部对比表1
 * @Author：gaotiantian
 * @CreateDate：2017-6-5 下午4:35:21
 * @Modifier：
 * @ModifyDate：
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface InternalCompare1Dao extends EntityDao<InternalCompare1Vo> {
	/**
	 * 查询对比信息
	 * @author gaotiantian
	 * @createDate：2017-6-5  下午4:35:21
	 * @version 1.0
	 */
	List<InternalCompare1Vo> getInternalCompare1(List<String> tnL,List<String> tnL1, String prevTime, String time, String dept);
	/**
	 * 初始化，向mongodb里面存数据
	 * @author gaotiantian
	 * @createDate：2017-6-7  下午4:35:21
	 * @version 1.0
	 */
	void saveInternalCompare1ToDB(List<String> maintnl, List<String> tnL, String begin, String end, String dept);
	/**
	 * 从mongodb中取数据
	 * @author gaotiantian
	 * @createDate:2017年6月8日 上午10:27:30 
	 * @version 1.0
	 */
	List<InternalCompare1Vo> queryForDBSque(String prevTime, String time, String depts1, String deptName);
	/**
	 * <p>根据虚拟科室code获取科室 </p>
	 * @Author: gaotiantian
	 * @CreateDate: 2017年7月11日 上午10:18:29 
	 * @Modifier: gaotiantian
	 * @ModifyDate: 2017年7月11日 上午10:18:29 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param ficDeptCode
	 * @return:List<FicDeptVO>
	 * @throws:
	 *
	 */
	List<FicDeptVO> getDept(String ficDeptCode);

}

