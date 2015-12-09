// -*- coding: utf-8 -*-

#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include "gmp.h"
unsigned long int seed;
int est_temoin(mpz_t a, mpz_t n);

int est_probablement_premier()
{
  int i;
  mpz_t a[25];
  mpz_t m;
  mpz_t d;
  mpz_t r;
  mpz_t n;
  mpz_init(m);
  mpz_init(n);
  mpz_init(d);
  mpz_init(r);
  mpz_set_str(d,"2",10);
  mpz_pow_ui(r,d,1023);
  gmp_randstate_t state;
  gmp_randinit_default(state);
  gmp_randseed_ui(state, seed);
  mpz_urandomm(n,state,r);
  mpz_add(n,n,r);
  mpz_mod_ui(m,n,2);
  if(mpz_cmp_ui(m,0)==0)
    mpz_add_ui(n,n,1);
  for(i=0;i<25;i++){
    mpz_init(a[i]);
    mpz_urandomm(a[i],state,n);
    if(mpz_cmp_ui(a[i],0)==0)
      mpz_add_ui(a[i],a[i],1);
    if(est_temoin(a[i],n)){
      seed++;
      return 0;
    }
  }
  //gmp_printf("%Zd\n",n);
  return 1;
}

int est_temoin(mpz_t a, mpz_t n){
  mpz_t t;
  int s=0,i;
  mpz_t p;
  mpz_t r;
  mpz_t res;
  mpz_t mod;
  mpz_t puis;
  //init
  mpz_init(t);
  mpz_init(p);
  mpz_init(r);
  mpz_init(res);
  mpz_init(mod);
  mpz_init(puis);

  mpz_sub_ui(p,n,1);
  mpz_mod_ui(r,p,2);
  if(mpz_cmp_ui(r,0)==0){
    mpz_tdiv_q_ui(t,p,2);
    s++;
    mpz_mod_ui(r,t,2);
  }

  while(mpz_cmp_ui(r,0)==0){
    mpz_tdiv_q_ui(t,t,2);
    mpz_mod_ui(r,t,2);
    s++;
  }
  mpz_powm(res,a,t,n);
  if(mpz_cmp_ui(res,1)==0 || mpz_cmp(res,p)==0)
    return 0;
  while(s!=0){
    mpz_powm_ui(res,res,2,n);
    if(mpz_cmp_ui(res,1)==0)
      return 1;
    if(mpz_cmp(res,p)==0)
      return 0;
    s--;
  }
  return 1;              // a est un témoin
}

int main(int argc,char *argv[])
{
  /*if(argc!=2){
    fprintf(stderr,"USAGE : %s <nombre impair>\n",argv[0]);
    return 1;
  }*/
  int premier=0,essai=0;
  int *tent;
  tent=malloc(sizeof(int)*1000000);
  srand(time(NULL));
  seed=rand()%123456;
  mpz_t n;               // Déclaration de l'entier GMP n
  mpz_init(n);           // Initialisation de l'entier GMP n
  //mpz_set_str(n, "170141183460469231731687303715884105727", 10);
  //mpz_set_str(n, "7", 10);

  //mpz_set_str(n, argv[1], 10);
  //printf("Le nombre ");
  //gmp_printf("%Zd", n);  // Affichage de l'entier GMP n, en décimal
  for(int i=0;i<1000000;i++){
    if(est_probablement_premier()){
      premier++;
      essai++;
      //printf("%d\n",premier);
    }
    else
      tent[essai]++;
    seed=rand()%123456;
  }
  /*if (est_probablement_premier())
    printf(" est très probablement premier!\n");
  else
    printf(" n'est absolument pas premier!\n");*/
    int moyenne=0;
    for(int i=0;i<essai;i++)
      moyenne+=tent[i];
    moyenne/=essai;
  printf("\nnombre de nombres premiers : %d\n",premier);
  printf("Tentatives en moyenne : %d\n",moyenne);
  //printf("Estimation de temps : %d\n")
  return 0;
}


/*
> make
gcc -o epp -I/usr/local/include -I/usr/include epp.c -L/usr/local/lib -L/usr/lib -lgmp
> ./epp
Le nombre 170141183460469231731687303715884105727 est ...
>
*/
