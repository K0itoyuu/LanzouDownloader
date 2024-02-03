package master.koitoyuu.lanzou;

import java.util.Map;

public class LanzouMain {
    public static void main(String[] args) throws Exception {
        LanzouDownloader.getFiles("Cookie: _uab_collina=170330764391201877114654; uag=91f5ff1fe9152a57e9571506aa928c14; ylogin=1795451; folder_id_c=-1; phpdisk_info=AzYHMgJpBTxXYVQxD2QGVQJmVl0OZlMzU2gBYw4%2FBjFUZgU%2FBmNRbgM0BVwIagBtAmMNbF5lAG8DMVI1BDRRZAMwBzYCYAVsV2ZUPA9hBmUCM1Y2DmJTMVNgATAOPAZlVDUFNAYwUT8DNwVjCFsAawJlDTdeNQBgAzVSNAQ6UWQDNAcw")
                .forEach((key,value) -> {

                });
        if (args.length >= 2) {
            Map<String,String> map = LanzouDownloader.getFileInfo(args[0],args[1]);
            map.forEach((key,value) -> System.out.println(key + ": " + value));
            return;
        }
        System.out.println("用法: \"java -jar LanzouDownloader.jar URL Password\"");
    }
}
