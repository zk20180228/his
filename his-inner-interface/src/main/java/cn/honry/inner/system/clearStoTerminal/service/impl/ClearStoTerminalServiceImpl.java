package cn.honry.inner.system.clearStoTerminal.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.StoTerminal;
import cn.honry.inner.system.clearStoTerminal.dao.ClearStoTerminalDAO;
import cn.honry.inner.system.clearStoTerminal.service.ClearStoTerminalService;

/** <p>门诊终端定时任务接口</p>
* @ClassName: StoTerminalServiceImpl 
* @author dtl
* @date 2017年3月20日
*  
*/
@Service("clearStoTerminalService")
@Transactional
@SuppressWarnings({ "all" })
public class ClearStoTerminalServiceImpl implements ClearStoTerminalService{

	@Autowired
	@Qualifier(value = "clearStoTerminalDAO")
	private ClearStoTerminalDAO clearStoTerminalDAO;

	@Override
	public StoTerminal get(String arg0) {
		return clearStoTerminalDAO.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(StoTerminal arg0) {
		if (StringUtils.isNotBlank(arg0.getId())) {
			clearStoTerminalDAO.update(arg0);
		}else {
			clearStoTerminalDAO.save(arg0);
		}
	}

	@Override
	public void clearStoTerminal() {
		clearStoTerminalDAO.clearStoTerminal();
	}
	
	


}

