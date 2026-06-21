CREATE TABLE expense_recon
(
    recon_id     VARCHAR(255) NOT NULL,
    expense_id   VARCHAR(255) NOT NULL,
    recon_amount DECIMAL      NOT NULL,
    recon_desc   VARCHAR(255),
    recon_date   date         NOT NULL,
    user_id      UUID         NOT NULL,
    CONSTRAINT pk_expense_recon PRIMARY KEY (recon_id)
);

ALTER TABLE expense_recon
    ADD CONSTRAINT FK_EXPENSE_RECON_ON_EXPENSE FOREIGN KEY (expense_id) REFERENCES expense (expense_id);

ALTER TABLE expense_recon
    ADD CONSTRAINT FK_EXPENSE_RECON_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);