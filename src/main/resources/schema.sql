DROP DATABASE IF EXISTS todo;

CREATE DATABASE todo;

USE todo;

CREATE TABLE `user`
(
    `id` BIGINT UNSIGNED AUTO_INCREMENT,
    `email` VARCHAR(254) NOT NULL,
    `username` VARCHAR(50) NOT NULL,
    `encrypted_password` VARCHAR(255) NOT NULL,
    `create_date` DATETIME NOT NULL,
    `update_date` DATETIME,
    CONSTRAINT `pk_user` PRIMARY KEY (id)
);

CREATE TABLE `list`
(
    `id` BIGINT UNSIGNED AUTO_INCREMENT,
    `user_id` BIGINT UNSIGNED NOT NULL,
    `title` VARCHAR(30) NOT NULL,
    `create_date` DATETIME NOT NULL,
    `update_date` DATETIME,
    CONSTRAINT `pk_list` PRIMARY KEY (id),
    CONSTRAINT `fk_list_user` FOREIGN KEY (user_id) REFERENCES `user` (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE `task`
(
    `id` BIGINT UNSIGNED AUTO_INCREMENT,
    `list_id` BIGINT UNSIGNED NOT NULL,
    `content` VARCHAR(255) NOT NULL,
    `due_date` DATETIME,
    `create_date` DATETIME NOT NULL,
    `update_date` DATETIME,
    `is_completed` BOOLEAN,
    CONSTRAINT `pk_task` PRIMARY KEY (id),
    CONSTRAINT `fk_task_list` FOREIGN KEY (list_id) REFERENCES `list` (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE `tasklist`
(
  `id` BIGINT UNSIGNED AUTO_INCREMENT,
  `list_id` BIGINT UNSIGNED NOT NULL,
  `task_id` BIGINT UNSIGNED NOT NULL,
  CONSTRAINT `pk_tasklist` PRIMARY KEY (id),
  CONSTRAINT `fk_tasklist_list` FOREIGN KEY (list_id) REFERENCES `list` (id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_tasklist_task` FOREIGN KEY (task_id) REFERENCES `task` (id) ON UPDATE CASCADE ON DELETE CASCADE
);