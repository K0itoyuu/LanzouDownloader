package master.koitoyuu.lanzou;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import master.koitoyuu.lanzou.utils.HTTPUtils;
import master.koitoyuu.lanzou.utils.LanzouException;
import master.koitoyuu.lanzou.utils.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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
        Map<String,String> fileInfo = getFileInfo(url, password);
        return fileInfo.get("DownloadURL");
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
    public static Map<String,String> getFileInfo(String url, String password) throws LanzouException, IOException, NullPointerException {
        String urlData = HTTPUtils.doGet(url,"codelen=1; pc_ad1=1");
        if (!urlData.isEmpty()) {
            String downloadScript = StringUtils.getSubString(StringUtils.replaceBlank(urlData), "functiondown_p(){", "});}");
            String sign = StringUtils.getSubString(downloadScript, "skdklds='", "';");
            String nextURL = "https://" + StringUtils.getSubString(url, "https://", "/") + StringUtils.getSubString(downloadScript, "url:'", "',");
            String params = "action=downprocess&sign=" + sign + "&p=" + password;
            String nextURLData = HTTPUtils.doPost(nextURL, params, url,"codelen=1; pc_ad1=1");
            JsonObject jsonObject = JsonParser.parseString(nextURLData).getAsJsonObject();
            if (jsonObject.get("zt").getAsInt() == 1) {
                Map<String,String> map = new HashMap<>();
                String downloadURL = jsonObject.get("dom").getAsString() + "/file/" + jsonObject.get("url").getAsString();
                String fileName = jsonObject.get("inf").getAsString();
                String size = StringUtils.getSubString(urlData, "<meta name=\"description\" content=\"文件大小：", "|\" />").replace(" ", "");
                String uploader = StringUtils.getSubString(urlData,"<span class=\"user-name\">","</span><span class=\"user-name-txt\">");
                map.put("DownloadURL", downloadURL);
                map.put("FileName", fileName);
                map.put("Size", size);
                map.put("Uploader",uploader);
                return map;
            }
            throw new LanzouException(jsonObject.get("inf").getAsString());
        }

        throw new NullPointerException();
    }

    /**
     * 使用 cookie 从服务器获取账号下所有文件信息。
     * @param cookie 蓝奏云 cookie。
     * @return 包含文件信息的映射表，其中文件名作为键，文件详细信息作为值。
     */
    public static Map<String,Map<String,String>> getFiles(String cookie) {
        String uid = StringUtils.getSubString(cookie,"ylogin=",";");
        String urlData = HTTPUtils.doPost("https://pc.woozooo.com/doupload.php?uid=" + uid,"task=5&folder_id=-1&pg=1&vei=UlZWVA1fBAhQBANTDFY%3D","https://pc.woozooo.com/doupload.php?uid=" + uid,cookie);
        if (!urlData.isEmpty()) {
            JsonObject jsonObject = JsonParser.parseString(urlData).getAsJsonObject();
            int zt = jsonObject.get("zt").getAsInt();
            if (zt == 1) {
                Map<String,Map<String,String>> files = new HashMap<>();
                Iterator<JsonElement> iterator = jsonObject.get("text").getAsJsonArray().iterator();
                while (iterator.hasNext()) {
                    JsonObject fileObject = iterator.next().getAsJsonObject();
                    Map<String,String> cachedMap = new HashMap<>();
                    cachedMap.put("id",fileObject.get("id").getAsString());
                    cachedMap.put("type",fileObject.get("icon").getAsString());
                    cachedMap.put("name",fileObject.get("name_all").getAsString());
                    cachedMap.put("size",fileObject.get("size").getAsString().replace(" ",""));
                    cachedMap.put("downs",fileObject.get("downs").getAsString());
                    boolean isLock = fileObject.get("onof").getAsString().equals("1");
                    cachedMap.put("is_lock", String.valueOf(isLock ? 1 : 0));
                    String fileData = HTTPUtils.doPost("https://pc.woozooo.com/doupload.php", "task=22&file_id=" + cachedMap.get("ID"), "https://pc.woozooo.com/mydisk.php?item=files&action=index&u=" + uid, cookie);
                    if (!fileData.isEmpty()) {
                        if (JsonParser.parseString(fileData).getAsJsonObject().get("zt").getAsInt() == 1) {
                            JsonObject fileInfoObject = JsonParser.parseString(fileData).getAsJsonObject().get("info").getAsJsonObject();
                            String fileURL = fileInfoObject.get("is_newd").getAsString() + "/" + fileInfoObject.get("f_id").getAsString();
                            String password = fileInfoObject.get("pwd").getAsString();
                            cachedMap.put("file_url", fileURL);
                            //没上锁的文件蓝奏云也会随机生成密码
                            cachedMap.put("password", password);
                        }
                    }
                    cachedMap.put("time",fileObject.get("time").getAsString());
                    files.put(cachedMap.get("name"),cachedMap);
                }
                return files;
            }
            throw new LanzouException(jsonObject.get("info").getAsString());
        }
        return new HashMap<>();
    }
}
