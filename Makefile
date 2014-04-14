
.PHONY: build
CLASSFILES_DIR := bin/frechet

build: src/frechet/*.java
	javac src/frechet/*.java
	test -d $(CLASSFILES_DIR) || (mkdir bin && mkdir bin/frechet)
	mv src/frechet/*.class $(CLASSFILES_DIR)

clean:
	test -d $(CLASSFILES_DIR) && rm -R bin
