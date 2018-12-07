package r06.propertiesWizards.testdemo;

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImageFactory {

	//將構造方法設置為private，禁止創建該類的實例
	private ImageFactory() {}
	    //圖片保存的絕對地址
	public static final String REAL_PATH = "C:\\Users\\jjw8610\\rdgit\\r06\\icons\\table\\";
	    //一些圖片名稱的常量
	public static final String DELETE_EDIT = "delete_edit.gif";

	public static final String SAVE_EDIT = "save_edit.gif";

	public static final String SCOPY_EDIT = "copy_edit.gif";

	public static final String CUT_EDIT = "cut_edit.gif";

	public static final String PRINT_EDIT = "print_edit.gif";

	public static final String HELP_CONTENTS = "help_contents.gif";

	public static final String ECLIPSE = "eclipse.gif";

	public static final String SAMPLES = "samples.gif";
	public static final String ADD_OBJ = "add_obj.gif";
	public static final String DELETE_OBJ = "delete_obj.gif";

	public static final String BACKWARD_NAV = "backward_nav.gif";
	public static final String FORWARD_NAV = "forward_nav.gif";
	public static final String ICON_GIRL = "girl.gif";
	public static final String ICON_BOY = "boy.gif";

	public static final String TOC_CLOSED = "toc_closed.gif";
	public static final String TOC_OPEN = "toc_open.gif";
	public static final String TOPIC = "topic.gif";

	public static final String UNDERLIN = "underline.gif";
	public static final String ITALIC = "italic.gif";
	public static final String BOLD = "bold.gif";
	public static final String BGCOLOR = "bgcol.gif";
	public static final String FORCOLOR = "forecol.gif";
	public static final String PROGRESS_TASK = "progress_task.gif";
	public static final String SAMPLE_ICON = "sample_icon.gif";
	public static final String FILE = "file.gif";
	public static final String FOLDER = "folder.gif";
	    //使用Hashtable保存使用的圖片資源 
	private static Hashtable<String, Image> htImage = new Hashtable<String, Image>();
	    //加載圖片.首先從htImage中獲得圖片對象，
	//如果沒有，則加載新的圖片並放入到htImage
	public static Image loadImage(Display display, String imageName) {
	   Image image = (Image) htImage.get(imageName.toUpperCase());
	   if (image == null) {
	    image = new Image(display, REAL_PATH + imageName);
	    htImage.put(imageName.toUpperCase(), image);
	   }
	   return image;
	}

	//釋放htImage中的所有的圖片資源
	public static void dispose() {
	   @SuppressWarnings("rawtypes")
	   Enumeration e = htImage.elements();
	   while (e.hasMoreElements()) {
	    Image image = (Image) e.nextElement();
	    if (!image.isDisposed())
	     image.dispose();
	   }
	}
	}