**epp** permet de calculer des nombres (probablements) premiers très grands (représentés par des entiers GMP en C) grâce au test de primalité de Miller-Rabin : 
* **int est_probablement_premier()** calcule aléatoirement un nombre impaire de 1024 bits puis détermine s'il est très probablement premier. La fonction renvoie 1 s'il est très probablement premier et 0 sinon. 
* **int est_temoin(mpz_t a,mpz_t n)** calcule si *a* est temoin de *n*. Renvoie 1 si *a* est témoin et 0 sinon.

Le programme final calcule la proportion de nombres premiers parmi 10^6 tirages ainsi que le nombre moyen de tentatives nécessaires pour trouver un nombre premier de 1024 bits.
Il ne prend aucun argument et affiche les résultats directement sur la sortie standard.
