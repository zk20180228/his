<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>电子病历模板维护</title>
	<base href="<%=basePath%>">
	<%@ include file="/common/metas.jsp" %>
	<script type="text/javascript" src="ueditor1_4_3_2/ueditor.config.js"></script>
	<script type="text/javascript" src="ueditor1_4_3_2/ueditor.all.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="ueditor1_4_3_2/formdesign/leipi.formdesign.v4.js?2021"></script>
	
</head>
<body>
<!-- 电子病历模板维护 -->
	<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;overflow-y: auto;" fit=true>
		<p style="padding: 5px 0px 5px 5px">
			<button type="button" onclick="templateDesign.exec('radios');" class="btn btn-info">单选</button>
<!-- 			<button type="button" onclick="templateDesign.exec('select');" class="btn btn-info">下拉菜单</button> -->
			<button type="button" onclick="templateDesign.exec('checkboxs');" class="btn btn-info">多选</button>
			<button type="button" onclick="templateDesign.exec('yesornoselect');" class="btn btn-info">有无选</button>
			
	        <button type="button" onclick="templateDesign.exec('text');" class="btn btn-info">录入提示</button>
	        <button type="button" onclick="templateDesign.exec('textarea');" class="btn btn-info">多行文本</button>
	        <button type="button" onclick="templateDesign.exec('number');" class="btn btn-info">数字</button>
	        <button type="button" onclick="templateDesign.exec('date');" class="btn btn-info">时间</button>
	        <button type="button" onclick="back();" class="btn btn-info">返回</button>
		</p>
		<form method="post" id="saveform" name="saveform" action="/index.php?s=/index/parse.html" style="width:100%; height: 88%;">
			<!--预览也要用 用来解析表单-->
			<input type="hidden" name="fields" id="fields" value="0">
			<!--要提交到服务器的-->
			<input type="hidden" name="type" id="leipi_type" value="save">
			<input type="hidden" name="formid" id="leipi_formid" value="1">
			<input type="hidden" name="ids" id="ids" value="${ids}">
			<input type="hidden" name="strContent" id="strContent">
			<textarea  name="parse_form" id="leipi_parse_form" style="display:none;"></textarea>		
			<script id="myTemplateDesign" type="text/plain" style="width:100%; height: 100%;">${strContent}</script>
		</form>
	</div>
</body>
<script type="text/javascript">
	//加载页面
	$(function(){
		
	});	

	var templateEditor = UE.getEditor('myTemplateDesign',{
        //allowDivTransToP: false,//阻止转换div 为p
        toolleipi:true,//是否显示，设计器的 toolbars
        textarea: 'design_content',   
        //这里可以选择自己需要的工具按钮名称,此处仅选择如下五个
        toolbars:[[
        'fullscreen', 'source', '|', 'undo', 'redo', '|','bold', 'italic', 'underline', 'fontborder', 'strikethrough',  'removeformat', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist','|', 'fontfamily', 'fontsize', '|', 'indent', '|', 'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|',  'link', 'unlink',  '|',  'horizontal',  'spechars',  'wordimage', '|', 'inserttable', 'deletetable',  'mergecells',  'splittocells']],
        //focus时自动清空初始化时的内容
        //autoClearinitialContent:true,
        //关闭字数统计
        wordCount:false,
        //关闭elementPath
        elementPathEnabled:false,
        //默认的编辑区域高度
        initialFrameHeight:'100%'
        ,iframeCssUrl:"bootstrap/css/bootstrap.css" //引入自身 css使编辑器兼容你网站css
        //更多其他参数，请参考ueditor.config.js中的配置项
    });

	var templateDesign = {
		/*执行控件*/
		exec : function (method) {
			templateEditor.execCommand(method);
		},
		/*
		    Javascript 解析模板内容
		    template 编辑器里的Html内容
		    fields 字段总数
		*/
		parse_form:function(template,fields){
		    //正则  radios|checkboxs|select|yesornoselect 匹配的边界 |--|  因为当使用 {} 时js报错
		    var preg =  /(<label>{.*?<span(((?!<span).)*leipiplugins=\"(radios|checkboxs|yesornoselect|number|date|text|textarea)\".*?)>(.*?)<\/span>}<\/label>|<(img|input|textarea).*?(<\/select>|<\/textarea>|\/>))/gi;
		    var preg_attr =/(\w+)=\"(.?|.+?)\"/gi;
		    var preg_group =/<input.*?\/>/gi;
		    if(!fields) fields = 0;
		
		    var template_parse = template,template_data = new Array(),add_fields=new Object(),checkboxs=0;
		
		    var pno = 0;
		    template.replace(preg, function(plugin,p1,p2,p3,p4,p5,p6){
		        var parse_attr = new Array(),attr_arr_all = new Object(),name = '', select_dot = '' , is_new=false;
		        var p0 = plugin;
		        var tag = p6 ? p6 : p4;
		        //alert(tag + " \n- t1 - "+p1 +" \n-2- " +p2+" \n-3- " +p3+" \n-4- " +p4+" \n-5- " +p5+" \n-6- " +p6);
		        if(tag == 'radios' || tag == 'checkboxs' || tag == 'yesornoselect')
		        {
		            plugin = p2;
		        }else if(tag == 'select')
		        {
		            plugin = plugin.replace('|-','');
		            plugin = plugin.replace('-|','');
		        }
		        plugin.replace(preg_attr, function(str0,attr,val) {
	                if(attr=='name')
	                {
	                    if(val=='leipiNewField')
	                    {
	                        is_new=true;
	                        fields++;
	                        val = 'data_'+fields;
	                    }
	                    name = val;
	                }
	                
	                if(tag=='select' && attr=='value')
	                {
	                    if(!attr_arr_all[attr]) attr_arr_all[attr] = '';
	                    attr_arr_all[attr] += select_dot + val;
	                    select_dot = ',';
	                }else
	                {
	                    attr_arr_all[attr] = val;
	                }
	                var oField = new Object();
	                oField[attr] = val;
	                parse_attr.push(oField);
		        }) 
		        if(tag =='checkboxs' || tag =='yesornoselect') /*复选组  多个字段 */
		        {
		            plugin = p0;
		            plugin = plugin.replace('|-','');
		            plugin = plugin.replace('-|','');
		            var name = 'checkboxs_'+checkboxs;
		            attr_arr_all['parse_name'] = name;
		            attr_arr_all['name'] = '';
		            attr_arr_all['value'] = '';
		            
		            attr_arr_all['content'] = '<label>{请选择'+attr_arr_all['title']+'<span leipiplugins="'+tag+'" name="'+attr_arr_all['name']+
                	'" title="'+attr_arr_all['title']+'" code="'+attr_arr_all['code']+'" inputcode="'+attr_arr_all['inputcode']+'" id="'+attr_arr_all['id']+
                	'" displaytype="'+attr_arr_all['displaytype']+'" attrtype="'+attr_arr_all['attrtype']+'" codetype="'+attr_arr_all['codetype']+'" attrkind="'+attr_arr_all['attrkind']+
                	'" prefix="'+attr_arr_all['prefix']+'" suffix="'+attr_arr_all['suffix']+'" notnullchecked="'+attr_arr_all['notnullchecked']+
                	'" mustselectchecked="'+attr_arr_all['mustselectchecked']+'" statflgchecked="'+attr_arr_all['statflgchecked']+
                	'" printflgchecked="'+attr_arr_all['printflgchecked']+'" style="display:none;">';
		            var dot_name ='', dot_value = '';
		            p5.replace(preg_group, function(parse_group) {
		                var is_new=false,option = new Object();
		                parse_group.replace(preg_attr, function(str0,k,val) {
		                    if(k=='name')
		                    {
		                        if(val=='leipiNewField')
		                        {
		                            is_new=true;
		                            fields++;
		                            val = 'data_'+fields;
		                        }
		
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
		                //if(!option['checked']) option['checked'] = '';
		                var disabled = ' disabled="true"';
		                var checked = option['checked'] !=undefined ? 'checked="checked"' : '';
		                attr_arr_all['content'] +='<input type="checkbox" name="'+option['name']+'" value="'+option['value']+'"  '+checked + disabled+'/>'+option['value']+'&nbsp;';
		
		                if(is_new)
		                {
		                    var arr = new Object();
		                    arr['name'] = option['name'];
		                    arr['leipiplugins'] = attr_arr_all['leipiplugins'];
		                    add_fields[option['name']] = arr;
		
		                }
		
		            });
		            attr_arr_all['content'] += '</span>}</label>';
		
		            //parse
		            template = template.replace(plugin,attr_arr_all['content']);
		            template_parse = template_parse.replace(plugin,'{'+name+'}');
		            template_parse = template_parse.replace('{|-','');
		            template_parse = template_parse.replace('-|}','');
		            template_data[pno] = attr_arr_all;
		            checkboxs++;
		
		         }else if(name)
		        {
		            if(tag =='radios') /*单选组  一个字段*/
		            {
		                plugin = p0;
		                plugin = plugin.replace('|-','');
		                plugin = plugin.replace('-|','');
		                attr_arr_all['value'] = '';
		                attr_arr_all['content'] = '<label>{请选择'+attr_arr_all['title']+'<span leipiplugins="radios" name="'+attr_arr_all['name']+
		                	'" title="'+attr_arr_all['title']+'" code="'+attr_arr_all['code']+'" inputcode="'+attr_arr_all['inputcode']+'" id="'+attr_arr_all['id']+
		                	'" displaytype="'+attr_arr_all['displaytype']+'" attrtype="'+attr_arr_all['attrtype']+'" codetype="'+attr_arr_all['codetype']+'" attrkind="'+attr_arr_all['attrkind']+
		                	'" prefix="'+attr_arr_all['prefix']+'" suffix="'+attr_arr_all['suffix']+'" notnullchecked="'+attr_arr_all['notnullchecked']+
		                	'" mustselectchecked="'+attr_arr_all['mustselectchecked']+'" statflgchecked="'+attr_arr_all['statflgchecked']+
		                	'" printflgchecked="'+attr_arr_all['printflgchecked']+'" style="display:none;">';
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
		                    //if(!option['checked']) option['checked'] = '';
		                    var disabled = ' disabled="true"';
		                    var checked = option['checked'] !=undefined ? 'checked="checked"' : '';
		                    attr_arr_all['content'] +='<input type="radio" name="'+attr_arr_all['name']+'" value="'+option['value']+'"  '+checked+disabled+'/>'+option['value']+'&nbsp;';
		
		                });
		                attr_arr_all['content'] += '</span>}</label>';
		
		            }else
		            {
		                attr_arr_all['content'] = is_new ? plugin.replace(/leipiNewField/,name) : plugin;
		            }
		            //attr_arr_all['itemid'] = fields;
		            //attr_arr_all['tag'] = tag;
		            template = template.replace(plugin,attr_arr_all['content']);
		            template_parse = template_parse.replace(plugin,'{'+name+'}');
		            template_parse = template_parse.replace('{|-','');
		            template_parse = template_parse.replace('-|}','');
		            if(is_new)
		            {
		                var arr = new Object();
		                arr['name'] = name;
		                arr['leipiplugins'] = attr_arr_all['leipiplugins'];
		                add_fields[arr['name']] = arr;
		            }
		            template_data[pno] = attr_arr_all;  
		        }
		        pno++;
		    })
		    var parse_form = new Object({
		        'fields':fields,//总字段数
		        'template':template,//完整html
		        'parse':template_parse,//控件替换为{data_1}的html
		        'data':template_data,//控件属性
		        'add_fields':add_fields//新增控件
		    });
		    return JSON.stringify(parse_form);
		},
		/*type  =  save 保存设计 versions 保存版本  close关闭 */
		fnCheckForm : function ( type ) {
		    if(templateEditor.queryCommandState( 'source' ))
		    	templateEditor.execCommand('source');//切换到编辑模式才提交，否则有bug
		        
		    if(templateEditor.hasContents()){
		    	templateEditor.sync();/*同步内容*/
		        
		        //--------------以下仅参考-----------------------------------------------------------------------------------------------------
		        var type_value='',formid=0,fields=$("#fields").val(),formeditor='';
				var ids = $('#ids').val();
		        if( typeof type!=='undefined' ){
		            type_value = type;
		        }
		        //获取表单设计器里的内容
		        formeditor=templateEditor.getContent();
		        //解析表单设计器控件
		        var parse_form = this.parse_form(formeditor,fields);
		        
		         //异步提交数据
		         $.ajax({
		            type: 'POST',
		            url : '<%=basePath %>emrs/emrTemplate/saveTemplate.action',
		            //dataType : 'json',
		            data : {'type' : type_value,'formid':formid,'parse_form':parse_form,'ids':ids},
		            success : function(data){
		              if(data == 'success'){
		            	  $.messager.confirm('确认', '保存成功，是否继续编辑？', function(res){//提示是否删除
							if (res){
								replace('<%=basePath %>emrs/emrTemplate/toTemplate.action?menuAlias=${menuAlias}&ids='+ids,'模板编辑');
			            	}else{
			            		back(); 
			            	}
		              	});
		              }
		              else{
		            	  $.messager.alert('提示','保存失败！请确认您输入的模板符合规范！'); 
		            	  setTimeout(function(){$(".messager-body").window('close')},3500);
		              }
		            }
		        });
		        
		    } else {
		    	$.messager.alert('提示','内容不能为空！')
		    	setTimeout(function(){$(".messager-body").window('close')},3500);
		        $('#submitbtn').button('reset');
		        return false;
		    }
		} ,
		/*预览表单*/
		fnReview : function (){
		    if(templateEditor.queryCommandState( 'source' ))
		    	templateEditor.execCommand('source');/*切换到编辑模式才提交，否则部分浏览器有bug*/
		        
		    if(templateEditor.hasContents()){
		    	templateEditor.sync();       /*同步内容*/
		
		        /*设计form的target 然后提交至一个新的窗口进行预览*/
		        document.saveform.target="mywin";
		        window.open('','mywin',"menubar=0,toolbar=0,status=0,resizable=1,left=0,top=0,scrollbars=yes,width=" +(screen.availWidth-10) + ",height=" + (screen.availHeight-50) + "\"");
				$('#strContent').val(templateEditor.getContent());
		        document.saveform.action="<%=basePath%>emrs/emrTemplate/toPreview.action";
		        document.saveform.submit(); //提交表单
		    } else {
		    	$.messager.alert('提示','内容不能为空！');
		    	setTimeout(function(){$(".messager-body").window('close')},3500);
		        return false;
		    }
		}
	};
	
	function back(){
		replace('<%=basePath %>emrs/emrTemplate/toViewTemplateList.action?menuAlias=${menuAlias}','模板编辑');
	}
		
	
	function replace(url,title){
		if (window.parent.$('#tabs').tabs('exists',title)){
			window.parent.$('#tabs').tabs('select', title);//选中并刷新
			var currTab = window.parent.$('#tabs').tabs('getSelected');
			if (url != undefined) {
				window.parent.$('#tabs').tabs('update', {
					tab : currTab,
					options : {
						content : window.parent.createFrame(url)
					}
				});
			}
		} else {
//	 		var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
			window.parent.$('#tabs').tabs('add',{
				title : title,
				content : window.parent.createFrame(url),
				closable:true
			});
			window.parent.tabClose();
		}
	}
</script>
