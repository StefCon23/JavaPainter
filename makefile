JFLAGS = -g
JC = javac
RM = rm
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	JavaPainter.java \
	Drawing.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
