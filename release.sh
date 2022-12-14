#!/bin/bash

set -euo pipefail

PROJECT="VisionMate Client"
MAIN_BRANCH="master"

RELEASE_TYPE=""
RELEASE_VERSION=""

# validate arguments
usage_error() {
  echo "Error: bad arguments" >&2
  echo "Usage: $0 {major|minor|patch}" >&2
  exit 1
}

if [ "$#" -ne 1 ]; then
  usage_error
fi
if [[ "$1" = "major" ]] || [[ "$1" = "minor" ]] || [[ "$1" = "patch" ]]; then
  RELEASE_TYPE="$1"
else
  usage_error
fi
exit 0
# validate prerequisites
if [[ ! $(command -v xmlstarlet) ]]; then
  echo "Error: xmlstarlet not found"
  exit 2
fi

# validate git state
if [[ ! $(git branch | grep \* | cut -d ' ' -f2) = "${MAIN_BRANCH}" ]]; then
  echo "Error: Not on ${MAIN_BRANCH} branch" >&2
  exit 3
fi
git fetch
if (( $(git log HEAD..origin/${MAIN_BRANCH} --oneline | wc -l) > 0 )); then
  echo "Error: Branch is not up-to-date with remote origin" >&2
  exit 4
fi

determine_version() {
  CURRENT_VERSION=$(xmlstarlet sel -t -v /_:project/_:version pom.xml | sed -e s/-SNAPSHOT//g)

  MAJOR=$(echo $CURRENT_VERSION | cut -d . -f 1 -)
  MINOR=$(echo $CURRENT_VERSION | cut -d . -f 2 -)
  PATCH=$(echo $CURRENT_VERSION | cut -d . -f 3 -)

  case "${RELEASE_TYPE}" in
    "major")
      MAJOR=$((MAJOR+1))
      MINOR=0
      PATCH=0
      ;;
    "minor")
      MINOR=$((MINOR+1))
      PATCH=0
      ;;
    "patch")
      # use patch version from snapshot version
      ;;
    *)
      echo "Error determining release type"
      exit 5
      ;;
  esac

  RELEASE_VERSION="${MAJOR}.${MINOR}.${PATCH}"
}

prepare() {
  echo "Preparing ${RELEASE_TYPE} release ${RELEASE_VERSION}..."
  mvn versions:set -DnewVersion=${RELEASE_VERSION} -DgenerateBackupPoms=false && \
  git commit -a -m "${PROJECT} v${RELEASE_VERSION} release" && \
  git tag -a v${RELEASE_VERSION} -m "${PROJECT} v${RELEASE_VERSION} release" && \
  TAGGED=true && \
  mvn clean install && \
  mvn versions:set -DnextSnapshot=true -DgenerateBackupPoms=false && \
  git commit -a -m "prepared for next development iteration"
}

rollback_local() {
  # undoes all changes from prepare function
  git reset --hard origin/${MAIN_BRANCH}
  if [[ ${TAGGED} = true ]]; then
    git tag -d v${RELEASE_VERSION}
  fi
  echo "Release failed. Changes reset." >&2
  exit 6
}

push() {
  echo "Pushing release..."
  # print these commands as they are not automatically rolled back
  set -x
  git push origin ${MAIN_BRANCH} && \
  git push origin v${RELEASE_VERSION} && \
  git checkout tags/v${RELEASE_VERSION} && \
  mvn deploy && \
  git checkout ${MAIN_BRANCH} && \
  set +x
}

push_error() {
  set +x
  echo "An error occurred while pushing the release. The process should probably be completed manually."
  exit 7
}

determine_version
prepare || rollback_local
push || push_error
echo "Release completed: ${PROJECT} v${RELEASE_VERSION}"
