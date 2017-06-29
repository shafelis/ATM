

package com.ATMTestCase;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;



public class ATM {


    public static final BigDecimal MAX_DEPOSIT_PER_DAY = new BigDecimal(150000);
    public static final BigDecimal MAX_DEPOSIT_PER_TRANSACION = new BigDecimal(40000);
    public static final int MAX_DEPOSIT_FREQUENCY = 4;


    public static final BigDecimal MAX_WITHDRAWAL_PER_DAY = new BigDecimal(50000);
    public static final BigDecimal MAX_WITHDRAWAL_PER_TRANSACION = new BigDecimal(20000);
    public static final int MAX_WITHDRAWAL_FREQUENCY = 3;

    private BigDecimal balance;
    private String errorMsg;

    private HashMap<LocalDate, TransactionsDaily> dailyDepositList;
    private HashMap<LocalDate, TransactionsDaily> dailyWithdrawalList;

    public boolean isStringValidPositiveNumber(String input){

        try
        {
            double d = Double.parseDouble(input);
            if (d <= 0)
                return false;
        }
        catch(NumberFormatException e )
        {
            return false;
        }

        return true;
    }

    public BigDecimal getBalance(){
        this.balance.setScale(2, BigDecimal.ROUND_HALF_EVEN );
        return this.balance;
    }

    public String getErrorMsg(){
        return this.errorMsg;
    }

    public void increaseBalance(BigDecimal deposit){

     
        if(deposit.compareTo(MAX_DEPOSIT_PER_TRANSACION) > 0){
            this.errorMsg = String.format("Deposit rejected - Deposit exceeds max transaction limit %s ", MAX_DEPOSIT_PER_TRANSACION.toPlainString());
            System.out.format(this.errorMsg);
            return;
        }

        if (dailyDepositList.containsKey(LocalDate.now())){

            if (dailyDepositList.get(LocalDate.now()).transactionFrequency>= MAX_DEPOSIT_FREQUENCY){
                this.errorMsg = String.format("Deposit rejected - More than %d deposits per day are not allowed", MAX_DEPOSIT_FREQUENCY);
                System.out.format(this.errorMsg);
                return;
            }

            BigDecimal proposedDailySum =  dailyDepositList.get(LocalDate.now()).dailySum.add(deposit);

            if ( proposedDailySum.compareTo(MAX_DEPOSIT_PER_DAY) > 0) {
                this.errorMsg = String.format("Deposit rejected - Daily total deposit limit %s exceeded. ", MAX_DEPOSIT_PER_DAY.toPlainString());
                System.out.format(this.errorMsg);
                return;
            }

        }

       
        if(dailyDepositList.containsKey(LocalDate.now())){
          
            if (dailyDepositList.get(LocalDate.now()).transactionFrequency  < MAX_DEPOSIT_FREQUENCY){
                this.balance = (this.balance).add(deposit);
                System.out.format("Updated Balance: %s %n", this.getBalance());
                Integer depositFrequency = dailyDepositList.get(LocalDate.now()).transactionFrequency + 1;
                BigDecimal newDailyDepositsSum = dailyDepositList.get(LocalDate.now()).dailySum.add(deposit);
                TransactionsDaily d = new TransactionsDaily(depositFrequency, newDailyDepositsSum);
                dailyDepositList.put(LocalDate.now(), d);
                return;
            }

        }
        else {
           
            this.balance = (this.balance).add(deposit);
            System.out.format("Updated Balance: %s %n", this.getBalance());
            TransactionsDaily d = new TransactionsDaily(1, deposit);
            dailyDepositList.put(LocalDate.now(), d);
            return;
        }

    }

    public void decreaseBalance(BigDecimal withdrawal){

      
        if (balance.compareTo(withdrawal) < 0){
            this.errorMsg = String.format("Withdrawal rejected - Cannot withdraw when balance is less than withdrawal amount");
            System.out.println(this.errorMsg);
            return;
        }

        if(withdrawal.compareTo(MAX_WITHDRAWAL_PER_TRANSACION) > 0){
            this.errorMsg = String.format("Withdrawal rejected - Withdrawal exceeds max transaction limit %s", MAX_WITHDRAWAL_PER_TRANSACION.toPlainString());
            System.out.format(this.errorMsg);
            return;
        }


        if(dailyWithdrawalList.containsKey(LocalDate.now())){

            if ( dailyWithdrawalList.get(LocalDate.now()).transactionFrequency>= MAX_WITHDRAWAL_FREQUENCY){
                this.errorMsg = String.format("Withdrawal rejected - More than %d per day are not allowed", MAX_WITHDRAWAL_FREQUENCY);
                System.out.format(this.errorMsg);
                return;
            }

            BigDecimal proposedDailySum = dailyWithdrawalList.get(LocalDate.now()).dailySum.add(withdrawal);

            if ( proposedDailySum.compareTo(MAX_WITHDRAWAL_PER_DAY) > 0) {
                this.errorMsg = String.format("Withdrawal rejected - Daily total withdrawal limit %s will be exceeded.", MAX_WITHDRAWAL_PER_DAY.toPlainString());
                System.out.println(this.errorMsg);
                return;
            }
        }

       
        if(dailyWithdrawalList.containsKey(LocalDate.now())){
          
            if (dailyWithdrawalList.get(LocalDate.now()).transactionFrequency < MAX_WITHDRAWAL_FREQUENCY){
                this.balance = (this.balance).subtract(withdrawal);
                System.out.format("Updated Balance: %s %n", this.getBalance());
                Integer withdrawalFrequency = dailyWithdrawalList.get(LocalDate.now()).transactionFrequency + 1;
                BigDecimal newDailyWithdrawalSum = dailyWithdrawalList.get(LocalDate.now()).dailySum.add(withdrawal);
                TransactionsDaily d = new TransactionsDaily(withdrawalFrequency, newDailyWithdrawalSum);
                dailyWithdrawalList.put(LocalDate.now(), d);
                return;
            }

        }
        else {
           
            this.balance = (this.balance).subtract(withdrawal);
            System.out.format("Updated Balance: %s %n", this.getBalance());
            TransactionsDaily d = new TransactionsDaily(1, withdrawal);
            dailyWithdrawalList.put(LocalDate.now(), d);
            return;
        }

    }

    public void displayMainMenuScreen(){

        StringBuilder mainMenu = new StringBuilder();

        mainMenu.append("*******************************")
                .append(System.getProperty("line.separator"))
                .append("1. Balance")
                .append(System.getProperty("line.separator"))
                .append("2. Deposit")
                .append(System.getProperty("line.separator"))
                .append("3. Withdrawal")
                .append(System.getProperty("line.separator"))
                .append("4. Quit")
                .append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("Enter Menu Option ")
                .append(System.getProperty("line.separator"))
                .append("> ");

        System.out.print(mainMenu);
    }

    public void selectChoice(Scanner input){

        int quitCounter = 0;

        this.displayMainMenuScreen();
        String option = input.next();

        while (quitCounter != 2){



            switch (option.toLowerCase()){

                case "1":
                case "balance":

                    System.out.println("*************************************");
                    System.out.println("BALANCE");
                    System.out.println(this.getBalance());
                    System.out.println();

                    System.out.println("Type 'menu' and press enter to go back to Main Menu ");
                    option = input.next();
                    if (!option.toLowerCase().equals("menu")){
                        option = "Invalid";
                    }

                    break;

                case "2":
                case "deposit":
                    System.out.format("Current Balance: %s%n", this.getBalance().toPlainString());

                    System.out.println("Enter amount and press enter (or type 'menu' and press enter to go back to main menu)");

                   
                    option = input.next();

                    if (option.toLowerCase().equals("menu")){
                        break;
                    }

                   
                    if (isStringValidPositiveNumber(option)){
                        BigDecimal deposit = new BigDecimal(option);
                        deposit.setScale(2, BigDecimal.ROUND_HALF_EVEN );

                        this.increaseBalance(deposit);
                        option = "menu";
                        break;
                    }
                    else{
                        option = "invalid";
                        break;
                    }


                case "3":
                case "withdrawal":

                    System.out.format("Current Balance: %s%n", this.getBalance().toPlainString());

                    System.out.println("Enter amount and press enter (or type 'menu' and press enter to go back to main menu)");

                    option = input.next();

                    if (option.toLowerCase().equals("menu")){
                        break;
                    }

                   
                    if (isStringValidPositiveNumber(option)){

                        BigDecimal withdrawal = new BigDecimal(option);
                        withdrawal.setScale(2, BigDecimal.ROUND_HALF_EVEN );
                        this.decreaseBalance(withdrawal);

                        option = "menu";
                        break;
                    }
                    else{
                        option = "invalid";
                        break;
                    }


                case "4":
                case "quit":

                    quitCounter = quitCounter + 1;
                    System.out.println("Are you sure you want to Quit - Y/N");
                    String yesOrNo = input.next();
                    if (yesOrNo.equals("Y") || yesOrNo.equals("y")){
                        quitCounter = quitCounter + 1;
                    }
                    else {
                        quitCounter = 0;
                        option = "menu";
                    }
                    break;

                case "menu":

                    this.displayMainMenuScreen();
                    option = input.next();
                    break;

                default:
                    System.out.println("Please enter a valid menu selection");
                    this.displayMainMenuScreen();
                    option = input.next();
                    break;
            }

        }



        input.close();
    }

    public ATM(){
        this.balance = new BigDecimal(0);
        this.dailyDepositList = new HashMap<LocalDate, TransactionsDaily>();
        this.dailyWithdrawalList = new HashMap<LocalDate, TransactionsDaily>();
        this.errorMsg = "";
    }


    public static void main(String[] args) {

        ATM sampleATM = new ATM();
        Scanner input = new Scanner(System.in);
        sampleATM.selectChoice(input);


    }


}
