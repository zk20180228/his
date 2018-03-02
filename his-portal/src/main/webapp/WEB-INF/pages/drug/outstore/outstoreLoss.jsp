<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>报销出库</title>
<%@ include file="/common/metas.jsp"%>
</head>
	<body>
	<!-- 报销出库 -->
		<div id="divLayout" class="easyui-layout" fit=true>
			<div data-options="region:'north',split:false" style="height: 35px">
				<table cellspacing="0" cellpadding="0" border="0" style="width: 100%;">
					<tr>
						<td style="padding: 5px 5px 0px 5px;" nowrap="nowrap">
							出库日期：
							<input ID="outTime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
						</tr>
				</table>
			</div>
		<div data-options="region:'center',split:false,border:false,title:'药品列表',iconCls:'icon-book'" style=" width: 100%;">
			<div id="divLayout" class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',split:false,border:true" style="height: 35px;">
						<table cellspacing="0" cellpadding="0" border="0">
							<tr>
								<td style="padding: 5px 5px 0px 5px;">
									药品类型：<input type="text" id="CodeDrugtype"/>
									&nbsp;查询条件：<input class="easyui-textbox" id="queryName" onkeydown="keyDown()" data-options="prompt:'通用名,拼音,五笔,自定义'" style="width:180px" />
									<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									</shiro:hasPermission>
								</td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',split:false,border:false" style=" width: 100%;">
						<table id="list" style="height: 255px" data-options="fit:true,url:'${pageContext.request.contextPath}/drug/outstore/queryStockinfoJoinDrugInfo.action?flag=2&menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
							<thead>
								<tr>
									<th data-options="field:'drugId',hidden:true">
										药品编码
									</th>
									<th data-options="field:'drugCommonname'">
										药品通用名
									</th>
									<th data-options="field:'storageDeptName'">
										库存科室
									</th>
									<th data-options="field:'drugSpec'">
										药品规格
									</th>
									<th data-options="field:'storeSumDrug'">
										库存数
									</th>
									<th data-options="field:'drugPackagingunit',formatter: packUnitFamater">
										包装单位
									</th>
									<th data-options="field:'drugPackagingnum'">
										包装数量
									</th>
									<th data-options="field:'producerCode',hidden:true">
										厂家id
									</th>
									<th data-options="field:'purchasePrice'">
										进价
									</th>
									<th data-options="field:'retailPrice'">
										零售价
									</th>
									<th data-options="field:'wholesalePrice'">
										批发价
									</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		<div data-options="region:'south',split:false,title:'待出库药品检索',iconCls:'icon-book'"style="height:50%;">
			<div id="divLayout" class="easyui-layout" fit=true>
				<div data-options="region:'north',split:false,border:false" style="height: 35px">
					<table cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td style="padding: 5px 5px 0px 5px;" nowrap="nowrap">
								查询条件：
								<input class="easyui-textbox" id="tradNameSerc" data-options="prompt:'通用名,拼音,五笔,自定义'" style="width:180px"/>
								<a href="javascript:void(0)" onclick="searchList()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							</td>
						</tr>
					</table>
				</div>
				<div data-options="region:'center',split:false,border:false" style="width: 100%">
					<div style="height: 90%">
						<form id="saveForm" method="post"style="height: 100%;">
							<input type="hidden" name="outstoreJson" id="outstoreJson">
							<table id="infolist" data-options="fit:true,url:'',method:'post',idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,toolbar: '#tb'"></table>
						</form>
					</div>
					<div id="totalDivId" style="display: none;height: 9%">
						<table style="width: 100%">
							<tr>
								<td style="padding-left:45%;" >
									总零售金额：
									<span ID="totalStoreCost" style="font-weight: bold;color: red;"></span>
								</td>
							</tr>
						</table>
					</div>
				</div>
				<div id="tb" style="height: auto">
						<shiro:hasPermission name="YBCK:function:add">
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
		var packUnitList = null;
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

			//出库时间默认当前
			var date = new Date();
			var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
			var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
			+ (date.getMonth() + 1);
			var strDate = date.getFullYear() + '-' + month + '-' + day;
			$('#outTime').val(strDate);
			
			//添加datagrid事件及分页
			$('#list').datagrid({
				pagination : true,
				pageSize : 20,
				pageList : [ 20, 30, 50, 80, 100 ],
				onBeforeLoad:function (param) {
					$.ajax({
						url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",
						data:{"type" : "packunit"},
						type:'post',
						success: function(data) {
							packUnitList = data;
						}
					});
		        },onDblClickRow: function (rowIndex, rowData) {//双击查看
					var row = $('#list').datagrid('getSelected');	                        
                       if(row){
                    	   attrValue(row);
			   			}
					}    
			});
			//添加datagrid事件及分页
			$('#infolist').edatagrid({
				striped:true,
				checkOnSelect:true,
				selectOnCheck:true,
				singleSelect:false,
				fitColumns:false,
		   		columns:[[    
			        {field : 'ck',checkbox:true}, 
			        {field : 'id',title:'id',hidden:true},
			        {field : 'flagId',title:'flagId',hidden:true,width:'0%',formatter:function(value,row,index){
			        	return "<div id='"+row.drugCode+"'></div>";
			        }},
			        {field : 'drugCode',title:'药品编码',hidden:true},
			        {field : 'drugType',title:'药品类型',hidden:true},
			        {field : 'drugQuality',title:'药品性质',hidden:true},
			        {field : 'showUnit',title:'显示的单位',hidden:true},
			        {field : 'companyCode',title:'供货单位代码',hidden:true},
			        {field : 'purchasePrice',title:'购入价',hidden:true},
			        {field : 'wholesalePrice',title:'批发价',hidden:true},
			        {field : 'groupCode',title:'批次号',hidden:true},
			        {field : 'tradeName',title:'药品名称',hidden : true},
			        {field : 'drugCommonname',title : '药品通用名',width : '10%'},
			        {field : 'drugCnamepinyin',title : '通用拼音码',hidden : true}, 
			        {field : 'drugCnamewb',title : '通用五笔码',hidden : true}, 
			        {field : 'drugCnameinputcode',title : '通用自定义码',hidden : true},
			        {field : 'specs',title:'规格',width:'7%'},
			        {field : 'retailPrice',title:'零售价',width:'7%',formatter:function(value,row,index){
			        	return "<div id='retailPrice_"+index+"'>"+value+"</div>";
			        }},
			        {field : 'packUnit',title:'包装单位',width:'7%',formatter: packUnitFamater},    
			        {field : 'storeSum',title:'库存数量',width:'7%'},    
			        {field : 'outNum',title:'出库数量',width:'7%',formatter:function(value,row,index){
			        	var retVal = "";
						if (value != null && value != "" && !isNaN(value) && value >0) {
							retVal = value;
						}else{
							retVal = '';
						}
			        	return "<input type='text' id='"+row.id+"_"+index+"' value='"+retVal+"' onChange = 'upperCase(this)'>";
			        }},     
			        {field : 'outlCost',title:'出库金额',width:'7%',formatter:function(value,row,index){
			        	if (value != null && value != "" && !isNaN(value) && value >0) {
							return value;
						}else{
							return '';
						}
			        	}},
			        {field : 'remark',title:'备注',width:'7%',formatter:function(value,row,index){
			        	var retVal = "";
			        	if(value!=null&&value!=""){
			        		retVal = value;
			        	}
			        	return "<input type='text' id='remark_"+index+"' value='"+retVal+"'>";
			        }}
				]]
			});
		});
		function submit(){
			if(check()){
				var rows = $('#infolist').edatagrid("getSelections");
				if (rows.length > 0) {//选中几行的话触发事件	
					for(var i=0; i<rows.length; i++){
						if(!isNaN(rows[i].outNum) && rows[i].outNum >= 0){
						}else{
							$.messager.alert('提示','请在第'+(i+1)+'行出库数量中输入大于0的数字');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							   return;
						}
					}
				if(checkStoreSum()){
					$('#infolist').edatagrid('acceptChanges');
					$('#outstoreJson').val(JSON.stringify( $('#infolist').edatagrid("getRows")));
					$('#saveForm').form('submit', {    
					    url:"<%=basePath%>drug/outstore/saveInOutstore.action?flag=1&outDate="+$('#outTime').val(),   
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
							}else{
								$.messager.alert('提示',"出库记录保存成功！");
								$('#list').datagrid('reload');
								$('#infolist').edatagrid('loadData', { total: 0, rows: [] });
								totalPrice();
							}
						}
					});
				}else{
					return;
				}
			}else{
					$.messager.alert("操作提示", "请选择要保存的条目！", "warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}
		}
		function check(){
			var outTime=$("#outTime").val();
			if(outTime==""){
				$.messager.alert('警告','请选择出库时间！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}
			return true;
		}
		//验证 某行信息的出库数量为0或者出库数量小于库存数量，给予用户提示“XX药出库数量不能小于0并且不能小于库存数量”
		function checkStoreSum(){
			var index = $('#infolist').edatagrid("getRows").length;
			for (var i=0;i<index;i++){
		       var dataGridRowData = $('#infolist').edatagrid('getRows')[i];
		       var outNum = dataGridRowData.outNum;
		       var storeSum = dataGridRowData.storeSum;
		       if(outNum==0 || parseFloat(outNum) > parseFloat(storeSum)){
		    	   $.messager.alert('提示',"待出库列表第"+(parseInt(i)+1)+"条记录出库数量为0了或者出库数量大于库存数量了");
		    	   setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
		    	   return false;
		       }
			}
			return true;
		}
		//移除待出库列表
		function delList() {
				var rows = $('#infolist').edatagrid("getChecked");
				if (rows.length > 0) {
					$.messager.confirm('确认', '确定要删除选中信息吗?', function(res) {//提示是否删除
						if (res) {
							var arr = [];
							for ( var i = 0; i < rows.length; i++) {
								var dd = $('#infolist').edatagrid('getRowIndex',
										rows[i]);//获得行索引
								var id = rows[i].id;
								if (arrIds.indexOf(id) != -1) {
									arrIds.remove(id);
								}
								arr.push(dd);
							}
							for ( var i = arr.length - 1; i >= 0; i--) {
								$('#infolist').edatagrid('deleteRow', arr[i]);//通过索引删除该行
							}
							totalPrice();
						} else {
							$.messager.alert("操作提示", "请选择要删除的条目！", "warning");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}
					});
				}
			}
		function upperCase(obj){
			var rowsInfolist = $('#infolist').edatagrid('getRows');
			var id = $(obj).prop("id").split("_");
			var index = parseInt(id[1]);
			var storeSum = rowsInfolist[index].storeSum;
			var drugCommonname = rowsInfolist[index].drugCommonname;
			var remark = document.getElementById('remark_' + index).value;
			var val = $(obj).val();
			if(storeSum < val){
				$.messager.alert("操作提示", "药品 "+drugCommonname+" 出库数量大于申请出库数量，请核对后在进行申请", "error");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				var index = $('#infolist').edatagrid('updateRow', {
					index : index,
					row : {
						outNum : storeSum,
						outlCost : (rowsInfolist[index].retailPrice * rowsInfolist[index].storeSum).toFixed(4),
						remark : remark
					}
				}).edatagrid('getRows').length - 1;
				//更新一下总金额
				totalPrice();
				return false;
			}
			var index = $('#infolist').edatagrid('updateRow',{
				index: index,
				row: {
					outNum : val,
					outlCost : (rowsInfolist[index].retailPrice * val).toFixed(4),
					remark : remark
				}
			}).edatagrid('getRows').length - 1;
			//计算总金额
			totalPrice();
		}
		//判断是否已经填充到待出库列表
		function attrValue(row){
			var rows = $('#infolist').edatagrid("getRows");
			var id = row.id;
			if(rows.length>0){
				if(arrIds.indexOf(id) == -1){
					conform(row);
					arrIds.push(id);
				}else{
					$.messager.alert("操作提示", "待出库列表已有此条记录！","warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}else{
				conform(row);
				if(arrIds.indexOf(id)==-1){
					arrIds.push(id);
				}
			}
		}
		//确认把数据填充到待出库列表
		function conform(row){
			var index =$('#infolist').edatagrid('appendRow',{
				id : row.id,
				drugCode : row.drugId,
				tradeName : row.drugName,
				drugCommonname : row.drugCommonname,
				drugCnamepinyin : row.drugCnamepinyin,
				drugCnamewb : row.drugCnamewb,
				drugCnameinputcode : row.drugCnameinputcode,
				specs : row.drugSpec,
				retailPrice :row.retailPrice,
				packUnit : row.drugPackagingunit,
				outNum : row.storeSumDrug,
				storeSum : row.storeSumDrug,
				outlCost : (row.storeSumDrug * row.retailPrice).toFixed(4),
				drugType : row.drugType,
		        drugQuality : row.drugQuality,
		        showUnit : row.showUnit,
		        companyCode : row.companyCode,
		        purchasePrice : row.purchasePrice,
		        wholesalePrice : row.wholesalePrice,
		        groupCode : row.groupCode,
		        remark : ""
			}).edatagrid('getRows').length-1;
         	$('#infolist').edatagrid('beginEdit',index);
         	var rows = $('#infolist').edatagrid("getRows");
			totalRows = cloneObject(rows);
			//计算总金额
			totalPrice();
			$("#totalDivId").show();
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
			var rows = $('#infolist').edatagrid('getRows');
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
			var queryName = $.trim($("#tradNameSerc").textbox('getValue'));
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
			var drugName = $.trim($('#queryName').textbox('getValue'));
			var drugType = $.trim($('#CodeDrugtype').combobox('getValue'));
			$('#list').datagrid('load', {
		    	drugNameweb: drugName,
		    	drugTypeSerc:drugType
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
			        	showWin("请药品类别","<%=basePath%>ComboxOut.action?xml="+"CodeDrugtype,0","50%","80%");
			        }
			    }
		    }
		} 
		//从xml文件中解析，读到下拉框
		function idCombobox(param){
			$('#CodeDrugtype').combobox({
			    url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type="+param,   
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
		//显示包装单位格式化，最小单位单位格式化 
		function packUnitFamater(value){
			if(value!=null){
				for(var i=0;i<packUnitList.length;i++){
					if(value==packUnitList[i].encode){
						return packUnitList[i].name;
					}
				}	
			}
		}
	</script>
</body>
</html>