<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>病历审签界面</title>
<base href="<%=basePath%>">
	<%@ include file="/common/metas.jsp" %>
	<script type="text/javascript" src="<%=basePath%>ueditor1_4_3_2/ueditor.config.js"></script>
	<script type="text/javascript" src="<%=basePath%>ueditor1_4_3_2/ueditor.all.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="<%=basePath%>ueditor1_4_3_2/formdesign/leipi.formdesign.v5.js?2010"></script>
	
</head>
<body>
	<div id="divLayout2" class="easyui-layout" fit="true"> 
		<div id="t" data-options="region:'north'" style="width: 80%;height: 60%;">
			<div id="test" style="height: 95%">
				<form method="post" id="saveform" name="saveform" action="/index.php?s=/index/parse.html" >
					<input type="hidden" id="id" name="emrMain.id" value='${emrMain.id }'>
					<input type="hidden" id="advice" name="emrMain.emrChiefAdvice" value='${emrMain.emrChiefAdvice }'>
					<input type="hidden" id="strContent" name="emrMain.strContent">
					<input type="hidden" id="emrState" name="emrMain.emrState" value='${emrMain.emrState }'>
					<script id="myTemplateDesign" type="text/plain" ></script>
				</form>
			</div>
		</div>
		<div data-options="region:'center',border:false,title:'审签意见'" style="width: 80%;height: 35%;">
			<div class="easyui-layout" fit="true" data-options="border:false"> 
				<div style="height: 100;width: 100%;"  data-options="region:'center',border:false">
					<input id="vice" class="easyui-textbox" data-options="multiline:true" value='${emrMain.emrChiefAdvice }' style="width:100%;height: 100%"> 
				</div>
			</div>
		</div>
		<div id="adviceButton" data-options="region:'south'" style="width: 80%;height: 30px;text-align: center;">
			<a id="tick" href="javascript:void(0)" onclick="tick(3)" class="easyui-linkbutton" data-options="iconCls:'icon-tick',plain:true">通过</a>
			<a id="untick" href="javascript:void(0)" onclick="tick(0)" class="easyui-linkbutton" data-options="iconCls:'icon-shenheshibai',plain:true">打回</a>
		</div>
	</div>
	<script type="text/javascript">
	var newContent = '';
	var dels = '<span style="text-decoration:line-through;border-bottom:4px double red;">';
	var dele = '</span>'
	var adds = '<span style="border-bottom:4px double red;">';
	var adde = '</span>'
	/**
	* 创建文本编辑器
	* @author  yeguanqun
	* @date 2016-4-12 10:53
	* @version 1.0
	*/
	var templateEditor = UE.getEditor('myTemplateDesign',{
	    //allowDivTransToP: false,//阻止转换div 为p
	    toolleipi : false,//是否显示，设计器的 toolbars
	    textarea: 'design_content',   
	    //这里可以选择自己需要的工具按钮名称,此处仅选择如下五个
	    toolbars:[[
//	    'source','fullscreen'
	    ]],
	    //focus时自动清空初始化时的内容
	    //autoClearinitialContent:true,
	    //关闭字数统计
	    wordCount:false,
	    //关闭elementPath
	    elementPathEnabled:false,
	    autosave : false,
	    //关闭右键
	    enableContextMenu : false,
	    allowDivTransToP : false,
	    //默认的编辑区域高度
	    initialFrameHeight:'100%',
	    initialFrameWidth:'100%'
	    ///,iframeCssUrl:"css/bootstrap/css/bootstrap.css" //引入自身 css使编辑器兼容你网站css
	    //更多其他参数，请参考ueditor.config.js中的配置项
	});
	
	
	$(function(){
		var state = ${emrMain.emrState};
		if(state == 3){
			document.title = '已审签病历查看';
			$('#vice').textbox('disable');
			$('#tick').linkbutton('disable');
			$('#untick').linkbutton('disable');
		}
		var strContent = '${emrMain.strContent }';
		var content = strContent.replace("'",'');
		if((typeof $('#id').val() == undefined) || $('#id').val() == null|| $('#id').val() == '' ){
			//正则  radios|checkboxs|select|yesornoselect 匹配的边界 |--|  因为当使用 {} 时js报错
		    var preg =  /(<label>{.*?<span(((?!<span).)*leipiplugins=\"(radios|checkboxs|yesornoselect|number|date|text|textarea)\".*?)>(.*?)<\/span>}<\/label>|<(img|input|textarea).*?(<\/textarea>|\/>))/gi;
		    var preg_attr =/(\w+)=\"(.?|.+?)\"/gi;
		    var preg_group =/<input.*?\/>/gi;
		    content = strContent.replace(preg, function(plugin,p1,p2,p3,p4,p5,p6){
		        var attr_arr_all = new Object();
		        var p0 = plugin;
		        var tag = p6 ? p6 : p4;
		        p1 = p1.replace('style="display:none;"','');
		        //alert(tag + " \n- t1 - "+p1 +" \n-2- " +p2+" \n-3- " +p3+" \n-4- " +p4+" \n-5- " +p5+" \n-6- " +p6);
		        if(tag == 'radios' || tag == 'checkboxs' || tag == 'yesornoselect')
		        {
		            return p1;
		        }
		        plugin.replace(preg_attr, function(str0,attr,val) {
	                    attr_arr_all[attr] = val;
		        }); 
		        //通过控件编码从后台查询数据中取出相应数据
		        var code = attr_arr_all['code'];
		        var attrkind = attr_arr_all['attrkind'];
		        if(attrkind == 0){
		        	return p1;
		        }
		        var val = eval('('+$('#sysAttr').val()+')');
		        var value = val[code];
		       if(tag == 'text'){
		           return p1.replace(' readonly="readonly"',' value="'+value+'" readonly="readonly"');
		       }
		       if(tag == 'textarea'){
		    	   return p1.replace(' readonly="readonly">',' readonly="readonly">'+value);
		       }
		       if(tag == 'number'){
		           return p1.replace(' readonly="readonly"',' value="'+value+'" readonly="readonly"');
		       }
		       if(tag == 'date'){
		    	   return p1.replace(' readonly="readonly"',' value="'+value+'" readonly="readonly"');
		       }
		    });
		}
		//判断ueditor 编辑器是否创建成功
		templateEditor.addListener("ready", function () {
			// editor准备好之后才可以使用
			templateEditor.execCommand("fullscreen");
			templateEditor.setContent(content);
			var state = ${emrMain.emrState};
			if(state == 3){
				templateEditor.setDisabled('fullscreen');
			}else{
				templateEditor.setEnabled();
			}
        });
	});
	
	
	/**
	* @param s1 原版
	* @param s2 修改版
	*/
	function setNewContent(s1,s2){
		var tempContent = '';
		var str_befor_lg_s1 = '';
		var tag_s1 = '';
		var index_lg_s1 = 0;
		var index_rg_s1 = 0;
		var index_label_s1 = 0;
		var str_befor_lg_s2 = '';
		var tag_s2 = '';
		var index_lg_s2 = 0;
		var index_rg_s2 = 0;
		var index_label_s2 = 0;
		/**
		*得到s2真正tag前的字符串和真正tag
		*/
		index_lg_s2 = s2.indexOf('<');
		index_rg_s2 = s2.indexOf('>');
		tag_s2 = s2.substring(index_lg_s2, index_rg_s2 + 1);
		if(tag_s2.indexOf('<br/>') != -1){
			str_befor_lg_s2 += beforlgs2(s2);
			s2 = s2.substring(str_befor_lg_s2.length, s2.length);
			index_lg_s2 = s2.indexOf('<');
			index_rg_s2 = s2.indexOf('>');
			tag_s2 = s2.substring(index_lg_s2, index_rg_s2 + 1);
		}else{
			str_befor_lg_s2 = s2.substring(0, index_lg_s2);
			s2 = s2.substring(str_befor_lg_s2.length,s2.length);
		}
		/**
		*得到s1真正tag前的字符串和真正tag
		*/
		index_lg_s1 = s1.indexOf(tag_s2);
		index_rg_s1 = index_lg_s1 + tag_s2.length;
		str_befor_lg_s1 = s1.substring(0, index_lg_s1);
		s1 = s1.substring(str_befor_lg_s1.length, s1.length);
		/**
		*对比s1和s2tag前的字符串
		*/
		if(str_befor_lg_s2 != str_befor_lg_s1){
			if(str_befor_lg_s2.length == 0){
				str_befor_lg_s1 = dels + str_befor_lg_s1 + dele;
			}else if(str_befor_lg_s1.length == 0){
				str_befor_lg_s2 = adds + str_befor_lg_s2 + adde;
			}else{
				str_befor_lg_s1 = dels + str_befor_lg_s1 + dele;
				str_befor_lg_s2 = adds + str_befor_lg_s2 + adde;
			}
		}else{
			str_befor_lg_s1 = '';
		}
		/**
		*将变动记录保存
		*/
		tempContent = str_befor_lg_s1 + str_befor_lg_s2;
		/**
		*判断真正的tag是否为控件
		*/
		if(tag_s2.indexOf('<label>') != -1){
			index_label_s2 = s2.indexOf('</label>') + 8;
			tag_s2 = s2.substring(0, index_label_s2);
// 			tag_s1 = s1.substring(0, index_label_s2);
		}
		/**
		* 减掉tag
		*/
		s1 = s1.substring(tag_s2.length, s1.length);
		s2 = s2.substring(tag_s2.length, s2.length);
		tempContent += tag_s2;
		newContent += tempContent;
		/**
		* 判断是否需要递归
		*/
		if(s1.length > 0 || s2.length > 0 ){
			setNewContent(s1,s2);
		}
	}
	/**
	* @param s2 <>后文字  包括<>
	*/
	function beforlgs2(s2){
		var index_lg = 0;
		var index_rg = 0;
		var tag = '';
		var beforbr = '';
		var index_br = 0;
		index_lg = s2.indexOf('<');
		index_rg = s2.indexOf('>');
		beforbr = s2.substring(0, index_lg);
		tag = s2.substring(index_lg, index_rg + 1);
		if(tag.indexOf('<br/>') == -1){
			return beforbr;
		}else{
			beforbr += '<br/>';
			s2 = s2.substring(beforbr.length, s2.length);
// 			beforlgs2(str,s2);
			return beforbr + beforlgs2(s2);
		}
	}
	
	
	/**
	* 提交审核
	*/
	function tick(state){
		templateEditor.sync();/*同步内容*/
		var ids = $('#id').val();
		//获取表单设计器里的内容
		var strContent = templateEditor.getContent();
		setNewContent('${emrMain.strContent }',strContent);
		$('#strContent').val(newContent);
		var advice = $('#vice').textbox('getValue');
		$('#advice').val(advice);
		$('#emrState').val(state);
		if(advice == ''){
			$.messager.confirm('确认对话框', '未填写审批意见，是否继续提交？', function(r){
				if (r){
					sub();
				}
			});
		}else{
			sub();
		}
	}
	/**
	* form提交
	*/
	function sub(){
		$('#saveform').form('submit', {
			url: "<%=basePath %>emrs/reviewEmrMain/conformEmrMain.action",
			success : function(data) {
				$.messager.progress('close');
				if(data == 'success'){
					$.messager.alert('提示',"保存成功");
					window.opener.reload();
					window.close();
				}
			},
			error : function(date) {
				$.messager.progress('close');
				$.messager.alert('提示',"保存失败");
			}
		});
	}
	
	</script>
</body>