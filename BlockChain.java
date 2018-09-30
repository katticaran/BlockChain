import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class BlockChain {
    public class Node{
        public Block nodeBlock;
        public Node next;

        public Node(Block block, Node next) {
            this.nodeBlock = block;
            this.next = next;
        }
    }

    private Node first;
    private Node last;
    private int size;
    private int total;
    private int initial;

    /**
     * BlockChain: Constructor.
     */
    public BlockChain(int initial) throws NoSuchAlgorithmException {
        Block newBlock = new Block(0, initial, null);
        Node newNode = new Node(newBlock, null);
        this.total = initial;
        this.initial = initial;
        this.first = newNode;
        this.last = newNode;
        this.size = 1;
    }

    /**
     * mine: creates a new block for the given amount and calculates the nonce.
     * @return Block
     */
    public Block mine(int amount) throws NoSuchAlgorithmException {
        Block newBlock = new Block(this.size, amount, this.last.nodeBlock.getHash());
        return newBlock;
    }


    /**
     * getSize: a getter method that returns the size of the blockChain.
     * @return int
     */
    int getSize() {
        return this.size;
    }


    /**
     * append: appends the given block to the end of the blockchain
     * @param Block ( The block to be appended to the blockchain)
     */
    void append (Block blk) {
        if (blk == null) {
            throw new IllegalArgumentException();
        }
        if (this.total + blk.getAmount() < 0) {
            throw new IllegalArgumentException();
        }
        Node newNode = new Node(blk, null);
        this.last.next = newNode;
        this.last = newNode;  
        this.size++;
        this.total += blk.getAmount();

    }


    /**
     * removeLast: removes the last node in the block if the block 
     * has more than one node. Returns true if successfully removed
     * @return boolean
     */
    public boolean removeLast() {
        if (this.size<=1) {
            return false;
        }

        this.total -= this.last.nodeBlock.getAmount(); 
        Node curNode = this.first;
        while(curNode.next.next != null) {
            curNode = curNode.next;
        }
        this.last = curNode;
        this.last.next = null;
        this.size--;
        return true;      
    }


    /**
     * getHash: a getter method that returns the field Hash.
     * (which is the hash of the current node)
     * @return Hash
     */
    public Hash getHash() {
        return this.last.nodeBlock.getHash();
    }

    boolean isValidBlockChain() throws NoSuchAlgorithmException {

        Node curNode = this.first;
        Hash savedPrevHash = curNode.nodeBlock.getHash();
        MessageDigest md = MessageDigest.getInstance("sha-256");

        while (curNode != null) {

            byte[] numByte = ByteBuffer.allocate(4).putInt(curNode.nodeBlock.getNum()).array();
            byte[] amtByte = ByteBuffer.allocate(4).putInt(curNode.nodeBlock.getAmount()).array();
            byte[] nonceByte = ByteBuffer.allocate(8).putLong(curNode.nodeBlock.getNonce()).array();

            md.update(numByte);
            md.update(amtByte);
            if (curNode.nodeBlock.getNum() > 0) {
                byte[] prevHashByte = savedPrevHash.getData();
                md.update(prevHashByte);
            }
            md.update(nonceByte);

            if (!(curNode.nodeBlock.getHash().isValid())) {
                return false;
            }
            Hash newHash = new Hash(md.digest());
            if (!( newHash.equals(curNode.nodeBlock.getHash()))) {
                return false;
            }

            savedPrevHash = newHash;
            curNode = curNode.next;
            if (curNode == null) {
                break;
            }

            if (!(Arrays.equals(curNode.nodeBlock.getPrevHash().getData(), savedPrevHash.getData()))) {
                return false;
            }
        }
        return true;
    }



    /**
     * printBalances: a method that prints the total amount transferred b/w the two parties.
     */
    public void printBalances() {
        String printVal = String.format("Alice: %d, Bob: %d ", this.total, this.initial - this.total);
        System.out.println(printVal);
    }

    /**
     * toString: a method that returns the contents of the blockChain as a String.
     * @returns String
     */
    public String toString() {
        Node curNode = this.first;
        StringBuilder retVal = new StringBuilder();


        while (curNode != null) {
            retVal.append(curNode.nodeBlock.toString());
            retVal.append("\n");
            curNode = curNode.next;
        }
        return retVal.toString();
    }

}
