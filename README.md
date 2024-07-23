# T8sGm9ZqL3xYp4RjWdN5

Java version 17

Swagger Ui
http://localhost:8080/swagger-ui/index.html#/

DB
http://localhost:8080/h2-console/

Post
http://localhost:8080/weather-service/update
{
"sensorId": "sensor-1",
"date": "2024-08-18",
"temperature": 22,
"humidity": 60.0,
"windSpeed": 5.0
}

Get
Latest
http://localhost:8080/weather-service/latest

Get
latest by sensorId
http://localhost:8080/weather-service/sensor-1

Get all sensors with multiple metrics, average statistic, with a time range
http://localhost:8080/weather-service/all-sensors/temperature,humidity,windspeed/average?from=2024-07-17&to=2024-08-19

TODO:

CSRF
input validation
Exception Handling