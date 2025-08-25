# AREPT1-WebServer

In this project, a web server implementation has been developed **without web frameworks**, using only `java.net`.  
The server is able to:

- Return **static files** stored in the local disk (HTML, CSS, JS, images).
- Provide a simple **REST API** for managing users (`GET`, `POST`, `DELETE`).

---

## Getting Started

This project was built with **Java** and **Maven**, and uses:

- **JUnit 5** → for testing.  
- **Jackson** → for managing JSON objects.

---

### Prerequisites

Before running the project, ensure you have installed:

- [Java SDK 21](https://jdk.java.net/21/)  
- [Apache Maven](https://maven.apache.org/)  
- [Git](https://git-scm.com/)


### Installing & Running
Clone and build the project:

```bash
git clone https://github.com/JaiderVargas05/AREPT1-WebServer.git
```
**Clones the repository** from GitHub into your local machine.

```bash
cd AREPT1-WebServer/httpserver
```
**Moves into the project folder** that was just cloned.

```bash
mvn clean install
```
**Builds the project with Maven**:  
- `clean`: removes previous compiled files.  
- `install`: compiles the source code, runs the tests, and installs the artifact (`.jar`) into the local Maven repository.  

---

Run the server:

```bash
mvn compile exec:java
```
**Starts the web server**:  
- `compile`: compiles the Java classes.  
- `exec:java`: runs the main class configured in the `pom.xml` (in this case, the web server).  

---

The server will be available at:

```
http://localhost:35000
```
The server listens on port **35000**, ready to serve static files and respond to API requests.  

---

### Examples

#### 1. Accessing static files

- `http://localhost:35000/index.html`
- `http://localhost:35000/styles.css`
- `http://localhost:35000/app.js`
- `http://localhost:35000/james.jpg`

#### 2. API: Users

- **Create user (POST):**

```
http://localhost:35000/api/users?name=Jaider&age=18
```

Response:
```json
{"id":"1","name":"Jaider","age":"18"}
```

- **Get user (GET):**

```
http://localhost:35000/api/users?id=1
```

Response:
```json
{"id":"1","name":"Jaider","age":"18"}
```

- **Delete user (DELETE):**

```
http://localhost:35000/api/users?id=1
```

Response:  
`200 OK`

---

## Running the tests

Execute the test suite with:

```bash
mvn test
```

### Test coverage

The tests validate:

- Static file serving (HTML, CSS, JS, images).  
- API endpoints for users (`GET`, `POST`, `DELETE`).  
- Error handling (`404 Not Found`).  


---

## Deployment

For deploying in a live environment:

1. Package the project:
   ```bash
   mvn clean package
   ```
2. Run the generated JAR:
   ```bash
   java -jar target/httpserver-1.0-SNAPSHOT.jar
   ```
3. Ensure the desired port (default **35000**) is open in your firewall or cloud provider.

---

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management  
* [Jackson](https://github.com/FasterXML/jackson) - JSON handling  
* [JUnit 5](https://junit.org/junit5/) - Testing framework  

---

## Versioning

This project uses Git for version control.
All changes and history can be tracked in the commit log of this repository.

---

## Authors

* **Jaider Vargas** - *Initial work & implementation*  
  [GitHub Profile](https://github.com/JaiderVargas05)

---

## Acknowledgments
* Inspired by minimal web servers and hands-on learning with `java.net`.  
