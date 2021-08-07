SOURCES=$(shell find . -name '*.java')

all: $(SOURCES)
	./build.sh

clean:
	rm -rf build save.data

.PHONY: clean all
