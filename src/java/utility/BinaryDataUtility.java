package utility;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class BinaryDataUtility {
	/**********************************************
	* Loggerオブジェクトの生成
	**********************************************/
	static final Logger log = Logger.getLogger(
                                BinaryDataUtility.class.getName());
	
	
	public static byte[] readBinaryData(String relative){
    	String filePath = getRealPath(relative);		// 絶対パスに変換
		Path path       = Paths.get(filePath);	// Pathオブジェクトを作成
		byte[]	binaryData = null;
		try {
			binaryData	= Files.readAllBytes(path);		// 全データを読み出す
		} catch (IOException ex) {
            log.severe(ex.toString());
		}
		return	binaryData;
    }
	
	public	static String getRealPath(String path){
		ServletContext context = (ServletContext) FacesContext
                                                          .getCurrentInstance()
                                                          .getExternalContext()
                                                          .getContext();
		return  context.getRealPath(path);
	}
}

