package cn.honry.mobile.machineManage.service.impl;

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
import cn.honry.mobile.machineManage.service.MachineManageService;
import cn.honry.mobile.moOperateLog.dao.MoOperateLogDao;
import cn.honry.mobile.whiteList.dao.WhiteListDao;
import cn.honry.utils.ShiroSessionUtils;
@Service("machineManageService")
public class MachineManageServiceImpl implements MachineManageService{

	@Resource
	private MachineManageDao machineManageDao;
	
	@Resource
	private WhiteListDao whiteListDao;
	
	@Resource
	private BlackListDao blackListDao;
	
	@Resource
	private MoOperateLogDao moOperateLogDao;
	
	@Resource
	private ParameterInnerDAO parameterInnerDAO;
	
	@Override
	public List<MMachineManage> getPagedMachineList(String page, String rows, MMachineManage machineManage) throws Exception {
		return machineManageDao.getList(page, rows, machineManage);
	}

	@Override
	public Integer getCount(MMachineManage machineManage) throws Exception {
		return machineManageDao.getTotal(machineManage);
	}
	
	@Override
	public void delMachines(String ids) throws Exception {
		String name = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String[] id = ids.split(",");
		List<String> list = Arrays.asList(id);
		for(int i = 0;i<list.size();i++){
			MMachineManage mac = machineManageDao.get(list.get(i));
			if(mac.getIs_white() == 2){
				MWhiteList mWhit = new MWhiteList();
				mWhit.setUser_account(mac.getUser_account());
				mWhit.setMachine_code(mac.getMachine_code());
				MWhiteList mWhiteList = whiteListDao.findWhiteByUserAccunt(mWhit);
				if(mWhiteList != null){
					whiteListDao.delWhiteById(mWhiteList);
					moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","设备管理",account,"UPDATE","M_WHITE_LIST",mWhiteList.getId(),""));
				}
			}
			
			if(mac.getIs_black() == 2){
				MBlackList mBlack = new MBlackList();
				mBlack.setMachine_code(mac.getMachine_code());
				mBlack.setUser_account(mac.getUser_account());
				MBlackList mBlackList = blackListDao.findBlackByUserAccunt(mBlack);
				if(mBlackList != null){
					blackListDao.delBlackById(mBlackList);
					moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","设备管理",account,"UPDATE","M_BLACK_LIST",mBlackList.getId(),""));
				}
				
			}
			mac.setDeleteTime(new Date());
			mac.setDeleteUser(name);
			mac.setDel_flg(1);
			machineManageDao.update(mac);
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","设备管理",account,"UPDATE","M_MACHINE_MANAGE",list.get(i),""));
		}
	}


	@Override
	public void save(MMachineManage mMachineManage) throws Exception{
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String name = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		if(StringUtils.isNotBlank(mMachineManage.getId())){
			MMachineManage machine  = machineManageDao.get(mMachineManage.getId());
			if(machine.getIs_white()==2){
				//级联更新白名单
				MWhiteList mWhite = new MWhiteList();
				mWhite.setUser_account(machine.getUser_account());
				mWhite.setMachine_code(machine.getMachine_code());
				MWhiteList mWhiteList=whiteListDao.findWhiteByUserAccunt(mWhite);
				mWhiteList.setUser_account(mMachineManage.getUser_account());
				mWhiteList.setMachine_code(mMachineManage.getMachine_mobile().replace("，", ","));
				mWhiteList.setUpdateUser(name);
				mWhiteList.setUpdateTime(new Date());
				whiteListDao.update(mWhiteList);
			}else{
				//级联更新黑名单
				MBlackList mBlack=new MBlackList();
				mBlack.setUser_account(machine.getUser_account());
				mBlack.setMachine_code(machine.getMachine_code());
				MBlackList mBlackList=blackListDao.findBlackByUserAccunt(mBlack);
				mBlackList.setUser_account(mMachineManage.getUser_account());
				mBlackList.setMachine_code(mMachineManage.getMachine_mobile().replace("，", ","));
				mBlackList.setUpdateUser(name);
				mBlackList.setUpdateTime(new Date());
				blackListDao.update(mBlackList);
			}
			machine.setMachine_code(mMachineManage.getMachine_code().replace("，", ","));
			machine.setMachine_mobile(mMachineManage.getMachine_mobile().replace("，", ","));
			machine.setUser_account(mMachineManage.getUser_account());
			machine.setUpdateUser(name);
			machine.setUpdateTime(new Date());
			machineManageDao.update(machine);
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("更新","设备管理",account,"UPDATE","M_MACHINE_MANAGE","",""));
			
			
		}else{
			mMachineManage.setId(null);
			mMachineManage.setIs_lost(1);
			mMachineManage.setIs_white(2);
			mMachineManage.setIs_black(1);
			mMachineManage.setCreateTime(new Date());
			mMachineManage.setCreateUser(name);
			mMachineManage.setDel_flg(0);
			mMachineManage.setStop_flg(0);
			machineManageDao.save(mMachineManage);
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("添加","设备管理",account,"INSERT INTO","M_MACHINE_MANAGE","",""));
			//插入到白名单
			MWhiteList mWhiteList = new MWhiteList();
			mWhiteList.setId(null);
			mWhiteList.setUser_account(mMachineManage.getUser_account());
			mWhiteList.setMachine_code(mMachineManage.getMachine_code().replace("，",","));
			mWhiteList.setCreateTime(new Date());
			mWhiteList.setCreateUser(name);
			mWhiteList.setDel_flg(0);
			mWhiteList.setStop_flg(0);
			whiteListDao.save(mWhiteList);
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("添加","设备管理",account,"INSERT INTO","M_WHITE_LIST",null,""));
		}
		
	}

	@Override
	public List<MMachineManage> findMachineByUserAccunt(String userAccunt) throws Exception {
		return machineManageDao.getMachinesByUserAccunt(userAccunt);
	}

	@Override
	public void updateLossOrActivate(String ids,String flg,String userAndMach) throws Exception {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String name = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		String[] id = ids.split(",");
		String[] machine=userAndMach.split(";");
		for(int i = 0; i < machine.length; i++){
			String[] machineMes=machine[i].split("_");
			if(StringUtils.isNotBlank(flg)&&"1".equals(flg)){
				MWhiteList mWhiteList=new MWhiteList();
				mWhiteList.setId(null);
				mWhiteList.setUser_account(machineMes[0]);//用户账户
				mWhiteList.setMachine_code(machineMes[1]);
				MWhiteList mWhite=whiteListDao.findWhiteByUserAccunt(mWhiteList);
				if(mWhite==null){
					mWhiteList.setCreateTime(new Date());
					mWhiteList.setCreateUser(name);
					mWhiteList.setDel_flg(0);
					mWhiteList.setStop_flg(0);
					whiteListDao.save(mWhiteList);
				}
				MBlackList mBlack=new MBlackList();
				mBlack.setUser_account(machineMes[0]);
				mBlack.setMachine_code(machineMes[1]);
				blackListDao.delBlackByUserAccount(mBlack);
			}else{
				MBlackList mBlack=new MBlackList();
				mBlack.setId(null);
				mBlack.setUser_account(machineMes[0]);//用户账户
				mBlack.setMachine_code(machineMes[1]);
				MBlackList mBlackList=blackListDao.findBlackByUserAccunt(mBlack);
				if(mBlackList==null){
					mBlack.setCreateTime(new Date());
					mBlack.setCreateUser(name);
					mBlack.setDel_flg(0);
					mBlack.setStop_flg(0);
					blackListDao.save(mBlack);
				}
				MWhiteList mWhiteList=new MWhiteList();
				mWhiteList.setUser_account(machineMes[0]);
				mWhiteList.setMachine_code(machineMes[1]);
				whiteListDao.delWhiteByUserAccount(mWhiteList);
			}
		}
		for (String string : id) {
			MMachineManage machineManage = machineManageDao.get(string);
			if(StringUtils.isNotBlank(flg)&&"1".equals(flg)){
				machineManage.setIs_lost(1);//激活
				machineManage.setIs_white(2);
				machineManage.setIs_black(1);
			}else{
				machineManage.setIs_lost(2);//挂失
				machineManage.setIs_white(1);
				machineManage.setIs_black(2);
			}
			machineManage.setUpdateUser(name);
			machineManage.setUpdateTime(new Date());
			machineManageDao.update(machineManage);
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("更新","设备管理",account,"UPDATE","M_MACHINE_MANAGE",string,""));
		}
		
	}

	@Override
	public void updateWhiteOrBlack(String ids, String flg,String userAndMach) throws Exception {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String name = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		List<String> idds = Arrays.asList(ids.split(","));
		List<String> list=Arrays.asList(userAndMach.split(";"));
		MMachineManage machineManage = null;
		if(StringUtils.isNotBlank(flg)&&"1".equals(flg)){
			for(int i = 0;i<list.size();i++){
				machineManage = machineManageDao.get(idds.get(i));
				if(machineManage!=null&&!"2".equals(machineManage.getIs_lost())){
					String[] mes=list.get(i).split("_");
					MWhiteList mWhiteList = new MWhiteList();
					mWhiteList.setId(null);
					mWhiteList.setUser_account(mes[0]);
					mWhiteList.setMachine_code(mes[1]);
					mWhiteList.setCreateTime(new Date());
					mWhiteList.setCreateUser(name);
					mWhiteList.setDel_flg(0);
					mWhiteList.setStop_flg(0);
					whiteListDao.save(mWhiteList);
					moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("添加","设备管理",account,"INSERT INTO","M_WHITE_LIST",mWhiteList.getId(),""));
					MBlackList mBlack = new MBlackList();
					mBlack.setUser_account(mes[0]);
					mBlack.setMachine_code(mes[1]);
					blackListDao.delBlackByUserAccount(mBlack);
					moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","设备管理",account,"UPDATE","M_BLACK_LIST",mes[0]+"-"+mes[1],""));
					//移至白名单
					machineManage.setIs_white(2);
					machineManage.setIs_black(1);
					machineManageDao.update(machineManage);
					moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("更新","设备管理",account,"UPDATE","M_MACHINE_MANAGE",idds.get(i),""));
				}
			}
		}else{
			for(int i = 0;i<list.size();i++){
				String[] mes=list.get(i).split("_");
				MBlackList mBlackList = new MBlackList();
				mBlackList.setId(null);
				mBlackList.setUser_account(mes[0]);
				mBlackList.setMachine_code(mes[1]);
				mBlackList.setCreateTime(new Date());
				mBlackList.setCreateUser(name);
				mBlackList.setDel_flg(0);
				mBlackList.setStop_flg(0);
				blackListDao.save(mBlackList);
				moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("添加","设备管理",account,"INSERT INTO","M_BLACK_LIST",mes[0]+"-"+mes[1],""));
				MWhiteList mWhite = new MWhiteList();
				mWhite.setUser_account(mes[0]);
				mWhite.setMachine_code(mes[1]);
				whiteListDao.delWhiteByUserAccount(mWhite);
				moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","设备管理",account,"UPDATE","M_WHITE_LIST",mes[0]+"-"+mes[1],""));
				machineManage = machineManageDao.get(idds.get(i));
				if(machineManage != null){
					machineManage.setIs_white(1);//移至黑名单
					machineManage.setIs_black(2);
					machineManageDao.update(machineManage);
					moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("更新","设备管理",account,"UPDATE","M_MACHINE_MANAGE",list.get(i),""));
				}
			}
		}
		
	}

	@Override
	public MMachineManage get(String arg0) {
		return machineManageDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(MMachineManage arg0) {
		if(StringUtils.isNotBlank(arg0.getId())){
			machineManageDao.update(arg0);
		}else {
			machineManageDao.save(arg0);
		}
		
	}

	@Override
	public MMachineManage getMachineByMachineCode(String machineCode) {
		return machineManageDao.getMachineByMachineCode(machineCode);
	}

	@Override
	public List<MMachineManage> getMachineByMobileNum(List<String> mobileNum) {
		return machineManageDao.getMachineByMobileNum(mobileNum);
	}

}
