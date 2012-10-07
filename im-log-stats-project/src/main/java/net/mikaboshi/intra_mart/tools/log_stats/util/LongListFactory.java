package net.mikaboshi.intra_mart.tools.log_stats.util;

import java.util.ArrayList;

import org.apache.commons.collections.Factory;

public final class LongListFactory implements Factory {

	public static LongListFactory INSTANCE = new LongListFactory();

	private LongListFactory() {}

	public Object create() {
		return new ArrayList<Long>();
	}
}
