package cn.honry.migrate.synDateManage.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.IDataSynch;
import cn.honry.migrate.synDateManage.dao.SynDateDao;
import cn.honry.migrate.synDateManage.service.SynDateService;
import cn.honry.utils.RedisUtil;
@Service("synDateService")
public class SynDateServiceImpl implements SynDateService {
	@Autowired
	@Qualifier(value="synDateDao")
	private SynDateDao synDateDao;
	
	public void setSynDateDao(SynDateDao synDateDao) {
		this.synDateDao = synDateDao;
	}
	public static final String COUNT = "COUNT";//查询数量Redis缓存域名
	public static final String SELATT = "SELATT";//查询表列属性语句Redis缓存域名
	public static final String SELREC = "SELREC";//查询表数据Redis缓存域名
	public static final String INSERT = "INSERT";//添加表数据Redis缓存域名
	public static final String UPDATE = "UPDATE";//修改表数据Redis缓存域名
	public static final String UPDINS = "UPDINS";//修改或添加表数据(合并语句)Redis缓存域名
	public static final String COLUMN = "COLUMN";//查询表列属性值Redis缓存域名
	public static final String SYNCHVO = "SYNCHVO";//查询迁移表对象信息Redis缓存域名
	public static final String PKMAP = "PKMAP";//查询迁移表主键信息Redis缓存域名
	public static final String REALTIME = "REALTIME";//查询迁移表最近一次更新时间信息Redis缓存域名
	@Autowired
	private RedisUtil redisUtil;
	@Override
	public List<IDataSynch> querySynDate(String queryCode, String rows,
			String page, String menuAlias,String dateState) {
		return synDateDao.querySynDate(queryCode, rows, page, menuAlias,dateState);
	}

	@Override
	public int querySynDateTotal(String queryCode, String menuAlias,String dateState) {
		return synDateDao.querySynDateTotal(queryCode, menuAlias,dateState);
	}

	@Override
	public void saveOrUpdateSynDate(IDataSynch entity) {
		IDataSynch iDataSynch = synDateDao.getOneDate(entity.getId());
		if(entity!=null&&StringUtils.isBlank(entity.getId())){//添加
			entity.setId(null);
			entity.setCode("00"+synDateDao.maxCode("i_data_synch", "code"));//获取序列
			synDateDao.save(entity);
		}else{
			synDateDao.save(entity);
			redisUtil.remove("SYNCH_"+iDataSynch.getServeCode());
			redisUtil.hDel("SYNCH_"+iDataSynch.getCode(), COUNT,SELATT,SELREC,INSERT,UPDATE,UPDINS,COLUMN,SYNCHVO);
		}
	}

	@Override
	public void delSynDate(String id) {
		id=id.replaceAll(".*([';]+|(--)+).*","");
		if(StringUtils.isNotBlank(id)){
			synDateDao.delSynDate(id);
		}
	}

	@Override
	public IDataSynch getOneDate(String id) {
		return synDateDao.getOneDate(id);
	}

	@Override
	public void updateState(String id, String state) throws Exception {
		if(StringUtils.isNotBlank(id)&&StringUtils.isNotBlank(state)){
			id=id.replaceAll(".*([';]+|(--)+).*","");
			state=state.replaceAll(".*([';]+|(--)+).*","");
			synDateDao.updateState(id, state);
			
		}else{
			 throw new Exception();
		}
	}

}
