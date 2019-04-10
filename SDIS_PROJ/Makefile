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
    src/Backup.java \
	src/Body.java \
	src/Chunk.java \
	src/Delete.java \
	src/Header.java \
	src/MC.java \
	src/MCBackup.java \
	src/MCRestore.java \
	src/PutChunk.java \
	src/Reclaim.java \
	src/Stored.java \
 
default: classes
 
classes: $(CLIENTCLASSES:.java=.class)\
$(SERVERCLASSES:.java=.class)

client : $(CLIENTCLASSES:.java=.class)

server : $(SERVERCLASSES:.java=.class)
 
clean:
	$(RM) *.class