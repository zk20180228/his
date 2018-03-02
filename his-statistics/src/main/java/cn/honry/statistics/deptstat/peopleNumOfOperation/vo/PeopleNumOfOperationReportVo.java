package cn.honry.statistics.deptstat.peopleNumOfOperation.vo;

import java.util.List;

/** 手术科室手术人数统计（含心内）VO
* @ClassName: PeopleNumOfOperationReportVo 手术科室手术人数统计（含心内）VO
* @Description: 手术科室手术人数统计（含心内）VO
* @author zxl
* @date 2017年6月6日
*  
*/
public class PeopleNumOfOperationReportVo{

	/** 
	* @Fields peopleNumOfOperationVo : 手术科室手术人数统计（含心内）
	*/ 
	private List<PeopleNumOfOperationVo> peopleNumOfOperationVo;

	public List<PeopleNumOfOperationVo> getPeopleNumOfOperationVo() {
		return peopleNumOfOperationVo;
	}

	public void setPeopleNumOfOperationVo(
			List<PeopleNumOfOperationVo> peopleNumOfOperationVo) {
		this.peopleNumOfOperationVo = peopleNumOfOperationVo;
	}

	public PeopleNumOfOperationReportVo(
			List<PeopleNumOfOperationVo> peopleNumOfOperationVo) {
		super();
		this.peopleNumOfOperationVo = peopleNumOfOperationVo;
	}
	
}
