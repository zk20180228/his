package cn.honry.migrate.outInterfaceManager.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.ExterInter;
import cn.honry.migrate.outInterfaceManager.dao.OutInterfaceManagerDao;
import cn.honry.migrate.outInterfaceManager.service.OutInterfaceManagerService;
import cn.honry.utils.RedisUtil;

@Service("outInterfaceManagerService")
@Transactional
public class OutInterfaceManagerServiceImpl implements OutInterfaceManagerService {
	
	@Autowired
	@Qualifier(value="outInterfaceManagerDao")
	private OutInterfaceManagerDao outInterfaceManagerDao;
	public void setOutInterfaceManagerDao(
			OutInterfaceManagerDao outInterfaceManagerDao) {
		this.outInterfaceManagerDao = outInterfaceManagerDao;
	}
	@Autowired
	private RedisUtil redisUtil;

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(ExterInter arg0) {
	}

	@Override
	public ExterInter get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdateExterInter(ExterInter exterInter) throws Exception {
		
		if (StringUtils.isBlank(exterInter.getId())) {//添加方法
			exterInter.setId(null);
			Integer i =  outInterfaceManagerDao.getMaxCode();
			String code = "0"+(i+1);
			exterInter.setCode(code);
		}
		outInterfaceManagerDao.save(exterInter);
		redisUtil.hset("company", exterInter.getFirmCode()+"-"+exterInter.getCode(),exterInter);
	}


	@Override
	public int getTotal(String queryCode, String menuAlias,String serviceState)  throws Exception{
		return  outInterfaceManagerDao.getTotal(queryCode,menuAlias,serviceState);
	}

	@Override
	public List<ExterInter> findAll(String queryCode, String menuAlias, String page, String rows,String serviceState)  throws Exception{
		return outInterfaceManagerDao.findAll(queryCode,menuAlias,page,rows,serviceState);
	}


	@Override
	public ExterInter get(Integer id) {
		return null;
	}


	@Override
	public ExterInter findById(String id) throws Exception{
		return outInterfaceManagerDao.findById(id);
	}


	@Override
	public void delInter(String ids) throws Exception{
		String[] split = ids.split(",");
		for (String s : split) {
			ExterInter exterInter = outInterfaceManagerDao.findById(s);
			outInterfaceManagerDao.delInter(s);
			redisUtil.hDel("company", exterInter.getFirmCode()+"-"+exterInter.getCode());
		}
	}


	@Override
	public ExterInter findByCode(String code) throws Exception {
		return outInterfaceManagerDao.findByCode(code);
	}


	@Override
	public void updateState(String id, String state) throws Exception {
		if(StringUtils.isNotBlank(id)&&StringUtils.isNotBlank(state)){
			id=id.replaceAll(".*([';]+|(--)+).*","");
			state=state.replaceAll(".*([';]+|(--)+).*","");
			outInterfaceManagerDao.updateState(id, state);
		}else{
			 throw new Exception("参数异常");
		}
		
		
	}


	@Override
	public List<ExterInter> getfireCode(String fireCode) {
		return outInterfaceManagerDao.getfireCode(fireCode);
	}


	@Override
	public Map<String, String> getfireCodeRender(String fireCode) {
		List<ExterInter> list=outInterfaceManagerDao.getfireCode(fireCode);
		Map<String,String> map=new HashMap<String,String>();
		for(ExterInter vo:list){
			map.put(vo.getCode(), vo.getName());
		}
		return map;
	}

}
