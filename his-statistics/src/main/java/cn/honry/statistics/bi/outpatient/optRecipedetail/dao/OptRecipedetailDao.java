package cn.honry.statistics.bi.outpatient.optRecipedetail.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiRegister;
import cn.honry.base.bean.model.BiRegisterGrade;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.outpatient.optRecipedetail.vo.OptRecipedetailVo;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.vo.OutpatientWorkloadVo;
import cn.honry.statistics.util.dateVo.DateVo;

@SuppressWarnings({"all"})
public interface OptRecipedetailDao extends EntityDao<BiRegister>{
	/**
	 * 查询所有科室
	 * @author huimingzheng
	 * @createDate：2016/7/15
	 * @version 1.0
	 */
	List<SysDepartment> queryAllDept();
	/**
	 * 动态查询统计数据(门诊医生工作量统计)
	 * @author huimingzheng
	 * @param diArrayKey :选择的维度种类拼接的数组
	 * @param list :key为维度种类，value为维度对应的值的字符串（用","分隔）  的 map 作为元素的list
	 * @param dateKey :查询出来的时间代理外键拼接的字符串（中间用","隔开）
	 * @param dateType :选择的时间维度的种类标识 1：年 ，2：季度，3：月，4：日（部分模块需要）
	 * @createDate：2016/7/15
	 * @version 1.0
	 */
	List<OptRecipedetailVo> querytOptRecipedetailDatagrid(String [] diArrayKey,List<Map<String,List<String>>> list,int datetype,DateVo datevo );
	
	/**
	 * 查询科室code、name
	 * @author hmz
	 * @return
	 */
	
	List<BiBaseOrganization> queryDeptForBiPublic(String deptType);



}
