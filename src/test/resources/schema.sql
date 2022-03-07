CREATE TABLE IF NOT EXISTS bank_account
(
    account_id  VARCHAR(30) NOT NULL PRIMARY KEY,
    customer_id INT         NOT NULL,
    status      INT
);

CREATE TABLE cash_account
(
    cash_account_id   INT AUTO_INCREMENT PRIMARY KEY,
    account_id        VARCHAR(30) NOT NULL,
    balance           INT         NOT NULL,
    currency_code     VARCHAR(4)  NOT NULL,
    available_balance INT
);

CREATE TABLE transaction_logs
(
    transaction_id   INT AUTO_INCREMENT PRIMARY KEY,
    cash_account_id  INT       NOT NULL,
    direction        INT       NOT NULL,
    amount           INT       NOT NULL,
    status           INT       NOT NULL,
    transaction_time TIMESTAMP NOT NULL,
    description      VARCHAR   NOT NULL
);

CREATE OR REPLACE VIEW all_cash_accounts
AS
SELECT bank_account.account_id,
       bank_account.customer_id,
       cash_account.cash_account_id,
       cash_account.balance,
       cash_account.currency_code,
       cash_account.available_balance
FROM bank_account
         JOIN cash_account ON bank_account.account_id = cash_account.account_id;


CREATE OR REPLACE VIEW vw_transaction_logs
AS
SELECT ca.account_id,
       ca.currency_code,
       tl.transaction_id,
       tl.cash_account_id,
       tl.direction,
       tl.amount,
       tl.status,
       tl.transaction_time,
       tl.description
FROM transaction_logs tl
         JOIN cash_account ca ON tl.cash_account_id = ca.cash_account_id;