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
</head>
	<body>
		<div class="easyui-panel" id="panelEast" data-options="fit:true">
			<div style="padding:10px">
	    		<form id="editForm" method="post">
					<input type="hidden" id="id" name="id" value="${matAddrate.id }">
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td class="honry-lable">加价规则：</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="addRate" name="addRate" value="${matAddrate.addRate }" data-options="required:true" missingMessage="请输入加价规则" style="width: 200"/></td>
		    			</tr>
		    			<tr>
			    			 <td class="honry-lable">物品科目：</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="kindCode" name="kindCode" value="${matAddrate.kindCode }" data-options="required:true" missingMessage="请输入物品科目" style="width: 200"/></td>
			    		</tr>
						<tr>
							<td class="honry-lable">规格：</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="specs" name="specs" value="${matAddrate.specs }" data-options="required:true" missingMessage="请输入规格" style="width: 200"/></td>
		    			</tr>
		    			<tr>
			    			<td class="honry-lable">起始价格：</td>
			    			<td class="honry-info"><input class="easyui-numberbox" id="lowPrice" name="lowPrice" value="${matAddrate.lowPrice }" data-options="required:true,min:0,max:9999999999,precision:0" missingMessage="请输入起始价格" style="width: 200"/></td>					
						</tr>
						<tr>
							<td class="honry-lable">终止价格：</td>
			    			<td class="honry-info"><input class="easyui-numberbox" id="highPrice" name="highPrice" value="${matAddrate.highPrice }" data-options="required:true,min:0,max:9999999999,precision:0" missingMessage="请输入终止价格" style="width: 200"/></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">加价率：</td>
							<td class="honry-info"><input class="easyui-numberbox" id="rate" name="rate" value="${matAddrate.rate }" data-options="required:true,min:0,max:999,precision:0" missingMessage="请输入加价率" style="width: 200"/></td>
						</tr>
						<tr>					
							<td class="honry-lable">附加费：</td>
			    			<td class="honry-info"><input class="easyui-numberbox" id="addFee" name="addFee" value="${matAddrate.addFee }" data-options="required:true,min:0,max:999999,precision:0" missingMessage="请输入附加费" style="width: 200"/></td>
		    			</tr>
						<tr>
							<td class="honry-lable">停用标志：</td>					
							<td align="left">
				    			<input type="hidden" id="stopFlgsHidden" name="stop_flg" value="${matAddrate.stop_flg }"/>
				    			<input type="checkBox" id="stopFlgs" onclick="javascript:checkBoxSelect('stopFlgs')"/>
			    			</td>
		    			</tr>
		    	</table>
			    <div style="text-align:center;padding:5px">
			     <c:if test="${matAddrate.id==null }">
			    	<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
			    </c:if>
			    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:reset();" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>
			    	<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
	    	</form>
	    </div>
	</div>
	<script type="text/javascript">
		//页面加载
		$(function(){
			$('#kindCode').combotree({    
			    url:"<c:url value='/material/addrate/findMatKindInfoTree.action'/>",    
			    editable:false,
			    onSelect:function(record){
			    	if(record.id=="0"){
			    		$('#kindCode').combotree('clear');
			    		$.messager.alert('提示','物品科目不能是物资分类信息的根节点');	
			    		return;
			    	}
			    }
			}); 
			//复选框赋值
			if($('#stopFlgsHidden').val()==1){
				$('#stopFlgs').attr("checked", true); 
			}
		});	
		//表单提交
		function submit(){ 
			$('#editForm').form('submit',{  
		    	url: "<c:url value='/material/addrate/saveAddrate.action'/>",
		        onSubmit:function(){
					if (!$('#editForm').form('validate')) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						return false;
					}
		        },  
		        success:function(data){  
		        	var dataMap = eval("("+data+")");
		        	$.messager.alert('提示',dataMap.resCode);
		        	if(dataMap.resMsg=="success"){
		        		reload();
		        		closeLayout();
		        	}
		        },
				error : function(data) {
					$.messager.alert('提示','保存失败!');	
				}							         
		    }); 
	    }	    
		//连续添加
		function addContinue(){ 
			$('#editForm').form('submit',{  
		    	url: "<c:url value='/material/addrate/saveAddrate.action'/>",
		        onSubmit:function(){
					if (!$('#editForm').form('validate')) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						return false;
					}
		        },  
		        success:function(data){  
		        	var dataMap = eval("("+data+")");
		        	$.messager.alert('提示',dataMap.resCode);
		        	if(dataMap.resMsg=="success"){
		        		reload();
		        		clear();
		        	}
		        },
				error : function(data) {
					$.messager.alert('提示','保存失败!');	
				}							         
		    }); 
	    }	    
		//清除
		function clear(){
			$('#editForm').form('clear');
		}
		//重置
		function reset(){
			$('#editForm').form('reset');
		}
		//关闭编辑窗口
		function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
		//复选框事件
		function checkBoxSelect(id){
			if($('#'+id).is(':checked')){
				$('#'+id+'Hidden').val(1);
			}else{
				$('#'+id+'Hidden').val(0);
			}
		}
	</script>
	</body>
</html>