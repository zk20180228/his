<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<body>
	<div class="easyui-layout" style="width:100%;height:100%;"fit=true>
		<div  data-options="region:'west'" title="药品与非药品选择列表" style="width:20%;padding:10px">
			<input type="hidden" id="hd" value="1">
			<table cellspacing="0" cellpadding="0" border="0" id="searchTab">
				<tr>
					<td style="padding: 5px 15px;">名称：</td>
						<td>
							<input id="name"  class="easyui-textbox"/>
						</td>
					<td>
						<shiro:hasPermission name="YZFJCLZD:function:query">
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						</shiro:hasPermission>
					</td>
				</tr>
			</table>
			<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td align="center">
						<shiro:hasPermission name="YZFJCLZD:function:query">
						<a href="javascript:void(0)" onclick="query(1)" class="easyui-linkbutton" iconCls="icon-search">药品</a>&nbsp;&nbsp;
						<a href="javascript:void(0)" onclick="query(2)" class="easyui-linkbutton" iconCls="icon-search">非药品</a>
						</shiro:hasPermission>
					</td>
				</tr>
			</table>
			<table id="list" data-options="idField:'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
			</table>
		</div>
		<div data-options="region:'center'" >
			<div id="divLayout" class="easyui-layout" fit=true >
		        <div data-options="region:'north',split:false" style="padding:5px;min-height:70%;height:auto;">	        
					<table id="list1" style="width: 100%" data-options="method:'post',idField:'id',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
						<thead>
							<tr>
								<th data-options="field:'undrugName',width:100">名称</th>
								<th data-options="field:'undrugDept',width:100,formatter:functiondeptid ">执行科室</th>
								<th data-options="field:'undrugSpec',width:100">规格</th>
								<th data-options="field:'undrugDefaultprice',width:100">单价</th>
								<th data-options="field:'undrugUnit',width:100">单位</th>
								<th data-options="field:'undrugInputcode',width:100">自定义码</th>
								<th data-options="field:'undrugSpecialtyname',width:100">专科名称</th>
								<th data-options="field:'undrugDiseaseclassification',width:100,formatter: functionDis">疾病分类</th>
								<th data-options="field:'undrugMedicalhistory',width:100">病史检查</th>
								<th data-options="field:'undrugNotes',width:100">注意事项</th>
								<th data-options="field:'undrugEquipmentno',width:100">设备编号</th>
								<th data-options="field:'undrugInspectionsite',width:100">检查部位及标本</th>
								<th data-options="field:'undrugRemark',width:100">备注</th>
							</tr>
						</thead>
					</table>
				</div>
				<div data-options="region:'center'" style="height:auto;">
					<form id="editForm" fit=true>
						<input type="hidden" id="oddGradeJson" name="oddGradeJson">
						<input type="hidden" id="infoId" name="infoId">
						<input type="hidden" id="codeId" name="zt">
						<table id="list2" style="width: 100%" data-options="singleSelect:'true',toolbar:'#toolbarId' " >
							<thead>
								<tr>
									<th data-options="field:'itemCode',width:100">项目名</th>
									<th data-options="field:'qty',formatter:funNum,width:100">数量</th>
									<th data-options="field:'undrugInputcode',width:100">单位</th>
									<th data-options="field:'price',width:100,formatter:funPrice">单价</th>
									<th data-options="field:'zongPrice',width:100,formatter:funPayCost">总价格</th>
									<th data-options="field:'useInterval',width:100">间隔</th>
								</tr>
							</thead>
						</table>
					</form>
				</div>
			</div>
		</div>
	</div>
	<div id="toolbarId">
		<shiro:hasPermission name="YZFJCLZD:function:add">
		<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">保存</a>	
		</shiro:hasPermission>
	</div>
<script type="text/javascript">
	var deptidlist="";
	var disList="";
		$(function(){
			$('#list').datagrid({    
			    url:'<%=basePath%>baseinfo/odditionalitem/queryInfoList.action',
			    pagination:true,
				pageSize:20,
				pageList:[20,30,50,80,100], 
			    columns:[[    
			        {field:'name',title:'药品',width:309},    
			    ]]    
			});  
			$('#list1').datagrid({    
			    pagination:true,
				pageSize:10,
				pageList:[10,30,50,80,100],
				url:'<%=basePath%>baseinfo/odditionalitem/queryUndrugList.action';
				onBeforeLoad: function (param) {//加载数据
					$.ajax({
						url: '<%=basePath%>baseinfo/department/queryDepartments.action',
						type:'post',
						success: function(datas) {
							deptidlist = eval("("+datas+")");
						}
					});	
					$.ajax({
						url: '<%=basePath%>likeDiseasetype.action',
						type:'post',
						success: function(disData) {
							disList = eval("("+disData+")");
						}
					});	
		        }
				,
				onDblClickRow: function (rowIndex, rowData) {//双击查看
					var index = $('#list2').edatagrid('appendRow',{
							itemCode: rowData.undrugName,//名称
							qty: 1,//数量
							undrugInputcode:rowData.undrugUnit,//单位
							price:rowData.undrugDefaultprice,//单价
							zongPrice:parseFloat(parseFloat(1)*parseFloat(rowData.undrugDefaultprice).toFixed(2)).toFixed(2),//总价格
							useInterval:''//间隔
						}).edatagrid('getRows').length-1;
						$('#list2').edatagrid('beginEdit',index);
				}    
			});  
		});
		
		function query(flay){
			if(flay=="1"){
				$('#hd').val(1);
				$('#list').datagrid({    
				    url:'<%=basePath%>baseinfo/odditionalitem/queryInfoList.action',
				    pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],    
				    columns:[[    
				        {field:'name',title:'药品',width:309},    
				    ]]    
				}); 
			}else if(flay=="2"){
				$('#hd').val(2);
				$('#list').datagrid({    
				    url:'<%=basePath%>baseinfo/odditionalitem/queryUndrugList.action', 
				    pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],   
				    columns:[[    
				        {field:'undrugName',title:'非药品',width:309},    
				    ]]    
				}); 
			}
		}
		function searchFrom(){
   		    var name =	$('#name').textbox('getValue');
   		    var zt = $('#hd').val();
   		    if(zt=="1"){
   		    	$('#list').datagrid({    
				    url:'<%=basePath%>baseinfo/odditionalitem/queryInfoList.action?name='+name,
				    pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],  
				    columns:[[    
				        {field:'name',title:'药品',width:309},    
				    ]]    
				}); 
   		    }else if(zt=="2"){
   		    	$('#list').datagrid({    
				    url:'<%=basePath%>baseinfo/odditionalitem/queryUndrugList.action?name='+name,
				    pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],   
				    columns:[[    
				        {field:'undrugName',title:'非药品',width:309},    
				    ]]    
				}); 
   		    }
		}
		$('#searchTab').find('input').on('keyup', function(event) {
				if (event.keyCode == 13) {
					searchFrom();
				}
			});
		
		//为数量添加keyup事件
		function funNum(value,row,index){
			return '<input id="num_'+index+'" type="text" class="datagrid-editable-input numberbox-f textbox-f" value="'+value+'" data-options="min:0,precision:0" onkeyup="keyupNum(this)" style="width:100%"></input>';
		}
		//为金额添加id
		function funPayCost(value,row,index){
			return '<div id="zongPrice_'+index+'">'+value+'</div>';
		}
		
		//为单价添加id
		function funPrice(value,row,index){
			return '<div id="price_'+index+'">'+value+'</div>';
		}
		//为数量添加keyup事件
		function keyupNum(inpObj){
			if($(inpObj).val().length==1){
				$(inpObj).val($(inpObj).val().replace(/[^1-9]/g,''));
			}else{
				$(inpObj).val($(inpObj).val().replace(/\D/g,''));
			}
			var val = $(inpObj).val();
			var id = $(inpObj).prop("id").split("_");
			var price = $('#price_'+id[1]).text();
			var money = parseFloat(0.00).toFixed(2);
			if(val!=null&&val!=""){
				money = parseFloat(parseFloat(val)*parseFloat(price).toFixed(2)).toFixed(2);
			}
			$('#list2').datagrid('updateRow',{
				index: id[1],
				row: {
					qty:val,
					zongPrice:money
				}
			});
			$('#num_'+id[1]).val("").focus().val(val);
		}
		function edit(){
			var zt = $('#hd').val();
				$('#codeId').val(zt);
				var infoId = getIdUtil('#list');
				if(infoId!=null){
					$('#infoId').val(infoId);
					$('#list2').edatagrid('acceptChanges');
					$('#oddGradeJson').val(JSON.stringify( $('#list2').edatagrid("getRows")));
					$('#editForm').form('submit',{ 
				        url:'<%=basePath%>baseinfo/odditionalitem/saveOdditionalitem.action',  
				        onSubmit:function(){ 
				            return $(this).form('validate');  
				        },  
				        success:function(data){  
				        	 window.location.href='<%=basePath%>baseinfo/odditionalitem/listOdditionalitemd.action';
				        },
						error : function(data) {
							$.messager.alert('提示信息','保存失败！');
						}			         
					}); 
				}
		}
		//科室id显示name
		function functiondeptid(value,row,index){
			for(var i=0;i<deptidlist.length;i++){
				if(value==deptidlist[i].id){
					return deptidlist[i].deptName;
				}
			}
		}	
		//疾病分类
		function functionDis(value,row,index){
			for(var i=0;i<disList.length;i++){
				if(value==disList[i].id){
					return disList[i].name;
				}
			}
		}
</script>
</body>