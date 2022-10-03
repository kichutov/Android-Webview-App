# Android Webview App
### Webview application that displays a web resource or native part depending on the server response

For the correct operation of the application, you must have a server.  
When you start the application, it establishes a connection with the server.  
The server analyzes the connection data and, depending on the settings, allows or denies showing the web resource in a Webview.  

If the server forbids displaying the web resource, it returns a 404 error.  
If the application received a 404 error, then it displays the native part of the application.
Otherwise, the application displays the web resource in a Webview.
