
.PHONY: build
BIN_DIR := bin
FRECHET_DIR := $(BIN_DIR)/frechet

build: src/frechet/*.java
	test -d $(BIN_DIR) || mkdir $(BIN_DIR)
	test -d $(FRECHET_DIR) || mkdir $(FRECHET_DIR)
	javac src/frechet/*.java -d $(BIN_DIR)

clean:
	test -d $(BIN_DIR) && rm -R bin
