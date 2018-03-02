package cn.honry.mobile.blackList.service.impl;

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
import cn.honry.mobile.blackList.service.BlackListService;
import cn.honry.mobile.machineManage.dao.MachineManageDao;
import cn.honry.mobile.moOperateLog.dao.MoOperateLogDao;
import cn.honry.mobile.whiteList.dao.WhiteListDao;
import cn.honry.utils.ShiroSessionUtils;

@Service("blackListService")
public class BlackListServiceImpl implements BlackListService{

	@Resource
	private BlackListDao blackListDao;
	
	@Resource
	private WhiteListDao whiteListDao;
	
	@Resource
	private ParameterInnerDAO parameterInnerDAO;
	
	@Resource
	private MoOperateLogDao moOperateLogDao;
	
	@Resource
	private MachineManageDao machineManageDao;

	@Override
	public List<MBlackList> getPagedBlackList(String rows, String page, MBlackList mBlackList) throws Exception {
		return blackListDao.getList(rows, page, mBlackList);
	}
	
	@Override
	public Integer getCount(MBlackList mBlackList) throws Exception {
		return blackListDao.getTotal(mBlackList);
	}

	@Override
	public void delBlackLists(String ids) throws Exception {
		String name = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String[] id = ids.split(",");
		//删除设备管理
		List<String> list=Arrays.asList(id);
		for(int i=0;i<list.size();i++){
			MBlackList mBlack =blackListDao.get(list.get(i));
			if(mBlack!=null){
				MMachineManage mac=new MMachineManage();
				mac.setUser_account(mBlack.getUser_account());
				mac.setMachine_code(mBlack.getMachine_code());
				mac.setIs_white(1);
				mac.setIs_black(2);
				mac.setDeleteTime(new Date());
				mac.setDeleteUser(name);
				machineManageDao.delMacByAccount(mac);
				moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","黑名单管理",account,"UPDATE","M_MACHINE_MANAGE",list.get(i),""));
			}
			blackListDao.delBlackById(mBlack);
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","黑名单管理",account,"UPDATE","M_BLACK_LIST",list.get(i),""));
		}
	}


	@Override
	public void saveOrUpdate(MBlackList mBlackList,String flg) throws Exception {
		String name = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if(StringUtils.isNotBlank(mBlackList.getId())){
			MBlackList mBlack =blackListDao.get(mBlackList.getId());
			
			//获取设备管理信息
			if(!"1".equals(flg)){
				MMachineManage mac=new MMachineManage();
				mac.setUser_account(mBlack.getUser_account());
				mac.setMachine_code(mBlack.getMachine_code());
				MMachineManage mach=machineManageDao.getMacByAccountAndMach(mac);
				//更新设备管理信息
				if(mach!=null){
					mach.setUser_account(mBlackList.getUser_account());
					mach.setMachine_code(mBlackList.getMachine_code());
					mach.setUpdateUser(name);
					mach.setUpdateTime(new Date());
					machineManageDao.update(mach);
				}
			}
			
			mBlack.setUser_account(mBlackList.getUser_account());
			mBlack.setMachine_code(mBlackList.getMachine_code().replace("，",","));
			mBlack.setUpdateUser(name);
			mBlack.setUpdateTime(new Date());
			blackListDao.update(mBlack);
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("修改","黑名单管理",account,"UPDATE","M_BLACK_LIST",mBlackList.getId(),""));
			
		}else{
			mBlackList.setId(null);
			mBlackList.setCreateTime(new Date());
			mBlackList.setCreateUser(name);
			mBlackList.setMachine_code(mBlackList.getMachine_code().replace("，",","));
			mBlackList.setUser_account(mBlackList.getUser_account());
			mBlackList.setDel_flg(0);
			mBlackList.setStop_flg(0);
			blackListDao.save(mBlackList);
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("添加","黑名单管理",account,"INSERT INTO","M_BLACK_LIST",mBlackList.getId(),""));
		}
		
		if("1".equals(flg)){
			//删除白名单信息
			MWhiteList mWhiteList=new MWhiteList();
			mWhiteList.setUser_account(mBlackList.getUser_account());
			mWhiteList.setMachine_code(mBlackList.getMachine_code().replace("，",","));
			whiteListDao.delWhiteByUserAccount(mWhiteList);
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","黑名单管理",account,"UPDATE","M_WHITE_LIST",mBlackList.getUser_account()+"-"+mBlackList.getMachine_code(),""));
			
			//级联更新设备表信息
			MMachineManage mac=new MMachineManage();
			mac.setUser_account(mBlackList.getUser_account());
			mac.setMachine_code(mBlackList.getMachine_code());
			mac.setIs_white(2);
			mac.setIs_black(1);
			mac.setUpdateUser(name);
			mac.setUpdateTime(new Date());
			machineManageDao.updateWOrB(mac);
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","黑名单管理",account,"UPDATE","M_MACHINE_MANAGE",mBlackList.getUser_account()+"_"+mBlackList.getMachine_code(),""));
		}
		
	}

	@Override
	public MBlackList findBlackByUserAccunt(String userAccunt, String machineCode) throws Exception {
		MBlackList mBlackList = new MBlackList();
		mBlackList.setUser_account(userAccunt);
		mBlackList.setMachine_code(machineCode);
		return blackListDao.findBlackByUserAccunt(mBlackList);
	}

	@Override
	public void moveWhite(String ids) throws Exception {
		String name = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		List<String> list=Arrays.asList(ids.split(","));
		//插入白名单信息
		for(int i=0;i<list.size();i++){
			MBlackList mBlack =blackListDao.get(list.get(i));
			if(mBlack != null){
				MWhiteList mWhiteList=new MWhiteList();
				mWhiteList.setUser_account(mBlack.getUser_account());
				mWhiteList.setMachine_code(mBlack.getMachine_code());
				mWhiteList.setCreateTime(new Date());
				mWhiteList.setCreateUser(name);
				mWhiteList.setDel_flg(0);
				mWhiteList.setStop_flg(0);
				whiteListDao.save(mWhiteList);
				moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("添加","黑名单管理",account,"INSERT INTO","M_WHITE_LIST",mWhiteList.getId(),""));
			}
			
			if(mBlack!=null){
				MMachineManage mac=new MMachineManage();
				mac.setUser_account(mBlack.getUser_account());
				mac.setMachine_code(mBlack.getMachine_code());
				mac.setIs_white(1);
				mac.setIs_black(2);
				mac.setUpdateUser(name);
				mac.setUpdateTime(new Date());
				machineManageDao.updateWOrB(mac);
				moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","黑名单管理",account,"UPDATE","M_MACHINE_MANAGE",mBlack.getUser_account(),""));
			}
			
		}
		//删除黑名单信息
		delBlackListDate(ids);
	}

	@Override
	public MBlackList getMBlackById(String id) {
		if (StringUtils.isNotBlank(id)) {
			 return blackListDao.getMBlackById(id);
		}
		return new MBlackList();
	}
	
	
	public void delBlackListDate(String ids) throws Exception {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String[] id = ids.split(",");
		for (String string : id) {
			MBlackList mBlackList = blackListDao.get(string);
			if(mBlackList != null){
				blackListDao.delBlackById(mBlackList);
				moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","黑名单管理",account,"UPDATE","M_BLACK_LIST",string,""));
			}
		}
	}

	@Override
	public MBlackList get(String arg0) {
		return blackListDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(MBlackList arg0) {
		if (StringUtils.isNotBlank(arg0.getId())) {
			blackListDao.update(arg0);
		}else{
			arg0.setId(null);
			blackListDao.save(arg0);
		}
		
	}

	@Override
	public List<MMachineManage> checkIsLost(String userAccount) {
		return machineManageDao.checkIsLost(userAccount);
	}

}
