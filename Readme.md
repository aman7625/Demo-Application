## Running the Application

To run the project from the root folder run the following commands - </br> 
1) ` ./mvnw clean package`  - This will create a jar file in the target folder
2) `java -jar target\demo-0.0.1-SNAPSHOT.jar` - This will run the application as required
If the commands are executed properly the application should start running the tomcat server at port `8080`

## Endpoints
`/user` is added as the prefix request path for all the below endpoints

1) `/register` - To register a new user
2) `/login` - Check if the specified user is a valid user. This generates a jwt token in case of a valid user
3) `/welcome` - This is a restricted page (only for authorized users) and can only be accessed if proper jwt token is passed in the header.
4) `/download` - This generates a pdf file containing the information of the current user (jwt token needs to be passed in the header in order to process this request).

Screenshots for the above requests are attached in the folder named "Output Images".
