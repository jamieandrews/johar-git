/**
 * This class generates IDF test files and validates them, and also writes the errors (that occurred during validation)
 * into log files. Each test file has a corresponding log file; for example, testFile1.idf will have a log file named testFile1.log
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

import johar.idf.*;

public class TCGenerator {
    private String ROOT_PATH;
    private String FILE_PATH;
    private String FILE_PATH2;
    private String FILE_PATH3;
    private String FILE_PATH4;
    private PrintWriter writerError = null;
    
    public TCGenerator(String BASE_PATH){   
    	ROOT_PATH = "." + File.separator;
	FILE_PATH = BASE_PATH + "Main IDF" + File.separator;
   	FILE_PATH2 = BASE_PATH + "Valid IDFs" + File.separator;
    	FILE_PATH3 = BASE_PATH + "Invalid IDFs" + File.separator;
	FILE_PATH4 = BASE_PATH + "Invalid IDFs 2" + File.separator;
	cleanupDirectories(FILE_PATH);
	cleanupDirectories(FILE_PATH2);
	cleanupDirectories(FILE_PATH3);
	cleanupDirectories(FILE_PATH4);
    }

    public static void main(String args[]){
        TCGenerator tcGen;
	String basePath = "";
        String idfFile = args[0];
        System.out.println();
        
	if (idfFile != null) {
	    if (idfFile.contains("/")){
		basePath = "."+File.separator+idfFile.substring(0,idfFile.lastIndexOf("/")+1);
	    }	    
	    tcGen = new TCGenerator(basePath);	    
	    System.out.println("Generating and Validating IDF Test files...");
            tcGen.generateMainFile(idfFile);
            tcGen.generateValidFiles(idfFile);
            tcGen.generateInvalidFiles(idfFile);
	    tcGen.generateInvalidFiles2(idfFile);
            System.out.println("Done.\n");
        }
    }
    
    public void generateMainFile(String idfFilePath){
        try {
            File newFile = new File(ROOT_PATH+idfFilePath);
            String fileName = newFile.getName();
            String newIdfFile = "";
            String actionTaken = "Removed all Forbidden lines";

            BufferedReader idfBuffer = new BufferedReader(new FileReader(ROOT_PATH+idfFilePath));
            PrintWriter writer = new PrintWriter(new File(FILE_PATH+fileName.substring(0, fileName.indexOf(".idf")) +"_Main.idf"));
            String currentLine = idfBuffer.readLine();
            int index = -1;
		
            while (currentLine != null){
                if (currentLine.contains("//Forbidden")){}
		else{
		    if (currentLine.contains("//")){
                        index = currentLine.indexOf("//");
                        writer.append(currentLine.substring(0, index));
                        writer.append("\n");
                    }
                    else {
			writer.append(currentLine);
                        writer.append("\n");
                    }
		}		  
                currentLine = idfBuffer.readLine();
            }               
	    
            writer.close();         
            idfBuffer.close(); 

            newIdfFile = FILE_PATH+fileName.substring(0, fileName.indexOf(".idf"))+"_Main.idf";
	    validateIdf(newIdfFile, actionTaken);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void generateValidFiles(String idfFilePath){
        try {
            File newFile = new File(ROOT_PATH+idfFilePath);
            String fileName = newFile.getName();
            String newIdfFile = "";
	    String actionTaken = "";
            
            BufferedReader idfBuffer = new BufferedReader(new FileReader(ROOT_PATH+idfFilePath));
            BufferedReader idfBuffer2;
            PrintWriter writer;
            String currentLine = idfBuffer.readLine();
            String currentLine2 = "";
            int index = -1;
            int counter = 1;

            while (currentLine != null){                
                idfBuffer2 = new BufferedReader(new FileReader(ROOT_PATH+idfFilePath));
                currentLine2 = idfBuffer2.readLine();
                if (currentLine.contains("//Optional")){
                    writer = new PrintWriter(new File(FILE_PATH2+fileName.substring(0, fileName.indexOf(".idf")) +"_Valid_" +counter+ ".idf"));
                    while (currentLine2 != null) {
                        if (!(currentLine.equals(currentLine2))){   
                            if (currentLine2.contains("//Forbidden")){}
			    else if (currentLine2.contains("//")){
                                index = currentLine2.indexOf("//");
                                writer.append(currentLine2.substring(0, index));
                                writer.append("\n");
                            }
                            else {
				writer.append(currentLine2);
		                writer.append("\n");
                            }
                        }
			else{
			     actionTaken = "Removed the Optional line \"" + currentLine.substring(0,currentLine.indexOf("//")).trim() + "\" from the IDF";
			}
                        currentLine2 = idfBuffer2.readLine();
                    }
                    writer.close();

                    newIdfFile = FILE_PATH2+fileName.substring(0, fileName.indexOf(".idf")) +"_Valid_" +counter+ ".idf";
                    validateIdf(newIdfFile, actionTaken);
                    
                    counter++;
                }
                currentLine = idfBuffer.readLine();
                idfBuffer2.close();
            }                        
            idfBuffer.close();        
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void generateInvalidFiles(String idfFilePath){
        try {
            File newFile = new File(ROOT_PATH+idfFilePath);
            String fileName = newFile.getName();
            String newIdfFile = "";
	    String actionTaken = "";
            
            BufferedReader idfBuffer = new BufferedReader(new FileReader(ROOT_PATH+idfFilePath));
            BufferedReader idfBuffer2;
            PrintWriter writer;
            String currentLine = idfBuffer.readLine();
            String currentLine2 = "";
            int index = -1;
            int counter = 1;

            while (currentLine != null){                
                idfBuffer2 = new BufferedReader(new FileReader(ROOT_PATH+idfFilePath));
                currentLine2 = idfBuffer2.readLine();
                if (currentLine.contains("//Required")){  
                    writer = new PrintWriter(new File(FILE_PATH3+fileName.substring(0, fileName.indexOf(".idf")) +"_Invalid_" +counter+ ".idf"));
                    while (currentLine2 != null) {
                        if (!(currentLine.equals(currentLine2))){   
                            if (currentLine2.contains("//Forbidden")){}
			    else if (currentLine2.contains("//")){
                                index = currentLine2.indexOf("//");
                                writer.append(currentLine2.substring(0, index));
                                writer.append("\n");
                            }
                            else {
				writer.append(currentLine2);
		                writer.append("\n");
                            }
                        }
			else{
			     actionTaken = "Removed the Required line \"" + currentLine.substring(0,currentLine.indexOf("//")).trim() + "\" from the IDF";
			}
                        currentLine2 = idfBuffer2.readLine();
                    }
                    writer.close();

                    newIdfFile = FILE_PATH3+fileName.substring(0, fileName.indexOf(".idf")) +"_Invalid_" +counter+ ".idf";
                    validateIdf(newIdfFile, actionTaken);
                    counter++;
                }
                currentLine = idfBuffer.readLine();
                idfBuffer2.close();
            }                        
            idfBuffer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void generateInvalidFiles2(String idfFilePath){
        try {
            File newFile = new File(ROOT_PATH+idfFilePath);
            String fileName = newFile.getName();
            String newIdfFile = "";
            String actionTaken = "";

            BufferedReader idfBuffer = new BufferedReader(new FileReader(ROOT_PATH+idfFilePath));
            BufferedReader idfBuffer2;
            PrintWriter writer;
            String currentLine = idfBuffer.readLine();
            String currentLine2 = "";
            int index = -1;
            int counter = 1;

            while (currentLine != null){                
                idfBuffer2 = new BufferedReader(new FileReader(ROOT_PATH+idfFilePath));
                currentLine2 = idfBuffer2.readLine();
                if (currentLine.contains("//Forbidden")){  
                    writer = new PrintWriter(new File(FILE_PATH4+fileName.substring(0, fileName.indexOf(".idf")) +"_Invalid2_" +counter+ ".idf"));
                    while (currentLine2 != null) {
                        if (!(currentLine.equals(currentLine2))){
			   if (currentLine2.contains("//Forbidden")){}
                           else if (currentLine2.contains("//")){
                                index = currentLine2.indexOf("//");
                                writer.append(currentLine2.substring(0, index));
                                writer.append("\n");
                            }
                            else {
				writer.append(currentLine2);
		                writer.append("\n");
                            }
                        }
			else{
			     index = currentLine2.indexOf("//");
                             writer.append(currentLine2.substring(0, index));
                             writer.append("\n");
		             actionTaken = "Removed all Forbidden lines, except \"" + currentLine.substring(0,currentLine.indexOf("//")).trim() + "\", from the IDF";
			}
                        currentLine2 = idfBuffer2.readLine();
                    }
                    writer.close();

                    newIdfFile = FILE_PATH4+fileName.substring(0, fileName.indexOf(".idf")) +"_Invalid2_" +counter+ ".idf";
                    validateIdf(newIdfFile, actionTaken);
                    counter++;
                }
                currentLine = idfBuffer.readLine();
                idfBuffer2.close();
            }                        
            idfBuffer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void validateIdf(String idfFile, String actionTaken){
    	try{    	
    		boolean status = convertToXML(idfFile, actionTaken);
    		if (status){
    		    Idf idf = Idf.idfFromFile(idfFile.replace(".idf", ".xml"));
    		    String errorMsg = "";
    		    errorMsg = Idf.getErrorMsgs();	//This method must exist in Idf.java
	            writerError.append("\n");
	            writerError.append("*****BEGIN: Errors detected in IDF\n");
	            writerError.append(errorMsg);
		    writerError.append("*****END: Errors detected in IDF\n");	            
    		}            
    	}
    	catch(Exception ex){
    		System.out.println(ex.getMessage());
		System.exit(1);
    	}
    	finally{
    		writerError.close();
    	}
    }
    
    public boolean convertToXML(String idfFile, String actionTaken){
    	Reader reader = null;
    	PrintWriter writerXML = null;
    	try {
		reader = new InputStreamReader(new FileInputStream(new File(idfFile)));
		writerXML = new PrintWriter(new File(idfFile.replace(".idf", ".xml")));
	    	writerError = new PrintWriter(new File(idfFile.replace(".idf", ".log")));
	    	writerError.write("*****BEGIN: Action Taken on the IDF");
	    	writerError.write("\n");
	    	writerError.write(actionTaken);
		writerError.write("\n");
		writerError.write("*****END: Action Taken on the IDF");
		writerError.write("\n\n");
		writerError.write("*****BEGIN: IDF to XML Conversion errors");
	    	writerError.write("\n");
	    	Idf2xml convertToXML = new Idf2xml(reader,writerXML,writerError);
	    	convertToXML.convert();
		writerError.write("*****END: IDF to XML Conversion errors");
		writerError.write("\n");
	    	
	    	return true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;			
		}
		finally{
			try {
				reader.close();
				writerXML.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}			
		}
    	
    }

    public void cleanupDirectories(String path){
	try{
	    File dirPath = new File(path);        
            String dirFiles[]; 
     		
	    if (dirPath.exists()){
            	if (dirPath.isDirectory()){  
                    dirFiles = dirPath.list();  
                    for (String file : dirFiles) {  
                        File dirFile = new File(dirPath, file);   
                        dirFile.delete();  
                    }  
             	}
	    }
	    else{
	    	dirPath.mkdirs();
	    } 
	}
	catch(Exception ex){
	     System.out.println(ex.getMessage());
	     System.exit(1);
	}
    }
}
