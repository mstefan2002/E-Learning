# Project Description
The E-Learning application is a computer program designed for learning purposes. It allows administrators to create and manage chapters, lessons, and tests. Users can access the application to study the created content and take tests. The application runs on a single computer, storing all user data locally. Additionally, there are plans to potentially incorporate server-side functionality in the future.

# Features
### Authentication
The authentication feature allows users to log in with a unique username and password.
### User Profile and Personal Information
Users, including administrators, can view and update their personal information stored in their user profile. 
### Chapters and Lessons
Students can select a chapter and view a predefined list of lessons. The system allows the student to mark lessons as viewed, but only after receiving confirmation from the student. The content of a lesson can be printed upon request.
### Key Terms
Lessons contain a series of key terms representing important concepts. Students can choose a key term, and the system displays relevant information in a separate window. After reviewing the information, the student can return to the previous lesson for further reading.
### Testing
After completing a lesson or all the lessons in a chapter, students can take a test to assess their knowledge. At the end of the test, the application provides the correct answers and the student's score, which is stored for reference.
### Test Management
Tests consist of multiple-choice questions created by the system administrator. Each question includes a statement, one or more correct answer options, and associated points for each correct option. The system stores the entered data.
### Test Modification and Deletion
Administrators have the ability to modify or delete test questions. This requires a search for the specific question using keywords or characters in the statement. The system displays a list of search results from which the administrator can choose the question to be modified or deleted.
### User Management
Administrators can modify user information, delete user accounts, and perform other related tasks.

## Getting Started

To get started with the E-Learning Application, follow these steps:

1. Clone the repository: `git clone https://github.com/mstefan2002/E-Learning.git`
2. Open the project in your preferred Java development environment.
3. Build and run the application.
4. Upon running the application, you will be presented with a login screen. Use the following credentials to log in:
- Username: admin
- Password: admin
5. Once logged in, it is strongly recommended to change your password. You can do this by navigating to your profile. Enter a new password of your choice and save the changes.
6. After changing your password, you can start creating chapters, lessons, tests, and keywords. As an administrator, you have the necessary privileges to manage the content of the application.
7. Explore the various features of the application, such as creating and organizing chapters, adding lessons within chapters, creating tests with multiple-choice questions, and associating keywords with lessons.
8. As an administrator, you have the option to create user accounts for students. You can create individual accounts for each student with a unique username and password. Once the user accounts are created, students can log in using their own credentials and access the learning materials you have created. They can view lessons, explore key terms, and take tests to assess their knowledge.
9. In the user management section, the administrator has the ability to view the test results of individual users. This feature allows the administrator to track and monitor the performance of students in their tests. By accessing the user management interface, the administrator can easily view and analyze the test scores and progress of each student, providing valuable insights for further guidance and support.

# Security
This application is currently a standalone application running on a single computer. All user data, including personal information, test results, and user profiles, is stored locally on the machine. It's important to ensure that the computer running the application is secure and protected from unauthorized access.

Note: In the future, there may be plans to add a server-side component to enhance the application's functionality and security. This could involve storing data on a remote server and implementing server-side security measures. Stay tuned for updates on the addition of server-side functionality.

# Contributing
Contributions are welcome!

# License
This project is licensed under the [MIT License](LICENSE).
