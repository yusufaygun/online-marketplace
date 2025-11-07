# Online Marketplace

A full-stack online marketplace application built with Spring Boot (backend) and Next.js (frontend). This application provides a complete e-commerce platform with user authentication, product management, seller management, shopping cart functionality, and admin dashboard.

## Features

### User Features
- **User Registration & Authentication**: Secure JWT-based authentication system
- **Product Browsing**: Browse and search through available products
- **Shopping Cart**: Add products to cart and manage quantities
- **Favorites List**: Save favorite products for quick access
- **Seller Blacklist**: Block specific sellers to filter their products
- **User Profile**: View and manage personal information, favorites, and blacklist

### Admin Features
- **User Management**: Create, read, update, and delete users
- **Admin Management**: Create new admin accounts
- **Seller Management**: Add, update, and delete sellers
- **Product Management**: Manage products for each seller (CRUD operations)
- **Role Management**: Assign roles to users

### Seller Features
- **Seller Pages**: View seller information and their products
- **Product Display**: Browse products by seller

## Tech Stack

### Backend
- **Java 21**
- **Spring Boot 3.3.2**
- **Spring Security**: JWT-based authentication and authorization
- **Spring Data JPA**: Database operations
- **MySQL**: Database
- **Lombok**: Code generation
- **Maven**: Dependency management

### Frontend
- **Next.js 14.2.5**: React framework with App Router
- **React 18**: UI library
- **Tailwind CSS**: Styling
- **Axios**: HTTP client
- **React Context API**: State management (Cart)

## Project Structure

```
online-marketplace/
├── backend/                    # Spring Boot backend
│   ├── src/main/java/com/example/online_marketplace/
│   │   ├── controller/         # REST controllers
│   │   ├── service/            # Business logic
│   │   ├── repository/         # Data access layer
│   │   ├── model/              # Entity models
│   │   ├── dto/                # Data Transfer Objects
│   │   ├── mapper/             # Entity-DTO mappers
│   │   ├── security/           # Security configuration
│   │   ├── exception/          # Exception handling
│   │   └── response/           # API response wrapper
│   └── src/main/resources/
│       └── application.properties
│
└── frontend/                   # Next.js frontend
    ├── app/
    │   ├── admin/              # Admin pages
    │   ├── auth/               # Authentication pages
    │   ├── products/           # Product pages
    │   ├── sellers/            # Seller pages
    │   ├── profile/            # User profile
    │   ├── cart/               # Shopping cart
    │   ├── components/         # React components
    │   ├── context/            # React contexts
    │   └── hooks/              # Custom hooks
    └── public/                 # Static assets
```

## Prerequisites

- **Java 21** or higher
- **Node.js 18** or higher
- **MySQL 8.0** or higher
- **Maven 3.6+**

## Setup Instructions

### Backend Setup

1. **Navigate to the backend directory:**
   ```bash
   cd backend
   ```

2. **Configure Database:**
   - Create a MySQL database named `online_marketplace_db`
   - Update `src/main/resources/application.properties` with your database credentials:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3307/online_marketplace_db
     spring.datasource.username=root
     spring.datasource.password=root
     ```

3. **Build and run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```
   Or on Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to the frontend directory:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Run the development server:**
   ```bash
   npm run dev
   ```

   The frontend will start on `http://localhost:3000`

## Default Credentials

The application automatically creates default users on startup:

- **Admin User:**
  - Username: `admin`
  - Password: `password123`

- **Regular User:**
  - Username: `user`
  - Password: `password123`

**⚠️ Important:** Change these default passwords in production!

## API Endpoints

### Authentication
- `POST /auth/login` - User login
- `POST /auth/register` - User registration

### Products
- `GET /products` - Get all products (paginated)
- `GET /products/{id}` - Get product by ID

### Sellers
- `GET /sellers` - Get all sellers (paginated)
- `GET /sellers/{id}` - Get seller by ID with products

### Profile (Authenticated)
- `GET /profile` - Get current user details
- `GET /profile/favorites` - Get favorite products
- `POST /profile/favorites` - Add product to favorites
- `DELETE /profile/favorites` - Remove product from favorites
- `GET /profile/blacklist` - Get blacklisted sellers
- `GET /profile/blacklistids` - Get blacklisted seller IDs
- `POST /profile/blacklist` - Add seller to blacklist
- `DELETE /profile/blacklist` - Remove seller from blacklist

### Admin (Admin Only)
- `GET /admin/users` - Get all users (paginated)
- `GET /admin/users/{id}` - Get user by ID
- `POST /admin/users` - Create new user
- `POST /admin/admins` - Create new admin
- `PUT /admin/users/{id}` - Update user
- `DELETE /admin/users/{id}` - Delete user
- `POST /admin/{username}/roles/{roleName}` - Assign role to user
- `GET /admin/sellers` - Get all sellers
- `POST /admin/sellers` - Create new seller
- `PUT /admin/sellers/{id}` - Update seller
- `DELETE /admin/sellers/{id}` - Delete seller
- `POST /admin/sellers/{sellerId}/products` - Add product to seller
- `PUT /admin/sellers/{sellerId}/products/{productId}` - Update product
- `DELETE /admin/sellers/{sellerId}/products/{productId}` - Remove product from seller

## Security

- JWT-based authentication
- Role-based access control (RBAC)
- Password encryption using BCrypt
- CSRF protection disabled (for API usage)
- Protected endpoints require authentication
- Admin endpoints require admin role

## Database Schema

### Entities
- **User**: Stores user information, roles, favorites, and blacklist
- **Product**: Stores product information and seller relationship
- **Seller**: Stores seller information and products
- **Role**: Stores user roles (ADMIN, USER)

### Relationships
- User ↔ Role: Many-to-Many
- User ↔ Product: Many-to-Many (favorites)
- User ↔ Seller: Many-to-Many (blacklist)
- Seller ↔ Product: One-to-Many
- Product ↔ Seller: Many-to-One

## Development

### Backend Development
- The backend uses Spring Boot's auto-configuration
- Database schema is managed by Hibernate (`spring.jpa.hibernate.ddl-auto=create-drop`)
- JWT tokens are generated on login and validated on each request

### Frontend Development
- Uses Next.js App Router
- Client-side routing with Next.js navigation
- Cart state managed via React Context
- Protected routes using custom hooks (`useAdminRoute`, `useUserRoute`)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is open source and available under the MIT License.

## Notes

- The forgot password functionality is currently not implemented
- Default database port is set to 3307 (change in `application.properties` if needed)
- The application uses `create-drop` for database schema, which will drop tables on restart (change to `update` for production)

