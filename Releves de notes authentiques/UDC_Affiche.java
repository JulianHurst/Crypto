
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author juju
 */
public class UDC_Affiche {
    String firstline=new String();
    
    UDC_Affiche(){}
    
    static void readpdf(String file) throws IOException{
        try {
            int ch;
            
            boolean nocomment=false;
            FileInputStream fp=new FileInputStream(file);
            FileOutputStream op=new FileOutputStream("temp");
            ch=fp.read();
            //System.out.print((char)ch);
            while(ch!='\n' && ch!='\r'){
                op.write(ch);
                ch=fp.read();                
            }
            System.out.print((char)ch);
            if(ch=='\n')
                op.write(ch);
            if(ch!='%' && ch!='\n'){                
                nocomment=true;
                op.write(ch);
            }
            while(ch!=-1 && !nocomment){
                System.out.print((char)ch);
                if(ch=='\r'){
                    ch=fp.read();
                    if(ch=='\n'){
                        ch=fp.read();   
                        if(ch!='%'){
                            op.write(ch);
                            nocomment=true;
                        }
                            
                    }
                    else if(ch!='%'){
                        op.write(ch);
                        nocomment=true;
                    }
                    System.out.println();
                }
                else if(ch=='\n'){
                    ch=fp.read();
                    System.out.println();
                    if(ch!='%'){
                        op.write(ch);
                        nocomment=true;
                    }
                }
               ch=fp.read();
            }            
            while(ch!=-1){
                op.write(ch);
                ch=fp.read();
            }
            fp.close();
            op.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UDC_Affiche.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static void insert(String file,String out){
        Process p;
        try {
            int ch,test=0;
            boolean nocomment=false;
            p = Runtime.getRuntime().exec("md5sum "+out);
            BufferedReader stdInput = new BufferedReader(new
                 InputStreamReader(p.getInputStream()));
            String md5=stdInput.readLine().split(" ")[0];
            File f=new File(out);
            f.delete();            
            FileInputStream fp=new FileInputStream(file);
            FileOutputStream op=new FileOutputStream(out);            
            ch=fp.read();
            if(ch!='%'){
                System.out.println("Wrong format");
                return;
            }
            //System.out.print((char)ch);
            while(ch!='\n' && ch!='\r'){
                if(test==1 && ch!='P'){
                    System.out.println("Wrong format");
                    return;
                }
                else if(test==2 && ch!='D'){
                    System.out.println("Wrong format");
                    return;
                }
                else if(test==3 && ch!='F'){
                    System.out.println("Wrong format");
                    return;
                }
                op.write(ch);
                ch=fp.read();   
                test++;
            }
            if(ch=='\n')
                op.write(ch);            
            String insertline="%UDC SIGNATURE \"0x<"+md5+">\"\r\n";
            byte [] b=insertline.getBytes();
            op.write(b);
            if(ch!='%' && ch!='\n'){
                nocomment=true;
                op.write(ch);
            }            
            while(ch!=-1 && !nocomment){
                System.out.print((char)ch);
                if(ch=='\r'){
                    ch=fp.read();
                    if(ch=='\n'){
                        ch=fp.read();   
                        if(ch!='%'){
                            nocomment=true;
                            op.write(ch);
                        }  
                    }
                    else if(ch!='%'){
                        nocomment=true;
                        op.write(ch);
                    }  
                    System.out.println();
                }
                else if(ch=='\n'){
                    ch=fp.read();
                    System.out.println();
                    if(ch!='%'){
                        nocomment=true;
                        op.write(ch);
                    }  
                }
               ch=fp.read();
            }            
            while(ch!=-1){
                op.write(ch);
                ch=fp.read();
            }
            fp.close();
            op.close();
        } catch (IOException ex) {
            Logger.getLogger(UDC_Affiche.class.getName()).log(Level.SEVERE, null, ex);
        }            
    }
    
    public static void main(String[] args){
        if(args.length!=1){
            System.out.println("USAGE : UDC_Affiche fichierpdf");
            return;
        }
        System.out.println(args[0]);
        try {
            readpdf(args[0]);
            insert(args[0],"temp");
        } catch (IOException ex) {
            Logger.getLogger(UDC_Affiche.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
