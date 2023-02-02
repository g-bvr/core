package org.jkube.gitbeaver.util.test;


import org.jkube.gitbeaver.GitBeaver;
import org.jkube.gitbeaver.util.Environment;
import org.jkube.gitbeaver.util.FileUtil;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CreatePluginDocumentation {

    public static final String WORKDIR = "workdir";
    public static final String MAIN = "main";
    public static final String CREATE_DOCU_BVR = "src/test/resources/documentation/create_docu";

    public static final String OUTPUT = "doc/";

    public static final String TARGET_CLASSES = "target/classes";

    public void createDocu() {
        FileUtil.clear(Path.of(OUTPUT));
        Environment.setDefault(Environment.GITBEAVER_CLASSPATH, TARGET_CLASSES);
        GitBeaver.run(new HashMap<>(Map.of(
                MAIN, CREATE_DOCU_BVR,
                WORKDIR, "."
        )));
        TestUtil.assertNoFailures();
    }

}
