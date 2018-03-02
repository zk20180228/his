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

</head>
<body>
	<div class="easyui-panel" id="panelEast" data-options="iconCls:'icon-form',fit:true" style="width: 100%;border:0">
		<div>
    		<form id="editForm" method="post" ">
				<input type="hidden" id="id" name="id" value="${menu.id}">
				<input type="hidden" id="morder" name="morder" value="${menu.morder}">
				<input type="hidden" id="path" name="path" value="${menu.path}">
				<input type="hidden" id="parentcode" name="parentcode" value="${menu.parentcode}">
				<input type="hidden" id="parentpath" name="parentpath" value="${menu.parentpath}">
				<input type="hidden" id="type" name="type" value="1">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin:5px auto 0;">
					<tr>
						<td class="honry-lable">上级栏目：</td>
		    			<td class="honry-info"><input id="parent" name="parent" style="width: 300"  value="${menu.parentcode}" />
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">栏目名称：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="name" name="name" style="width: 300" value="${menu.name}" data-options="required:true,missingMessage:'请输入栏目名称'"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">栏目Code：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="code" name="code" style="width: 300" value="${menu.code}" data-options="required:true,missingMessage:'请输入栏目code'"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">栏目说明：</td>
		    			<td class="honry-info"><input class="easyui-textbox" data-options="multiline:true" id="explain" name="explain" style="width: 300" value="${menu.explain}" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">发布权限：</td>
		    			<td class="honry-info">
		    			<input type="hidden"  id="mpublish" name = "mpublish" value="${menu.mpublish}"/>
		    			<input type="text"  class="easyui-textbox" data-options="buttonIcon:'icon-folder'"  id="mmpublish"  style="width: 300" value="${mmpublish}"  /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">审核权限：</td>
		    			<td class="honry-info">
		    			<input type="hidden" id="mcheck" name = "mcheck" value="${menu.mcheck}"/>
		    			<input type="text"  class="easyui-textbox" data-options="buttonIcon:'icon-folder'" id="mmcheck"  style="width: 300" value="${mmcheck}"   /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">直接发布:</td>
				    	<td><input type="hidden" id="publishdirtHidden" name = "publishdirt" value="${menu.publishdirt}"/>
				    	<input type="checkBox" id="publishdirt" onclick="javascript:onclickBoxPublish('publishdirt')"/>
			    		</td>
					</tr>
	    			<tr>
						<td class="honry-lable">停用标志:</td>
				    	<td><input type="hidden" id="stopFlgHidden" name="stop_flag" value="${menu.stop_flag}"/>
				    	<input type="checkBox" id="stopFlg" onclick="javascript:onclickBox('stopFlg')"/>
			    		</td>
					</tr>
<!-- 	    			<tr> -->
<!-- 						<td class="honry-lable">开启评论:</td> -->
<%-- 				    	<td><input type="hidden" id="mcommentHidden" name="mcomment" value="${menu.mcomment}"/> --%>
<!-- 				    	<input type="checkBox" id="mcomment" onclick="javascript:onclickBoxComment('mcomment')"/> -->
<!-- 			    		</td> -->
<!-- 					</tr> -->
		    	</table>
			    <div style="text-align:center;padding:20px">
			    	<a id="save" href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
	    	</form>
	    </div>
	</div>    
	<div id="mpublish-window"><iframe class="iframeid" style = 'width:100%;height:100%;margin: 0;padding: 0;border: none;' ></iframe></div>
	<div id="mview-window"><iframe  class="iframeid" style = 'width:100%;height:100%;margin: 0;padding: 0;border: none;' ></iframe></div>
	<div id="mcheck-window"><iframe class="iframeid" style = 'width:100%;height:100%;margin: 0;padding: 0;border: none;' ></iframe></div>
	<script type="text/javascript">
		//加载页面
		var publishDirt = '${menu.publishdirt}';
		var stopFlag = '${menu.stop_flag}';
		var comment = '${menu.mcomment}';
		if(publishDirt == "1"){
//			console.log($('#mmcheck').parent().children())
			setTimeout(function(){
				$('#mmcheck').textbox('disable');
				$('#publishdirt').attr('checked',true);
			},100)
		}
		if(stopFlag == "1"){
			$('#stopFlg').attr('checked',true);
		}
		if(comment == "1"){
			$('#mcomment').attr('checked',true);
		}
		
		$(function(){
			$('#mview-window').window({    
			    width:'80%',    
			    height:'90%',    
			    modal:true,
			    title:"浏览授权"
			}).window('close');
			$('#mpublish-window').window({    
				width:'80%',    
			    height:'90%',     
			    modal:true,
			    title:"发布授权"
			}).window('close'); 
			
			$('#mcheck-window').window({    
				width:'80%',    
			    height:'90%',     
			    modal:true,
			    title:"审核授权"
			}).window('close');
			
			//加载父节点
			 $('#parent').combotree({ 
				url: '<%=basePath%>oa/patMenuManager/showTree.action',   
				required: true, 
				missingMessage:'请选择所属栏目',
				width:'300',
				onBeforeCollapse:function(node){
					if(node.id=="1"){
						return false;
					}
			    },
			    editable:false,
			});
			
		    $("#mmpublish").textbox({onClickButton:function(){
		    	$("#mpublish-window .iframeid")[0].src = '<%=basePath%>oa/patMenuManager/addRight.action?type=1'
	    		$('#mpublish-window').window('open');
		    }})
		    
		
		    $("#mmview").textbox({onClickButton:function(){
		    	$("#mview-window .iframeid")[0].src = '<%=basePath%>oa/patMenuManager/addRight.action?type=3'
	    		$('#mview-window').window('open');
		    }})
		
		    $("#mmcheck").textbox({onClickButton:function(){
		    	$("#mcheck-window .iframeid")[0].src = '<%=basePath%>oa/patMenuManager/addRight.action?type=2'
	    		$('#mcheck-window').window('open');
		    }})
		    
		});
		
	    
		//表单提交
		function submitForm(){
			$('#editForm').form('submit',{  
				url:'<%=basePath%>oa/patMenuManager/saveMenu.action', 
	        	onSubmit:function(){
					if (!$('#editForm').form('validate')) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						return false;
					}
					var code = $("#code").textbox('getText');
					var re =  /[\u4e00-\u9fa5]/;  
					 if(re.test(code)){
						 $.messager.alert("提示","栏目code不能为中文","info");
						 return false;
					 }
	        	},
	        	success:function(dataMap){
					var data =  JSON.parse(dataMap);
	        		if(dataMap.resCode=="success"){
						$.messager.alert('提示',data.resMsg);
						closeLayout();
	        		}else{
	        			$.messager.alert('提示',data.resMsg);
	        			closeLayout();
	        		}
	        			reload();
	    		 },
	        	error:function(dataMap){
	        		$.messager.alert('提示','保存失败');
	        		closeLayout();
	        	}
	   		}); 
		}
	    
		//清除页面填写信息
		function clear(){
			$('#editForm').form('reset');
			$('#mcheck').val('');
			$('#mpublish').val('');
			$('#mview').val('');
			$('#mmview').textbox('setValue','');
			$('#mmpublish').textbox('setValue','');
			$('#mmcheck').textbox('setValue','');
		}
		
		//发布复选框赋值
		function onclickBoxPublish(id){
			if($('#'+id).is(':checked')){
				$('#'+id+'Hidden').val(1);
				$('#mmcheck').textbox('setValue',"");
				$('#mmcheck').textbox('disable');
				$('#mcheck').val("");
			}else{
				$('#'+id+'Hidden').val(0);
				$('#mmcheck').textbox('enable');
			}
		}
		//停用复选框赋值
		function onclickBox(id){
			if($('#'+id).is(':checked')){
				$('#'+id+'Hidden').val(1);
			}else{
				$('#'+id+'Hidden').val(0);
			}
		}
		//评论复选框赋值
		function onclickBoxComment(id){
			if($('#'+id).is(':checked')){
				$('#'+id+'Hidden').val(1);
			}else{
				$('#'+id+'Hidden').val(0);
			}
		}
		
		//复选框初始化
		function cheHid(){
			if($('#stopFlgHidden').val()==1){
				$('#stopFlg').prop("checked", true); 
			}
		}
		
		function submit(){
			submitForm();
		}
		function collapseTree() {//关闭树
			$('#parent').combotree('collapseAll');
		}
		
	</script>
<%-- 	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script> --%>
	</body>
</html>