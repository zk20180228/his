<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>物资调价单</title>
<%@ include file="/common/metas.jsp" %>
<style type="text/css">
#form1 .datagrid-wrap {
	border-left:0
}
</style>
</head>
<body class="easyui-layout" style="margin: 0px;padding: 0px;" data-options="fit:true">   
    <div data-options="region:'north'" style="height:50px;border-top:0">
    	<div style="text-align: left; padding: 5px 5px 5px 0px;">
    	<shiro:hasPermission name="${menuAlias}:function:query">
    		<a href="javascript:funadd()" class="easyui-linkbutton" style="margin:0 0 0 1%;" data-options="iconCls:'icon-add'">新建</a>
   		</shiro:hasPermission>
   		<shiro:hasPermission name="${menuAlias}:function:delete">
    		<a href="javascript:fundel()" class="easyui-linkbutton" style="margin:0 0 0 1%;" data-options="iconCls:'icon-remove'">删除</a>
   		</shiro:hasPermission>
   		<shiro:hasPermission name="${menuAlias}:function:save">
    		<a href="javascript:save()" class="easyui-linkbutton" style="margin:0 0 0 1%;" data-options="iconCls:'icon-save'">保存</a>
   		</shiro:hasPermission>
   		<shiro:hasPermission name="${menuAlias}:function:query">
    		<a href="javascript:funtjd()" class="easyui-linkbutton" style="margin:0 0 0 1%;" data-options="iconCls:'icon-note'">调价单</a>
    	</shiro:hasPermission>
    	<shiro:hasPermission name="${menuAlias}:function:view">
    		<a href="javascript:winDate()" class="easyui-linkbutton" style="margin:0 0 0 1%;" data-options="iconCls:'icon-date'">日期</a>
     	</shiro:hasPermission>
     	</div>
    </div> 
    <div id='p' data-options="region:'west'" style="width:20%;height:93%;padding-left:5px" data-options="fit:true">
		<div id='d1'>
			<div id="treeDiv" style="width: 100%; overflow-y: auto;">
				<ul id="tDt">数据加载中...</ul>
			</div>
		</div>
		<div id='d2' style="display: none">
		<div data-options="region:'north'" style="width:100%;height: 10%;" data-options="fit:true">
			<table style="width:100%; height:100%" data-options="fit:true">
				<tr>
					<td style="font-size: 14px" >
						查询条件：<input class="easyui-textbox" id="queryName" name="queryName" data-options="prompt:'物资名称，别名，物资编码'"  style="width:200px">
						<a href="javascript:searchFrom()" class="easyui-linkbutton" iconCls="icon-search" >查询</a>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center'" style="width:100%;height: 90%;float: right" data-options="fit:true" >
			<table id="materialList" class="easyui-edatagrid" style="width:100%; height:100%" data-options="fit:true"></table>
		</div>
		
		</div>
    </div>   
    <div id="divLayout" data-options="region:'center',fit:true"style="width:80%;height:93%;">
    	<div style="width:100%;height:10%" >
			<span style="padding-left: 40%;font-size:26px" class="wzadjustTit">调价单</span>
		</div>
		<form id='form1' method="post" enctype="multipart/form-data" style="width:100%;height:84%" data-options="fit:true">
			<div style="margin: 1% 0 0 0 ;width:100%;height:8%" >
				<span style="font-size: 16px" class="adjustNum">调价单号：</span>
				<span id='adjustListCodeView'></span>
				<input type='hidden' id='adjustListCode' name='tjdh'/>
				<span style="margin: 0 0 0 6%;">生效时间：
					<input id="inureTime" class="Wdate" type="text" name='zxsh' data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					<input id='ischange' type="checkbox" name='ljzx' value='1' style="margin: 0 0 0 7%"/>
					<span>立即生效</span>
					<span style="margin: 0 0 0 4%;">操作人：</span>
					<span id='updateUser' ></span>
					<span style="margin: 0 0 0 4%;">操作时间：</span>
					<span id='updateTime'></span>
				</span>
			</div>
			<div style="padding:5px 5px 5px 5px; width:100%;height:15%">
				<table style="width:100%; height:90%;border:1px solid #95b8e7;padding:2px;" class="changeskin">
					<tr>
						<td style="padding: 5px 15px;" >
							<!-- 查询条件：
							<input  id="codes" data-options="prompt:'物品名称'"  class="easyui-textbox"  style="width: 200px" onkeyup="KeyDown()"/>
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a> -->
							<span style="padding-bottom:0;margin: 0 0 0 10%">红色调赢，蓝色调亏，黑色无变化</span>
						</td>
					</tr>
				</table>
			</div>
			<div style="width:100%;height:76%;">
				<table id="wzadjustPriceList" class="easyui-datagrid" 
				data-options="fit:true">
				</table>
			</div>
		</form>
	 	<div id="dialog"></div> 
    </div>   
</body>   
<script type="text/javascript">
	var packUnitList = "";//格式化单位所用
	var m='0';//用于判断调价单是否已执行
	var arrIds=[];//记录已添加的条目Id
	var updateUser = "";//渲染操作人
	//渲染操作人
	function functionUpdateUser(value,row,index){
		if(value!=null&&value!=''){
			return updateUser[value];
		}
	}
	$(function(){
// 		//78:页面顶部logo的高度，30:菜单栏的高度，27：tab栏的高度，
// 		var winH=$("body").height();
// 		$('#p').height(winH-78-30-27-14);
// 		$('#d2').height(winH-78-30-27-14);
// 		$('#tDt').height(winH-78-30-27-14);
		
		//回车事件
		bindEnterEvent('queryName',searchFrom,'easyui');
		//渲染最小包装单位
// 		$.ajax({
// 			url:"<c:url value='/comboBox.action'/>", 
// 			data:{"str" : "CodeDrugpackagingunit"},
// 			type:'post',
// 			success: function(packUnitdata) {
// 				packUnitList = eval("("+ packUnitdata +")");
// 			}
// 		});
		
		$("#ischange").click(function(){
			if($(this).is(":checked")){
				var date =new Date(); //会乱码，会乱码，会乱码!!! 
				var year = date.getFullYear();
				var month = date.getMonth()+1;
				if(Number(month)<10){
					month="0"+month;
				}
				var day = date.getDate();
				if(Number(day)<10){
					day="0"+day;
				}
				var hour = date.getHours();
				if(Number(hour)<10){
					hour="0"+hour;
				}
				var minute=date.getMinutes();
				if(Number(minute)<10){
					minute="0"+minute;
				}

				var second=date.getSeconds();
				if(Number(second)<10){
					second="0"+second;
				}
				
				var nowTime = year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
				$("#inureTime").val(nowTime);
			}else{
				$("#inureTime").val("");
			}
		});
		
		
		tree();
		//调价详细
		$('#wzadjustPriceList').datagrid({
			url:'<%=basePath%>material/wzadjustPrice/wzadjustPriceList.action',
			selectOnCheck:false,rownumbers:true,
   			fitColumns:false,singleSelect:true,checkOnSelect:false,
   			autoSave:true,
			columns:[[
			          {field:'ck',checkbox:'true'},
			          {field:'id',title:'id',hidden:'true',
			        		formatter:function(value,row,index){
			        			return "<input class='easyui-numberbox' name='idList["+index+"]' value='"+(row.id==null?'':value)+"'></input>";
			        		}  
			          },
			          {field:'itemCode',title:'物品编码',hidden:'true',width : '10%',
			        	  formatter:function(value,row,index){
			        		  return "<input class='easyui-numberbox' name='itemList["+index+"]' value='"+(value==null?'':value)+"'></input>";
			        	  }
			          },//在新增时实际值为新增物品的id(利于数据传输)
			          {field:'itemName',title:'物品名称[规格]',width : '10%',
			        	  formatter: function(value,row,index){
			        		  if(row.specs==null){
			        			  return value;
			        		  }
			        		  return value+'['+row.specs+']';
			        	  }  
			          },
			          {field:'preSalePrice',title:'原零售价',width : '10%'},
			          {field:'salePrice',title:'现零售价',width : '10%',
			        	  formatter:function(value,row,index){
		        			  return "<input class='easyui-numberbox' id='salePrice"+index+"' name='salePriceList["+index+"]' value='"+(value==null?'':value)+"'></input>";
		       			  }
			          },
			          {field:'viewPrice',title:'调整金额',width : '10%',
			        		formatter:function(value,row,index){
			        			if(row.salePrice!=null){
			        				value = Number(row.salePrice)-Number(row.preSalePrice);
			        			}
			        			return value;
			        		}  
			          },
			          {field:'adjustFileNo',title:'调价文件号',width : '10%',
			        	  formatter:function(value,row,index){
	        			  	return "<input class='easyui-textbox' name='adjustFileNoList["+index+"]' value='"+(value==null?'':value)+"'></input>";	
			       		  }
			          },
			          {field:'memo',title:'备注',width : '10%',
			        	  formatter:function(value,row,index){
		        			  return "<input class='easyui-textbox' name='memoList["+index+"]' value='"+(value==null?'':value)+"'></input>";	
			       		  }
		        	  },
			          {field:'adjustFileId',title:'调价依据',width : 160,
 			     			formatter:function(value,row,index){
//  			     				if("undefined"==typeof(value)){
//  			     				}
 			     				var v = typeof(value) == "undefined" ? "选择文件":value;
// 			       				return "<span><input type='file' onchange='putFileName("+index+")' id='wenjian_"+index+"' name='fileList["+index+"]' style='width: 200%;'></span>";
// 			       				return "<span><input type='file' onchange='putFileName("+index+")' id='wenjian_"+index+"' name='fileOne' style='width: 150px;display:none'><input id='wenjianName_"+index+"' name='wjList["+index+"]' value='"+v+"' onclick='chooseFile("+index+")'/></span>";
			       				return "<span><input type='file' onchange='putFileName("+index+")' id='wenjian_"+index+"' name='fileList["+index+"]' style='width: 150px;display:none'><input id='wenjianName_"+index+"' name='wjList["+index+"]' value='"+v+"' onclick='chooseFile("+index+")'/></span>";
			       			}	
 			     	  },
 			     	  {field:'adjustGist',title:'操作',width: 100,
 			     			formatter:function(value,row,index){
 			     				if("undefined"==typeof(value)){
 			     					return null
			     				}else{
 			     				
 			     					return "<a href='#' id='v_"+index+"' name='adjustGist' onclick='checkFiles("+index+")' class='easyui-linkbutton'></a>";
			     				}
 			     			}  
 			     	  }
			  ]],
			  onSelect: function(rowIndex, rowData){
					$('#salePrice'+rowIndex).blur(function(){
						var a=$('#salePrice'+rowIndex).val();
						var b=rowData.preSalePrice;
						$('#wzadjustPriceList').datagrid('updateRow',{
							index: rowIndex,
							row: {
								viewPrice:a-b
							}
						});
						$('#salePrice'+rowIndex).val(a);
					});
	   			},
			  rowStyler:function(index,row){
				  value = Number(row.salePrice)-Number(row.preSalePrice);
					if(value<0){
						return 'color:blue;';
					}
					if(value>0){
						return 'color:red;';
					}
					if(row.viewPrice<0){
						return 'color:blue;';
					}
					if(row.viewPrice>0){
						return 'color:red;';
					}
				},
				onLoadSuccess:function(data){ 
			        $("a[name='adjustGist']").linkbutton({text:'查看文件',plain:true,iconCls:'icon-see'});    
			}, 
		});	
		
	});
	//查看文件
	function checkFiles(checkFilePath){
		if(checkFilePath!=null){
 			window.open($('#wzadjustPriceList').datagrid('getRows')[checkFilePath].adjustGist); 
		}else{
			$.messager.alert('提示',"尚未上传调价文件");
		}
	}
	//给file框赋值
	function chooseFile(idFile){
		var idClick = "wenjian_"+idFile;
		$('#'+idClick).trigger("click");
// 		document.Form1.idClick.click();
	}
	//选择完后讲文件名给input
	function putFileName(idInput){
		var idOnChange = "wenjianName_"+idInput;
		var idClick = "wenjian_"+idInput;
// 		var fileName = $('#'+idClick).files;
		var f = document.getElementById(idClick).files;  
// 		fileList[f];
		$('#'+idOnChange).val(f[0].name);
	}
	function tree(){
		$('#tDt').tree({
			url : '<%=basePath %>material/wzadjustPrice/wzadjustPriceTree.action',
			method:'get',
			lines : true,
			cache : false,
			animate : true,
			formatter : function(node) {//统计节点总数
				var s = node.text;
				if (node.children) {
					s += '&nbsp;<span style=\'color:blue\'>('
							+ node.children.length + ')</span>';
				}
				return s;
			},onClick : function(node) {//点击节点
				if(node.id!=1){
					$('#adjustListCodeView').text(node.id);
					$('#adjustListCode').val(node.id);
					$('#inureTime').val(node.attributes.inureTime);
					$('#updateTime').text(node.attributes.updateTime);
				    var userId=	node.attributes.updateUser;
					//渲染操作人
					$.ajax({
						url:'<%=basePath%>baseinfo/employee/querySysEmployeeByJobNo.action?jobNot='+userId, 
						type:'post',
						success: function(updateUserData) {
							updateUser = updateUserData;
							$('#updateUser').text("");
					        $('#updateUser').text(updateUser.name);
						}
					});
					m = node.attributes.state;
					$('#wzadjustPriceList').datagrid('load', {    
						adjustListCode:node.id
					}); 
					funwzadjustPriceList();
				}
			},onLoadSuccess : function(node, data) {//默认选中
				$('#tDt').tree('select',$('#tDt').tree('find', '1').target);
			}
		});
	}
	
	//物品列表
	function funmaterialList(){
		$('#adjustListCodeView').text('');
		m='0';
		$('#materialList').datagrid({
			url:'<%=basePath%>material/base/queryMatBaseinfo.action',
			selectOnCheck:false,
			idField: 'id',
			pageSize:20,
			pagination:true,
   			fitColumns:false,
   			singleSelect:true,
   			checkOnSelect:false,
   			columns:[[{field:'id',hidden:'true'},
   			          {field:'itemCode',title:'物品编码',width:'20%'},
   			          {field:'itemName',title:'物品名称',width:'30%'},
   			          {field:'specs',title:'规格',width:'15%'},
   			          {field:'salePrice',title:'单价',width:'20%'},
   			          {field:'minUnit',title:'单位',width:'15%',
//    			        	  formatter:function(value,row,index){
// 	   			        		if(value!=null){
// 	   			 				for(var i=0;i<packUnitList.length;i++){
// 	   			 					if(value==packUnitList[i].id){
// 	   			 						return packUnitList[i].name;
// 	   			 					}
// 	   			 				}	
// 	   			 			}
//    			        	  }
   			          }
 			   ]],onDblClickRow:function(index, row){
//  				  var obj=$('#wzadjustPriceList').edatagrid('getData');
				  //判断物品是否存在库存，判断是否已存在
				  $.ajax({
					  url:'<%=basePath%>material/wzadjustPrice/matisaAmple.action?id='+row.id,
						type:'post',
						success: function(data) {
							if(data=='"T"'){//存在库存
								//判断左侧物资是否已在右侧列表中
								var rs=$('#wzadjustPriceList').datagrid('getRows');
								var tag=row.id;
								if(rs.length>0){
									var i=arrIds.indexOf(tag);
									if(i!=-1){
										$.messager.alert("提示", "列表中已有此条记录！","warning");
										$('#wzadjustPriceList').datagrid('uncheckAll');
										$('#wzadjustPriceList').datagrid('checkRow',i);
										return;
									}
								}
								$('#wzadjustPriceList').datagrid('appendRow',{
									  itemCode:row.id,
									  itemName:row.itemName+'['+row.specs+']',
									  preSalePrice:row.salePrice,
								  });
								arrIds.push(row.id);
							}else{
								$.messager.alert('友情提示','此物品尚无库存信息，暂不能调价！');
							}
						}
					});
 			   }
			
		});
	}
	
	//物资调价列表
	function funwzadjustPriceList(){
		
	}
	
	function funreset(){
		$('#form1').form('clear');
		$('#wzadjustPriceList').datagrid('load', {    
			adjustListCode:''
		}); 
	}
	
	//保存
	function save(){
		var a=$("#ischange").is(':checked');
		if(!a){
			var inureTime = $('#inureTime').val();
			if(inureTime==null||inureTime==''){
				$.messager.alert("提示", "请填写生效时间", "info");
				return;
			}
		}
		if(m!='0'){
			$.messager.alert("提示", "只能提交未执行的调价单", "info");
			return;
		}
		$.messager.confirm('提示','确定要提交数据吗?',function(res){
    		if(res){
    			$('#form1').form('submit',{
    				url:'<%=basePath%>material/wzadjustPrice/addwzadjustPrice.action',
    		        onSubmit:function(){ 
    		           return $(this).form('validate');  
    		        },  
    		        success:function(data){
    		        	if(data=='success'){
    		        		$.messager.alert("提示","操作成功！");
    		        		//弹出提示成功的信息,1秒之后自动关闭提示框 
    						setTimeout(function(){
    							window.location='<%=basePath%>material/wzadjustPrice/wzadjustPriceURL.action?menuAlias=WZTJ';
        		        		arrIds=[];
    						},2000);
    		        	}
    		        },
    				error : function(data) {
    					$.messager.alert('提示','保存失败!');	
    				}			         
    			});
    		}
    	});
	}
	
	function fundel(){
		var obj=$('#wzadjustPriceList').datagrid('getChecked');
		var arr =new Array();
		var brr =new Array();
		a=false;
		if(obj.length>0){
			$.each(obj,function(i,n){
				brr[i]=n;
				if(n.id!=null){
					arr[i]=n.id;
					j=i+1;
					if(m=='0'){
					a=true;
					}
				}
			});
			$.messager.confirm('确认对话框','确定要删除吗?', function(r){
				if (r){
					//视图删除
			    	$.each(brr,function(i,n){
			    		$('#wzadjustPriceList').datagrid('deleteRow',$('#wzadjustPriceList').datagrid('getRowIndex',n));
			    		if(arrIds.indexOf(n.itemCode)!=-1){
			    			arrIds.remove(n.itemCode);
			    		}
					});
			    	if(a){
						$.ajax({
							url:'<%=basePath %>material/wzadjustPrice/wzadjustPriceDel.action',
							type:'post',
							traditional:true,//数组提交解决方案
							data:{'ids':arr},
							dataType:'json',
							success:function(data){
			 					if(data=="success"){
			 						tree();
			 						$('#wzadjustPriceList').datagrid('loadData',{total:0,rows:[]});
			 						arrIds=[];
			 					}
							}
						});
					}
				}
			});
		}else{
			$.messager.alert('提示','请选中要删除的行！');
		}
	}
	
	function winDate(){
		Adddilog('请选择日期','<%=basePath%>material/wzadjustPrice/materialListTimeURl.action');
	}
	
	//新建按钮
	function funadd(){
		funreset();
		$('#d1').hide();
		$('#d2').show();
		funmaterialList();
	}
	//调价单按钮
	function funtjd(){
		$('#d2').hide();
		$('#d1').show();
		//tree();
	}
	
	//搜索
	function searchFrom(){
		var queryName = $('#queryName').textbox('getValue');
		$('#materialList').datagrid('load', {
			queryName : queryName
		});
	}
	
	//加载dialog
	function Adddilog(title, url) {
		$('#dialog').dialog({    
		    title: title,    
		    width: '40%',    
		    height:'40%',    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
	   });    
	}
	//打开dialog
	function openDialog() {
		$('#dialog').dialog('open'); 
	}
	//关闭dialog
	function closeDialog() {
		$('#dialog').dialog('close');  
	} 
</script>  
</html>