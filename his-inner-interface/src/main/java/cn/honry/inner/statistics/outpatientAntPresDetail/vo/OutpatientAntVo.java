package cn.honry.inner.statistics.outpatientAntPresDetail.vo;

/**
 * 
 * 
 * <p>门诊抗菌药物处方比例VO </p>
 * @Author: XCL
 * @CreateDate: 2017年7月6日 下午6:07:43 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月6日 下午6:07:43 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public class OutpatientAntVo {
	/**
	 * 医生姓名
	 */
	private String docName;
	
	/**
	 * 药物处方数
	 */
	private String drugCfs;
	
	/**
	 * 抗菌药物处方数
	 */
	private String drugKjcfs;
	
	/**
	 * 抗菌药物处方比例
	 */
	private String drugBl;
	
	/**
	 * 院规比例
	 */
	private String ygbl;
	
	/**
	 * 对比
	 */
	private String equel;
	
	/**
	 * 时间
	 */
	private String name;
	
	/**
	 * 科室
	 */
	private String dept;
	
	public String getName() {
		return name;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDrugCfs() {
		return drugCfs;
	}

	public void setDrugCfs(String drugCfs) {
		this.drugCfs = drugCfs;
	}

	public String getDrugKjcfs() {
		return drugKjcfs;
	}

	public void setDrugKjcfs(String drugKjcfs) {
		this.drugKjcfs = drugKjcfs;
	}

	public String getDrugBl() {
		return drugBl;
	}

	public void setDrugBl(String drugBl) {
		this.drugBl = drugBl;
	}

	public String getYgbl() {
		return ygbl;
	}

	public void setYgbl(String ygbl) {
		this.ygbl = ygbl;
	}

	public String getEquel() {
		return equel;
	}

	public void setEquel(String equel) {
		this.equel = equel;
	}

	@Override
	public String toString() {
		return "OutpatientAntVo [docName=" + docName + ", drugCfs=" + drugCfs
				+ ", drugKjcfs=" + drugKjcfs + ", drugBl=" + drugBl + ", ygbl="
				+ ygbl + ", equel=" + equel + "]";
	}
	
}
