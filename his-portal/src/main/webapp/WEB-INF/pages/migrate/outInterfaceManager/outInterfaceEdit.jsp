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
		<div class="easyui-panel" id = "panelEast" data-options="title:'接口编辑',iconCls:'icon-form',fit:true,border:false" >
			<div style="width: 100%;height: 98%">
	    		<form id="editForm" method="post" >
					<input type="hidden" id="id" name="id" value="${exterInter.id }">
					<!-- 隐藏域:解决了修改页面时,数据库的创建时间被删除的问题 -->
					<input type="hidden" id="code" name="code" value="${exterInter.code }">
					<table class="honry-table" cellpadding="1" cellspacing="1" data-options="fit:true"  style="margin-left:auto;margin-right:auto;margin-top:10px;">
<!-- 		    			<tr> -->
<!-- 			    			 <td class="honry-lable">接口代码:</td> -->
<%-- 			    			<td class="honry-info"><input class="easyui-textbox"  id="code" name="code" value="${exterInter.code}" data-options="required:true" style="width:200px" /></td> --%>
<!-- 			    		</tr> -->
						<tr>
							<td class="honry-lable">接口名称:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="name" name="name" value="${exterInter.name }" data-options="required:true" style="width:200px" missingMessage="请输入接口名称"/></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">接口服务:</td>
			    			<td class="honry-info"><input  id="serve" class="easyui-textbox" name="serve" value="${exterInter.serve }"  style="width:200px" /></td>
		    			</tr>
		    			
						<tr>
							<td class="honry-lable">服务名称:</td>
			    			<td class="honry-info"><input  id="serveCode" name="serveCode" value="${exterInter.serveCode }"  style="width:200px" /></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">参数字段:</td>
			    			<td class="honry-info"><input  id="parameterField" class="easyui-textbox" name="parameterField" value="${exterInter.parameterField }"  style="width:200px" /></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">厂商:</td>
			    			<td class="honry-info"><input  id="firmCode" name="firmCode" value="${exterInter.firmCode }"  style="width:200px" /></td>
		    			</tr>
<!-- 		    			<tr> -->
<!-- 							<td class="honry-lable">接口方式:</td> -->
<%-- 							<td class="honry-info"><input class="easyui-textbox" id="way" name="way" value="${exterInter.way }" style="width:200px" /></td> --%>
<!-- 						</tr> -->
		    			<tr>
			    			<td class="honry-lable">当前方式:</td>
							<td class="honry-info"><input class="easyui-combobox" id="curWay" name="curWay" value="${exterInter.curWay }" style="width:200px" data-options="required:true,editable:false,
								valueField: 'id',
								textField: 'value',
								data:[
								{id:'json',value:'json'},
								{id:'webService',value:'webService'},
								{id:'xml',value:'xml'},
								{id:'view',value:'view'}]"/></td>
						</tr>
						<tr>					
							<td class="honry-lable">接口读写:</td>
			    			<td><input class="easyui-combobox" id="rwJuri" name="rwJuri" value="${exterInter.rwJuri }"  style="width:200px" data-options="required:true,editable:false,
								valueField: 'id',
								textField: 'value',
								data:[
								{id:'0',value:'只读'},
								{id:'1',value:'只写'},
								{id:'2',value:'读写'}]"/></td>
		    			</tr>
		    			<tr>
			    			<td class="honry-lable">接口调用间隔:</td>
							<td><input class="easyui-textbox" id="callSapce" name="callSapce" value="${exterInter.callSapce }" style="width:200px" data-options="required:true"/></td>
		    			</tr>
		    			<tr>					
							<td class="honry-lable">间隔单位:</td>
			    			<td class="honry-info"><input class="easyui-combobox" id="callUnit" name="callUnit" value="${exterInter.callUnit }" style="width:200px" data-options="required:true,
							editable:false,
							valueField: 'id',
							textField: 'value',
							data:[
							{id:'S',value:'秒'},
							{id:'M',value:'分'},
							{id:'H',value:'时'},
							{id:'D',value:'天'},
							{id:'W',value:'周'}]"/></td>
		    			</tr>
		    			<tr>
			    			<td class="honry-lable">频次:</td>
							<td><input class="easyui-textbox" id="frequency" name="frequency" value="${exterInter.frequency }" style="width:200px" data-options="required:true,prompt:'频次格式:10/10'"/></td>
		    			</tr>
		    			<tr>
			    			<td class="honry-lable">是否安全认证:</td>
							<td class="honry-info"><input class="easyui-combobox" id="isAuth" name="isAuth" style="width:200px" value="${exterInter.isAuth }" data-options="required:true,editable:false,
								valueField: 'id',
								textField: 'value',
								data:[
								{id:'0',value:'是'},
								{id:'1',value:'否'}]"/></td>
						</tr>
						<tr>
							<td class="honry-lable">认证有效期:</td>
			    			<td><input class="easyui-textbox" id="authVali" name="authVali" value="${exterInter.authVali }" style="width:200px" data-options="required:true"/></td>
						</tr>
						<tr>
							<td class="honry-lable">认证有效期单位:</td>
			    			<td><input class="easyui-combobox" id="authUnit" name="authUnit"  value="${exterInter.authUnit }" style="width:200px" data-options="required:true,
							editable:false,
							valueField: 'id',
							textField: 'value',
							data:[
							{id:'M',value:'分'},
							{id:'H',value:'时'},
							{id:'D',value:'天'},
							{id:'W',value:'周'}]"/></td>
						</tr>
						<tr>
							<td class="honry-lable">有效期开始时间:</td>
			    			<td><input id="authStime" name="authStime" type="text" class="Wdate" value="${exterInter.authStimeSDF }"   onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'{%y+100}-%M-%d %H-%m-%s'})"  readonly="readonly" /> </td>
						</tr>
						<tr>
							<td class="honry-lable">有效期结束时间:</td>
			    			<td><input id="authEtime" name="authEtime" type="text" class="Wdate" value="${exterInter.authEtimeSDF }"   onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'{%y+100}-%M-%d %H-%m-%s'})" readonly="readonly"  /> </td>
						</tr>
						<tr>
							<td class="honry-lable">状态:</td>
			    			<td><input class="easyui-combobox" id="state" name="state" value="${exterInter.state }"  style="width:200px;"  data-options="required:true,editable:false,
								valueField: 'id',
								textField: 'value',
								data:[
								{id:'0',value:'正常'},
								{id:'1',value:'停用'}]"/></td>
						</tr>
						<tr>
							<td class="honry-lable">执行SQL:</td>
			    			<td><input class="easyui-textbox" id="implementSql" name="implementSql" value="${exterInter.implementSql }" data-options="multiline:true" style="width:200px;height:60px;"/></td>
						</tr>
						<tr>
							<td class="honry-lable">备注:</td>
			    			<td><input class="easyui-textbox" id="remarks" name="remarks" value="${exterInter.remarks }" data-options="multiline:true" style="width:200px;height:60px;"/></td>
						</tr>
		    		</table>
			    <div style="text-align:center;padding:5px">
			    	<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();void(0)" id="cle" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
	    	</form>
	    </div>
	</div>
	
	<script>
	var id = $('#id').val();
		/**
		 * 表单提交
		 * @author  liujinliang
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
			function submit(flg){
			  	$('#editForm').form('submit',{
			  		url:"<%=basePath%>migrate/outInterfaceManager/saveInter.action",
			  		 onSubmit:function(){ 
			  			if (!$('#editForm').form('validate')) {
							$.messager.show({
								title : '提示信息',
								msg : '验证没有通过,不能提交表单!'
							});
							
							return false;
						}
			  			var begin=$.trim($('#authStime').val());
			  			if(begin==null||begin==''){
			  				$.messager.alert('提示','开始时间不能为空');
			  				return false;
			  			}
			  			var end=$.trim($('#authEtime').val());
			  			if(end==null||end==''){
			  				$.messager.alert('提示','结束时间不能为空');
			  				return false;
			  			}
			  			if(new Date(begin.replace(/-/,'/')).getTime()>new Date(end.replace(/-/,'/')).getTime()){
			  				$.messager.alert('提示','开始时间大于结束时间');
			  				return false;
			  			}
			  			var temp=$('#frequency').textbox('getValue');
			  			var tempArr=temp.split('/');
			  			if(tempArr.length!=2){
			  				$.messager.alert('提示','频次格式不符');
			  				return false;
			  			}
			  			var re = /^[0-9]+.?[0-9]*$/;
			  			if(!re.test(tempArr[0])||!re.test(tempArr[1])){
			  				$.messager.alert('提示','频次只能包含数字');
			  				return false;
			  			}
			  			return true;
					 },  
					 success : function(data) {
							data = JSON.parse(data);
							if (data.resCode == 'success') {
								$.messager.alert('提示',data.resMsg);
								closeLayout();
							}else if(data.resCode == 'error'){
								$.messager.alert('提示',data.resMsg);
							}
						},
					error:function(date){
						 $.messager.progress('close');
						 $.messager.alert('提示','保存失败');
					}
			  	});
			  	
	 	 	}
	 	 	/**
		 * 连续添加
		 * @author  liudelin
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
	 	 	function addContinue(){ 
			    $('#editForm').form('submit',{  
			        url:"<%=basePath%>migrate/outInterfaceManager/saveInter.action",  
			        onSubmit:function(){
			        	$.messager.progress('close');
			        	var codes=$("#codes").val();
						var codeOld=codes.split(",");
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
			
			$('#firmCode').combobox({ 
				url: '<%=basePath%>migrate/outInterfaceManager/findAllFired.action',   
				required: true,
				editable:false,
				valueField:'code',    
			    textField:'name',  
				missingMessage:'请选择所属厂商',
				width:'200',
			});
			//加载服务列表
			 $('#serveCode').combobox({ 
				url: '<%=basePath%>migrate/outInterfaceManager/findAllServer.action',   
				required: true,
				editable:false,
				valueField:'code',    
			    textField:'name',  
				missingMessage:'请选择所属栏目',
				width:'200',
			});
	</script>
	
	</body>
</html>