package com.github.mwmahlberg.speedy;

import java.util.HashMap;

public class ModelAndView {
	
	HashMap<String,Object> model;
	String view;
	
	public ModelAndView(HashMap<String, Object> model, String view) {
		this.model = model;
		this.view = view;
	}

	public HashMap<String, Object> getModel() {
		return model;
	}

	public void setModel(HashMap<String, Object> model) {
		this.model = model;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

}
