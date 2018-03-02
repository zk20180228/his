package cn.honry.statistics.bi.inpatient.hospitalizationExpenses.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BiInpatientFeeinfo;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.inpatient.hospitalizationExpenses.vo.HospitalizationExpensesVo;
import cn.honry.statistics.util.dateVo.DateVo;

@SuppressWarnings({"all"})
public interface HospitalizationExpensesDao extends EntityDao<BiInpatientFeeinfo>{
	

	List<HospitalizationExpensesVo> querytDatagrid(String [] diArrayKey,List<Map<String,List<String>>> list,int datetype,DateVo datevo);

}
