#!/usr/bin/env bash

# command to execute from Jenkins as a post step:
# ./deploy.sh relay{dev}etl01.tmsgf.trb $POM_VERSION $WORKSPACE

target_host=$1
version=$2
workspace=$3
env=$4
pk=$5


function assertExpectedInput {
    if [ "${target_host}" = '' ]
    then
        echo "please specify target host"
        exit 1
    fi
    if [ "${version}" = '' ]
    then
        echo "please specify version"
        exit 1
    fi
    if [ "${workspace}" = '' ]
    then
        echo "please specify workspace"
        exit 1
    fi
    if [ "${env}" = '' ]
    then
        echo "please specify environment (one of: dev, live or qa) "
        exit 1
    fi
}

assertExpectedInput

relay_blob_conusumer_jar="${workspace}/target/RelayBlobConsumer-${version}.jar"

relay_blob_conusumer_root="/relay/udisConsumer"
remote_builds_home="${relay_blob_conusumer_root}/builds"
latest_version_dir="${remote_builds_home}/latest"
current_version_dir="${remote_builds_home}/${version}_$(date "+%Y-%m-%d_%H-%M-%S")"

echo "Deploying artifacts to ${target_host}:"

echo " - Creating directory ${current_version_dir}"
ssh -i ${pk} -o StrictHostKeyChecking=no ${target_host} mkdir -p ${current_version_dir}

echo " - Copying RelayBlobConsumer.jar to ${current_version_dir}"
scp -i ${pk} -o StrictHostKeyChecking=no ${relay_blob_conusumer_jar} ${target_host}:${current_version_dir}/RelayBlobConsumer.jar

echo " - Copying RelayBlobConsumer Service Script to /etc/init.d/"
scp -i ${pk} ${workspace}/udisConsumer.sh ${target_host}:/etc/init.d/udisConsumer

echo " - Copying RelayBlobConsumer Service Function Script to /home/conf/cron/shared/"
scp -i ${pk} ${workspace}/udisConsumer_functions.sh ${target_host}:/home/conf/cron/shared/udisConsumer_functions.sh

echo " - Setting permissions to RelayBlobConsumer Start Script "
ssh  -i ${pk} -o StrictHostKeyChecking=no ${target_host} chmod 777 /etc/init.d/udisConsumer

echo " - Creating PID File on host"
ssh  -i ${pk} -o StrictHostKeyChecking=no ${target_host} touch /var/run/udisConsumer.pid


echo " - Setting permissions to PID file "
ssh  -i ${pk} -o StrictHostKeyChecking=no ${target_host} chmod 777 /var/run/udisConsumer.pid

echo " - Linking ${current_version_dir} as ${latest_version_dir}"
ssh -i ${pk} -o StrictHostKeyChecking=no ${target_host} ln -fns ${current_version_dir} ${latest_version_dir}

echo " - Restarting RelayBlobConsumer by executing 'service udisConsumer restart'"
ssh -i ${pk} -o StrictHostKeyChecking=no ${target_host} service udisConsumer restart

echo "Done, logging to /relay/logs/RelayBlobConsumer.log"
