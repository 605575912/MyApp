package com.auto.launcher;

import com.auto.launcher.Bean;

interface IShareService {

	Map getBean();

	void addBean(in Bean bean);
	void registerServiceReceiver();

	void unregisterServiceReceiver();
}
