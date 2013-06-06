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
    private String ROOT_PATH;
    private String BASE_PATH;   
    private String FILE_PATH;
    private String FILE_PATH2;
    private String FILE_PATH3;
    private PrintWriter writerError = null;
    
    public TCSummary(String BASE_PATH){   
	ROOT_PATH = "." + File.separator;
   	FILE_PATH = BASE_PATH + "Valid IDFs" + File.separator;
    	FILE_PATH2 = BASE_PATH + "Invalid IDFs" + File.separator;
	FILE_PATH3 = BASE_PATH + "Invalid IDFs 2" + File.separator;
    }

    public static void main(String args[]){
        TCSummary tcSum;
	String basePath = "";
	int logFilesCountValid = -1;
	int logFilesCountInvalid = -1;
	int logFilesCountInvalid2 = -1;    
	String idfFile = args[0];   
	String fileName = ""; 
        
	try{	    
	    if (idfFile != null) {
		    if (idfFile.contains("/")){
			basePath = "."+File.separator+idfFile.substring(0,idfFile.lastIndexOf("/")+1);
		    }
		    tcSum = new TCSummary(basePath);
		    File newFile = new File(tcSum.ROOT_PATH+idfFile);
		    fileName = newFile.getName();

		    System.out.println("\nGenerating TEST REPORT...\n");
		    System.out.print("***Test Suite 1 (Removal of Optional Attributes):  ");
		    logFilesCountValid = tcSum.filesCount(tcSum.FILE_PATH);
	
		    if (logFilesCountValid > 0){		
		    	List<String> filesList = tcSum.getValidFilesSummary(logFilesCountValid, fileName);
		    	if (filesList != null){
		             System.out.print(logFilesCountValid - filesList.size() + " of " + logFilesCountValid + " IDFs passed. ");
		    	     if (filesList.size() > 0)
			     	System.out.println("The following IDF(s) failed: " + filesList.toString());
			     else
			     	System.out.println();
		    	}
		    	else
		 	     System.out.println("Not Available.");
		    }
		    else{
		    	System.out.println("0 of 0 IDFs passed.");
		    }			
		
		    System.out.print("\n***Test Suite 2 (Removal of Required Attributes):  "); 	
		    logFilesCountInvalid = tcSum.filesCount(tcSum.FILE_PATH2); 
		     
		    if (logFilesCountInvalid > 0){
		    	List<String> filesList2 = tcSum.getInvalidFilesSummary(logFilesCountInvalid, fileName);
		    	if (filesList2 != null){
		            System.out.print(logFilesCountInvalid - filesList2.size() + " of " + logFilesCountInvalid + " IDFs passed. ");
			    if (filesList2.size() > 0)
			     	System.out.println("The following IDF(s) failed: " + filesList2.toString());
			    else
			     	System.out.println();
			}
			else
		 	    System.out.println("Not Available.");
		    }
		    else{
		    	System.out.println("0 of 0 IDFs passed.");
		    }
		    
                    System.out.print("\n***Test Suite 3 (Removal of Forbidden Attributes):  "); 	
		    logFilesCountInvalid2 = tcSum.filesCount(tcSum.FILE_PATH3);
		     
		    if (logFilesCountInvalid2 > 0){
		    	List<String> filesList3 = tcSum.getInvalidFilesSummary2(logFilesCountInvalid2, fileName);
		    	if (filesList3 != null){
		            System.out.print(logFilesCountInvalid2 - filesList3.size() + " of " + logFilesCountInvalid2 + " IDFs passed. ");
			    if (filesList3.size() > 0)
			     	System.out.println("The following IDF(s) failed: " + filesList3.toString());
			    else
			     	System.out.println();
			}
			else
		 	    System.out.println("Not Available.");
		    }
		    else{
		    	System.out.println("0 of 0 IDFs passed.");
		    }
	
		    System.out.println();
	    }
	    
	}
	catch (Exception ex){
    	    System.out.println(ex.getMessage());
	}        
    }
    
    public List<String> getValidFilesSummary(int numberOfFiles, String fileName){
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
		idfBuffer = new BufferedReader(new FileReader(FILE_PATH+fileName.substring(0, fileName.indexOf(".idf"))+"_Valid_"+index+".log"));
                currentLine = idfBuffer.readLine();
                while (currentLine != null){
                   if (currentLine.contains("BEGIN: IDF to XML Conversion")){
                        errorCounter = 0;
 			containsError = false;
                   }
		   else if (currentLine.contains("BEGIN: Errors detected in IDF")){
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
                   else if (currentLine.contains("END: Errors detected in IDF")){
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
		    outputList.add(fileName.substring(0, fileName.indexOf(".idf"))+"_Valid_"+index+".idf");
		}
                idfBuffer.close();		
                index = index + 1;	    
	    }	    
            return outputList;	
        } catch (Exception ex) {
	    System.out.println(ex.getMessage());
            return null;
        }
    }

    public List<String> getInvalidFilesSummary(int numberOfFiles, String fileName){
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
		idfBuffer = new BufferedReader(new FileReader(FILE_PATH2+fileName.substring(0, fileName.indexOf(".idf"))+"_Invalid_"+index+".log"));
                currentLine = idfBuffer.readLine();
                while (currentLine != null){
                   if (currentLine.contains("BEGIN: IDF to XML Conversion")){
                        errorCounter = 0;
 			containsError = false;
                   }
		   else if (currentLine.contains("BEGIN: Errors detected in IDF")){
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
                   else if (currentLine.contains("END: Errors detected in IDF")){
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
		    outputList.add(fileName.substring(0, fileName.indexOf(".idf"))+"_Invalid_"+index+".idf");
		}
                idfBuffer.close();		
                index = index + 1;	    
	    }	    
            return outputList;	
        } catch (Exception ex) {
            return null;
        }
    }
    
    public List<String> getInvalidFilesSummary2(int numberOfFiles, String fileName){
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
		idfBuffer = new BufferedReader(new FileReader(FILE_PATH3+fileName.substring(0, fileName.indexOf(".idf"))+"_Invalid2_"+index+".log"));
                currentLine = idfBuffer.readLine();
                while (currentLine != null){
                   if (currentLine.contains("BEGIN: IDF to XML Conversion")){
                        errorCounter = 0;
 			containsError = false;
                   }
		   else if (currentLine.contains("BEGIN: Errors detected in IDF")){
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
                   else if (currentLine.contains("END: Errors detected in IDF")){
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
		    outputList.add(fileName.substring(0, fileName.indexOf(".idf"))+"_Invalid2_"+index+".idf");
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
