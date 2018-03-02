package cn.honry.inner.statistics.hosIncomeCount.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

public class SortMapUtils {
/**
 * @Description:对map进行降序，map的value是double类型
 * @param h：map
 * @return
 * Map.Entry[] :Entry
 * @exception:
 * @author: zhangkui
 * @time:2017年5月20日 上午10:17:12
 */
@SuppressWarnings({"all"})	
public static Map.Entry[] reverseMap(Map h) {
   
	Set set = h.entrySet();
    Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
    Arrays.sort(entries, new Comparator() {
    	public int compare(Object arg0, Object arg1) {
			      Double key1 = Double.valueOf(((Map.Entry) arg0).getValue().toString());
			      Double key2 = Double.valueOf(((Map.Entry) arg1).getValue().toString());
      
			      if (key1 < key2)
			             return 1;   // Neither val is NaN, thisVal is smaller
			         if (key1 > key2)
			             return -1;   // Neither val is NaN, thisVal is larger
			         long thisBits = Double.doubleToLongBits(key1);
			         long anotherBits = Double.doubleToLongBits(key2);
			
			         return (thisBits == anotherBits ?  0 : // Values are equal
			                 (thisBits < anotherBits ? 1 : // (-0.0, 0.0) or (!NaN, NaN)
			                  -1));                          // (0.0, -0.0) or (NaN, !NaN)
			     }
   
    });
   
   
    return entries;
   
	}	
}
