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
	<title>模板编辑双击查看界面</title>
</head>
	<body>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false" style="height: 42%">
			<input type="hidden" id="id" name="emrTemplate.id" value="${emrTemplate.id }">
			<input type="hidden" id="tempChkflg" name="emrTemplate.tempChkflg" value="${emrTemplate.tempChkflg }">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
				<tr>
					<td class="honry-lable">
						编码:
					</td>
					<td class="honry-view">
						${emrTemplate.tempCode}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						名称:
					</td>
					<td class="honry-view">
						${emrTemplate.tempName}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						拼音码:
					</td>
					<td class="honry-view">
						${emrTemplate.pingyin}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						五笔码:
					</td>
					<td class="honry-view">
						${emrTemplate.wb}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						自定义码:
					</td>
					<td class="honry-view">
						${emrTemplate.inputcode}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						模板类别:
					</td>
					<td class="honry-view">
					<input id="type" style="border:none" readonly="readonly" ></input>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						诊断名称:
					</td>
					<td class="honry-view">
						${emrTemplate.tempDiag}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						书写类别:
					</td>
					<td class="honry-view">
					<input id="writeType" style="border:none" readonly="readonly" ></input>
							<input id="tempWritetype"  value="${emrTemplate.tempWritetype}"  type="hidden">
						&nbsp;
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center',border:false" style="height: 50%;width: 100%;overflow: auto;">
		${emrTemplate.strContent}
			<%-- <textarea style="width: 100%; height: 100%;" id="content" readonly="readonly">${emrTemplate.strContent}</textarea> --%>
		</div>
		<div data-options="region:'south',border:false" style="text-align:center;height: 8%">
			<span id="check">
		    	<a id="check1" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-shenhetongguo'" onclick="check(1)">审核通过</a>
		    	<a id="check2" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-shenheshibai'" onclick="check(2)">审核不通过</a>
		    </span>
		    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
	    </div>
	</div>
	<script>
			$(function(){
				var flag = $('#tempChkflg').val();
				if(flag == 0){
					$('#check').show();
				}else{
					$('#check').hide();
				}
				/* document.getElementById("content").innerText = ${emrTemplate.strContent}; */
				formatType();
				formatWritetype();
			});
			
			/**
			 * 关闭查看窗口
			 */
			function formatType(){
				var tempType = ${emrTemplate.tempType };
				if(tempType == '0'){
					$('#type').val('通用');
				}
				if(tempType == '1'){
					$('#type').val('科室');
				}
				if(tempType == '2'){
					$('#type').val('个人');
				}
			}
			/**
			 * 关闭查看窗口
			 */
			function formatWritetype(){
				var writeType = ${emrTemplate.tempWritetype };
				if(writeType == '0'){
					$('#writeType').val('不限次书写');
				}
				if(writeType == '1'){
					$('#writeType').val('仅首次单次书写');
				}
				if(writeType == '2'){
					$('#writeType').val('单次书写');
				}
			}
			/* 
			 * 关闭界面
			 */
			function check(flag){
				var ids = $('#id').val();
				$.ajax({
					url: '<%=basePath %>emrs/emrTemplate/check.action?ids='+ids+'&tempChkflg='+flag,
					type:'post',
					success: function(data) {
						if(data == 'success'){
							$.messager.alert('提示','保存成功');
							closeLayout();
							load();
						}
					},
					error: function(data){
					}
				});
			}
			/* 
			 * 关闭界面
			 */
				function closeLayout(){
					$('#temWins').dialog('close'); 
				}
			
		</script>
	</body>
</html>