/**
 * 
 */
package com.etz.loanCalculator.dto;

/**
 * @author tony.ezeanya
 *
 */
public class File {

	private String fileSavedDir;
    private String Name;
    private String mime;
    private long length;
    private byte[] data;
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
        int extDot = name.lastIndexOf('.');
        if(extDot > 0){
            String extension = name.substring(extDot +1);
            if("bmp".equals(extension)){
                mime="image/bmp";
            } else if("jpg".equals(extension)){
                mime="image/jpeg";
            } else if("gif".equals(extension)){
                mime="image/gif";
            } else if("png".equals(extension)){
                mime="image/png";
            }
            else if("xls".equals(extension)){
                mime="application/vnd.ms-excel";
            }
             else if("xlxx".equals(extension)){
                mime="application/vnd.ms-excel";
            }
            else {
                mime = "image/unknown";
            }
        }
    }
    public long getLength() {
        return length;
    }
    public void setLength(long length) {
        this.length = length;
    }
    
    public String getMime(){
        return mime;
    }
	public String getFileSavedDir() {
		return fileSavedDir;
	}
	public void setFileSavedDir(String fileSavedDir) {
		this.fileSavedDir = fileSavedDir;
	}
}
