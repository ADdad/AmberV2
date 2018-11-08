### For local build ###
1. Create db instance with all tables from schema. Add some inputs in db from schema. (DB_With_User-Role_Tables.sql)
2. Check src/main/resources/application.properties and set your local db credentials.
3. Use command `mvn package` in root of the project to build jar file.
4. Use command `java -jar target/amber-0.0.1.jar` to run application.
5. Go to localhost:8080 to see your project

### For deploy on heroku ###
1. Make sure you are collaborator for our [heroku app](amber-project.herokuapp.com) (contact me if you want to contribute sdimka0777@gmail.com)
2. Check application.properties and set credentials of remote db (You can see them on app-dashboard -> resources -> heroku postgres -> settings -> Database credentials)
3. Use command `mvn package` in root of the project to build jar file.
4. Use command `heroku login` to login in your heroku account.
5. Use command `mvn heroku:deploy` to deploy.
6. Go to [your app](amber-project.herokuapp.com).