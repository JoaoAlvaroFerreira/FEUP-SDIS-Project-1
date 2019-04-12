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
	src/FileInfo.java \
	src/StorageSystem.java \
	
MESSAGECLASSES = \
	src/Body.java \
	src/Header.java \
	src/MC.java \
	src/MCBackup.java \
	src/MCRestore.java \
	src/PutChunk.java \
	src/Stored.java \
 
default: classes
 
classes: $(CLIENTCLASSES:.java=.class)\
$(SERVERCLASSES:.java=.class)\
$(MESSAGECLASSES:.java=.class)

client : $(CLIENTCLASSES:.java=.class)

server : $(SERVERCLASSES:.java=.class)

message : $(MESSAGECLASSES:.java=.class)
 
clean:
	$(RM) *.class