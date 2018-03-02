<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>时限质控项目设置</title>
<script type="text/javascript">
var medicTypeMap = new Map();
$(function(){
	setTimeout(function(){
		$('#list').datagrid({
			url:'<%= basePath%>/emrs/timerule/getAllTimeRule.action',
		});
	},800);
	$.ajax({
		url:"<%=basePath%>emrs/medcialRecord/getemrtypeCombox.action",
		success: function(data){
			for(var i=0;i<data.length;i++){
				medicTypeMap.put(data[i].encode,data[i].name);
			}
		}
	});
});
function submit(){
	var submitType = $('#submitType').val();
	var url = "<%=basePath%>emrs/timerule/addTimeRule.action";
	if(submitType==1){
		url = "<%=basePath%>emrs/timerule/updateTimeRule.action";
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
        		$('#ruletrSet').text('');
        		$('#relationRule').text('');
        		$('#ruledesc').text('');
        	});   
	    },    
	    error: function(data){    
	    	$.messager.progress('close');
	    	$.messager.alert('提示','保存失败！！','info');   
	    } 
	});
}
//新增
function addvalue(){
	$('#addDForm').form('reset');
	$('#submitType').val(0);//新增
	$('#win').window('open');
	itemEmrTypeComBox();
}
//修改
function updatevalue(){
	var row = $('#list').datagrid('getSelected');
	$('#submitType').val(1);//新增
	itemEmrTypeComBox();
	$('#win').window('open');
	$('#ruleid').val(row.id);
	$('#rulecode').textbox('setValue',row.code);
	$('#rulename').textbox('setValue',row.name);
	$('#ruledesc').text(row.desc);
	$('#responsibilityEvel').textbox('setValue',row.responsibilityEvel);
	$('#rulevalue').numberbox('setValue',row.value);
	$('#ruletip').textbox('setValue',row.tip);
	$('#rulewarn').textbox('setValue',row.warn);
	$('#ruletrSet').text(row.trSet);
	$('#ruleloop').numberbox('setValue',row.loop);
	$('#ruleloopGap').numberbox('setValue',row.loopGap);
	$('#ruledispose').numberbox('setValue',row.dispose);
	$('#relationRule').text(row.relationRule);
	$('#emrType').combobox('setValue',row.emrType);
	$('#deductpionts').numberbox('setValue',row.deductpionts);
}
//删除
function delvalue(){
	var row = $('#list').datagrid('getSelected');
	$.ajax({
		url:"<%=basePath%>emrs/timerule/delTimeRule.action",
		data:{id:row.id}
		,success: function(data){
			$.messager.progress('close');
        	$.messager.alert('提示',data.resMsg,'info',function(){
        		$('#list').datagrid('reload');
        		$('#addDForm').form('reset');
        		$('#win').window('close');
        	});   
	    }
		,error: function(data){ 
			$.messager.progress('close');
	    	$.messager.alert('提示','保存失败！！','info');   
	    }
	});
}
//病历分类下拉
function itemEmrTypeComBox(){
	$('#emrType').combobox({
		url:"<%=basePath%>emrs/medcialRecord/getemrtypeCombox.action",
		valueField:'encode',    
		textField:'name'   
	});
}
function formatedata(value,row,index){
	if(value==1){
		return "周";
	}else if(value==2){
		return "天";
	}else if(value==3){
		return "小时";
	}else if(value==4){
		return "分钟";
	}else if(value==5){
		return "秒";
	}
}
//渲染病历分类
function formtterType(value,row,index){
	return medicTypeMap.get(value);
}
</script>
</head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false" style="height:35px" data-options="fit:true">
		  <form  data-options="border:false">	        
				<table style="width:100%;border:false;padding:1px;">
					<tr >
						<td>
							&nbsp;<a href="javascript:void(0)" onclick="addvalue()" class="easyui-linkbutton" iconCls="icon-add">添加</a>
							&nbsp;&nbsp;<a href="javascript:void(0)" onclick="updatevalue()" class="easyui-linkbutton" iconCls="l-btn-icon icon-edit">修改</a>
							&nbsp;&nbsp;<a href="javascript:void(0)" onclick="delvalue()" class="easyui-linkbutton" iconCls="l-btn-icon icon-remove">删除</a>
						</td>
					</tr>
				</table>
			</form>
	</div>   
    <div data-options="region:'center',border:false" data-options="fit:true">
    	<table id="list" class="easyui-datagrid" style="width:100%" data-options="rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,fitColumns:true,singleSelect:true,fit:true">   
		    <thead>   
		        <tr>   
		        	<th data-options="field:'ck',checkbox:true" ></th>
		            <th data-options="field:'code',width:100">时限编码</th>   
		            <th data-options="field:'name',width:100">时限名称</th>   
		            <th data-options="field:'desc',width:100">时限描述</th>   
		            <th data-options="field:'group',width:100">时限分组</th>   
		            <th data-options="field:'responsibilityEvel',width:100">责任级别</th>   
		            <th data-options="field:'value',width:100">时限（分钟）</th>   
		            <th data-options="field:'trSet',width:100">条件设置</th>   
		            <th data-options="field:'tip',width:100">提示信息</th>   
		            <th data-options="field:'warn',width:100">警告信息</th>   
		            <th data-options="field:'op',width:100">操作方式</th>   
		            <th data-options="field:'loop',width:100">循环次数</th>   
		            <th data-options="field:'loopGap',width:100">循环间隔</th>   
		            <th data-options="field:'dispose',width:100">处理方式</th>   
		            <th data-options="field:'relationRule',width:100">相关规则</th>   
		            <th data-options="field:'emrType',width:100,formatter:formtterType">病历分类</th>   
		            <th data-options="field:'deductpionts',width:100">扣分</th>   
		        </tr>   
		    </thead>   
		</table>  
    </div>   
	<div id="win" class="easyui-window" title="新增/修改" style="width:600px;height:400px; position: relative;" data-options="modal:true,closed:true,iconCls:'icon-save',collapsible:false,minimizable:false,maximizable:false,closable:true">
		<div class="easyui-layout" data-options="fit:true,border:false">   
		    <div data-options="region:'center',split:false,border:false">
		    	<form id="addDForm" method="post" >
			<input id="ruleid" type="hidden" name="rule.id" value="${rule.id}"/>
			<input id="submitType" type="hidden">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin:10px auto 0;">
				<tr>
					<td class="honry-lable">
						<span>时限编码:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-textbox" id="rulecode"
									name="rule.code"
									value="${rule.code }"
									data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>时限名称:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-textbox" id="rulename"
									name="rule.name"
									value="${rule.name }"
									data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>时限描述:</span>
					</td>
					<td class="honry-info">
						<textarea style="width: 200px;" id="ruledesc"	name="rule.desc" value="${rule.desc }"	data-options="required:true"></textarea>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>责任级别:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-textbox" id="responsibilityEvel"
									name="rule.responsibilityEvel"
									value="${rule.responsibilityEvel }"
									data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>时限(分钟):</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-numberbox" id="rulevalue"
									name="rule.value"
									value="${rule.value }"
									data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>提示信息:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-textbox" id="ruletip"
									name="rule.tip"
									value="${rule.tip }"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>警告信息:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-textbox" id="rulewarn"
									name="rule.warn"
									value="${rule.warn }"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>条件设置:</span>
					</td>
					<td class="honry-info">
						<textarea style="width:200px;" id="ruletrSet" name="rule.trSet" value="${rule.trSet }"></textarea>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>循环次数:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-numberbox" id="ruleloop"
									name="rule.loop"
									value="${rule.loop }"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>循环间隔(秒):</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-numberbox" id="ruleloopGap"
									name="rule.loopGap"
									value="${rule.loopGap }"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>处理方式:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-numberbox" id="ruledispose"
									name="rule.dispose"
									value="${rule.dispose }"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>相关规则:</span>
					</td>
					<td class="honry-info">
						<textarea style="width:200px;" id="relationRule" name="rule.relationRule" value="${rule.relationRule }"></textarea>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>病历分类:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-combobox" id="emrType"
									name="rule.emrType"
									value="${rule.emrType }"
									data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>扣分:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-numberbox" id="deductpionts"
									name="rule.deductpionts"
									value="${rule.deductpionts }"
									data-options="required:true"/>
					</td>
				</tr>
			</table>
		</form>
		    </div>   
		    <div data-options="region:'south',split:false" style="height:50px;border-width:1px 0 0 0;">
		    	<div style="text-align: center; padding: 5px;">
					<a href="javascript:submit();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_tick'">确定</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#win').window('close');" data-options="iconCls:'icon-cancel'">关闭</a>
				</div>
		    </div>   
		</div>
	</div>
</body>
</html>