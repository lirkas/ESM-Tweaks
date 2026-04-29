## Development Infos

Since this mod has been developed in VSCode, compatibiliy with other IDE is not guaranteed, 
but gradle wrapper tasks should work fine in most CLI, 
as long as the required assets to download for the first time build are reacheable 
(internet connection).<br>
The commands below should be run in a terminal window.


#### Compiling the mod:
```bash
# compile the "dev" jar (mainly for testing purposes)
./gradlew build

# both "dev" and "release" jars
./gradlew build -Prelease
```

Compiled jars will be found in `build/libs` once the task is successful.

#### Running Minecraft client from the project:
```bash
# -Pclient will tell minecraft to use a specific folder to run the instance
# see "gradle.build" for the folder location
./gradlew runclient -Pclient
```

#### Running Minecraft server from the project:
```bash
# -Pserver will tell minecraft to use a specific folder to run the instance
# see "gradle.build" for the folder location
./gradlew runserver -Pserver
```

The `--debug-jvm` option can be added to attach a debugger before the client/server starts running.

Extra Mods to test with can either be put in the `libs/` folder or added from the `build.gradle` file.

### Extra Notes

ForgeGradle version used here is outdated (*2.3-SNAPSHOT*) and not supported anymore. 
Various issues have taken place trying to make this project work with newer ones, 
and the final decision was to stick with it.
For that reason, it currently does not seem possible to use the latest *1.12.2* forge build (*14.23.5.2860*) 
when running Minecraft from gradlew. 
This project currently uses the latest compatible forge build.<br>
The compiled mod still works fine with both forge versions. 
See [gradle.properties](/gradle.properties) for all the versions used in the project.<br>
Other issues may still arise while attempting to run the client from IDE 
(such as minecraft not finding assets to download due to invalid URLs).
