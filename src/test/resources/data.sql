INSERT INTO bank_account(account_id, customer_id, status)
VALUES ('TEST_1', 1, 2);

INSERT INTO cash_account(cash_account_id, account_id, balance, currency_code, available_balance)
VALUES (1, 'TEST_1', 1000, 'USD', 1000);

INSERT INTO cash_account(cash_account_id, account_id, balance, currency_code, available_balance)
VALUES (2, 'TEST_1', 1000, 'EUR', 1000);

