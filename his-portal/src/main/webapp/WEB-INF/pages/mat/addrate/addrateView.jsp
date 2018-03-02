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
		<div class="easyui-panel" id="panelEast" data-options="fit:true">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin:10px;">
				<tr>
					<td class="honry-lable">
						序号:
					</td>
					<td class="honry-view">
						${matAddrate.seqNo }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						加价规则:
					</td>
					<td class="honry-view">
						${matAddrate.addRate}
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						物品科目:
					</td>
					<td class="honry-view">
						${kindMap[matAddrate.kindCode] }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						规格:
					</td>
					<td class="honry-view">
						${matAddrate.specs }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						起始价格:
					</td>
					<td class="honry-view">
						<fmt:formatNumber value="${matAddrate.lowPrice }" pattern="0"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						终止价格:
					</td>
					<td class="honry-view">
						<fmt:formatNumber value="${matAddrate.highPrice }" pattern="0"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						加价率:
					</td>
					<td class="honry-view">
						<fmt:formatNumber value="${matAddrate.rate }" pattern="0"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						附加费:
					</td>
					<td class="honry-view">
						<fmt:formatNumber value="${matAddrate.addFee }" pattern="0"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						操作员:
					</td>
					<td class="honry-view">
						${userMap[matAddrate.operCode] }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						操作日期:
					</td>
					<td class="honry-view">
						<fmt:formatDate value="${matAddrate.operDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						是否停用:
					</td>
					<td class="honry-view">
						<c:choose>
						<c:when test="${matAddrate.stop_flg eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
				<div style="text-align:center;padding:5px">
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
		</div>
		<script type="text/javascript">
			//关闭查看窗口
			function closeLayout(){
				$('#divLayout').layout('remove','east');
			}
		</script>
	</body>
</html>