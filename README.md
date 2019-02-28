# maven使用

## 常用命令
> 参考内容
* [maven常用命令](https://www.cnblogs.com/wkrbky/p/6352188.html)
* [maven教程](https://www.yiibai.com/maven)

## 创建工程

## 打包时跳过测试
```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <configuration>
                    <skip>true</skip>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

## 指定编译版本
```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
```
## 编译
> mvn compile

## 打包
> mvn package

### 指定打包文件名以及输出路径
> 打包文件名可以自定义，${project.artifactId}-${project.version}是默认生成的文件名，不指定就是这种格式，也可以修改为其他名字。${project.build.directory}是默认输出路径，也可以指定其他路径。
```xml
<build>
    <finalName>${project.artifactId}-${project.version}</finalName>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <version>3.1.1</version>
            <configuration>
                <!-- 指定打包的jar包输出路径-->
                <outputDirectory>${project.build.directory}</outputDirectory>
            </configuration>
        </plugin>
    </plugins>
</build>
```
### 拷贝jar包依赖项
> 使用插件maven-dependency-plugin，默认路径是${project.build.directory}，也可以设置其他路径，
如果不是默认路径，必须设置jar包查找路径，需要配置classpathPrefix，两者必须要一致，否则jar包找不到依赖的jar包。
比如下面设置的outputDirectory=${project.build.directory}/lib要与classpathPrefix=lib一致

```xml
<!-- 拷贝依赖的jar包到指定目录目录 -->
<plubins>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-dependency-plugin</artifactId>
        <executions>
            <execution>
                <configuration>
                    <outputDirectory>${project.build.directory}/lib</outputDirectory>
                </configuration>
            </execution>
        </executions>
    </plugin>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <configuration>
            <archive>
                <manifest>
                    <classpathPrefix>lib/</classpathPrefix>
                </manifest>
            </archive>
        </configuration>  
    </plugin> 
</plubins>
```

### 把依赖的jar文件打包成一个文件
> 使用插件maven-assembly-plugin，指定输出路径，配置manifest，把依赖的jar包里的内容打包进一个jar包
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-assembly-plugin</artifactId>
    <version>3.1.0</version>
    <configuration>
        <outputDirectory>${project.build.directory}</outputDirectory>
        <archive>
            <manifest>
                <mainClass>cn.xx.Aes.xxEncode</mainClass>
            </manifest>
            <manifestEntries>
                <Class-Path>.</Class-Path>
            </manifestEntries>
        </archive>
        <descriptorRefs>
            <descriptorRef>jar-with-dependencies</descriptorRef>
        </descriptorRefs>
    </configuration>
    <executions>
        <execution>
            <id>make-assembly</id>
            <phase>package</phase>
            <goals>
                <goal>single</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### 打包加时戳
> 参考内容
* [maven打包加时间戳](https://www.cnblogs.com/winner-0715/p/8398422.html)
> 时戳的几种加法
* 使用maven自带的属性
```text
设置时间戳格式：在pom.xml文件中加入以下配置

<properties>
<maven.build.timestamp.format>yyyyMMddHHmmss</maven.build.timestamp.format>
</properties>
在打包plugin中引用该属性

<finalName>
  ${project.artifactId}-${project.version}_${maven.build.timestamp}
</finalName>
Maven自带时间戳使用${maven.build.timestamp}，但是时区是UTC。 
如果要使用GMT+8，就需要插件提供支持。
```
* 使用buildnubmer-maven-plugin
> 使用${timestamp}得到的是当前区(东八区)的时间，可以使用这个配置
```xml
<build>
    <finalName>${project.artifactId}-${project.version}_${timestamp}</finalName>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>buildnumber-maven-plugin</artifactId>
            <version>1.4</version>
            <configuration>
                <timestampFormat>yyyyMMdd-HHmmss</timestampFormat>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>create-timestamp</goal>
                    </goals>
                </execution>
            </executions>
            <inherited>false</inherited>
        </plugin>    
    </plugins>
</build>

```

* 使用build-helper-maven-plugin
> 实际使用有问题，configuration配置不正确，未找到原因
```xml
<build>
    <finalName>ProjectName-${current.time}</finalName>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>build-helper-maven-plugin</artifactId>
            <version>1.9.1</version>
            <executions>
                <execution>
                    <id>timestamp-property</id>
                    <goals>
                        <goal>timestamp-property</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <name>current.time</name>
                <pattern>yyyyMMdd-HHmmss</pattern>
                <timeZone>GMT+8</timeZone>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### 添加manifest文件
> 参考内容
* [maven中自定义可执行jar包的MANIFEST](https://blog.csdn.net/apexlj/article/details/47019173)
* [用maven在MANIFEST.MF文件中的Class-Path中增加当前目录(.)](https://www.cnblogs.com/byron0918/p/4825343.html)
* [How to set manifest class-path in maven shade plugin?](https://stackoverflow.com/questions/17242945/how-to-set-manifest-class-path-in-maven-shade-plugin)

> maven-jar-plugin插件默认生成的MAINIFEST.MF文件包含以下几项：
```text
  Manifest-Version: 1.0
  Archiver-Version: Plexus Archiver
  Created-By: Apache Maven
  Built-By: ${user.name}
  Build-Jdk: ${java.version}
```  
> 配置manifest项
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <version>3.1.1</version>
    <configuration>
        <archive>
            <manifest>
                <!-- 在jar包的MF文件中生成Class-Path属性 -->
                <addClasspath>true</addClasspath>
                <!-- Class-Path 前缀 -->
                <classpathPrefix>lib/</classpathPrefix>
                <!-- main全限定包名 -->
                <mainClass>com.xxx.MainTask</mainClass>
            </manifest>
            <!-- 添加classpath缺少的内容-->
            <manifestEntries>
                <Main-Version>${current.time}</Main-Version>
                <Class-Path>. lib/yyy.jar lib/xxx.jar</Class-Path>
            </manifestEntries>
        </archive>
    </configuration>
</plugin>
```

### 使用已有的manifest文件
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <version>3.1.1</version>
    <configuration>
        <archive>
            <manifestFile>${project.build.outputDirectory}/META-INF/MANIFEST.MF</manifestFile>
        </archive>
    </configuration>
</plugin>
```

### 打包配置文件
> 参考内容
* [利用MAVEN打包时，如何包含更多的资源文件](https://bglmmz.iteye.com/blog/2063856)
* [maven打包插件](https://www.cnblogs.com/selier/p/9510326.html)

> 将指定目录下的文件打包到jar包中，**可以递归指定目录下文件夹，可以配置targetPath不为${project.build.directory}，就不会打包到jar包中，${project.build.directory}默认是target\classes，若不设置，则会拷贝到该目录
```xml
<build>
    <!-- resources设置的路径下的文件会被打包到jar包 -->
    <resources>
        <!-- 若没有设置resource.directory则打包classpath(resources)下的文件，若设置了directory则把所有权给你了，需指定所有要打包的文件，否则不会默认打包classpath下的文件 -->
        <resource>
            <!-- 当前路径是相对于classpath位置，不设置include则默认递归打包该目录下所有文件 -->
            <directory>./</directory>
            <includes>
                <include>*.xml</include>
                <include>root_resource/*.xml</include>
            </includes>
            <!-- 将文件编译时拷贝到指定的目录 -->
            <!-- 可以不拷贝到class路径，那样就不会打包到jar包中了 -->
            <targetPath>${project.build.directory}/lib</targetPath>
        </resource>
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <!-- 递归当前指定目录下所有文件 -->
                <include>**/*</include>
            </includes>
            <excludes>
                <!-- 排除当前任何目录下的xml文件 -->
                <exclude>*/*.xml</exclude>
            </excludes>
        </resource>
    </resources>
</build>
```
