<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body>
		<div class="easyui-layout" data-options="fit:true,border:true">   
			<div data-options="region:'north',border:false,split:false" style="height:40px;padding-left:5px;padding-top:5px;">
				<input id="analysisQueryName">
				<a href="javascript:void(0)" onclick="analysisSearchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
			</div>   
			<div data-options="region:'center',border:false" class="analysis">
				<table id="analysisEdId">
					<thead>
						<tr>
							<th data-options="field:'order',width:80,halign:'center',align:'right',rowspan:2">序号</th>
							<th data-options="field:'name',width:140,halign:'center',align:'right',rowspan:2">疾病分类</th>
							<th data-options="field:'sex',width:80,halign:'center',align:'right',rowspan:2">患者人数</th>
							<th data-options="field:'age',width:140,halign:'center',align:'right',colspan:2">性别分布</th>
							<th data-options="field:'address',width:140,halign:'center',colspan:4">年龄分布</th>
							<th data-options="field:'h',width:140,halign:'center',align:'right',colspan:2">区域分布</th>
							<th data-options="field:'j',width:100,halign:'center',align:'right',rowspan:2,styler:stylerJ">诊疗费用(元)</th>
							<th data-options="field:'dept',width:100,halign:'center',align:'right',rowspan:2,styler:stylerDept">药品费用(元)</th>
							<th data-options="field:'doctor',width:100,halign:'center',align:'right',rowspan:2,styler:stylerDoctor">手术费用(元)</th>
						<tr>
						<th data-options="field:'nan',halign:'center',align:'right',styler:stylerNan">男</th>
						<th data-options="field:'nv',halign:'center',align:'right',styler:stylerNv">女</th>
						<th data-options="field:'xiaoyu',halign:'center',align:'right',styler:stylerXiaoyu">小于20</th>
						<th data-options="field:'zyts',halign:'center',align:'right',styler:stylerZyts">20-35</th>
						<th data-options="field:'zyylfy',halign:'center',align:'right',styler:stylerZyylfy">35-55</th>
						<th data-options="field:'yishang',halign:'center',align:'right',styler:stylerYishang">55以上</th>
						<th data-options="field:'chengshi',halign:'center',align:'right',styler:stylerChengshi">城市</th>
						<th data-options="field:'nongcun',halign:'center',align:'right',styler:stylerNongcun">农村</th>
						</tr>
					</thead>
				</table>
			</div> 
		</div>
		<script type="text/javascript">
		$('#analysisEdId').datagrid({
			striped:true,
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			url:'<%=basePath%>baseinfo/outpatientAdvice/outpatientAdviceAnalysisList.action',
			method:'get',
			fit:true,
			onLoadSuccess:function(data){
				var merges = [{
					index: 13,
					colspan: 2
				}];
				for(var i=0; i<merges.length; i++){
					$(this).datagrid('mergeCells',{
						index: merges[i].index,
						field: 'order',
						colspan: merges[i].colspan
					});
				}
			}
		});
		$('#analysisQueryName').combobox({
			data:[{id:'1',text:'CT门诊'},{id:'2',text:'内科门诊'},{id:'3',text:'口腔科'},
			      {id:'4',text:'骨科'},{id:'5',text:'妇科'},{id:'6',text:'皮肤科'},
			      {id:'7',text:'体检科'},{id:'8',text:'鼻科'},{id:'9',text:'眼科'}],
			valueField:'id',
			textField:'text',
			onLoadSuccess:function(){
				$(this).combobox('select','1');
			},
			onSelect:function(record){
				$('#analysisEdId').datagrid('load',{id:record.id});
			}
		});
		function stylerNan(value,row,index){
			if(parseInt(value)>parseInt(row.nv)){
				return 'color:#FF0000;';
			}
		}
		function stylerNv(value,row,index){
			if(parseInt(value)>parseInt(row.nan)){
				return 'color:#FF0000;';
			}
		}
		function stylerXiaoyu(value,row,index){
			if(parseInt(value)>parseInt(row.zyts)&&parseInt(value)>parseInt(row.zyylfy)&&parseInt(value)>parseInt(row.yishang)){
				return 'color:#FF0000;';
			}
		}
		function stylerZyts(value,row,index){
			if(parseInt(value)>parseInt(row.xiaoyu)&&parseInt(value)>parseInt(row.zyylfy)&&parseInt(value)>parseInt(row.yishang)){
				return 'color:#FF0000;';
			}
		}
		function stylerZyylfy(value,row,index){
			if(parseInt(value)>parseInt(row.xiaoyu)&&parseInt(value)>parseInt(row.zyts)&&parseInt(value)>parseInt(row.yishang)){
				return 'color:#FF0000;';
			}
		}
		function stylerYishang(value,row,index){
			if(parseInt(value)>parseInt(row.xiaoyu)&&parseInt(value)>parseInt(row.zyts)&&parseInt(value)>parseInt(row.zyylfy)){
				return 'color:#FF0000;';
			}
		}
		function stylerChengshi(value,row,index){
			if(parseInt(value)>parseInt(row.nongcun)){
				return 'color:#FF0000;';
			}
		}
		function stylerNongcun(value,row,index){
			if(parseInt(value)>parseInt(row.chengshi)){
				return 'color:#FF0000;';
			}
		}
		function stylerJ(value,row,index){
			if(parseInt(value)>parseInt(row.dept)&&parseInt(value)>parseInt(row.doctor)){
				return 'color:#FF0000;';
			}
		}
		function stylerDept(value,row,index){
			if(parseInt(value)>parseInt(row.j)&&parseInt(value)>parseInt(row.doctor)){
				return 'color:#FF0000;';
			}
		}
		function stylerDoctor(value,row,index){
			if(parseInt(value)>parseInt(row.j)&&parseInt(value)>parseInt(row.dept)){
				return 'color:#FF0000;';
			}
		}
		</script>
	</body>
</html>