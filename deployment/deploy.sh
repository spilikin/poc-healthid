#!/bin/bash
HOST=healthid
rm -rf ./dist/
mkdir ./dist/
cd ../healthid-keycloak/healthid-authenticator/
gradle clean
gradle build
cd -
cp ../healthid-keycloak/healthid-authenticator/build/libs/healthid-authenticator.jar ./dist/
cp ../healthid-keycloak/healthid-authenticator/healthid-challenge.ftl ./dist/
cp -r ./static-web dist/
cp ./docker-compose.yaml dist/

cd ../aua/aua-vue/
yarn build
cd -
mkdir -p ./dist/aua/aua-vue
cp -r ../aua/aua-vue/dist ../aua/aua-vue/Dockerfile ../aua/aua-vue/nginx.conf ./dist/aua/aua-vue/


ssh $HOST rm -rf healthid/*
ssh $HOST mkdir -p healthid
ssh $HOST cp -f .env healthid/
ssh $HOST mkdir -p healthid_volumes
scp -r dist/* $HOST:healthid/