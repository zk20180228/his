package cn.honry.mobile.whiteList.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.MBlackList;
import cn.honry.base.bean.model.MMachineManage;
import cn.honry.base.bean.model.MSysOperateLog;
import cn.honry.base.bean.model.MWhiteList;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.mobile.blackList.dao.BlackListDao;
import cn.honry.mobile.machineManage.dao.MachineManageDao;
import cn.honry.mobile.moOperateLog.dao.MoOperateLogDao;
import cn.honry.mobile.whiteList.dao.WhiteListDao;
import cn.honry.mobile.whiteList.service.WhiteListService;
import cn.honry.utils.ShiroSessionUtils;

@Service("whiteListService")
public class WhiteListServiceImpl implements WhiteListService{

	@Resource
	private WhiteListDao whiteListDao;
	
	@Resource
	private BlackListDao blackListDao;

	@Resource
	private MoOperateLogDao moOperateLogDao;

	@Resource
	private MachineManageDao machineManageDao;
	
	@Resource
	private ParameterInnerDAO parameterInnerDAO;
	
	@Override
	public List<MWhiteList> getPagedWhiteList(String rows, String page, MWhiteList mWhiteList) throws Exception {
		return whiteListDao.getList(rows, page, mWhiteList);
	}
	
	@Override
	public Integer getCount(MWhiteList mWhiteList) throws Exception {
		return whiteListDao.getTotal(mWhiteList);
	}

	@Override
	public void delWhiteLists(String ids) throws Exception {
		String name = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String[] id = ids.split(",");
		//删除设备管理
		List<String> list=Arrays.asList(id);
		for(int i=0;i<list.size();i++){
			MWhiteList mWhite =whiteListDao.get(list.get(i));
			if(mWhite!=null){
				MMachineManage mac=new MMachineManage();
				mac.setUser_account(mWhite.getUser_account());
				mac.setMachine_code(mWhite.getMachine_code());
				mac.setIs_white(2);
				mac.setIs_black(1);
				mac.setDeleteTime(new Date());
				mac.setDeleteUser(name);
				machineManageDao.delMacByAccount(mac);
				moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","白名单管理",account,"UPDATE","M_MACHINE_MANAGE",ids,""));
				//删除白名单
				whiteListDao.delWhiteById(mWhite);
				moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","白名单管理",account,"UPDATE","M_WHITE_LIST",ids,""));
			}
		}
		
	}

	@Override
	public void saveOrUpdate(MWhiteList mWhiteList,String flg) throws Exception {
		String name = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if(StringUtils.isNotBlank(mWhiteList.getId())){
			MWhiteList mWhite = whiteListDao.get(mWhiteList.getId());
			//获取设备管理信息
			if(!"1".equals(flg)){
				MMachineManage mac=new MMachineManage();
				mac.setUser_account(mWhite.getUser_account());
				mac.setMachine_code(mWhite.getMachine_code());
				MMachineManage mach=machineManageDao.getMacByAccountAndMach(mac);
				//更新设备管理信息
				if(mach!=null){
					mach.setUser_account(mWhiteList.getUser_account());
					mach.setMachine_code(mWhiteList.getMachine_code());
					mach.setUpdateUser(name);
					mach.setUpdateTime(new Date());
					machineManageDao.update(mach);
				}
			}
			//白名单更新
			mWhite.setUser_account(mWhiteList.getUser_account());
			mWhite.setMachine_code(mWhiteList.getMachine_code().replace("，",","));
			mWhite.setUpdateUser(name);
			mWhite.setUpdateTime(new Date());
			whiteListDao.update(mWhite);
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("更新","白名单管理",account,"UPDATE","M_WHITE_LIST",mWhiteList.getId(),""));
			
		}else{
			//白名单添加
			mWhiteList.setId(null);
			mWhiteList.setCreateTime(new Date());
			mWhiteList.setMachine_code(mWhiteList.getMachine_code().replace("，",","));
			mWhiteList.setCreateUser(name);
			mWhiteList.setDel_flg(0);
			mWhiteList.setStop_flg(0);
			whiteListDao.save(mWhiteList);
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("添加","白名单管理",account,"INSERT INTO","M_WHITE_LIST",mWhiteList.getId(),""));
		
		}
		if("1".equals(flg)){
			//删除黑名单信息
			MBlackList mBlackList=new MBlackList();
			mBlackList.setUser_account(mWhiteList.getUser_account());
			mBlackList.setMachine_code(mWhiteList.getMachine_code().replace("，",","));
			blackListDao.delBlackByUserAccount(mBlackList);
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","黑名单管理",account,"UPDATE","M_BLACK_LIST",mWhiteList.getUser_account()+"-"+mWhiteList.getMachine_code().replace("，",","),""));
		
			//更新设备管理信息
			MMachineManage mac=new MMachineManage();
			mac.setUser_account(mWhiteList.getUser_account());
			mac.setMachine_code(mWhiteList.getMachine_code());
			mac.setIs_white(1);
			mac.setIs_black(2);
			mac.setUpdateUser(name);
			mac.setUpdateTime(new Date());
			machineManageDao.updateWOrB(mac);
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","白名单管理",account,"UPDATE","M_MACHINE_MANAGE",mWhiteList.getUser_account()+"-"+mWhiteList.getMachine_code().replace("，",","),""));
		}
	}

	@Override
	public MWhiteList findWhiteByUserAccunt(String userAccunt, String machineCode) throws Exception {
		MWhiteList mWhiteList = new MWhiteList();
		mWhiteList.setUser_account(userAccunt);
		mWhiteList.setMachine_code(machineCode);
		return whiteListDao.findWhiteByUserAccunt(mWhiteList);
	}

	@Override
	public void moveBlack(String ids) throws Exception {
		String name = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		List<String> list=Arrays.asList(ids.split(","));
		//插入黑名单信息
		for(int i=0;i<list.size();i++){
			MWhiteList mWhite =whiteListDao.get(list.get(i));
			MBlackList mBlackList=new MBlackList();
			mBlackList.setId(null);
			mBlackList.setUser_account(mWhite.getUser_account());
			mBlackList.setMachine_code(mWhite.getMachine_code());
			mBlackList.setCreateTime(new Date());
			mBlackList.setCreateUser(name);
			mBlackList.setDel_flg(0);
			mBlackList.setStop_flg(0);
			blackListDao.save(mBlackList);
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("添加","白名单管理",account,"INSERT INTO","M_BLACK_LIST",mBlackList.getId(),""));
			
			if(mWhite!=null){
				MMachineManage mac=new MMachineManage();
				mac.setUser_account(mWhite.getUser_account());
				mac.setMachine_code(mWhite.getMachine_code());
				mac.setIs_white(2);
				mac.setIs_black(1);
				mac.setUpdateUser(name);
				mac.setUpdateTime(new Date());
				machineManageDao.updateWOrB(mac);
				moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","白名单管理",account,"UPDATE","M_MACHINE_MANAGE",mWhite.getUser_account(),""));
			}
		}
		//删除白名单信息
		delWhiteListData(ids);
		
	}

	@Override
	public MWhiteList getMWhiteById(String id) {
		if (StringUtils.isNotBlank(id)) {
			 return whiteListDao.getMWhiteById(id);
		}
		return new MWhiteList();
	}
	
	public void delWhiteListData(String ids) throws Exception {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String[] id = ids.split(",");
		for (String string : id) {
			MWhiteList mWhiteList = whiteListDao.get(string);
			whiteListDao.delWhiteById(mWhiteList);
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","白名单管理",account,"UPDATE","M_WHITE_LIST",string,""));
		}
		//删除白名单
	}

	@Override
	public MWhiteList get(String arg0) {
		return whiteListDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(MWhiteList arg0) {
		if (StringUtils.isNotBlank(arg0.getId())) {
			whiteListDao.update(arg0);
		}else {
			whiteListDao.save(arg0);
		}
	}

}
