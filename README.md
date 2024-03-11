# wildfires

# Install virtualbox vagrant vagrant-manager on local machine, for example in MacOS:
> brew install --cask virtualbox vagrant vagrant-manager

# Build, Deploy, and Test:
0. git clone https://github.com/changwanpeng/wildfires
1. cd wildfires
2. vagrant up
3. vagrant ssh
# vagrant VM (ubuntu) start
4. cd /wildfires/backend
5. backend> mvn test
6. backend> mvn spring-boot:run &
7. cd /wildfires/frontend
8. frontend> rm -rf node_modules
9. frontend> npm install
10. frontend> npm start &
11. test web brower: http://localhost:8000
# vagrant VM (ubuntu) end

# Install chromedriver and the following line need to be changed for automation.
    # (ResultPageTest.java)
    String driverpath="/Users/mchang/bin/chromedriver";
1. (local machine) wildfires> cd automation
2. automation> mvn test

# How to query the database to find out how many requests have been made against the API:
The application is writing its log to stdout that can be easily connected to a log database, like Elasticsearch, and then count the number of requests. The log statement in WildFireRestController:
  System.out.println("The request URL of the endpoint '/api/openmaps' is " + requestURL); //Line 43