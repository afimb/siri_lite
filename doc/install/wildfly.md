# Install and configure Wildfly

Download
--------
* Download [Wildfly 8.2.0.Final](http://wildfly.org/downloads/)

* Uncompress Wildfly in installation directory (/opt for example)

Setup
-----

In installation directory (/opt/wildfly-8.2.0.Final),
start server :
* On default ports (8080 and 9990 for administration)
```sh
bin/standalone.sh -c standalone-full.xml
```
* If port 8080 is used (8180 and 10090 for administration)
```sh
bin/standalone.sh -c standalone-full.xml -Djboss.socket.binding.port-offset=100
```

Add a management user for web administration console :
```sh
bin/add-user.sh
type: management user (a)
login : admin
password : admin
```

Install as a service
------------------

On github [sukharevd](https://gist.github.com/sukharevd) gives a shell to download and install as a Linux service :  

[wildfly-install.sh](https://gist.github.com/sukharevd/6087988)

After using it, just process steps from "add a managment user for web administration console".
