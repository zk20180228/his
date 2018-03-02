<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
	<body>
		<div class="easyui-layout" class="easyui-layout"  style="width: 100%; height: 100%;">
			<div id="treeFlag" data-options="region:'west',split:true"  style="width: 15%;">
					<ul id="tDt"></ul>
			</div>
			<div data-options="region:'center'">
				<div id="divLayout" class="easyui-layout" fit=true style="width: 100%; height: 100%;">
					<div data-options="region:'north',border:false" style="height: 40px">
						<form id="search" method="post">
							<table style="width: 100%;  padding: 5px;">
								<tr>
									<td style="width:240px;">名称：<input class="easyui-textbox" id="drugName" name="drugName"  /></td>
									<td>
											<a href="javascript:void(0)" onclick="searchFromName()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
											<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
									</td>
								</tr>
							</table>
						</form>
					</div>
					<div data-options="region:'center'" id="stackDiv" style=" width: 100%;border-left:0">
						<table id="histroyList" fit="true" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">	
							<thead>
								<tr>
									<th data-options="field:'tradeName',rowspan:2">药品</th> 
									<th data-options="field:'specs',rowspan:2">规格</th> 
									<th data-options="field:'drugStartdate',rowspan:2,formatter: formatDatebox" >生产日期</th> 
									<th data-options="field:'producer',rowspan:2,formatter:manufacturerFamater" >生产厂家</th>
									<th data-options="field:'adjustReason',rowspan:2,formatter:adjustReasonFamater">调价依据</th>  
									<th data-options="field:'ls',colspan:3,align:'center'">零售价</th>
									<th data-options="field:'pf',colspan:3,align:'center'">批发价</th> 
								</tr>
								<tr> 
									<th data-options="field:'preRetailPrice'">原价</th> 
									<th data-options="field:'retailPrice'">新价</th> 
									<th data-options="field:'balanceRePriceLs',formatter:balanceRePriceLsFamater">差价</th> 
									<th data-options="field:'preWholesalePrice'">原价</th> 
									<th data-options="field:'wholesalePrice'">新价</th> 
									<th data-options="field:'balanceRePricePf',formatter:balanceRePricePfFamater">差价</th> 
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		 </div>
		<script type="text/javascript">
			var adjustMode=0;
			var manufacturer = self.parent.manufacturer;
			var adjustReason = '';
			//加载页面
			$(function(){
				var manufacturer = self.parent.manufacturer;
				$('#histroyList').datagrid({
					pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],
					url:"<c:url value='/drug/adjustPriceInfo/findByAdjustCode.action'/>",
					onBeforeLoad : function(){
						var name = $("#drugName").textbox('getValue');
						var node = $('#tDt').tree('getSelected');
						if (node == null || node == '' || node.attributes.isNo != 1) {
							if(name == null ||  name == ''){
								return false;
							}
						}
					}
				});	
				bindEnterEvent('drugName', searchFromName, 'easyui');
				$.ajax({
					url: '<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=adjustReason',
					success: function(data) {
						adjustReason = data;
					}
				});
			});
			//取差值方法
			function calculate(firstValue,secValue){
				var balanceRePrice = '';
				if(firstValue != null && secValue != null){
					balanceRePrice = (secValue - firstValue).toFixed(2);
				}
				return balanceRePrice;
			}
			//加载部门树
			$('#tDt').tree({
				url :"<c:url value='/drug/adjustPriceInfo/adjustPriceTree.action'/>",
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
				},
				onClick : function(node) {
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
					var node = $('#tDt').tree('getSelected');
					if (node) {
						if(node.attributes.isNo=='1'){
							$('#histroyList').datagrid('load', {
								id:node.id
							});
						}
					}
				}
			});
			//查询
			function searchFromName() {
				var name = $.trim($('#drugName').textbox('getValue'));
				var node = $('#tDt').tree('getSelected');
				var id = '';
				if (node) {
					if(node.attributes.isNo=='1'){
						id = node.id;
					}
				}
				$('#histroyList').datagrid('load', {
					name : name,
					id : id
				});
			}
			//树操作
			function refresh() {//刷新树
				$('#tDt').tree('options').url = "<c:url value='/drug/adjustPriceInfo/adjustPriceTree.action'/>",
				$('#tDt').tree('reload');
			}
			function expandAll() {//展开树
				$('#tDt').tree('expandAll');
			}
			function collapseAll() {//关闭树
				$('#tDt').tree('collapseAll');
			}
			function searchTree() {//查询树
				$.ajax({
					url : "<c:url value='/searchTree.action'/>?searcht="+ encodeURI(encodeURI($('#searchTree').val())),
					type : 'post',
					success : function(data) {
						$('#tDt').tree('collapseAll');//单独展开一个节点,先收缩树
						var node = $('#tDt').tree('find', data);
						$('#tDt').tree('expandTo', node.target).tree('select',
								node.target); //展开指定id的节点
						$("#list").datagrid("uncheckAll");
						$('#list').datagrid('load', {
							deptId : node.id
						});
					}
				});
			}
			function KeyDown() {
				if (event.keyCode == 13) {
					event.returnValue = false;
					event.cancel = true;
					searchFrom();
				}
			}
			function KeyDownTree() {
				if (event.keyCode == 13) {
					event.returnValue = false;
					event.cancel = true;
					searchTree();
				}
			}
			  //datagrid中datebox组件的时间格式化
			Date.prototype.format = function (format) {  
				var o = {  
					"M+": this.getMonth() + 1, // month  
					"d+": this.getDate(), // day  
					"h+": this.getHours(), // hour  
					"m+": this.getMinutes(), // minute  
					"s+": this.getSeconds(), // second  
					"q+": Math.floor((this.getMonth() + 3) / 3), // quarter  
					"S": this.getMilliseconds()  
					// millisecond  
				};  
				if (/(y+)/.test(format))  
					format = format.replace(RegExp.$1, (this.getFullYear() + "")  
						.substr(4 - RegExp.$1.length));  
				for (var k in o)  
					if (new RegExp("(" + k + ")").test(format))  
						format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));  
				return format;  
			};  
			function formatDatebox(value) {  
				if (value == null || value == '') {  
					return '';  
				}  
				var dt;  
				if (value instanceof Date) {  
					dt = value;  
				} else {  
					dt = new Date(value);  
				}  
			  
				return dt.format("yyyy-MM-dd"); //扩展的Date的format方法(上述插件实现)  
			}
			function manufacturerFamater(value,row,index){
				if(value!=null&&value!=''){
					return manufacturer[value];
				}
			}
			function adjustReasonFamater(value,row,index){
				for(var i = 0; i < adjustReason.length; i++){
					if(value == adjustReason[i].encode){
						return adjustReason[i].name;
					}
				}  
			}
			function balanceRePriceLsFamater(value,row,index){
				return calculate(row.preRetailPrice, row.retailPrice);
			}
			function balanceRePricePfFamater(value,row,index){
				return calculate(row.preWholesalePrice, row.wholesalePrice);
			}
			
		 // 药品列表查询重置
			function searchReload() {
				$('#drugName').textbox('setValue','');
				searchFromName();
			}
		</script>
	</body>
</html>