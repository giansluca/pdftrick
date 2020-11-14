if [ $TRAVIS_OS_NAME = 'osx' ]; then
  mvn clean package -DskipTests -P win
else
  mvn clean package -DskipTests -P mac -B -V
fi

echo "DONE"