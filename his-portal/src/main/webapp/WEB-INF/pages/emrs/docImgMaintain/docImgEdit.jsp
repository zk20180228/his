<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>医用图片编辑界面</title>
<%@ include file="/common/metas.jsp"%>

</head>
<body>
	<div class="easyui-panel" id = "panelEast" data-options="title:'编辑',iconCls:'icon-form',border:false,fit:true" style="width:100%;">
		<div style="padding:10px">
			<form id="editForm" method="post" enctype="multipart/form-data">
			<input id="picId" type="hidden" name="emcPicture.id" value="${emcPicture.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" style="margin-left:auto;margin-right:auto;">
					<tr>
						<td class="honry-lable">图片名称：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="picName" name="emcPicture.picName" value="${emcPicture.picName }" data-options="required:true" style="width:200px"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">身体部位：</td>
		    			<td class="honry-info"><input id="picBody" name="emcPicture.picBody" value="${emcPicture.picBody }" style="width:200px"/>
	    			</tr>	
	    			<tr>
						<td class="honry-lable">
							<span style="font-size: 14">图片：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;"  colspan="3">
							<input type="file" name="filePhoto" id="photoFile"
								onChange="onCheckPHOTO(this)">
							<img id="copyPhoto"  alt="" onclick="showPhoto('${emcPicture.picPath }')">
							<br>
							<input type="button" value="浏览"
								OnClick="JavaScript:$('#photoFile').click();">
							(上传格式:jpg,jpeg,png,gif图片,大小不超过1M)
						</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">图片路径：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="picPath" name="emcPicture.picPath" value="${emcPicture.picPath }" style="width:200px"/></td>
	    			</tr> 
	    			<tr>
						<td class="honry-lable">备注：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="picBak" name="emcPicture.picBak" value="${emcPicture.picBak }" data-options="multiline:true" style="width:200px;height:60px;"/></td>
	    			</tr> 
				</table>
				<div style="text-align:center;padding:5px">
				<c:if test="${empty emcPicture.id}">
			    	<a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a></c:if>
			    	<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" id="cle">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
			</form>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
//清除
function clear(){
	if($('#picId').val() != null && $('#picId').val() != ""){
		$('#editForm').form('reset');
	}else{
		$('#editForm').form('clear');
		$("#copyPhoto").attr("alt", "");
	}
	
}
//关闭
function closeLayout(){
	$('#divLayout').layout('remove','east');
}

$(function(){
	<%-- 图片预览，因大小问题，暂时无法解决 --%>
// 	if($('#picPath').val().length > 0){
// 		var path = $('#picPath').val().replace(/\\/g, "/").substring(1);
<%-- 		$("#copyPhoto").attr("src", "<%=basePath%>"+path); --%>
		
// 	}
	if($('#picId').val() != null && $('#picId').val() != ""){
		$('#cle').html("还原");
	}
	$('#picBody').combobox({
		url: "<%=basePath%>emrs/docImgMaintain/queryCheckpoint.action",	
		disabled:false,
// 		editable:false,
		valueField:'encode',    
		textField:'name',
		onHidePanel:function(none){
		    var data = $(this).combobox('getData');
		    var val = $(this).combobox('getValue');
		    var result = true;
		    for (var i = 0; i < data.length; i++) {
		        if (val == data[i].encode) {
		            result = false;
		        }
		    }
		    if (result) {
		        $(this).combobox("clear");
		    }else{
		        $(this).combobox('unselect',val);
		        $(this).combobox('select',val);
		    }
		},
		filter: function(q, row){
		    var keys = new Array();
		    keys[keys.length] = 'encode';
		    keys[keys.length] = 'name';
		    keys[keys.length] = 'pinyin';
		    keys[keys.length] = 'wb';
		    keys[keys.length] = 'inputCode';
		    return filterLocalCombobox(q, row, keys);
		}
	});
	
	$('#photoFile').hide();
});
var checkFlag;
/**
 * 处理图片和签名图片的方法
 * @author  lt
 * @date 2015-10-30 10:53
 * @version 1.0
 */
function onCheckPHOTO(filePicker) {
	var fName = document.getElementById("photoFile").value.toLowerCase();
	$("#copyPhoto").attr("src", "");
	$("#copyPhoto").attr("alt", fName);
	if (fName != null && fName != "") {
		var ftype = fName.toLowerCase().split(".");
		var fTypeName = ftype[ftype.length - 1];
		if (!fTypeName == '') {
			if (fTypeName != "jpg" && fTypeName != "jpeg"
					&& fTypeName != "png" && fTypeName != "gif") {
				checkFlag = "photoPatternNo";//提交时验证代表格式不对
				$.messager.alert('提示',"上传的文件格式不正确，请选jpg,jpeg,png,gif格式的照片上传！");
			} else {
				if (filePicker.files[0].size > 1 * 1024 * 1024) {
					$.messager.alert('提示',"上传的文件大小大于1M");
					checkFlag = "photoSizeNo";//提交时验证代表大小不对
				}else{
					 checkFlag = "";
					 $('#picPath').textbox('setValue',fName);
				}
			}
		} else {
			checkFlag = "photoPatternNo";//提交时验证代表格式不对
			$.messager.alert('提示',"上传的文件格式不正确，请选jpg,jpeg,png,gif格式的照片上传！");
		}
	}
}

function submit(flg) {
	$('#editForm').form('submit', {
		url:"<%=basePath %>emrs/docImgMaintain/saveOrUpdateEmcPicture.action",
		onSubmit : function() {
			if($('#picName').val()==""){
       		 $.messager.alert('操作提示', '名称不能为空！','error');  
                return false;  
			}
			if (!$('#editForm').form('validate')) {
				$.messager.show({
					title : '提示信息',
					msg : '验证没有通过,不能提交表单!'
				});
				return false;
			}
			if(checkFlag == "photoPatternNo"){
				$.messager.alert('提示信息','图片格式不正确,不能提交表单!','error');
				return false;
			}
			if(checkFlag == "photoSizeNo"){
				$.messager.alert('提示信息','图片大小不正确,不能提交表单!','error');
				return false;
			}
			$.messager.progress({text:'保存中，请稍后...',modal:true});
		},
		success : function(data) {
			 $.messager.progress('close');
			if (flg == 0) {
				$.messager.alert('提示',"保存成功");
				$('#divLayout').layout('remove', 'east');
				//实现刷新
				$("#list").datagrid("reload");
			} else if (flg == 1) {
				//清除editForm
				$('#editForm').form('reset');
			}
		},
		error : function(date) {
			 $.messager.progress('close');
			 $.messager.alert('提示',"保存失败");
		}
	});
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>