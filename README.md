# Hunters Hunted

This is one of several Java applications I wrote as part of a course on
object oriented programming.

## About

The idea here is a simple simulation of hunter and prey, both with distance
limited perception. There are three types of agent indicated by three colours.
Each colour predates on one of the other colours and is in turn predated upon
by agents of the third colour.

To complicate things further, each agent leaves a wake behind it that decays
over time and is not able to cross its own trail. When an agent is predated,
a replacement immediately appears at a random location.

## Details

The project is in the standard Java format and so should import cleanly into
an IDE such as NetBeans. As such the actual source files are hidden away in
the bottom of the nested directory structure under the src folder.

I've provided a couple of shell scripts called run.sh and build.sh which do
what their names probably suggest; run the compiled binary and build the
application. Building has been tested as working under both macOS and Linux
and the generated .jar file tested was also running on Windows. The .jar file
should be able to be run on any environment supporting the JVM and Swing.

Further documentation about the application can be found in agents.pdf.

If you're running on macOS or Linux you should be able to run the .jar file
directly using "java -jar AgentApp.jar".
