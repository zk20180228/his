package cn.honry.migrate.tableFiledType.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.TableFiledType;
import cn.honry.migrate.synDateManage.dao.SynDateDao;
import cn.honry.migrate.tableFiledType.dao.TableFieldDao;
import cn.honry.migrate.tableFiledType.service.TableFieldService;
@Service("tableFieldService")
public class TableFieldServiceImpl implements TableFieldService {
	@Autowired
	@Qualifier(value="tableFieldDao")
	private TableFieldDao tableFieldDao;
	
	public void setTableFieldDao(TableFieldDao tableFieldDao) {
		this.tableFieldDao = tableFieldDao;
	}

	@Autowired
	@Qualifier(value="synDateDao")
	private SynDateDao synDateDao;
	
	public void setSynDateDao(SynDateDao synDateDao) {
		this.synDateDao = synDateDao;
	}

	@Override
	public List<TableFiledType> queryTableField(String queryCode, String rows,
			String page, String menuAlias) {
		return tableFieldDao.queryTableField(queryCode, rows, page, menuAlias);
	}

	@Override
	public int queryTableFieldTotal(String queryCode, String menuAlias) {
		return tableFieldDao.queryTableFieldTotal(queryCode, menuAlias);
	}

	@Override
	public void saveOrUpdateTableField(TableFiledType entity) {
		if(entity!=null&&StringUtils.isBlank(entity.getId())){//添加
			entity.setId(null);
			entity.setCode("00"+synDateDao.maxCode("I_TABLE_FIELDTYPE", "code"));
		}
		tableFieldDao.save(entity);
	}

	@Override
	public void delTableField(String id) {
		id=id.replaceAll(".*([';]+|(--)+).*","");
		if(StringUtils.isNotBlank(id)){
			tableFieldDao.delTableField(id);
		}
	}

	@Override
	public TableFiledType getOneDate(String id) {
		return tableFieldDao.getOneDate(id);
	}

//	@Override
//	public void updateState(String id, String state) throws Exception {
//		if(StringUtils.isNotBlank(id)&&StringUtils.isNotBlank(state)){
//			id=id.replaceAll(".*([';]+|(--)+).*","");
//			state=state.replaceAll(".*([';]+|(--)+).*","");
//			tableFieldDao.updateState(id, state);
//			
//		}else{
//			 throw new Exception();
//		}
//	}

}
