package top.linyisong.util;

import java.io.File;
import java.util.Map;

import org.junit.Test;

public class FastDFSClientUtilTest {
	
	@Test
	public void testUploadFile() throws Exception {
		String fileId = FastDFSClientUtil.uploadFile(new File("D:/Study/7M.jpg"));
		System.out.println(fileId);//group1/M00/00/00/wKjKgVyv2QqAbf0zAHEGV7OaW-0679.jpg
	}
	
	@Test
	public void testGetFileMetadata() throws Exception {
		String fileId = "group1/M00/00/00/wKjKgVyv2QqAbf0zAHEGV7OaW-0679.jpg";
		Map<String,String> result = FastDFSClientUtil.getFileMetadata(fileId);
		System.out.println(result);
	}
	
	@Test
	public void testDownloadFile() throws Exception {
		String fileId = "group1/M00/00/00/wKjKgVyv2QqAbf0zAHEGV7OaW-0679.jpg";
		int result = FastDFSClientUtil.downloadFile(fileId,new File("D:/Study/download.jpg"));
		System.out.println(result);
	}
	
	@Test
	public void testDeleteFile() throws Exception {
		String fileId = "group1/M00/00/00/wKjKgVyv2QqAbf0zAHEGV7OaW-0679.jpg";
		int result = FastDFSClientUtil.deleteFile(fileId);
		System.out.println(result);
	}
	
}
