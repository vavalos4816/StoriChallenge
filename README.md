# StoriChallenge
This repository stores the code challenge for Stori
Steps to make it work:
Make sure you have a MySQL database called "stori" in your your localhost. For quick creation, import stori.sql file to avoid issues.
Import the project to any Java IDE
Make sure the CSV file exists under src/mypackage and have valid entries
In the class SendEmail, update the emails and credentials 
If using Gmail, disable 2 step verification and allow access to less secure applications here: https://myaccount.google.com/lesssecureapps
Run the project, the entries on the CSV will be registered if not exist in the table, the information will be queried and sent to the specified email address
