package cc.trity.model.entities;

public class AlarmMsg {
	private String label;
	private String time;
	private String week;//用与显示日期
	private long times;
	private boolean isOpen;
	private int [] weeks;//存储星期,用于匹配闹铃
	public int[] getWeeks() {
		return weeks;
	}
	public void setWeeks(int[] weeks) {
		this.weeks = weeks;
	}
	public long getTimes() {
		return times;
	}
	public void setTimes(long times) {
		this.times = times;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	//得到唯一的ID
	public int getId(){
		return (int)getTimes()/1000/60;
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
}
