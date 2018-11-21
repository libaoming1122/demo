package com.bibenet.controller.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.standard.PrinterName;
import sun.awt.AppContext;
public class ZplPrinter {
    private String printerURI = null;           //��ӡ������·��  
    private PrintService printService = null;   //��ӡ������  
    private byte[] dotFont;  
    private String darkness = "~SD10";  //Set Darkness����ɫ����ɫ����� 0-30  
    private String width = "^PW1200";   //Print Width��ӡ���0-1500  
    private String length = "^LL800";   //Label Length��ǩ����0-x(��������)  
    private String begin = "^XA" + darkness + width;    //��ǩ��ʽ��^XA��ʼ  
    private String end = "^XZ";         //��ǩ��ʽ��^XZ����  
    private String content = "";        //��ӡ����  
    private String message = "";        //��ӡ�Ľ����Ϣ  
  
    /** 
    * ���췽�� 
     * @param printerURI ��ӡ��·�� 
     */  
   public ZplPrinter(String printerURI){  
        this.printerURI = printerURI;  
        //��������  "C:/Program Files (x86)/LabelShop/LabelShop/LabelShop.exe" -startlabelshop
       File file = new File("C://ts24.lib");  
       if(file.exists()){  
           FileInputStream fis;  
            try {  
                fis = new FileInputStream(file);  
                dotFont = new byte[fis.available()];  
                fis.read(dotFont);  
                fis.close();  
            } catch (IOException e) {  
               e.printStackTrace();  
            }  
        }else{  
            System.out.println("C://ts24.lib������");  
        }  
        //��ʼ����ӡ��  
        AppContext.getAppContext().put(PrintServiceLookup.class.getDeclaredClasses()[0], null);//ˢ�´�ӡ���б�  
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null,null);  
        if (services != null && services.length > 0) {  
            for (PrintService service : services) {  
               if (printerURI.equals(service.getName())) {  
                    printService = service;  
                   break;  
                }  
            }  
        }  
        if (printService == null) {  
           System.out.println("û���ҵ���ӡ����["+printerURI+"]");  
            //ѭ�������еĴ�ӡ��  
            if (services != null && services.length > 0) {  
                System.out.println("���õĴ�ӡ���б�");  
                for (PrintService service : services) {  
                    System.out.println("["+service.getName()+"]");  
                }  
            }  
        }else{  
            System.out.println("�ҵ���ӡ����["+printerURI+"]");  
            System.out.println("��ӡ�����ƣ�["+printService.getAttribute(PrinterName.class).getValue()+"]");  
        }  
    }  
    /** 
     * ���������� 
     * @param barcode �����ַ� 
     * @param zpl ������ʽģ�� 
     */ 
   public void setBarcode(String barcode,String zpl) {  
        content += zpl.replace("${data}", barcode);  
    }  
 
    /** 
     * �����ַ���Ӣ���ַ�(��������)��� 
     * @param str ���ġ�Ӣ�� 
     * @param x x���� 
    * @param y y���� 
     * @param eh Ӣ������߶�height 
     * @param ew Ӣ��������width 
     * @param es Ӣ��������spacing 
    * @param mx ����x������ͼ�ηŴ��ʡ���Χ1-10��Ĭ��1 
    * @param my ����y������ͼ�ηŴ��ʡ���Χ1-10��Ĭ��1 
    * @param ms ���������ࡣ24�Ǹ��ȽϺ��ʵ�ֵ�� 
    */
    public void setText(String str, int x, int y, int eh, int ew, int es, int mx, int my, int ms) {  
        byte[] ch = str2bytes(str);  
        for (int off = 0; off < ch.length;) {  
            if (((int) ch[off] & 0x00ff) >= 0xA0) {//�����ַ�  
               try {  
                    int qcode = ch[off] & 0xff;  
                    int wcode = ch[off + 1] & 0xff;  
                    content += String.format("^FO%d,%d^XG0000%01X%01X,%d,%d^FS\n", x, y, qcode, wcode, mx, my);  
                    begin += String.format("~DG0000%02X%02X,00072,003,\n", qcode, wcode);  
                   qcode = (qcode + 128 - 32) & 0x00ff;  
                    wcode = (wcode + 128 - 32) & 0x00ff;  
                    int offset = ((int) qcode - 16) * 94 * 72 + ((int) wcode - 1) * 72;  
                    for (int j = 0; j < 72; j += 3) {  
                        qcode = (int) dotFont[j + offset] & 0x00ff;  
                      wcode = (int) dotFont[j + offset + 1] & 0x00ff;  
                       int qcode1 = (int) dotFont[j + offset + 2] & 0x00ff;  
                      begin += String.format("%02X%02X%02X\n", qcode, wcode, qcode1);  
                   }  
                  x = x + ms * mx;  
                   off = off + 2;  
                } catch (Exception e) {  
                    e.printStackTrace();  
                    //�滻��X��  
                    setChar("X", x, y, eh, ew);  
                    x = x + es;//ע�������ΪӢ���ַ����  
                    off = off + 2;  
                }  
            } else if (((int) ch[off] & 0x00FF) < 0xA0) {//Ӣ���ַ�  
                setChar(String.format("%c", ch[off]), x, y, eh, ew);  
                x = x + es;  
                off++;  
            }  
        }  
   }  
    /** 
     * Ӣ���ַ���(��������) 
     * @param str Ӣ���ַ��� 
     * @param x x���� 
    * @param y y���� 
     * @param h �߶� 
     * @param w ��� 
     */ 
   public void setChar(String str, int x, int y, int h, int w) {  
        content += "^FO" + x + "," + y + "^A0," + h + "," + w + "^FD" + str + "^FS";  
    }  
    /** 
     * Ӣ���ַ�(��������)˳ʱ����ת90�� 
     * @param str Ӣ���ַ��� 
     * @param x x���� 
     * @param y y���� 
    * @param h �߶� 
     * @param w ��� 
     */
   public void setCharR(String str, int x, int y, int h, int w) {  
        content += "^FO" + x + "," + y + "^A0R," + h + "," + w + "^FD" + str + "^FS";  
   }  
   /** 
     * ��ȡ������ZPL 
     * @return 
     */  
    public String getZpl() {  
        return begin + content + end;  
    }  
   /** 
     * ����ZPLָ�����Ҫ��ӡ����ֽ��ʱ����Ҫ���á� 
     */ 
   public void resetZpl() {  
       begin = "^XA" + darkness + width;  
        end = "^XZ";  
       content = "";  
    }  
    /** 
     * ��ӡ 
     * @param zpl ������ZPL 
     */  
    public boolean print(String zpl){  
       if(printService==null){  
            message = "��ӡ����û���ҵ���ӡ��["+printerURI+"]";  
           System.out.println("��ӡ����û���ҵ���ӡ��["+printerURI+"]");  
           return false;  
       }  
        DocPrintJob job = printService.createPrintJob();  
        byte[] by = zpl.getBytes();  
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;  
        Doc doc = new SimpleDoc(by, flavor, null);  
        try {  
            job.print(doc, null);  
           message = "�Ѵ�ӡ";  
            System.out.println("�Ѵ�ӡ");  
           return true;  
        } catch (PrintException e) {  
            message = "��ӡ����:"+e.getMessage();  
            System.out.println("��ӡ����:"+e.getMessage());  
            e.printStackTrace();  
            return false;  
        }  
    }  
    public String getMessage(){  
        return message;  
    }  
    /** 
     * �ַ���תbyte[] 
     * @param s 
     * @return 
     */ 
    private byte[] str2bytes(String s) {  
        if (null == s || "".equals(s)) {  
            return null;  
        }  
        byte[] abytes = null;  
        try {  
            abytes = s.getBytes("gb2312");  
       } catch (UnsupportedEncodingException ex) {  
            ex.printStackTrace();  
       }  
        return abytes;  
    } 

}
