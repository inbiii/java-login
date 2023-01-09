# java-login

Java login is a project that was created to function as a login for multiple personal projects

What it consists of is multiple end points listed below:

- REGISTRATION CONTROLLER
  - api/v1
    - POST: "/register"
    - GET: "/register/confirm"
    - POST: "/register/recover"
    - POST: "/register/confirm"


- USER CONTROLLER
  - api/v1
    - GET: "/userList"
    - POST: "/login"
    - POST: "/role/save"
    - POST: "/role/addtouser"
    - GET: "/token/refresh"