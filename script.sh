
#!/bin/bash

# pth="/home/Zakaria/Documents/Ihm/javafx-sdk-17.0.15/lib"

export pth=/usr/share/openjfx/lib

javac -d bin --module-path "$pth" --add-modules javafx.controls,javafx.graphics,javafx.fxml pendu_pour_etu/src/*.java

java -cp bin --module-path "$pth" --add-modules javafx.controls,javafx.graphics,javafx.fxml Pendu



