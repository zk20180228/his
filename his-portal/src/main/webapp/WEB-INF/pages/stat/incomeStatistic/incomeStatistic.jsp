<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <%@ include file="/common/metas.jsp" %>
    <title>收入统计汇总</title>
    <style type="text/css">
        * {
            box-sizing: border-box;
        }

        .panel-body, .panel {
            overflow: visible;
        }

        .addList dl:first-child ul {
            overflow: visible;
        !important;
            clear: both;
        }

        .clearfix:after {
            content: "";
            display: table;
            height: 0;
            visibility: hidden;
            clear: both;
        }

        .xmenu dl dd ul {
            overflow: visible;
        !important;
            clear: both;
        }

        .clearfix {
            *zoom: 1; /* IE/7/6*/
        }

        #searchForm {
            display: block;
            height: 38px;
            width: 100%;
            position: absolute;
            top: 0;
            left: 0;
            z-index: 1;
        }

        #contentBox {
            width: 100%;
            height: 100%;
            padding-top: 38px;
        }
    </style>
</head>
<body style="margin: 0px; padding: 0px;">
<!--<div id="divLayout" class="easyui-layout" data-options="fit:true" style="width:100%;height:100%">-->
<form id="searchForm">
    <table border="0" style="height:100%;width: 100%">

        <tr>
            <td style="width:55px;padding-left:5px">
                日期:
            </td>
            <td style="width:98px;padding-right:5px">
                <input id="start" name="t1.startDate" class="Wdate" type="text" value="${sTime}" onClick="WdatePicker()"
                       style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
            </td>
            <td style="width:10px;">
                至
            </td>
            <td style="width:98px;padding-left:5px">
                <input id="end" name="t1.endDate" class="Wdate" type="text" value="${eTime}" onClick="WdatePicker()"
                       style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
            </td>
            <td id="classA" class="newMenu" style="width:120px;z-index:1;position: relative;padding-left:5px">
                <div class="deptInput menuInput">
                    <input id="queryDept" class="ksnew" placeholder="请选择科室" readonly/><span></span></div>
                <div id="m3" class="xmenu" style="display: none;">
                    <div class="searchDept">
                        <input type="text" name="searchByDeptName" placeholder="回车查询"/>
                        <span class="searchMenu"><i></i>查询</span>
                        <a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn">
                            <span class="a-btn-text">取消</span>
                        </a>
                        <a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn">
                            <span class="a-btn-text">清空</span>
                        </a>
                        <a name="menu-confirm" href="javascript:void(0);" class="a-btn">
                            <span class="a-btn-text">确定</span>
                        </a>
                    </div>
                    <div class="select-info" style="display:none">
                        <label class="top-label">已选部门：</label>
                        <ul class="addDept">
                        </ul>
                    </div>
                    <div class="depts-dl">
                        <div class="addList"></div>
                        <div class="tip" style="display:none">没有检索到数据</div>
                    </div>
                </div>
            </td>
            <td id="classB" class="newMenu" style="width:120px;z-index:1;position: relative;padding-left:5px">
                <div class="totalInput menuInput">
                    <input class="ksnew" id="statType" placeholder="请选择统计类别" readonly/><span></span></div>
                <!-- 统计大类弹出层  -->
                <div id="m2" class="xmenu" style="display: none;">
                    <div class="searchDept">
                        <input type="text" name="searchByDeptName" placeholder="回车查询"/>
                        <span class="searchMenu"><i></i>查询</span>
                        <a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn">
                            <span class="a-btn-text">取消</span>
                        </a>
                        <a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn">
                            <span class="a-btn-text">清空</span>
                        </a>
                        <a name="menu-confirm" href="javascript:void(0);" class="a-btn">
                            <span class="a-btn-text">确定</span>
                        </a>
                    </div>
                    <div class="select-info" style="display:none">
                        <label class="top-label">已选费别：</label>
                        <ul class="addDept">
                        </ul>
                    </div>
                    <div class="depts-dl">
                        <div class="addList"></div>
                        <div class="tip" style="display:none">没有检索到数据</div>
                    </div>
                </div>
            </td>
            <td style="">
                <shiro:hasPermission name="${menuAlias}:function:query">
                    <a href="javascript:void(0)" onclick="functionsearch()" class="easyui-linkbutton"
                       iconCls="icon-search" style="margin-left:5px;margin-top:0px;">查询</a>
                </shiro:hasPermission>
                <a href="javascript:clear()" onclick="" class="easyui-linkbutton" iconCls="reset"
                   style="margin-top:0px;">重置</a>
                <shiro:hasPermission name="${menuAlias}:function:print">
                    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="printOut()"
                       data-options="iconCls:'icon-printer'">打印</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="${menuAlias}:function:export">
                    <a href="javascript:queryBottenWeek()" class="easyui-linkbutton" data-options="iconCls:'icon-down'">导出</a>
                </shiro:hasPermission>
            </td>
            <td id="btn-title" style="" class="incomeStatisticSize2" colspan="9">
                <shiro:hasPermission name="${menuAlias}:function:query">
                    <a href="javascript:void(0)" onclick="timeSearch(5)" style="float: right;margin-left: 10px"
                       class="easyui-linkbutton" iconCls="icon-date">当年</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="${menuAlias}:function:query">
                    <a href="javascript:void(0)" onclick="timeSearch(7)" style="float: right;margin-left: 10px"
                       class="easyui-linkbutton" iconCls="icon-date">上月</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="${menuAlias}:function:query">
                    <a href="javascript:void(0)" onclick="timeSearch(4)" style="float: right;margin-left: 10px"
                       class="easyui-linkbutton" iconCls="icon-date">当月</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="${menuAlias}:function:query">
                    <a href="javascript:void(0)" onclick="timeSearch(6)" style="float: right;margin-left: 10px"
                       class="easyui-linkbutton" iconCls="icon-date">十五天</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="${menuAlias}:function:query">
                    <a href="javascript:void(0)" onclick="timeSearch(3)" style="float: right;margin-left: 10px"
                       class="easyui-linkbutton" iconCls="icon-date">七天</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="${menuAlias}:function:query">
                    <a href="javascript:void(0)" onclick="timeSearch(2)" style="float: right;margin-left: 10px"
                       class="easyui-linkbutton" iconCls="icon-date">三天</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="${menuAlias}:function:query">
                    <a href="javascript:void(0)" onclick="timeSearch(1)" style="float: right;" class="easyui-linkbutton"
                       iconCls="icon-date">当天</a>
                </shiro:hasPermission>
            </td>
        </tr>
    </table>
</form>
<div id="contentBox" data-options="region:'center',split:false,border:false,fit:true">
    <div id="st" class="easyui-layout" style="width: 100%; height: 100%; padding:0px 0px 0px 0px">
        <div data-options="region:'center',split:false,border:false" style="width: 60%; height: 100%;">
            <table id="grid" style="width: 100%;height:100%" data-options="fit:true"></table>
            <form id="saveForm" method="post">
                <input type="hidden" name="rows" id="rows" value=""/>
            </form>
            <form method="post" id="reportToHiddenForm">
                <input type="hidden" name="start" id="reportToStart" value=""/>
                <input type="hidden" name="end" id="reportToEnd" value=""/>
                <input type="hidden" name="queryDept" id="reportToQueryDept" value=""/>
                <input type="hidden" name="statType" id="reportToStatType" value=""/>
                <input type="hidden" name="rows" id="reportToRows" value=""/>
                <input type="hidden" name="fileName" id="reportToFileName" value=""/>
            </form>
        </div>
    </div>
    <div class="easyui-window"
         data-options="iconCls:'icon-book',closed:true,modal:true,collapsible:false,minimizable:false,maximizable:false"
         style="z-index: 0">
        <table id="statgrid"></table>
    </div>
</div>
<!--</div>-->
</body>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
    var empFlag = true;
    var menuAlias = '${menuAlias}';
    var flag = "${flag}";//职务判断
    var ids;//存取当前登录科室Code
    var data = '';
    var deptMap = new Map();//科室渲染
    var beginTime;//开始时间
    var endTime;//结束时间

    var allColumn = {
        deptName: {field: 'deptName', title: '住院科室', width: '9%', align: 'center', formatter: deptCombo},
        totCost01: {field: 'totCost01', title: '西药费', width: '7%', align: 'right', formatter: formatterNum},
        totCost02: {field: 'totCost02', title: '中成药费', width: '6%', align: 'right', formatter: formatterNum},
        totCost03: {field: 'totCost03', title: '中草药费', width: '6%', align: 'right', formatter: formatterNum},
        totCost04: {field: 'totCost04', title: '床位费', width: '6%', align: 'right', formatter: formatterNum},
        totCost05: {field: 'totCost05', title: '治疗费', width: '6%', align: 'right', formatter: formatterNum},
        totCost07: {field: 'totCost07', title: '检查费', width: '6%', align: 'right', formatter: formatterNum},
        totCost08: {field: 'totCost08', title: '放射费', width: '6%', align: 'right', formatter: formatterNum},
        totCost09: {field: 'totCost09', title: '化验费', width: '6%', align: 'right', formatter: formatterNum},
        totCost10: {field: 'totCost10', title: '手术费', width: '6%', align: 'right', formatter: formatterNum},
        totCost11: {field: 'totCost11', title: '输血费', width: '6%', align: 'right', formatter: formatterNum},
        totCost12: {field: 'totCost12', title: '输氧费', width: '6%', align: 'right', formatter: formatterNum},
        totCost13: {field: 'totCost13', title: '材料费', width: '6%', align: 'right', formatter: formatterNum},
        totCost14: {field: 'totCost14', title: '其他费用', width: '6%', align: 'right', formatter: formatterNum},
        totCost15: {field: 'totCost15', title: '护理费', width: '6%', align: 'right', formatter: formatterNum},
        totCost16: {field: 'totCost16', title: '诊察费', width: '6%', align: 'right', formatter: formatterNum},
        totCost: {
            field: 'totCost', title: '总计', width: '7%', align: 'right', formatter: function (value, row, index) {
                return getSum(row);
            }
        }
    };
    var fields = [];
    var initColumns = [[]];
    for (var column in allColumn) {
        if (column !== "deptName" && column !== "totCost") {
            fields.push(column);
        }
        initColumns[0].push(allColumn[column]);
    }
    var printFile;
    $(function () {
        $(".deptInput").MenuList({
            width: 530, //设置宽度，不写默认为530，不要加单位
            height: 400, //设置高度，不写默认为400，不要加单位
            menulines: 2, //设置菜单每行显示几列（1-5），默认为2
            dropmenu: "#m3",//弹出层id，必须要写
            isSecond: false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
            para: "I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
            firsturl: "<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias=" + menuAlias + "&deptTypes=", //获取列表的url，必须要写
            relativeInput: ".doctorInput"	//与其级联的文本框，必须要写
        });

        $('#m3 .addList h2 input').click();
        $('a[name=\'menu-confirm\']').click();
        if ($('#queryDept').getMenuIds() == '') {
            $("#tableList").datagrid();
            $("body").setLoading({
                id: "body",
                isImg: false,
                text: "无数据权限"
            });
        }
        //科室渲染
        $.ajax({
            url: "<%=basePath%>publics/consultation/querydeptComboboxs.action",
            success: function (deptData) {
                deptMap = deptData;
            }
        });
        var v = $('#start').val();
        beginTime = v;
        $('#startDate').html(v);
        var v = $('#end').val();
        endTime = v;
        $('#endDate').html(v);

        $('#start').blur(function () {//当下拉面板隐藏的时候触发
            var v = $('#start').val();
            $('#startDate').html(v);
        });
        $('#end').blur(function () {
            var v = $('#end').val();
            $('#endDate').html(v);
        });

        $('#queryDept').val("${loginDeptName}");
        ids = "${loginDeptCode}";
        $(".totalInput").MenuList({
            width: 530, //设置宽度，不写默认为530，不要加单位
            height: 400, //设置高度，不写默认为400，不要加单位
            dropmenu: "#m2",//弹出层id，必须要写
            para: "ZY01",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
            firsturl: "<%=basePath%>statistics/Statistic/getStatName1.action?reportCode=" //获取列表的url，必须要写
        });
        if (typeof(listParam) == 'undefined') {
            listParam = "";
        }

        var startDate = $('#start').val();
        var endDate = $('#end').val();
        var deptCodes = $('#queryDept').getMenuIds();
        //初始化不查询所有科室
        deptCodes = "";
        $('#grid').datagrid({
            fitColumns: true,
            striped: true,
            rownumbers: true,
            showFooter: true,
            // columns:initColumns,
            singleSelect: true
        });
        loadGrid(startDate, endDate, deptCodes, initColumns);
        $('#check1').bind('click', function () {
            $('#grid2').datagrid('checkAll');
        })
        $('#check2').bind('click', function () {
            unselectRow();
        })

    });

    function functionsearch() {
        var start = $('#start').val();
        var end = $('#end').val();
        if (start && end) {
            if (start > end) {
                $.messager.alert("提示", "开始时间不能大于结束时间！");
                close_alert();
                return;
            }
        }
        var fiel = $("#statType").attr("name");
        var startDate = $('#start').val();
        var endDate = $('#end').val();
        var deptCodes = $('#queryDept').getMenuIds();
        if (fiel) {
            var tempColumns = [[]];
            tempColumns[0].push(allColumn["deptName"]);
            var fiels = fiel.split('_');
            for (var i = 0; i < fiels.length; i++) {
                if (fiels[i]) {
                    var field = "totCost" + fiels[i];
                    tempColumns[0].push(allColumn[field]);//放入新的需要求和的列
                }
            }
            tempColumns[0].push(allColumn["totCost"]);
            loadGrid(startDate, endDate, deptCodes, tempColumns);
        }
        else {
            loadGrid(startDate, endDate, deptCodes, initColumns);
        }

    }

    function loadGrid(startDate, endDate, deptCodes, columns) {
        $('#grid').datagrid("loading");
        $.post("${pageContext.request.contextPath}/statistics/Statistic/statisticData.action?menuAlias=${menuAlias}&flag=" + ids,
            {
                sTime: startDate,
                eTime: endDate,
                ids: deptCodes
            },
            function (data) {
                $('#grid').datagrid("loaded");
                $('#grid').datagrid({
                    columns: columns
                });
                var resultData = {};
                resultData.rows = data;
                resultData.footer = [];
                resultData.footer.push(getRowSum(data));
                $('#grid').datagrid("loadData", resultData);
            });
    }

    //时间段查询
    function timeSearch(val) {
        if (val == 1) {
            var myDate = new Date();
            var year = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
            var month = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
            var date = myDate.getDate();
            month = month < 10 ? "0" + month : month;
            date = date < 10 ? "0" + date : date;
            var day = year + "-" + month + "-" + date;
            var Stime = $('#start').val(day);
            var Etime = $('#end').val(day);
            functionsearch();
        } else if (val == 2) {
            var nowd = new Date();
            var myDate = new Date(nowd.getTime() - 3 * 24 * 3600 * 1000);
            var year = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
            var month = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
            var date = myDate.getDate();
            month = month < 10 ? "0" + month : month;
            date = date < 10 ? "0" + date : date;
            var day2 = year + "-" + month + "-" + date;
            var Stime = $('#start').val(day2);
            nowd = new Date();
            myDate = new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
            year = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
            month = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
            date = myDate.getDate();
            month = month < 10 ? "0" + month : month;
            date = date < 10 ? "0" + date : date;
            day2 = year + "-" + month + "-" + date;
            var Etime = $('#end').val(day2);
            functionsearch();
        } else if (val == 3) {
            var nowd = new Date();
            var myDate = new Date(nowd.getTime() - 7 * 24 * 3600 * 1000);
            var year = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
            var month = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
            var date = myDate.getDate();
            month = month < 10 ? "0" + month : month;
            date = date < 10 ? "0" + date : date;
            var day3 = year + "-" + month + "-" + date;
            var Stime = $('#start').val(day3);
            nowd = new Date();
            myDate = new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
            year = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
            month = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
            date = myDate.getDate();
            month = month < 10 ? "0" + month : month;
            date = date < 10 ? "0" + date : date;
            day3 = year + "-" + month + "-" + date;
            var Etime = $('#end').val(day3);
            functionsearch();
        } else if (val == 4) {
            var myDate = new Date();
            var year = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
            var month = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
            month = month < 10 ? "0" + month : month;
            var day = year + "-" + month + "-" + "01";
            var Stime = $('#start').val(day);
            myDate = new Date();
            year = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
            month = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
            date = myDate.getDate();
            month = month < 10 ? "0" + month : month;
            date = date < 10 ? "0" + date : date;
            day = year + "-" + month + "-" + date;
            var Etime = $('#end').val(day);
            functionsearch();
        } else if (val == 5) {
            var myDate = new Date();
            var year = myDate.getFullYear();
            var day = year + "-" + "01" + "-" + "01";
            var Stime = $('#start').val(day);
            myDate = new Date();
            year = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
            month = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
            date = myDate.getDate();
            month = month < 10 ? "0" + month : month;
            date = date < 10 ? "0" + date : date;
            day = year + "-" + month + "-" + date;
            var Etime = $('#end').val(day);
            functionsearch();
        } else if (val == 6) {
            var nowd = new Date();
            var myDate = new Date(nowd.getTime() - 15 * 24 * 3600 * 1000);
            var year = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
            var month = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
            var date = myDate.getDate();
            month = month < 10 ? "0" + month : month;
            date = date < 10 ? "0" + date : date;
            var day2 = year + "-" + month + "-" + date;
            var Stime = $('#start').val(day2);
            nowd = new Date();
            myDate = new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
            year = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
            month = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
            date = myDate.getDate();
            month = month < 10 ? "0" + month : month;
            date = date < 10 ? "0" + date : date;
            day2 = year + "-" + month + "-" + date;
            var Etime = $('#end').val(day2);
            functionsearch();
        } else if (val == 7) {
            var date = new Date();
            var year = date.getFullYear();
            var month = date.getMonth();
            if (month == 0) {
                year = year - 1;
                month = 12;
            }
            var startTime = year + '-' + month + '-01';
            $('#start').val(startTime);
            var date = new Date(year, month, 0);
            var endTime = year + '-' + month + '-' + date.getDate();
            $('#end').val(endTime);
            functionsearch();
        }
    }

    function conveterParamsToJson(paramsAndValues) {
        var jsonObj = {};

        var param = paramsAndValues.split("&");
        for (var i = 0; param != null && i < param.length; i++) {
            var para = param[i].split("=");
            jsonObj[para[0]] = para[1];
        }

        return jsonObj;
    }

    /**
     * 将表单数据封装为json
     * @param form
     * @returns
     */
    function getFormData(form) {
        var formValues = $("#" + form).serialize();
        if (flag == 'true') {
            if ('t1.id' != $('#queryDept').getMenuIds()) {
                formValues += ('&t1.id=' + $('#queryDept').getMenuIds());
            }
        } else {
            formValues += ('&t1.id=' + ids);
        }
        //关于jquery的serialize方法转换空格为+号的解决方法
        formValues = formValues.replace(/\+/g, " "); // g表示对整个字符串中符合条件的都进行替换
        var temp = decodeURIComponent(JSON
            .stringify(conveterParamsToJson(formValues)));
        var queryParam = JSON.parse(temp);
        return queryParam;
    }

    /**
     * 反选功能
     */
    function unselectRow() {
        var s_rows = $.map($('#grid2').datagrid('getSelections'),
            function (n) {
                return $('#grid2').datagrid('getRowIndex', n);
            });
        $('#grid2').datagrid('selectAll');
        $.each(s_rows, function (i, n) {
            $('#grid2').datagrid('unselectRow', n);
        });
    }

    /**
     * 计算行总计
     */
    function getSum(row) {
        var opts = $('#grid').datagrid('getColumnFields');
        //去掉deptName列
        opts.shift();
        //去掉totCost列
        opts.pop();
        var sum = 0;
        for (var i = 0; i < opts.length; i++) {
            var colName = opts[i];
            if (row[colName]) {
                sum += row[colName];
            }
        }
        return formatterNum(sum);
    }

    /**
     * 计算列统计
     */
    function getRowSum(data) {
        var opts = $('#grid').datagrid('getColumnFields');
        //去掉deptName列
        opts.shift();
        var colName = '';
        var sum = 0;
        var field = '{deptName:' + "'总计:'";
        for (var i = 0; i < opts.length; i++) {
            var totSum = 0;
            colName = opts[i];
            for (var j = 0; j < data.length; j++) {
                if (data[j][colName]) {
                    totSum += data[j][colName];
                }
            }
            sum += totSum;
            field = field + ',' + colName + ':' + totSum.toFixed(2);
        }
        field += ',totCost:' + sum.toFixed(2) + '}';
        field = eval("(" + field + ")");
        return field;
    }

    //清空
    function clear() {
        $('#start').val('${sTime}');
        $('#end').val('${eTime}');
        $('#queryDept').val('');
        $('#queryDept').attr('name', '');
        $('#statType').val('');
        $('#statType').attr('name', '');
        $("a[name='menu-confirm-cancel']").click();
        functionsearch();
    }

    //科室渲染
    function deptCombo(value, row, index) {
        if (value != null) {
            return value;
        } else if (null != row.inhosDeptcode) {
            if (deptMap[row.inhosDeptcode]) {
                return deptMap[row.inhosDeptcode];
            } else {
                return '';
            }
        }
    }

    function formatterNum(value, row, index) {
        if (value != null) {
            value = parseFloat((value + "").replace(/[^\d\.-]/g, "")).toFixed(2) + "";
            var l = value.split(".")[0].split("").reverse(), r = value.split(".")[1];
            t = "";
            for (i = 0; i < l.length; i++) {
                t += l[i] + ((i + 1) % 3 === 0 && (i + 1) !== l.length && l[i + 1] !== "-" ? "," : "");
            }
            t = t.split("").reverse().join("") + "." + r;
            //去掉负数负号之后的逗号，例如-,125.00改为-125.00
            if (t.indexOf("-") === 0 && t.indexOf(",") === 1) {
                t = t.substring(0, 1) + t.substring(2, t.length);
            }
            return t;
        } else {
            return "0.00";
        }
    }

    /***
     * 打印功能
     */
    function printOut() {
        var rows = $('#grid').datagrid('getRows');
        var start = $('#start').val();
        var end = $('#end').val();
//     var queryDept = $('#queryDept').val();
//     var statType = $('#statType').val();
        if (start == null || start == '' || end == null || end == '') {
            $.messager.alert("提示", "日期不能为空！");
            return;
        }
        if (rows != null && rows != '') {
            $.messager.confirm('确认', '确定要打印吗?', function (res) {//提示是否打印
                if (res) {
                    //hedong 20170316  报表打印参数传递改造为表单POST提交的方式示例
                    //给表单的隐藏字段赋值
                    $("#reportToStart").val(start);
                    $("#reportToEnd").val(end);
// 			    $("#reportToQueryDept").val(queryDept);
// 			    $("#reportToStatType").val(statType);
                    printFile = fields;
                    printFile.push("deptName");
                    $("#reportToRows").val(JSON.stringify(rows, fields));
                    $("#reportToFileName").val("ZYSRTJHZ");

                    //表单提交 target
                    var formTarget = "hiddenFormWin";
                    var tmpPath = "<%=basePath%>statistics/Statistic/iReportStatistic_pdf.action";
                    //设置表单target
                    $("#reportToHiddenForm").attr("target", formTarget);
                    //设置表单访问路径
                    $("#reportToHiddenForm").attr("action", tmpPath);
                    //表单提交时打开一个空的窗口
                    $("#reportToHiddenForm").submit(function (e) {
                        var timerStr = Math.random();
                        window.open('about:blank', formTarget, 'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no,status=no');
                    });
                    //表单提交
                    $("#reportToHiddenForm").submit();
                }
            });
        } else {
            $.messager.alert("提示", "没有数据，不能打印！");
        }
    }

    /***
     * 导出功能
     */
    function queryBottenWeek() {
        var start = $('#start').val();
        var end = $('#end').val();
        if (start == null || start == '' || end == null || end == '') {
            $.messager.alert("提示", "日期不能为空！");
            return;
        }
        var rows = $('#grid').datagrid('getRows');
        if (rows == null || rows.length == 0) {
            $.messager.alert("提示", "没有数据，不能导出！");
            return;
        } else {
            $.messager.confirm('确认', '确定要导出吗?', function (res) {//提示是否导出
                if (res) {
                    printFile = fields;
                    printFile.push("deptName");
                    //给表单的隐藏字段赋值
                    $("#rows").val(JSON.stringify(rows, printFile));
                    $('#saveForm').form('submit', {
                        url: '<%=basePath%>statistics/Statistic/statisticInfo.action',
                        onSubmit: function () {
                            return $(this).form('validate');
                        },
                        success: function (data) {
                            $.messager.alert("提示", "导出成功！", "success");
                        },
                        error: function (data) {
                            $.messager.alert("提示", "导出失败！", "error");
                        }
                    });
                }
            });
        }
    }

    $(function () {
        if ($("#searchForm").width() < 1400) {
            $("#searchForm").css({
                "height": "35px",
            });
            $("#contentBox").css("height", "calc(100% - 15px)")
            $("#classA .menuInput").css("width", "110px")
            $("#classB .menuInput").css("width", "110px")
            $("#classA div input").css("width", "80px")
            $("#classB div input").css("width", "80px")
            $("#searchForm td").css("fontSize", "14px")
            $("#searchForm .easyui-linkbutton").children().children().css("fontSize", "14px")
        }
    })
</script>
</html>