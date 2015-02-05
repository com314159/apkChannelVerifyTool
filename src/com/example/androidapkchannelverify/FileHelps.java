package com.example.androidapkchannelverify;

import java.io.File;

import android.annotation.SuppressLint;
import android.os.StatFs;

public class FileHelps {

	public static boolean renameFile(String oldName, String newName) {
		File oldFile = new File(oldName);
		File newFile = new File(newName);
		try {
			oldFile.renameTo(newFile);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean deleteFileOrDir(String fileOrDir) {
		File file = new File(fileOrDir);
		if (!file.isDirectory()) {
			return file.delete();
		} else {
			return delFolder(fileOrDir);
		}
	}

	private static boolean delFolder(String folderPath) {
		boolean flag = false;
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			flag = myFilePath.delete(); // 删除空文件
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	private static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			file.delete();
			return;
		}

		File[] files = file.listFiles();

		for (int i = 0; i < files.length; ++i) {
			File f = files[i];
			if (f.isDirectory()) {
				delFolder(f.getPath());
			} else {
				f.delete();
			}
		}
	}

	public static String getFilePathName(String fileName) {
		int index = fileName.lastIndexOf(".");
		if (index < 0) {
			return fileName;
		} else {
			return fileName.substring(0, index);
		}
	}
	
	public static String getFileExtention(String fileName) {
		if (fileName == null) {
			return null;
		}
		int index = fileName.lastIndexOf(".");
		if (index < 0) {
			return null;
		} else {
			return fileName.substring(index, fileName.length());
		}
	}
	
	public static String getFileNameWithoutExtention(String fileName) {
		String fileNameWithExtention = getFileNameWithExtention(fileName);
		if (fileNameWithExtention != null) {
			int index = fileName.lastIndexOf(".");
			if (index < 0) {
				return fileName;
			} else {
				fileName.substring(0, index);
			}
		}
		return null;
	}
	
	public static String getFileNameWithExtention(String fileName) {
		if (fileName == null) {
			return null;
		}
		
		int index = fileName.lastIndexOf(File.separator);
		
		if (index == -1) {
			return null;
		}
		
		return fileName.substring(index, fileName.length());
	}

	public static long getFileDirSize(File directory) {
	    long length = 0;
	    
	    if(directory == null || directory.listFiles() == null) {
	    	return length;
	    }
	    
	    for (File file : directory.listFiles()) {
	        if (file.isFile())
	            length += file.length();
	        else
	            length += getFileDirSize(file);
	    }
	    return length;
	}
	
	public static boolean isFileExist(String filePath) {
		if (filePath == null) {
			return false;
		}
		return (new File(filePath)).exists();
	}
	
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static long getAvailableBytes(File root) {
		StatFs stat = new StatFs(root.getPath());
		// put a bit of margin (in case creating the file grows the system by a
		// few blocks)

		if (android.os.Build.VERSION.SDK_INT < 18) {
			long availableBlocks = (long) stat.getAvailableBlocks() - 4;

			return stat.getBlockSize() * availableBlocks;
		} else {
			
			long availableBlocks = (long) stat.getAvailableBlocksLong() - 4;

			return stat.getBlockSizeLong() * availableBlocks;
		}
	}

}
