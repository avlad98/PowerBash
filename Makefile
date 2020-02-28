build:
	javac -Xlint src/*.java -d ./;

clean:
	rm -rf *.class
	rm -rf output errors


