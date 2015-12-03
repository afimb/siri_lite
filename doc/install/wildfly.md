# Install and configure Wildfly

Download
--------
download Wildfly 8.2.0.Final from [http://wildfly.org/downloads/](http://wildfly.org/downloads/)

uncompress wildfly in installation directory (/opt for exemple)

Setup
-----

in installation directory (/opt/wildfly-8.2.0.Final)
start server : 
on default ports (8080 and 9990 for administration)
```sh
bin/standalone.sh -c standalone-full.xml
```
if port 8080 is used (8180 and 10090 for adminstration)
```sh
bin/standalone.sh -c standalone-full.xml -Djboss.socket.binding.port-offset=100
```
add a managment user for web administration console
```sh
bin/add-user.sh
type: management user (a)
login : admin
password : admin
```

Install as service
------------------

On github sukharevd gives a shell to download and install as a linux service :

[wildfly-install.sh](https://gist.github.com/sukharevd/6087988)

after using it, just process steps from "add a managment user for web administration console"