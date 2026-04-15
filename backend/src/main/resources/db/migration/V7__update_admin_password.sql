UPDATE clients 
SET email = 'alvaro@admin.com',
    password_hash = '$2a$10$48yJIxScSScEXagEc5.hoOXJV8vPyoIqeEnlesgrE05K/LnY5EHWW' 
WHERE role = 'ADMIN';
