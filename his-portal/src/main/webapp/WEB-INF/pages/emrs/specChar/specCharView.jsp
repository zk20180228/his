<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<title>特殊字符维护查看界面</title>
</head>
	<body>
		<div class="easyui-panel"  id="panelEast" data-options="title:'查看',iconCls:'icon-form',border: false">
			<input type="hidden" id="id" name="id" value="${specChar.id }">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin-left:auto;margin-right:auto;margin-top:10px;">
				<tr>
					<td class="honry-lable">
						编码:
					</td>
					<td class="honry-view">
						${specChar.specCharCode }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						字符:
					</td>
					<td class="honry-view">
						${specChar.specCharName}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						拼音码:
					</td>
					<td class="honry-view">
						${specChar.specCharPinYin }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						五笔码:
					</td>
					<td class="honry-view">
						${specChar.specCharWb }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						自定义码:
					</td>
					<td class="honry-view">
						${specChar.specCharInputCode }&nbsp;
					</td>
				</tr>
			</table>
				<div style="text-align:center;padding:5px">
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
		</div>
	</body>
</html>