<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>书写病历界面</title>
<base href="<%=basePath%>">
	<%@ include file="/common/metas.jsp" %>
	<script type="text/javascript" src="<%=basePath%>ueditor1_4_3_2/ueditor.config.js"></script>
	<script type="text/javascript" src="<%=basePath%>ueditor1_4_3_2/ueditor.all.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="<%=basePath%>ueditor1_4_3_2/formdesign/leipi.formdesign.v5.js?2010"></script>
	
</head>
<body>
	<div id="divLayout2" class="easyui-layout" fit="true"> 
		<div id="t" data-options="region:'center'" style="width: 80%;height: 100%">
				<form method="post" id="saveform" name="saveform" action="/index.php?s=/index/parse.html" >
					<input type="hidden" id="flag"  value="editMain">
					<input type="hidden" id="sysAttr" name="sysAttr" value='${sysAttr }'>
					<input type="hidden" id="id" name="emrMain.id" value='${emrMain.id }'>
					<input type="hidden" id="tempId" name="emrMain.tempId" value='${emrMain.tempId }'>
					<input type="hidden" id="erType" name="emrMain.emrType" value='${emrMain.emrType }'>
					<input type="hidden" id="strContent" name="strContent">
				    <script id="myTemplateDesign" type="text/plain"style="width: 100%;height: 100%" ></script>
			   	</form>
		</div>
		<div id="tt" data-options="region:'east',title:'工具箱',collapsible:true" style="width: 20%;height:100%;">
			<div id="ttt" class="easyui-tabs" data-options="fit:false,tabPosition:'bottom'" style="clear: both;height:100%;"> 
				<div  id="" style="padding: 5px 5px 5px 5px;height:95%;">   
					<div id="divLayout1" class="easyui-layout" fit=true> 				
						<div id="m"data-options="region:'north',split:true" style="width: 240px;height:58%;">
							<table id="toolboxInfo" style="padding: 5px 5px 5px 5px;"></table>		
						</div>
						<div id="n" data-options="region:'center'" style="width: 240px;height:35%;background-color: #EDEDED;">				
							<table id="toolbox" class="toolbox">
								<tr id="docPic">
									<td>
										&nbsp;图库
									</td>
								</tr>
								<tr id="specChar">
									<td>
										&nbsp;特殊字符
									</td>
								</tr>
								<tr id="commonWord">
									<td>
										&nbsp;常用词
									</td>
								</tr>
								<tr id="symptom">
									<td>
										&nbsp;医技症状
									</td>
								</tr>					
								<tr id="konwledgeBase">
									<td>
										&nbsp;知识库
									</td>
								</tr>					
							</table>
						</div>
					</div>			
			    </div>
	    	</div>
		</div>
	</div>
	<style type="text/css">
		.toolbox td{
			width:310px;
			height:22px;
		}
		.mouse_color{
			background-color: #CFCFCF;
			cursor:pointer;
		} 
	</style>
	<script type="text/javascript">
	/**
	* 鼠标移到的颜色
	*/
	$("#toolbox tr").mouseover(function(){
	$(this).addClass("mouse_color");
	});

	/**
	* 鼠标移出的颜色
	*/
	$("#toolbox tr").mouseout(function(){
	$(this).removeClass("mouse_color");
	}); 
	
	/**
	* 创建文本编辑器
	* @author  yeguanqun
	* @date 2016-4-12 10:53
	* @version 1.0
	*/
	var templateEditor = UE.getEditor('myTemplateDesign',{
	    toolleipi:true,//是否显示，设计器的 toolbars
	    textarea: 'design_content',   
	    //这里可以选择自己需要的工具按钮名称,此处仅选择如下五个
	    toolbars:[[
	    'fullscreen', 'source', '|', 'undo', 'redo', '|','bold', 'italic', 'underline', 'fontborder', 'strikethrough',  'removeformat', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist','|', 'fontfamily', 'fontsize', '|', 'indent', '|', 'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|',  'link', 'unlink',  '|',  'horizontal',  'spechars',  'wordimage', '|', 'simpleupload', 'scrawl', 'inserttable', 'deletetable',  'mergecells',  'splittocells']],
	    //focus时自动清空初始化时的内容
	    //autoClearinitialContent:true,
	    //关闭字数统计
	    wordCount:false,
	    //关闭elementPath
	    elementPathEnabled:false,
	    //默认的编辑区域高度
	    initialFrameHeight:'100%'
	});
	$(function(){
		var strContent = ${emrMain.strContent };
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
		        if(tag == 'radios' || tag == 'checkboxs' || tag == 'yesornoselect')
		        {
		            return p1;
		        }
		        plugin.replace(preg_attr, function(str0,attr,val) {
	                    attr_arr_all[attr] = val;
		        }); 
		        //通过空间编码从后台查询数据中取出相应数据
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
	        templateEditor.setContent(content);
        });
	});
	
	var templateDesign = {
			/*执行控件*/
			exec : function (method) {
				templateEditor.execCommand(method);
			},
			/*type  =  save 保存设计 versions 保存版本  close关闭 */
			fnCheckForm : function ( type ) {
			    if(templateEditor.queryCommandState( 'source' ))
			    	templateEditor.execCommand('source');//切换到编辑模式才提交，否则有bug
			        
			    if(templateEditor.hasContents()){
			    	templateEditor.sync();/*同步内容*/
			        //--------------以下仅参考-----------------------------------------------------------------------------------------------------
			        var type_value='';
					var ids = $('#id').val();
			        if( typeof type!=='undefined' ){
			            type_value = type;
			        }
			        //获取表单设计器里的内容
			        var strContent = templateEditor.getContent();
			        if(ids != null && ids != ''){
			        	window.parent.save(strContent,ids,'1');
			        }else{
			        	window.parent.save(strContent,$('#erType').val(),'2');
			        }
			    } else {
			    	$.messager.alert('提示','内容不能为空！');
			    	setTimeout(function(){$(".messager-body").window('close')},1500);
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
			        window.open('','mywin','menubar=no,toolbar=no,status=no,resizable=no,left='+(screen.availWidth-756)/2+',top=0,scrollbars=1,width=756,height=1086');
					$('#strContent').val(templateEditor.getContent());
			        document.saveform.action="<%=basePath%>emrs/writeMedRecord/toPreviewForInpatient.action";
			        document.saveform.submit(); //提交表单
			    } else {
			    	$.messager.alert('提示','内容不能为空！');
			    	setTimeout(function(){$(".messager-body").window('close')},1500);
			        return false;
			    }
			}
		};
	
	$("#toolbox td").click(function(){
		var trId = $(this).parent().attr("id");
		if(trId == 'konwledgeBase'){
			parent.Adddilog('在线知识库',"<%=basePath %>emrs/konwledgeBase/toSelectKonwledgeBaseView.action",'900px','600px');
		}else{
			$('#toolboxInfo').datagrid({  
			    fitColumns:true,
				singleSelect:true,
				url: '<%=basePath%>emrs/writeMedRecord/queryRecordInfo.action',	
				queryParams : {
					trId: trId
				},
				columns :[[  
							{field:'name',width:100}
					    ]],
			    onDblClickRow :function(rowIndex, rowData){
			    	if(trId == 'docPic'){//如果是图片则调用涂鸦按钮的点击事件，此方法是从开发者模式内找到，可能不稳定
			    		return $EDITORUI["edui107"]._onClick(event, this);
			    	}else{
			         var name = rowData.name;
			         templateEditor.execCommand('insertHtml',name);
			    	}
			    }    
			});
		}
	});

	</script>
</body>