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
<title></title>
</head>
<body>
<script type="text/javascript">
	$(function(){
		var wayY = '${count.executeWayY}';
		var wayM = '${count.executeWayM}';
		var wayD = '${count.executeWayD}';
		if(wayY==1){
			$('#sqlTRY').show();
		}else{
			$('#sqlTRY').hide();
		}
		if(wayM==1){
			$('#sqlTRM').show();
		}else{
			$('#sqlTRM').hide();
		}
		if(wayD==1){
			$('#sqlTRD').show();
		}else{
			$('#sqlTRD').hide();
		}
		$('#munyType').combobox({
			url:'<%=basePath%>sys/pretreatment/getMenuCombobx.action',
			required:true,
			valueField:'alias',    
		    textField:'name'
	    	,filter:function(q,row){
	    		var keys = new Array();
	    		keys[keys.length] = 'name';
	    		keys[keys.length] = 'alias';
	    		keys[keys.length] = 'pinyin';
	    		keys[keys.length] = 'wb';
	    		return filterLocalCombobox(q, row, keys);
	    	}
		});
		$('#executeTime').combobox({
			required:true,
			valueField: 'id',
			textField: 'text',
			data:[{
				'id' : 3,
				'text' : '年'
			},{
				'id' : 2,
				'text' : '月'
			},{
				'id' : 1,
				'text' : '日'
			}]
		});
		$('#state').combobox({
			required:true,
			valueField: 'id',
			textField: 'text',
			data:[{
				'id' : 1,
				'text' : '开启'
			},{
				'id' : 0,
				'text' : '关闭'
			}]
		});
		$('#executeWayY').combobox({
			required:true,
			valueField: 'id',
			textField: 'text',
			data:[{
				'id' : 1,
				'text' : 'SQL'
			},{
				'id' : 2,
				'text' : '大数据'
			},{
				'id' : 3,
				'text' : '间接'
			}],
			onSelect : function(record){
				if(record.id=="1"){
					$('#sqlTRY').show();
				}else{
					$('#executeSQLY').val("");
					$('#sqlTRY').hide();
				}
			}
		});
		$('#executeWayM').combobox({
			required:true,
			valueField: 'id',
			textField: 'text',
			data:[{
				'id' : 1,
				'text' : 'SQL'
			},{
				'id' : 2,
				'text' : '大数据'
			},{
				'id' : 3,
				'text' : '间接'
			}],
			onSelect : function(record){
				if(record.id=="1"){
					$('#sqlTRM').show();
				}else{
					$('#executeSQLM').val("");
					$('#sqlTRM').hide();
				}
			}
		});
		$('#executeWayD').combobox({
			required:true,
			valueField: 'id',
			textField: 'text',
			data:[{
				'id' : 1,
				'text' : 'SQL'
			},{
				'id' : 2,
				'text' : '大数据'
			},{
				'id' : 3,
				'text' : '间接'
			}],
			onSelect : function(record){
				if(record.id=="1"){
					$('#sqlTRD').show();
				}else{
					$('#executeSQLD').val("");
					$('#sqlTRD').hide();
				}
			}
		});
		if($('#startTimeH').val()){
			var tmpVal = $('#startTimeH').val()
			$('#startTime').val(tmpVal.substring(0,19))
		}
		if($('#endTimeH').val()){
			var tmpVal = $('#endTimeH').val()
			$('#endTime').val(tmpVal.substring(0,19))
		}
		if($('#executeWay').val()==1){
			$('#sqlTR').show();
		}
	});
	function filterLocalCombobox(q, row, keys){
		if(keys!=null && keys.length > 0){
			for(var i=0;i<keys.length;i++){ 
				if(row[keys[i]]!=null&&row[keys[i]]!=''){
						var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1;
						if(istrue==true){
							return true;
						}
				}
			}
		}else{
			var opts = $(this).combobox('options');
			return row[opts.textField].indexOf(q.toUpperCase()) > -1;
		}
	}
</script>
<div >
			<div data-options="region:'center',border:false" style="width:100%;padding:0px">
	    		<form id="editForm" method="post">
					<input type="hidden" id="id" name="count.id" value="${count.id }">
					<input type="hidden" id="createUser" name="count.createUser" value="${count.createUser }">
					<input type="hidden" id="createDept" name="count.createDept" value="${count.createDept }">
					<input type="hidden" id="createTime" name="count.createTime" value="${count.createTime }">
		    		<table class="honry-table" cellpadding="1" cellspacing="1" border="0px" style="width:100%;border-left:0;" data-options="border:false">
						<tr>
							<td class="honry-lable">栏目：</td>
			    			<td class="honry-info">
			    			<input class="easyui-combobox" id="munyType" name="count.munyType" value="${count.munyType }" data-options="required:true" style="width:220px" /></td>
		    			</tr>
						
						<tr>
							<td class="honry-lable">开始时间：</td>
			    			<td class="honry-info">
			    			<input id="startTime" name="count.startTime" value="${count.startTime}" style="width:220px" class="Wdate" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})"/>
			    			<input id="startTimeH" type="hidden" value="${count.startTime}" />
						<tr>					
							<td class="honry-lable">结束时间：</td>
			    			<td class="honry-info">
			    			<input id="endTime" name="count.endTime" value="${count.endTime}" style="width:220px" class="Wdate" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})"/>
			    			<input id="endTimeH" type="hidden" value="${count.endTime}"/>
		    			</tr>
		    			<tr>
							<td class="honry-lable">执行方式(年)：</td>
			    			<td class="honry-info">
			    			<input class="easyui-combobox" id="executeWayY" name="count.executeWayY" value="${count.executeWayY }" style="width:220px" /></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">执行方式(月)：</td>
			    			<td class="honry-info">
			    			<input class="easyui-combobox" id="executeWayM" name="count.executeWayM" value="${count.executeWayM }" style="width:220px" /></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">执行方式(日)：</td>
			    			<td class="honry-info">
			    			<input class="easyui-combobox" id="executeWayD" name="count.executeWayD" value="${count.executeWayD }" style="width:220px" /></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">开启/关闭：</td>
			    			<td class="honry-info">
			    			<input class="easyui-combobox" id="state" name="count.state" value="${count.state }" style="width:220px" /></td>
		    			</tr>
						<tr>
							<td class="honry-lable">停用标志：</td>
			    			<td>
				    			<input type="hidden" id="stopflgHidden" name="count.stop_flg" value="${count.stop_flg }"/>
				    			<input type="checkBox" id="stopflg" onclick="javascript:checkBoxSelect('stopflg')"/>
			    			</td>
						</tr>
						<tr id="sqlTRY">
							<td class="honry-lable">SQL(年)：</td>
			    			<td>
			    			<textarea rows="6" cols="5" id="executeSQLY" name="count.executeSQLY" value="" data-options="multiline:true" style="width:95%;height:60px;">${count.executeSQLY }</textarea></td>
						</tr>
						<tr id="sqlTRM">
							<td class="honry-lable">SQL(月)：</td>
			    			<td>
			    			<textarea rows="6" cols="5" id="executeSQLM" name="count.executeSQLM" value="" data-options="multiline:true" style="width:95%;height:60px;">${count.executeSQLM }</textarea></td>
						</tr>
						<tr id="sqlTRD">
							<td class="honry-lable">SQL(日)：</td>
			    			<td>
			    			<textarea rows="6" cols="5" id="executeSQLD" name="count.executeSQLD" value="" data-options="multiline:true" style="width:95%;height:60px;">${count.executeSQLD }</textarea></td>
						</tr>
		    	</table>
		    	<div style="text-align:center;padding:5px">
			    	<a href="javascript:submit();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
			    </form>
	    </div>
	</div>
</body>
</html>