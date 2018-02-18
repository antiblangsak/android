package com.antiblangsak.antiblangsak.models;

/**
 * Created by Alica on 2/18/18.
 */

public class BankAccountModel {

    private int id;
    private String bankName;
    private String accountName;
    private String accountNumber;
    private String branchName;

    public BankAccountModel(int id, String bankName, String accountName, String accountNumber, String branchName) {
        this.id = id;
        this.bankName = bankName;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.branchName = branchName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
