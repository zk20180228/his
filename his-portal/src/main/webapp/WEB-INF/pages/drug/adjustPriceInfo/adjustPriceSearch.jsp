<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
	<body>
		<div id="c" class="easyui-layout" fit="true" >
			<div data-options="region:'north'" style="width: 100%;height: 40px;">
				<table style="width:100%;padding: 4px">
					<tr>
						<td>
							日期：
							<input id="inureTimeS" name="inureTimeS" class="Wdate" type="text" onFocus="var endDate=$dp.$('inureTimeE');WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:function(){endDate.focus();},maxDate:'#F{$dp.$D(\'inureTimeE\')}'})" style="font-size: 14"/>
							-
							<input id="inureTimeE" name="inureTimeE" class="Wdate" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'inureTimeS\')}'})" style="font-size: 14"/>
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
						</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center'" style="width: 100%;">
				<table id="searchList"  data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
					<thead>  
						<tr>  
							<th data-options="field:'tradeName'">药品</th> 
							<th data-options="field:'specs'">规格</th> 
							<th data-options="field:'drugStartdate',formatter: formatDatebox" >生产日期</th> 
							<th data-options="field:'producer',formatter:manufacturerFamater" >生产厂家</th>
							<th data-options="field:'preWholesalePrice'">调价前批发价格</th> 
							<th data-options="field:'wholesalePrice'">调价后批发价格</th> 
							<th data-options="field:'preRetailPrice'">调价前零售价格</th> 
							<th data-options="field:'retailPrice'">调价后零售价格</th> 
							<th data-options="field:'adjustReason',formatter:adjustReasonFamater">调价依据</th>  
						</tr>
					</thead>  
				</table>
			</div>
		</div>
		<script type="text/javascript">
			var addAndEdit;
			var manufacturer = self.parent.manufacturer;
			var adjustReason = '';
			//加载页面
			$(function(){
				$('#searchList').datagrid({
					pagination:true,
                    pageSize:20,
                    pageList:[20,30,50,80,100],
					url:"<c:url value='/drug/adjustPriceInfo/findByAdjustCode.action'/>",
					onBeforeLoad : function(){
						var inureTimeS = $("#inureTimeS").val();
						var inureTimeE = $("#inureTimeE").val();
						if(inureTimeS == null || inureTimeS ==''){
							if(inureTimeE == null ||  inureTimeE == ''){
								return false;
							}
						}
					}
				});	
	            $.ajax({
	                   url: '<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=adjustReason',
	                   success: function(data) {
	                       adjustReason = data;
	                   }
	               });
			});
			


			//查询
			function searchFrom() {
				var inureTimeS = $("#inureTimeS").val();
				var inureTimeE = $("#inureTimeE").val();
				if(inureTimeS == null || inureTimeS ==''){
					if(inureTimeE == null ||  inureTimeE == ''){
						$.messager.alert('操作提示','开始结束时间至少输入一个');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return false;
					}
				}
				$('#searchList').datagrid('load', {
					inureTimeS:inureTimeS,
					inureTimeE:	inureTimeE
				});
			}
			function KeyDown() {
				if (event.keyCode == 13) {
					event.returnValue = false;
					event.cancel = true;
					searchFrom();
				}
			}
			
			//datagrid中datebox组件的时间格式化
            Date.prototype.format = function (format) {  
                var o = {  
                    "M+": this.getMonth() + 1, // month  
                    "d+": this.getDate(), // day  
                    "h+": this.getHours(), // hour  
                    "m+": this.getMinutes(), // minute  
                    "s+": this.getSeconds(), // second  
                    "q+": Math.floor((this.getMonth() + 3) / 3), // quarter  
                    "S": this.getMilliseconds()  
                    // millisecond  
                };  
                if (/(y+)/.test(format))  
                    format = format.replace(RegExp.$1, (this.getFullYear() + "")  
                        .substr(4 - RegExp.$1.length));  
                for (var k in o)  
                    if (new RegExp("(" + k + ")").test(format))  
                        format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));  
                return format;  
            };  
            function formatDatebox(value) {  
                if (value == null || value == '') {  
                    return '';  
                }  
                var dt;  
                if (value instanceof Date) {  
                    dt = value;  
                } else {  
                    dt = new Date(value);  
                }  
                return dt.format("yyyy-MM-dd"); //扩展的Date的format方法(上述插件实现)  
            }
            function manufacturerFamater(value,row,index){
                if(value!=null&&value!=''){
                    return manufacturer[value];
                }
            }
            function adjustReasonFamater(value,row,index){
                for(var i = 0; i < adjustReason.length; i++){
                    if(value == adjustReason[i].encode){
                        return adjustReason[i].name;
                    }
                }
            }
         // 药品列表查询重置
    		function searchReload() {
    			$('#inureTimeS').val('');
    			$('#inureTimeE').val('');
    			$('#searchList').datagrid('loadData',[]);
    		}
		</script>
	</body>
</html>