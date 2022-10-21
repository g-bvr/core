package org.jkube.gitbeaver.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableSubstitution {

    public static final Pattern VAR_EXPRESSION = Pattern.compile(".*\\$\\{([a-zA-Z0-9_-]+)\\}.*");

    public static String substituteVariables(final String line, Map<String, String> variables) {
        StringBuilder result = new StringBuilder();
        String remain = line;
        Matcher m;
        while ((m = VAR_EXPRESSION.matcher(remain)).matches()) {
            String var = m.group(1);
            int start = m.start(1)-2;
            int end = m.end(1)+1;
            if ((start > 0) && (remain.charAt(start-1) == '$')) {
                result.append(substituteVariables(remain.substring(0, start-1), variables));
                result.append(remain, start, end);
                remain = remain.substring(end);
            } else {
                String value = variables.getOrDefault(var, "");
                remain = replace(remain, start, end, value);
            }
        }
        result.append(remain);
        return result.toString();
    }

    public static String replace(final String string, final int start, final int end, String subs) {
        return string.substring(0, start)+subs+string.substring(end);
    }

}
