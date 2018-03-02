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
		<div>
			<form id="editForm" method="post">
				<input type="hidden" id="id" name="cpVcontrol.id" value="${cpVcontrol.id }">
				<input type="hidden" id="cpId" name="cpVcontrol.cpId" value="${cpVcontrol.cpId }">
				<table class="honry-table" style="width: 100%" cellpadding="1" cellspacing="1"
					border="1px solid black" style="margin-left:auto;margin-right:auto;">
					<tr>
						<td class="honry-lable">
							临床路径名称:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="cpName" readonly="readonly"
								name="cpVcontrol.cpName" value="${cpVcontrol.cpName }"
								data-options="required:true" style="width: 290px" 
								missingMessage="请输入临床路径名称" />
						</td>
					</tr>
					<tr>	
						<td class="honry-lable">
							临床路径版本号:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="versionNo"
								name="cpVcontrol.versionNo" value="${cpVcontrol.versionNo }"
								data-options="required:true" style="width: 290px" 
								missingMessage="请输入临床路径名称" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							标准名称:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="standCode"
								name="cpVcontrol.standCode" value="${cpVcontrol.standCode }"
								data-options="required:true" style="width: 290px" 
								missingMessage="请选择标准名称" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							标准版本号:
						</td>
						<td class="honry-info">
							<input class="easyui-combobox"  id="standVersionNo"
								name="cpVcontrol.standVersionNo" value="${cpVcontrol.standVersionNo }"
								data-options="required:true" style="width: 290px" 
								missingMessage="请选择标准版本号" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							版本说明:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="versionMemo"
								name="cpVcontrol.versionMemo" value="${cpVcontrol.versionMemo }" style="width: 290px"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							版本日期:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="versionDate"
								name="cpVcontrol.versionDate" readonly="readonly"
								value="${cpVcontrol.versionDate }" style="width: 290px" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							适用范围:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="applyScope"
								name="cpVcontrol.applyScope"
								value="${cpVcontrol.applyScope }" style="width: 290px" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							审批人:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="approvalUser"
								name="cpVcontrol.approvalUser" readonly="readonly"
								value="${cpVcontrol.approvalUser }" style="width: 290px" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							审批日期:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="approvalDate"
								name="cpVcontrol.approvalDate" readonly="readonly"
								value="${cpVcontrol.approvalDate }" style="width: 290px" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							审批标志:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="approvalFlag"
								name="cpVcontrol.approvalFlag" readonly="readonly"
								value="${cpVcontrol.approvalFlag }" style="width: 290px" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							标准住院日:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="standardDate"
								name="cpVcontrol.standardDate"
								value="${cpVcontrol.standardDate }" style="width: 290px" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							日期单位:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="dateUnit"
								name="cpVcontrol.dateUnit"
								value="${cpVcontrol.dateUnit }" style="width: 290px" />
						</td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<a href="javascript:submit();void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-save'">保存</a>
					<c:if test="${sysDepartment.id==null }">
						<a href="javascript:clear();void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-clear'">清除</a>
					</c:if>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog()">关闭</a>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<script type="text/javascript">
	$(function() {
		/**标准名称下拉**/
		$('#standCode').combobox({
			url : "<c:url value='inpatient/clinicalPathwayAction/queryStand.action'/>",
			valueField : 'standCode',
			textField : 'standName',
			multiple : false,
			editable:false,
			onSelect: function(rec){    
	            $('#standVersionNo').combobox({
	    			url : "<c:url value='inpatient/clinicalPathwayAction/queryVersion.action?standCode="+rec.standCode+"'/>",
	    			valueField : 'standVersionNo',
	    			textField : 'standVersionNo',
	    			multiple : false,
	    			editable:false,
	    			onSelect: function(rec){
	    				$('#versionDate').textbox('setValue',rec.createTime);
	    			}
	    		});
	        }
		});
		/**使用范围下拉**/
		$('#applyScope').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=applyScope",
			valueField : 'encode',
			textField : 'name',
			panelHeight:'100px',
			multiple : false,
			editable:false
		});
	});
	//表单提交   
	function submit() {
		$('#editForm').form('submit', {
			url : "<%=basePath%>inpatient/clinicalPathwayAction/saveVersion.action",
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
					closeDialog();
					$('#tDt').tree('reload');
				}else if(data=='false'){
					$.messager.alert('提示','网络异常，请联系管理员');
				}else{
					$.messager.alert('提示',data);
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
</script>
</body>
</html>