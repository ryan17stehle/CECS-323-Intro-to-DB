CREATE TABLE Customers (
  cust_id    VARCHAR(10) NOT NULL,
  cust_fname VARCHAR(15) NOT NULL,
  cust_lname VARCHAR(15) NOT NULL,
  cust_mid_ini VARCHAR (1)       ,
  cust_ssn    VARCHAR(12)        ,
  cust_address  VARCHAR(30)      ,
  zip_code      INTEGER(5)       ,
  cust_email VARCHAR(30)         ,
  CONSTRAINT pk_customers PRIMARY KEY (cust_id)
  );

CREATE TABLE Inventory (
  VIN    VARCHAR(30)     NOT NULL   ,
  invoice_price VARCHAR(15) NOT NULL,
  retail_price VARCHAR(15) NOT NULL ,
  engine_size VARCHAR (5)           ,
  driving_range    INTEGER(12)      ,
  odometer  INTEGER(20)             ,
  license_plate      VARCHAR(7)     ,
  warranty VARCHAR(30)              ,
  color VARCHAR(15)                 , 
  body_style VARCHAR(15)            ,
  sunroof VARCHAR(10)               ,
  leather_interior VARCHAR(10)      ,
  new_used CHAR(1)                  ,
  fuel_type VARCHAR(20)             ,
  make VARCHAR(20)                  ,
  model VARCHAR(20)                 ,
  year INTEGER (4)                  , 
  CONSTRAINT pk_inventory PRIMARY KEY (VIN)
  );

CREATE TABLE PhoneNumbers (
  phone_type VARCHAR (10),
  phone_number VARCHAR(10),
  CONSTRAINT pk_PhoneNumbers PRIMARY KEY()
);

CREATE TABLE ZipLocations (
  zip_code INTEGER(5) NOT NULL,
  city VARCHAR(20)            ,
  state VARCHAR(20)           ,
  CONSTRAINT pk_zipLocations PRIMARY KEY (zip_code)
);

CREATE TABLE Employees (
  employee_id VARCHAR(20)    NOT NULL,
  employee_fname VARCHAR(15) NOT NULL,
  employee_lname VARCHAR(15) NOT NULL,
  employee_mid_ini VARCHAR (1)       ,
  employee_ssn    VARCHAR(12)        ,
  employee_address  VARCHAR(30)      ,
  zip_code      INTEGER(5)           ,
  employee_email VARCHAR(30)         ,
  department VARCHAR (15)            ,
  current_position VARCHAR(15)       ,
  hire_date DATE(dd.mm.yyyy)         ,
  unused_vacation_days INTEGER(1)    ,
  benefits_premium VARCHAR (10)      ,
  CONSTRAINT pk_Employees PRIMARY KEY (employee_id)
);

CREATE TABLE Dependents (
  dependent_fname VARCHAR(20) NOT NULL,
  dependent_lname VARCHAR(20) NOT NULL,
  relation VARCHAR(10)                ,
  employee_id VARCHAR(20) NOT NULL    ,
  CONSTRAINT pk_Dependents PRIMARY KEY (employee_id)
);

CREATE TABLE EmergencyContacts (
  emer_cont_fname VARCHAR(20) NOT NULL,
  emer_cont_lname VARCHAR(20) NOT NULL,
  emer_cont_address VARCHAR(20)       ,
  zip_code INTEGER(5)                 ,
);

ALTER TABLE Dependents
          ADD CONSTRAINT Dependents_Employees_fk
          FOREIGN KEY (employee_id)
          REFERENCES Employees (employee_id);

ALTER TABLE CUSTOMERS
          ADD CONSTRAINT Customers_ZipLocations_fk
          FOREIGN KEY (zip_code)
          REFERENCES ZipLocations (zip_code);

ALTER TABLE Employees
          ADD CONSTRAINT Employees_ZipLocations_fk
          FOREIGN KEY (zip_code)
          REFERENCES ZipLocations (zip_code);

ALTER TABLE EmergencyContacts
          ADD CONSTRAINT EmergencyContacts_ZipLocations_fk
          FOREIGN KEY (zip_code)
          REFERENCES ZipLocations (zip_code);

ALTER TABLE PhoneNumbers
          ADD CONSTRAINT PhoneNumbers_Customers_fk
          FOREIGN KEY (cust_id)
          REFERENCES Customers (cust_id);

ALTER TABLE PhoneNumbers
          ADD CONSTRAINT PhoneNumbers_Employees_fk
          FOREIGN KEY (employee_id)
          REFERENCES Employees (employee_id);

ALTER TABLE PhoneNumbers
          ADD CONSTRAINT PhoneNumbers_EmergencyContacts_fk
          FOREIGN KEY (emer_cont_id)
          REFERENCES EmergencyContacts (emer_cont_id);