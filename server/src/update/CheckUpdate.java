package update;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
 
public class CheckUpdate extends JFrame {
    JFrame c = this;
 
    public CheckUpdate() {
        //设置窗体属性
        setAttb();
 
        JLabel title = new JLabel("正在检查网络上的更新资源");
        this.add(title, BorderLayout.NORTH);
        JTextArea msg = new JTextArea();
        this.add(msg, BorderLayout.CENTER);
        JLabel process = new JLabel();
        this.add(process, BorderLayout.SOUTH);
        
        //启动更新线程
        new Check(msg, process).start();
    }
 
    private class Check extends Thread {
        //标识,是否存在新的更新文件
        private boolean isUpdated = false;
        //保存最新的版本
        String netVersion;
        //本地版本文件名
        String LocalVerFileName = "ver.txt";
 
        //显示信息
        private JTextArea msg;
        private JLabel process;
 
        public Check(JTextArea msg, JLabel process) {
            this.msg = msg;
            this.process = process;
        }
 
        public void run() {
            //更新文件版本标识URL
            String versionUrl = "http://XXX.XXX.XXX/AutoUpdate/ver";
 
            /**//*
			这里是通过HTTP访问一个页面,以取得网络上的版本号
			比如这里就是在这个页面直接打印出 6.19.1.1
			然后把这个版本号比对本地的版本号,如果版本号不同的话,就从网络上下载新的程序并覆盖现有程序
            */
 
            URL url = null;
            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader netVer = null;
 
            //读取网络上的版本号
            try {
                url = new URL(versionUrl);
                is = url.openStream();
                isr = new InputStreamReader(is);
 
                netVer = new BufferedReader(isr);
                String netVerStr = netVer.readLine();
                String localVerStr = getNowVer();
 
                if (netVerStr.equals(localVerStr)) {
                    msg.append("当前文件是最新版本\n");
                    isUpdated = false;
                } else {
                    msg.append("存在更新文件,现在开始更新\n");
                    isUpdated = true;
                    netVersion = netVerStr;
                }
 
            } catch (MalformedURLException ex) {
            } catch (IOException ex) {
            } finally {
                //释放资源
                try {
                    netVer.close();
                    isr.close();
                    is.close();
                } catch (IOException ex1) {
                }
            }
 
            //如果版本不同,下载网络上的文件,更新本地文件
            if (isUpdated) {
                //本地需要被更新的文件
                File oldFile = new File("client.exe");
                //缓存网络上下载的文件
                File newFile = new File("temp.exe");
                
                //网络上的文件位置
                String updateUrl =
                        "http://XXX.XXX.XXX/downloads/simpkle.exe";
 
                HttpURLConnection httpUrl = null;
                BufferedInputStream bis = null;
                FileOutputStream fos = null;
 
                try {
                    //打开URL通道
                    url = new URL(updateUrl);
                    httpUrl = (HttpURLConnection) url.openConnection();
 
                    httpUrl.connect();
 
                    byte[] buffer = new byte[1024];
 
                    int size = 0;
 
                    is = httpUrl.getInputStream();
                    bis = new BufferedInputStream(is);
                    fos = new FileOutputStream(newFile);
 
                    msg.append("正在从网络上下载新的更新文件\n");
 
                    //保存文件
                    try {
                        int flag = 0;
                        int flag2 = 0;
                        while ((size = bis.read(buffer)) != -1) {
                            //读取并刷新临时保存文件
                            fos.write(buffer, 0, size);
                            fos.flush();
 
                            //模拟一个简单的进度条
                            if (flag2 == 99) {
                                flag2 = 0;
                                process.setText(process.getText() + ".");
                            }
                            flag2++;
                            flag++;
                            if (flag > 99 * 50) {
                                flag = 0;
                                process.setText("");
                            }
                        }
                    } catch (Exception ex4) {
                        System.out.println(ex4.getMessage());
                    }
 
                    msg.append("\n文件下载完成\n");
 
                    //把下载的临时文件替换原有文件
                    CopyFile(oldFile,newFile);
                    
                    //把本地版本文件更新为网络同步
                    UpdateLocalVerFile();
 
                } catch (MalformedURLException ex2) {
                } catch (IOException ex) {
                    msg.append("文件读取错误\n");
                } finally {
                    try {
                        fos.close();
                        bis.close();
                        is.close();
                        httpUrl.disconnect();
                    } catch (IOException ex3) {
                    }
                }
            }
 
            //启动应用程序
            try {
                msg.append("启动应用程序");
                Thread.sleep(500);
                Process p = Runtime.getRuntime().exec("client.exe");
            } catch (IOException ex5) {
            } catch (InterruptedException ex) {
            }
            
            //退出更新程序
            System.exit(0);
        }
//复制文件
        private void CopyFile(File oldFile, File newFile) {
            FileInputStream in = null;
            FileOutputStream out = null;
            
            try {
                if(oldFile.exists()){
                    oldFile.delete();
                }
                in = new FileInputStream(newFile);
                out = new FileOutputStream(oldFile);
 
                byte[] buffer = new byte[1024 * 5];
                int size;
                while ((size = in.read(buffer)) != -1) {
                    out.write(buffer, 0, size);
                    out.flush();
                }
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            } finally {
                try {
                    out.close();
                    in.close();
                } catch (IOException ex1) {
                }
            }
 
        }
 
        private void UpdateLocalVerFile() {
            //把本地版本文件更新为网络同步
            FileWriter verOS = null;
            BufferedWriter bw = null;
            try {
                verOS = new FileWriter(LocalVerFileName);
 
                bw = new BufferedWriter(verOS);
                bw.write(netVersion);
                bw.flush();
 
            } catch (IOException ex) {
            } finally {
                try {
                    bw.close();
                    verOS.close();
                } catch (IOException ex1) {
                }
            }
        }
 
        private String getNowVer() {
            //本地版本文件
            File verFile = new File(LocalVerFileName);
 
            FileReader is = null;
            BufferedReader br = null;
 
            //读取本地版本
            try {
                is = new FileReader(verFile);
 
                br = new BufferedReader(is);
                String ver = br.readLine();
 
                return ver;
            } catch (FileNotFoundException ex) {
                msg.append("本地版本文件未找到\n");
            } catch (IOException ex) {
                msg.append("本地版本文件读取错误\n");
            } finally {
                //释放资源
                try {
                    br.close();
                    is.close();
                } catch (IOException ex1) {
                }
            }
            return "";
        }
    }
 
 
    private void setAttb() {
        //窗体设置
        this.setTitle("Auto Update");
        this.setSize(200, 150);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
 
        // 窗体居中
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2,
                         (screenSize.height - frameSize.height) / 2);
    }
 
    public static void main(String[] args) {
        CheckUpdate checkupdate = new CheckUpdate();
        checkupdate.setVisible(true);
    }
}