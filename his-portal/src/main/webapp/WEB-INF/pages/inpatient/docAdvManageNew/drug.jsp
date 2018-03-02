<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body style="margin: 0px;padding: 0px">
		<div id="drugEl" class="easyui-layout" data-options="fit:true">   
			<div data-options="region:'north',border:false" style="height:42px;">
				<div style="padding:5px 5px 5px 5px;">
					<table>
						<tr>
							<td>
								药品查询：
								<input id="drugName" name="drugName" style="width:200px;"/>
								<a href="javascript:searchFromDrug()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							</td>
						</tr>
					</table>
				</div>
			</div>   
			<div data-options="region:'center'" class="drug">
				<div style="width:100%;height:100%;">
					<table id="listdurg">
						<thead>
							<tr>
							<th data-options="field:'drugCommonname'" style="width:18%">通用名称</th>
								<th data-options="field:'drugCnamepinyin'" style="width:8%">名称拼音码</th>
								<th data-options="field:'drugCnamewb'" style="width:8%">名称五笔码</th>
								<th data-options="field:'drugCnameinputcode'" style="width:12%">名称自定义码</th>
								<th data-options="field:'spec'" style="width:12%">规格</th>
								<th data-options="field:'drugType',formatter:typemapFamater" style="width:8%">药品类别</th>
								<th data-options="field:'drugPackagingunit',formatter:drugpackagingunitFamater" style="width:8%">包装单位</th>
								<th data-options="field:'packagingnum'" style="width:8%">包装数量</th>
								<th data-options="field:'drugBasicdose'" style="width:8%">基本剂量</th>
							</tr>
						</thead>
					</table>
				</div>
			</div> 
		</div>
		<script type="text/javascript">
		var drugpackagingunitMap="";
		var typemap="";
		$('#drugName').textbox({
			prompt:'药品名称,拼音,五笔,自定义'
		});
		//加载页面
		$('#listdurg').datagrid({
			method:'post',
			rownumbers:true,
			idField: 'id',
			striped:true,
			border:false,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			fit:true,
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			url:'<%=basePath%>drug/info/queryDrug.action',
			onLoadSuccess : function(data){
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
		});
		bindEnterEvent('drugName',searchFromDrug,'easyui');
		//查询包装单位
		$.ajax({
			url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action",		
			data:{type:'packunit'},
			type:'post',
			success: function(drugpackagingunitdata) {					
				drugpackagingunitMap= drugpackagingunitdata;	
			}
		});
		//查询包装单位
		$.ajax({
			url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action",		
			data:{type:'drugType'},
			type:'post',
			success: function(data) {					
				typemap= data;	
			}
		});
		//最小单位 显示		
		function drugpackagingunitFamater(value,row,index){			
			if(value!=null&&value!=""){
				return drugpackagingunitMap[value];					
			}			
		}
		//最小单位 显示		
		function typemapFamater(value,row,index){			
			if(value!=null&&value!=""){
				return typemap[value];					
			}			
		}
		</script>
	</body>
</html>