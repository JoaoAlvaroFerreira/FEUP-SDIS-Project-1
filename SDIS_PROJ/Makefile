JFLAGS = -g

JC = javac

JVM = java

CLASSPATH = -classpath src/

BIN = -d bin/

.SUFFIXES: .java .class
.java.class:
	$(JC) $(BIN) $(JFLAGS) $(CLASSPATH) $*.java
 
CLIENTCLASSES = \
    src/start/TestApp.java \

SERVERCLASSES = \
	src/p2p/Peer.java \
    src/utils/RMI.java \
 
default: classes
 
classes: $(CLIENTCLASSES:.java=.class)\
$(SERVERCLASSES:.java=.class)

client : $(CLIENTCLASSES:.java=.class)

server : $(SERVERCLASSES:.java=.class)
 
clean:
	$(RM) *.class
