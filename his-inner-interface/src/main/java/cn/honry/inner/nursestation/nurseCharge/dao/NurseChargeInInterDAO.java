package cn.honry.inner.nursestation.nurseCharge.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugStockinfo;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientMedicineList;
import cn.honry.base.bean.model.MatUndrugCompare;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.nursestation.nurseCharge.vo.NurseChargeVo;

/**
 * @author 护士站收费DAO
 *
 */
public interface NurseChargeInInterDAO extends EntityDao<DrugUndrug>{


	/**  
	 *  
	 * @Description：渲染开方医生（根据床号）
	 * @Author：zhangjin
	 * @CreateDate：2016-1-6 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	NurseChargeVo querNurseDoctor(String id);

	/**  
	 *  
	 * @Description：最小费用与统计大类信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-9 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<NurseChargeVo> treeNurseAdvice(String soushu);
	/**  
	 *  
	 * @Description：最小费用与统计大类信息(子节点)
	 * @Author：zhangjin
	 * @CreateDate：2016-1-9 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<NurseChargeVo> treeNurseAdvicep(String feeStatCode,String soushu);
	/**  
	 *  
	 * @Description：组套信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-11 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<NurseChargeVo> treeStackNurseAdvicep(String deptId);
	/**  
	 *  
	 * @Description：组套信息(子节点)
	 * @Author：zhangjin
	 * @CreateDate：2016-1-11 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<NurseChargeVo> treeStackSonNurseAdvice(String stackId);
	/**  
	 *  
	 * @Description：回显最小费用与统计大类信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-9 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<NurseChargeVo> chargeMinfeetostat(String naId);
	/**  
	 *  
	 * @Description：回显组套信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-9 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<NurseChargeVo> businessStack(String naId,String deptCode);
	/**  
	 *  
	 * @Description：最小费用与统计大类信息下的非药品信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-12 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<NurseChargeVo> treeNurseAdviceUndrug(String naId);
	/**  
	 *  
	 * @Description：项目名称（下拉框（datagrid））总数
	 * @Author：zhangjin
	 * @CreateDate：2016-1-13 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(String q);

	/**  
	 *  
	 * @Description：项目名称（下拉框（datagrid））分页
	 * @Author：zhangjin
	 * @CreateDate：2016-1-13 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<NurseChargeVo> getPageSql(String page, String rows,String q);
	/**  
	 *  
	 * @Description：渲染单价
	 * @Author：zhangjin
	 * @CreateDate：2016-1-13
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<NurseChargeVo> nurseChargeMoney();
	/**  
	 *  
	 * @Description：渲染合同
	 * @Author：zhangjin
	 * @CreateDate：2016-1-13
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<NurseChargeVo> nurseChargePactCode();
	/**  
	 *  
	 * @Description：加载患者药品和非药品明细细信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-18
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<NurseChargeVo> InpatientMessage(String no);
	/**  
	 *  
	 * @Description：获取价格形式
	 * @Author：zhangjin
	 * @CreateDate：2016-1-19
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<NurseChargeVo> querNursePubRati(String id);
	/**  
	 *  
	 * @Description：加载婴儿是否绑定母亲
	 * @Author：zhangjin
	 * @CreateDate：2016-1-20
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<NurseChargeVo> hospitalParameter();
	/**  
	 *  
	 * @Description：加载住院金额
	 * @Author：zhangjin
	 * @CreateDate：2016-1-20
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<NurseChargeVo> nurseInpatBalance(String id);
	
    
	/**  
	 *  
	 * @Description： 获取取药科室
	 * @Author：zhangjin
	 * @CreateDate：2016-1-20
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	NurseChargeVo getInfoID(String deptCode);
	/**  
	 *  
	 * @Description：查询本病区下的医生
	 * @Author：zhangjin
	 * @CreateDate：2016-3-25
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<SysEmployee> queryEmplCode();
	/**  
	 *  
	 * @Description：查询当日的收费信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-22
	 * @Modifier：zhangjin
	 * @ModifyDate：2016-3-22 
	 *  @Modifier：zhangjin
	 * @ModifyDate：2016-3-25  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<NurseChargeVo> dayCharge(String inpatientNo);
	/**  
	 * @Description： 根据病历号或住院号查询信息
	 * @Author：donghe
	 * @CreateDate：2016-3-21
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<InpatientInfo> queryInpatientInfoList(String inpatientNo);
	/**  
	 *  
	 * @Description：渲染科室
	 * @Author：zhangjin
	 * @CreateDate：2016-3-30
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<SysDepartment> querydeptComboboxs();

	/**
	 * 查询非药品项目名称
	 * @author   zhangjin
	 * @createDate： 
	 * @modifyDate：2016-4-13
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<DrugUndrug> gainUndrug(String id);

	/**
	 * 查询计费人
	 * @author   zhangjin
	 * @createDate： 
	 * @modifyDate：2016-4-13
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */

	List<User> nurseEmply();

	/**
	 * 查询是否绑定物资
	 * @author   zhangjin
	 * @createDate： 
	 * @modifyDate：2016-4-13
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	MatUndrugCompare queryWz(String nid);

	/**
	 * 查询是否物资
	 * @author   zhangjin
	 * @createDate： 
	 * @modifyDate：2016-4-13
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	InpatientItemList getSequenceNo(String nid, String recipeNo);

	/**
	 * 获取住院科室对应药房的id和库存数量
	 * @author   zhangjin
	 * @createDate： 
	 * @modifyDate：2016-5-18
	 * @param：   nid(项目ID)
	 * @param2：   deptCode(住院科室)
	 * @modifyRmk：  
	 * @version 1.0
	 */
	DrugStockinfo getkydept(String deptCode,String nid);

	/**
	 * 对成功收费的药品进行查询（扣库存）
	 * @author   zhangjin
	 * @createDate： 
	 * @modifyDate：2016-6-3
	 * @param：   nid(项目ID)
	 * @param2：   deptCode(住院科室)
	 * @modifyRmk：  
	 * @version 1.0
	 */
	InpatientMedicineList getDrugSequenceNo(String nid, String string);
}
