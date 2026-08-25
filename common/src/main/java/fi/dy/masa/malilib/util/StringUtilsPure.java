package fi.dy.masa.malilib.util;

import java.util.Locale;

public class StringUtilsPure {
    public static String splitCamelCase(String str) {
        str = str.replaceAll(
           String.format("%s|%s|%s",
              "(?<=[A-Z])(?=[A-Z][a-z])",
              "(?<=[^A-Z])(?=[A-Z])",
              "(?<=[A-Za-z])(?=[^A-Za-z])"
           ),
           " "
        );
        if (str.length() > 1 && str.charAt(0) > 'Z') {
            str = str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1);
        }
        return str;
    }
}
