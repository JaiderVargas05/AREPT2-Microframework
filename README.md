# AREPT2-Microframework

This project aims to enhance an existing web server, which currently supports HTML files, JavaScript, CSS, and images, by converting it into a fully functional web framework. This framework will enable the development of web applications with backend REST services. The new framework will provide developers with tools to define REST services using lambda functions, manage query values within requests, and specify the location of static files.

---
## Architecture

### Main components

- **`HttpServer`** – Accepts TCP connections via `ServerSocket`, parses the request line, routes `/api/...` to services or serves static files from disk. It writes the HTTP status line, headers (`Content-Type`, `Content-Length`, `Connection`) and the response body.
- **`WebApplication`** – Application entry point: registers the static files directory and binds service routes (e.g., `/users`), then starts the server.
- **`Service`** – Functional interface for HTTP handlers (`executeService(HttpRequest, HttpResponse)`).
- **`HttpRequest`** – Minimal wrapper around the request URI with helpers to read query parameters.
- **`HttpResponse`** – Holder for response metadata (status, content type). Extend as needed.

### Request flow

```
Client
  |
  v
[HttpServer] --accept--> [Socket]
  | parse "METHOD /path HTTP/1.1"
  +--> if path starts with /api -----> [Service Router] -> [Service impl] -> (User repo)
  |                                       | sets status/content-type on HttpResponse
  |                                       '-- returns JSON/body (String/bytes)
  '--> else ----------------------------> [Static Handler] -> read file -> MIME + bytes

[HttpServer] writes:
- Status-Line + Headers (Content-Type, Content-Length, Connection: close)
- CRLF (\r\n)
- Body
then closes the socket
```
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
git clone https://github.com/JaiderVargas05/AREPT2-Microframework.git
```
**Clones the repository** from GitHub into your local machine.

```bash
cd AREPT2-Microframework/httpserver
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

- **Get user (GET):**

```
http://localhost:35000/api/users?id=1
```

Response:
```json
{"id":1,"name":"Jaider","age":18}
```
---

## Running the tests

Execute the test suite with:

```bash
mvn test
```

## What the tests cover

- **Server up & index** — `GET /` returns `200` and `text/html`.
- **Static files** — CSS, JS and JPG are served with the correct content types and non‑empty content.
- **404 for missing file** — `GET /eci.com` → `404`.
- **Users API round‑trip** — `GET /api/users?id=<id>`; asserts `id`, `name`, `age`.
- **User not found** — `GET /api/users?id=999999` → `404`.


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
