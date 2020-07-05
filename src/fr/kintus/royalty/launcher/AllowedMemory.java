package fr.kintus.royalty.launcher;

import java.util.Arrays;
import java.util.List;

public enum AllowedMemory {

	XMX1G("1 Go", "-Xmx1G"),
	XMX2G("2 Go", "-Xmx2G", "-Xms1G"),
	XMX3G("3 Go", "-Xmx3G", "-Xms1G"),
	XMX4G("4 Go", "-Xmx4G", "-Xms2G"),
	XMX5G("5 Go", "-Xmx5G", "-Xms2G"),
	XMX6G("6 Go", "-Xmx6G", "-Xms3G"),
	XMX7G("7 Go", "-Xmx7G", "-Xms3G"),
	XMX8G("8 Go", "-Xmx8G", "-Xms3G"),
	XMX9G("9 Go", "-Xmx9G", "-Xms3G"),
	XMX10G("10 Go", "-Xmx10G", "-Xms4G"),
	XMX12G("12 Go", "-Xmx12G", "-Xms4G"),
	XMX14G("14 Go", "-Xmx14G", "-Xms4G"),
	XMX15G("15 Go", "-Xmx15G", "-Xms4G"),
	XMX16G("16 Go", "-Xmx16G", "-Xms6G"),
	XMX32G("32 Go", "-Xmx32G", "-Xms8G");

	private String name;
	private List<String> vmArgs;

	AllowedMemory(String name, String... vmArgs) {
		this.name = name;
		this.vmArgs = Arrays.asList(vmArgs);
	}

	@Override
	public String toString() {
		return name;
	}

	public List<String> getVmArgs() {
		return vmArgs;
	}
}
