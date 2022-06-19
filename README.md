# idea-convert-request-header-plugin
idea插件，转换请求头，将键值对转换成Java的Map代码

例如：
```txt
Host: www.jianshu.com
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:101.0) Gecko/20100101 Firefox/101.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8
//Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2
//Accept-Encoding: gzip, deflate, br
//Referer: https://www.xxx.com/u/3adf83c26b4e
//Connection: keep-alive
//Cookie: sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22180ccdd41075c-0447aa648351a9-c555420-1218816-180ccdd41081655617063
//Upgrade-Insecure-Requests: 1
//Sec-Fetch-Dest: document
//Sec-Fetch-Mode: navigate
//Sec-Fetch-Site: same-origin
//Sec-Fetch-User: ?1
Pragma: no-cache
Cache-Control: no-cache
```
转换成
```java
Map<String, String> headers = new HashMap<String, String>();
headers.put("Host", "www.xxx.com");
headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:101.0) Gecko/20100101 Firefox/101.0");
headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8");
headers.put("Pragma", "no-cache");
headers.put("Cache-Control", "no-cache");
```


> 注：如果以 `//` 开头表示注释掉该行，该行不生效。