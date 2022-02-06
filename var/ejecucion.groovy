def call(){
pipeline {
    agent any
    environment {
        NEXUS_USER         = credentials('NEXUS-USER')
        NEXUS_PASSWORD     = credentials('NEXUS-PASS')
    }
    parameters {
        choice(
            name:'compileTool',
            choices: ['Maven', 'Gradle'],
            description: 'Seleccione herramienta de compilacion'
        )
        text description: 'Enviar los stages separados por ";" ... (Ejemplo: build;test;sonar;nexus) Vacio si necesita todos los stages', name: 'stages'
    }
    stages {
        stage("Pipeline"){
            steps {
                script{
                    sh "env"
                    sh "env.GIT_BRANCH"
                    env.DESCRTIPTION_STAGE = ""
                  switch(params.compileTool)
                    {
                        case 'Maven':
                            gradle.call(params.stages);
                        break;
                        case 'Gradle':
                            gradle.call(params.stages);
                        break;
                    }
                }
            }
            post{
                success{
                    slackSend color: 'good', message: "[Hernan Moreno] [${JOB_NAME}] [${BUILD_TAG}] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token-slack-jenkins'
                }
                failure{
                    slackSend color: 'danger', message: "[Hernan Moreno] [${env.JOB_NAME}] [${BUILD_TAG}] Ejecucion fallida en stage [${env.DESCRTIPTION_STAGE}]", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token-slack-jenkins'
                }
            }
        }
    }
}
}
return this;