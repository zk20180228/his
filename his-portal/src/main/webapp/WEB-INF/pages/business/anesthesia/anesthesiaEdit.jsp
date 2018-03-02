<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:include page="/javascript/default.jsp"></jsp:include>
	<body>
		<div class="easyui-panel" id = "panelEast" data-options="title:'安排麻醉编辑',iconCls:'icon-form'" style="width:580px">
			<div style="padding:10px">
	    		<form id="editForm" method="post">
					<input type="hidden" id="id" name="id" value="${operationapply.id }">
					<input type="hidden" id="aneDoctors" name="aneDoctor">
					<input type="hidden" id="anesTypes" name="anesType">
					<input type="hidden" id="aneWays" name="aneWay">
					<input type="hidden" id="aneNotes" name="aneNote">
					<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
						<tr>
							<td class="honry-lable">麻醉施术者:</td>
			    			<td><input class="easyui-combobox" id="aneDoctor"   data-options="valueField:'id',textField:'name',url:'inpatient/prepayin/queryPrepayinPredoct.action'" style="width:200px" value="${operationapply.aneDoctor }"  /></td>
		    			</tr>
		    			<tr>
			    			 <td class="honry-lable">麻醉类型:</td>
			    			<td>
			    				<select id="anesType"   style="width:200px" value="${operationapply.anesType }">
			    					<option></option>   
			    					<option value="1">局麻</option>   
    								<option value="2">全麻</option>
			    				</select>
			    			</td>
			    		</tr>
						<tr>
							<td class="honry-lable">麻醉方式:</td>
			    			<td><input class="easyui-combobox" id="aneWay"  data-options="valueField:'id',textField:'name',url:'likeAneway.action'"  style="width:200px" value="${operationapply.aneWay }"  /></td>
		    			</tr>
		    			<tr>
			    			<td class="honry-lable">麻醉注意事项:</td>
			    			<td><input class="easyui-textbox" id="aneNote"  value="${operationapply.aneNote }"  style="width:200px;height:60px;" /></td>					
						</tr>
		    	</table>
			    <div style="text-align:center;padding:5px">
			    	<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
	    	</form>
	    </div>
	</div>
	<script>
		/**
		 * 表单提交
		 * @author  liujinliang
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
			function submit(){ 
			var  aneDoctor = $('#aneDoctor').combobox('getValue');
			$('#aneDoctors').val(aneDoctor);
			var  anesType = $('#anesType').val();
			$('#anesTypes').val(anesType);
			var  aneWay = $('#aneWay').combobox('getValue');
			$('#aneWays').val(aneWay);
			var  aneNote = $('#aneNote').textbox('getValue');
			$('#aneNotes').val(aneNote);
		    $('#editForm').form('submit',{  
		        url:'business/saveAnesthesia.action',  
		        onSubmit:function(){
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
		        success:function(){  
		        	$.messager.progress('close');
		        	$.messager.alert("提示",'保存成功');
		             //实现刷新栏目中的数据
                    $('#divLayout').layout('remove','east');
				    $("#list").datagrid("reload");
		        },
				error : function(data) {
					$.messager.progress('close');
					$.messager.alert("提示",'保存失败！');	
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
				$('#editForm').form('clear');
			}
			function closeLayout(){
				$('#divLayout').layout('remove','east');
				$("#list").datagrid("reload");
			}
		
	</script>
	</body>
</html>