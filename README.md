CREATE TABLE LocationInfo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    country VARCHAR(50),
    code VARCHAR(10) UNIQUE,
    city VARCHAR(50),
    state VARCHAR(50),
    activate CHAR(1) CHECK (activate IN ('Y', 'N')),
    createdBy VARCHAR(50),
    createdOn DATE
);


INSERT INTO LocationInfo (country, code, city, state, activate, createdBy, createdOn) VALUES
-- India
('India', 'IN001', 'Mumbai', 'Maharashtra', 'Y', 'admin', CURDATE()),
('India', 'IN002', 'Delhi', 'Delhi', 'Y', 'admin', CURDATE()),
('India', 'IN003', 'Bangalore', 'Karnataka', 'Y', 'admin', CURDATE()),
-- (… Add other records …)
-- Australia
('Australia', 'AU009', 'Gold Coast', 'Queensland', 'Y', 'admin', CURDATE()),
('Australia', 'AU010', 'Newcastle', 'New South Wales', 'N', 'admin', CURDATE());
