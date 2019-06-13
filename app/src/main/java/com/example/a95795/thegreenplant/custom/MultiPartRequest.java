package com.example.a95795.thegreenplant.custom;

import java.io.File;
import java.util.Map;

/**
 * @author xudong
 * @version 2018年10月7日 上午11:04:36
 */
public interface MultiPartRequest {

    public void addFileUpload(String param, File file);
    
    public void addStringUpload(String param, String content);
    
    public Map<String, File> getFileUploads();
    
    public Map<String, String> getStringUploads();
}