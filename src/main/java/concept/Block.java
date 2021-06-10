package blockchain.concept;

import java.util.Date;

public class Block {

    /** record when it was created */
    private Long timestamp;
    /** hash of the block came before it */
    private String lastHash;
    /** the hash itself for the new block, generated for all of its own data */
    private String hash;
    /** the data we want to store: a string, an array of anything
     * or a object representing transaction or any other kind of data */
    private String data;

    Block(Long timestamp, String lastHash, String hash, String data) {
        this.timestamp = timestamp;
        this.lastHash = lastHash;
        this.hash = hash;
        this.data = data;
    }

    public static Block genesis() {
        return new Block(
                new Date().getTime(),
                "Yes-I-am-coming-soon.Amen.Come-Lord-Jesus",
                "The-stone-the-builders-rejected-has-become-the-cornerstone",
                "Book of Genesis 1.1");
    }

    /** creates hash for the new mined block */
    public static Block mineBlock(Block lastBlock, String data) {
        Long timestamp = new Date().getTime();
        String lastHash = lastBlock.hash;
        String hash = CryptoHash.calculateHash(timestamp, lastHash, data);

        return new Block(
                timestamp,
                lastHash,
                hash,
                data
        );
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getLastHash() {
        return lastHash;
    }

    public String getHash() {
        return hash;
    }

    public Object getData() {
        return data;
    }
}
