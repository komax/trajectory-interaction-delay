
.PHONY: build
BIN_DIR := bin

build: src/frechet/*.java src/visualization/*.java
	test -d $(BIN_DIR) || mkdir $(BIN_DIR)
	javac src/frechet/*.java -d $(BIN_DIR) -cp $(BIN_DIR)
	javac src/matlabconversion/*.java -d $(BIN_DIR) -cp $(BIN_DIR)
	javac src/visualization/*.java -d $(BIN_DIR) -cp $(BIN_DIR)

clean:
	test -d $(BIN_DIR) && rm -R bin
