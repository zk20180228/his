package cn.honry.inner.inpatient.info.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.service.BaseService;
import cn.honry.inner.inpatient.info.vo.FeeInInterVo;


/**
 * ClassName: InpatientInfoService 
 * @Description: 住院登记表业务逻辑接口
 * @author lt
 * @date 2015-6-24
 */
public interface InpatientInfoMobileterService extends BaseService<InpatientInfoNow> {
	/**
	 * @Description:费用接口
	 * @Author： yeguanqun
	 * @CreateDate： 2016-2-19
	 * @param feeVoList：费用结算vo集合
	 * map中的几种消息状态及信息：
	 * resCode：对应的处理状态的信息提示
	 * resMsg：表示处理的状态
	 * resCode="success" 生成费用成功
	 * resCode="arrearage" -患者余额不足
	 * resCode="error"-患者正在出院结算或已经无费退院，不能进行收费操作、婴儿患者母亲信息不存在，不能进行收费操作、婴儿患者母亲非在院状态，请先进行召回操作、患者账户关闭，不能进行收费操作
	 * @version 1.0
	**/
	public Map<String, Object> saveInpatientFeeInfo(List<FeeInInterVo> feeVoList,String empJobNo,String deptCode);
	
	
	/**
	 * 反交易收费	
	* @Title: reverseTran
	* @Description: 
	* @param feeVoList  某一个患者的所有退费项目 
	* @return 
	* @date 2016年5月16日下午5:48:02
	 */
	Map<String,Object> reverseTran(List<FeeInInterVo> feeVoList,String empoJobNo,String deptCode);
}
