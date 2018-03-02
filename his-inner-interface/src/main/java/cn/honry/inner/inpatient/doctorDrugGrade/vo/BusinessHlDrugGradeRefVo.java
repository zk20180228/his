package cn.honry.inner.inpatient.doctorDrugGrade.vo;

/**
 * @className：BusinessHlDrugGradeRefVo
 * @Description:药品等级表和医院表中间表联合查询的VO，只需要相关药品关联的医院的ID和名称
 * @Author: huangbiao
 * @CreateDate: 2016年4月8日
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */
public class BusinessHlDrugGradeRefVo {
	
	/**
	 * @Description:医院ID
	 */
	private int id;
	/**
	 * @Description:医院名称
	 */
	private String name;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
