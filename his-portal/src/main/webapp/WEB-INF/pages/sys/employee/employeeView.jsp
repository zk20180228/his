<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp" %>
<title>员工管理员工查看</title>
<script type="text/javascript">
	var stateMap = new Map();
	$.ajax({
		url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=workstate",
		async:false,
		success: function(data) {
			var deptType = data;
			for(var i=0;i<deptType.length;i++){
				stateMap.put(deptType[i].encode,deptType[i].name);
			}
		}
	});
</script>
</head>
	<body style="text-align: center;">
		<div id="divLayout" class="easyui-layout" data-options="fit:true,border:false">
			<div data-options="region:'north',title:'员工查看',iconCls:'icon-form',border:false,collapsible:false" style="width:100%;height: 90%;">	
				<input type="hidden" id="id" name="id" value="${employee.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" style="width:100%">
						<tr>
							<td class="honry-lable" style="width:400px">
								部门编号：
							</td>
							<td style="width:400px">${dname}</td>
							<td class="honry-lable" style="width:400px">
								用户编号：
							</td >
							<td style="width:400px">${accountt}</td>
							
						</tr>
						 <tr>
							<td class="honry-lable">
								工作号：
							</td>
							 <td >${employee.jobNo}</td> 
							<td class="honry-lable">
								姓名：
							</td>
							<td >${employee.name}</td>
						</tr>
						<tr>
							<td class="honry-lable">
								曾用名：
							</td>
							<td>${employee.oldName}</td>
							<td class="honry-lable">
								拼音码：
							</td>
			    			<td>${employee.pinyin }</td>
						</tr>
						 <tr>
							<td class="honry-lable">
								自定义码：
							</td>
							<td>${employee.inputCode}</td>
							<td class="honry-lable">
								性别：
							</td>
							<td id = "sexId"></td>
						</tr>
						<tr>	
							<td class="honry-lable">
								民族：
							</td>
							<td>${familyt}</td>
							<td class="honry-lable">
								出生日期：
							</td>
							<td><fmt:formatDate value="${employee.birthday}" pattern="yyyy-MM-dd" /></td>
						<tr>	
							<td class="honry-lable">
								学历：
							</td>
							<td>${educationt}</td>
							<td class="honry-lable">
								员工类型：
							</td>
							<td>
								<input id="typeName" style="border:none" readonly="readonly" ></input>
								<input id="typeId"  value="${employee.type }"  type="hidden">
							</td>
						</tr>
						<tr>	
							<td class="honry-lable">
								职称：
							</td>
							<td>${titlet}</td>
						
							<td class="honry-lable">
								办公室：
							</td>
							<td>${employee.office}</td>
						</tr>
						<tr>	
							<td class="honry-lable">
								职务：
							</td>
							<td>${postt}</td>
						
							<td class="honry-lable">
								办公电话：
							</td>
							<td>${employee.officePhone}</td>
						</tr>
						<tr>	
							<td class="honry-lable">
								传真：
								</td>
							<td>${employee.fax}</td>
						
							<td class="honry-lable">
								移动电话：
							</td>
							<td>${employee.mobile}</td>
						</tr>
						<tr>	
							<td class="honry-lable">
								电子邮件：
								</td>
							<td>${employee.email}</td>
						
							<td class="honry-lable">
								身份证：
							</td>
							<td>${employee.idEntityCard}</td>
						</tr>
						<tr>
							<td class="honry-lable">
								工作状态：
							</td>
							<td>
								<input id="workhidden" type="hidden" value="${employee.workState}"></input>
								<input id="work" type="text" style="border:none" readonly="readonly"></input>
							</td>
							<td class="honry-lable">
								是否是干部：
							</td>
							<td>${ifcadre}
								<c:choose>
								<c:when test="${employee.ifcadre eq '1'}">
									是
								</c:when>
								<c:when test="${employee.ifcadre eq '2'}">
									否
								</c:when>
								
								</c:choose>
							
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								工资账号：
							</td>
							<td  style="text-align: left;">${employee.wagesAccount}</td>
							<td class="honry-lable">
								电话小号：
							</td>
							<td  style="text-align: left;">${employee.minPhone}</td>
							
						</tr>
						<tr>
							<td class="honry-lable">
								备注：
							</td>
							<td colspan="3" style="text-align: left;">${employee.remark}</td>
							
						</tr>
						<tr>
							<td class="honry-lable">
								电子签名：
								</td>
							<td colspan="3" style="text-align: left;"><img  id="copyEsign" alt="" src=""> <input id="esign" type="hidden" value="${employee.esign}"> </td>
						</tr>
						<tr>
							<td class="honry-lable">
								照片：
							</td>
							<td colspan="3"><img  id="copyPhoto" alt="" src=""> <input id="photo" type="hidden" value="${employee.photo}"></td>
						</tr>
						<tr>
							<td class="honry-lable">创建时间：</td>
							<td class="honry-view" colspan="3"><fmt:formatDate value="${employee.createTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
						</tr>
						<tr>
							<td colspan="4">
							能否改票据：
							<c:choose>
							<c:when test="${employee.canEditBill eq '1'}">
								能
							</c:when>
							<c:otherwise>
								否
							</c:otherwise>
							</c:choose>
							&nbsp;&nbsp;能否直接收费：
							<c:choose>
							<c:when test="${employee.canCharge eq '1'}">
								能
							</c:when>
							<c:otherwise>
								否
							</c:otherwise>
							</c:choose>
							&nbsp;&nbsp;是否是专家：
							<c:choose>
							<c:when test="${employee.isExpert eq '1'}">
								是
							</c:when>
							<c:otherwise>
								否
							</c:otherwise>
							</c:choose>
							&nbsp;&nbsp;停用标志：
							<c:choose>
							<c:when test="${employee.stop_flg eq '1'}">
								是
							</c:when>
							<c:otherwise>
								否
							</c:otherwise>
							</c:choose>
						</td>
					</tr> 
<!-- 						<tr> -->
<!-- 							<td colspan="4"> -->
<!-- 							电话是否可见： -->
<%-- 							<c:choose> --%>
<%-- 							<c:when test="${employee.mofficePhoneVisible eq '0'}"> --%>
<!-- 								是 -->
<%-- 							</c:when> --%>
<%-- 							<c:otherwise> --%>
<!-- 								否 -->
<%-- 							</c:otherwise> --%>
<%-- 							</c:choose> --%>
<!-- 							&nbsp;&nbsp;手机是否可见： -->
<%-- 							<c:choose> --%>
<%-- 							<c:when test="${employee.mmobileVisible eq '0'}"> --%>
<!-- 								是 -->
<%-- 							</c:when> --%>
<%-- 							<c:otherwise> --%>
<!-- 								否 -->
<%-- 							</c:otherwise> --%>
<%-- 							</c:choose> --%>
<!-- 							&nbsp;&nbsp;邮箱是否可见： -->
<%-- 							<c:choose> --%>
<%-- 							<c:when test="${employee.memailVisible eq '0'}"> --%>
<!-- 								是 -->
<%-- 							</c:when> --%>
<%-- 							<c:otherwise> --%>
<!-- 								否 -->
<%-- 							</c:otherwise> --%>
<%-- 							</c:choose> --%>
<!-- 						</td> -->
<!-- 					</tr>  -->
				</table>
			</div>
			<div region="center" style="text-align:center;padding:7px;hight:5%" >
			    <a href="javascript:closeDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			</div>
		</div>
	<script>
	var fileServerURL = '${fileServerURL}';
	console.log(fileServerURL)
	var sexMap=new Map();
		//性别渲染
		$.ajax({
			url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
			data:{"type":"sex"},
			type:'post',
			success: function(data) {
				var v = data;
				for(var i=0;i<v.length;i++){
					sexMap.put(v[i].encode,v[i].name);
				}
				sexRend();
			}
		});
		function sexRend(){
			var sex = "${employee.sex}";
			$('#sexId').html(sexMap.get(sex));
		}
	    var typeList = null;
	    $.ajax({//下拉框员工类型 
	      	url :"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action" ,  
		    type : "post",  
			data:{"type":"empType"},
			success: function(list) { 
				typeList=list;
				if($('#typeId').val()!=null){
					var value =$('#typeId').val();
					for ( var i = 0; i < typeList.length; i++) {
						if (value == typeList[i].encode) {
							$('#typeName').val(typeList[i].name);
						}
					}
				}
		    }
	    });
	    var workList = getWorkStateArray();
		//加载页面
			$(function(){
				//编辑时用为了只显示名称 而不是把路径显示出来
				var esign = $('#esign').val();
				var photo = $('#photo').val();
				if(esign!=null&&esign!=""){
					$("#copyEsign").attr("src",fileServerURL + esign);
				}
				if(photo!=null&&photo!=""){
					$("#copyPhoto").attr("src",fileServerURL + photo);
			}
		});

		window.onload=function(){
			var value =$('#workhidden').val();
			if(value!=null){
				$('#work').val(stateMap.get(value));
			}
		}
		//关闭dialog
		function closeDialog() {
			window.close();  
		}
	</script>
	</body>
</html>