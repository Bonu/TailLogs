package com.bj.logUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogTailer {

	public static void main(String args[]) {
		
//	String fileName = "ssl_request_log.2014-04-06";
	String fileName = "ssl_request_log_ac";
//	String fileName = "test.log";
	String dir = "D:/Logtailer";
	
	Path path = Paths.get(dir, fileName);
	Charset charset = Charset.forName("UTF-8");
	String line = "";

	Path pathou = Paths.get(dir, fileName+".tail");
	
	if(!Files.exists(pathou, LinkOption.NOFOLLOW_LINKS)){
				
	}
	
	try (BufferedReader reader = Files.newBufferedReader(path , charset)) {
	  while ((line = reader.readLine()) != null ) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/YYYY:hh:mm:ss");

		String datePattern ="[0-3]\\d/[a-zA-z]{3}/[1-2]\\d{3}[:][0-2]\\d[:][0-5]\\d[:][0-5]\\d";
		
		Pattern pattern = Pattern.compile(datePattern);
		
		 Matcher matchDate = pattern.matcher(line);  
	       if(matchDate.find()) {
	    	   Date lineDate = null;
	    	try {
				lineDate = sdf.parse(matchDate.group());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    	
	    	long logTime = lineDate.getTime();
	    	Date sysDate = new Date((System.currentTimeMillis()));
	    	Calendar cal = Calendar.getInstance();
	    	long sysTime = sysDate.getTime();
	    	/* if it is greater than now 
				then
					write the line to another file
					move to next line
				else
					wait until the time to reach given in that line
					if time reach
						write the line to another file
						move to next line
					else
						wait until the time to reach given in that line
				
	    	 */
//	    	System.out.println("Line Date:"+lineDate.toString());
//	    	System.out.println("Sys  Date:"+sysDate.toString());
	    	
	    	long logTimeInSec = (lineDate.getHours()*60*60)+(lineDate.getMinutes()*60)+(lineDate.getSeconds());
	    	
	    	long sysTimeInSec = (sysDate.getHours()*60*60)+(sysDate.getMinutes()*60)+(sysDate.getSeconds());
	    	
	    	if(logTimeInSec < sysTimeInSec){
	    		
	    		try (BufferedWriter out = new BufferedWriter(
	    				Files.newBufferedWriter
	    					(pathou,Charset.defaultCharset(),StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
	    			
	    			out.append(line);
	    			out.append('\n');
	    			
	    		}catch (IOException x) {
	    		    System.err.println(x);
	    		}	    		
	    			    		
	    	}else{	    		
	    		long sysTimeInSec2 = 0;	    		
	    		do{
		    		try {
						Thread.sleep(1000);
						Date sysDate2 = new Date((System.currentTimeMillis()));
			    		sysTimeInSec2 = (sysDate2.getHours()*60*60)+(sysDate2.getMinutes()*60)+(sysDate2.getSeconds());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}while(logTimeInSec > sysTimeInSec2);
	    			try (BufferedWriter out = new BufferedWriter(
		    				Files.newBufferedWriter
		    					(pathou,Charset.defaultCharset(),StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
		    			
		    			out.append(line);
		    			out.append('\n');
		    		}catch (IOException x) {
		    		    System.err.println(x);
		    		}    			    	
	    		}
		   }
	  }
	} catch (IOException e) {
	    System.err.println(e);
	}

	}
}
