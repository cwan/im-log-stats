package net.mikaboshi.intra_mart.tools.log_stats.util;

import org.apache.commons.collections.Factory;

public final class SingleIntFactory implements Factory {

	public static SingleIntFactory INSTANCE = new SingleIntFactory();

	private SingleIntFactory() {}

	public Object create() {
		return new int[] { 0 };
	}
}
