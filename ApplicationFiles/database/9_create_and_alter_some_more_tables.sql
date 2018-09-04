USE toeiconline;

CREATE TABLE exercise (
  exerciseid BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  createddate TIMESTAMP NULL,
  modifieddate TIMESTAMP NULL,
  type VARCHAR(100) NOT NULL,
  UNIQUE (name)
);

CREATE TABLE exercisequestion (
  exercisequestionid BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  image VARCHAR(255),
  audio VARCHAR(255),
  question TEXT NOT NULL,
  option1 VARCHAR(300) NOT NULL,
  option2 VARCHAR(300) NOT NULL,
  option3 VARCHAR(300) NOT NULL,
  option4 VARCHAR(300) NOT NULL,
  correctanswer VARCHAR(10) NOT NULL,
  exerciseid BIGINT NOT NULL,
  createddate TIMESTAMP NULL,
  modifieddate TIMESTAMP NULL
);

ALTER TABLE exercisequestion ADD CONSTRAINT fk_exercisequestion_exercise FOREIGN KEY (exerciseid) REFERENCES exercise(exerciseid);

CREATE TABLE examination (
  examinationid BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  createddate TIMESTAMP NULL,
  modifieddate TIMESTAMP NULL,
  UNIQUE(name)
);

CREATE TABLE examinationquestion (
  examinationquestionid BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  image VARCHAR(255),
  audio VARCHAR(255),
  question TEXT NOT NULL,
  paragraph TEXT,
  option1 VARCHAR(300),
  option2 VARCHAR(300),
  option3 VARCHAR(300),
  option4 VARCHAR(300),
  correctanswer VARCHAR(10),
  examinationid BIGINT NOT NULL,
  createddate TIMESTAMP NULL,
  modifieddate TIMESTAMP NULL
);

ALTER TABLE examinationquestion ADD CONSTRAINT fk_examinationquestion_examination FOREIGN KEY (examinationid) REFERENCES examination(examinationid);


CREATE TABLE result (
  resultid BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  listenscore INT NOT NULL,
  readingscore INT NOT NULL,
  examinationid BIGINT NOT NULL,
  userid BIGINT NOT NULL,
  createddate TIMESTAMP NULL,
);

ALTER TABLE result ADD CONSTRAINT fk_result_examination FOREIGN KEY (examinationid) REFERENCES examination(examinationid);
ALTER TABLE result ADD CONSTRAINT fk_result_user FOREIGN KEY (userid) REFERENCES user(userid);



