<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>批量代收</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
    <script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="/css/style.css"  />
</head>
<body>
<div id="wrapper" class="toggled">
    <#--边栏sidebar-->
    <#include "../nav.ftl"/>
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="page-header">
                <h1>
                    发起批量代收
                </h1>
            </div>
        </div>
    </div>
    <form method="post" action="/lmhr/plds/sendFile" enctype="multipart/form-data">
        <div class="form-group">
            <label for="exampleInputFile">上传txt文件</label><input type="file" name="file" />
        </div>
        <br/><br/>
        <input type="submit" value="解析并发送" /><span style="color:#ff0000">${Msg!""}</span>
</div>
</body>
</html>