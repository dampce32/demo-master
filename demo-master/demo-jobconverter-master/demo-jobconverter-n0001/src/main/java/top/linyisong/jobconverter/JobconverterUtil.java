package top.linyisong.jobconverter;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.jodconverter.LocalConverter;
import org.jodconverter.document.DocumentFormat;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeUtils;
/**
 * jobconverter工具类
 * @description: 
 * @createTime: 2019年3月29日下午2:46:51
 * @author：lin.yisong
 * @version：1.0
 */
public class JobconverterUtil {
	
	private static LocalOfficeManager officeManager = null;
	
	static Properties applicationPro = new Properties();
	/**
	 * 取得OfficeManager，做成单例目的：如果是linux系统部分服务器，连接会占用大量时间，如果是web系统，连接一次后续可直接使用该连接
	 * @createTime: 2019年3月29日 下午2:47:40
	 * @author: lin.yisong
	 * @return
	 */
	public static LocalOfficeManager getOfficeManager() {
        if (officeManager == null) {
        	try {
        		applicationPro.load(JobconverterUtil.class.getClassLoader().getResourceAsStream("application.properties"));
    			Properties prop = System.getProperties();
    			String os = prop.getProperty("os.name");
    			String officeHome = null;
    			if (os != null && os.toLowerCase().indexOf("linux") > -1) {
    				officeHome = applicationPro.get("officeHome.linux").toString();
    			}else{
    				officeHome = applicationPro.get("officeHome.window").toString();
    			}
    			officeManager = LocalOfficeManager.builder().portNumbers(8100).officeHome(officeHome).build();
    			officeManager.start();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        }
        return officeManager;
    }
	/**
	 * 将文件inputFile转化成文件outputFile
	 * @createTime: 2019年3月29日 下午2:49:46
	 * @author: lin.yisong
	 * @param inputFile
	 * @param outputFile
	 * @throws OfficeException
	 */
	public static void convert(File inputFile,File outputFile) throws OfficeException{
			LocalConverter.builder()
			.officeManager(getOfficeManager())
			.build()
			.convert(inputFile)
			.to(outputFile)
			.execute();
	}
	/**
	 * 将文件路径inputFilePath下的文件转化成文件路径outputFilePath下的文件
	 * @createTime: 2019年3月29日 下午2:50:29
	 * @author: lin.yisong
	 * @param inputFilePath
	 * @param outputFilePath
	 * @throws OfficeException
	 */
	public static void convert(String inputFilePath,String outputFilePath) throws OfficeException{
		convert(new File(inputFilePath), new File(outputFilePath));
	}
	/**
	 * 使用IO流的方式转化
	 * @createTime: 2019年3月29日 下午2:51:10
	 * @author: lin.yisong
	 * @param inputStream
	 * @param inputType
	 * @param outputStream
	 * @param outType
	 * @throws OfficeException
	 */
	public static void convert(InputStream inputStream,DocumentFormat inputType ,OutputStream outputStream,DocumentFormat outType) throws OfficeException{
		LocalConverter.builder()
		.officeManager(getOfficeManager())
		.build()
		.convert(inputStream)
		.as(inputType)
		.to(outputStream)
		.as(outType)
		.execute();
	}
	

	public static void main(String[] args) {
		if(args==null||args.length<2){
			System.out.println("请输入参数");
		}
		String inputFilePath = args[0];
		String outputFilePath = args[1];
//		File inputFile = new File("D:/tmp/1.docx");
//		File outputFile = new File("D:/tmp/1.pdf");
		try {
			JobconverterUtil.convert(inputFilePath, outputFilePath);
			
//			InputStream inputStream = new FileInputStream(inputFile);
//			OutputStream outputStream = new FileOutputStream(outputFile);
			
//			JobconverterUtil.convert(inputStream,DefaultDocumentFormatRegistry.DOCX, outputStream,DefaultDocumentFormatRegistry.PDF);
		}  catch (Exception e) {
			e.printStackTrace();
		}finally {
		    OfficeUtils.stopQuietly(officeManager);
		}

	}
}
