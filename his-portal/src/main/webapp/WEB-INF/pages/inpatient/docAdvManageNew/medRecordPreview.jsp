<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String strContent = request.getParameter("strContent");
%>
<!DOCTYPE html> 
<html>
<head>
<title>病历预览</title>
	<%@ include file="/common/metas.jsp" %>
</head>
<body>
<!-- 电子病历模板维护 -->
	<div id="btn" class="easyui-layout" style="width:100%;">
		<div id="divLayout" style="width:100%;height:100%;overflow-y: auto;font: 16px;" fit=true>
	
	</div>
	</div>
	
</body>
<style type="text/css">
	body, ul, li, form, hr, p, h1, h2, h3, h4, h5, h6, table, tr, td, input, a, span, img, dl, dt, dd {
	    margin: 0px;
	    padding: 0px;
	    font-size: 16px;
	}
	@page :pseudo-class {
	  size: A4 landscape;
	  margin:2cm;
	}
</style>

<script type="text/javascript">

	//加载页面
	$(function(){
		var strContent = '<%=strContent%>';
		strContent = rep(strContent);
		document.getElementById('divLayout').innerHTML = strContent;
	});	
	function rep(template){//找到控件代码后对不同空间进行不同操作
		//正则  radios|checkboxs|select|yesornoselect 匹配的边界 |--|  因为当使用 {} 时js报错
		var preg =  /(<label>{.*?<span(((?!<span).)*leipiplugins=\"(radios|checkboxs|yesornoselect|number|date|text|textarea)\".*?)>(.*?)<\/span>}<\/label>|<(img|input|textarea).*?(<\/select>|<\/textarea>|\/>))/gi;
		var preg_attr =/(\w+)=\"(.?|.+?)\"/gi;
		var preg_group =/<input.*?\/>/gi;
		var preg_area =/<textarea[^>]*>([^<]+)<\/textarea>/gi;
		var preg_page =/_ueditor.*?tag_/gi;//用于预览分页，但是由于预览界面不是浏览器自带打印预览界面或其他原因无法实现分页
		
		var checkboxs=0;
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
			html += attr_arr_all['prefix'];
			if(tag =='checkboxs') /*复选组  多个字段 */
			{
				plugin = p0;
				html += '<u>';
				var checkedValue = '';
				var dot_name ='', dot_value = '';
				p5.replace(preg_group, function(parse_group) {
					var option = new Object();
					parse_group.replace(preg_attr, function(str0,k,val) {//读取选项
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
					if(option['checked'] != undefined){
						checkedValue += option['value'] + ',';
					}
				});		
				if(checkedValue == ''){//如果没有选中选项  则把该控件位置置空
					html += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>';
				}else{//如果有选中选项，将最后一个逗号去掉
					checkedValue = checkedValue.substring(0, checkedValue.length-1);
					html += checkedValue + '</u>';
				}
			}else if(tag =='yesornoselect') /*有无选组*/
			{
				plugin = p0;
				var checkFlag = false;
				var unCheckFlag = false;
				var checkHtml = '有:<u>';
				var unCheckHtml = ' 无:<u>';
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
					if(option['checked'] !=undefined){//如果此选项选中，改变选项选中标志，并将此选项选项值加入选中HTML
						checkFlag = true;
						checkHtml += option['value'] + ',';
					}else{//如果此未选项选中,改变选项未选中标志，并将此选项选项值加入未选中HTML
						unCheckFlag = true;
						unCheckHtml += option['value'] + ',';
					}
					});
					if(checkFlag){//如果有选项选中，则去掉最后的逗号
						checkHtml = checkHtml.substring(0, checkHtml.length-1);
						checkHtml += '</u>';
					}else{//如果没有选项被选中
						checkHtml += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>';
					}
					if(unCheckFlag){//如果有选项未选中，则去掉最后的逗号
						unCheckHtml = unCheckHtml.substring(0, unCheckHtml.length-1);
						unCheckHtml += '</u>';
					}else{//如果选项被全部选中
						unCheckHtml += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>';
					}
					html += checkHtml + unCheckHtml;
				}else if(tag =='radios') /*单选组  一个字段*/
				{
					plugin = p0;
					var checkFalg = false;
					html += '<u>';
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
						if(option['checked'] !=undefined){//若有选项选中
							checkFalg = true;
							html += option['value'] + '</u>';
							return ;
						}
					});
					if(!checkFalg){
						html += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>';
					}
				}else if(tag != 'textarea'){
					plugin = p0;
					html += '<u>';
					p5.replace(preg_group, function(parse_group) {
						var option = new Object();
						parse_group.replace(preg_attr, function(str0,k,val) {
							if(k=='value')
							{
								attr_arr_all['value'] = val;
							}
							option[k] = val;    
						});
						if( option['value'] != null && option['value'] != '' && option['value'] != 'undefined'){
							html += option['value'] + '</u>';
						}else{
							html += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>';
						}
					});					
				}else{//文本框 有值填充  没值置空
					plugin = p0;
					html += '<u>';
					p5.replace(preg_area, function(preg_area,p1,p2,p3) {
						if(p1 != null && p1 != ''){
							html += p1 + '</u>';
						}else{
							html += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>';
						}
					});		
				}
				return html + attr_arr_all['suffix'];
		});
		template = template.replace(preg_page,'<div style="page-break-after: always;"></div> ');
		return template;
	}	

</script>
</html>