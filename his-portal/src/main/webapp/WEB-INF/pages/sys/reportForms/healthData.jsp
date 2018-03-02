<%@ page contentType="text/html; UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
</head>
<body>
<div id="outLayout" class="easyui-layout" data-options="fit:true">
    <div data-options="region:'west',title:'病种分类',collapsible:false" style="width: 20%;padding-left:10px">
        <div id="inTreeLayout" class="easyui-layout" data-options="fit:true">
<%--            <div data-options="region:'north',border:false" style="height:40px">
                <table style="padding:7px 10px 5px 10px">
                    <tr>
                        <td nowrap="nowrap">
                            &lt;%&ndash;<label for="icdCode"></label>&ndash;%&gt;
                            <input id="icdAssort" data-options="prompt:'病种分类名称'" class="easyui-textbox"
                                   style="width: 200px"/>
                            <a href="javascript:void(0)" onclick="searchIcdAssort()" class="easyui-linkbutton"
                               iconCls="icon-search">查询</a>
                        </td>
                    </tr>
                </table>
            </div>--%>
            <div data-options="region:'center',border:false" style="height: 80%">
                <ul id="icdAssortTree"></ul>
            </div>
        </div>
    </div>
    <div data-options="region:'center'" style="width:80%">
        <div id="inLayout" class="easyui-layout" data-options="fit:true">
            <div data-options="region:'north',title:'健康数据统计',collapsible:false" style="height:70px">
                <table style="padding: 7px 10px 5px 10px">
                    <tr>
                        <td nowrap="nowrap">
                            <%--<label for="icdCode"></label>--%>
                            <input id="icd" data-options="prompt:'诊断码或诊断名称'" class="easyui-textbox"
                                   style="width: 200px"/>
                            <a href="javascript:void(0)" onclick="searchIcd()" class="easyui-linkbutton"
                               iconCls="icon-search">查询</a>
                        </td>
                    </tr>
                </table>
            </div>
            <div data-options="region:'center'" style="height: 50%">
                <table id="grid" class="easyui-datagrid"
                       data-options="fit:true,rownumbers:true,singleSelect:true,onLoadSuccess: onLoadSuccess,
                        pagination:true,pageSize:15,pageList:[15,30,50,100],showFooter:true,queryParams: {icdAssortId: 'root'},
                        url:' ${pageContext.request.contextPath}/disease/health/queryHealthData.action',method:'post',border:false">
                    <thead>
                    <tr>
                        <th data-options="field:'icd10Code',width:100,align:'left',rowspan:2">诊断码</th>
                        <th data-options="field:'name',width:350,align:'left',rowspan:2">诊断名称</th>
                        <th data-options="field:'totalCount',width:80,align:'right',rowspan:2">患者例数</th>
                        <th data-options="field:'age',width:140,align:'center',colspan:3">性别分布</th>
                        <th data-options="field:'gender',width:140,align:'center',colspan:10">年龄分布</th>
                        <th data-options="field:'district',width:140,align:'center',colspan:3">区域分布</th>
                        <%--				<th data-options="field:'unDrugCost',width:140,align:'right',rowspan:2">诊疗费用(元)</th>
                                        <th data-options="field:'drugCost',width:140,align:'right',rowspan:2">药品费用(元)</th>
                                        <th data-options="field:'operationCost',width:140,align:'right',rowspan:2">手术费用(元)</th>--%>
                    </tr>
                    <tr>
                        <th data-options="field:'male',width:45,align:'right'">男</th>
                        <th data-options="field:'female',width:45,align:'right'">女</th>
                        <th data-options="field:'unknownGender',align:'right'">未记录</th>
                        <th data-options="field:'gte0lte7',align:'right'">0-7岁</th>
                        <th data-options="field:'gte8lte13',align:'right'">8-13岁</th>
                        <th data-options="field:'gte14lte20',align:'right'">14-20岁</th>
                        <th data-options="field:'gte21lte35',align:'right'">21-35岁</th>
                        <th data-options="field:'gte36lte45',align:'right'">36-45岁</th>
                        <th data-options="field:'gte46lte55',align:'right'">46-55岁</th>
                        <th data-options="field:'gte56lte65',align:'right'">56-65岁</th>
                        <th data-options="field:'gte66lte75',align:'right'">66-75岁</th>
                        <th data-options="field:'gte76',align:'right'">76岁以上</th>
                        <th data-options="field:'unknownAge',align:'right'">未记录</th>
                        <th data-options="field:'city',align:'right'">城市</th>
                        <th data-options="field:'village',align:'right'">农村</th>
                        <th data-options="field:'unknownDistrict',align:'right'">未记录</th>
                    </tr>
                    </thead>
                </table>
            </div>
            <div data-options="region:'south',collapsible:false" id="main" style="height:40%"></div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">
    var $icdAssortTree = $("#icdAssortTree");
    var $grid = $("#grid");
    var $icdAssort = $("#icdAssort");
    var initTreeParams = {id: 'root'};
    $icdAssortTree.tree({
        url: '${pageContext.request.contextPath}/icdAssort/icdTree.action',
        method: 'post',
        queryParams: initTreeParams,
        onClick: function (node) {
            if ($icdAssortTree.tree("isLeaf", node.target)) {
                var icdCode = node.id;
            } else {
                var icdAssortId = node.id;
            }
            $grid.datagrid('load', {
                icdAssortId: icdAssortId,
                icdCode: icdCode
            });
        }
    });

    function searchIcdAssort() {
        var icdAssortValue = $icdAssort.val()
        if (icdAssortValue) {
            $.post("${pageContext.request.contextPath}/icdAssort/icdTree.action", {assortName: icdAssortValue},
                function(data){
                    $icdAssortTree.tree("loadData", data);
                });
        } else {
            $icdAssortTree.tree({
                queryParams: initTreeParams,
            })

        }
    }

    function onLoadSuccess(data) {
        console.log(data);
        if (data && data.rows) {
            var barDate = [];
            var xAxisDate = [];

            for (var i = 0; i < data.rows.length; i++) {
                var barObject = {};
                barObject.name = data.rows[i].name;
                barObject.value = data.rows[i].totalCount;
                barDate.push(barObject);
                xAxisDate.push(data.rows[i].name);
            }
            loadBar(xAxisDate, barDate);
        }

    }

    function searchIcd() {
        var $icd = $("#icd");
        $grid.datagrid('load', {
            where: $icd.val()
        });
    }

    function loadBar(xAxisDate, barDate) {
        require.config({
            paths: {
                echarts: '${pageContext.request.contextPath}/javascript/echarts'
            }
        });
        require(
            [
                'echarts',
                'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('main'));

                var option = {
                    tooltip: {
                        show: true
                    },
                    // legend: {
                    //     data: ['疾病分类']
                    // },
                    toolbox: {
                        show: true,
                        feature: {
                            mark: {show: true},
                            dataView: {show: true, readOnly: false},
                            //  magicType : {show: true, type: ['line', 'bar']},
                            restore: {show: true},
                            saveAsImage: {show: true}
                        }
                    },
                    calculable: true,
                    xAxis: [
                        {
                            type: 'category',
                            data: xAxisDate
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    dataZoom: {
                        show: true,
                        handleSize: 15,
                        realtime: true,
                        zoomLock: true,
                        start: 0,
                        end: 800 / xAxisDate.length,
                        fillerColor: 'rgba(0,102,153,0.2)'
                    },
                    series: [
                        {
                            name: "诊断名称",
                            type: "bar",
                            barWidth: 50,
                            data: barDate
                        }
                    ]
                };
                myChart.setOption(option);
            }
        );
    }
</script>
</html>
