# StanVoiceRecognition
Voice recognition

# Windows build information
## build
> "%JAVA_HOME%/bin/javac" -sourcepath ./src -d bin Main.java

## run
> "%JAVA_HOME%/bin/java" -classpath ./bin Main

## build_jar_lib
> "%JAVA_HOME%/bin/jar" -cf StanVoiceRecognition.jar -C bin .