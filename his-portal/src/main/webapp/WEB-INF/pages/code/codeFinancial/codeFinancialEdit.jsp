<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <jsp:include page="/javascript/default.jsp"></jsp:include>
  <body>
  <div align="center" id="panelEast" class="easyui-panel" data-options="title:'财务分类编辑',iconCls:'icon-form'" >
  	<div style="padding: 10px">
  		<form id="editForm" method="post">
  			<input type="hidden" id="id" name="id" value="${financial.id }"/>
	  		<input type="hidden" id="createUser" name="createUser" value="${financial.createUser }">
	  		<input type="hidden" id="createDept" name="createDept" value="${financial.createDept }">
	  		<input type="hidden" id="createTime" name="createTime" value="${financial.createTime }"/>
	  		<input type="hidden" id="updateUser" name="updateUser" value="${financial.updateUser }"/>
	  		<input type="hidden" id="updateTime" name="updateTime" value="${financial.updateTime }"/>
	  		<input type="hidden" id="deleteUser" name="deleteUser" value="${financial.deleteUser }"/>
	  		<input type="hidden" id="del_flg" name="del_flg" value="${financial.del_flg }"/>
	  		
	  		<table title="编辑" class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
	   			<tr>
	   				<td class="honry-lable">代码：</td>
	  				<td class="honry-info"><input class="easyui-validatebox" type="text" id="encode" name="financial.encode" value="${financial.encode }" data-options="required:true" style="width: 350px"  missingMessage="请输入代码"/></td>
	   			</tr>
	  			<tr>
	  				<td class="honry-lable">名称：</td>
	  				<td class="honry-info">
						<input class="easyui-validatebox" type="text" id="name" name="financial.name" value="${financial.name }" data-options="required:true" style="width: 95%"  missingMessage="请输入名称"/>
					</td>
	  			</tr>
	  			<c:if test="${not empty financial.id }">
	  			<tr>
	  				<td class="honry-lable">拼音码：</td>
	  				<td class="honry-info">${financial.pinyin}&nbsp;&nbsp;</td>
	  			</tr>
	  			<tr>
	  				<td class="honry-lable">五笔码：</td>
	  				<td class="honry-info">${financial.wb }&nbsp;&nbsp;</td>
	  			</tr>
	  			</c:if>
	  			<tr>
	  				<td class="honry-lable">自定义码：</td>
	  				<td class="honry-info"><input class="easyui-textbox" type="text" id="inputCode" name="financial.inputCode" value="${financial.inputCode }"  style="width: 95%"/></td>
	  			</tr>
	  			<tr>
	  				<td class="honry-lable">排序：</td>
	  				<td class="honry-info"><input class="easyui-numberspinner" type="text" id="order" name="financial.order" value="${financial.order }" data-options="required:true" style="width: 95%"  missingMessage="请输入排序"/></td>
	  			</tr>
	  			<tr>
	  				<td class="honry-lable">适用医院：</td>
	  				<td class="honry-info"><input class="easyui-textbox" type="text" id="hospital" name="financial.hospital" value="${financial.hospital }"/></td>
	  			</tr>
	  			<tr>
	  				<td class="honry-lable">不适用医院：</td>
	  				<td class="honry-info"><input class="easyui-textbox" type="text" id="nonhospital" name="financial.nonhospital" value="${financial.nonhospital }"/></td>
	  			</tr>
	  			<tr>
	  				<td class="honry-lable">说明：</td>
	  				<td class="honry-info"><input class="easyui-textbox" id="description" name="financial.description" value="${financial.description }" data-options="multiline:true" style="width:95%;height:60px;"/></td>
	  			</tr>
	  			<tr>
	  				<td colspan="2">可选标志:
		    			<input type="hidden" id="canselectHidden" name="financial.canselect" value="${financial.canselect}"/>
		    			<input type="checkBox" id="canselect" onclick="javascript:onclickBox('canselect')"/>
					&nbsp;&nbsp;默认标志:
		    			<input type="hidden" id="isdefaultHidden" name="financial.isdefault" value="${financial.isdefault}"/>
		    			<input type="checkBox" id="isdefault" onclick="javascript:onclickBox('isdefault')"/>				    			
					&nbsp;&nbsp;停用标志:
		    			<input type="hidden" id="stopFlgHidden" name="financial.stop_flg" value="${financial.stop_flg}"/>
		    			<input type="checkBox" id="stopFlg" onclick="javascript:onclickBox('stopFlg')"/>
	    			</td>
	    		</tr>
	  		</table>
	  		<div style="text-align:center;padding:5px">
	  		  <c:if test="${empty financial.id}">
	  		      <a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
	  		  </c:if>
	  			
				<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
				<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
				<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			</div>
	  	</form>
  	</div>
  </div>
 <script type="text/javascript">
 	 $(function(){
 	 		if($('#stopFlgHidden').val()==1){
				$('#stopFlg').attr("checked", true); 
			}
 	 		if($('#canselectHidden').val()==1){
				$('#canselect').attr("checked", true); 
			}
			if($('#isdefaultHidden').val()==1){
				$('#isdefault').attr("checked", true); 
			}
			
		});
 
	  	//表单提交submit信息
	  	function submit(flg){
	  	$('#editForm').form('submit',{
	  		url:'saveOrUpdateFinancial.action',
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
					$.messager.alert('提示',"保存成功");
			   		$('#divLayout').layout('remove','east');
				    //实现刷新
		          	$("#list").datagrid("reload");
			   }else if(flg==1){
					//清除editForm
					$('#editForm').form('reset');
			  	}
			 },
			error:function(data){
				$.messager.alert('提示',"保存失败");
			}
	  	});
	  	}
  	
	  	 //清除信息
	  	function clear(){
		  	$('#editForm').form('reset');
	  	}
	  	//关闭
	  	function closeLayout(){
			$('#divLayout').layout('remove','east');
			$("#list").datagrid("reload");
		}
		/*
		*param:id 复选框id
		*
		*
		*/
		function onclickBox(id){
		  if($('#'+id).is(':checked')){
				$('#'+id+'Hidden').val(1);
			 }else{
				$('#'+id+'Hidden').val(0);
			}
		}
  </script>
</body>
</html>
