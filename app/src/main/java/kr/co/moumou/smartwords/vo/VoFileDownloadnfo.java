package kr.co.moumou.smartwords.vo;

import java.io.Serializable;

public class VoFileDownloadnfo implements Serializable {

	private static final long serialVersionUID = -2958328377137532506L;
	
	private String url;
	private String name;
	private long createDate;
	private String pCode;
	private long fileSize;
	
	
	
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getpCode() {
		return pCode;
	}
	public void setpCode(String pCode) {
		this.pCode = pCode;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	
}
