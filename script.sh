

export pth=/usr/share/openjfx/lib
javac -d bin --module-path $pth --add-modules javafx.controls,javafx.graphics src/*.java
java -cp bin:graphics --module-path $pth --add-modules javafx.controls,javafx.graphics AppliPlusieursFenetres


