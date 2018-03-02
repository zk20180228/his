package cn.honry.inner.statistics.hospitalday.dao;

import java.util.Date;
import java.util.List;

import cn.honry.base.bean.model.BusinessYzcxzhcx;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.statistics.hospitalday.vo.HospitaldayVo;
import cn.honry.inner.statistics.hospitalday.vo.ResultVo;

public interface HospitaldayinnerDao extends EntityDao<BusinessYzcxzhcx>{
	/**  
	 * 
	 * 预处理在院人数
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	void init_YYMRHZ(String menuAlias, String type, String date) ;
	/**  
	 * 
	 * 查询门诊收入
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<HospitaldayVo> querymz(List<String> tnL,String parameter,String startTime,String endTime);
	/**  
	 * 
	 * 查询在院人次
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<HospitaldayVo> queryHospitaldayVoin(String date);
	/**  
	 * 
	 * 查询手术人次
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<HospitaldayVo> queryHospitaldayVoopear(String date);
	/**  
	 * 
	 * 查询出院人次
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<HospitaldayVo> queryHospitaldayVooutpa(String date);
	/**  
	 * 
	 * 查询医院每日的汇总
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<HospitaldayVo> queryHospitaldayList(List<String> tnL,String parameter,String startTime,String endTime);
	/**  
	 * 
	 * 查询医院每日的汇总
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<ResultVo> queryzhuyanList(List<String> tnL,String endTime);
	/**  
	 * 
	 * 查询每日运营情况接口
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<HospitaldayVo> queryListinner(String date);
	
	void save(BusinessYzcxzhcx businessYzcxzhcx);
	String update(BusinessYzcxzhcx businessYzcxzhcx);
	/**
	 * 根据时间和院区code来查询数据是否存在
	 */
	BusinessYzcxzhcx queryBusinessYzcxzhcx(Date operDate,String yq);
	/**
	 * 查询经营日报最大的时间
	 */
	List<BusinessYzcxzhcx> queryBusinessYzcxzhcxMaxdate();
	/**
	 * 根据时间查询经营日报数据 来判断是否存在
	 */
	List<BusinessYzcxzhcx> queryBusinessYzcxzhcx(String date);
}
