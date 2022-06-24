# logfile-processor
Log File processor


Log file processor is a spring boot based command line application which takes log file path as input and processes the log events in the file.
During the processing long events (events whose execution time is >4ms) are logged in the logs of application and overall all the events are processed and save into an in memory H2 database.

The saved eventg details can be retrieved by using a rest api call to /events endpoint.


Steps to execute:

1. Run the jar file using the command:
java -jar log-file-processor-1.0.jar

2. After the application is booted up you will get the message like below:
Enter file path (Press enter to kill the process)::

3. Here provide the valid log file path like C:\Users\sohit\Desktop\CS\logfile.txt

4. The code validates the log file in a way that each line should be a vaida json with id, state and timestamp being the mandatory attributes. It also validates whether there is a combination of STARTED and FINISHED events. If validation passes it, processes the file. The long running events (events taking >4ms) are logged in application logs and all the events are then saved to in memory database H2. 

5. The saved events can be visualized by using the Get Rest API call: http://localhost:8085/events

6. After one file processing you can opt to process another file by providing the path again as an input.

7. Press enter on command line to kill the process. 
