### comdor

[![Build Status](https://travis-ci.org/amihaiemil/comdor.svg?branch=master)](https://travis-ci.org/amihaiemil/comdor)
[![Coverage Status](https://coveralls.io/repos/github/amihaiemil/comdor/badge.svg?branch=master)](https://coveralls.io/github/amihaiemil/comdor?branch=master)
[![PDD status](http://www.0pdd.com/svg?name=amihaiemil/comdor)](http://www.0pdd.com/p?name=amihaiemil/comdor)

[![DevOps By Rultor.com](http://www.rultor.com/b/amihaiemil/comdor)](http://www.rultor.com/p/amihaiemil/comdor)
[![We recommend IntelliJ IDEA](http://amihaiemil.github.io/images/intellij-idea-recommend.svg)](https://www.jetbrains.com/idea/)

Comdor can help you automate anything related to your project on Github.

More details coming soon.

### Contributing 

If you would like to contribute, just open an issue or a PR.

Make sure the maven build:

``$mvn clean install -Pcheckstyle``

passes before making a PR. [Checkstyle](http://checkstyle.sourceforge.net/) will make sure
you're following our code style and guidlines.

### Running Integration Tests

In order to run the integration tests add the ``itcases`` profile to the maven command:

``$mvn clean install -Pcheckstyle -Pitcases``

Docker has to be intalled on the machine, with the default configuration, in order for the IT cases to work.
