<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	<div id="queryBedInfoTabs" class="easyui-tabs"data-options="tabPosition:'top',border:false,showHeader:false,fit:true">
		<div title="卡片视图" data-options="border:false">
			<table id="cardView" class="easyui-datagrid"  
				data-options="singleSelect:true,pagination:true,pageSize:20,pageList:[20,50,100],fit:true,border:false">
				<thead>
					<tr>
						<th
							data-options="field:'patientName',width:100,align:'center',hidden:true">患者姓名</th>
						<th
							data-options="field:'sex',width:100,align:'center',hidden:true">患者性别</th>
						<th
							data-options="field:'inpatientNo',width:100,align:'center',hidden:true">病历号</th>
						<th
							data-options="field:'deptName',width:100,align:'center',hidden:true">住院科室</th>
						<th
							data-options="field:'bedName',width:100,align:'center',hidden:true">住院病床</th>
						<th
							data-options="field:'souceName',width:100,align:'center',hidden:true">入院来源</th>
						<th
							data-options="field:'reportBirthday',width:100,align:'center',hidden:true">年龄</th>
						<th
							data-options="field:'inDate',width:100,align:'center',hidden:true">住院日期</th>
						<th
							data-options="field:'inTime',width:100,align:'center',hidden:true">住院次数</th>
						<th
							data-options="field:'huliName',width:100,align:'center',hidden:true">护理</th>
						<th
							data-options="field:'payName',width:100,align:'center',hidden:true">结算类别</th>
						<th
							data-options="field:'userName',width:100,align:'center',hidden:true">住院医生</th>
						<th
							data-options="field:'diagName',width:100,align:'center',hidden:true">诊断</th>
					</tr>
				</thead>
			</table>
		</div>
		<div title="列表视图">
			<table id="listView" class="easyui-datagrid" 
				data-options="rownumbers:true,singleSelect:true,pagination:true,
				pageSize:20,pageList:[20,50,100],fitColumns:true,fit:true,border:false">
				<thead>
					<tr>
						<th data-options="field:'patientName',width:'9%',align:'left'">姓名</th>
						<th
							data-options="field:'sex',width:'4%',align:'center'">性别</th>
						<th
							data-options="field:'inpatientNo',width:'11.5%',align:'center'">病历号</th>
						<th
							data-options="field:'deptName',width:'11%',align:'center'">科室</th>
						<th
							data-options="field:'bedName',width:'5%',align:'center'">病床</th>
						<th
							data-options="field:'souceName',width:'11%',align:'center'">入院来源</th>
						<th data-options="field:'reportBirthday',width:'5%',align:'center',formatter:funAges">年龄</th>
						<th data-options="field:'inDate',width:'11%',align:'center'">入院日期</th>
						<th data-options="field:'inTime',width:'4%',align:'center'">住院次数</th>
						<th data-options="field:'linkmanName',width:'9%',align:'center'">联系人</th>
						<th data-options="field:'linkmanTel',width:'9%',align:'center'">联系人电话</th>
						<th data-options="field:'patientStatus',width:'9%',align:'center',formatter:funStatus">患者状态</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<script type="text/javascript">
	var stateMap=new Map();
	var toolArr = new Array('首页','上一页','下一页','尾页','刷新','切换视图','切换视图');
$(function(){
	//卡片视图
	   $("#cardView").datagrid({
		   url:'<%=basePath%>nursestation/nurse/cardBedInfoAllList.action',
		   view: cardview,
		   onBeforeLoad:function(param){
				 var pager3 = $('#cardView').datagrid('getPager');    // 得到datagrid的pager对象  
				 pager3.pagination({
					    showPageList:true,
					    buttons:[{    
					        iconCls:'icon-reverse_blue',    
					        handler:function(){    
					        	 $("#queryBedInfoTabs").tabs('select',0);   
					        	 $('#printReport').linkbutton('disable');
					        }    
					    },{    
					        iconCls:'icon-resultset_next',    
					        handler:function(){    
					            $("#queryBedInfoTabs").tabs('select',1); 
					            $('#printReport').linkbutton('enable');
					            $('#listView').datagrid({
					            	url:'<%=basePath%>nursestation/nurse/cardBedAllList.action',
					            	onBeforeLoad:function(param){
					            		 var pager3 = $('#listView').datagrid('getPager');    // 得到datagrid的pager对象  
					            			pager3.pagination({    
					            			    showPageList:true,    
					            			    buttons:[{  
					            			    	title:'切换',
					            			        iconCls:'icon-reverse_blue',    
					            			        handler:function(){    
					            			        	 $("#queryBedInfoTabs").tabs('select',0);    
					            			        	 $('#printReport').linkbutton('disable');
					            			        }    
					            			    },{    
					            			        iconCls:'icon-resultset_next',    
					            			        handler:function(){    
					            			            $("#queryBedInfoTabs").tabs('select',1);
					            			            $('#printReport').linkbutton('enable');
					            			        }    
					            			    }],
					            			});
					            	}
					            })
					        }    
					    }] 
					});
		   }
	   })
	   
	   $.ajax({
		   url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=status",
			type:'post',
			success: function(data) {
				var type = data;
				for(var i=0;i<type.length;i++){
					stateMap.put(type[i].encode,type[i].name);
				}
			}
		});
})

var cardview = $.extend({}, $.fn.datagrid.defaults.view, {
    renderRow: function(target, fields, frozen, rowIndex, rowData){
    	if(!$.isEmptyObject(rowData)){
    		var cc = [];
            cc.push("<div colspan=" + fields.length + " style='margin:15px;padding:20px 10px;float:left;background-color: #c8dae4;border-radius: 14px;width:180px;height:270px' class='nurseBedView'>");
            if (!frozen){
                cc.push("<div style='float:right'>");
                for(var i=0; i<fields.length; i++){
                	var val = null;
                	if(fields[i]=='reportBirthday'){
                		var ages=DateOfBirth(rowData[fields[i]]);
                		val = ages.get("nianling")+ages.get('ageUnits');
                	}else{
                		val = rowData[fields[i]];
                	}
                	var copts = $(target).datagrid('getColumnOption', fields[i]);
                	cc.push("<p  style='height:20px;padding:0px 20px'><span class='c-label'>" + copts.title + ":</span>" + val + "</p>");
                }
                cc.push("</div>");
            }
            cc.push("</div>");
          return cc.join(''); 
    	};
        return '';
    }
});

function funStatus(value){
	if(value!=null&&value!=''){
		return stateMap.get(value);
	}
}
function funAges(value){
	if(value!=null&&value!=''){
		var ages=DateOfBirth(value);
		return ages.get("nianling")+ages.get('ageUnits');
	}
}
function hiden1(){
	var pager = $("#cardView").datagrid('getPager');
	var aArr = $(pager).find('a');
	var iArr = $(pager).find('input');
	$(iArr[0]).tooltip('hide');	
	for(var i=0;i<aArr.length;i++){
		$(aArr[i]).tooltip('hide');
	}
}
function hiden2(){
	var pager = $("#listView").datagrid('getPager');
	var aArr = $(pager).find('a');
	var iArr = $(pager).find('input');
	$(iArr[0]).tooltip('hide');	
	for(var i=0;i<aArr.length;i++){
		$(aArr[i]).tooltip('hide');
	}
}
</script>
</body>
</html>