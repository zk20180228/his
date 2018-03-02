<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>ICD查看</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px;padding: 0px">
	<div class="easyui-panel" id="panelEast" data-options="iconCls:'icon-form',border:false,fit:true">
		<div style="padding: 5px">
			<input type="hidden" id="id" name="id" value="${icd.id }">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
				<tr>
					<td class="honry-lable">
						<span>诊断码：</span>
					</td>
					<td class="honry-view">
						<span>${icd.code }</span>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>诊断名称：</span>
					</td>
					<td class="honry-view">
					<span>	${icd.name }</span>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>诊断别名1：</span>
					</td>
					<td class="honry-view">
					<span>	${icd.alias }</span>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>诊断别名2：</span>
					</td>
					<td class="honry-view">
					<span>	${icd.alias2 }</span>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>医保诊断编码：</span>
					</td>
					<td class="honry-view">
					<span>	${icd.dsCode }</span>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>附加码：</span>
					</td>
					<td class="honry-view">
					<span>	${icd.addcode }</span>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>拼音码：</span>
					</td>
					<td class="honry-view">
						<span>${icd.pinyin }</span>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>五笔码：</span>
					</td>
					<td class="honry-view">
						<span>${icd.wb }</span>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>自定义码：</span>
					</td>
					<td class="honry-view">
						<span>${icd.inputcode }</span>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>疾病分类：</span>
					</td>
					<td class="honry-view">
						<span>${codeDiseasetypeMap[icd.diseasetype] }</span>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>拼音码：</span>
					</td>
					<td class="honry-view">
						<span>${icd.pinyin }</span>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>疾病死亡原因：</span>
					</td>
					<td class="honry-view">
						<span>${icd.diereason }</span>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>适用性别：</span>
					</td>
					<td class="honry-view">
						<span id = 'sexId'>
							<c:choose>
								<c:when test="${icd.sex eq 'A'}">
									<span style="font-size: 14">全部</span>
								</c:when>
								<c:when test="${icd.sex eq 'M'}">
									<span style="font-size: 14">男</span>
								</c:when>
								<c:when test="${icd.sex eq 'F'}">
									<span style="font-size: 14">女</span>
								</c:when>
								<c:when test="${icd.sex eq 'U'}">
									<span style="font-size: 14">未知</span>
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
						</span>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>是否30中疾病：</span>
					</td>
					<td class="honry-view">
						<c:choose>
							<c:when test="${icd.isThirty eq '1'}">
							<span>是</span>
							</c:when>
							<c:otherwise>
							<span>否</span>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>是否传染病：</span>
					</td>
					<td class="honry-view">
						<c:choose>
							<c:when test="${icd.isCom eq '1'}">
							<span>是</span>
							</c:when>
							<c:otherwise>
							<span>否</span>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>是否肿瘤：</span>
					</td>
					<td class="honry-view">
						<c:choose>
							<c:when test="${icd.isTumor eq '1'}">
						<span>	是</span>
							</c:when>
							<c:otherwise>
							<span>否</span>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
					<span>是否中医诊断：</span>
					</td>
					<td class="honry-view">
						<c:choose>
							<c:when test="${icd.isTcm eq '1'}">
							<span>是</span>
							</c:when>
							<c:otherwise>
						    <span>否</span>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>停用标志：</span>
					</td>	
					<td class="honry-view">
						<c:choose>
							<c:when test="${icd.stop_flg eq '1'}">
							<span>是</span>
							</c:when>
							<c:otherwise>
							<span>否</span>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">创建时间：</td>
					<td class="honry-view"><fmt:formatDate value="${icd.createTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
				</tr>
			</table>
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'" onclick="closeDialog()">关闭</a>
			</div>
		</div>
	</div>
	<script>
var sexMap=new Map();
</script>
</body>
</html>