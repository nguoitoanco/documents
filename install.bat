mvn clean package
docker build -f src/main/docker/Dockerfile -t app .
cd src/main/docker
docker-compose up
