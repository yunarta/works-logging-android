# works-logging-android

Logging library for Android by using java.util.Logger rather than android.util.Log.

android.util.Log debug method is totally ignored when the application compiled as release therefore will cause problem for libraries need to send a debug log

This library focuses on the log handler creation and well as configuring the output format.

## Install

[ ![Download](https://api.bintray.com/packages/yunarta-kartawahyudi/maven/com.mobilesolutionworks%3Aworks-logging/images/download.svg) ](https://bintray.com/yunarta-kartawahyudi/maven/com.mobilesolutionworks%3Aworks-logging/_latestVersion)

**build.gradle**

```
compile 'com.mobilesolutionworks:works-logging:1.0.0' // replace the number with latest version above
```

## Usage

**LogUtil bootstrap**

```java
Context context;

...

// default need to be provided before configuration
Class<? extends LogHandler> handler = LogCatHandler.class;
LogUtil.setDefaultLogger(handler);

// this will try to look for logutil.xml inside /res/xml
LogUtil.configure(context);
 
// while this provide custom_log.xml to be used for configuration
LogUtil.configure(context, R.xml.custom_log);
```

**logutil.xml**

```xml
<loggable>
    <package
        name="com.mobilesolutionworks"
        level="FINE"
        prefix="/!"
        shortName="true" />
        
     <package
            name="com.mobilesolutionworks"
            level="FINE"
            prefix="/!"
            shortName="true">
            <fileLogger
                count="2"
                file="file-%g.log"
                limit="1000000"/>
        </package>
</loggable>
```

**XML attribute**

package

- *name* = prefix name filter for logging
- *level* = log level according to java.util.logging.Level
- *prefix* = prefix to be added in front of the log, useful for logcat ignored by file logger
- *shortName* = print short name instead of full class name

fileLogger

- *count* = maximum file count to keep
- *file* = file name with %g as the log number
- *limit* = maximum file size

The log file produced by file logger will be store in your application private directory /files/log
 