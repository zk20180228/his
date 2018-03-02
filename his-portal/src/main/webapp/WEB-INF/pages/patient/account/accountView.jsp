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
<body>
	<div class="easyui-panel"
		data-options="title:'用户账户查看',iconCls:'icon-form'">
		<div style="padding: 10px">
			<input type="hidden" id="id" name="id" value="${account.id }">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="honry-lable">
						就诊卡编号：
					</td>
					<td class="honry-view">
						${account.idcard.idcardNo }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						参考编号：
					</td>
					<td class="honry-view">
						${account.accountRefid}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						名称:
					</td>
					<td class="honry-view">
						${account.accountName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						类型:
					</td>
					<td class="honry-view">
						${account.accountType }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						金额:
					</td>
					<td class="honry-view">
						${account.accountBalance }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						冻结金额:
					</td>
					<td class="honry-view">
						${account.accountFrozencapital }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						冻结时间:
					</td>
					<td class="honry-view">
						${account.accountFrozentime }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						解冻时间:
					</td>
					<td class="honry-view">
						${account.accountUnfrozentime }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						备注：
					</td>
					<td class="honry-view">
						${account.accountRemark }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						建立人员:
					</td>
					<td>
						<p>
							${account.createUser }
						</p>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						建立部门:
					</td>
					<td>
						<p>
							${account.createDept }
						</p>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						建立时间:
					</td>
					<td>
						<p>
							${account.createTime }
						</p>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						修改人员:
					</td>
					<td>
						<p>
							${account.updateUser }
						</p>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						修改时间:
					</td>
					<td>
						<p>
							${account.updateTime }
						</p>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						停用标志:
					</td>
					<td>
						<p>
							${account.stop_flg }
						</p>
					</td>
				</tr>
			</table>
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			</div>
			<script>
			/**
			 * 关闭查看窗口
			 * @author  lt
			 * @date 2015-5-22 10:53
			 * @version 1.0
			 */
			function closeLayout(){
				$('#divLayout').layout('remove','east');
			}
		</script>
</body>
</html>