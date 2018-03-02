<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>药品集中发送</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
//渲染表单中的包装单位
 var unitMap="";
 //渲染表单中的最小单位
 var minunitMap="";
var packUnitList=null;
$(function(){
	/**
	 *摆药单分类树
	 */
	$('#tDt').tree({
		url: '<%=basePath%>inpatient/deliveryDelivery/treeDelivery.action',
		method:'get',
		animate:true,
		lines:true,
		formatter:function(node){//统计节点总数
			return node.text;
		},onClick: function(node){
			$('#list').datagrid({
				url:'<%=basePath %>inpatient/deliveryDelivery/queryDelivery.action',
				queryParams: {billCode : node.attributes.code},
				onLoadSuccess: function (data) {//默认选中
					$('#list').datagrid("autoMergeCells", ['name','combNo','bedId']);
					$('#list').datagrid('checkAll');
				},
				onClickRow:function(rowIndex, rowData){
					$('#list').datagrid('unselectAll');
					var par = 0;
					var tname = rowData.name;
					if($("#isDefault").is(":checked")){
						if($("#stop_flg").is(":checked")){
							par = 1;
							tname = rowData.name;	
						}
						
					}else if($("#isDefault").is(":checked")){
						if($("#stop_flg").is(":checked")){
						}else{
							par = 1;
							tname = rowData.name;
						}
					
					}else if($("#stop_flg").is(":checked")){
						if($("#isDefault").is(":checked")){
						}else{
							par = 2;
							tname = rowData.combNo;
						}
					}
					var rows = $('#list').datagrid('getRows');
					if(rows!=null&&rows.length>0){
						for(var i=0;i<rows.length;i++){
							if(par==0||par==1){
								if(rows[i].name == tname){
									var index = $('#list').datagrid('getRowIndex',rows[i])
									$('#list').datagrid('selectRow',index);
								}
							}
							if(par==2){
								if(rows[i].combNo == tname){
									var index = $('#list').datagrid('getRowIndex',rows[i])
									$('#list').datagrid('selectRow',index);
								}
							}
							
						}
					}
				}
			});
		},
	}); 
	/**
	 * 发送单号树
	 */
	$('#tDt1').tree({    
	    url: '<%=basePath%>inpatient/deliveryDelivery/treeApplyOut.action',    
	    method:'get',
	    animate:true,
	    lines:true,
	    formatter:function(node){//统计节点总数
			var s = node.text;
			return s;
		},onClick: function(node){
			$('#listBill').datagrid({
				url:'<%=basePath %>inpatient/deliveryDelivery/queryAlreadyBill.action',
				queryParams: {drugEdBill : node.text}
			});
		}
	});
	$('#list').datagrid({
	});
	$('#listBill').datagrid({
		pagination:true,
		pageSize:20,
		pageList:[20,30,50,80,100],
		onLoadSuccess : function(data){
			var pager = $(this).datagrid('getPager');
			var aArr = $(pager).find('a');
			var iArr = $(pager).find('input');
			$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
			for(var i=0;i<aArr.length;i++){
				$(aArr[i]).tooltip({
					content:toolArr[i],
					hideDelay:1
				});
				$(aArr[i]).tooltip('hide');
			}
		}
	});
	$("#listBill").datagrid('loadData', { total: 0, rows: [] });
	$.ajax({
		url: "<%=basePath %>inpatient/deliveryDelivery/likeDrugPackagingunit.action",
		type:'post',
		success: function(packUnitdata) {
			packUnitList = packUnitdata ;
		}
	});
	
	//渲染表单中的包装单位
	$.ajax({
		url: "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionaryMap.action?type=packunit", 
		type:'post',
		success: function(data) {
			unitMap = data;
		}
	});
	
	//渲染表单中的最小单位
	$.ajax({
		url: "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionaryMap.action?type=minunit", 
		type:'post',
		success: function(data) {
			minunitMap = data;
		}
	});
	
});
//渲染表单中的包装单位
function funPackUnit(value,row,index){
	if(value!=null&&value!=''){
		if(row.showFlag=="1"){//包装单位
			return unitMap[value];
		}else if(row.showFlag=="0"){//最小单位
			return minunitMap[value];
		}
	}
}
//渲染数量
function funNum(value,row,index){
	if(value!=null&&value!=''){
		if(row.showFlag=="1"){//包装单位
			return value/row.packQty;
		}else if(row.showFlag=="0"){//最小单位
			return value;
		}
	}
	return value;
}

function functionDefault(){
	$('#list').datagrid('uncheckAll');
	if($("#isDefault").is(":checked")){   //患者选中
		if($("#stop_flg").is(":checked")){
			if($("#canSelect").is(":checked")){
			}else{
				$('#canSelect').prop("checked",true); 
			}
			$('#list').datagrid('checkAll');
		}else{
			$("#canSelect").prop("checked",false);
		}
	}else if($("#stop_flg").is(":checked")){    //组合号选中
		if($("#isDefault").is(":checked")){	
			if($('#canSelect').is(":checked")){
			}else{
				$('#canSelect').prop("checked",true);
			}
			$('#list').datagrid('checkAll');
		}else{
			$("#canSelect").prop("checked",false);
		}
	}else if($("#canSelect").is(":checked")){  //全选
		if($("#isDefault").is(":checked")){
		}else{
			$('#isDefault').prop("checked",true); 
		}
		if($("#stop_flg").is(":checked")){
		}else{
			$('#stop_flg').prop("checked",true); 
		}
		$('#list').datagrid('checkAll');
	}
}


function loadList(){
	$('#list').datagrid({
		url:'<%=basePath %>inpatient/deliveryDelivery/queryDelivery.action?menuAlias=${menuAlias}',
		onLoadSuccess: function (data) {//默认选中
			$('#list').datagrid("autoMergeCells", ['name','combNo','bedId']);
			$('#list').datagrid('checkAll');
		},onClickRow:function(rowIndex, rowData){
			$('#list').datagrid('unselectAll');
				var par = 0;
				var tname = rowData.name;
			if($("#isDefault").is(":checked")){
				if($("#stop_flg").is(":checked")){
					par = 1;
					tname = rowData.name;	
				}
				
			}else if($("#isDefault").is(":checked")){
				if($("#stop_flg").is(":checked")){
				}else{
					par = 1;
					tname = rowData.name;
				}
			
			}else if($("#stop_flg").is(":checked")){
				if($("#isDefault").is(":checked")){
				}else{
					par = 2;
					tname = rowData.combNo;
				}
			}
			var rows = $('#list').datagrid('getRows');
			if(rows!=null&&rows.length>0){
				for(var i=0;i<rows.length;i++){
					if(par==0||par==1){
						if(rows[i].name == tname){
							var index = $('#list').datagrid('getRowIndex',rows[i])
							$('#list').datagrid('selectRow',index);
						}
					}
					if(par==2){
						if(rows[i].combNo == tname){
							var index = $('#list').datagrid('getRowIndex',rows[i])
							$('#list').datagrid('selectRow',index);
						}
					}
					
				}
			}
		}
		
	});
}
function loadListBill(drugedClass){
	$('#listBill').datagrid({
	});
	$('#listBill').datagrid({
		pagination:true,
		pageSize:20,
		pageList:[20,30,50,80,100],
		url:'<%=basePath %>inpatient/deliveryDelivery/queryAlreadyBill.action?menuAlias=${menuAlias}',
		queryParams: {drugEdBill : drugedClass}
	});
}
/**
 *显示单位格式化
 */
function packUnitFamater(value,row,index){
	if(value!=null){
		for(var i=0;i<packUnitList.length;i++){
			if(value==packUnitList[i].encode){
				return packUnitList[i].name;
			}
		}	
	}
}
$.extend($.fn.datagrid.methods, {
	autoMergeCells: function (jq, fields) {
		return jq.each(function () {
			var target = $(this);
			if (!fields) {
				fields = target.datagrid("getColumnFields");
			}
			var rows = target.datagrid("getRows");
			var i = 0,
			j = 0,
			temp = {};
			for (i; i < rows.length; i++) {
				var row = rows[i];
				j = 0;
				for (j; j < fields.length; j++) {
					var field = fields[j];
					var tf = temp[field];
					if (!tf) {
						tf = temp[field] = {};
						tf[row[field]] = [i];
					} else {
						var tfv = tf[row[field]];
						if (tfv) {
							tfv.push(i);
						} else {
							tfv = tf[row[field]] = [i];
						}
					}
				}
			}
			$.each(temp, function (field, colunm) {
				$.each(colunm, function () {
				var group = this;
					if (group.length > 1) {
						var before,
						after,
						megerIndex = group[0];
						for (var i = 0; i < group.length; i++) {
							before = group[i];
							after = group[i + 1];
							if (after && (after - before) == 1) {
							    continue;
							}
							var rowspan = before - megerIndex + 1;
							if (rowspan > 1) {
								target.datagrid('mergeCells', {
									index: megerIndex,
									field: field,
									rowspan: rowspan
								});
							}
							if (after && (after - before) != 1) {
							    megerIndex = after;
							}
						}
					}
				});
			});
		});
	}
});

/*
 * 格式化发送类型
 */
 function sendTypeFamater(value,row,index){
	if(value==1){
		return "集中发送";
	}else if(value==2){
		return "临时发送";
	}else if(value==3){
		return "全部";
	}
}
 /*
  *格式化有效性
  */
  function validStateFamater(value,row,index){
 	if(value==1){
 		return "有";
 	}else if(value==0){
 		return "无";
 	}
 }
  /*
   *格式摆药状态
   */
   function stateFamater(value,row,index){
  	if(value==2){
  		return "是";
  	}else{
  		return "否";
  	}
  }

//发送
function save() {
	//选中要发送的行
	var rows = $('#list').datagrid('getChecked');
	var no='';
	var sendType=$('input:radio[name="sendType"]:checked').val();
	if (rows.length > 0) {//选中几行的话触发事件	                        
		$.messager.confirm('确认','确定要发送选中信息吗?',function(res) {//提示是否发送
			if (res) {
			    var ids = '';
				for ( var i = 0; i < rows.length; i++) {
				   if (ids != '') {
					  ids += ',';
				   }
					  ids += rows[i].id;
					if(no !=''){
						no +="','";
					}
					no +=rows[i].inpatientNo;
				}
				$.messager.progress({text:'正在发送，请稍后...',modal:true});
				$.ajax({
					url:'<%=basePath %>inpatient/deliveryDelivery/saveApplyOut.action',
					data:{id:ids,sendType:sendType},
					type:'post',
					success: function(data) {
						$.messager.progress('close');
						var drugedClass=data;
						var length=rows.length;
						$.messager.confirm('确认','发送成功!是否打印摆药单?',function(res){ 
							if (res){ 
								$.messager.progress({text:'正在打印摆药单，请稍后...',modal:true});
								var map = new Map();
								var inpatientNo="";
								for (var i = 0; i < rows.length; i++) {
									if(inpatientNo!=rows[i].inpatientNo){
										inpatientNo=rows[i].inpatientNo;
										var timerStr = Math.random();
										window.open("<%=basePath %>inpatient/deliveryDelivery/iReportInvoiceBill.action?inpatientNo="+inpatientNo+"&drugedbill="+drugedClass+"&fileName=baiyaodan",i,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
									}
								}
								$.ajax({
									url:'<%=basePath %>inpatient/deliveryDelivery/updateApplyOut.action',
									data:{id:ids,sendType:$('input:radio[name="sendType"]:checked').val()},
									type:'post',
									success: function() {
										$.messager.progress('close');
										$.messager.alert('提示',"打印成功");
										setTimeout(function(){
											$(".messager-body").window('close');
										},3500);
									}
								}); 
							}
						});
						loadList();
						loadListBill(drugedClass);
					    $('#tDt').tree('reload');
					    $('#tDt1').tree('reload');
					},error : function() {
						$.messager.progress('close');
						$.messager.alert('提示','保存失败！');
					}
				});	
			}
		});
	} else {
		$.messager.alert('提示',"请选择要发送的记录!");
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
	}
}

</script>
<style type="text/css">
.panel-header{
	border-top:0;
}
</style>
</head>
<body style="margin: 0px;padding: 0px;">
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	    <div id="p" data-options="region:'west',split:true" style="width:15%;height:100%;border-top:0">
		    <div id="tt" class="easyui-tabs" style="width: 100%;" data-options="border:false,fit:true" >
			    <div title="未发药" data-options="border:false">   
			      	<ul id="tDt" data-options="border:false"></ul> 
			    </div>   
			    <div title="已发药" data-options="border:false">
			         <ul id="tDt1" data-options="border:false"></ul>   
			    </div>
		    </div>
		</div>  
		<div data-options="region:'center',title:'摆药信息'" style="width: 85%;height: 100%;border-top:0">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'center',border:false" style="height: 46%">
				<div style="width: 100%;height: 100%">
					<table id="list" 
		    			data-options="method:'post',rownumbers:true,striped:true,border:false,fit:true,checkOnSelect:true,selectOnCheck:false,singleSelect:false,fitColumns:false,toolbar:'#toolbarId'">
						<thead>
							<tr>
								<th data-options="field:'ck',checkbox:true "></th>
								<th data-options="field:'bedId'" style="width: 70px">床号</th>
								<th data-options="field:'name'" style="width:70px">姓名</th>
								<th data-options="field:'combNo'" style="width: 90px">组合号</th>
								<th data-options="field:'applyNumber'" style="width: 90px">流水号</th>  
								<th data-options="field:'drugName'" style="width: 250px">药品名称</th>   
								<th data-options="field:'specs'"  style="width: 160px">规格</th>   
								<th data-options="field:'dfqFreq'" style="width: 150px">频次</th> 
								<th data-options="field:'useName'" style="width:150px">用法</th>   
								<th data-options="field:'applyNumSum',formatter:funNum" style="width: 40px">总量</th>   
								<th data-options="field:'showUnit',formatter:funPackUnit" style="width: 60px">单位</th>  
								<th data-options="field:'applyDeptName'" style="width: 150px">申请科室</th>   
								<th data-options="field:'drugDeptName'" style="width: 150px">取药药房</th>   
								<th data-options="field:'sendType',formatter:sendTypeFamater" style="width: 90px">发送类型</th>  
								<th data-options="field:'billclassName'" style="width: 200px">摆药单分类</th>
								<th data-options="field:'printEmplName'" style="width: 90px">发送人</th>  
								<th data-options="field:'printDate'" style="width: 150px">发送时间</th>   
								<th data-options="field:'validState',formatter:validStateFamater" style="width: 90px">有效性</th>   
								<th data-options="field:'pinyin'" style="width: 90px">拼音码</th>   
								<th data-options="field:'wb'"  style="width: 90px">五笔码</th> 
								<th data-options="field:'print',formatter:stateFamater" style="width:90px">是否摆药</th>
								<th data-options="field:'drugEdBill',hidden:true" style="width: 90px",>摆药单号</th> 
								<th data-options="field:'inpatientNo',hidden:true" style="width: 90px",>住院流水号</th> 
							</tr>   
						</thead>   
					</table>
				</div>
				</div>
				<div  data-options="region:'south'" style="height: 49%;border-left:0">
					<table id="listBill" class="easyui-datagrid"
		    			data-options="title:'已发药账单',method:'post',rownumbers:true,striped:true,border:false,fit:true,checkOnSelect:true,selectOnCheck:false,singleSelect:false,fitColumns:false">
						<thead>
							<tr>
								<th data-options="field:'bedId'" style="width: 70px">床号</th>   
								<th data-options="field:'name'" style="width: 70px">姓名</th>
								<th data-options="field:'patientNo',hidden:true" ></th>
								<th data-options="field:'applyNumber'" style="width: 90px">流水号</th>  
								<th data-options="field:'drugName'" style="width: 250px">药品名称</th>   
								<th data-options="field:'specs'"  style="width: 160px">规格</th>   
								<th data-options="field:'dfqFreq'" style="width: 150px">频次</th> 
								<th data-options="field:'useName'" style="width: 150px">用法</th>   
								<th data-options="field:'applyNumSum',formatter:funNum" style="width: 40px">总量</th>   
								<th data-options="field:'showUnit',formatter:funPackUnit" style="width: 60px">单位</th>  
								<th data-options="field:'applyDeptName'" style="width: 150px">申请科室</th>   
								<th data-options="field:'drugDeptName'" style="width: 150px">取药药房</th>   
								<th data-options="field:'sendType',formatter:sendTypeFamater" style="width: 90px">发送类型</th>  
								<th data-options="field:'drugEdBill'" style="width: 150px">摆药单号</th> 
								<th data-options="field:'printEmplName'" style="width: 90px">发送人</th>  
								<th data-options="field:'printDate'" style="width: 150px">发送时间</th>  
								<th data-options="field:'drugedEmplName'" style="width: 90px">发药人</th>
								<th data-options="field:'applyDate'" style="width: 150px">发药时间</th> 
								<th data-options="field:'validState',formatter:validStateFamater" style="width: 90px">有效性</th>   
								<th data-options="field:'print',formatter:stateFamater" style="width: 90px">是否摆药</th>
								<th data-options="field:'pinyin'" style="width: 90px">拼音码</th>   
								<th data-options="field:'wb'"  style="width: 90px">五笔码</th> 
							</tr>   
						</thead>   
					</table> 
				</div>
			</div>
	    </div>   
	</div> 
	<div id="toolbarId" style="padding:7px;">
		<shiro:hasPermission name="${menuAlias}:function:save"> 
			<a href="javascript:save()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">保存</a>
		</shiro:hasPermission>
		&nbsp;
		<input type="hidden" id="canSelects"/>
		<input type="checkBox" id="canSelect" checked="checked" onclick="functionDefault()"/>全选
		<input type="hidden" id="isDefaults"/>
		<input type="checkBox" id="isDefault" checked="checked" onclick="functionDefault()"/>按患者选择
		&nbsp;
		<input type="hidden" id="stop_flgs"/>
		<input type="checkBox" checked="checked" id="stop_flg" onclick="functionDefault()"/>按组合选择
		&nbsp;&nbsp;
		<input type="radio" name="sendType" value=3 />全部
		&nbsp;
		<input type="radio" name="sendType" value=1 checked="checked"/>集中发送
		&nbsp;
		<input type="radio" name="sendType"  value=2 />临时发送
		&nbsp;<font style="font-size:12px;color: red">注意：毒麻药请按手工处方发送</font>
	</div>
</body>
</html>