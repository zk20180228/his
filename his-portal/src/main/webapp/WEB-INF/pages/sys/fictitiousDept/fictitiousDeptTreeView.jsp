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
				<table class="honry-table" style="width: 100%" cellpadding="1" cellspacing="1"
					border="1px solid black" style="margin-left:auto;margin-right:auto;">
					<tr>
						<td class="honry-lable">
							科室分类:
						</td>
						<td class="honry-view">
						<input type="hidden" id="deptType" value="${fictitiousDept.deptType }"/><p id="deptTypeId"></p>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							虚拟部门:
						</td>
						<td class="honry-info">${fictitiousDept.deptName }</td>
					</tr>
					<tr>
						<td class="honry-lable">
							部门简称:
						</td>
						<td class="honry-info">${fictitiousDept.deptBrev }</td>
					</tr>
					<tr>
						<td class="honry-lable">
							部门英文:
						</td>
						<td class="honry-info">${fictitiousDept.deptEname }</td>
					</tr>
					<!-- TODO -->
					<!-- 2017-5-22 11:05:22 杜天亮 渲染有问题，临时注释掉 -->
					<%-- <tr>
						<td class="honry-lable">
							所在院区:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="deptAddress"
								name="deptDistrict"
								value="${fictitiousDept.deptDistrict }"
								data-options="required:true,valueField:'id',textField:'text',data: [{id:'1',text:'河西院区'},{id:'2',text:'郑东院区'},{id:'1',text:'惠济院区'}]" style="width: 290px"
								missingMessage="请输入部门地点" readonly/>
						</td>
					</tr> --%>
					<tr>
						<td class="honry-lable">
							自定义码:
						</td>
						<td class="honry-info">${fictitiousDept.deptInputCode }</td>
					</tr>
					
					<tr>
						<td class="honry-lable">
							是否是挂号部门:
						</td>
						<td>${fictitiousDept.deptIsforregister }</td>
					</tr>
					<tr>
						<td class="honry-lable">
							停用标志:
						</td>
						<td>${fictitiousDept.stop_flg }</td>
					</tr>
					<tr>
						<td class="honry-lable">
							备注:
						</td>
						<td class="honry-info">${fictitiousDept.deptRemark }</td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog()">关闭</a>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	$(function() {
		$.ajax({
			url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action",
			data : {"type":"depttype"},
			success: function(data) {
				for(var i=0;i<data.length;i++){
			 		var type=$('#deptType').val();
			 		if(type==data[i].encode){
			 			$('#deptTypeId').text(data[i].name);
			 		}
				}
			}
		});
		if ($('#deptIsforregisterHidden').val() == 1) {
			$('#deptIsforregister').attr("checked", true);
			$('#deptRegisterno').textbox({
				disabled : false
			});
		} else {
			$('#deptRegisterno').textbox({
				disabled : true
			});
		}
		if ($('#stopFlgHidden').val() == 1) {
			$('#stopFlg').attr("checked", true);
		}
	});
</script>
</body>
</html>