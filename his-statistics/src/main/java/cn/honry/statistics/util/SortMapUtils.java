package cn.honry.statistics.util;

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
public static Map.Entry[] reverseMap(Map h) {
    Set set = h.entrySet();
    Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
    Arrays.sort(entries, new Comparator() {
     public int compare(Object arg0, Object arg1) {
      Double key1 = Double.valueOf(((Map.Entry) arg0).getValue()
        .toString());
      Double key2 = Double.valueOf(((Map.Entry) arg1).getValue()
        .toString());
      
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
	
public static void main(String[] args){
//	HashMap<String,Double> map = new HashMap<String,Double>();
//	map.put("老大", 100.0);
//	map.put("老二", 80.0);
//	map.put("老三", 60.0);
//	map.put("老四", 99.0);
//	
//	Entry[] entries = reverseMap(map);
//		for(Entry e:entries){
//			System.out.println(e.getKey()+":"+e.getValue());
//		}
//	
//	
//		Map<String,Double> linkMap = new LinkedHashMap<String,Double>();
//		linkMap.put("老大", 100.0);
//		linkMap.put("老二", 80.0);
//		linkMap.put("老三", 60.0);
//		linkMap.put("老四", 99.0);
//		linkMap.put("其他", 0.0);
//		System.out.println(linkMap);
//		ArrayList<String> list = new ArrayList<String>();
//		
//		list.add("张三");
//		list.add("李四");
//		list.add("王五");
//		list.add("赵六");
//		list.add("xiaoqi");
//		System.out.println("排序前"+list);
//		
//		Collections.sort(list);
//		System.out.println("排序后"+list);
		
//		int a= "费别".compareTo("张三");
//		System.out.println(a);
		
		
		
		
		
		
		
}








}
