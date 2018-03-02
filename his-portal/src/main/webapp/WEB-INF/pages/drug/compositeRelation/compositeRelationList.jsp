<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body  style="margin: 0px;padding: 0px;">
	<div class="easyui-layout"style="width:100%;height:100%;" data-options="fit:true">
	    <div  data-options="region:'west',iconCls:'icon-book',split:true" style="width:25%;height:92%;border-top:0">
		    <div class="easyui-layout" style="width: 100%; height: 100%;"data-options="fit:true">
			    <div data-options="region:'north'" style="width:100%;height:38px;border-top:0;border-right:0;">
			    	<form  id="searchForm" style="padding:5px 0px 5px 4px;" >
				    	<input class="easyui-textbox " name="tackUndrug" id ="tackUndrug"  style="width:180px" data-options="prompt:'名称、编码、拼音码、五笔码、自定义码'"/>
						<button type="button" onclick="searchTack()" class="easyui-linkbutton" iconCls="icon-search">查询</button>
						<button type="button" onclick="clearTack()" class="easyui-linkbutton" iconCls="reset">重置</button>
			    	</form>
			    </div>
				<div data-options="region:'center',border:false" style="width:100%;height: 90%">
					<table id=tackUndrugList style="width:100%;height: 100%" data-options="fit:true,border:false"></table>
				</div>			
		    </div>
	    </div>  
	    <div data-options="region:'center',border:false" style="width:65%;height:94%;">
		    <div class="easyui-layout" style="width:100%;height:100%;">   
			    <div  class="easyui-layout" data-options="region:'north'" style="width:100%;height:50%;border-top:0" >
					<div class="easyui-layout" style="width:100%;height:100%;">
					    <div data-options="region:'north'" style="width:100%;height: 38px;border-top:0;border-left:0;">
					    	<form style="padding:5px 4px">
						    	查询条件：<input class="easyui-textbox" name="noTackUndrug" id ="noTackUndrug" style="width:300px" data-options="prompt:'名称、编码、拼音码、五笔码、自定义码'"/>
								<button type="button" onclick="searchNoTack()" class="easyui-linkbutton" iconCls="icon-search">查询</button>&nbsp;
								<button type="button" onclick="clearNoTack()" class="easyui-linkbutton" iconCls="reset">重置</button>
					    	</form>
						</div>
						<div data-options="region:'center',border:false" style="width:100%;height:90%;border: none;" > 
					    	<table id="noTackUndrugList" style="width:100%;hight:100%" data-options="fit:true,border:false"></table>
						</div>
					</div>
				</div> 
			    <div  class="easyui-layout" style="width:100%;height:50%;border-top:0;" data-options="region:'center'">
			    	<div class="easyui-layout" style="width:100%;height:100%;">
					    <div data-options="region:'north'" style="width:100%;height: 38px;border-top:0;border-left:0;">
					    	<form style="padding-top:5px">
								&nbsp;<button type="button" class="easyui-linkbutton" onclick="cal(0)" iconCls="icon-save">保存</button>
								&nbsp;<button type="button" class="easyui-linkbutton" onclick="deleteRows()" iconCls="icon-remove">删除</button>
					    	</form>
						</div>
						<div data-options="region:'center',border:false" style="width:100%;height:90%"> 
					    	<table id="detailedList" style="width:100%;" data-options="fit:true,idField: 'id',border:false"></table>
						</div>
					</div>
				</div>
			</div>  
	    </div>   
	</div>
	<form id="editForm" method="post">
			<input type="hidden" id="drugUndrugJson" name="drugUndrugJson" >
			<input type="hidden" id="packCode" name="packCode" >
			<input type="hidden" id="ifUpdateUndrug" name="ifUpdateUndrug" >
			<input type="hidden" id="sum" name="sum" >
			<input type="hidden" id="del" name="del" >
	</form>
</body>

<script type="text/javascript">
		var undrugIndex; //记录组套信息的下表
		var isEditingRowIndex=0;//记录正在编辑的行索引
		var packCode='';
		var packName='';
		//页面初始化
		$(function(){
			$('#tackUndrugList').datagrid({
				url:'<%=basePath%>baseinfo/compositeRelation/queryTackUndrug.action',
				columns:[[
				          {field:'name',title:'药品名称',width:'70%'},
				          {field:'defaultprice',title:'默认价格',width:'34%'},
				          {field:'code',title:'ID',hidden:true}
				          ]],
				pageSize:30, 
				singleSelect:true,
				pagination:true,
				onDblClickRow:function(rowIndex, rowData){//双击 查询组套下已有明细
					//清空保存临时删除数据的变量
					delArr="";
					undrugIndex=rowIndex;
					var row = $('#tackUndrugList').datagrid('getSelected');
					if (row) {
						packCode='';
						packName='';
						packCode=row.code;
						packName=row.name;
						$('#detailedList').datagrid("options").url='<%=basePath%>baseinfo/compositeRelation/queryUndrugById.action';
						$('#detailedList').datagrid('load', {
							packCode : row.code,
						});
					}
				},onLoadSuccess:function(row, data){
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
					}}
			});
			$.extend($.fn.validatebox.defaults.rules, {    
			    minValue: {    
			        validator: function(value){
			        	return value>0;
			        },    
			        message: '请输入一个大于0的数'   
			    } 
			});  
		
			$('#noTackUndrugList').datagrid({
				url:'<%=basePath%>baseinfo/compositeRelation/queryNotTackUndrug.action',
				columns:[[
			          {field:'code',title:'项目编码',width:'100'},
			          {field:'name',title:'药品名称',width:'140'},
			          {field:'undrugPinyin',title:'拼音码',width:'60'},
			          {field:'undrugWb',title:'五笔码',width:'60'},
			          {field:'undrugInputcode',title:'自定义码',width:'100'},
			          {field:'defaultprice',title:'默认价',width:'60'},
			          {field:'childrenprice',title:'儿童价',width:'60'},
			          {field:'specialprice',title:'特诊价',width:'60'},
			          {field:'unit',title:'单位',width:'5%'},
			          {field:'undrugRequirements',title:'检查要求',width:'100'},
			          {field:'undrugNotes',title:'注意事项',width:'100'},
			          {field:'undrugIspreorder',title:'是否预约',width:'100'},
			          {field:'undrugEmergencycaserate',title:'急诊比例',width:'100'},
			          {field:'undrugIsa',title:'是否甲类',hidden:true,
			        	  formatter:function(value,rows){
			        		 if(rows.undrugIsa==1){
			        			 rows.type='甲类';
			        		 }
			          	  }},
			          {field:'undrugIsb',title:'是否乙类',hidden:true,
			        	  formatter:function(value,rows){
			        		 if(rows.undrugIsb==1){
			        			 rows.type='乙类';
			        		 }
					       }},
			          {field:'undrugIsc',title:'是否丙类',hidden:true,
			        	  formatter:function(value,rows){
			        		 if(rows.undrugIsc==1){
			        			 rows.type='丙类';
			        		 }
					      }},
			          {field:'type',title:'甲乙类标识',width:'100'},
			          {field:'id',title:'ID',hidden:true}
			          ]],
			    pageSize:10, 
			    fixed:false,
			    fitColumns:true,
			    singleSelect:true,
				pagination:true,
				onDblClickRow:function(rowIndex, row){
					if(packCode==''){
						$.messager.alert('提示',"请先选择非药品组套数据！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return;
					}
					//判断记录是否已经添加到右侧列表中
					var rows=$('#detailedList').datagrid('getRows');
					for(var i=0;i<rows.length;i++){
						if(row.code==rows[i].itemCode){
							$.messager.alert("提示", "列表中已有此条记录！","warning");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							return;
						}
					}
					//添加一行并赋值
					$('#detailedList').datagrid('appendRow', {
						id:row.id,
						itemName : row.name,
						itemCode : row.code,
						spellCode : row.undrugPinyin,
						wbCode : row.undrugWb,
						inputCode : row.undrugInputcode,
						defaultprice : row.defaultprice,
						applyPrice : row.inPrice,
						childrenprice : row.childrenprice,
						specialprice : row.specialprice,
						applyNum:1,
						validState:0,
						sysClass3mean:1,
						qty:1,
						packageName:packName,
						packageCode:packCode,
						isDel:1
					});
				},onLoadSuccess:function(row, data){
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
					}}
			
			}); 
			//详情临时列表
			$('#detailedList').datagrid({
				url:'',
				columns:[[
					  {field:'ck',width:'2%',checkbox:true},
			          {field:'itemName',title:'名称',width:'350'},
			          {field:'qty',title:'数量',width:'70',
			        	  editor: {
								type: 'numberbox',
			          		    options:{
			          		    	required:true,
			          		    	missingMessage:'数量不能为空',
				        			invalidMessage:'数量须大于0'
			          		    	}
			          		    }
		         		 },
			          {field:'validState',title:'是否有效',width:'70',
							  editor: {
									type: 'combobox',
									options: {
										required:true,
										valueField: 'id',
										textField: 'text',
										data:[{id:1,text:'是'},{id: 0,text: '否'}]
									}
								},
									formatter:validValue
			         		 },
			          {field:'spellCode',title:'拼音码',width:'70'},
			          {field:'wbCode',title:'五笔码',width:'80'},
			          {field:'inputCode',title:'自定义码',width:'100'},
			          {field:'defaultprice',title:'默认价',width:'60'},
			          {field:'childrenprice',title:'儿童价',width:'60'},
			          {field:'specialprice',title:'特诊价',width:'60'},
			          {field:'packageCode',hidden:true},
			          {field:'packageName',hidden:true},
			          {field:'itemCode',hidden:true},
			          {field:'sortId',hidden:true}
			          ]],
			    fixed:false,
			    fitColumns:true,
				pageSize:10, 
				onClickRow:function(rowIndex, rowData){
					//上次编辑的行索引
					$('#detailedList').datagrid('endEdit',isEditingRowIndex);
					//得到当前点击的行索引					
					isEditingRowIndex= rowIndex;	
					//开启编辑行
					$('#detailedList').datagrid('beginEdit',rowIndex);
				}
			});
			
			bindEnterEvent('tackUndrug',searchTack,'easyui');
			bindEnterEvent('noTackUndrug',searchNoTack,'easyui');
		});
		
		//查询组套信息
		function searchTack() {
			var tackUndrug = $.trim($('#tackUndrug').val());
			$('#tackUndrugList').datagrid('load', {
				tackUndrug : tackUndrug
			});
		}
		//查询非组套信息
		function searchNoTack() {
			var noTackUndrug = $.trim($('#noTackUndrug').val());
			$('#noTackUndrugList').datagrid('load', {
				noTackUndrug : noTackUndrug
			});
		}
		
		/**
		 * 是否有效格式化
		 */
		function validValue(value,row,index){
			if(value==0){
				return '否';
			}else{
				return '是';
			}
		}
		
		/**
		 * 计算金额
		 */
		 function cal(flg){
			//关闭编辑栏状态
			 $('#detailedList').datagrid('endEdit',isEditingRowIndex);
			 var row = $('#detailedList').datagrid("getData");
			 var rows = row.rows;
			 var num ;
			 $('#drugUndrugJson').val(JSON.stringify(rows));
			 for(var q=0;q<rows.length;q++){
				 num = rows[q].qty;
				 if(num==null||num=='undefined'){
					 $.messager.alert('提示',"请输入数量！");
					 setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
				     return;
				 }
			}
		    var a=0;
		    //获取所有数据
			var row= $('#detailedList').datagrid('getData');
			var rows = row.rows;
			for(var n=0;n<rows.length;n++){
				var q=parseInt(rows[n].qty);
				var d=parseInt(rows[n].defaultprice);
				a=a+(q * d);
			}
			var row2 = $('#tackUndrugList').datagrid('getSelected');
			var b=parseInt(row2.defaultprice);
			if(a!=b){
				$.messager.confirm('确认对话框', '当前选择的组套信息总价格('+ a +'元)与非药品总价格('+b+'元)不一致,是否需要同时更新非药品总价格?', function(r){
					if (r){
						$('#sum').val(a);
						 save('1',flg);
						 $('#tackUndrugList').datagrid('getRows')[undrugIndex].defaultprice = a;
						 $('#tackUndrugList').datagrid('refreshRow', undrugIndex);
					}else{
						save('0',flg);
					}
				});
			}else{
				save('0',flg);
			}
		 }
		
		 /**
			 * 提交数据
			 */
		 function save(ifUpdateUndrug,flg){
			 $('#packCode').val(packCode);
			 $('#ifUpdateUndrug').val(ifUpdateUndrug);
			 $('#del').val(delArr);
			 $('#editForm').form('submit',{
		         url:"<%=basePath%>baseinfo/compositeRelation/save.action",
		         success:function(data){  
		         	$.messager.progress('close');
				 	$("#detailedList").datagrid("reload",{
				 		packCode:packCode
					});
				 	if(flg==0){
						$.messager.alert('提示',"保存成功！");
				 	}else{
				 		$.messager.alert('提示',"删除成功！");
				 	}
					//清空保存临时删除数据的变量
					delArr="";
					$('#detailedList').datagrid('uncheckAll');
		         },
				 error : function(data) {
				    $.messager.progress('close');
				    if(flg==0){
				    	$.messager.alert('提示',"保存失败！");	
				 	}else{
				 		$.messager.alert('提示',"删除失败！");
				 	}
				 }			         
			  }); 
		  }
	 
		 /**
		  * 删除
		  */
		  //记录删除的药品code
		 var delArr="";
		 function deleteRows(){
			 var rows=$('#detailedList').edatagrid('getChecked');
			 var length=rows.length;
			 if(length==0){
				 $.messager.alert('提示',"请选中要删除的记录！");	
				 setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				 return;
			 }
         	 if (rows.length > 0) {//选中几行的话触发事件	 
         		if(delArr!=""){
         			delArr+=",";
         		}
         		var arr = [];
				for ( var i = 0; i < rows.length; i++) {
					var dd = $('#detailedList').edatagrid('getRowIndex',rows[i]);//获得行索引
					arr.push(dd);
					//判断 如果是持久化数据， 添加至delArr变量中  在保存方法中更改其状态
					if(typeof(rows[i].isDel)=="undefined"){
						if(i>0){
							delArr+=",";
						}
						delArr+="'"+rows[i].itemCode+"'";
					}
				}
				for ( var i = arr.length - 1; i >= 0; i--) {
					$('#detailedList').edatagrid('deleteRow', arr[i]);//通过索引删除该行
				}
         	 }
         	cal(1);//自动去触发保存方法
		}
		
	    /**
	     *清空明细药品输入框
	     */
	     function clearNoTack(){
		    $('#noTackUndrug').textbox('reset');
	    	 searchNoTack();
	     }
	    /**
	     *清空组套药品输入框
	     */
	     function clearTack(){
		   $('#tackUndrug').textbox('reset');
	    	 searchTack();
	     }
</script>
</html>