<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>参数管理编辑</title>
<%@ include file="/common/metas.jsp" %>
</head>
	<body style="margin: 0px;padding: 0px">
		<div id="panelEast" class="easyui-panel" data-options="title:'参数编辑',iconCls:'icon-form',border:false,fit:true">
			<div style="padding:5px;">
	    		<form id="editForm" method="post">
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
						<input type="hidden" id="parameterID" name="parameter.id" value="${para.id}">
		    			<tr>
			    			<td class="honry-lable">参数名称:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="parameterName" name="parameter.parameterName" value="${para.parameterName}" style="width: 420px" data-options="required:true" missingMessage="请输入参数名称"/></td>
			    		</tr>
						<tr>
							<td class="honry-lable">参数代码:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="parameterCode" name="parameter.parameterCode" value="${para.parameterCode }" style="width: 420px" data-options="required:true" missingMessage="请输入参数代码"/></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">参数类型:</td>
							<td class="honry-info"><input class="easyui-textbox" id="parameterType" name="parameter.parameterType" value="${para.parameterType }" style="width: 420px"  /></td>
						</tr>
		    			<tr id="isUpDown">
							<td class="honry-lable">是否有上下限:</td>
							<td class="honry-info" style="text-align: left;"><input type="checkBox" id="upDown"  onclick="javascript:onclickUpDown()" style="width: 30px;height: 15px"/></td>
						</tr>
						<tr id="paraValue">					
							<td class="honry-lable">参数值:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="parameterValue" name="parameter.parameterValue" value="${para.parameterValue }" style="width: 420px"  missingMessage="请输入参数值"/></td>
		    			</tr>
		    			<tr id="paraUp" style="display: none">					
							<td class="honry-lable">参数上限:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="parameterDownlimit" name="parameter.parameterDownlimit" value="${para.parameterDownlimit }" style="width: 420px"  missingMessage="请输入参数上限"/></td>
		    			</tr>
		    			<tr id="paraDown" style="display: none">
							<td class="honry-lable" >参数下限:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="parameterUplimit" name="parameter.parameterUplimit" value="${para.parameterUplimit }" style="width: 420px"  missingMessage="请输入参数下限"/></td>
						</tr>
		    			<tr>
							<td class="honry-lable" >参数单位:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="parameterUnit" name="parameter.parameterUnit" value="${para.parameterUnit }" style="width: 420px" /></td>
						</tr>
						
						<tr>					
							<td class="honry-lable">备注:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="parameterRemark" name="parameter.parameterRemark" value="${para.parameterRemark }" data-options="multiline:true" style="width: 420px;height:60px;"/></td>
		    			</tr>
						<tr id="menuId" >
							<td class="honry-lable">医院名称:</td>
					    	<td>
					    	   <table border="0">
								    <tr>
									    <td id="buttonListId">所有医院名称:<br>
										    <select multiple="multiple" id="selectAll" style="width:150px;height:160px;">
										        <c:if test="${null!=hospitalList && !hospitalList.isEmpty() }">
											        <c:forEach var="list" items="${hospitalList }">
											             <option value="${list.id }">${list.name }</option>
											        </c:forEach>
										        </c:if>
										    </select>
									    </td>
									    <td>
									    <input type="button" value="&gt;&nbsp;" id="add_this"><br>
									    <input type="button" value="&lt;&nbsp;" id="remove_this"><br>
									    <input type="button" value="&gt;&gt; " id="add_all"><br>
									    <input type="button" value="&lt;&lt; " id="remove_all"><br>
									    </td>
									    <td>要选择的医院:<br>
										    <select multiple="multiple" id="selectBut" name="hosid" style="width:150px;height:160px;">
										         <c:forEach var="list" items="${hoslocList }">
										             <c:if test="${list.id!=null }">
											             <option value="${list.id }">${list.name }</option>
											         </c:if>
											     </c:forEach>
										    </select>
									    </td>
								    </tr>
								</table>
				    		</td>
						</tr>
		    	</table>
			    <div style="text-align:center;padding:5px">
			    	<a href="javascript:submit(1);void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:submit(0);void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存并缓存</a>
			    	<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
	    	</form>
	    </div>
	</div>
	<script>	
	    //页面加载的时候判断是否有上下线
		 $(function(){
			 var id=$('#id').val();
			 var paraDown=$('#parameterDownlimit').val();
			 var paraUp=$('#parameterUplimit').val();
			 if (id!=null&&id!=""){
				 $('#isUpDown').hide();
			 } else {
				 $('#isUpDown').show();
			 }
			 if ((paraDown==null&&paraUp==null)||(paraDown==""&&paraUp=="")) {
			    $('#paraUp').hide();
				$('#paraDown').hide();
				$('#paraValue').show();
				$('#parameterValue').textbox({
					disabled : false,
					required : true
				})
				} else {
				$('#upDown').attr("checked", true);
				$('#paraUp').show();
				$('#paraDown').show();
				$('#paraValue').hide();
				$('#parameterDownlimit').textbox({
					disabled : false,
					required : true
				});
				$('#parameterUplimit').textbox({
					disabled : false,
					required : true
				}); 
				}
		 })
	   /**
		* @Description editForm表单提交
		* @author   
		* @Modifier tangfeishuai
		* @ModifyDate 2016-04-12
		* @ModifyRmk 
		* @CreateDate 2015-05-23
		* @version   1.0 
		*/  
		 function submit(iscache){
			
			$('#selectBut option').prop("selected",true);
		    $('#editForm').form('submit',{  
		        url:"<%=basePath%>sys/editParameters.action?iscache="+iscache,  
		        onSubmit:function(){ 
		        	var down = $('#parameterDownlimit').textbox('getValue');
	                var up = $('#parameterUplimit').textbox('getValue');
		        	if(down != null && down != '' && up != null && up != '' && up < down){
		        		$.messager.alert('操作提示','请检查参数上下限');
		        		close_alert();
		        		return false;
		        	}
		        	if (!$('#editForm').form('validate')) {
		        		$.messager.progress('close');
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						return false;
					}
		        	$.messager.progress({text:'保存中，请稍后...',modal:true});
		        },  
		        success:function(data){  
		        	$.messager.progress('close');
		        	$('#divLayout').layout('remove','east');
		             //实现刷新栏目中的数据
		             $("#list").datagrid("reload");
		        },
				error : function(data) {
					$.messager.progress('close');
					$.messager.alert('提示','保存失败！');	
				}			         
		    }); 
		} 
	    /**
	    *判断是否有上下限
	    *
	    */
		 function isUpDown(){
			 var id=$('#id').val();
			 var paraDown=$('#parameterDownlimit').val();
			 var paraUp=$('#parameterUplimit').val();
			 if (id!=null&&id!=""){
				 $('#isUpDown').hide();
			 } else {
				 $('#isUpDown').show();
			 }
			 if ((paraDown==null&&paraUp==null)||(paraDown==""&&paraUp=="")) {
			    $('#paraUp').hide();
				$('#paraDown').hide();
				$('#paraValue').show();
				$('#parameterValue').textbox({
					disabled : false,
					required : true
				})
				} else {
				$('#upDown').attr("checked", true);
				$('#paraUp').show();
				$('#paraDown').show();
				$('#paraValue').hide();
				$('#parameterDownlimit').textbox({
					disabled : false,
					required : true
				});
				$('#parameterUplimit').textbox({
					disabled : false,
					required : true
				}); 
				}
		 }
	   /**
		* @Description 清除页面填写信息
		* @author   lt
		* @Modifier tangfeishuai
		* @ModifyDate 2016-04-12
		* @ModifyRmk 
		* @CreateDate 2015-06-19
		* @version   1.0 
		*/
		function clear(){
            var id = $('#parameterID').val();
			$('#editForm').form('clear');
			if(id){
				$('#parameterID').val(id);
			}
			isUpDown()
		}
	   /**
		* @Description 关闭页面编辑窗口
		* @author   lt
		* @Modifier tangfeishuai
		* @ModifyDate 2016-04-12
		* @ModifyRmk 
		* @CreateDate 2015-06-19
		* @version   1.0 
		*/
		function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
		
	   /**
		* @Description 判断是否有上下线 单选按钮赋值
		* @author   tangfeishuai
		* @CreateDate 2015-06-19
		* @param 标签id
		* @version   1.0 
		*/
		function onclickUpDown(id) {
			if ($('#upDown').is(':checked')) {
				$('#paraUp').show();
				$('#paraDown').show();
				$('#paraValue').hide();
				 $('#parameterDownlimit').textbox({
					disabled : false,
					required : true
				});
				$('#parameterUplimit').textbox({
					disabled : false,
					required : true
				}); 
				$('#parameterValue').textbox({
					disabled : false,
					required : false
				})
			} else {
				$('#paraUp').hide();
				$('#paraDown').hide();
				$('#paraValue').show();
				$('#parameterValue').textbox({
					disabled : false,
					required : true
				});
				 $('#parameterDownlimit').textbox({
					disabled : false,
					required : false
					});
				$('#parameterUplimit').textbox({
					disabled : false,
					required : false
				}); 
			}
		}
	 //移到右边
    $('#add_this').click(function() {
    //获取选中的选项，删除并追加给对方
        $('#selectAll option:selected').appendTo('#selectBut');
    });
    //移到左边
    $('#remove_this').click(function() {
        $('#selectBut option:selected').appendTo('#selectAll');
    });
    //全部移到右边
    $('#add_all').click(function() {
        //获取全部的选项,删除并追加给对方
        if(!$('#selectAll').prop("disabled")){
        	 $('#selectAll option').appendTo('#selectBut');
        }
    });
    //全部移到左边
    $('#remove_all').click(function() {
        $('#selectBut option').each(function(){
        	if(!$(this).prop("disabled")){
        		$(this).appendTo('#selectAll');
        	}
        });
    });
    //双击选项
    $('#selectAll').dblclick(function(){ //绑定双击事件
        //获取全部的选项,删除并追加给对方
        $("option:selected",this).appendTo('#selectBut'); //追加给对方
    });
    //双击选项
    $('#selectBut').dblclick(function(){
       $("option:selected",this).appendTo('#selectAll');
    });	
    
    
	</script>
	</body>
</html>