package cc.trity.model.entities;

import java.io.Serializable;

public class AlarmMsg implements Serializable {
	private String label;//具体标签
	private String time;//时间的格式
	private String week;//用与显示星期
	private long times;//转化成微妙的具体时间
	private boolean isOpen;//是否关闭
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
