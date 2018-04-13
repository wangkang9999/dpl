top :   
	javac -d classfiles -sourcepath src src/dpl.java
	javac -d classfiles -sourcepath src src/gNode.java

problem:
	cat ./test/RPN.mylang
	cat ./test/t.input
problemx:
	java -classpath classfiles dpl  ./test/RPN.mylang  ./test/t.input
error1:
	cat ./test/err1.mylang
error1x:
	java -classpath classfiles dpl ./test/err1.mylang
error2:
	cat ./test/err2.mylang
error2x:
	java -classpath classfiles dpl ./test/err2.mylang
error3:
	cat ./test/err3.mylang
error3x:
	java -classpath classfiles dpl ./test/err3.mylang
arrays:
	cat ./test/arrays.mylang
arraysx:
	java -classpath classfiles dpl ./test/arrays.mylang
conditionals:
	cat ./test/conditionals.mylang
conditionalsx:
	java -classpath classfiles dpl ./test/conditionals.mylang
recursion:
	cat ./test/recursion.mylang
recursionx:
	java -classpath classfiles dpl ./test/recursion.mylang
iteration:
	cat ./test/iteration.mylang
iterationx:
	java -classpath classfiles dpl ./test/iteration.mylang
functions:
	cat ./test/functions.mylang
functionsx:      
	java -classpath classfiles dpl ./test/functions.mylang
dictionary:
	cat ./test/dictionary.mylang
dictionaryx  :
	echo Not implemented
	#java -classpath classfiles dpl ./test/dictionary.mylang

all:
	java -classpath classfiles dpl  ./test/RPN.mylang  ./test/t.input
	java -classpath classfiles dpl ./test/arrays.mylang
	java -classpath classfiles dpl ./test/conditionals.mylang
	java -classpath classfiles dpl ./test/recursion.mylang
	java -classpath classfiles dpl ./test/iteration.mylang
	java -classpath classfiles dpl ./test/functions.mylang

clean : 
	rm -f classfiles/*.class
	

