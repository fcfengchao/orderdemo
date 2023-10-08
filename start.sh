#!/bin/bash

# Build the Docker image
docker-compose build

# Start the Docker containers
docker-compose up -d

# Wait for the application to start
sleep 30

# Run any additional setup or tests if needed
# e.g., Run integration tests
# docker-compose exec app ./gradlew test

# Print logs to the console
docker-compose logs -f
