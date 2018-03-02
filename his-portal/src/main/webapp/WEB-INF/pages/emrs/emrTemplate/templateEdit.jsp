<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>模板添加/编辑</title>
<base href="<%=basePath%>">
	<%@ include file="/common/metas.jsp" %>
</head>
<body>
	<div class="easyui-layout" data-options="region:'center',fit:true">
		<div data-options="region:'center',fit:true"  style="padding:10px">
			<form id="editForm" method="post" style="width: 100%">
				<input type="hidden" id="id" name="emrTemplate.id" value="${emrTemplate.id }"/>
				<input type="hidden" id="type" name="emrTemplate.tempErtype" value="${erType }"/>
				<input type="hidden" id="tempDiagid" name="emrTemplate.tempDiagid" value="${emrTemplate.tempDiagid }"/>
				<input type="hidden" id="tempChkflg" name="emrTemplate.tempChkflg" value="${tempChkflg }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="margin-left:auto;margin-right:auto;width:90% ；" >
					<tr>
						<td class="honry-lable">名称:</td>
		    			<td class="honry-info" ><input class="easyui-textbox" id="tempName" name="emrTemplate.tempName" value="${emrTemplate.tempName }" data-options="required:true" style="width: 90%"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">自定义码:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="inputCode" name="emrTemplate.inputcode" value="${emrTemplate.inputcode }" style="width: 90%"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">模板分类:</td>
		    			<td class="honry-info"><input id="tempType" name="emrTemplate.tempType" value="${emrTemplate.tempType }" data-options="editable:false" style="width: 90%"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">诊断名称:</td>
		    			<td class="honry-info">
		    			<input class="easyui-textbox" id="tempDiag" name="emrTemplate.tempDiag" value="${emrTemplate.tempDiag }" data-options="editable:false" style="width: 90%"/>
		    			<a href="javascript:querybtn();" class="easyui-linkbutton">选择</a></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">书写类型:</td>
		    			<td class="honry-info"><input id="tempWritetype" name="emrTemplate.tempWritetype" value="${emrTemplate.tempWritetype }" data-options="editable:false" style="width: 90%"/></td>
	    			</tr>
				</table>
				<div data-options="region:'south'" style="text-align:center;padding:5px ;height: 30px">
					<shiro:hasPermission name="${menuAlias}:function:add">
				    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
				    </shiro:hasPermission>
				    	<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
				    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	//选中节点的level
	var leve;
	var node = $('#tDt').tree('getSelected');
	leve = node.attributes.level;
	//加载页面
	$(function(){
		$('#tempType').combobox({
			valueField:'id',    
		    textField:'text',
		    data: [{
		    	id: '0',
		    	text: '通用'
			},{
				id: '1',
				text: '科室'
			},{
				id: '2',
				text: '个人'
			}]
		});
		$('#tempWritetype').combobox({
			valueField:'id',    
		    textField:'text',
		    data: [{
		    	id: '0',
		    	text: '不限次书写'
			},{
				id: '1',
				text: '仅首次单次书写'
			},{
				id: '2',
				text: '单次书写'
			}]
		});
	});
	/**
	* 弹出诊断名称选择窗口
	* @param textId 页面上commbox的的id
	* @version 1.0
	*/
	function querybtn(){
		popWinToDiagId();
	}
	/**
	* 弹出诊断名称选择窗口
	* @param textId 页面上诊断名的输入框id，诊断code的隐藏inpute的id
	* @version 1.0
	*/
	function popWinToDiagId(){
		var tempWinPath = "<%=basePath%>popWin/popWinBusinessIcd10/toBusinessIcd10PopWinDB.action?textId=tempDiag,tempDiagId";
		window.open (tempWinPath,'newwindow',' left=300,top=150,width='+ (screen.availWidth - 400) +',height='+ (screen.availHeight-310) +',scrollbars,resizable=yes,toolbar=no')
	}
		/**
		 * 清除页面填写信息
		 */
			function clear(){
				$('#editForm').form('reset');
			}
		/* 
		 * 关闭界面
		 */
			function closeLayout(){
				$('#temWins').dialog('close'); 
				$('#list').datagrid('reload');
			}
			
			/**
		 * 判断添加/修改表单提交
		 */
		function submit(){ 
			 var url;
			 var id = $('#id').val().toString();
			 if(id == null || id == ""){
				 url = "<%=basePath %>emrs/emrTemplate/add.action"; 
				 sub(url);
			 }else{
				 url = "<%=basePath %>emrs/emrTemplate/edit.action";
				 sub(url);
			    }
	    }	
			/**
		 * 表单提交
		 */
			function sub(url) {
				$('#editForm').form('submit',{  
		        	url: url,  
		        	onSubmit:function(){
						if (!$('#editForm').form('validate')) {
							$.messager.alert("提示","验证不通过");
							setTimeout(function(){$(".messager-body").window('close')},3500);
							return false;
						}
					   $.messager.progress({text:'保存中，请稍后...',modal:true});
		        	},  
			        success:function(data){  
			        	$.messager.progress('close');
			        	closeLayout();
			        	replace('<%=basePath %>emrs/emrTemplate/toTemplate.action?menuAlias=${menuAlias}&ids='+data,'模板编辑');
			        },
					error : function(data) {
						$.messager.progress('close');
						$.messager.alert("提示",'保存失败！');	
					}							         
		   		}); 
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
//			 		var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
					window.parent.$('#tabs').tabs('add',{
						title : title,
						content : window.parent.createFrame(url),
						closable:true
					});
					window.parent.tabClose();
				}
			}
	</script>
</body>