if [ $TRAVIS_OS_NAME = 'osx' ]; then
  mvn clean package -DskipTests -P mac -B
else
  mvn clean package -DskipTests -P win -B
fi

