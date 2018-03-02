<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<html>
<body>
	<div id="tanchu1"  fit=true class="easyui-panel"  >
		<div class="easyui-panel" data-options="title:'信息查询',iconCls:'icon-search'">
						<table cellspacing="0" cellpadding="0" border="0">
							<tr>
								<td>&nbsp;&nbsp;&nbsp;&nbsp;
								搜索：
									<input type="text" ID="encode" name="codeEmrtype.encode" onkeydown="KeyDown()"/>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search"style="width: 70px">查询</a>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<a href="javascript:void(0)" onclick="queding()" class="easyui-linkbutton" style="width: 70px">确定</a>
								</td>
							</tr>
						</table>
					
				</div>
		
			<div class="easyui-panel"  data-options="title:'',iconCls:'icon-book'"style="padding: 5px;">
			<input type="hidden" value="${id }" id="id"></input>
			<table id="list" 
				data-options="url:'Popupwindow.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
				<thead>
					<tr>
						<th field="ck" checkbox="true"></th>
						<th data-options="field:'name'" style="width: 7%">
							名称
						</th>
						<th data-options="field:'pinyin'" style="width: 7%">
							拼音
						</th>
						<th data-options="field:'wb'" style="width: 7%">
							五笔
						</th>
						<th data-options="field:'hospital'" style="width: 7%">
							适用医院
						</th>
						<th data-options="field:'nonhospital'" style="width: 7%">
							不适用医院
						</th>
						
					</tr>
					
				</thead>
			</table>
		</div>
	</div>

	<script type="text/javascript">
	//加载页面
	var comboxid = "${comboxid}"
	$(function() {
		var id = "${id}"; //存储数据ID
		//添加datagrid事件及分页
		//datagrid初始化 
		//url为弹出数据的路径${test}为要读取的xml名字
		$('#list').datagrid({
			url:'Popupwindow.action?str='+'${test}',
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			onLoadSuccess : function(data) {//默认选中
				var rowData = data.rows;
				$.each(rowData, function(index, value) {
					if (value.id == id) {
						$("#list").datagrid("checkRow", index);
					}
				});
			}
		});
	});
		
		function KeyDown()  
				{  
				    if (event.keyCode == 32)  
				    { 
				    //event.keyCode==32   13 huiche 
				        event.returnValue=false;  
				        event.cancel = true; 
				        searchFrom();
				    }  
				    
				} 
		//查询
	   		function searchFrom(){ 
	   		    var ids =$('#encode').val();
			    $('#list').datagrid('load', {
					ids:ids
				});
			}
	
	//点击确定按钮
	function  queding(){
		if (getIdUtil("#list").length != 0) {
           	var ids = getIdUtil("#list");
           	//弹出窗口的返回父级窗口的action
			$.post('tanchuFanHuiShuJu.action?ids='+ids+'&str=${test}',function(data){
			var userName = eval("("+data+")");
			var user = userName.split(",");
			var parentd=user[0];
			var parentId=user[1];
			//下拉框赋值
			parent.$('#useMode').combobox('setValue',parentd );
			parent.$('#'+comboxid).combobox('setValue',parentd );
			parent.win.dialog('close');
			});
					}
	
	}
</script>
</body>
</html>
