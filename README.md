# Introduction

Enhanced the drop wizard demo application with many to many database persistence (Process - Task).
Original code placed in `com.example.helloworld` package.
The custom implemented code placed in `custom` package.
Unit and integration tests has been implemented also.

# Running The Application

To test the example application run the following commands.

* To package the example run.

        mvn clean package

* To setup the h2 database run.

        java -jar target/dropwizard-example-1.0.0-SNAPSHOT.jar db migrate example.yml

* To run the server run.

        java -jar target/dropwizard-example-1.0.0-SNAPSHOT.jar server example.yml
        
# REST API Overview

All CRUD operations are implemented for `Process`, `Task` and `TaskConnection`

* Create Process with two tasks association (101 and 103)

        POST http://localhost:8080/processes
        {
          "name": "process with task assoc",
          "taskAssoc": [
            {"position": "2", "task": {"id": "103"}},
            {"position": "4", "task": {"id": "101"}}
          ]
        }
        
    201 CREATED will return, and response entity like:
        
        {
            "id": 1013,
            "name": "process with task assoc",
            "tasks": [
                {
                    "id": 103,
                    "position": 2
                },
                {
                    "id": 101,
                    "position": 4
                }
            ],
            "connections": []
        }
        
    if taskAssoc is not need we can omit this, process will be created without attached tasks
    
* Update process

        PUT http://localhost:8080/processes/1013
        {
          "name": "process updated"
        }

    response 200 OK, name changed tasks detached:
    
        {
            "id": 1013,
            "name": "process updated",
            "tasks": [],
            "connections": []
        }

* Delete process

        DELETE http://localhost:8080/processes/1013
        
    204 NO CONTENT will be response

* Create Task that exist in two processes (1001 and 1000)

        POST http://localhost:8080/tasks
        {
          "name": "process with task assoc",
          "processAssoc": [
            {"position": "135", "process": {"id": "1000"}},
            {"position": "136", "process": {"id": "1001"}}
          ]
        }
        
    201 CREATED response:
    
        {
            "id": 1014,
            "name": "process with task assoc",
            "category": null,
            "loc": null,
            "processes": [
                {
                    "id": 1000,
                    "position": 135
                },
                {
                    "id": 1001,
                    "position": 136
                }
            ],
            "fromConnections": null,
            "toConnections": null
        }
    
* Create TaskConnection from Task(id 101) to Task(id 103) in Process(id 100)

        POST http://localhost:8080/connections
        {
          "fromConnector": "A",
          "toConnector": "B",
          "from": {"id": "101"},
          "to": {"id": "103"},
          "process": {"id": "100"}
        }
        
    201 CREATED response
    
        {
            "id": 1018,
            "name": null,
            "fromTask": 101,
            "toTask": 103,
            "process": 100,
            "fromConnector": "A",
            "toConnector": "B"
        }
        
    then get this 100 process
    
        GET http://localhost:8080/processes/100
        {
            "id": 100,
            "name": "go.GraphLinksModel",
            "tasks": [
                {
                    "id": 103,
                    "position": 0
                },
                {
                    "id": 101,
                    "position": -13
                }
            ],
            "connections": [
                {
                    "id": 1018,
                    "name": null,
                    "fromTask": 101,
                    "toTask": 103,
                    "process": 100,
                    "fromConnector": "A",
                    "toConnector": "B"
                }
            ]below
        }
    
    connection 1018 was added
    If you try create connection with broken Process - Task association, wou will get bad response error.
    
* Others operations are similar described above