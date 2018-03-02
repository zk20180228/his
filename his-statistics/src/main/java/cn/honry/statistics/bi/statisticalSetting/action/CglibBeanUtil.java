package cn.honry.statistics.bi.statisticalSetting.action;



public class CglibBeanUtil {
	 /**  
     * 实体Object  
     *//*    
   public Object object = null;    
       
   *//**  
     * 属性map  
     *//*    
   //public BeanMap beanMap = null;    
       
   public CglibBeanUtil() {    
     super();    
   }    
       
   @SuppressWarnings("unchecked")    
   public CglibBeanUtil(Map<String,Object> propertyMap) {    
     this.object = generateBean(propertyMap);    
     this.beanMap = BeanMap.create(this.object);    
   }    
       
   *//**  
     * 给bean属性赋值  
     * @param key 属性名  
     * @param value 值
     *//*    
   public void setValue(String key, Object value) {
     beanMap.put(key, value);    
   }    
       
   *//**  
     * 通过属性名得到属性值  
     * @param property 属性名  
     * @return 值  
     *//*    
   public Object getValue(String property) {    
     return beanMap.get(property);    
   }    
       
   *//**  
     * 得到该实体bean对象  
     * @return  
     *//*    
   public Object getObject() {    
     return this.object;    
   }    
   
   @SuppressWarnings("unchecked")    
   private Object generateBean(Map<String,Object> propertyMap) {    
     BeanGenerator generator = new BeanGenerator();    
     Set keySet = propertyMap.keySet();    
     for (Iterator i = keySet.iterator(); i.hasNext();) {    
      String key = (String) i.next();    
      generator.addProperty(key, (Class) propertyMap.get(key));    
     }    
     return generator.create();   
   }  */  
}
