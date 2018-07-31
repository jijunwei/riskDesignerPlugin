package util;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
/**
 *  on 17/12/8.
 */
public class FileUtils {

    public static void writeStrToDisk(List<String> strs, String filePath){
        File f = new File(filePath);
        if(!f.exists())
            try {
                f.createNewFile();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath,true)));
            Iterator<String> iterator = strs.iterator();
            while (iterator.hasNext()) {
                String string = (String) iterator.next();
                writer.write(string);
                writer.write(';');
                writer.write('\n');
                writer.newLine();

            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            if( null != writer)
                try {
                    writer.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
    }
    public static String readFile(String filePathAndName) {
        String fileContent = "";
        try {
            File f = new File(filePathAndName);
            if(f.isFile()&&f.exists()){
                InputStreamReader read = new InputStreamReader(new FileInputStream(f),"UTF-8");
                BufferedReader reader=new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    fileContent += line;
                }
                read.close();
            }
        } catch (Exception e) {
            System.out.println("读取文件内容操作出错");
            e.printStackTrace();
        }
        return fileContent;
    }

    public static void writeFile(String filePathAndName, String fileContent) throws IOException {
        BufferedWriter writer = null;
        try {
            File f = new File(filePathAndName);
            if (!f.exists()) {
                f.createNewFile();
            }
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
            writer=new BufferedWriter(write);
            writer.write(fileContent);
            writer.close();
        } catch (Exception e) {
            System.out.println("写文件内容操作出错");
            e.printStackTrace();
        }finally{
            if(writer!=null){
                writer.close();
            }
        }
    }
    /**
     * A方法追加文件：使用RandomAccessFile
     */
    public static void appendMethodA(String fileName, String content) {
        try {
            File f = new File(fileName);
            if (!f.exists()) {
                f.createNewFile();
            }
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * B方法追加文件：使用FileWriter
     */
    public static void appendMethodB(String fileName, String content) {
        try {
            File f = new File(fileName);
            if (!f.exists()) {
                f.createNewFile();
            }
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void method1(String file, String conent) {
        BufferedWriter out = null;
        try {
            File f = new File(file);
            if (!f.exists()) {
                f.createNewFile();
            }
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            out.write(conent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
   /* public static String uploadFormFlie(String base,MultipartFile filedata){
        Path path = null;
        MultipartFile multipartFile = filedata;
       // MultipartFile multipartFile1 = (MultipartFile)multipartRequest.getFile("filedata");
        String sourceName = multipartFile.getOriginalFilename(); // 原始文件名
        String fileType = sourceName.substring(sourceName.lastIndexOf("."));
        System.out.println("上传的文件名为:"+sourceName+"类型为:"+fileType);
        File file = new File(base);
       if(!file.exists()){
           file.mkdirs();
       }
        try {
            byte[] bytes = filedata.getBytes();
            path = Paths.get(base+sourceName);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path.toString();
    }*/
    public static String readFile2(String filePathAndName) {
        StringBuffer fileContent = new StringBuffer("");
        try {
            File f = new File(filePathAndName);
            if(f.isFile()&&f.exists()){
                InputStreamReader read = new InputStreamReader(new FileInputStream(f),"UTF-8");
                BufferedReader reader=new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    //System.out.println(line);
                    fileContent.append(line).append("#");
                }
                read.close();
            }
        } catch (Exception e) {
            System.out.println("读取文件内容操作出错");
            e.printStackTrace();
        }
        return fileContent.toString();
    }
    public static String readTxtFile(String filePath) {

        try {
            String encoding = "GBK";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                StringBuffer ph = new StringBuffer();
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    // System.out.println(lineTxt);
                    ph.append(lineTxt + ",");
                }
                read.close();
                return ph.toString();
            } else {
                System.out.println("找不到指定的文件");
                return "meiy";
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
            return "error";
        }

    }
}

