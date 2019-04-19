# floodAR #
### Version 1.0 ###
#### Release Notes ####
* Implements bounding box functionality for an example box in downtown Savannah
* Improves verbage on help screen and adds an animated GIF demonstrating controls
* UI swiping and location functionality is improved
#### Known Issues #####
* Storm category slider should be disabled on historical storm view
  * Setting slider to disabled in the code causes the slider to disappear
* Links in card are very difficult to click, likely due to the gesture behavior implemented for the cards
  * In the process of looking for a solution. Changing the links to buttons with a larger click area may solve the issue.
#### Bugs Fixed ####
* Previously the app would crash if certain keys were missing, but it handles these errors more gracefully now
* Some text in the cards was overflowing below the screen, the card height has been increased to accommodate for this
* Autofill of location search was showing up on a transparent background, making it hard to read. This has been fixed.
  
## Install Guide ##
### Pre-requisites ###
* Java JDK 1.8 must be installed
### Libraries ###
* The latest version of Android studio must used to run this project
* The application uses other libraries, but they're included in the build.gradle file and will be downloaded when the project is initialized
### Download Instructions ###
* Download a zip of this repository, or check it out from the new project screen of Android studio
### Installation ###
* Run a gradle build of the application directory to install necessary libraries
### Running ###
* Using Android Studio, either set up a virtual device or connect a physical Android phone with developer mode enabled to your computer
 * Please note your phone/emulator should be capable of running AR applications with ARCore. As such it must be running at least Android SDK 24, and preferably Android SDK 28
* Run the project from the Android Studio build menu and select your phone/emulator as the target
### Troubleshooting ###
* When running the app on a device, be sure to update your Google Play Store. This being out of date can cause some strange and unclear errors.
* If there are errors running the build.gradle, ensure that the correct Android SKDS are installed. There are a number of different versions, so selecting one that is atleast version 24 with Google Play Store and AR core capabilities is important.
