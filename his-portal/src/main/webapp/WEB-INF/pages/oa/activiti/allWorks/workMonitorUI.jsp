<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>流程监控</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
</head>

<body>
<div id="tt" class="easyui-tabs"  data-options="fit:true,border:false">
    <div title="统计概况"  >

            <div style="height:6%;weight:100%;">
                <div style="padding-top:15px;padding-bottom:0px;padding-left:5px;text-align:left">
                    <span style="font-size: 16px">流程名称:</span> <input  id="search_text_1"  style="height:24px;width:250px"><span>&nbsp;</span>
                    <span style="font-size: 16px">发起时间:</span> <input id="search_text_2-s" class="Wdate" type="text" value="${sTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss' ,maxDate:'#F{$dp.$D(\'search_text_2-e\')}'})"
                                                                      style="width:180px;height: 24px;border: 1px solid #95b8e7;border-radius: 5px;"/><span style="font-size: 16px">&nbsp;至&nbsp;</span>
                    <input id="search_text_2-e" class="Wdate" type="text" value="${eTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss' ,minDate:'#F{$dp.$D(\'search_text_2-s\')}'})"
                           style="width:180px;height: 24px;border: 1px solid #95b8e7;border-radius: 5px;"/> <span>&nbsp;</span>

                    <a href="javascript:void(0)" onclick="dgReload1()" class="easyui-linkbutton"
                       iconCls="icon-search">查询</a>
                    <a href="javascript:void(0)" onclick="dgReset1()" class="easyui-linkbutton"
                       iconCls="reset">重置</a>
                </div>
            </div>
            <div style="height:94%;weight:100%;"><table id="dg1"></table></div>

    </div>

    <div title="流转中">
        <div class="easyui-layout" data-options="fit:true">
            <div data-options="region:'west',title:'导航树',split:true,tools:'#toolSMId1'" style="width:240px;">
                <ul id="tree1"></ul>
                <div id="toolSMId1">
                    <a href="javascript:void(0)" onclick="refresh1()" class="icon-reload"></a>
                    <a href="javascript:void(0)" onclick="collapseAll1()" class="icon-fold"></a>
                    <a href="javascript:void(0)" onclick="expandAll1()" class="icon-open"></a>
                </div>
            </div>

            <div data-options="region:'center'">
                <div style="height:6%;weight:100%;">
                    <div style="padding-top:15px;padding-bottom:0px;padding-left:5px;text-align:left">
                        <span style="font-size: 16px">发起人:</span>   <input    id="search_text_33"   style="height:24px;width:180px"> <span>&nbsp;</span>
                        <span style="font-size: 16px">发起时间:</span> <input id="search_text_44-s" class="Wdate" type="text" value="${sTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss' ,maxDate:'#F{$dp.$D(\'search_text_44-e\')}'})"
                                     style="width:180px;height: 24px;border: 1px solid #95b8e7;border-radius: 5px;"/><span style="font-size: 16px">&nbsp;至&nbsp;</span>
                        <input id="search_text_44-e" class="Wdate" type="text" value="${eTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss' ,minDate:'#F{$dp.$D(\'search_text_44-s\')}'})"
                               style="width:180px;height: 24px;border: 1px solid #95b8e7;border-radius: 5px;"/> <span>&nbsp;</span>

                        <a href="javascript:void(0)" onclick="dgReload2()" class="easyui-linkbutton"
                           iconCls="icon-search">查询</a>
                        <a href="javascript:void(0)" onclick="dgReset2()" class="easyui-linkbutton"
                           iconCls="reset">重置</a>
                    </div>
                </div>
                <div style="height:94%;weight:100%;"><table id="dg2"></table></div>
            </div>
        </div>
    </div>

    <div title="已办毕" >
        <div  class="easyui-layout" data-options="fit:true">
            <div data-options="region:'west',title:'导航树',split:true,tools:'#toolSMId2'" style="width:240px;">
                <ul id="tree2"></ul>
                <div id="toolSMId2">
                    <a href="javascript:void(0)" onclick="refresh2()" class="icon-reload"></a>
                    <a href="javascript:void(0)" onclick="collapseAll2()" class="icon-fold"></a>
                    <a href="javascript:void(0)" onclick="expandAll2()" class="icon-open"></a>
                </div>
            </div>

            <div data-options="region:'center'">
                <div style="height:6%;weight:100%;">
                    <div style="padding-top:15px;padding-bottom:0px;padding-left:5px;text-align:left">
                        <span style="font-size: 16px">发起人:</span>   <input    id="search_text_55"   style="height:24px;width:180px"> <span>&nbsp;</span>
                        <span style="font-size: 16px">发起时间:</span> <input id="search_text_66-s" class="Wdate" type="text" value="${sTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss' ,maxDate:'#F{$dp.$D(\'search_text_66-e\')}'})"
                                     style="width:180px;height: 24px;border: 1px solid #95b8e7;border-radius: 5px;"/><span style="font-size: 16px">&nbsp;至&nbsp;</span>
                        <input id="search_text_66-e" class="Wdate" type="text" value="${eTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss' ,minDate:'#F{$dp.$D(\'search_text_66-s\')}'})"
                               style="width:180px;height: 24px;border: 1px solid #95b8e7;border-radius: 5px;"/> <span>&nbsp;</span>

                        <a href="javascript:void(0)" onclick="dgReload3()" class="easyui-linkbutton"
                           iconCls="icon-search">查询</a>
                        <a href="javascript:void(0)" onclick="dgReset3()" class="easyui-linkbutton"
                           iconCls="reset">重置</a>
                    </div>
                </div>

                <div style="height:94%;weight:100%;"><table id="dg3"></table></div>
            </div>
        </div>
    </div>

    <div title="催办记录">
        <div  class="easyui-layout" data-options="fit:true">
            <div data-options="region:'west',title:'导航树',split:true,tools:'#toolSMId3'" style="width:240px;">
                <ul id="tree3"></ul>
                <div id="toolSMId3">
                    <a href="javascript:void(0)" onclick="refresh3()" class="icon-reload"></a>
                    <a href="javascript:void(0)" onclick="collapseAll3()" class="icon-fold"></a>
                    <a href="javascript:void(0)" onclick="expandAll3()" class="icon-open"></a>
                </div>
            </div>
            <div data-options="region:'center'">
                <div style="height:6%;weight:100%;">
                    <div style="padding-top:15px;padding-bottom:0px;padding-left:5px;text-align:left">
                        <span style="font-size: 16px">催办人:</span><input  id="search_text_77"   style="height:24px;width:180px"> <span>&nbsp;</span>
                        <span style="font-size: 16px">催办时间:</span><input id="search_text_88-s" class="Wdate" type="text" value="${sTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss' ,maxDate:'#F{$dp.$D(\'search_text_88-e\')}'})"
                                                     style="width:180px;height: 24px;border: 1px solid #95b8e7;border-radius: 5px;"/><span style="font-size: 16px">&nbsp;至&nbsp;</span>
                        <input id="search_text_88-e" class="Wdate" type="text" value="${eTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss' ,minDate:'#F{$dp.$D(\'search_text_88-s\')}'})"
                               style="width:180px;height: 24px;border: 1px solid #95b8e7;border-radius: 5px;"/> <span>&nbsp;</span>

                        <a href="javascript:void(0)" onclick="dgReload4()" class="easyui-linkbutton"
                           iconCls="icon-search">查询</a>
                        <a href="javascript:void(0)" onclick="dgReset4()" class="easyui-linkbutton"
                           iconCls="reset">重置</a>
                    </div>
                </div>

                <div style="height:94%;weight:100%;"><table id="dg4"></table></div>
            </div>
        </div>
    </div>


    <div title="操作记录"  >
        <div  class="easyui-layout" data-options="fit:true">
            <div data-options="region:'west',title:'导航树',split:true,tools:'#toolSMId4'" style="width:240px;">
                <ul id="tree4"></ul>
                <div id="toolSMId4">
                    <a href="javascript:void(0)" onclick="refresh4()" class="icon-reload"></a>
                    <a href="javascript:void(0)" onclick="collapseAll4()" class="icon-fold"></a>
                    <a href="javascript:void(0)" onclick="expandAll4()" class="icon-open"></a>
                </div>
            </div>
            <div data-options="region:'center'">
                <div style="height:6%;weight:100%;">
                    <div style="padding-top:15px;padding-bottom:0px;padding-left:5px;text-align:left">
                        <span style="font-size: 16px">流程名称:</span> <input  id="search_text_5"  style="height:24px;width:250px"><span>&nbsp;</span>
                        <span style="font-size: 16px">发起人:</span>   <input    id="search_text_5_5"   style="height:24px;width:180px"> <span>&nbsp;</span>
                        <span style="font-size: 16px">发起时间:</span> <input id="search_text_5-s" class="Wdate" type="text" value="${sTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss' ,maxDate:'#F{$dp.$D(\'search_text_2-e\')}'})"
                                                                          style="width:180px;height: 24px;border: 1px solid #95b8e7;border-radius: 5px;"/><span style="font-size: 16px">&nbsp;至&nbsp;</span>
                        <input id="search_text_5-e" class="Wdate" type="text" value="${eTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss' ,minDate:'#F{$dp.$D(\'search_text_2-s\')}'})"
                               style="width:180px;height: 24px;border: 1px solid #95b8e7;border-radius: 5px;"/> <span>&nbsp;</span>

                        <a href="javascript:void(0)" onclick="dgReload5()" class="easyui-linkbutton"
                           iconCls="icon-search">查询</a>
                        <a href="javascript:void(0)" onclick="dgReset5()" class="easyui-linkbutton"
                           iconCls="reset">重置</a>
                    </div>
                </div>
                 <div style="height:94%;weight:100%;"><table id="dg5"></table></div>
            </div>
        </div>
    </div>

</div>
<div id="win"></div>
</body>
</html>
<script type="text/javascript">

    $(function(){

        //tree树加载
        $.ajax({
            type:"post",
            url:'<%=basePath%>/oa/juris/getProcessTree.action',
            success:function(backData){
                $('#tree1').tree({
                    data : backData,
                    onClick : function(node){
                        dgReload2();
                    },
                    onLoadSuccess : function(node, data){
                        var id = backData[0].children[0].id;
                        $('#tree1').tree('select',$('#tree1').tree('find',id).target);
                    },
                    onBeforeCollapse:function(node){
                        if(node.id=="1"){
                            return false;
                        }
                    }
                });
                $('#tree2').tree({
                    data : backData,
                    onClick : function(node){
                        dgReload3();
                    },
                    onLoadSuccess : function(node, data){
                        var id = backData[0].children[0].id;
                        $('#tree2').tree('select',$('#tree2').tree('find',id).target);
                    },
                    onBeforeCollapse:function(node){
                        if(node.id=="1"){
                            return false;
                        }
                    }
                });
                $('#tree3').tree({
                    data : backData,
                    onClick : function(node){
                        dgReload4();
                    },
                    onLoadSuccess : function(node, data){
                        var id = backData[0].children[0].id;
                        $('#tree3').tree('select',$('#tree3').tree('find',id).target);
                    },
                    onBeforeCollapse:function(node){
                        if(node.id=="1"){
                            return false;
                        }
                    }
                });

                $('#tree4').tree({
                    data : backData,
                    onClick : function(node){
                        dgReload5();
                    },
                    onLoadSuccess : function(node, data){
                        var id = backData[0].children[0].id;
                        $('#tree4').tree('select',$('#tree4').tree('find',id).target);
                    },
                    onBeforeCollapse:function(node){
                        if(node.id=="1"){
                            return false;
                        }
                    }
                });

            }
        })

        //发起人
        $.ajax({
            type:"get",
            url:"<%=basePath%>activiti/allWorks/empList.action",
            success:function(backData){

                $('#search_text_33').combobox({
                    data: backData,
                    valueField : 'empJobNo',
                    textField : 'empName',
                    filter:function(q,row){
                        var keys = new Array();
                        keys[keys.length] = 'empJobNo';
                        keys[keys.length] = 'empName';
                        if(filterLocalCombobox(q, row, keys)){
                            row.selected=true;
                        }else{
                            row.selected=false;
                        }
                        return filterLocalCombobox(q, row, keys);
                    }
                });

                $('#search_text_55').combobox({
                    data: backData,
                    valueField : 'empJobNo',
                    textField : 'empName',
                    filter:function(q,row){
                        var keys = new Array();
                        keys[keys.length] = 'empJobNo';
                        keys[keys.length] = 'empName';
                        if(filterLocalCombobox(q, row, keys)){
                            row.selected=true;
                        }else{
                            row.selected=false;
                        }
                        return filterLocalCombobox(q, row, keys);
                    }
                });

                $('#search_text_5_5').combobox({
                    data: backData,
                    valueField : 'empJobNo',
                    textField : 'empName',
                    filter:function(q,row){
                        var keys = new Array();
                        keys[keys.length] = 'empJobNo';
                        keys[keys.length] = 'empName';
                        if(filterLocalCombobox(q, row, keys)){
                            row.selected=true;
                        }else{
                            row.selected=false;
                        }
                        return filterLocalCombobox(q, row, keys);
                    }
                });

                $('#search_text_77').combobox({
                    data: backData,
                    valueField : 'empJobNo',
                    textField : 'empName',
                    filter:function(q,row){
                        var keys = new Array();
                        keys[keys.length] = 'empJobNo';
                        keys[keys.length] = 'empName';
                        if(filterLocalCombobox(q, row, keys)){
                            row.selected=true;
                        }else{
                            row.selected=false;
                        }
                        return filterLocalCombobox(q, row, keys);
                    }
                });
            }
        });

        //流程名称列表
        $.ajax({
            url: "<%=basePath%>activiti/allWorks/processList.action",
            type:"post",
            success:function(backData){
                $('#search_text_1').combobox({
                    data:backData,
                    valueField : 'processId',
                    textField : 'processName',
                    filter:function(q,row){
                        var keys = new Array();
                        keys[keys.length] = 'processId';
                        keys[keys.length] = 'processName';
                        if(filterLocalCombobox(q, row, keys)){
                            row.selected=true;
                        }else{
                            row.selected=false;
                        }
                        return filterLocalCombobox(q, row, keys);
                    }
                });


                $('#search_text_5').combobox({
                    data:backData,
                    valueField : 'processId',
                    textField : 'processName',
                    filter:function(q,row){
                        var keys = new Array();
                        keys[keys.length] = 'processId';
                        keys[keys.length] = 'processName';
                        if(filterLocalCombobox(q, row, keys)){
                            row.selected=true;
                        }else{
                            row.selected=false;
                        }
                        return filterLocalCombobox(q, row, keys);
                    }
                });

            }
        });



        /************************************************************************************统计概况************************************************************************/
        //统计概况
        $('#dg1').datagrid({    //&work_Name='+encodeURI("审批")
            url:'${pageContext.request.contextPath}/activiti/allWorks/workList.action?work_Flag=1',
            fitColumns:true,
            pagination:true,
            rownumbers: true,
            striped:true,
            fit:true,
            singleSelect:true,
            pageList:[10,20,30,40,50],
            pageSize:20,
            queryParams: {
                startTime: $("#search_text_2-s").val(),
                endTime: $("#search_text_2-e").val()
            },
            columns:[[
                {field:'pName',title:'流程名称',width:100,align:'left', formatter: function (value) {
                        return "<span title='" + value + "'>" + value + "</span>";
                    }},
                {field:'runNum',title:'流转中数量',width:100,align:'left'},
                {field:'completeNum',title:'已办毕数量',width:100,align:'left'},
                {field:'remindNum',title:'催办数量',width:100,align:'left'}
            ]]});


        /************************************************************************************操作记录***********************************************************************/

        $('#dg5').datagrid({
            url:'${pageContext.request.contextPath}/activiti/allWorks/workList.action?work_Flag=5',
            fitColumns:true,
            pagination:true,
            rownumbers: true,
            striped:true,
            fit:true,
            singleSelect:true,
            pageList:[10,20,30,40,50],
            pageSize:20,
            queryParams: {
                startTime: $("#search_text_5-s").val(),
                endTime: $("#search_text_5-e").val()
            },
            columns:[[
                {field:'processName',title:'申请标题',width:150,align:'left', formatter: function (value) {
                        return "<span title='" + value + "'>" + value + "</span>";
                    }},
                {field:'taskName',title:'操作环节',width:60,align:'left'},
                {field:'oldAssigneeName',title:'原审批人',width:60,align:'left', formatter: function (value) {
                        return "<span title='" + value + "'>" + value + "</span>";
                    }},
                {field:'newAssigneeName',title:'现审批人',width:60,align:'left', formatter: function (value) {
                        return "<span title='" + value + "'>" + value + "</span>";
                    }},
                {field:'createuserName',title:'操作人',width:80,align:'left'},
                {field:'createtime',title:'操作时间',width:80,align:'left'},
                {field:'processStarterName',title:'发起人',width:50,align:'left'},
                {field:'processStartTime',title:'发起时间',width:80,align:'left'}
            ]]});

        /************************************************************************************流转中************************************************************************/
        //流转中
        $('#dg2').datagrid({    //&serial_Number=127501&work_Name='+encodeURI("审批")
            url:'${pageContext.request.contextPath}/activiti/allWorks/workList.action?work_Flag=2',
            fitColumns:true,
            pagination:true,
            rownumbers: true,
            fit:true,
            singleSelect:true,
            pageList:[10,20,30,40,50],
            pageSize:20,
            queryParams: {
                startTime: $("#search_text_44-s").val(),
                endTime: $("#search_text_44-e").val()
            },
            columns:[[
                {field:'title',title:'申请标题',width:150,align:'left', formatter: function (value) {
                        if(value==null||value==undefined||value==""){
                            return "";
                        }else{
                            return "<span title='" + value + "'>" + value + "</span>";
                        }
                    }},
                {field:'curNode',title:'当前环节',width:80,align:'left', formatter: function (value) {
                        if(value==null||value==undefined||value==""){
                            return "";
                        }else{
                            return "<span title='" + value + "'>" + value + "</span>";
                        }
                    }},
                {field:'approver',title:'审批人',width:100,align:'left'},
                {field:'blockTime',title:'停留时长',width:50,align:'left'},
                {field:'owner',title:'发起人',width:40,align:'left'},
                {field:'startTime',title:'发起时间',width:80,align:'left'},
                {field:'pType',title:'流程分类',width:60,align:'left'},
                {field:'blDept',title:'所属科室',width:50,align:'left'},
                {field:'option',title:'操作',width:140,align:'left'},
                {field:'processInstanceId',hidden:true},
                {field:'businessKey',hidden:true},
                {field:'tid',hidden:true},
                {field:'allApproversJobNum',hidden:true},
                {field:'allApproversName',hidden:true},
                {field:'ownerJobNum',hidden:true},
                {field:'taskId',hidden:true}
            ]],
            onLoadSuccess:function(data){
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
                var rows = data.rows;
                if(rows.length>0){
                    for(var i=0;i<rows.length;i++){
                        //指定审批人时所用的数据
                        var ps="{taskInfoId:'"+rows[i].tid+"',processName:'"+rows[i].title+"',taskName:'"+rows[i].curNode+"',oldAssignee:'"+rows[i].allApproversJobNum+"',oldAssigneeName:'"+rows[i].allApproversName+"',processStarter:'"+rows[i].ownerJobNum+"',processStarterName:'"+rows[i].owner+"',processStartTime:'"+rows[i].startTime+"',taskId:'"+rows[i].taskId+"'}";
                        $(this).datagrid('updateRow',{
                            index: $(this).datagrid('getRowIndex',rows[i]),
                            row: {
                                option : '<a class="sickCls8" onclick="query(\''+rows[i].processInstanceId+'\')" href="javascript:void(0)"></a>&nbsp;'
                                +'<a class="sickCls88" onclick="appointAssignees('+ps+')" href="javascript:void(0)"></a>&nbsp;'
                                +'<a class="sickCls18" onclick="shenpixiangq(\''+rows[i].businessKey+'\')" href="javascript:void(0)"></a>'
                            }
                        });
                    }
                    $('.sickCls8').linkbutton({text:'流转详情',plain:false,height:'20px'});
                    $('.sickCls88').linkbutton({text:'指定审批人',plain:false,height:'20px'});
                    $('.sickCls18').linkbutton({text:'审批详情',plain:false,height:'20px'});
                }

            }
        });



        /************************************************************************************已办毕************************************************************************/
        //已办毕
        $('#dg3').datagrid({
            url:'${pageContext.request.contextPath}/activiti/allWorks/workList.action?work_Flag=3',
            fitColumns:true,
            fit:true,
            pagination:true,
            singleSelect:true,
            rownumbers: true,
            pageList:[10,20,30,40,50],
            pageSize:20,
            queryParams: {
                startTime: $("#search_text_66-s").val(),
                endTime: $("#search_text_66-e").val()
            },
            columns:[[
                {field:'title',title:'申请标题',width:150,align:'left', formatter: function (value) {
                        if(value==null||value==undefined||value==""){
                            return "";
                        }else{
                            return "<span title='" + value + "'>" + value + "</span>";
                        }
                    }},
                {field:'owner',title:'发起人',width:50,align:'left'},
                {field:'startTime',title:'发起时间',width:80,align:'left'},
                {field:'endTime',title:'办毕时间',width:80,align:'left'},
                {field:'totalTime',title:'流程总用时',width:60,align:'left'},
                {field:'pType',title:'流程分类',width:80,align:'left'},
                {field:'blDept',title:'所属科室',width:80,align:'left'},
                {field:'option',title:'操作',width:80,align:'left'},
                {field:'processInstanceId',hidden:true,width:80,align:'left'},
                {field:'businessKey',hidden:true,width:80,align:'left'}
            ]],
            onLoadSuccess:function(data){
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
                var rows = data.rows;
                if(rows.length>0){
                    for(var i=0;i<rows.length;i++){
                        $(this).datagrid('updateRow',{
                            index: $(this).datagrid('getRowIndex',rows[i]),
                            row: {
                                option : '<a class="sickCls9" onclick="query(\''+rows[i].processInstanceId+'\')" href="javascript:void(0)"></a>&nbsp;'
                                +'<a class="sickCls19" onclick="shenpixiangq(\''+rows[i].businessKey+'\')" href="javascript:void(0)"></a>'
                            }
                        });
                    }
                    $('.sickCls9').linkbutton({text:'流转详情',plain:false,height:'20px'});
                    $('.sickCls19').linkbutton({text:'审批详情',plain:false,height:'20px'});
                }

            }
        });

        /************************************************************************************催办记录************************************************************************/
        //催办记录
        $('#dg4').datagrid({
            url:'${pageContext.request.contextPath}/activiti/allWorks/workList.action?work_Flag=4',
            fitColumns:true,
            fit:true,
            singleSelect:true,
            pagination:true,
            rownumbers: true,
            pageList:[10,20,30,40,50],
            pageSize:20,
            queryParams: {
                startTime: $("#search_text_88-s").val(),
                endTime: $("#search_text_88-e").val()
            },
            columns:[[
                {field:'title',title:'申请标题',width:180,align:'left', formatter: function (value) {
                        if(value==null||value==undefined||value==""){
                            return "";
                        }else{
                            return "<span title='" + value + "'>" + value + "</span>";
                        }
                    }},
                {field:'pNode',title:'催办环节',width:80,align:'left', formatter: function (value) {
                        if(value==null||value==undefined||value==""){
                            return "";
                        }else{
                            return "<span title='" + value + "'>" + value + "</span>";
                        }
                    }},
                {field:'reminder',title:'催办人',width:70,align:'left'},
                {field:'reminded',title:'被催办人',width:80,align:'left', formatter: function (value) {
                        if(value==null||value==undefined||value==""){
                            return "";
                        }else{
                            return "<span title='" + value + "'>" + value + "</span>";
                        }
                    }},
                {field:'startTime',title:'催办时间',width:110,align:'left'},
                {field:'endTime',title:'办毕时间',width:110,align:'left'},
                {field:'blockTime',title:'持续时间',width:70,align:'left'},
                {field:'reContent',title:'催办内容',width:100,align:'left', formatter: function (value) {
                        if(value==null||value==undefined||value==""){
                            return "";
                        }else{
                            return "<span title='" + value + "'>" + value + "</span>";
                        }
                    }},
                {field:'pType',title:'流程分类',width:70,align:'left'},
                {field:'blDept',title:'所属科室',width:70,align:'left'},
                {field:'remindNum',title:'催办次数',width:50,align:'left'},
                {field:'isReader',title:'已读',width:35,align:'left'},
                {field:'isResponse',title:'已回',width:35,align:'left'}
            ]]});

    });

    /****************************************************************************************************************************************************************************/

    /**************************************************************统计概况************************************************************************************/
    //查询
    function dgReload1(){
        var processId = $('#search_text_1').combobox('getValue');
        var startTime = $("#search_text_2-s").val();
        var endTime  =  $("#search_text_2-e").val();

        $('#dg1').datagrid('load',{
            processId: processId,
            startTime: startTime,
            endTime:endTime
        });
    }
    //重置
    function dgReset1(){
        $('#search_text_1').combobox('clear');
        $("#search_text_2-s").val("");
        $("#search_text_2-e").val("");
        dgReload1();
    }
    /**************************************************************操作记录************************************************************************************/
    //查询
    function dgReload5(){
        var processId = $('#search_text_5').combobox('getValue');
        var empJobNo = $('#search_text_5_5').combobox('getValue');
        var startTime = $("#search_text_5-s").val();
        var endTime  =  $("#search_text_5-e").val();


        var pType = "";
        var blDept = "";
        var node=$('#tree4').tree('getSelected');
        if(node!=null&&node.id!="root"&&node.id!="category"&&node.id!="deptcode"){
            var fNode = $('#tree4').tree("getParent",node.target);
            if(fNode.id=="category"){
                pType=node.id;
            }
            if(fNode.id=="deptcode"){
                blDept=node.id;
            }
        }

        $('#dg5').datagrid('load',{
            pType:pType,
            blDept:blDept,
            processId: processId,
            startTime: startTime,
            empJobNo:empJobNo,
            endTime:endTime
        });
    }
    //重置
    function dgReset5(){
        $('#search_text_5').combobox('clear');
        $('#search_text_5_5').combobox('clear');
        $("#search_text_5-s").val("");
        $("#search_text_5-e").val("");
        dgReload5();
    }
    /**************************************************************流转中************************************************************************************/

    //查询
    function dgReload2(){
        var pType = "";
        var blDept = "";
        var node=$('#tree1').tree('getSelected');
        if(node!=null&&node.id!="root"&&node.id!="category"&&node.id!="deptcode"){
            var fNode = $('#tree1').tree("getParent",node.target);
            if(fNode.id=="category"){
                pType=node.id;
            }
            if(fNode.id=="deptcode"){
                blDept=node.id;
            }
        }

        var empJobNo = $('#search_text_33').combobox('getValue');
        var startTime = $("#search_text_44-s").val();
        var endTime  =  $("#search_text_44-e").val();

        $('#dg2').datagrid('load',{
            pType:pType,
            blDept:blDept,
            empJobNo:empJobNo,
            startTime: startTime,
            endTime:endTime
        });
    }
    //重置
    function dgReset2(){
        $('#search_text_33').combobox('clear');
        $("#search_text_44-s").val("");
        $("#search_text_44-e").val("");
        dgReload2();
    }


    /**************************************************************已办毕************************************************************************************/

    //查询
    function dgReload3(){
        var pType = "";
        var blDept = "";
        var node=$('#tree2').tree('getSelected');
        if(node!=null&&node.id!="root"&&node.id!="category"&&node.id!="deptcode"){
            var fNode = $('#tree2').tree("getParent",node.target);
            if(fNode.id=="category"){
                pType=node.id;
            }
            if(fNode.id=="deptcode"){
                blDept=node.id;
            }
        }
        var empJobNo = $('#search_text_55').combobox('getValue');
        var startTime = $("#search_text_66-s").val();
        var endTime  =  $("#search_text_66-e").val();

        $('#dg3').datagrid('load',{
            pType:pType,
            blDept:blDept,
            empJobNo:empJobNo,
            startTime: startTime,
            endTime:endTime
        });
    }
    //重置
    function dgReset3(){
        $('#search_text_55').combobox('clear');
        $("#search_text_66-s").val("");
        $("#search_text_66-e").val("");
        dgReload3();
    }

    /*************************************************************催办记录************************************************************************************/

    //查询
    function dgReload4(){
        var pType = "";
        var blDept = "";
        var node=$('#tree3').tree('getSelected');
        if(node!=null&&node.id!="root"&&node.id!="category"&&node.id!="deptcode"){
            var fNode = $('#tree3').tree("getParent",node.target);
            if(fNode.id=="category"){
                pType=node.id;
            }
            if(fNode.id=="deptcode"){
                blDept=node.id;
            }
        }
        var empJobNo = $('#search_text_77').combobox('getValue');
        var startTime = $("#search_text_88-s").val();
        var endTime  =  $("#search_text_88-e").val();

        $('#dg4').datagrid('load',{
            pType:pType,
            blDept:blDept,
            empJobNo:empJobNo,
            startTime: startTime,
            endTime:endTime
        });
    }
    //重置
    function dgReset4(){
        $('#search_text_77').combobox('clear');
        $("#search_text_88-s").val("");
        $("#search_text_88-e").val("");
        dgReload4();
    }


/*****************************************************审批详情和催办详情****************************************************************************************************************/

    function shenpixiangq(id){
        AddOrShowEast(id,"<%=basePath%>activiti/humanTask/viewTaskFormYj.action");
    }

    function query(processInstanceId){
        AddOrShowEast1(processInstanceId,"<%=basePath%>activiti/operation/viewHistory.action");
    }

    function AddOrShowEast(id, url) {
        var w = $("body").width()*0.9
        var h = $("body").height()*0.9
        var id = id;
        var url = url;
        var name = '查看';
        var width = w;
        var height = h;
        var top = (window.screen.availHeight-30-height)/2;
        var left = (window.screen.availWidth-10-width)/2;
        if($("#winOpenFrom").length<=0){
            var form = "<form id='winOpenFrom' action='"+url+"' method='post' target='"+name+"'>" +
                "<input type='hidden' id='winOpenFromInpId' name='humanTaskId'/></form>";
            $("body").append(form);
        }
        $('#winOpenFromInpId').val(id);
        openWindow('about:blank',name,width,height,top,left);
        $('#winOpenFrom').prop('action',url);
        $("#winOpenFrom").submit();
    }

    function AddOrShowEast1(processInstanceId, url) {
        var w = $("body").width()*0.9
        var h = $("body").height()*0.9
        var processInstanceId = processInstanceId;
        var url = url;
        var name = '查看';
        var width = w;
        var height = h;
        var top = (window.screen.availHeight-30-height)/2;
        var left = (window.screen.availWidth-10-width)/2;
        if($("#winOpenFrom1").length<=0){
            var form = "<form id='winOpenFrom1' action='"+url+"' method='post' target='"+name+"'>" +
                "<input type='hidden' id='winOpenFromInpId1' name='id'/></form>";
            $("body").append(form);
        }
        $('#winOpenFromInpId1').val(processInstanceId);
        openWindow('about:blank',name,width,height,top,left);
        $('#winOpenFrom1').prop('action',url);
        $("#winOpenFrom1").submit();
    }

    //打开窗口
    function openWindow(url,name,width,height,top,left){
        window.open(url, name, 'height=' + height + ',innerHeight=' + height + ',width=' + width + ',innerWidth=' + width + ',top=' + top + ',left=' + left + ',status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no');
    }


    function refresh1(){
        $.ajax({
            type: "post",
            url: '<%=basePath%>/oa/juris/getProcessTree.action',
            async:false,
            success: function (backData) {
                $('#tree1').tree({
                    data:backData
                });
            }
        })
        dgReload2();
    }

    function collapseAll1() {//关闭树
        $('#tree1').tree('collapseAll');
    }
    function expandAll1() {//展开树
        $('#tree1').tree('expandAll');
    }


    function refresh2(){
        $.ajax({
            type: "post",
            url: '<%=basePath%>/oa/juris/getProcessTree.action',
            async:false,
            success: function (backData) {
                $('#tree2').tree({
                    data:backData
                });

            }
        })
        dgReload3();
    }
    function collapseAll2() {//关闭树
        $('#tree2').tree('collapseAll');
    }
    function expandAll2() {//展开树
        $('#tree2').tree('expandAll');
    }


    function refresh3(){
        $.ajax({
            type: "post",
            url: '<%=basePath%>/oa/juris/getProcessTree.action',
            async:false,
            success: function (backData) {
                $('#tree3').tree({
                    data:backData
                });
            }
        })
        dgReload4();
    }
    function collapseAll3() {//关闭树
        $('#tree3').tree('collapseAll');
    }
    function expandAll3() {//展开树
        $('#tree3').tree('expandAll');
    }


    function refresh4(){
        $.ajax({
            type: "post",
            url: '<%=basePath%>/oa/juris/getProcessTree.action',
            async:false,
            success: function (backData) {
                $('#tree4').tree({
                    data:backData
                });
            }
        })
        dgReload5();
    }
    function collapseAll4() {//关闭树
        $('#tree4').tree('collapseAll');
    }
    function expandAll4() {//展开树
        $('#tree4').tree('expandAll');
    }

    function appointAssignees(ps){
        //弹出窗口
        var bodyWidth =  $("body").width()
        var bodyHeight = $("body").height()
        var height = bodyHeight;
        var width = bodyWidth;
        var top = Math.round((window.screen.height - height) / 2);
        var left = Math.round((window.screen.width - 970) / 2);
        $('#win').window({
            width : 970,
            height : 550,
            top:top,
            left:left,
            title : "人员选择",
            modal : true,
            collapsible:false,
            minimizable : false,
            maximizable : false,
            content : "<iframe src='${pageContext.request.contextPath}/activiti/allWorks/toappointAssigneesUI.action?voStr="+encodeURI(JSON.stringify(ps))+"' height='100%' width='100%' frameborder='0px' ></iframe>"
        });
    }


</script>




