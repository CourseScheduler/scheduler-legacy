Course Scheduler
Open Source

Source is available on request via email to licensing@coursescheduler.io
Redistribution in source and binary forms, with or without modification
is permitted, however, we respectfully request that any redistributions
state that it is an unofficial redistribution. Also, we request that any
modifications be submitted via email along with a detailed report for inclusion
in future releases.
 
 
 Installation Notes:
 	The new deployment method for the Kettering Course Scheduler is to use the Java Web Start runtime. WebStart is installed as part of Java SE 8 and should register .jnlp files to be run by javaws.
 	This allows the user to download a small .jnlp file from the hosting website, which will then be launched by Java Web Start. Java Web Start will download any application files needed
 	and will install desktop and menu shortcuts. It also registers the program's associated file types with the operating system.
 	
 Un-Installation Notes:
 	Java Web Start manages its downloaded applications via the Java Control Panel. Click on the "View" button in the Temporary Internet files section to bring up the Java Cache Viewer.
 	This will allow you to uninstall, reinstall, or completely remove the Scheduler from your computer.

ChangeLog
4.12.9
	-Fixes issue #32, bug preventing course download due to high latency server responses (>30 seconds) by adding a socket connection timeout configuration
	-Added UI option for configuring the socket timeout
	-Increased default timeout from JRE default of 3s to 45s per statistical testing against jweb.kettering.edu
	-Added better error transparency during course data retrieval tto better allow for the user to identify the problem
	-Changed from console logging to a single, non-rolling, overwritten log file for better error reporting and ease of use   
	
4.12.8
	-Fixes issue #29, bug preventing download of course data for Summer 2015 due to invalid credit value check

4.12.7
	-Update dependencies
	-Build process improvement
	-Improved application startup and bootstrapping
	-Added jnlp version to webstart to improve version checking
	-Added j2se href for auto JRE update on platforms that support it
	-Prepare for Java 8 upgrade by bumping jnlp spec & runtime
	-Add Java 7 end of life (Oracle) warning
	-Fixes issue #23, illegal use of Long.max(long, long) in Java 7
	-Updated about window with social media details

4.12.6
	-Fixed issue #14, UI bug where conflict would occupy inaccurate portion of the day
	-Replaced console logging statements with SLF4J logging to the console
	
4.12.5
	-Improved reliability and readability of the analytics code. 
	-Added analytic attributes and improved data type storage for attributes
	
4.12.4
	-New JSoup-based parser with multi-threaded parsing for faster, more reliable parsing.
	-Fixed issue with next Term calculation
	-Fixed issue with Period initialization
	
4.12.3
	-Resolved parsing issue (#11) with the course detail page preventing proper open/closed course detection
	 
4.12.2
	-Updated BannerWeb parsing to accommodate latest changes to website (issue #7)
	-Integrated anonymous data collection and updated privacy policy
	-Updated branding to the new Course Schedule brand assests
	
4.12.1
	-Signed JAR with new code signing SSL certificate (issue #1)
	-Fixed issue that prevented instructor names from being included in course data (issue #2)
	-Fixed issue caused by changes to default enabled HTTPS protocol versions (issue #6)
	-Fixed JNLP issue causing the Scheduler not to start on some machines with JRE 6

4.12 Build 01510
	-New course retrieval process for the updated version of BannerWeb (8.5.1)
	-Can only handle up to 2 meeting times per section
	
4.11 Build 01482
	-Fixed the default URLs for Kettering Undergrade, Grad On-Campus, and Grad Off-Campus (recently changed)

4.11 Build 01479
	-Replaced the ButtonTabComponent class from Sun Microsystems with the LGPL XTabComponent class written by Phil DeMonaco
		with images by Alex Thomson
	-Added the Report Conflicts option as a saveable schedule parameter
	-Fixed bug that prevented the Rate-My-Professor fix file from being loaded improperly from the jar
	-Fixed bug that prevented proper download of the Kettering Courses due to change in HTML format

4.11 Build 01475 Release
	-Added conflict tooltips on the schedule display
	-Added debug flag for terminal conflict output
	-Fixed bug in the "Minimum Number of Courses to Use" scenario which incorrectly calculated schedules
	-Fixed bug where schedule and details are not properly cleared when no schedules are found

4.11 Build 01473
	-Fixed bug in Choose Term window that prevented the "X" from clearing changes and
		closing the window
	-Added schedule conflicts to the schedule list
	-Added schedule conflict display to the display window
	-Added schedule conflict details to the details tab
	-Fixed bug in schedule opening algorithm that prevented schedule from opening under specific circumstances
	-Fixed bug which prevented proper printing on some systems

4.11 Build 01471
	-Fixed a few build algorithm bugs that reduced performance
	-Fixed simple stdout conflict reporting for improved accuracy
	-Added Schedule list flagging and tooltips with schedule descriptions

4.11 Build 01470
	-Fixed bug in schedule opening algorithm that caused excess builds
	-Added stdout simple conflict reporting (convert to gui in a later build)

4.10 Build 01469 Release
	-Added JNLP OS associations for .ssf
	-Added command line arguement handling for *.ssf
	-Added singleton server status
	-Added new activation single instance listener

4.10 Build 01467
	-Added centralized data storing and jar-enclosed resources
	-Added Nimbus UI check for SE 6 update 10+ users 

4.10 Build 01462
	-Converted professor scheduling options to multithreaded model
	-Improved professor scheduling accuracy and reliability

4.10 Build 01457
	-Added professor scheduling options backend (with full optional dependancy building)
	-Added support for saving with specified/excluded sections
	-Added support for saving with section number selections
	-Added master, schedule, and schedules list enumeration
	-Added closed/open course descriptions

4.09 Build 01440
	-Added professor scheduling options (multiple sections of course per schedule) gui only, no backend
	-Added default loading of Rate My Professor fix file from the archive if external file not found
	-Added default loading of images from the archive to reduce install footprint
	-Minor UI improvements

4.09 Build 01435 Release
	-Improved creation of Schedules and increased display speed
	-Added Specify/Exclude section for schedule creation
	-Added Graduate Course Downloads
	-Added view course type on the build schedule tabs
	-Increased schedule build speed by 2-20x (depending on schedule dependency structure)
	-Restructured options menu
	-Removed Beta Mac installer due to issues (will replace with .zip until more work can be done)
	-Numerous minor bug fixes

4.08 Build 01431
	-Fixed potential bug in the Options Frame
	-Fixed bug in schedule opening algorithm
	-Improved installer functions
	-Added Beta Mac installer
	-Added Drag and Drop adding and replacing of courses
	-Improved reliability of schedule opening algorithm
	

4.07 Build 01430
	-Fixed minor bugs with the primary course flagging system
	-Added support for user linking of courses for linked dependencies
	-Fixed minor bugs with button enabling/disabling
	-Added GUI interface for viewing/removing primary courses
	-Added GUI interface for viewing/removing course links
	-Added schedule dependency restoration and advanced schedule calculation
	

4.06 Build 01428
	-Fixed bug in requesting update of database when opening a schedule
	-Fixed bug causing infinite loop when no schedule options found
	-Fixed dialog box that cut off number of schedules being built
	-Added checkbox to turn off auto-refresh on changes to schedule list


4.05 Build 01427
	-Fixed bug in using the Minimum number of courses option when building schedules
	-Added primary courses flag for building schedules
	-Fixed saving of minimum number of courses used
	-Added saving of primary courses flags
	-Added schedule updating after changing minUse value
	-Fixed bug in opening schedules that had courses that were no longer in the course set


4.05 Build 01426
	-Fixed bug in the description of the schedule when selecting a file
	-Minor GUI changes to the file selection window
	

4.04 Build 01425
	-Fixed major bug in the multi-threaded build algorithm
	-Fixed minor gui scaling issues

4.03 Build 01424
	-Added modified schedule indicator
	-Improved Rate-My-Professor download methodology
	-Fixed Bug in opening empty schedules and updating the schedule tab title, as well as the modified indicator
	-Improved File Description in the file choosing windows
	-Fixed View Pane collape/expand issue after re-enabling rate-my-professor ratings
	

4.03 Build 01423
	-Added Schedule -> Delete Schedule menu item
	-Minor UI interaction improvements
	-Fixed minor bug in schedule building
	

4.02 Build 01422
	-Fixed bug in course download multithreading
	-Added schedule build multithreading
	-Improved progress monitoring and canceling
	

4.01 Beta Build 01421
	-Linux installer and post install scripts adjusted, shortcut permissions and /usr/bin/scheduler symlink fixed
	-Minor GUI adjustments to fix a few problems on Linux
	-Improved printing of course detail view
	

4.01 Beta Build 01420
	-Added multi-threaded support during download of Rate-My-Professor ratings
	-Improved algorithm for sending jobs to the printer
	-Minor aesthetic improvements
	-Added closed course and minimum course number autoadjust to saved courses
	-Improved opening saved schedules
	-Added number of open seats to detailed view
	-Added the splash screen to JAR manifest


4.01 Beta Build 01419
	-First Beta build
	-Bug fixes on schedule creation tabs

	
Legal Notices

ButtonTabComponent.java && ButtonTabComponent.class Disclaimer

Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

  - Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.

  - Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
  - Neither the name of Sun Microsystems nor the names of its
    contributors may be used to endorse or promote products derived
    from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.