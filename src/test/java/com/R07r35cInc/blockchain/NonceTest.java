package com.R07r35cInc.blockchain;

import com.R07r35cInc.util.CryptoHash;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class NonceTest {

	@Test
	public void algorithmConcept() {
		String message = "Jesus love you!";
		System.out.println("Message: " + message);

		String hashValue = CryptoHash.generateHash(message);
		System.out.println(String.format("Hash: %s", hashValue));

		String nonceKey = "12345"; // E.x. "00000" :
		String zeroGoal = new String(new char[nonceKey.length()]).replace("\0", "0");

		long nonce = 0;
		boolean isNonceFound = false;
		String nonceHash = "";
         
		long start = System.currentTimeMillis();
		while (!isNonceFound) {
			nonceHash = CryptoHash.generateHash(message + nonce);
			isNonceFound = nonceHash.substring(0, nonceKey.length()).equals(zeroGoal);
			if (!isNonceFound) {
				nonce++;
			}
		}

		long ms = System.currentTimeMillis() - start;
		System.out.println(String.format("Nonce: %d", nonce));
		System.out.println(String.format("Nonce Hash: %s", nonceHash));
		System.out.println(String.format("Nonce Search Time: %s ms", ms));
		
		assertTrue(nonce > 0);
		assertTrue(nonceHash.startsWith("00000"));
	}

}
