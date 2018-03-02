<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body>
	<div class="easyui-layout" data-options="fit:true,border:false" style="padding:0px">
		<div   style="padding:5px" data-options="region:'center',border:false">
		<form id="editForm" method="post">
			<input type="hidden" id="id" name="id" value="${sysCodeType.id }">
			<table class="honry-table" border="1px solid black" style="width:100%">
				 <tr>
		    			<td class="honry-lable">代码:</td><!-- 代码新添加完以后不允许修改 -->
		    			<td class="honry-info">
		    		    <c:choose>
		    		       <c:when test="${empty sysCodeType.id}">
		    		           <input class="easyui-validatebox" type="text" id="code" name="sysCodeType.code" value="${sysCodeType.code}" data-options="required:true" style="width:290px" missingMessage="请输入代码"/>
		    		       </c:when>
		    		       <c:otherwise>
		    		           <input type="hidden" name="sysCodeType.code" value="${sysCodeType.code}"> 
		    			       ${sysCodeType.code}
		    		       </c:otherwise>
		    		    </c:choose>
		    		    </td>
		    	</tr>
		    	 <tr>
		    				<td class="honry-lable">名称:</td>
		    				<td class="honry-info">
		    			    <c:choose>
		    		       <c:when test="${empty sysCodeType.id}">
		    		          <input class="easyui-validatebox" type="text" id="name" name="sysCodeType.name" value="${sysCodeType.name}" data-options="required:true"  style="width:95%" missingMessage="请输入名称"/>
		    		       </c:when>
		    		       <c:otherwise>
		    		           <input type="hidden" name="sysCodeType.name" value="${sysCodeType.name}"> 
		    			       ${sysCodeType.name}
		    		       </c:otherwise>
		    		      </c:choose>
		    		       </td>
		    		</tr>
		    		<tr>
		    		    <td class="honry-lable">允许层级:</td><!-- 允许层级新添加完以后不允许修改 -->
		    			<td class="honry-info">
		    			  <input  type="hidden" id="type" name="sysCodeType.type" value="2"/><!-- 类型 -->
		    			  <c:choose>
		    		       <c:when test="${empty sysCodeType.id}">
		    		          <input class="easyui-numberspinner" type="text" id="level" name="sysCodeType.level" value="${sysCodeType.level }" data-options="required:true" style="width:95%;height:18px;" missingMessage="请输入允许层级"/>
		    		       </c:when>
		    		       <c:otherwise>
		    		           <input type="hidden"  name="sysCodeType.level" value="${sysCodeType.level }"> 
		    			       ${sysCodeType.level}
		    		       </c:otherwise>
		    		    </c:choose>
		    		    </td>
		    		</tr>
					<tr>
					    <td class="honry-lable">适用医院:</td>
		    			<td class="honry-info"><input class="easyui-textbox" type="text" id="hospital" name="sysCodeType.hospital" value="${sysCodeType.hospital}"   style="width:95%" missingMessage="请输入适用医院"/></td>
		    		 </tr>
		    		 <tr>
		    		    <td class="honry-lable">排序:</td>
		    			<td class="honry-info"><input class="easyui-numberspinner" type="text" id="order" name="sysCodeType.order" value="${sysCodeType.order }" data-options="required:true" style="width:95%;height:18px;" missingMessage="请输入排序"/></td>
		    		</tr>
		    		   <tr>
						<td class="honry-lable">说明:</td>
		    			<td  class="honry-info"><input class="easyui-textbox" type="text" id="description" name="sysCodeType.description" value="${sysCodeType.description }" data-options="multiline:true" style="width:95%;height:60px;" missingMessage="请输入说明"/></td>
					</tr>
		    		
					<tr>
						<td class="honry-lable">停用标志:</td>
			    		<td>
				    		<input type="hidden" id="stopFlgHidden" name="sysCodeType.stop_flg" value="${sysCodeType.stop_flg}"/>
		    				<input type="checkBox" id="stopFlg" onclick="javascript:onclickBox('stopFlg')"/>
			    		</td>
					</tr>
			</table>
			 <div style="text-align:center;padding:5px">
			    	<c:if test="${empty sysCodeType.id}">
	  		         <a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
	  		        </c:if>
	  			   <a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeWin()">关闭</a>
			    </div>	
		</form>	
		</div>
		</div>
		<script type="text/javascript">
	   $(function(){
 	 		if($('#stopFlgHidden').val()==1){
				$('#stopFlg').attr("checked", true); 
			}
	    });
		/**
		 * 页面加载
		 * @author  hedong
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
		$(function(){
			if($('#id').val()==''){//判断是否编辑状态，编辑状态修改复选框选中状态
				var stop_flg=$('#frequency.stop_flg').val(); //删除标志
				if(stop_flg==1){
					document.getElementById('stop_flg').checked=true;
				}
			}
		});	
		//表单提交submit信息
	  	function submit(flg){
	  	$('#editForm').form('submit',{
	  		url:'saveCodeType.action',
	  		 onSubmit:function(){ 
	  		     
			     return $(this).form('validate');  
			 },  
			success:function(data){
			  
				if(flg==0){
					$.messager.alert('提示','保存成功');
			   	    //关闭弹出窗口
			   	    parent.win.dialog('close');
			   	   /* refresh();
				    $("#list").datagrid("reload");*/
			   }else if(flg==1){
				   $.messager.alert('提示','保存成功');
			        //清除editForm
					$('#editForm').form('reset');
					/*refresh();
				    $("#list").datagrid("reload");*/
			  	}
			 },
			error:function(date){
				$.messager.alert('提示','保存失败');
			}
	  	});
	  	}
	    
		/**
		 * 清除页面填写信息
		 * @author  hedong
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
		function clear(){
			$('#editForm').form('reset');
		}
	    /**
		 * 关闭弹出窗口
		 * @author  hedong
		 * @date 2015-6-3
		 * @version 1.0
		 */
		function closeWin(){
			parent.win.dialog('close');
		}
	
	   /*
		*param:id 复选框id
		*author:hedong
		*date:2015-06-24
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