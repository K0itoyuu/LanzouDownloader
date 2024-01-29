# LanzouDownloader (蓝奏云直链解析)

LanzouDownloader 是一个用于解析蓝奏云直链的 Java 工具，可以方便地将蓝奏云分享的文件直链转换为可直接下载的链接。

## 快速开始

### 使用示例

#### 不作为依赖
例子: `java -jar LanzouDownloader.jar URL Password`


#### 作为依赖
获取文件下载链接例子:
~~~java
LanzouDownloader.getDownloadURL(URL, Password);
~~~

获取蓝奏云账号下所有文件信息例子:

###### Cookie需自行获取
~~~java
LanzouDownloader.getFiles(Cookie);
~~~