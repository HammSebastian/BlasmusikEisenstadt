-- Create enum type for user roles
CREATE TYPE role_enum AS ENUM ('USER', 'ADMIN');

-- Create users table
CREATE TABLE _user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role role_enum NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create gigs table
CREATE TABLE gig (
    id BIGSERIAL PRIMARY KEY,
    gig_titel VARCHAR(255) NOT NULL,
    gig_description TEXT,
    gig_location VARCHAR(255) NOT NULL,
    gig_date DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create images table
CREATE TABLE image (
    id BIGSERIAL PRIMARY KEY,
    image_path VARCHAR(255) NOT NULL UNIQUE,
    image_category VARCHAR(100) NOT NULL,
    image_author VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better query performance
CREATE INDEX idx_gig_date ON gig(gig_date);
CREATE INDEX idx_image_category ON image(image_category);

-- Create a function to update the updated_at column
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create triggers to automatically update updated_at
CREATE TRIGGER update_user_updated_at
BEFORE UPDATE ON _user
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_gig_updated_at
BEFORE UPDATE ON gig
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_image_updated_at
BEFORE UPDATE ON image
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Insert initial admin user (password: admin123)
INSERT INTO _user (username, email, password, role) 
VALUES ('admin', 'admin@example.com', '$2a$10$XURPShQNCsLjp1ESc2laoObo9QZDhxz73hJPaEv7/cBha4pk0AgP.', 'ADMIN');
