if [ $TRAVIS_OS_NAME = 'osx' ]; then
  export SSHPASS=$DEPLOY_PASS
  sshpass -e scp \
  -o stricthostkeychecking=no \
  $MAC_FILE_PATH \
  $DEPLOY_USER@$DEPLOY_HOST:/home/gians/test-deploy
else
  pscp \
  -pw $DEPLOY_PASS \
  -hostkey $SERVER_FINGERPRINT \
  -P 22 \
  $WIN_FILE_PATH \
  $DEPLOY_USER@$DEPLOY_HOST:/home/gians/test-deploy
fi


