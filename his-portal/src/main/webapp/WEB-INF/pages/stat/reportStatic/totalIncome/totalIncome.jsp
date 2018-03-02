﻿
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta charset="utf-8"/>
    <title>总收入情况统计</title>
    <style type="text/css">
        * {
            box-sizing: border-box;
        }

        li {
            list-style: none;
        }

        .clearfix:after {
            content: "";
            height: 0;
            line-height: 0;
            display: block;
            clear: both;
            visibility: hidden;
        }

        body {
            overflow-y: auto;
            position: relative;
        }

        .clearfix {
            zoom: 1;
        }

        .All {
            width: 100%;
        }

        .All li {
            float: left;
            width: 25%;
        }

        .tableBox li {
            float: left;
            width: 25%;
            height: 280px;
            overflow-y: auto;
            border-bottom: 1px solid;
            border-color: #bfe8e8 !important;
        }

        .timeBox li {
            float: left;
            width: 25%;
            position: relative;
        }

        .timeBox li > div:nth-child(1) {
            width: 100%;
            height: 50px;
            line-height: 50px;
        }

        .timeBox li > div:nth-child(1) span {
            line-height: 50px;
        }

        .AtimeBoxll li > div:nth-child(1) span:first-child {
            margin-left: 20px;
        }

        .All li > div:nth-child(2) {
            width: 100%;
            height: 280px;
        }

        #timeSelfS {
            width: 100px;
        }

        #timeSelfE {
            width: 100px;
        }

        #tableBox li > table:nth-child(2) {
            margin-top: 37px;
        }

        .tabletitle {
            position: absolute;
            width: 25%;
            top: 50px;
        }

        table tr td {
            width: 33.33%;
            /* 				text-indent: 10px; */
            line-height: 25px;
            /* 				padding: 0 !important; */
            padding: 0 5px;
        }

        .TDlabel td {
            line-height: 27px;
        }

        .line {
            position: fixed;
            z-index: 1000;
            top: 0;
            width: 1px;
            height: 100%;
            background-color: #D4D4D4;
        }

        .line1 {
            left: 25%;
        }

        .line2 {
            left: 50%;
        }

        .line3 {
            left: 75%;
        }

        .timeBox li div span:nth-child(1) {
            margin-left: 10px
        }

        .tabletitle .TDlabel {
            text-align: center;
            background-color: #E0ECFF;
        }

        /*@media screen and (max-height: 805px) {
            .line1 {
                left: calc(25% - 6px);
            }

            .line2 {
                left:calc(50% - 8px) ;
            }

            .line3 {
                left:calc(75% - 11px);
            }
        }*/
    </style>
</head>

<body class=clearfix>
<ul class="timeBox clearfix">
    <li>
        <div class="day_top">
            <table style="padding-top: 10px;margin-left: 20px;">
                <tr>
                    <td>按(日)统计:
                        <input id="dayTime" class="Wdate" type="text" value="${Etime}"
                               onClick="WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:picked1,maxDate:'%y-%M-%d'})"/>
                    </td>
                </tr>
            </table>
        </div>
    </li>
    <li>
        <div class="month_top">
            <table style="padding-top: 10px;margin-left: 20px;">
                <tr>
                    <td>按(月)统计:
                        <input id="monthTime" class="Wdate" type="text" value="${Etime}"
                               onClick="WdatePicker({dateFmt:'yyyy-MM',onpicked:picked2,maxDate:'%y-%M'})"/>
                    </td>
                </tr>
            </table>
        </div>
    </li>
    <li>
        <div class="year_top">
            <table style="padding-top: 10px;margin-left: 20px;">
                <tr>
                    <td>按(年)统计:
                        <input id="yearTime" class="Wdate" type="text" value=""
                               onClick="WdatePicker({dateFmt:'yyyy',onpicked:picked3,maxDate:'%y'})"/>
                    </td>
                </tr>
            </table>
        </div>
    </li>
    <li>
        <div class="diy_top">
            <table style="padding-top: 10px;margin-left: 20px;">
                <tr>
                    <td>自定义统计:
                        <input id="timeSelfS" class="Wdate" type="text" value="${Stime}"
                               onClick="var timeSelfE=$dp.$('timeSelfE');WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'timeSelfE\')}',onpicked:function(){timeSelfE.click();}})"/>
                        至
                        <input id="timeSelfE" class="Wdate" type="text" value="${Etime}"
                               onClick="WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:pickedFuncBySelf,minDate:'#F{$dp.$D(\'timeSelfS\')}'})"/>
                    </td>
                </tr>
            </table>
        </div>
    </li>
</ul>
<ul class="tableBox clearfix">
    <li id="dayTable">
        <table class="tableCss tabletitle" style="border-collapse: collapse;border-spacing: 0;">
            <tr class="TDlabel">
                <td>费别</td>
                <td>金额(万元)</td>
                <td>比例</td>
            </tr>
        </table>
        <table id="dayDetai" class="tableCss"
               style="margin-top: 27px;width: 100%;border-collapse: collapse;border-spacing: 0;">

        </table>
    </li>
    <li id="monthTable">
        <table class="tableCss tabletitle" style="border-collapse: collapse;border-spacing: 0;">
            <tr class="TDlabel">
                <td>费别</td>
                <td>金额(万元)</td>
                <td>比例</td>
            </tr>
        </table>
        <table id="monthDetai" class="tableCss"
               style="margin-top: 27px;width: 100%;border-collapse: collapse;border-spacing: 0;">
        </table>
    </li>
    <li id="yearTable">
        <table class="tableCss tabletitle" style="border-collapse: collapse;border-spacing: 0;">
            <tr class="TDlabel">
                <td>费别</td>
                <td>金额(万元)</td>
                <td>比例</td>
            </tr>
        </table>
        <table id="yearDetai" class="tableCss"
               style="margin-top: 27px;width: 100%;border-collapse: collapse;border-spacing: 0;">
        </table>
    </li>
    <li id="diyTable">
        <table class="tableCss tabletitle" style="border-collapse: collapse;border-spacing: 0;">
            <tr class="TDlabel">
                <td>费别</td>
                <td>金额(万元)</td>
                <td>比例</td>
            </tr>
        </table>
        <table id="diyDetai" class="tableCss"
               style="margin-top: 27px;width: 100%;border-collapse: collapse;border-spacing: 0;">
        </table>
    </li>
</ul>
<ul class="All clearfix">
    <li class="day_box">
        <div id="day_middle">
            <div id="day_middle_echar" style="width: 100%;height: 350px;"></div>
        </div>
    </li>
    <li class="month">
        <div id="month_middle">
            <div id="month_middle_echar" style="width: 100%;height: 350px;"></div>
        </div>
    </li>
    <li class="year_top">
        <div id="year_middle">
            <div id="year_middle_echar" style="width: 100%;height: 350px;"></div>
        </div>
    </li>
    <li class="diy">
        <div id="diy_middle">
            <div id="diy_middle_echar" style="width: 100%;height: 350px;"></div>
        </div>
    </li>
    <li class="day_box_bottom">
        <div id="day_middle_echar_bottom" style="width: 100%;height: 350px;"></div>
    </li>
    <li class="day_box_bottom">
        <div id="month_middle_echar_bottom" style="width: 100%;height: 350px;"></div>
    </li>
    <li class="day_box_bottom">
        <div id="year_middle_echar_bottom" style="width: 100%;height: 350px;"></div>
    </li>
    <li class="day_box_bottom">
        <div id="diy_middle_echar_bottom" style="width: 100%;height: 350px;"></div>
    </li>
</ul>
<div class="datagrid-pager line line1"></div>
<div class="datagrid-pager line line2"></div>
<div class="datagrid-pager line line3"></div>
</body>
<script src="${pageContext.request.contextPath}/javascript/echarts/echarts.min.js" type="text/javascript"
        charset="utf-8"></script>
<script type="text/javascript">
    var startDate = "${Etime}";
    var dateFor = startDate.split('-');
    $('#yearTime').val(dateFor[0]);
    $('#monthTime').val(dateFor[0] + '-' + dateFor[1]);
    $('#dayTime').val(startDate);
    $("#timeSelfS").val(dateFor[0] + '-' + dateFor[1] + '-' + "01")
    $("#timeSelfE").val(startDate)
    var divData = dateFor[0] + '-' + dateFor[1] + '-' + "1," + startDate
    var colorObj = {} // 设置颜色对象
    var color = ['#ff7f50', '#da70d6', '#32cd32', '#6495ed',
        '#ff69b4', '#ba55d3', '#cd5c5c', '#ffa500', '#40e0d0',
        '#1e90ff', '#ff6347', '#7b68ee', '#00fa9a', '#ffd700',
    ];
    var areaColor = ['#61a0a8', '#653957', '#d48265'];

    //金额格式
    function changeCost(manny) {
        if (manny != null) {
            var value = manny / 10000;
            value = parseFloat((value + "").replace(/[^\d\.-]/g, "")).toFixed(2) + "";
            var l = value.split(".")[0].split("").reverse(),
                r = value.split(".")[1],
                t = "";
            for (i = 0; i < l.length; i++) {
                t += l[i] + ((i + 1) % 3 === 0 && (i + 1) !== l.length ? "," : "");
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

    var $Obj = {
        day: document.getElementById('day_middle_echar'),
        month: document.getElementById('month_middle_echar'),
        year: document.getElementById('year_middle_echar'),
        diy: document.getElementById('diy_middle_echar'),
        daybottom: document.getElementById('day_middle_echar_bottom'),
        monthbottom: document.getElementById('month_middle_echar_bottom'),
        yearbottom: document.getElementById('year_middle_echar_bottom'),
        diybottom: document.getElementById('diy_middle_echar_bottom')
    };
    var $table = {
        dayTable: document.getElementById('dayTable'),
        monthTable: document.getElementById('monthTable'),
        yearTable: document.getElementById('yearTable'),
        diyTable: document.getElementById('diyTable')
    }

    var timeValueObj = {
        dayTime: document.getElementById('dayTime'),
        monthTime: document.getElementById('monthTime'),
        yearTime: document.getElementById('yearTime'),
        statTime: document.getElementById('timeSelfS'),
        endTime: document.getElementById('timeSelfE')
    }
    var echartsObj = {
        dayChar: echarts.init($Obj.day),
        monthChar: echarts.init($Obj.month),
        yearChar: echarts.init($Obj.year),
        diyChar: echarts.init($Obj.diy),
        daybottomChar: echarts.init($Obj.daybottom),
        monthbottomChar: echarts.init($Obj.monthbottom),
        yearbottomChar: echarts.init($Obj.yearbottom),
        diybottomChar: echarts.init($Obj.diybottom)
    }
    //获取颜色数组
    $(function () {
        $.ajax({
            url: "<%=basePath%>statistics/listTotalIncomeStatic/queryFeeName.action",
            type: "post",
            success: function (data) {
                for (var i = 0; i < data.length; i++) {
                    colorObj[data[i].feeStatName] = color[data[i].feeStatCode - 1]
                }
                $.ajax({
                    url: "<%=basePath%>statistics/listTotalIncomeStatic/queryAreaName.action",
                    type: "post",
                    success: function (data) {
                        for (var i = 0; i < data.length; i++) {
                            colorObj[data[i].name] = areaColor[data[i].encode - 1]
                        }
                        // init();
                    }
                })
            }
        });
    });

    //初始化
    for (var key in $Obj) {
        res($Obj[key], echartsObj[key + "Char"])
    }

    pickedFunc1($('#dayTime').val());
    pickedFunc2($('#monthTime').val());
    pickedFunc3($('#yearTime').val());
    pickedFuncBySelf();

    //初始化方法
    function res($dom, $char) {
        $char.setOption({
            title: {
                text: '',
                x: 'center',
                y: "5px"
            },

            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.name + "(" + params.percent + "%)<br/>" + (Number(params.value) / 10000).toFixed(2) + "万元";
                }
            },
            /* legend: {
                orient: 'vertical',
                x: 'left',
                data: [],
                padding: [40, 0, 0, 40]
            }, */
            series: [{
                name: '总收入情况',
                type: 'pie',
                radius: '55%',
                center: ['50%', '55%'],
                data: [],
                itemStyle: {
                    normal: {
                        label: {
                            show: true,
                            formatter: '{b} {d}%'
                        },
                        labelLine: {
                            show: true
                        }
                    }
                },
                fontSize: 30
            }]
        });
    }

    //设置上面的char 和 table
    function setchar(dataAll, $char, $dom, noDataName, $tablea) {
        if (dataAll.data.length == 0) {
            $($dom).setLoading({
                id: noDataName,
                text: "无数据",
                opacity: 1,
                backgroudColor: "#ffffff",
                isImg: false
            })
            $tablea.html(null)
        } else {
            $tablea.html(null)
            $tablea.append(setTable(dataAll));
            if ($tablea.height() > 170) {
                $tablea.parent().children().eq(0).css("width", "calc(25% - 17px)")
            }

            $char.setOption({
                title: [{
                    text: "合计：" + dataAll.TotCost + "万元"
                }, {
                    text: "费别",
                    x: 'center',
                    y: "bottom"
                }],
                color: setColor(dataAll.data),
                series: [{
                    data: dataAll.data
                }]
            })
        }
    }

    //绑定颜色
    function setColor(data) {
        var arr = []
        for (var i = 0; i < data.length; i++) {
            arr.push(colorObj[data[i].name])
        }
        return arr
    }

    //设置table
    function setTable(database) {
        var htmlDay = ""
        for (var i = 0; i < database.data.length; i++) {
            htmlDay += "<tr><td>" + database.data[i].name + "</td>" +
                "<td style = 'text-align:right'>" + changeCost(database.data[i].value) + "</td>" +
                "<td style = 'text-align:right'>" + (Number(database.TotCost) === 0 ? 0.00 :((Number(database.data[i].value) / Number(database.TotCost)) / 10000 * 100).toFixed(2)) + "%</td></tr>";
        }
        return htmlDay;
    }

    // 设置下面的char
    function setBottomchar(dataAll, $char, $dom, noDataName, $table, tablename) {
        if (dataAll.data.length == 0) {
            $($table).setLoading({
                id: tablename,
                text: "无数据",
                opacity: 1,
                backgroudColor: "#ffffff",
                isImg: false
            })
            $($dom).setLoading({
                id: noDataName,
                text: "无数据",
                opacity: 1,
                backgroudColor: "#ffffff",
                isImg: false
            })
        } else {
            $char.setOption({
                title: {
                    text: '院区',
                    x: 'center',
                    y: "75%"
                },
                color: setColor(dataAll.data),
                series: [{
                    center: ['50%', '40%'],
                    data: dataAll.data
                }]
            })
        }
    }

    //获取数据
    function ajaxAll(data, callback) {
        $.ajax({
            type: "get",
            url: "<%=basePath%>statistics/listTotalIncomeStatic/queryTotalCount.action",
            async: true,
            data: data,
            success: function (dataBase) {
                //回调函数
                callback(dataBase)
            }
        });
    }

    //日
    function picked1() {
        pickedFunc1(this.value);
    }
    //月
    function picked2() {
        pickedFunc2(this.value);
    }
    //年
    function picked3() {
        pickedFunc3(this.value);
    }

    //年
    function pickedFunc3(date) {
        $($Obj.year).rmoveLoading("yearLoadnodata")
            .setLoading({
                id: "yearLoad",
                text: "计算中..."
            })

        $($Obj.yearbottom).rmoveLoading("yearbottomLoadnodata")
            .setLoading({
                id: "yearbottomLoad",
                text: "计算中..."
            });
        $($table.yearTable).rmoveLoading("yearTableLoadnodata")
            .setLoading({
                id: "yearTableLoad",
                text: "计算中..."
            });

        ajaxAll({
            date: date,
            dateSign: 3
        }, function (dataBase) {
            $($Obj.year).rmoveLoading("yearLoad")
            $($Obj.yearbottom).rmoveLoading("yearbottomLoad")
            $($table.yearTable).rmoveLoading("yearTableLoad")

            setchar({
                data: dataBase.feeOfYear,
                TotCost: (dataBase.yearTotCost / 10000).toFixed(2)
            }, echartsObj.yearChar, $Obj.year, "yearLoadnodata", $("#yearDetai"))

            setBottomchar({
                data: dataBase.areaOfYear
            }, echartsObj.yearbottomChar, $Obj.yearbottom, "yearbottomLoadnodata", $table.yearTable, "yearTableLoadnodata")
        })
    }

    //月
    function pickedFunc2(date) {
        $($Obj.month).rmoveLoading("monthLoadnodata")
            .setLoading({
                id: "monthLoad",
                text: "计算中..."
            });
        $($Obj.monthbottom).rmoveLoading("monthbottomLoadnodata")
            .setLoading({
                id: "monthbottomLoad",
                text: "计算中..."
            });
        $($table.monthTable).rmoveLoading("monthTableLoadnodata")
            .setLoading({
                id: "monthTableLoad",
                text: "计算中..."
            });

        ajaxAll({
            date: date,
            dateSign: 2
        }, function (dataBase) {
            $($Obj.month).rmoveLoading("monthLoad")
            $($Obj.monthbottom).rmoveLoading("monthbottomLoad")
            $($table.monthTable).rmoveLoading("monthTableLoad")

            setchar({
                data: dataBase.feeOfMonth,
                TotCost: (dataBase.monthTotCost / 10000).toFixed(2)
            }, echartsObj.monthChar, $Obj.month, "monthLoadnodata", $("#monthDetai"))
            setBottomchar({
                data: dataBase.areaOfMonth,
            }, echartsObj.monthbottomChar, $Obj.monthbottom, "monthbottomLoadnodata", $table.monthTable, "monthTableLoadnodata")
        })
    }

    //日
    function pickedFunc1(date) {
        $($Obj.day).rmoveLoading("dayLoadnodata")
            .setLoading({
                id: "dayLoad",
                text: "计算中..."
            });
        $($Obj.daybottom).rmoveLoading("daybottomLoadnodata")
            .setLoading({
                id: "daybottomLoad",
                text: "计算中..."
            });

        $($table.dayTable).rmoveLoading("dayTableLoadnodata")
            .setLoading({
                id: "dayTableLoad",
                text: "计算中..."
            });
        ajaxAll({
            date: date,
            dateSign: 1
        }, function (dataBase) {
            $($Obj.day).rmoveLoading("dayLoad")
            $($Obj.daybottom).rmoveLoading("daybottomLoad")
            $($Obj.dayTable).rmoveLoading("dayTableLoad")

            setchar({
                data: dataBase.feeOfDay,
                TotCost: (dataBase.dayTotCost / 10000).toFixed(2)
            }, echartsObj.dayChar, $Obj.day, "dayLoadnodata", $("#dayDetai"))
            setBottomchar({
                data: dataBase.areaOfDay,
            }, echartsObj.daybottomChar, $Obj.daybottom, "daybottomLoadnodata", $table.dayTable, "dayTableLoadnodata")
        })
    }

    //自定义
    function pickedFuncBySelf() {
        var st = $("#timeSelfS").val();
        var end = $("#timeSelfE").val();
        if (st && end) {
            if (st > end) {
                $.messager.alert("提示", "开始时间不能大于结束时间！");
                close_alert();
                $($Obj.diy).setLoading({
                    id: "diyLoadnodata",
                    text: "无数据",
                    opacity: 1,
                    backgroudColor: "#ffffff",
                    isImg: false
                })
                $($Obj.diybottom).setLoading({
                    id: "diybottomLoadnodata",
                    text: "无数据",
                    opacity: 1,
                    backgroudColor: "#ffffff",
                    isImg: false
                })
                $($table.diyTable).setLoading({
                    id: "diyTableLoadnodata",
                    text: "无数据",
                    opacity: 1,
                    backgroudColor: "#ffffff",
                    isImg: false
                })
                return;
            }
        }
        $($Obj.diy).rmoveLoading("diyLoadnodata")
            .setLoading({
                id: "diyLoad",
                text: "计算中..."
            });
        $($Obj.diybottom).rmoveLoading("diybottomLoadnodata")
            .setLoading({
                id: "diybottomLoad",
                text: "计算中..."
            });
        $($table.diyTable).rmoveLoading("diyTableLoadnodata")
            .setLoading({
                id: "diyTableLoad",
                text: "计算中..."
            });

        ajaxAll({
            date: st + "," + end,
            dateSign: 4
        }, function (dataBase) {
            $($Obj.diy).rmoveLoading("diyLoad")
            $($Obj.diybottom).rmoveLoading("diybottomLoad")
            $($Obj.diyTable).rmoveLoading("diyTableLoad")
            setchar({
                data: dataBase.feeOfCustom,
                TotCost: (dataBase.customTotCost / 10000).toFixed(2)
            }, echartsObj.diyChar, $Obj.diy, "diyLoadnodata", $("#diyDetai"))
            setBottomchar({
                data: dataBase.areaOfCustom,
            }, echartsObj.diybottomChar, $Obj.diybottom, "diybottomLoadnodata", $table.diyTable, "diyTableLoadnodata")

        })
    }
</script>
</html>