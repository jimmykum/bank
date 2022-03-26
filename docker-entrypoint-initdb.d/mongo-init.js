db = db.getSiblingDB('bank_dev');
db.createUser(
  {
    user: 'dev_user',
    pwd: 'dev1234',
    roles: [{ role: 'readWrite', db: 'bank_dev' }],
  },
);


db = db.getSiblingDB('bank_test');
db.createUser(
  {
    user: 'test_user',
    pwd: 'test1234',
    roles: [{ role: 'readWrite', db: 'bank_test' }],
  },
);
