package com.alextrost.onscreenjoystickdemo;

public enum Server {
	INIT_OK("105"),
	MOVED("205"),
	ERROR("204"),
	CLOSE("305");
	private String value;
	private Server(String v){
		this.value = v;
	}
}
