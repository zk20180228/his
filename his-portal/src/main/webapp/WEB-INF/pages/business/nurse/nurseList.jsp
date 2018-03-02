<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<div id="cc" class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north'" style="height:10%;align:center">
    	<div style="height:100%;vertical-align:middle; ">  
        	<p style="font-size:30px;text-align:center">在院患者欠费报警</p>
        	<p style="text-align:center">　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　打印日期：<span id="spanTime"></span></p>
		</div>
    </div>   
    <div data-options="region:'center'" >
    <table id="alertView" class="easyui-datagrid" data-options="fit:true,toolbar:'#tb',url:'<%=basePath%>nursestation/nurse/VidBedInfoAllList.action?moneyLine='+1,
    rownumbers:true,
    singleSelect:false,
    fitColumns:true,
    pagination:true,
    pageSize:20,
    pageList:[20,50,100]">
     <thead>   
        <tr>
            <th data-options="field:'id',checkbox:true"></th>   
            <th data-options="field:'patientName',width:'8%',align:'left'">患者姓名</th>   
            <th data-options="field:'medicalrecordId',width:'8%',align:'center'">病历号</th> 
            <th data-options="field:'paykindCode',width:'8%',align:'center',formatter:paykindFunction">结算类别</th> 
            <th data-options="field:'pactCode',width:'10%',align:'center'">合同单位</th>   
            <th data-options="field:'bedId',width:'6%',align:'center'">住院病床</th>   
            <th data-options="field:'inSource',width:'6%',align:'center'">入院来源</th>
            <th data-options="field:'extFlag2',width:'4%',align:'center'">年龄</th>
            <th data-options="field:'inDate',width:'10%',align:'center'">住院日期</th>   
            <th data-options="field:'prepayCost',width:'5%',align:'center'" align="right" halign="left">预交金</th>
            <th data-options="field:'totCost',width:'7%',align:'center'" align="right" halign="left">总金额</th>   
            <th data-options="field:'freeCost',width:'8%',align:'center'" align="right" halign="left">余额</th>  
            <th data-options="field:'moneyAlert',width:'6%',align:'center'" align="right" halign="left">警戒线</th>   
            <th data-options="field:'moneyAlert',width:'6%',align:'center'" align="right" halign="left">补缴金额</th>    
        </tr>   
	    </thead> 
	    </table>
    </div>  
    <div id="tb">
		<input id="moneyType" class="easyui-combobox"style="width:120px;border:0" value="0"  data-options="
		valueField: 'label',
		textField: 'value',
		data: [{
			label: '0',value: '全部'
		},{
			label: '1',value: '按指定标准'
		},{
			label: '2',value: '按比例'
		},{
			label: '3',value: '按最底下限'
		}]
		,onSelect:function(record){
		if(record.label=='0'||record.label=='3'){
		styleHide();
		$('#alertView').datagrid('reload',{
		   style:record.label
		});
		}else{
			styleShow();
			
		}
	    }"/>
		<span id="nn" style="display: none;">比例：<input id="money" style="width:100px;"    onkeypress="return  EnterPress(event)" onkeydown="EnterPress(event)"></input></span>  
		类别：<input id="payKind1"  class="easyui-combobox" style="width:120px" value="00" data-options="" />
		</div>

</div> 
 <script type="text/javascript">
 var paykind="";
//时间
var toolArr = new Array('首页','上一页','下一页','尾页','刷新','打印');
function showTime(){
	var now=new Date();
	var text=now.getFullYear()+"年"+(now.getMonth()+1)+"月"+now.getDate()+"日";
	var hour=now.getHours();
	if(hour<10)
		hour="0"+hour;
	var fen=now.getMinutes();
	if(fen<10)
		fen="0"+fen;
	var miao=now.getSeconds();
	if(miao<10)
		miao="0"+miao;
	text=text+"  "+hour+":"+fen+":"+miao;
	$("#spanTime").html(text);
 }
$(function(){
	$('#alertView').datagrid({
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
		}
	});
	$.ajax({
		url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=paykind',
		type:'post',
		success: function(empIdObj) {
			paykind=empIdObj;
		}
	});	
	
	$("#payKind1").combobox({
		url:'<%=basePath%>nursestation/nurse/findpayKind.action',  
	    valueField:'encode',    
	    textField:'name',
	    multiple:false,
	    onSelect:function(record){
			$('#alertView').datagrid('reload',{
			   payKind:record.encode
			});
	    }
	})
	$("#money").textbox({
		prompt:'请输入比例',
		onChange:function(newValue, oldValue){
			var zz=$("#money").textbox('getValue');
			if(zz==""||zz==null){
				return;
			}
			if(!/^[0-9]+([.]{1}[0-9]+){0,1}$/.test(this.value)) {
				$.messager.alert('警告','只能输入整数或小数 !');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				$("#money").textbox('setValue','');
				return;
			}
			
			var style=$("#moneyType").combobox('getValue');
			var money=$("#money").textbox('getValue');
		    if(style=="2"&&money==""){
				$.messager.alert('提示','请输入一个比例!');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return;
			};
			$("#alertView").datagrid('reload',{
				money:money,
				style:style
			});
		}
	})
	  var pager3 = $('#alertView').datagrid('getPager');    // 得到datagrid的pager对象  
		pager3.pagination({    
		    showPageList:true, 
		    buttons:[{
		        iconCls:'icon-2012081511202',    
		        handler:function(){    
		        	var row=$("#alertView").datagrid("getChecked");
		            if(row.length==0){
		          	  $.messager.alert('警告','请选择一个患者'); 
		          	setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
		          	  return;
		            }
		            var inpatientNos='';
		            for(var i=0;i<row.length;i++){
		            	inpatientNos+=row[i].inpatientNo+',';
		            	 
		            }
		            inpatientNos=inpatientNos.substr(0,inpatientNos.length-1)
		              var timerStr = Math.random();
			      	  window.open ("<c:url value='/iReport/iReportPrint/iReportToDeposit.action?randomId='/>"+timerStr+"&inpatientNo="+inpatientNos+"&fileName=YJCKD",'newwindow'+i,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
		        }    
		    }]
		});
});

$(function(){
	showTime();
	window.setInterval("showTime()",1000);
	
});

function EnterPress(e){ //传入 event
	var e = e || window.event;
	if(e.keyCode == 13){
		var style=$("#moneyType").combobox('getValue');
		var money=$("#money").textbox('getValue');
	    if(style=="2"&&money==""){
			$.messager.alert('提示','请输入一个比例!');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return;
		};
		$("#alertView").datagrid('reload',{
			money:money,
			style:style
		});
	}
}

function styleHide(){
	$("#nn").hide();
}
function styleShow(){
	$("#money").textbox('setValue','');
	$("#nn").show();
}

function paykindFunction(value,row,index){
	for ( var i = 0; i < paykind.length; i++) {
		if (value == paykind[i].encode) {
			return paykind[i].name;
		}
	}
}
</script>
</body>
</html>