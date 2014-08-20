package com.framework.file_handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class FileHandler {
	
	public static void saveDataToFile(Context context, String fileName, int mode, Serializable data) throws FileNotFoundException, IOException{
		ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(fileName, mode));
		oos.writeObject(data);
		oos.close();
	}
	
	public static Object loadDataFromFile(Context context, String fileName) throws StreamCorruptedException, FileNotFoundException, IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(context.openFileInput(fileName));
		Object data = ois.readObject();
		ois.close();
		return data;
	}
	
	public static void saveDataToExternalFile(Context context,String folder , String fileName, int mode, Serializable data){
		File rootPath = new File(Environment.getExternalStorageDirectory(), folder);
    	if(!rootPath.exists()){
    		rootPath.mkdirs();
    	}
    	File file = new File(rootPath, fileName);
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(data);
			oos.close();
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		}
	}
	
	public static Object loadDataFromExternalFile(Context context, String folder, String fileName){
		File rootPath = new File(Environment.getExternalStorageDirectory(), folder);
    	if(!rootPath.exists()){
    		rootPath.mkdirs();
    	}
    	File file = new File(rootPath, fileName);
    	Object data = null;
		try {
			ObjectInputStream oos = new ObjectInputStream(new FileInputStream(file));
			data = oos.readObject();
			oos.close();
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		} catch (ClassNotFoundException e) {
			
		}
		return data;
	}
	
	public static boolean isExternalStorageWritable(){
		String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public static void writeStringToExternalStorage(Context context, String folder ,String fileName, String format, String data){
		File rootPath = new File(Environment.getExternalStorageDirectory(), folder);
    	if(!rootPath.exists()){
    		rootPath.mkdirs();
    	}
    	File file = new File(rootPath, fileName + "." + format);
    	if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
    		Toast.makeText(context, "Tidak bisa menggunakan sdcard", Toast.LENGTH_SHORT).show();
    	}
    	try{
    		FileOutputStream mOutPut = new FileOutputStream(file, false);
    		OutputStreamWriter osw = new OutputStreamWriter(mOutPut);
    		osw.write(data);
    		osw.flush();
    		mOutPut.close();
    		Toast.makeText(context, "simpan data", Toast.LENGTH_LONG).show();
    	}
    	catch (FileNotFoundException e) {
    		Toast.makeText(context, "exception ile", Toast.LENGTH_LONG).show();
    	}
    	catch (IOException e) {
    		Toast.makeText(context, "exception io", Toast.LENGTH_LONG).show();
    	}
	}
	
	public static ArrayList<File> getListFileInFolder(Context context, String folder){
		File rootPath = new File(Environment.getExternalStorageDirectory(), folder);
    	if(!rootPath.exists()){
    		rootPath.mkdirs();
    	}
    	File[] listFile = rootPath.listFiles();
    	ArrayList<File> listAllFile = new ArrayList<File>();
    	for (int i = 0; i < listFile.length; i++) {
			listAllFile.add(listFile[i]);
		}
		return listAllFile;
	}

}
