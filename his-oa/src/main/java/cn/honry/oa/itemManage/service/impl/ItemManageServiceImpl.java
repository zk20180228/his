package cn.honry.oa.itemManage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.TmItems;
import cn.honry.oa.itemManage.dao.ItemManageDao;
import cn.honry.oa.itemManage.service.ItemManageService;
import cn.honry.utils.TreeJson;

@Service("itemManageService")
public class ItemManageServiceImpl implements ItemManageService {
	
	@Autowired
	@Qualifier(value = "itemManageDao")
	private ItemManageDao itemManageDao;

	@Override
	public TmItems get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(TmItems arg0) {
		
	}

	@Override
	public List<TmItems> getAllType() {
		return itemManageDao.getAllType();
	}

	@Override
	public List<TmItems> getItemsByType(Integer cid) {
		return itemManageDao.getItemsByType(cid);
	}

	@Override
	public List<TmItems> getItemsById(String id) {
		return itemManageDao.getItemsById(id);
	}

	@Override
	public void saveOrUpdateItem(TmItems tmItems) {
		if (StringUtils.isBlank(tmItems.getId())) {
			tmItems.setId(null);
			tmItems.setCreateTime(new Date());
		}else{
			tmItems.setUpdateTime(new Date());
		}
		
		if (StringUtils.isBlank(tmItems.getCode())) {
			String  maxCode = itemManageDao.getMaxCodeByType(tmItems.getType());
			Integer code = null;
			if (StringUtils.isNotBlank(maxCode)) {
				code = Integer.valueOf(maxCode) + 1;
			}else {
				code = 1;
			}
			tmItems.setCode(code+"");
		}
		itemManageDao.save(tmItems);
	}

	@Override
	public void saveOrUpdateItemType(TmItems tmItems) {
		if("ROOT".equals(tmItems.getParentId())){
			tmItems.setIsParent(1);
		}else{
			tmItems.setIsParent(2);
		}
		if (null == tmItems.getType()) {
			Integer  type = itemManageDao.getMaxType(tmItems.getType());
			if (null == type) {
				type = 0;
			}
			tmItems.setType(type + 1);
		}
		if (StringUtils.isBlank(tmItems.getCode())) {
			String  maxCode = itemManageDao.getMaxCodeByType(tmItems.getType());
			Integer code = null;
			if (StringUtils.isNotBlank(maxCode)) {
				code = Integer.valueOf(maxCode) + 1;
			}else {
				code = 1;
			}
			tmItems.setCode(code+"");
		}
		tmItems.setCreateTime(new Date());
		itemManageDao.save(tmItems);
	}

	@Override
	public void delItem(String ids) {
		String[] idArr = ids.split(",");
		for (String id : idArr) {
			itemManageDao.delItem(id);
		}
	}

	@Override
	public void delType(Integer type,String parentId,String id) {
		itemManageDao.delType(type,parentId,id);		
	}

	@Override
	public TmItems getTypeName(Integer type) {
		return itemManageDao.getTypeName(type);
	}

	@Override
	public List<TreeJson> queryAllType(String id) {
		List<TreeJson> trJsons = new ArrayList<TreeJson>();
		if(StringUtils.isBlank(id)){
			TreeJson json = new TreeJson();
			json.setId("ROOT");
			json.setText("模块类别");
			json.setState("open");
			Map<String, String> map55 = new HashMap<String,String>();
			map55.put("ids", "ROOT");
			map55.put("parentId", null);
			map55.put("path", null);
			map55.put("type", null);
			json.setAttributes(map55);
			//查询所有级别为1的
			List<TmItems> items1 = itemManageDao.queryItems("1",null,"ROOT");
			List<TreeJson> trJsons1 = new ArrayList<TreeJson>();
			for (int i = 0; i < items1.size(); i++) {
				TreeJson json1 = new TreeJson();
				json1.setId(items1.get(i).getType().toString());
				json1.setText(items1.get(i).getTypeName());
				Map<String, String> map = new HashMap<String,String>();
				map.put("ids", items1.get(i).getId());
				map.put("parentId", items1.get(i).getParentId());
				map.put("path", items1.get(i).getPath());
				map.put("type", items1.get(i).getType().toString());
				json1.setAttributes(map);
				if(items1.get(i).getPath()==null||"".equals(items1.get(i).getPath())){
					json1.setState("closed");
				}else{
					json1.setState("open");
				}
				
				List<TmItems> items2 = itemManageDao.queryItems("2",items1.get(i).getType().toString(),items1.get(i).getId());
				List<TreeJson> trJsons3 = new ArrayList<TreeJson>();
				for (int i1 = 0; i1 < items2.size(); i1++) {
					TreeJson json2 = new TreeJson();
					json2.setId(items2.get(i1).getType().toString());
					json2.setText(items2.get(i1).getName());
					Map<String, String> map1 = new HashMap<String,String>();
					map1.put("ids", items2.get(i1).getId());
					map1.put("parentId", items2.get(i1).getParentId());
					map1.put("path", items2.get(i1).getPath());
					map1.put("type", items2.get(i1).getType().toString());
					json2.setAttributes(map1);
					if(items2.get(i1).getPath()==null||"".equals(items2.get(i1).getPath())){
						json2.setState("closed");
					}else{
						json2.setState("open");
					}
					
					List<TmItems> items3 = itemManageDao.queryItems("3",items2.get(i1).getType().toString(),items2.get(i1).getId());
					List<TreeJson> trJsons2 = new ArrayList<TreeJson>();
					for (int j = 0; j < items3.size(); j++) {
						TreeJson json3 = new TreeJson();
						json3.setId(items3.get(j).getType().toString());
						json3.setText(items3.get(j).getName());
						Map<String, String> map2 = new HashMap<String,String>();
						map2.put("ids", items3.get(j).getId());
						map2.put("parentId", items3.get(j).getParentId());
						map2.put("path", items3.get(j).getPath());
						map2.put("type", items3.get(j).getType().toString());
						json3.setAttributes(map2);
						if(items3.get(j).getPath()==null||"".equals(items3.get(j).getPath())){
							json3.setState("closed");
						}else{
							json3.setState("open");
						}
						trJsons2.add(json3);
					}
					json2.setChildren(trJsons2);
					trJsons3.add(json2);
				}
				json1.setChildren(trJsons3);
				trJsons1.add(json1);
			}
			json.setChildren(trJsons1);
			trJsons.add(json);
		}else{
			
		}
		return trJsons;
	}

	@Override
	public List<TmItems> queryTmItems(String id, String parentId, String path) {
		return itemManageDao.queryTmItems(id, parentId, path);
	}
}
