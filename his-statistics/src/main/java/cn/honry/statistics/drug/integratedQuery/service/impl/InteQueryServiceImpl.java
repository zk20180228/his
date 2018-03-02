package cn.honry.statistics.drug.integratedQuery.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugAdjustPriceInfo;
import cn.honry.base.bean.model.DrugCheckdetail;
import cn.honry.base.bean.model.DrugInStore;
import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.bean.model.DrugOutstoreNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.statistics.drug.integratedQuery.dao.InteQueryDao;
import cn.honry.statistics.drug.integratedQuery.service.InteQueryService;
import cn.honry.statistics.drug.integratedQuery.vo.InteQueryVO;
import cn.honry.utils.MyBeanUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.TreeJson;
/**  
 * 药房药库综合查询Service实现类
 * @Author:luyanshou  
 * @version 1.0
 */
@Service("inteQueryService")
@Transactional
@SuppressWarnings({"all"})
public class InteQueryServiceImpl implements InteQueryService {

	private InteQueryDao inteQueryDao;
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	@Autowired
	@Qualifier(value="inteQueryDao")
	public void setInteQueryDao(InteQueryDao inteQueryDao) {
		this.inteQueryDao = inteQueryDao;
	}
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	/**
	 * 获取药房药库列表
	 * @throws Exception 
	 */
	public List<TreeJson> getDrugStore() throws Exception{
		List<TreeJson>  treeJsonList = new ArrayList<TreeJson>();
		TreeJson json1=new TreeJson();
		json1.setId("0");
		json1.setText("药房药库");
		json1.setIconCls("icon-branch");
		treeJsonList.add(json1);
		List<Object[]> list = inteQueryDao.getDrugStore();
		for (Object[] obj : list) {
			TreeJson json2=new TreeJson();
			json2.setId(obj[0].toString());
			json2.setText(obj[1].toString());
			json2.setIconCls("icon-user_brown");
			Map<String,String> attributes =new HashMap<String,String>();
			attributes.put("pid", "0");
			json2.setAttributes(attributes);
			treeJsonList.add(json2);
		}
		return TreeJson.formatTree(treeJsonList);
	}
	
	/**
	 * 获取分页数据
	 * @param vo 查询条件 
	 * @param kind 汇总类型(0-按药品;1-按单据;2-按部门)
	 * @param type 查询类别(0-入库;1-出库;2-盘点;3-调价)
	 * @param firstResult 起始位置
	 * @param maxResults 每页显示的记录数
	 * @throws Exception 
	 */
	public Map getListByPage(InteQueryVO vo,int kind, int type,int firstResult,int maxResults) throws Exception{
		Map map = new HashMap(2);
//		long count = inteQueryDao.getCount(vo, kind, type);//统计记录数
		List list = inteQueryDao.getListByPage(vo, kind, type, firstResult, maxResults);
		if(type==0){
			List<DrugInStore> list0=(List<DrugInStore>)list;
			if(kind==1){//按单据汇总
				Map<String,Double> m= new HashMap();
				Map<String,Double> m2= new HashMap();
				List<DrugInStore> li=new ArrayList();
				for (DrugInStore in : list0) {
					String inListCode = in.getInListCode();//入库单号
					Double retailCost = in.getRetailCost();//零售金额
					Double purchaseCost = in.getPurchaseCost();//入库总金额
					Double p = m2.get(inListCode);
					Double d = m.get(inListCode);
					if(d==null){
						d=0d;
						p=0d;
						DrugInStore inStore = new DrugInStore();
						MyBeanUtils.copyPropertiesButNull(inStore, in);
						li.add(inStore);
					}
					d+=retailCost;
					p+=purchaseCost;
					m.put(inListCode, d);
					m2.put(inListCode, p);
				}
				for (DrugInStore dru : li) {
					String inListCode = dru.getInListCode();
					Double d = m.get(inListCode);
					Double p = m2.get(inListCode);
					dru.setRetailCost(d);
					dru.setPurchaseCost(p);
				} 
				map.put("total", li.size());
				map.put("rows",  li);
				return map;
			}
		}
		if(type==1){
			List<DrugOutstoreNow> list0=(List<DrugOutstoreNow>)list;
			if(kind==2){//按部门汇总
				Map<String,Double> m=new HashMap();
				List<DrugOutstoreNow> li=new ArrayList();
				for (DrugOutstoreNow out : list0) {
					String deptCode = out.getDrugDeptCode();//出库科室编码
					String storageCode = out.getDrugStorageCode();//领药科室编码
					Double outNum = out.getOutNum();//出库数量
					Double price = out.getPurchasePrice();//购入价格
					Double d = m.get(storageCode+deptCode);
					if(d==null){
						d=0d;
						DrugOutstoreNow outstore = new DrugOutstoreNow();
						MyBeanUtils.copyPropertiesButNull(outstore, out);
						li.add(outstore);
					}
					d+=(outNum*price);
					m.put(storageCode+deptCode, d);
				}
				for (DrugOutstoreNow dru : li) {
					String deptCode = dru.getDrugDeptCode();//出库科室编码
					String storageCode = dru.getDrugStorageCode();//领药科室编码
					Double sum = m.get(storageCode+deptCode);
					dru.setApplyNum(sum);
				}
				map.put("total", li.size());
				map.put("rows",  li);
				return map;
			}
		}
		if(type==2){
			List<DrugCheckdetail> list0=(List<DrugCheckdetail>)list;
		}
		if(type==3){
			List<DrugAdjustPriceInfo> list0=(List<DrugAdjustPriceInfo>)list;
		}
		String redKey;
		if(vo!=null){
			redKey= "YFYKZHCX:"+vo.getBeginTime()+"_"+vo.getEndTime()+"_"+vo.getDrugCode()+"_"+vo.getDrugDeptCode()+"_"+kind+"_"+type;

		}else{
			redKey= "YFYKZHCX:"+kind+"_"+type;
		}
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum==null){
			totalNum =inteQueryDao.getCount(vo, kind, type);//统计记录数
			redisUtil.set(redKey, totalNum);
		}
		String val = parameterInnerDAO.getParameterByCode("deadTime");
		if(StringUtils.isNotBlank(val)){
			redisUtil.expire(redKey,Integer.valueOf(val));
		}else{
			redisUtil.expire(redKey, 300);
		}
		map.put("total", totalNum);
		map.put("rows",  list);
		return map;
	}
	
	/**
	 * 获取列字段
	 * @param kind 汇总类型(0-按药品;1-按单据;2-按部门)
	 * @param type 查询类别(0-入库;1-出库;2-盘点;3-调价)
	 */
	public String[] getfields(int kind,int type){
		String k=""+kind+type;
		String[] fields=null;
		switch (k) {
		case "00":
			fields=new String[]{"inState","tradeName","specs","inNum","packUnit","storeNum","retailPrice","retailCost",
					"purchasePrice","purchaseCost","lpx"};
			break;
		case "01":
			fields=new String[]{"outState","tradeName","specs","num","packUnit","storeNum","placeCode",
					"storeCost","packQty","packUnit1"};
			break;
		case "02":
			fields=new String[]{"checkState","tradeName","specs","packUnit","fstoreNum","adjustNum","cstoreNum","profitFlag","profitLossNum"};
			break;
		case "03":
			fields=new String[]{"currentState","tradeName","specs","inureTime","preRetailPrice","preWholesalePrice",
					"retailPrice","wholesalePrice","storeSum","packUnit","checkUser","checkTime","remark"};
			break;
		case "10":
			fields=new String[]{"inListCode","inState","payState","retailCost","purchaseCost"};
			break;
		case "11":
			fields=new String[]{"outBillCode","outState","drugDeptCode","drugStorageCode"};
			break;
		case "12":
			fields=new String[]{"checkCode","checkState","tradeName","profitFlag","profitLossNum"};
			break;
		case "13":
			fields=new String[]{"adjustBillCode","currentState","inureTime","checkUser","checkTime"};
			break;
		case "20":
			fields=new String[]{"inState","payState","companyCode","retailCost","purchaseCost","lpx"};
			break;
		case "21":
			fields=new String[]{"drugStorageCode","sum","drugDeptCode"};
			break;
		case "22":
			fields=new String[]{"drugDeptCode","tradeName","profitFlag","profitLossNum"};
			break;
		case "23":
			fields=new String[]{"currentState","tradeName","drugDeptCode","specs","inureTime","preRetailPrice","preWholesalePrice",
					"retailPrice","wholesalePrice","storeSum","proloSum","packUnit","checkUser","checkTime"};
			break;

		default:
			break;
		}
		return fields;
	}
	
	/**
	 * 获取字段名称
	 * @param kind 汇总类型(0-按药品;1-按单据;2-按部门)
	 * @param type 查询类别(0-入库;1-出库;2-盘点;3-调价)
	 */
	public Map<String,String> getName(int kind, int type){
		Map<String,String> map= new HashMap();
		map.put("inState", "当前状态");
		map.put("outState", "当前状态");
		map.put("tradeName", "名称");
		map.put("specs", "规格");
		map.put("inNum", "入库数量");
		map.put("storeNum", "库存数量");
		map.put("packUnit", "单位");
		map.put("packUnit1", "包装单位");
		map.put("retailPrice", "零售价");
		map.put("retailCost", "零售总金额");
		map.put("purchasePrice", "入库单价");
		map.put("purchaseCost", "入库总金额");
		map.put("lpx", "零批差");
		map.put("packQty","包装数量");
		map.put("num", "数量");
		map.put("placeCode", "货位号");
		map.put("storeCost", "库存金额");
		map.put("checkState", "盘点状态");
		map.put("fstoreNum", "封帐库存数量");
		map.put("adjustNum", "实际盘存数量");
		map.put("cstoreNum", "结存库存数量");
		map.put("profitFlag", "盈亏状态");
		map.put("profitLossNum", "盈亏数量");
		map.put("currentState", "当前状态");
		map.put("inureTime", "执行时间");
		map.put("preRetailPrice", "原零售价");
		map.put("preWholesalePrice", "原批发价");
		map.put("storeSum", "调价时库存量");
		map.put("wholesalePrice", "调价后批发价");
		map.put("checkUser", "操作员");
		map.put("checkTime", "操作时间");
		map.put("remark", "备注");
		map.put("inListCode", "单据号");
		map.put("payState", "付款状态");
		map.put("outBillCode", "出库单据号");
		map.put("drugDeptCode", "出库科室");
		map.put("drugStorageCode", "领药科室");
		map.put("checkCode", "单据号");
		map.put("adjustBillCode", "单据号");
		map.put("companyCode", "供货公司");
		map.put("sum", "金额");
		map.put("proloSum", "盈亏金额");
		return map;
	}
	
	/**
	 * 获取公司id和名称
	 * @throws Exception 
	 */
	public List<Map<String,String>> getCompanyName() throws Exception{
		List<Map<String,String>> li=new ArrayList();
		 List<Object[]> list = inteQueryDao.getCompanyName();
		 for (Object[] object : list) {
			 Map<String,String> map =new HashMap();
			map.put("companyId", object[0].toString());
			map.put("companyName", object[1].toString());
			li.add(map);
		}
		 return li;
	}
	
	/**
	 * 查询科室信息
	 * @throws Exception 
	 */
	public List<SysDepartment> getDeptIdName() throws Exception{
		return inteQueryDao.getDept();
	}
}
