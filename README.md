# Weather Query API

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green)
![Maven](https://img.shields.io/badge/Maven-3.9.x-orange)
![License](https://img.shields.io/badge/License-MIT-yellow)

A simple and robust REST API built with Spring Boot to query real-time weather data from the [OpenWeatherMap API](https://openweathermap.org/). The API allows users to retrieve weather information for a specified city, including temperature, weather description, rainfall, and personalized clothing and umbrella advice.

## Features
- Retrieve current weather data for any city.
- Custom advice for clothing and umbrella based on temperature and rainfall.
- Comprehensive error handling for invalid inputs, missing cities, and API errors.
- Secure API key management using environment variables.
- Structured JSON responses with `WeatherDTO` for success and `ErrorResponse` for errors.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Example Responses](#example-responses)
- [Technology Stack](#technology-stack)
- [Contributing](#contributing)
- [Future Plans](#future-plans)
- [License](#license)

## Prerequisites
- **Java 17** or later
- **Maven 3.9.x** or later
- **OpenWeatherMap API Key**: Sign up at [OpenWeatherMap](https://openweathermap.org/) to obtain a free API key.
- IDE (e.g., IntelliJ IDEA, Eclipse, or VS Code) with Lombok support enabled.
- Optional: [Postman](https://www.postman.com/) or `curl` for testing API endpoints.

## Installation
1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/weather-query-api.git
   cd weather-query-api
