So far this plugin only holds a way to request Android runtime permissions from
JavaScript. In the future, other platform code can be added here.

The filesystem paths in the following are examples. You'll have to adapt them to
your machine.


Work with the code
------------------

There are two things you can do:

1. Edit, lint and compile the (Java) code.
2. Install the plugin in a Cordova project (eg. Mind Ease), compile and test it
   as part of the app.


### Set the classpath for editing and compiling

    export CLASSPATH=$(bb classpath --android-home=~/Android --app-home=~/Repos/MindEase/app)

This is useful when you're using an editor with built-in linting. Or you can
just run this:

    javac -Xlint src/android/Permission.java


### Prettify the code

    npm run prettify

When you use opinionated autoformatters, you don't have to think about how to
format things.


### Install the plugin in a Cordova project

There are two parts to this: Installing the plugin initially, then reinstalling
JavaScript files whenever they change. Initial plugin installation:

    cd ~/Repos/MindEase/app
    cordova plugin add --link ~/Repos/cordova-plugin-mindease

Reinstallation of JavaScript files:

    cd ~/Repos/cordova-plugin-mindease
    bb reinstall-js --app-home=~/Repos/MindEase/app

http://eradman.com/entrproject/ can be used to run this automatically when files
change. Note that reinstall-js only reinstalls www/mindease.js so far. If you
want to make it handle multiple files, you have to extend it.

Why is the above necessary? `cordova plugin add --link` symlinks some files, but
not the JavaScript files. It wraps the code from JavaScript files in a function
and writes it to multiple places in a Cordova project. The problem is that it's
very slow. This is why I made a fast alternative.
