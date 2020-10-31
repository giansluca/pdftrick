# PdfTrick 1.3

https://sourceforge.net/projects/pdftrick/  

Pdf images extractor  
PdfTrick is a free open source software released under Gpl 3 license. It requires java jdk 11 or or higher.  

<h3>Compiling native lib:</h3>  
In order to make PdfTrick working you need to compile the C source pdftrick_native.c file contained in src-jni folder.
To compile it you have to link the C Mupdf libs, compiled for win (64 bit only) and mac.
After generating the native lib (arch and Os dependent) you have to include it in the package org.gmdev.pdftrick.resouces.nativelib.

MuPdf 1.7a --> http://mupdf.com

The names for the native libs are:
win: libpdftrick_native_1.7a_64.dll  
mac: libpdftrick_native_1.7a_64.jnilib
