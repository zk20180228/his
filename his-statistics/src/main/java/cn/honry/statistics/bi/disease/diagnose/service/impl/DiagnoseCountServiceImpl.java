package cn.honry.statistics.bi.disease.diagnose.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.honry.statistics.bi.disease.diagnose.dao.DiagnoseCountDao;
import cn.honry.statistics.bi.disease.diagnose.service.DiagnoseCountService;
import cn.honry.statistics.bi.disease.diagnose.vo.DiagnoseVo;
import cn.honry.utils.NumberUtil;

@Service("diagnoseCountService")
public class DiagnoseCountServiceImpl implements DiagnoseCountService {

	@Resource
	private DiagnoseCountDao diagnoseCountDao;
	
	@Override
	public List<DiagnoseVo> diagnoseList(String page, String rows, String feature) {
		List<DiagnoseVo> list= diagnoseCountDao.diagnoseList(page,rows,feature);
		//用来记录病症出现总次数的map,别太认真都是模拟数据,如("胸闷",3)
		Map<String,Integer> map = new HashMap<String,Integer> ();
		for(DiagnoseVo d:list){
				Integer integer = map.get(d.getFeature());
				if(integer!=null){
					map.put(d.getFeature(), d.getCount()+integer);
				}else{
					map.put(d.getFeature(), d.getCount());
				}
		}
		
		//计算匹配度
		for(DiagnoseVo v:list){
		    Double d = v.getCount().doubleValue();
			Double m=map.get(v.getFeature()).doubleValue();
			
			String r = NumberUtil.init().format(100*(d/m), 2);
			v.setMatchingDegree(r+"%");	
		}
		
		return list;
	}

	@Override
	public Long diagnoseTotal(String feature) {
		return diagnoseCountDao.diagnoseTotal(feature);
	}

	public List featureMap(){
		return diagnoseCountDao.featureMap();
	}
	
}
