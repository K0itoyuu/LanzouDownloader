package master.koitoyuu.lanzou.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static String getSubString(String text, String left, String right) {
        if (left == null || left.isEmpty()) {
            return "";
        }
        int startIndex = text.indexOf(left);
        if (startIndex == -1) {
            return "";
        }
        startIndex += left.length();
        int endIndex = right.isEmpty() ? text.length() : text.indexOf(right, startIndex);
        if (endIndex == -1) {
            endIndex = text.length();
        }
        return text.substring(startIndex, endIndex);
    }

    public static String replaceBlank(String str) {
        if (str == null) {
            return "";
        }
        Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll("");
    }
}
