package com.example.androidtest;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.jheader.App1Header;
import net.sourceforge.jheader.JpegHeaders;
import net.sourceforge.jheader.TagValue;
import net.sourceforge.jheader.App1Header.Tag;

public class RepairPhotoTime {
	
	public void printExifInfo(File file)  throws Exception {
		String filePath = file.getPath();
		JpegHeaders jpegHeaders = new JpegHeaders(filePath);
		App1Header exifHeader = jpegHeaders.getApp1Header();

		// 遍历显示EXIF
		SortedMap<Tag, TagValue> tags = exifHeader.getTags();
//		Map.Entry<Tag, TagValue> entry = tags.entrySet();
		for (Map.Entry<Tag, TagValue> entry: tags.entrySet()) {
			System.out.println(entry.getKey() + "[" + entry.getKey().name
					+ "]:" + entry.getValue());
		}

		// 修改EXIF的拍照日期
//		exifHeader.setValue(Tag.DATETIMEORIGINAL, getTimeByFileName(filename));
		// 保存
		jpegHeaders.save(true);
	}
	
	public long getFileModifiedTime(String filePath) {
		File f = new File(filePath);  
		return f.lastModified();  
	}
	
	public boolean setFileModifiedTime(String filePath, long time) {
		File f = new File(filePath);
		return f.setLastModified(time);
	}
	
	public Calendar getCaputureTimeByFileName(String filePath) {
		Calendar cal = tryMiuiPattern(filePath);
		if (cal != null) {
			return cal;
		}

		cal = tryC360Pattern(filePath);
		if (cal != null) {
			return cal;
		}
		
		System.err.println("have not matched!");
		return null;
	}
	
	private Calendar tryMiuiPattern(String fileName) {
		Pattern pattern = Pattern.compile("IMG_([0-9]{8})_([0-9]{6})");		// example: IMG_20130210_161320*.jpg
		Matcher matcher = pattern.matcher(fileName);
		if (matcher.find()) {
			String dateTime = matcher.group(1) + " " + matcher.group(2);
			System.out.println("group=" + matcher.group() + " dateTime=" + dateTime);
			
			DateFormat df = new SimpleDateFormat("yyyyMMdd HHmmss");
			try {
				df.parse(dateTime);
			} catch (ParseException e) {
				throw new RuntimeException("parser file name fail, your pattern need to update");
			}
			
			return df.getCalendar();
		}
		
		return null;
	}
	
	private Calendar tryC360Pattern(String fileHame) {
		Pattern pattern = Pattern.compile("C360_([0-9-]{19})");		// example: C360_2013-02-10-16-13-49*.jpg
		Matcher matcher = pattern.matcher(fileHame);
		if (matcher.find()) {
			String dateTime = matcher.group(1);
			System.out.println("group=" + matcher.group() + " dateTime=" + dateTime);
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			try {
				df.parse(dateTime);
			} catch (ParseException e) {
				throw new RuntimeException("parser file name fail, pattern need to update");
			}

			return df.getCalendar();
		}
		
		return null;
	}
	
	
	/*************************************-test-*********************************************/
	private static final boolean DEBUG_MODE = true;
	public void debug() {
		if (!DEBUG_MODE) {
			throw new UnsupportedOperationException("this class is not in debug mode");
		}
		
		String filePath = "D:\\My Documents\\My Pictures\\testtime\\IMG_20130210_161320-new.jpg";
		
		File file = new File(filePath);
		filePath = file.getName();
		Calendar cal = getCaputureTimeByFileName(filePath);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("DEBUG-MODE: " + df.format(cal.getTime()));
		
		file = new File(filePath);
		filePath = file.getName();
		filePath = "D:\\My Documents\\My Pictures\\testtime\\C360_2013-02-10-16-13-49.jpg";
		cal = getCaputureTimeByFileName(filePath);
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("DEBUG-MODE: " + df.format(cal.getTime()));
	}
}
