<!DOCTYPE html>
<html xmlns="">
<head>
    <meta charset="UTF-8">
    <title>实时协议（机构仅用代收协议）</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
    <script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="/css/style.css"  />
</head>
<script>
$(document).ready(function (){//通用方法去输入框前后空格
$("form").on("change",function () {
$("form input[type=text]").each(function () {
this.value=$.trim(this.value);
});
})
})
</script>
<body>
<div id="wrapper" class="toggled">
    <#--边栏sidebar-->
    <#include "../nav.ftl"/>
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="page-header">
                <h1>
                    新增实时协议
                </h1>
            </div>
            <form class="form-horizontal" method="post" action="/lmhr/ssxy/sendSsxy">
                <div class="form-group">
                    <label class="col-sm-4 control-label " >用户编号</label>
                    <div class="col-sm-3">
                        <input name="userNo" class="form-control" type="text" value="${(ssxyMsg.userNo)!" "}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label " >用户名称</label>
                    <div class="col-sm-3">
                        <input name="userName" class="form-control" type="text" value="${(ssxyMsg.userName)!" "}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label " >联系人电话</label>
                    <div class="col-sm-3">
                        <input name="contactNo" class="form-control" type="text" value="${(ssxyMsg.contactNo)!" "}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label  class="col-sm-4 control-label ">付款行行号</label>
                    <div class="col-sm-3">
                        <input name="payerBank" class="form-control" type="text" value="${(ssxyMsg.payerBank)!""}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label  class="col-sm-4 control-label">付款人账号</label>
                    <div class="col-sm-3">
                        <input name="payerAcc" class="form-control" type="text" value="${(ssxyMsg.payerAcc)!""}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label  class="col-sm-4 control-label">付款人户名</label>
                    <div class="col-sm-3">
                        <input name="payerName" class="form-control" type="text" value="${(ssxyMsg.payerName)!""}"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-4 col-sm-10">
                        <button type="submit" class=" btn btn-success">发送请求</button>  <span style="color:#ff0000">${Msg!""}</span>
                    </div>
                </div>
            </form>
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
                        <button onclick='location.href="/lmhr/ssxy/list"' type="button" class="btn btn-default" data-dismiss="modal">查看</button>

                    </div>
                </div>
            </div>
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