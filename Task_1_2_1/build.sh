#!/bin/bash
# Скрипт сборки для Task_1_1_1 (JUnit 5)

SRC_DIR="src/main/java"
TEST_DIR="src/test/java"
BUILD_DIR="build"
CLASSES_DIR="$BUILD_DIR/classes"
TEST_CLASSES_DIR="$BUILD_DIR/test-classes"
DOCS_DIR="docs"
LIBS_DIR="libs"   # папка с JUnit 5 JAR’ами

# Создаём папки для сборки
mkdir -p $CLASSES_DIR
mkdir -p $TEST_CLASSES_DIR
mkdir -p $DOCS_DIR

# Компиляция исходников
echo "Компиляция исходников"
find $SRC_DIR -name "*.java" > sources.txt
javac -d $CLASSES_DIR @sources.txt

# Генерация документации
echo "Генерация документации"
javadoc -d $DOCS_DIR -sourcepath $SRC_DIR ru.nsu.gaev

# Компиляция тестов
javac -cp "build/classes:libs/junit-platform-console-standalone-1.10.0.jar" -d build/test-classes $(find src/test/java -name "*.java")

# Запуск тестов
java -jar libs/junit-platform-console-standalone-1.10.0.jar --class-path build/classes:build/test-classes --scan-class-path

# Проверка наличия main метода
echo "Сборка jar"
jar cfe $BUILD_DIR/SampleApp.jar ru.nsu.gaev.Sample -C $CLASSES_DIR .

# Запуск приложения
echo "Запуск приложения"
java -jar $BUILD_DIR/SampleApp.jar
