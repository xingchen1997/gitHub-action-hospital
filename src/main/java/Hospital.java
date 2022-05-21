import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Hospital {

    public static void main(String[] args) throws IOException {
        StringBuffer res = new StringBuffer();
        String[][] contents=help();
        res.append("| 索引 | 时间 | 内容 |\n" +
                "| ---- | ---- | ---- |\n");
        for (int i = 0; i < contents.length; i++) {
            res.append("|" + (i + 1) + ". |" + contents[i][0] + "|" + contents[i][1] + "\n");
        }
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

        System.out.println("当前时间为: " + ft.format(dNow));
        res.append("更新时间："+ ft.format(dNow));
        toMarkdown("README", res.toString());
    }
    public static String[][] help(){
        String[][] contents = new String[0][];
        try {
            //建立连接
//            http://www.tjwsrc.com/qzzp/index.shtml
//            http://wsjk.tj.gov.cn/ZWGK3158/ZFXXGK5869/FDZDGKNR4356/zklyn/
            URL url = new URL("http://www.tjwsrc.com/qzzp/index.shtml");
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //获取输入流
            InputStream input = httpUrlConn.getInputStream();
            //将字节输入流转换为字符输入流
            InputStreamReader read = new InputStreamReader(input, "utf-8");
            //为字符输入流添加缓冲
            BufferedReader br = new BufferedReader(read);
            // 读取返回结果
            String data = br.readLine();
            StringBuffer resLine = new StringBuffer();
            LinkedList<String> list = new LinkedList<>();
            LinkedList<String> listTime = new LinkedList<>();
            int index = 0;
            System.out.println("| 索引 | 时间 | 内容 |\n" +
                    "| ---- | ---- | ---- |");
            while (data != null) {
                if (data.contains("title=")) {
                    index++;
                    String[] v = data.split("\"");
                    resLine.append("[" + v[13] + "](");
                    resLine.append(v[9] + ")|");
                    list.add(resLine.toString());
                    resLine = new StringBuffer();
                }
                if (data.contains("<div align=\"right\">")) {
                    String[] v = data.split("\"right\">");
                    String time = v[1].split("</div></td>")[0];
                    listTime.add(time);
                    System.out.println("|" + index + ". |" + listTime.get(index - 1) + "|" + list.get(index - 1));
                }

                data = br.readLine();
            }
            contents=new String[list.size()][2];
            for (int i = 0; i < list.size(); i++) {
                contents[i][0]=listTime.get(i);
                contents[i][1]=list.get(i);
            }
            // 释放资源
            br.close();
            read.close();
            input.close();
            httpUrlConn.disconnect();
            return contents;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }

    //重写文件
    public static void toMarkdown(String fileName, String content) throws IOException {
        File f = new File(fileName + ".md");//指定文件 "C:\\a.txt"
        FileOutputStream fos = new FileOutputStream(f);//创建输出流fos并以f为参数
        OutputStreamWriter osw = new OutputStreamWriter(fos);//创建字符输出流对象osw并以fos为参数
        BufferedWriter bw = new BufferedWriter(osw);//创建一个带缓冲的输出流对象bw，并以osw为参数
        bw.write(content);//使用bw写入一行文字，为字符串形式String
        bw.newLine();//换行
        bw.close();//关闭并保存
    }

    //追加文件：使用FileWriter
    public static void toFileWriter(String fileName, String content) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}