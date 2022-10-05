package org.jkube.gitbeaver.util;

import org.jkube.gitbeaver.interfaces.LogConsole;
import org.jkube.logging.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static org.jkube.logging.Log.debug;
import static org.jkube.logging.Log.log;

public class ExternalProcess {

	private static final long INITIAL_TIMEOUT = 15*60L; // 15 minutes
	private static final long DESTROY_SLEEP = 1000;
	private static final long RUNTIME_WARN = 10;

	private final List<String> output = new ArrayList<>();
	private final List<String> warnings = new ArrayList<>();
	private final List<String> errors = new ArrayList<>();
	private final List<Pattern> acceptedErrorLines = new ArrayList<>();
	private final List<Pattern> warningLines = new ArrayList<>();
	private final List<Pattern> errorOutputLines = new ArrayList<>();
	private Pattern successMarker;
	private boolean allowSuccessMarkerMissing;
	private boolean successMarkerFound;
	private ProcessBuilder pb;
	private Predicate<String> lineFilter;
	private String command;
	private boolean nonZeroExitValueAllowed;
	private long timeout = INITIAL_TIMEOUT;
	private boolean hasTimedOut = false;

	private LogConsole logConsole;

	public ExternalProcess command(String... command) {
		this.pb = new ProcessBuilder(command);
		this.successMarker = null;
		StringBuilder sb = new StringBuilder();
		for (String c : command) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(c);
		}
		this.command = sb.toString();
		return this;
	}

	public ExternalProcess logConsole(LogConsole logConsole) {
		this.logConsole = logConsole;
		return this;
	}

	public ExternalProcess dir(String directory) {
		this.pb.directory(new File(directory));
		return this;
	}

	public ExternalProcess dir(Path path) {
		this.pb.directory(path.toFile());
		return this;
	}

	public ExternalProcess lineFilter(Predicate<String> lineFilter) {
		this.lineFilter = lineFilter;
		return this;
	}

	public ExternalProcess execute() {
		successMarkerFound = false;
		try {
			tryExecute();
		} catch (IOException e) {
			error("IOException occurred: "+e);
		} catch (InterruptedException e) {
			warn("Process was interrupted");
			throw new RuntimeException("interrupted", e);
		}
		if (!warnings.isEmpty()) {
			warn("There were "+warnings.size()+" warning lines");
		}
		if (!errors.isEmpty()) {
			warn("There were "+errors.size()+" error lines");
		}
		if ((successMarker != null) && !successMarkerFound && !allowSuccessMarkerMissing) {
			error("Success marker '"+successMarker+"' was not found in output.");				
		}
		return this;
	}

	private void tryExecute() throws IOException, InterruptedException {
		logConsole.command(command);
		debug("COMMAND> "+command);
		long timestamp = System.currentTimeMillis();
		Process proc = pb.start();
		OutputReader outReader = createReader(proc.getInputStream(), logConsole, false);
		OutputReader errReader = createReader(proc.getErrorStream(), logConsole, true);
		proc.waitFor(timeout, TimeUnit.SECONDS);
		long seconds = (System.currentTimeMillis() - timestamp + 500)/1000;
		String runtimeMessage = "(was running for "+seconds+" seconds)";
		if (seconds < RUNTIME_WARN) {
			logConsole.ignore(runtimeMessage);
		} else {
			logConsole.warn(runtimeMessage);
		}
		while (proc.isAlive()) {
			hasTimedOut = true;
			warn("Process not finished after maximal timeout. Destroying it");
			proc.destroyForcibly();
			Thread.sleep(DESTROY_SLEEP);
		}
		outReader.stop();
		errReader.stop();
		if (proc.exitValue() != 0) {
			if (nonZeroExitValueAllowed) {
				warn("Exit value: "+proc.exitValue());
			} else {
				error("Exit value: "+proc.exitValue());
			}
		}					
	}

	private OutputReader createReader(InputStream inputStream, LogConsole console, boolean error) {
		return new OutputReader(inputStream) {
			@Override
			public void processLine(String line) {
				processOutputLine(line, console, error);
			}
		};
	}

	protected void processOutputLine(String line, LogConsole console, boolean error) {
		if ((lineFilter != null) && !lineFilter.test(line)) {
			console.ignore(line);	
			log("IGNORE> "+line);
		} else if ((successMarker != null) && matches(line, successMarker)) {
			successMarkerFound = true;
			console.success(line);
			output.add(line);
			log("SUCCESS> "+line);
		} else if (error && matches(line, acceptedErrorLines)) {
			console.ignore(line);						
			output.add(line);
			log("ACCEPT> "+line);
		} else if (matches(line, warningLines)) {
			console.warn(line);
			warnings.add(line); 
			log("WARN> "+line);
		} else if (error) {
			console.error(line);
			errors.add(line);
			log("ERROR> "+line);
		} else if (matches(line, errorOutputLines)) {
			console.error(line);						
			errors.add(line);
			log("ERROR> "+line);
		} else {
			console.add(line);	
			output.add(line);
			log("OUTPUT> "+line);
		}
	}

	private void error(String string) {
		Log.error(string);
	}

	private void warn(String string) {
		Log.log(string);
	}

	private boolean matches(String line, List<Pattern> patterns) {
		if (line.isBlank()) {
			return true;
		}
		for (Pattern pattern : patterns) {
			if (matches(line, pattern)) {
				return true;
			}
		}
		return false;
	}

	private boolean matches(String line, Pattern pattern) {
		return pattern.matcher(line).matches();
	}

	public ExternalProcess successMarker(String pattern) {
		this.successMarker = createMatcher(pattern);
		return noError(pattern);
	}

	public ExternalProcess allowSuccessMarkerMissing() {
		this.allowSuccessMarkerMissing = true;
		return this;
	}

	public boolean successMarkerIsOptional() {
		return allowSuccessMarkerMissing;
	}

	public ExternalProcess errorMarker(String pattern) {
		errorOutputLines.add(createMatcher(pattern));
		return this;
	}

	public ExternalProcess warning(String pattern) {
		warningLines.add(createMatcher(pattern));
		return this;
	}

	public ExternalProcess noError(String pattern) {
		acceptedErrorLines.add(createMatcher(pattern));
		return this;
	}

	private Pattern createMatcher(String pattern) {
		if (!pattern.contains(".*")) {
			pattern += ".*";
		}
		return Pattern.compile(pattern);
	}

	public ExternalProcess warning(List<String> patterns) {
		patterns.forEach(this::warning);
		return this;
	}

	public ExternalProcess noError(List<String> patterns) {
		patterns.forEach(this::noError);
		return this;
	}

	public List<String> getErrors() {
		return errors;
	}

	public List<String> getWarnings() {
		return warnings;
	}

	public List<String> getOutput() {
		return output;
	}

	public boolean hasSucceeded() {
		return errors.isEmpty();
	}

	public boolean hasFailed() {
		return !hasSucceeded();
	}

	public boolean successMarkerFound() {
		return successMarkerFound;
	}

	public void nonZeroExitValueAllowed() {
		nonZeroExitValueAllowed = true;
	}

	public ExternalProcess timeoutSeconds(int numSecs) {
		timeout = numSecs;
		return this;
	}

	public boolean hasTimedOut() {
		return hasTimedOut;
	}

	public boolean hasCommand() {
		return command != null;
	}
}
