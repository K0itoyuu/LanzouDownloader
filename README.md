# LanzouDownloader (蓝奏云直链解析)

LanzouDownloader 是一个用于解析蓝奏云直链的 Java 工具，可以方便地将蓝奏云分享的文件直链转换为可直接下载的链接。

## 快速开始

### 使用示例

```java
public static void main(String[] args) {
    LanzouDownloader.getDownloadURL(URL, PASSWORD);
}
