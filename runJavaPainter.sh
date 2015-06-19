#!/bin/bash

#	runJavaPainter.sh
#
#	compiles the program
#	runs the program
#	deletes the program
#
#	Conor Stefanini, 19 June 2015

make
java JavaPainter
make clean
