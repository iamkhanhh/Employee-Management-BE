# Employee Management System - API Documentation

## Base URL
```
http://localhost:8080
```

---

## Authentication APIs

### 1. Login
**Endpoint:** `POST /auth/login`

**Description:** Authenticate user and receive JWT token in cookie

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "status": "success",
  "message": "Login successfilly",
  "data": null
}
```

**Cookie Set:** `access_token` (HttpOnly, 5 days expiry)

---

## Employee Management APIs

### 2. Get All Employees (with Filtering & Pagination)
**Endpoint:** `GET /employees`

**Description:** Retrieve a paginated list of employees with optional filters

**Query Parameters:**
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | Integer | No | 0 | Page number (zero-based) |
| pageSize | Integer | No | 10 | Number of items per page |
| status | EmployeeStatus | No | - | Filter by status (ACTIVE, INACTIVE, ON_LEAVE, TERMINATED) |
| deptId | Integer | No | - | Filter by department ID |
| hireDate | LocalDate | No | - | Filter by hire date (format: dd/MM/yyyy) |
| search | String | No | - | Search in employee full name (case-insensitive) |

**Example Requests:**
```
GET /employees
GET /employees?page=0&pageSize=20
GET /employees?status=ACTIVE
GET /employees?deptId=5
GET /employees?hireDate=01/01/2024
GET /employees?search=john
GET /employees?page=0&pageSize=10&status=ACTIVE&deptId=5&search=john
```

**Response:**
```json
{
  "status": "success",
  "message": "Get employees successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "fullName": "John Doe",
        "gender": "MALE",
        "phoneNumber": "1234567890",
        "department": "IT Department",
        "address": "123 Main St",
        "dob": "01/01/1990",
        "hireDate": "01/01/2024",
        "roleInDept": "STAFF",
        "status": "ACTIVE",
        "username": "johndoe",
        "createdAt": "01/01/2024"
      }
    ],
    "currentPage": 0,
    "pageSize": 10,
    "totalElements": 100,
    "totalPages": 10,
    "hasNext": true,
    "hasPrevious": false
  }
}
```

---

### 3. Get Employee by ID
**Endpoint:** `GET /employees/{id}`

**Description:** Retrieve a single employee by their ID

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Integer | Yes | Employee ID |

**Example Request:**
```
GET /employees/1
```

**Response:**
```json
{
  "status": "success",
  "message": "Get employee successfully",
  "data": {
    "id": 1,
    "fullName": "John Doe",
    "gender": "MALE",
    "phoneNumber": "1234567890",
    "department": "IT Department",
    "address": "123 Main St",
    "dob": "01/01/1990",
    "hireDate": "01/01/2024",
    "roleInDept": "STAFF",
    "status": "ACTIVE",
    "username": "johndoe",
    "createdAt": "01/01/2024"
  }
}
```

**Error Response:**
```json
{
  "status": "error",
  "message": "Employee not found with id: 1"
}
```

---

### 4. Create Employee
**Endpoint:** `POST /employees`

**Description:** Create a new employee

**Request Body:**
```json
{
  "userId": 1,
  "deptId": 5,
  "fullName": "John Doe",
  "gender": "MALE",
  "dob": "01/01/1990",
  "phoneNumber": "1234567890",
  "address": "123 Main St",
  "hireDate": "01/01/2024",
  "status": "ACTIVE",
  "roleInDept": "STAFF"
}
```

**Field Descriptions:**
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| userId | Integer | Yes | Associated user ID |
| deptId | Integer | Yes | Department ID |
| fullName | String | Yes | Employee full name |
| gender | Gender | Yes | MALE, FEMALE, OTHER |
| dob | LocalDate | Yes | Date of birth (dd/MM/yyyy) |
| phoneNumber | String | No | Phone number (10-20 digits) |
| address | String | No | Address |
| hireDate | LocalDate | Yes | Hire date (dd/MM/yyyy) |
| status | EmployeeStatus | No | Default: ACTIVE |
| roleInDept | RoleInDepartment | No | Default: STAFF (HEAD, DEPUTY, STAFF) |

**Response:**
```json
{
  "status": "success",
  "message": "Employee created successfully",
  "data": {
    "id": 1,
    "fullName": "John Doe",
    "gender": "MALE",
    "phoneNumber": "1234567890",
    "department": "IT Department",
    "address": "123 Main St",
    "dob": "01/01/1990",
    "hireDate": "01/01/2024",
    "roleInDept": "STAFF",
    "status": "ACTIVE",
    "username": "johndoe",
    "createdAt": "01/01/2024"
  }
}
```

**Status Code:** `201 Created`

---

### 5. Update Employee
**Endpoint:** `PUT /employees/{id}`

**Description:** Update an existing employee (partial update supported)

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Integer | Yes | Employee ID |

**Request Body:**
```json
{
  "deptId": 6,
  "fullName": "John Updated Doe",
  "gender": "MALE",
  "dob": "01/01/1990",
  "phoneNumber": "0987654321",
  "address": "456 New St",
  "hireDate": "01/01/2024",
  "status": "ACTIVE",
  "roleInDept": "HEAD"
}
```

**Note:** All fields are optional. Only provided fields will be updated.

**Response:**
```json
{
  "status": "success",
  "message": "Employee updated successfully",
  "data": {
    "id": 1,
    "fullName": "John Updated Doe",
    "gender": "MALE",
    "phoneNumber": "0987654321",
    "department": "HR Department",
    "address": "456 New St",
    "dob": "01/01/1990",
    "hireDate": "01/01/2024",
    "roleInDept": "HEAD",
    "status": "ACTIVE",
    "username": "johndoe",
    "createdAt": "01/01/2024"
  }
}
```

---

### 6. Delete Employee
**Endpoint:** `DELETE /employees/{id}`

**Description:** Soft delete an employee (sets isDeleted = true)

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Integer | Yes | Employee ID |

**Example Request:**
```
DELETE /employees/1
```

**Response:**
```json
{
  "status": "success",
  "message": "Employee deleted successfully",
  "data": null
}
```

**Status Code:** `200 OK`

---

## Enums Reference

### Gender
- `MALE`
- `FEMALE`
- `OTHER`

### EmployeeStatus
- `ACTIVE`
- `INACTIVE`
- `ON_LEAVE`
- `TERMINATED`

### RoleInDepartment
- `HEAD`
- `DEPUTY`
- `STAFF`

### UserRole
- `ADMIN`
- `HR`
- `EMPLOYEE`

### UserStatus
- `ACTIVE`
- `DELETED`
- `DISABLED`
- `PENDING`

---

## Common Response Structure

### Success Response
```json
{
  "status": "success",
  "message": "Operation successful message",
  "data": {
    // Response data object
  }
}
```

### Error Response
```json
{
  "status": "error",
  "message": "Error message describing what went wrong",
  "data": null
}
```

---

## Authentication

Most endpoints (except `/auth/login`) require authentication via JWT token stored in an HttpOnly cookie named `access_token`.

The token is automatically sent with each request if you're using the same browser session.

---

## Date Format

All dates in the API use the format: `dd/MM/yyyy`

**Examples:**
- `01/01/2024` - January 1, 2024
- `25/12/2023` - December 25, 2023

---

## Pagination

Pagination follows this structure:
- `page`: Zero-based page number (0 = first page)
- `pageSize`: Number of items per page
- `totalElements`: Total number of items across all pages
- `totalPages`: Total number of pages
- `hasNext`: Boolean indicating if there's a next page
- `hasPrevious`: Boolean indicating if there's a previous page

---

## Error Handling

### Common Error Codes

| Status Code | Description |
|-------------|-------------|
| 200 | Success (GET, PUT, DELETE) |
| 201 | Created (POST) |
| 400 | Bad Request (validation error) |
| 401 | Unauthorized (authentication required) |
| 403 | Forbidden (insufficient permissions) |
| 404 | Not Found |
| 500 | Internal Server Error |

---

## Database Entities

### Employee Entity Fields
- `id` - Integer (Primary Key, Auto-generated)
- `userId` - Integer (Foreign Key to User)
- `deptId` - Integer (Foreign Key to Department)
- `fullName` - String (150 chars)
- `gender` - Enum (Gender)
- `dob` - LocalDate
- `phoneNumber` - String (20 chars)
- `address` - String (255 chars)
- `hireDate` - LocalDate
- `status` - Enum (EmployeeStatus)
- `roleInDept` - Enum (RoleInDepartment)
- `createdAt` - LocalDateTime (Auto-generated)
- `updatedAt` - LocalDateTime (Auto-updated)
- `isDeleted` - Boolean (Soft delete flag)

---

## Additional Entities (Available in Database)

The following entities are available in the system but endpoints are not yet implemented:

- **Departments** - Department management
- **Positions** - Job positions
- **Contracts** - Employment contracts
- **EmployeeDocuments** - Employee documents (BHYT, certificates, etc.)
- **Attendance** - Attendance tracking
- **Tasks** - Task management
- **TaskAssignments** - Task assignments to employees
- **LeaveRequests** - Leave request management
- **Payroll** - Payroll information
- **KpiPeriods** - KPI evaluation periods
- **KpiCriteria** - KPI criteria
- **KpiScores** - KPI scores
- **Notifications** - System notifications
- **EmailLogs** - Email logs

---

## Notes

1. **Soft Delete**: Employees are never physically deleted from the database. The `DELETE` operation sets `isDeleted = true`.

2. **Filtering**: All filters can be combined. The API uses AND logic for multiple filters.

3. **Search**: The search parameter performs case-insensitive partial matching on the employee's full name.

4. **Validation**:
   - Required fields must be provided
   - Phone numbers must be 10-20 digits
   - Email format is validated
   - Dates must follow dd/MM/yyyy format

5. **Related Data**: Employee responses automatically include:
   - Username from the User entity
   - Department name from the Department entity

---

## Future Endpoints (To Be Implemented)

- User management APIs
- Department management APIs
- Contract management APIs
- Attendance tracking APIs
- Leave request APIs
- Payroll management APIs
- KPI management APIs
- Task management APIs
- Notification APIs

---

*Last Updated: November 8, 2024*
