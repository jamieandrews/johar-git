/**
 * This is the class that generates and displays the outcome of the test carried out.
 *
 * @author Oladapo Oyebode
 */

package test.IdfTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.FilenameFilter;
import java.util.List;
import java.util.ArrayList;

import johar.idf.*;

public class TCSummary {
    private String BASE_PATH;   
    private String FILE_PATH;
    private String FILE_PATH2;
    private String FILE_PATH3;
    private PrintWriter writerError = null;
    
    public TCSummary(){   
    	BASE_PATH = "." + File.separator + "test" + File.separator + "IdfTest" + File.separator;
   	FILE_PATH = BASE_PATH + "Valid IDF Files" + File.separator;
    	FILE_PATH2 = BASE_PATH + "Invalid IDF Files" + File.separator;
    }

    public static void main(String args[]){
        TCSummary tcSum;
	String basePath = "";
	int logFilesCountValid = -1;
	int logFilesCountInvalid = -1;        
        
	try{
	    tcSum = new TCSummary();
	    System.out.println("Generating TEST REPORT...\n");
	    System.out.print("***Test Suite 1 (Removal of Optional Attributes):  ");
            logFilesCountValid = tcSum.filesCount(tcSum.FILE_PATH);
	
	    if (logFilesCountValid > 0){		
	    	List<String> filesList = tcSum.getValidFilesSummary(logFilesCountValid);
	    	if (filesList != null){
                     System.out.print(logFilesCountValid - filesList.size() + " of " + logFilesCountValid + " IDF files passed. ");
	    	     if (filesList.size() > 0)
		     	System.out.println("The following IDF file(s) failed: " + filesList.toString());
		     else
		     	System.out.println();
	    	}
	    	else
	 	     System.out.println("Not Available.");
	    }
	    else{
	    	System.out.println("0 of 0 IDF files passed.");
	    }			
		
	    System.out.print("\n***Test Suite 2 (Removal of Required Attributes):  "); 	
	    logFilesCountInvalid = tcSum.filesCount(tcSum.FILE_PATH2); 
	     
            if (logFilesCountInvalid > 0){
            	List<String> filesList2 = tcSum.getInvalidFilesSummary(logFilesCountInvalid);
	    	if (filesList2 != null){
                    System.out.print(logFilesCountInvalid - filesList2.size() + " of " + logFilesCountInvalid + " IDF files passed. ");
		    if (filesList2.size() > 0)
		     	System.out.println("The following IDF file(s) failed: " + filesList2.toString());
		    else
		     	System.out.println();
	        }
	        else
	 	    System.out.println("Not Available.");
	    }
	    else{
	    	System.out.println("0 of 0 IDF files passed.");
	    }
	
	    System.out.println();
	}
	catch (Exception ex){
    	    System.out.println(ex.getMessage());
	}        
    }
    
    public List<String> getValidFilesSummary(int numberOfFiles){
        try {
	    int index = 1;
	    int errorCounter = 0;
	    boolean containsError = false;
            
	    BufferedReader idfBuffer;
	    List<String> outputList = new ArrayList<String>();
            String currentLine;
	    
	    while (index <= numberOfFiles){
		errorCounter = 0;
 		containsError = false;
		idfBuffer = new BufferedReader(new FileReader(FILE_PATH+"TestFile_Valid_"+index+".log"));
                currentLine = idfBuffer.readLine();
                while (currentLine != null){
                   if (currentLine.contains("BEGIN: IDF to XML Conversion")){
                        errorCounter = 0;
 			containsError = false;
                   }
		   else if (currentLine.contains("BEGIN: Errors detected in IDF file")){
                        if (errorCounter > 0){
			     containsError = true;
			     break;
                        }
		   }
                   else if (currentLine.contains("END: IDF to XML Conversion")){
                        if (errorCounter > 0){
			     containsError = true;
			     break;
                        }
		   }
                   else if (currentLine.contains("END: Errors detected in IDF file")){
                        if (errorCounter > 0){
			     containsError = true;
			     break;
                        }
		   }
                   else {
			if (!currentLine.trim().toString().equals("")){			     
                    	     errorCounter = errorCounter + 1;
			}
                   }
                   currentLine = idfBuffer.readLine();
            	}
		
		if (containsError){
		    outputList.add("TestFile_Valid_"+index+".idf");
		}
                idfBuffer.close();		
                index = index + 1;	    
	    }	    
            return outputList;	
        } catch (Exception ex) {
            return null;
        }
    }

    public List<String> getInvalidFilesSummary(int numberOfFiles){
        try {
	    int index = 1;
	    int errorCounter = 0;
	    boolean containsError = false;
            
	    BufferedReader idfBuffer;
	    List<String> outputList = new ArrayList<String>();
            String currentLine;
	    
	    while (index <= numberOfFiles){
		errorCounter = 0;
 		containsError = false;
		idfBuffer = new BufferedReader(new FileReader(FILE_PATH2+"TestFile_Invalid_"+index+".log"));
                currentLine = idfBuffer.readLine();
                while (currentLine != null){
                   if (currentLine.contains("BEGIN: IDF to XML Conversion")){
                        errorCounter = 0;
 			containsError = false;
                   }
		   else if (currentLine.contains("BEGIN: Errors detected in IDF file")){
                        if (errorCounter > 0){
			     containsError = true;
			     break;
                        }
		   }
                   else if (currentLine.contains("END: IDF to XML Conversion")){
                        if (errorCounter > 0){
			     containsError = true;
			     break;
                        }
		   }
                   else if (currentLine.contains("END: Errors detected in IDF file")){
                        if (errorCounter > 0){
			     containsError = true;
			     break;
                        }
		   }
                   else {
			if (!currentLine.trim().toString().equals(""))
                    	     errorCounter = errorCounter + 1;
                   }
                   currentLine = idfBuffer.readLine();
            	}
		
		if (!containsError){
		    outputList.add("TestFile_Invalid_"+index+".idf");
		}
                idfBuffer.close();		
                index = index + 1;	    
	    }	    
            return outputList;	
        } catch (Exception ex) {
            return null;
        }
    }
    
    public int filesCount(String path){
	try{
    	    File directory = new File(path);

    	    File[] files = directory.listFiles(new FilenameFilter() { 
	   	public boolean accept(File folder, String name){ 
		    return name.toLowerCase().endsWith(".log"); 
	   	}
    	    } );

	    return files.length; 
	} catch (Exception ex) {
	    ex.printStackTrace();
            return -1;
        }

    }
}
