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
</head>
<body>
	<div class="easyui-panel" id="panelEast" data-options="title:'收费编辑',iconCls:'icon-form',border:false" style="width:100%;padding:0px;margin: 0px;">
			<div >
			<form id="editForm" method="post">
				<input type="hidden" id="unitIds" name="treeId" value="${treeCode }"/>  <!--传过来合同id  -->
				<input type="hidden" id="id" name="id" value="${registerFee.id }">
				<table class="honry-table" cellpadding="0" cellspacing="4" border="0" style="width:390px;margin: 0px;padding: 0px;border-left:0;border-top:0">
					<tr>
						<td class="honry-lable" style="border-left:0;border-top:0">
							<span style="font-size: 14">挂号级别:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;border-top:0">
								<input id="registerGrade" name="registerGrade"  value="${registerFee.registerGrade }"   missingMessage="请选择挂号级别"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="border-left:0">
							<span style="font-size: 14">挂号费:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-numberbox" id="registerFee" name="registerFee" value="${registerFee.registerFee}" data-options="min:0,precision:2,required:true"  />
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="border-left:0">
							<span style="font-size: 14">检查费:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input   class="easyui-numberbox" id="checkFee" name="checkFee" value="${registerFee.checkFee}" data-options="min:0,precision:2,required:true" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="border-left:0">
							<span style="font-size: 14">治疗费:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-numberbox"  id="treatmentFee" name="treatmentFee" value="${registerFee.treatmentFee}" data-options="min:0,precision:2,required:true" />
						</td>
					</tr>
				    <tr>	
						<td class="honry-lable" style="border-left:0">
							<span style="font-size: 14">其他费:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-numberbox" id="otherFee" name="otherFee" value="${registerFee.otherFee}" data-options="min:0,precision:2,required:true"/>

						</td>
					</tr>	
					<tr>	
						<td class="honry-lable" style="border-left:0">
							<span style="font-size: 14">排序:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-numberspinner" id="order" name="order" value="${registerFee.order}" data-options="min:1"/>
						</td>
					</tr>						
					<tr>
				  	    <td class="honry-lable" style="border-left:0">
							<span style="font-size: 14">说明:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<textarea  rows="3" cols="30" id="description" name="description">${registerFee.description }</textarea>
						</td>
					</tr>
				</table>
				 <div style="text-align:center;padding:5px">
			    	<c:if test="${empty registerFee.id}">			    	
			    	<a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
			    	</c:if>
			    	<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear(0);" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:closeLayout()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
	    	</form>
	    </div>
	</div>
<script type="text/javascript">
	//页面加载
	$(function(){
		//加载挂号级别
		$('#registerGrade').combobox({   
			valueField:'code',
			textField:'name',
			url: "<c:url value='/finance/registerFee/gradeFeeCombobox.action'/>",    
			required: true,
			onSelect:function(record){
				var unitId=$('#unitIds').val();
				var gradeId = record.code;
				$.ajax({
					url:"<c:url value='/finance/registerFee/queryFeeValidate.action'/>?unid="+unitId+"&gradeId="+gradeId,
					type:'post',
					success: function(data) {
						if(data=="false"){
							$('#registerGrade').combobox('setValue',clear);
							$.messager.alert('提示','该级别已存在，请重新选择级别！');
						}
					}
				});	
			}
		});
	}); 
	
	/**
	* 绑定挂号级别回车事件
	* @author  zhuxiaolu
	* @date 2016-03-22 14:30   
	* @version 1.0
	*/
	bindEnterEvent('registerGrade',popWinToGrade,'easyui');//绑定回车事件
	//表单提交submit信息
	function submit(flg){
		var treeCode=$('#unitIds').val();
	  	$('#editForm').form('submit',{
	  		url:"<%=basePath%>finance/registerFee/saveOrUpdateRegisterFee.action?treeCode="+treeCode,
	  		 onSubmit:function(){ 
	  		 	if(!$('#editForm').form('validate')){
					$.messager.show({  
					     title:'提示信息' ,   
					     msg:'验证没有通过,不能提交表单!'  
					}); 
					   return false ;
			     }
			 },  
			success:function(data){
				if(flg==0){
					$.messager.alert("提示","保存成功");
			   		$('#divLayout').layout('remove','east');
				    //实现刷新
		          	$("#list").datagrid("reload");
				    $("#tDt").tree("reload");
			   }else if(flg==1){
					//清除editForm
					$('#editForm').form('reset');
					$("#list").datagrid("reload");
			  	}
			 },
			error:function(data){
				$.messager.alert("提示","保存失败");
			}
	  	});
	 }
	//关闭
  	function closeLayout(){
		$('#divLayout').layout('remove','east');
		$("#list").datagrid("reload");
	}
	// 清除页面填写信息
	function clear(){
		$('#editForm').form('reset');
	}
	
	/**
	* 回车弹出挂号级别选择窗口
	* @author  zhuxiaolu
	* @param textId 页面上commbox的的id
	* @date 2016-03-22 14:30   
	* @version 1.0
	*/
	function popWinToGrade(){
		popWinGradeCallBackFn = function(node){
			
	    	$('#registerGrade').combobox('setValue',node.id);
	    
		};
		var tempWinPath = "<%=basePath%>popWin/popWinGrade/popWinRegisterGradeBedList.action?textId=registerGrade";
		window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -700) +',height='
		+ (screen.availHeight-500) +',scrollbars,resizable=yes,toolbar=yes')
		
		   		
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>