###########
Tessellator
###########

Tessellator is a triangulation tool for Java aimed for those who wish to manipulate and play around with large datasets of complex geometrical shapes. Tesselator does not define its own geomety classes but tries instead to preserve the accessibility and extensibility that standard awt components offer. Tesselator also takes use of 
`JogAmp's <http://jogamp.org/>`_ cross-platform bindings to the 
`OpenGL Utility Library (GLU) <http://www.opengl.org/resources/faq/technical/glu.htm>`_ 

Dependencies
============
 * `Download <https://github.com/anonomis/tessellator/archives/master>`_ or clone using ``git clone git://github.com/anonomis/tessellator.git``
 * `Download JOGL <http://code.google.com/p/processing/source/browse/trunk/processing/java/libraries/opengl/library/jogl.jar>`_ 
 * `Download GlueGen <http://code.google.com/p/processing/source/browse/trunk/processing/java/libraries/opengl/library/gluegen-rt.jar>`_ 
 * Download native libraries for both JOGL and GlueGen `all platforms here <http://code.google.com/p/processing/source/browse/trunk/processing#processing%2Fjava%2Flibraries%2Fopengl%2Flibrary%2Flinux64>`_
 * Setup build path variables GLLUEGEN and JOGL.
 * Setup native libraries location for GLUEGEN and JOGL.


Getting started
===============
So... you have a huge bunch of good ol' awt shapes of arbitrary form and want to manage them easier by breaking them up in small uniformed comparable pieces. This is what Tesselator does:

``List<Triangle> triangles = new ArrayList<Triangle>();``  

``for (Shape shape : shapeHeap) {``

``triangles.addAll(new Tessellator().getTriangles(anyShape, null));``

``}``

And that's it.


Known issues
============
So far... none... (For the sake of this software: Please prove me wrong!)

Have fun!