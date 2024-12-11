
CREATE TABLE author (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(50) NOT NULL,
                        email VARCHAR(100) NOT NULL UNIQUE CHECK (email LIKE '%@%'),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE schedule (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          task VARCHAR(200) NOT NULL,
                          name VARCHAR(50) NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          author_id BIGINT NOT NULL,
                          FOREIGN KEY (author_id) REFERENCES author(id) ON DELETE CASCADE
);
