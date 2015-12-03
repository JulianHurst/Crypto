// -*- coding: utf-8 -*-

#include <stdio.h>
#include <string.h>

#ifndef LG_FLUX
#define LG_FLUX 10
#endif
// Ce programme ne produira par défaut que les 10 premiers octets de le clef longue.

typedef unsigned char uchar; // Les octets sont non-signés.

void echange (uchar *state, int i, int j)
{
    uchar temp = state[i]; 
    state[i] = state[j]; 
    state[j] = temp; 
}

void initialisation(uchar *state, uchar clef[])
{
  int i, j, lg;
  
  for (i=0; i < 256; i++) state[i] = i;

  lg = strlen( (char*) clef);
  j = 0;
  for (i=0; i < 256; i++) {
    j = (j + state[i] + clef[i % lg]) % 256; 
    echange(state,i,j);           // Echange des octets en i et j
  }   
}

int main() 
{
  uchar state[256], clef[]={0x01, 0x02, 0x03, 0x04, 0x05};    
  initialisation(state, clef); // Phase d'initialisation de state   
  int i=0, j=0, w, k;    
  for (k=0; k < LG_FLUX; k++)  {
    i = (i + 1) % 256;            // Incrémentation de i modulo 256
    j = (j + state[i]) % 256;     // Déplacement de j
    echange(state,i,j);           // Echange des octets en i et j
    w = state[(state[i] + state[j]) % 256];
    printf("0x%02X ", w);         // Affichage d'un octet généré
  }   
  
  printf("\n");     
  return 0; 
}

/*
> make
gcc -o mon_RC4 -I/usr/local/include -I/usr/include mon_RC4.c -L/usr/local/lib -L/usr/lib -lm -lssl -lcrypto -g -Wall
> ./mon_RC4
0xB2 0x39 0x63 0x05 0xF0 0x3D 0xC0 0x27 0xCC 0xC3 
> gcc -o mon_RC4 -I/usr/local/include -I/usr/include mon_RC4.c -L/usr/lib -lm -g -Wall -DLG_FLUX=20
> ./mon_RC4
0xB2 0x39 0x63 0x05 0xF0 0x3D 0xC0 0x27 0xCC 0xC3 0x52 0x4A 0x0A 0x11 0x18 0xA8 0x69 0x82 0x94 0x4F 
> 
*/
