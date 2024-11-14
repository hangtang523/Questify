# Questify

Questify: An On-line Questionnaire Survey and Management System

## Description

This on-line questionnaire survey ans management system is to provide an efficient,  flexible and secure questionnaire management platform. The front desk features  focus on the user experience, ensuring that users can easily create and manage  questionnaires and get feedback in real time. The background function is committed  to the stable operation and management of the system, and helps administrators  effectively maintain the system and manage user data through a centralized  management interface. The overall design focuses on compatibility and extensibility  to adapt to possible future changes in requirements and functional expansion.

## Getting Started

### Dependencies

* JDK 8
* Maven 3.9
* MySQL 8
* Node.js 12 | 14 | 18

### Installing

* git clone https://github.com/hangtang523/Questify.git
* reset your own resources files, e.g. application.yml

### Executing program

* Front-End

  On the project directory, enter the following commands:

  ``` shell
  cd ./frontend/
  npm install
  npm run dev
  ```

* Back-End

​		Run SpringBoot Application in IDE. 

## Roadmap

### Sprint 0: Overall Framework & Notice Module
User Cases:
1. System Initialization
- Build backend, frontend, and database framework.
2. Notice Management
- Create Notice: Administrator creates a Notice.
- Delete Notice: Administrator deletes a Notice.
- Edit Notice: Administrator edits a Notice.
- Query Notice: Administrator queries a notice
  WBS Tasks:
3. Analysis & Design
- Define the workflow for creating and displaying and deleting Notices.
- Effort: 0.5 days
4. Development
- Build the backend and frontend framework.
- Develop APIs and frontend components for Notice management.
- Effort: 0.5 days
5. Testing
- Perform unit testing on the Notice module.
- Effort: 0.5 days
6. Deployment
- Deploy the system framework and Notice module in the staging environment.
- Write API documentation for Notice creation and display.
- Effort: 0.5 days

### Sprint 1: Admin-side user Management and Questionnaire Administration
  User Cases:
1. User Login/Out
- User Registration: Allow users to register.
- User Login: Enable users to log in.
- Profile Management: Update personal information and reset passwords.
2. Admin Questionnaire Administration
- Manage Questionnaire: Administrator modifies  questionnaire content.
- Manage Question: Administrator modifies  questionnaire content.
- Manage Option: Administrator modifies  question content.
- Query and View Response: Administrator reviews and processes submitted responses.
  WBS Tasks:
3. Analysis & Design
- Design workflows for user authentication, registration, and profile management.
- Define questionnaire Question Option creation, editing, and logic workflows.
- Effort: 0.5 days
4. Development
- Implement user management functionalities (login, registration, profile updates).
- Develop admin client for questionnaire,question,option management.
- Develop answer management.
- Effort: 5 days
5. Testing
- Conduct integration testing for user and questionnaire modules.
- Debug issues related to user management and questionnaire functionalities.
- Effort: 2 days
6. Deployment
- Deploy user and questionnaire modules to staging.
- Write detailed API documentation for the new functionalities.
- Effort: 1days

### Sprint 2: User-Side Questionnaire Interaction
User Cases:
1. User Client Questionnaire Interaction
- View Questionnaires template: Questionnaire: users can view available questionnaires.
- Preview Questionnaire: Questionnaire: users can preview a questionnaire before answering.
- Copy Template: Questionnaire: Users can copy an existing questionnaire template for reuse.
- Edit Questionnaire,Question,Option:Users can edit the specific content of a questionnaire
2. Questionnaire Sharing
- Publish and Share Questionnaire: Generate links codes for sharing questionnaires.
3.  Answer Questionnaire: Users can submit responses.
    WBS Tasks:
4. Analysis & Design
- Design workflows for displaying, previewing, and answering questionnaires.
- Define template copying and sharing functionality.
- Effort: 1 days
5. Development
- Implement frontend interfaces for questionnaire display, preview, and answering.
- Add backend support for template copying.
- Develop sharing functionality with link and QR code generation.
- Effort: 8 days
6. Testing
- Test functionality of template copying and sharing features.
- Effort: 3 days
7. Deployment
- Deploy the user-side questionnaire interaction features.
- Write user guides and API documentation.
- Effort: 2 days

### Sprint 3: Data Analysis & Reporting
  User Cases:
1. Data Analysis
- Collect Results: Administrators can collect basic statistical analysis.
- Generate Charts: Administrators can view visualized results.
  WBS Tasks:
2. Analysis & Design
- Define data processing workflows for analysis and chart generation.
- Identify key metrics for statistical analysis.
- Effort: 3 days
3. Development
- Implement backend data processing and analysis functionalities.
- Build chart generation for visualized data representation.
- Effort: 3days
4. Testing
- Test the accuracy of data analysis and chart visualization.
- Validate system performance for large datasets.
- Effort: 2 days
5. Deployment
- Deploy the data analysis module.
- Write documentation for the data processing pipeline and administrator guides.
- Effort: 2 days



## Authors

Contributors names and contact info

[@hangtang523](https://github.com/hangtang523)	[@ChenBowen-se](https://github.com/ChenBowen-se)	[@LiuYihan111](https://github.com/LiuYihan111)	[@RDKKKKKK](https://github.com/RDKKKKKK)	[@zscmngykx](https://github.com/zscmngykx)

## Version History

Refer to [GitHub Releases](https://github.com/hangtang523/releases)

