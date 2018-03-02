<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body style="padding:0px;margin:0px">
		<div class="easyui-panel" id="panelEast" data-options="title:'诊室编辑',iconCls:'icon-form',border:false" style="width:100%;padding:0px;margin:0px" >
			<form id="editForm" method="post" >
				<input type="hidden" id="id" name="id" value="${clinic.id }">
				<input type="hidden" id="deptid" name="deptid" value="${deptid}">
				<table class="honry-table" cellpadding="0" cellspacing="1" border="0" style="border-top:0;border-left:0">
					<tr>
						<td class="honry-lable"  style="text-align:right;border-top:0;border-left:0" >
							<span>诊室名称:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;border-top:0">
							<input id="clinicName" class="easyui-textbox"  value="${clinic.clinicName}" name="clinic.clinicName" style="width:60%" data-options="required:true" missingMessage="诊室名称"></input>
						</td>
					  </tr>
					  <tr>	
						<td class="honry-lable" style="border-left:0">
							<span>诊室编码:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="clinicInputcode"  class="easyui-textbox" value="${clinic.clinicInputcode}" name="clinic.clinicInputcode" data-options="required:true" style="width:60%" ></input>
						</td>
					  </tr>
					  <tr>
				  	    <td class="honry-lable" style="border-left:0">
							<span>所属科室:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="clinicDeptIdHidden"  type="hidden"    missingMessage="请选择所属科室"/>
					    	<input type="hidden" id="deptNameid" name="deptNameid"  />
					    	<input id="clinicDeptId" type="hidden" name="clinic.clinicDeptId" value="${clinic.clinicDeptId}" style="width: 200"/>
			    	
			    			<input id="clinicDeptIdText" type="text"  style="width: 185">
					    	
					    	<a href="javascript:delSelectedData('clinicDeptIdText');"  class="easyui-linkbutton" 
					    	data-options="iconCls:'icon-opera_clear',plain:true"></a>
					    	
						</td>
					  </tr>
					  <tr>	
						<td class="honry-lable" style="border-left:0">
							<span>备注:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="clinicRemark"  class="easyui-textbox" value="${clinic.clinicRemark}" name="clinic.clinicRemark" style="width:60%" ></input>
						</td>
						<input type="hidden" id="clinicUser" name="clinic.createUser" value="${clinic.createUser }">
						<input type="hidden" id="clinicDept" name="clinic.createDept" value="${clinic.createDept }">
						<input type="hidden" id="clinicTime" name="clinic.createTime" value="${clinic.createTime }">										
					  </tr>
				</table>
				 <div style="text-align:center;padding:5px">
			    	<c:if test="${empty clinic.id}">			    	
			    	<a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
			    	</c:if>
			    	<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:closeLayout()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
	    	</form>
		</div>
		<script type="text/javascript">
		var popWinDeptCallBackFn = null;
			$(function(){
								
				$('#clinicDeptIdText').textbox({
					readonly:true,
					required:true,
					missingMessage:"请选择工作科室",
					value:(function(){
						var deptNames=$("#deptNames").val();
						if(deptNames=="科室信息"||deptNames==''){
							for(var i=0;i<delptList.length;i++){
								$("#clinicDeptId").val()
								if($("#clinicDeptId").val()==delptList[i].id){
									deptNames = delptList[i].deptName;
									break;
								}}
							$('#clinicDeptIdText').textbox('setValue',deptNames);
						
							}
						else{
							$('#clinicDeptIdText').textbox('setValue',deptNames);
						}
					}),
					
				}); 
				
					bindEnterEvent('clinicDeptIdText',popWinToDept,'easyui');//绑定回车事件
					
			});
			/**
			 * 表单提交
			 */
			function submit(flg) {
				$('#editForm').form('submit', {
					url : "<c:url value='/baseinfo/clinic/saveOrUpdateClinic.action'/>?deptname="+$('#deptNameid').val(),
					onSubmit : function() {
						if (!$('#editForm').form('validate')) {
							$.messager.show({
								title : '提示信息',
								msg : '验证没有通过,不能提交表单!'
							});
							return false;
						}
					},
					success : function(data) {
						data = $.parseJSON(data);
						if(data.mesCode == 1){
							if (flg == 0) {
								$.messager.alert('友情提示','操作成功！');
								$('#divLayout').layout('remove', 'east');
								//实现刷新
								$("#list").datagrid("reload");
							} else if (flg == 1) {
								//清除editForm
								$("#list").datagrid("reload");//刷新页面
								$('#clinicName').textbox('setValue','');//清空名字
								$('#clinicInputcode').textbox('setValue','');//清空
								$('#clinicRemark').textbox('setValue','');//清空
							}
						}else if(data.mesCode == 2){
							$.messager.alert('友情提示','诊室名称已被占用，请重新输入','warning');
							close_alert();
						}else{
							$.messager.alert('友情提示','诊室编码已被占用，请重新输入','warning');
							close_alert();
						}
					},
					error : function(data) {
						$.messager.alert('提示','保存失败！');
					}
				});
			}
			/**
			 * 清除页面填写信息
			 * @author  zpty
			 * @date 2015-6-2 10:53
			 * @version 1.0
			 */
			function clear(){
				$('#editForm').form('reset');
			}
			/**
			* 回车弹出所属科室选择窗口
			* @author  zhuxiaolu
			* @param deptIsforregister 是否是挂号科室 1是 0否
			* @param textId 页面上commbox的的id
			* @date 2016-03-22 14:30   
			* @version 1.0
			*/

			function popWinToDept(){
				popWinDeptCallBackFn = function(node){
			    	$("#deptid").val(node.deptCode);
			    	$("#clinicDeptId").val(node.deptCode);
					 $('#clinicDeptIdText').textbox('setValue',node.deptName);
				};
				var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?textId=clinicDeptIdText";
				window.open (tempWinPath,'newwindow',' left=100,top=50,width='+ (screen.availWidth -200) +',height='+ (screen.availHeight-110) 
			+',scrollbars,resizable=yes,toolbar=yes')
			
			}

		</script>
	</body>
</html>