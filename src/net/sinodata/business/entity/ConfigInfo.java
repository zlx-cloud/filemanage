package net.sinodata.business.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("application")
public class ConfigInfo {

	@Value("${filesize}")
	private Long filesize;

	@Value("${bufferfolder}")
	private String bufferfolder;

	@Value("${targetfolder}")
	private String targetfolder;

	@Value("${gabakfolder}")
	private String gabakfolder;

	@Value("${errorfolder}")
	private String errorfolder;

	@Value("${expire}")
	private String expire;

	@Value("${fileno}")
	private int fileno;

	@Value("${networkareacode}")
	private String networkareacode;

	@Value("${bufferfoldertwofinal}")
	private String bufferfoldertwofinal;

	@Value("${targetfoldertwofinal}")
	private String targetfoldertwofinal;
	
	@Value("${uploaderrorfile}")
	private String uploaderrorfile;

	public String getBufferfolder() {
		return bufferfolder;
	}

	public void setBufferfolder(String bufferfolder) {
		this.bufferfolder = bufferfolder;
	}

	public String getTargetfolder() {
		return targetfolder;
	}

	public void setTargetfolder(String targetfolder) {
		this.targetfolder = targetfolder;
	}

	public Long getFilesize() {
		return filesize;
	}

	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}

	public String getGabakfolder() {
		return gabakfolder;
	}

	public void setGabakfolder(String gabakfolder) {
		this.gabakfolder = gabakfolder;
	}

	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}

	public String getErrorfolder() {
		return errorfolder;
	}

	public void setErrorfolder(String errorfolder) {
		this.errorfolder = errorfolder;
	}

	public int getFileno() {
		return fileno;
	}

	public void setFileno(int fileno) {
		this.fileno = fileno;
	}

	public String getNetworkareacode() {
		return networkareacode;
	}

	public void setNetworkareacode(String networkareacode) {
		this.networkareacode = networkareacode;
	}

	public String getBufferfoldertwofinal() {
		return bufferfoldertwofinal;
	}

	public void setBufferfoldertwofinal(String bufferfoldertwofinal) {
		this.bufferfoldertwofinal = bufferfoldertwofinal;
	}

	public String getTargetfoldertwofinal() {
		return targetfoldertwofinal;
	}

	public void setTargetfoldertwofinal(String targetfoldertwofinal) {
		this.targetfoldertwofinal = targetfoldertwofinal;
	}

	public String getUploaderrorfile() {
		return uploaderrorfile;
	}

	public void setUploaderrorfile(String uploaderrorfile) {
		this.uploaderrorfile = uploaderrorfile;
	}

}