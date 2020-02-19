## Assumptions

Dates are input in the same format they are to be output

No header rows in files

In cases where there are duplicates and no secondary sort the final order is not entirely stable

## Running the project

*NOTE: Run all commands from the project directory.*

You can run the project either as a command-line application, or as a REST server.

To run the project as a command-line application, use the `cli` alias:

```
clj -Acli [SORT] [FILES ...]
```

`SORT` is either:

 1. “gender” to sort by gender (females first) then by last name
 2. “birthdate” to sort by birth date ascending.
 3. “name” to sort by last name, ascending.
 
 `FILES` are the names of one or more files (either comma, space, or pipe separated) to be processed.
 
 The program will read in the files, sort the data, and print it as comma separated values to standard out.
 
 To run the project as a REST server, use the `server` alias:
 
 ```
clj -Aserver [PORT]
```

`PORT` is the port on which you’d like the server to listen.

The REST server responds to requests as specified in the project description. Here are some example `curl` commands:

```
curl -vv -XPOST http://localhost:8080/records --data-binary @comma.csv
```

```
curl -XGET http://localhost:8080/records/birthdate
```

```
curl -XGET http://localhost:8080/records/gender
```

```
curl -XGET http://localhost:8080/records/name
```

The first will cause the server to process and store the submitted file. The other three will return `text/plain` responses of the sorted data.
