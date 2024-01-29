package master.koitoyuu.lanzou;

import java.util.Map;

public class LanzouMain {
    public static void main(String[] args) throws Exception {
        if (args.length >= 2) {
            Map<String,String> map = LanzouDownloader.getFileInfo(args[0],args[1]);
            map.forEach((key,value) -> System.out.println(key + ": " + value));
            return;
        }
        System.out.println("用法: \"java -jar LanzouDownloader.jar URL Password\"");
    }
}
