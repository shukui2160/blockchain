package com.tlabs.blockchain.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by admin on 2018/7/25.
 */
public class FileUtil {
  /*  private static final Logger logger = Logger.getLogger(FileUtil.class);*/


    // �����ļ�
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        // �½��ļ����������������л���
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff=new BufferedInputStream(input);

        // �½��ļ���������������л���
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff=new BufferedOutputStream(output);

        // ��������
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len =inBuff.read(b)) != -1) {
            outBuff.write(b, 0, len);
        }
        // ˢ�´˻���������
        outBuff.flush();

        //�ر���
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }
    // �����ļ���
    public static void copyDirectiory(String sourceDir, String targetDir)  throws IOException {
        File target = new File(targetDir);
        if(!target.exists()){//���Ŀ�겻�����򴴽�
            //log.info("������"+targetDir+" �ɹ���");
            target.mkdirs();
        }else{//������ɾ��Ȼ�����´���
            //log.info("ɾ����"+targetDir);
            FileUtil.deleteDirectory(targetDir);
            target.mkdirs();
        }
        if(new File(sourceDir).exists()){
            //��ȡԴ�ļ��е�ǰ�µ��ļ���Ŀ¼
            File[] file = (new File(sourceDir)).listFiles();
            for (int i = 0; i < file.length; i++) {
                if (file[i].isFile()) {
                    // Դ�ļ�
                    File sourceFile=file[i];
                    // Ŀ���ļ�
                    File targetFile=new File(new File(targetDir).getAbsolutePath()+File.separator+file[i].getName());
                    copyFile(sourceFile,targetFile);
                }
                if (file[i].isDirectory()) {
                    // ׼�����Ƶ�Դ�ļ���
                    String dir1=sourceDir + "/" + file[i].getName();
                    //log.info("����Դ�ļ��У�"+dir1);
                    // ׼�����Ƶ�Ŀ���ļ���
                    String dir2=targetDir + "/"+ file[i].getName();
                    //log.info("��Ŀ���ļ��У�"+dir1);
                    copyDirectiory(dir1, dir2);
                }
            }
        }
    }

    //ɾ���ļ���
    public static boolean deleteDirectory(String sPath) {
        //���sPath�����ļ��ָ�����β���Զ�����ļ��ָ���
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //���dir��Ӧ���ļ������ڣ����߲���һ��Ŀ¼�����˳�
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //ɾ���ļ����µ������ļ�(������Ŀ¼)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //ɾ�����ļ�
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                //log.info("ɾ�����ļ�"+files[i].getAbsolutePath()+"�ɹ���");
                if (!flag) break;
            } //ɾ����Ŀ¼
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                //log.info("ɾ�����ļ���"+files[i].getAbsolutePath()+"�ɹ���");
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //ɾ����ǰĿ¼
        if (dirFile.delete()) {
            //log.info("ɾ����ǰ�ļ���"+dirFile+"�ɹ���");
            return true;
        } else {
            return false;
        }
    }
    //ɾ���ļ�
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // ·��Ϊ�ļ��Ҳ�Ϊ�������ɾ��
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * �����ļ�
     */
    public static boolean createFile(String fileName,String filecontent,String path){
        Boolean bool = false;
        String filenameTemp = path+fileName+".html";//�ļ�·��+����+�ļ�����
        File file = new File(filenameTemp);
        try {
            //����ļ������ڣ��򴴽��µ��ļ�
            if(!file.exists()){
                file.createNewFile();
                bool = true;
                System.out.println("success create file,the file is "+filenameTemp);
                //�����ļ��ɹ���д�����ݵ��ļ���
                writeFileContent(filenameTemp, filecontent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bool;
    }

    /**
     * ���ļ���д������
     */
    public static boolean writeFileContent(String filepath,String newstr) throws IOException{
        Boolean bool = false;
        String filein = newstr+"\r\n";//��д����У�����
        String temp  = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);//�ļ�·��(�����ļ�����)
            //���ļ�����������
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            //br = new BufferedReader(isr);
            br = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
            StringBuffer buffer = new StringBuffer();

            //�ļ�ԭ������
            for(int i=0;(temp =br.readLine())!=null;i++){
                buffer.append(temp);
                // ������֮��ķָ��� �൱�ڡ�\n��
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(new OutputStreamWriter(fos,"UTF-8"));
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            //��Ҫ���ǹر�
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }

    /**
     * ɾ���ļ�
     */
    public static boolean delFile(String fileName,String path){
        Boolean bool = false;
        String filenameTemp = path+fileName+".html";
        File file  = new File(filenameTemp);
        try {
            if(file.exists()){
                file.delete();
                bool = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bool;
    }
}
