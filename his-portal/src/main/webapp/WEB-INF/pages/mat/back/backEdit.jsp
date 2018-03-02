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
		<title>添加页面</title>
	</head>
	<body>
		<div id="panelEast" class="easyui-panel backtEditFont" title="编辑" style="padding: 10px; background: #fafafa;" data-options="fit:'true',border:'false'">
			<form id="backForm" action="" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" value="${back.id }">
					<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%">
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">回收流水号：</span>
						</td>
						<td style="text-align: left;">
							<input id="matBackNo" class="easyui-textbox" name="matBackNo" value="${back.matBackNo }" data-options="required:true,missingMessage:'请填写回收流水号!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">回收序号：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-numberbox" type="text" value="${back.itemNo }" name="itemNo" id="itemNo" data-options="required:true,missingMessage:'请填写回收序号!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">所在科室：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" id="matDeptName" name="matDeptName" value="${back.matDeptName}" data-options="required:true,missingMessage:'请选择所在科室!'" style="width: 200px"/>
							<input type="hidden" id="matDept" name="matDept" value="${back.matDept}" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">回收科室：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" id="matBackDname" name="matBackDname" value="${back.matBackDname}" data-options="required:true,missingMessage:'请选择回收科室!'" style="width: 200px"/>
							<input type="hidden" id="matBackDept" name="matBackDept" value="${back.matBackDept}" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">物品科目：</span>
						</td>
						<td style="text-align: left;">
							<input type="hidden" id="kindCode" value="${back.kindCode }" name="kindCode">
							<input class="easyui-textbox" id="kindName" name="kindName" value="${back.kindName }" data-options="required:true,missingMessage:'请选择物品科目!'" style="width: 200px"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">回收项目：</span>
						</td>
						<td style="text-align: left;">
							<input type="hidden" id="itemCode" value="${back.itemCode }" name="itemCode">
							<input class="easyui-textbox" id="itemName" name="itemName" value="${back.itemName }" data-options="required:true,missingMessage:'请选择回收项目!'" style="width: 200px"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">规格：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${back.specs }" name="specs" id="specs"  style="width: 200px" readonly="readonly"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">最小单位：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${back.minUnit }" name="minUnit" id="minUnit"  style="width: 200px" readonly="readonly"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">大包装单位：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${back.packUnit }" name="packUnit" id="packUnit"  style="width: 200px" readonly="readonly"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">大包装数量：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-numberbox" type="text" value="${back.packQty }" name="packQty" id="packQty"  style="width: 200px" readonly="readonly"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">回收价格：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${back.backPrice }" name="backPrice" id="backPrice"  style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">零售价格：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${back.salePrice }" name="salePrice" id="salePrice"  style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">生产厂商：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" id="producerName" name="producerName" value="${back.producerName}" data-options="required:true,missingMessage:'请选择生产厂商!'" style="width: 200px"/>
							<input type="hidden" id="producerCode" name="producerCode" value="${back.producerCode}" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">回收数量：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-numberbox" type="text" value="${back.backNumber }" name="backNumber" id="backNumber" data-options="required:true,missingMessage:'请填写回收数量!'" style="width: 200px"></input>
						</td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<c:if test="${back.id == null}">
						<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
					</c:if>
					<a id="hospitalOKbtn" href="javascript:void(0)" data-options="iconCls:'icon-save'" onclick="onClickOKbtn()" class="easyui-linkbutton">确定</a>
					<a id="hospitalClearbtn" href="javascript:void(0)" data-options="iconCls:'icon-clear'" onclick="clears()" class="easyui-linkbutton">清空</a>
					<a id="hospitalClearbtn" href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeLayout()" class="easyui-linkbutton">关闭</a>
				</div>
			</form>
		</div>
		<script type="text/javascript">
	$(function() {
		$.extend($.fn.validatebox.defaults.rules, {
			email : {
				validator : function(value) { //email验证	
					return /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(value);
				},
				message : '请输入有效的邮箱账号(例：123456@qq.com)'
			},
			Phone : function(value) {
				var rex = /^1[3-8]+\d{9}$/;
				//var rex=/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
				//区号：前面一个0，后面跟2-3位数字 ： 0\d{2,3}
				//电话号码：7-8位数字： \d{7,8
				//分机号：一般都是3位数字： \d{3,}
				//这样连接起来就是验证电话的正则表达式了：/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/		 
				var rex2 = /^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
				if (rex.test(value) || rex2.test(value)) {
					return true;
				} else {
					return false;
				}

			},
			message : '请输入正确电话或手机格式'
		});
		if($('#stopFlgHidden').val()==1){
			$('#stopFlg').attr("checked", true); 
		}
		
		//科室下拉框渲染
		$('#matDeptName').combobox({
			url : "<%=basePath%>mat/back/queryDeptList.action",
			valueField : 'deptName',
			textField : 'deptName',
			multiple : false,
			onSelect : function(data){
				$('#matDept').val(data.deptCode);
			},
		});
		//科室下拉框渲染
		$('#matBackDname').combobox({
			url : "<%=basePath%>mat/back/queryDeptList.action",
			valueField : 'deptName',
			textField : 'deptName',
			multiple : false,
			onSelect : function(data){
				$('#matBackDept').val(data.deptCode);
			},
		});
		//物品科目下拉框渲染
		$('#kindName').combobox({
			url : "<%=basePath%>material/kind/getMatKindinfoList.action",
			valueField : 'kindName',
			textField : 'kindName',
			multiple : false,
			onSelect : function(data){
				$('#kindCode').val(data.kindCode);
			},
		});
		//生产商下拉框渲染
		$('#producerName').combobox({
			url : "<%=basePath%>material/base/queryMatCompany.action",
			valueField : 'companyName',
			textField : 'companyName',
			multiple : false,
			onSelect : function(data){
				$('#producerCode').val(data.companyCode);
			},
		});
		var name = $.trim($('#itemName').val());
		$('#itemName').combogrid({
			  url:'<%=basePath%>mat/back/queryMatBaseinfo.action', 
			    queryParams:{itemName:name},	
			    editable:true,
				pagination:true,
	    		required:true,
	    		pageSize:10,
				pageList:[10,30,50,80,100],
	    		panelWidth:500,
	    		panelHeight:320,
	            idField:'itemName',    
	            textField:'itemName', 
	            mode:'remote',
			    columns:[[    
			        {field:'itemName',title:'物品名称',width:200},    
			        {field:'itemCode',title:'物品编码',width:100},    
			        {field:'otherName',title:'别名',width:100},    
			        {field:'specs',title:'规格',width:200}, 
			        {field:'minUnit',title:'最小单位',width:100},  
			        {field:'packUnit',title:'大包装单位',width:100},  
			        {field:'packQty',title:'大包装数量',width:100}  
			    ]],
			    onClickRow:function(rowIndex, rowData){
			    	$('#itemCode').val(rowData.itemCode);
			    	$('#specs').textbox('setValue',rowData.specs);
			    	$('#minUnit').textbox('setValue',rowData.minUnit);
			    	$('#packUnit').textbox('setValue',rowData.packUnit);
			    	$('#packQty').textbox('setValue',rowData.packQty);
			    },
		});
	});
	function onClickOKbtn() {
		var url;
		url = '<%=basePath %>mat/back/saveOrupdateBack.action';
			$('#backForm').form('submit', {
				url : url,
				data : $('#backForm').serialize(),
				dataType : 'json',
				onSubmit : function() {
					if (!$('#backForm').form('validate')) {
						$.messager.alert('提示',"验证没有通过,不能提交表单!");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return false;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});	//显示进度条
				},
				success : function(data) {
					if(data=='double'){
						$.messager.progress('close');
						$.messager.alert('提示',"回收流水号不可重复!");
					}else if(data=='error'){
						closeLayout();
						$.messager.alert('提示',"操作失败!");
					}else{
						$.messager.progress('close');
						$('#list').datagrid('load', '<%=basePath %>mat/back/queryMatBack.action');
						closeLayout();
						$.messager.alert('提示',"操作成功!");
					}
				},
				error : function(data) {
					$.messager.progress('close');
					$.messager.alert('提示',"操作失败!");
				}
			});
	}
	/**
	 *
	 *连续添加
	 *
	 */
	function addContinue() {
		if (addAndEdit == 0) {
			$('#backForm').form('submit', {
				url : '<%=basePath %>mat/back/saveOrupdateBack.action',
				data : $('#backForm').serialize(),
				dataType : 'json',
				onSubmit : function() {
					if (!$('#backForm').form('validate')) {
						$.messager.alert('提示',"验证没有通过,不能提交表单!");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return false;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});	//显示进度条
				},
				success : function(data) {
					if(data=='double'){
						$.messager.progress('close');
						$.messager.alert('提示',"回收流水号不可重复,请重新输入!");
					}else if(data=='error'){
						closeLayout();
						$.messager.alert('提示',"操作失败!");
					}else{
						$.messager.progress('close');
						$('#list').datagrid('load', '<%=basePath %>mat/back/queryMatBack.action');
						closeLayout();
						$.messager.alert('提示',"操作成功!");
					}
				},
				error : function(data) {
					$.messager.progress('close');
					$.messager.alert('提示',"操作失败!");
				}
			});
		} else {
			$.messager.alert('操作提示',"添加按钮不能执行修改操作!");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	//清除所填信息
	function clears() {
		$('#backForm').form('clear');
	}
	/**
	* @Description 可选标志的渲染
	* @author   
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
