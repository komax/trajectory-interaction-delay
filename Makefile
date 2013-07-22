
.PHONY: stuff

stuff: src/frechet/*.java
	javac src/frechet/*.java
	mv src/frechet/*.class bin/frechet/
