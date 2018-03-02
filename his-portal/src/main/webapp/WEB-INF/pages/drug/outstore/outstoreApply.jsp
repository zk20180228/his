<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>出库退库申请</title>
<%@ include file="/common/metas.jsp"%>
<script language="javascript" type="text/javascript" src="<%=basePath%>datePicker/WdatePicker.js"></script>
</head>
<body style="margin: 0px; padding: 0px;">
	<div id="divLayout" class="easyui-layout" style="width: 100%; height: 100%;"data-options="fit:true">
		<!-- 出库退库申请 -->
		<div data-options="region:'north',split:false"style="width: 100%; height: 35px;">
				<table cellspacing="0" cellpadding="0" border="0">
					<tr>
						<td style="padding: 5px 5px 0px 5px;" nowrap="nowrap">
						<input type="hidden" value="${loginDeptId}" id="loginDept">
							出库科室：<input class="easyui-combobox" type="text" ID="deptList" />
							&nbsp;出库日期：<input style='width:145px;' name='outTimeS' id="outTimeS" class="Wdate"  onFocus="var outTimeS=$dp.$('outTimeS');WdatePicker({dateFmt:'yyyy-MM',onpicked:function(){outTimeS.focus();},maxDate:'#F{$dp.$D(\'outTimeE\')}'})"/>
							&nbsp;至	&nbsp;<input style='width:145px;' name='outTimeE' id="outTimeE" class="Wdate"  onFocus="var outTimeS=$dp.$('outTimeE');WdatePicker({dateFmt:'yyyy-MM',onpicked:function(){outTimeE.focus();},minDate:'#F{$dp.$D(\'outTimeS\')}'})"/>
						</td>
					</tr>
				</table>
			</div>
		<div class="easyui-panel" data-options="region:'center',split:false,title:'药品列表',iconCls:'icon-book'" style="height:32%; width: 50%;">
			<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north'" style="width:100%;height: 35px">
			<table cellspacing="0" cellpadding="0" border="0"  style="padding: 5px 5px 0px 5px;">
				<tr>
					<td>
						药品类型：<input type="text" id="CodeDrugtype"/>
						&nbsp;查询条件：
						<input class="easyui-textbox" id="queryName" onkeydown="keyDown()" data-options="prompt:'通用名,拼音,五笔,自定义'" style="width:180px" />
						<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						</shiro:hasPermission>
					</td>
				</tr>
			</table>
			</div>
			<div data-options="region:'center'" style="width: 100%; height: 85%">
			<table id="list" style="width: 100%" 
			data-options="url:'',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,
			selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">
				<thead>
					<tr>
						<th data-options="field:'id',hidden:true">
							出库记录Id
						</th>
						<th data-options="field:'drugDeptCode',hidden:true">
							发药科室
						</th>
						<th data-options="field:'drugCode',hidden:true">
							药品编码
						</th>
						<th data-options="field:'drugCommonname',width:'10%'">
							药品通用名
						</th>
						<th data-options="field:'drugSpec',width:'5%'">
							药品规格
						</th>
						<th data-options="field:'validDate',width:'8%',hidden:true">
							有效期
						</th>
						<th data-options="field:'outNum',width:'5%'">
							库存
						</th>
						<th data-options="field:'drugMinimumunit',formatter: minFamater,width:'7%'">
							最小单位
						</th>
						<th data-options="field:'purchasePrice',width:'5%'">
							进价
						</th>
						<th data-options="field:'drugRetailprice',width:'5%'">
							零售价
						</th>
						<th data-options="field:'companyCode',hidden:true">厂家</th>
						<th data-options="field:'drugPackagingunit',hidden:true" ></th>
					</tr>
				</thead>
			</table>
			</div>
			</div>
			
		</div>
		<div class="easyui-panel" data-options="collapsible:false,region:'south',split:false,title:'待出库药品检索',iconCls:'icon-book'"style="width: 100%;height:60%;">
			<div id="divLayout" class="easyui-layout" fit=true>
				<div data-options="region:'north',split:false,border:false" style="height: 35px">
					<table cellspacing="0" cellpadding="0" border="0" style="padding:5px 5px 0px 5px;">
						<tr>
							<td  nowrap="nowrap">
								查询条件：
								<input class="easyui-textbox" id="tradNameSerc" data-options="prompt:'通用名,拼音,五笔,自定义'" style="width:180px"/>
								<a href="javascript:void(0)" onclick="searchList()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							</td>
						</tr>
					</table>
				</div>
				<div data-options="region:'center',split:false,border:false" style="height: 100%;">
					<div style="height: 89%">
						<form id="saveForm" method="post" style="height: 100%;">
							<input type="hidden" name="outstoreJson" id="outstoreJson">
							<table id="infolist" style="width: 100%;height:80%" data-options="fit:true,url:'',method:'post',idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,toolbar: '#tb'"></table>
						</form>
					</div>
					<div id="totalDivId" style="width: 100%;display: none;height: 10%">
						<table width="100%">
							<tr>	
								<td style="padding: 2px 15px;">
									<font style="padding-left:45%;">总零售金额：</font>
									<span ID="totalStoreCost" style="font-weight: bold;color: red;"></span>
								</td>
							</tr>
						</table>
					</div>
				<div id="tb" style="height: auto">
					<shiro:hasPermission name="${menuAlias}:function:add">
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="submit()">保存</a>
					</shiro:hasPermission>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="delList()">删除</a>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		//定义全局变量记录总的待出库信息条目
		var totalRows = "";
		//记录已经存在待出库列表条目药品id
		var arrIds = [];
		//标记出库数量为0或者出库数量小于库存数量
		var ischeck=true;
		//格式化所用 2016年10月18日 新加
		var minList = null;
		var packList = null;
		//加载页面
		$(function(){
			//初始化下拉框
			//idCombobox("CodeDrugtype");
			idCombobox("drugType");
			//下拉框的keydown事件   调用弹出窗口
			var drugType = $('#CodeDrugtype').combobox('textbox'); 
			drugType.keyup(function(){
				KeyDown1(0,"CodeDrugtype");
			});
			
			bindEnterEvent('tradNameSerc',searchList,'easyui');//绑定回车事件
			bindEnterEvent('queryName',searchFrom,'easyui');//绑定回车事件
			/** 2016年10月18日 GH新加  包装单位和最小单位区分开 不再是一种编码**/
			$.ajax({
				url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
				data:{"type" : "minunit"},
				type:'post',
				success: function(data) {
					minList = data;
				}
			});
			$.ajax({
				url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action", 
				data:{"type" : "packunit"},
				type:'post',
				success: function(data) {
					packList = data;
				}
			});
			
			//初始化目标科室下拉
			$('#deptList').combobox({
				url : "<%=basePath%>baseinfo/department/getDeptBySessionDept.action?loginDepteId="+$('#loginDept').val()+"&type=2",
				valueField : 'deptCode',
				textField : 'deptName',
				width : 170,
				onSelect:function(record){
					searchFrom();
				}
			});
			setTimeout(function(){
				$('#list').datagrid({
					pagination : true,
					pageSize : 20,
					pageList : [ 20, 30, 50, 80, 100 ],
					url:"<%=basePath%>drug/outstore/queryReturnOutstore.action?flag=1&menuAlias=${menuAlias}",
					queryParams:{
						"deptId" : $('#deptList').combobox('getValue')
					},
					onDblClickRow: function (rowIndex, rowData) {//双击查看
						var flagSubmit;
						if($('#deptList').combobox('getValue')==null ||$('#deptList').combobox('getValue')==""){
							flagSubmit=1;
							$.messager.alert('提示',"请先选择出库科室!");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}if(flagSubmit !=1){
							var row = $('#list').datagrid('getSelected');
							if (row) {
								attrValue(row);
							}
						}
					}
				});
			},1);
			
			//添加datagrid事件及分页
			$('#infolist').datagrid({
				striped:true,
				checkOnSelect:true,
				selectOnCheck:true,
				singleSelect:false,
				fitColumns:false,
		   		columns:[[    
			        {field:'ck',checkbox:true}, 
			        {field:'flagId',title:'flagId',hidden:true,width:'0%',formatter:function(value,row,index){
			        	return "<div id='"+row.drugCode+"'></div>";
			        }},
			        {field:'id',title:'出库记录Id',hidden:true},
			        {field:'drugCode',title:'药品编码',hidden:true},
			        {field:'drugType',title:'药品类型',hidden:true},
			        {field:'drugQuality',title:'药品性质',hidden:true},
			        {field:'showUnit',title:'显示的单位',hidden:true},
			        {field:'purchasePrice',title:'购入价',hidden:true},
			        {field:'wholesalePrice',title:'批发价',hidden:true},
			        {field:'doseUnit',title:'剂量单位',hidden:true},
			        {field:'drugDeptCode',title:'发药科室',hidden:true},
			        {field:'drugStorageCode',title:'领药科室',hidden:true},
			        {field:'tradeName',title:'药品名称',hidden : true},
			        {field:'drugCommonname',title : '药品通用名',width : '10%'},
			        {field:'drugCnamepinyin',title : '通用拼音码',hidden : true}, 
			        {field:'drugCnamewb',title : '通用五笔码',hidden : true}, 
			        {field:'drugCnameinputcode',title : '通用自定义码',hidden : true},
			        {field:'specs',title:'规格',width:'7%'},
			        {field:'retailPrice',title:'零售价',width:'7%',formatter:function(value,row,index){
			        	return "<div id='retailPrice_"+index+"'>"+value+"</div>";
			        }},
			        {field:'packQty',title:'包装数量',width:'7%'}, 
			        {field:'packUnit',title:'包装单位',width:'7%',formatter: packFamater}, 
					{field:'storeNum',title:'库存',width:'7%',hidden : true}, 
					{field:'outNum',title:'库存',width:'7%'}, 
			        {field:'returnNum',title:'申请退库数量',width:'7%',formatter:function(value,row,index){
			        	var retVal = "";
						if (value != null && value != "" && !isNaN(value) && value >0) {
							retVal = value;
						}else{
							retVal = '';
						}
				        return "<input type='text' id='"+row.id+"_"+index+"' value='"+retVal+"' onChange = 'upperCase(this)'>";
			        }},     
			        {field:'outlCost',title:'申请退库金额',width:'7%',formatter:function(value,row,index){
			        	if (value != null && value != "" && !isNaN(value) && value >0) {
							return value;
						}else{
							return '';
						}
			        	}},
			        {field:'mark',title:'备注',width:'7%',formatter:function(value,row,index){
			        	var retVal = "";
			        	if(value!=null&&value!=""){
			        		retVal = value;
			        	}
			        	return "<input type='text' id='remark_"+index+"' value='"+retVal+"'>";
			        }}
				]]
			});
		});
		//确认把数据填充到待出库列表
		function conform(row){
			var outlCost = row.drugRetailprice * row.outNum;
			if (isNaN(outlCost)) {
				outlCost = "";
			}
			var index =$('#infolist').datagrid('appendRow',{
				id:row.id,
				drugCode : row.drugCode,
				drugDeptCode : row.drugDeptCode,
				drugStorageCode : row.drugStorageCode,
				tradeName : row.drugName,
				drugCommonname : row.drugCommonname,
				drugCnamepinyin : row.drugCnamepinyin,
				drugCnamewb : row.drugCnamewb,
				drugCnameinputcode : row.drugCnameinputcode,
				specs : row.drugSpec,
				retailPrice : row.drugRetailprice,
				wholesalePrice : row.wholesalePrice,
				packUnit : row.drugPackagingunit,
				packQty : row.drugPackagingnum,
				returnNum : row.outNum,
				outlCost : outlCost.toFixed(4),
				doseUnit : row.doseUnit,
				drugType : row.drugType,
		        drugQuality : row.drugQuality,
		        showUnit : row.showUnit,
		        purchasePrice : row.purchasePrice,
		        storeNum : row.storeNum,
		        outNum : row.outNum
			}).edatagrid('getRows').length-1;
         			$('#infolist').edatagrid('beginEdit',index);
         	var rows = $('#infolist').datagrid("getRows");
			totalRows = cloneObject(rows);
			//计算总金额
			totalPrice();
			$("#totalDivId").show();
		}
		function submit(){
			var rows = $('#infolist').datagrid('getChecked');
			var Checklength=$('#infolist').datagrid("getChecked").length;
			if(Checklength>0){
				for(var i=0; i<rows.length; i++){
					if(!isNaN(rows[i].outNum) && rows[i].outNum >= 0){
					}else{
						$.messager.alert('提示','请在第'+(i+1)+'行申请退库数量中输入大于0的数字');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						   return;
					}
				}
				if(validCancleNum()){
					$('#outstoreJson').val(JSON.stringify( $('#infolist').datagrid("getChecked")));
					var deptId = $('#deptList').combobox('getValue')
					if (deptId != null && deptId != "") {
					$('#saveForm').form('submit', {    
					    url:"<%=basePath%>drug/outstore/updateReturnOutstore.action?flag=1&deptId="+deptId,
					    onSubmit: function(){
					    	if (!$('#saveForm').form('validate')) {
					    		$.messager.alert('提示',"验证没有通过,不能提交表单!");
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								return false;
							}
					    	$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
					    },    
					    success:function(data){
					    	$.messager.progress('close');
							var data = eval('(' + data + ')');
							if(data.resCode == "error"){
								$.messager.alert('提示',data.resMes,"warning");
								return false;
							}
								  $.messager.alert('提示',"出库申请记录保存成功！");
								  $('#list').datagrid('reload');
// 								  $('#infolist').datagrid('unselectAll');
// 								  $('#infolist').datagrid('uncheckAll');
									rows.length=0;
								  $('#infolist').datagrid('loadData', { total: 0, rows: [] });
					    },
					    error : function(data) {
					    	$.messager.progress('close');
					    	$.messager.alert('提示',"保存失败！");
						}
					});
				} else {
					$.messager.alert("操作提示", "请选择供货科室！", "warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					}
				}
			}else{
				$.messager.alert('操作提示',"请勾选待出库退库的记录！","warning");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		}
		//移除待出库列表
		function delList(){
			var rows = $('#infolist').datagrid("getChecked");
			if(rows.length>0){
				var arr=[];
				for(var i=0; i<rows.length; i++){
					var dd = $('#infolist').edatagrid('getRowIndex',rows[i]);//获得行索引
					var tag=rows[i].drugCode+"_"+rows[i].groupCode;
					if(arrIds.indexOf(tag)!=-1){
						arrIds.remove(tag);
					}
					arr.push(dd);
				}
				for(var i=arr.length-1;i>=0;i--){
					$('#infolist').edatagrid('deleteRow',arr[i]);//通过索引删除该行
				}
				totalPrice();
			}else{
				$.messager.alert("操作提示", "请选择要删除的条目！","warning");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		}
		function upperCase(obj){
			var val = $(obj).val();
			var index = $(obj).prop("id").split("_");
			var i = parseInt(index[1]);
			var retailPrice = $('#retailPrice_' + i).text();
			var outlCost = retailPrice * val;
			var rowsInfolist = $('#infolist').datagrid('getRows');
			var outNum = rowsInfolist[i].outNum;
			var drugCommonname = rowsInfolist[i].drugCommonname;
			var mark = document.getElementById('remark_' + i).value;
			if(parseFloat(outNum) < parseFloat(val)){
				$.messager.alert('提示',"药品 "+drugCommonname+" 申请数量大于库存数量，请核对后重新输入");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				var index = $('#infolist').datagrid('updateRow', {
					index : i,
					row : {
						returnNum : outNum,
						outlCost : (rowsInfolist[i].retailPrice * rowsInfolist[i].outNum).toFixed(4),
						mark : mark
					}
				}).edatagrid('getRows').length - 1;
				$('#infolist').edatagrid('beginEdit', index);
				//更新一下总金额
				totalPrice();
				return false;
			}
			var index = $('#infolist').datagrid('updateRow', {
				index : i,
				row : {
					returnNum : val,
					outlCost : (rowsInfolist[i].retailPrice * val).toFixed(4),
					mark : mark
				}
			}).edatagrid('getRows').length - 1;
			$('#infolist').edatagrid('beginEdit', index);
			//更新一下总金额
			totalPrice();
		}
		function validCancleNum(){
			var row = $('#infolist').datagrid("getChecked");
			for (var i=0;i<row.length;i++){
			   $('#infolist').datagrid('endEdit',i);
		       var storeNum = row[i].storeNum;
		       var returnNum = row[i].returnNum;
		       if(returnNum !=0){
		    	   if(parseFloat(returnNum)>parseFloat(storeNum)){
		    		   $.messager.alert('提示',"退回数量大于库存数量");
		    		   setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
				      return false;
			       }
		       }else{
		    	   $.messager.alert('提示',"请填写申请退库数量");
		    	   setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
		    	   return false;
		       }
			}
			return true;
		}
		//判断是否已经填充到待出库列表
		function attrValue(row){
			var rows = $('#infolist').datagrid("getRows");
			var tag=row.drugCode+"_"+row.groupCode;
			if(rows.length>0){
				if(arrIds.indexOf(tag) == -1){
					conform(row);
					arrIds.push(tag);
				}else{
					$.messager.alert("操作提示", "待出库列表已有此条记录！","warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}else{
				conform(row);
				if(arrIds.indexOf(tag)==-1){
					arrIds.push(tag);
				}
			}
		}
		
		function cloneObject(obj){
			var o = obj.constructor === Array ? [] : {};
			for(var i in obj){
				if(obj.hasOwnProperty(i)){
					o[i] = typeof obj[i] === "object" ? cloneObject(obj[i]) : obj[i];
				}
			}
			return o;
		}
		//计算总的金额
		function totalPrice(){
			var rows = $('#infolist').datagrid('getRows');
			var price=0;
			for(var i=0;i<rows.length;i++){
				var drugCode = rows[i].drugCode;
				if(!$('#'+drugCode).parent().parent().parent().is(":hidden")){//判断是否隐藏
					price += parseFloat(rows[i].outlCost);
				}
			}
			if(!isNaN(price) && price >= 0){
				price = price.toFixed(2);
			}else{
				price = '';
			}
			$("#totalStoreCost").html(price);
		}
		//页面查询检索
		var arr=[];
		function searchList(){
			var queryName = $("#tradNameSerc").textbox('getValue');
			if ((queryName == null || queryName == "")) {
				//显示全部
				for ( var i = 0; i < arr.length; i++) {
					arr[i].show();
				}
				arr.length = 0;
			} else {
				if (queryName != null && queryName != "") {
					for ( var i = 0; i < totalRows.length; i++) {
						var namestr = totalRows[i].drugCommonname;
						var pinstr = totalRows[i].drugCnamepinyin;
						var wbstr = totalRows[i].drugCnamewb;
						var searchItem=namestr.indexOf(queryName) == -1 && pinstr.indexOf(queryName) == -1 && wbstr.indexOf(queryName) == -1;
						if(totalRows[i].drugCnameinputcode!=null){
							var inputstr = totalRows[i].drugCnameinputcode;
							searchItem=namestr.indexOf(queryName) == -1 && pinstr.indexOf(queryName) == -1 && wbstr.indexOf(queryName) == -1&& inputstr.indexOf(queryName) == -1;
						}
						//不匹配数据，执行删除
						if (searchItem) {
							$("#" + totalRows[i].drugCode).parent().parent().parent().hide();
							arr.push($("#" + totalRows[i].drugCode).parent().parent().parent());
						}else{
							$("#" + totalRows[i].drugCode).parent().parent().parent().show();
						}
					}
				}
			}
			totalPrice();
		}
		// 药品列表查询
		function searchFrom() {
			var drugName = $('#queryName').textbox('getValue');
			var drugType = $('#CodeDrugtype').combobox('getValue');
			 var outTimeS=$('#outTimeS').val();
			 var outTimeE=$('#outTimeE').val();
			$('#list').datagrid('load', {
				"deptId" : $('#deptList').combobox('getValue'),
				drugNameweb: drugName,
		    	drugTypeSerc:drugType,
		    	outTimeS:outTimeS,
		    	outTimeE:outTimeE
			});
		}
		//空格弹窗事件
		function KeyDown1(flg,tag){ 	    	
	    	if(flg==1){//回车键光标移动到下一个输入框
		    	if(event.keyCode==13){	
		    		event.keyCode=9;
		    	}
		    } 
		    if(flg==0){	//空格键打开弹出窗口
			    if (event.keyCode == 32)  
			    { 
			        event.returnValue=false;  
			        event.cancel = true; 
			        if(tag=="CodeDrugtype"){
			        	showWin("请药品类别","<c:url value='/ComboxOut.action'/>?xml="+"CodeDrugtype,0","50%","80%");
			        }
			    }
		    }
		} 
		//从xml文件中解析，读到下拉框
		function idCombobox(param){
			$('#CodeDrugtype').combobox({
			    url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type="+param,    
			    valueField:'encode',    
			    textField:'name',
			    multiple:false,
			    onLoadSuccess: function() {//请求成功后
					$(list).each(function(){
						  if ($('#CodeDrugtype').val() == this.Id) {
							  $('#CodeDrugtype').combobox("select",this.Id);
						   }
					});
				},
				onSelect: function(record){
					searchFrom();
				}
			});
		}
		/** 2016年10月18日 GH新加  包装单位和最小单位区分开 不再是一种编码**/
		//显示最小单位格式化
		function minFamater(value){
			if(value!=null){
				for(var i=0;i<minList.length;i++){
					if(value==minList[i].encode){
						return minList[i].name;
					}
				}	
			}
		}
		//显示包装单位格式化
		function packFamater(value){
			if(value!=null){
				for(var i=0;i<packList.length;i++){
					if(value==packList[i].encode){
						return packList[i].name;
					}
				}	
			}
		}
	</script>
</body>
</html>