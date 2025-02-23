DROP TABLE IF EXISTS Tasks CASCADE;
DROP TABLE IF EXISTS Users CASCADE;

create table Users
(
    userID    serial PRIMARY KEY,
    firstName varchar(50),
    lastName  varchar(50),
    password  varchar(50),
    email     varchar(50),
    role      varchar(50) default 'user'
);

insert into Users (firstName, lastName, password, email, role)
values ('Elmer', 'Funez', 'password', 'funeze@carolinau.edu', 'admin'),
       ('Amy', 'Portillo', '12345', 'portilloa@carolinau.edu', 'admin'),
       ('John', 'Doe', 'password123', 'john.doe@example.com', 'user'),
       ('Bob', 'Johnson', 'bobSecure', 'bob.johnson@example.com', 'user'),
       ('Charlie', 'Brown', 'charliePwd', 'charlie.brown@example.com', 'user');

create table Tasks
(
    taskID      serial PRIMARY KEY,
    userID      int references users (userID),
    title       varchar(255),
    description text,
    status      bool        DEFAULT false,
    dueDate     date,
    priority    varchar(10) DEFAULT 'Low',
    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);


-- Make updated_at field on Tasks use current timestamp when table is updated
CREATE OR REPLACE FUNCTION update_timestamp()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_timestamp
    BEFORE UPDATE
    ON Tasks
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

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
