<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div id="divLayout" class="easyui-layout" fit=true>
		<div data-options="region:'north',split:false" style="padding: 5px;">
			<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td style="padding: 5px 15px;" nowrap="nowrap">
						出库日期：
					</td>
					<td>
						<input type="text" ID="outTime" class="easyui-datebox" data-options="showSeconds:false"/>
					</td>
					<td style="padding: 5px 15px;" nowrap="nowrap">
						申请单：
					</td>
					<td>
						<input class="inputCss" id="outApplyBill" name="applyNumber" value="" placeHolder="空格选择出库申请单"/>
					</td>
				</tr>
			</table>
		</div>
		<div class="easyui-panel" data-options="region:'center',split:false,title:'药品列表',iconCls:'icon-book'" style="padding: 5px; width: 50%;">
			<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td style="padding: 2px 15px;">
						药品类型：<input type="text" id="CodeDrugtype"/>
					</td>
					<td style="padding: 2px 15px;">
						查询条件：
						<input class="easyui-textbox" id="queryName" onkeydown="keyDown()" data-options="prompt:'通用名,拼音,五笔,自定义'" style="width:180px" />
					</td>
					<td>
						&nbsp;&nbsp;
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</td>
				</tr>
			</table>
			<table id="list" data-options="url:'${pageContext.request.contextPath}/drug/outstore/queryAduitOutstore.action?flag=3',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
				<thead>
					<tr>
						<th data-options="field:'id',hidden:true">
							id
						</th>
						<th data-options="field:'outBillCode',width : '8%'">
							出库单号
						</th>
						<th data-options="field:'drugCode',hidden:true">
							药品编码
						</th>
						<th data-options="field:'drugCommonname',width : '12%'">
							药品通用名
						</th>
						<th data-options="field:'drugSpec',width : '5%'">
							药品规格
						</th>
						<th id ="CodeMinimumunit" data-options="field:'drugMinimumunit',formatter: packUnitFamater,width : '5%'">
							单位
						</th>
						<th data-options="field:'drugSpec',width : '5%'">
							规格
						</th>
						<th data-options="field:'applyNum',width : '10%'">
							申请出库数量
						</th>
						<th data-options="field:'producerCode',hidden:true">
							厂家id
						</th>
						<th data-options="field:'purchasePrice',width : '10%'">
							进价
						</th>
						<th data-options="field:'drugRetailprice',width : '10%'">
							零售价
						</th>
						<th data-options="field:'placeCode',width : '10%'">
							货位号
						</th>
						<th data-options="field:'drugPackagingunit',hidden:true" ></th>
					</tr>
				</thead>
			</table>
		</div>
		<div class="easyui-panel" data-options="region:'south',split:false,title:'待出库药品检索',iconCls:'icon-book'"style="padding: 10px;height:50%;">
			<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td style="padding: 2px 15px;" nowrap="nowrap">
						查询条件：
						<input class="easyui-textbox" id="tradNameSerc" data-options="prompt:'通用名,拼音,五笔,自定义'" style="width:180px"/>
					</td>
					<td>
						&nbsp;&nbsp;
						<a href="javascript:void(0)" onclick="searchList()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</td>
				</tr>
			</table>
			<form id="saveForm" method="post">
				<input type="hidden" name="outstoreJson" id="outstoreJson">
				<table id="infolist" data-options="url:'',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,toolbar: '#tb'"></table>
			</form>
			<div id="tb" style="height: auto">
				<shiro:hasPermission name="YBCK:function:add">
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="submit()">保存</a>
				</shiro:hasPermission>
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="delList()">删除</a>
			</div>
			<div id="totalDivId" style="display: none;">
				<table>
					<tr>	
						<td style="padding: 2px 15px;">
							<font style="padding-left:400px;">总零售金额：</font>
							<span ID="totalStoreCost" style="font-weight: bold;color: red;"></span>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div id="selectApplyOut"></div>
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
			idCombobox("CodeDrugtype");
			//下拉框的keydown事件   调用弹出窗口
			var drugType = $('#CodeDrugtype').combobox('textbox'); 
			drugType.keyup(function(){
				KeyDown1(0,"CodeDrugtype");
			});
			$('#list').datagrid({
				onBeforeLoad:function (param) {
					$.ajax({
						url: "<c:url value='/comboBox.action'/>",
						data:{"str" : "CodeDrugpackagingunit"},
						type:'post',
						success: function(packUnitdata) {
							packUnitList = packUnitdata;
						}
					});
		        },
				onDblClickRow: function (rowIndex, rowData) {
					var row = $('#list').datagrid('getSelected');	                        
                    if(row){
                   	   attrValue(row);
		   			}
				}    
			});
			$('#infolist').datagrid({
				striped:true,
				checkOnSelect:true,
				selectOnCheck:true,
				singleSelect:false,
				fitColumns:false,
				rownumbers:true,
		   		columns:[[    
			        {field:'ck',checkbox:true}, 
			        {field:'flagId',title:'flagId',hidden:true,width:'0%',formatter:function(value,row,index){
			        	return "<div id='"+row.drugCode+"'></div>";
			        }},
			        {field:'id',title:'出库记录或是出库申请Id',hidden:true},
			        {field:'differentId',title:'区分是出库记录Id还是出库申请Id',hidden:true},
			        {field:'drugCode',title:'药品编码',hidden:true},
			        {field:'drugType',title:'药品类型',hidden:true},
			        {field:'drugQuality',title:'药品性质',hidden:true},
			        {field:'showUnit',title:'显示的单位',hidden:true},
			        {field:'companyCode',title:'供货单位代码',hidden:true},
			        {field:'purchasePrice',title:'购入价',hidden:true},
			        {field:'wholesalePrice',title:'批发价',hidden:true},
			        {field:'groupCode',title:'批次号',hidden:true},
			        {field:'tradeName',title:'药品名称',hidden : true},
			        {field:'drugCommonname',title : '药品通用名',width : '10%'},
			        {field:'drugCnamepinyin',title : '通用拼音码',hidden : true}, 
			        {field:'drugCnamewb',title : '通用五笔码',hidden : true}, 
			        {field:'drugCnameinputcode',title : '通用自定义码',hidden : true},
			        {field:'specs',title:'规格',width:'7%'},
			        {field:'retailPrice',title:'零售价',width:'7%'},
			        {field:'packUnit',title:'包装单位',width:'7%',formatter: packUnitFamater},    
			        {field:'storeSum',title:'库存数量',width:'7%'},    
			        {field:'outNum',title:'出库数量',width:'7%'},     
			        {field:'outlCost',title:'出库金额',width:'7%'},
			        {field:'remark',title:'备注',width:'7%'}
				]]
			});
			bindBlackEvent("outApplyBill",keyUpApplyOut);
		});
		//确认把数据填充到待出库列表
		function conform(row){
			var index =$('#infolist').datagrid('appendRow',{
				id:row.id,
				differentId:1,
				drugCode: row.drugId,
				tradeName: row.drugName,
				drugCommonname : row.drugCommonname,
				drugCnamepinyin : row.drugCnamepinyin,
				drugCnamewb : row.drugCnamewb,
				drugCnameinputcode : row.drugCnameinputcode,
				specs: row.drugSpec,
				retailPrice:row.retailPrice,
				packUnit:row.drugPackagingunit,
				outNum:row.storeSumDrug,
				storeSum:row.storeSumDrug,
				outlCost:(row.storeSumDrug*row.retailPrice).toFixed(2),
				drugType:row.drugType,
		        drugQuality:row.drugQuality,
		        showUnit:row.showUnit,
		        companyCode:row.companyCode,
		        purchasePrice:row.purchasePrice,
		        wholesalePrice:row.wholesalePrice,
		        groupCode:row.groupCode
			}).edatagrid('getRows').length-1;
         			$('#infolist').edatagrid('beginEdit',index);
         	var rows = $('#infolist').datagrid("getRows");
			totalRows = cloneObject(rows);
			//计算总金额
			totalPrice();
			$("#totalDivId").show();
		}
		function submit(){
			if(check()){
				$('#infolist').datagrid('acceptChanges');
				$('#outstoreJson').val(JSON.stringify( $('#infolist').datagrid("getRows")));
				$('#saveForm').form('submit', {    
				    url:"<c:url value='/drug/outstore/saveOutstoreDepartment.action'/>?outDate="+$("#outTime").datebox('getValue'),   
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
						  if(data=="success"){
							  $.messager.alert("提示", "出库记录保存成功！");
							  $('#list').datagrid('reload');
							  $('#infolist').datagrid('loadData', { total: 0, rows: [] });
						  }		  
				    }
				});
			}else{
				$.messager.alert("警告", "请选择出库时间！");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		}
		function check(){
			var outTime=$("#outTime").datebox('getValue');
			if(outTime==""){
				return false;
			}
			return true;
		}
		//移除待出库列表
		function delList(){
			var rows = $('#infolist').datagrid("getChecked");
			if(rows.length>0){
				var arr=[];
				for(var i=0; i<rows.length; i++){
					var dd = $('#infolist').edatagrid('getRowIndex',rows[i]);//获得行索引
					if(arrIds.indexOf(rows[i].id)!=-1){
						arrIds.remove(rows[i].id);
					}
					arr.push(dd);
				}
				for(var i=arr.length-1;i>=0;i--){
					$('#infolist').edatagrid('deleteRow',arr[i]);//通过索引删除该行
				}
			}else{
				$.messager.alert("操作提示", "请选择要删除的条目！","warning");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		}
		//判断是否已经填充到待出库列表
		function attrValue(row){
			var rows = $('#infolist').datagrid("getRows");
			var tag=row.drugId+"_"+row.groupCode;
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
		function keyUpApplyOut() {  
			Adddilog("选择申请单", "<c:url value='/drug/outstore/getMedicalApply.action'/>",'selectApplyOut');
   	 	}
		// 药品列表查询
		function searchFrom() {
			var drugName = $('#queryName').textbox('getValue');
			var drugType = $('#CodeDrugtype').combobox('getValue');
			$('#list').datagrid('load', {
		    	drugName: drugName,
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
			        	showWin("请药品类别","<c:url value='/ComboxOut.action'/>?xml="+"CodeDrugtype,0","50%","80%");
			        }
			    }
		    }
		} 
		//从xml文件中解析，读到下拉框
		function idCombobox(param){
			$('#'+param).combobox({
			    url:"<c:url value='/comboBox.action'/>?str="+param,       
			    valueField:'id',    
			    textField:'name',
			    multiple:false,
			    onLoadSuccess: function() {//请求成功后
					$(list).each(function(){
						  if ($('#'+param).val() == this.Id) {
							  $('#'+param).combobox("select",this.Id);
						   }
					});
				}
			});
		}
		//显示包装单位格式化，最小单位单位格式化 
		function packUnitFamater(value){
			if(value!=null){
				for(var i=0;i<packUnitList.length;i++){
					if(value==packUnitList[i].id){
						return packUnitList[i].name;
					}
				}	
			}
		}
		//加载dialog
		function Adddilog(title, url,id) {
			$('#'+id).dialog({    
			    title: title,    
			    width: '60%',    
			    height:'60%',    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true 
			   });  
		}
		//打开dialog
		function openDialog(id) {
			$('#'+id).dialog('open'); 
		}
		//关闭dialog
		function closeDialog(id) {
			$('#'+id).dialog('close');  
		}
	</script>
</body>
</html>