package com.allen.lightstreamdemo.bean;

import java.util.List;

/**
 * Created by: allen on 16/9/2.
 */

public class Account {

    /**
     * accountId : QYYVO
     * accountName : CFD
     * accountAlias : null
     * status : ENABLED
     * accountType : CFD
     * preferred : true
     * balance : {"balance":0,"deposit":0,"profitLoss":0,"available":0}
     * currency : USD
     * canTransferFrom : true
     * canTransferTo : true
     */

    private List<AccountsEntity> accounts;

    public List<AccountsEntity> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountsEntity> accounts) {
        this.accounts = accounts;
    }

    public static class AccountsEntity {
        private String accountId;
        private String accountName;
        private Object accountAlias;
        private String status;
        private String accountType;
        private boolean preferred;
        /**
         * balance : 0.0
         * deposit : 0.0
         * profitLoss : 0.0
         * available : 0.0
         */

        private BalanceEntity balance;
        private String currency;
        private boolean canTransferFrom;
        private boolean canTransferTo;

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

        public Object getAccountAlias() {
            return accountAlias;
        }

        public void setAccountAlias(Object accountAlias) {
            this.accountAlias = accountAlias;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public boolean isPreferred() {
            return preferred;
        }

        public void setPreferred(boolean preferred) {
            this.preferred = preferred;
        }

        public BalanceEntity getBalance() {
            return balance;
        }

        public void setBalance(BalanceEntity balance) {
            this.balance = balance;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public boolean isCanTransferFrom() {
            return canTransferFrom;
        }

        public void setCanTransferFrom(boolean canTransferFrom) {
            this.canTransferFrom = canTransferFrom;
        }

        public boolean isCanTransferTo() {
            return canTransferTo;
        }

        public void setCanTransferTo(boolean canTransferTo) {
            this.canTransferTo = canTransferTo;
        }

        public static class BalanceEntity {
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
    }
}
