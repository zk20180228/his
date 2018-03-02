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
<body style="margin: 0px; padding: 0px">
	<div class="easyui-panel" id = "panelEast" data-options="title:'编辑',iconCls:'icon-form',fit:true" >
		<div style="padding:5px">
			<form id="editForm" method="post">
			
			  <input type="hidden" id="id" name="adviceInfo.id" value="${adviceInfo.id }">
			 
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%">
					<tr>
						<td class="honry-lable">医嘱类型：</td>
		    			<td class="honry-info"><input  id="itemId" name="adviceInfo.typeId" value="${typeId}" style="width:200px"/>
		    			                      
		    			</td>
	    			</tr>	    			
	    			<tr>
						<td class="honry-lable">系统类别：</td>
		    			<td class="honry-info">
		    				<table border="0" id="list" cellspacing="0" class="adviceInfoEdit">
								    <tr>
									    <td id="buttonListId">所有类别名称:<br>
										    <select multiple="multiple" id="selectAll" style="width:130px;height:160px;">
										        <c:if test="${null!=codeList && !codeList.isEmpty() }">
											        <c:forEach var="list" items="${codeList }">
											             <option value="${list.encode }">${list.name }</option>
											        </c:forEach>
										        </c:if>
										    </select>
									    </td>
									    <td>
									    <input type="button" value="&nbsp&gt;&nbsp;" id="add_this"><br>
									    <input type="button" value="&nbsp&lt;&nbsp;" id="remove_this"><br>
									    <input type="button" value="&nbsp&gt;&gt&nbsp; " id="add_all"><br>
									    <input type="button" value="&nbsp&lt;&lt&nbsp; " id="remove_all"><br>
									    </td>
									    <td class="selectKind">要选择的类别:<br>
										    <select multiple="multiple" id="selectBut" name="adviceInfo.classId" style="width:130px;height:160px;">
										         <c:forEach var="list" items="${codeLists }">
										             <c:if test="${list.code!=null }">
											             <option value="${list.code }" selected="selected">${list.name }</option>
											         </c:if>
											     </c:forEach>
										    </select>
									    </td>
								    </tr>
								</table>
		    				</td>
		    			</td>
	    			</tr>
				</table>
				<div style="text-align:center;padding:5px">
			    	<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
			</form>
		</div>
	</div>
<script type="text/javascript">
		//渲染医嘱类别下拉框
		$('#itemId').combobox({
			valueField : 'typeCode',
			textField : 'typeName',
			url:'<%=basePath%>baseinfo/advice/queryBusinessAdvicetoInpatientKind.action', 
			disabled:false,
			editable:false,
			onSelect:function(record){
				setTimeout(function(){
					closeAndOpenEdit(record.typeCode);	
				},0)
			}
		});
	//关闭
	function closeLayout(){
		$('#divLayout').layout('remove','east');
	}
	//保存
	function submit(flg) {
		var classId = document.getElementById("selectBut").options
		var clasId = [];
		for(var i = 0; i < classId.length; i++){
			clasId.push(classId[i].value);
		}
		$('#editForm').form('submit', {
			url:"<%=basePath %>baseinfo/advice/saveBusinessAdvicetoSystemclass.action",
			queryParams:{clasId:clasId},
			onSubmit : function() {
				return $(this).form('validate');
			},
			success : function(data) {
				if (flg == 0) {
					$.messager.alert("操作提示","保存成功");
					$('#divLayout').layout('remove', 'east');
					$('#list').datagrid('clearSelections'); //获取当前选中行   
					//实现刷新
					$("#list").datagrid("reload");
				} else if (flg == 1) {
					//清除editForm
					$('#editForm').form('reset');
				}
			},
			error : function(date) {
				$.messager.alert("操作提示","保存失败");
			}
		});
	}
	 //移到右边
    $('#add_this').click(function() {
    //获取选中的选项，删除并追加给对方
        $('#selectAll option:selected').appendTo('#selectBut');
        $('#selectBut option').attr('selected','selected');//将右侧数据再次选中  否则修改或保存时会有问题 
    });
    //移到左边
    $('#remove_this').click(function() {
        $('#selectBut option:selected').appendTo('#selectAll');
        $('#selectBut option').attr('selected','selected');//将右侧数据再次选中  否则修改或保存时会有问题 
    });
    //全部移到右边
    $('#add_all').click(function() {
        //获取全部的选项,删除并追加给对方
        if(!$('#selectAll').prop("disabled")){
        	 $('#selectAll option').appendTo('#selectBut');
        }
        $('#selectBut option').attr('selected','selected');//将右侧数据再次选中  否则修改或保存时会有问题 
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
        $('#selectBut option').attr('selected','selected');//将右侧数据再次选中  否则修改或保存时会有问题 
    });
    //双击选项
    $('#selectBut').dblclick(function(){
       $("option:selected",this).appendTo('#selectAll');
       $('#selectBut option').attr('selected','selected');//将右侧数据再次选中  否则修改或保存时会有问题 
    });	
    
</script>
</body>
</html>