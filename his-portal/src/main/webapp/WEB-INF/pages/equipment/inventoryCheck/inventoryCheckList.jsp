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
   <div id="cc" class="easyui-layout" style="width:100%;height:100%;">   
	    <div data-options="region:'north',split:true" style="height:6%;padding-top: 15px;padding-left: 10px;padding: 10px;">
	    <shiro:hasPermission name="${menuAlias}:function:closeAccount"> 
	    	<a  href="javascript:sealing();" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:false">封账</a>
	    </shiro:hasPermission> 
	    <shiro:hasPermission name="${menuAlias}:function:closeAccount">
	        <a  href="javascript:batchSealing();" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:false">批量封账</a>
	    </shiro:hasPermission>
	        <a  href="javascript:del();" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:false">删除</a>
	    <shiro:hasPermission name="${menuAlias}:function:checkAccount">
	        <a  href="javascript: inventory();" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:false">盘点单</a>
	    </shiro:hasPermission>
	    <shiro:hasPermission name="${menuAlias}:function:balance">
	        <a  id="balan" href="javascript:balance();" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:true">结存</a>
	    </shiro:hasPermission>  
	    <shiro:hasPermission name="${menuAlias}:function:unfreeze"> 
	        <a  id="deblocking" href="javascript:deblocking();" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:true">解封</a>
	    </shiro:hasPermission>
	    <shiro:hasPermission name="${menuAlias}:function:export">    
	        <a  id="derive" href="javascript:derive();" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:true">导出</a>
	    </shiro:hasPermission>    
	        <a  href="javascript:stamp();" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:false">打印</a>
	        <input id="deptId" name="deptId" value="${deptId}" type="hidden">
	    </div>   
	    <div data-options="region:'center'" style="width:50%;height: 94%">
			    <div id="fz" style="width: 100%;height: 87%">
			         <div id="fz1" style="width: 45%;height: 100%;float: left;border-right-color: black;">
			         		<div id="fzan"style="width: 100%;height: 8%;padding-left: 30px;padding-top: 10px">
				         		<input id="qu" class="easyui-textbox">  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="dim">模糊
				         		<a href="javascript:query();" class="easyui-linkbutton" data-options="iconCls:'icon-add'" >查询</a><br>
				         		库存汇总方式：<input class="easyui-combobox" id="pur">
			         		</div>
				         	<table id="listfz"  class="easyui-datagrid" style="width: 100%;height: 92%"data-options="fitColumns:true,singleSelect:true,selectOnCheck:false,checkbox:true">   
					        	<thead>
					        		<tr>
					        			<th data-options="field:'ck',checkbox:true,checked:false" style="width: 5%" ></th>
										<th data-options="field:'itemName'"style="width: 9%">物品名称</th>
										<th data-options="field:'itemCode',hidden:true"style="width: 9%">物品编码</th>
										<th data-options="field:'inNo',hidden:true"style="width: 9%">流水号</th>
										<th data-options="field:'specs'"style="width: 7%" >规格</th>
										<th data-options="field:'inPrice'" style="width: 8%">入库单价</th>
										<th data-options="field:'salePrice'" style="width: 8%">零售价格</th>
										<th data-options="field:'storeNum'" style="width: 8%">库存数量</th>
										<th data-options="field:'minUnit',formatter:funcunit" style="width: 8%">最小单位</th>
										<th data-options="field:'packUnit'" style="width: 10%">大包装单位</th>
										<th data-options="field:'packQty'" style="width: 10%">大包装数量</th>
										<th data-options="field:'highvalueBarcode'" style="width: 13%">高值耗材条形码</th>
										<th data-options="field:'batchNo'"style="width: 9%" >批次号</th>
										<th data-options="field:'storageCode'"style="width: 9%" >货位号</th>
						        	</tr>
					        	</thead>
					        </table>
			         </div>
			         <div id="fz2" style="width: 55%;height: 100%;float: right;">
			             <div id="fzan2" style="width: 100%;height: 5%;padding-top: 10px">
			                <input id="qu2" class="easyui-textbox">  <input type="checkbox" id="over">全盘
			             </div>
				    	 <table id="listfz2" class="easyui-edatagrid" style="width:100%;height: 90%" data-options="fitColumns:true,singleSelect:true,selectOnCheck:false,checkbox:true">
							<thead>
								 <tr>
									<th data-options="field:'ck',checkbox:true,checked:false"style="width: 10%" ></th>
									<th data-options="field:'itemCode',hidden:true"style="width: 10%">物品编码</th>
									<th data-options="field:'stockCode',hidden:true"style="width: 10%">库存流水号</th>
									<th data-options="field:'stockNo',hidden:true"style="width: 10%">库存序号</th>
									<th data-options="field:'storageCode',hidden:true"style="width: 10%">仓库编码</th>
									<th data-options="field:'inNum',hidden:true"style="width: 10%">购入数量</th>
									<th data-options="field:'itemName'"style="width: 10%">物品名称</th>
									<th data-options="field:'inPrice'" style="width: 10%">购入价</th>
									<th data-options="field:'salePrice'"style="width: 10%" >零售价</th>
									<th data-options="field:'specs'" style="width: 10%">规格</th>
									<th data-options="field:'packQty'"style="width: 10%" >大包装数量</th>
									<th data-options="field:'storeNum'" style="width: 10%">封账库存数量</th>
									<th data-options="field:'storeNum'" style="width: 10%">封账大包装数量</th>
									<th data-options="field:'storeCost'"style="width: 10%" >库存金额</th>
									<th data-options="field:'saleCost'" style="width: 10%">库存零售金额</th>
									 <th data-options="field:'minAmount',editor:{type:'numberbox'}" style="width: 10%">小包装盘点数量</th> 
									<th data-options="field:'minUnit',formatter:funcunit" style="width: 10%">最小单位</th>
									<th data-options="field:'fstoreNum',editor:{type:'numberbox'}" style="width: 10%">大包装盘点数量</th>
									<th data-options="field:'packUnit',formatter:funcunit" style="width: 10%">大包装单位</th>
									<th data-options="field:'pdje'" style="width: 10%">盘点金额</th>
									<th data-options="field:'pdlsje'" style="width: 10%">盘点零售金额</th>
									<th data-options="field:'profitLossNum'" style="width: 10%">盈亏数量</th>
									<th data-options="field:'inpriceLoss'" style="width: 10%">盈亏金额</th>
									<th data-options="field:'salepriceLoss'" style="width: 10%">盈亏零售金额</th>
									<th data-options="field:'memo',editor:{type:'textbox'}" style="width: 10%">备注</th>
									<th data-options="field:'operCode'"style="width: 10%" >操作员</th>
									<th data-options="field:'operDate'" style="width: 10%">操作日期</th>
									<th data-options="field:'highvalueBarcode'" style="width: 10%">高值耗材条形码</th>
								 </tr>
							</thead>
				     </table>
			     </div>
	       </div>
	       <div id="pdan" style="display: none;width: 100%;height: 100%" >
		        <div style="width:15%;height: 100%;float: left;border-color: black;border: 2px;">
		            <ul id="tt">加载中.....</ul>  <input type="hidden" id="trrId">
		        </div>   
	    		<div  style="width: 85%;height: 100%;float: right;">
	    				  <div style="width: 100%;height: 5%">
	    		          		<input id="qu3" class="easyui-textbox">  <input type="checkbox" id="over">全盘
   		          		  </div>
   		          		  <form id="saveForm" method="post">
	   		          		  <div style="width: 100%;height: 90%">
	   		          		  		<input type="hidden" name="infoJson" id="infoJson">
			    		          <table id="listpd" class="easyui-edatagrid" style="width:100%;height: 95%" data-options="fitColumns:true,singleSelect:true,selectOnCheck:false,checkbox:true">
										<thead>
											 <tr>
												<th data-options="field:'ck',checkbox:true,checked:false"style="width: 5%" ></th>
												<th data-options="field:'itemName'"style="width: 8%">物品名称</th>
												<th data-options="field:'checkdetailCode',hidden:true" style="width: 5%">明细流水号</th>
												<th data-options="field:'storageCode',hidden:true" style="width: 5%">仓库编码</th>
												<th data-options="field:'itemCode',hidden:true" style="width: 5%">物品编码</th>
												<th data-options="field:'stockCode',hidden:true" style="width: 5%">库存流水号</th>
												<th data-options="field:'checkCode',hidden:true" style="width: 5%">流水号</th>
												<th data-options="field:'stockNo',hidden:true" style="width: 5%">库存序号</th>
												<th data-options="field:'inNum',hidden:true" style="width: 5%">原始购入数量</th>
												<th data-options="field:'inPrice'" style="width: 5%">购入价</th>
												<th data-options="field:'salePrice'"style="width: 5%" >零售价</th>
												<th data-options="field:'specs'" style="width: 8%">规格</th>
												<th data-options="field:'packQty'"style="width: 5%" >大包装数量</th>
												<th data-options="field:'fstoreNum'" style="width: 5%">封账库存数量</th>
												<th data-options="field:'fstoreNum'" style="width: 5%">封账大包装数量</th>
												 <th data-options="field:'minAmount',editor:{type:'numberbox'}" style="width: 5%">小包装盘点数量</th> 
												<th data-options="field:'minUnit',formatter:funcunit" style="width: 5%">最小单位</th>
												<th data-options="field:'fstoreNum1',editor:{type:'numberbox'}" style="width: 5%">大包装盘点数量</th>
												<th data-options="field:'packUnit',formatter:funcunit" style="width: 5%">大包装单位</th>
												<th data-options="field:'pdje'" style="width: 8%">盘点金额</th>
												<th data-options="field:'pdlsje'" style="width: 8%">盘点零售金额</th>
												<th data-options="field:'profitLossNum'" style="width: 5%">盈亏数量</th>
												<th data-options="field:'inpriceLoss'" style="width: 8%">盈亏金额</th>
												<th data-options="field:'salepriceLoss'" style="width: 8%">盈亏零售金额</th>
												<th data-options="field:'memo',editor:{type:'textbox'}" style="width: 8%">备注</th>
												<th data-options="field:'operCode'"style="width: 5%" >操作员</th>
												<th data-options="field:'operDate'" style="width: 8%">操作日期</th>
												<th data-options="field:'highvalueBarcode'" style="width: 8%">高值耗材条形码</th>
											 </tr>
										</thead>
							     </table>
						     </div>
					     </form>
	    		</div>   
	       </div>
	  </div> 
	  <div id="fzlb" class="easyui-window"data-options="modal:true,closed:true,iconCls:'icon-save',collapsible:false,minimizable:false,maximizable:false" style="width: 240px;height: 500px">
		   <div id="treesort" style="width: 240px;height: 450px">
	       		<ul id="sort"></ul><input type="hidden" id="sortId">
	       </div>
	       <div id="plan" style="width: 240px;height: 40px; ">
	       <shiro:hasPermission name="${menuAlias}:function:query">
	           <input type="button" onclick="ensure()" value="确定">
	       </shiro:hasPermission>   
	           <input type="button" onclick="delEnsure()" value="取消">
	       </div>
	 </div>
	 <div id="pdd" class="easyui-window"data-options="modal:true,closed:true,iconCls:'icon-save',collapsible:false,minimizable:false,maximizable:false" style="width: 500px;height: 300px">
	      <table class="honry-table" style="width: 100%;height: 100%;">
	            <tr>
	            <td>盘点流水号：</td><td> <input class="easyui-textbox" id="pdName"></td>
	            </tr>
	          <tr><td colspan="2">
	          <shiro:hasPermission name="${menuAlias}:function:save">
	           <input type="button" onclick="pddName()" value="确定">
	           </shiro:hasPermission>
	           <input type="button" onclick="delName()" value="取消">
	           </td></tr>
	           
	      </table>
	      
	 </div>
	       
</div>  
<script type="text/javascript">
$("#pur").combobox({
	 data:[{"id":0,"text":"按照物资id汇总"},{"id":1,"text":"按照零售价汇总"},{"id":2,"text":"全部"}],    
	    valueField:'id',    
	    textField:'text',
	    required:true,    
	    editable:false
});
//批量封账
 function batchSealing() {
	 $('#balan').menubutton('disable');//结存
	 $('#deblocking').menubutton('disable');//解封
	 $('#derive').menubutton('disable');//导出
	 $('#fzlb').window('open');
	 
	 $('#sort').tree({ //分类
		    url:"<%=basePath %>material/inventoryCheck/treeMatKindinfo.action?deptId="+$("#deptId").val(),
		    method:'get',
		    animate:true,  //点在展开或折叠的时候是否显示动画效果
		    lines:true,    //是否显示树控件上的虚线
		    formatter:function(node){//统计节点总数
				var s = node.text;
				  if (node.children){
					s += '<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				}  
				return s;
			},onSelect: function(node){//点击节点
				var id = node.id;
				 $('#sortId').val(id);  //拿到树子节点的id 
				 
			}
		}); 
}
//批量封账确定
function ensure(){
	var deptId=$("#deptId").val();
	var kindCode=$('#sortId').val();
	$('#fzlb').window('close');
	var rows = $("#listfz").datagrid("getRows");//清空listfz
	 if(rows!=null){
		 for(var i=0;i<rows.length;i++){
			var index=$("#listfz").datagrid('getRowIndex',rows[i]);
			$("#listfz").datagrid('deleteRow',index);
		 }
	 }
	 var row = $("#listfz2").datagrid("getRows");//清空listfz2
	 if(rows!=null){
		 for(var i=0;i<row.length;i++){
			var index=$("#listfz2").datagrid('getRowIndex',row[i]);
			$("#listfz2").datagrid('deleteRow',index);
		 }
	 }
	$("#listfz").datagrid({//加载数据
		url:"<%=basePath %>material/inventoryCheck/querMatStockdetail.action",
		queryParams:{deptId:deptId,kindCode:kindCode},		
		method:"post",
		onDblClickRow: function(rowIndex, rowData){
			 var itemCode=rowData.itemCode;
			 var inNo=rowData.inNo;
			 $.ajax({
					url:"<%=basePath %>material/inventoryCheck/querMatCheckdetail.action",
					data:{itemCode:itemCode,deptId:deptId,inNo:inNo},
					type:"post",
					success:function(data){
						map=eval("("+data+")");
						var st=map.stock;
						if(map.Check=="error"){
							$.messager.alert('提示',"该物品已经在盘点，不能重复添加！");
						}else{
							
							<%-- url:"<%=basePath %>material/inventoryCheck/querMatStockdetailtj.action",
							queryParams:{deptId:deptId,itemCode:itemCode},		
							method:"post" --%>
							var rowfz=$("#listfz2").datagrid("getRows");
							if(rowIndex!=null&&rowIndex!=''){
								for(var m=0;m<rowfz.length;m++){
									var stockCode=rowfz[m].stockCode
									if(st[0].stockCode==stockCode){
										$.messager.alert('提示',"该物品已经在封账！");
										return;
									}
								}
								$("#listfz2").datagrid("appendRow",{
									itemCode:st[0].itemCode,
									stockCode:st[0].stockCode,
									stockNo:st[0].stockNo,
									storageCode:st[0].storageCode,
									inNum:st[0].inNum,
									itemName:st[0].itemName,
									inPrice:st[0].inPrice,
									salePrice:st[0].salePrice,
									specs:st[0].specs,
									packQty:st[0].packQty,
									storeNum:st[0].storeNum,
									storeNum:st[0].storeNum,
									storeCost:st[0].storeCost,
									minUnit:st[0].minUnit,
									cstoreNum:st[0].cstoreNum,
									packUnit:st[0].packUnit,
									memo:st[0].memo,
									operCode:st[0].operCode,
									operDate:st[0].operDate,
									highvalueBarcode:st[0].highvalueBarcode
								 }); 
							}
						}
						
					}
			 });
		 }
	});
	
}
//批量封账取消
function delEnsure(){
	$("#fzlb").window("close");
}
//封账
function sealing(){
	var rows=$("#listfz2").datagrid("getRows");
	if(rows.length>0){
			$('#balan').menubutton('disable');//结存
			 $('#deblocking').menubutton('disable');//解封
			 $('#derive').menubutton('disable');//导出
			 $("#fz").show();
			 $("#pdan").hide();
			 $('#pdd').window('open');
			 var retVal="";
			   var date=new Date();
				var year = date.getFullYear();
				var month = date.getMonth()+1;       //获取当前月份(0-11,0代表1月)
				var ndate=date.getDate();
				var hours=date.getHours()+1;       //获取当前小时数(0-23)
				var minut=date.getMinutes()+1;     //获取当前分钟数(0-59)
				retVal += year+''+month+''+ndate+''+hours+''+minut;
			 $("#pdName").textbox("setValue",retVal);
		
	}else{
		$.messager.alert('提示',"未加载要封账的项目");
	}
	
}
//封账弹窗盘点单的Name（确定）
function pddName() {
	 var checkName=$("#pdName").textbox("getValue");
	 var rowq=$("#listfz2").edatagrid("getChecked");
	 $('#pdd').window('close');
	 if(rowq!=null&&rowq!=[]&&rowq!=""){
		 var stringfz= JSON.stringify(rowq);
		 $.ajax({
			  url:"<%=basePath%>material/inventoryCheck/saveMatCheckdetail.action",
			  data:{stringfz:stringfz,checkName:checkName},
			  type:"post",
			  success:function(){
				  $.messager.alert('提示',"封账成功");
			  },error:function(){
				  $.messager.alert('提示',"封账失败");
			  }
		  });
	 }else{
		 $.messager.alert('提示',"请选择封账的项目");
	 }
	 
	  
}
//封账时Name(取消)
function delName(){
	 $('#pdd').window('close');
}
//盘点单
 function inventory(){
	 
	 $("#fz").hide();
	 $("#pdan").show();
	 $('#tt').tree({ //未盘点
		    url:"<%=basePath%>material/inventoryCheck/treeMatCheckdetail.action?deptId="+$("#deptId").val(),
		    method:'get',
		    animate:true,  //点在展开或折叠的时候是否显示动画效果
		    lines:true,    //是否显示树控件上的虚线
		    formatter:function(node){//统计节点总数
				var s = node.text;
				  if (node.children){
					s += '<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				}  
				return s;
			},onSelect: function(node){//点击节点
				var id = node.id;
				 $('#trrId').val(id);  //拿到树子节点的id 
				 matCheckdetail(); 
			}
		}); 
	 $('#balan').menubutton('enable');//结存
	 $('#deblocking').menubutton('enable');//解封
	 $('#derive').menubutton('enable');//导出
	 $("#listpd").datagrid({  
			fit:true,
			singleSelect:true,
			rownumbers:false,		
			striped:true,
			border:true,
			selectOnCheck:false,
			checkOnSelect:true,
			fitColumns:false ,
		});
 }
 //盘点信息的加载
 function matCheckdetail(){
	 var checkCode=$('#trrId').val();
	 $("#listpd").datagrid({
		url:"<%=basePath%>material/inventoryCheck/addMatCheckdetail.action",
		queryParams:{checkCode:checkCode},
		method:"post",
		onSelect: function(rowIndex, rowData){
			var onDbListRows = $('#listpd').datagrid('getRows');
			if(onDbListRows.length>0){
				for(var m=0;m<onDbListRows.length;m++){
					var indexRows = $('#listpd').datagrid('getRowIndex',onDbListRows[m]);
					$('#listpd').datagrid('endEdit',indexRows);
				}
			}
			$('#listpd').datagrid('beginEdit',rowIndex);
			var ed = $('#listpd').datagrid('getEditor', {index:rowIndex,field:'minAmount'});//小包装盘点数量
			var end=$('#listpd').datagrid('getEditor', {index:rowIndex,field:'fstoreNum1'});//大包装盘点数量
			var endm=$('#listpd').datagrid('getEditor', {index:rowIndex,field:'memo'});//备注
		    var t = $(ed.target).numberbox('getText'); //小包装盘点数量
			var tn = $(end.target).numberbox('getText');//大包装盘点数量
			var tnm = $(endm.target).textbox('getText');//备注
		    /* $(ed.target).next("span").children().first().val("").focus().val(t);//小包装盘点数量
		    $(endm.target).next("span").children().first().val("").focus().val(tnm);//备注 */
			$(end.target).next("span").children().first().val("").focus().val(tn);//大包装数量
			
			var inPrice = rowData.inPrice;//购入价
			var salePrice=rowData.salePrice;//零售价
			var packQty=rowData.packQty;//大包装数量
			$(end.target).numberbox('textbox').bind('keyup', function(event) {
				    var minAmount = $(ed.target).numberbox('getText');//小包装盘点数量
					var fstoreNum1 = $(end.target).numberbox('getText');//大包装盘点数量
					var memo=$(endm.target).textbox('getText');//备注
					var pdje= (inPrice*fstoreNum1).toFixed(2);//盘点金额
					var pdlsje= (salePrice*fstoreNum1).toFixed(2);//盘点零售金额
					var profitLossNum=packQty-fstoreNum1;//盈亏数量
		  	  	    /* var selfCost=(moneyMount-privilegeCost-pubCost).toFixed(2);//自付 */
		  	  	    var inpriceLoss=(profitLossNum*inPrice).toFixed(2);//盈亏金额
		  	  	    var salepriceLoss=(salePrice*profitLossNum).toFixed(2);//盈亏零售金额
					$('#listpd').datagrid('updateRow',{
						index: rowIndex,
						row: {
							minAmount:minAmount,
							fstoreNum1:fstoreNum1,
							pdje:pdje,
							pdlsje:pdlsje,
							profitLossNum:profitLossNum,
							inpriceLoss:inpriceLoss,
							salepriceLoss:salepriceLoss,
							memo:memo
						}
					});
					$('#listpd').datagrid('selectRow',rowIndex);
				});
		 }
	 });
 }
 //结存
 function balance(){
	 var rowq=$("#listpd").edatagrid("getChecked");
	 if(rowq!=null&&rowq!=[]&&rowq!=""){
		 $.messager.confirm('提示','结存后将更新库存，是否确认进行结存？',function(r){    
			    if (r){    
			    	 var stringfz= JSON.stringify(rowq);
					 $.ajax({
						  url:"<%=basePath%>material/inventoryCheck/matCheckdetailBalance.action",
						  data:{stringfz:stringfz,zd:1},
						  type:"post",
						  success:function(){
							  $.messager.alert('提示',"结存成功");
						  },error:function(){
							  $.messager.alert('提示',"结存失败");
						  }
					  });  
			    }    
			});  
		
	 }else{
		 $.messager.alert('提示',"请选择要结存的项目");
	 }
	 
 }
//解封
 function deblocking(){
	 var rowq=$("#listpd").edatagrid("getChecked");
	 if(rowq!=null&&rowq!=[]&&rowq!=""){
		 var stringfz= JSON.stringify(rowq);
		 $.ajax({
			  url:"<%=basePath%>material/inventoryCheck/matCheckdetailBalance.action",
			  data:{stringfz:stringfz,zd:2},
			  type:"post",
			  success:function(){
				  $.messager.alert('提示',"解封成功");
			  },error:function(){
				  $.messager.alert('提示',"解封失败");
			  }
		  });
	 }else{
		 $.messager.alert('提示',"请选择要解封的项目");
	 }
	 
 }
 //加载库存信息
 var unitMap="";
 var map="";
 $(function() {
	 $.ajax({
		 url:"<%=basePath %>material/inventoryCheck/querunit.action",
		 type:"post",
		 success:function(data){
			 unitMap=eval("("+data+")");
		 }
	 });
	var deptId=$("#deptId").val();
	$("#listfz").datagrid({
		url:"<%=basePath %>material/inventoryCheck/querMatStockdetail.action",
		queryParams:{deptId:deptId},		
		method:"post",
		onDblClickRow: function(rowIndex, rowData){
			 var itemCode=rowData.itemCode;
			 var inNo=rowData.inNo;
			 $.ajax({
					url:"<%=basePath %>material/inventoryCheck/querMatCheckdetail.action",
					data:{itemCode:itemCode,deptId:deptId,inNo:inNo},
					type:"post",
					success:function(data){
						map=eval("("+data+")");
						var st=map.stock;
						if(map.check=="error"){
							$.messager.alert('提示',"该物品已经在盘点，不能重复添加！");
						}else{
							
							<%-- url:"<%=basePath %>material/inventoryCheck/querMatStockdetailtj.action",
							queryParams:{deptId:deptId,itemCode:itemCode},		
							method:"post" --%>
							var rowfz=$("#listfz2").datagrid("getRows");
							if(rowIndex!=null&&rowIndex!=''){
								for(var m=0;m<rowfz.length;m++){
									var stockCode=rowfz[m].stockCode
									if(st[0].stockCode==stockCode){
										$.messager.alert('提示',"该物品已经在封账！");
										return;
									}
								}
								$("#listfz2").datagrid("appendRow",{
									itemCode:st[0].itemCode,
									stockCode:st[0].stockCode,
									stockNo:st[0].stockNo,
									storageCode:st[0].storageCode,
									inNum:st[0].inNum,
									itemName:st[0].itemName,
									inPrice:st[0].inPrice,
									salePrice:st[0].salePrice,
									specs:st[0].specs,
									packQty:st[0].packQty,
									storeNum:st[0].storeNum,
									storeNum:st[0].storeNum,
									storeCost:st[0].storeCost,
									minUnit:st[0].minUnit,
									cstoreNum:st[0].cstoreNum,
									packUnit:st[0].packUnit,
									memo:st[0].memo,
									operCode:st[0].operCode,
									operDate:st[0].operDate,
									highvalueBarcode:st[0].highvalueBarcode
								 }); 
								/* editIndex = $("#listfz2").edatagrid('getRows').length-1;
						 	 	$("#listfz2").edatagrid('selectRow', editIndex).edatagrid('beginEdit', editIndex); */
							}
						}
						
					}
			 });
		 }
	});
}); 
 function funcunit(value,row,index){
	 if(value!=""){
		 return unitMap[value];
	 }
	 
 }
 //计算金额
 $("#listfz2").edatagrid({
			 onSelect: function(rowIndex, rowData){
					var onDbListRows = $('#listfz2').datagrid('getRows');
					if(onDbListRows.length>0){
						for(var m=0;m<onDbListRows.length;m++){
							var indexRows = $('#listfz2').datagrid('getRowIndex',onDbListRows[m]);
							$('#listfz2').datagrid('endEdit',indexRows);
						}
					}
					$('#listfz2').datagrid('beginEdit',rowIndex);
					var ed = $('#listfz2').datagrid('getEditor', {index:rowIndex,field:'minAmount'});//小包装盘点数量
					var end=$('#listfz2').datagrid('getEditor', {index:rowIndex,field:'fstoreNum'});//大包装盘点数量
					var endm=$('#listfz2').datagrid('getEditor', {index:rowIndex,field:'memo'});//备注
				    var t = $(ed.target).numberbox('getText'); //小包装盘点数量
					var tn = $(end.target).numberbox('getText');//大包装盘点数量
					var tnm = $(endm.target).textbox('getText');//备注
				    /* $(ed.target).next("span").children().first().val("").focus().val(t);//小包装盘点数量
				    $(endm.target).next("span").children().first().val("").focus().val(tnm);//备注 */
					$(end.target).next("span").children().first().val("").focus().val(tn);//大包装数量
					
					var inPrice = rowData.inPrice;//购入价
					var salePrice=rowData.salePrice;//零售价
					var packQty=rowData.packQty;//大包装数量
					$(end.target).numberbox('textbox').bind('keyup', function(event) {
						    var minAmount = $(ed.target).numberbox('getText');//小包装盘点数量
							var fstoreNum = $(end.target).numberbox('getText');//大包装盘点数量
							var memo=$(endm.target).textbox('getText');//备注
							var pdje= (inPrice*fstoreNum).toFixed(2);//盘点金额
							var pdlsje= (salePrice*fstoreNum).toFixed(2);//盘点零售金额
							var profitLossNum=packQty-fstoreNum;//盈亏数量
				  	  	    /* var selfCost=(moneyMount-privilegeCost-pubCost).toFixed(2);//自付 */
				  	  	    var inpriceLoss=(profitLossNum*inPrice).toFixed(2);//盈亏金额
				  	  	    var salepriceLoss=(salePrice*profitLossNum).toFixed(2);//盈亏零售金额
							$('#listfz2').datagrid('updateRow',{
								index: rowIndex,
								row: {
									minAmount:minAmount,
									fstoreNum:fstoreNum,
									pdje:pdje,
									pdlsje:pdlsje,
									profitLossNum:profitLossNum,
									inpriceLoss:inpriceLoss,
									salepriceLoss:salepriceLoss,
									memo:memo
								}
							});
							$('#listfz2').datagrid('selectRow',rowIndex);
					});
			 }
 });
 //删除按钮
 function del(){
	 var row= $("#listfz2").edatagrid("getChecked");
	 var rows= $("#listpd").edatagrid("getChecked");
	 if(row.length>0){
		 for(var i=0;i<row.length;i++){
			 index=$("#listfz2").edatagrid('getRowIndex',row[i]);
				$("#listfz2").edatagrid('deleteRow',index);
	 	 	    index = $("#listfz2").edatagrid('getChecked').length-1;
		 }
		
	 }
	 if(rows.length>0){
		 for(var i=0;i<rows.length;i++){
			 index=$("#listpd").edatagrid('getRowIndex',rows[i]);
				$("#listpd").edatagrid('deleteRow',index);
	 	 	    index = $("#listpd").edatagrid('getChecked').length-1;
		 }
		
	 }
 }
 //导出
 function derive(){
	 var rows = $('#listpd').datagrid("getRows");
	/*  $('#infoJson').val(JSON.stringify(rows)); */
		/* var copyRows = cloneObject(rows); */
		 $.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否删除
			if (res) {
				$("#infoJson").val(JSON.stringify(rows));
				$("#saveForm").form('submit', {
					url : "<%=basePath %>material/inventoryCheck/exportInstore.action",
					onSubmit : function() {
						return $(this).form('validate');
					},
					success : function(data) {
						$.messager.alert("操作提示", "导出成功！", "success");
					},
					error : function(data) {
						$.messager.alert("操作提示", "导出失败！", "error");
					}
				});
			}
		}); 
 }
</script>
</body>
</html>