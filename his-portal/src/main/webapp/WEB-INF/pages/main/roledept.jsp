<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
	<meta charset="utf-8">
	<meta name="description" content="">
	<meta name="keywords" content="">
	<title>郑州大学第一附属医院-综合信息应用平台(HIAS)</title> 
	<base href="<%=basePath%>">	
</head>
<body>
	<div class="rolemain">
		<div class="role chose">
		<h2>选择角色</h2>
			<div class="roleList choseList">
				<a href="javascript:void(0)" class="prevR"></a>
				<a href="javascript:void(0)" class="nextR"></a>
				<div class="roleListMid choseListMid">
					<ul id="roleDidId">
					</ul>
				</div>
			</div>
		</div>
		<div class="dept chose">
		<h2>选择部门</h2>
		<div class="deptList choseList">
				<!--按钮可以点击时class为prev或next，不可以点击时为prevdisable和nextdisable-->
				<a href="javascript:void(0)" class="prevD"></a>
				<a href="javascript:void(0)" class="nextD"></a>
				<div class="deptListMid choseListMid">
					<ul id="deptDidId">
					</ul>
				</div>
			</div>
		</div>
		<div class="button" style="display:none;">
			<input type="submit" name="" value="提交" class="tijiao"/>
		</div>
	</div>
	<script src="<%=basePath%>javascript/js/alertwindow.js" type="text/javascript"></script>
	<script type="text/javascript">
	$(function(){
		$('.prevR').css('visibility','hidden');
		$('.nextR').css('visibility','hidden');
		$('.prevD').css('visibility','hidden');
		$('.nextD').css('visibility','hidden');
		$('.button').show();
		var curRoleFlag = "";
		var curDeptFlag = "";
		$.ajax({
			url: 'findRolebyLoginUser.action',
			type:'post',
			success: function(data) {
				var roleParentVOList = eval("("+data+")");
				for(var i=0;i<roleParentVOList.length;i++){
					$('#roleDidId').append("<div class=\"role\" id=\""+roleParentVOList[i].id+"\" style=\""+roleParentVOList[i].attribute+"\">");
					for(var j=0;j<roleParentVOList[i].roleSubclassVO.length;j++){
						$('#'+roleParentVOList[i].id).append("<li id=\""+roleParentVOList[i].roleSubclassVO[j].id+"\" style=\""+roleParentVOList[i].roleSubclassVO[j].parameter+"\" onclick=\"onRoleClick(this,'"+roleParentVOList[i].roleSubclassVO[j].id+"',"+roleParentVOList[i].roleSubclassVO[j].isAdmin+")\" class><div class=\""+roleParentVOList[i].roleSubclassVO[j].icon+"\"></div>"+roleParentVOList[i].roleSubclassVO[j].name+(roleParentVOList[i].roleSubclassVO[j].isAdmin?"<br>(可不选择部门)":"")+"</li>");
						if("${sessionScope.loginRole.id}" == roleParentVOList[i].roleSubclassVO[j].id){
							$("#"+roleParentVOList[i].roleSubclassVO[j].id).addClass("selected");
							curRoleFlag = i;
						}
					}
					$('#roleDidId').append("</div>");
				}
				$("#role"+curRoleFlag*5).show().siblings().hide();
				if(curRoleFlag > 0){
					$('.prevR').css('visibility','visible');
				}				
				if(curRoleFlag == (roleParentVOList.length-1)){
					$('.nextR').css('visibility','hidden');
				}else{
					$('.nextR').css('visibility','visible');
				}
			}
		});
		$.ajax({
			url: 'findDeptbyRole.action',
			type:'post',
			success: function(data) {
				var deptParentVOList = eval("("+data+")");
				for(var i=0;i<deptParentVOList.length;i++){
					$('#deptDidId').append("<div class=\"dept\" id=\""+deptParentVOList[i].id+"\" style=\""+deptParentVOList[i].attribute+"\">");
					for(var j=0;j<deptParentVOList[i].deptSubclassVO.length;j++){
						$('#'+deptParentVOList[i].id).append("<li id=\""+deptParentVOList[i].deptSubclassVO[j].id+"\" style=\""+deptParentVOList[i].deptSubclassVO[j].parameter+"\" onclick=\"onDeptClick(this,'"+deptParentVOList[i].deptSubclassVO[j].id+"')\" class><div class=\""+deptParentVOList[i].deptSubclassVO[j].icon+"\"></div>"+deptParentVOList[i].deptSubclassVO[j].name+"</li>");
						if("${sessionScope.loginDepartment.deptCode}" == deptParentVOList[i].deptSubclassVO[j].id){
							$("#"+deptParentVOList[i].deptSubclassVO[j].id).addClass("selected");
							curDeptFlag = i;
						}
					}
					$('#deptDidId').append("</div>");
				}
				$("#dept"+curDeptFlag*5).show().siblings().hide();
				if(curDeptFlag > 0){
					$('.prevD').css('visibility','visible');
				}				
				if(curDeptFlag == (deptParentVOList.length-1)){
					$('.nextD').css('visibility','hidden');
				}else{
					$('.nextD').css('visibility','visible');
				}
				$('.prevD').click(function(){
					var divs = $("[class='dept']:visible");
					divs.each(function(){
						$(this).hide();
						$(this).prev().show();
						$('.nextD').css('visibility','visible');
						if($(this).prev().prev().prop('id')==null){
							$('.prevD').css('visibility','hidden');
						}
						return;
					});
				});
				$('.nextD').click(function(){
					var divs = $("[class='dept']:visible");
					divs.each(function(){
						$(this).hide();
						$(this).next().show();
						$('.prevD').css('visibility','visible');
						if($(this).next().next().prop('id')==null){
							$('.nextD').css('visibility','hidden');
						}
						return;
					});
				});
			}
		});
		$('.prevR').click(function(){
			var divs = $("[class='role']:visible");
			divs.each(function(){
				$(this).hide();
				$(this).prev().show();
				$('.nextR').css('visibility','visible');
				if($(this).prev().prev().prop('id')==null){
					$('.prevR').css('visibility','hidden');
				}
				return;
			});
		});
		
		$('.nextR').click(function(){
			var divs = $("[class='role']:visible");
			divs.each(function(){
				$(this).hide();
				$(this).next().show();
				$('.prevR').css('visibility','visible');
				if($(this).next().next().prop('id')==null){
					$('.nextR').css('visibility','hidden');
				}
				return;
			});
		});
		$(".tijiao").click(function(){	
			$(".panel-tool-close").click();			
		});
	});
	var curRole=null;
	var curDept=null;
	function onRoleClick(ths,id,isA){
		$('.deptList').show();
		$(ths).parent().parent().find("li").removeClass("selected");
		$(ths).addClass("selected").siblings().removeClass("selected");
		curRole=id;
		if(isA){
			$('.tijiao').unbind(); 
			$('.button').show();
			if($("#deptDidId li.selected").index() != -1){
				curDept = $("#deptDidId li.selected").attr("id");
			}			
			$('.tijiao').click(function(){
				subclick(isA);
			});
		}else{
			if($("#deptDidId li.selected").index() != -1){
				$('.tijiao').unbind(); 
				$('.button').show();
				curDept = $("#deptDidId li.selected").attr("id");
				$('.tijiao').click(function(){
					subclick(isA);
				});
			}else if(curDept==null||curDept==''){
				$('.tijiao').unbind();
				$('.button').hide();
			}
		}
	}
	function onDeptClick(ths,id){		
		$(ths).parent().parent().find("li").removeClass("selected");
		$(ths).addClass("selected").siblings().removeClass("selected");
		$('.tijiao').unbind(); 
		$('.button').show();
		curDept=id;
		if($("#roleDidId li.selected").index() != -1){
			curRole = $("#roleDidId li.selected").attr("id");
		}
		$('.tijiao').click(function(){
			subclick(false);
		});
	}
	$('.dropOut_btn').click(function(){
		document.location="<%=basePath%>userLoginAction!loginout";
	});

	function subclick(isA){
		if(isA == "false"){
			isA = false;
		}else{
			isA = true;
		}
		if(curRole!=null){
			if(curDept==null&&!isA){
   				qikoo.dialog.alert("请选择部门!");
			}else{			
				$.ajax({
					url:"<%=basePath%>mainAction!getLogInf",
					async:false,
					cache:false,
					data:{'roleId':curRole,'deptId':curDept,'login':1},
					dataType:"json",
					type:"POST",
					success:function(o){
						if(o.status){
							location.reload()
							//document.location="<%=basePath%>mainAction!getMenu"; 貌似方法有问题
						}else{
 							qikoo.dialog.alert(o.message);
						}
					},
					error:function(){
						qikoo.dialog.alert("请求超时请重试!");
					}
				}); 
			}
		}else{
   			qikoo.dialog.alert("请选择角色!");
		}
	}
	</script>
</body>
</html>