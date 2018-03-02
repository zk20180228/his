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
		<title>流程配置</title>
		<jsp:include page="/baseframe/header/bootstrap/honeySwitch.jsp"></jsp:include>
	</head>

	<body>
		<div class="panel panel-default">
			<input style="text-align: center;margin: 20px 20px;" type="button" class="btn btn-theme" id="save" value="保存" />
			<table class="table">
				<thead>
					<tr>
<!-- 						<th>id</th> -->
						<th>节点标识</th>
						<th>名称</th>
						<th>是否可指定任务人</th>
						<th>是否消息提示</th>
						<th>是否可驳回</th>
						<th>是否可逐级驳回</th>
						<th>是否可催办</th>
						<th>是否同部门</th>
					</tr>
				</thead>
				<tbody>
					<tr>
<!-- 						<td></td> -->
						<td></td>
						<td>全局</td>
						<td>
							<span class="switch-on header-sw" onclick="Allclick(this,'.tr-isAssigner')" themeColor="#63F166"></span>
						</td>
						<td>
							<span class="switch-on header-sw" onclick="Allclick(this,'.tr-message')" themeColor="#63F166"></span>
						</td>
						<td>
							<span class="switch-on header-sw" onclick="Allclick(this,'.tr-reject')" themeColor="#63F166"></span>
						</td>
						<td>
							<span class="switch-on header-sw" onclick="Allclick(this,'.tr-stepreject')" themeColor="#63F166"></span>
						</td>
						<td>
							<span class="switch-on header-sw" onclick="Allclick(this,'.tr-urge')" themeColor="#63F166"></span>
						</td>
						<td>
							<span class="switch-on header-sw" onclick="Allclick(this,'.tr-withdept')" themeColor="#63F166"></span>
						</td>

					</tr>
					<c:forEach items="${voList}" var="vo">
						<tr>
<%-- 							<td>${vo.id}</td> --%>
							<td>${vo.sid}</td>
							<td>${vo.name}</td>
							<c:if test="${vo.isAssigner == true}">
								<td><span item = "${vo.id}-isAssigner" name = "switch" class="switch-on tr-isAssigner" value = "true" ></span></td>
							</c:if>
							<c:if test="${vo.isAssigner == false}">
								<td><span item = "${vo.id}-isAssigner" name = "switch" class="switch-off tr-isAssigner" value = "false" ></span></td>
							</c:if>

							<c:if test="${vo.message == true}">
								<td><span item = "${vo.id}-message" name = "switch" class="switch-on tr-message" value = "true"></span></td>
							</c:if>
							<c:if test="${vo.message == false}">
								<td><span item = "${vo.id}-message" name = "switch" class="switch-off tr-message" value = "false"></span></td>
							</c:if>

							<c:if test="${vo.reject == true}">
								<td><span item = "${vo.id}-reject" name = "switch" class="switch-on tr-reject" value = "true"></span></td>
							</c:if>
							<c:if test="${vo.reject == false}">
								<td><span item = "${vo.id}-reject" name = "switch" class="switch-off tr-reject" value = "false"></span></td>
							</c:if>

							<c:if test="${vo.stepreject == true}">
								<td><span item = "${vo.id}-stepreject" name = "switch" class="switch-on tr-stepreject" value = "true"></span></td>
							</c:if>
							<c:if test="${vo.stepreject == false}">
								<td><span item = "${vo.id}-stepreject" name = "switch" class="switch-off tr-stepreject" value = "false"></span></td>
							</c:if>

							<c:if test="${vo.urge == true}">
								<td><span item = "${vo.id}-urge" name = "switch" class="switch-on tr-urge" value = "true"></span></td>
							</c:if>
							<c:if test="${vo.urge == false}">
								<td><span  item = "${vo.id}-urge" name = "switch" class="switch-off tr-urge" value = "false"></span></td>
							</c:if>

							<c:if test="${vo.withdept == true}">
								<td><span item = "${vo.id}-withdept" name = "switch" class="switch-on tr-withdept" value = "true"></span></td>
							</c:if>
							<c:if test="${vo.withdept == false}">
								<td><span item = "${vo.id}-withdept" name = "switch" class="switch-off tr-withdept" value = "false"></span></td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</body>
	<script type="text/javascript">
			function Allclick (self,ele){
				if($(self).hasClass('switch-on')) {
					//开
					changeSw(false,ele)
				} else {
					//关
					changeSw(true,ele)
				}
			}
			function changeSw (item,ele){
				if(item){
					if ($(ele).hasClass('switch-off')){
						$(ele).removeClass('switch-off').addClass('switch-on').attr("value","true")
					}
				}else{
					if ($(ele).hasClass('switch-on')){
						$(ele).removeClass('switch-on').addClass('switch-off').attr("value","false")
					}
				}
			}
			$("#save").on("click",function(){
				var dataAll = {}	
				$("[name = switch]").each(function(i,v){
					console.log($(v).attr("value"))
					dataAll[$(v).attr("item")] = $(v).attr("value")
				})
				
				$.ajax({
					type:"post",
					url:"<%=basePath%>activiti/process/saveNode.action",
					async:true,
					data:{jsonData:$.toJSON(dataAll)},
					success:function(dataMap){
						if(dataMap.resCode == 'success'){
							$.messager.alert('提示',dataMap.resMsg,'info',function(){
								window.close();  
							});
						}else{
							$.messager.alert('提示',dataMap.resMsg);
						}
					}
				});
			})
			
			
	</script>

</html>