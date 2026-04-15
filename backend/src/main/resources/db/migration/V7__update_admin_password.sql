UPDATE clients 
SET email = 'alvaro@admin.com',
    password_hash = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgCcU7uxpJWPUUnVBeasH5xc4JZ.' 
WHERE role = 'ADMIN';
