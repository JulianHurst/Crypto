import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


public class cert {

	String corps=new String();
        String header=new String();
        int separation=0;

	void readcorps(String file) throws FileNotFoundException, IOException{
		boolean read,car1,car;
		read=car1=car=false;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"))) {
                for(int line; (line = br.read()) != -1; ) {
                    if(read)
                        corps=corps+(char)line;
                    else if(!car)
                        header=header+(char)line;
                    if(line=='\n' && !car1)
                        car1=true;
                    else if(line=='\r' && car1)
                        car=true;
                    else if(line=='\n' && car)
                        read=true;
                    else
                        car=car1=false;
                    if(!read)
                        separation++;
                }
            }
		    System.out.println("corps :"+corps);
	}

        
        static String calculehash(String msg) throws NoSuchAlgorithmException, UnsupportedEncodingException{
                byte[] buffer,resume,bS,bipad,bopad;
                
                int i=0;                
                
                String S = "0x7e7e7e7e7e7e7e7e7e7e7e7e7e7e7e7e";
                String ipad = "0x36363636363636363636363636363636";
                String opad = "0x5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c";
                String str;
                
                bS=S.getBytes("UTF-8");                
                bipad=ipad.getBytes("UTF-8");                
                byte[] bstr = new byte[bS.length];
                for (byte b : bS)
                    bstr[i] = (byte) (b ^ bipad[i++]);
		MessageDigest fonction_de_hachage;
		fonction_de_hachage = MessageDigest.getInstance("MD5");                
		//buffer = new byte[1024];		 
                //buffer = S.getBytes("UTF-8");
		//buffer = msg.getBytes("UTF-8");
                buffer = bstr;
		fonction_de_hachage.update(buffer);                        
                //fonction_de_hachage.update(buffer);		
		resume=fonction_de_hachage.digest();
                
                bopad=opad.getBytes();
                bstr = new byte[bS.length];
                i=0;
                for (byte b : bS)
                    bstr[i] = (byte) (b ^ bopad[i++]);
                
                str=Arrays.toString(bstr);
                str = str.concat(Arrays.toString(resume));
                str = str.concat(msg);
                
                buffer=str.getBytes("UTF-8");
                fonction_de_hachage.update(buffer);                
                
                resume=fonction_de_hachage.digest();
                str="0x";
                for(byte b : resume)
                    str=str+String.format("%02X",b);
                return str;
                    
        }
        
        
	void resume(String msg,String file) throws NoSuchAlgorithmException, UnsupportedEncodingException{		
                String str = header+"X-UdC_authentique: "+calculehash(corps)+"\r\n\r\n"+corps;                
                File source = new File(file);
                File dest = new File("email1-secure.txt");
                OutputStreamWriter o;
            try {
                o = new OutputStreamWriter(new FileOutputStream(dest),"UTF-8");
                 o.write(str);                         
                 o.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(cert.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(cert.class.getName()).log(Level.SEVERE, null, ex);
            }
                       				              
	}

	public static void main(String[] args) {
		cert c = new cert();
		try {
			c.readcorps(args[0]);
			c.resume(c.corps,args[0]);
		} catch (IOException | NoSuchAlgorithmException e) {
		}
	}
}