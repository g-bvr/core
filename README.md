# Repository g-bvr/core

This repository builds the core gitbeaver component in form of the 
docker image  [core docker image](https://hub.docker.com/r/gitbeaver/core). 

## Building

Building the docker image is done by the following two commands:

```
mvn package
docker build . -t gitbeaver/core:main
```

Manual bulding is usually not required, the docker image will be built automatically (from main branch) by
[this CI/CD script](.github/workflows/docker-image.yml) and pushed to [https://hub.docker.com/r/gitbeaver/core](https://hub.docker.com/r/gitbeaver/core).

## Smoke Test

Create a file ```main.bvr``` with one line:

```
LOG Hello World!
```

Then run the following command:
```
docker run -v $PWD:/workdir gitbeaver/core     
```

## Command Line Arguments

There can be an arbitrary number of command line arguments of the form ```key=value```. 
The passed key value pairs are available as variables in the executed beaver script.

The following three variables have special purpose and are equipped with default settings (which apply if no corresponding commandline argument was set).

| Variable | Special purpose                                                                        | Default value |
|:---------|:---------------------------------------------------------------------------------------|:--------------|
| main     | Name of beaver script to be executed (the extension ".bvr" will be added automatically | main          |
| run      | Identifier of this run (used in logging)                                               | init          |
| workdir  | The path (inside the docker image) of the directory that will be used as workspace     | workdir       |

## Documentation of defined commands

A list of the built-in commands can be found in this [automatically generated documentation](https://htmlpreview.github.io/?https://raw.githubusercontent.com/g-bvr/core/main/doc/CorePlugin.html).

**Note:** The git-beaver sercurity concept relies on the principle that the core image has minimal functionality and 
is subject to few changes over time. To achieve this, only a minimally necessary set of commands is build into the core 
executable and docker file. Additional functionality can be linked in at runtime. See the repository [g-bvr/base](https://github.com/g-bvr/base)
for the basic general purpose commands needed to build more advanced beaver scripts.

## Inner workings

The [Dockerfile](Dockerfile) adds the files ```main.jar``` (created by maven) and shell script [gitbeaver](gitbeaver) (which serves as entrypoint) to the docker image,
and sets the environment variable ```GITBEAVER_CLASSPATH``` to the folder where plugin classes are compiled.

At startup, the environment variable ```GITBEAVER_MASTERKEY``` is copied into the file ```/masterkey``` and then replaced with a dummy value. 
The [security plugin](https://github.com/g-bvr/security) reads this file and deletes it when initialized. This prevents
executed code (in plugins) from using this secret value, if they are initialized after the security plugin. 

The [java main](src/main/java/org/jkube/gitbeaver/Main.java) executes the class [GitBeaver](src/main/java/org/jkube/gitbeaver/GitBeaver.java) which serves as 
static handler for singletons of all components. The main thread executes the beaver script specified by variable ```main``` and then terminates
(the JVM will however stay alive as long as other threads are still running, for instance the [webserver plugin](https://github.com/g-bvr/web-server) has 
such a worker thread running until the web server is shut down).

## Curated open source release

A [docker file](https://hub.docker.com/r/gitbeaver/release) with a curated selection of plugins is provided by the E. Breuninger GmbH & Co. in the public repository
[e-breuninger/git-beaver](https://github.com/e-breuninger/git-beaver).

