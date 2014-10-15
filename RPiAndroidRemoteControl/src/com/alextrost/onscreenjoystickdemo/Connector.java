package com.alextrost.onscreenjoystickdemo;

import java.io.IOException;

public interface Connector {
	public String createConnection() throws IOException;
	public String sendData(String data) throws IOException;
	public String closeConnection() throws IOException;
}
