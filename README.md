Problem Situation:

I have static log files with me and I would like to input the realtime log time to logstash.
The question is how to simulate the static log file as realtime log time. 

Solution:

  Pre-requisites: The log file should contain time stamp
  
  The input line file will be scanned line by line. Time stamp is extracted and further time in hh:mm:ss format without date is converted to long and compare with sys time. If matches the log line will send to new file(the file input for logstash). If time don't match, wait for 1 sec and retry.
  
  Version: Draft/Initial version
