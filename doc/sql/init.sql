-- log 日志
DROP TABLE IF EXISTS log;
CREATE TABLE log (
  id          CHAR (36)  NOT NULL   UNIQUE      PRIMARY KEY,
  ver         INTEGER    NOT NULL DEFAULT 0,
  user_id     VARCHAR(36)  NOT NULL,
  context     VARCHAR(255) NOT NULL,
  create_time TIMESTAMP    NOT NULL           DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT log_users_id_fk FOREIGN KEY (user_id) REFERENCES users(id)
  );

-- users 用户
DROP TABLE IF EXISTS users;
CREATE TABLE users(
  id CHAR (36) NOT NULL UNIQUE PRIMARY KEY ,
  ver         INTEGER    NOT NULL DEFAULT 0,
  account VARCHAR(36) NOT NULL ,
  password VARCHAR (36) NOT NULL ,
  name_ VARCHAR (10) NOT NULL ,
  spell VARCHAR (10) NOT NULL ,
  kind_code INTEGER NOT NULL ,
  tel VARCHAR (15),
  email VARCHAR (30)  ,
  address VARCHAR (50),
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  state INTEGER NOT  NULL  DEFAULT 0,
  remark VARCHAR (255),
  update_time TIMESTAMP NOT NULL,
  attachment_id VARCHAR (36),
  CONSTRAINT users_attachment_id_fk FOREIGN KEY (attachment_id) REFERENCES attachment(id)
)

-- attachment 附件
DROP TABLE IF EXISTS attachment;
CREATE TABLE attachment(
  id CHAR (36) NOT NULL UNIQUE PRIMARY KEY ,
  origina_name VARCHAR (50) NOT NULL ,
  content_type VARCHAR (50) NOT NULL ,
  real_name VARCHAR (36) NOT NULL,
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)

-- category 类别
DROP TABLE IF EXISTS category;
CREATE TABLE category(
 id CHAR (36) NOT NULL UNIQUE PRIMARY KEY ,
 ver INTEGER NOT NULL DEFAULT 0,
 name_ VARCHAR (20) NOT NULL ,
 remark VARCHAR (255),
 state INTEGER NOT NULL DEFAULT 0,
 parent_id VARCHAR (36),
 portrait VARCHAR (36),
 sort INTEGER DEFAULT 0,
 CONSTRAINT category_category_id_fk FOREIGN KEY (parent_id) REFERENCES category(id),
CONSTRAINT category_attachment_id_fk FOREIGN KEY (portrait) REFERENCES attachment(id)
 );

-- post 发表物
DROP TABLE IF EXISTS post;
CREATE TABLE post(
    id              VARCHAR (36)   NOT NULL PRIMARY KEY UNIQUE ,
    ver             INTEGER        NOT NULL  DEFAULT  0,
    post_type       VARCHAR(2)       NOT NULL           ,
    category_id     VARCHAR (36)    NOT NULL ,
    parent_id       VARCHAR (36),
    author          VARCHAR(30),
    title           VARCHAR(100),
    sub_title        VARCHAR(100),
    terms           VARCHAR(100),
    brief           VARCHAR (50),
    content         TEXT,
    creator_id      VARCHAR(36)    NOT NULL ,
    create_time     TIMESTAMP      NOT NULL            DEFAULT CURRENT_TIMESTAMP ,
    update_time     TIMESTAMP     NOT NULL  ,
    state           INTEGER        NOT NULL           DEFAULT 0,
    attachment_id  VARCHAR(36),
    CONSTRAINT post_post_id_fk FOREIGN KEY (parent_id) REFERENCES post(id),
    CONSTRAINT post_category_id_fk FOREIGN KEY (category_id) REFERENCES category(id),
    CONSTRAINT post_user_id_fk FOREIGN KEY (creator_id) REFERENCES users(id),
    CONSTRAINT post_attachment_id_fk FOREIGN KEY (attachment_id) REFERENCES attachment(id)

)

-- post_attachment 发表物附件
DROP TABLE IF EXISTS post_attachment;
CREATE TABLE post_attachment(
  id VARCHAR (36)  NOT NULL PRIMARY KEY UNIQUE ,
  owner VARCHAR (36),
  attachment_id VARCHAR (36),
  CONSTRAINT post_attachment_post_id_fk FOREIGN KEY (owner) REFERENCES post(id),
  CONSTRAINT post_attachment_attachment_id_fk FOREIGN KEY (attachment_id) REFERENCES attachment(id)
)