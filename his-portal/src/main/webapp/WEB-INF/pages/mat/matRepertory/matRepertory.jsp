<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head></head>  
<body >
	<div class="easyui-layout" style="width:100%;padding:5px 5px 5px 5px;">
		<shiro:hasPermission name="${menuAlias}:function:query">
			<a href="javascript:void(0)"  id="searchquery"  class="easyui-linkbutton" onclick="searchquery()" data-options="iconCls:'icon-search'">查&nbsp;询&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:save">
			<a href="javascript:void(0)"  id="save"  class="easyui-linkbutton" onclick="save()" data-options="iconCls:'icon-save'">保&nbsp;存&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:print">
			<a href="javascript:void(0)"  id="print"  class="easyui-linkbutton" onclick="" data-options="iconCls:'icon-save'">打&nbsp;印&nbsp;设&nbsp;置&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:export">
			<a href="javascript:void(0)"  id="export"  class="easyui-linkbutton" onclick="exportout()" data-options="iconCls:'icon-save'">导&nbsp;出&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;
		</shiro:hasPermission>
	</div>
	<div class="easyui-layout" style="width:100%;height: 90%;padding:15px 5px 5px 5px;">
		<div class="easyui-layout" data-options="region:'west'" style="width: 20%;height: 100%">
			<div data-options="region:'north'" style="width:100%;height: 40%">
				<table>
					<tr>
						<td style="padding:5px 20px 5px 5px">库存科室：</td>
						<td>
							<input class="easyui-combobox" id="matDept"/>
						</td>
					</tr>
				</table>
				<ul id="dtd"></ul>
				<div id="chooseTime"></div>
			</div>
			<div data-options="region:'south'" style="width:100%;height: 60%">
				<div style="padding:5px 5px 5px">
				<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)"  id="search"  class="easyui-linkbutton" onclick="searchquery()" data-options="iconCls:'icon-search'">关&nbsp;系&nbsp;查&nbsp;询&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;
				</shiro:hasPermission>
					<a href="javascript:void(0)"  id="chongzhi"  class="easyui-linkbutton" onclick="chongzhi()" data-options="iconCls:'icon-search'">重&nbsp;置&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<table  id="listsearch" style="padding:5px 5px 5px 5px" data-options="rownumbers:true,singleSelect:true">
				</table>
			</div>
		</div>
		<div class="easyui-layout" data-options="region:'center'" style="width: 80%;height: 100%">
			<div data-options="region:'north'" style="height:60%">
				<table>
					<tr>
						<td style="padding:5px 20px 5px 5px">查询码：</td>
						<td>
							<input class="easyui-textbox" id="searchCode"/>
						</td>
					</tr>
				</table>
				<table class="easyui-edatagrid" id='list1' style="width: 100%" data-options="">
					<thead>
						<tr>
							<th field="stockNo" align="center" width="100px" >库存序号</th>
							<th field="storageCode" align="center" width="100px" >仓库编号</th>
							<th field="itemCode" align="center" width="100px" >物品编号</th>
							<th field="kindCode" align="center" width="100px" formatter="functionkind" >分类名称</th>
							<th field="specs" align="center" width="100px" >规格</th>
							<th field="storeNum" align="center" width="100px" >库存数量</th>
							<th field="minUnit" align="center" width="100px" >最小单位</th>
							<th field="inPrice" align="center" width="100px" >购入价</th>
							<th field="storeCost" align="center" width="100px" >库存金额</th>
							<th field="salePrice" align="center" width="100px" >零售单价</th>
							<th field="saleCost" align="center" width="100px" >零售金额</th>
							<th field="placeCode" align="center" width="100px" editor="{type:'numberbox'}">库位编号</th>
							<th field="validDate" align="center" width="100px" >有效期</th>
							<th field="topNum" align="center" width="100px" editor="{type:'numberbox'}">上线数量</th>
							<th field="lowNum" align="center" width="100px" editor="{type:'numberbox'}">下线数量</th>
							<th field="lackFlag" align="center" width="100px"  formatter="functionflag1">缺货标志</th>
							<th field="validState" align="center" width="100px" formatter="functionflag2">有效标志</th>
							<th field="memo" align="center" width="100px" editor="{type:'textbox'}">备注</th>
							<th field="operCode" align="center" width="100px" >操作员</th>
							<th field="operDate" align="center" width="100px" >操作日期</th>
							<th field="spellCode" align="center" width="100px" >拼音码</th>
							<th field="wbCode" align="center" width="100px" >五笔码</th>
							<th field="customCode" align="center" width="100px" >自定义码</th>
							<th field="highvalueBarcode" align="center" width="100px" >高值耗材条形码</th>
						</tr>
					</thead>
				</table>
			</div>  
			<div data-options="region:'south',title:'库存明细'" style="height:40%">
				<table class="easyui-datagrid" id="list2"   data-options="fitColumns:true,singleSelect:true">
					<thead>
						<tr>
							<th data-options="field:'batchNo',width:'10%'" >批次号</th>
							<th data-options="field:'itemName',width:'10%'">物品名称</th>
							<th data-options="field:'specs',width:'10%'">规格</th>
							<th data-options="field:'inPrice',width:'10%'">购入价</th>
							<th data-options="field:'storeNum',width:'10%'">数量</th>
							<th data-options="field:'minUnit',width:'10%'">单位</th>
							<th data-options="field:'companyCode',width:'10%',formatter:functioncompany">供货单位</th>
							<th data-options="field:'factoryCode',width:'10%',formatter:functioncompany">生产厂家</th>
							<th data-options="field:'validDate',width:'10%'">有效期</th>
							<th data-options="field:'highvalueBarcode',width:'10%'">高值耗材条形码</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
<script type="text/javascript">
	var kindTyped=null;
	var dongtai={type:'datebox'};
	var dongtai=null;
	var num=null;
	var comPanyMap=null;
	var kindMap=new Map();
	$(function(){
		//查询分类名称list
		$.ajax({
			url:'<%=basePath%>matrepertory/querykindCode.action',
			success:function(data){
				var kindlist= eval("("+data+")");
				for(var m=0;m<kindlist.length;m++){
					kindMap.put(kindlist[m].id,kindlist[m].companyName);
				}
			}
		});
		//查询生产厂家map
		$.ajax({
			url:'<%=basePath%>matrepertory/queryCompany.action',
			success:function(data){
				comPanyMap= eval("("+data+")");
			}
		});
		$('#matDept').combobox({
			url:"<%=basePath%>matrepertory/querymatdept.action",
			 valueField:'id',    
			 textField:'deptName'
		});
		$('#dtd').tree({
			url:"<%=basePath%>matrepertory/queryTreeT.action",
			method : 'get',
			animate : true,
			lines : true,
			formatter : function(node) {//统计节点总数
				var s = node.text;
				if (node.children) {
					s += '&nbsp;<span style=\'color:blue\'>('
							+ node.children.length + ')</span>';
				}
				return s;
			},onDblClick:function(node){
				kindTyped=node.id;
				searchquery(kindTyped);
			}
		});
		$('#listsearch').edatagrid({
			columns:[[{
				field : 'relation',
				title: '关系' ,
				type : 'combobox',
				width : '24%',
				editor:{type : 'combobox',options:{ data: [{ id: '无', value: '无'},{ id: 'or', value: 'or'},{id:'and',value:'and'}],
								  valueField: 'id', 
								  textField: 'value',
								 onSelect:function(record){
									 if(record.id=="or"||record.id=="and"){
										 var rows=$('#listsearch').datagrid('getRows');
										 var number=num+1;
										 if(number==rows.length){
											 $('#listsearch').datagrid('appendRow',{
													relation: '',
													tiaojian: '',
													caozuo: '',
													zhi:''
												}); 
										 }
									 }
								 }
					   }}},
					  {field :'tiaojian',title:'条件',width:'24%',formatter:function(value,row,index){
							if(value=="kind_Code"){
								return "分类名称";
							}else if(value=="specs"){
								return "规格";
							}else if(value=="store_Num"){
								return "库存数量";
							}else if(value=="min_Unit"){
								return "最小单位";
							}else if(value=="in_Price"){
								return "购入价";
							}else if(value=="store_Cost"){
								return "库存金额";
							}else if(value=="sale_Price"){
								return "零售单价";
							}else if(value=="sale_Cost"){
								return "零售金额";
							}else if(value=="place_Code"){
								return "库位编号";
							}else if(value=="valid_Date"){
								return "有效期";
							}else if(value=="top_Num"){
								return "上线数量";
							}else if(value=="low_Num"){
								return "下线数量";
							}else if(value=="lack_Flag"){
								return "缺货标志";
							}else if(value=="valid_State"){
								return "有效标志";
							}else if(value=="memo"){
								return "备注";
							}else if(value=="oper_Code"){
								return "操作员";
							}else if(value=="oper_Date"){
								return "操作日期";
							}else if(value=="highvalue_Barcode"){
								return "高值耗材条形码";
							}
					  		},editor:{type:'combobox',options:{
							data:[
									{id:'kind_Code',value:'分类名称'},
									{id:'specs',value:'规格'},
									{id:'store_Num',value:'库存数量'},
									{id:'min_Unit',value:'最小单位'},
									{id:'in_Price',value:'购入价'},
									{id:'store_Cost',value:'库存金额'},
									{id:'sale_Price',value:'零售单价'},
									{id:'sale_Cost',value:'零售金额'},
									{id:'place_Code',value:'库位编号'},
									{id:'valid_Date',value:'有效期'},
									{id:'top_Num',value:'上线数量'}, 
									{id:'low_Num',value:'下线数量'}, 
									{id:'lack_Flag',value:'缺货标志'}, 
									{id:'valid_State',value:'有效标志'}, 
									{id:'memo',value:'备注'}, 
									{id:'oper_Code',value:'操作员'}, 
									{id:'oper_Date',value:'操作日期'}, 
									{id:'highvalue_Barcode',value:'高值耗材条形码'}], 
								valueField: 'id', 
								textField: 'value',
								onSelect:function(record){
									var roww=$('#listsearch').datagrid('getSelected');
									num=$('#listsearch').datagrid('getRowIndex',roww);
									var ed = $('#listsearch').datagrid('getEditor', {index:num,field:'zhi'});
									ed.target.textbox({    
									    width:80
									});
									if(record.id=="operDate"){
										 $('#listsearch').datagrid('endEdit',num);
										 $('#chooseTime').dialog({    
								       		    title: "添加时间",    
								       		    width: '10%',    
								       		    height:'10%',    
								       		    closed: false,    
								       		    cache: false,
								       		 	method:"post",
								       		 	queryParams:{"num":num},
								       		    href: "<%=basePath%>matrepertory/chooseTime.action",    
								       		    modal: true   
								       		   });
									}else if(record.id=="minUnit"){
										//最小单位
										ed.target.combobox({    
										    width:80
										});
									}else if(record.id=="operCode"){
										//操作员 员工表
										ed.target.combobox({
											url:"<%=basePath%>baseinfo/employee/employeeCombobox.action",    
											valueField:'id',    
											textField:'name',
											width:80
										});
									}else if(record.id=="kindCode"){
										//物资分类
										ed.target.combobox({
											url:'<%=basePath%>matrepertory/querykindCode.action',
											valueField:'id',
											textField:'kindName',
											width:80
										});
									}
								}
								}}},
					{field:'caozuo',title:'操作',width:'24%',editor:{type:'combobox',options:{
								data: [{ id: '>', value: '>'},{ id: '>=', value: '>='},{ id: '=', value: '='},{ id: '&lt;=', value: '<='},{ id: '&lt;', value: '<'},{ id: 'like', value: 'like'}],
								valueField: 'id', 
								textField: 'value'
						}}},
					{field:'zhi',title:'值',width:'24%',editor:{type:'textbox'}}
					]],
			onDblClickRow:function(rowIndex,rowData){
				num=rowIndex;
				 var rows=$('#listsearch').datagrid('getRows');
				 for(var i=0;i<rows.length;i++){
					 $('#listsearch').datagrid('endEdit',i);
				 }
				$('#listsearch').datagrid('beginEdit',rowIndex);
				if(rowIndex==0){
					var ed = $('#listsearch').datagrid('getEditor', {index:0,field:'relation'});
					ed.target.combobox({disabled:true});
				}
// 				ed.target.prop('readonly',true);
			}
		});
		addBill(); 
		searchquery();
		$('#list1').edatagrid({
			onDblClickRow:function(rowIndex,rowData){
				$('#list2').datagrid({
					url:'<%=basePath%>matrepertory/queryDatagrid2.action',
					queryParams:{name:rowData.itemCode}
				});
			}
		});
	});
	//默认添加一行数据
	function addBill(){
		 $('#listsearch').datagrid('appendRow',{
				relation: '',
				tiaojian: '',
				caozuo: '',
				zhi:''
			}); 
		 $('#listsearch').datagrid('appendRow',{
				relation: '',
				tiaojian: '',
				caozuo: '',
				zhi:''
			});
		 var rows=$('#listsearch').datagrid('getRows');
		 for(var i=0;i<rows.length;i++){
			 $('#listsearch').datagrid('endEdit',i);
		 }
	}
	function searchquery(){
		var rows=$('#listsearch').datagrid('getRows');
		for(var i=0;i<rows.length;i++){
			 $('#listsearch').datagrid('endEdit',i);
		}
		var name=$('#searchCode').textbox('getValue');
		var deptId=$('#matDept').combobox('getValue');
		var kindType=kindTyped;
		var rowss=$('#listsearch').datagrid('getRows');
		var date=JSON.stringify(rowss);
		$('#list1').datagrid({
			url:'<%=basePath%>matrepertory/queryDatagrid.action',
			queryParams:{name:name,deptId:deptId,kindType:kindType,date:date}
		});
	}
	function chongzhi(){
		$('#listsearch').datagrid('loadData', { total: 0, rows: [] });
		addBill();
	} 
	function save(){
		var rowsss=$('#list1').datagrid('getRows');
		for(var i = 0;i<rowsss.length;i++){
			$('#list1').datagrid('endEdit',i);
		}
		var rows = $('#list1').datagrid('getRows');
		var date=JSON.stringify(rows);
		$.ajax({
			url:'<%=basePath%>matrepertory/saveDatagrid.action',
			data:{date:date},
			success:function(data){
				datam = eval("("+data+")");
				if(datam=="success"){
					$.messager.alert('提示',"保存成功");
				}else{
					$.messager.alert('提示',"保存失败");
				}
			},
			error:function(data){
				$.messager.alert('提示',"保存失败");
			}
		});
	}
	function exportout(){
		var rows = $('#list1').datagrid('getRows');
		var date=JSON.stringify(rows);
		$.ajax({
			url:'<%=basePath%>matrepertory/exportout.action',
			data:{date:date},
			success:function(data){
				date=eval("("+data+")");
				if(date=="success"){
					$.messager.alert('提示',"导出成功");
				}else{
					$.messager.alert('提示',"导出失败");
				}
			},
			error:function(data){
				$.messager.alert('提示',"导出失败");
			}
		});
	}
	//渲染生产厂家和供货商
	function functioncompany(value,row,index){
		if(value!=null&&value!=''){
			return comPanyMap[value];
		}
	}
	//渲染分类名称
	function functionkind(value,row,index){
		if(value!=null&&value!=''){
			return kindMap[value];
		}
	}
	//渲染缺货标志
	function functionflag1(value,row,index){
		if(value==1){
			return '<input type="checkbox" id="lackFlag" name="lackFlag" checked="checked"/>';
		}else{
			return '<input type="checkbox" id="lackFlag" name="lackFlag" />';
		}
	}
	//渲染有效标志
	function functionflag2(value,row,index){
		if(value==1){
			return '<input type="checkbox" id="validState" name="validState" checked="checked"/>';
		}else{
			return '<input type="checkbox" id="validState" name="validState" />';
		}
	}
</script>
</body>
</html>