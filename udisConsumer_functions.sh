#!/bin/bash
##
##
## Created 2018-08-29; bshannon
##
## Common functions for wrangling the NDL process.
##

ERR=0
RUN=""

export PATTERN="[Rr]elay[Bb]lob[Cc]onsumer\.jar"

kill_all_blobConsumer() {
    ps aux -www | grep $PATTERN | awk '{print $2;}' | sort -n | while read pid; do
        echo "$(date): Killing udisConsumer with pid $pid"
        $RUN kill -9 $pid
    done

    PROCCOUNT=`ps aux -www | grep [Rr]elay[Bb]lob[Cc]onsumer\.jar | wc -l `
    echo "$(date): After killing all udisConsumer processes, there are $PROCCOUNT running."
}


get_pid() {
    ps aux -www | grep $PATTERN | awk '{print $2;}' | head -1
}


get_blobConsumer_count() {
    ps aux -www | grep $PATTERN | grep -v 'grep' | wc -l
}



start_blobConsumer() {

    START_CMD=$1

    if [ -z $START_CMD ] ; then
        echo "ERROR: start_blobConsumer() function wasn't given the parameter to start udisConsumer." 2>&1
        exit 1
    fi

    PROCCOUNT=`ps aux -www | grep $PATTERN | wc -l `
    echo "$(date): PROCCOUNT is $PROCCOUNT"

    if [ $PROCCOUNT -eq 0 ] ; then
        echo "$(date): udisConsumer is not running, starting."
        $RUN $START_CMD
    elif [ $PROCCOUNT -gt 1 ] ; then
        echo "$(date): Too many udisConsumer processes running; killing them before starting a single instance. ($PROCCOUNT running)"
        kill_all_ndl
    else
        echo "$(date): udisConsumer is already running. $PROCCOUNT processes running."
    fi

}


check_blobConsumer_instance_count() {
    PROCCOUNT=`ps aux -www | grep $PATTERN | wc -l `
    echo "$(date): check_ndl_instance_count(): there are $PROCCOUNT udisConsumer processes running on $(hostname)."
    if [ $PROCCOUNT -ne 1 ] ; then
        echo "$(date): Error starting udisConsumer, process count is $PROCCOUNT"
    fi

}