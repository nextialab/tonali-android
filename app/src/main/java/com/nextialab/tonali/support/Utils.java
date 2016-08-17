package com.nextialab.tonali.support;

import java.util.List;

/**
 * Created by Nelson on 8/16/2016.
 */
public class Utils {

    public static <T> String makeString(String separator, List<T> items) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            builder.append(items.get(i));
            if (i < items.size() - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

}
