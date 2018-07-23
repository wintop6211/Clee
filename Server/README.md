## Server:

The web services used by Clee was implemented in Java. Jersey was the RESTful Web Services Framework we used. Steps for setting up the project was shown below.

### Requirements
1. IntelliJ / Eclipse
2. Java
3. Tomcat

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

### Notification services and Email services comments
#### APN
The notification service used for iOS devices is called APN. Apple's official website has the very good explanation on how APN works. The library used in this project is called **pushy**, and it is also the open source project on Github. The link is here: https://github.com/relayrides/pushy. **pushy** library has encapsulated most work for us, so developers do not need to worry about the APN protocal format when the notification is needed to be sent. Moreover, one class called **APNConnector** in the project has been created for sending purchase notifications. If some other kinds of notifications need to be included, feel free to extend this class to have more functionalities.
#### Email
The program itself is expected to send verifiaction email to the user. When the user creates the account, he/she should always verify his/her account through email because we expect users are only students. The current project has verfication web services implemented. The class **EmailController** is for sending emails. Same as above, if you need to expand it to have more functionalities, feel free to do that. The current program flow does not have this process included. When the user creates the account, he/her is set to `verified=True`.


