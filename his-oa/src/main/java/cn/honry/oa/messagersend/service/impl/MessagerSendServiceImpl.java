package cn.honry.oa.messagersend.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysEmployee;
import cn.honry.oa.messagersend.dao.MessagerSendDao;
import cn.honry.oa.messagersend.service.MessagerSendService;
import cn.honry.sms.clientN.Client;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
@Transactional
@Service("messagerSendService")
public class MessagerSendServiceImpl implements MessagerSendService {
	@Autowired
	@Qualifier("messagerSendDao")
	private MessagerSendDao messagerSendDao;
	@Override
	public List<String> getAcceptUser(String acceptUser) {
		//all-全部人员；1-个人；2-级别post；3-部门；4-职位title
		//0_#3_#4_#2_#1_043501:张春霖,041841:夏俊杰,141350:刘彩霞,170016:陈琳,040339:马红帅
		String dutyCode = "";
		String titleCode = "";
		String account = "";
		String deptCode= "";
		String[] codes = acceptUser.split("#");
		for (int i = 0; i < codes.length; i++) {
			String code = codes[i];
			String[] splits = code.split("_");
			if(splits!=null&&splits.length==2){
				String type = splits[0];
				String str = splits[1];
				if("1".equals(type)){
					account = resolute(str);
				}
				if("2".equals(type)||"6".equals(type)){
					dutyCode = resolute(str);
				}
				if("3".equals(type)){
					deptCode = resolute(str);
				}
				if("4".equals(type)||"5".equals(type)){
					titleCode = resolute(str);
				}
				if("all".equals(type)){//全部人员
					List<SysEmployee> list = messagerSendDao.getAllEmployee();
					List<String> mobile = new ArrayList<String>();
					for (SysEmployee sys : list) {
						if(StringUtils.isNotBlank(sys.getMobile())){
							mobile.add(sys.getMobile());
						}
					}
					return mobile;
				}
			}
		}
		return messagerSendDao.getEmployeBysome(dutyCode, titleCode, account, deptCode);
	}
	/**  
	 * <p> 解析code</p>
	 * @Author: mrb
	 * @CreateDate: 2017年10月24日 下午2:40:56 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年10月24日 下午2:40:56 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param str
	 * @return
	 * String
	 */
	private String resolute(String str){
		String re = "";
		String[] splits = str.split(",");
		for (String string : splits) {
			String[] split2 = string.split(":");
			if(split2!=null&&split2.length>0){
				String code = split2[0];
				if(StringUtils.isNotBlank(re)){
					re += "','";
				}
				re += code;
			}
		}
		return re;
	}
	/**
	 * 
	 * <p> list去重</p>
	 * @Author: mrb
	 * @CreateDate: 2017年10月24日 下午2:40:40 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年10月24日 下午2:40:40 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param list
	 * @return
	 * List<String>
	 */
	private List<List<String>> uniqList(List<String> list){
		List<String> uniq = new ArrayList<String>();
		List<List<String>> returnlist = new ArrayList<List<String>>();
		Map<String,String> map = new HashMap<String, String>();
		for (String string : list) {
			map.put(string, "1");
		}
		for (Entry<String, String> str : map.entrySet()) {
			String key = str.getKey();
			if(StringUtils.isNotBlank(key)&&!"null".equals(key)){
				uniq.add(key);
			}
		}
		int size = uniq.size();
		int len = 4990;
		int time = size/len;
		int count;
		if(time<=0){
			returnlist.add(uniq);
		}else{
			for (int i = 0; i < time+1; i++) {
				List<String> subList = new ArrayList<String>();
				if(i==time){
					count = uniq.size();
				}else{
					count = (i+1)*len;
				}
				for (int j = i*len;j<count;j++) {
					subList.add(uniq.get(j));
				}
				returnlist.add(subList);
			}
		}
		return returnlist;
	}
	private List<String> formatMobiles(String definedaccept){
		List<String> list = new ArrayList<String>();
		String[] mobiles = definedaccept.split(",");
		for (String mobile : mobiles) {
			if(!list.contains(mobile)){
				list.add(mobile);
			}
		}
		return list;
	}
	@Override
	public Map<String, String> msgSend(String acceptUser, String content,String definedaccept) {
		Map<String,String> map = new HashMap<String, String>();
		try{
			List<String> list = this.getAcceptUser(acceptUser);
			List<String> formatMobiles = this.formatMobiles(definedaccept);
			list.addAll(formatMobiles);
			List<List<String>> uniqList = this.uniqList(list);//去重后的手机号
			//短信发送
			Client client = Client.getInstance();
			String msg = "error";
			if(uniqList.size()<=0){
				map.put("resCode", "0000");
				map.put("resMsg", "系统未获取到相关手机号,请核对后重试...");
				return map;
			}
			for (List<String> list2 : uniqList) {
				System.err.println(list2);
				msg = client.send(list2, content, 5, HisParameters.SMSUUID, HisParameters.SMSUSER, HisParameters.SMSPASSWORD);
			}
			System.err.println(msg);
			String regex = "<message>(.*)</message>";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(msg);
			if(matcher.find()){
				String group = matcher.group(1);
				map.put("resCode", group);
				map.put("resMsg", group);
			}else{
				map.put("resCode", msg);
				map.put("resMsg", msg);
			}
		}catch(Exception e){
			map.put("resCode", e.getLocalizedMessage());
			map.put("resMsg", e.getLocalizedMessage());
		}
		//解析发送返回的值
		return map;
	}

}
