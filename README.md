# How to Serverless with AWS - Part 1

> This is Part 1 in a series of 3 workshops on how to build Serverless applications using AWS Technologies
>
> [Part 2](https://github.com/MihaiBogdanEugen/how-to-serverless-with-aws-part2)
>
> [Part 3](https://github.com/MihaiBogdanEugen/how-to-serverless-with-aws-part3)

## Prerequisites
- [AWS Command Line Interface](https://aws.amazon.com/cli/) installed and properly set up
- access to the Amazon Console GUI
- [Amazon Corretto 11](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/what-is-corretto-11.html)
- [Python 3.8.x](https://www.python.org/) 

## Introduction

Movies is a fictional application used for the management of movies.

### Data Models

#### Movie Info

| Property          | Type   |
|-------------------|--------|
| movie_id          | String |
| name              | String |
| country_of_origin | String |
| release_date      | String |

#### Movie Rating

| Property               | Type   |
|------------------------|--------|
| movie_id               | String |
| rotten_tomatoes_rating | Number |
| imdb_rating            | Number |

### Data Flows
- batch ingestion: periodically, a new file is uploaded to the S3 bucket, containing all Movie Info objects
- single update: occasionally, ratings (a Movie Rating object) are updated
- data retrieval: frequently, a full Movie model is retrieved  

## Architecture

![Movies Architecture](https://raw.githubusercontent.com/MihaiBogdanEugen/how-to-serverless-with-aws-part1/master/movies-architecture.png)

## AWS Technologies

### [AWS Lambda](https://aws.amazon.com/lambda/)
- Serverless, event-driven, stateless compute workloads 
- Lightweight, limited execution time, resource-bound
- Works best as "glue" between other stateless AWS services

### [AWS DynamoDB](https://aws.amazon.com/dynamodb/)
- Highly performant, Key-Value and Document NoSQL Data Store
- Serverless, automatically scalable
- Supports ACID transactions 

### [AWS S3](https://aws.amazon.com/s3/)
- Simple and secure object storage
- Serverless & stateless
- Object management supports event notifications

### [AWS API Gateway](https://aws.amazon.com/api-gateway/)
- Basic API Gateway, translates events from "web" protocols (HTTP, WebSockets, etc.) into AWS events 
- Fully managed service

### Others
- [AWS IAM](https://aws.amazon.com/iam/) - secures access to AWS services and resources
- [AWS CloudWatch](https://aws.amazon.com/cloudwatch/) - logging and monitoring for AWS services
- [AWS X-Ray](https://aws.amazon.com/xray/) - tracing for AWS services

## Action Plan

1. Create the DynamoDB tables, note down their names
2. Create the S3 buckets, note down their name
3. Create the IAM Policies using the values from 1. and 2.
4. Create the IAM Roles for `lambda.amazonaws.com` trusted entities, using the previously defined policies
5. Package the AWS Lambdas into zip archives using the `package` make target
6. Create AWS Lambdas used the packages generated at 5., the roles defined at 4.
7. Configure AWS Lambdas with proper environment variables, memory settings and tracing
8. Create an API Gateway
9. Add required resources for route `movies/{movieId}`
10. Add `get` method with AWS Lambda Proxy integration for specific lambda
11. Add `patch` method with AWS Lambda Proxy integration for specific lambda
12. Create a deployment for the API Gateway 
13. Add S3 bucket integration for specific lambda

## Test

- Generate input test file:
```csv
cat movie-infos.csv
```
```csv
mv1,Interstellar,United States,2014-10-26
mv2,The Dark Knight Rises,United States,2012-07-16
mv3,The Prestige,United Kingdom,2006-10-17
```
- Upload file to S3 bucket
```bash
aws s3 cp ~/movie-infos.csv s3://${bucket_name}
```

- Send PATCH request
```bash
curl --request PATCH \
  --url ${API_GW_DEPLOYMENT_URI}/movies/mv1 \
  --header 'content-type: application/json' \
  --data '{
	"movieId": "mv1",
	"rottenTomatoesRating": 98,
	"imdbRating": 43
}'
```
- Send GET request, verify results
```bash
curl --request GET \
  --url ${API_GW_DEPLOYMENT_URI}/movies/mv1
```