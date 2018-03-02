<%--
  DESC:
  User: zhangkui
  Date: 2018/1/5
  Time: 14:27
--%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <%@ include file="/common/metas.jsp"%>
    <title>日志列表</title>
</head>
<body>
<div class="easyui-layout" data-options="fit:true" >
            <div data-options="region:'north'" style="height:42px;width:100%;border-top:0;border-left:0;border-right:0;">
                <table style="width:100%;padding: 0px 5px 0px 5px;height: 40px;">
                    <tr>
                        <td nowrap="nowrap">日期:
                            <input id="startTime" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')}'})" style="height:23px;width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>&nbsp;至
                            <input id="endTime" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'})" style="height:23px;width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>&nbsp;
                            <a href="javascript:void(0)"  onclick="searchList()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
                            <a href="javascript:void(0)"  onclick="addLog()" class="easyui-linkbutton" data-options="iconCls:'icon-add'"  >添加删除</a>
                        </td>
                    </tr>
                </table>
            </div>

            <div data-options="region:'center',border: false" style="width:100%;">
                <table id="list" class="easyui-datagrid">
                    <thead>
                    <tr>
                        <th data-options="field:'id',hidden:true,align:'left'" ></th>
                        <th data-options="field:'type',align:'left',formatter: function(value,row,index){
                                                                                    if (value=='1'){
                                                                                        return '流程';
                                                                                    } else if(value=='2'){
                                                                                        return '员工';
                                                                                    }else if(value=='3'){
                                                                                        return '科室'
                                                                                    }
                                                                                  }
                 "  style="width: 80px;" >操作类型</th>
                        <th data-options="field:'t_value',align:'left',formatter:formatterIsu"  style="width: 150px;" >类型名称</th>
                        <th data-options="field:'num',align:'left'"  style="width: 80px;">删除数量</th>
                        <th data-options="field:'deptName',align:'left'"  style="width: 80px;">创建科室</th>
                        <th data-options="field:'name',align:'left'"  style="width: 80px;" >创建人</th>
                        <th data-options="field:'createTime',align:'left'"  style="width: 80px;" >创建时间</th>
                    </tr>
                    </thead>
                </table>
            </div>
    <div id="win"></div>
</div>

</body>
</html>
<script type="text/javascript">

    $(function(){
        $("#list").datagrid({
            method: 'post',
            url:"${pageContext.request.contextPath}/oa/log/findTaskDelVoList.action",
            fit: true,
            remoteSort: false,
            pagination: true,
            pageSize: 20,
            idField:'id',
            fitColumns:true,
            rownumbers:true,
            singleSelect:true,
            pageList: [20, 30, 50, 100],
            onLoadSuccess: function(data) {
                //分页工具栏作用提示
                var pager = $(this).datagrid('getPager');
                var aArr = $(pager).find('a');
                var iArr = $(pager).find('input');
                $(iArr[0]).tooltip({
                    content: '回车跳转',
                    showEvent: 'focus',
                    hideEvent: 'blur',
                    hideDelay: 1
                });
                for(var i = 0; i < aArr.length; i++) {
                    $(aArr[i]).tooltip({
                        content: toolArr[i],
                        hideDelay: 1
                    });
                    $(aArr[i]).tooltip('hide');
                }
            }

        });
    })


    function searchList(){

        var startTime=$("#startTime").val();
        var endTime=$("#endTime").val();
        $('#list').datagrid('load', {
            startTime: startTime,
            endTime:endTime
        });

    }

    function addLog(){

        var bodyWidth =  $("body").width()
        var bodyHeight = $("body").height()
        var height = bodyHeight*0.9;
        var width = bodyWidth*0.5;
        var top = Math.round((window.screen.height - height) / 2)-135;
        var left = Math.round((window.screen.width - width) / 2);

        $('#win').window({
            width : width,
            height : height,
            top:top,
            left:left,
            title : " ",
            modal : true,
            collapsible:false,
            minimizable : false,
            maximizable : false,
            content : "<iframe src='${pageContext.request.contextPath}/oa/log/toChooseUI.action' height='100%' width='100%' frameborder='0px' ></iframe>"
        });
    }


    function formatterIsu(value, row, index){
        if(value==null||value==undefined||value==""){
            return "";
        }else{
            return "<span title='" + value + "'>" + value + "</span>";
        }
    }




</script>