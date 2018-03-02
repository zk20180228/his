package cn.honry.inner.technical.mat.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.MatApply;
import cn.honry.base.bean.model.MatOutput;
import cn.honry.base.bean.model.MatStockInfo;
import cn.honry.base.bean.model.MatStockdetail;
import cn.honry.inner.technical.mat.dao.MatOutPutInInterDAO;
import cn.honry.inner.technical.mat.service.MatOutPutInInterService;
import cn.honry.inner.technical.mat.vo.OutputInInterVO;
import cn.honry.utils.MyBeanUtils;

/**  
 *  
 * @className：MatOutPutServiceImpl.java
 * @Author：luyanshou
 * @version 1.0
 *
 */
@Service("matOutPutInInterService")
@Transactional
@SuppressWarnings({"all"})
public class MatOutPutInInterServiceImpl implements MatOutPutInInterService {

	private MatOutPutInInterDAO matOutPutDAO;
	
	@Autowired
	@Qualifier(value="matOutPutInInterDAO")
	public void setMatOutPutDAO(MatOutPutInInterDAO matOutPutDAO) {
		this.matOutPutDAO = matOutPutDAO;
	}


	/**
	 * 物资出库时判断非药品是否为物资,如果是物资,判断是否库存足够
	 */
	public Map<String,Object> judgeMat(OutputInInterVO out){
		Map<String,Object> map =new HashMap();
		Double applyNum = out.getApplyNum();//获取申请数量
		String itemCode = getItemCode(out);//获取物品编码
		Integer type = out.getTransType();//获取交易类型
		if(itemCode==null){//非物资
			map.put("resCode", 3);
			map.put("resMsg", "非物资");
			return map;
		}
		if(type==1||(type==2 && out.getUseNum()>0)){
			Integer sum = getSum(itemCode,out.getStorageCode());//获取库存总数
			if(type==2){
				applyNum=out.getUseNum();
			}
			if(sum<applyNum){//库存数量小于申请数量,即库存不足
				map.put("resCode", 1);
				map.put("resMsg", "库存不足");
				return map;
			}
		}
		map.put("resCode", 0);
		map.put("resMsg", "物资库存充足");
		return map;
	}
	
	/**
	 * 物资出库(或退库)时添加出库记录、出库申请并修改库存明细表和库管表相应的数量及金额
	 */
	public Map<String,Object> addRecord(OutputInInterVO out){
		Map<String,Object> map =new HashMap();
		String outListCode=null;//出库单号
		Double applyNum = out.getApplyNum();//获取申请数量
		Double useNum = out.getUseNum().doubleValue();//使用数量
		String itemCode = getItemCode(out);//获取物品编码
		if(itemCode==null){//非物资
			map.put("resCode", 3);
			map.put("resMsg", "非物资");
			return map;
		}
		Integer type = out.getTransType();//获取交易类型
		String returnOutNo = null;
		List<MatOutput> newList = null;
		if(type==2){//反交易
			List<MatOutput> matOutputList = getMatOutput(out);//获取出库记录
			if(matOutputList==null||matOutputList.size()==0){
				map.put("resCode", 2);
				map.put("resMsg", "未找到交易记录");
				return map;
			}
			if(useNum>0){
				outListCode=getMaxNo("SEQ_OUT_BILL_CODE");//新的出库记录的出库单号
				newList= new ArrayList();//新的出库记录
			}
			for (MatOutput m : matOutputList) {
				if(useNum>0){
					MatOutput matOutput = new MatOutput();//新的出库记录
					MyBeanUtils.copyPropertiesButNull(matOutput, m);
					matOutput.setOutListCode(outListCode);//设置出库单号
					matOutput.setId(null);
					Double n=m.getOutNum()>useNum?useNum:m.getOutNum();
					matOutput.setOutNum(n);//出库数量
					matOutput.setOutCost(n*matOutput.getOutPrice());//出库金额
					String outNo = getMaxNo("SEQ_MAT_OUT_NO");//出库流水号
					matOutput.setOutNo(outNo);//设置出库流水号
					useNum=useNum-n;
					newList.add(matOutput);
				}
				MatOutput output = new MatOutput();
				MyBeanUtils.copyPropertiesButNull(output, m);
				output.setId(null);
				output.setTransType(2);//设置交易类型
				output.setOutNum(-1*m.getOutNum());//设置出库数量
				output.setOutCost(-1*m.getOutCost());//设置出库金额
				matOutPutDAO.add(output);//添加冲账记录
				returnOutNo=m.getOutNo();//记录退掉的出库流水号
				update(m, 1);//更新库存和金额
			}
			if(out.getUseNum()==0){//执行数量为0,即全部退库
				map.put("resCode", 0);
				map.put("resMsg", "退库成功");
				return map;
			}
		}
		if(type==1||(type==2 && out.getUseNum()>0)){//正交易(或反交易但执行数量大于0)
			Integer sum = getSum(itemCode,out.getStorageCode());//获取库存总数
			if(type==2){
				applyNum=out.getUseNum();
			}
			if(sum<applyNum){//库存数量小于申请数量,即库存不足
				map.put("resCode", 1);
				map.put("resMsg", "库存不足");
				return map;
			}
			
			List<MatOutput> list = null;
			if(type==1){
				list=addOutput(applyNum, itemCode, out.getStorageCode());
			}else{
				list= newList;
			}
			if(list==null||list.size()==0){
				map.put("resCode", 2);
				map.put("resMsg", "此物资出库其他条件不满足");
				return map;
			}
			String applyListCode = getMaxNo("SEQ_MAT_APPLY_LISTCODE");//生成申请单号
			for (MatOutput m : list) {
				//m.setTargetDept(getPatientId(out));//设置领用科室编码(即患者Id)
				m.setTargetDept(out.getInpatientNo());//设置领用科室编码(住院流水号或门诊号)
				m.setRecipeNo(out.getRecipeNo());//设置处方号
				m.setSequenceNo(out.getSequenceNo());//设置处方内流水号
				m.setGetPersonid(out.getInpatientNo());//住院流水号或门诊卡号
				//m.setApplyOper(getPatientId(out));//申请人
				m.setApplyOper(out.getInpatientNo());
				MatApply matApply = getMatApply(m);
				matApply.setApplyListCode(applyListCode);//设置申请单号
				if(type==2){
					m.setReturnOutNo(returnOutNo);//设置已退掉的出库流水号
				}
				matOutPutDAO.add(matApply);
				matOutPutDAO.add(m);
				update(m, -1);//更新库存和金额
				outListCode=m.getOutListCode();//出库单号
			}
			map.put("resCode", 0);
			if(type==1){
				map.put("resMsg", "出库成功");
			}
			if(type==2){
				map.put("resMsg", "退库成功");
			}
			map.put("billNo", outListCode);
		}	
		
		return map;
	}
	/**
	 * 查询出库记录
	 * @param out
	 * @return
	 */
	private List<MatOutput> getMatOutput(OutputInInterVO out){
		MatOutput output = new MatOutput();
		//String patientId = getPatientId(out);//获取患者Id
		String patientId=out.getInpatientNo();//改为住院流水号或门诊号
		if(StringUtils.isBlank(patientId)){
			return null;
		}
		String outListCode= out.getOutListCode();//出库单号
		if(StringUtils.isBlank(outListCode)){
			return null;
		}
		String itemCode = getItemCode(out);//根据非药品编码获取物资编码
		output.setTargetDept(patientId);//设置领用科室编码
		output.setRecipeNo(out.getRecipeNo());//设置处方号
		output.setSequenceNo(out.getSequenceNo());//设置处方内流水号
		output.setTransType(1);//设置交易类型
		output.setItemCode(itemCode);//设置物资编码
		output.setStorageCode(out.getStorageCode());//设置仓库编码
		output.setOutListCode(outListCode);//出库单号
		List<MatOutput> list = matOutPutDAO.getMatOutput(output, null);//获取出库记录
		return list;
	}
	/**
	 * 获取患者Id
	 */
	private String getPatientId(OutputInInterVO out){
		String patientId = null;
		String flag = out.getFlag();
		String inpatientNo= out.getInpatientNo();
		if(flag=="C"){//inpatientNo是住院流水号
			patientId= matOutPutDAO.getPatientID(inpatientNo);//根据住院流水号获取患者id
		}
		if(flag=="T"){//inpatientNo是门诊号
			patientId= matOutPutDAO.getPatientIdByNo(inpatientNo);//根据门诊号获取患者id
		}
		return patientId;
	}
	/**
	 * 获取itemCode(物资编码)
	 * @param out
	 * @return
	 */
	private String getItemCode(OutputInInterVO out){
		String itemCode=matOutPutDAO.getItemCode(out.getUndrugItemCode());//根据非药品编码获取物资编码;
		return itemCode;
	}
	
	/**
	 * 获取库存数量
	 * @param itemCode
	 * @param storageCode
	 * @return
	 */
	private Integer getSum(String itemCode,String storageCode){
		Integer storeSum = matOutPutDAO.getSum(itemCode, storageCode);//库存总数
		Integer preoutSum = matOutPutDAO.getpreoutSum(itemCode, storageCode);//预扣库存
		Integer sum= storeSum - preoutSum;
		return sum;
	}
	/**
	 * 添加出库记录
	 */
	private List<MatOutput> addOutput(Double applyNum,String itemCode,String storageCode){
		List<MatOutput> outputList=new ArrayList<>();
		MatOutput output;
		Date date= new Date();
		Double i=0d;
		List<MatStockdetail> list = matOutPutDAO.getList(itemCode, storageCode);//根据物资编码和仓库编码查询出对应的库存明细记录
		if(list==null||list.size()==0){
			return null;
		}
		String outListCode = getMaxNo("SEQ_OUT_BILL_CODE");//生成出库单号
		for (MatStockdetail m : list) {
			if(applyNum>0){
				if(m.getStoreNum()>applyNum){
					i++;//单内序号
					 output = createMatOutput(m, applyNum, i);//根据库存明细创建出库记录
					 output.setOutListCode(outListCode);//设置出库单号
					applyNum=0d;
				}else{
					i++;
					 output = createMatOutput(m, m.getStoreNum(), i);
					 output.setOutListCode(outListCode);//设置出库单号
					applyNum=applyNum-m.getStoreNum().intValue();
				}
				output.setOutDate(date);//出库日期
				output.setApplyDate(date);//申请时间
				output.setExamDate(date);//审核时间
				output.setApproveDate(date);//核准时间
				output.setCreateTime(date);//创建时间
				output.setUpdateTime(date);//更新时间
				outputList.add(output);
			}
		}
		return outputList;
	}
	/**
	 * 根据库存明细表生成出库记录
	 */
	private MatOutput createMatOutput(MatStockdetail m,Double num,Double i){
		MatOutput out = new MatOutput();
		String applyNo=getMaxNo("SEQ_MAT_APPLY");//生成申请流水号
		out.setApplyNo(applyNo);//设置申请流水号
		out.setOutNo(getMaxNo("SEQ_MAT_OUT_NO"));//设置出库流水号
		out.setOutSerialNo(i.intValue());//设置单内序号
		out.setTransType(1);//设置交易类型
		out.setStorageCode(m.getStorageCode());//仓库编码
		out.setStockCode(m.getStockCode());//库存流水号
		out.setRegCode(m.getRegCode());//生产厂家认证记录编号
		out.setOutState(2);//状态:2-核准出库
		out.setItemCode(m.getItemCode());//物资编码
		out.setItemName(m.getItemName());//物资名称
		out.setKindCode(m.getKindCode());//科目编码
		out.setSpecs(m.getSpecs());//规格
		out.setBatchNo(m.getBatchNo());//批号
		out.setOutNum(num.doubleValue());//出库数量
		out.setMinUnit(m.getMinUnit());//最小单位
		out.setPackUnit(m.getPackUnit());//大包装单位
		out.setPackQty(m.getPackQty());//大包装包装数量
		out.setOutPrice(m.getInPrice());//出库价格
		out.setOutCost(num*m.getInPrice());//出库金额
		out.setSalePrice(m.getSalePrice());//零售价格
		out.setSaleCost(num*m.getSalePrice()*m.getPackQty());//零售金额
		out.setValidDate(m.getValidDate());//有效期
		out.setPlaceCode(m.getPlaceCode());//库位号
		out.setGetType("1");//领用类型(0-科室领用,1-患者领用)
		out.setProduceDate(m.getProduceDate());//生产日期
		out.setBarCode(m.getBarCode());//条形码
		out.setHighvalueFlag(m.getHighvalueFlag());//高值耗材标志
		out.setHighvalueBarcode(m.getHighvalueBarcode());//高值耗材条形码
		return out;
	}
	
	/**
	 * 根据出库记录生成申请记录
	 */
	private MatApply getMatApply(MatOutput output){
		MatApply apply = new MatApply();
		apply.setApplyNo(output.getApplyNo());//申请流水号
		apply.setApplySerialNo(output.getOutSerialNo());//单内序号
		apply.setStorageCode(output.getStorageCode());//申请部门
		apply.setTargetDept(output.getTargetDept());//目标科室
		apply.setStockClass(2);//类型:1-入库申请、2-出库申请
		apply.setApplyState(2);//申请状态(0-申请,1-审批,2-核准)
		apply.setItemCode(output.getItemCode());//物资编码
		apply.setItemName(output.getItemName());//物资名称
		apply.setKindCode(output.getKindCode());//物资科目编码
		apply.setSpecs(output.getSpecs());//规格
		apply.setMinUnit(output.getMinUnit());//最小单位
		apply.setPackUnit(output.getPackUnit());//包装单位
		apply.setPackQty(output.getPackQty());//包装数量
		apply.setApplyNum(output.getOutNum().intValue());//申请数量
		apply.setApplyPrice(output.getOutPrice());//申请价格
		apply.setApplyCost(output.getOutCost());//申请金额
		apply.setSalePrice(output.getSalePrice());//零售价格
		apply.setSaleCost(output.getSaleCost());//零售金额
		apply.setApplyOper(output.getApplyOper());//申请人
		apply.setApplyDate(output.getApplyDate());//申请时间
		apply.setOutListCode(output.getOutListCode());//出库单号
		apply.setCreateUser(output.getApplyOper());//创建人
		apply.setCreateTime(output.getApplyDate());//创建时间
		apply.setCreateDept(output.getStorageCode());//创建部门
		return apply;
	}
	/**
	 * 生成申请流水号、申请单号、出库流水号、出库单号等
	 * @param seq
	 * @return
	 */
	private String getMaxNo(String seq){
		String no = matOutPutDAO.getMaxNo(seq);
		return no;
	}
	/**
	 * 更新库管表和库存明细表记录
	 */
	private void update(MatOutput m,Integer i){
		
		MatStockInfo matStockInfo = new MatStockInfo();
		MatStockdetail matStockdetail = new MatStockdetail();
		
		matStockInfo.setItemCode(m.getItemCode());//设置物品编码
		matStockInfo.setItemDeptCode(m.getStorageCode());//设置仓库编码
		matStockInfo.setPlaceCode(m.getPlaceCode());//设置库位号
		
		matStockdetail.setItemCode(m.getItemCode());//设置物品编码
		matStockdetail.setStorageCode(m.getStorageCode());//设置物品编码
		matStockdetail.setBatchNo(m.getBatchNo());//设置批号
		matStockdetail.setPlaceCode(m.getPlaceCode());//设置库位号
		
		MatStockInfo info = matOutPutDAO.getMatStockInfo(matStockInfo);//获取库管表实体
		//修改库管表库存和金额记录
		Double storeSum = info.getStoreSum();//获取库存
		Double storeCost = info.getStoreCost();//获取金额
		info.setStoreSum(storeSum+m.getOutNum()*i);//修改库存
		info.setStoreCost(storeCost+m.getOutCost()*i);//修改金额
		
		MatStockdetail stockdetail = matOutPutDAO.getMatStockdetail(matStockdetail);//获取库存明细实体
		//修改库存明细表库存和金额
		Double storeNum = stockdetail.getStoreNum();
		Double cost = stockdetail.getStoreCost();
		stockdetail.setStoreNum(storeNum+m.getOutNum()*i);
		stockdetail.setStoreCost(cost+m.getOutCost()*i);
		Double salePrice = stockdetail.getSalePrice();//获取零售价格
		Integer packQty = stockdetail.getPackQty();//获取包装数量
		stockdetail.setSaleCost(stockdetail.getStoreNum()*salePrice*packQty);//修改零售金额
		//更新记录
		matOutPutDAO.updateStockInfo(info);
		matOutPutDAO.updateStockdetail(stockdetail);
	} 
}
