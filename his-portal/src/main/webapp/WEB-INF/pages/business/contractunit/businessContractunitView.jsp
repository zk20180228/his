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
</head>
<body>
      	<div align="center" class="easyui-panel" style="padding:10px">
		<form id="treeEditForm" method="post" >
			<input type="hidden" id="id" name="businessContractunit.id" value="${businessContractunit.id }">
			<input type="hidden" id="pinyin" name="businessContractunit.pinyin" value="${businessContractunit.pinyin }">
			<input type="hidden" id="wb" name="businessContractunit.wb" value="${businessContractunit.wb }">
			<input type="hidden" id="createUser" name="businessContractunit.createUser" value="${businessContractunit.createUser }">
			<input type="hidden" id="createDept" name="businessContractunit.createDept" value="${businessContractunit.createDept }">
			<input type="hidden" id="stop_flg" name="businessContractunit.stop_flg" value="${businessContractunit.stop_flg }">
			<input type="hidden" id="del_flg" name="businessContractunit.del_flg" value="${businessContractunit.del_flg }">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr align="center">
	    			<td class="honry-lable">名称：</td>
	    			<td>${businessContractunit.name }</td>
	    			<td class="honry-lable">代码：</td>
	    			<td>${businessContractunit.encode }</td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">拼音码：</td>
	    			<td>${businessContractunit.pinyin }</td>
	    			<td class="honry-lable">五笔码：</td>
	    			<td>${businessContractunit.wb }</td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">自定义码：</td>
	    			<td>${businessContractunit.inputCode }</td>
	    			<td class="honry-lable">排序：</td>
	    			<td>${businessContractunit.order }</td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">结算类别：</td>
	    			<td>${paykindCode }</td>
	    			<td class="honry-lable">价格形势：</td>
	    			<td>${priceForm }</td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">公费比例：</td>
	    			<td>${businessContractunit.pubRatio }</td>
	    			<td class="honry-lable">自负比例：</td>
	    			<td>${businessContractunit.payRatio }</td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">自费比例：</td>
	    			<td>${businessContractunit.ownRatio }</td>
	    			<td class="honry-lable">优惠比例：</td>
	    			<td>${businessContractunit.ecoRatio }</td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">欠费比例：</td>
	    			<td>${businessContractunit.arrRatio }</td>
	    			<td class="honry-lable">标识：</td>
	    			<td>${flag }</td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">日限额：</td>
	    			<td>${businessContractunit.dayLimit }</td>
	    			<td class="honry-lable">月限额：</td>
	    			<td>${businessContractunit.monthLimit }</td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">年限额：</td>
	    			<td>${businessContractunit.yearLimit }</td>
	    			<td class="honry-lable">一次限额：</td>
	    			<td>${businessContractunit.onceLimit }</td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">床位上限：</td>
	    			<td>${businessContractunit.bedLimit }</td>
	    			<td class="honry-lable">空调上限：</td>
	    			<td>${businessContractunit.airLimit }</td>
	    		</tr>
	    		<tr>
	    			<td colspan="4">
	    						&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-size: 14">婴儿标志：</span>
					    			<input type="hidden" id="babyFlagh" name="businessContractunit.babyFlag" value="${businessContractunit.babyFlag }"/>
					    			<input type="checkBox" id="babyFlag" disabled="true"/>
								&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-size: 14">是否要求必须有医疗证号：</span>
					    			<input id="mcardFlagh" type="hidden" name="businessContractunit.mcardFlag" value="${businessContractunit.mcardFlag }"/>
					    			<input type="checkBox" id="mcardFlag" disabled="true"/>
								&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-size: 14">是否监控：</span>
					    			<input id="controlFlagh" type="hidden" name="businessContractunit.controlFlag" value="${businessContractunit.controlFlag }"/>
					    			<input type="checkBox" id="controlFlag" disabled="true"/>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">说明：</td>
	    			<td  colspan="3">${businessContractunit.description }</td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">创建时间：</td>
	    			<td  colspan="3">${businessContractunit.createTime }</td>
	    		</tr>
				<tr>
					<td colspan="4" align="center">
   					<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" class="easyui-linkbutton" onclick="closeDialog()">关闭</a>
   					</td>
				</tr>
			</table>	
		</form>
	</div>
<script type="text/javascript">
	var paykindCodeArray = null;
	var priceFormArray = null;
	var flagArray = null;
$(function(){
			//判断是否编辑状态，编辑状态修改复选框选中状态
			if($('#id').val()!=''){
				if($('#babyFlagh').val()==1){
					$('#babyFlag').attr("checked", true); 
				}
				if($('#mcardFlagh').val()==1){
					$('#mcardFlag').attr("checked", true); 
				}
				if($('#controlFlagh').val()==1){
					$('#controlFlag').attr("checked", true); 
				}
			}
		});
	
</script>	
</body>

</html>