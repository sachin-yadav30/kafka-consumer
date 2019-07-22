
#!/bin/bash
. /home/conf/cron/shared/udisConsumer_functions.sh

JAVA_HOME='/usr/java/jdk1.8.0_144'
#DISABLE_SPIN_LIBRARY=" -Dspin.off.starter=com.gracenote.fakespin.FakeSpinStarter -DfakespinDebug=true "
START_SCRIPT="nohup $JAVA_HOME/bin/java -jar /relay/udisConsumer/builds/latest/RelayBlobConsumer.jar "
PID_FILE='/var/run/udisConsumer.pid'

# ***********************************************
# ***********************************************

#ARGS="" # optional start script arguments
DAEMON=$START_SCRIPT

# colors
red='\e[0;31m'
green='\e[0;32m'
yellow='\e[0;33m'
reset='\e[0m'

echoRed() { echo -e "${red}$1${reset}"; }
echoGreen() { echo -e "${green}$1${reset}"; }
echoYellow() { echo -e "${yellow}$1${reset}"; }

start() {
    echo "$(date) $HOSTNAME $0 started" >> /logs/scripts/blobConsumer_start.log
    $DAEMON  > /dev/null 2>&1 &
}

log() {
    echo $@ >> /logs/scripts/blobConsumer_start.log
    echo $@
}

 FORCE=0
  if [[ "$2" = "-force" ]]; then
    echo "Ignoring -force, use -FORCE (all in caps) in order to force a 'stop'"
    exit 0
  fi

  if [[ "$2" = "-FORCE" ]]; then
    FORCE=1
  fi

case "$1" in
start)

    log "$(date) $HOSTNAME $0 starting with init script"
    if [[ `get_blobConsumer_count` -gt 1 ]] ; then
        log "$(date) $HOSTNAME $0: Found $(get_blobConsumer_count) udisConsumer processes. (NOT Good)"
        kill_all_blobConsumer
        start
    elif [[ `get_blobConsumer_count` -eq 0 ]] ; then
        log "$(date) $HOSTNAME $0: udisConsumer not running. Starting now."
        start
    else
        log "$(date) $HOSTNAME $0: udisConsumer already running at pid $(get_pid)."
    fi
;;

status)
    get_pid
;;

stop)

    log "$(date) $HOSTNAME $0 stopped with init script"
    kill_all_blobConsumer
    rm -f $PID_FILE
;;

restart)
    $0 stop
    $0 start
;;

*)
    echo "Usage: $0 {status|start|stop|restart}"
    exit 1
esac