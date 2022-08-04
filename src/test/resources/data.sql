USE b2_test;

INSERT INTO element
(number, name)
VALUES
(1, 'Asset'),
(2, 'Liability'),
(3, 'Equity'),
(4, 'Income'),
(5, 'Expense'),
(6, 'Accumulated Other Comprehensive Income'),
(0, 'ZERO'),
(99, 'NINETY-NINE'),
(999, 'NINE-NINETY-NINE'),
(-1, 'NEGATIVE-ONE');

INSERT INTO player
(name, is_bank)
VALUES
('Chase Bank', true),      -- 1
('Bank of America', true), -- 2
('US Bank', true),         -- 3
('Vanguard', true),        -- 4
('McDonald\'s', false),    -- 5
('Walmart', false),        -- 6
('Target', false),         -- 7
('Costco', false),         -- 8
('Amazon', false),         -- 9
('99', false);             -- 10

INSERT INTO gl_account
(number, name, element_id, player_id)
VALUES
('1000', 'Checking', 1, 1),                -- 1
('1001', 'Savings', 1, 2),                 -- 2
('2000', 'Accounts Payable', 2, NULL),     -- 3
('3000', 'Retained Earnings', 3, NULL),    -- 4
('4000', 'Salary', 4, NULL),               -- 5
('4001', 'Interest Income', 4, NULL),      -- 6
('5000', 'Food', 5, NULL),                 -- 7
('5001', 'Entertainment', 5, NULL),        -- 8
('6000', 'Unrealized Gain/Loss', 6, NULL), -- 9
('99', 'NINETY-NINE', 6, NULL);            -- 10

INSERT INTO gl_transaction
(date_entered, memo)
VALUES
('2022-01-31', 'memo-1'), -- 1
('2022-02-28', 'memo_2'), -- 2
('2022-03-31', 'memo.3'), -- 3
('2022-04-30', 'memo:4'), -- 4
('2022-05-31', '5-memo'), -- 5
('2022-06-30', '6_memo'), -- 6
('2022-08-31', '8.memo'), -- 7
('2022-08-31', '8:memo'), -- 8
('2022-09-30', '9memo9'), -- 9
('2022-10-31', '0memo0'), -- 10
('2022-11-30', 'me-mo'),  -- 11
('2022-12-31', '99');     -- 12

INSERT INTO gl_transaction_line
(gl_transaction_id, line_id, gl_account_id, player_id, amount, memo, date_reconciled)
VALUES
(1, 1, 7, 6, 100, '1-memo', NULL),
(1, 2, 7, 6, 50, '2-memo', NULL),
(1, 3, 7, 6, 25, '3-memo', NULL),
(1, 4, 1, 6, -175, '4-memo', NULL),

(2, 1, 8, 9, 100, '1.memo', NULL),
(2, 2, 8, 9, 200, '2.memo', NULL),
(2, 3, 8, 9, 300, '3.memo', NULL),
(2, 4, 2, 9, -600, '4.memo', NULL),

(3, 1, 1, 1, 300, 'memo-1', NULL),
(3, 2, 5, 1, -300, 'memo-2', NULL),

(4, 1, 2, 2, 400, 'memo.1', NULL),
(4, 2, 6, 2, -400, 'memo.2', NULL),

(5, 1, 7, 6, 500, 'me1mo', NULL),
(5, 2, 1, 6, -500, 'me2mo', NULL),

(6, 1, 7, 6, 600, 'me-1-mo', NULL),
(6, 2, 1, 6, -600, 'me-2-mo', NULL),

(7, 1, 7, 6, 700, '1memo1', NULL),
(7, 2, 1, 6, -700, '2memo2', NULL),

(8, 1, 7, 6, 800, 'memo', NULL),
(8, 2, 1, 6, -800, 'memo', NULL),

(9, 1, 7, 6, 900, 'memo', NULL),
(9, 2, 1, 6, -900, 'memo', NULL),

(10, 1, 7, 6, 1000, 'memo', NULL),
(10, 2, 1, 6, -1000, 'memo', NULL),

(11, 1, 7, 6, 1100, 'memo', NULL),
(11, 2, 1, 6, -1100, 'memo', NULL);
