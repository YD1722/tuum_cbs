INSERT INTO bank_account(account_id, customer_id, status)
VALUES ('TEST_1',100, 2);

INSERT INTO bank_account(account_id, customer_id, status)
VALUES ('TEST_2', 101, 2);

INSERT INTO cash_account(cash_account_id, account_id, balance, currency_code, available_balance)
VALUES (100, 'TEST_1', 1000, 'USD', 1000);

INSERT INTO cash_account(cash_account_id, account_id, balance, currency_code, available_balance)
VALUES (101, 'TEST_1', 1000, 'EUR', 1000);

INSERT INTO cash_account(cash_account_id, account_id, balance, currency_code, available_balance)
VALUES (102, 'TEST_2', 1000, 'USD', 1000);

INSERT INTO cash_account(cash_account_id, account_id, balance, currency_code, available_balance)
VALUES (103, 'TEST_2', 1000, 'EUR', 1000);

