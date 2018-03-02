package cn.honry.outpatient.recipedetail.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessStack;
import cn.honry.base.bean.model.OutpatientRecipedetail;
import cn.honry.inner.baseinfo.stack.dao.StackInInterDAO;
import cn.honry.outpatient.recipedetail.dao.RecipedetailDAO;
import cn.honry.outpatient.recipedetail.service.RecipedetailService;
import cn.honry.utils.TreeJson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service("recipedetailService")
@Transactional
@SuppressWarnings({ "all" })
public class RecipedetailServiceImpl implements RecipedetailService{

	@Autowired
	@Qualifier(value = "recipedetailDAO")
	private RecipedetailDAO recipedetailDAO;
	@Autowired
	@Qualifier(value = "stackInInterDAO")
	private StackInInterDAO stackInInterDAO;
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public OutpatientRecipedetail get(String id) {
		return recipedetailDAO.get(id);
	}

	@Override
	public void saveOrUpdate(OutpatientRecipedetail entity) {
		
	}

	/**  
	 *  
	 * @Description：  分类组套树
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-7 上午10:42:41  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-7 上午10:42:41  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<TreeJson> getSortStackTree() throws Exception{
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
	
			//根节点
			TreeJson pTreeJson = new TreeJson();
			pTreeJson.setId("0");
			pTreeJson.setText("组套信息");
			treeJsonList.add(pTreeJson);
			//院级组套
			TreeJson yTreeJson = new TreeJson();
			yTreeJson.setId("1");
			yTreeJson.setText("全院级组套");
			Map<String,String> yAttMap = new HashMap<String, String>();
			yAttMap.put("pid", "0");
			yTreeJson.setAttributes(yAttMap);
			treeJsonList.add(yTreeJson);
			List<BusinessStack> yStackList = stackInInterDAO.getStackByType(4,null);
			if(yStackList!=null&&yStackList.size()>0){
				for(BusinessStack stack : yStackList){
					TreeJson ysTreeJson = new TreeJson();
					ysTreeJson.setId(stack.getId());
					ysTreeJson.setText(stack.getName());
					Map<String,String> ysAttMap = new HashMap<String, String>();
					ysAttMap.put("pid","1");
					ysAttMap.put("isNO","1");
					ysTreeJson.setAttributes(ysAttMap);
					treeJsonList.add(ysTreeJson);
				}
			}
			//部门级组套
			TreeJson bTreeJson = new TreeJson();
			bTreeJson.setId("2");
			bTreeJson.setText("部门级组套");
			Map<String,String> bAttMap = new HashMap<String, String>();
			bAttMap.put("pid", "0");
			bTreeJson.setAttributes(bAttMap);
			treeJsonList.add(bTreeJson);
			List<BusinessStack> bStackList = stackInInterDAO.getStackByType(2,null);
			if(bStackList!=null&&bStackList.size()>0){
				for(BusinessStack stack : bStackList){
					TreeJson bsTreeJson = new TreeJson();
					bsTreeJson.setId(stack.getId());
					bsTreeJson.setText(stack.getName());
					Map<String,String> bsAttMap = new HashMap<String, String>();
					bsAttMap.put("pid","2");
					bsAttMap.put("isNO","1");
					bsTreeJson.setAttributes(bsAttMap);
					treeJsonList.add(bsTreeJson);
				}
			}
			//个人级组套
			TreeJson gTreeJson = new TreeJson();
			gTreeJson.setId("3");
			gTreeJson.setText("个人级组套");
			Map<String,String> gAttMap = new HashMap<String, String>();
			gAttMap.put("pid", "0");
			gTreeJson.setAttributes(gAttMap);
			treeJsonList.add(gTreeJson);
			List<BusinessStack> gStackList = stackInInterDAO.getStackByType(3,null);
			if(gStackList!=null&&gStackList.size()>0){
				for(BusinessStack stack : gStackList){
					TreeJson gsTreeJson = new TreeJson();
					gsTreeJson.setId(stack.getId());
					gsTreeJson.setText(stack.getName());
					Map<String,String> gsAttMap = new HashMap<String, String>();
					gsAttMap.put("pid","3");
					gsAttMap.put("isNO","1");
					gsTreeJson.setAttributes(gsAttMap);
					treeJsonList.add(gsTreeJson);
				}
			}
		return treeJsonList;
	}

	/**  
	 *  
	 * @Description：  添加
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-13 下午05:27:56  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-13 下午05:27:56  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param recHerMedJson
	 *
	 */
	@Override
	public void saveRecipedetail(OutpatientRecipedetail rec,String recNonHerMedJson,String recHerMedJson) {
		Gson gson = new Gson();
		List<OutpatientRecipedetail> nonHerMedList = gson.fromJson(recNonHerMedJson, new TypeToken<List<OutpatientRecipedetail>>(){}.getType());
		for(OutpatientRecipedetail recipedetail : nonHerMedList){//非草药医嘱
			recipedetail.setClinicCode(rec.getClinicCode());
			recipedetail.setPatientNo(rec.getPatientNo());
			recipedetail.setRegDate(rec.getRegDate());
			recipedetail.setRegDept(rec.getRegDept());
			recipedetail.setOperDate(new Date());
			recipedetail.setCreateTime(new Date());
			recipedetailDAO.save(recipedetail);
		}
		List<OutpatientRecipedetail> herMedList = gson.fromJson(recHerMedJson, new TypeToken<List<OutpatientRecipedetail>>(){}.getType());
		for(OutpatientRecipedetail recipedetail : herMedList){//草药医嘱
			recipedetail.setClinicCode(rec.getClinicCode());
			recipedetail.setPatientNo(rec.getPatientNo());
			recipedetail.setRegDate(rec.getRegDate());
			recipedetail.setRegDept(rec.getRegDept());
			recipedetail.setOperDate(new Date());
			recipedetail.setCreateTime(new Date());
			recipedetailDAO.save(recipedetail);
		}
	}
	@Override
	public List query(String entity) {
		List lst =  recipedetailDAO.query(entity);
		return lst;
	}
	@Override
	public int getTotal(OutpatientRecipedetail entity) {
		int total = recipedetailDAO.getTotal(entity);
		return total;
	}
	@Override
	public List queryDate() {
		List lst =  recipedetailDAO.queryDate();
		return lst;
	}

}
