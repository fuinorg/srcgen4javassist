# srcgen4javassist
A small wrapper for Javassist that can be used to generate source files and bytecode.

For more details and examples see:
http://www.fuin.org/srcgen4javassist/index.html

[![Build Status](https://fuin-org.ci.cloudbees.com/job/srcgen4javassist/badge/icon)](https://fuin-org.ci.cloudbees.com/job/srcgen4javassist/)

<a href="https://fuin-org.ci.cloudbees.com/job/srcgen4javassist"><img src="http://www.fuin.org/images/Button-Built-on-CB-1.png" width="213" height="72" border="0" alt="Built on CloudBees"/></a>

###Snapshots

Snapshots can be found on the [OSS Sonatype Snapshots Repository](http://oss.sonatype.org/content/repositories/snapshots/org/fuin "Snapshot Repository"). 

Add the following to your .m2/settings.xml to enable snapshots in your Maven build:

```xml
<repository>
    <id>sonatype.oss.snapshots</id>
    <name>Sonatype OSS Snapshot Repository</name>
    <url>http://oss.sonatype.org/content/repositories/snapshots</url>
    <releases>
        <enabled>false</enabled>
    </releases>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```
 