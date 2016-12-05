离线的整体思路
1.系统缓存 app cache
将每个界面都有用到的jquery缓存
1.1<html lang="en" manifest="offline.manifest.php">
1.2offline.manifest.php
<?php
header("Content-Type: text/cache-manifest");
?>
CACHE MANIFEST
# 2012-12-09 v6
jquery.min.js

NETWORK:
*

2.业务缓存 localStorage
将业务用到的js和css以json格式存放到localStorage中，
浏览器检测出在线时，下载js,css的内容存放到localStrage中，并插入并执行js，css
离线时，从localStorage中取出js和css的内容，并插入执行

3.业务数据web sql
访问时，插入本地sql中
INSERT INTO articles