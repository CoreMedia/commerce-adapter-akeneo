#!/usr/bin/env groovy

import com.coremedia.cm.DockerAgent
import com.coremedia.cm.Jenkins

@org.jenkinsci.plugins.workflow.libs.Library(['coremedia-internal', 'coremedia-aws@shared-library-aws-v2']) _

String AWS_REGISTRY_ID = '601133629472'
String AWS_REGION = 'eu-central-1'
String AWS_ECR_HOST = "${AWS_REGISTRY_ID}.dkr.ecr.eu-central-1.amazonaws.com"
String DOCKER_REGISTRY = "coremedia-cs"
String ECR_REGISTRY_PATH = "${AWS_ECR_HOST}/${DOCKER_REGISTRY}"
String JENKINS_CREDENTIALS_ID = 'presales-aws-credentials-eu-central-1'

String ecrTag;

pipeline {

  agent {
    label Jenkins.defaultNodeLabel
  }

  options {
    timestamps()
    disableResume()
    durabilityHint('PERFORMANCE_OPTIMIZED')
    timeout(time: 1, unit: 'HOURS')
    disableConcurrentBuilds()
    buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '30'))
  }

  stages {

    stage('Prepare') {
      steps {
        script {
          cmRequireParameters('ECR_TAG')
          ecrTag = params.ECR_TAG
        }
      }
    }

    stage('AWS Login') {
      steps {
        awsContext(jenkinsCredentialsId: JENKINS_CREDENTIALS_ID, region: AWS_REGION, profileName: 'default') {
          awsEcrLogin(registryId: AWS_REGISTRY_ID, region: AWS_REGION)
        }
      }
    }

    stage('Build and Push Docker Image') {
      agent {
        docker {
          image "${Jenkins.ecrPullThroughProxyRegistry}/cm-tools/maven:3.8.6-11.0.17.8-1-cm-1.1.1"
          args "-e DOCKER_CONFIG=/.docker ${DockerAgent.defaultMavenArgs} ${DockerAgent.defaultDockerArgs} -v /home/coremedia-ci/.docker/config.json:/.docker/config.json"
          reuseNode true
        }
      }
      steps {
        script {
          String dockerOptions = "-Pdefault-image -Djib.useOnlyProjectCache=true -Djib.goal=build -Djib.allowInsecureRegistries=true -Dapplication.image-prefix=${ECR_REGISTRY_PATH} -Dapplication.image-tag=${ecrTag} "
          cmMaven(cmd: "clean install -Pdefault-image " + dockerOptions, scanMvnLog: true)
        }
      }
    }
  }

  post {
    always {
      script {
        awsEcrLogout(server: AWS_ECR_HOST)
        cmCleanup()
      }

    }
  }

}
