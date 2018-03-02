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
<div id="divLayout" class="easyui-layout" data-options="fit:true,border:false">   
	<div data-options="region:'north',collapsible:false,border:false" style="height:89px;">
		<input type="hidden" id="id" name="id" value="${businessStack.id }">
		<table class="honry-table" style="width:100%;border: none;" cellpadding="1" cellspacing="1"	>
			<tr>
				<td class="honry-lable" style="width: 10%" >科室:</td>
				<td style="width: 10%">${deptName}&nbsp;</td>
				<td class="honry-lable" style="width: 10%" >组套类型：</td>
				<td style="width: 10%">
					<c:choose>
						<c:when test="${businessStack.type eq '1'}">
							药品组套
						</c:when>
						<c:when test="${businessStack.type eq '2'}">
							非药品组套
						</c:when>
					</c:choose>
				</td>
				<td class="honry-lable" style="width: 10%" >组套来源：</td>
				<td style="width: 10%">
					<c:choose>
						<c:when test="${businessStack.source eq '1'}">
							全院
						</c:when>
						<c:when test="${businessStack.source eq '2'}">
							科室
						</c:when>
						<c:when test="${businessStack.source eq '3'}">
							医生
						</c:when>
					</c:choose>
				</td>
				<td class="honry-lable" style="width: 10%" >组套对象：</td>
				<td style="width: 10%">
					<c:choose>
						<c:when test="${businessStack.stackObject eq '1'}">
							财务组套
						</c:when>
						<c:when test="${businessStack.stackObject eq '2'}">
							医嘱组套
						</c:when>
					</c:choose>
				</td>
				<td class="honry-lable" style="width: 10%" >组套医师：</td>
				<td style="width: 10%">${businessStack.docShow}&nbsp;</td>
			</tr>
			<tr>
				<td class="honry-lable" style="width: 10%"  >名称：</td>
				<td style="width: 10%">${businessStack.name}&nbsp;</td>
				<td class="honry-lable" style="width: 10%" >拼音码：</td>
				<td style="width: 10%">${businessStack.pinYin }&nbsp;</td>
				<td class="honry-lable" style="width: 10%" >五笔码：</td>
				<td style="width: 10%">${businessStack.wb }&nbsp;</td>
				<td class="honry-lable" style="width: 10%" >自定义码：</td>
				<td style="width: 10%">
					${businessStack.inputCode }&nbsp;
				</td>
				<td class="honry-lable" style="width: 10%" >是否共享：</td>
				<td style="width: 10%">
					<c:choose>
						<c:when test="${businessStack.shareFlag eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td class="honry-lable" style="width: 10%" >备注：</td>
				<td >
					${businessStack.remark }&nbsp;
				</td>
				<td id="use"  class="honry-lable"   style="display: none;width: 10%">组套用处:</td>
				<td id="use1" colspan="7" style="display: none;width: 10%">
					<c:choose>
						<c:when test="${businessStack.stackInpmertype eq '1'}">
							住院
						</c:when>
						<c:when test="${businessStack.stackInpmertype eq '2'}">
							门诊
						</c:when>
					</c:choose>
				</td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center',border:false" style="height: 50%">
		<table id="listInfo">
			<thead>
				<tr>
					<th data-options="field:'stackInfoItemIdShow', width : '250px'" >
						项目名称
					</th>
					<th data-options="field:'stackInfoNum',width:'90px'">
						开立数量
					</th>
					<th data-options="field:'drugPackagingunit',width:'90px'">
						药品单位
					</th>
					<th data-options="field:'unit',width:'50px'">
						单位
					</th>
					<th data-options="field:'stackInfoItemName',width:'150px'">
						执行科室
					</th>
					<th data-options="field:'combNo',width:'100px'">
						组合号
					</th>
					<th data-options="field:'stackInfoRemark',width:'100px'">
						备注
					</th>
					<th data-options="field:'defaultprice',width:'10%'">
						默认价
					</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
<div id="stackview">
	<a class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="closeLayout()">关闭</a>
</div>
<script>
	var fkId=$('#id').val();
	var type ="${businessStack.type}";
	
	$(function(){
		$('#listInfo').datagrid({
			toolbar: '#stackview',
			fit:true,
			border:false,
			url:"<c:url value='/baseinfo/stack/viewStackinfo.action'/>?fkId="+fkId + "&type="+type+"&menuAlias=${menuAlias}",
			onLoadSuccess:function(data){    
		 		if(type=='1'){
	 			 $("#listInfo").datagrid("showColumn", "drugPackagingunit"); // 设置隐藏列 
	 			 $("#listInfo").datagrid("hideColumn", "unit"); // 设置隐藏列 
	 			}else if(type=='2'){
	 			 $("#listInfo").datagrid("hideColumn", "drugPackagingunit"); // 设置隐藏列 
	 			 $("#listInfo").datagrid("showColumn", "unit"); // 显示隐藏列 
	 			}   
			} 
		});
		var Dx = "${businessStack.stackObject}";
		var use = "${businessStack.stackInpmertype}";
		if(Dx=='2'){
			$('#use').show();
	 		$('#use1').show();
		}
	 	if(Dx=="1"){
 			$('#use').hide();
 			$('#use1').hide();
	 	}
	});
	function closeLayout(){
		$('#divLayout').layout('remove','east');
	}
</script>
</body>
</html>