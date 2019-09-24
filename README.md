# Lokal

> i18n & l10n for (isomorphic) Scala applications

Please visit the [documentation](https://lokal.taig.io/) microsite to learn how to install and use _Lokal_.

## Building the microsite

The microsite relies on [`sbt-microsites`](https://github.com/47deg/sbt-microsites) and does therefore require `ruby` and `jekyll` to be installed on your system. When these requirements are met, the microsite can be built as follows.

```
sbt website/makeMicrosite
cd website/target/site/
jekyll serve
```

Alternatively, when `ruby` and `jekyll` are not available the microsite can be built via docker.

```
docker build -t lokal .
docker run -it -p 4000:4000  -v $PWD:/home/lokal/ lokal 
sbt website/makeMicrosite
cd website/target/site/
jekyll serve --host=0.0.0.0
```

The site can now be opened in a web browser at [`http://localhost:4000/`](http://localhost:4000/).