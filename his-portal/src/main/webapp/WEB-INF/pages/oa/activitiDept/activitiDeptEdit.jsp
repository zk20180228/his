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
    		<form id="editForm" method="post">
    			<input type="hidden" id="dId" name="dId" value="${dId }">
				<input type="hidden" id="id" name="activitiDept.id" value="${activitiDept.id }">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin:10px auto 0;">
					<tr>
		    			<td class="honry-lable">科室名称:</td>
		    			<td class="honry-info"><input class="easyui-combobox" type="text" id="deptCode" name="activitiDept.deptCode" style="width: 300" value="${activitiDept.deptCode }" data-options="required:true" missingMessage="请选择科室"/></td>
		    			<input type="hidden" id="deptName" name="activitiDept.deptName" value="${activitiDept.deptName }">
		    		</tr>
	    			<tr>
						<td class="honry-lable">顺序号：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="deptOrder" name="activitiDept.deptOrder" style="width: 300" value="${activitiDept.deptOrder }" />
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">停用标志：</td>
		    			<td>
			    			<input type="hidden" id="deptTypeHidden" name="activitiDept.deptType" value="${activitiDept.deptType }"/>
			    			<input type="checkBox" id="deptType" onclick="javascript:checkBoxSelect('deptType')"/>
		    			</td>
					</tr>
		    	</table>
			    <div style="text-align:center;padding:5px">
			    	<a id="save" href="javascript:submitTreeForm();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clearForm();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
	    	</form>
	    </div>
	</div>    
	<div id="menuphoto"></div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<script type="text/javascript">
		$(function(){
			//复选框赋值
			if($('#deptTypeHidden').val()==1){
				$('#deptType').attr("checked", true); 
			}
			//科室下拉框及时定位查询 
			$('#deptCode').combobox({
				url: "<%=basePath%>baseinfo/department/departmentCombobox.action", 
				valueField : 'deptCode',
				textField : 'deptName',
				filter:function(q,row){
					var keys = new Array();
					keys[keys.length] = 'deptCode';
					keys[keys.length] = 'deptName';
					keys[keys.length] = 'deptPinyin';
					keys[keys.length] = 'deptWb';
					keys[keys.length] = 'deptInputcode';
					if(filterLocalCombobox(q, row, keys)){
						row.selected=true;
					}else{
						row.selected=false;
					}
					return filterLocalCombobox(q, row, keys);
			    },
				onLoadSuccess:function(data){
					if(data!=null && data.length==1){
						var code= data[0].deptCode;
						$('#deptCode').combobox('select',code);
					}
				},
				onSelect:function(rec){
					var code=rec.deptCode;
					havSelect = false;
					$('#deptName').val(rec.deptName);
				}
			});
		});
		
		//表单提交
		function submitTreeForm(){
			$('#editForm').form('submit',{  
				url:"<c:url value='/oa/activitiDept/editDept.action'/>",
	        	onSubmit:function(){
	        		if(!$('#editForm').form('validate')){
						$.messager.alert('提示',"验证没有通过,不能提交表单!");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						   return false ;
				     }
					var deptOrder=$('#deptOrder');
					if(isNaN(deptOrder.val())){
						$.messager.alert('提示','顺序号必须为数字');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return false;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});  
	        	},  
		        success:function(result){ 
		        	$.messager.progress('close');
		        	var data = JSON.parse(result);
		        	if(data.resCode == 'success'){
		        		closeLayout();
		        		$("#tDt").tree("reload");
		        		searchFrom();
		        		$.messager.alert('提示','保存成功！');
		        	}else if(data.resCode == 'double'){
		        		$.messager.alert('提示','科室编码重复,请重新输入！');	
		        	}else{
						$.messager.alert('提示','保存失败！');	
		        	}
		        }						         
	   		}); 
		}
		//清除页面填写信息
		function clearForm(){
			$('#editForm').form('reset');
		}
		
		//关闭编辑窗口
		function closeLayout(){
			$('#dialogDivId').dialog('close');
		}
		/**
		 * 复选框选中
		 * @param defalVal 默认值
		 * @param selVal 选中值
		 * @author  liujinliang
		 * @date 2015-5-25 10:53
		 * @version 1.0
		 */
		function checkBoxSelect(id){
			if($('#'+id).is(':checked')){
				$('#'+id+'Hidden').val(1);
			}else{
				$('#'+id+'Hidden').val(0);
			}
		}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>