package org.jkube.gitbeaver;

import org.jkube.gitbeaver.interfaces.FileResolver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.jkube.logging.Log.onException;

public class DefaultFileResolver implements FileResolver {
    public static final Pattern VAR_EXPRESSION = Pattern.compile(".*\\$\\{([a-zA-Z0-9_-]+)\\}.*");

    @Override
    public List<String> resolve(Path workspacePath, Path filePath, Map<String, String> variables) {
        return onException(() -> Files.readAllLines(filePath)).fail("Could not load lines of file: "+filePath)
                .stream()
                .map(line -> substituteVariables(line, variables))
                .collect(Collectors.toList());
    }

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
        //System.out.println("Replace "+string+" from "+start+" to "+end+" with "+subs);
        return string.substring(0, start)+subs+string.substring(end);
    }

}
