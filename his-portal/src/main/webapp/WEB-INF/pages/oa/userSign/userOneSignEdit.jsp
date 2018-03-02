<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
	body {
		-moz-user-select : none;
		-webkit-user-select: none;
	}
</style>
</head>
<body>
	<div class="easyui-panel" id="panelEast" data-options="iconCls:'icon-form',border:false,fit:true" style="width:580px">
		<div style="padding:10px">
			<form id="editForm">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin:10px auto 0;">
					<input type="hidden" id="userSignId" name="userSign.id" value="${userSign.id }">
					<input type="hidden" id="userAcc" name="userSign.userAcc" value="${userSign.userAcc }">
					<tr>
						<td class="honry-lable">类别：</td>
						<td class="honry-info"><input id="signType" name="userSign.signType" value="${userSign.signType }" data-options="required:true,editable:false"/></td>
					</tr>
					<tr>
						<td class="honry-lable">电子印章分类：</td>
						<td class="honry-info"><input id="signCategory" name="userSign.signCategory" value="${userSign.signCategory }" data-options="required:true,editable:false"/></td>
					</tr>
					<tr>
						<td class="honry-lable">用户：</td>
						<td class="honry-info"><input id="userAccName" name="userSign.userAccName" value="${userSign.userAccName }" data-options="prompt:'请回车查询',required:true,editable:false,missingMessage:'请回车查询',width:300"/></td>
					</tr>
					<tr>
						<td class="honry-lable">名称：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="signName" name="userSign.signName" value="${userSign.signName }" data-options="required:true,missingMessage:'请输入名称',width:300"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">密码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" type="password" id="signPassword" name="userSign.signPassword" value="${userSign.signPassword }" data-options="required:true,width:300"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">再输入一次：</td>
		    			<td class="honry-info"><input class="easyui-textbox" type="password" id="signPassword1" value="${userSign.signPassword }"  data-options="required:true,width:300"/></td>
	    			</tr>
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
			<div style="text-align:center;padding:5px">
		    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
		    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
		    	<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
		    </div>
		</div>
	</div>
	<div id="menuWin"></div>
	<script type="text/javascript">
	var userAccount = "${userAccount}";
	var userName = "${userName}";
	$('#userAccName').textbox({
	});
	$('#signCategory').combobox({
		width:300,
		valueField:'code',
		textField:'name',
		data:[{code:1,name:'按用户'}],
		select:1,
		onSelect:function(record){
			if($('#signType').combobox('getValue')=='1'){
				if(record.code!=1){
					$.messager.alert('提示','电子签名只能按用户分类！');
					$('#signCategory').combobox('setValue','1');
				}
			}
		}
	});
	$('#signType').combobox({
		width:300,
		valueField:'code',
		textField:'name',
		data:[{code:1,name:'电子签名'}],
		onLoadSuccess:function(none){
			$('#signType').combobox('select',1);
		},
		onSelect:function(record){
			$('#signCategory').combobox('select',1);
			$('#userAccName').textbox('setValue',userName);
			$('#userAcc').val(userAccount);
		}
	});
	
	bindEnterEvent('userAccName',openBedWin,'easyui');
	$('#signType').combobox('textbox').bind('focus',function(){
		$('#signType').combogrid('showPanel');
	});
	$('#signCategory').combobox('textbox').bind('focus',function(){
		$('#signCategory').combogrid('showPanel');
	});
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
	function doNothing(){  
        window.event.returnValue=false;  
        return false;  
    }
	function submit(){
		if($('#signPassword').textbox('getValue')!=$('#signPassword1').textbox('getValue')){
			$.messager.alert('提示','两次输入的密码不一致，请重新输入!');
			$('#signPassword').textbox('setValue','');
			$('#signPassword1').textbox('setValue','');
			return;
		}
		if (!$('#editForm').form('validate')) {
			$.messager.show({
				title : '提示信息',
				msg : '验证没有通过,不能提交表单!'
			});
			$('#selectBut option').prop("selected",false);
			return;
		}
		var signType = $('#signType').combobox('getValue');
		var userSignId = $('#userSignId').val();
		if(userSignId==null||userSignId==''){
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
		}
		$.ajax({
	        url:'<%=basePath%>oa/userSign/saveUserSign.action',
	        type:'POST',    
	        data:new FormData($("#editForm")[0]),    
	        async:false,    
	        cache:false,    
	        contentType:false,    
	        processData:false,    
	        success: function (dataMap) {    
	        	if(dataMap.resCode=="success"){
					$('#ed').datagrid('load');
					$('#divLayout').layout('remove','east');
				}
				$.messager.alert('提示',dataMap.resMsg);
	        },    
	        error: function () {    
	        	$.messager.alert('提示','请求失败!');
	        }    
	    });
	}
	function openBedWin(){
		var signType = $('#signType').combobox('getValue');
		if(signType==null||signType==""){
			$.messager.alert('提示','还未选择类别!');
			return;
		}
		if(signType==1){
			$.messager.alert('提示','电子签名只能选择自己!');
			return;
		} 
		var signCategory = $('#signCategory').combobox('getValue');
		if(signCategory==null||signCategory==''){
			$.messager.alert('提示','还未签章分类!');
			return;
		}
		var signCategory = $('#signCategory').combobox('getValue');
		Adddilog("请选择用户",'<%=basePath%>oa/userSign/queryOpen.action?type='+signCategory,'40%','60%');
	}
	function closeLayout(){
		$('#divLayout').layout('remove','east');
	}
	function clear(){
		$('#editForm').form('reset');
	}
	//加载模式窗口
	function Adddilog(title, url, width, height) {
		$('#menuWin').dialog({    
		    title: title,    
		    width: width,    
		    height: height,    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		});
	}
	function queryValue(names,codes){
		$('#userAccName').textbox('setValue',names);
		$('#userAcc').val(codes);
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