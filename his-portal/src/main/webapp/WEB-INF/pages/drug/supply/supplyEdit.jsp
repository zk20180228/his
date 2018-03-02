<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>添加页面</title>
	</head>
	<body>
		<div id="p" class="easyui-panel supplyEditFont" title="编辑" style="padding: 10px; background: #fafafa;" data-options="fit:'true',border:'false'">
			<form id="editForm" method="post" enctype="multipart/form-data">
				<input type="hidden" id="id" name="id" value="${supplycompany.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%">
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">名称：</span>
						</td>
						<td style="text-align: left;">
							<input id="hospitalID" class="easyui-textbox" value="${supplycompany.companyName}"  style="width: 200px" name="companyName"  data-options="required:true" missingMessage="请输入供应商名称" >
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">自定义码：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${supplycompany.companyInputcode }" name="companyInputcode"  style="width: 200px"></input>

						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">联系方式：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${supplycompany.companyLink }" name="companyLink"  style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">开户银行：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${supplycompany.companyBank }" name="companyBank"  style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">开户账号：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${supplycompany.companyAccount }" name="companyAccount"  style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">地址：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${supplycompany.companyAddress }" name="companyAddress"  style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">GSP：</span>
						</td>
						
						<td style="text-align: left;"  colspan="3">
							<input class="easyui-textbox" id="companyGsp" data-options="readonly:'true',required:true,missingMessage:'请选择文件上传！'" name="supplycompany.companyGsp"
								value="${supplycompany.companyGsp } " style="width: 200px">
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
							<input class="easyui-textbox" type="text" value="${supplycompany.companyRemark }" name="companyRemark"  style="width: 200px"></input>
						</td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<c:if test="${empty supplycompany.id}">
						<a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
					</c:if>
					<a href="javascript:submit(0);" data-options="iconCls:'icon-save'"  class="easyui-linkbutton">确定</a>
					<a href="javascript:clear()" data-options="iconCls:'icon-clear'"  class="easyui-linkbutton">清空</a>
					<a href="javascript:closeLayout()" data-options="iconCls:'icon-cancel'" class="easyui-linkbutton">关闭</a>
				</div>
			</form>
		</div>
		<script type="text/javascript">
			$(function() {
				$('#cost').hide();
			});
			//表单提交submit信息
		  	function submit(flg){
		  		var flgs=zswbjy();
		  		if(flgs){
		  			$('#editForm').form('submit',{
				  		url:'<%=basePath%>drug/supply/saveOrupdate.action',
				  		 onSubmit:function(){ 
				  		 	if(!$('#editForm').form('validate')){
								$.messager.alert('提示',"验证没有通过,不能提交表单!");
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								   return false ;
						     }
				  		 	$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
						 },  
						success:function(data){
							$.messager.progress('close');
							if(flg==0){
								$.messager.alert('提示',"保存成功");
								closeLayout();
							    //实现刷新
					          	$("#list").datagrid("reload");
						   }else if(flg==1){
								//清除editForm
								$('#editForm').form('reset');
						  	}
						 },
						error:function(data){
							$.messager.progress('close');
							$.messager.alert('提示',"保存失败");
						}
				  	});
		  		}
			  	
		  	}
			//清除所填信息
			function clear() {
				$('#editForm').form('clear');
				$("#zswbdiv").empty();
			}
			
			function zswbjy(){
			    var zswb = $("#cost").val();
			    var path = $('#companyGsp').textbox('getText');
			    if(path != null && path != ''){
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
					$('#companyGsp').textbox('setText',path);
				}
				
			}
		</script>
	</body>
</html>
