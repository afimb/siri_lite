# siri_lite 

Siri Lite is a java project that provides ta bridge between a web service Siri server and Siri lite clients with following features

* Uses SIRI 2.0 FR-IDF 2.4 local agreement
* implements stop monitoring, vehicle monitoring, general message, stop dicovery and line discovery services


This java project is split into modules :

* siri_lite.server : **REST server (ear)**
* uk.org.siri.siri : siri xsd mapped classes
* uk.org.siri.wsdl : siri wsdl mapped classes
* siri_lite.common : Common classes and interfaces
* siri_lite.discovery : Discovery services bridge implementation
* siri_lite.stop_monitoring : Stop monitoring service bridge implementation
* siri_lite.vehicle_monitoring : Vehicle monitoring service bridge implementation
* siri_lite.general_message : General message service bridge implementation
* siri_lite.api : REST API implementation

## Release Notes

The release notes (in French) can be found in [CHANGELOG](./CHANGELOG.md) file 

## Requirements
 
This code has been run and tested on [Travis](http://travis-ci.org/afimb/chouette?branch=master) with : 
* oraclejdk7
* oraclejdk8
* openjdk7
* openjdk8
* wildfly 8.2.0
* wildfly 9.2.0

## External Deps

On Debian/Ubuntu/Kubuntu OS : 
```sh
sudo apt-get install openjdk-7-jdk 
```

For installation from sources : 
```sh
sudo apt-get install git
sudo add-apt-repository ppa:natecarlson/maven3
sudo apt-get update 
sudo apt-get install maven3
sudo ln -s /usr/share/maven3/bin/mvn /usr/bin/mvn
```
if ```apt-get update``` fails, modify file :
/etc/apt/sources.list.d/natecarlson-maven3-trusty.list
value ```trusty``` by ```precise``` 

## Installation

### Installation from sources

Get git repository :
```sh
git clone -b V1_1 git://github.com/afimb/siri_lite
cd siri_lite
```

Test :

```sh
mvn test -DskipWildfly
```

Deployment :

[Install and configure Wildfly](./doc/install/wildfly.md) 

deploy ear (wildfly must be running)
```sh
mvn -DskipTests install
```

### Installation from binary
download siri_lite.x.y.z.zip from [maven repository](http://maven.chouette.mobi/siri/siri_lite/siri_lite.server)

[Install and configure Wildfly](./doc/install/wildfly.md) 

in wildfly installation repository :
```sh
bin/jboss-cli.sh connect, deploy --force  (path to ...)/siri_lite.ear
bin/jboss-cli.sh -c "/system-property=siri_lite.producer.address:add(value='web service SIRI server endpoint address')"
```

## Documentation
 
Documentation is available in french on [Chouette Project website](http://www.chouette.mobi).

## License
 
This project is licensed under the CeCILL-B license, a copy of which can be found in the [LICENSE](./LICENSE.md) file.

 
## Support
 
Users looking for support should file an issue on the GitHub [issue tracking page](../../issues), or file a [pull request](../../pulls) if you have a fix available.
