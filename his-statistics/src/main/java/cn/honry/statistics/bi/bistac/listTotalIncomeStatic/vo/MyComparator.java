package cn.honry.statistics.bi.bistac.listTotalIncomeStatic.vo;
import java.util.Comparator;

import cn.honry.statistics.util.customVo.CustomVo;

public class MyComparator implements  Comparator<CustomVo>{

	/**
	 * @Description:返回1代表o1大于o2,0代表相等，-1代表o1小于o2
	 * @param o1
	 * @param o2
	 * @return
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月13日 下午2:26:52
	 */
	public int compare(CustomVo o1, CustomVo o2) {
		String s1= o1.getName();
		String s2=o2.getName();
		if(s1!=null){
			return s1.compareTo(s2);//按照name进行自然排序
		}else{
			return -1;//如果为s1为null,算小于
		}
	}
	
	
}