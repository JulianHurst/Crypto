CC = gcc

INCLUDE = -I/usr/local/include -I/usr/include
LIB = -L/usr/local/lib -L/usr/lib -lm -lssl -lcrypto -g -Wall

all: mon_RC4

mon_RC4: mon_RC4.c
	$(CC) -o mon_RC4 $(INCLUDE) mon_RC4.c $(LIB)

clean:
	-rm *.o *~
	-rm mon_RC4
