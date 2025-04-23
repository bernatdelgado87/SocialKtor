# SocialKtor

![Kotlin](https://img.shields.io/badge/Kotlin-1.8.0-purple.svg)
![Ktor](https://img.shields.io/badge/Ktor-2.2.3-blue.svg)
![Exposed](https://img.shields.io/badge/Exposed-0.41.1-orange.svg)
![AWS SDK](https://img.shields.io/badge/AWS_SDK-2.20.56-yellow.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)

A social network backend made in Kotlin using Ktor and Exposed. All media files are uploaded to Amazon AWS using AWS Java SDK.

## Features

- **User Management**
  - Registration and authentication
  - Profile creation and management
  - Secure password handling

- **Social Network Functionality**
  - Post creation and sharing
  - Commenting on posts
  - Like/Unlike posts
  - Follow/Unfollow users
  - Activity feed

- **Media Management**
  - Image uploading to AWS S3
  - Media storage and retrieval
  - File type validation

- **API Features**
  - RESTful API endpoints
  - JWT authentication
  - Rate limiting
  - Pagination for feed content

## Technology Stack

- **Backend Framework**: Ktor
- **Database**: SQL with Exposed ORM
- **Authentication**: JWT
- **Cloud Storage**: Amazon AWS S3
- **Templating**: Mustache
- **Containerization**: Docker

## Prerequisites

- JDK 11 or newer
- Gradle
- Docker (optional, for containerized deployment)
- AWS Account with S3 bucket (for media storage)

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/bernatdelgado87/SocialKtor.git
cd SocialKtor
```

### Configure AWS Credentials

Create or edit the `src/main/resources/application.conf` file with your AWS credentials:

```hocon
aws {
    accessKey = "your-access-key"
    secretKey = "your-secret-key"
    region = "your-aws-region"
    bucketName = "your-s3-bucket-name"
}
```

### Configure Database

Update the database configuration in `application.conf`:

```hocon
database {
    driverClassName = "org.h2.Driver" # or your preferred database driver
    jdbcURL = "jdbc:h2:file:./build/db" # or your database URL
    username = "username" # if needed
    password = "password" # if needed
}
```

### Running the Application

#### Using Gradle

```bash
./gradlew run
```

The server will start on `http://localhost:8080`

#### Using Docker

Build the Docker image:

```bash
docker build -t socialktor .
```

Run the container:

```bash
docker run -p 8080:8080 socialktor
```

## API Documentation

### Authentication

#### Register a New User

```
POST /api/register
```

Request Body:
```json
{
  "username": "user",
  "email": "user@example.com",
  "password": "password123"
}
```

#### Login

```
POST /api/login
```

Request Body:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

Response:
```json
{
  "token": "jwt-token",
  "userId": "user-id"
}
```

### Posts

#### Create a Post

```
POST /api/posts
```

Headers:
```
Authorization: Bearer jwt-token
```

Request Body (multipart/form-data):
```
text: "Post content"
image: [binary file] (optional)
```

#### Get Posts Feed

```
GET /api/feed?page=1&size=10
```

Headers:
```
Authorization: Bearer jwt-token
```

### User Interactions

#### Follow a User

```
POST /api/follow/{userId}
```

Headers:
```
Authorization: Bearer jwt-token
```

#### Like a Post

```
POST /api
