package swl.app.tvonline.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class PlaybackXmlParser {

	private static final String TAG = "PlaybackXmlParser";

	public List<ChannelInfo> parseChannelList(String url) {
		InputStream is = openInputStream(url);
		if (is == null) {
			Log.e(TAG, "get inputStream fail!");
			return null;
		}
		
		XmlPullParser xmlParser = Xml.newPullParser();
		try {
			xmlParser.setInput(is, "utf-8");
		} catch (XmlPullParserException e) {
			Log.e(TAG, "input stream error: " + e);
			return null;
		}

		List<ChannelInfo> list = parseChannelList(xmlParser);
		try {
			if (is != null) is.close();
		} catch (IOException e) {
			Log.w(TAG, "close input stream error");
		}
		
		return list;
	}

	public List<ProgramInfo> parseProgramList(String url) {
		InputStream is = openInputStream(url);
		if (is == null) {
			Log.e(TAG, "get inputStream fail!");
			return null;
		}
		
		XmlPullParser xmlParser = Xml.newPullParser();
		try {
			xmlParser.setInput(is, "utf-8");
		} catch (XmlPullParserException e) {
			Log.e(TAG, "input stream error: " + e);
			return null;
		}
		
		List<ProgramInfo> list = parseProgramList(xmlParser);
		try {
			if (is != null) is.close();
		} catch (IOException e) {
			Log.w(TAG, "close input stream error");
		}

		return list;
	}
	
	private List<ChannelInfo> parseChannelList(XmlPullParser xmlParser) {
		// TODO 
		return null;
	}
	
	private List<ProgramInfo> parseProgramList(XmlPullParser xmlParser) {
		// TODO 
		return null;
	}

	private InputStream openInputStream(String url) {
		try {
			URL mUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
			conn.setConnectTimeout(5);
			conn.setReadTimeout(5);
			InputStream is = conn.getInputStream();
			return is;
//        conn.setInstanceFollowRedirects(false);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
