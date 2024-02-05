#!/bin/sh

echo "Cleaning build environment..."
rm -rf classes
rm -f AgentApp.jar

echo "Compiling application..."
javac -d classes -classpath classes -sourcepath src \
    src/uk/ac/tees/v8206593/agent/AgentApp.java

[ $? -eq 0 ] || exit 1

echo "Creating jar..."
jar -cfe AgentApp.jar uk/ac/tees/v8206593/agent/AgentApp -C classes .
echo "done."

