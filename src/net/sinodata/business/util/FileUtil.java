package net.sinodata.business.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

	/**
	 * 向文件中写入内容
	 * 
	 * @param filePath 文件路径
	 * @param fileName 文件名称
	 * @param fileInfo 文件信息
	 * @throws IOException
	 */
	public static void writeFile(String filePath, String fileName, String fileInfo) throws IOException {
		File checkFile = new File(filePath + fileName);
		FileWriter writer = null;
		try {
			// 向目标文件中写入内容
			writer = new FileWriter(checkFile, true);
			writer.append(fileInfo);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != writer)
				writer.close();
		}
	}

}