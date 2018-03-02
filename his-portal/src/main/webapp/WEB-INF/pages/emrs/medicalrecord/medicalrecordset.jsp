<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>病案评分标准设置</title>
<script type="text/javascript">
var map = new Map();
$(function(){
	var tab = $('#tt').tabs('getSelected');
	var index = $('#tt').tabs('getTabIndex',tab);
	var tabid = tab.context.id;
	getdatagrid(tabid);
	$('#tt').tabs({
		onSelect:function(title,index){
			var tab = $('#tt').tabs('getTab',index);
			var tabid = tab.context.id;
			
			getdatagrid(tabid);
		}
	});
	//绑定焦点事件(评分编号)
	$('#recordscoreCode').textbox('textbox').bind('blur', function(event) {
		var code = event.currentTarget.value;
		var tab = $('#tt').tabs('getSelected');
		var tabid = tab.context.id;
		var url = '<%=basePath%>emrs/medcialRecord/getRecordByscoreCode.action';
		validateCode(url,tabid+code,'recordscoreCode');
	});
});
function validateCode(url,code,id){
	if(code==null||code==""){
		return false;
	}
	$.ajax({
		url:url,
		data:{itemCode:code},
		success: function(data){
			if(data!=null&&data.scoreId!=null){
				$('#'+id).textbox('setValue','');
				return false;
			}
	    }
	});
}
function getdatagrid(tabid){
	$('#'+tabid+'tab').datagrid({
		url:'<%=basePath%>emrs/medcialRecord/getRecordByItemCode.action',
		queryParams:{itemCode:tabid},
		toolbar:'#toolbarId'
	});
}
	function submit(){
		var itemsubmitT = $('#itemsubmitT').val();
		var url = "<%=basePath%>emrs/medcialRecord/addStartItem.action";
		if(itemsubmitT==1){
			url = "<%=basePath%>emrs/medcialRecord/updateStartItem.action";
		}
		$.messager.progress({text:'保存中,请稍候...',modal:true});
		$('#addDForm').form('submit',{
			url:url,
			onSubmit: function(){
				if(!$(this).form('validate')){
					$.messager.progress('close');
					$.messager.alert('操作提示','尚有数据不符合规定，请修改后保存！！');
					return false;
				}
			},
			success: function(data){
				$.messager.progress('close');
				var data = eval("("+data+")");
	        	$.messager.alert('提示',data.resMsg,'info',function(){
	        		$('#list').datagrid('reload');
	        		$('#addDForm').form('reset');
	        		$('#win').window('close');
	        		window.location.href="<%=basePath%>emrs/medcialRecord/toMedicalRecordSet.action?menuAlias=${menuAlias}";
	        	});   
		    },    
		    error: function(data){    
		    	$.messager.progress('close');
		    	$.messager.alert('提示','保存失败！！','info');   
		    } 
		});
	}
	function submitrecord(){
		var recordsubmitT = $('#recordsubmitT').val();
		var url = '<%=basePath%>emrs/medcialRecord/addStandardRecord.action';
		if(recordsubmitT==1){
			url = '<%=basePath%>emrs/medcialRecord/updateStandardRecord.action';
		}
		$.messager.progress({text:'保存中,请稍候...',modal:true});
		$('#recordForm').form('submit',{
			url:url,
			onSubmit: function(){
				if(!$(this).form('validate')){
					$.messager.progress('close');
					$.messager.alert('操作提示','尚有数据不符合规定，请修改后保存！！');
					return false;
				}
			},
			success: function(data){
				var tab = $('#tt').tabs('getSelected');
				var tabid = tab.context.id;
				$('#'+tabid+"tab").datagrid('reload');
				$.messager.progress('close');
				var data = eval("("+data+")");
	        	$.messager.alert('提示',data.resMsg,'info',function(){
	        		$('#recordForm').form('reset');
	        		$('#recordwin').window('close');
	        	});   
		    },    
		    error: function(data){    
		    	$.messager.progress('close');
		    	$.messager.alert('提示','保存失败！！','info');   
		    } 
		});
	}
	
	//添加大项
	function additem(){
		$('#itemsubmitT').val(0);
		$('#win').window('open');
		itemEmrTypeComBox();
		clearaddDForm();
		$.ajax({
			url:'<%=basePath%>emrs/medcialRecord/getAllItem.action',
			success: function(data){
				for(var i=0;i<data.length;i++){
					map.put(data[i].itemId,1);
				}
		    }
		});
	}
	//修改大项
	function edititem(){
		$('#itemsubmitT').val(1);
		itemEmrTypeComBox()
		var tab = $('#tt').tabs('getSelected');
		var tabid = tab.context.id;
		$.ajax({
			url:'<%=basePath%>emrs/medcialRecord/getItemByItemCode.action',
			data:{itemCode:tabid},
			success: function(data){
				$('#itemId').textbox('setText',data.itemId);
				$('#itemName').textbox('setValue',data.itemName);
				$('#itemEmrType').combobox('setValue',data.itemEmrType);
				$('#itemScore').textbox('setValue',data.itemScore);
				$('#id').val(data.id);
				$('#win').window('open');
		    }
		});
	}
	//删除大项
	function deltitem(){
		var tab = $('#tt').tabs('getSelected');
		var tabid = tab.context.id;
		$.messager.progress({text:'删除中,请稍候...',modal:true});
		$.ajax({
			url:'<%=basePath%>emrs/medcialRecord/delStartItem.action',
			data:{itemCode:tabid},
			success: function(data){
				$.messager.progress('close');
				var data = eval("("+data+")");
	        	$.messager.alert('提示',data.resMsg,'info',function(){
	        		window.location.href="<%=basePath%>emrs/medcialRecord/toMedicalRecordSet.action?menuAlias=${menuAlias}";
	        	});   
		    },    
		    error: function(data){    
		    	$.messager.progress('close');
		    	$.messager.alert('提示','删除失败!请检查网络...','info');   
		    } 
		});
	}
	//病历分类下拉
	function itemEmrTypeComBox(){
		$('#itemEmrType').combobox({
			url:"<%=basePath%>emrs/medcialRecord/getemrtypeCombox.action",
			valueField:'encode',    
			textField:'name'   
		});
	}
	//添加评分标准
	function addrecord(){
		$('#recordsubmitT').val(0);
		var tab = $('#tt').tabs('getSelected');
		var index = $('#tt').tabs('getTabIndex',tab);
		$('#recorditemId').val(tab.context.id);
		$('#scoreAutoFlagH').val(0);
		$('#scoresingleH').val(0);
		$('#scoreAutoFlag').prop('checked',false);
		$('#scoresingleH').prop('checked',true);
		$('#scoreFieldTR').hide();
		clearrecordForm();//清空表单
		$('#recordwin').window('open');
	}
	//修改评分标准
	function editrecord(){
		$('#recordsubmitT').val(1);
		var tab = $('#tt').tabs('getSelected');
		var index = $('#tt').tabs('getTabIndex',tab);
		var tabsID = tab.context.id;
		var row = $('#'+tabsID+"tab").datagrid('getSelected');
		if(row==null||row==""){
			$.messager.alert('提示','请选择要修改的行!','info');
			return false;
		}
		$('#recordscoreCode').textbox('setValue',row.scoreCode);
		$('#recordscoreDesc').textbox('setValue',row.scoreDesc);
		$('#recordscoreValue').textbox('setValue',row.scoreValue);
		$('#recordscoreBak').textbox('setValue',row.scoreBak);
		$('#recordid').val(row.scoreId);
		$('#recorditemId').val(row.itemId);
		if(row.scoreAutoFlag==1){
			$('#scoreAutoFlag').prop('checked',true);
			$('#scoreFieldTR').show();
			$('#scoreField').textbox('setValue',row.scoreField);
		}else{
			$('#scoreAutoFlag').prop('checked',false);
			$('#scoreFieldTR').hide();
		}
		if(row.single==1){
			$('#scoresingle').prop('checked',true);
		}else{
			$('#scoresingle').prop('checked',false);
		}
		$('#recordwin').window('open');
	}
	//删除评分标准
	function delrecord(){
		$('#recordsubmitT').val(1);
		var tab = $('#tt').tabs('getSelected');
		var index = $('#tt').tabs('getTabIndex',tab);
		var tabsID = tab.context.id;
		var row = $('#'+tabsID+"tab").datagrid('getSelected');
		if(row==null||row==""){
			$.messager.alert('提示','请选择要删除的行!','info');
			return false;
		}
		$.ajax({
			url:"<%=basePath%>emrs/medcialRecord/delStandardRecord.action",
			data:{id:row.id},
			success: function(data){
				var tab = $('#tt').tabs('getSelected');
				var tabid = tab.context.id;
				$('#'+tabid+"tab").datagrid('reload');
				$.messager.progress('close');
				var data = eval("("+data+")");
	        	$.messager.alert('提示',data.resMsg,'info',function(){
	        		$('#recordForm').form('reset');
	        		$('#recordwin').window('close');
	        	});   
		    },    
		    error: function(data){    
		    	$.messager.progress('close');
		    	$.messager.alert('提示','删除失败！！','info');   
		    } 
		});
		
		
	}
	//自动评分标志 1是 0否
	function checkBoxSelect(){
		if($('#scoreAutoFlag').is(':checked')){
			$('#scoreAutoFlagH').val(1);
			$('#scoreFieldTR').show();
			$('#scoreField').textbox({required:true});
		}else{
			$('#scoreAutoFlagH').val(0);
			$('#scoreFieldTR').hide();
			$('#scoreField').textbox({required:false});
		}
	}
	//单次评分
	function checkBoxSingle(){
		if($('#scoresingle').is(':checked')){
			$('#scoresingleH').val(0);
		}else{
			$('#scoresingleH').val(1);
		}
	}
	var msg = "编号不可重复!";
	$.extend($.fn.validatebox.defaults.rules, {
		itemIdValidate:{
			validator: function(value,param){
				if(map.get(value)==1){
					msg = "<span color='red'>编码重复,不可使用!</span>";
					return false;
				}
				msg = "<span color='blue'>编码可使用!</span>";
				return true;
			}
			,message:msg
		}
	});
	//自动评分标志渲染
	function formatterFlag(value,row,index){
		if(value==1){
			return "√";
		}else{
			return "×";
		}
	}
	//单次扣分渲染
	function formatterSingle(value,row,index){
		if(value==0){
			return "√";
		}else if(value==1){
			return "×";
		}
	}
	//清空评分标准表单
	function clearrecordForm(){
		$('#recordid').val('');
		$('#recorditemId').val('');
		$('#recordscoreCode').textbox('setValue','');
		$('#recordscoreDesc').textbox('setValue','');
		$('#recordscoreValue').numberbox('setValue','');
		$('#recordscoreBak').textbox('setValue','');
		$('#scoreAutoFlagH').val(0);
		$('#scoresingleH').val(0);
		$('#scoresingle').prop('checked',true);
		$('#scoreAutoFlag').prop('checked',false);
		$('#scoreField').textbox('setValue','');
	}
	//清空大项表单
	function clearaddDForm(){
		$('#id').val('');
		$('#itemId').textbox('setValue','');
		$('#itemName').textbox('setValue','');
		$('#itemEmrType').combobox('setValue','');
		$('#itemScore').numberbox('setValue','');
	}
</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;"fit=true>
	<div class="easyui-layout" data-options="region:'north'" style="height:35px">
		<div style="padding:2px 1px 1px 1px;">
			<a id="btnSave" href="javascript:additem();" class="easyui-linkbutton" data-options="iconCls:'icon-add',disabled:false">添加</a>&nbsp;&nbsp;
			<a id="btnSerach" href="javascript:edititem();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',disabled:false">修改</a>&nbsp;&nbsp;
			<a id="btnDel" href="javascript:deltitem();" class="easyui-linkbutton" data-options="iconCls:'icon-remove',disabled:false">删除</a>&nbsp;&nbsp;
		</div> 	
	</div>   
    <div class="easyui-layout" data-options="region:'center',border:false" style="width:100%;height:100%;" data-options="fit:true">   
        <div class="easyui-layout" data-options="fit:true,border:false">   
            <div data-options="region:'center',border:false">
            	<div id="tt" class="easyui-tabs" style="width:100%;height:100%;" data-options="fit:true">
            		<c:forEach var="item" items="${itemList }">
					    <div title="${item.itemName }(${item.itemScore })分" id="${item.itemId }" style="padding:1px;">   
					       <table id="${item.itemId }tab" class="easyui-datagrid" style="width:auto;height:auto" data-options="fitColumns:true,singleSelect:true,fit:true" >   
							    <thead>   
							        <tr>   
							            <th data-options="field:'scoreCode',width:'10%' ">评分编码</th>   
							            <th data-options="field:'scoreDesc',width:'50%'">评分项目</th>   
							            <th data-options="field:'scoreValue',width:'10%' ">扣分</th>   
							            <th data-options="field:'scoreBak',width:'10%' ">评分标准</th>   
							            <th data-options="field:'scoreAutoFlag',width:'10%',formatter:formatterFlag ">自动评分标志</th>   
							            <th data-options="field:'single',width:'10%',formatter:formatterSingle ">是否单次扣分</th>   
							        </tr>   
							    </thead> 
						    </table>  
					    </div>   
            		</c:forEach>
				</div>  
            </div>   
        </div>   
    </div>   
	
<div id="win" class="easyui-window" title="添加评分大项" style="width:600px;height:400px" data-options="iconCls:'icon-save',modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false">   
<!-- 	<tbody class="easyui-layout"> -->
<!-- 		<div data-options="region:'north'" style="height:auto;width:auto"> -->
			
<!-- 		</div>    -->
<!-- 	    <div data-options="region:'south'">    -->
	        
<!-- 	    </div>    -->
		
<!-- 	</tbody> -->
     <form id="addDForm" method="post" >
     	<input id="id" type="hidden" name="item.id" value="${item.id}"/>
     	<input id="itemsubmitT" type="hidden"  value="0"/>
     	<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin: 10px auto 0;">
				<tr>
					<td class="honry-lable">
						<span>大项编号:</span>
					</td>
					<td class="honry-info">
						<a href="javascript:void(0)" title="编号不可重复！" data-options="position:'bottom'" class="easyui-tooltip">
			    		<input type="text" class="easyui-textbox" id="itemId" name="item.itemId" value="${item.itemId }" data-options="required:true,precision:0,validType:['itemIdValidate']" style="width: 200"/></a>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>大项名称:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-textbox" id="itemName"
									name="item.itemName"
									value="${item.itemName }"
									data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>病历分类:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-combobox" id="itemEmrType"
									name="item.itemEmrType"
									value="${item.itemEmrType }"
									data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>大项分数:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-numberbox" id="itemScore"
									name="item.itemScore"
									value="${item.itemScore }"
									data-options="required:true"/>
					</td>
				</tr>
			</table>
     </form>
     <div style="text-align: center; padding: 5px">
			<a href="javascript:submit(0);void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_tick'">确定</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#win').window('close');" data-options="iconCls:'icon-cancel'">关闭</a>
	</div>
</div>
<div id="recordwin" class="easyui-window" title="添加评分标准" style="width:600px;height:400px" data-options="iconCls:'icon-save',modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false">   
     <form id="recordForm" method="post">
     	<input id="recordid" type="hidden" name="record.scoreId" value="${record.scoreId}"/>
     	<input id="recorditemId" type="hidden" name="record.itemId" value="${record.itemId}"/>
     	<input id="recordsubmitT" type="hidden" value="0"/>
     	<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin: 10px auto 0;">
     			<tr>
					<td class="honry-lable">
						<span>评分编号:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-textbox" id="recordscoreCode"
									name="record.scoreCode"
									value="${record.scoreCode }"
									data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>评分描述:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-textbox" id="recordscoreDesc"
									name="record.scoreDesc"
									value="${record.scoreDesc }"
									data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>扣分:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-numberbox" id="recordscoreValue"
									name="record.scoreValue"
									value="${record.scoreValue }"
									data-options="required:true,precision:2"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>评分标准:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-textbox" id="recordscoreBak"
									name="record.scoreBak"
									value="${record.scoreBak }"
									data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>自动评分标志:</span>
					</td>
					<td class="honry-info">
						<input type="hidden" id="scoreAutoFlagH" name="record.scoreAutoFlag"  value="${record.scoreAutoFlag }"/>
						<input type="checkbox" id="scoreAutoFlag" checked="false" onclick="javascript:checkBoxSelect()"/>
					</td>
				</tr>
				<tr id="scoreFieldTR">
					<td class="honry-lable">
						<span>关键字段:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-textbox" id="scoreField"
									name="record.scoreField"
									value="${record.scoreField }"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>是否单次扣分:</span>
					</td>
					<td class="honry-info">
						<input type="hidden" id="scoresingleH" name="record.single"  value="${record.single }"/>
						<input type="checkbox" id="scoresingle" checked="false" onclick="javascript:checkBoxSingle()"/>
					</td>
				</tr>
			</table>
     </form>
     <div style="text-align: center; padding: 5px">
			<a href="javascript:submitrecord();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_tick'">确定</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#recordwin').window('close');" data-options="iconCls:'icon-cancel'">关闭</a>
	</div>
</div>
<div id="toolbarId">
	<shiro:hasPermission name="${menuAlias}:function:add">
		<a href="javascript:void(0)" onclick="addrecord()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:edit">
		<a href="javascript:void(0)" onclick="editrecord()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:delete">		
		<a href="javascript:void(0)" onclick="delrecord()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	</shiro:hasPermission>
</div>
</div>
</body>
</html>