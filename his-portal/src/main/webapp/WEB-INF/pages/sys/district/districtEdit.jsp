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
</head>
	<body>
	<div class="easyui-panel" id="panelEast" data-options="title:'行政区编码编辑',iconCls:'icon-form',fit:true">
		<div style="padding:10px;">
    		<form id="editForm" method="post">
				<input type="hidden" id="id" name="id" value="${district.id  }">
				<input type="hidden" id="level" name="level" value="${district.level  }">
				<input type="hidden" id="order" name="order" value="${district.order  }">
				<input type="hidden" id="ordertoPath" name="ordertoPath" value="${district.ordertoPath  }">
				<input type="hidden" id="path" name="path" value="${district.path  }">
				<input type="hidden" id="upperPath" name="upperPath" value="${district.upperPath  }">
				<input type="hidden" id="citys" name="citys" value="${codelist}">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin-left:auto;margin-right:auto;">
					<tr>
							<td  style="font-size: 14"class="honry-lable">父级 ：</td>
			    			<td style="font-size: 14" class="honry-info">
				    	       	<input  id="parentId" name="parentId" value="${district.parentId }" style="width:200px">
				    		</td>
				    </tr>
					<tr>
							<td  style="font-size: 14"class="honry-lable">城市代码  ：</td>
			    			<td  style="font-size: 14"class="honry-info">
				    	    <input  id="cityCode" name="cityCode"  value="${district.cityCode }" data-options="required:true" style="width:200px">
				    		</td>
				    	</tr>
						<tr>
							<td  style="font-size: 14"class="honry-lable">城市名称  ：</td>
			    			<td  style="font-size: 14"class="honry-info">
				    	       	<input class="easyui-textbox" id="cityName" name="cityName" value="${district.cityName }" data-options="required:true" style="width:200px">
				    		</td>
				    	</tr>
						<tr>
							<td  style="font-size: 14"class="honry-lable">城市简称 ：</td>
			    			<td  style="font-size: 14"class="honry-info">
				    	       	<input class="easyui-textbox" id="shortname"  name="shortname"value="${district.shortname }" style="width:200px" >
				    		</td>
				    	</tr>
						<tr>
							<td  style="font-size: 14"class="honry-lable">英文名称 ：</td>
			    			<td  style="font-size: 14"class="honry-info">
				    	       	<input class="easyui-textbox"  id="ename"  name="ename" value="${district.ename }" style="width:200px">
				    		</td>
				    	</tr>
				    	
				    	<tr>
							<td  style="font-size: 14"class="honry-lable">是否直辖市 ：</td>
			    			<td  style="font-size: 14"class="honry-info">
				    	       	<input class="easyui-combobox"  id="municpalityFlag" name="municpalityFlag" 
				    	       	data-options="valueField:'id',textField:'value',data: [{id: 1,value: '非直辖市'},{id: 2,value: '直辖市'}]" value="${district.municpalityFlag }" style="width:200px">
				    		</td>
				    	</tr>
				    	<tr>
							<td  style="font-size: 14"class="honry-lable">自定义码 ：</td>
			    			<td  style="font-size: 14"class="honry-info">
				    	       	<input  class="easyui-textbox" id="defined" name="defined" value="${district.defined }" style="width:200px">
				    		</td>
				    	</tr>
		    			<tr>
							<td  style="font-size: 14"class="honry-lable">有效标识:</td>
					    	<td  style="font-size: 14">
					    	<input id="validFlgHidden" type="hidden" name="validFlag" value="${district.validFlag }" />
					    	<input type="checkBox" id="validFlag" onclick="javascript:onclickBox('validFlag')" checked="true"  />
				    		</td>
						</tr>
						
						<tr>
							<td style="font-size: 14" class="honry-lable">备注 ：</td>
			    			<td style="font-size: 14" class="honry-info">
				    	       	<input class="easyui-textbox"  id="remark" name="remark" value="${district.remark }" style="width:200px">
				    		</td>
				    	</tr>
						
		    	</table>
			    <div style="text-align:center;padding:5px">
				    <c:if test="${district.id==null }">
				    	<a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
			    	</c:if>
			    	<a id="save" href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
	    	</form>
	    </div>
	</div>    
	<script type="text/javascript">
	var cityCodes="${codelist}"
	var cityCode;
	if(cityCodes.length>1){
			cityCode=cityCodes.split(",");
	}
	
	$("#parentId").combobox({
		url: "<c:url value='/baseinfo/district/getDistrictTree.action'/>", 
	    valueField:'cityCode',    
	    textField:'cityName',
	    mode:'remote'
	    
	});
	$("#cityCode").textbox({
		onChange : function(newValue, oldValue){
			if(newValue){
				$.ajax({
					url: "<%=basePath%>baseinfo/district/vailCode.action",
					data:{disId:"${district.id}",disCode:newValue},
					type : 'post',
					success : function(data) {
						if(data == 1){
							$.messager.alert('警告','该系统编号已经存在');
							$("#cityCode").textbox('setText','');
							close_alert();
						}
					}
				});
			}
		}
	});
	
		//加载父节点
		$('#municpalityFlag').val(1);
		$('#validFlgHidden').val(1);
		if("${district.municpalityFlag}"==2){
			$('#municpalityFlag').val(2);
		}
			/**
			* 绑定父级名称回车事件
			* @author  zhuxiaolu
			* @date 2016-03-22 14:30   
			* @version 1.0
			*/
			bindEnterEvent('parentId',popWinToDistrict,'easyui');//绑定回车事件
			function popWinToDept(){
				$('#getdrug').combotree('setValue','');
				var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?textId=getdrug";
				window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -900) +',height='+ (screen.availHeight-370) 

			+',scrollbars,resizable=yes,toolbar=yes')
			}
			
			if ($('#validFlgHidden').val() == 1) {
	         	$('#validFlag').attr("checked", true);
	        }
	       
		function onclickBox(id) {
		if ($('#' + id).is(':checked')) {
			$('#validFlgHidden').val(1);
		} else {
			$('#validFlgHidden').val(2);
		}
	}
		
		//表单提交
		function submit(flg){
			
			$('#editForm').form('submit',{ 
				url : "<c:url value='/baseinfo/district/saveDistrict.action'/>",
	        	onSubmit:function(){
					if (!$('#editForm').form('validate')) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						return false;
					}
	        	},  
		        success:function(result){ 
		        
		        if (flg == 0) {
		        		$.messager.alert('提示','保存成功');
                         $('#list').datagrid('reload');
							$('#divLayout').layout('remove', 'east');
							//实现刷新
						} else if (flg == 1) {
							//清除editForm
							$('#editForm').form('reset');
						}
		        },
				error : function(result) {
					$.messager.alert('提示','保存失败！');	
				}							         
	   		}); 
		}
	    
		//清除页面填写信息
		function clear(){
			$('#editForm').form('reset');
		}
		
		//关闭编辑窗口
		function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
		
		/**
		* 回车弹出父级名称选择窗口
		* @author  zhuxiaolu
		* @param textId 页面上commbox的的id
		* @date 2016-03-22 14:30   
		* @version 1.0
		*/
		function popWinToDistrict(){
			popWinDistrictCallBackFn = function(node){
				$("#parentId").combotree('setValue',node.cityCode);
			
			}
			
				var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=parentId";
				window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -700) +',height='+ (screen.availHeight-370) 
				+',scrollbars,resizable=yes,toolbar=yes')
			
		}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>