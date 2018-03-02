package cn.honry.inpatient.outBalance.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientChangeprepay;
import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.base.dao.EntityDao;

public interface InpatientInPrepayDAO extends EntityDao<InpatientInPrepay>{
	/**
	 * 根据住院流水号和时间查询预交金总额
	 */
	List<InpatientInPrepay> queryprepayCost(String inpatientNo,String outDate,String inDate);
	/**
	 * 根据流水号转入预交金
	 */
	List<InpatientChangeprepay> queryInpatientChangeprepay(String inpatientNo,String outDate,String inDate);
}
