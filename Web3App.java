package org.web3j;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.generated.contracts.ERC20;
import org.web3j.generated.contracts.HelloWorld;
import org.web3j.generated.contracts.JavaToken;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>This is the generated class for <code>web3j new helloworld</code></p>
 * <p>It deploys the Hello World contract in src/main/solidity/ and prints its address</p>
 * <p>For more information on how to run this project, please refer to our <a href="https://docs.web3j.io/latest/command_line_tools/#running-your-application">documentation</a></p>
 */
public class Web3App {

   private static final String nodeUrl = System.getenv().getOrDefault("WEB3J_NODE_URL", "http://172.172.196.97:8545");

   public static void main(String[] args) throws Exception {
        Credentials credentials = Credentials.create("50616c6d65697261734e616f54656d4d756e6469616c35314e616f436f6e7461");
        Web3j web3j = Web3j.build(new HttpService(nodeUrl));
        Web3App app = new Web3App();
        app.erc20Ops(web3j, credentials);
//        app.helloWorldOps(web3j, credentials);
   }

    public void erc20Ops(Web3j web3j, Credentials credentials) {
        try {
            System.out.println("Deploying ERC20 contract ...");
            StaticGasProvider gasProvider = new StaticGasProvider(new BigInteger("0"), new BigInteger("9000000"));
//            JavaToken token = JavaToken.deploy(web3j, credentials, gasProvider).send();
            JavaToken token = JavaToken.load("0x44167ea4476b5abf23755ad7f328193e34bf795b", web3j, credentials, new DefaultGasProvider());
            System.out.println("JavaToken Contract address: " + token.getContractAddress());
            System.out.println("token name is: " + token.name().send());
            System.out.println("token decimals is: " + token.decimals().send());


            String recipientAddr = "0x4e063FAc22663e02693E22065A239E49Bc1056dC";
            System.out.println("new balance is ");
            System.out.println(token.balanceOf(recipientAddr).send());

            System.out.println("Minting tokens to: " + recipientAddr + " ...");
            TransactionReceipt txReceipt = token.mint(recipientAddr, new BigInteger("10000000000000000000000", 10)).send();
            if (txReceipt.isStatusOK()) {
                System.out.println("new balance is ");
                System.out.println(token.balanceOf(recipientAddr).send());
            } else {
                System.err.println("transaction failed");
                System.err.println(txReceipt);
            }
        } catch (Exception ex) {
            System.out.println("Exception");
            System.err.println(ex.getLocalizedMessage());
        }
    }

   public void helloWorldOps(Web3j web3j, Credentials credentials) {
       try {
        //        System.out.println("Deploying HelloWorld contract ...");
        //        HelloWorld helloWorld = HelloWorld.deploy(web3j, credentials, new DefaultGasProvider(), "Hello Blockchain World!").send();
           System.out.println("Loading hello world");
           HelloWorld helloWorld = HelloWorld.load("0x1bd3d93712a06b8b21e1ddbd24a1b824021d750d", web3j, credentials, new DefaultGasProvider());
           System.out.println("Contract address: " + helloWorld.getContractAddress());
           System.out.println("Greeting method result: " + helloWorld.greeting().send());

           Date currentDate = new Date();
           SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           String formattedCurrentDateTime = dateFormat.format(currentDate);
           String newGreetingMsg = "Test new tx at " + formattedCurrentDateTime;
           System.out.println("changing the greeting to: " + newGreetingMsg + " ...");
           TransactionReceipt txReceipt = helloWorld.newGreeting(newGreetingMsg).send();
           if (txReceipt.isStatusOK()) {
               System.out.println("new greeting is ");
               System.out.println(helloWorld.greeting().send());
           } else {
               System.err.println("transaction failed");
               System.err.println(txReceipt);
           }
       } catch (Exception ex) {
           System.out.println(ex.getLocalizedMessage());
       }
   }
}

