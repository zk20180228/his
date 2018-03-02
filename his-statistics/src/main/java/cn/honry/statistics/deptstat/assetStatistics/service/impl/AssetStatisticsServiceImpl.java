package cn.honry.statistics.deptstat.assetStatistics.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.AssetsDevice;
import cn.honry.base.bean.model.AssetsDeviceUse;
import cn.honry.statistics.deptstat.assetStatistics.dao.AssetStatisticsDAO;
import cn.honry.statistics.deptstat.assetStatistics.service.AssetStatisticsService;
import cn.honry.statistics.deptstat.assetStatistics.vo.AssetsDeviceVo;
import cn.honry.utils.NumberUtil;
@Service("assetStatisticsService")
@Transactional
@SuppressWarnings({ "all" })
public class AssetStatisticsServiceImpl implements AssetStatisticsService{
	@Autowired
	@Qualifier(value = "assetStatisticsDAO")
	private AssetStatisticsDAO assetStatisticsDAO;
	/**  
	 * 
	 * 资产分类list
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:09:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:09:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<AssetsDevice> queryAssetsDevice(String officeName,
			String className, String classCode, String deviceName, String page,
			String rows) throws Exception {
		return assetStatisticsDAO.queryAssetsDevice(officeName, className, classCode, deviceName, page, rows);
	}
	/**  
	 * 
	 * 资产分类Total
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:09:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:09:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public Integer queryAssetsDeviceTotal(String officeName, String className,
			String classCode, String deviceName) throws Exception {
		return assetStatisticsDAO.queryAssetsDeviceTotal(officeName, className, classCode, deviceName);
	}
	/**  
	 * 
	 * 领用部门list
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:09:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:09:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	
	@Override
	public List<AssetsDeviceUse> queryAssetsDeviceUse(String deptCode,
			String page, String rows) throws Exception {
		return assetStatisticsDAO.queryAssetsDeviceUse(deptCode, page, rows);
	}
	/**  
	 * 
	 * 领用部门Total
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:10:42 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:10:42 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	
	@Override
	public Integer queryAssetsDeviceUseTotal(String deptCode) throws Exception {
		return assetStatisticsDAO.queryAssetsDeviceUseTotal(deptCode);
	}
	/**  
	 * 
	 * 资产价值list(设备现值=设备总价-原价*【{（1/折旧年限）*100%}/12】*月份)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:09:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:09:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	
	@Override
	public List<AssetsDeviceVo> queryAssetsDeviceValue(String officeName,
			String className, String classCode, String deviceName, String page,
			String rows) throws Exception {
		 List<AssetsDeviceVo> list = assetStatisticsDAO.queryAssetsDeviceValue(officeName, className, classCode, deviceName, page, rows);
		 for (AssetsDeviceVo vo : list) {
			double month = this.getMonth(vo.getDeviceDate(), new Date());
			double depreciation=vo.getDepreciation()==null?1:vo.getDepreciation();
			vo.setNewValue(vo.getPurchTotal()*(1-month/depreciation/12));
		}
		return list;
		 
	}
	/**  
	 * 
	 * 资产价值Total
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:10:42 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:10:42 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public Integer queryAssetsDeviceValueTotal(String officeName,
			String className, String classCode, String deviceName)
			throws Exception {
		return assetStatisticsDAO.queryAssetsDeviceValueTotal(officeName, className, classCode, deviceName);
	}
	/** 
     * 获取两个日期相差几个月 
     * @param start 
     * @param end 
     * @return 
     */  
    public static int getMonth(Date start, Date end) {  
        if (start.after(end)) {  
            Date t = start;  
            start = end;  
            end = t;  
        }  
        Calendar startCalendar = Calendar.getInstance();  
        startCalendar.setTime(start);  
        Calendar endCalendar = Calendar.getInstance();  
        endCalendar.setTime(end);  
        Calendar temp = Calendar.getInstance();  
        temp.setTime(end);  
        temp.add(Calendar.DATE, 1);  
  
        int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);  
        int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);  
  
        if ((startCalendar.get(Calendar.DATE) == 1)&& (temp.get(Calendar.DATE) == 1)) {  
            return year * 12 + month + 1;  
        } else if ((startCalendar.get(Calendar.DATE) != 1) && (temp.get(Calendar.DATE) == 1)) {  
            return year * 12 + month;  
        } else if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) != 1)) {  
            return year * 12 + month;  
        } else {  
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);  
        }  
    }  
}
