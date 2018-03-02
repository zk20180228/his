<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>全院病房修改</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px;padding: 0px">
<div class="easyui-panel bedWardEdit" id="panelEast">
	<div style="padding:10px">
   		<form id="editForm" method="post">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
 				<tr style="display: none">
					<td class="honry-lable">
						护士站编码：
					</td>
					<td class="honry-info">
						<input class="easyui-textbox" id="nursestation" name="bedward.nursestation" value="${bedward.nursestation }" style="width: 200"/>
					</td>
				</tr>
				<tr style="display: none">
					<td class="honry-lable">
						修改标识：
					</td>
					<td class="honry-info">
						<input class="easyui-textbox" id="bedwardzid" name="bedward.id" value="${bedward.id}" style="width: 200"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						病房编号：
					</td>
					<td class="honry-info">
						<input class="easyui-textbox" id="bedwardName" name="bedward.bedwardName" value="${bedward.bedwardName }"
							data-options="required:true" missingMessage="请选择药房" style="width: 200"/>
						<input id="bedwardCode" name="bedward.bedwardCode" type="hidden"  />
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						自定义码：
					</td>
					<td class="honry-info">
						<input class="easyui-textbox" id="codeInputcode" name="bedward.codeInputcode" value="${bedward.codeInputcode }"
							 style="width: 200"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						额定床位数：
					</td>
					<td class="honry-info">
					<input id="planbednum" name="bedward.planbednum" value="${bedward.planbednum }" class="easyui-numberspinner"  style="width:200px;"   
       							 required="required" data-options="min:1">  
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						开放床位数：
					</td>
					<td class="honry-info">
						<input id="openbednum" name="bedward.openbednum" value="${bedward.openbednum }" class="easyui-numberspinner" style="width:200px;"   
       							 required="required" data-options="min:1">  
					</td>
    			</tr>
	    	</table>
	    	 <div style="text-align:center;padding:5px">
		    	<a href="javascript:markUpdata();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
		    	&nbsp;&nbsp;<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
		    </div>
    	</form>
    </div>
</div>

<script type="text/javascript">

function markUpdata(){
	$('#bedwardCode').val($('#bedwardName').textbox('getText'));
	$('#editForm').form('submit', {    
	    url:'<%=basePath %>baseinfo/BedWard/addORedit.action',    
	    onSubmit: function(){    
	    	if (!$('#editForm').form('validate')) {
	    		$.messager.alert('友情提示','验证没有通过,该操作不可进行!','warning');
	    		setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				return false;
			}  
	    	if(!verification()){
	    		$.messager.alert('友情提示','在该护士站下，病房编号已存在!','warning');
	    		setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				return false;
	    	}
	    },    
	    success:function(data){    
	    	$.messager.alert('友情提示','操作成功!','info');  
	    	setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
	    	closeLayout();
	    	reload();
	    }    
	});
}

/***
 * 数据验证
 */
function verification(){
	var fication ;
	$.ajax({
		url:'<%=basePath %>baseinfo/BedWard/verification.action',  
		data : $('#editForm').serialize(),
		type:'post',
		async:false,
		success: function(data){
			fication = data;
		}
	});
	return fication;
}



</script>
</body>
</html>