# wildfires

Install virtualbox vagrant vagrant-manager
(MacOS)
> brew install --cask virtualbox vagrant vagrant-manager
(Windows: the restart of terminal shell or windows may be required)
> winget install Oracle.VirtualBox
> winget install Hashicorp.Vagrant

Local Vagrant Setup:

0. git clone https://github.com/changwanpeng/wildfires
1. cd wildfires
2. vagrant up
3. vagrant ssh
4. cd /wildfires/backend
5. backend> mvn test
6. backend> mvn spring-boot:run &
7. cd /wildfires/frontend
8. frontend> rm -rf node_modules
9. frontend> npm install
10. frontend> npm start &
11. web brower test: http://localhost:8000
12. cd /wildfires/automation
13. automation>