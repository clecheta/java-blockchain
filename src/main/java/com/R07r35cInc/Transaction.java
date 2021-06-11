package com.R07r35cInc;

public class Transaction implements Tx {
	
	private String hash;
	private String value;
	
	public String hash() { return hash; }
	
	public Transaction(String value) {
		this.hash = CryptoHash.generateHash(value);
		this.setValue(value);		
	}

	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		
		// new value need to re-calc hash
		this.hash = CryptoHash.generateHash(value);
		this.value = value;
	}
	
	public String toString() { 
		return this.hash + " : "+this.getValue();		
	}
}