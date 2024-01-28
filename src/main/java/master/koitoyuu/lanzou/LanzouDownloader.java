package master.koitoyuu.lanzou;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import master.koitoyuu.lanzou.utils.HTTPUtils;
import master.koitoyuu.lanzou.utils.LanzouException;
import master.koitoyuu.lanzou.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Koitoyuu
 */
public class LanzouDownloader {
    /**
     * 获取蓝奏云文件直链
     *
     * @param url 蓝奏云文件链接
     * @param password 蓝奏云文件密码
     * @return 蓝奏云下载直链
     */
    public static String getDownloadURL(String url,String password) throws Exception {
        String urlData = HTTPUtils.doGet(url);
        if (!urlData.isEmpty()) {
            String downloadScript = StringUtils.getSubString(StringUtils.replaceBlank(urlData), "functiondown_p(){", "});}");
            String sign = StringUtils.getSubString(downloadScript, "skdklds='", "';");
            String nextURL = "https://" + StringUtils.getSubString(url, "https://", "/") + StringUtils.getSubString(downloadScript, "url:'", "',");
            String params = "action=downprocess&sign=" + sign + "&p=" + password;
            String nextURLData = HTTPUtils.doPost(nextURL,params,url);
            JsonObject jsonObject = JsonParser.parseString(nextURLData).getAsJsonObject();
            if (jsonObject.get("zt").getAsInt() == 1)
                return jsonObject.get("dom").getAsString() + "/file/" + jsonObject.get("url").getAsString();
            throw new LanzouException(jsonObject.get("inf").getAsString());
        }
        throw new NullPointerException();
    }

    /**
     * 根据输入的蓝奏云文件链接和密码生成下载链接的映射表。
     * 对输入的映射表中的每个URL，根据对应的密码生成下载链接，并将结果存储在新的映射表中返回。
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
}
