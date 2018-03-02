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
<title>附材管理</title>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<style type="text/css">
.top {
	padding: 5px 5px 5px 5px;
	height: 25px;
	line-height: 25px;
}

.center {
	padding: 5px 5px 5px 5px;
	height: 90%;
	overflow: auto;
}

.center #center-right-div {
	margin-left: 251px;
	border: 1px solid #95b8e7;
}
</style>
</head>
<body style="margin: 0px; padding: 0px">
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',border:false" style="height: 35px;">
			<div class="top">
				<shiro:hasPermission name="${menuAlias}:function:save">
					<a href="javascript:saveItem()" class="easyui-linkbutton"
						data-options="iconCls:'icon-page_save'">保存</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:delete">
					<a onclick="delItem()" class="easyui-linkbutton"
						iconCls="icon-remove">删除</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:export">
					<a href="javascript:exportList(0);"
						data-options="iconCls:'icon-down'" class="easyui-linkbutton">导出</a>
				</shiro:hasPermission>
			</div>
		</div>
		<div data-options="region:'center',border:false" style="width: 100%;">
			<div class="easyui-layout" data-options="fit:true">
				<div
					data-options="region:'west',title:'当前科室【${curDeptName}】',split:true"
					style="width: 15%; height: 100%">
					<div class="easyui-layout" data-options="fit:true,border:false">
						<div data-options="region:'north',border:false"
							style="height: 50px; line-height: 20px;">
							<input type="hidden" name="curUserDept" value="${curUserDeptId}" />
							<span>
								&nbsp;<input type="radio" id="useAgeId" name="ifDrug" value="SHI" onclick="singleSelectQuery(this.value);">
								药品用法 
							</span> 
							<span style="padding-left: 15px;">
								<input type="radio"id="unDrugId" name="ifDrug" value="FOU" onclick="singleSelectQuery(this.value);">
								非药品 
							</span>
							&nbsp;<input class="easyui-textbox" id="queryName" name="queryName" data-options="prompt:'用法、非药品名称'" style="width: 50%" /> 
								<a onclick="searchX()" class="easyui-linkbutton"  data-options="iconCls:'icon-search'">查询</a>
						</div>
						<div data-options="region:'center',border:false">
							<table id="listDrug">
							</table>
						</div>
					</div>
				</div>
				<div data-options="region:'center',border:true"
					style="width: 85%; height: 100%">
					<div class="easyui-layout" data-options="fit:true,border:false">
						<div data-options="region:'north',border:false"style="width: 100%; height: 30px; padding-top: 3px">
							 &nbsp;类别：<input id="jbType" name="jbType" value='000' /> 
							     快捷码：<input class="easyui-textbox" id="codeName" name="codeName" onkeydown="KeyDown(0)" data-options="prompt:'拼音,五笔,自定义'" />
							     名称：<input id="name" class="easyui-textbox" /> 
							  <a onclick="searchForm();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							  <a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
						</div>
						<div data-options="region:'center',border:false"
							style="width: 100%; height: 50%;">
							<table id="list1"
								title="非药品列表<span style='color:#C24641;font-size:10px;'>　双击加入附材列表</span>"
								style="width: 100%;"
								data-options="method:'post',idField:'id',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">
								<thead>
									<tr>
										<th data-options="field:'name',width:'20%'">名称</th>
										<th data-options="field:'spec',width:'5%'">规格</th>
										<th data-options="field:'unit',width:'5%'">单位</th>
										<th data-options="field:'defaultprice',width:'6%'">默认价</th>
										<th
											data-options="field:'undrugDiseaseclassification',width:'10%',formatter: functionDis">疾病分类</th>
										<th
											data-options="field:'undrugDept',width:'10%',formatter:functiondeptid">执行科室</th>
										<th data-options="field:'undrugSpecialtyname',width:'10%'">专科名称</th>
										<th data-options="field:'undrugPinyin',width:'10%'">拼音码</th>
										<th data-options="field:'undrugInputcode',width:'10%'">自定义码</th>
										<th data-options="field:'undrugWb',width:'10%'">五笔码</th>
									</tr>
								</thead>
							</table>
						</div>
						<div
							data-options="region:'south',title:'附材列表',collapsible:false,border:false"
							style="width: 100%; height: 40%;">
							<form id="editForm" method="post">
							<input type="hidden" id="oddGradeJson" name="oddGradeJson">
								<div style="width: 100%; height: 100%">
									<table id="list2" class="easyui-edatagrid"
										data-options="url:'',method:'post',iconCls: 'icon-edit',idField: 'id',striped:true,border:false,
									checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">
										<thead>
											<tr>
												<th field="getIdUtil" checkbox="true" style="width: 5%"></th>
												<th data-options="field:'itemName',width:'20%'">名称</th>
												<th data-options="field:'qty',formatter:funNum,width:'10%'">数量</th>
												<th
													data-options="field:'unit',width:'10%'">单位</th>
												<th
													data-options="field:'price',formatter:funPrice,width:'10%'">单价</th>
												<th
													data-options="field:'totalPrice',width:'10%',formatter:funPayCost">总价格</th>
												<th data-options="field:'useInterval',formatter:funInterval,width:'10%'">间隔(小时)</th>
												<th data-options="field:'useAgeId',width:'10%',hidden:true">用法id</th>
												<th data-options="field:'unDrugId',width:'10%',hidden:true">非药品id</th>
												<th data-options="field:'drugFlag',width:'10%',hidden:true">标记</th>
												<th data-options="field:'id',width:'10%',hidden:true">标记</th>
											</tr>
										</thead>
									</table>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
var codeNonMap=null; //单位渲染map
$.ajax({
	url:'<%=basePath%>baseinfo/department/queryDepartments.action',
	type:'post',
	success: function(datas) {
		deptidlist = datas;
	}
});	
$.ajax({
	url:'<%=basePath%>publics/odditionalManage/likeDisease.action',
	type:'post',
	success: function(disData) {
		disList = disData;
	}
});	
$.ajax({
	url:'<%=basePath%>publics/odditionalManage/getCodeMap.action',
	type:'post',
	success:function(data){
		codeNonMap=data;
	}
});
$(function(){
	var deptFlg = "${deptFlg}"
	console.log(deptFlg)
	
	$('#jbType').combobox({
		url:'<%=basePath%>publics/odditionalManage/likeDiseaseType.action',
	    valueField:'encode',    
	    textField:'name',
	    editable:true,
	    onSelect:function(record){
	    	searchForm();
		},onHidePanel:function(none){
		    var data = $(this).combobox('getData');
		    var val = $(this).combobox('getValue');
		    var result = true;
		    for (var i = 0; i < data.length; i++) {
		        if (val == data[i].encode) {
		            result = false;
		        }
		    }
		    if (result) {
		        $(this).combobox("clear");
		    }else{
		        $(this).combobox('unselect',val);
		        $(this).combobox('select',val);
		    }
		},
		filter: function(q, row){
		    var keys = new Array();
		    keys[keys.length] = 'encode';
		    keys[keys.length] = 'name';
		    keys[keys.length] = 'pinyin';
		    keys[keys.length] = 'wb';
		    keys[keys.length] = 'inputCode';
		    return filterLocalCombobox(q, row, keys);
		}
	});
 singleSelectQuery('SHI');
	$('#queryName').textbox('textbox').bind('keyup', function(event) {
		searchX();
	});
	$('#name').textbox('textbox').bind('keyup', function(event) {
		searchForm();
	});
	$('#codeName').textbox('textbox').bind('keyup', function(event) {
		searchForm();
	});
});
function codeMaps(value,row,index){
 	if(row.unit){
		return codeNonMap[value];
	}else{
		return '未知';
	}
}
	    var useAgeUnDrugArr =[];//用于存放新增的用法id和非药品的id 判断是否可以新增 [{selectedUseAgeId:'',selectedUnDrugId:''}]
		var deptidlist="";
		var disList="";
		
		
	     var index=null;
	     //单选
	     function singleSelectQuery(singlSelectVal){
	       //SHI
	       if(singlSelectVal=='SHI'){//药品
	            document.getElementById("useAgeId").checked=true;
	            $('#listDrug').datagrid({
	            	checkOnSelect:true,
	            	singleSelect:true,
	            	selectOnCheck:true,
	            	fit:true,
	            	border:false,
	            	url:'<%=basePath%>publics/odditionalManage/queryUseAge.action',
	            	columns : [ [ {
						field : 'name',title : '药品用法',width : '100%'}
					] ],
					queryParams: {
				    	queryName: null
					},
				   onDblClickRow: function (rowIndex, rowData) {
				      if(useAgeUnDrugArr.length>0){//若有新添加的未保存的数据提示用户
					       $.messager.confirm('确认', '附材中有未保存的数据是否继续?',function(res){
					          if(res){
					                useAgeUnDrugArr.length=0;//新添加的数组数据清空
						          	$('#list2').datagrid({
				                        url:"<%=basePath%>publics/odditionalManage/queryItemListByXId.action?menuAlias=${menuAlias}&flg=useAge",
				                        queryParams: {
				                        	drugFlg: 1,
				                        	xId:rowData.encode
				                    	}
				                   });
					          }
			               });
			           }else{//不用提示用户
			                 $('#list2').datagrid({
			                	        url:"<%=basePath%>publics/odditionalManage/queryItemListByXId.action?menuAlias=${menuAlias}&flg=useAge",
				                        queryParams: {
				                        	drugFlg: 1,
				                        	xId:rowData.encode,
				                    	}
				              });
			           }
				      index=rowData.id;
				   },
					onLoadSuccess:function(){
						if(index!=null){
							var rows=$('#listDrug').datagrid('getRows');
							for(var i=0;i<rows.length;i++){
								if(rows[i].id==index){
									var indx=$('#listDrug').datagrid('getRowIndex',rows[i]);
									$('#listDrug').datagrid('selectRow',indx);
								}
							}
						}
					}
	       		});
	       }else if(singlSelectVal=='FOU'){//非药品
				document.getElementById("unDrugId").checked=true;
				$('#listDrug').datagrid({
					url:'<%=basePath%>publics/odditionalManage/queryUnDrugItem.action',
				    columns:[[
				        {field:'unDrugName',title:'非药品',width:'100%'},    
				    ]],//unDrug
				    queryParams: {
				    	queryName: null
					},
				     onDblClickRow: function (rowIndex, rowData) {
				       if(useAgeUnDrugArr.length>0){//若有新添加的未保存的数据提示用户
					        $.messager.confirm('确认', '附材中有未保存的数据是否继续?',function(res){
					            if(res){
					                useAgeUnDrugArr.length=0;
					                $('#list2').datagrid({
		                        		url:"<%=basePath%>publics/odditionalManage/queryItemListByXId.action?menuAlias=${menuAlias}&flg=unDrug",
		                        		queryParams: {
				                        	drugFlg: 2,
				                        	xId:rowData.encode
				                    	}
		                    		});
					            }
					        });
	                   }else{//不用提醒用户
	                   		 $('#list2').datagrid({
		                        		url:"<%=basePath%>publics/odditionalManage/queryItemListByXId.action?menuAlias=${menuAlias}&flg=unDrug",
		                        		queryParams: {
				                        	drugFlg: 2,
				                        	xId:rowData.encode
				                    	}
		                     });
	                   }
				       index=rowData.id;
				   },
				});
	            
	       }
	     }
	     $('#list1').datagrid({
		    pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			url:'<%=basePath%>publics/odditionalManage/queryUndrugInfoList.action',
			onBeforeLoad: function (param) {//加载数据
				
	        },
	        onLoadSuccess:function(row, data){
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
				}},
			onDblClickRow: function (rowIndex, rowData) {//双击查看 onClickRow
			    var tempId=rowData.code;
			    var drugOrUseAgeId = getIdUtilMessage('#listDrug','药品用法或非药品');//选中的用法id
			    if(drugOrUseAgeId!=null){
			      var tempDefalutPrice=rowData.defaultprice==null?0:rowData.defaultprice;
			          if(document.getElementById("useAgeId").checked==true){
								 $.ajax({
									url:"<%=basePath%>publics/odditionalManage/queryItemByUnDrugId.action",
									data:{
										tempId:tempId,
										drugFlg:1,
										useAgeId:drugOrUseAgeId
								    },
									type:'post',
									success: function(data) {
									     if(data=='1'){//有记录
											  setTimeout(addRow(rowData.name,1,rowData.unit,tempDefalutPrice,parseFloat(parseFloat(1)*parseFloat(tempDefalutPrice).toFixed(2)).toFixed(2),'24',drugOrUseAgeId,tempId,1),2000);   
									     }else if(data=='0'){
										    $.messager.alert('提示','该类非药品附材已经存在！','error');
									     }
									},
									error:function(data){
									}
								 });
					      
			        }else if(document.getElementById("unDrugId").checked==true){
			              var typeCode = getTypeCodeUtilMessage("#listDrug");
			              if(typeCode==null){
			                  addRow(rowData.name,1,rowData.unit,tempDefalutPrice,parseFloat(parseFloat(1)*parseFloat(tempDefalutPrice).toFixed(2)).toFixed(2),'24',drugOrUseAgeId,tempId,2);
			              }else{
			                   $.ajax({
									url:"<%=basePath%>publics/odditionalManage/queryItemByUnDrugId.action",
									data:{
										tempId:tempId,
										drugFlg:2,
										useAgeId:typeCode
									},
									type:'post',
									success: function(data) {
									     if(data=='1'){//有记录
											  setTimeout(addRow(rowData.name,1,rowData.unit,tempDefalutPrice,parseFloat(parseFloat(1)*parseFloat(tempDefalutPrice).toFixed(2)).toFixed(2),'24',drugOrUseAgeId,tempId,2),2000);   
									     }else if(data=='0'){
										    $.messager.alert('提示','该类非药品附材已经存在！','error');
									     }
									},
									error:function(data){
									}
							});
			              }
			        }
			    }else{
			    	 $('#list1').datagrid("unselectRow", rowIndex);//取消该行选中
			    }
			}    
		});  
		
		//添加新行
		function addRow(itemName,qty,unit,price,totalPrice,useInterval,useAgeId,unDrugId,drugFlag){
		    var canAdd = true;
            for(var i=0;i<useAgeUnDrugArr.length;i++){
	             if(useAgeUnDrugArr[i].selectedUseAgeId==useAgeId&&useAgeUnDrugArr[i].selectedUnDrugId==unDrugId){
	                 $.messager.alert('提示','该类非药品附材已经存在！','error');
	                 canAdd = false;
	                 break;
	             }
	        }
            
            if(canAdd==true){
		        var index = $('#list2').edatagrid('appendRow',{
					itemName: itemName,//名称
					qty: qty,//数量
					unit:unit,//单位
					price:price,//单价
					totalPrice:totalPrice,//总价格
					useInterval:useInterval,//间隔
					useAgeId:useAgeId,
					unDrugId:unDrugId,
					drugFlag:drugFlag,
					id:guid()
				}).edatagrid('getRows').length-1;
				$('#list2').edatagrid('beginEdit',index);
				useAgeUnDrugArr.push({'selectedUseAgeId':useAgeId,'selectedUnDrugId':unDrugId});
            }
		}
		    function guid() {
			    return Math.floor(Math.random()*10)+Math.floor(Math.random()*10)+Math.floor(Math.random()*10);
			}
		//科室id显示name
		function functiondeptid(value,row,index){
			for(var i=0;i<deptidlist.length;i++){
				if(value==deptidlist[i].deptCode){
					return deptidlist[i].deptName;
				}
			}
		}	
		//疾病分类
		function functionDis(value,row,index){
			if(value!=null&&value!=""){
				if(disList[value]!=null&&disList[value]!=""){
					return disList[value];
				}
				return value;
			}
		}
		//为数量添加keyup事件
		function funNum(value,row,index){
			return '<input id="num_'+index+'" type="text" class="datagrid-editable-input numberbox-f textbox-f" value="'+value+'" data-options="min:0,precision:0" onkeyup="keyupNum(this)" style="width:100%"></input>';
		}
		//为间隔时间添加keyup事件
		//2017年2月14日16:03:25  GH 更改间隔项为可修改
		function funInterval(value,row,index){
			return '<input id="interval_'+index+'" type="text" class="datagrid-editable-input numberbox-f textbox-f" value="'+value+'" data-options="min:0,precision:0" onkeyup="keyupInterval(this)" style="width:100%"></input>';
		}
		//为数量添加keyup事件
		function keyupInterval(inpObj){
			if($(inpObj).val().length==1){
				$(inpObj).val($(inpObj).val().replace(/[^1-9]/g,''));
			}else{
				$(inpObj).val($(inpObj).val().replace(/\D/g,''));
			}
			var val = $(inpObj).val();
			var id = $(inpObj).prop("id").split("_");
			var time = 24;
			if(val!=null&&val!=""){
				time = val;
			}
			$('#list2').datagrid('updateRow',{
				index: id[1],
				row: {
					useInterval:time
				}
			});
			$('#interval_'+id[1]).val("").focus().val(val);
		}
		//为金额添加id
		function funPayCost(value,row,index){
			return '<div id="totalPrice_'+index+'">'+value+'</div>';
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
					totalPrice:money
				}
			});
			$('#num_'+id[1]).val("").focus().val(val);
		}
		
	    //按回车键提交表单！
		$('#search').find('input').on('keyup', function(event) {
			if (event.keyCode == 13) {
				searchForm();
			}
		});
		//非药品查询
		function searchForm(){
			var name =	$.trim($('#name').textbox('getText'));
			var jbType =document.getElementsByName("jbType")[0].value;
			var codeName = $.trim($('#codeName').textbox('getText'));
			$('#list1').datagrid('load', {
				name : name,
				jbType:jbType,
				codeName:codeName,
			});
		}
		 
		 
		//hd  获得选中的id
		function getIdUtilMessage(tableID,message){
			   var row = $(tableID).datagrid("getSelections");
		       var i = 0;    
		       var getid = ""; 
		       if(row.length!=1){
		       		$.messager.alert("操作提示", "请选择一条"+message+"记录！","warning");
		       		return null;
		       }else{
		    	   if(document.getElementById("useAgeId").checked==true){
		    		   for(i;i<row.length;i++){ 
							getid = row[i].encode;
					   }
		    	   }else if(document.getElementById("unDrugId").checked==true){
		    		  
		    		   for(i;i<row.length;i++){ 
							getid = row[i].encode;
					   }
		    	   }
				} 
			return getid;	
		}
		//hd 获得选中记录的 其它属性 itemId
		function getAttrUtilMessage(tableID){
			var row = $(tableID).datagrid("getSelections");  
		       var i = 0;    
		       var getid = ""; 
		       if(row.length!=1){
		       		return null;
		       }else{  
					 for(i;i<row.length;i++){ 
				      getid = row[i].itemId; 
					 }
				} 
			return getid;	
		}
		function getTypeCodeUtilMessage(tableID){
			var row = $(tableID).datagrid("getSelections");  
		       var i = 0;    
		       var getid = ""; 
		       if(row.length!=1){
		       		return null;
		       }else{  
					 for(i;i<row.length;i++){ 
				      getid = row[i].typeCode; 
					 }
				} 
			return getid;	
		}
		
		function openBedWin(unDrugId,useAgeId,itemCode,qty,unit,price,total,useInterval){
		   var tempWinPath="<c:url value='/publics/odditionalManage/addItemWin.action?unDrugId='/>"+unDrugId+"&useAgeId="+useAgeId+"&itemCode="+itemCode+"&qty="+qty+"&unit="+unit+"&price="+price+"&total="+total+"&useInterval=";
		   window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth - 1050) +',height='+ (screen.availHeight-700) +',scrollbars,resizable=yes,toolbar=no'); 
		};
		
	  
	  function saveItem(){
		  var rows=$('#list2').datagrid('getRows');
		  if(rows.length==0){
			  $.messager.alert('提示','列表无数据,无法保存');
			  return;
		  }
		  $.messager.confirm('确认', '是否确认保存该数据？', function(res){//提示是否保存
				if (res){
					$('#list2').edatagrid('acceptChanges');
					$('#oddGradeJson').val(JSON.stringify( $('#list2').edatagrid("getRows")));
					$('#editForm').form('submit',{
						url:'<%=basePath%>publics/odditionalManage/saveItem.action',
				        onSubmit:function(){ 
				           return $(this).form('validate');
				        },  
				        success:function(data){ 
				        	$('#queryName').textbox('clear');
				        	var len=$('#list2').datagrid('getRows').length;
				        	var num;
				        	var timer;
				        	for(var i=0;i<len;i++){
				        		timer=$('#interval_'+i).val();
				        		num=$('#num_'+i).val();
				        		$('#num_'+i).parent().text(num);
				        		$('#interval_'+i).parent().text(timer);
				        	}
				        	 $.messager.alert('提示','保存成功！');
				        },
						error : function(data) {
							$.messager.alert('提示','保存失败！');
						}			         
					}); 
				}
		  });
		} 
		function delItem(){
		  var rows = $('#list2').datagrid('getChecked');
		  if(rows.length>0){
				  var copyRows = [];
				  var updateArr=[];
			      for ( var j= 0; j < rows.length; j++) {
			          copyRows.push(rows[j]);
			          if(rows[j].id.length>30){//是数据库已有记录
			            updateArr.push(rows[j]);
			          }
			        	
			      }
			      var updateIds ="";
			      for(var x=0;x<updateArr.length;x++){
			          if(x!=updateArr.length-1){
			             updateIds=updateIds+updateArr[x].id+"_";
			          }else{
			          	 updateIds=updateIds+updateArr[x].id;
			          }
			      }
		           if (copyRows.length > 0) {//选中几行的话触发事件	                        
						$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
							if (res){
								//ajax后台删除数据库已有的数据
								if(updateIds!=""){//先删除后台数据然后从页面进行删除
									   $.ajax({
												url:"<%=basePath%>publics/odditionalManage/delItemByIds.action",
												data:{
													updateIds:updateIds,
													randomNum:guid()
												},
												type:'post',
												success: function(data) {
													 index=null;
												     delPageItem(copyRows);
												     updateArr.length=0;
												     $('#queryName').textbox('setValue','');
												     if(document.getElementById("unDrugId").checked==true){
											        		singleSelectQuery("FOU");
											        	} else{
											        		singleSelectQuery("SHI");
											        	}
												},
												error:function(data){
												      $.messager.alert('提示','删除失败！');
												}
									  });
								}else{//只有页面新添加数据
								    delPageItem(copyRows);
								}
							}
		               });
		          }
		  }else{
		       $.messager.alert("操作提示", "请先选中要删除的记录！","warning");
		  }
		 
		}
		
		function  delPageItem(copyRows){//从页面中删除
			//删除页面删除选中的行
			for(var i=0;i<copyRows.length;i++){
			    var index = $('#list2').datagrid('getRowIndex',copyRows[i]);
			    $('#list2').datagrid('deleteRow', index);
				 //useAgeUnDrugArr数组中删除已删除的记录 {'selectedUseAgeId':useAgeId,'selectedUnDrugId':unDrugId}这种格式
				 if(useAgeUnDrugArr.length>0){
				     useAgeUnDrugArr.remove({'selectedUseAgeId':copyRows[i].useAgeId,'selectedUnDrugId':copyRows[i].unDrugId});
				 }
			}
			$.messager.alert('提示','删除成功！');
		}
		
		 //用于  [{selectedUseAgeId:'',selectedUnDrugId:''}] 这种格式数组数据的删除
		 Array.prototype.indexOf = function(val) {//[{selectedUseAgeId:'',selectedUnDrugId:''}]  删除里面是map的数组 调用删除时应是   {selectedUseAgeId:'',selectedUnDrugId:''}这种格式           
			    for (var i = 0; i < this.length; i++) {  
			        if (this[i].selectedUseAgeId == val.selectedUseAgeId&&this[i].selectedUnDrugId == val.selectedUnDrugId) return i;  
			    }  
			    return -1;  
			};
		    Array.prototype.remove = function(val) {  
		        var index = this.indexOf(val);  
		        if (index > -1) {  
		            this.splice(index, 1);  
		        }  
		    };
		   //附材管理：新增一条附材-> 单击选中一条用法->双击非药品  ->如果该用法和该类费用品已经存在 将不允许再进行新增一条记录    可以在左侧通过双击该用法或该非药品名称查看到该数据  数量可调整。
		   //跳转问题：当新增一条记录后没有进行数据的保存 而又双击了左侧菜单中的用法或非药品 将提示用户是否继续进行页面的跳转
		   
		   function searchX(){
			   var queryName = $.trim($('#queryName').textbox('getText'));
			   if(document.getElementById("unDrugId").checked==true){
				      $('#listDrug').datagrid({    
						    url:"<%=basePath%>publics/odditionalManage/queryAllUnDrug.action",
						    queryParams: {
						    	queryName: queryName
							},
							columns:[[
								{field:'id',width:'20%',hidden:true}, 
								{field:'typeCode',width:'20%',hidden:true}, 
								{field:'encode',width:'20%',hidden:true}, 
						        {field:'unDrugName',title:'非药品',width:'100%'}
						    ]]
						});
			   }else if(document.getElementById("useAgeId").checked==true){
			       $('#listDrug').datagrid({
					    url:"<%=basePath%>publics/odditionalManage/queryUseAge.action",
						queryParams : {
							queryName : queryName
						},
						columns : [ [ {
							field : 'name',title : '药品用法',width : '100%'}
						] ]
					});
				}
			}
		   
			//导出列表
			function exportList() {
				var rows = $('#list2').edatagrid("getRows");
				if(rows.length==0){
					$.messager.alert("操作提示", "列表无数据,无法导出!");
					return;
				}
				$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
					if (res) {
						
						//为了结束编辑行 不然获取的rows里没有编辑状态下的数据
						for ( var i = 0; i < rows.length; i++) {
							$('#list2').edatagrid("endEdit",
									$('#list2').edatagrid("getRowIndex", rows[i]));
						}
						$('#oddGradeJson').val(JSON.stringify(rows));
						$('#editForm').form('submit', {
							url :"<%=basePath%>publics/odditionalManage/exports.action",
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
			
			// 列表查询重置
			function searchReload() {
				$('#jbType').combobox('setValue','000');
				$('#name').textbox('setValue','');
				$('#codeName').textbox('setValue','');
				searchForm();
			}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</html>
