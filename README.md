


# What is Clee:
Clee is an mobile app that provide a platform for college student to trade items like books, furniture, video games and ?.

Compare to traidional similar platform that prevalent in college like mailing list, forum and facebook posts. Clee aim to provide a more ? user experience. 
## Where is "Clee" come from?
The name of Clee come from the combination of two vocab - College and Flea, as Flea is part of another term - Flea Market, combined these two together we get Clea, for simplicity, replace the last "a" with "e", and Clee is born.

# Screenshots:

![](https://lh3.googleusercontent.com/fVQ1mG0S32msgUhjGXWOZniIPpurpe0RzrO46fg8Xpa14piJw1mKRqisD2A0gsadJGnusnX3wQRW "Login")
![](https://lh3.googleusercontent.com/ac5I3JBR6KWPFEnz0QioNO3eC5iNKo1G1yhZs0hMz6azY08YyI1tYaP0st2uyHX9YsLdZ20qGnkN "Item List")
![](https://lh3.googleusercontent.com/sroJB-K4Hdo9W-1Fg6Qj0Lkg2DPnCYIdOLRfPcXlbresxNkex7rHQdNuJ6d793GZi-56CVSg9Hok "Item Detail")
![](https://lh3.googleusercontent.com/K8N8ZbAW-01epBd4xHqx14JkJ2UhdeVLYMd9aWPwBM_V-BaQXKKsZWEo0E1_X8G8-OayuHbZrwS1)
![](https://lh3.googleusercontent.com/1Pz9SuC6hUqXqmrnUWPj2lI62FLW7UikjyVllSHZy-LQM4UjfPbVqLd3Ts5FDOPsvwZ_lWTF7Ac_)
![](https://lh3.googleusercontent.com/zHCiwMRh4x2n1-UEefaEYs6_sEDxWrvQvfR-EBbeD3CYVLQeX8WpKMso99q7Wjr72cHhwkilzeqL "User Center")

# License:

This project is released under GNU GPL v3.0 license. Below is a summary of it's permissions, conditions and limitations:

| Permissions | Conditions | Limitations |
|--|--|--|
| Commercial Use | Disclose source | Liability |
| Distribution | License and copyright notice | Warranty |
| Modification | Same license |  |
| Patent Use | State changes |  |
| Private Use |  |  |

For more information regarding this license, checkout out this link: https://choosealicense.com/licenses/gpl-3.0/

# Project Founders:

## Clee is founded by 3 university students, they are:

* Tian Tuo You (Software Engineering/Computer Science Majors, Syracuse University)
	* Linkedin: https://www.linkedin.com/in/tian-tuo-you-5068319b/

* Ya Hui Liang (Software and Computer Engineering Majors, Milwaukee School of Engineering)
	* Linkedin: https://www.linkedin.com/in/yahui-liang/

* Yi Pei Zhu (Computer Science Major, Syracuse University)
	* Linkedin: https://www.linkedin.com/in/yipei-zhu-90b952139/

This project won't be here without any of these students' contribution, each one of them are equally appreciated.

  

# Dev Environment Setup:

## Front End:

The front end is written in Swift, with cocoa touch framework and various 3rd party components managed under Cocoapods.

### Requirements:

* A Mac
* XCode (IDE, Avaliable on Mac's App Store for free)
* Cocoapods (Dependency Mananger)

### Cocoapods Installation Tutorial:

Instead of typing tons of steps here, I would like to share a video tutorial made by Cocoapods' invetor - Google, it went through the installation process throughly and talked about how to use Cocoapods on a XCode project, plus, it's quite funny ;) Here is the link: https://www.youtube.com/watch?v=iEAjvNRdZa0

### Step 1:
Clone the code from github repository: https://github.com/wintop6211/Clee.git
or
Simply run `git clone https://github.com/wintop6211/Clee.git`

### Step 2:
Run `pod install` in project folder, this will download all dependencies.

### Step 3:
Open .xcodeworkspace to open the project.


## Back End:

The web services used by Clee was implemented in Java. Jersey was the RESTful Web Services Framework we used. Steps for setting up the project was shown below.

### Servlet Container

Here are current versions of Tomcat we are using right now. You can use any versions you want, but we are not sure if others versions work exact same as these two. 

#### Tomcat 7.0.78 / 8.5.16

Please download the correspond versions Tomcat from their official site: https://tomcat.apache.org

### IDE

IntelliJ was the IDE we originally used. We highly recommend to use IntelliJ since it is very robust on detecting the project type, frameworks, and databases you used.

#### IntelliJ

##### Import Project

1. Open **IntelliJ**.
2. Click **Import Project**.
3. Navigate to **Server** folder.
4. Click **Open**.
5. Select **create Project from existing sources**, and click **Next**.
6. Keep the **default project name**, and click **Next**.
7. Keep the default **src** folder.
8. Select **lib** is there are multiple libraries detected.
9. Keep clicking **Next** with default values.
10. Click **Finish**.
11. **IntelliJ** will guide you to set up **database sources** and **pom.xml** dependencies. Messages will appear on the bottom right corner of the window.
12. The message center of **IntelliJ** will report this in the message center. 
![The message is like this](https://lh3.googleusercontent.com/Y2UpRXqmVPwDzasKo8kyc0uXpR-j4o8EwcGBF_INR8PKTmG09uYUQ0NE09mUn2vyjc_buAoMy-L4)
The message center is like this. Click on this button, the message panel will pop up.
![](https://lh3.googleusercontent.com/4ie1pKdv50zfPnhqNFtmwZouP9tS6nnMAeE_sH-CPAMMQ4t_kMHZir1ZyA_SBgFQoNLvzu15EC2Y)
Please click **Add as Maven Project**

##### Tomcat Set up
1. It is the time to setup **Tomcat** run configurations. <br> ![enter image description here](https://lh3.googleusercontent.com/920NnqnySyiX_Fch5Ns3hcOHVV_urKqrC5qsSUrwFaHoXJm-EJ6o_thEBGiDyquPdMS3ABgp4VYQ)
2. Please click **local**. <br>![enter image description here](https://lh3.googleusercontent.com/7u4T00G4lgv7yBIRu9FVp-vjF2sAVqNBfcxjwFQf094cTVQHoIAwSsO1VKGuoLkF2VCPE-wTZRtR)
3. Please **name** your run settings. I named it to be **Tomcat 8.5.16**.<br>![enter image description here](https://lh3.googleusercontent.com/JMxwvZ6eR3iVa6Kdhfm08L1UoXZDR4Q0C43FNQgJsJrZbw0aCeQbrhEyA9o2Ix2ncH1xYbW83ppH)
4. You will see message appear at the bottom of the window. Click **Fix**.<br>![enter image description here](https://lh3.googleusercontent.com/AOY_RQv5VPpjtFPwWCBJg2CXMHEUs_u-E_xM7_bJxBbQuchAJkeHc6aIaCEneYYTZySekizq43pk)
5. Click either is fine. <br>![enter image description here](https://lh3.googleusercontent.com/5QERjyf0NUC-WkKSc7-qk9FNbZX79xpIFBvyZFOwrIvmQpWScYL5RGzTmCcwMePf22FeXA7LiseV)
6. Click **Ok**.

##### Run the server
![enter image description here](https://lh3.googleusercontent.com/hIhSyGhdSnE5bp6UvTQnBouKfPJvi8r-1ZZyBJrAh0GubMH64GvX3_qCk-w2iWS2Td0mkoWLbGvU)<br>Select **Tomcat 8.5.16** or **other names**, and click **green arrow** on the right side of the box. The server should be started easily.
###### Note: Lack of permission can lead to unsuccessful starting the server. Please make sure Tomcat folders have enough permissions.

# Front-End Architecture

The architecture of iOS front-end is followed the MVC(Model-View-Controller) pattern that described in Apple Developer's document:
https://developer.apple.com/library/content/documentation/General/Conceptual/CocoaEncyclopedia/Model-View-Controller/Model-View-Controller.html

View layer is responsible for what user see and interact with on the screen. In Clee's project folder, files with .storyboard and .xib postfix is view layer related, they allow developer to design UI in a WYSIWYG(What you see is what you get) fashion. Here are some  related information:
https://developer.apple.com/xcode/interface-builder/
https://www.raywenderlich.com/160521/storyboards-tutorial-ios-11-part-1

Model layer contain all the logic that is crucial to app's operation, Clee's model layer include web service API, data object model and helper functions.

Controller is the coordinator between view and model layer, it receive notification from front-end when user performed action on screen, coordinate the model layer to process and instruct the front-end to display the result. Every file in controller folder is controller layer related. For information, this resource might help:
https://developer.apple.com/library/content/referencelibrary/GettingStarted/DevelopiOSAppsSwift/WorkWithViewControllers.html
