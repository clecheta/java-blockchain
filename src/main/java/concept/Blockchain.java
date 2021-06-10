package blockchain.concept;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {

    private List<Block> chain;

    Blockchain() {
        this.chain = new ArrayList<Block>();
        this.chain.add(Block.genesis());
    }

    /** add a new block to the chain based on the last block that came before it */
    public void addBlock(String data) {
        Block lastBlock = this.chain.get(this.chain.size()-1);
        Block newBlock = Block.mineBlock(lastBlock, data);

        this.chain.add(newBlock);
    }

    /** Chain Validation - inspect a Blockchain (inspect each block was construct correctly) */
    public Boolean isValidChain() {
        Block currentBlock;
        Block previousBlock;
        String validatedHash;

        //loop through blockchain to check hashes:
        for(int i=1; i < chain.size(); i++) {
            currentBlock = chain.get(i);
            previousBlock = chain.get(i-1);

            //compare registered hash and calculated hash:
            validatedHash = CryptoHash.calculateHash(currentBlock);
            if(!currentBlock.getHash().equals(validatedHash) ){
                System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.getHash().equals(currentBlock.getLastHash()) ) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
        }
        return true;
    }

}
