#!/bin/bash
#
# KoKu release management script.
#
# Ixonos / aspluma
#

modules='processes services ui'
svn_repo_base=https://svn.mermit.fi

function usage() {
  echo "usage: koku-build.sh -r release_version -c {tag_release | build_packages}"
  exit 1
}

function fail_if_tag_exists() {
  local mods="$1"
  for m in $mods; do
    mod_tags=$(svn ls $svn_repo_base/kohtikumppanuutta/$m/tags)
    rv=$?
    [ $rv -ne 0 ] && echo "failed to get tags for $m, exiting" && exit 1
    echo "$mod_tags" | grep -q "^$koku_rel_v/$"
    rv=$?
    [ $rv -eq 0 ] && echo "tag $koku_rel_v already exists for module '$m', exiting" && exit 1
  done
}

function tag_release() {
  local mods="$1"
  for m in $mods; do
    svn copy $svn_repo_base/kohtikumppanuutta/$m/trunk \
           $svn_repo_base/kohtikumppanuutta/$m/tags/$koku_rel_v \
        -m "KoKu / $m: $koku_rel_v tag."
  done
}

function build_packages() {
  # create release dirs
  mkdir release-$koku_rel_v
  cd "release-$koku_rel_v"
  mkdir $modules
  mkdir kunpo loora eap

  # do a full, fresh checkout
  for m in $modules; do
    cd $m
    svn co $svn_repo_base/kohtikumppanuutta/$m/tags/$koku_rel_v
    cd ..
  done

  # build
  # build: services
  pushd services/$koku_rel_v
  mvn -Dkoku.build.version=$koku_rel_v clean install
  cp */target/*.ear intalio/target/palvelut-web-service-*.jar ../../eap
  popd

  # build: ui
  # build/ui: kunpo packages
  pushd ui/$koku_rel_v
  mvn -Dkoku.build.version=$koku_rel_v clean install
  cp kks/target/kks-portlet-*.war pyh/target/pyh-portlet-*.war ../../kunpo
  cp arcusys-portlet/koku-palvelut-portlet/target/palvelut-portlet.war arcusys-portlet/koku-message-portlet/target/koku-message-portlet.war \
    arcusys-portlet/koku-taskmanager-portlet/target/koku-taskmanager-portlet.war \
    arcusys-portlet/koku-navi-portlet/target/koku-navi-portlet.war ../../kunpo

  # build/ui: loora packages
  sed -i'' -e 's/\/portlet" prefix=/\/portlet_2_0" prefix=/' {kks,lok}/src/main/webapp/WEB-INF/jsp/*/imports.jsp
  sed -i'' '/EPP only: start/,/EPP only: end/d' */src/main/webapp/WEB-INF/web.xml
  sed -i'' '/EPP only: start/,/EPP only: end/d' arcusys-portlet/*/src/main/webapp/WEB-INF/web.xml
  mvn -Dkoku.build.version=$koku_rel_v clean install
  cp kks/target/kks-portlet-*.war lok/target/lok-portlet-*.war ../../loora
  cp arcusys-portlet/koku-palvelut-portlet/target/palvelut-portlet.war arcusys-portlet/koku-message-portlet/target/koku-message-portlet.war \
    arcusys-portlet/koku-taskmanager-portlet/target/koku-taskmanager-portlet.war \
    arcusys-portlet/koku-navi-portlet/target/koku-navi-portlet.war ../../loora
  popd
}

while getopts "r:c:e" o; do
  case $o in
    r) koku_rel_v=$OPTARG
	  ;;
    c) build_command=$OPTARG
	  ;;
    e) is_ext_user=1
	  ;;
    *) echo "?"
	  exit 1
	  ;;
  esac
done

if [ "x" = "x$koku_rel_v" -o "x" = "x$build_command" ]; then
  usage
fi

if [ "x" = "x$is_ext_user" ]; then
  svn_repo_base=$svn_repo_base/projects
else
  svn_repo_base=$svn_repo_base/ext
fi

case $build_command in
  tag_release)
	fail_if_tag_exists "$modules"
	echo "tag $koku_rel_v doesn't exist. tagging"
	tag_release "$modules"
	;;
  build_packages)
	build_packages
	;;
  *) echo "unknown build command"
	exit 1
	;;
esac


exit 0

