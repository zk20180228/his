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
		<title>添加页面</title>
	</head>
	<body>
		<div id="panelEast" class="easyui-panel manufacturertEditFont" title="编辑" style="padding: 10px; background: #fafafa;" data-options="fit:'true',border:'false'">
			<form id="hospitalForm" action="" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" value="${manufacturer.id }">
					<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%">
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">名称：</span>
						</td>
						<td style="text-align: left;">
							<input id="hospitalID" class="easyui-textbox" value="${manufacturer.manufacturerName }" data-options="required:true,missingMessage:'请填写名称!'" style="width: 200px" name="manufacturerName">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">自定义码：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${manufacturer.manufacturerInputcode }" name="manufacturerInputcode" id="manufacturerInputcode" data-options="required:true,missingMessage:'请填写自定义码!'" style="width: 200px"></input>

						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">联系方式：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${manufacturer.manufacturerLink }" name="manufacturerLink" id="manufacturerLink" data-options="required:true,missingMessage:'请填写联系方式!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">开户银行：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${manufacturer.manufacturerBank }" id="manufacturerBank" name="manufacturerBank" data-options="required:true,missingMessage:'请填写开户银行!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">开户账号：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${manufacturer.manufacturerAccount }" id="manufacturerAccount" name="manufacturerAccount" data-options="required:true,missingMessage:'请填写开户账号!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">地址：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${manufacturer.manufacturerAddress }" id="manufacturerAddress" name="manufacturerAddress" data-options="required:true,missingMessage:'请填写地址!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">GMP：</span>
						</td>
						
						<td style="text-align: left;"  colspan="3">
							<input class="easyui-textbox" id="manufacturerGmp" data-options="readonly:true,required:true,missingMessage:'请选择文件上传！'" name="manufacturer.manufacturerGmp"
								value="${manufacturer.manufacturerGmp}" style="width: 200px">
							<input type="file" name="fileCost" id="cost"
								onChange="change()">
							<input type="button" value="浏览"
								OnClick="JavaScript:$('#cost').click();">
							<div id="zswbdiv"></div>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">备注：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${manufacturer.manufacturerRemark }" id="manufacturerRemark" name="manufacturerRemark" data-options="required:true,missingMessage:'请填写备注!'" style="width: 200px"></input>
						</td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<c:if test="${manufacturer.id == null}">
						<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
					</c:if>
					<a id="hospitalOKbtn" href="javascript:void(0)" data-options="iconCls:'icon-save'" onclick="onClickOKbtn()" class="easyui-linkbutton">确定</a>
					<a id="hospitalClearbtn" href="javascript:void(0)" data-options="iconCls:'icon-clear'" onclick="clears()" class="easyui-linkbutton">清空</a>
					<a id="hospitalClearbtn" href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeLayout()" class="easyui-linkbutton">关闭</a>
				</div>
			</form>
		</div>
		<script type="text/javascript">
	$(function() {
		$('#manufacturerGmp').textbox({
			readonly:true,
			required:true,
			missingMessage:'请选择文件上传！'
		});
		$('#cost').hide();
		$.extend($.fn.validatebox.defaults.rules, {
			email : {
				validator : function(value) { //email验证	
					return /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(value);
				},
				message : '请输入有效的邮箱账号(例：123456@qq.com)'
			},
			Phone : function(value) {
				var rex = /^1[3-8]+\d{9}$/;
				//var rex=/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
				//区号：前面一个0，后面跟2-3位数字 ： 0\d{2,3}
				//电话号码：7-8位数字： \d{7,8
				//分机号：一般都是3位数字： \d{3,}
				//这样连接起来就是验证电话的正则表达式了：/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/		 
				var rex2 = /^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
				if (rex.test(value) || rex2.test(value)) {
					return true;
				} else {
					return false;
				}

			},
			message : '请输入正确电话或手机格式'
		});
	});
	function onClickOKbtn() {
		var url;
		url = '<%=basePath %>drug/manufacturer/saveOrupdateManufacturer.action';
		var flg=zswbjy();
		if(flg){
			$('#hospitalForm').form('submit', {
				url : url,
				data : $('#hospitalForm').serialize(),
				dataType : 'json',
				onSubmit : function() {
					if (!$('#hospitalForm').form('validate')) {
						$.messager.alert('提示',"验证没有通过,不能提交表单!");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return false;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});	//显示进度条
				},
				success : function(data) {
					$.messager.progress('close');
					$('#list').datagrid('load', '<%=basePath %>drug/manufacturer/queryDrugManufacturer.action');
					closeLayout();
					$.messager.alert('提示',"操作成功!");
				},
				error : function(data) {
					$.messager.progress('close');
					$.messager.alert('提示',"操作失败!");
				}
			});
		}
	}
	/**
	 *
	 *连续添加
	 *
	 */
	function addContinue() {
		if (addAndEdit == 0) {
			$('#hospitalForm').form('submit', {
				url : '<%=basePath %>drug/manufacturer/saveOrupdateManufacturer.action',
				data : $('#hospitalForm').serialize(),
				dataType : 'json',
				onSubmit : function() {
					if (!$('#hospitalForm').form('validate')) {
						$.messager.alert('提示',"验证没有通过,不能提交表单!");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return false;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});	//显示进度条
				},
				success : function(data) {
					$.messager.progress('close');
					$('#list').datagrid('load', '<%=basePath %>drug/manufacturer/queryDrugManufacturer.action');
					$.messager.alert('提示',"操作成功!");
					clears();
				},
				error : function(data) {
					$.messager.progress('close');
					$.messager.alert('提示',"操作失败!");
				}
			});
		} else {
			$.messager.alert('操作提示',"添加按钮不能执行修改操作!");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	//清除所填信息
	function clears() {
		$('#hospitalForm').form('clear');
		$("#zswbdiv").empty();
	}
	
	function zswbjy(){
	    var zswb = $("#cost").val();
	    var path = $('#manufacturerGmp').textbox('getText');
	    if(path != null){
		    if(zswb.length>1 && zswb != ''){
		        var lodt = zswb.lastIndexOf(".");
		        var type = zswb.substring(lodt+1);
		        if(type == "doc" ||type == "docx" || type == "xsl" || type == "xlsx" ){
		        	if(zswb.length>1){
		        		 $("#zswbdiv").empty();
				            return true;
		        	}else{
		        		$("#zswbdiv").empty();
				        $("#zswbdiv").append("<font color='red'>正式文本不能为空！</font>");
				        return false;
		        	}
		        	
		        }else if( type == "jpeg" || type == "jpg" || type == "png" || type == "gif"){
		        	  $("#zswbdiv").empty();
			            return true;
		        }else{
		        	$("#zswbdiv").empty();
		            $("#zswbdiv").append("<font color='red'>上传的文件必须为文档、表格或图片！</font>");
		            return false;
		        }
		    } else{
		    	return true;
		    }
	    }else{
	        $("#zswbdiv").empty();
	        $("#cost").empty();
	        $("#zswbdiv").append("<font color='red'>请选择文件上传！</font>");
	        return false;
	    }
	}
	function clearFile(){
		$("#zswbdiv").empty();
	}
	function change(){
		if(zswbjy()){
			var zswb = $("#cost").val();
			var list = zswb.split("\\");
			var path = list[list.length - 1];
			$('#manufacturerGmp').textbox('setText',path);
		}
		
	}
</script>
	</body>
</html>
