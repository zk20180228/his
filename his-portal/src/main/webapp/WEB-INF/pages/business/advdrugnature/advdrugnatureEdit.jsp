<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
 <%@ include file="/common/metas.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <jsp:include page="/javascript/default.jsp"></jsp:include>
 <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

  <body>
  <div align="center" id="panelEast" class="easyui-panel" data-options="title:'长期医嘱限制性药品性质编辑',iconCls:'icon-form',fit: true" >
  	<div style="border: 'false'">
  		<form id="editForm" method="post">
  			<input type="hidden" id="id" name="id" value="${advdrugnature.id }"/>
	  		<input type="hidden" id="name" name="advdrugnature.name" value="${advdrugnature.name }"/>
	  		<input type="hidden" id="inputCode" name="advdrugnature.inputCode" value="${advdrugnature.inputCode }"/>
	  		
	  		<table title="编辑" class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-top:10px">
	   			<tr>
	  				<td class="honry-lable">代码：</td>
	  				<td class="honry-info">
						<input id="encodet" type="text" style="border:none" readonly="readonly"  value="${advdrugnature.encode}"   style="width:300px" ></input>
					</td>
	  			</tr> 
	  			 <tr>
	  				<td class="honry-lable">名称：</td>
	  				<td class="honry-info">
						<input id="encode" class="easyui-combobox"  value="${advdrugnature.encode}" name="advdrugnature.encode"  style="width: 300px" data-options="valueField:'encode',textField:'name',url:'<c:url value='/inpatient/advdrugnatrue/queryCodeDrugproperties.action'/>',required:true" missingMessage="请选择名称"></input>
					</td>
	  			</tr> 
	  			 
	  			<c:if test="${not empty advdrugnature.id }">
	  			<tr>
	  				<td class="honry-lable">拼音码：</td>
	  				<td class="honry-info">${advdrugnature.pinyin}&nbsp;&nbsp;</td>
	  			</tr>
	  			<tr>
	  				<td class="honry-lable">五笔码：</td>
	  				<td class="honry-info">${advdrugnature.wb }&nbsp;&nbsp;</td>
	  			</tr>
	  			</c:if>
	  			<tr>
	  				<td class="honry-lable">排序：</td>
	  				<td class="honry-info"><input class="easyui-numberspinner" type="text" id="order" name="advdrugnature.order" value="${advdrugnature.order }" data-options="required:true" style="width: 300px"  missingMessage="请输入排序"/></td>
	  			</tr>
	  			<tr>
	  				<td class="honry-lable">适用医院：</td>
	  				<td class="honry-info">
	  					<input id="hospital"  value="${advdrugnature.hospital}" name="advdrugnature.hospital"  style="width: 300px"  missingMessage="请选择名称"></input>
	  					<a href="javascript:delSelectedData('hospital');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
	  				</td>
	  			</tr>
	  			<tr>
	  				<td class="honry-lable">说明：</td>
	  				<td class="honry-info"><input class="easyui-textbox" id="description" name="advdrugnature.description" value="${advdrugnature.description }" data-options="multiline:true" style="width: 300px;height:60px;"/></td>
	  			</tr>
	  			<tr>
	  				<td colspan="2">可选标志:
		    			<input type="hidden" id="canselectHidden" name="advdrugnature.canselect" value="${advdrugnature.canselect}"/>
		    			<input type="checkBox" id="canselect" onclick="javascript:onclickBox('canselect')"/>
					&nbsp;&nbsp;默认标志:
		    			<input type="hidden" id="isdefaultHidden" name="advdrugnature.isdefault" value="${advdrugnature.isdefault}"/>
		    			<input type="checkBox" id="isdefault" onclick="javascript:onclickBox('isdefault')"/>				    			
					&nbsp;&nbsp;停用标志:
		    			<input type="hidden" id="stopFlgHidden" name="advdrugnature.stop_flg" value="${advdrugnature.stop_flg}"/>
		    			<input type="checkBox" id="stopFlg" onclick="javascript:onclickBox('stopFlg')"/>
	    			</td>
	    		</tr>
	  		</table>
	  		<div style="text-align:center;padding:5px">
	  		  <c:if test="${empty advdrugnature.id}">
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
	 $('#hospital').combobox({  
		 valueField:'code',
		 textField:'name',
		 url:'<c:url value='/inpatient/advdrugnatrue/queryHospital.action'/>',
		 required:true
	 });
	
 	 $(function(){
 		//下拉框onSelect事件，取得对象name赋值给隐藏域
		$('#encode').combobox({  
		    required:true,    
	        multiple:false,
	        editable:false,
	        onSelect:function(node) {
	        	$('#encodet').val(node.encode);
		    	$('#name').val(node.name);
		    	$('#inputCode').val(node.inputCode);
            }
	    });
		//绑定医院弹框事件
	     bindEnterEvent('hospital',popWinToCodeHospital,'easyui');
 		//复选框回显
 		if ($('#stopFlgHidden').val()==1){
			$('#stopFlg').attr("checked", true); 
		}
	 	if ($('#canselectHidden').val()==1){
			$('#canselect').attr("checked", true); 
		}
		if ($('#isdefaultHidden').val()==1){
			$('#isdefault').attr("checked", true); 
		}
		
	});
 
	/**
	* @Description 选择医院弹框跳转
	* @author   tangfeishuai
	* @CreateDate 2016-04-13
	* @version   1.0 
	*/
 	function popWinToCodeHospital(){
		var tempWinPath = "<%=basePath%>inpatient/advdrugnatrue/toCodePopWinHospital.action?textId=hospital";
		var aaa=window.open (tempWinPath,'newwindow',' left=100,top=50,width='+ (screen.availWidth -200) +',height='+ (screen.availHeight-110) +',scrollbars,resizable=yes,toolbar=no')
	}
 	 
 	   /**
		* @Description 表单editForm提交
		* @author   
		* @Modifier tangfeishuai
		* @ModifyDate 2016-04-12
		* @ModifyRmk 
		* @CreateDate 2015-05-23
		* @version   1.0 
		*/
	  	function submit(flg){
	  	$('#editForm').form('submit',{
	  		url:"<c:url value='/inpatient/advdrugnatrue/saveOrUpdateAdvdrugnature.action'/>",
	  		 onSubmit:function(){ 
	  			
	  		 	if (!$('#editForm').form('validate')){
	  		 		$.messager.progress('close');
					$.messager.show({  
					     title:'提示信息' ,   
					     msg:'验证没有通过,不能提交表单!'  
					}); 
					   return false ;
			     }
	  		 	$.messager.progress({text:'保存中，请稍后...',modal:true});
			 },  
			success:function(data){
				$.messager.progress('close');
				var map = eval("("+data+")");
			  	if(map.success=="success"){
			  		$.messager.alert("操作提示","保存成功！");
		          	$("#list").datagrid("reload");
			  		setTimeout(function(){
						$(".messager-body").window('close');
				 	},3500);
			  		if(flg==0){
			  			$('#divLayout').layout('remove','east');
			  		}else{
			  			AddOrShowEast('EditForm', 'addCodeAdvdrugnature.action');
			  		}
				    //实现刷新
			  	}else if(map.warn=="warn"){
			  		$.messager.alert("操作提示","该医院已维护此长期药品！");
			  		setTimeout(function(){
						$(".messager-body").window('close');
				 	},3500);
			  	} 
			 },
			error:function(data){
				$.messager.progress('close');
				$.messager.alert("操作提示","保存失败");
			}
	  	});
	  	}
  	
	   /**
		* @Description 清除页面信息
		* @author   
		* @Modifier tangfeishuai
		* @ModifyDate 2016-04-12
		* @ModifyRmk 
		* @CreateDate 2015-05-23
		* @version   1.0 
		*/
	  	function clear(){
		  	$('#editForm').form('reset');
	  	}
	  	/**
		* @Description 关闭该页面
		* @author   
		* @Modifier tangfeishuai
		* @ModifyDate 2016-04-12
		* @ModifyRmk 
		* @CreateDate 2015-05-23
		* @version   1.0 
		*/
	  	function closeLayout(){
			$('#divLayout').layout('remove','east');
			$("#list").datagrid("reload");
		}
	
		/**
		* @Description 可选标志的渲染
		* @author   
		* @Modifier tangfeishuai
		* @ModifyDate 2016-04-12
		* @ModifyRmk 
		* @CreateDate 2015-05-23
		* @version   1.0 
		* @param id 复选框id
		*/
		function onclickBox(id){
		    if($('#'+id).is(':checked')){
				$('#'+id+'Hidden').val(1);
			 }else{
				$('#'+id+'Hidden').val(0);
			}
		}
  </script>
  <script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>
