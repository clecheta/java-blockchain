package blockchain.concept;

import com.google.gson.GsonBuilder;

public class TestChain {

    public static void main(String[] args) {
        //Test 1
        Block genesisBlock = Block.genesis();
        System.out.println("Hash for block 1 : " + genesisBlock.getHash());

        Block secondBlock = Block.mineBlock(genesisBlock, "Yo im the second block");
        System.out.println("Hash for block 2 : " + secondBlock.getHash());

        Block thirdBlock = Block.mineBlock(secondBlock, "Hey im the third block");
        System.out.println("Hash for block 3 : " + thirdBlock.getHash());

        //Test 2
        Blockchain blockchain = new Blockchain();
        blockchain.addBlock("Yo im the second block");
        blockchain.addBlock("Hey im the third block");

        System.out.println("\nValid: " + blockchain.isValidChain());
        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockchainJson);
    }

}
