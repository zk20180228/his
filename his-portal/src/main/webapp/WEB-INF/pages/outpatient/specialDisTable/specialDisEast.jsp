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
<div class="easyui-panel" id="panelEast" border="none">
	<div style="padding:10px">
		<input type="hidden" id="loginDept" value="${loginDept }"/>
		<input type="hidden" id="loginType" value="${loginType }"/>
   		<form id="editForm" method="post">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin-left:auto;margin-right:auto;">
					<td class="honry-lable">
						药房：
					</td>
					<td class="honry-info">
						<input class="easyui-combobox" id="dept" name="stoTerminalSpe.deptid"
							data-options="required:true" missingMessage="请选择药房" style="width: 200"/>
					</td>
    			</tr>
				<tr>
					<td class="honry-lable">
						类别：
					</td>
					<td id="leibie" class="honry-info">
						<input class="easyui-combobox" id="itemType" name="stoTerminalSpe.itemType"
							missingMessage="请选择要添加的特殊类别" style="width: 200"/>
					</td>
    			</tr>
				<tr>
					<td class="honry-lable">
						配药台：
					</td>
					<td id='peiyao' class="honry-info">
						<input class="easyui-combobox" id="code" name="stoTerminalSpe.code"
							missingMessage="请选择配药台" style="width: 200"/>
					</td>
    			</tr>
    			<tr>
					<td class="honry-lable">
						备注：
					</td>
					<td class="honry-info">
						<textarea class="easyui-textbox" rows="2" cols="32" id="mark" name="stoTerminalSpe.mark"
							data-options="multiline:true" maxlength="25" style="width:200;height:80"></textarea>
					</td>
    			</tr>
				<tr>
					<td class="honry-lable">
						项目名称：
					</td>
					<td class="honry-info">
						<input id="itemName" type="text" name='stoTerminalSpe.itemName'
							missingMessage="点击键盘" style="width: 200;height: 50"/>
							<input type="hidden" id="iCode" name="stoTerminalSpe.itemCode">
					</td>
    			</tr>
				<tr style="display: none">
					<td class="honry-lable">
						数据：
					</td>
					<td class="honry-info">
						<input id="itemCode" type="text" name='jsDataJSON'
							style="width: 200;height: 50"/>
					</td>
    			</tr>
	    	</table>
	    		<div style="text-align: center; padding: 5px">
					<c:if test="${ListVo.id==null }">
						<a href="javascript:addContinue();" class="easyui-linkbutton"
							data-options="iconCls:'icon-save'">连续添加</a>
					</c:if>
					<a href="javascript:submit();void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-save'">保存</a>
					<a href="javascript:clear();void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-clear'">清除</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
				</div>
    	</form>
    	<div id="dd"></div>
    	<div id="dilogtree"></div>
    </div>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">

$(function(){
	
	<%-- 初始化类别，配药台，项目名称，备注  文本框 --%>
	$('#itemType').combobox({ 
		required:true,
		readonly:true
	})
	$('#code').combobox({
		required:true,
		readonly:true
	})
	$('#itemName').textbox({ 
		required:true,
		multiline:true,
		editable:false
	})
	$('#itemCode').textbox({ 
		multiline:true,
	})
	
	<%-- 加载药房     根据药房查找药房下配药台类别   根据药房和类别查找 配药台    --%>
	$('#dept').combobox({    
		url:'<%=basePath%>drug/specialDispensingtable/loadDept.action',
	    valueField:'deptCode',    
	    textField:'deptName',
// 	    editable:false,
	    onLoadSuccess: function(data){
	    	if($('#loginType').val() == 'P'){
	    		$('#dept').combobox('select',$('#loginDept').val());
	    	}else{
	    		$('#dept').combobox('select',data[0].deptCode);
	    	}
	    },
	    onChange:function(record){
	    	funitemType(record);
	    	$('#code').combobox('setValue', '');
	    	$('#itemCode').textbox('setValue', '');
	    	$('#code').combobox({
	    		readonly:true
	    	});
	    	$('#itemName').textbox({
	    		disable:true
	        });
	    },filter: function(q, row){
	        var keys = new Array();
	        keys[keys.length] = 'deptCode';
	        keys[keys.length] = 'deptName';
	        keys[keys.length] = 'deptPinyin';
	        keys[keys.length] = 'deptWb';
	        keys[keys.length] = 'deptInputcode';
	        return filterLocalCombobox(q, row, keys);
	    },onHidePanel:function(none){
	        var data = $(this).combobox('getData');
	        var val = $(this).combobox('getValue');
	        var result = true;
	        for (var i = 0; i < data.length; i++) {
	            if (val == data[i].deptCode) {
	                result = false;
	            }
	        }
	        if (result) {
	            $(this).combobox("clear");
	        }else{
	            $(this).combobox('unselect',val);
	            $(this).combobox('select',val);
	        }
	    }
	});
});


<%--***  根据药房信息查找药房下配药台类别     ****************         添加修改js  begin           ***********************************--%>
function funitemType(dept){
	$('#itemType').combobox({    
		//data:itemTypeData(dept),
		readonly:false,
		data:getitemTypeArray(),
	    valueField:'id',    
	    textField:'value',
	    editable:false,
	    onChange:function(record){
	    	funcode(record);
	    	$('#itemCode').textbox('setValue', '');
	    }
	}); 
}

<%--***  根据药房信息  配药台类别     查找配药台***--%>
function funcode(record){
	$('#code').combobox({ 
		readonly:false
	})
	var deptid=$("#dept").combobox('getValue');
	
	$.ajax({
		url:'<%=basePath%>drug/specialDispensingtable/dispensingTableForDeptitemType.action',
		data:{deptid:deptid,'stoTerminalSpe.itemType':record},
		type:'post',
		async:false,
		success:function(data){
			$('#code').combobox({    
				data:data,
			    valueField:'id',    
			    textField:'name',
			    editable:false,
			    onChange:function(record){
			    	$('#itemName').textbox({
			    		editable:true
			        });
			    	$('#itemName').textbox('setValue', '');
			    	$('#itemCode').textbox('setValue', '');
			    	$('#itemName').textbox('textbox').keydown(function(e){
			    		funadditem();
			        });
			    }
			}); 
		}
	});
	
}


<%-- 渲染，拼接 药房下配药台类别下拉   信息 --%>
function itemTypeData(deptid){
	var obj=null;
	$.ajax({
		url:'<%=basePath %>drug/specialDispensingtable/dispensingTableForDept.action?deptid='+deptid,
		type:'post',
		async:false,
		success:function(data){
			obj=eval("("+data+")");
		}
	});
	if(obj.length>0){
		var rev="[";
		$.each(obj,function(i,n){
			if(rev.length>1){
				rev+=",";
			}
			rev+="{'id':'"+n+"','value':'"+itemTypeMap.get(n)+"'}";
		});
		rev+="]";
		return eval("("+ rev +")");
	}
}


<%--***  添加相应类别的项目名称    *******--%>
function funadditem(){
	var title=$('#itemType').combobox('getText');
	var dialog_itemType=$('#itemType').combobox('getValue');
	if($("#d_item")){
		$("#d_item").remove();
	}
	if(dialog_itemType==1){
		openWindow('<%=basePath %>drug/specialDispensingtable/dialogDrugTabView.action?dialogItemType=' + dialog_itemType);
	}else if(dialog_itemType==2||dialog_itemType==4){
		openWindow('<%=basePath %>drug/specialDispensingtable/dialogTreeView.action?dialogItemType=' + dialog_itemType);
	}else{
		openWindow('<%=basePath %>drug/specialDispensingtable/dialogView.action?dialogItemType=' + dialog_itemType);
	}
}

/**  
 *  
 * 下拉弹框
 *
 */
function openWindow(tempWinPath){
	var aaa=window.open(tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth - 800) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
}

//edit的保存按钮
function setValue(code,name){
	$('#itemCode').textbox('setValue', code);
	 $('#itemName').textbox('setValue', name);
}

//表单提交   
function submit() {
	$('#editForm').form('submit', {
		url : "<%=basePath%>drug/specialDispensingtable/jsDataSaveOrUpdate.action",
		data : $('#editForm').serialize(),
		dataType : 'json',
		onSubmit : function() {
			if (!$('#editForm').form('validate')) {
				$.messager.progress('close');
				$.messager.alert('提示信息','验证没有通过,请修改后保存!','warning');
				setTimeout(function(){$(".messager-body").window('close')},1500);
				return false;
			}
			$.messager.progress({text:'保存中，请稍后...',modal:true});
		},
		success : function(data) {
			$.messager.progress('close');
			data = eval('('+data+')');
        	if(data.resCode=='success'){
        		$.messager.alert('提示','保存成功');
				$('#list').datagrid('reload');
				$('#divLayout').layout('remove', 'east');
        	}else{
        		var sto = data.resMsg;
        		$.messager.alert('提示','操作失败；原因：项目名称     '+sto+'    已存在');
        		setTimeout(function(){$(".messager-body").window('close')},1500);
        	}
			//实现刷新栏目中的数据
			//$("#list").datagrid("reload");
		},
		error : function(data) {
			$.messager.progress('close');
			$.messager.alert('提示','保存失败！');
		}
	});
}
//连续添加
function addContinue() {
	$('#editForm').form('submit', {
		url : "<%=basePath%>drug/specialDispensingtable/jsDataSaveOrUpdate.action",
		onSubmit : function() {
			if (!$('#editForm').form('validate')) {
				$.messager.progress('close');
				$.messager.alert('提示信息','验证没有通过,不能提交表单!','warning');
				setTimeout(function(){$(".messager-body").window('close')},1500);
				return false;
			}
			$.messager.progress({text:'保存中，请稍后...',modal:true});
		},
		success : function() {
			$.messager.progress('close');
			data = eval('('+data+')');
        	if(data.resCode=='success'){
        		$.messager.alert('提示','保存成功');
        		setTimeout(function(){$(".messager-body").window('close')},1500);
				$('#editForm').form('reset');
				$('#list').datagrid('reload');
        	}else{
        		var sto = data.resMsg;
        		$.messager.alert('提示','操作失败；原因：项目名称     '+sto+'    已存在');
        		setTimeout(function(){$(".messager-body").window('close')},1500);
        	}
		},
		error : function(data) {
			$.messager.progress('close');
			$.messager.alert('提示','保存失败！');
		}
	});
}

//清除所填信息
function clear() {
	$('#editForm').form('reset');
}
function closeLayout() {
	$('#divLayout').layout('remove', 'east');
}

</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>