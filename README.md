# Welcome to Enliven Wild Space

This project is to enliven microservice concepts with all modern programming languages and inspire ourselves to code more.

```
en·liv·en
/inˈlīvən,enˈlīvən/
verb
make (something) more entertaining, interesting, or appealing.
make (someone) more cheerful or animated.
Chinese 
活躍,使生動
```

## Auth Serivce - OAuth based authentiation service ##
### Business workflow
1.  Login/Auth mechanism
* Support User/Password with local userstore
* Support OpenAuth to Facebook, LinkedIn, Twitter, Google
* Support Spring Boot AuthorizationServer

2. User application data
* Show users' social user info
* Link to user's social media applications
* Remeber my login session in desire peroid from same device or 
* Remeber the login device with extra permit action (advanced)

### Main Techs
SpringBoot Framework(Java), Bootstrap(JavaScript), Gradle(for packaging and build), AWS Service APIs
Html5, CSS, ReactJS/Redux(Javascript)

### Code space
```
src/main/java/com/enliven/authservice --  authentication service module
```

### Implementation
1. auth-service: support OAuth2, authorization, login page
* application.yml defines OAuth2 setting, social media login API token, client authorization-service, and default Sprint Boot service http setting 
2. auth-service-client: support client to use auth-service
* client.yml defines client of auth-service, Sprint Boot service http setting, authorization-service access setting

### Deployment
```
cd auth-service
gradle build bootRun

cd auth-serivce-client
gradle build bootRun
```
Browse access http://localhost:9999/client  

```
Users enter browser ----> http://localhost:9999/client

Browser redirect to ----> http://localhost:8080

Users click github login ----> 
Navigated to ----> https://github.com/login?client_id=bd1c0a783ccdd1c9b9e4&return_to=%2Flogin%2Foauth%2Fauthorize%3Fclient_id%3Dbd1c0a783ccdd1c9b9e4%26redirect_uri%3Dhttp%253A%252F%252Flocalhost%253A8080%252Flogin%252Fgithub%26response_type%3Dcode%26state%3DeVwCPB

XHR ----> OPTION https://api.github.com/_private/browser/stats
XHR ----> POST "https://api.github.com/_private/browser/stats
Navigated to ----> http://localhost:8080/oauth/authorize?client_id=acme&redirect_uri=http://localhost:9999/client/login&response_type=code&state=tyLQNh

Navigated to ----> http://localhost:9999/client/
```


  

