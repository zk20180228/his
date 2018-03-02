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
<title>知识库维护查看页面</title>
</head>
	<body>
		<div class="easyui-panel"  id="panelEast" data-options="title:'查看',iconCls:'icon-form',fit:true">
			<input type="hidden" id="id" name="konwledgeBase.id" value="${konwledgeBase.id }">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="border-left:0;border-top:0;">
				<tr>
					<td class="honry-lable" style="border-top:0;">
						编码:
					</td>
					<td class="honry-view" style="border-top:0;">
						${konwledgeBase.konwCode }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						名称:
					</td>
					<td class="honry-view">
						${konwledgeBase.konwName}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						排序号:
					</td>
					<td class="honry-view">
						${konwledgeBase.konwOrder }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						拼音码:
					</td>
					<td class="honry-view">
						${konwledgeBase.konwPinYin }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						五笔码:
					</td>
					<td class="honry-view">
						${konwledgeBase.konwWb }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						自定义码:
					</td>
					<td class="honry-view">
						${konwledgeBase.konwInputCode }&nbsp;
					</td>
				</tr>
				<c:if test="${flag == 1}">
				<tr>
					<td class="honry-lable">
						内容:
					</td>
					<td class="honry-view konwledgeBaseFont">${konwledgeBase.strContent}&nbsp;
					</td>
				</tr>
				</c:if>
			</table>
				<div style="text-align:center;padding:5px">
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="clos()">关闭</a>
			    </div>
		</div>
	</body>
</html>