**cert** takes a file as parameter, calculates the appendice h (MD5 of body + secret using opad and ipad) and adds the X-UdC_authentique : h field to the header of the email.

*	 input : email.txt
*	 output : email-secure.txt (email.txt with X-UdC_authentique : h added to header)

**check** takes a file as parameter and checks if the file contains the X-UdC_authentique : h field where h is the MD5 of body + secret (using opad and ipad).

* 	input : email-secure.txt
*	output : Ok if valid, Not Ok otherwise
