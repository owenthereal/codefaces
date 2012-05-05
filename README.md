CodeFaces
=========

By [Jingwen Owen Ou](https://twitter.com/#!/JingwenOwenOu) and [KK Lo](https://twitter.com/#!/nondeterminism).

[CodeFaces](http://codefaces.org) is a web-based source control client that targets at easing the pain of navigating codes from a source control system on a browser. It allows connecting to multiple source control systems and structurally navigating codes on a unified web interface.

## About

Veteran programmers read thousands of lines of codes on the web everyday. However, [most source control system’s web interfaces are far from ideal](http://owenou.com/2010/07/05/why-code-navigation-sucks-on-most-scm-web-interface.html). It would be nice to build a web-based tool that makes the experiences of reading codes on the web smooth and enjoyable.

## Compiling from source

You will need:

* JDK 1.5 or greater
* Ruby 1.8.7 or greater

To compile, just type:

```bash
$ cd org.codefaces.build
$ bundle install
$ bundle exec rake package
```

This will package a war file to a destination folder. You will find the settings in `org.codefaces.build/config/config.yml`.

## Setting up for development

You will need [Eclipse RAP](http://www.eclipse.org/rap) 1.3.0 or
greater. Further set up steps are available [here](http://www.eclipse.org/rap/downloads/).

## On the news

We are proud to be listed as one of the demo apps on Eclipse RAP: [http://www.eclipse.org/rap/users/](http://www.eclipse.org/rap/users/).

## More information

Visit [http://codefaces.org](http://codefaces.org) for more information.

## License

[Eclipse Public License 1.0](http://opensource.org/licenses/eclipse-1.0.php)
