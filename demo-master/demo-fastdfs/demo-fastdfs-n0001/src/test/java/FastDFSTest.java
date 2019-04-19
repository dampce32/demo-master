import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.ServerInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

public class FastDFSTest {

	@SuppressWarnings("resource")
	@Test
	public void testUpload() throws Exception {
		// 可参看https://github.com/happyfish100/fastdfs-client-java/blob/master/src/test/java/org/csource/fastdfs/TestClient.java
		// 1、把FastDFS提供的jar包添加到工程中
		// 2、初始化全局配置。加载一个配置文件。
		ClientGlobal.initByProperties("fastdfs-client.properties");
		// 3、创建一个TrackerClient对象。
		TrackerClient trackerClient = new TrackerClient();

		// 4、创建一个TrackerServer对象。
		TrackerServer trackerServer = trackerClient.getConnection();
		// 5、声明一个StorageServer对象，null。
		StorageServer storageServer = null;
		// 6、获得StorageClient对象。
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		// 7、直接调用StorageClient对象方法上传文件即可。
		String[] results = storageClient.upload_file("D:/Study/7M.jpg", "jpg", null);
		String group_name;
		String remote_filename;
		ServerInfo[] servers;
		byte[] file_buff;

		if (results == null) {
			System.err.println("upload file fail, error code: " + storageClient.getErrorCode());
			return;
		} else {
			group_name = results[0];// group1 组名，卷名
			remote_filename = results[1];// M00/00/00/wKjKgVyu9sqAZC5_AHEGV7OaW-0895.jpg
											// 远程文件路径

			servers = trackerClient.getFetchStorages(trackerServer, group_name, remote_filename);
			if (servers == null) {
				System.err.println("get storage servers fail, error code: " + trackerClient.getErrorCode());
			} else {
				file_buff = storageClient.download_file(group_name, remote_filename);// 下载
				// 将文件保存到本地文件
				if (file_buff != null) {
					System.out.println("file length:" + file_buff.length);

					BufferedOutputStream bos = null;
					FileOutputStream fos = null;
					File file = new File("D:/Study/download.jpg");
					fos = new FileOutputStream(file);
					bos = new BufferedOutputStream(fos);
					bos.write(file_buff);
				}
			}
		}
	}

	@SuppressWarnings("resource")
	@Test
	public void testDownload() throws IOException, MyException {
		ClientGlobal.initByProperties("fastdfs-client.properties");
		// 3、创建一个TrackerClient对象。
		TrackerClient trackerClient = new TrackerClient();
		// 4、创建一个TrackerServer对象。
		TrackerServer trackerServer = trackerClient.getConnection();
		// 5、声明一个StorageServer对象，null。
		StorageServer storageServer = null;
		// 6、获得StorageClient对象。
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		String group_name = "group1";
		ServerInfo[] servers;
		String remote_filename = "M00/00/00/wKjKgVyvJ9WAXiJ7AHEGV7OaW-0795.jpg";
		byte[] file_buff;

		servers = trackerClient.getFetchStorages(trackerServer, group_name, remote_filename);
		if (servers == null) {
			System.err.println("get storage servers fail, error code: " + trackerClient.getErrorCode());
		} else {
			file_buff = storageClient.download_file(group_name, remote_filename);// 下载
			// 将文件保存到本地文件
			if (file_buff != null) {
				System.out.println("file length:" + file_buff.length);

				BufferedOutputStream bos = null;
				FileOutputStream fos = null;
				File file = new File("D:/Study/download.jpg");
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos);
				bos.write(file_buff);
			}
		}
	}
	
	
	
}
