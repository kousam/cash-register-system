package org.controlmatic.client.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;

public class StringUtil {
    public static String toUTF8(String string) {
        return new String(string.getBytes(), StandardCharsets.UTF_8);
    }

    public static String bigDecimalToPriceString(BigDecimal price) {
        return price.setScale(2, RoundingMode.HALF_EVEN).toString();
    }

    public static String toPriceString(String string) {
         return bigDecimalToPriceString(new BigDecimal(string));
    }

    public static String toEuro(String string) {
        return toUTF8(toPriceString(string) + "€");
    }
}
