package com.example.beans;

import android.graphics.Color;

public class SignalManager {
	private double signalLevel;
	private int color = -1;
	private boolean change = true;

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public boolean isChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}

	public double getSignalLevel() {
		return signalLevel;
	}

	public void setSignalLevel(double signalLevel) {
		this.signalLevel = signalLevel;
		int newColor = this.indicateColor(signalLevel);
		if(color != newColor){
			color = newColor;
			change = true;
		}
	}

	public int indicateColor(double i) {
		if (i < -110) {
			return Color.GRAY;
		} else if (i < -100) {
			return Color.GREEN;
		} else if (i < -90) {
			return Color.BLUE;
		} else if (i < -80) {
			return Color.YELLOW;
		} else if (i < -70) {
			return Color.MAGENTA;
		} else if (i < -60) {
			return Color.RED;
		} else {
			return Color.RED;
		}
	}

	public String getStringSignal() {
		return String.valueOf(signalLevel);
	}
}
