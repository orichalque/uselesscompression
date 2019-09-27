# Useless Compression

### What for ?

It takes an image as an input, and reduces its quality with a given factor (starting from 2), but somehow maintains its weight.
This is especially useless.

### But, how ?

Makes a matrix out of an image, computes average colors out of pixels (2^n pixels, where n is the factor) and replace the previous values of the pixel with the one computed. The initial matrix keeps its size, that's why the image size remains the same.
It is possible to reduce the size of the matrix, in order to do a proper compression, but, that would not be useless.

Before: 

<img src="https://github.com/orichalque/uselesscompression/blob/master/src/test/resources/download.png?raw=true" width="100">

After:

<img src="https://github.com/orichalque/uselesscompression/blob/master/src/test/resources/output.jpg?raw=true" width="100">

# Weird Splitters

This project propose some weird image experimentations, easily usable in the WeirdSplitters classes.
The purpose is to transform images according to some algorithms, providing some random results.

Before: 

<img src="https://github.com/orichalque/uselesscompression/blob/master/src/test/resources/octopus.jpg?raw=true" width="100">

After:

<img src="https://github.com/orichalque/uselesscompression/blob/master/src/test/resources/downloadWeird.jpg?raw=true" width="100">

# That's great, how can I use your code?

Easy, that's a maven project, download it and run `mvn package`. Feel free to modify it.

