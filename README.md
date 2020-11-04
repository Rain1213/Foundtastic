# **FOUNDTASTIC**
 The Main idea behind this project is to build an Android App to help find lost items in the university. Two kinds of people would help make this app possible; firstly, those who have lost something and secondly the good Samaritans who are kind enough to report any item they have found.
To make use of the app you first have to login to it. If the user does not have an account, he/she can easily create a new one. Once Logged in, you are given an option to either click on the “Lost” or the “Found” button which will direct you to two separate pages and where you have the freedom to choose from various item type. You will be required to enter the details of the item lost/found and click on “Submit” button. If the details of lost and found items match, people who have reported the item will be notified.

Our Application “Foundtastic” is unfortunately only limited to the university campus. It also compulsorily requires internet connection to operate, for databases have to be stored in the firebase database. 

With our previous knowledge in Java we ventured out to learn more on how Android application are made and their databases are stored. We wanted to use our creativity and knowledge to give back to the society, especially our university. Since there was an ongoing problem with many different items being lost or being found in the campus, we were motivated to build an application in this direction. 
<br/><br/>

## **SOFTWARE DESIGN**
### **SIGN IN PAGE**
When you first click Foundtastic, you will be greeted by a sign-in page. If you already have an account you can simply login to proceed. If you don’t, fret not, you can click on the button located on the top right corner to Sign Up and create a new account. We also have an email verification system to make sure only genuine people sign up.

<img align="center" alt="logIn" width="25%" src="https://github.com/Rain1213/Foundtastic/blob/master/LogInPage.png?raw=true" />

<img align="center" alt="signIn" width="25%" src="https://github.com/Rain1213/Foundtastic/blob/master/SignUpPage.png?raw=true" />
<br/><br/>

### **HOME PAGE**
You find your first choice, report a found item or a lost item. On the top right corner, you will see a “3 dot” menu button that leads you to a summery view by choosing the “My Account” option or safely sign-out using the second “Signout” button.

<center><img align="center" alt="homePage" width="25%" src="https://github.com/Rain1213/Foundtastic/blob/master/MainPage.png?raw=true" /></center>
<br/><br/>

### **SLIDER VIEW**
Already to your second choice. In this Slider page you will be presented with generalized options like Audio Devices, Books, Mobile Phones etc. A total of 8 common items would help the user easily report the item. If the item doesn’t match those 8 options, we also have a choice for “OTHERS”, to help locate items that are not among the presented ones.

<center><img align="center" alt="sliderView" width="100%" src="https://github.com/Rain1213/Foundtastic/blob/master/Slider.png?raw=true" /></center>
<br/><br/>

### **PAGE TO ENTER DETAILS**
As the title suggests, when you have selected the item from the slider page, you will be asked to enter the details regarding the item.  For Example, if you have chosen watch, you will asked to enter if the item you are reporting is an analogue/digital watch, what is the colour. Fill in the correct details and click on SUBMIT. Your values have been successfully registered in the firebase database. Now all you have to do is wait until the other party fills in the correct details and you get a match.

<center><img align="center" alt="detailPage" width="25%" src="https://github.com/Rain1213/Foundtastic/blob/master/DetailEntryPage.png?raw=true" /></center>
<br/><br/>

### **SUMMARY VIEW**
This page houses all the reports you made till now. It will display the items you have reported lost or found and that too if it has been match to some other person. Also if a match does occur, you will get a notification. This means that you don’t have to keep regularly checking “MY ACCOUNT” to see if there is a match or not.

<center><img align="center" alt="summaryView" width="25%" src="https://github.com/Rain1213/Foundtastic/blob/master/account.JPG?raw=true" /></center>
<br/><br/>
