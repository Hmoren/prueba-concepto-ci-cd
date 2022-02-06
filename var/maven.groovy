/*
    forma de invocación de método call:
    def ejecucion = load 'script.groovy'
    ejecucion.call()
*/
import utilities.*
def call(stages) {
    sh "echo 'ESTOY EN MAVEN'"

    def listStages = stages.split(';')
    listStages.each {
        println('este es uno de los stages ===> {it}')
    }

    def listStagesOrder = [
        'compile': 'stageCleanCompile',
        'test': 'stageCleanTest',
        'build': 'stageCleanBuild',
        'sonar': 'stageSonar',
        'run_spring_curl': 'stageRunSpringCurl',
        'upload_nexus': 'stageUploadNexus',
        'download_nexus': 'stageDownloadNexus',
        'run_jar': 'stageRunJar',
        'curl_jar': 'stageCurlJar'
    ]

    def arrayUtils = new array.arrayExtentions()
    def stagesArray = []
    stagesArray = arrayUtils.searchKeyInArray(stages, ';', listStagesOrder)

    if (stagesArray.isEmpty()) {
        echo 'El pipeline se ejecutará completo'
        allStages()
    } else {
        echo 'Stages a ejecutar :' + stages
        stagesArray.each { stageFunction ->//variable as param
            echo 'Ejecutando ' + stageFunction
            "${stageFunction}"()
        }
    }
// env.TAREA = 'Paso 1: Compliar'
// stage("${env.TAREA}") {
//     sh 'mvn clean compile -e'
// }
// env.TAREA = 'Paso 2: Testear'
// stage("${env.TAREA}") {
//     sh 'mvn clean test -e'
// }
// env.TAREA = 'Paso 3: Build .Jar'
// stage("${env.TAREA}") {
//     sh 'mvn clean package -e'
// }
// env.TAREA = 'Paso 4: Sonar - Análisis Estático'
// stage("${env.TAREA}") {
//     sh "echo 'Análisis Estático!'"
//     withSonarQubeEnv('sonarqube') {
//         sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build'
//     }
// }
// env.TAREA = 'Paso 5: Curl Springboot Maven sleep 50'
// stage("${env.TAREA}") {
//     sh 'mvn spring-boot:run &'
//     sh "sleep 50 && curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
// }
// env.TAREA = 'Paso 6: Subir Nexus'
// stage("${env.TAREA}") {
//     nexusPublisher nexusInstanceId: 'nexus',
//   nexusRepositoryId: 'devops-usach-nexus',
//   packages: [
//       [$class: 'MavenPackage',
//           mavenAssetList: [
//               [classifier: '',
//               extension: 'jar',
//               filePath: 'build/DevOpsUsach2020-0.0.1.jar'
//           ]
//       ],
//           mavenCoordinate: [
//               artifactId: 'DevOpsUsach2020',
//               groupId: 'com.devopsusach2020',
//               packaging: 'jar',
//               version: '0.0.1'
//           ]
//       ]
//   ]
// }
// env.TAREA = 'Paso 7: Descargar Nexus'
// stage("${env.TAREA}") {
//     sh ' curl -X GET -u $NEXUS_USER:$NEXUS_PASSWORD "http://nexus:8081/repository/devops-usach-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar" -O'
// }
// env.TAREA = 'Paso 8: Levantar Artefacto Jar'
// stage("${env.TAREA}") {
//     sh 'java -jar DevOpsUsach2020-0.0.1.jar & >/dev/null'
// }
// env.TAREA = 'Paso 9: Testear Artefacto - Dormir(Esperar 60sg) '
// stage("${env.TAREA}") {
//     sh "sleep 60 && curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
// }
}

def allStages() {
    stageCleanCompile()
    stageCleanTest()
    stageCleanBuild()
    stageSonar()
    stageRunSpringCurl()
    stageUploadNexus()
    stageDownloadNexus()
    stageRunJar()
    stageCurlJar()
}

def stageCleanCompile() {
    env.DESCRTIPTION_STAGE = 'Paso 1: Compliar'
    stage("${env.DESCRTIPTION_STAGE}") {
        env.STAGE = "Compliar - ${env.DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh 'mvn clean compile -e'
    }
}

def stageCleanTest() {
    env.DESCRTIPTION_STAGE = 'Paso 2: Testear'
    stage("${env.DESCRTIPTION_STAGE}") {
        env.STAGE = "Test - ${env.DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh 'mvn clean test -e'
    }
}

def stageCleanBuild() {
    env.DESCRTIPTION_STAGE = 'Paso 3: Build .Jar'
    stage("${env.DESCRTIPTION_STAGE}") {
        env.STAGE = "Build - ${env.DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh 'mvn clean package -e'
    }
}

def stageSonar() {
    env.DESCRTIPTION_STAGE = 'Paso 4: Sonar - Análisis Estático'
    stage("${env.DESCRTIPTION_STAGE}") {
        env.STAGE = "Sonar - ${DESCRTIPTION_STAGE}"
        withSonarQubeEnv('sonarqube') {
            sh "echo  ${env.STAGE}"
            sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build'
        }
    }
}

def stageRunSpringCurl() {
    env.DESCRTIPTION_STAGE = 'Paso 5: Curl Springboot Maven sleep 50'
    stage("${env.DESCRTIPTION_STAGE}") {
        env.STAGE = "run_spring_curl - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh 'mvn spring-boot:run &'
        sh "sleep 50 && curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
    }
}

def stageUploadNexus() {
    env.DESCRTIPTION_STAGE = 'Paso 6: Subir Nexus'
    stage("${env.DESCRTIPTION_STAGE}") {
        nexusPublisher nexusInstanceId: 'nexus',
        nexusRepositoryId: 'devops-usach-nexus',
        packages: [
            [$class: 'MavenPackage',
                mavenAssetList: [
                    [classifier: '',
                    extension: 'jar',
                    filePath: 'build/DevOpsUsach2020-0.0.1.jar'
                ]
            ],
                mavenCoordinate: [
                    artifactId: 'DevOpsUsach2020',
                    groupId: 'com.devopsusach2020',
                    packaging: 'jar',
                    version: '0.0.1'
                ]
            ]
        ]
        env.STAGE = "upload_nexus - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
    }
}

def stageDownloadNexus() {
    env.DESCRTIPTION_STAGE = 'Paso 7: Descargar Nexus'
    stage("${env.DESCRTIPTION_STAGE}") {
        env.STAGE = "download_nexus - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh ' curl -X GET -u $NEXUS_USER:$NEXUS_PASSWORD "http://nexus:8081/repository/devops-usach-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar" -O'
    }
}

def stageRunJar() {
    env.DESCRTIPTION_STAGE = 'Paso 8: Levantar Artefacto Jar'
    stage("${env.DESCRTIPTION_STAGE}") {
        env.STAGE = "run_jar - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh 'java -jar DevOpsUsach2020-0.0.1.jar & >/dev/null'
    }
}

def stageCurlJar() {
    env.DESCRTIPTION_STAGE = 'Paso 9: Testear Artefacto - Dormir(Esperar 60sg)'
    stage("${env.DESCRTIPTION_STAGE}") {
        env.STAGE = "curl_jar - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh "sleep 60 && curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
    }
}

return this
