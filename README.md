# Software Design

Laboratory work in the discipline "Software Design"

### Important:
- The project will be successfully built only inside the repository. (This is due to the automatic generation of the current version of the assembly)
- When cloning the repository to the root directory of the project, you need to create a `keystore.properties` file, inside which the data will be stored for signing the release certificate of the APK

# Lab №1
The application to receive phone information to display the current device’s IMEI. If negative, IMEI will not be displayed. You can start the application again to request permission, and then respond positively to it.

# Lab №2
Testing deep link: adb shell am start -a android.intent.action.VIEW -d "app://myapp/frag3"