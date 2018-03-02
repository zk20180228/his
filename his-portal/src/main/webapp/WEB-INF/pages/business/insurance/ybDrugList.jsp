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
<title>药品信息</title>
<script type="text/javascript"> 
           var editIndex = undefined;
           var drugData = undefined;
			//加载页面
			$(function(){
				bindAddButton();
				bindDelButton();
				bindSaveButton();
			var winH=$("body").height();
			 $('#list1').datagrid({
				 pagination:true,
				 pageSize:10,
				 pageList:[10,20,30,50,80,100],
				 onClickRow: onClickRow,
				 onBeforeLoad:function(){
						//GH 2017年2月17日 翻页时清空前页的选中项
						$('#list1').datagrid('clearChecked');
						$('#list1').datagrid('clearSelections');
					}
			 })
			$('#list').height(winH-78-30-27-26);
				var id='${id}'; //存储数据ID
				//添加datagrid事件及分页
				$('#list').datagrid({
					pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],
					onLoadSuccess: function (data) {//默认选中
			            var rowData = data.rows;
			            $.each(rowData, function (index, value) {
			            	if(value.id == id){
			            		$('#list').datagrid('checkRow', index);
			            	}
			            });
			          //分页工具栏作用提示
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
			        },onDblClickRow: function (rowIndex, drowData) {//双击查看
			        	drugData=drowData;
			        	$('#id').val(drowData.id);
			        	searchHisInfo(drowData.drugCode);
			       	},onClickRow: function (rowIndex, rowData) {//单击保存id
			        	$('#id').val(rowData.id);
			       	},
					onBeforeLoad:function(){
						$('#list').datagrid('clearChecked');
						$('#list').datagrid('clearSelections');
					}
				});
					bindEnterEvent('queryName',searchFrom,'easyui');
				});
			
				function formatflag(val,row,index){
					if (val == 1){
						return '是';
					} else {
						return '否';
					}
				}				
	         	function reload(){
					//实现刷新栏目中的数据
					 $("#list1").datagrid("reload");
				}
	         	
				function searchReload() {
					$('#queryName').textbox('setValue','');
					searchFrom();
				}
				
				function searchFrom() {
					var queryName = $.trim($('#queryName').val());
					$('#list').datagrid('load', {
						str : queryName
						        
					});
				}
				function searchHisInfo(druginfo) {
					 $('#list1').datagrid({
                 		url:"${pageContext.request.contextPath}/baseinfo/insurance/queryHisList.action?menuAlias=${menuAlias}",
                 		queryParams: {
	                        	page: 1,
	                        	rows:10,
	                        	str:druginfo
	                    	}
              })
	    	}
			//一次保存所有		
			 var JSONObj=new Object();
			//编辑状态
		      function endEditing() {
		          if (editIndex == undefined) { return true }
		          if ($('#list1').datagrid('validateRow', editIndex)) {
		              var ed = $('#list1').datagrid('getEditor', { index: editIndex, field: 'productid' });
		              $('#list1').datagrid('endEdit', editIndex);
		              editIndex = undefined;
		              return true;
		          } else {
		              return false;
		          }
		      }
		      //单击某行进行编辑
		      function onClickRow(index) {
		          if (editIndex != index) {
		              if (endEditing()) {
		                  $('#list1').datagrid('selectRow', index)
		                          .datagrid('beginEdit', index);
		                  editIndex = index;
		              } else {
		                  $('#list1').datagrid('selectRow', editIndex);
		              }
		          }
		      }
		 
		      //添加一行
		      function append() {
		          if (endEditing()) {
		              $('#list1').datagrid('appendRow', {
							insuranceNo:drugData.insuranceNo,
							ybItemCode:drugData.drugCode,
							ybItemName:drugData.drugName,
							costType:drugData.costType,
							itemType:"0",
							inSelfProportion:drugData.inSelfProportion
		              });
		              editIndex = $('#list1').datagrid('getRows').length - 1;
		              $('#list1').datagrid('selectRow', editIndex)
		                      .datagrid('beginEdit', editIndex);
		          }
		      }
		      //删除一行
		      function remove() {
		          if (editIndex == undefined) {return}
		        		  $('#list1').datagrid('cancelEdit', editIndex)
		                  .datagrid('deleteRow', editIndex);
		                   editIndex = undefined;       
		      }
		      //撤销编辑
		      function reject() {
		          $('#list1').datagrid('rejectChanges');
		          editIndex = undefined;
		      }
		 
		      //获得编辑后的数据,并提交到后台
		      function saveChanges() {
		          if (endEditing()) {
		              var $dg = $('#list1');
		              var rows = $dg.datagrid('getChanges');
		              if (rows.length) {
		                  var inserted= $dg.datagrid('getChanges', "inserted");
		                  var deleted = $dg.datagrid('getChanges', "deleted");
		                  var updated = $dg.datagrid('getChanges', "updated");
		          		}
			      }
	              if(inserted!=undefined&&deleted!=undefined&&updated!=undefined){
	            	  $.ajax({
	  		  			type: "post",
	  		  			url: "${pageContext.request.contextPath}/baseinfo/insurance/HandleHisList.action",
	  		  			async: true,
	  		  			data: {
	  		  				inserted: JSON.stringify(inserted),
	  		  				updated: JSON.stringify(updated),
	  		  				deleted: JSON.stringify(deleted),
	  		  			},
	  		  			success: function(data) {
	  		  			    console.info(data)
	  		  				if(data.flag==0){
		  		  				reload();
		  		  				$.messager.alert("提示", "医保对照成功！");
	  		  				}else{
	  		  				   $.messager.alert("提示",data.msg);
	  		  				}
	  		  			}
	  		  		}); 
	              }else{
	            	  $.messager.alert("提示", "无任何需要数据需要操作！");
	              }
		     
		  }

		  function bindSaveButton() {
		      $("#saveButton").click(function () {
		          saveChanges();
		      });
		  }
		  function bindAddButton() {
		      $("#addButton").click(function () {
		          append();
		          });
		  }
		  function bindDelButton() {
		      $("#delButton").click(function () {
		          remove();
		      });
		  }	
		  //数据格式化
		  function formatzz(val,row,index){
				if(val=='0'){
					return "否"
				}
				if(val=='1'){
					return "是 "
				}else{
					return val
				}
			}
			function formatcostype(val,row,index){
				if(val=='01'){
					return "西药"
				}
				if(val=='02'){
					return "中成药 "
				}
				if(val=='03'){
					return "中草药 "
				}else{
					return val
				}
			}
			function formatprescFlag(val,row,index){
				if(val=='0'){
					return "非处方药"
				}
				if(val=='1'){
					return "处方药 "
				}else{
					return val
				}
			}
			function formatlevel(val,row,index){
				if(val=='1'){
					return "甲类"
				}
				if(val=='2'){
					return "乙类"
				}
				if(val=='3'){
					return "自费"
				}
				if(val=='5'){
					return "丁类"
				}else{
					return val
				}
			}
			function formatitemType(val,row,index){
				if(val=='0'){
					return "药品"
				}
				if(val=='1'){
					return "诊疗项目"
				}
				if(val=='2'){
					return "服务设施"
				}
			}
			function formatHisCostype(val,row,index){
				if(val=='01'){
					return "西药"
				}
				if(val=='02'){
					return "中成药 "
				}
				if(val=='03'){
					return "中草药 "
				}
				if(val=='04'){
					return "常规检查"
				}
				if(val=='05'){
					return "CT "
				}
				if(val=='06'){
					return "核磁 "
				}
				if(val=='07'){
					return "B超"
				}
				if(val=='08'){
					return "治疗费 "
				}
				if(val=='09'){
					return "化验费 "
				}
				if(val=='10'){
					return "手术费"
				}
				if(val=='11'){
					return "输氧费"
				}
				if(val=='12'){
					return "放射费 "
				}
				if(val=='13'){
					return "输血费"
				}
				if(val=='14'){
					return "注射费 "
				}
				if(val=='15'){
					return "透析 "
				}
				if(val=='16'){
					return "化疗"
				}
				if(val=='22'){
					return "特殊检查费 "
				}
				if(val=='32'){
					return "特殊治疗费 "
				}
				if(val=='34'){
					return "床位费"
				}
				if(val=='91'){
					return "其他费用 "
				}else{
					return val
				}
			}
	</script>
</head>
<body style="margin: 0px;padding: 0px">
<div id="divLayout" class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 30px;">
	<input type="hidden" name="type" id="id" />
		<table >
			<tr>
				<td>
					<input type="text" id="queryName" name="queryName"  class="easyui-textbox" data-options="prompt:'名称、代码、辅助码'" />
					<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-search">查询</a>
					<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
					<a href="javascript:void(0)" onclick="updateload()" class="easyui-linkbutton" iconCls="reset">更新中心目录</a>
				</td>
			</tr>
		</table>
	</div>	
	<div data-options="region:'center',title:'中心药品信息',border:true">
		<table id="list" style="width:100%;" data-options="url:'${pageContext.request.contextPath}/baseinfo/insurance/queryDrugList.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">
			<thead>
				<tr>
<!-- 					<th data-options="field:'ck',checkbox:true" ></th> -->
                    <th data-options="field:'id'"width="6%"hidden=true>ID</th>
					<th data-options="field:'serialNo'"width="3%">序号</th>
					<th data-options="field:'drugCode'"width="6%">中心编码</th>
					<th data-options="field:'drugName'"width="9%">中心名称</th>
					<th data-options="field:'englishName'"width="9%">英文名称</th>
					<th data-options="field:'costType',formatter:formatcostype"width="3%">费用类别</th>
					<th data-options="field:'prescFlag',formatter:formatprescFlag"width="3%">处方药标志</th>
					<th data-options="field:'costItemGrade',formatter:formatlevel"width="3%">收费项目等级</th>
					<th data-options="field:'drugDoseunit'"width="3%">剂量单位</th>
					<th data-options="field:'maxPrice'"width="3%">最高价格</th>
					<th data-options="field:'outSelfProportion'"width="3%">门诊自付比例</th>
					<th data-options="field:'inSelfProportion'"width="3%">住院自付比例</th>
					<th data-options="field:'drugDosageform'"width="3%">剂型</th>
					<th data-options="field:'drugOncedosage'"width="3%">每次用量</th>
					<th data-options="field:'drugFrequency'"width="3%">使用频次</th>
					<th data-options="field:'drugUsemode'"width="3%">用法</th>
					<th data-options="field:'drugUnit'"width="3%">单位</th>
					<th data-options="field:'drugSpec'"width="3%">规格</th>
					<th data-options="field:'limitTime'"width="3%">限定天数</th>
					<th data-options="field:'goodsPrice'"width="3%">商品名价格</th>
					<th data-options="field:'drugManufacturer'"width="3%">药厂名称</th>
					<th data-options="field:'madeSelfFlag',formatter:formatzz"width="3%">自制药品</th>
					<th data-options="field:'insuranceNo'"width="3%">定点医疗机构编号</th>
					<th data-options="field:'medicine'"width="3%">国药准字</th>
					<th data-options="field:'lxSelfProportion'"width="3%">离休自付比例</th>
					<th data-options="field:'gsSelfProportion'"width="3%">工伤自付比例</th>
					<th data-options="field:'sySelfProportion'"width="3%">生育自付比例</th>
					<th data-options="field:'eySelfProportion'"width="3%">二乙自付比例</th>
					<th data-options="field:'drugType',formatter:formatcostype"width="3%">药品种类</th>
					<th data-options="field:'approveFlag',formatter:formatflag"width="3%">是否需要审批标志</th>
					<th data-options="field:'operator'"width="3%">经办人</th>            
				</tr>
			</thead>
		</table>
	</div>
	<div data-options="region:'south',title:'药品对照信息',split:true" style="height:40%;">
	<table id="list1" style="width:100%;" data-options="url:'',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true,toolbar:'#toolbarId'">
			<thead>
				<tr>				
<!-- 				    <th data-options="field:'ck',checkbox:true" ></th> -->
				    <th data-options="field:'id'"width="6%"hidden=true>ID</th>
					<th data-options="field:'serialNo'"width="3%" editor="text">组内序号</th>
					<th data-options="field:'insuranceNo'"width="6%">医疗机构编号</th>
					<th data-options="field:'itemCode'"width="9%" editor="text">院内编码</th>
					<th data-options="field:'itemName'"width="9%" editor="text">院内名称</th>
					<th data-options="field:'itemType',formatter:formatitemType"width="6%">项目类别</th>
					<th data-options="field:'ybItemCode'"width="6%">中心编码</th>
					<th data-options="field:'ybItemName'"width="6%">中心名称</th>
					<th data-options="field:'costType',formatter:formatHisCostype,
						editor:{type:'combobox',options:{valueField:'id',textField:'text',multiple:false,method:'post',
						data:[{id:'01',text:'西药'},{id:'02',text:'中成药'}	,{id:'03',text:'中草药'},{id:'04',text:'常规检查'},{id:'05',text:'CT'},{id:'06',text:'核磁'}
						,{id:'07',text:'B超'},{id:'08',text:'治疗费'},{id:'09',text:'化验费'},{id:'10',text:'手术费'},{id:'11',text:'输氧费'},{id:'12',text:'放射费'},{id:'13',text:'输血费'}
						,{id:'14',text:'注射费'},{id:'15',text:'透析'},{id:'16',text:'化疗'},{id:'22',text:'特殊检查费'},{id:'32',text:'特殊治疗费'},{id:'34',text:'床位费'},{id:'91',text:'其他费用'}] 
						}}" 
					width="6%" >院内收费类别</th>
					<th data-options="field:'itemDosageform'"width="6%" editor="text">院内项目剂型</th>
					<th data-options="field:'itemSpec'"width="6%" editor="text">院内规格</th>
					<th data-options="field:'itemUnit'"width="6%" editor="text">院内单位</th>
					<th data-options="field:'itemPrice'"width="6%" editor="text">院内价格</th>	
					<th data-options="field:'inSelfProportion'"width="6%">住院自付比例</th>
					<th data-options="field:'operator'"width="6%">经办人</th>            
				</tr>
			</thead>
		</table>
		<div id="add"></div>
		<div id="toolbarId">
<!-- 		<input type="text" id="queryinfo" name="queryName"  class="easyui-textbox" data-options="prompt:'名称、代码、辅助码'" /> -->
<!-- 		<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-search">查询</a> -->
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" id="addButton" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">	
			<a href="javascript:void(0)" id="delButton" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
            <a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'" id="saveButton">保存</a>
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
			<span style="color:red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;所有操作只需最后保存一次，保存方可生效！</span>
		</div>
</div>
</body>
</html>