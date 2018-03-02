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
</head>
	<body>
		<div class="easyui-panel" id = "panelEast" data-options="title:'医院编辑',iconCls:'icon-form',fit:true,border:false" >
			<div style="width: 100%;height: 98%">
	    		<form id="editForm" method="post" enctype="multipart/form-data">
					<input type="hidden" id="id" name="hospital.id" value="${hospital.id }">
					<input type="hidden" id="codes" name="id" value="${codes }">
					<!-- 隐藏域:解决了修改页面时,数据库的创建时间被删除的问题 -->
					<input type="hidden" name="hospital.createUser" value="${hospital.createUser }">
					<input type="hidden" name="hospital.createDate" value="${hospital.createDate }">
					<table class="honry-table" cellpadding="1" cellspacing="1" data-options="fit:true"  style="margin-left:auto;margin-right:auto;margin-top:10px;">
						<tr>
							<td class="honry-lable">医院名称:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="name" name="hospital.name" value="${hospital.name }" data-options="required:true" style="width:200px" missingMessage="请输入医院名称"/></td>
		    			</tr>
		    			<tr>
			    			 <td class="honry-lable">系统编号:</td>
			    			<td class="honry-info"><input  id="hospitalCode" name="hospital.code" value="${hospital.code}" style="width:200px" /></td>
			    		</tr>
						<tr>
							<td class="honry-lable">简称:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="brev" name="hospital.brev" value="${hospital.brev }"  style="width:200px" missingMessage="请输入简称"/></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">LOGO:</td>
							<td  style="text-align: left;"  colspan="3">
								<input type="file" name="fileLogo" id="fileLogo"
									onChange="onCheckLogo(this)">
								<input id="hlogo" style="width: 200px" type="text" >
								<input type="button" value="浏览"
									OnClick="JavaScript:$('#fileLogo').click();">
								<img id="copyLogo" alt="" >
								<input id="logo" name="hospital.logo" type="hidden"
									value="${hospital.logo }">
								<!-- (上传格式:jpg图片,大小不超过1M) -->
							</td>
						</tr>
		    			<tr>
							<td class="honry-lable">照片:</td>
							<td  style="text-align: left;"  colspan="3">
								<input type="file" name="filePhoto" id="filePhoto"
									onChange="onCheckPhoto(this)">
								<input id="hphoto" style="width: 200px" type="text" >
								<input type="button" value="浏览"
									OnClick="JavaScript:$('#filePhoto').click();">
								<img id="copyPhoto" alt="" > &nbsp;
								<input id="photo" name="hospital.photo" type="hidden" 
									value="${hospital.photo }">
								<!-- (上传格式:jpg图片,大小不超过1M) -->
							</td>
						</tr>
		    			
		    			<%-- <tr>
			    			<td class="honry-lable">LOGO:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="logo" name="hospital.logo" value="${hospital.logo }"  style="width:200px" /></td>					
						</tr>
						<tr>
							<td class="honry-lable">照片:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="photo" name="hospital.photo" value="${hospital.photo }" style="width:200px"/></td>
		    			</tr> --%>
		    			<tr>
							<td class="honry-lable">所在省市县:</td>
							<td class="honry-info"><input class="easyui-textbox" id="district" name="hospital.district" value="${hospital.district }" style="width:200px" missingMessage="请输入所在省市县"/></td>
						</tr>
		    			<tr>
			    			<td class="honry-lable">描述:</td>
							<td class="honry-info"><input class="easyui-textbox" id="description" name="hospital.description" value="${hospital.description }" style="width:200px"/></td>
						</tr>
						<tr>					
							<td class="honry-lable">交通路线:</td>
			    			<td><input class="easyui-textbox" id="trafficRoutes" name="hospital.trafficRoutes" value="${hospital.trafficRoutes }"  style="width:200px" missingMessage="请输入交通路线"/></td>
		    			</tr>
		    			<tr>
			    			<td class="honry-lable">详细地址:</td>
							<td><input class="easyui-textbox" id="address" name="hospital.address" value="${hospital.address }" style="width:200px"/></td>
		    			</tr>
		    			<tr>					
							<td class="honry-lable">等级:</td>
			    			<td class="honry-info"><input id="level" name="hospital.level" value="${hospital.level }" style="width:200px"/></td>
		    			</tr>
		    			<tr>
			    			<td class="honry-lable">盈利性:</td>
							<td class="honry-info"><input id="rentability" name="hospital.rentability" style="width:200px" value="${hospital.rentability }"/></td>
						</tr>
						<tr>
							<td class="honry-lable">性质:</td>
			    			<td><input id="property" name="hospital.property" value="${hospital.property }" style="width:200px"/></td>
						</tr>
						<tr>
							<td class="honry-lable">医生总数:</td>
			    			<td><input class="easyui-textbox" id="doctorNum" name="hospital.doctorNum" data-options="validType:'integer'" value="${hospital.doctorNum }" style="width:200px"/></td>
						</tr>
						<tr>
							<td class="honry-lable">护士总数:</td>
			    			<td><input class="easyui-textbox" id="nurseNum" name="hospital.nurseNum" data-options="validType:'integer'" value="${hospital.nurseNum }" style="width:200px"/></td>
						</tr>
						<tr>
							<td class="honry-lable">备注:</td>
			    			<td><input class="easyui-textbox" id="remark" name="hospital.remark" value="${hospital.remark }" data-options="multiline:true" style="width:200px;height:60px;"/></td>
						</tr>
		    		</table>
			    <div style="text-align:center;padding:5px">
			    <c:if test="${hospital.id == null }">
			    	<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a></c:if>
			    	<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    <c:if test="${hospital.id == null }">
			    	<a href="javascript:clear();void(0)" id="cle" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a></c:if>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
	    	</form>
	    </div>
	</div>
	<script>
	var fileServerURL="${fileServerURL}";
	$('#hospitalCode').textbox({required:true});
	var hospitalCode = $('#hospitalCode').textbox('getValue');
	var id = $('#id').val();
	//加载页面
		$(function(){
			
			//验证系统编号是否存在
			$('#hospitalCode').next("span").children().first().blur(function(){
				 var codes=$("#codes").val();
				 var codeOld=codes.split(",");
				 var codeNew=$('#hospitalCode').textbox('getValue');
				 for(var i=0;i<codeOld.length;i++){
					 if(hospitalCode == codeNew){
						 continue;
						 }
					 if(codeOld[i]==codeNew){
						 $.messager.alert('提示','该系统编码已存在，请重新输入');
						 close_alert();
					 }
				 }
			});
			
			//实现模仿上传file控件  吧真实file控件隐藏
			$('#filePhoto').hide();
			$('#fileLogo').hide(); 
			$('#hphoto').hide();
			$('#hlogo').hide(); 
			
			//编辑时用为了只显示名称 而不是把路径显示出来
			var logo = $('#logo').val();
			var photo = $('#photo').val();
			if(logo!=null&&logo!=""){
				logo = logo.replace(/\\/g,"/");//等同于replaceAll（“\\”)的用法 js没有replaceAll 用法可以用词正则代替 或者（str.replace(new RegExp("\\","gm"),"/")*/
				var flogo = logo.split(".");
				var copyLogo = logo.split("/");
				var relpath =copyLogo[copyLogo.length-4]+"/"+copyLogo[copyLogo.length-3]+"/"+copyLogo[copyLogo.length-2]+"/162X122."+flogo[flogo.length-1];
				$("#copyLogo").attr("src",fileServerURL + logo);
			}
			if(photo!=null&&photo!=""){
				photo = photo.replace(/\\/g,"/");
				var fphoto = photo.split(".");
				var copyPhoto = photo.split("/");
				var relphoto = copyPhoto[copyPhoto.length-4]+"/"+copyPhoto[copyPhoto.length-3]+"/"+copyPhoto[copyPhoto.length-2]+"/162X122."+fphoto[fphoto.length - 1];
				$("#copyPhoto").attr("src",fileServerURL + photo);
			}
			
			
			//盈利性下拉框
		$('#level').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '01',
		    	name: '三甲'
			},{
				encode: '02',
				name: '三乙'
			},{
				encode: '03',
				name: '三丙'
			}]
		});
			//盈利性下拉框
		$('#rentability').combobox({
			valueField:'id',    
		    textField:'text',
		    editable:false,
		    data: [{
		    	id: '1',
		    	text: '盈利性'
			},{
				id: '2',
				text: '非营利性'
			}]
		});
			//性质下拉框
		$('#property').combobox({
			valueField:'id',    
		    textField:'text',
		    editable:false,
		    data: [{
		    	id: '1',
		    	text: '公立'
			},{
				id: '2',
				text: '私营'
			}]
		});
		})
		/**
		 * 表单提交
		 * @author  liujinliang
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
			function submit(flg){
			  	$('#editForm').form('submit',{
			  		url:"<%=basePath%>baseinfo/hospital/saveHospital.action",
			  		 onSubmit:function(){ 
					    $.messager.progress({text:'保存中，请稍后...',modal:true});
			  			var codes=$("#codes").val();
						 var codeOld=codes.split(",");
						 var codeNew=$('#hospitalCode').textbox('getValue');
						 if(codeNew != hospitalCode){
							 for(var i=0;i<codeOld.length;i++){
								 if(hospitalCode == codeNew){
									 continue;
									 }
								 if(codeOld[i]==codeNew){
						  			$.messager.progress('close');
									 $.messager.alert('提示','该系统编码已存在，请重新输入');
									 close_alert();
									 return false;
								 }
							 }
						 }
						 
					    return $(this).form('validate');
					 },  
					success:function(data){
						 $.messager.progress('close');
						if(flg==0){
							$.messager.alert('提示','保存成功');
					   		$('#divLayout').layout('remove','east');
						   //实现刷新
				          	$("#list").datagrid("reload");
					   }else if(flg==1){
							//清除editForm
							$('#editForm').form('reset');
					  	}
					 },
					error:function(date){
						 $.messager.progress('close');
						 $.messager.alert('提示','保存失败');
					}
			  	});
			  	
	 	 	}
			//提交时验证标志
			var checkFlag = "";
			/**
			 * 处理图片和签名图片的方法
			 * @author  lt
			 * @date 2015-10-30 10:53
			 * @version 1.0
			 */
			function onCheckPhoto(filePicker) {
				var fName = document.getElementById("filePhoto").value.toLowerCase();
				/* $("#copyPhoto").attr("src", "");
				$("#copyPhoto").attr("alt", fName);  */
				$("#copyPhoto").hide();
				$("#hphoto").val(fName);
				$("#hphoto").show();
				if (fName != null && fName != "") {
					var ftype = fName.toLowerCase().split(".");
					var fTypeName = ftype[ftype.length - 1];
					if (!fTypeName == '') {
						if (fTypeName != "jpg" && fTypeName != "jpeg"
								&& fTypeName != "png" && fTypeName != "gif") {
							$.messager.alert('提示','上传的文件格式不正确，请选jpg,jpeg,png,gif格式的照片上传！');
							close_alert();
							checkFlag = "photoPatternNo";//提交时验证代表格式不对
						} else {
							if (filePicker.files[0].size > 1 * 1024 * 1024) {
								$.messager.alert('提示','上传的文件大小大于1M');
								checkFlag = "photoSizeNo";//提交时验证代表大小不对
							}else{
								 checkFlag = "";
							}
						}
					} else {
						$.messager.alert('提示','上传的文件格式不正确，请选jpg,jpeg,png,gif格式的照片上传！');
						close_alert();
						checkFlag = "photoPatternNo";//提交时验证代表格式不对
					}
				}
			}
			function onCheckLogo(filePicker) {
				var fName = document.getElementById("fileLogo").value;
				/* $("#copyLogo").attr("src", "");
				$("#copyLogo").attr("alt", fName); */
				$("#copyLogo").hide();
				$("#hlogo").val(fName);
				$("#hlogo").show();
				if (fName != null && fName != "") {
					var ftype = fName.toLowerCase().split(".");
					var fTypeName = ftype[ftype.length - 1];
					if (!fTypeName == '') {
						if (fTypeName != "jpg" && fTypeName != "jpeg"
								&& fTypeName != "png" && fTypeName != "gif") {
							$.messager.alert('提示','上传的文件格式不正确，请选jpg,jpeg,png,gif格式的照片上传！');
							close_alert();
							checkFlag = "logoPatternNo";//提交时验证代表格式不对
						} else {
							if (filePicker.files[0].size > 1 * 1024 * 1024) {
								$.messager.alert('提示','上传的文件大小不能大于1M，请重新上传！');
								close_alert();
								checkFlag = "logoSizeNo";//提交时验证代表大小不对
							}else{
								checkFlag = "";
							}
						}
					} else {
						$.messager.alert('提示','上传的文件格式不正确，请选jpg,jpeg,png,gif格式的照片上传！');
						close_alert();
						checkFlag = "logoPatternNo";//提交时验证代表格式不对
					}
				}

			}
	 	 	/**
		 * 连续添加
		 * @author  liudelin
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
	 	 	
	 	 	function addContinue(){ 
			    $('#editForm').form('submit',{  
			        url:"<%=basePath%>baseinfo/hospital/saveHospital.action",  
			        onSubmit:function(){
			        	$.messager.progress('close');
			        	var codes=$("#codes").val();
						var codeOld=codes.split(",");
						var codeNew=$('#hospitalCode').textbox('getValue');
						for(var i=0;i<codeOld.length;i++){
							if(codeOld[i]==codeNew){
								$.messager.alert('提示','该系统编码已存在，请重新输入');
								close_alert();
								return false;
							}
						 }
						if (!$('#editForm').form('validate')) {
							$.messager.show({
								title : '提示信息',
								msg : '验证没有通过,不能提交表单!'
							});
							return false;
						}
						$.messager.progress({text:'保存中，请稍后...',modal:true});
			        },  
			        success:function(){ 
			        	$.messager.progress('close');
			             //实现刷新栏目中的数据
	                     $('#list').datagrid('reload');
	                     clear();
			        },
					error : function(data) {
						$.messager.progress('close');
						$.messager.alert('提示','保存失败！');	
					}							         
			  	  }); 
	    	 }
	
		/**
		 * 清除页面填写信息
		 * @author  liujinliang
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
			function clear(){
				$('#editForm').form('reset');
			}
			function closeLayout(){
				$('#divLayout').layout('remove','east');
				$("#list").datagrid("reload");
			}
		/**
		 * 复选框选中
		 * @param defalVal 默认值
		 * @param selVal 选中值
		 * @author  liujinliang
		 * @date 2015-5-25 10:53
		 * @version 1.0
		 */
			function checkBoxSelect(defalVal,selVal){
				var name = 'hospital.'+this.name;
				var obj = document.getElementById(name);
				if(this.checked==true){
					obj.value=selVal;
				}else{
					obj.value=defalVal;
				}
			}
			
			$.extend($.fn.validatebox.defaults.rules, {
				integer : {// 验证整数  
	                validator : function(value) {  
	                    return /^[+]?[0-9]+\d*$/i.test(value);  
	                },  
	                message : '请输入整数'  
	            }    
			});
	</script>
	</body>
</html>