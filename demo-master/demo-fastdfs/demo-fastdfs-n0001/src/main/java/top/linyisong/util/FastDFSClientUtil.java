package top.linyisong.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
/**
 * @description: FastDFS客户端工具类
 * @createTime: 2019年4月19日下午4:21:37
 * @author：lin.yisong
 * @version：1.0
 */
public class FastDFSClientUtil {
	public static final String SPLIT_GROUP_NAME_AND_FILENAME_SEPERATOR = "/";
    private static StorageClient1 storageClient = null;

    // 初始化FastDFS Client
    static {
        try {
            ClientGlobal.initByProperties("fastdfs-client.properties");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            if (trackerServer == null) {
                throw new IllegalStateException("getConnection return null");
            }
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            if (storageServer == null) {
                throw new IllegalStateException("getStoreStorage return null");
            }

            storageClient = new StorageClient1(trackerServer,storageServer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 上传文件
     * @createTime: 2019年4月19日 下午4:06:39
     * @author: lin.yisong
     * @param file
     * @return
     */
    public static String uploadFile(File file) {
        return uploadFile(file,file.getName(),null);
    }
    /**
     * 上传文件
     * @param file 文件对象
     * @param fileName 文件名
     * @return
     */
    public static String uploadFile(File file, String fileName) {
        return uploadFile(file,fileName,null);
    }

    /**
     * 上传文件
     * @param file 文件对象
     * @param fileName 文件名
     * @param metaList 文件元数据
     * @return
     */
    public static String uploadFile(File file, String fileName, Map<String,String> metaList) {
        try {
            byte[] buff = FileUtils.readFileToByteArray(file);
            NameValuePair[] nameValuePairs = null;
            if (metaList != null) {
                nameValuePairs = new NameValuePair[metaList.size()];
                int index = 0;
                for (Iterator<Map.Entry<String,String>> iterator = metaList.entrySet().iterator(); iterator.hasNext();) {
                    Map.Entry<String,String> entry = iterator.next();
                    String name = entry.getKey();
                    String value = entry.getValue();
                    nameValuePairs[index++] = new NameValuePair(name,value);
                }
            }
            String parts[] = storageClient.upload_file(buff,FilenameUtils.getExtension(fileName),nameValuePairs);
            if (parts != null) {
              return parts[0] + SPLIT_GROUP_NAME_AND_FILENAME_SEPERATOR + parts[1];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件元数据
     * @param fileId 文件ID
     * @return
     */
    public static Map<String,String> getFileMetadata(String fileId) {
        try {
            NameValuePair[] metaList = storageClient.get_metadata1(fileId);
            if (metaList != null) {
                HashMap<String,String> map = new HashMap<String, String>();
                for (NameValuePair metaItem : metaList) {
                    map.put(metaItem.getName(),metaItem.getValue());
                }
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除文件
     * @createTime: 2019年4月19日 下午4:21:06
     * @author: lin.yisong
     * @param fileId
     * @return 0：成功、1：失败
     */
    public static int deleteFile(String fileId) {
        try {
            return storageClient.delete_file1(fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 下载文件
     * @createTime: 2019年4月19日 下午4:19:22
     * @author: lin.yisong
     * @param fileId
     * @param outFile
     * @return 0：成功、1：失败
     */
    public static int downloadFile(String fileId, File outFile) {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            byte[] content = storageClient.download_file1(fileId);
            fos = new FileOutputStream(outFile);
            bos = new BufferedOutputStream(fos);
			bos.write(content);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                	bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }
}
