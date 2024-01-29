package master.koitoyuu.lanzou;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import master.koitoyuu.lanzou.utils.HTTPUtils;
import master.koitoyuu.lanzou.utils.LanzouException;
import master.koitoyuu.lanzou.utils.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * LanzouDownloader类提供了对蓝奏云文件的相关操作
 * 包括获取文件直链和生成下载链接映射表
 * 作者：Koitoyuu
 */
public class LanzouDownloader {
    /**
     * 获取蓝奏云文件直链
     *
     * @param url 蓝奏云文件链接
     * @param password 蓝奏云文件密码
     * @return 蓝奏云下载直链
     * @throws Exception 网络请求或解析异常
     */
    public static String getDownloadURL(String url, String password) throws Exception {
        JsonObject fileInfo = getFileInfo(url, password);
        return fileInfo.get("DownloadURL").getAsString();
    }

    /**
     * 根据输入的蓝奏云文件链接和密码生成下载链接的映射表。
     *
     * @param map 包含蓝奏云文件链接和密码的映射表
     * @return 包含蓝奏云文件链接和对应下载链接的映射表
     */
    public static Map<String, String> getDownloadURL(Map<String, String> map) {
        Map<String, String> downloadMap = new HashMap<>();

        map.forEach((url, password) -> {
            try {
                String downloadURL = getDownloadURL(url, password);
                downloadMap.put(url, downloadURL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return downloadMap;
    }

    /**
     * 获取文件信息
     *
     * @param url 蓝奏云文件链接
     * @param password 文件密码
     * @return 包含文件信息的JsonObject
     * @throws LanzouException 解析异常
     * @throws IOException 网络异常
     * @throws NullPointerException 网路异常
     */
    public static JsonObject getFileInfo(String url, String password) throws LanzouException, IOException, NullPointerException {
        String urlData = HTTPUtils.doGet(url);
        if (!urlData.isEmpty()) {
            String downloadScript = StringUtils.getSubString(StringUtils.replaceBlank(urlData), "functiondown_p(){", "});}");
            String sign = StringUtils.getSubString(downloadScript, "skdklds='", "';");
            String nextURL = "https://" + StringUtils.getSubString(url, "https://", "/") + StringUtils.getSubString(downloadScript, "url:'", "',");
            String params = "action=downprocess&sign=" + sign + "&p=" + password;
            String nextURLData = HTTPUtils.doPost(nextURL, params, url);
            JsonObject jsonObject = JsonParser.parseString(nextURLData).getAsJsonObject();
            if (jsonObject.get("zt").getAsInt() == 1) {
                JsonObject object = new JsonObject();
                String downloadURL = jsonObject.get("dom").getAsString() + "/file/" + jsonObject.get("url").getAsString();
                String fileName = jsonObject.get("inf").getAsString();
                String size = StringUtils.getSubString(urlData, "<meta name=\"description\" content=\"文件大小：", "|\" />").replace(" ", "");
                String uploader = StringUtils.getSubString(urlData,"<span class=\"user-name\">","</span><span class=\"user-name-txt\">");
                object.addProperty("DownloadURL", downloadURL);
                object.addProperty("FileName", fileName);
                object.addProperty("Size", size);
                object.addProperty("Uploader",uploader);
                return object;
            }
            throw new LanzouException(jsonObject.get("inf").getAsString());
        }

        throw new NullPointerException();
    }
}
