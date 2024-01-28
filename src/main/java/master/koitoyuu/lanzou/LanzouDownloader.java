package master.koitoyuu.lanzou;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import master.koitoyuu.lanzou.utils.HTTPUtils;
import master.koitoyuu.lanzou.utils.LanzouException;
import master.koitoyuu.lanzou.utils.StringUtils;

/**
 * @author Koitoyuu
 */
public class LanzouDownloader {
    /**
     *
     * @param url 蓝奏云文件链接
     * @param password 蓝奏云文件密码
     * @return 蓝奏云下载直链
     */
    public static String getDownloadURL(String url,String password) {
        try {
            String urlData = HTTPUtils.doGet(url);
            if (!urlData.isEmpty()) {
                String downloadScript = StringUtils.getSubString(StringUtils.replaceBlank(urlData), "functiondown_p(){", "});}");
                String sign = StringUtils.getSubString(downloadScript, "skdklds='", "';");
                String nextURL = "https://" + StringUtils.getSubString(url, "https://", "/") + StringUtils.getSubString(downloadScript, "url:'", "',");
                String params = "action=downprocess&sign=" + sign + "&p=" + password;
                String nextURLData = HTTPUtils.doPost(nextURL,params,url);
                JsonObject jsonObject = JsonParser.parseString(nextURLData).getAsJsonObject();
                System.out.println(nextURLData);
                if (jsonObject.get("zt").getAsInt() == 1)
                    return jsonObject.get("dom").getAsString() + "/file/" + jsonObject.get("url").getAsString();
                throw new LanzouException(jsonObject.get("inf").getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
