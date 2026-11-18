-- ===================================================================
-- Vstay Hotel Management System - Database Schema for MS SQL Server
-- ===================================================================

CREATE DATABASE VstayDB;
GO

USE VstayDB;
GO

-- 1. Table: users
CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    fullName NVARCHAR(255) NOT NULL,
    phone NVARCHAR(50),
    email NVARCHAR(255) NOT NULL UNIQUE,
    passwordHash NVARCHAR(255) NOT NULL,
    salt NVARCHAR(255) NOT NULL,
    vipTier NVARCHAR(50) DEFAULT 'Silver',
    vPoints INT DEFAULT 0,
    role NVARCHAR(50) DEFAULT 'USER',
    createdAt DATETIME2 DEFAULT GETDATE(),
    lastLoginAt DATETIME2
);
GO

-- 2. Table: hotels
CREATE TABLE hotels (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    category NVARCHAR(100),
    city NVARCHAR(100) NOT NULL,
    address NVARCHAR(500),
    price BIGINT NOT NULL,
    oldPrice BIGINT,
    discountPercent INT,
    stars INT DEFAULT 5,
    rating FLOAT DEFAULT 9.0,
    ratingText NVARCHAR(100),
    reviewsCount INT DEFAULT 0,
    image NVARCHAR(500),
    badge NVARCHAR(100),
    badgeSub NVARCHAR(100),
    roomType NVARCHAR(200),
    urgency NVARCHAR(200),
    distance NVARCHAR(200),
    taxNote NVARCHAR(200),
    galleryCount INT DEFAULT 0,
    description NVARCHAR(2000),
    featured BIT DEFAULT 0,
    inclusions NVARCHAR(1000),
    amenities NVARCHAR(1000),
    tags NVARCHAR(500),
    gallery NVARCHAR(2000),
    createdAt DATETIME2 DEFAULT GETDATE()
);
GO

-- 3. Table: rooms
CREATE TABLE rooms (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    hotel_id BIGINT NOT NULL FOREIGN KEY REFERENCES hotels(id) ON DELETE CASCADE,
    name NVARCHAR(255) NOT NULL,
    type NVARCHAR(100),
    price BIGINT NOT NULL,
    oldPrice BIGINT,
    capacity INT DEFAULT 2,
    bedType NVARCHAR(100),
    area FLOAT,
    image NVARCHAR(500),
    amenities NVARCHAR(1000),
    available BIT DEFAULT 1
);
GO

-- 4. Table: bookings
CREATE TABLE bookings (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    bookingCode NVARCHAR(100) NOT NULL UNIQUE,
    user_id BIGINT FOREIGN KEY REFERENCES users(id) ON DELETE SET NULL,
    hotel_id BIGINT FOREIGN KEY REFERENCES hotels(id) ON DELETE SET NULL,
    hotelName NVARCHAR(255),
    roomType NVARCHAR(255),
    guestName NVARCHAR(255) NOT NULL,
    guestEmail NVARCHAR(255) NOT NULL,
    guestPhone NVARCHAR(50),
    checkIn DATE NOT NULL,
    checkOut DATE NOT NULL,
    nights INT DEFAULT 1,
    totalPrice BIGINT NOT NULL,
    vPointsEarned INT DEFAULT 0,
    paymentMethod NVARCHAR(100),
    specialRequests NVARCHAR(500),
    specialNotes NVARCHAR(500),
    paymentStatus NVARCHAR(50) DEFAULT 'PAID',
    createdAt DATETIME2 DEFAULT GETDATE()
);
GO

-- 5. Table: reviews
CREATE TABLE reviews (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    hotel_id BIGINT NOT NULL FOREIGN KEY REFERENCES hotels(id) ON DELETE CASCADE,
    user_id BIGINT FOREIGN KEY REFERENCES users(id) ON DELETE SET NULL,
    authorName NVARCHAR(255),
    authorAvatar NVARCHAR(500),
    rating FLOAT NOT NULL,
    comment NVARCHAR(1000),
    travelType NVARCHAR(100),
    createdAt DATETIME2 DEFAULT GETDATE()
);
GO

-- 6. Table: favorites
CREATE TABLE favorites (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL FOREIGN KEY REFERENCES users(id) ON DELETE CASCADE,
    hotel_id BIGINT NOT NULL FOREIGN KEY REFERENCES hotels(id) ON DELETE CASCADE,
    createdAt DATETIME2 DEFAULT GETDATE()
);
GO

-- 7. Table: contacts
CREATE TABLE contacts (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    phone NVARCHAR(50),
    email NVARCHAR(255) NOT NULL,
    topic NVARCHAR(200),
    message NVARCHAR(2000),
    createdAt DATETIME2 DEFAULT GETDATE()
);
GO

-- ===================================================================
-- Default Admin & Demo Account
-- Password for both is: admin123 and demo123 (generated via PasswordUtil)
-- Note: You can also use the `/seed` URL in the web browser to auto-seed!
-- ===================================================================
