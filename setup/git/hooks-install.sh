#!/bin/sh

#
# install KoKu custom Git client-side hooks for each repository
# 
# installation:
# cd $KOKU_REPO_ROOT
# sh koku-main/setup/git/hooks-install.sh
#

repos=`find . -name 'koku-*' -type d -maxdepth 1`
hooks="commit-msg msg-check.pl"

for i in $repos; do
  echo "installing hook to repository: $i"
  pushd $i/.git/hooks > /dev/null
  [ $? -ne 0 ] && echo "skipping\n" && continue

  # install hooks
  /bin/echo -n "- hook: "
  for h in $hooks; do
    /bin/echo -n "$h "
    fn="../../../koku-main/setup/git/$h"
    if [ ! -e  "$fn" ]; then
      echo "fatal: missing file: $fn"
      exit 1
    fi
    ln -s ../../../koku-main/setup/git/$h .
  done
  echo "\n"

  popd > /dev/null
done
