<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>个人签名</title>
<style type="text/css">
	body {
		-moz-user-select : none;
		-webkit-user-select: none;
	}
</style>
</head>
<body>
	<div id="diaEmp" class="easyui-panel"  style="padding: 10px; background: #fafafa;" data-options="fit:'true',border:'false'">
		<form id="editForm">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin:10px auto 0;">
					<input type="hidden" id="userSignId" name="userSign.id" value="${userSign.id }">
					<input type="hidden" id="userAcc" name="userSign.userAcc" value="${userAccount }">
					<input type="hidden" id="userAccName" name="userSign.userAccName" value="${userName }">
					<input type="hidden" id="signType" name="userSign.signType" value="1"/>
					<input type="hidden" id="signCategory" name="userSign.signCategory" value="1"/>
					<tr>
						<td class="honry-lable">类别：</td>
						<td class="honry-info">电子签名</td>
					</tr>
					<tr>
						<td class="honry-lable">电子印章分类：</td>
						<td class="honry-info">电子印章分类</td>
					</tr>
					<tr>
						<td class="honry-lable">用户：</td>
						<td class="honry-info">${userName }</td>
					</tr>
					<tr>
						<td class="honry-lable">名称：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="signName" name="userSign.signName" value="${userSign.signName }" data-options="required:true,missingMessage:'请输入名称',width:300"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">密码：</td>
		    			<td class="honry-info"><input class="easyui-textbox"  sign="true" type="password" id="signPassword" value="${userSign.signPassword }" name="userSign.signPassword" data-options="required:true,width:300"/>
		    				<a  id="signPasswordShow" class="easyui-linkbutton" onclick="javascript:void(0);clickPas('signPassword','signPasswordShow');" data-options="iconCls:'icon-tip',plain:true"></a>
		    			</td>
	    			</tr>
<!-- 	    			<tr> -->
<!-- 						<td class="honry-lable">再输入一次：</td> -->
<%-- 		    			<td class="honry-info"><input class="easyui-textbox" sign="true" type="password" id="signPassword1" value="${userSign.signPassword }"  data-options="required:true,width:300"/> --%>
<!-- 		    			<a  id="signPassword1Show" class="easyui-linkbutton" onclick="javascript:void(0);clickPas('signPassword1','signPassword1Show');" data-options="iconCls:'icon-tip',plain:true"></a></td> -->
<!-- 	    			</tr> -->
					<tr>
						<td class="honry-lable">自定义码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="signName" name="userSign.signInputcode" value="${userSign.signInputcode }" data-options="width:300"/></td>
	    			</tr>
					<tr>
						<td class="honry-lable">描述：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="signDesc" name="userSign.signDesc" value="${userSign.signDesc }" data-options="multiline:true,height:100,width:300"/></td>
	    			</tr>				
					<tr>
						<td class="honry-lable">电子图片：</td>
		    			<td class="honry-info"><div id="imgDivId" style="display:none;padding-bottom:5px;"><img id="imgId"></div><input id="signInfo" name="signInfoFile"></td>
	    			</tr>
				</table>
			</form>
	<div style="text-align: center; padding: 5px">
		<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
		<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
		<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeDialog()" class="easyui-linkbutton">关闭</a>
	</div>
</div>
<script type="text/javascript">
	$(function(){
		$('#imgId').prop('src',null);
		var id = $('#userSignId').val();
		if(id!=null&&id!=''){
			$('#imgDivId').show();
			$('#imgId').prop('src','<%=basePath%>oa/userSign/getuserSignInfo.action?search='+id+'&v='+Math.random());
		}
		$('#signInfo').filebox({
			required:(id!=null&&id!='')?false:true,
			buttonText:'选择文件',
			prompt:'请选择电子图片',
			missingMessage:'请选择电子图片',
			width:300,
			onChange:function(newValue, oldValue){
				$('#imgDivId').hide();
			}
		});
	});
	function submit(){
// 		if($('#signPassword').textbox('getValue')!=$('#signPassword1').textbox('getValue')){
// 			$.messager.alert('提示','两次输入的密码不一致，请重新输入!');
// 			$('#signPassword').textbox('setValue','');
// 			$('#signPassword1').textbox('setValue','');
// 			return;
// 		}
		if (!$('#editForm').form('validate')) {
			$.messager.show({
				title : '提示信息',
				msg : '验证没有通过,不能提交表单!'
			});
			$('#selectBut option').prop("selected",false);
			return;
		}
		var signType = $('#signType').val();
		var userSignId = $('#userSignId').val();
<%-- 		if(userSignId==null||userSignId==''){
			if(signType==1){
				var userAcc = $('#userAcc').val();
				$.ajax({
					url: '<%=basePath%>oa/userSign/getSignRow.action',
					data:{userAccount:userAcc},
					type:'post',
					async:false,
					success: function(data) {
						if(data.id!=null){
							$.messager.alert('提示','该用户已经存在电子签名，请停用或删除后再继续添加!');
							return;
						}
					}
				});
			}
		} --%>
		$.ajax({
	        url:'<%=basePath%>oa/userSign/saveExecSign.action',
	        type:'POST',    
	        data:new FormData($("#editForm")[0]),    
	        async:false,    
	        cache:false,    
	        contentType:false,    
	        processData:false,    
	        success: function (dataMap) {    
				$.messager.alert('提示',dataMap.resMsg);
				closeDialog();
	        },    
	        error: function () {    
	        	$.messager.alert('提示','请求失败!');
	        }    
	    });
	}
	function del(){
		var ids = $('#userSignId').val();
		if(ids==null||ids==''){
			$.messager.alert('提示','还未添加过数据，无法删除!');
			return;
		}
		$.ajax({
			url: '<%=basePath%>oa/userSign/deleteSign.action',
			data:{signid:ids},
			type:'post',
			success: function(data) {
				$.messager.alert('提示',data.resMsg);	
				closeDialog();
			}
		});	
	}
	function clickPas(viewId,linkId){
		var sign=$('#'+viewId).attr('sign');
		if('true'==sign){
			viewPas(viewId,linkId);
		}else{
			hidePas(viewId,linkId);
		}
	}
	//查看密码
	function viewPas(viewId,linkId){
		$('#'+viewId).textbox({
			type:'text',
			
		});
		$('#'+linkId).linkbutton({
			iconCls:'icon-lock',
		})
// 		$('#'+viewId).off("click").on("click",hidePas(viewId));
		//$('#'+viewId).off("click", '#'+viewId,hidePas(viewId));
		$('#'+viewId).attr('sign','false');
	}
	//隐藏密码
	function hidePas(viewId,linkId){
		$('#'+viewId).textbox({
			type:'password',
		});
		$('#'+linkId).linkbutton({
			iconCls:'icon-tip',
		})
		$('#'+viewId).attr('sign','true');
// 		$('#'+viewId).off("click").on("click",viewPas(viewId));
		//$('#'+viewId).off("click", '#'+viewId,viewPas(viewId));
	}
	function iEsc(){ return false; }
	function iRec(){ return true; }
	function DisableKeys() {
	if(event.ctrlKey || event.shiftKey || event.altKey)  {
	window.event.returnValue=false;
	iEsc();}
	}
	document.ondragstart=iEsc;
	document.onkeydown=DisableKeys;
	document.oncontextmenu=iEsc;
	if (typeof document.onselectstart !="undefined")
	document.onselectstart=iEsc;
	else{//qsyz.net
	document.onmousedown=iEsc;
	document.onmouseup=iRec;
	}
	function DisableRightClick(www_qsyz_net){
	if (window.Event){
	if (www_qsyz_net.which == 2 || www_qsyz_net.which == 3)
	iEsc();}
	else
	if (event.button == 2 || event.button == 3){
	event.cancelBubble = true
	event.returnValue = false;
	iEsc();}
	}
</script>
</body>
</html>