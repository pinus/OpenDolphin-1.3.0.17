#!/bin/sh
mvn install:install-file -Dfile=glulogic.jar -DgroupId=jp.motomachi-hifuka -DartifactId=glulogic -Dversion=1.0 -Dpackaging=jar

mvn install:install-file -Dfile=quaqua-pns.jar -DgroupId=jp.motomachi-hifuka -DartifactId=quaqua-pns -Dversion=7.3.4-pns -Dpackaging=jar

mvn install:install-file -Dfile=Quaqua.jar -DgroupId=jp.motomachi-hifuka -DartifactId=quaqua-pns -Dversion=9.1-pns -Dpackaging=jar

