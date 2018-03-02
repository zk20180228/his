<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String strContent = request.getParameter("strContent");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@ include file="/common/metas.jsp" %>
</head>
<body>
	<div id="cc" class="easyui-layout" fit=true>   
	     
	    <div id="template" data-options="region:'center'" style="overflow: auto;">
	    
	    </div>   
</div>  

	

<!-- 电子病历模板维护 -->
	<div id="divLayout" class="easyui-layout" style="overflow: auto;font-size: 16px;" fit=true>
	
	</div>
</body>

<style type="text/css">
	body, ul, li, form, hr, p, h1, h2, h3, h4, h5, h6, table, tr, td, input, a, span, img, dl, dt, dd {
	    margin: 0px;
	    padding: 0px;
	    font-size: 16px;
	}
</style>

<script type="text/javascript">
	//加载页面
	$(function(){
		var strContent = '<%=strContent%>';
		strContent = rep(strContent);
		document.getElementById('template').innerHTML = strContent;
	});	
	function rep(template){
		
		//正则  radios|checkboxs|select|yesornoselect 匹配的边界 |--|  因为当使用 {} 时js报错
		var preg =  /(<label>{.*?<span(((?!<span).)*leipiplugins=\"(radios|checkboxs|yesornoselect|number|date|text|textarea)\".*?)>(.*?)<\/span>}<\/label>|<(img|input|textarea).*?(<\/select>|<\/textarea>|\/>))/gi;
		var preg_attr =/(\w+)=\"(.?|.+?)\"/gi;
		var preg_group =/<input.*?\/>/gi;
		template = template.replace(preg, function(plugin,p1,p2,p3,p4,p5,p6){
			var attr_arr_all = new Object(), name = '', html = '';
			var p0 = plugin;
			var tag = p6 ? p6 : p4;

			if(tag == 'radios' || tag == 'checkboxs' || tag == 'yesornoselect')
			{
				plugin = p2;
			}
			plugin.replace(preg_attr, function(str0,attr,val) {
                if(attr=='name')
                {
                    if(val=='leipiNewField')
                    {
                        val = 'data_';
                    }
                    name = val;
                }
                    attr_arr_all[attr] = val;
	        }) 

			html = attr_arr_all['prefix'];
			if(tag =='checkboxs' || tag =='yesornoselect') /*复选组  多个字段 */
			{
				plugin = p0;
				
				var dot_name ='', dot_value = '';
				p5.replace(preg_group, function(parse_group) {
					var option = new Object();
					parse_group.replace(preg_attr, function(str0,k,val) {
						if(k=='name')
						{
							attr_arr_all['name'] += dot_name + val;
							dot_name = ',';
						}
						else if(k=='value')
						{
							attr_arr_all['value'] += dot_value + val;
							dot_value = ',';
						}
						option[k] = val;    
					});
					
					if(!attr_arr_all['options']) attr_arr_all['options'] = new Array();
					attr_arr_all['options'].push(option);
					var disabled = ' disabled="true"';
					var checked = option['checked'] !=undefined ? 'checked="checked"' : '';
					html += '<input type="checkbox" name="'+option['name']+'" value="'+option['value']+'"'+checked + disabled+'/>'+option['value']+'&nbsp;';		
				});		
				html=html.substring(0, html.length-6);
			 }else if(tag =='radios') /*单选组  一个字段*/
				{
					plugin = p0;
					
					var dot='';
					p5.replace(preg_group, function(parse_group) {
						var option = new Object();
						parse_group.replace(preg_attr, function(str0,k,val) {
							if(k=='value')
							{
								attr_arr_all['value'] += dot + val;
								dot = ',';
							}
							option[k] = val;    
						});
						option['name'] = attr_arr_all['name'];
						if(!attr_arr_all['options']) attr_arr_all['options'] = new Array();
						attr_arr_all['options'].push(option);
						var disabled = ' disabled="true"';
						var checked = option['checked'] !=undefined ? 'checked="checked"' : '';
						html += '<input type="radio" name="'+attr_arr_all['name']+'" value="'+option['value']+'"'+checked+disabled+'/>'+option['value']+'&nbsp;';

					});
						html=html.substring(0, html.length-6);
				}else if(tag =='text' || tag == 'textarea'){
					html += '<input type="text" placeholder="'+attr_arr_all['title']+'" readonly="readonly"/>';
				}else if(tag =='date'){
					var date = new Date();
					var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
					var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
					+ (date.getMonth() + 1);
					var strDate = date.getFullYear() + '-' + month + '-' + day;
					html += '<input type="text" value="'+strDate+'" readonly="readonly"/>';
				}else if(tag =='number'){
					html += '<input type="text" placeholder="请输入数字" readonly="readonly"/>';
				}
				return html + attr_arr_all['suffix'];
		});
		return template;
	}	

</script>
</html>