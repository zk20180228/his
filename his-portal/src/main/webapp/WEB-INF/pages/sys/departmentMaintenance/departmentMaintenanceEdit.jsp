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
	<div class="easyui-panel" id="panelEast" data-options="iconCls:'icon-form',fit:true" style="width: 100%;border:0">
		<div>
    		<form id="editForm" method="post">
				<input type="hidden" id="id" name="id" value="${employeeDividept.id}">
				<%--<input type="hidden" id="divisionName" name="employeeDividept.divisionName" value="${employeeDividept.divisionName}" /> --%>
				<%--<input type="hidden" id="divisionCode" name="employeeDividept.divisionCode" value="${employeeDividept.divisionCode}" />--%>
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin:10px auto 0;">
					<tr>
						<td class="honry-lable">领导：</td>
		    			<td class="honry-info"><input type="hidden" id="account" name="employeeDividept.account"   value="${employeeDividept.account}" data-options="required:true"/>
		    								   <input  id="name" name="employeeDividept.name"  style="width:120px" value="${employeeDividept.name}" />
		    			</td>
	    			</tr>
					<tr>
						<td class="honry-lable">关联科室：</td>
		    			<td class="honry-info">
		    			<input type="hidden" id="deptCode1" name = "employeeDividept.deptCode" value="${employeeDividept.deptCode}"/>
		    			<input type="hidden"  name = "employeeDividept.type" value="${employeeDividept.type}"/>
		    			<input type="text"  class="easyui-textbox" data-options="required:true,buttonIcon:'icon-folder'" name = "employeeDividept.deptName" id="deptName"  style="width: 300" value="${employeeDividept.deptName}" />
		    			</td>
	    			</tr>
		    	</table>
			    <div style="text-align:center;padding:5px">
			    	<a id="save" href="javascript:submitForm();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
	    	</form>
	    </div>
	</div>    
	<div id="mview-window"><iframe  class="iframeid" style = 'width:100%;height:100%;margin: 0;padding: 0;border: none;' ></iframe></div>
	<script type="text/javascript">
        $('#name').combogrid({
            idField:'employeeName',
            textField:'employeeName',
            mode:'remote',
            panelWidth:650,
            panelHeight:350,
            striped:true,
            checkOnSelect:true,
            selectOnCheck:false,
            singleSelect:true,
            pagination:true,
            pageNumber:1,
            pageSize:10,
            pageList:[10,20,30,50],
            <%--url:'<%=basePath %>sys/userMenuDataJuris/queryDataJurisUserList.action',--%>
            url : "<c:url value='/baseinfo/departmentMaintenance/queryEmployeeExtendList.action'/>",
            columns:[[
                {field:'employeeJobNo',title:'账户',width:80},
                {field:'employeeName',title:'姓名',width:90},
                {field:'departmentCode',title:'部门编号',width:90},
                {field:'department',title:'部门名称',width:90},
                // {field:'division',title:'学部名称',width:90},
                // {field:'divisionCode',title:'学部编号',width:90},
                {field:'titleName',title:'人员职称',width:90},
                {field:'dutiesName',title:'职务',width:90},
                {field:'employeeMobile',title:'手机',width:100}
            ]],
            onSelect:function(index,row){
                var account = row.employeeJobNo;
                $('#account').val(row.employeeJobNo);
                // $('#divisionName').val(row.division);
                // $('#divisionCode').val(row.divisionCode);
                $.ajax({
                    url: "<c:url value='/baseinfo/departmentMaintenance/queryVo.action'/>?account="+account,
                    type:'post',
                    success: function(data) {
                        if (data!=null) {
                            var dNames="";
                            var dCodes="";
                            for(var i=0;i<data.length;i++){
                                if(dNames!=""){
                                    dNames += ",";
                                    dCodes += ",";
                                }
                                dNames+=data[i].deptName;
                                dCodes+=data[i].deptCode;
                            }
                            $('#deptName').textbox('setValue',dNames);
                            $('#deptCode1').val(dCodes);
                        }

                    }
                });
            }
        });
		//加载页面
		$(function(){
			$('#mview-window').window({    
			    width:1060,    
			    height:620,    
			    modal:true,
			    minimizable:false,
			    title:"关联科室"
			}).window('close');
			
		 	$("#deptName").textbox({onClickButton:function(){
		    	$("#mview-window .iframeid")[0].src = '<%=basePath%>baseinfo/departmentMaintenance/addRight.action'
	    		$('#mview-window').window('open');
		    }})
		    
		   	
		});
		
	    
		//表单提交
		function submitForm(){
			// $('#name').val($('#account').val());
			$('#editForm').form('submit',{  
				url:'<%=basePath%>baseinfo/departmentMaintenance/saveDeptMaintenance.action', 
	        	onSubmit:function(){
	        		if (!$('#editForm').form('validate')) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						return false;
					}
	        		$.messager.progress({text:'保存中，请稍后...',modal:true});
	        	},
	        	success:function(dataMap){
	        		$.messager.progress('close');
					var data =  JSON.parse(dataMap);
	        		if(dataMap.resCode=="success"){
						$.messager.alert('提示',data.resMsg);
						closeLayout();
	        		}else{
	        			$.messager.alert('提示',data.resMsg);
	        			closeLayout();
	        		}
	        		$('#list').datagrid('reload');
	        		refresh();
	    		 },
	        	error:function(dataMap){
	        		$.messager.alert('提示','保存失败');
	        		closeLayout();
	        	}
	   		}); 
		}
	    
		//清除页面填写信息
		function clear(){
			$('#editForm').form('reset');
			$('#account').combobox('setValue',"");
			$('#deptName').textbox('setValue','');
		}
		//关闭编辑窗口
		function closeLayout(){
			$('#dialog').dialog('close');
		}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>