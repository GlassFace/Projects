/*
Faolan Project, a free Age of Conan server emulator made for educational purpose
Copyright (C) 2008 Project Faolan team

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

#ifndef CRYPTO_H_
#define CRYPTO_H_

#include <string>
#include <iostream>


namespace AoCCrypto {



	template < typename Result, typename Source >
	Result lexical_cast_from_hex(Source & value)
	{
		std::stringstream convertor;
		convertor << value;
		Result result;
		if (!(convertor >> std::hex >> result) || !convertor.eof())
		{
			//throw boost::bad_lexical_cast();
		}
		return result;
	}

	void TEAEncrypt(unsigned long* buffer, unsigned long* hashbytes)
	{
		unsigned int m = buffer[0];;
		unsigned int n = buffer[1];

		unsigned int sum = 0;

		for(int i = 0; i < 32; i++)
		{
			sum += 0x9e3779b9;

			m += ((n << 4) + hashbytes[0]) ^ ((n >> 5) + hashbytes[1]) ^ (sum + n);
			n += ((m << 4) + hashbytes[2]) ^ ((m >> 5) + hashbytes[3]) ^ (sum + m);
		}

		buffer[0] = m;
		buffer[1] = n;
	}

	void TEADecrypt(unsigned long* buffer, unsigned long* hashbytes)
	{
		unsigned int m = buffer[0];
		unsigned int n = buffer[1];

		unsigned int sum = 0xC6EF3720;

		for(int i = 0; i < 32; i++)
		{
			n -= ((m >> 5) + hashbytes[3]) ^ ((m << 4) + hashbytes[2]) ^ (sum + m);
			m -= ((n >> 5) + hashbytes[1]) ^ ((n << 4) + hashbytes[0]) ^ (sum + n);

			sum -= 0x9e3779b9;
		}

		buffer[0] = m;
		buffer[1] = n;
	}


	uint8* encrypt(char* message, int* newLen, unsigned long* key)
	{
		//Generate an 8 byte random number
		unsigned char random8bytes[8];

		for(int i = 0; i < 8; i++)
		{
			unsigned int r = rand();
			random8bytes[i] = (r&0x000000ff);
		}

		//TEMP
		random8bytes[0] = 0x60;
		random8bytes[1] = 0x60;
		random8bytes[2] = 0x60;
		random8bytes[3] = 0x60;
		random8bytes[4] = 0x60;
		random8bytes[5] = 0x60;
		random8bytes[6] = 0x60;
		random8bytes[7] = 0x60;
		//END OF TEMP

		//Create the message array
		*newLen = strlen(message) + 12;
		*newLen += 8 - *newLen % 8;
		unsigned char* encryptedMessage = new unsigned char[*newLen];
		memset(encryptedMessage, 0x20, *newLen);

		//Copy random 8 bytes
		memcpy(encryptedMessage, random8bytes, 8);

		//Copy len part
		int htonl_loginString_len = htonl(strlen(message));
		memcpy(encryptedMessage + 8, &htonl_loginString_len, sizeof(int));

		//Copy loginString
		memcpy(encryptedMessage + 12, message, strlen(message));

		unsigned long* buflocation = (unsigned long*)encryptedMessage;

		for(int i = 0; i < *newLen/4; i+=2)
		{
			if (i > 0)
			{
				buflocation[i] ^= buflocation[i-2];
				buflocation[i+1] ^= buflocation[i-1];
			}

			TEAEncrypt(buflocation + i, key);
		}

		return encryptedMessage;

	}

	uint8* decrypt(unsigned char* message, int len, unsigned long* key)
	{
		unsigned char* buf = new unsigned char[len+1];
		buf[len] = '\0';
		memcpy(buf, message, len);

		unsigned long* oldArr = (unsigned long*)message;
		unsigned long* newArr = (unsigned long*)buf;

		for(int i = 0; i < len/4; i+=2)
		{
			TEADecrypt(newArr + i, key);

			if (i > 1)
			{
				newArr[i] ^= oldArr[i-2];
				newArr[i+1] ^= oldArr[i-1];
			}
		}

		return buf;
	}

	void AoCDecrypt(std::string& decryptedMessage, std::string& encryptedMessage, const char* key)
	{

		uint32 encryptedMessageSize = encryptedMessage.size();

		char* encryptedString = new char[encryptedMessageSize*2+1];
		encryptedString[encryptedMessageSize*2] = '\0';
		unsigned char key16Bytes[16];

		char* pattern = new char[5];
		pattern[0] = '0'; pattern[1] = 'x'; pattern[4] = '\0';
		
		for(uint32 i=0; i < (encryptedMessageSize / 2 ); i++){

			pattern[2]=encryptedMessage[0+i*2];
			pattern[3]=encryptedMessage[1+i*2];

			unsigned short s = lexical_cast_from_hex<unsigned short>(pattern);
			unsigned char*conv = (unsigned char *)&s;

			encryptedString[i] = conv[0];

		}

		printf("Encrypted String : %s", encryptedString);

		for(int i = 0; i < 16; i++)
		{
			unsigned int c;
			sscanf(key + i*2, "%2x", &c);
			key16Bytes[i] = (c&0x0000ff);
		}

		uint8* rawDecrypt;
		rawDecrypt = decrypt((unsigned char*)encryptedString, (encryptedMessageSize / 2 ), (unsigned long*)key16Bytes);
		decryptedMessage.append(rawDecrypt,rawDecrypt+(encryptedMessageSize / 2 ));
		delete[] encryptedString;
		delete[] rawDecrypt;
		delete[] pattern;
	}

}

#endif
