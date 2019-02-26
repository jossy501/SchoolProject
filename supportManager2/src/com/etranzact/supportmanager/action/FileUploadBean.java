/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.supportmanager.action;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.etranzact.supportmanager.dto.File;
import com.etranzact.supportmanager.model.ReportModel;
import java.io.*;
import org.apache.commons.io.FileUtils;


/**
 * @author tony.ezeanya
 *
 */
@Scope(ScopeType.SESSION)
@Name("cardfileUploadBean")
public class FileUploadBean{  
    
	
    private ArrayList<File> files = new ArrayList<File>();
    private int uploadsAvailable = 5;
    private boolean autoUpload = false;
    private boolean useFlash = false;
    String fileSavedDir;
    private byte[] byteupload;
    private long fileSize;
    private String title;
    private String author;
    private String descr;
    private String fileName;
    private String filePath;
    
	@In
    FacesMessages facesMessages;
    
   

	public int getSize() {
        if (getFiles().size()>0){
            return getFiles().size();
        }else 
        {
            return 0;
        }
    }

    public FileUploadBean() {
    }

    public void paint(OutputStream stream, Object object) throws IOException {
        stream.write(getFiles().get((Integer)object).getData());
        System.out.print("welcome to file uploaderss");
    }
    public void listener(UploadEvent event) throws Exception
    {   
    	
        UploadItem item = event.getUploadItem();
        File file = new File();
        file.setData(item.getData());
        file.setLength(item.getData().length);
        file.setName(item.getFileName());
        
        byteupload = item.getData();
        
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
		
        String c = sc.getRealPath("") + "\\temp";
        System.out.println(c + " first");
        
        /*java.io.File f = new java.io.File(c);
        if (!f.exists()) 
        {
            f.mkdirs();
        }
        
        java.io.File desfile = new java.io.File(c.toString() + "\\" + new java.io.File(file.getName()).getName());
        */
        
       /* int tmpIndex = c.lastIndexOf(java.io.File.separator + "tmp" + java.io.File.separator);
        System.out.println("tmpIndex " + tmpIndex);
        if (tmpIndex > 0) 
        {
            c = c.substring(0, tmpIndex) + java.io.File.separator + "PINUploadedFile";
        }
        java.io.File f = new java.io.File(c);
        if (!f.exists()) {
            f.mkdirs();
        }

        java.io.File desfile = new java.io.File(c.toString() + "\\" + new java.io.File(file.getName()).getName());
        
        
        fileSavedDir = desfile.toString();
        
        System.out.println(c + " c ");
        System.out.println(desfile + " desfile ");
        
        file.setFileSavedDir(desfile.toString());
        OutputStream out = new FileOutputStream(desfile);
        out.write(item.getData());
        out.close();
        files.add(file);
*/
       // uploadsAvailable--;
    }  
      
    public void listener2(UploadEvent event) throws Exception
    {   
    
    	
        UploadItem item = event.getUploadItem();
        File file = new File();
        file.setData(item.getData());
        file.setLength(item.getData().length);
        
        file.setName(item.getFileName());
        
        byteupload = item.getData();
        
       
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
		
        String c = sc.getRealPath("") + "\\temp";
        System.out.println(c + " first");
        
      /*  java.io.File f = new java.io.File(c);
        if (!f.exists()) 
        {
            f.mkdirs();
            
        }
        
        java.io.File desfile = new java.io.File(c.toString() + "\\" + new java.io.File(file.getName()).getName());*/
        
        
        int tmpIndex = c.lastIndexOf(java.io.File.separator + "tmp" + java.io.File.separator);
        System.out.println("tmpIndex " + tmpIndex);
        if (tmpIndex > 0) 
        {
            c = c.substring(0, tmpIndex) + java.io.File.separator + "PINUploadedFile";
        }
        java.io.File f = new java.io.File(c);
        if (!f.exists()) {
            f.mkdirs();
        }

       java.io.File desfile = new java.io.File(c.toString() + "\\" + new java.io.File(file.getName()).getName());
       
        
   
       fileSavedDir = desfile.toString();
       
     
       
       /* 
        System.out.println(c + " c ");
        String zip = desfile+".zip";
        System.out.println(desfile + " ---- > >  > >> > > > > >> >>>>> >>>           - - -- - desfile ");
        System.out.println(zip + " ---- > >  > >> > > > > >> >>>>> >>>           - - -- - desfile zip ");
        System.out.println("System Path -- > "+fileSavedDir);*/
       
        
        
        //String outFilename = "C:\\Users\\joshua.aruno\\Downloads\\myzip.zip";
        String outFilename =  c.toString()+ "\\" + file.getName()+".zip";
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));

               out.putNextEntry(new ZipEntry(file.getName().toString()));
               out.write(item.getData());
               out.closeEntry();
               // Close the ZIP file
               out.close();
        
     /*   file.setFileSavedDir(desfile.toString());
        OutputStream out = new FileOutputStream(desfile);
        out.write(item.getData());
        out.close();*/
      
        
        long  filesizes =  file.getLength();
        String fileName = file.getName()+".zip";
        
        String fileNames = file.getName();
       
        files.add(file);
       
        int size = getSize();
        setFileSize(filesizes);
        setFileName(fileNames);
   

       System.out.println("AFTER FILE UPLOAD..."+ getSize()+"LONG FILLE....."+filesizes+"KB."+"FileNAME"+fileName);
       System.out.println("File Absolute PAth  "+fileSavedDir);
       System.out.println("File Path  "+getFilePath());

    }  
    
    public void listener3(UploadEvent event) throws Exception
    {   
    
    	
        UploadItem item = event.getUploadItem();
        File file = new File();
        file.setData(item.getData());
        file.setLength(item.getData().length);
        
        file.setName(item.getFileName());
        
        byteupload = item.getData();
        
       
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
		
        String c = sc.getRealPath("") + "\\temp";
        System.out.println(c + " first");
        
      /*  java.io.File f = new java.io.File(c);
        if (!f.exists()) 
        {
            f.mkdirs();
            
        }
        
        java.io.File desfile = new java.io.File(c.toString() + "\\" + new java.io.File(file.getName()).getName());*/
        
        
        int tmpIndex = c.lastIndexOf(java.io.File.separator + "tmp" + java.io.File.separator);
        System.out.println("tmpIndex " + tmpIndex);
        if (tmpIndex > 0) 
        {
            c = c.substring(0, tmpIndex) + java.io.File.separator + "PINUploadedFile";
        }
        java.io.File f = new java.io.File(c);
        if (!f.exists()) {
            f.mkdirs();
        }

       java.io.File desfile = new java.io.File(c.toString() + "\\" + new java.io.File(file.getName()).getName());
       
        
   
       fileSavedDir = desfile.toString();
       
     
       
       /* 
        System.out.println(c + " c ");
        String zip = desfile+".zip";
        System.out.println(desfile + " ---- > >  > >> > > > > >> >>>>> >>>           - - -- - desfile ");
        System.out.println(zip + " ---- > >  > >> > > > > >> >>>>> >>>           - - -- - desfile zip ");
        System.out.println("System Path -- > "+fileSavedDir);*/
       
        
        
        //String outFilename = "C:\\Users\\joshua.aruno\\Downloads\\myzip.zip";
       
       
       // write into a zip file
   /*     String outFilename =  c.toString()+ "\\" + file.getName()+".zip";
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));

               out.putNextEntry(new ZipEntry(file.getName().toString()));
               out.write(item.getData());
               out.closeEntry();
               // Close the ZIP file
               out.close();*/
        
        file.setFileSavedDir(desfile.toString());
        OutputStream out = new FileOutputStream(desfile);
        out.write(item.getData());
        out.close();
      
        
        long  filesizes =  file.getLength();
       // String fileName = file.getName()+".zip";
        
        String fileNames = file.getName();
       
        files.add(file);
       
        int size = getSize();
        setFileSize(filesizes);
        setFileName(fileNames);
   

       System.out.println("AFTER FILE UPLOAD..."+ getSize()+"LONG FILLE....."+filesizes+"KB."+"FileNAME"+fileName);
       System.out.println("File Absolute PAth  "+fileSavedDir);
       System.out.println("File Path  "+getFilePath());

    }  
   
    
    public void createUploadFile()
    {
    	
    	try
    	{
    	
    		ReportModel reportModel = new ReportModel();
    		String fileName = getFileName();  // filename
    		String filePath = getFileSavedDir(); //  filePath
    		String status =   "queued";
    		
    	
    		
    		FacesContext context = FacesContext.getCurrentInstance();
			String uploadBy = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
			
			String code = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
			
	    	String message = reportModel.createUploadFile(fileName,filePath,status,uploadBy,code);
	    		
	    	if(filePath == null && fileName == null)
	    	{
	    		
	    		message = "File is required ";
	    	}
	    	else
	    	{
	    		if(message.equalsIgnoreCase("EXISTED"))
				{
					message = "Record Already Exists";
				}
				else if(message.equalsIgnoreCase("SUCCESS"))
				{
					message = "Records successfully inserted";
					
				}
				else
				{
					message = "Records not inserted";
				}	
	    	
				
	    	}
		
			
	    	facesMessages.add(Severity.INFO, message);
    		
			
    		
    	}
    	catch(Exception ex)
    	{
    	
    		ex.printStackTrace();
    	}
    	
    	
    }
    
    public void SaveFileDetails()
    {
    	
    	
    	try
    	{
	    	ReportModel reportmodel = new ReportModel();
	    	String title = getTitle();
	    	String author = getAuthor();
	    	String description = getDescr();
	    	long fileSize =getFileSize();
	    	String filePath = getFileSavedDir();
	    	String fileName = getFileName();
	    
	    	
	    	System.out.println("FILE Name here ------ > > > "+fileName);
	    	
	    	//String relativePath = "c://Data/Snapshot_20120424_27.JPG";
	    	
	       /* java.io.File f = new java.io.File("c://Data");
	        if (!f.exists()) 
	        {
	            f.mkdirs();
	            
	        }*/

	    	//String relativePath = "/C:/Data/"+fileName;
	    
	   
	    	// writeToFile(relativePath,fileName,getByteupload());
	    	
	    		    	
	    	System.out.println("Title"+title+"Author"+author+"Descr"+description+"File SIZE"+fileSize+"fILENAMEParth"+fileName);
	 
	    	//previous
	    	//String message = reportmodel.SaveFileDetails(title, author, description, fileSize, filePath,fileName);
	    	
	
	    	String message = reportmodel.SaveFileDetails(title, author, description, fileSize, filePath,fileName);
	    		
	    	if(filePath == null && fileName == null)
	    	{
	    		
	    		message = "File is required ";
	    	}
	    	else
	    	{
	    		if(message.equalsIgnoreCase("EXISTED"))
				{
					message = "Record Already Exists";
				}
				else if(message.equalsIgnoreCase("SUCCESS"))
				{
					message = "Records successfully inserted";
				}
				else
				{
					message = "Records not inserted";
				}	
	    	
				
	    	}
		
			
	    	facesMessages.add(Severity.INFO, message);
			setTitle(null);
			setDescr(null);
			setAuthor(null);
			
		
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
		
    	
    	
    }
    
   /* public String writeToFile(String filePath, String filename, byte [] filedata)
    {
    	String resp= "";
    	if(filePath == null || filePath.trim().equals("")) filePath= "c:/data/";
    	if(filename == null  || filename.trim().equals("")) filename= "noname.jpg";
    	
    	
 
			try
		     {
				
				byte[] contentInBytes = filedata;  //getByteupload();
				
				System.out.println(" : contentInBytes :"+contentInBytes.length);
				
				java.io.File file = new java.io.File(filePath+"/"+filename);
				 
				//if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
				
		      FileOutputStream fos = new FileOutputStream(file);
		         
		      
		       * To write byte array to a file, use
		       * void write(byte[] bArray) method of Java FileOutputStream class.
		       *
		       * This method writes given byte array to a file.
		       
		     
		       fos.write(contentInBytes);
		     
		      
		       * Close FileOutputStream using,
		       * void close() method of Java FileOutputStream class.
		       *
		       
		     
		       fos.close();
		     
		     }
		     catch(FileNotFoundException ex)
		     {
			      System.out.println("FileNotFoundException : " + ex);
			      ex.printStackTrace();
		     }
		     catch(IOException ioe)
		     {
		      System.out.println("IOException : " + ioe);
		         ioe.printStackTrace();
		     }
 
			System.out.println(": Done :");
 
	
    	return resp;
    }
     */ 
    public String clearUploadData() {
        files.clear();
        setFileName(null);
        setUploadsAvailable(5);
        System.out.println("Clear borad   ");
        return null;
      
    }
    
    public long getTimeStamp(){
        return System.currentTimeMillis();
    }
    
    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) { 
        this.files = files;
    }

    public int getUploadsAvailable() {
        return uploadsAvailable;
    }

    public void setUploadsAvailable(int uploadsAvailable) {
        this.uploadsAvailable = uploadsAvailable;
    }

    public boolean isAutoUpload() {
        return autoUpload;
    }

    public void setAutoUpload(boolean autoUpload) {
        this.autoUpload = autoUpload;
    }

    public boolean isUseFlash() {
        return useFlash;
    }

    public void setUseFlash(boolean useFlash) {
        this.useFlash = useFlash;
    }

	public String getFileSavedDir() {
		return fileSavedDir;
	}

	public void setFileSavedDir(String fileSavedDir) {
		this.fileSavedDir = fileSavedDir;
	}

	public byte[] getByteupload() {
		return byteupload;
	}

	public void setByteupload(byte[] byteupload) {
		this.byteupload = byteupload;
	}

    public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
		



}
