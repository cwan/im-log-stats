/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package net.mikaboshi.intra_mart.tools.log_stats.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.mikaboshi.intra_mart.tools.log_stats.parser.LogFileReader;
import net.mikaboshi.intra_mart.tools.log_stats.parser.LogLayoutDefinitions;
import net.mikaboshi.intra_mart.tools.log_stats.parser.ParserParameter;
import net.mikaboshi.intra_mart.tools.log_stats.parser.RequestLogParser;
import net.mikaboshi.intra_mart.tools.log_stats.parser.Version;


public class LogFileReaderSample {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		List<File> logFiles = new ArrayList<File>();
		logFiles.add(new File("C:/imart/iwp724/log/platform/r1.log"));
		logFiles.add(new File("C:/imart/iwp724/log/platform/r2.log"));
		logFiles.add(new File("C:/imart/iwp724/log/platform/r3.log"));
		logFiles.add(new File("C:/imart/iwp724/log/platform/r4.log"));

		RequestLogParser parser = new RequestLogParser(
				new ParserParameter(),
				LogLayoutDefinitions.getStandardRequestLogLayout(Version.V72));

		LogFileReader logFileReader = null;

		try {
			logFileReader = new LogFileReader(logFiles, parser, "UTF-8");

			while(logFileReader.hasNext()) {
				System.out.println(logFileReader.nextLine());
			}
		} finally {
			logFileReader.close();
		}
	}

}
