# Caleto
Caleto is a Java implementation of [https://en.wikipedia.org/wiki/Steganography](steganography) which hiddes files inside bmp images.

# GUI
Here’s an screenshot of Caleto:

![Merged file](https://raw.githubusercontent.com/NicolasBonet/Caleto/master/example/gui.png)

# License
All code is licensed under the GPL 3.0 license. For more information please visit: http://www.gnu.org/licenses/gpl-3.0.en.html

# Example
Here’s a simple BMP file:

![Merged file](https://raw.githubusercontent.com/NicolasBonet/Caleto/master/example/example.bmp)

We are going to hide the following zip inside the former image:

http://www.colorado.edu/conflict/peace/download/peace_essay.zip

This is the result, as you can see there are no noticeable differences between the original file and the new one:

![Merged file](https://raw.githubusercontent.com/NicolasBonet/Caleto/master/example/example_merged.bmp)

It’s possible, using Caleto, to hide images and also recover the file hidden within the image.

# Known issues:
Files to be hidden must be at least 8 times lighter than the BMP file used to hide it.

