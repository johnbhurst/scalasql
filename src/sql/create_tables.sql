-- $Id$
-- John Hurst (jbhurst@attglobal.net)
-- 2009-01-06

CREATE TABLE test (
  id INTEGER NOT NULL,
  name VARCHAR2(10) NOT NULL
)
/

INSERT INTO test VALUES (1, 'ONE')
/
INSERT INTO test VALUES (2, 'TWO')
/
INSERT INTO test VALUES (3, 'THREE')
/
COMMIT WORK
/
