// -*- coding: utf-8 -*-

public class MonRC4
{     
    static int[] state = new int[256];       // Ces int sont <256.
    static int i = 0, j = 0;                 // Ils représentent un octet.
    static int[] clef = {'K', 'Y', 'O', 'T', 'O'};     // La clef courte fait 5 octets
    static int lg = clef.length;
    private static int LG_FLUX = 10;
    // Ce programme ne produira que les 10 premiers octets de le clef longue.
     
    private static void echange(int k, int l)
    {	
	int temp = state[k]; 
	state[k] = state[l]; 
	state[l] = temp; 
    }

    private static void initialisation()
    {	
	for (i=0; i < 256; i++) state[i] = i;
	j = 0;
	for (int i=0; i < 256; i++) {
	    j = (j + state[i] + clef[i % lg]) % 256;
	    echange(i,j);                // Echange des octets en i et j
	}
    }

    public static void main(String[] args)
    {
	int w;	
	initialisation();
	i = 0;
	j = 0;
	for (int k = 0; k < LG_FLUX; k++) {
	    i = (i + 1) % 256;           // Incrémentation de i modulo 256
	    j = (j + state[i]) % 256;    // Déplacement de j
	    echange(i,j);                // Echange des octets en i et j
	    w = state[(state[i] + state[j]) % 256];
	    System.out.print(String.format("0x%02X ", w));
	                                 // Affichage d'un octet généré
	}
	System.out.print("\n");
    }
     
}
/*
> make
javac *.java 
$ java MonRC4
0xB2 0x39 0x63 0x05 0xF0 0x3D 0xC0 0x27 0xCC 0xC3
> 
*/
