echo "Building microservice user-mgmt-system..."
mvn install -DskipTests
echo "Dockerizing microservice user-mgmt-system..."
docker rm user-mgmt-system
docker build -t user-mgmt-system .
echo "Running container user-mgmt-system...and Mysql in 2 different containers using docker compose"
docker-compose up --build
