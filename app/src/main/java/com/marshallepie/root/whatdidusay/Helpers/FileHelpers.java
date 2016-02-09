package com.marshallepie.root.whatdidusay.Helpers;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by dotechws16 on 6/1/16.
 */
public class FileHelpers {


    private String groups = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
            + ".What_did_you_say" + File.separator + "Groups";
    private String groupsDefault = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
            + ".What_did_you_say" + File.separator + "Groups" + File.separator + "Default";


    /*public FileHelpers(Context context) {

        groups = context.getFilesDir() + File.separator
                + ".What_did_you_say"+File.separator+"Groups";

        groupsDefault = context.getFilesDir() + File.separator
                + ".What_did_you_say"+File.separator+"Groups"+File.separator+"Default";

        File dirFile = new File(groups);
        if (!dirFile.exists()){
            dirFile.mkdirs();
        }
        File defaultDirFile = new File(groupsDefault);
        if (!defaultDirFile.exists()){
            defaultDirFile.mkdirs();
        }
    }*/

    public FileHelpers() {

        File dirFile = new File(groups);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File defaultDirFile = new File(groupsDefault);
        if (!defaultDirFile.exists()) {
            defaultDirFile.mkdirs();
        }
    }

    public void makeNewFolder(String folderName) {

        File dirFolder = new File(groups + File.separator + folderName);
        if (!dirFolder.exists()) {
            dirFolder.mkdirs();
        }
    }

    public void deleteFolder(String folderName) {

        File dirFolder = new File(groups + File.separator + folderName);
        if (dirFolder.exists()) {
            dirFolder.delete();

        }

    }

    public void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }


    public File[] fetchAllFolders() {

        File directory = new File(groups);

        File[] fList = directory.listFiles();

        return fList;
    }

}
