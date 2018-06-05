# Front-End Architecture

The architecture of iOS front-end is followed the MVC(Model-View-Controller) pattern that described in Apple Developer's document:
https://developer.apple.com/library/content/documentation/General/Conceptual/CocoaEncyclopedia/Model-View-Controller/Model-View-Controller.html

View layer is responsible for what user see and interact with on the screen. In Clee's project folder, files with .storyboard and .xib postfix is view layer related, they allow developer to design UI in a WYSIWYG(What You See Is What You Get) fashion. Here are some  related information:
https://developer.apple.com/xcode/interface-builder/
https://www.raywenderlich.com/160521/storyboards-tutorial-ios-11-part-1

Model layer contain all the logic that is crucial to app's operation, Clee's model layer include web service API, data object model and helper functions.

Controller is the coordinator between view and model layer, it receive notification from front-end when user performed action on screen, coordinate the model layer to process and instruct the front-end to display the result. Every file in controller folder is controller layer related. For information, this resource might help:
https://developer.apple.com/library/content/referencelibrary/GettingStarted/DevelopiOSAppsSwift/WorkWithViewControllers.html
