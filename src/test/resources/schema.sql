CREATE TABLE IF NOT EXISTS bank_account
(
    account_id  VARCHAR(30) NOT NULL,
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

CREATE OR REPLACE VIEW public.all_cash_accounts
AS
SELECT bank_account.account_id,
       bank_account.customer_id,
       cash_account.cash_account_id,
       cash_account.balance,
       cash_account.currency_code,
       cash_account.available_balance
FROM bank_account
         JOIN cash_account ON bank_account.account_id = cash_account.account_id;