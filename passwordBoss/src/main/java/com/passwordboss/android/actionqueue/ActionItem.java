package com.passwordboss.android.actionqueue;

import java.util.Timer;

public interface ActionItem {
	void doTask();
	Timer getTimer();
}
