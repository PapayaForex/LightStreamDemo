package com.allen.lightstreamdemo.bean;

import java.util.List;

/**
 * Created by: allen on 16/9/1.
 */

public class Session {
    /**
     * accountType : CFD
     * accountInfo : {"balance":2329658.5,"deposit":5596.8,"profitLoss":-130.16,"available":2323931.5}
     * currencyIsoCode : GBP
     * currentAccountId : A12345
     * lightstreamerEndpoint : https://apd.marketdatasystems.com
     * accounts : [{"accountId":"A12345","accountName":"CFD","preferred":true,"accountType":"CFD"}]
     * clientId : 100017136
     */

    private String accountType;
    /**
     * balance : 2329658.5
     * deposit : 5596.8
     * profitLoss : -130.16
     * available : 2323931.5
     */

    private AccountInfoEntity accountInfo;
    private String currencyIsoCode;
    private String currentAccountId;
    private String lightstreamerEndpoint;
    private String clientId;
    /**
     * accountId : A12345
     * accountName : CFD
     * preferred : true
     * accountType : CFD
     */

    private List<AccountsEntity> accounts;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public AccountInfoEntity getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfoEntity accountInfo) {
        this.accountInfo = accountInfo;
    }

    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    public void setCurrencyIsoCode(String currencyIsoCode) {
        this.currencyIsoCode = currencyIsoCode;
    }

    public String getCurrentAccountId() {
        return currentAccountId;
    }

    public void setCurrentAccountId(String currentAccountId) {
        this.currentAccountId = currentAccountId;
    }

    public String getLightstreamerEndpoint() {
        return lightstreamerEndpoint;
    }

    public void setLightstreamerEndpoint(String lightstreamerEndpoint) {
        this.lightstreamerEndpoint = lightstreamerEndpoint;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<AccountsEntity> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountsEntity> accounts) {
        this.accounts = accounts;
    }

    public static class AccountInfoEntity {
        private double balance;
        private double deposit;
        private double profitLoss;
        private double available;

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public double getDeposit() {
            return deposit;
        }

        public void setDeposit(double deposit) {
            this.deposit = deposit;
        }

        public double getProfitLoss() {
            return profitLoss;
        }

        public void setProfitLoss(double profitLoss) {
            this.profitLoss = profitLoss;
        }

        public double getAvailable() {
            return available;
        }

        public void setAvailable(double available) {
            this.available = available;
        }
    }

    public static class AccountsEntity {
        private String accountId;
        private String accountName;
        private boolean preferred;
        private String accountType;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public boolean isPreferred() {
            return preferred;
        }

        public void setPreferred(boolean preferred) {
            this.preferred = preferred;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }
    }
}
