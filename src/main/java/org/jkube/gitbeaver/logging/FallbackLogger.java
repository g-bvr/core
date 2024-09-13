package org.jkube.gitbeaver.logging;

import org.jkube.gitbeaver.logging.Log;
import org.jkube.gitbeaver.logging.Logger;

import java.io.PrintStream;

public class FallbackLogger implements Logger {

	private String previous = "";
	private int count = 0;

	@Override
	public void log(final LogLevel level, final Throwable e, final String message, final Object[] parameters) {
		PrintStream logto = level.compareTo(LogLevel.LOG) < 0 ? System.err : System.out;
		if (message != null) {
			String line = substitute(message, parameters);
			if (line.equals(previous)) {
				count++;
			} else {
				if (count > 0) {
					int p = 0;
					while (p < line.length() && (line.charAt(p) == ' ')) {
						p++;
					}
					logto.println(" ".repeat(p)+"(repeated "+count+" times)");
					count = 0;
				}
				logto.println(line);
				previous = line;
			}
		}
		if (e != null) {
			e.printStackTrace();
		}
 	}

	public static String substitute(final String text, final Object[] parameters) {
		if ((parameters == null) || (parameters.length == 0)) {
			return text;
		}
		StringBuilder sb = new StringBuilder();
		int parNum = 0;
		int pos = 0;
		while (pos < text.length()) {
			char c = text.charAt(pos);
			if ((pos < text.length()-1) && (c == '{')  && (text.charAt(pos+1) == '}')) {
				pos++;
				if (parNum < parameters.length) {
					sb.append(parameters[parNum]);
				} else {
					sb.append("?");
				}
				parNum++;
			} else {
				sb.append(c);
			}
			pos++;
		}
		if (parNum != parameters.length) {
			Log.warn("Mismatch in log parameter: message has {} slots, {}  parameters provided", parNum, parameters.length);
		}
		return sb.toString();
	}

}
