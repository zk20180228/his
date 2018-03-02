package cn.honry.mobile.iosUpdateVersion.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.MIosApkVersion;
import cn.honry.base.bean.model.MSysOperateLog;
import cn.honry.base.bean.model.OFBatchPush;
import cn.honry.mobile.iosUpdateVersion.dao.IosUpdateVersionDao;
import cn.honry.mobile.iosUpdateVersion.service.IosUpdateVersionService;
import cn.honry.mobile.moOperateLog.dao.MoOperateLogDao;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
@Service("iosUpdateVersionService")
public class IosUpdateVersionServiceImpl implements IosUpdateVersionService{

	@Resource
	private IosUpdateVersionDao  iosUpdateVersionDao;
	
	@Resource
	private MoOperateLogDao moOperateLogDao;
	
	@Override
	public List<MIosApkVersion> getPagedVersionList(MIosApkVersion mIosApkVersion, String rows, String page) throws Exception {
		return iosUpdateVersionDao.getList(mIosApkVersion, rows, page);
	}

	@Override
	public int getTotal(MIosApkVersion mIosApkVersion) throws Exception {
		return iosUpdateVersionDao.getTotal(mIosApkVersion);
	}
	

	@Override
	public void delVersions(String ids) throws Exception {
		String name = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String[] id = ids.split(",");
		MIosApkVersion mIosApkVersion = null;
		for (String str : id) {
			mIosApkVersion = iosUpdateVersionDao.get(str);
			mIosApkVersion.setDel_flg(1);
			mIosApkVersion.setDeleteTime(new Date());
			mIosApkVersion.setDeleteUser(name);
			iosUpdateVersionDao.update(mIosApkVersion);
		}
		
		moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","IOS版本升级",account,"UPDATE","MIOSAPKVERSION",ids,""));
	}

	@Override
	public Map<String, String> save(MIosApkVersion mIosApkVersion) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String name = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		Map<String, Object> batchPushMap = new HashMap<String, Object>();
		mIosApkVersion.setApkClearCache(mIosApkVersion.getApkClearCache() == null ? 1 : mIosApkVersion.getApkClearCache());
		mIosApkVersion.setSendRadio(mIosApkVersion.getSendRadio() == null ? 1 : mIosApkVersion.getSendRadio());
		if(StringUtils.isNotBlank(mIosApkVersion.getId())){
			MIosApkVersion mApk =iosUpdateVersionDao.get(mIosApkVersion.getId());
			mApk.setApkNewestVnum(mIosApkVersion.getApkNewestVnum());
			mApk.setApkVersionName(mIosApkVersion.getApkVersionName());
			mApk.setForceUpdateFlg(mIosApkVersion.getForceUpdateFlg());
			mApk.setApkClearCache(mIosApkVersion.getApkClearCache());
			mApk.setSendRadio(mIosApkVersion.getSendRadio());
			mApk.setApkDownloadAddr(StringUtils.isNotBlank(mIosApkVersion.getApkDownloadAddr())?mIosApkVersion.getApkDownloadAddr():"");
			//二维码地址
			if(StringUtils.isNotBlank(mIosApkVersion.getApkDownloadQRAddr())){
				mApk.setApkDownloadQRAddr(mIosApkVersion.getApkDownloadQRAddr());
			}
			mApk.setUpdateUser(name);
			mApk.setUpdateTime(new Date());
			iosUpdateVersionDao.update(mApk);
			if(mIosApkVersion.getSendRadio()!=null&&mIosApkVersion.getSendRadio()==2){
				//广播推送begin
				batchPushMap.put("iosApkVersionName", mApk.getApkVersionName());
				batchPushMap.put("iosForceUpdateFlg", mApk.getForceUpdateFlg());
				batchPushMap.put("iosNewestVNum", mApk.getApkNewestVnum());
				batchPushMap.put("iosClearCache", mApk.getApkClearCache());
				batchPushMap.put("appStoreAddr", mApk.getApkDownloadAddr());
				String batchPushMapJson = JSONUtils.toJson(batchPushMap);
				OFBatchPush ofBatchPush = new OFBatchPush();
				ofBatchPush.setBody(batchPushMapJson);
				ofBatchPush.setCreateTime(new Date());
				ofBatchPush.setStatus(1);
				iosUpdateVersionDao.save(ofBatchPush);
				//广播推送end
			}
			map.put("resCode", "0");
			map.put("resMsg", "保存成功");
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("修改","IOS版本升级",account,"UPDATE","MIOSAPKVERSION",mIosApkVersion.getId(),""));
		}else{
			mIosApkVersion.setId(null);
			mIosApkVersion.setCreateTime(new Date());
			mIosApkVersion.setCreateUser(name);
			mIosApkVersion.setDel_flg(0);
			mIosApkVersion.setStop_flg(0);
			iosUpdateVersionDao.save(mIosApkVersion);
			map.put("resCode", "0");
			map.put("resMsg", "保存成功");
			
			if(mIosApkVersion.getSendRadio()!=null&&mIosApkVersion.getSendRadio()==2){
				//广播推送begin
				batchPushMap.put("iosApkVersionName", mIosApkVersion.getApkVersionName());
				batchPushMap.put("iosForceUpdateFlg", mIosApkVersion.getForceUpdateFlg());
				batchPushMap.put("iosNewestVNum", mIosApkVersion.getApkNewestVnum());
				batchPushMap.put("iosClearCache", mIosApkVersion.getApkClearCache());
				batchPushMap.put("appStoreAddr", mIosApkVersion.getApkDownloadAddr());
				String batchPushMapJson = JSONUtils.toJson(batchPushMap);
				OFBatchPush ofBatchPush = new OFBatchPush();
				ofBatchPush.setBody(batchPushMapJson);
				ofBatchPush.setCreateTime(new Date());
				ofBatchPush.setStatus(1);
				iosUpdateVersionDao.save(ofBatchPush);
				//广播推送end
			}
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("添加","IOS版本升级",account,"INSERT INTO","MIOSAPKVERSION",mIosApkVersion.getId(),""));
		}
		return map;
	}

	@Override
	public MIosApkVersion get(String arg0) {
		return iosUpdateVersionDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(MIosApkVersion mApkVersion) {
		if (StringUtils.isNotBlank(mApkVersion.getId())) {
			iosUpdateVersionDao.update(mApkVersion);
		}else {
			iosUpdateVersionDao.save(mApkVersion);
		}
	}


}
