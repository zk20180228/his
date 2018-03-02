package cn.honry.inner.drug.stockInfo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugCheckdetail;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugStockinfo;
import cn.honry.inner.drug.drugInfo.dao.DrugInfoInInerDAO;
import cn.honry.inner.drug.stockInfo.dao.BusinessStockInfoInInterDAO;
import cn.honry.inner.drug.stockInfo.service.BusinessStockInfoInInterService;
import cn.honry.inner.system.utli.OperationUtils;

/**
 * @author abc
 *
 */
@Service("businessStockInfoInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class BusinessStockInfoInInterServiceImpl implements BusinessStockInfoInInterService{
	
	
	@Autowired
	@Qualifier(value = "businessStockInfoInInterDAO")
	private BusinessStockInfoInInterDAO stockInfoDAO;
	
	/** 
	* @Fields drugInfoInInerDAO : druginfo接口dao
	*/ 
	@Autowired
	private DrugInfoInInerDAO drugInfoInInerDAO;
	
	/**
	 * @Description: 根据科室和药品得到该药品的可用数量（库存数量减预扣库存数量）
	 * @param storageDeptId 部门
	 * @param drugId 药品
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月18日
	 * @Version: V 1.0
	 */
	@Override
	public Double getDrugStockInfoStoreNum(String storageDeptId,String drugId){
		return stockInfoDAO.getDrugStockInfoStoreNum(storageDeptId, drugId);
	}

	@Override
	public DrugStockinfo get(String arg0) {
		return stockInfoDAO.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(DrugStockinfo arg0) {
		stockInfoDAO.save(arg0);
	}
	@Override
	public String withholdStock(String deptId, String drugId, double applyNum, String minunit) {
		DrugInfo info = drugInfoInInerDAO.getByCode(drugId);
		Integer showFlag = info.getShowFlag() == null ? 0 : info.getShowFlag();
		Integer show = Integer.parseInt(minunit);
		Integer packNum = info.getPackagingnum() == null ? 1 : info.getPackagingnum();
		Double prece = info.getDrugRetailprice() == null ? 0D : info.getDrugRetailprice();
		if (show - showFlag == 1) {
			applyNum = applyNum * packNum;
		}else if (show - showFlag == -1) {
			applyNum = applyNum / packNum;
		}
		/**以下操作是修改storageInfo表的预扣库存**/
		DrugStockinfo drugStockinfoCurr=stockInfoDAO.getStockInfo(deptId,drugId);
		if(drugStockinfoCurr==null){
			return "库存不存在！";
		}
		drugStockinfoCurr.setPreoutSum(drugStockinfoCurr.getPreoutSum()==null ? applyNum : drugStockinfoCurr.getPreoutSum() + applyNum);
		if (showFlag == 0) {
			drugStockinfoCurr.setPreoutCost(drugStockinfoCurr.getPreoutSum()  * prece / packNum);
		}else {
			drugStockinfoCurr.setPreoutCost(drugStockinfoCurr.getPreoutSum()  * prece);
		}
		stockInfoDAO.save(drugStockinfoCurr);
		OperationUtils.getInstance().conserve(drugStockinfoCurr.getId(),"修改预扣库存","UPDATE","T_DRUG_STORAGE",OperationUtils.LOGACTIONINSERT);
		return null;
	}

	@Override
	public String enoughYn(String deptCode, String drugCode, double applyNum) {
		Double sum=	stockInfoDAO.getDrugStockInfoStoreNum(deptCode, drugCode);
		if (applyNum > sum) {
			return "库存不足！";
		}
		return null;
	}

	@Override
	public Map<String, Object> ynValiuDrug(String deptCode, List<String> drugCodes, List<Double> applyNums, List<Integer> showFlags, boolean showInfo, boolean isStock, boolean isStatus) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer mesgs = new StringBuffer();
		map.put("valiuFlag", 1);//默认为可用
		map.put("failMesgs", "所有药品均可正常使用");//默认为可用
		Map<String, Double> appInfoMap = new HashMap<>();//申请信息map 用作判断库存数量
		Map<String, Double> storeNums = new HashMap<>();//库存数量map
		Map<String, Double> preNums = new HashMap<>();//预扣库存数量map
		Map<String, Integer> flags = new HashMap<>();//药品单位map
		Map<String, String> names = new HashMap<>();//药品名称map
		Map<String, Integer> packNums = new HashMap<>();//药品包装数量map
		Map<String, Integer> delMap = new HashMap<>();//药品删除标记map
		Map<String, Integer> stopMap = new HashMap<>();//药品停用标记map
		
		String drugCode;//申请药品编码循环变量
		Double applyNum;//申请数量循环变量
		Integer showFlag;//申请药品单位标记循环变量
		Double store;//库存数量
		Double preDouble;//预扣数量
		Integer flag;//申请药品单位标记循环变量
		String name;//药品通用名
		
		List<DrugInfo> infos = new ArrayList<>();
		List<DrugStockinfo> stockinfos = new ArrayList<>();
		
		/**
		 * 对药品集合做去重处理，并为所需信息map赋值
		 */
		for (int i = 0; i < drugCodes.size(); i++) {
			//为循环变量赋值
			drugCode = drugCodes.get(i);
			if (!appInfoMap.containsKey(drugCode)) {//如果之前没有遍历过此药品
				DrugInfo info = drugInfoInInerDAO.getByCode(drugCode);//获得药品信息
				flags.put(drugCode, info.getShowFlag());
				names.put(drugCode, info.getDrugCommonname());
				packNums.put(drugCode, info.getPackagingnum() == null || info.getPackagingnum() == 0 ? 1 : info.getPackagingnum());
				delMap.put(drugCode, info.getDel_flg());
				stopMap.put(drugCode, info.getStop_flg());
			}
			DrugStockinfo drugStockinfo = stockInfoDAO.getStockInfo(deptCode, drugCode);//得到科室下该药品库存信息
			if (drugStockinfo == null) {//科室下没有此药品库存记录
				mesgs.append(names.get(drugCode) + "在相应科室没有库存记录,");
				continue;
			}else {//为库存数量以及预扣库存赋值
				storeNums.put(drugCode, drugStockinfo.getStoreSum());
				preNums.put(drugCode, drugStockinfo.getPreoutSum() == null ? 0D : drugStockinfo.getPreoutSum());
			}
			/**
			 * 将数量统一为druginfo的数量单位
			 */
			if(isStock){//当考虑库存时
				applyNum = applyNums.get(i);
				showFlag = showFlags.get(i);
				if (mesgs.length() == 0) {//如果药品都有库存记录
					if (showFlag - flags.get(drugCode) == 1) {//申请药品单位为包装单位，药品信息为最小单位，则申请数量需乘以药品包装数量
						applyNum = applyNum * packNums.get(drugCode);
					}
					if (showFlag - flags.get(drugCode) == -1) {//申请药品单位为最小单位，药品信息为包装单位，则申请数量需除以药品包装数量
						applyNum = applyNum / packNums.get(drugCode);
					}
				}
				if (appInfoMap.containsKey(drugCode)) {//若之前已经遍历过了该药品，则增加该药品申请数量
					appInfoMap.put(drugCode, appInfoMap.get(drugCode) + applyNum);
				}else {//若之前没有遍历过该药品，则直接将该申请数量作为申请数量
					appInfoMap.put(drugCode, applyNum);
				}
			}
		}
		if (mesgs.length() > 0) {//如果存在药品在科室内没有库存记录情况
			map.put("valiuFlag", 0);
			String failMesgs = mesgs.toString();
			failMesgs = failMesgs.substring(0, failMesgs.length() - 1);//去掉最后一个空格
			map.put("failMesgs", failMesgs);
			return map;
		}
		//经过去重后的药品code
		Set<String> codes = appInfoMap.keySet();
		/**
		 * 判断状态是否可用
		 */
		if (isStatus) {//如果需要判断状态
			for (String code : codes) {
				if (delMap.get(code) - 1 == 0) {//判断是否删除
					mesgs.append(names.get(code) + "已被删除,");
					continue ;
				}
				if (stopMap.get(code) - 1 == 0) {//判断是否停用
					mesgs.append(names.get(code) + "已被停用,");
					continue ;
				}
				List<DrugCheckdetail> checkdetails = stockInfoDAO.getDrugChecks(code, deptCode);
				if (checkdetails != null && checkdetails.size() > 0) {//判断是否封账结存
					mesgs.append(names.get(code) + "已被封账盘点,");
				}
			}
			if (mesgs.length() > 0) {//如果存在药品在科室内没有库存记录情况
				map.put("valiuFlag", 0);
				String failMesgs = mesgs.toString();
				failMesgs = failMesgs.substring(0, failMesgs.length() - 1);//去掉最后一个空格
				map.put("failMesgs", failMesgs);
				return map;
			}
		}
		/**
		 * 若不需要考虑库存数量
		 */
		if(!isStock){//当不考虑库存时，药品可用
			return map;
		}
		
		/**
		 * 判断可用数量（考虑预扣库存）
		 */
		for (String code : codes) {
			store = storeNums.get(code) - preNums.get(code);//库存数量
			Double applySum = appInfoMap.get(code);//申请数量
			if (store - applySum >= 0) {
				continue ;
			}else {
				mesgs.append(names.get(code) + "库存不足,");
				continue ;
			}
		}
		if (mesgs.length() > 0) {//有药品不可用
			map.put("valiuFlag", 0);
			String failMesgs = mesgs.toString();
			failMesgs = failMesgs.substring(0, failMesgs.length() - 1);//去掉最后一个空格
			map.put("failMesgs", failMesgs);
		}
		return map;
	}
}
