JFLAGS = -g

JC = javac

JVM = java

CLASSPATH = -classpath src/

BIN = -d bin/

.SUFFIXES: .java .class
.java.class:
	$(JC) $(BIN) $(JFLAGS) $(CLASSPATH) $*.java
 
CLIENTCLASSES = \
    src/TestApp.java \

SERVERCLASSES = \
	src/Peer.java \
	src/Chunk.java \
	src/BackUpInfo.java \
	src/FileInfo.java \
	src/StorageSystem.java \
	src/RMI.java
	
MESSAGECLASSES = \
	src/Header.java \
	src/Message.java \
	src/MessageParser.java \
	src/Channel.java \
 
default: classes
 
classes: $(CLIENTCLASSES:.java=.class)\
$(SERVERCLASSES:.java=.class)\
$(MESSAGECLASSES:.java=.class)

client : $(CLIENTCLASSES:.java=.class)

server : $(SERVERCLASSES:.java=.class)

message : $(MESSAGECLASSES:.java=.class)
 
clean:
	$(RM) *.class