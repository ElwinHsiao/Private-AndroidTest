package swl.app.tvonline.model;

import java.util.List;

import android.os.AsyncTask;

/**
 * This class 
 * @author Elwin
 *
 */
public class ChannelInfoTask {

	private static final String DEFAULT_CHANNEL_LIST_URL = "http://204.45.127.242:8778/yuanltech/data/record/column.xml";
	private OnChannelListListener mOnChannelListListener;
	private OnProgramListListener mOnProgramListListener;
	private ProgramListTask mProgramListTask;
	
	/**
	 * request the entire channel list, use the default URL.
	 * @param listener
	 */
	public void requestChannelList(OnChannelListListener listener) {
		requestChannelList(DEFAULT_CHANNEL_LIST_URL, listener);
	}
	
	/**
	 * request the entire channel list.
	 * @param url
	 * @param listener
	 */
	public void requestChannelList(String url, OnChannelListListener listener) {
		mOnChannelListListener = listener;
		new ChannelListTask().execute(url);
	}
	
	/**
	 * request the program list for a channel.
	 * @param programListUrl : the URL of the program list XML file.
	 * @param listener
	 */
	public void requestProgramList(String programListUrl, OnProgramListListener listener) {
		if (mProgramListTask != null) {
			mProgramListTask.cancel(true);
		}
		mOnProgramListListener = listener;
		mProgramListTask = new ProgramListTask();
		mProgramListTask.execute(programListUrl);
	}
	
	public interface OnChannelListListener {
		/**
		 * Notice the requested channel list is coming.
		 * @param list : null if some errors have happened. 
		 */
		void onChannelList(List<ChannelInfo> list);
	}
	
	public interface OnProgramListListener {
		
		/**
		 * Notice the requested program list is coming.
		 * @param programListUrl : The original URL.
		 * @param list : null if some errors have happened.
		 */
		void onProgramList(String programListUrl, List<ProgramInfo> list);
	}
	
	PlaybackXmlParser mPlaybackXmlParser;
	private PlaybackXmlParser getXmlParser() {
		if (mPlaybackXmlParser == null) {
			mPlaybackXmlParser = new PlaybackXmlParser();
		}
		
		return mPlaybackXmlParser;
	}
	
	class ChannelListTask extends AsyncTask<String, Integer, List<ChannelInfo>> {

		@Override
		protected List<ChannelInfo> doInBackground(String... params) {
			return getXmlParser().parseChannelList(params[0]);
		}
		
		@Override
		protected void onPostExecute(List<ChannelInfo> result) {
			if (mOnChannelListListener != null) {
				mOnChannelListListener.onChannelList(result);
			}
	    }
	}
	
	class ProgramListTask extends AsyncTask<String, Integer, List<ProgramInfo>> {
		private String url;

		@Override
		protected List<ProgramInfo> doInBackground(String... params) {
			this.url = params[0];
			return getXmlParser().parseProgramList(params[0]);
		}
		
		@Override
		protected void onPostExecute(List<ProgramInfo> result) {
			if (mOnProgramListListener != null) {
				mOnProgramListListener.onProgramList(this.url, result);
			}
	    }
	}
}
