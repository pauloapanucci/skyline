CREATE SCHEMA skylinedb;

CREATE TABLE skylinedb.version_audit (
  id SERIAL NOT NULL,
  version varchar(255) DEFAULT NULL,
  init_installation timestamp DEFAULT NULL,
  finish_installation timestamp DEFAULT NULL,
  PRIMARY KEY (id));

CREATE TABLE skylinedb.wallet (
  id SERIAL NOT NULL,
  brl_balance decimal(19,2) DEFAULT NULL,
  btc_balance decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (id));

CREATE TABLE skylinedb.user (
  cpf varchar(11) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  wallet_id bigint NOT NULL,
  UNIQUE (cpf),
  CONSTRAINT FK_WALLET_ID
	FOREIGN KEY(wallet_id)
	  REFERENCES skylinedb.wallet(id));

CREATE TABLE skylinedb.transaction (
  id SERIAL NOT NULL,
  amount decimal(19,2) DEFAULT NULL,
  brl_amount decimal(19,2) DEFAULT NULL,
  transaction_date_time timestamp DEFAULT NULL,
  transaction_type smallint DEFAULT NULL,
  user_wallet_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_USER_WALLET_ID
	FOREIGN KEY(user_wallet_id)
	  REFERENCES skylinedb.wallet(id));
