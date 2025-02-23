DROP TABLE IF EXISTS Tasks;
DROP TABLE IF EXISTS Users;

CREATE TABLE Users
(
    userID    INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50),
    lastName  VARCHAR(50),
    password  VARCHAR(50),
    email     VARCHAR(50),
    role      VARCHAR(50) DEFAULT 'user'
);

INSERT INTO Users (firstName, lastName, password, email, role)
VALUES ('Elmer', 'Funez', 'password', 'funeze@carolinau.edu', 'admin'),
       ('Amy', 'Portillo', '12345', 'portilloa@carolinau.edu', 'admin'),
       ('John', 'Doe', 'password123', 'john.doe@example.com', 'user'),
       ('Bob', 'Johnson', 'bobSecure', 'bob.johnson@example.com', 'user'),
       ('Charlie', 'Brown', 'charliePwd', 'charlie.brown@example.com', 'user');

CREATE TABLE Tasks
(
    taskID      INT AUTO_INCREMENT PRIMARY KEY,
    userID      INT,
    title       VARCHAR(255),
    description TEXT,
    status      TINYINT(1)  DEFAULT 0,
    dueDate     DATE,
    priority    VARCHAR(10) DEFAULT 'Low',
    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (userID) REFERENCES Users (userID)
);

INSERT INTO Tasks (title, description, priority, userID)
VALUES ('Complete project proposal', 'Draft and finalize the project proposal for the client.', 'High', 1),
       ('Team meeting', 'Discuss project updates and upcoming deadlines.', 'High', 1),
       ('Code review', 'Review pull requests and provide feedback on recent commits.', 'Medium', 1),
       ('Write documentation', 'Update the API documentation for the new feature.', 'Medium', 1),
       ('Fix production bug', 'Investigate and resolve the reported issue in the live system.', 'High', 1),
       ('Prepare presentation', 'Create slides for the upcoming stakeholder meeting.', 'High', 2),
       ('Schedule one-on-one', 'Set up a meeting with a team member for performance review.', 'Medium', 2),
       ('Optimize database queries', 'Analyze slow queries and improve database performance.', 'Medium', 2),
       ('Test new feature', 'Perform testing on the latest feature before deployment.', 'High', 2),
       ('Update task board', 'Organize and prioritize tasks in the project management tool.', 'Low', 2);
