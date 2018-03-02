<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div class="easyui-panel" id="panelEast"
		data-options="iconCls:'icon-form',border:false" style="width: 100%">
		<div style="padding: 5px">
			<form id="editForm" method="post">
				<input type="hidden" id="id" name="pathVsIcd.id" value="${pathVsIcd.id }">
				<input type="hidden" id="createUser" name="pathVsIcd.createUser" value="${pathVsIcd.createUser }">
				<input type="hidden" id="createTime" name="pathVsIcd.createTime" value="${pathVsIcd.createTime }">
				<table class="honry-table" style="width: 100%" cellpadding="1" cellspacing="1"
					border="1px solid black" style="margin-left:auto;margin-right:auto;">
					<tr>
						<td class="honry-lable">
							临床路径ID:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="cpId"
								name="pathVsIcd.cpId" value="${pathVsIcd.cpId }"
								data-options="required:true" style="width: 290px" 
								readonly="readonly" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							ICD名称:
						</td>
						<td class="honry-info">
							<input id="icdName" class="easyui-combobox" 
								name="pathVsIcd.icdName" value="${pathVsIcd.icdName }"
							 data-options="required:true,editable:false" 
							 missingMessage="请选择ICD编码"" style="width: 290px" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							ICD编码:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="icdCode"
								name="pathVsIcd.icdCode" value="${pathVsIcd.icdCode }"
								data-options="required:true" style="width: 290px" 
								missingMessage="请输入ICD编码" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							所属院区:
						</td>
						<td class="honry-info">
							<input class="easyui-combobox"  id="areaCode"
								name="pathVsIcd.areaCode" data-options="required:true" 
								value="${pathVsIcd.areaCode }" style="width: 290px"
								missingMessage="请输入所属院区" />
						</td>
					</tr>
					<tr >
						<td class="honry-lable" style="font-size: 14">是否删除：</td>
		    			<td class=""><input type="hidden" id="isDelHidden" name="pathVsIcd.del_flg" value="${pathVsIcd.del_flg }"/>
			    					<input type="checkBox" id="isDel" onclick="javascript:onclickBox('isDel')"/></td>
	    			</tr>
					<tr >
						<td class="honry-lable" style="font-size: 14">是否停止：</td>
		    			<td class=""><input type="hidden" id="isStopHidden" name="pathVsIcd.stop_flg" value="${pathVsIcd.stop_flg }"/>
			    					<input type="checkBox" id="isStop" onclick="javascript:onclickBox('isStop')"/></td>
	    			</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<a href="javascript:submit();void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-save'">保存</a>
					<c:if test="${sysDepartment.id==null }">
						<a href="javascript:clear();void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-clear'">清除</a>
					</c:if>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<script type="text/javascript">
	$(function() {
		var time = "${pathVsIcd.createTime }";
		var del = "${pathVsIcd.del_flg }";
		var stop = "${pathVsIcd.stop_flg }";
		if(del!=''&&del!=null){
			if(del=='1'){
				$("#isDel").attr("checked", true);
			}
		}
		if(stop!=''&&stop!=null){
			if(stop=='1'){
				$("#isStop").attr("checked", true);
			}
		}
		$('#createTime').val(time.substring(0,19))
		$('#areaCode').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=hospitalArea",
			valueField : 'encode',
			textField : 'name',
			multiple : false,
			onSelect:function(record){
				console.info(record);
				$('#areaCode').combobox('setValue',record.encode);
			}
		});
		$('#icdName').combobox({
			url : "<c:url value='/inpatient/clinicalPathVsICD/queryICDDictionary.action'/>",
			valueField : 'name',
			textField : 'name',
			multiple : false,
			onSelect:function(record){
				$('#icdCode').textbox('setValue',record.code);
			}
		});
	});
	//表单提交   
	function submit() {
		$('#editForm').form('submit', {
			url : "<%=basePath%>inpatient/clinicalPathVsICD/saveOrUpdate.action",
			data : $('#editForm').serialize(),
			dataType : 'json',
			onSubmit : function() {
				if (!$('#editForm').form('validate')) {
					$.messager.progress('close');
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					$.messager.alert('提示信息','验证没有通过,不能提交表单!','warning');
					close_alert();
					return false;
				}
				$.messager.progress({text:'保存中，请稍后...',modal:true});
			},
			success : function(data) {
				$.messager.progress('close');
				if(data=='true'){
					$.messager.alert('提示','保存成功！');
					//实现刷新科室树
					closeLayout();
					$('#tDt').tree('reload');
					$('#list').datagrid('load', {
						modelId : '',
						keyWord: ''
					});
				}else{
					$.messager.alert('提示','保存失败');
				}
				
			},
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示','保存失败！');
			}
		});
	}
	//清除所填信息
	function clear() {
		$('#editForm').form('reset');
	}
	//复选框赋值
	function onclickBox(id){
		if($('#'+id).is(':checked')){
			$('#'+id+'Hidden').val(1);
		}else{
			$('#'+id+'Hidden').val(0);
		}
	}
</script>
</body>
</html>