import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Block {
    private int num;
    private int amount;
    private Hash prevHash;
    private long nonce;
    private Hash thisHash;

    /**
     * Block: Constructor
     * @param int num: The number of the block in the chain
     * @param int amount: the amount transferred between the two parties
     * @param Hash prevHash: the Hash of the previous node in the BlockChain
     */
    public Block(int num, int amount, Hash prevHash) throws NoSuchAlgorithmException{
        this.num = num;
        this.amount = amount;
        this.prevHash = prevHash;
        computeHash(0);
    }

    /**
     * Block: Constructor
     * @param int num: The number of the block in the chain
     * @param int amount: the amount transferred between the two parties
     * @param Hash prevHash: the Hash of the previous node in the BlockChain
     * @param long nonce: the nonce value of the block
     */
    public Block(int num, int amount, Hash prevHash, long nonce) throws NoSuchAlgorithmException {
        this.num = num;
        this.amount = amount;
        this.prevHash = prevHash; 
        this.nonce = nonce;
        computeHash(1);
    }

    /**
     * getNum: a getter method that returns the field num.
     * @return integer
     */
    public int getNum() {
        return this.num;
    }

    /**
     * getAmount: a getter method that returns the field amount.
     * @return integer
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * getNonce: a getter method that returns the field nonce.
     * @return long
     */
    public long getNonce() {
        return this.nonce;
    }

    /**
     * getPrevHash: a getter method that returns the field prevHash.
     * (which is the hash of the previous node)
     * @return Hash
     */
    public Hash getPrevHash() {
        return this.prevHash;
    }

    /**
     * getHash: a getter method that returns the field Hash.
     * (which is the hash of the current node)
     * @return Hash
     */
    public Hash getHash() {
        return this.thisHash;
    }

    /**
     * toString: returns a string that contains the block, amount
     * Nonce and hash value of the object.
     * @return String
     */
    public String toString() {
        String retString;
        if (this.prevHash == null) {
            retString = String.format("Block %d (Amount: %d, Nonce: %d, hash: %s)",
                    this.num, this.amount, this.nonce, this.thisHash.toString());
        } else {
            retString = String.format("Block %d (Amount: %d, Nonce: %d, prevHash: %s, hash: %s)",
                    this.num, this.amount, this.nonce, this.prevHash.toString(), 
                    this.thisHash.toString());
        }
        return retString;
    }

    /**
     * computeHash: Computes the hash value of the object
     */
    private void computeHash(int type) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("sha-256");
        byte[] numByte = ByteBuffer.allocate(4).putInt(this.num).array();
        byte[] amtByte = ByteBuffer.allocate(4).putInt(this.amount).array();
        byte[] prevHashByte = null;
        if (this.prevHash != null) {
            prevHashByte = this.prevHash.getData();
        }

        long nonce;
        Hash newHash;

        if (type == 0) {
            for(nonce = 0; ; nonce++) {
                byte[] nonceByte = ByteBuffer.allocate(8).putLong(nonce).array();
                md.update(numByte);
                md.update(amtByte);
                if (this.num != 0) {
                    md.update(prevHashByte);
                }
                md.update(nonceByte);

                newHash = new Hash(md.digest());
                if (newHash.isValid()) {
                    break;
                }
            }
            this.thisHash = newHash;
            this.nonce = nonce;
            return;

        } else {

            byte[] nonceByte = ByteBuffer.allocate(8).putLong(this.nonce).array();
            md.update(numByte);
            md.update(amtByte);
            if (this.num != 0) {
                md.update(prevHashByte);
            }
            md.update(nonceByte);

            newHash = new Hash(md.digest());
            if (newHash.isValid()) {
                this.thisHash = newHash;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
}

