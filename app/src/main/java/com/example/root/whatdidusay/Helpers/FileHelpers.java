package com.example.root.whatdidusay.Helpers;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by dotechws16 on 6/1/16.
 */
public class FileHelpers {

    private final String groups = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
            + ".What_did_you_say"+File.separator+"Groups";
    private final String groupsDefault = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
            + ".What_did_you_say"+File.separator+"Groups"+File.separator+"Default";

    public FileHelpers() {
        File dirFile = new File(groups);
        if (!dirFile.exists()){
            dirFile.mkdirs();
        }
        File defaultDirFile = new File(groupsDefault);
        if (!defaultDirFile.exists()){
            defaultDirFile.mkdirs();
        }
    }

    public void makeNewFolder(String folderName){

        File dirFolder = new File(groups+File.separator+folderName);
        if (!dirFolder.exists()){
            dirFolder.mkdirs();
        }
    }

    public void deleteFolder(String folderName){

        File dirFolder = new File(groups+File.separator+folderName);
        if (dirFolder.exists()) {
            dirFolder.delete();

        }

    }

    public File[] fetchAllFolders(){

        File directory = new File(groups);

        File[] fList = directory.listFiles();

        return fList;
    }

}
