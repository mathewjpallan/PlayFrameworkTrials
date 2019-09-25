# PlayTrials
Trying play framework and trying to understand the impact of blocking/non-blocking DB calls in play framework. Also trying to see the impact of using Akka actors (blocking/non-blocking) in playframework for DB calls.

## What this repo contains
This repo contains a sample play framework (2.7.3) project that exposes the below mentioned APIs. The APIs read a random node from Neo4j (Graph DB) and so Neo4j needs to be running on the same machine on the default port.

```
GET /test - This is a test API to check if the play server is running fine.              
GET /getData - This is a test API which reads a node from Neo4j and uses a blocking call to Neo4j            
GET /getDataAsync - This is a test API which reads a node from Neo4j and uses a non-blocking call to Neo4j
GET /getDataAsync2 - This is a test API which invokes the Neo4j read twice(non-blocking) and was created to understand the impact of throughput when we have 2 calls to the DB.     
GET /getDataAkka - This is a test API that uses 2 AKKA actors in its processing. The GraphEngine actor is used to read data from Neo4j and the RequestRouter is used to route the requests to a pool of GraphEngine actors. The GraphEngine actor uses a blocking call to Neo4j.
GET /getDataAkkaAsync - This is a test API that uses an actor to fetch the node from Neo4j. The actor(GraphEngineAsync) is injected into the controller and the actor makes a non-blocking call to Neo4j  
GET /getDataAkkaAsync2 - This API is similar to /getDataAkkaAsync with the only difference being that it makes 2 calls to the actor and thereby helps understand the impact of throughput when we have 2 calls to the DB.
```

## How to run
- Neo4j and Playframework requires Java 8 to run so Java 8 has to be installed
- Download and run neo4j. 
- Clone the repo and execute the following commands
```
cd clonedfolder/
mvn clean package
mvn play2:start
mvn play2:stop //to stop the server after your tests
```

## Benchmarking Results
- The objective was to understand the impact of non-blocking calls to the DB and also the impact of using Akka actors in playframework. Hence there were no optimizations applied on the java runtime/linux configuration but all the tests were run in an indentical environment.
- The tests were run on a laptop running Ubuntu 16.04 with 4 cores (Intel® Core™ i5-6300U CPU @ 2.40GHz) and 16 GB of RAM. 
- For every test (TestID in the below table) the same scenario was executed twice and the RunID reflects the same. The play server was restarted after every TestID execution. 
- Netty server was configured for the test instead of the default Akkahttp server that comes with Play 2.7. 
- AB tool (ab -k -c 200 -n 100000 http://localhost:9000/test) was used to simulate the load. 
- All test result times are in millisecond. 
- All tests were run with 200 concurrent requests and 100000 requests in total.

| Test ID	| Run ID	| API	| RPS	| Mean Response Time| 	Max Response Time	| 99 percentile| 
| --- | --- | --- | --- | --- | --- | --- | 
|1	|1	|http://localhost:9000/test	|11595	|17	|2371	|90|
|1	|2	|http://localhost:9000/test	|39059	|5	|1142	|16|
|2	|1	|http://localhost:9000/getData	(Blocking)|2937	|68	|6028	|293|
|2	|2	|http://localhost:9000/getData	(Blocking)|3900	|51	|1478	|159|
|3	|1	|http://localhost:9000/getDataAsync	|2792	|71	|2115|	330|
|3	|2	|http://localhost:9000/getDataAsync	|5137	|38	|1112|	75|
|4	|1	|http://localhost:9000/getDataAsync2	|2293	|87	|2273	|391|
|4	|2	|http://localhost:9000/getDataAsync2	|3318	|60|	1170|	99|
|5	|1	|http://localhost:9000/getDataAkka	(Blocking)|2618	|76	|2372	|300|
|5	|2	|http://localhost:9000/getDataAkka (Blocking)	|3416	|58|	1117|	211|
|7	|1	|http://localhost:9000/getDataAkkaAsync	|2924	|68	|2241	|318|
|7	|2	|http://localhost:9000/getDataAkkaAsync	|4981	|40|	1170|	87|
|8	|1	|http://localhost:9000/getDataAkkaAsync2	|2350	|85	|2096	|417|
|8	|2	|http://localhost:9000/getDataAkkaAsync2	|3370	|59|	1123|	94|
