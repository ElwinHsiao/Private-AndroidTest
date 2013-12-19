package com.example.androidtest;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.jheader.App1Header;
import net.sourceforge.jheader.JpegHeaders;
import net.sourceforge.jheader.TagValue;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class RepairPhotoTime {
	
	private static final String TAG = "RepairPhotoTime";
	private File mFile;
	
	RepairPhotoTime() {		// for debug
		
	}
	
	public RepairPhotoTime(File file) {
		mFile = file;
	}
	
	public RepairPhotoTime(String filePath) {
		mFile = new File(filePath);
	}
	
	public boolean needToRepair(File file) throws IOException {
		// evaluate the GPS date and time stamp
		if (evaluteGpsDateTime(file)) {
//			Log.i(TAG, "evalute GPS date stamp passed!");
			return false;
		}
		
		// evaluate the Exif DateTimeOriginal
		if (evaluteDateTimeOriginal(file)) {
//			Log.i(TAG, "evalute Exif DateTimeOriginal passed!");
			return false;
		}
				
		return true;
	}
	
	public void repairDateTime(File file) throws IOException, ParseException {
		Calendar time = getCaputureTimeByFileName(file.getPath());
		if (time == null) {
			throw new ParseException("miss match file parttern, can not repair", 0);
		}
		
		if (!evaluteDateTime(file, time.getTime())) {
			Log.i(TAG, "in repairDateTime: " + file.getName() + " need repair");
			long modifiedTime = getFileModifiedTime(file.getPath());
			
//			setDateTime(time.getTime());
			long modifiedTime2 = getFileModifiedTime(file.getPath());
			if (modifiedTime2 != modifiedTime) {
				Log.i(TAG, "restore file modified time");
				setFileModifiedTime(file.getPath(), modifiedTime);
			}
		}
	}
	
	private void setDateTime(File file, Date time) throws IOException {
		DateFormat df = new SimpleDateFormat(DATETIME_FORMAT);
		String dateTimeString = df.format(time);
		
		ExifInterface exif = new ExifInterface(file.getPath());
		exif.setAttribute(ExifInterface.TAG_DATETIME, dateTimeString);
		exif.saveAttributes();
	}

	private boolean evaluteDateTime(File file, Date time) throws IOException {
		ExifInterface exif = new ExifInterface(file.getPath());
		
		String item = exif.getAttribute(ExifInterface.TAG_DATETIME);
		if (item == null || item.equals("")) {
			return false;
		}
		
		Calendar dateTime = parseDateTime(item);
		if (!time.equals(dateTime.getTime())) {
			long delta = dateTime.getTimeInMillis() - time.getTime();
			if (delta > 1000*60* 10) {	// 60 minute
				Log.w(TAG, "in evaluteDateTime: no equal: file=" + file.getName() + " TAG_DATETIME=" + item + " delta=" + delta);
				return false;
			}			
		}
		
		return true;
	}
	
	private static final String DATETIME_FORMAT = "yyyy:MM:dd HH:mm:ss";
	private Calendar parseDateTime(String dateTimeString) {
		DateFormat df = new SimpleDateFormat(DATETIME_FORMAT);
		try {
			df.parse(dateTimeString);
		} catch (ParseException e) {
			return null;
		}
		
		return df.getCalendar();
	}

	private boolean evaluteGpsDateTime(File file) throws IOException {
		ExifInterface exif = null;
		exif = new ExifInterface(file.getPath());
		
		String item = exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
		if (item == null || item.equals("")) {
			return false;
		}
		Log.d(TAG, file.getName() + " TAG_GPS_DATESTAMP=" + item);
		
		item = exif.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
		if (item == null || item.equals("")) {
			return false;
		}
		Log.d(TAG, file.getName() + " TAG_GPS_TIMESTAMP=" + item);
		
		return true;
	}

	private boolean evaluteDateTimeOriginal(File file) throws IOException {
//		Metadata metadata = null;
//		try {
//			metadata = JpegMetadataReader.readMetadata(file);
//		} catch (JpegProcessingException e) {
//			System.out.println("in evaluteDateTimeOriginal: metadata == null" + e);
//			return false;
//		}
//
//		Directory directory = metadata.getDirectory(ExifSubIFDDirectory.class);	//ExifIFD0Directory、ExifInteropDirectory、ExifSubIFDDirectory、ExifThumbnailDirectory
//		if (directory == null) {
//			System.out.println("in evaluteDateTimeOriginal: ExifSubIFDDirectory == null");
//			return false;
//		}
//		
//		Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
//		if (date == null) {
//			System.out.println("in evaluteDateTimeOriginal: date == null");
//			return false;
//		}
		
		String filePath = file.getPath();
		JpegHeaders jpegHeaders;
		try {
			jpegHeaders = new JpegHeaders(filePath);
		} catch (Exception e) {
			Log.d(TAG, file.getName() + " " + e);
			return false;
		} 
		App1Header exifHeader = jpegHeaders.getApp1Header();
		if (exifHeader == null) {
			Log.d(TAG, "in evaluteDateTimeOriginal: exifHeader == null");
			return false;
		}
		TagValue value = exifHeader.getValue(net.sourceforge.jheader.App1Header.Tag.DATETIMEORIGINAL);
		if (value == null) {
			Log.d(TAG, "in evaluteDateTimeOriginal: value == null");
			return false;
		}
		Log.d(TAG, file.getName() + " DATETIMEORIGINAL=" + value);
		
		return true;
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
//			Log.i(TAG, "in getCaputureTimeByFileName: : [" + filePath + "] matched Miui Pattern");
			return cal;
		}

		cal = tryC360Pattern(filePath);
		if (cal != null) {
//			Log.i(TAG, "in getCaputureTimeByFileName: [" + filePath + "] matched Camera360 Pattern");
			return cal;
		}
		
		cal = tryCommonPattern(filePath);
		if (cal != null) {
//			Log.i(TAG, "in getCaputureTimeByFileName: [" + filePath + "] matched Common Pattern");
			return cal;
		}
		
		Log.e(TAG, "in getCaputureTimeByFileName: [" + filePath + "] have not matched any pattern!");
		return null;
	}
	
	private Calendar tryMiuiPattern(String fileName) {
		Pattern pattern = Pattern.compile("IMG_([0-9]{8})_([0-9]{6})");		// example: IMG_20130210_161320*.jpg
		Matcher matcher = pattern.matcher(fileName);
		if (matcher.find()) {
			String dateTime = matcher.group(1) + " " + matcher.group(2);
//			Log.d(TAG, "in tryMiuiPattern: group=" + matcher.group() + " dateTime=" + dateTime);
			
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
	
	private Calendar tryC360Pattern(String fileName) {
		Pattern pattern = Pattern.compile("C360_([0-9-]{19})");		// example: C360_2013-02-10-16-13-49*.jpg
		Matcher matcher = pattern.matcher(fileName);
		if (matcher.find()) {
			String dateTime = matcher.group(1);
//			Log.d(TAG, "in tryC360Pattern: group=" + matcher.group() + " dateTime=" + dateTime);
			
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
	
	private Calendar tryCommonPattern(String fileName) {
		String fileNameDig = fileName.replaceAll("[^0-9]", "");
		//Log.d(TAG, "in tryCommonPattern: fileName=" + fileName + " fileNameDig=" + fileNameDig);
		
		Pattern pattern = Pattern.compile("^[0-9]{14}");		// example: 201302101613*.jpg
		Matcher matcher = pattern.matcher(fileNameDig);
		if (matcher.find()) {
			String dateTime = matcher.group();
			Log.d(TAG, "in tryCommonPattern: file=" + fileName + " fileNameDig=" + fileNameDig + " group=" + matcher.group());
			
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
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
		
//		filePath = file.getName();
//		Calendar cal = getCaputureTimeByFileName(filePath);
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		System.out.println("DEBUG-MODE: " + df.format(cal.getTime()));
//		
//		file = new File(filePath);
//		filePath = file.getName();
//		filePath = "D:\\My Documents\\My Pictures\\testtime\\C360_2013-02-10-16-13-49.jpg";
//		cal = getCaputureTimeByFileName(filePath);
//		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		System.out.println("DEBUG-MODE: " + df.format(cal.getTime()));
		
		// test needToRepair
		System.out.println("DEBUG-MODE: test needToRepair start >>>>>>");
		String baseName = "2012-09-30_13-14-43_HDR";
		filePath = Environment.getExternalStorageDirectory() + "/amyfile_elwin/Camera/" + baseName + ".jpg";	//	Camera/
		file = new File(filePath);
		
		File fileDir = new File(Environment.getExternalStorageDirectory() + "/amyfile_elwin/Camera/");
		int count = 0;
		File[] files = fileDir.listFiles();
		
		for (int i = 0; i < files.length; ++i) {
			file = files[i];
			try {
//				boolean result = needToRepair(file);
				repairDateTime(file);
//				Log.d(TAG, "file(" + i + ")=" + file.getName() + " needToRepair=" + result);
//				if (result) {
//					++count;
//					Log.d(TAG, "need repair: " + file.getName() + " c=" + count);
//				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		System.out.println("DEBUG-MODE: test needToRepair end <<<<<<");
		
	}
}
