package com.mcds.app.android.estar.bean;

import java.util.ArrayList;
import java.util.List;

public class PhotoListDate {
	public String time;
	public List<PhotoList> photos;
	
	public PhotoListDate(){
		photos = new ArrayList<PhotoList>();
	}
}
