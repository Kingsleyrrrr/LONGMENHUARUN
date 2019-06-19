<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>批量代收列表</title>
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
                            <th>交易序号</th>
                            <th>委托日期</th>
                            <th>清算日期</th>
                            <th>业务种类</th>
                            <th>用户号</th>
                            <th>付款行号</th>
                            <th>分支行号</th>
                            <th>付款人账号</th>
                            <th>付款户名</th>
                            <th>币种</th>
                            <th>金额</th>
                            <th>手续费</th>
                            <th>附言</th>
                            <th>流水号</th>
                            <th>返回码</th>
                            <th>返回信息</th>
                            <th>交易状态</th>
                            <th>到账状态</th>
                            <th>到账时间</th>
                            <th>创建时间</th>
                        </tr>
                        </thead>
                        <tbody>


                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>