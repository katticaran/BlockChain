import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class BlockChainDriver {


    public static void main(String[] args) throws NoSuchAlgorithmException {

        Scanner scanner = new Scanner(System.in);
        boolean exitFlag = false;
        String output;
        //used to consume the extra blank space characters.
        @SuppressWarnings("unused")
        String extraLine;
        int inputAmount;



        //Check conditions
        if (args.length != 1) {
            System.out.println("Error: Improper Usage");
            System.out.println("Usage: BlockChainDriver <amount>");
            scanner.close();
            return;
        }
        int amount = Integer.parseInt(args[0]);
        if(amount < 0) {
            System.out.println("Error: Improper Usage");
            System.out.println("The amount should be non-negative");
            scanner.close();
            return;
        }
        BlockChain newBlockChain = new BlockChain(amount);

        while (!exitFlag) {
            System.out.print(newBlockChain.toString());
            System.out.print("Command? ");

            String input = scanner.nextLine();
            String caseInput = input.toLowerCase();

            //handle the input
            switch (caseInput) {

            case "mine":
                System.out.print("Amount transferred? ");
                inputAmount = scanner.nextInt();
                extraLine = scanner.nextLine();
                Block newBlock = newBlockChain.mine(inputAmount);
                output = String.format("amount = %d, nonce = %d",
                        inputAmount, newBlock.getNonce());
                System.out.println(output);
                break;


            case "append":
                System.out.print("Amount transferred? ");
                inputAmount = scanner.nextInt();
                System.out.print("Nonce? ");
                long inputNonce = scanner.nextLong();
                extraLine = scanner.nextLine();
                newBlock = new Block(newBlockChain.getSize(), inputAmount, 
                        newBlockChain.getHash(), inputNonce);
                newBlockChain.append(newBlock);
                break;



            case "remove":
                newBlockChain.removeLast();
                break;

            case "check":
                if(newBlockChain.isValidBlockChain()) {
                    System.out.println("Chain is valid!");
                }
                else {
                    System.out.println("Chain is invalid!");
                }
                break;

            case "report":
                newBlockChain.printBalances();
                break;

            case "help":
                System.out.println("Valid commands:\n" + 
                        "    mine: discovers the nonce for a given transaction\n" + 
                        "    append: appends a new block onto the end of the chain\n" + 
                        "    remove: removes the last block from the end of the chain\n" + 
                        "    check: checks that the block chain is valid\n" + 
                        "    report: reports the balances of Alice and Bob\n" + 
                        "    help: prints this list of commands\n" + 
                        "    quit: quits the program\n\n");
                break;

            case "quit":
                exitFlag = true;
                break;

            default:
                System.out.println("Invalid option. Try again!");
                break;

            }
            System.out.println("\n");
        }
        scanner.close();
    }

}
