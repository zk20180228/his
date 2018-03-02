package cn.honry.inpatient.doctorAdvice.service;

import java.util.List;

import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.InpatientDrugbilldetail;
import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.service.BaseService;
import cn.honry.utils.TreeJson;

public interface DoctorAdviceService extends BaseService<InpatientInfo>{
	
	/**  
	 *  
	 * @Description： 药物执行单树
	 * @Author：yeguanqun
	 * @param id：树节点id  
	 * @CreateDate：2015-12-12   
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	public List<TreeJson> treeDrugExes(String id) throws Exception;
	/**  
	 *  
	 * @Description： 医嘱类别表
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-18   
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	public List<InpatientKind> treeDrugExe() throws Exception;
	/**  
	 *  
	 * @Description： 非药物执行单树
	 * @Author：yeguanqun
	 * @param id：树节点id  
	 * @CreateDate：2015-12-14   
	 * @version 1.0
	 *
	 */
	public List<TreeJson> treeNoDrugExe(String id);
	
	/**  
	 *  
	 * @Description： 查询执行单信息
	 * @Author：yeguanqun
	 * @param  
	 * @CreateDate：2015-12-14   
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	public List queryDocAdvExe(String ids,String billName) throws Exception;
	/**  
	 *  
	 * @Description：  添加&修改
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-16 14:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	public void saveOrUpdateInpatientExecbill(InpatientExecbill inpatientExecbill) throws Exception;
	
	/**  
	 *  
	 * @Description： 保存表格中新增的医嘱执行单数据
	 * @Author：yeguanqun
	 * @param str：页面新增行数据  
	 * @CreateDate：2015-12-19 14:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	public void saveOrUpdateInpatientDrugbilldetail(String str,String billNo) throws Exception;
	/**  
	 *  
	 * @Description：  删除执行单tab
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-17 10:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	public void del(String id) throws Exception;
	/**  
	 *  
	 * @Description： 非药物执行单树
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-14   
	 * @version 1.0
	 * @param rows 
	 * @param page 
	 * @throws Exception 
	 *
	 */
	public List<InpatientDrugbilldetail> queryInpatientDrugbilldetail(InpatientDrugbilldetail inpatientDrugbilldetail) throws Exception;
	/**  
	 *  
	 * @Description：  删除医嘱执行明细
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-17 10:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	public void delDrugbilldetail(String id) throws Exception;
	/**  
	 *  
	 * @Description： 查询非药品信息
	 * @Author：yeguanqun
	 * @CreateDate：2016-4-26   
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	public List<DrugUndrug> queryUndrugInfo(String page,String rows,DrugUndrug undrug) throws Exception;
	/**  
	 *  
	 * @Description：  查询非药品信息总条数
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2016-4-26   
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	int getTotal(DrugUndrug undrug) throws Exception;
	/**  
	 *  
	 * @Description： 查询非药品信息--页面渲染
	 * @Author：yeguanqun
	 * @CreateDate：2016-4-26   
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	public List<DrugUndrug> queryUndrugInfo() throws Exception;
	
	/**  
	 *  
	 * @Description： 得到医嘱执行单明细列表(分页)
	 * @Author：zxl
	 * @CreateDate：2015-12-17   
	 * @version 1.0
	 * @param rows 
	 * @param page 
	 * @throws Exception 
	 *
	 */
	public List<InpatientDrugbilldetail> queryDrugbilldetail(
			InpatientDrugbilldetail inpatientDrugbilldetail, String page,
			String rows) throws Exception;
	
	/**  
	 *  
	 * @Description： 得到医嘱执行单明细列表(分页条数)
	 * @Author：zxl
	 * @CreateDate：2015-12-17   
	 * @version 1.0
	 * @param rows 
	 * @param page 
	 * @throws Exception 
	 *
	 */
	public int getTotalBilldetail(InpatientDrugbilldetail inpatientDrugbilldetail) throws Exception;
	/**
	 * 
	 * 
	 * <p>根据id 名称查询执行单 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午4:56:44 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午4:56:44 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param ids
	 * @param billName
	 * @return:
	 * @throws Exception 
	 *
	 */
	public List<InpatientExecbill> queryDocAdvExe2(String ids,String billName) throws Exception;
	
	/**
	 * 
	 * 
	 * <p>保存医嘱执行单 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午4:50:52 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午4:50:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param inpatientDrugbilldetail:
	 * @throws Exception 
	 *
	 */
	public void saveDrugBillDetail(InpatientDrugbilldetail inpatientDrugbilldetail) throws Exception;
	
	/**
	 * 
	 * 
	 * <p>获取药品类别 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午4:58:55 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午4:58:55 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws Exception 
	 *
	 */
	public List queryDrugType() throws Exception;
	/**
	 * 
	 * 
	 * <p>获取用法 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午4:59:33 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午4:59:33 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws Exception 
	 *
	 */
	public List queryDrugUsage() throws Exception;
	
	/**
	 * 
	 * 
	 * <p>加载所有非药品名称下拉 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午4:49:59 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午4:49:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws Exception 
	 *
	 */
	public List queryAllUndrug() throws Exception;
	
	/**
	 * 
	 * 
	 * <p>查询所有执行单 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午4:51:07 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午4:51:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param inpatientDrugbilldetail
	 * @return:
	 * @throws Exception 
	 *
	 */
	public List queryAllBillDetail(InpatientDrugbilldetail inpatientDrugbilldetail) throws Exception;
	
	/**
	 * 
	 * 
	 * <p>更新执行单 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午4:53:25 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午4:53:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param oInpatientDrugbilldetail:
	 * @throws Exception 
	 *
	 */
	public void updateDrugBillDetail(InpatientDrugbilldetail inpatientDrugbilldetail) throws Exception;
}
