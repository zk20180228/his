package cn.honry.portal.until;
/**  
 * 类说明   
 *  
 * @author qh 
 * @date 2017年3月20日  
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import ognl.DefaultTypeConverter;

	public class DateConverter extends DefaultTypeConverter {
	    
	    private String[] patterns = new String[] { "yyyyMMdd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "HH:mm", "yyyy-MM-dd" };

	    @SuppressWarnings("rawtypes")
	    public Object convertValue(Map context, Object value, Class toType) {
	        try {
	            if (toType == Date.class) {
	                String dataStr = ((String[]) value)[0];
	                for (int i = 0; i < patterns.length; i++) {
	                    if (patterns[i].length() == dataStr.length()) {
	                        SimpleDateFormat f = new SimpleDateFormat((patterns[i]).toString()); 
	                        return f.parse(dataStr);
	                    }
	                }
	            } else if (toType == String.class) {
	                String dataStr = ((Date) value).toString();
	                return dataStr;
	            }
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        return null;

	    }
	}

