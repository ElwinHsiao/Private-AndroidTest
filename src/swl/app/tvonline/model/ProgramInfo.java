package swl.app.tvonline.model;


public class ProgramInfo {
	private int id;//ID
//	private Channel channel;//频道信息
	private String date;//日期
	private String time;//时间
	private String title;//名称
	private String description;//详细信息
//	private int flag;//标记是否可回看
	private String filmid;
	
	private String start;
	private long timeLong;//视频长度
	
	public ProgramInfo(){
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
//	public Channel getChannel() {
//		return channel;
//	}
//	public void setChannel(Channel channel) {
//		this.channel = channel;
//	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
//	public int getFlag() {
//		return flag;
//	}
//	public void setFlag(int flag) {
//		this.flag = flag;
//	}

	public long getTimeLong() {
		return timeLong;
	}
	public void setTimeLong(long timeLong) {
		this.timeLong = timeLong;
	}
	public String getFilmid() {
		return filmid;
	}
	public void setFilmid(String filmid) {
		this.filmid = filmid;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getUrl() {
		return null;
	}
}
