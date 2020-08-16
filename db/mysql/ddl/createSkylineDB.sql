CREATE TABLE `skylinedb`.`version_audit` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `version` varchar(255) DEFAULT NULL,
  `init_installation` datetime DEFAULT NULL,
  `finish_installation` datetime DEFAULT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `skylinedb`.`wallet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `brl_balance` decimal(19,2) DEFAULT NULL,
  `btc_balance` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `skylinedb`.`user` (
  `cpf` varchar(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `wallet_id` bigint NOT NULL,
  PRIMARY KEY (`wallet_id`),
  UNIQUE KEY (cpf));

CREATE TABLE `skylinedb`.`transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(19,2) DEFAULT NULL,
  `brl_amount` decimal(19,2) DEFAULT NULL,
  `transaction_date_time` datetime DEFAULT NULL,
  `transaction_type` smallint DEFAULT NULL,
  `user_wallet_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_USER_WALLET_ID` (`user_wallet_id`));
