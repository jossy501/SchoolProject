/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etz.loanCalculator.action;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.etz.loanCalculator.dto.File;



/**
 * @author tony.ezeanya
 *
 */
@Scope(ScopeType.SESSION)
@Name("fileUploadBean")
public class FileUploadBean{
    
	
    private ArrayList<File> files = new ArrayList<File>();
    private int uploadsAvailable = 5;
    private boolean autoUpload = false;
    private boolean useFlash = false;
    String fileSavedDir;
    private byte[] byteupload;
    
   
    
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
    }
    public void listener(UploadEvent event) throws Exception
    {   
    	
        UploadItem item = event.getUploadItem();
        File file = new File();
        file.setData(item.getData());
        file.setLength(item.getData().length);
        file.setName(item.getFileName());
        
        byteupload = item.getData();
        
        //FacesContext context = FacesContext.getCurrentInstance();
        //ServletContext sc = (ServletContext) context.getExternalContext().getContext();
		
        //String c = sc.getRealPath("") + "\\temp";
        //System.out.println(c + " first");
        
        /*java.io.File f = new java.io.File(c);
        if (!f.exists()) 
        {
            f.mkdirs();
        }
        
        java.io.File desfile = new java.io.File(c.toString() + "\\" + new java.io.File(file.getName()).getName());*/
        
        
        /*int tmpIndex = c.lastIndexOf(java.io.File.separator + "tmp" + java.io.File.separator);
        System.out.println("tmpIndex " + tmpIndex);
        if (tmpIndex > 0) 
        {
            c = c.substring(0, tmpIndex) + java.io.File.separator + "PINUploadedFile";
        }
        java.io.File f = new java.io.File(c);
        if (!f.exists()) {
            f.mkdirs();
        }

        java.io.File desfile = new java.io.File(c.toString() + "\\" + new java.io.File(file.getName()).getName());*/
        
        
        /*fileSavedDir = desfile.toString();
        
        System.out.println(c + " c ");
        System.out.println(desfile + " desfile ");
        
        file.setFileSavedDir(desfile.toString());
        OutputStream out = new FileOutputStream(desfile);
        out.write(item.getData());
        out.close();
        files.add(file);

        uploadsAvailable--;*/
    }  
      
    public String clearUploadData() {
        files.clear();
        setUploadsAvailable(5);
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



}
