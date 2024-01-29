package master.koitoyuu.lanzou;

import java.util.Map;

public class LanzouMain {
    public static void main(String[] args) throws Exception {
        LanzouDownloader.getFiles("_uab_collina=170330764391201877114654; phpdisk_info=BzICNwJpAzoDNVQxCWJXBFA0UVpbMwdnDjUBYwU0U2RQYgQ%2BVTBXaAA3Vw5ZZwZlVWQAMQljVDYOawRmVWQHPQdlAmQCaAM7AzdUPAlhV29QY1FgW2cHaA44AWMFMlMyUGwENFVkV2wAM1dmWQoGbVUyADoJYlQ0DjgEYlVrBzIHMAI1; uag=91f5ff1fe9152a57e9571506aa928c14; ylogin=1795451; folder_id_c=-1; PHPSESSID=vfubo94kggq6cefiid1nnno1fnd380g7").forEach((key,value) -> {
            value.forEach((v,vv) -> System.out.println(v + ": " + vv));
        });
        if (args.length >= 2) {
            Map<String,String> map = LanzouDownloader.getFileInfo(args[0],args[1]);
            map.forEach((key,value) -> System.out.println(key + ": " + value));
            return;
        }
        System.out.println("用法: \"java -jar LanzouDownloader.jar URL Password\"");
    }
}
