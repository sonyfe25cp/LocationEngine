package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 大文件读取，配合{@code DLDELineReaderOperation}使用,则必须配size
 * 若只普通流式读取，则不可配size
 * @author ChenJie
 *
 */
public abstract class DLDELineReader {
	private static DLDELogger logger = new DLDELogger();
	private String filePath;

	public abstract void opterate(String line);


	public DLDELineReader(String filePath) {
		this.filePath = filePath;
	}

	public void read() throws IOException {
		FileInputStream fin = new FileInputStream(filePath);
		FileChannel fc = fin.getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(1024*1024*10);//10M缓冲区

		StringBuilder line = new StringBuilder();
		int i = 1;

		while (fc.read(buffer) != -1) {
			buffer.flip();
			byte[] dst = new byte[buffer.capacity()];
			buffer.get(dst, 0, buffer.limit());
			String s = new String(dst);
			int pos = s.indexOf('\n');
			int los = s.lastIndexOf('\n');
			if (pos == -1) {//没有回车
				line.append(s);
			} else {
				if (pos == los) {//只有一个回车
					line.append(s.substring(0, pos));
					logger.debug(i + " : " + line.toString());
					opterate(line.toString());
					line = new StringBuilder();
					line.append(s.substring(pos + 1));
					i++;
				} else {
					String tmp[] = s.split("\n");
					for (int j = 0; j < tmp.length; j++) {
						if (j != tmp.length - 1) {//处理前面的行
							line.append(tmp[j]);
							logger.debug(i + " : " + line.toString());
							opterate(line.toString());
							line = new StringBuilder();
							i++;
						} else {//最后一段单独处理
							if (los == s.length()) {//如果最后一个是回车
								line.append(tmp[j]);
								logger.debug(i + " : " + line.toString());
								opterate(line.toString());
								line = new StringBuilder();
								i++;
							} else {//最后一段不是一行
								line.append(tmp[j]);
							}
						}
					}
				}
			}
			buffer.clear();
		}
		if (buffer.hasRemaining()) {//处理余下的内容
			logger.debug(i + " : " + line.toString());
			opterate(line.toString());
		}
	}
}
