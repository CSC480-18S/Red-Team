# Scrabble Dictionary API

Dictionary API for the Scrabble Game

## Getting Started

### Prerequisites
```
1. Java 8
2. Maven
```

### Running
The application can be run in two ways:

#### Using the Spring Boot Maven Plugin
```
$ mvn spring-boot:run
```

#### Packaging and then running the executable jar
```
$ mvn package
$ java -jar target/dictionary-<version>.jar
```

## Using the REST API
* Check whether each word of the word list is valid, bad and special: `GET /dictionary/validate?words=word1,word2,word3,...`

## Built With
* [Spring](http://spring.io/)

## Author
* **Shakhar Dasgupta**

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
