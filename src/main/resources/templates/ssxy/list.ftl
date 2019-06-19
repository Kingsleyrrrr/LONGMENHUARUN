<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>实时列表</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
    <script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="/css/style.css"  />
</head>
<body>
<div id="wrapper" class="toggled">

    <#--边栏sidebar-->
    <#include "../nav.ftl">

    <#--主要内容content-->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <table class="table table-bordered table-condensed">
                        <thead>
                        <tr>
                            <th>协议号</th>
                            <th>业务种类</th>
                            <th>用户编号</th>
                            <th>付款行行号</th>
                            <th>付款人账号</th>
                            <th>付款人名称</th>
                            <th>附言</th>
                            <th>返回码</th>
                            <th>返回信息</th>
                            <th>业务流水号</th>
                            <th>状态</th>
                            <th>提交日期</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list ssxyVoPage.content as ssxyVo>
                            <tr>
                                <td>${ssxyVo.protNo}</td>
                                <td>${ssxyVo.getTypeMsg()}</td>
                                <td>${ssxyVo.userNo}</td>
                                <td>${ssxyVo.payerBank}</td>
                                <td>${ssxyVo.payerAcc}</td>
                                <td>${ssxyVo.payerName}</td>
                                <td>${ssxyVo.remark!""}</td>
                                <td>${ssxyVo.retCd!""}</td>
                                <td>${ssxyVo.retCdRemark!""}</td>
                                <td>${ssxyVo.workReturnNo}</td>
                                <td>${ssxyVo.getXYStatus()!""}</td>
                                <td>${ssxyVo.createTime}</td>
                                <#if ssxyVo.status =="E" >
                                <td><a  href="/lmhr/ssxy/cancel?msgId=${ssxyVo.msgId}">撤销</a></td>
                                <#else>
                                <td></td>
                                </#if>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <#if Msg??>
            <script>
                alert("${Msg}");//弹出
            </script>
        </#if>
    </div>
    <ul class="pagination pull-right">
        <#if currentpage lte 1>
            <li class="disabled"><a href="#">上一页</a></li>
        <#else>
            <li>
                <a href="/lmhr/ssxy/list?page=${currentpage-1}&size=${size}">上一页</a>
            </li>
        </#if>
        <#if ssxyVoPage?? && ssxyVoPage.getTotalPages()==0>
            <li></li>
        <#else>
        <#list 1..ssxyVoPage.getTotalPages() as index >
            <#if currentpage==index>
                <li class="active">
            <#else>
                <li>
            </#if>
            <a href="/lmhr/ssxy/list?page=${index}&size=${size}">
                ${index}
            </a>
            </li>

        </#list>
        </#if>
        <#if currentpage gte ssxyVoPage.getTotalPages()>
            <li class="disabled"><a href="#">下一页</a></li>
        <#else>
            <li>
                <a href="/lmhr/ssxy/list?page=${currentpage+1}&size=${size}">下一页</a>
            </li>
        </#if>
    </ul>
    <div class="modal fade" id="notice" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" id="myModalLabel">
                        提醒
                    </h4>
                </div>
                <div id="noticebody" class="modal-body">

                </div>
                <div class="modal-footer">
                    <button  onclick='location.href="/lmhr/ssds/list"' type="button" class="btn btn-default" data-dismiss="modal">查看</button>

                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    var websocket=null;
    if('WebSocket'in window){
        websocket =new WebSocket('ws://127.0.0.1:8888/webSocket');
    }else{
        alert('该浏览器不支持');
    }
    websocket.onopen=function (event) {
        console.log("建立连接");
    }
    websocket.onclose=function (event) {
        console.log("连接关闭");
    }
    websocket.onmessage=function (event) {
        //弹窗
        var body = document.getElementById("noticebody");
        body.innerText="请求"+event.data+"已有回应，请前往查看";
        $("#notice").modal("show");
    }
    websocket.onerror=function (event) {
        console.log("通信错误");
    }
    websocket.onbeforeunload=function (event) {
        websocket.close();
    }
</script>
</html>