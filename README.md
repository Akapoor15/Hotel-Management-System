# StayEase Hotel Management System

A full-stack hotel management system built with **Spring Boot** (Backend), **React** (Frontend), and **MySQL** (Database). This application provides a comprehensive solution for managing hotel rooms, bookings, and users.

## 🏗️ Architecture

- **Backend**: Spring Boot (REST API)
- **Frontend**: React 18.2.0
- **Database**: MySQL
- **Image Storage**: AWS S3 (for storing room images and hotel assets)
- **Authentication**: JWT (JSON Web Tokens)

## 🚀 Features

### Guest Features
- **User Authentication**: Secure login and registration system
- **Room Browsing**: Browse all available rooms with detailed information and images stored on AWS S3
- **Room Search**: Search for available rooms by check-in/check-out dates and room type
- **Room Booking**: Book rooms with detailed booking information
- **Find Booking**: Search for existing bookings using confirmation code
- **Profile Management**: View and edit user profile information
- **Service Information**: View hotel amenities and services (WiFi, AC, Parking, Mini Bar)

### Admin Features
- **Admin Dashboard**: Comprehensive admin panel for hotel management
- **Room Management**: 
  - Add new rooms with images (images are uploaded and stored on AWS S3)
  - Edit existing room details
  - Delete rooms
  - View all rooms with images served from AWS S3
- **Booking Management**:
  - View all bookings
  - Edit booking details
  - Manage booking status
- **User Management**: View and manage user accounts

## 📋 Prerequisites

Before running this project, make sure you have the following installed:

- **Java JDK** 11 or higher
- **Maven** 3.6 or higher (for Spring Boot backend)
- **Node.js** (v14 or higher)
- **npm** (v6 or higher) or **yarn**
- **MySQL** Server 8.0 or higher
- **AWS Account** with S3 access (Required for storing and serving room images)

## 🗄️ Database Setup

### MySQL Database Schema

Create a MySQL database named `stayease_hotel` (or your preferred name) and the following tables:

#### Table: `users`
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255),
    role VARCHAR(255) NOT NULL
);
#### Table: `rooms`
CREATE TABLE rooms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_description VARCHAR(255),
    room_photo_url VARCHAR(255),
    room_price DECIMAL(38,2),
    room_type VARCHAR(255)
);**Note**: The `room_photo_url` field stores the AWS S3 URL of the room image.

#### Table: `bookings`
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_confirmation_code VARCHAR(255) UNIQUE,
    check_in_date DATE,
    check_out_date DATE,
    num_of_adults INT,
    num_of_children INT,
    total_num_of_guest INT,
    room_id BIGINT,
    user_id BIGINT,
    FOREIGN KEY (room_id) REFERENCES rooms(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);#
# ⚙️ Backend Configuration

### Spring Boot `application.properties`

In your Spring Boot backend project, configure the `application.properties` file (located in `src/main/resources/application.properties`):
ties

here you need to add keys ,username and password . kindly refer my file for better idea

### AWS S3 Setup for Image Storage

1. **Create an S3 Bucket**:
   - Log in to AWS Console
   - Navigate to S3 service
   - Create a new bucket (e.g., `stayease-hotel-images`)
   - Configure bucket permissions (make it private or public based on your security requirements)

2. **Configure CORS** (if accessing images from frontend):
   [
       {
           "AllowedHeaders": ["*"],
           "AllowedMethods": ["GET", "PUT", "POST", "DELETE"],
           "AllowedOrigins": ["http://localhost:3000"],
           "ExposeHeaders": []
       }
   ]
   3. **Create IAM User for S3 Access**:
   - Create an IAM user with programmatic access
   - Attach policy with S3 read/write permissions
   - Save the Access Key ID and Secret Access Key
   - Use these credentials in `application.properties`

4. **Bucket Policy** (example for public read access):n
   {
       "Version": "2012-10-17",
       "Statement": [
           {
               "Sid": "PublicReadGetObject",
               "Effect": "Allow",
               "Principal": "*",
               "Action": "s3:GetObject",
               "Resource": "arn:aws:s3:::your-bucket-name/*"
           }
       ]
   }
   ## 📸 Image Storage Architecture

### How Images are Stored

1. **Upload Process**:
   - Admin uploads room image through the frontend
   - Frontend sends image to Spring Boot backend via `/rooms/add` or `/rooms/update/{roomId}` endpoint
   - Backend receives the image file
   - Backend uploads the image to AWS S3 bucket
   - Backend receives the S3 URL of the uploaded image
   - Backend saves the S3 URL in the `room_photo_url` field of the `rooms` table

2. **Retrieval Process**:
   - When rooms are fetched, the `room_photo_url` contains the AWS S3 URL
   - Frontend directly displays images using the S3 URL
   - Images are served directly from AWS S3 CDN

3. **Benefits of AWS S3 Storage**:
   - Scalable storage solution
   - Fast image delivery via CDN
   - Reduces database size
   - Easy to manage and organize images
   - Cost-effective for large numbers of images

## 🔧 Backend Setup

1. Navigate to the backend directory:
cd backend2. Update `application.properties` with your database and AWS S3 credentials

3. Ensure AWS S3 bucket is created and configured (see AWS S3 Setup section above)

4. Build the project:ash
mvn clean install5. Run the Spring Boot application:ash
mvn spring-boot:run
The backend API will be available at `http://localhost:4040`

## 🔧 Frontend Setup

1. Navigate to the frontend directory:sh
cd "hotel mgt/frontend/stayease-hotel-app"2. Install dependencies:
npm install3. Start the development server:
npm startThe frontend application will open in your browser at `http://localhost:3000`

## Postman API Collection

Setting up API in Postman
Create a new Collection named "StayEase Hotel API"
Set Collection Variables:
base_url: http://localhost:4040
token: (will be set after login)
Add Authentication:
Create a Pre-request Script in the collection:
   pm.request.headers.add({
       key: "Authorization",
       value: "Bearer " + pm.collectionVariables.get("token")
   });

   API Endpoints for Postman
Authentication
POST {{base_url}}/auth/register
body:    {
        "name": "John Doe",
        "email": "john@example.com",
        "password": "password123",
        "phoneNumber": "1234567890",
        "role": "USER"
    }


  POST {{base_url}}/auth/login
  
  User Management
GET {{base_url}}/users/all
GET {{base_url}}/users/get-by-id/{userId}
GET {{base_url}}/users/get-logged-in-profile-info
GET {{base_url}}/users/get-user-bookings/{userId}
DELETE {{base_url}}/users/delete/{userId}

Room Management
POST {{base_url}}/rooms/add
Body (form-data):
roomType: String
roomPrice: Decimal
roomDescription: String
roomPhoto: File (image) - Image will be uploaded to AWS S3
GET {{base_url}}/rooms/all-available-rooms
GET {{base_url}}/rooms/available-rooms-by-date-and-type?checkInDate=2024-01-01&checkOutDate=2024-01-05&roomType=SINGLE
GET {{base_url}}/rooms/types
GET {{base_url}}/rooms/all
GET {{base_url}}/rooms/room-by-id/{roomId}
PUT {{base_url}}/rooms/update/{roomId}
Body (form-data):
roomType: String
roomPrice: Decimal
roomDescription: String
roomPhoto: File (image) - Image will be uploaded to AWS S3
DELETE {{base_url}}/rooms/delete/{roomId}

Booking Management
POST {{base_url}}/bookings/book-room/{roomId}/{userId}
GET {{base_url}}/bookings/all
GET {{base_url}}/bookings/get-by-confirmation-code/{bookingCode}
DELETE {{base_url}}/bookings/cancel/{bookingId}


