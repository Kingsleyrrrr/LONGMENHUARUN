<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>协议列表</title>
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
                    <form class="form-inline"  method="post" action="/lmhr/ssxy/list">
                        <div class="form-group">
                            <label class="control-label" for="name">用户编号</label>
                            <input type="text" class="form-control" type="text"
                                   name="userNo">
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="name">用户名称</label>
                            <input type="text" class="form-control" type="text"
                                   name="userName">
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="name">银行代码</label>
                            <input type="text" class="form-control" type="text"
                                   name="payerBank" >
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="name">银行账号</label>
                            <input type="text" class="form-control" type="text"
                                   name="payerAcc">
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="name">银行户名</label>
                            <input type="text" class="form-control" type="text"
                                   name="payerName">
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="name">协议号</label>
                            <input type="text" class="form-control" type="text"
                                   name="protNo" >
                        </div>&nbsp;&nbsp;&nbsp;
                        <button type="submit" class="btn btn-primary">查询</button>
                    </form>
                    <br><br>
                    <table class="table table-bordered table-condensed">
                        <thead>
                        <tr>
                            <th>协议号</th>
                            <th>业务种类</th>
                            <th>用户编号</th>
                            <th>用户名称</th>
                            <th>付款行行号</th>
                            <th>付款人账号</th>
                            <th>付款人名称</th>
                            <th>附言</th>
                            <th>生效日期</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list ssxyVoPage.content as ssxyVo>
                            <tr>
                                <td>${ssxyVo.protNo}</td>
                                <td>${ssxyVo.getTypeMsg()}</td>
                                <td>${ssxyVo.userNo}</td>
                                <td>${ssxyVo.userName}</td>
                                <td>${ssxyVo.payerBank}</td>
                                <td>${ssxyVo.payerAcc}</td>
                                <td>${ssxyVo.payerName}</td>
                                <td>${ssxyVo.remark!""}</td>
                                <td>${ssxyVo.activeDate}</td>
                                <td><a  href="/lmhr/ssxy/cancel?protNo=${ssxyVo.protNo}">撤销</a></td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <#if Msg??>
            <script>
                if (confirm("${Msg}"))
                {
                    location.reload();
                }
            </script>
        </#if>
    </div>
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
                    <button  onclick='location.href="/lmhr/ssxy/list"' type="button" class="btn btn-default" data-dismiss="modal">查看</button>

                </div>
            </div>
        </div>
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
</div>
</body>
<script>
    var websocket=null;
    if('WebSocket'in window){
        websocket =new WebSocket('ws://localhost:8080/webSocket');
    }else{
        alert('该浏览器不支持Websocket');
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