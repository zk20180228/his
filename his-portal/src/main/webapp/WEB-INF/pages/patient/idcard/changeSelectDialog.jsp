<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript">
		
</script>
</head>
<body>
	<div style="padding: 3px 5px 5px 5px;height: 7%">
		&nbsp;变更时间:
		<input id="start" class="Wdate" type="text" onClick="var endDate=$dp.$('end');WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:function(){endDate.focus();},maxDate:'#F{$dp.$D(\'end\')}'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/> 至
		<input id="end" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'start\')}'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
		&nbsp;变更类型:
		<input id="type"  class="easyui-combobox" style="width:100px;"/>
		<a herf="javascript:void(0)" onclick="query()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
		<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
	</div>
	<div style="padding: 0px 5px 5px 5px;height: 88%">
		<table id="selectDialoglist" style="width: 100%;hight:100%;" data-options="fit:true"></table>
	</div>
	
<script type="text/javascript">
	var userMap=null;//人员
	function query(){
		var type=$('#type').combobox('getValue');
		var start=$('#start').val();
		var end=$('#end').val();
		if(start&&end){
			if(start>=end){
				$.messager.alert("提示","结束时间必须大于开始时间");
				return;
			}
		}
		$('#selectDialoglist').edatagrid('load',{
			patientId:$('#ids').val(),
			type:type,
			startTime:start,
			endTime:end
		});
	}


	$(function(){
		$('#type').combobox({
			valueField:'id',
			textField:'value',
			data:[{
				id:1,
				value:'补卡'
			},{
				id:2,
				value:'退卡'
			}
			]
		})
		//查询多条患者的数据窗口
		$('#selectDialoglist').edatagrid({
			url:"<%=basePath%>patient/idcardChange/queryChange.action", 
			queryParams:{
				patientId:$('#ids').val()
			},
			pagination:true,
			pageSize:20,
	   		pageList:[10,20,30,50],
			striped:true,
			border:true,
			selectOnCheck:false,
			rownumbers:true,
			idField: 'id',
   			fitColumns:true,
   			singleSelect:true,
   			checkOnSelect:false, 
   			onBeforeLoad:function(){
   			//查询userMap
   				$.ajax({
   					url:'<%=basePath%>patient/idcardChange/queryUser.action',
   					async:false,
   					success:function(data){
   						userMap=data;
   					}
   				});
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
				}
			},
   			columns:[[{field:'oldIdcardNo',title:'旧卡卡号',width : '18%'},
			          {field:'oldIdcardType',title:'旧卡类型', width : '10%',
			        	  formatter:function(value,row,index){
			        		  if(value==1){
			        				return '磁卡';
			        			}else if(value==2){
			        				return 'IC卡';
			        			}else if(value==3){
			        				return '保障卡';
			        			}else if(value==0){
			        				return '就诊卡';
			        			}
			        	  }
			          },
   				      {field:'idcardNo',title:'新卡卡号',width : '18%'},
			          {field:'idcardType',title:'新卡类型', width : '10%',
			        	  formatter:function(value,row,index){
			        		  if(value==1){
			        				return '磁卡';
			        			}else if(value==2){
			        				return 'IC卡';
			        			}else if(value==3){
			        				return '保障卡';
			        			}else if(value==0){
			        				return '就诊卡';
			        			}
			        	  }
			          },
			          {field:'changeStatus',title:'变更类型', width : '10%',
			        	  formatter:function(value,row,index){
			        		  if(value==1){
			        				return '补卡';
			        			}else if(value==2){
			        				return '退卡';
			        			}
			        	  }
			          },
			          {field:'createUser',title:'变更人', width : '12%',formatter:funcuser},
			          {field:'createTime',title:'变更时间', width : '20%'}
			  ]]
		});
	});
	
	// 药品列表查询重置
	function searchReload() {
		$('#start').val('');
		$('#end').val('');
		$('#type').combobox('setValue','');
		query();
	}
	
	/**  
	 *  
	 * @Description：渲染员工
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	function funcuser(value,rowData,rowIndex) {
		if(value!=null&&value!=""){
			return userMap[value];
		}
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>