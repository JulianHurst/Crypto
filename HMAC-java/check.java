
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class check{       
    
    String hash;
    
    void readauthentique(String file) throws FileNotFoundException, IOException{
		boolean read,car1,car;		
                hash = "";
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"))) {
                for(String line; (line = br.readLine()) !=null ;) {                        
                    if(line.contains("X-UdC_authentique"))
                        hash=line.split(":")[1].trim();                                                                
                }
            }
            System.out.println("hash trouvé : "+hash);
            System.out.println();
    }
    
    boolean diff(String hash,String file) throws IOException, NoSuchAlgorithmException{
        cert c = new cert();
        c.readcorps(file);        
        System.out.println("hash veritable : "+cert.calculehash(c.corps));
        return cert.calculehash(c.corps).equals(hash);        
    }
    
    public static void main(String[] args){
        check c = new check();       
        try {
            c.readauthentique(args[0]);
            if(c.diff(c.hash, args[0]))
                System.out.println("OK");                            
            else
                System.out.println("Not Ok");
        } catch (IOException | NoSuchAlgorithmException ex) {
            Logger.getLogger(check.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}