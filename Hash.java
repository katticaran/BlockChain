import java.util.Arrays;

public class Hash {
    private byte[] data;

    /**
     * Hash: Constructor
     * @param an array of bytes
     */
    public Hash(byte[] data){
        this.data = data;
    }

    /**
     * getData: a getter method that returns data.
     * @return byte[].
     */
    public byte[] getData() {
        return this.data;
    }

    /**
     * isValid: Checks if data is valid in the object
     * @return boolean.
     */
    public boolean isValid() {
        String hexVal = toString();
        return (hexVal.startsWith("000"));
    }

    /**
     * toString: Converts the bits to Hex and returns the hex string
     * @return String
     */
    public String toString() {
        String ret = javax.xml.bind.DatatypeConverter.printHexBinary(this.data);
        return ret;
    }

    /**
     * equals: Returns true if the argument passed is an instance of Hash with the same
     * data field as this object.
     * @return boolean
     */

    public boolean equals(Object other) {
        if (!(other instanceof Hash)) {
            return false;
        }
        Hash newObj = (Hash) other;
        if (Arrays.equals(this.data, newObj.data)){
            return true;
        }

        return false;

    }
}
