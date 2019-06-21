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
    <div id="page-content-wrapper" >
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column" style="overflow-x:auto; " >
                    <table class="table table-bordered table-condensed">
                        <thead>
                        <tr>
                            <th>批量包序号</th>
                            <th>客户发起文件名</th>
                            <th>发起文件名</th>
                            <th>发起日期</th>
                            <th>回应文件名</th>
                            <th>回应日期</th>
                            <th>总笔数</th>
                            <th>总金额</th>
                            <th>成功笔数</th>
                            <th>成功金额</th>
                            <th>失败笔数</th>
                            <th>失败金额</th>
                            <th>状态</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list pldsVoPage.content as pldsVo>
                            <tr>
                                <td>${pldsVo.msgId}</td>
                                <td>${pldsVo.batchId}</td>
                                <td>${pldsVo.sendfileName}</td>
                                <td>${pldsVo.createTime}</td>
                                <td>${pldsVo.rspfileName!""}</td>
                                <td>${pldsVo.rspTime!""}</td>
                                <td>${pldsVo.totCnt}</td>
                                <td>${pldsVo.totAmt}</td>
                                <td>${pldsVo.sucCnt!""}</td>
                                <td>${pldsVo.sucAmt!""}</td>
                                <td>${pldsVo.failCnt!""}</td>
                                <td>${pldsVo.failAmt!""}</td>
                                <td>${pldsVo.getJYStatus()!""}</td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                    <ul class="pagination pull-right">
                        <#if currentpage lte 1>
                            <li class="disabled"><a href="#">上一页</a></li>
                        <#else>
                            <li>
                                <a href="/lmhr/plds/list?page=${currentpage-1}&size=${size}">上一页</a>
                            </li>
                        </#if>
                        <#if pldsVoPage?? && pldsVoPage.getTotalPages()==0>
                            <li></li>
                        <#else>
                            <#list 1..pldsVoPage.getTotalPages() as index>
                                <#if currentpage==index>
                                    <li class="active">
                                <#else>
                                    <li>
                                </#if>
                                <a href="/lmhr/plds/list?page=${index}&size=${size}">
                                    ${index}
                                </a>
                                </li>
                            </#list>
                        </#if>
                        <#if currentpage gte pldsVoPage.getTotalPages()>
                            <li class="disabled"><a href="#">下一页</a></li>
                        <#else>
                            <li>
                                <a href="/lmhr/plds/list?page=${currentpage+1}&size=${size}">下一页</a>
                            </li>
                        </#if>
                    </ul>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>