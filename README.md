# Mini-Anti-virus
Group Project for Software design II for Cape Peninsula University of technology. Creating a mini antivirus software

Mini Antivirus

A small antivirus program that is written in Java. It scans a folder for suspicious files, moves them to a safe "quarantine" folder, allows you to restore or delete them, checks for database updates and generates a scan report.

Problem Statement:

Normal activities like visiting web pages, downloading files, clicking on online ads that look harmless or plugging in external drives such as USB sticks can infect computer users with malware.

The problem becomes more serious when users do not have a simple tool to scan for suspicious files without consuming the computer’s memory and processor (CPU).

The Solution:

A mini antivirus desktop-based application that flags suspicious files when scanning for a folder, and keeps them safely separated with a dedicated quarantine manager so that they can't be accidentally opened. It scans in parallel(multithreading), so that scans remain fast without overloading the computer. It logs actions and prevents running so the problems can be traced later, and further generates a report that summarizes the raw scanned data into a short summary

Its features:

Scans
Checks the files in a folder and flags suspicious ones. In this version, any file ending in .exe is flagged. 
Multithreading
Splits scan work across several threads that run at the same time.
Quarantine
Moves a flagged file into a quarantine folder and remembers where it came from.
Restore
Moves a quarantined file back to its original location.
Delete
Permanently removes a quarantined file.
Updates
Checks whether a newer antivirus database is available and installs it. 
Report
Shows the scan date and how many files were flagged. 
Logging
 Records what the program does while it runs.

How to use the MiniAntiVirus:

Step 1(Have a Java JDK installed):

1. Visit <https://www.oracle.com/java/technologies/downloads/>.
2. Choose the tab that matches your system (Windows, macOS or Linux).
3. Get the installer:
   - Windows: x64 Installer (.exe).
   - macOS: the.dmg for your chip (ARM64 for Apple M1/M2/M3/M4, x64 for Intel Macs).
   Linux: the .deb (Ubuntu/Debian) or .rpm (Fedora/Red Hat) package.
4. Double click on the downloaded file and follow the installer. Just keep clicking Next or Continue until it is done.
5. *Close your terminal, open another terminal.* This is an important step, the new install will not see the old window.
6. Run javac -version once more. You should see a version number

Step 2(Get  the project from  GitHub):

1. Check out the project on GitHub: <https://github.com/Thembalakhe-Ntimbane/Mini-Anti-virus>
2. Click the green Code button, near the top right of the file list.
3. Click Download ZIP You download a file with a name like Mini-Antivirus-main.zip.
4. Locate the ZIP file (usually in your Downloads folder).
5. Pull it out:- Windows: right-click the ZIP, select Extract All..., then click Extract. Don't just open the ZIP and drag out one file. Extract everything.)
   - macOS: double-click the ZIP
   - Linux: right-click and select Extract Here or type unzip Mini-Antivirus-main.zip.
6. Move the extracted folder to a place you can easily find (like Documents).

Step 3(Runnning ):

Option A: Running The GUI

Installing Apache NetBeans


Go to <https://netbeans.apache.org/front/main/download/>
Download the installer for your system and run it. Accept the defaults.
Open NetBeans. If it asks which JDK to use, choose the Oracle JDK from Step 1. If it cannot find one, go to Tools>Java Platforms >Add Platforms> Add Platform, choose Java Standard Edition, and select the folder where the JDK was installed.

Open the project

In NetBeans, click File > Open Project...
Browse to the project folder you downloaded in Step 2.
Select the MiniAntiVirus Folder. NetBeans projects show a small coffe-cup icon.
Click Open Project. The project appears in the Projects panel on the left.

Build the project

Click Run > Clean and Build Project 
2. Look at the output panel at the bottom. Wait until it says BUILD SUCCESSFUL.

Run the GUI

In the Projects Panel, expand Source Packages > miniantivirus
Right-click MinWindow.java and choose Run File 
The MiniAntiVirus window opens. Continue to Create Safe Test Files

Option B: Run from the Command Line

Open a terminal inside the project folder (Inside the folder that contains the src)

Windows: open the project folder in the File Explorer, click the address bar at the top, type cmd , and press Enter. A Command Prompt opens already in that folder.
macOS: Type cd (with a space after it) in Terminal, drag the project folder from Frinder into the Terminal window, and press Enter.
Linux: right-click inside the folder and choose Open in Terminal, or use cd path/to/project

List the files to check if you are in the right place(you should see src in the list)                                                                                                                                                       :
Windows dir
macOs/Linux: ls

Build the program

Enter the following command prompts:

macOs/Linux: 

mkdir out 
javac -d out src\*.java 

windows:

mkdir out 
javac -d out src\*.java

If nothing is printed and you get your prompt back, the build worked. Compiled files are now in the out folder. If you see errors, troubleshoot.

Run the program
enter this command : java -cp out PrototypeDemo

The menu will appear. Continue to Create Safe Test Files using Using the Command-Line Menu. Remember to always run these commands from the project folder (the one containing src).

Create Safe Test Files

If you're using windows, 

Create a folder called test-folder on your Desktop.
2. Open File Explorer, click View > Show > File name extensions so you can see the end of each file name.
3. Open test-folder, right-click an empty space, and choose New > Text Document.
4. Rename New Text Document.txt to sample.exe. Confirm the warning about changing the extension.
5. Create a second file, notes.txt, the same way. It will not be flagged.

If you are using macOs/Linux,
Open a terminal and run:

   mkdir -p ~/Desktop/test-folder
   echo "harmless test file" > ~/Desktop/test-folder/sample.exe
   echo "my notes" > ~/Desktop/test-folder/notes.txt

For the command-line version:

Create a folder named prototype-test inside the project folder and put a copy of sample.exe in it. The command-line menu always scans that folder.

Your computer may warn about the .exe name. That is expected.

Using the GUI:

	Scan a folder
Click Browse and choose your test-folder (or type its path in the box).
2. Click Start Scan.
3. Watch the progress bar. Click Stop Scan to cancel.
4. When it finishes, sample.exe appears in Scan Results. notes.txt does not.

	Quarantine a file
In Scan Results, click the flagged file to select it.
2. Click Quarantine.
3.The file moves out of your test folder into the program's quarantine folder and appears in the Quarantine tab.

	Restore or delete a file
Open the Quarantine tab and click a file.
2. Click Restore to move it back where it came from, or Delete to remove it permanently. Deleting cannot be undone.

	Check for updates

Click Update. The program checks for a newer antivirus database. On the bottom bar there is a bar that shows when the last update was made.
	Generate a report

Click Generate Report. A window shows the scan date and number of flagged files. Click Close when done.

Using the Command-Line Menu

After running java -cp out PrototypeDemo you will see:

===== MINI ANTIVIRUS =====
1. Scan test folder
2. Quarantine test file
3. Check for updates
4. Exit
Enter your choice:

Type a number and press enter. Choose what you want to happen and press enter. if you pressed 1, you should see this :

   Suspicious file found: sample.exe
   Scan completed.
   Files flagged: 1
   Scan report:
   Date: (today's date and time)
   Files flagged: 1

Type 2 and press enter. Use either prototype-test\sample.exe or    prototype-test/sample.exe when asked for a file path. You should see the following:

   Quarantine records saved successfully.
   File moved to quarantine successfully.
   Quarantined file: quarantine/sample.exe

3. Type 3 to check for updates.
4. Type 4 to exit.

How the Code works:

Scanning

The Scanner looks at each regular file directly inside the chosen folder and checks its name. Names ending in .exe count as suspicious. It returns a Report holding the scan date and the number of flagged files. Scanning is multithreaded. The Multithreading class runs scan work on several threads at once (runParallel()) and waits for them all to finish (joinThreads()).

Quarantine

QuarantineMgr handles quarantine:

quarantine moves a file into the quarantine folder.
restore moves it back to where it came from.
delete permanently removes it.

Each file's original location is stored in quarantine/records.txt. The record file is written safely (to a temporary file first, then swapped in), and if saving fails the file is moved back so nothing is lost. QuarantinedFile represents one quarantined file with its path and date.

Updates

UpdateService compares database version numbers to see whether a newer one exists. When downloading a real update, PrinceAntivirusMethods applies safety checks: the address must use HTTPS, only an approved host can be contacted, redirects are rejected, downloads are limited to 1 MB, each entry must be a valid 64-character hexadecimal hash, and the update's digital signature is verified.

Understanding the Results:

Files flagged is the number of files the scanner marked as suspicious.
Flagged does not mean infected. Here it only means the file name ends in .exe.
Quarantine does not destroy a file. It moves it to the quarantine folder, and the program saves its original location in quarantine/records.txt. That is how Restore still works after you close and reopen the program.
A file will not be quarantined if it is a folder, a shortcut (symbolic link), or already in quarantine. The program never overwrites an existing file.

Project Structure

Mini-Antivirus/
├── MiniAntivirus/                 NetBeans project (open this in NetBeans for the GUI)
│   └── src/
│       └── miniantivirus/
│           ├── MainWindow.java    GUI window
│           └── ReportDialog.java  Report window
├── src/                           Core classes (scanner, quarantine, updates, etc.)
├── out/                           Created when you build from the command line
├── prototype-test/                Test folder for the command-line menu (you create this)
└── quarantine/                    Created automatically; holds quarantined files
    └── records.txt                Where each quarantined file came from


Troubleshooting

1. Problem: javac is "not recognized" or "command not found".
 Fix: nstall the JDK (Step 1), then close and reopen your terminal.

2. Problem: javac works in one terminal but not another
Fix: Close all terminals and open a new one after installing.
3. Problem: NetBeans says no JDK was found	
fix: Tools > Java Platforms > Add Platform, then choose the JDK installation folder.
4. Problem: NetBeans build fails with a Java version error	
fix: Right-click the project, choose Properties > Sources, and set Source/Binary Format to match your JDK.

5. Problem : Pressing Run does nothing in NetBeans	
fix: Right-click MainWindow.java and choose Run File, or set the project's Main Class (see A4).
6. Problem: Command line: src\*.java or src/*.java "file not found"	
Fix: You are in the wrong folder. Use cd to move into the folder that contains src (see B1).
7. Problem: Command line: "Could not find or load main class PrototypeDemo"	
Fix: Build first (B2), and run the command from the same folder as out.
8. Problem: Command line: many "cannot find symbol" errors	
Fix: You may have downloaded only some files. Redo Step 2 and get the whole project.
9. Problem: The scan finds nothing	
Fix: Make sure the folder has a file ending in .exe. Only files directly inside the folder are scanned, not subfolders.
10. Problem: Typing in the NetBeans console does nothing	
Fix: Click inside the Output panel first, then type.
Windows or macOS warns about sample.exe	Expected. The file is harmless.







