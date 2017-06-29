
package com.ATMTestCase;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ATMTestCase.ATM;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Scanner;

import static org.junit.Assert.*;


@RunWith(JUnitParamsRunner.class)
public class ATMTest {

    @Test
    //Base Case: Happy path
    public void testBalanceIncrease() {
        ATM sampleATM = new ATM();
        BigDecimal input = new BigDecimal(10);
        sampleATM.increaseBalance(input);
        assertEquals(BigDecimal.TEN, sampleATM.getBalance());
    }

    @Test
    //Base Case: Happy path
    public void testBalanceDecrease() {
        ATM sampleATM = new ATM();
        BigDecimal deposit = new BigDecimal(1000);
        sampleATM.increaseBalance(deposit);

        BigDecimal withdrawal = new BigDecimal(100);
        sampleATM.decreaseBalance(withdrawal);

        BigDecimal balance = new BigDecimal(900);
        assertEquals(balance, sampleATM.getBalance());
    }

    @Test
    //Base Case: Boundary Value Test, Outlined in Spec
    public void testMaxDepositPerDay() {
        ATM sampleATM = new ATM();

        sampleATM.increaseBalance(BigDecimal.valueOf(40000));
        sampleATM.increaseBalance(BigDecimal.valueOf(40000));
        sampleATM.increaseBalance(BigDecimal.valueOf(40000));
        sampleATM.increaseBalance(BigDecimal.valueOf(30000));

        assertEquals(BigDecimal.valueOf(150000), sampleATM.getBalance());

    }

    @Test
    //Base Case: Negative Test, Outlined in Spec
    public void testMaxDepositPerDayExceeded() {
        ATM sampleATM = new ATM();

        sampleATM.increaseBalance(BigDecimal.valueOf(40000));
        sampleATM.increaseBalance(BigDecimal.valueOf(40000));
        sampleATM.increaseBalance(BigDecimal.valueOf(40000));
        sampleATM.increaseBalance(BigDecimal.valueOf(30000.01));

        assertEquals(BigDecimal.valueOf(120000), sampleATM.getBalance());

        String expectedErrMsg = "Deposit rejected - Daily total deposit limit 150000 exceeded. ";
        assertEquals(expectedErrMsg, sampleATM.getErrorMsg());


    }

    @Test
    //Base Case: Boundary Value Test, Outlined in Spec
    public void testDepositEqualToMaxLimitPerTransaction() {
        ATM sampleATM = new ATM();
        BigDecimal input = ATM.MAX_DEPOSIT_PER_TRANSACION;
        sampleATM.increaseBalance(input);

        assertEquals(ATM.MAX_DEPOSIT_PER_TRANSACION, sampleATM.getBalance());

    }

    @Test
    //Base Case: Negative Test, Outlined in Spec
    public void testDepositGreaterThanMaxLimitPerTransaction() {
        ATM sampleATM = new ATM();
        BigDecimal input = ATM.MAX_DEPOSIT_PER_TRANSACION.add(BigDecimal.ONE);
        sampleATM.increaseBalance(input);

        assertEquals(BigDecimal.ZERO, sampleATM.getBalance());

        String expectedErrMsg = "Deposit rejected - Deposit exceeds max transaction limit 40000 ";
        assertEquals(expectedErrMsg, sampleATM.getErrorMsg());
    }

    @Test
    //Base Case: Negative Test, Outlined in Spec
    public void testMaxDepositFrequency() {
        ATM sampleATM = new ATM();
        BigDecimal input = new BigDecimal(100);
        //Deposit 5 times (1 more than MAx deposit frequency of 4
        //ToDo: Better to do this as a loop with MAX_Deposit_Frequency +1
        sampleATM.increaseBalance(input);
        sampleATM.increaseBalance(input);
        sampleATM.increaseBalance(input);
        sampleATM.increaseBalance(input);
        sampleATM.increaseBalance(input);

        //4 deposits were successful, 5th one should error out
        BigDecimal expected = new BigDecimal(400);
        assertEquals(expected, sampleATM.getBalance());

        String expectedErrMsg = "Deposit rejected - More than 4 deposits per day are not allowed";
        assertEquals(expectedErrMsg, sampleATM.getErrorMsg());


    }

    @Test
    //Base Case: Boundary Value Test, Outlined in Spec
    public void testMaxWithdrawalPerDay() {
        ATM sampleATM = new ATM();

        //First deposit, then withdraw 1 cent more than max. withdrawal limit of 50K per day
        sampleATM.increaseBalance(BigDecimal.valueOf(40000));
        sampleATM.increaseBalance(BigDecimal.valueOf(40000));

        sampleATM.decreaseBalance(BigDecimal.valueOf(20000));
        sampleATM.decreaseBalance(BigDecimal.valueOf(20000));
        sampleATM.decreaseBalance(BigDecimal.valueOf(10000));

        assertEquals(BigDecimal.valueOf(30000), sampleATM.getBalance());

    }

    @Test
    //Base Case: Negative Test, Outlined in Spec
    public void testMaxWithdrawalPerDayExceeded() {
        ATM sampleATM = new ATM();

        //First deposit, then withdraw 1k more than max. withdrawal limit of 50K per day
        sampleATM.increaseBalance(BigDecimal.valueOf(40000));
        sampleATM.increaseBalance(BigDecimal.valueOf(40000));

        sampleATM.decreaseBalance(BigDecimal.valueOf(20000));
        sampleATM.decreaseBalance(BigDecimal.valueOf(20000));
        sampleATM.decreaseBalance(BigDecimal.valueOf(10000.01));

        assertEquals(BigDecimal.valueOf(40000), sampleATM.getBalance());

        String expectedErrMsg = "Withdrawal rejected - Daily total withdrawal limit 50000 will be exceeded.";
        assertEquals(expectedErrMsg, sampleATM.getErrorMsg());


    }


    @Test
    //Base Case: Boundary Value Test, Outlined in Spec
    public void testWithdrawalEqualToMaxLimitPerTransaction() {
        ATM sampleATM = new ATM();

        //First increase balance then withdraw 1k greater than max limit per transaction

        sampleATM.increaseBalance(BigDecimal.valueOf(40000));
        sampleATM.decreaseBalance(BigDecimal.valueOf(20000));

        assertEquals(BigDecimal.valueOf(20000), sampleATM.getBalance());

    }

    @Test
    //Base Case: Negative Test, Outlined in Spec
    public void testWithdrawalGreaterThanMaxLimitPerTransaction() {
        ATM sampleATM = new ATM();

        //First increase balance then withdraw 1k greater than max limit per transaction

        sampleATM.increaseBalance(BigDecimal.valueOf(40000));
        sampleATM.decreaseBalance(BigDecimal.valueOf(20000.01));

        assertEquals(BigDecimal.valueOf(40000), sampleATM.getBalance());

        String expectedErrMsg = "Withdrawal rejected - Withdrawal exceeds max transaction limit 20000";
        assertEquals(expectedErrMsg, sampleATM.getErrorMsg());
    }

    @Test
    //Base Case: Negative Test, Outlined in Spec
    public void testMaxWithdrawalFrequency() {
        ATM sampleATM = new ATM();
        BigDecimal input = new BigDecimal(1000);
        //First increase balance, then do more than 3 withdrawals
       
        sampleATM.increaseBalance(input);

        sampleATM.decreaseBalance(BigDecimal.valueOf(100));
        sampleATM.decreaseBalance(BigDecimal.valueOf(100));
        sampleATM.decreaseBalance(BigDecimal.valueOf(100));
        sampleATM.decreaseBalance(BigDecimal.valueOf(100));

        //3 withdrawals per day are successful, 4th one should error out
        BigDecimal expected = new BigDecimal(700);
        assertEquals(expected, sampleATM.getBalance());

        String expectedErrMsg = "Withdrawal rejected - More than 3 per day are not allowed";
        assertEquals(expectedErrMsg, sampleATM.getErrorMsg());


    }


    @Test
    //Base Case: Boundary Value Test, Outlined in Spec
    public void testWithdrawalEqualToBalance() {
        ATM sampleATM = new ATM();
        BigDecimal input = new BigDecimal(1000);

        //First deposit then withdraw 1 cent more than deposited amount
        sampleATM.increaseBalance(input);
        sampleATM.decreaseBalance(input);

        assertEquals(BigDecimal.ZERO, sampleATM.getBalance());

    }

    @Test
    //Base Case: Negative Test
    public void testWithdrawalWhenBalanceIsZero() {
        ATM sampleATM = new ATM();
        BigDecimal withdraw = new BigDecimal(1000);

        sampleATM.decreaseBalance(withdraw);

        assertEquals(BigDecimal.ZERO, sampleATM.getBalance());

        String expectedErrMsg = "Withdrawal rejected - Cannot withdraw when balance is less than withdrawal amount";
        assertEquals(expectedErrMsg, sampleATM.getErrorMsg());

    }

    @Test
    //Base Case: Negative Test, Outlined in Spec
    public void testWithdrawalGreaterThanBalance() {
        ATM sampleATM = new ATM();
        BigDecimal input = new BigDecimal(1000);

        //First deposit then withdraw 1 cent more than deposited amount
        sampleATM.increaseBalance(input);
        sampleATM.decreaseBalance(input.add(BigDecimal.valueOf(0.01)));

        assertEquals(input, sampleATM.getBalance());

        String expectedErrMsg = "Withdrawal rejected - Cannot withdraw when balance is less than withdrawal amount";
        assertEquals(expectedErrMsg, sampleATM.getErrorMsg());
    }

    @Test
    @Parameters({
            "123.10, true",
            "100, true",
            "0.01, true",
            "0, false",
            "-1, false",
            "Test, false",
            ", false",
            "!@#$%^&*()_+, false"})
    public void testIsValidPositiveNumber(String input, Boolean valid) {
        ATM sampleATM = new ATM();

        assertTrue(sampleATM.isStringValidPositiveNumber(input) == valid);

    }

    @Test
    public void testDisplayMenuScreen() {

        PrintStream originalStdout = System.out;

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ATM sampleATM = new ATM();
        sampleATM.displayMainMenuScreen();

        StringBuilder expectedOutput = new StringBuilder();

        expectedOutput.append("*******************************")
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

        assertEquals(expectedOutput.toString(), outContent.toString());
        System.setOut(originalStdout);
    }

    @Test
    @Parameters({
            "1 menu 4 Y Y",
            "1 men 4 Y Y",
            "1 menu 4 Y N 4 Y Y",
            "1 menu 4 ? 4 Y Y",
            "1 menu 2 1000 4 Y Y",
            "1 menu 2 ? 1000 4 Y Y",
            "1 menu 2 1000 3 100 4 Y Y",
            "1 menu 2 1000 3 % 100 4 Y Y",
            "1 menu 2 menu 4 Y Y",
            "1 menu 3 menu 4 Y Y",
            "1 menu 2 menu 4 Y ? 4 Y Y"})
    //ToDo: Add asserts to make the tests more robust. Currently, it just checks there are no runtime errors with above input sequences
    public void testSelectChoice(String input_sequence) {
        ATM sampleATM = new ATM();

        InputStream originalStdin = System.in;

        //String input_sequence = "1 menu 4 Y Y";
        InputStream in = new ByteArrayInputStream(input_sequence.getBytes());
        System.setIn(in);

        Scanner fake_console = new Scanner(System.in);
        sampleATM.selectChoice(fake_console);

        System.setIn(originalStdin);
    }


}