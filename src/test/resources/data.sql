USE b2_test;

INSERT INTO element
(id, name, created_at, created_by)
VALUES
(1, 'Asset', CURRENT_DATE, 'DBA'),
(2, 'Liability', CURRENT_DATE, 'DBA'),
(3, 'Equity', CURRENT_DATE, 'DBA'),
(4, 'Income', CURRENT_DATE, 'DBA'),
(5, 'Expense', CURRENT_DATE, 'DBA'),
(6, 'Accumulated Other Comprehensive Income', CURRENT_DATE, 'DBA'),
(0, 'Statistic', CURRENT_DATE, 'DBA'),
(99, 'TEST', CURRENT_DATE, 'DBA'),
(999, ' `1234567890-=~!@#$%^&*()_+[]\\{}|;\':",./<>?`', CURRENT_DATE, 'DBA'),
(-1, 'Other', CURRENT_DATE, 'DBA');

INSERT INTO player
(name, is_bank, created_at, created_by)
VALUES
('McDonald\'s', FALSE, CURRENT_DATE, 'DBA'),    -- 1
('Target', FALSE, CURRENT_DATE, 'DBA'),         -- 2
('Costco', FALSE, CURRENT_DATE, 'DBA'),         -- 3
('Chase Bank', TRUE, CURRENT_DATE, 'DBA'),      -- 4
('US Bank', TRUE, CURRENT_DATE, 'DBA'),         -- 5
('Bank of America', TRUE, CURRENT_DATE, 'DBA'), -- 6
('Google', FALSE, CURRENT_DATE, 'DBA'),         -- 7
('King County', FALSE, CURRENT_DATE, 'DBA'),    -- 8
(' `1234567890-=~!@#$%^&*()_+[]\\{}|;\':",./<>?`', TRUE, CURRENT_DATE, 'DBA'); --9

INSERT INTO gl_account
(id, player_id, element_id, name, created_at, created_by)
VALUES
('1000', NULL, 1, 'Cash', CURRENT_DATE, 'DBA'),
('1001', 4, 1, 'Chase Checking', CURRENT_DATE, 'DBA'),
('1002', 4, 1, 'Chase Savings', CURRENT_DATE, 'DBA'),
('1003', 5, 1, 'USB Checking', CURRENT_DATE, 'DBA'),
('1004', 6, 1, 'BOA Checking', CURRENT_DATE, 'DBA'),
('2000', NULL, 2, 'Accounts Payable', CURRENT_DATE, 'DBA'),
('2010', 4, 2, 'USB Mortgage', CURRENT_DATE, 'DBA'),
('4000', NULL, 4, 'Wages & Salaries', CURRENT_DATE, 'DBA'),
('4001', NULL, 4, 'Payroll Tax', CURRENT_DATE, 'DBA'),
('4010', NULL, 4, 'Interest Income', CURRENT_DATE, 'DBA'),
('5000', NULL, 5, 'Property Tax', CURRENT_DATE, 'DBA'),
('5001', NULL, 5, 'Mortgage Principal', CURRENT_DATE, 'DBA'),
('5002', NULL, 5, 'Mortgage Interest', CURRENT_DATE, 'DBA'),
('5010', NULL, 5, 'Groceries', CURRENT_DATE, 'DBA'),
('5011', NULL, 5, 'Dining Out', CURRENT_DATE, 'DBA'),
('0', NULL, 5, 'Statistic', CURRENT_DATE, 'DBA'),
('99', NULL, 5, 'TEST', CURRENT_DATE, 'DBA'),
('999', NULL, 5, 'Other', CURRENT_DATE, 'DBA'),
(' `1234567890-=~!@#$%^&*()_+[]\\{}|;\':",./<>?`', NULL, 5, 'ABC', CURRENT_DATE, 'DBA'),
('ABC', NULL, 5, ' `1234567890-=~!@#$%^&*()_+[]\\{}|;\':",./<>?`', CURRENT_DATE, 'DBA');

INSERT INTO gl_transaction
(date_entered, memo, created_at, created_by)
VALUES
('2022-01-25', 'memo_1', CURRENT_DATE, 'DBA'), -- 1
('2022-02-01', 'memo_2', CURRENT_DATE, 'DBA'), -- 2
('2022-02-01', 'memo-3', CURRENT_DATE, 'DBA'), -- 3
('2022-03-15', 'memo-4', CURRENT_DATE, 'DBA'), -- 4
('2022-03-31', '5.memo', CURRENT_DATE, 'DBA'), -- 5
('2022-04-01', '6_memo', CURRENT_DATE, 'DBA'), -- 6
('2022-10-31', '7-memo', CURRENT_DATE, 'DBA'), -- 7
('2022-12-25', ' `1234567890-=~!@#$%^&*()_+[]\\{}|;\':",./<>?`', CURRENT_DATE, 'DBA'); -- 8

INSERT INTO gl_transaction_line
(transaction_id, line_id, account_id, player_id, amount, memo, date_reconciled, created_at, created_by)
VALUES
(1, 1, '1001', 7, 2500, 'Salary', NULL, CURRENT_DATE, 'DBA'),
(1, 2, '4000', 7, -3000, 'Salary', NULL, CURRENT_DATE, 'DBA'),
(1, 3, '4001', 7, 500, 'Salary', NULL, CURRENT_DATE, 'DBA'),

(2, 1, '5000', 8, 200, 'Property Tax', NULL, CURRENT_DATE, 'DBA'),
(2, 2, '1001', 8, 200, 'Property Tax', NULL, CURRENT_DATE, 'DBA'),

(3, 1, '5001', 5, 500, 'Mortgage Principal', NULL, CURRENT_DATE, 'DBA'),
(3, 2, '5002', 5, 400, 'Mortgage Interest', NULL, CURRENT_DATE, 'DBA'),
(3, 3, '1003', 5, -900, 'Mortgage', NULL, CURRENT_DATE, 'DBA'),

(4, 1, '1000', 5, 10000, 'ABC', NULL, CURRENT_DATE, 'DBA'),
(4, 2, '1000', 5, -10000, ' `1234567890-=~!@#$%^&*()_+[]\\{}|;\':",./<>?`', NULL, CURRENT_DATE, 'DBA'),

(5, 1, '1000', 1, -500, 'L1', NULL, CURRENT_DATE, 'DBA'),
(5, 2, '1001', 1, -500, 'L2', NULL, CURRENT_DATE, 'DBA'),
(5, 3, '2000', 1, 1000, 'L3', NULL, CURRENT_DATE, 'DBA'),

(6, 1, '5000', 5, 100, 'L1', NULL, CURRENT_DATE, 'DBA'),
(6, 2, '5001', 5, 200, 'L2', NULL, CURRENT_DATE, 'DBA'),
(6, 3, '5002', 5, 300, 'L3', NULL, CURRENT_DATE, 'DBA'),
(6, 4, '1001', 5, -600, 'L4', NULL, CURRENT_DATE, 'DBA'),

(7, 1, '1002', 5, 800, 'L1', NULL, CURRENT_DATE, 'DBA'),
(7, 2, '1003', 5, 25.25, 'L2', NULL, CURRENT_DATE, 'DBA'),
(7, 3, '4000', 5, -1000, 'L3', NULL, CURRENT_DATE, 'DBA'),
(7, 4, '4001', 5, 200, 'L4', NULL, CURRENT_DATE, 'DBA'),
(7, 5, '4010', 5, -25.25, 'L5', NULL, CURRENT_DATE, 'DBA'),

(8, 1, '5010', 2, 150.50, 'L1', NULL, CURRENT_DATE, 'DBA'),
(8, 2, '5011', 2, 200, 'L2', NULL, CURRENT_DATE, 'DBA'),
(8, 3, '5011', 3, 5, 'L3', NULL, CURRENT_DATE, 'DBA'),
(8, 4, '5011', 3, 13, 'L4', NULL, CURRENT_DATE, 'DBA'),
(8, 5, '1000', 5, -350.50, 'L5', NULL, CURRENT_DATE, 'DBA'),
(8, 6, '1000', 5, -18, 'L6', NULL, CURRENT_DATE, 'DBA');
