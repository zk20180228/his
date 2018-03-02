package cn.honry.mobile.updateVersion.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.MApkVersion;
import cn.honry.base.bean.model.MSysOperateLog;
import cn.honry.base.bean.model.OFBatchPush;
import cn.honry.mobile.moOperateLog.dao.MoOperateLogDao;
import cn.honry.mobile.updateVersion.dao.UpdateVersionDao;
import cn.honry.mobile.updateVersion.service.UpdateVersionService;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
@Service("updateVersionService")
public class UpdateVersionServiceImpl implements UpdateVersionService{

	@Resource
	private UpdateVersionDao  updateVersionDao;
	
	@Resource
	private MoOperateLogDao moOperateLogDao;
	
	@Override
	public List<MApkVersion> getPagedVersionList(MApkVersion mApkVersion, String rows, String page) throws Exception {
		return updateVersionDao.getList(mApkVersion, rows, page);
	}

	@Override
	public int getTotal(MApkVersion mApkVersion) throws Exception {
		return updateVersionDao.getTotal(mApkVersion);
	}
	

	@Override
	public void delVersions(String ids) throws Exception {
		String name = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String[] id = ids.split(",");
		MApkVersion mApkVersion = null;
		for (String string : id) {
			mApkVersion = updateVersionDao.get(string);
			mApkVersion.setDel_flg(1);
			mApkVersion.setDeleteTime(new Date());
			mApkVersion.setDeleteUser(name);
			updateVersionDao.update(mApkVersion);
		}
		
		moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","版本升级",account,"UPDATE","MAPKVERSION",ids,""));
	}

	@Override
	public Map<String, String> save(MApkVersion mApkVersion) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String name = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		String addressOut = HisParameters.OUTFILEUPLOADURL;
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		Map<String, Object> batchPushMap = new HashMap<String, Object>();
		mApkVersion.setApkClearCache(mApkVersion.getApkClearCache() == null ? 1 : mApkVersion.getApkClearCache());
		if(StringUtils.isNotBlank(mApkVersion.getId())){
			MApkVersion mApk =updateVersionDao.get(mApkVersion.getId());
			/*mApk.setApkMinimumNum(mApkVersion.getApkMinimumNum());
			mApk.setApkCurrentVnum(mApkVersion.getApkCurrentVnum());*/
			mApk.setApkNewestVnum(mApkVersion.getApkNewestVnum());
			mApk.setApkVersionName(mApkVersion.getApkVersionName());
			mApk.setForceUpdateFlg(mApkVersion.getForceUpdateFlg());
			mApk.setApkClearCache(mApkVersion.getApkClearCache());
			if(StringUtils.isNotBlank(mApkVersion.getApkDownloadAddr())){
				mApk.setApkDownloadAddr(mApkVersion.getApkDownloadAddr());
			}
			if(StringUtils.isNotBlank(mApkVersion.getApkDownloadFixedAddr())){
				mApk.setApkDownloadFixedAddr(mApkVersion.getApkDownloadFixedAddr());
			}
			//二维码地址
			if(StringUtils.isNotBlank(mApkVersion.getApkDownloadQRAddr())){
				mApk.setApkDownloadQRAddr(mApkVersion.getApkDownloadQRAddr());
			}
			mApk.setUpdateUser(name);
			mApk.setUpdateTime(new Date());
			updateVersionDao.update(mApk);
			if(StringUtils.isNotBlank(mApkVersion.getApkDownloadAddr())){
				//广播推送begin
				batchPushMap.put("apkNewestVNum", mApk.getApkNewestVnum());
				batchPushMap.put("forceUpdateFlg", mApk.getForceUpdateFlg());
				batchPushMap.put("apkDownloadAddr", addressOut+mApk.getApkDownloadAddr());
				batchPushMap.put("apkVersionName", mApk.getApkVersionName());
				batchPushMap.put("apkClearCache", mApk.getApkClearCache());
				String batchPushMapJson = JSONUtils.toJson(batchPushMap);
				OFBatchPush ofBatchPush = new OFBatchPush();
				ofBatchPush.setBody(batchPushMapJson);
				ofBatchPush.setCreateTime(new Date());
				ofBatchPush.setStatus(1);
				updateVersionDao.save(ofBatchPush);
				//广播推送end
			}
			map.put("resCode", "0");
			map.put("resMsg", "保存成功");
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("修改","版本升级",account,"UPDATE","MAPKVERSION",mApkVersion.getId(),""));
		}else{
			mApkVersion.setId(null);
			mApkVersion.setCreateTime(new Date());
			mApkVersion.setCreateUser(name);
			mApkVersion.setDel_flg(0);
			mApkVersion.setStop_flg(0);
			updateVersionDao.save(mApkVersion);
			map.put("resCode", "0");
			map.put("resMsg", "保存成功");
			//广播推送begin
			batchPushMap.put("apkNewestVNum", mApkVersion.getApkNewestVnum());
			batchPushMap.put("forceUpdateFlg", mApkVersion.getForceUpdateFlg());
			batchPushMap.put("apkDownloadAddr",addressOut+mApkVersion.getApkDownloadAddr());
			batchPushMap.put("apkVersionName", mApkVersion.getApkVersionName());
			batchPushMap.put("apkClearCache", mApkVersion.getApkClearCache());
			String batchPushMapJson = JSONUtils.toJson(batchPushMap);
			OFBatchPush ofBatchPush = new OFBatchPush();
			ofBatchPush.setBody(batchPushMapJson);
			ofBatchPush.setCreateTime(new Date());
			ofBatchPush.setStatus(1);
			updateVersionDao.save(ofBatchPush);
			//广播推送end
			
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("添加","版本升级",account,"INSERT INTO","MAPKVERSION",mApkVersion.getId(),""));
		}
		return map;
	}

	@Override
	public MApkVersion get(String arg0) {
		return updateVersionDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(MApkVersion mApkVersion) {
		if (StringUtils.isNotBlank(mApkVersion.getId())) {
			updateVersionDao.update(mApkVersion);
		}else {
			updateVersionDao.save(mApkVersion);
		}
	}


}
