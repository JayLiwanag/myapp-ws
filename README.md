Mini Sprngboot CRUD project

```
mvn test
mvn clean package
mvn springboot:run
```
JSON sample data

Signup/Profile model
{
    "username": "JNSmit",
    "firstName": "Jane",
    "lastName": "Smith",
    "email": "JNSmith@myapp.com",
    "password" : "!58603V97d624a1",
    "passwordConfirmation" : "!58603V97d624a1"
}

Login Model
{
    "username": "JNSmit",
    "password" : "!58603V97d624a1"
}

## Header
Authorization : Bearer + JWT token
UserId : public userId


## Paths
- Signup endpoint: `http://localhost:8080/api/users`
- Login endpoint : `http://localhost:8080/api/users/login`
- UserProfile endpoint: `http://localhost:8080/api/profile/{UserId}`
- Swagger UI endpoint: `http://localhost:8080/api/swagger-ui/index.html`


